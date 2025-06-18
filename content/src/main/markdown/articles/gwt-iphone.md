GWT iPhone
===

_Google Developer Relations Team_

_July 2007_

_This article is obsolete, but may be of historical interest._

It's now been a few weeks since the release of GWT 1.4 and Apple's iPhone.
We've spent some of that time learning how to optimize GWT
applications for the iPhone. Since nothing beats experience with real
code, we decided to write an application that we would find useful and that
shows off the cool features of the iPhone. The result is the
GWT Feed Reader, an RSS feed reader that uses the
[Google
AJAX Feed API](https://developers.google.com/feed/) with a user interface optimized for the iPhone. This article
will discuss what we've learned from writing this RSS reader.

The good news is that writing a GWT application that
targets the iPhone is no different from writing any other application.
On the other hand, the way in which your users interact with a mobile application is
somewhat different from how they interact with a "desktop" application.
Even though your existing desktop GWT application may execute on the iPhone,
it might not be very easy to use, and might not feel like a mobile application should.
For more than just occasional use, your users will want an
interface optimized for their device.

Before we dive in, it's worth noting that
developers that intend to target the iPhone should refer to Apple's
[development
guide for the iPhone](http://developer.apple.com/iphone/). It covers how users interact with web
applications on the iPhone, ways to optimize your application for the iPhone,
and links to other iPhone-related development communities. These
guidelines are applicable to static content as well as client-side application
development using GWT.

Instead of covering the basics of writing a GWT application, we'll stick
to highlighting the design decisions that we made to make the GWT Feed Reader a
usable mobile application. Most of design stems from understanding the
limitations of the device.

## Interface design decisions

The iPhone has three primary UI gestures: tapping (or pointing),
swiping, and double-taps. Tapping is the primary command gesture,
analogous to a mouse click, and can be handled with standard
[ClickListener.onClick()](/javadoc/latest/com/google/gwt/user/client/ui/ClickListener.html)
events. Swiping, in both vertical and horizontal directions, is used to
pan the viewport over the (sometimes larger) virtual page.

When the UI can be designed as a vertically strip, a properly-sized
[Panel](/javadoc/latest/com/google/gwt/user/client/ui/Panel.html),
combined with the viewport meta tag, can be easily adapted to provide a
"[wall-to-wall](http://gwt-feed-reader.googlecode.com/svn/trunk/src/com/google/gwt/sample/feedreader/client/WallToWallPanel.java)"
layout that eliminates horizontal scrolling. Applications that are
designed to fit entirely within a single column should set a viewport width of
320 pixels by adding `<meta
name="viewport" content="width=320">` in the
`<head>` section of the host
page. CSS width rules using relative sizes will use the size of the actual
viewport, and not the default virtual page size of 980 pixels. If you
experience unwanted horizontal overflow, the width of various widgets can be
constrained by use of the `max-width`
CSS attribute. This is especially useful to constrain images from sources
that may not target the iPhone directly. In the case of the GWT Feed
Reader, user scaling is disabled completely to decrease the navigational
complexity of the user interface by specifying the viewport meta
`content="width=320; initial-scale=1.0; maximum-scale=1.0;
user-scalable=0;"`.

The double-tap gesture will zoom the document to fill the screen with the
element enclosing the target point. This maps very nicely onto the
Element-per-Widget design of GWT's UI toolkit. By structuring your widget
hierarchy to group related collections of Widgets into Panels (and perhaps
nesting related Panels in outer panels), the double-tap gesture will allow the
user to navigate the document in a hierarchical fashion and allow more effective
targeting of the zoom region.

It's important to remember that fingers are both opaque and of non-zero
size when pressed against the display on the iPhone. If we assume that the
smallest target should be about the size of a pressed fingertip, we have a
minimum size of a quarter of an inch square. The iPhone's screen has better than
average dot pitch of 160 dpi, making the smallest useful target on the order of
40x40 pixels. If the application is intended for use on the iPhone as well
as the desktop, you will want to use CSS media selectors to load the correct
stylesheet as outlined in the iPhone developer's guide.

## Element-per-Widget design

The Document Object Model (DOM) specification defines a hierarchical box
model that is used to compose the image that is displayed on your screen when
you view a web page. Typically, web browsers convert the HTML data
received from a web server into a DOM structure, however it is also possible to
manipulate the DOM via purely programmatic means via client-side JavaScript
code. The GWT UI classes provide abstractions over the underlying DOM
structures that they represent, allowing you, the developer, to think about
high-level Widgets and Panels, rather than collections of DOM elements.
Each Widget has associated with it a DOM Element that represents the Widget in
the DOM. Simple Widgets, such as Button, can be represented by a single
DOM Element.

More complicated Widgets, like VerticalPanel, have a single root Element
that act as containers for the Elements of child Widgets. The individual
rows of the VerticalPanel are Elements that can be targeted by the zoom
gesture. If the Widgets contained by the VerticalPanel are of unequal
widths, the zoom gesture will still allow the user to approximately zoom in on
the row's element, even in the case of missing the desired Widget. The
user can then more accurately target the desired widget, as it will have
increased in visible size.

## Improving interactions

Both actual as well as perceived application performance are critically
important factors to consider when designing user interactions. If the
application pauses, hangs, or otherwise stalls during use, users will very
quickly become frustrated.
Progressive (or lazy) rendering in the UI and retention of already-rendered UI
elements adds to the perception of responsiveness.
Applications typically "feel faster" and annoy the user less when something happens
immediately in response to a user's event, even if it is to simply displaying a
"Loading..." message. Using
[DeferredCommand.addCommand()](/javadoc/latest/com/google/gwt/user/client/DeferredCommand.html) with an
[IncrementalCommand](/javadoc/latest/com/google/gwt/user/client/IncrementalCommand.html)
allows you to implement a "deferred Iterator". This will avoid blocking
the UI event loop while you create UI elements from a list of data
objects:

```java
final List objects = ....;
  DeferredCommand.addCommand(new IncrementalCommand() {
    Iterator i = objects.iterator();
    public boolean execute() {
      Foo foo = (Foo)i.next();
      .... do something ...
      return i.hasNext();
    }
  });
  
```

Robust use of GWT's
[History
support](/javadoc/latest/com/google/gwt/user/client/History.html) adds to the usability of the application. The browser's back
and forward buttons are always on-screen, so you may as well take advantage of
them in your application. Most of the state changes within the GWT Feed
Reader are controlled by history tokens. Instead of having user-generated
events directly cause panels to be shown or hidden, the code simply executes a
`History.newItem()` call. This
ensures that externally-driven behavior (back/forward, deep-linking) is
identical to UI-driven behavior and serves to decouple event-handling code from
presentation code. For example, articles in a feed are viewed by setting the
history token to a combination of the feed's URL and the link target URL of the
article. See the
[processHistoryToken()](http://gwt-feed-reader.googlecode.com/svn/trunk/src/com/google/gwt/sample/feedreader/client/GwtFeedReader.java)
function.

## Program data

Minimizing the number of round-trips to the server was also a design
priority for the GWT Feed Reader application. We are using
[ImmutableResourceBundle](http://code.google.com/p/google-web-toolkit-incubator/wiki/ImmutableResourceBundle)
and
[StyleInjector](http://code.google.com/p/google-web-toolkit-incubator/wiki/StyleInjector)
from the new
[GWT
Incubator](http://code.google.com/p/google-web-toolkit-incubator) project. This combination allows program resources, such as
CSS and background-image files to be either cached "forever" or inlined directly
into the GWT application. The latter behavior allows the feed reader to
always be able to render correctly, even when the iPhone is temporarily unable
to access the Internet. Programmatic access to module
[Resources](http://gwt-feed-reader.googlecode.com/svn/trunk/src/com/google/gwt/sample/feedreader/client/resources/Resources.java)
also helps in the development phase, because the compiler will warn you of
missing files. As an additional deployment optimization, the module
selection script has been inlined into the host HTML page as a post-build
step. The net effect of these optimizations is that the entire GWT Feed
Reader application and all of its runtime resources can be downloaded in just
two HTTP round-trips.

The Feed Reader needs to get its feed data from somewhere. Enough
of the
[Google
AJAX Feed API](https://developers.google.com/feed/) was imported with Java bindings using the
[GWT
JavaScript Interop](http://code.google.com/p/gwt-api-interop) project. This eliminated the need to hand-write
JSNI calls to the underlying JavaScript API by declaring the binding with a
flyweight-style API. The binding classes are located in the
[com.google.gwt.ajaxfeed](http://gwt-feed-reader.googlecode.com/svn/trunk/src/com/google/gwt/ajaxfeed)
package.

Because this is a ultimately a demonstration app that needs to be able to
run without server support (e.g. from a local filesystem), we use GWT's
[support
for manipulating browser cookies](/javadoc/latest/com/google/gwt/user/client/Cookies.html) to store configuration and last-read
information. The data is stored as a JSON-encoded string in the cookie and
accessed through the
[Configuration](http://gwt-feed-reader.googlecode.com/svn/trunk/src/com/google/gwt/sample/feedreader/client/Configuration.java)
class. A more fully-featured version of the application might include
server-side support for configuration and tracking last-read articles.

## Wrapping it up

After deciding on the UI layout style, implementing the RSS reader
application was just like writing any other GWT application. Much of the
gross feature set was worked out with hosted-mode development and then the
fit-and-finish of the application was finalized using a combination of Safari3
and an iPhone. Most of the time, the test application was accessed over
the EDGE network, to simulate the typical use case. Targeting the
high-latency, low-bandwidth configuration makes using the application on a WiFi
network even better.

We look forward to seeing more GWT applications on the iPhone in the
future. If you are interested in the code for the GWT Feed Reader, it is
Open-Source under the Apache 2.0 License, and has its own
[project
page](http://code.google.com/p/gwt-feed-reader/) on Google Code.

Happy coding,

 -The GWT team.
