FAQ - Server
===

1.  [Why doesn't GWT provide a synchronous server connection option?](#Why_doesn)
2.  [What is the Same Origin Policy, and how does it affect GWT?](#What_is_the_Same_Origin_Policy,_and_how_does_it_affect_GWT?)
3.  [How do I make a call to the server if I am not using GWT RPC?](#How_do_I_make_a_call_to_the_server_if_I_am_not_using_GWT_RPC?)
4.  [Does GWT RPC support java.io.Serializable?](#Does_the_GWT_RPC_system_support_the_use_of_java.io.Serializable)
5.  [How can I dynamically fetch JSON feeds from other web domains?](#How_can_I_dynamically_fetch_JSON_feeds_from_other_web_domains?)

## Why doesn't GWT provide a synchronous server connection option?<a id="Why_doesn't_GWT_provide_a_synchronous_server_connection_opt"></a>

GWT's network operations are all asynchronous, or non-blocking. That is, they return immediately as soon as called, and require the user to use a callback method to handle the
results when they are eventually returned from the server. Though in some cases asynchronous operations are less convenient to use than synchronous operations, GWT does not provide
synchronous operations.

The reason is that most browsers' JavaScript engines are single-threaded. As a result, blocking on a call to XMLHTTPRequest also blocks the UI thread, making the browser appear
to freeze for the duration of the connection to the server. Some browsers provide a way around this, but there is no universal solution. GWT does not implement a synchronous
network connection because to do so would be to introduce a feature that does not work on all browsers, violating GWT's commitment to n o-compromise, cross-browser AJAX. It would
also introduce complexity for developers, who would have to maintain two different versions of their communications code in order to handle all browsers. 

## What is the Same Origin Policy, and how does it affect GWT?<a id="What_is_the_Same_Origin_Policy,_and_how_does_it_affect_GWT?"></a>

Modern browsers implement a security model known as the _Same Origin Policy_ (SOP). Conceptually, it is very simple, but the limitations it applies to JavaScript
applications can be quite subtle.

Simply stated, the SOP states that JavaScript code running on a web page may not interact with any resource not originating from the same web site. The reason this security
policy exists is to prevent malicious web coders from creating pages that steal web users' information or compromise their privacy. While very necessary, this policy also has the
side effect of making web developers' lives difficult.

It's important to note that the SOP issues described below are not specific to GWT; they are true of any AJAX application or framework. This FAQ simply details the specific
ways in which GWT is affected by this universal limitation.

### How the SOP Affects GWT<a id="How_the_SOP_Affects_GWT"></a>

When you compile a GWT application, it produces JavaScript code and HTML files. Because a GWT application is broken up into multiple files, the browser will make multiple
requests from the HTTP server to fetch all the pieces. Previous to GWT 1.4, this meant that all the GWT files must live on the same web server as the page which launches the GWT
application so as not to break the SOP. In GWT 1.4 and later, this is no longer an issue thanks to the new cross-site script inclusion bootstrap process mode.

Typically, the bootstrap process will load the GWT application files from the same web server that is serving the HTML host page. That is, supposing the host HTML file was
located at `http://mydomain.com/index.html`, the GWT application files would also be in the same directory (or a subdirectory) as the index.html file. To load the GWT
application, the `<`module`>`.nocache.js file would be bootstrapped by having the index.html file include a `<`script`>` tag referencing
the `<`module`>`.nocache.js file. This is the standard bootstrap process to load a GWT application. The `<`script`>` tag that needs to be
included in the main host HTML page is shown below:

```html
<script language="JavaScript" src="http://mydomain.com/<module>.nocache.js"></script>
```

However, many organizations setup their deployment platform in such a way that their main host HTML page is served up from [http://mydomain.com/](http://mydomain.com/), but any other resources such as images and JavaScript files are served up from a separate static server under
`http://static.mydomain.com/`. In older versions of GWT, this configuration would not be possible as the SOP prevented the GWT bootstrap process from allowing script from
files that were added from a different server to access the iframe in the main host HTML page. As of GWT 1.5, the bootstrap model now provides support for this kind of server
configuration via the cross-site linker (xs-linker). As of
GWT 2.1, you should however rather use the cross-site iframe linker (xsiframe-linker) which
sandboxes the GWT code inside an iframe, like the standard linker but contrary to the cross-site linker.

When using the cross-site linker or cross-site iframe linker, the compiler will still generate a
`<module>.nocache.js` that you will want to reference within your index.html. The
difference though, is that the `<module>.nocache.js` produced by the cross-site
linkers will link in a cache.js file for each of your permutations rather than
a cache.html file.

To enable the cross-site linking simply add the following to your
`<module>.gwt.xml` and include a reference to your `<module>.nocache.js` in your
index.html as you normally would.

```xml
<add-linker name="xsiframe"/>
```

For more details on the GWT bootstrap process, see "What's with all the cache/nocache stuff and weird filenames?"

### SOP, GWT, and XMLHTTPRequest Calls<a id="SOP,_GWT,_and_XMLHTTPRequest_Calls"></a>

The second area where the SOP is most likely to affect GWT users is the use of XMLHTTPRequest &mdash; the heart of AJAX. The SOP limits the XMLHTTPRequest browser call to URLs on
the same server from which the host page was loaded. This means that it is impossible to make any kind of AJAX-style request to a different site than the one from which the page
was loaded. For example, you might want to have your GWT application served up from `http://pages.mydomain.com/` but have it make requests for data to
`http://data.mydomain.com/`, or even a different port on the same server, such as `http://pages.mydomain.com:8888/`. Unfortunately, both these scenarios are
impossible, prevented by the SOP.

This limitation affects all forms of calls to the server, whether JSON, XML-RPC, or GWT's own RPC library. Generally, you must either run your CGI environment on the same
server as the GWT application, or implement a more sophisticated solution such as round-robin DNS load balancing or a reverse-proxy mechanism for your application's CGI calls.

In certain cases, it is possible to work around this limitation. For an example, see "How can I dynamically fetch JSON feeds from other web domains?"

### SOP and GWT Development Mode<a id="SOP_and_GWT_Hosted_Mode"></a>

The SOP applies to all GWT applications, whether running in web (compiled) mode on a web server, or in development mode.

This can cause problems if you are attempting to develop a GWT application that uses server-side technologies not supported by the GWT development mode, such as EJBs or Python code.
In such cases, you can use the `-noserver` argument for development mode to launch your GWT application from the server technology of your choice. For more information, see [How
do I use my own server in development mode instead of GWT's built-in Jetty instance?](DevGuideCompilingAndDebugging.html#use_my_own_server_in_development_mode_instead_of_GWT) Even if you choose to use this feature, however, remember that the SOP still applies.

## How do I make a call to the server if I am not using GWT RPC?<a id="How_do_I_make_a_call_to_the_server_if_I_am_not_using_GWT_RPC?"></a>

The heart of AJAX is making data read/write calls to a server from the JavaScript application running in the browser. GWT is "RPC agnostic" and has no particular requirements
about what protocol is used to issue RPC requests, or even what language the server code is written in. Although GWT provides a library of classes that makes the RPC
communications with a J2EE server extremely easy, you are not required to use them. Instead you can build custom HTTP requests to retrieve, for example, JSON or XML-formatted
data.

To communicate with your server from the browser without using GWT RPC:

1.  Create a connection to the server, using the browser's XMLHTTPRequest feature.
2.  Construct your payload according to whatever protocol you wish to use, convert it to a string, and send it to the server over the connection.
3.  Receive the server's response payload, and parse it according to the protocol.

You must use an asynchronous server connection.

### The com.google.gwt.http.client.html package<a id="The_com.google.gwt.http.client.html_package"></a>

GWT provides a library to largely (though not completely) automate the steps necessary to communicate with the server via HTTP requests. GWT application developers should use
the [RequestBuilder](/javadoc/latest/com/google/gwt/http/client/RequestBuilder.html) class and other classes in
the `com.google.gwt.http.client.html` package, which contains a clean asynchronous server request callback implementation.

**Note:** 
New users frequently notice the `com.google.gwt.user.client.HTTPRequest` class and attempt to use it.
However, the HTTPRequest class is deprecated and will be removed at some point in the future.
You should definitely use `RequestBuilder` instead.

### Troubleshooting<a id="Troubleshooting"></a>

If you are attempting to use the RequestBuilder class and are having problems, first check your module XML file to make sure that the HTTP module is inherited, as follows:

```xml
<inherits name="com.google.gwt.http.HTTP"/>
```

### Learn more<a id="Learn_more"></a>

*   To learn more about alternatives to GWT RPC, see the Developer's Guide, [Making HTTP Requests](DevGuideServerCommunication.html#DevGuideHttpRequests).
*   For a working example of making HTTP requests to retrieve JSON-formatted data, see the Tutorial, [Get JSON via HTTP](tutorial/JSON.html).
*   For documentation of the classes and methods you can use to build HTTP requests, see the API Reference, the [com.google.gwt.http.client](/javadoc/latest/com/google/gwt/http/client/package-summary.html) package.

## Does GWT RPC support java.io.Serializable?<a id="Does_the_GWT_RPC_system_support_the_use_of_java.io.Serializable"></a>

The GWT RPC system does support the use of `java.io.Serializable`, however only under a certain condition.

Previous to 1.4, the GWT RPC mechanism used an "IsSerializable" marker interface for denoting classes that can be serialized. Many users expressed the wish to reuse code with
GWT that they have already written that used the standard `java.io.Serializable` marker interface. Since both interfaces are empty marker interfaces, there is no technical
reason why GWT's RPC mechanism could not use the standard `java.io.Serializable`. However, there are good reasons to choose not to do so:

*   The semantics of GWT's serialization are much less sophisticated than standard Java serialization, and so to use `java.io.Serializable` as the marker interface would imply that GWT's serialization system is capable of more than it actually is.
*   Conversely, GWT's serialization mechanism is simpler than standard Java's, and so to use `java.io.Serializable` would imply that users have more to worry about (such as serialization version IDs) than they actually do.
*   GWT implements only a subset of the full Java JRE classes, and specifically implements nothing in `java.io`. To use `java.io.Serializable` as the GWT RPC serialization marker interface dilutes the message that `java.io` is not usable within a GWT application.

While each of the points above still hold, the GWT Team felt that the community was generally aware of these issues but preferred the convenience of being able to use the
standard `java.io.Serializable` interface rather than to have their classes implement the `isSerializable` marker interface, although both marker interfaces to
denote serializable classes are supported in GWT 1.4 and later. Considering this, the GWT Team made changes to the GWT RPC system to support the use of `java.io.Serializable` for
data transfer objects (commonly referred to as DTOs) that would be transferred over the wire. However, there is one condition to enable support for `java.io.Serializable`
in the new GWT RPC system.

RPC now generates a serialization policy file during GWT compilation. The serialization policy file contains a whitelist of allowed types which may be serialized. Its name is a
strong hash name followed by .gwt.rpc. In order to enable support for `java.io.Serializable`, the types that your application will send over the wire must be included in
the serialization policy whitelist. Also, the serialization policy file must be deployed to your web server as a public resource, accessible from a `RemoteServiceServlet`
via `ServletContext.getResource()`. If it is not deployed properly, RPC will run in 1.3.3 compatibility mode and refuse to serialize types implementing
`java.io.Serializable`.

Another important point to note is that none of the classes that implement `java.io.Serializable` in the full Java JRE implement `java.io.Serializable` in GWT's
emulated JRE. What this means is that types that implement `java.io.Serializable` in the JRE like `Throwable`, or `StackTraceElement` won't be able to
transfer across the wire through GWT RPC since the client won't be able to serialize/deserialize them. However, this isn't an issue for other types like `String`,
`Number`, etc... that don't implement `java.io.Serializable` in the emulated JRE but have custom field serializers so that they can be properly serialized.

These differences between types that implement `java.io.Serializable` in the real and emulated JREs may be reconciled in a future version of GWT, but for the time this
is an important point to keep in mind if your application uses GWT RPC.

## How can I dynamically fetch JSON feeds from other web domains?<a id="How_can_I_dynamically_fetch_JSON_feeds_from_other_web_domains?"></a>

Like all AJAX tools, GWT's HTTP client and RPC libraries are restricted to only accessing data from 
the same site where your application was loaded, due to the browser Same
Origin Policy. If you are using JSON, you can work around this limitation using a
`<script>` tag (aka JSON-P).

First, you need an external JSON service which can invoke user defined callback functions with 
the JSON data as argument. An example of such a service is [GData's "alt=json-in-script&amp;
callback=myCallback" support](https://developers.google.com/gdata/docs/json). Then, you can 
use [JsonpRequestBuilder](/javadoc/latest/com/google/gwt/jsonp/client/JsonpRequestBuilder.html) 
to make your call, in a way similar to a `RequestBuilder` when you're not making a cross-site 
request.
