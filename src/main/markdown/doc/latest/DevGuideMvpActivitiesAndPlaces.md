MVP Activities and Places
===

GWT 2.1 introduced a built-in framework for browser history management.
The Activities and Places framework allows you to create bookmarkable
URLs within your application, thus allowing the browser's back button and
bookmarks to work as users expect.
It builds on GWT's [history mechanism](DevGuideCodingBasicsHistory.html)
and may be used in conjunction with MVP development, though not required.

Strictly speaking, MVP architecture
is not concerned with browser history management, but Activities and Places
may be used with MVP development as shown in this article. If you're not 
familiar with MVP, you may want to read these articles first:

*   [Large scale application development and MVP, Part I](../../articles/mvp-architecture.html)
*   [Large scale application development and MVP, Part II](../../articles/mvp-architecture-2.html)

**Definitions**

An _activity_ simply represents something the user is doing.
An Activity contains no Widgets or UI code. Activities typically 
restore state ("wake up"), perform initialization ("set up"), 
and load a corresponding UI ("show up"). Activities are started
and stopped by an ActivityManager associated with a container Widget. 
An Activity can automatically
display a warning confirmation when the Activity is about to be stopped
(such as when the user navigates to a new Place). In addition, the
ActivityManager warns the user before the window is about to be closed.

A _place_ is a Java object representing a particular
state of the UI. A Place can be converted to and from a URL history
token (see GWT's [History mechanism](DevGuideCodingBasicsHistory.html))
by defining a PlaceTokenizer for each
Place, and GWT's PlaceHistoryHandler automatically updates the browser URL
corresponding to each Place in your app.

You can download all of the code referenced here in [this sample app](https://storage.googleapis.com/google-code-archive-downloads/v2/code.google.com/google-web-toolkit/Tutorial-hellomvp-2.1.zip). The
sample app is a simple "Hello, World!" example demonstrating the use of 
Activities and Places with MVP.

Let's take a look at each of the moving parts in a GWT 2.1 app
using Places and Activities.

1.  [Views](#Views)
2.  [ClientFactory](#ClientFactory)
3.  [Activities](#Activities)
4.  [Places](#Places)
5.  [PlaceHistoryMapper](#PlaceHistoryMapper)
6.  [ActivityMapper](#ActivityMapper)

Then we'll take at how you wire it all together and how it works.

1.  [Putting it all together](#Putting_it_all_together)
2.  [How it all works](#How_it_all_works)
3.  [How to navigate](#How_to_navigate)
4.  [Related resources](#resources)

## Views<a id="Views"></a>

A view is simply the part of the UI
associated with an Activity. In MVP development, a view is defined by an
interface, which allows multiple view implementations based on client
characteristics (such as mobile vs. desktop) and also facilitates
lightweight unit testing by avoiding the time-consuming GWTTestCase.
There is no View interface or class in GWT which views must implement or
extend; however, GWT 2.1 introduces an IsWidget interface that is
implemented by most Widgets as well as Composite. It is useful for views
to extend IsWidget if they do in fact provide a Widget. Here is a simple
view from our sample app.

```java
public interface GoodbyeView extends IsWidget {
    void setName(String goodbyeName);
}
```

The corresponding view implementation extends Composite, which keeps
dependencies on a particular Widget from leaking out.

```java
public class GoodbyeViewImpl extends Composite implements GoodbyeView {
    SimplePanel viewPanel = new SimplePanel();
    Element nameSpan = DOM.createSpan();

    public GoodbyeViewImpl() {
        viewPanel.getElement().appendChild(nameSpan);
        initWidget(viewPanel);
    }

    @Override
    public void setName(String name) {
        nameSpan.setInnerText("Good-bye, " + name);
    }
}
```

Here is a slightly more complicated view that additionally
defines an interface for its corresponding presenter (activity).

```java
public interface HelloView extends IsWidget {
    void setName(String helloName);
    void setPresenter(Presenter presenter);

    public interface Presenter {
        void goTo(Place place);
    }
}
```

The Presenter interface and setPresenter method allow for
bi-directional communication between view and presenter, which
simplifies interactions involving repeating Widgets and also allows view
implementations to use UiBinder with @UiHandler methods that delegate to
the presenter interface.

The HelloView implementation uses
[UiBinder](DevGuideUiBinder.html)
and a template.

```java
public class HelloViewImpl extends Composite implements HelloView {
    private static HelloViewImplUiBinder uiBinder = GWT
            .create(HelloViewImplUiBinder.class);

    interface HelloViewImplUiBinder extends UiBinder<Widget, HelloViewImpl> {
    }

    @UiField
    SpanElement nameSpan;
    @UiField
    Anchor goodbyeLink;
    private Presenter presenter;
    private String name;

    public HelloViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void setName(String name) {
        this.name = name;
        nameSpan.setInnerText(name);
    }

    @UiHandler("goodbyeLink")
    void onClickGoodbye(ClickEvent e) {
        presenter.goTo(new GoodbyePlace(name));
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }
}
```

Note the use of @UiHandler that delegates to the presenter.
Here is the corresponding template:

```html
<!DOCTYPE ui:UiBinder SYSTEM "http://dl.google.com/gwt/DTD/xhtml.ent">
<ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
             xmlns:g="urn:import:com.google.gwt.user.client.ui">
    <ui:style>
        .important {
            font-weight: bold;
        }
    </ui:style>
    <g:HTMLPanel>
        Hello,
        <span class="{style.important}" ui:field="nameSpan" />
        <g:Anchor ui:field="goodbyeLink" text="Say good-bye"></g:Anchor>
    </g:HTMLPanel>
</ui:UiBinder>
```

Because Widget creation involves DOM operations, views are
relatively expensive to create. It is therefore good practice to make
them reusable, and a relatively easy way to do this is via a view
factory, which might be part of a larger ClientFactory.

## ClientFactory<a id="ClientFactory"></a>

A ClientFactory is not required to use Activities and Places; however, it
is helpful to use a factory or dependency injection framework like GIN
to obtain references to objects needed throughout your application like
the event bus. Our example uses a ClientFactory to provide an EventBus,
GWT PlaceController, and view implementations.

```java
public interface ClientFactory {
    EventBus getEventBus();
    PlaceController getPlaceController();
    HelloView getHelloView();
    GoodbyeView getGoodbyeView();
}
```

Another advantage of using a ClientFactory is that you can use it
with GWT deferred binding to use different implementation classes based
on user.agent or other properties. For example, you might use a
MobileClientFactory to provide different view implementations than the
default DesktopClientFactory. To do this, instantiate your ClientFactory
with GWT.create in onModuleLoad(), like this:

```java
ClientFactory clientFactory = GWT.create(ClientFactory.class);
```

Specify the implementation class in .gwt.xml:

```xml
<!-- Use ClientFactoryImpl by default -->
    <replace-with class="com.hellomvp.client.ClientFactoryImpl">
    <when-type-is class="com.hellomvp.client.ClientFactory"/>
    </replace-with>
```

You can use `<when-property-is>` to specify different
implementations based on user.agent, locale, or other properties you
define. The [mobilewebapp](https://github.com/gwtproject/gwt/blob/main/samples/mobilewebapp/)
sample application defines a "formfactor" property used to 
select a different view implementations for mobile, tablet, and desktop devices.

Here is a default implementation of ClientFactory for the sample app:

```java
public class ClientFactoryImpl implements ClientFactory {
    private final EventBus eventBus = new SimpleEventBus();
    private final PlaceController placeController = new PlaceController(eventBus);
    private final HelloView helloView = new HelloViewImpl();
    private final GoodbyeView goodbyeView = new GoodbyeViewImpl();

    @Override
    public EventBus getEventBus() {
        return eventBus;
    }
    ...
}
```

## Activities<a id="Activities"></a>

Activity classes implement com.google.gwt.activity.shared.Activity. For
convenience, you can extend AbstractActivity, which provides default
(null) implementations of all required methods. Here is a HelloActivity,
which simply says hello to a named user:

```java
public class HelloActivity extends AbstractActivity implements HelloView.Presenter {
    // Used to obtain views, eventBus, placeController
    // Alternatively, could be injected via GIN
    private ClientFactory clientFactory;
    // Name that will be appended to "Hello,"
    private String name;

    public HelloActivity(HelloPlace place, ClientFactory clientFactory) {
        this.name = place.getHelloName();
        this.clientFactory = clientFactory;
    }

    /**
     * Invoked by the ActivityManager to start a new Activity
     */
    @Override
    public void start(AcceptsOneWidget containerWidget, EventBus eventBus) {
        HelloView helloView = clientFactory.getHelloView();
        helloView.setName(name);
        helloView.setPresenter(this);
        containerWidget.setWidget(helloView.asWidget());
    }

    /**
     * Ask user before stopping this activity
     */
    @Override
    public String mayStop() {
        return "Please hold on. This activity is stopping.";
    }

    /**
     * Navigate to a new Place in the browser
     */
    public void goTo(Place place) {
        clientFactory.getPlaceController().goTo(place);
    }
}
```

The first thing to notice is that HelloActivity makes reference
to HelloView, which is a view interface, not an implementation. One style
of MVP coding defines the view interface in the presenter. This is
perfectly legitimate; however, there is no fundamental reason why an
Activity and it's corresponding view interface have to be tightly bound
together. Note that HelloActivity also implements the view's Presenter interface.
This is used to allow the view to call methods on the Activity, which
facilitates the use of UiBinder as we saw above.

The HelloActivity constructor takes two arguments: a HelloPlace
and the ClientFactory. Neither is strictly required for an Activity. The
HelloPlace simply makes it easy for HelloActivity to obtain properties
of the state represented by HelloPlace (in this case, the name of the
user we are greeting). Accepting an instance of a HelloPlace in the
constructor implies that a new HelloActivity will be created for each
HelloPlace. You could instead obtain an activity from a factory, but 
it's typically cleaner to use a newly constructed Activity so you
don't have to clean up any prior state. Activities are designed to be
disposable, whereas views, which are more expensive to create due to the
DOM calls required, should be reusable. In keeping with this idea,
ClientFactory is used by HelloActivity to obtain a reference to the
HelloView as well as the EventBus and PlaceController.

The start method is invoked by the ActivityManager and sets
things in motion. It updates the view and then swaps the view back into
the Activity's container widget by calling setWidget.

The non-null mayStop() method provides a warning that will be
shown to the user when the Activity is about to be stopped due to window
closing or navigation to another Place. If it returns null, no such
warning will be shown.

Finally, the goTo() method invokes the PlaceController to
navigate to a new Place. PlaceController in turn notifies the
ActivityManager to stop the current Activity, find and start the
Activity associated with the new Place, and update the URL in
PlaceHistoryHandler.

## Places<a id="Places"></a>

In order to be accessible via a URL, an Activity needs a
corresponding Place. A Place extends com.google.gwt.place.shared.Place and
must have an associated PlaceTokenizer which knows how to serialize the
Place's state to a URL token. By default, the URL consists of the
Place's simple class name (like "HelloPlace") followed by a colon (:)
and the token returned by the PlaceTokenizer.

```java
public class HelloPlace extends Place {
    private String helloName;

    public HelloPlace(String token) {
        this.helloName = token;
    }

    public String getHelloName() {
        return helloName;
    }

    public static class Tokenizer implements PlaceTokenizer<HelloPlace> {
        @Override
        public String getToken(HelloPlace place) {
            return place.getHelloName();
        }

        @Override
        public HelloPlace getPlace(String token) {
            return new HelloPlace(token);
        }
    }
}
```

It is convenient (though not required) to declare the
PlaceTokenizer as a static class inside the corresponding Place.
However, you need not have a PlaceTokenizer for each Place. Many Places
in your app might not save any state to the URL, so they could just
extend a BasicPlace which declares a PlaceTokenizer that returns a null
token.

## PlaceHistoryMapper<a id="PlaceHistoryMapper"></a>

PlaceHistoryMapper declares all the Places available in your app.
You create an interface that extends PlaceHistoryMapper and uses the
annotation @WithTokenizers to list each of your tokenizer classes. Here
is the PlaceHistoryMapper in our sample:

```java
@WithTokenizers({HelloPlace.Tokenizer.class, GoodbyePlace.Tokenizer.class})
public interface AppPlaceHistoryMapper extends PlaceHistoryMapper
{
}
```

At GWT compile time, GWT generates (see
PlaceHistoryMapperGenerator) a class based on your interface that
extends AbstractPlaceHistoryMapper. PlaceHistoryMapper is the link
between your PlaceTokenizers and GWT's PlaceHistoryHandler that
synchronizes the browser URL with each Place.

For more control of the PlaceHistoryMapper, you can use the
@Prefix annotation on a PlaceTokenizer to change the first part of the
URL associated with the Place. For even more control, you can instead
implement PlaceHistoryMapperWithFactory and provide a TokenizerFactory
that, in turn, provides individual PlaceTokenizers.

## ActivityMapper<a id="ActivityMapper"></a>

Finally, your app's ActivityMapper maps each Place to its
corresponding Activity. It must implement ActivityMapper, and will
likely have lots of code like "if (place instanceof SomePlace) return
new SomeActivity(place)". Here is the ActivityMapper for our
sample app:

```java
public class AppActivityMapper implements ActivityMapper {
    private ClientFactory clientFactory;

    public AppActivityMapper(ClientFactory clientFactory) {
        super();
        this.clientFactory = clientFactory;
    }

    @Override
    public Activity getActivity(Place place) {
        if (place instanceof HelloPlace)
            return new HelloActivity((HelloPlace) place, clientFactory);
        else if (place instanceof GoodbyePlace)
            return new GoodbyeActivity((GoodbyePlace) place, clientFactory);
        return null;
    }
}
```

Note that our ActivityMapper must know about the ClientFactory so it can provide
it to activities as needed.

## Putting it all together<a id="Putting_it_all_together"></a>

Here's how all the pieces come together in onModuleLoad():

```java
public class HelloMVP implements EntryPoint {
    private Place defaultPlace = new HelloPlace("World!");
    private SimplePanel appWidget = new SimplePanel();

    public void onModuleLoad() {
        ClientFactory clientFactory = GWT.create(ClientFactory.class);
        EventBus eventBus = clientFactory.getEventBus();
        PlaceController placeController = clientFactory.getPlaceController();

        // Start ActivityManager for the main widget with our ActivityMapper
        ActivityMapper activityMapper = new AppActivityMapper(clientFactory);
        ActivityManager activityManager = new ActivityManager(activityMapper, eventBus);
        activityManager.setDisplay(appWidget);

        // Start PlaceHistoryHandler with our PlaceHistoryMapper
        AppPlaceHistoryMapper historyMapper= GWT.create(AppPlaceHistoryMapper.class);
        PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(historyMapper);
        historyHandler.register(placeController, eventBus, defaultPlace);

        RootPanel.get().add(appWidget);
        // Goes to the place represented on URL else default place
        historyHandler.handleCurrentHistory();
    }
}
```

## How it all works<a id="How_it_all_works"></a>

The ActivityManager keeps track of all Activities running within
the context of one container widget. It listens for
PlaceChangeRequestEvents and notifies the current activity when a new
Place has been requested. If the current Activity allows the Place
change (Activity.onMayStop() returns null) or the user allows it (by
clicking OK in the confirmation dialog), the ActivityManager discards
the current Activity and starts the new one. In order to find the new
one, it uses your app's ActivityMapper to obtain the Activity associated
with the requested Place.

Along with the ActivityManager, two other GWT classes work to
keep track of Places in your app. PlaceController initiates navigation
to a new Place and is responsible for warning the user before doing so.
PlaceHistoryHandler provides bi-directional mapping between Places and
the URL. Whenever your app navigates to a new Place, the URL will be
updated with the new token representing the Place so it can be
bookmarked and saved in browser history. Likewise, when the user clicks
the back button or pulls up a bookmark, PlaceHistoryHandler ensures that
your application loads the corresponding Place.

## How to navigate<a id="How_to_navigate"></a>

To navigate to a new Place in your application, call the goTo()
method on your PlaceController. This is illustrated above in the goTo()
method of HelloActivity. PlaceController warns the current Activity that
it may be stopping (via a PlaceChangeRequest event) and once allowed,
fires a PlaceChangeEvent with the new Place. The PlaceHistoryHandler
listens for PlaceChangeEvents and updates the URL history token
accordingly. The ActivityManager also listens for PlaceChangeEvents and
uses your app's ActivityMapper to start the Activity associated with the
new Place.

Rather than using PlaceController.goTo(), you can also create a
Hyperlink containing the history token for the new Place obtained by
calling your PlaceHistoryMapper.getToken(). When the user navigates to a
new URL (via hyperlink, back button, or bookmark), PlaceHistoryHandler
catches the ValueChangeEvent from the History object and calls your
app's PlaceHistoryMapper to turn the history token into its
corresponding Place. It then calls PlaceController.goTo() with the new
Place.

What about apps with multiple panels in the same window whose
state should all be saved together in a single URL? You can accomplish
this by using an ActivityManager and ActivityMapper for each panel. Using
this technique, a Place can be mapped to multiple activities. For
further examples, see the resources below.

## Related resources<a id="resources"></a>

*   [High-Performance GWT](https://www.youtube.com/watch?v=0F5zc1UAt2Y)
        (starting at 18:12, slides 23-41)
*   [Using GWT and Eclipse to Build Great Mobile Web Apps](https://www.youtube.com/watch?v=N1aCo5LvMf8)
        (form factor discussion starting at 3:56, slides 6-20)
