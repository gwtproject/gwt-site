<h2 id="DevGuideClientSide">Client-side code</h2>

<p><a href="DevGuideCodingBasicsClient.html">Client-side code</a> describes how to create an entry-point (code that executes when the user starts the application) into a client-side application.
When your application is sent across a network to a user, it runs as JavaScript inside their web browser. </p>


<h2 id="DevGuideJavaCompatibility">Compatibility with the Java Language and Libraries</h2>

<p><a href="DevGuideCodingBasicsCompatibility.html">Compatibility with the Java Language and Libraries</a> describes the differences in syntax and semantics between GWT and the core Java language.  It is important to remember that the target language of your GWT application is ultimately JavaScript, so there are some differences between running your application in <a href='DevGuideCompilingAndDebugging.html#DevGuideDevMode'>dev-mode</a>, <a href='../../articles/superdevmode.html'>superdev-mode</a>, and <a href='DevGuideCompilingAndDebugging.html#DevGuideProdMode'>production</a> mode. </p>


<h2 id="DevGuideHistory">History</h2>

<p><a href="DevGuideCodingBasicsHistory.html">History</a> describes how to integrate Ajax applications with the browser history.  Ajax applications sometimes fail to meet user's expectations because they do not interact with the browser in the same way as static web pages. It is often apparent &mdash; and
frustrating for users &mdash; when an Ajax application does not integrate with browser history. For example, users expect browsers to be able to navigate back to previously visited pages
using back and forward actions. Because an Ajax application is a usually a single page running JavaScript logic and not a series of pages, the browser history needs help from the
application to support this use case.  GWT's history mechanism makes history support fairly straightforward.</p>


<h2 id="DevGuideDateAndNumberFormat">Number and Date Formatting</h2>

<p><a href="DevGuideCodingBasicsFormatting.html">Number and Date Formatting</a> describes how to format numbers and dates in GWT.  GWT does not provide full emulation for the date and number formatting classes (such as java.text.DateFormat, java.text.DecimalFormat, java.text.NumberFormat, and java.TimeFormat). Instead, a subset of the functionality of the JRE classes is provided by com.google.gwt.i18n.client.NumberFormat and com.google.gwt.i18n.client.DateTimeFormat.  The major difference between the standard Java classes and the GWT classes is the ability to switch between different locales for formating dates and numbers at runtime. In GWT, the deferred binding mechanism is used to load only the logic needed for the current locale into the application.</p>


<h2 id="DevGuideDeferredCommand">Programming Delayed Logic</h2>

<p><a href="DevGuideCodingBasicsDelayed.html">Programming Delayed Logic</a> describes how to defer running code until a later point in time using three classes: Timer, DeferredCommand, and IncrementalCommand.  This is useful for scheduling an activity for some time in the future, periodically querying the server or updating the interface, queuing up work to do that must wait for other initialization to finish, and performing a large amount of computation.
</ul>


<h2 id="DevGuideJSON">Working with JSON</h2>

<p>Many AJAX application developers have adopted JSON as the data format of choice for server communication. It is a relatively simple format that is based on the object-literal notation of JavaScript. <a href="DevGuideCodingBasicsJSON.html">Working with JSON</a> explains how you can use JSON-encoded data within your application, GWT contains classes you can use to parse and manipulate JSON objects, as well as the very useful and elegant concept of JavaScript Overlay Types.</p>


<h2 id="DevGuideXML">Working with XML</h2>

<p>Extensible Markup Language (XML) is a data format commonly used in modern web applications. XML uses custom tags to describe
data and is encoded as plain text, making it both flexible and easy to work with.  <a href="DevGuideCodingBasicsXML.html">Working with XML</a> describes the GWT class library set of types designed for processing XML data.</p>


<h2 id="DevGuideJavaScriptNativeInterface">JavaScript Native Interface (JSNI)</h2>

<p>Often, you will need to integrate GWT with existing handwritten JavaScript or with a third-party JavaScript library. Occasionally you may need to access low-level browser functionality not exposed by the GWT class API's. The <a href="DevGuideCodingBasicsJSNI.html">JavaScript Native Interface</a> (JSNI) feature of GWT can solve both of these problems by allowing you to integrate JavaScript directly into your application's Java source code.</p>


<h2 id="DevGuideOverlayTypes">JavaScript Overlay Types</h2>

<p>Suppose you're happily using JSNI to call bits of handwritten JavaScript from within your GWT module. It works well, but JSNI only works at the level of individual methods. Some integration scenarios require you to more deeply intertwine JavaScript and Java objects &mdash; DOM and JSON programming are two good examples &mdash; and so what we really want is a way to interact directly with JavaScript objects from our Java source code. In other words, we want JavaScript objects that <i>look like</i> Java objects when we're coding.</p>

<p><a href="DevGuideCodingBasicsOverlay.html">JavaScript Overlay Types</a> make it easy to integrate entire families of JavaScript objects into your GWT project. There are many benefits of this technique, including the ability to use your Java IDE's code completion and refactoring capabilities even as you're working with untyped JavaScript objects.</p>


<h2 id="DevGuideDeferredBinding">Deferred Binding</h2>

<p><a href="DevGuideCodingBasicsDeferred.html">Deferred Binding</a> is a feature of the GWT compiler that works by generating many versions of code at compile time, only one of which needs to be loaded by a particular client
during bootstrapping at runtime. Each version is generated on a per browser basis, along with any other axis that your application defines or uses. For example, if you were to internationalize your application using GWT's Internationalization module, the GWT compiler would generate various versions of your application per browser environment, such as &quot;Firefox in English&quot;, &quot;Firefox in French&quot;, &quot;Internet Explorer in English&quot;, and so forth. As a result, the deployed JavaScript code is compact and quicker to download than hand coded JavaScript, containing only the code and resources it needs for a particular browser environment.</p>

