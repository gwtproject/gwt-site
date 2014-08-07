MVP Activities and Places
===

<p>GWT 2.1 introduced a built-in framework for browser history management.
The Activities and Places framework allows you to create bookmarkable
URLs within your application, thus allowing the browser's back button and
bookmarks to work as users expect.
It builds on GWT's <a href="DevGuideCodingBasicsHistory.html">history mechanism</a>
and may be used in conjunction with MVP development, though not required.</p>

<p class="note">Strictly speaking, MVP architecture
is not concerned with browser history management, but Activities and Places
may be used with MVP development as shown in this article. If you're not 
familiar with MVP, you may want to read these articles first:
<ul>
	<li><a href="../../articles/mvp-architecture.html">Large scale application development and MVP, Part I</a></li>
	<li><a href="../../articles/mvp-architecture-2.html">Large scale application development and MVP, Part II</a></li>
</ul>
</p>

<p><b id="Definitions">Definitions</b></p>

<p>An <i>activity</i> simply represents something the user is doing.
An Activity contains no Widgets or UI code. Activities typically 
restore state ("wake up"), perform initialization ("set up"), 
and load a corresponding UI ("show up"). Activities are started
and stopped by an ActivityManager associated with a container Widget. 
An Activity can automatically
display a warning confirmation when the Activity is about to be stopped
(such as when the user navigates to a new Place). In addition, the
ActivityManager warns the user before the window is about to be closed.</p>

<p>A <i>place</i> is a Java object representing a particular
state of the UI. A Place can be converted to and from a URL history
token (see GWT's <a href="DevGuideCodingBasicsHistory.html">History mechanism</a>)
by defining a PlaceTokenizer for each
Place, and GWT's PlaceHistoryHandler automatically updates the browser URL
corresponding to each Place in your app.</p>

<p>You can download all of the code referenced here in <a
	href="http://code.google.com/p/google-web-toolkit/downloads/detail?name=Tutorial-hellomvp-2.1.zip">this sample app</a>. The
sample app is a simple "Hello, World!" example demonstrating the use of 
Activities and Places with MVP.</p>

<p>Let's take a look at each of the moving parts in a GWT 2.1 app
using Places and Activities.</p>

<ol class="toc">
	<li><a href="#Views">Views</a></li>
	<li><a href="#ClientFactory">ClientFactory</a></li>
	<li><a href="#Activities">Activities</a></li>
	<li><a href="#Places">Places</a></li>
	<li><a href="#PlaceHistoryMapper">PlaceHistoryMapper</a></li>
	<li><a href="#ActivityMapper">ActivityMapper</a></li>
</ol>

<p>Then we'll take at how you wire it all together and how it works.</p>
<ol class="toc">
	<li><a href="#Putting_it_all_together">Putting it all together</a></li>
	<li><a href="#How_it_all_works">How it all works</a></li>
	<li><a href="#How_to_navigate">How to navigate</a></li>
	<li><a href="#resources">Related resources</a></li>
</ol>


<h2 id="Views">Views</h2>

<p>A view is simply the part of the UI
associated with an Activity. In MVP development, a view is defined by an
interface, which allows multiple view implementations based on client
characteristics (such as mobile vs. desktop) and also facilitates
lightweight unit testing by avoiding the time-consuming GWTTestCase.
There is no View interface or class in GWT which views must implement or
extend; however, GWT 2.1 introduces an IsWidget interface that is
implemented by most Widgets as well as Composite. It is useful for views
to extend IsWidget if they do in fact provide a Widget. Here is a simple
view from our sample app.</p>

<pre class="prettyprint">
public interface GoodbyeView extends IsWidget {
    void setName(String goodbyeName);
}
</pre>

<p>The corresponding view implementation extends Composite, which keeps
dependencies on a particular Widget from leaking out.</p>

<pre class="prettyprint">
public class GoodbyeViewImpl extends Composite implements GoodbyeView {
    SimplePanel viewPanel = new SimplePanel();
    Element nameSpan = DOM.createSpan();

    public GoodbyeViewImpl() {
        viewPanel.getElement().appendChild(nameSpan);
        initWidget(viewPanel);
    }

    @Override
    public void setName(String name) {
        nameSpan.setInnerText(&quot;Good-bye, &quot; + name);
    }
}
</pre>

<p>Here is a slightly more complicated view that additionally
defines an interface for its corresponding presenter (activity).</p>

<pre class="prettyprint">
public interface HelloView extends IsWidget {
    void setName(String helloName);
    void setPresenter(Presenter presenter);

    public interface Presenter {
        void goTo(Place place);
    }
}
</pre>

<p>The Presenter interface and setPresenter method allow for
bi-directional communication between view and presenter, which
simplifies interactions involving repeating Widgets and also allows view
implementations to use UiBinder with @UiHandler methods that delegate to
the presenter interface.</p>

<p>The HelloView implementation uses
<a href="DevGuideUiBinder.html">UiBinder</a>
and a template.</p>

<pre class="prettyprint">
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
</pre>

<p>Note the use of @UiHandler that delegates to the presenter.
Here is the corresponding template:</p>

<pre class="prettyprint">
&lt;!DOCTYPE ui:UiBinder SYSTEM &quot;http://dl.google.com/gwt/DTD/xhtml.ent&quot;&gt;
&lt;ui:UiBinder xmlns:ui=&quot;urn:ui:com.google.gwt.uibinder&quot;
			 xmlns:g=&quot;urn:import:com.google.gwt.user.client.ui&quot;&gt;
	&lt;ui:style&gt;
		.important {
			font-weight: bold;
		}
	&lt;/ui:style&gt;
	&lt;g:HTMLPanel&gt;
		Hello,
		&lt;span class=&quot;{style.important}&quot; ui:field=&quot;nameSpan&quot; /&gt;
		&lt;g:Anchor ui:field=&quot;goodbyeLink&quot; text=&quot;Say good-bye&quot;&gt;&lt;/g:Anchor&gt;
	&lt;/g:HTMLPanel&gt;
&lt;/ui:UiBinder&gt;
</pre>

<p>Because Widget creation involves DOM operations, views are
relatively expensive to create. It is therefore good practice to make
them reusable, and a relatively easy way to do this is via a view
factory, which might be part of a larger ClientFactory.</p>


<h2 id="ClientFactory">ClientFactory</h2>

<p>A ClientFactory is not required to use Activities and Places; however, it
is helpful to use a factory or dependency injection framework like GIN
to obtain references to objects needed throughout your application like
the event bus. Our example uses a ClientFactory to provide an EventBus,
GWT PlaceController, and view implementations.</p>

<pre class="prettyprint">
public interface ClientFactory {
    EventBus getEventBus();
    PlaceController getPlaceController();
    HelloView getHelloView();
    GoodbyeView getGoodbyeView();
}
</pre>

<p>Another advantage of using a ClientFactory is that you can use it
with GWT deferred binding to use different implementation classes based
on user.agent or other properties. For example, you might use a
MobileClientFactory to provide different view implementations than the
default DesktopClientFactory. To do this, instantiate your ClientFactory
with GWT.create in onModuleLoad(), like this:</p>

<pre class="prettyprint">
    ClientFactory clientFactory = GWT.create(ClientFactory.class);
</pre>

<p>Specify the implementation class in .gwt.xml:</p>

<pre class="prettyprint">
    &lt;!-- Use ClientFactoryImpl by default --&gt;
    &lt;replace-with class="com.hellomvp.client.ClientFactoryImpl"&gt;
    &lt;when-type-is class="com.hellomvp.client.ClientFactory"/&gt;
    &lt;/replace-with&gt;
</pre>

<p>You can use &lt;when-property-is&gt; to specify different
implementations based on user.agent, locale, or other properties you
define. The <a href="https://gwt.googlesource.com/gwt/+/master/samples/mobilewebapp/">mobilewebapp</a>
sample application defines a "formfactor" property used to 
select a different view implementations for mobile, tablet, and desktop devices.</p>

<p>Here is a default implementation of ClientFactory for the sample
app:</p>

<pre class="prettyprint">
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
</pre>


<h2 id="Activities">Activities</h2>

<p>Activity classes implement com.google.gwt.activity.shared.Activity. For
convenience, you can extend AbstractActivity, which provides default
(null) implementations of all required methods. Here is a HelloActivity,
which simply says hello to a named user:</p>

<pre class="prettyprint">
public class HelloActivity extends AbstractActivity implements HelloView.Presenter {
    // Used to obtain views, eventBus, placeController
    // Alternatively, could be injected via GIN
    private ClientFactory clientFactory;
    // Name that will be appended to &quot;Hello,&quot;
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
        return &quot;Please hold on. This activity is stopping.&quot;;
    }

    /**
     * Navigate to a new Place in the browser
     */
    public void goTo(Place place) {
        clientFactory.getPlaceController().goTo(place);
    }
}
</pre>

<p>The first thing to notice is that HelloActivity makes reference
to HelloView, which is a view interface, not an implementation. One style
of MVP coding defines the view interface in the presenter. This is
perfectly legitimate; however, there is no fundamental reason why an
Activity and it's corresponding view interface have to be tightly bound
together. Note that HelloActivity also implements the view's Presenter interface.
This is used to allow the view to call methods on the Activity, which
facilitates the use of UiBinder as we saw above.</p>

<p>The HelloActivity constructor takes two arguments: a HelloPlace
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
HelloView as well as the EventBus and PlaceController.</p>

<p>The start method is invoked by the ActivityManager and sets
things in motion. It updates the view and then swaps the view back into
the Activity's container widget by calling setWidget.</p>

<p>The non-null mayStop() method provides a warning that will be
shown to the user when the Activity is about to be stopped due to window
closing or navigation to another Place. If it returns null, no such
warning will be shown.</p>

<p>Finally, the goTo() method invokes the PlaceController to
navigate to a new Place. PlaceController in turn notifies the
ActivityManager to stop the current Activity, find and start the
Activity associated with the new Place, and update the URL in
PlaceHistoryHandler.</p>


<h2 id="Places">Places</h2>

<p>In order to be accessible via a URL, an Activity needs a
corresponding Place. A Place extends com.google.gwt.place.shared.Place and
must have an associated PlaceTokenizer which knows how to serialize the
Place's state to a URL token. By default, the URL consists of the
Place's simple class name (like "HelloPlace") followed by a colon (:)
and the token returned by the PlaceTokenizer.</p>

<pre class="prettyprint">
public class HelloPlace extends Place {
    private String helloName;

    public HelloPlace(String token) {
        this.helloName = token;
    }

    public String getHelloName() {
        return helloName;
    }

    public static class Tokenizer implements PlaceTokenizer&lt;HelloPlace&gt; {
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
</pre>

<p>It is convenient (though not required) to declare the
PlaceTokenizer as a static class inside the corresponding Place.
However, you need not have a PlaceTokenizer for each Place. Many Places
in your app might not save any state to the URL, so they could just
extend a BasicPlace which declares a PlaceTokenizer that returns a null
token.</p>


<h2 id="PlaceHistoryMapper">PlaceHistoryMapper</h2>

<p>PlaceHistoryMapper declares all the Places available in your app.
You create an interface that extends PlaceHistoryMapper and uses the
annotation @WithTokenizers to list each of your tokenizer classes. Here
is the PlaceHistoryMapper in our sample:</p>

<pre class="prettyprint">
@WithTokenizers({HelloPlace.Tokenizer.class, GoodbyePlace.Tokenizer.class})
public interface AppPlaceHistoryMapper extends PlaceHistoryMapper
{
}
</pre>

<p>At GWT compile time, GWT generates (see
PlaceHistoryMapperGenerator) a class based on your interface that
extends AbstractPlaceHistoryMapper. PlaceHistoryMapper is the link
between your PlaceTokenizers and GWT's PlaceHistoryHandler that
synchronizes the browser URL with each Place.</p>

<p>For more control of the PlaceHistoryMapper, you can use the
@Prefix annotation on a PlaceTokenizer to change the first part of the
URL associated with the Place. For even more control, you can instead
implement PlaceHistoryMapperWithFactory and provide a TokenizerFactory
that, in turn, provides individual PlaceTokenizers.</p>


<h2 id="ActivityMapper">ActivityMapper</h2>

<p>Finally, your app's ActivityMapper maps each Place to its
corresponding Activity. It must implement ActivityMapper, and will
likely have lots of code like "if (place instanceof SomePlace) return
new SomeActivity(place)". Here is the ActivityMapper for our
sample app:</p>

<pre class="prettyprint">
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
</pre>

<p>Note that our ActivityMapper must know about the ClientFactory so it can provide
it to activities as needed.</p>

<h2 id="Putting_it_all_together">Putting it all together</h2>

<p>Here's how all the pieces come together in onModuleLoad():</p>

<pre class="prettyprint">
public class HelloMVP implements EntryPoint {
    private Place defaultPlace = new HelloPlace(&quot;World!&quot;);
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
</pre>


<h2 id="How_it_all_works">How it all works</h2>

<p>The ActivityManager keeps track of all Activities running within
the context of one container widget. It listens for
PlaceChangeRequestEvents and notifies the current activity when a new
Place has been requested. If the current Activity allows the Place
change (Activity.onMayStop() returns null) or the user allows it (by
clicking OK in the confirmation dialog), the ActivityManager discards
the current Activity and starts the new one. In order to find the new
one, it uses your app's ActivityMapper to obtain the Activity associated
with the requested Place.</p>

<p>Along with the ActivityManager, two other GWT classes work to
keep track of Places in your app. PlaceController initiates navigation
to a new Place and is responsible for warning the user before doing so.
PlaceHistoryHandler provides bi-directional mapping between Places and
the URL. Whenever your app navigates to a new Place, the URL will be
updated with the new token representing the Place so it can be
bookmarked and saved in browser history. Likewise, when the user clicks
the back button or pulls up a bookmark, PlaceHistoryHandler ensures that
your application loads the corresponding Place.</p>


<h2 id="How_to_navigate">How to navigate</h2>

<p>To navigate to a new Place in your application, call the goTo()
method on your PlaceController. This is illustrated above in the goTo()
method of HelloActivity. PlaceController warns the current Activity that
it may be stopping (via a PlaceChangeRequest event) and once allowed,
fires a PlaceChangeEvent with the new Place. The PlaceHistoryHandler
listens for PlaceChangeEvents and updates the URL history token
accordingly. The ActivityManager also listens for PlaceChangeEvents and
uses your app's ActivityMapper to start the Activity associated with the
new Place.</p>

<p>Rather than using PlaceController.goTo(), you can also create a
Hyperlink containing the history token for the new Place obtained by
calling your PlaceHistoryMapper.getToken(). When the user navigates to a
new URL (via hyperlink, back button, or bookmark), PlaceHistoryHandler
catches the ValueChangeEvent from the History object and calls your
app's PlaceHistoryMapper to turn the history token into its
corresponding Place. It then calls PlaceController.goTo() with the new
Place.</p>

<p>What about apps with multiple panels in the same window whose
state should all be saved together in a single URL? You can accomplish
this by using an ActivityManager and ActivityMapper for each panel. Using
this technique, a Place can be mapped to multiple activities. For
further examples, see the resources below.</p>

<h2 id="resources">Related resources</h2>
<ul>
	<li>
		<a href="http://www.google.com/events/io/2011/sessions/high-performance-gwt-best-practices-for-writing-smaller-faster-apps.html">High-Performance GWT</a>
		(starting at 18:12, slides 23-41)
	</li>
	<li>
		<a href="http://www.google.com/events/io/2011/sessions/using-gwt-and-eclipse-to-build-great-mobile-web-apps.html">Using GWT and Eclipse to Build Great Mobile Web Apps</a>
		(form factor discussion starting at 3:56, slides 6-20)
	</li>
</ul>

