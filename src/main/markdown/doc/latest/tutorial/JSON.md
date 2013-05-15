
<style>
code, .code {font-size: 9pt; font-family: Courier, Courier New, monospace; color:#007000;}
.highlight {background-color: #ffc;}
.strike {text-decoration:line-through; color:red;}
.header {margin-top: 1.5ex;}
.details {margin-top: 1ex;}
</style>

<p>
At this point, you've created the initial implementation of the StockWatcher application, simulating stock data in the client-side code.
</p>
<p>
On this page, you'll make a call to your local server to retrieve JSON-formatted stock data instead:</p>
<ol>
    <li><a href="#server">Creating a source of JSON data on your local server</a></li>
    <li><a href="#client">Manipulating JSON data int he client-side code</a></li>
    <li><a href="#http">Making HTTP requests to retrieve data from the server</a></li>
    <li><a href="#errors">Handling GET errors</a></li>
</ol>
<p>
Additional information follows:
</p>
<ol style="list-style: none;">
    <li><a href="#more">More about JSON, JSNI, Overlay types, and HTTP</a></li>
</ol>

<p class="note" style="margin-left: 1.2em; margin-right: 1.5em;">
<b>Note:</b> For a broader guide to client-server communication in a GWT application, see <a href="../DevGuideServerCommunication.html">Communicate with a Server</a>.
</p>

<h3>What is JSON?</h3>
<p>
JSON is a universal, language-independent format for data. In this way, it's similar to XML. Whereas XML uses tags, JSON is based on the object-literal notation of JavaScript. Therefore the format is simpler than XML. In general, JSON-encoded data is less verbose than the equivalent data in XML and so JSON data downloads more quickly than XML data.
</p>
<p>
When you encode the stock data for StockWatcher in JSON format, it will look something like this (but the whitespace will be stripped out).
</p>

<pre class="code">
[
  {
    "symbol": "ABC",
    "price": 87.86,
    "change": -0.41
  },
  {
    "symbol": "DEF",
    "price": 62.79,
    "change": 0.49
  },
  {
    "symbol": "GHI",
    "price": 67.64,
    "change": 0.05
  }
]
</pre>


<a name="server"></a>
<h2>1. Creating a source of JSON data on your local server</h2>

<h3>Reviewing the existing implementation</h3>
<p>
In the original StockWatcher implementation, you created a StockPrice class and used the refreshWatchList method to generate random stock data and then call the updateTable method to populate StockWatcher's flex table.
</p>


<pre class="code">
  /**
   * Generate random stock prices.
   */
  private void refreshWatchList() {
    final double MAX_PRICE = 100.0; // $100.00
    final double MAX_PRICE_CHANGE = 0.02; // +/- 2%

    StockPrice[] prices = new StockPrice[stocks.size()];
    for (int i = 0; i &lt; stocks.size(); i++) {
      double price = Random.nextDouble() * MAX_PRICE;
      double change = price * MAX_PRICE_CHANGE
          * (Random.nextDouble() * 2.0 - 1.0);

      prices[i] = new StockPrice(stocks.get(i), price, change);
    }

    updateTable(prices);
  }
</pre>


<p>
In this tutorial, you'll create a servlet to generate the stock data in JSON format. Then you'll make an HTTP call to retrieve the JSON data from the server. You'll use JSNI and GWT overlay types to work with the JSON data while writing the client-side code.
</p>
<h3>Writing the servlet</h3>
<p>
To serve up hypothetical stock quotes in JSON format, you'll create a servlet. To use the embedded servlet container (Jetty) to serve the data, add the JsonStockData class to the server directory of your StockWatcher project and reference the servlet in the web application deployment descriptor (web.xml).
</p>
<p>
<b>Note</b>: If you have a web server (Apache, IIS, etc) installed locally and PHP installed, you could instead <a href="JSONphp.html">write a PHP script to generate the stock data</a> and make the call to your local server. What's important for this example is that the stock data is JSON-encoded and that the server is local.
</p>
<ol class="instructions">
    <li>
        <div class="header">Create a servlet.</div>
        <div class="details">In the Package Explorer, select the client package: <br /><code>com.google.gwt.sample.stockwatcher.client</code>
        <div class="details">In Eclipse, open the New Java Class wizard (File &gt; New &gt; Class).</div>
    </li>
    <li>
        <div class="header">At Package, change the name from <code>.client</code> to <code>.server</code></div>
        <div class="details">At name, enter <code>JsonStockData</code>.</div>
        <div class="details">Eclipse will create a package for the server-side code and a stub for the JsonStockData class.</div>
    </li>
    <li>
        <div class="header">Replace the stub with the following code.</div>
        <div class="details">

<pre class="code">
package com.google.gwt.sample.stockwatcher.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JsonStockData extends HttpServlet {

  private static final double MAX_PRICE = 100.0; // $100.00
  private static final double MAX_PRICE_CHANGE = 0.02; // +/- 2%

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    Random rnd = new Random();

    PrintWriter out = resp.getWriter();
    out.println('[');
    String[] stockSymbols = req.getParameter("q").split(" ");
    for (String stockSymbol : stockSymbols) {

      double price = rnd.nextDouble() * MAX_PRICE;
      double change = price * MAX_PRICE_CHANGE * (rnd.nextDouble() * 2f - 1f);

      out.println("  {");
      out.print("    \"symbol\": \"");
      out.print(stockSymbol);
      out.println("\",");
      out.print("    \"price\": ");
      out.print(price);
      out.println(',');
      out.print("    \"change\": ");
      out.println(change);
      out.println("  },");
    }
    out.println(']');
    out.flush();
  }

}
</pre>

</div>
    </li>
</ol>

<h3>Including the server-side code in the GWT module</h3>
<p>
The embedded servlet container (Jetty) can host the servlet that generates the stock data in JSON format.
</p>
<p>
To set this up, add &lt;servlet&gt; and &lt;servlet-mapping&gt; elements to the web application deployment descriptor (web.xml) and point to JsonStockData.
</p>
<p class="note">
Starting with GWT 1.6, servlets should be defined in the web application deployment descriptor (web.xml) instead of the GWT module (StockWatcher.gwt.xml).
</p>
<p>
In the &lt;servlet-mapping&gt; element, the url-pattern can be in the form of an absolute directory path (for example, <code>/spellcheck</code> or <code>/common/login</code>).
If you specify a default service path with a @RemoteServiceRelativePath annotation on the service interface (as you did with StockPriceService), then make sure the path attribute matches the annotation value.

Because you've mapped the StockPriceService to "stockPrices" and the module rename-to attribute in StockWatcher.gwt.xml is "stockwatcher", the full URL will be: <br />
<code>http://localhost:8888/stockwatcher/stockPrices</code>
</p>
<ol class="instructions">
    <li>
        <div class="header">Edit the web application deployment descriptor (StockWatcher/war/WEB-INF/web.xml).</div>
        <div class="details">Since the greetServlet is no longer needed, its definition can be removed.<pre class="code">
&lt;?xml version="1.0" encoding="UTF-8"?&gt;
&lt;!DOCTYPE web-app
    PUBLIC "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN"
    "http://java.sun.com/dtd/web-app_2_3.dtd"&gt;

&lt;web-app&gt;

  &lt;!-- Default page to serve --&gt;
  &lt;welcome-file-list&gt;
    &lt;welcome-file&gt;StockWatcher.html&lt;/welcome-file&gt;
  &lt;/welcome-file-list&gt;

  &lt;!-- Servlets --&gt;
<span class="highlight"> &lt;servlet&gt;
    &lt;servlet-name&gt;jsonStockData&lt;/servlet-name&gt;
    &lt;servlet-class&gt;com.google.gwt.sample.stockwatcher.server.JsonStockData&lt;/servlet-class&gt;
  &lt;/servlet&gt;

  &lt;servlet-mapping&gt;
    &lt;servlet-name&gt;jsonStockData&lt;/servlet-name&gt;
    &lt;url-pattern&gt;/stockwatcher/stockPrices&lt;/url-pattern&gt;
  &lt;/servlet-mapping&gt;</span>

&lt;/web-app&gt;
</pre></div>
    </li>
</ol>

<h3>Testing your ability to retrieve JSON data from the server</h3>
<ol class="instructions">
    <li>
        <div class="header">Debug StockWatcher in development mode.</div>
        <div class="details">At this point, the stock data is still coming from the client-side code.</div>
    </li>
    <li>
        <div class="header">Test the stock quote server.</div>
        <div class="details">Ensure that the development mode code server is running and pass stock codes to the servlet URL<br /><code>http://localhost:8888/stockwatcher/stockPrices?q=ABC+DEF</code></div>
    </li>
    <li>
        <div class="header">The servlet generates an array of simulated stock data encoded in JSON format.</div>
        <div class="details"><img src="images/JSONdata.png" alt="JSON formatted stock data" /></div>
    </li>
</ol>

<a name="client"></a>
<h2>2. Manipulating JSON data in the client-side code</h2>

<h3>Overview</h3>
<p>
At this point, you've verified that you are able to get JSON data from a server.
Later in this section, you'll code the HTTP GET request to the server.
First, focus on working with the JSON-encoded text that's returned to the client-side code.
Two techiques you'll use are JSNI (JavaScript Native Interface) and GWT overlay types.
</p>
<p>
This is the JSON data coming back from the server.
</p>

<pre class="code">[
  {
    "symbol": "ABC",
    "price": 47.65563005127077,
    "change": -0.4426563818062567
  },
]</pre>

<p>
First, you'll use a JavaScript eval() function to convert the JSON string into JavaScript objects.
</p>
<pre class="code">
  private final native JsArray&lt;StockData&gt; asArrayOfStockData(String json) /*-{
    return eval(json);
  }-*/;</pre>
<p>
Then, you'll be able to write methods to access those objects.
</p>
<pre class="code">
  // JSNI methods to get stock data.
  public final native String getSymbol() /*-{ return this.symbol; }-*/;
  public final native double getPrice() /*-{ return this.price; }-*/;
  public final native double getChange() /*-{ return this.change; }-*/;
</pre>
<p>
In both cases, you'll use JSNI. When the client-side code is compiled to JavaScript, the Java methods are replaced with the JavaScript exactly as you write it inside the tokens.
</p>

<h4>Coding with JSNI</h4>
<p>
As you see in the examples above, using JSNI you can call handwritten (as opposed to GWT-generated) JavaScript methods from within the GWT module.
</p>
<p>
JSNI methods are declared native and contain JavaScript code in a specially formatted comment block between the end of the parameter list and the trailing semicolon.
A JSNI comment block begins with the exact token /*-{ and ends with the exact token }-*/.
JSNI methods are called just like any normal Java method.
They can be static or instance methods.
</p>
<p class="note">
<b>In Depth:</b> For tips, tricks, and caveats about mixing handwritten JavaScript into your Java source code, see the Developer's Guide, <a href="../DevGuideCodingBasics.html#DevGuideJavaScriptNativeInterface">JavaScript Native Interface (JSNI)</a>.
</p>

<h3>Converting JSON into JavaScript objects</h3>
<p>
First you need to convert the JSON text from the server into JavaScript objects.
The simplest and fastest way to do this is by using JavaScript's built-in eval() function, which can correctly parse valid JSON text and produce a corresponding object.
</p>
<p>
However, because eval() can execute any JavaScript code (not just JSON data) this approach has some serious security implications.
Make sure the servers you interact with are <strong>absolutely trustworthy</strong>, because they will have the ability to execute arbitrary JavaScript code within your application. In this example, since you are using the servlet container to access data on your own machine, this is not an issue.
</p>
<ol class="instructions">
    <li>
        <div class="header">Convert the string into a JavaScript array.</div>
        <div class="details">In the StockWatcher class, add the following method.</div>
        <div class="details"><pre class="code">
<span class="highlight">  /**
   * Convert the string of JSON into JavaScript object.
   */
  private final native JsArray&lt;StockData&gt; asArrayOfStockData(String json) /*-{
    return eval(json);
  }-*/;</span>
</pre></div>
        <div class="details">Eclipse flags JsArray and StockData. StockData is the overlay type you will use to replace the StockPrice class.</div>
    </li>
    <li>
        <div class="header">Declare the import.</div>
        <div class="details"><pre class="code">
<span class="highlight">import com.google.gwt.core.client.JsArray;</span></pre></div>
    </li>
    <li>
        <div class="header">Create a stub for the StockData class in the client package.</div>
        <div class="details">Ignore any remaining compile errors in the StockWatcher class; these will be resolved after you code the StockData class.
    </li>
</ol>

<h4>JSON data types</h4>
<p>
As you might expect, JSON data types correspond to the built-in types of JavaScript.
JSON can encode strings, numbers, booleans, and null values, as well as objects and arrays composed of those types.
As in JavaScript, an object is actually just an unordered set of name/value pairs.
In JSON objects, however, the values can only be other JSON types (never functions containing executable JavaScript code).
</p>
<p class="note">
Another technique for a converting a JSON string into something you can work with is to use the static <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/json/client/JSONParser.html">JSONParser.parse(String)</a> method. GWT contains a full set of JSON types for manipulating JSON data in the com.google.gwt.json.client package. If you prefer to parse the JSON data, see the Developer's Guide, <a href="/web-toolkit/doc/{{ release_path }}/DevGuideCodingBasics#DevGuideJSON">Working with JSON</a>. Ultimately both techniques rely on the JavaScript eval() function; so you are still responsible for ensuring that you are using a trusted source of JSON data.
</p>

<h3>Creating an overlay type</h3>
<p>
Your next task is to replace the existing StockPrice type with the StockData type.
</p>
<p>
Not only do you want to be access the array of JSON objects, but you want to be able to work with them as if they were Java objects while you're coding. GWT <i>overlay types</i> let you do this.
</p>
<p>
The new StockData class will be an overlay type which overlays the existing JavaScript array.
</p>
<ol class="instructions">
    <li>
        <div class="header">Replace the stub with the following code.</div>
        <p class="note"><b>Note:</b> The commented numbers refer to the implementation notes below. You can delete them.</p>
        <div class="details"><pre class="code">
package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.core.client.JavaScriptObject;

class StockData extends JavaScriptObject {                              // <span style="color:black;"><b>(1)</b></span>
  // Overlay types always have protected, zero argument constructors.
  protected StockData() {}                                              // <span style="color:black;"><b>(2)</b></span>

  // JSNI methods to get stock data.
  public final native String getSymbol() /*-{ return this.symbol; }-*/; // <span style="color:black;"><b>(3)</b></span>
  public final native double getPrice() /*-{ return this.price; }-*/;
  public final native double getChange() /*-{ return this.change; }-*/;

  // Non-JSNI method to return change percentage.                       // <span style="color:black;"><b>(4)</b></span>
  public final double getChangePercent() {
    return 100.0 * getChange() / getPrice();
  }
}
</pre></div>
    </li>
</ol>
<h4>Implementation Notes</h4>

<p>
(1) StockData is a subclass of JavaScriptObject, a marker type that GWT uses to denote JavaScript objects.<br />
JavaScriptObject gets special treatment from the GWT compiler and development mode code server.
Its purpose is to provide an opaque representation of native JavaScript objects to Java code.
</p>
<p>
(2) Overlay types always have protected, zero-argument constructors.
</p>
<p>
(3) Typically methods on overlay types are JSNI.<br />
These getters directly access the JSON fields you know exist.<br />
By design, all methods on overlay types are final and private; thus every method is statically resolvable by the compiler, so there is no need for dynamic dispatch at runtime.
</p>
<p>
(4) However, methods on overlay types are not required to be JSNI.<br />
Just as you did in the StockPrice class, you calculate the change percentage based on the price and change values.
</p>

<h4>Benefits of using overlay types</h4>
<p>
Using an overlay type creates a normal looking Java type that you can interact with using code completion, refactoring, and compile-time checking. Yet, you also have the flexibility of interacting with arbitrary JavaScript objects, which makes it simpler to access JSON services using RequestBuilder (which you'll do in the next section).
</p>
<p>
GWT now understands that any instance of StockData is a true JavaScript object that comes from outside this GWT module.
You can interact with it exactly as it exists in JavaScript.
In this example, you can access directly the JSON fields you know exist: this.Price and this.Change.
</p>
<p>
Because the methods on overlay types can be statically resolved by the GWT compiler, they are candidates for automatic inlining. Inlined code runs significantly faster. This makes it possible for the GWT compiler to create highly-optimized JavaScript for your application's client-side code.
</p>

<a name="http"></a>
<h2>3. Making HTTP requests to retrieve data from the server</h2>
<p>
Now that you have the mechanism for working with the JSON data in place, you'll write the HTTP request that gets the data from the server.
</p>
<p>
In this example, you're going to replace the current refreshWatchList method with a new implementation that uses HTTP.
</p>
<h3>Specifying the URL</h3>
<p>
First, specify the URL where the servlet lives, that is:<br /><code>http://localhost:8888/stockwatcher/stockPrices?q=ABC+DFD</code><br />
<b>Note:</b> If you are doing the php example, substitute the corresponding URL.
</p>
<p>
Then, append the stock codes in the watch list to the base module URL.
Rather than hardcoding the URL for the JSON server, add a constant to the StockWatcher class.
</p>
<ol>
    <li>
        <div class="header">Add a constant to the StockWatcher class that specifies the URL of the JSON data.</div>
        <div class="details"><b>Note:</b> Earlier you specified a path to /stockPrices in the module XML file.</div>
        <div class="details"><pre class="code">
  private static final String JSON_URL = GWT.getModuleBaseURL() + "stockPrices?q=";</pre>
        </div>
        <div class="details">Eclipse flags GWT.</div>
        </li>
    <li>
        <div class="header">Include the import declarations.</div>
        <div class="details"><pre class="code">
import com.google.gwt.core.client.GWT;</pre></div>
    </li>
    <li>
        <div class="header">Append the stock codes to the query URL and encode it.</div>
        <div class="details">Replace the existing refreshWatchList method with the following code.</div>
        <div class="details"><pre class="code">
  private void refreshWatchList() {
<span class="highlight">    if (stocks.size() == 0) {
      return;
    }

    String url = JSON_URL;

    // Append watch list stock symbols to query URL.
    Iterator<String> iter = stocks.iterator();
    while (iter.hasNext()) {
      url += iter.next();
      if (iter.hasNext()) {
        url += "+";
      }
    }

    url = URL.encode(url);

    // TODO Send request to server and handle errors.</span>

  }</div>
   <div class="details">Eclipse flags Iterator and URL.</div>
    </li>
    <li>
        <div class="header">Include the import declarations.</div>
        <div class="details"><pre class="code">
import com.google.gwt.http.client.URL;

import java.util.Iterator;</pre></div>
    </li>
</ol>
<h3>Asynchronous HTTP</h3>
<p>
To get the JSON text from the server, you'll use the HTTP client classes in the com.google.gwt.http.client package. These classes contain the functionality for making asynchronous HTTP requests.
</p>
<p>
The HTTP types are contained within separate GWT modules that StockWatcher needs to inherit.
</p>
<ol class="instructions">
    <li>
        <div class="header">To inherit other GWT modules, edit the module XML file.</div>
        <div class="details">In StockWatcher.gwt.xml, add &lt;inherits&gt; tags and specify the HTTP module.</div>
        <div class="details"><pre class="code">
  &lt;!-- Other module inherits --&gt;
  &lt;inherits name="com.google.gwt.http.HTTP" /&gt;
        </pre></div>
    </li>
    <li>
        <div class="header"></div>
        <div class="details">Open StockWatcher.java and include the following import declarations. Declare the imports for the following Java types.</div>
        <div class="details"><pre class="code">
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;</pre></div>
    </li>
</ol>

<h3>Building a custom HTTP request</h3>
<p>
To send a request, you'll create an instance of the RequestBuilder object.
You specify the HTTP method (GET, POST, etc.) and URL in the constructor.
If necessary, you can also set the username, password, timeout, and headers to be used in the HTTP request. In this example, you don't need to do this.
</p>
<p>
When you're ready to make the request, you call sendRequest(String, RequestCallback).
</p>
<p>
The RequestCallback argument you pass will handle the response in its onResponseReceived(Request, Response) method, which is called when and if the HTTP call completes successfully.
If the call fails (for example, if the HTTP server is not responding), the onError(Request, Throwable) method is called instead.
The RequestCallback interface is analogous to the AsyncCallback interface in GWT remote procedure calls.
</p>
<ol class="instructions">
    <li>
        <div class="header">Make the HTTP request and parse the JSON response.</div>
        <div class="details">In the refreshWatchList method, replace the TODO comments with the following code  .</div>

        <div class="details"><pre class="code">
    // Send request to server and catch any errors.
    RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);

    try {
      Request request = builder.sendRequest(null, new RequestCallback() {
        public void onError(Request request, Throwable exception) {
          displayError("Couldn't retrieve JSON");
        }

        public void onResponseReceived(Request request, Response response) {
          if (200 == response.getStatusCode()) {
            updateTable(asArrayOfStockData(response.getText()));
          } else {
            displayError("Couldn't retrieve JSON (" + response.getStatusText()
                + ")");
          }
        }
      });
    } catch (RequestException e) {
      displayError("Couldn't retrieve JSON");
    }</pre></div>
    </li>
    <li>
     <div class="header">You receive two compile errors which you will resolve in a minute.</div>
    </li>
</ol>

<h4>Modify the updateTable method</h4>
<p>
To fix the compile errors, modify the updateTable method.
</p>
<p

<ul>
    <li>Change: StockPrice[] prices<br /> To: JsArray&lt;StockData&gt; prices</li>
    <li>Change: prices.length<br /> To: prices.length()</li>
    <li>Change: prices[i]<br /> To: prices.get(i)</li>
</ul>

<ol class="instructions">
    <li>
        <div class="header"></div>
        <div class="details">Replace the existing update updateTable(StockPrice[]) method with the following code.</div>
        <div class="details"><pre class="code">
  /**
   * Update the Price and Change fields for all rows in the stock table.
   *
   * @param prices Stock data for all rows.
   */
  private void updateTable(<span class="highlight">JsArray&lt;StockData&gt;</span> prices) {
    for (int i = 0; i &lt; <span class="highlight">prices.length()</span>; i++) {
      updateTable(<span class="highlight">prices.get(i)</span>);
    }

    // Display timestamp showing last refresh.
    lastUpdatedLabel.setText("Last update : "
        + DateTimeFormat.getMediumDateTimeFormat().format(new Date()));
  }</pre></div>
    </li>
    <li>
        <div class="header">Make a corresponding change in the updateTable(StockPrice price) method.</div>
        <div class="details">Change the reference to the StockPrice class to StockData class.</div>
        <div class="details"><pre class="code">
  private void updateTable(<span class="highlight">StockData</span> price) {
    // Make sure the stock is still in the stock table.
    if (!stocks.contains(price.getSymbol())) {
      return;
    }

    ...

  }
</pre></div>
    </li>
</ol>

<a name="errors"></a>
<h2>4. Handling GET errors</h2>
<p>
If something breaks along the way (for example, if the server is offline, or the JSON is malformed), you'll trap the error and display a message to the user. To do this you'll create a Label widget and write a new method, displayError(String).
</p>
<ol class="instructions">
    <li>
        <div class="header">Clear the compiler error by creating a stub method in the StockWatcher class.</div>
        <div class="details">Set the text for the error message and make the Label widget visible.</div>
        <div class="details"><pre class="code">
  /**
   * If can't get JSON, display error message.
   * @param error
   */
  private void displayError(String error) {
    errorMsgLabel.setText("Error: " + error);
    errorMsgLabel.setVisible(true);
  }
</pre></div>
    </li>
    <li>
      <div class="header">Eclipse flags errorMsgLabel.</div>
      <div class="details">Ignore the compile errors for a moment; you'll implement a Label widget to display the error text in the next step.</div>
    </li>
    <li>
        <div class="header">If the error is corrected, hide the Label widget.</div>
        <div class="details">In the updateTable(JsArray&lt;StockData&gt; prices) method, clear any error messages.</div>
        <div class="details"><pre class="code">
  private void updateTable(JsArray&lt;StockData&gt; prices) {
    for (int i=0; i &lt; prices.length; i++) {
      updateTable(prices[i]);
    }

    // Display timestamp showing last refresh.
    lastUpdatedLabel.setText("Last update : " +
        DateTimeFormat.getMediumDateTimeFormat().format(new Date()));

<span class="highlight">    // Clear any errors.
    errorMsgLabel.setVisible(false);</span>
  }
</pre>

</div>
    </li>
</ol>
<h4>Displaying error messages</h4>
<p>
In order to display the error, you will need a new UI component; you'll implement a Label widget.
</p>
<ol class="instructions">
    <li>
        <div class="header">Define a style for the error message so that it will attract the user's attention.</div>
        <div class="details">In StockWatcher.css, create a style rule that applies to any element with a class attribute of errorMessage.</div>
        <div class="details"><pre class="code">
.negativeChange {
  color: red;
}

<span class="highlight">.errorMessage {
  color: red;
}</span></pre></div>
    </li>
    <li>
        <div class="header">To hold the text of the error message, add a Label widget.</div>
        <div class="details">In StockWatcher.java, add the following instance field.</div>
        <div class="details"><pre class="code">
  private ArrayList&lt;String&gt; stocks = new ArrayList&lt;String&gt;();
<span class="highlight">  private Label errorMsgLabel = new Label();</span></pre></div>
    </li>
    <li>
        <div class="header">Initialize the errorMsgLabel when StockWatcher launches.</div>
        <div class="details">In the onModuleLoad method, add a secondary class attribute to errorMsgLabel and do not display it when StockWatcher loads.</div>
        <div class="details">Add the error message to the Main panel above the stocksFlexTable.</div>

        <div class="details"><pre class="code">
    // Assemble Add Stock panel.
    addPanel.add(newSymbolTextBox);
    addPanel.add(addButton);
    addPanel.addStyleName("addPanel");

    // Assemble Main panel.
<span class="highlight">    errorMsgLabel.setStyleName("errorMessage");
    errorMsgLabel.setVisible(false);

    mainPanel.add(errorMsgLabel);</span>
    mainPanel.add(stocksFlexTable);
    mainPanel.add(addPanel);
    mainPanel.add(lastUpdatedLabel);
</pre></div>
    </li>
</ol>

<h3>Test the HTTP request and error handling</h3>

<ol class="instructions">
    <li>
        <div class="header">Refresh StockWatcher in development mode.</div>
    </li>
    <li>
        <div class="header">Enter several stock codes.</div>
        <div class="client">StockWatcher should display Price and Change data for each stock. Rather than being generated from within the program the stock data is coming from you local server in JSON format.</div>
    </li>
    <li>
        <div class="header">Test the HTTP error messages by calling an invalid URL.</div>
        <div class="details">In StockWatcher,java, edit the URL.</div>
        <div class="details">
Change: <code>private static final String JSON_URL = GWT.getModuleBaseURL()
      + "stockPrices?q=";</code><br />
      To: <code>private static final String JSON_URL = GWT.getModuleBaseURL()
      + "BADURL?q=";</code></div>
    </li>
    <li>
        <div class="header">Refresh StockWatcher in development mode.</div>
    </li>
    <li>
        <div class="header">Enter a stock code.</div>
        <div class="details">StockWatcher displays a red error message. Valid stock code data stops being refreshed.</div>
        <div class="details"><img src="images/JSONerrormessage.png" alt="screenshot: JSON error message" /></div>
    </li>
    <li>
        <div class="header">In StockWatcher,java, correct the URL.</div>
    </li>
    <li>
        <div class="header">Refresh StockWatcher in development mode.</div>
        <div class="details">Enter several stock codes.</div>
        <div class="details">StockWatcher should display Price and Change data for each stock again.</div>
    </li>
</ol>

<h2 id="more">More about JSON, JSNI, Overlay types, and HTTP</h2>
<p>
At this point you've retrieved JSON-encoded stock data from a local server and used it to update the Price and Change fields for the stocks in your watch list. If you'd like to see how to retrieve JSON from web server on another domain, see <a href="/web-toolkit/doc/latest/tutorial/Xsite">Making cross-site requests</a>.
</p>
<p>
To learn more about client-server communication, see the Developer's Guide, Communicating with the Server. <br />Topics include:
</p>
<ul>
    <li><a href="../DevGuideServerCommunication.html#DevGuideHttpRequests">Making HTTP Requests</a></li>
    <li><a href="../DevGuideCodingBasics.html#DevGuideJSON">Working with JSON</a></li>
</ul>

<p>
To learn more about JSNI, see the Developer's Guide, <a href="../DevGuideCodingBasics.html#DevGuideJavaScriptNativeInterface">JavaScript Native Interface (JSNI)</a>. <br />Topics include:
</p>
<ul>
    <li>Writing Native JavaScript Method</li>
    <li>Accessing Java Methods and Fields from JavaScript</li>
    <li>Sharing objects between Java source and JavaScript</li>
    <li>Exceptions and JSNI</li>
    <li>JavaScript Overlay Types</li>
</ul>


