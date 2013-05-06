<i>Google Developer Relations Team</i>
<br>
<i>July 2007</i>

<p>
It's now been a few weeks since the release of GWT 1.4 and Apple's iPhone.&nbsp;
We've spent some of that time learning how to optimize GWT
applications for the iPhone.&nbsp; Since nothing beats experience with real
code, we decided to write an application that we would find useful and that
shows off the cool features of the iPhone.&nbsp; The result is the
<a href="http://gwt.google.com/samples/GwtFeedReader">GWT
Feed Reader</a>, an RSS feed reader that uses the
<a href="/feed/">Google
AJAX Feed API</a> with a user interface optimized for the iPhone. This article
will discuss what we've learned from writing this RSS reader.
</p>

<p>
The good news is that writing a GWT application that
targets the iPhone is no different from writing any other application.&nbsp; 
On the other hand, the way in which your users interact with a mobile application is
somewhat different from how they interact with a "desktop" application.&nbsp;
Even though your existing desktop GWT application may execute on the iPhone,
it might not be very easy to use, and might not feel like a mobile application should.&nbsp;
For more than just occasional use, your users will want an
interface optimized for their device.&nbsp;
</p>

<p>
Before we dive in, it's worth noting that
developers that intend to target the iPhone should refer to Apple's
<a href="http://developer.apple.com/iphone/">development
guide for the iPhone</a>.&nbsp; It covers how users interact with web
applications on the iPhone, ways to optimize your application for the iPhone,
and links to other iPhone-related development communities.&nbsp; These
guidelines are applicable to static content as well as client-side application
development using GWT.</p>

<p>
Instead of covering the basics of writing a GWT application, we'll stick
to highlighting the design decisions that we made to make the GWT Feed Reader a
usable mobile application.&nbsp; Most of design stems from understanding the
limitations of the device.
</p>

<h2>Interface design decisions</h2>

<p>
The iPhone has three primary UI gestures: tapping (or pointing),
swiping, and double-taps.&nbsp; Tapping is the primary command gesture,
analogous to a mouse click, and can be handled with standard
<a href="http://google-web-toolkit.googlecode.com/svn/javadoc/1.5/com/google/gwt/user/client/ui/ClickListener.html">ClickListener.onClick()</a>
events.&nbsp; Swiping, in both vertical and horizontal directions, is used to
pan the viewport over the (sometimes larger) virtual page.
</p>

<p>
When the UI can be designed as a vertically strip, a properly-sized
<a href="http://google-web-toolkit.googlecode.com/svn/javadoc/1.5/com/google/gwt/user/client/ui/Panel.html">Panel</a>,
combined with the viewport meta tag, can be easily adapted to provide a
"<a href="http://gwt-feed-reader.googlecode.com/svn/trunk/src/com/google/gwt/sample/feedreader/client/WallToWallPanel.java">wall-to-wall</a>"
layout that eliminates horizontal scrolling.&nbsp; Applications that are
designed to fit entirely within a single column should set a viewport width of
320 pixels by adding <code>&lt;meta
name="viewport" content="width=320"&gt;</code> in the
<code>&lt;head&gt;</code> section of the host
page.&nbsp; CSS width rules using relative sizes will use the size of the actual
viewport, and not the default virtual page size of 980 pixels.&nbsp; If you
experience unwanted horizontal overflow, the width of various widgets can be
constrained by use of the <code>max-width</code>
CSS attribute.&nbsp; This is especially useful to constrain images from sources
that may not target the iPhone directly.&nbsp; In the case of the GWT Feed
Reader, user scaling is disabled completely to decrease the navigational
complexity of the user interface by specifying the viewport meta
<code>content="width=320; initial-scale=1.0; maximum-scale=1.0;
user-scalable=0;"</code>.
</p>

<p>
The double-tap gesture will zoom the document to fill the screen with the
element enclosing the target point.&nbsp; This maps very nicely onto the
Element-per-Widget design of GWT's UI toolkit.&nbsp; By structuring your widget
hierarchy to group related collections of Widgets into Panels (and perhaps
nesting related Panels in outer panels), the double-tap gesture will allow the
user to navigate the document in a hierarchical fashion and allow more effective
targeting of the zoom region.
</p>

<p>
It's important to remember that fingers are both opaque and of non-zero
size when pressed against the display on the iPhone.&nbsp; If we assume that the
smallest target should be about the size of a pressed fingertip, we have a
minimum size of a quarter of an inch square. The iPhone's screen has better than
average dot pitch of 160 dpi, making the smallest useful target on the order of
40x40 pixels.&nbsp; If the application is intended for use on the iPhone as well
as the desktop, you will want to use CSS media selectors to load the correct
stylesheet as outlined in the iPhone developer's guide.<br>
</p>

<h2>Element-per-Widget design</h2>

<p>
The Document Object Model (DOM) specification defines a hierarchical box
model that is used to compose the image that is displayed on your screen when
you view a web page.&nbsp; Typically, web browsers convert the HTML data
received from a web server into a DOM structure, however it is also possible to
manipulate the DOM via purely programmatic means via client-side JavaScript
code.&nbsp; The GWT UI classes provide abstractions over the underlying DOM
structures that they represent, allowing you, the developer, to think about
high-level Widgets and Panels, rather than collections of DOM elements.&nbsp;
Each Widget has associated with it a DOM Element that represents the Widget in
the DOM.&nbsp; Simple Widgets, such as Button, can be represented by a single
DOM Element.
</p>

<p>
More complicated Widgets, like VerticalPanel, have a single root Element
that act as containers for the Elements of child Widgets.&nbsp; The individual
rows of the VerticalPanel are Elements that can be targeted by the zoom
gesture.&nbsp; If the Widgets contained by the VerticalPanel are of unequal
widths, the zoom gesture will still allow the user to approximately zoom in on
the row's element, even in the case of missing the desired Widget.&nbsp; The
user can then more accurately target the desired widget, as it will have
increased in visible size.
</p>

<h2>Improving interactions</h2>

<p>
Both actual as well as perceived application performance are critically
important factors to consider when designing user interactions.&nbsp; If the
application pauses, hangs, or otherwise stalls during use, users will very
quickly become frustrated.&nbsp;
Progressive (or lazy) rendering in the UI and retention of already-rendered UI
elements adds to the perception of responsiveness.&nbsp;
Applications typically "feel faster" and annoy the user less when something happens
immediately in response to a user's event, even if it is to simply displaying a
"Loading..." message. &nbsp; Using
<a href="http://google-web-toolkit.googlecode.com/svn/javadoc/1.5/com/google/gwt/user/client/DeferredCommand.html">DeferredCommand.addCommand()</a> with an
<a href="http://google-web-toolkit.googlecode.com/svn/javadoc/1.5/com/google/gwt/user/client/IncrementalCommand.html">IncrementalCommand</a>
allows you to implement a "deferred Iterator".&nbsp; This will avoid blocking
the UI event loop while you create UI elements from a list of data
objects:
</p>

<pre>
  final List objects = ....;
  DeferredCommand.addCommand(new IncrementalCommand() {
    Iterator i = objects.iterator();
    public boolean execute() {
      Foo foo = (Foo)i.next();
      .... do something ...
      return i.hasNext();
    }
  });
</pre>

<p>
Robust use of GWT's
<a href="http://google-web-toolkit.googlecode.com/svn/javadoc/1.5/com/google/gwt/user/client/History.html">History
support</a> adds to the usability of the application.&nbsp; The browser's back
and forward buttons are always on-screen, so you may as well take advantage of
them in your application.&nbsp; Most of the state changes within the GWT Feed
Reader are controlled by history tokens.&nbsp; Instead of having user-generated
events directly cause panels to be shown or hidden, the code simply executes a
<code>History.newItem()</code> call.&nbsp; This
ensures that externally-driven behavior (back/forward, deep-linking) is
identical to UI-driven behavior and serves to decouple event-handling code from
presentation code. For example, articles in a feed are viewed by setting the
history token to a combination of the feed's URL and the link target URL of the
article.&nbsp; See the
<a href="http://gwt-feed-reader.googlecode.com/svn/trunk/src/com/google/gwt/sample/feedreader/client/GwtFeedReader.java">processHistoryToken()</a>
function.
</p>

<h2>Program data</h2>

<p>
Minimizing the number of round-trips to the server was also a design
priority for the GWT Feed Reader application.&nbsp; We are using
<a href="http://code.google.com/p/google-web-toolkit-incubator/wiki/ImmutableResourceBundle">ImmutableResourceBundle</a>
and
<a href="http://code.google.com/p/google-web-toolkit-incubator/wiki/StyleInjector">StyleInjector</a>
from the new
<a href="http://code.google.com/p/google-web-toolkit-incubator">GWT
Incubator</a> project.&nbsp; This combination allows program resources, such as
CSS and background-image files to be either cached "forever" or inlined directly
into the GWT application.&nbsp; The latter behavior allows the feed reader to
always be able to render correctly, even when the iPhone is temporarily unable
to access the Internet.&nbsp; Programmatic access to module
<a href="http://gwt-feed-reader.googlecode.com/svn/trunk/src/com/google/gwt/sample/feedreader/client/resources/Resources.java">Resources</a>
also helps in the development phase, because the compiler will warn you of
missing files.&nbsp; As an additional deployment optimization, the module
selection script has been inlined into the host HTML page as a post-build
step.&nbsp; The net effect of these optimizations is that the entire GWT Feed
Reader application and all of its runtime resources can be downloaded in just
two HTTP round-trips.
</p>

<p>
The Feed Reader needs to get its feed data from somewhere.&nbsp; Enough
of the
<a href="/feed/">Google
AJAX Feed API</a> was imported with Java bindings using the
<a href="http://code.google.com/p/gwt-api-interop">GWT
JavaScript Interop</a> project.&nbsp; This eliminated the need to hand-write
JSNI calls to the underlying JavaScript API by declaring the binding with a
flyweight-style API.&nbsp; The binding classes are located in the
<a href="http://gwt-feed-reader.googlecode.com/svn/trunk/src/com/google/gwt/ajaxfeed">com.google.gwt.ajaxfeed</a>
package.
</p>

<p>
Because this is a ultimately a demonstration app that needs to be able to
run without server support (e.g. from a local filesystem), we use GWT's
<a href="http://google-web-toolkit.googlecode.com/svn/javadoc/1.5/com/google/gwt/user/client/Cookies.html">support
for manipulating browser cookies</a> to store configuration and last-read
information.&nbsp; The data is stored as a JSON-encoded string in the cookie and
accessed through the
<a href="http://gwt-feed-reader.googlecode.com/svn/trunk/src/com/google/gwt/sample/feedreader/client/Configuration.java">Configuration</a>
class. A more fully-featured version of the application might include
server-side support for configuration and tracking last-read articles.&nbsp;
</p>

<h2>Wrapping it up</h2>

<p>After deciding on the UI layout style, implementing the RSS reader
application was just like writing any other GWT application.&nbsp; Much of the
gross feature set was worked out with hosted-mode development and then the
fit-and-finish of the application was finalized using a combination of Safari3
and an iPhone.&nbsp; Most of the time, the test application was accessed over
the EDGE network, to simulate the typical use case.&nbsp; Targeting the
high-latency, low-bandwidth configuration makes using the application on a WiFi
network even better.
</p>

<p>
We look forward to seeing more GWT applications on the iPhone in the
future.&nbsp; If you are interested in the code for the GWT Feed Reader, it is
Open-Source under the Apache 2.0 License, and has its own
<a href="http://code.google.com/p/gwt-feed-reader/" target="_blank">project
page</a> on Google Code.
</p>

<p>
Happy coding,<br>
 -The GWT team.
</p>


