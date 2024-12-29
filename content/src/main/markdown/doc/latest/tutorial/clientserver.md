Ajax Communication: Introduction
===

All GWT applications run as JavaScript code in the end user's web browser. Frequently, though, you'll want to create more than just a standalone client-side application. Your application will need to communicate with a web server, sending requests and receiving updates.

Each time traditional web applications talk to the web server, they fetch a completely new HTML page. In contrast, AJAX applications offload the user interface logic to the client and make asynchronous calls to the server to send and receive only the data itself. This allows the UI of an AJAX application to be much more responsive and fluid while reducing an application's bandwidth requirements and the load on the server.

At this point, you've created the initial implementation of the StockWatcher application, simulating stock data in the client-side code. In the following three tutorials, you will learn how to retrieve the stock data from a server.

**Note:** For a broader guide to client-server communication in a GWT application, see [Communicate with a Server](../DevGuideServerCommunication.html).

## Choosing an Implementation Strategy

GWT provides a few different ways to communicate with a server.
Which data format you use will ultimately depend on the server you need to interact with.

### Making Remote Procedure Calls (GWT RPC)

If you can run Java on the backend and are creating an interface for your application's server-side business logic, GWT RPC is probably your best choice.
GWT RPC is a mechanism for passing Java objects to and from a server over standard HTTP.
You can use the GWT RPC framework to transparently make calls to Java servlets and let GWT take care of low-level details like object serialization.

To try this out, see the tutorial, [Making Remote Procedure Calls](RPC.html).

### Retrieving JSON Data via HTTP

If your application talks to a server that cannot host Java servlets, or one that already uses another data format like JSON or XML, you can make HTTP requests to retrieve the data. GWT provides generic HTTP classes that you can use to build the request, and JSON and XML client classes that you can use to process the response. You can also use overlay types to convert JavaScript objects into Java objects that you can interact with in your IDE while developing.

To try this out, see the tutorial, [Retrieving JSON Data](JSON.html). Although this tutorial covers making HTTP requests only for JSON data, the code can be adapted to work with XML data instead.

### Making Cross-Site Requests for JSONP

If you're creating a mashup application that needs to use data from one or more remote web servers, you will have to work around SOP (Same Origin Policy) access restrictions. In this tutorial, you'll use JsonpRequestBuilder to retrieve JSON with padding (JSONP).

To try this out, see the tutorial, [Making Cross-Site Requests](Xsite.html).

Note: There is a variety of public sources of JSON-formatted data you can practice with, including [Google Data APIs](https://developers.google.com/gdata/) and [Yahoo! Web Services](http://developer.yahoo.com/).

## Making Asynchronous Calls

Whether you use GWT RPC or get JSON data via HTTP, all the calls you make from the HTML page to the server are asychronous.
This means they do not block while waiting for the call to return.
The code following the call executes immediately.
When the call completes, the callback method you specified when you made the call will execute.

Asynchronous calls are a core principle of AJAX (Asynchronous JavaScript And XML) development.
The benefits of making asynchronous calls rather than simpler (for the developer) synchronous calls are in the improved end-user experience:

*   The user interface remains responsive.
The JavaScript engines in web browsers are generally single-threaded, so calling a server synchronously causes the web page to "hang" until the call completes. If the network is slow or the server is unresponsive, this could ruin the end-user experience.
*   You can perform other work while waiting on a pending server call.
For example, you can build up your user interface while simultaneously retrieving the data from the server to populate the interface. This shortens the overall time it takes for the user to see the data on the page.
*   You can make multiple server calls at the same time.
However, just how much parallelism you can add using asynchronous calls is limited because browsers typically restrict the number of outgoing network connections to two at a time.

If you are new to AJAX development, the hardest thing to get used to about asynchronous calls is that the calls are non-blocking. However, Java inner classes go a long way toward making this manageable.

For more information on making asynchronous calls, see the Developer's Guide, [Getting Used to Asynchronous Calls](../DevGuideServerCommunication.html#DevGuideGettingUsedToAsyncCalls)
