

<style>
code, .code {font-size: 9pt; font-family: Courier, Courier New, monospace; color:#007000;}
.highlight {background-color: #ffc;}
.strike {text-decoration:line-through; color:red;}
.header {margin-top: 1.5ex;}
.details {margin-top: 1ex;}
</style>

<p>
All GWT applications run as JavaScript code in the end user's web browser. Frequently, though, you'll want to create more than just a standalone client-side application. Your application will need to communicate with a web server, sending requests and receiving updates.
</p>

<p>
Each time traditional web applications talk to the web server, they fetch a completely new HTML page. In contrast, AJAX applications offload the user interface logic to the client and make asynchronous calls to the server to send and receive only the data itself. This allows the UI of an AJAX application to be much more responsive and fluid while reducing an application's bandwidth requirements and the load on the server.
</p>

<p>
At this point, you've created the initial implementation of the StockWatcher application, simulating stock data in the client-side code. In the following three tutorials, you will learn how to retrieve the stock data from a server.
</p>

<p class="note" style="margin-left: 1.2em; margin-right: 1.5em;">
<b>Note:</b> For a broader guide to client-server communication in a GWT application, see <a href="../DevGuideServerCommunication.html">Communicate with a Server</a>.
</p>

<h2>Choosing an Implementation Strategy</h2>
<p>
GWT provides a few different ways to communicate with a server.
Which data format you use will ultimately depend on the server you need to interact with.
</p>

<h3>Making Remote Procedure Calls (GWT RPC)</h3>
<p>
If you can run Java on the backend and are creating an interface for your application's server-side business logic, GWT RPC is probably your best choice.
GWT RPC is a mechanism for passing Java objects to and from a server over standard HTTP.
You can use the GWT RPC framework to transparently make calls to Java servlets and let GWT take care of low-level details like object serialization.
</p>
<p>
To try this out, see the tutorial, <a href="RPC.html">Making Remote Procedure Calls</a>.
</p>

<h3>Retrieving JSON Data via HTTP</h3>
<p>
If your application talks to a server that cannot host Java servlets, or one that already uses another data format like JSON or XML, you can make HTTP requests to retrieve the data. GWT provides generic HTTP classes that you can use to build the request, and JSON and XML client classes that you can use to process the response. You can also use overlay types to convert JavaScript objects into Java objects that you can interact with in your IDE while developing.
</p>
<p>
To try this out, see the tutorial, <a href="JSON.html">Retrieving JSON Data</a>. Although this tutorial covers making HTTP requests only for JSON data, the code can be adapted to work with XML data instead.
</p>




<h3>Making Cross-Site Requests for JSONP</h3>
<p>
If you're creating a mashup application that needs to use data from one or more remote web servers, you will have to work around SOP (Same Origin Policy) access restrictions. In this tutorial, you'll use JavaScript Native Interface (JSNI) to write a &lt;script&gt; tag that retrieves JSON with padding (JSONP).
</p>
<p>
To try this out, see the tutorial, <a href="Xsite.html">Making Cross-Site Requests</a>.
</p>
<p class="note">
Note: There is a variety of public sources of JSON-formatted data you can practice with, including <a href="//developers.google.com/gdata/">Google Data APIs</a> and <a href="http://developer.yahoo.com/">Yahoo! Web Services</a>.
</p>

<h2>Making Asynchronous Calls</h2>
<p>
Whether you use GWT RPC or get JSON data via HTTP, all the calls you make from the HTML page to the server are asychronous.
This means they do not block while waiting for the call to return.
The code following the call executes immediately.
When the call completes, the callback method you specified when you made the call will execute.
</p>
<p>
Asynchronous calls are a core principle of AJAX (Asynchronous JavaScript And XML) development.
The benefits of making asynchronous calls rather than simpler (for the developer) synchronous calls are in the improved end-user experience:
</p>
<ul>
    <li>The user interface remains responsive.<br />The JavaScript engines in web browsers are generally single-threaded, so calling a server synchronously causes the web page to "hang" until the call completes. If the network is slow or the server is unresponsive, this could ruin the end-user experience.</li>
    <li>You can perform other work while waiting on a pending server call.<br />For example, you can build up your user interface while simultaneously retrieving the data from the server to populate the interface. This shortens the overall time it takes for the user to see the data on the page.</li>
    <li>You can make multiple server calls at the same time.<br />However, just how much parallelism you can add using asynchronous calls is limited because browsers typically restrict the number of outgoing network connections to two at a time.</li>
</ul>
<p>
If you are new to AJAX development, the hardest thing to get used to about asynchronous calls is that the calls are non-blocking. However, Java inner classes go a long way toward making this manageable.
</p>

<p class="note">
For more information on making asynchronous calls, see the Developer's Guide, <a href="../DevGuideServerCommunication.html#DevGuideGettingUsedToAsyncCalls">Getting Used to Asynchronous Calls</a>
</p>


