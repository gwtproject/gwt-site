Retrieving JSON
===

At this point, you've created the initial implementation of the StockWatcher application, simulating stock data in the client-side code.

On this page, you'll make a call to your local server to retrieve JSON-formatted stock data instead:

1.  [Creating a source of JSON data on your local server](#server)
2.  [Manipulating JSON data in the client-side code](#client)
3.  [Making HTTP requests to retrieve data from the server](#http)
4.  [Handling GET errors](#errors)

Additional information follows:

1.  [More about JSON, JSNI, Overlay types, and HTTP](#more)

**Note:** For a broader guide to client-server communication in a GWT application, see [Communicate with a Server](../DevGuideServerCommunication.html).

### What is JSON?

JSON is a universal, language-independent format for data. In this way, it's similar to XML. Whereas XML uses tags, JSON is based on the object-literal notation of JavaScript. Therefore the format is simpler than XML. In general, JSON-encoded data is less verbose than the equivalent data in XML and so JSON data downloads more quickly than XML data.

When you encode the stock data for StockWatcher in JSON format, it will look something like this (but the whitespace will be stripped out).

```json
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
```

##  Creating a source of JSON data on your local server <a id="server"></a>

### Reviewing the existing implementation

In the original StockWatcher implementation, you created a StockPrice class and used the refreshWatchList method to generate random stock data and then call the updateTable method to populate StockWatcher's flex table.

```java
  /**
   * Generate random stock prices.
   */
  private void refreshWatchList() {
    final double MAX_PRICE = 100.0; // $100.00
    final double MAX_PRICE_CHANGE = 0.02; // +/- 2%

    StockPrice[] prices = new StockPrice[stocks.size()];
    for (int i = 0; i < stocks.size(); i++) {
      double price = Random.nextDouble() * MAX_PRICE;
      double change = price * MAX_PRICE_CHANGE
          * (Random.nextDouble() * 2.0 - 1.0);

      prices[i] = new StockPrice(stocks.get(i), price, change);
    }

    updateTable(prices);
  }
```

In this tutorial, you'll create a servlet to generate the stock data in JSON format. Then you'll make an HTTP call to retrieve the JSON data from the server. You'll use JSNI and GWT overlay types to work with the JSON data while writing the client-side code.

### Writing the servlet

To serve up hypothetical stock quotes in JSON format, you'll create a servlet. To use the embedded servlet container (Jetty) to serve the data, add the JsonStockData class to the server directory of your StockWatcher project and reference the servlet in the web application deployment descriptor (web.xml).

**Note**: If you have a web server (Apache, IIS, etc) installed locally and PHP installed, you could instead [write a PHP script to generate the stock data](JSONphp.html) and make the call to your local server. What's important for this example is that the stock data is JSON-encoded and that the server is local.

1.  Create a servlet.
    *  In the Package Explorer, select the client package:`com.google.gwt.sample.stockwatcher.client`
    *  In Eclipse, open the New Java Class wizard (File > New > Class).
2.  At Package, change the name from `.client` to `.server`
    *  At name, enter `JsonStockData`.
    *  Eclipse will create a package for the server-side code and a stub for the JsonStockData class.
3.  Replace the stub with the following code.

```java
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
    boolean firstSymbol = true;
    for (String stockSymbol : stockSymbols) {

      double price = rnd.nextDouble() * MAX_PRICE;
      double change = price * MAX_PRICE_CHANGE * (rnd.nextDouble() * 2f - 1f);

      if (firstSymbol) {
        firstSymbol = false;
      } else {
        out.println("  ,");
      }
      out.println("  {");
      out.print("    \"symbol\": \"");
      out.print(stockSymbol);
      out.println("\",");
      out.print("    \"price\": ");
      out.print(price);
      out.println(',');
      out.print("    \"change\": ");
      out.println(change);
      out.println("  }");
    }
    out.println(']');
    out.flush();
  }

}
```

### Including the server-side code in the GWT module

The embedded servlet container (Jetty) can host the servlet that generates the stock data in JSON format.

To set this up, add `<servlet>` and `<servlet-mapping>` elements to the web application deployment descriptor (web.xml) and point to JsonStockData.

Starting with GWT 1.6, servlets should be defined in the web application deployment descriptor (web.xml) instead of the GWT module (StockWatcher.gwt.xml).

In the `<servlet-mapping>` element, the url-pattern can be in the form of an absolute directory path (for example, `/spellcheck` or `/common/login`).
If you specify a default service path with a @RemoteServiceRelativePath annotation on the service interface (as you did with StockPriceService), then make sure the path attribute matches the annotation value.

Because you've mapped the StockPriceService to "stockPrices" and the module rename-to attribute in StockWatcher.gwt.xml is "stockwatcher", the full URL will be: 

`http://localhost:8888/stockwatcher/stockPrices`

1.  Edit the web application deployment descriptor (StockWatcher/war/WEB-INF/web.xml).
    *  Since the greetServlet is no longer needed, its definition can be removed.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE web-app
    PUBLIC "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN"
    "http://java.sun.com/dtd/web-app_2_3.dtd">

<web-app>

  <!-- Default page to serve -->
  <welcome-file-list>
    <welcome-file>StockWatcher.html</welcome-file>
  </welcome-file-list>

  <!-- Servlets -->
  <servlet>
    <servlet-name>jsonStockData</servlet-name>
    <servlet-class>com.google.gwt.sample.stockwatcher.server.JsonStockData</servlet-class>
  </servlet>

  <servlet-mapping>
    <servlet-name>jsonStockData</servlet-name>
    <url-pattern>/stockwatcher/stockPrices</url-pattern>
  </servlet-mapping>

</web-app>
```

### Testing your ability to retrieve JSON data from the server

1.  Debug StockWatcher in development mode.
    *  At this point, the stock data is still coming from the client-side code.
2.  Test the stock quote server.
    *  Ensure that the development mode code server is running and pass stock codes to the servlet URL
`http://localhost:8888/stockwatcher/stockPrices?q=ABC+DEF`
3.  The servlet generates an array of simulated stock data encoded in JSON format.
    *  ![JSON formatted stock data](images/JSONdata.png)

##  Manipulating JSON data in the client-side code <a id="client"></a>

### Overview

At this point, you've verified that you are able to get JSON data from a server.
Later in this section, you'll code the HTTP GET request to the server.
First, focus on working with the JSON-encoded text that's returned to the client-side code.
Two techniques you'll use are JSNI (JavaScript Native Interface) and GWT overlay types.

This is the JSON data coming back from the server.

```json
[
  {
    "symbol": "ABC",
    "price": 47.65563005127077,
    "change": -0.4426563818062567
  },
]
```

First, you'll use [JsonUtils.safeEval()](/javadoc/latest/com/google/gwt/core/client/JsonUtils.html) to convert the JSON string into JavaScript objects. Then, you'll be able to write methods to access those objects.

```java
  // JSNI methods to get stock data.
  public final native String getSymbol() /*-{ return this.symbol; }-*/;
  public final native double getPrice() /*-{ return this.price; }-*/;
  public final native double getChange() /*-{ return this.change; }-*/;
```

For the latter, you'll use JSNI. When the client-side code is compiled to JavaScript, the Java methods are replaced with the JavaScript exactly as you write it inside the tokens.

#### Coding with JSNI

As you see in the examples above, using JSNI you can call handwritten (as opposed to GWT-generated) JavaScript methods from within the GWT module.

JSNI methods are declared native and contain JavaScript code in a specially formatted comment block between the end of the parameter list and the trailing semicolon.
A JSNI comment block begins with the exact token /*-{ and ends with the exact token }-*/.
JSNI methods are called just like any normal Java method.
They can be static or instance methods.

**In Depth:** For tips, tricks, and caveats about mixing handwritten JavaScript into your Java source code, see the Developer's Guide, [JavaScript Native Interface (JSNI)](../DevGuideCodingBasics.html#DevGuideJavaScriptNativeInterface).

### Converting JSON into JavaScript objects

First you need to convert the JSON text from the server into JavaScript objects. This can be easily done using the static `JsonUtils.safeEval()` method. We'll see later how to use it.

#### JSON data types

As you might expect, JSON data types correspond to the built-in types of JavaScript.
JSON can encode strings, numbers, booleans, and null values, as well as objects and arrays composed of those types.
As in JavaScript, an object is actually just an unordered set of name/value pairs.
In JSON objects, however, the values can only be other JSON types (never functions containing executable JavaScript code).

Another technique for a converting a JSON string into something you can work with is to use the static [JSONParser.parse(String)](/javadoc/latest/com/google/gwt/json/client/JSONParser.html) method. GWT contains a full set of JSON types for manipulating JSON data in the com.google.gwt.json.client package. If you prefer to parse the JSON data, see the Developer's Guide, [Working with JSON](../DevGuideCodingBasics.html#DevGuideJSON).

### Creating an overlay type

Your next task is to replace the existing StockPrice type with a StockData type.

Not only do you want to access the array of JSON objects, but you want to be able to work with them as if they were Java objects while you're coding. GWT _overlay types_ let you do this.

The new StockData class will be an overlay type which overlays the existing JavaScript object.

1.  Create the StockData class.

    **Note:** The commented numbers refer to the implementation notes below. You can delete them.



```java
package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.core.client.JavaScriptObject;

class StockData extends JavaScriptObject {                              // (1)
  // Overlay types always have protected, zero argument constructors.
  protected StockData() {}                                              // (2)

  // JSNI methods to get stock data.
  public final native String getSymbol() /*-{ return this.symbol; }-*/; // (3)
  public final native double getPrice() /*-{ return this.price; }-*/;
  public final native double getChange() /*-{ return this.change; }-*/;

  // Non-JSNI method to return change percentage.                       // (4)
  public final double getChangePercent() {
    return 100.0 * getChange() / getPrice();
  }
}
```

#### Implementation Notes

(1) StockData is a subclass of JavaScriptObject, a marker type that GWT uses to denote JavaScript objects.

JavaScriptObject gets special treatment from the GWT compiler and development mode code server.
Its purpose is to provide an opaque representation of native JavaScript objects to Java code.

(2) Overlay types always have protected, zero-argument constructors.

(3) Typically methods on overlay types are JSNI.

These getters directly access the JSON fields you know exist.

By design, all methods on overlay types are final and private; thus every method is statically resolvable by the compiler, so there is no need for dynamic dispatch at runtime.

(4) However, methods on overlay types are not required to be JSNI.

Just as you did in the StockPrice class, you calculate the change percentage based on the price and change values.

#### Benefits of using overlay types

Using an overlay type creates a normal looking Java type that you can interact with using code completion, refactoring, and compile-time checking. Yet, you also have the flexibility of interacting with arbitrary JavaScript objects, which makes it simpler to access JSON services using RequestBuilder (which you'll do in the next section).

GWT now understands that any instance of StockData is a true JavaScript object that comes from outside this GWT module.
You can interact with it exactly as it exists in JavaScript.
In this example, you can access directly the JSON fields you know exist: this.Price and this.Change.

Because the methods on overlay types can be statically resolved by the GWT compiler, they are candidates for automatic inlining. Inlined code runs significantly faster. This makes it possible for the GWT compiler to create highly-optimized JavaScript for your application's client-side code.

##  Making HTTP requests to retrieve data from the server <a id="http"></a>

Now that you have the mechanism for working with the JSON data in place, you'll write the HTTP request that gets the data from the server.

In this example, you're going to replace the current refreshWatchList method with a new implementation that uses HTTP.

### Specifying the URL

First, specify the URL where the servlet lives, that is:
`http://localhost:8888/stockwatcher/stockPrices?q=ABC+DFD`

**Note:** If you are doing the php example, substitute the corresponding URL.

Then, append the stock codes in the watch list to the base module URL.
Rather than hardcoding the URL for the JSON server, add a constant to the StockWatcher class.

1.  Add a constant to the StockWatcher class that specifies the URL of the JSON data.
    *  **Note:** Earlier you specified a path to /stockPrices in the module XML file.

```java
private static final String JSON_URL = GWT.getModuleBaseURL() + "stockPrices?q=";
```

*  Eclipse flags GWT.
2.  Include the import declarations.

```java
import com.google.gwt.core.client.GWT;
```

3.  Append the stock codes to the query URL and encode it.
    *  Replace the existing refreshWatchList method with the following code.
    *

```java
private void refreshWatchList() {
  if (stocks.size() == 0) {
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

  // TODO Send request to server and handle errors.

}
```

   *  Eclipse flags Iterator and URL.
4.  Include the import declarations.

```java
import com.google.gwt.http.client.URL;
import java.util.Iterator;
```

### Asynchronous HTTP

To get the JSON text from the server, you'll use the HTTP client classes in the com.google.gwt.http.client package. These classes contain the functionality for making asynchronous HTTP requests.

The HTTP types are contained within separate GWT modules that StockWatcher needs to inherit.

1.  To inherit other GWT modules, edit the module XML file.
    *  In StockWatcher.gwt.xml, add `<inherits>` tags and specify the HTTP module.

```xml
<!-- Other module inherits -->
<inherits name="com.google.gwt.http.HTTP" />
```
2.
    *  Open StockWatcher.java and include the following import declarations. Declare the imports for the following Java types.

```java
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
```

### Building a custom HTTP request

To send a request, you'll create an instance of the RequestBuilder object.
You specify the HTTP method (GET, POST, etc.) and URL in the constructor.
If necessary, you can also set the username, password, timeout, and headers to be used in the HTTP request. In this example, you don't need to do this.

When you're ready to make the request, you call sendRequest(String, RequestCallback).

The RequestCallback argument you pass will handle the response in its onResponseReceived(Request, Response) method, which is called when and if the HTTP call completes successfully.
If the call fails (for example, if the HTTP server is not responding), the onError(Request, Throwable) method is called instead.
The RequestCallback interface is analogous to the AsyncCallback interface in GWT remote procedure calls.

1.  Make the HTTP request and parse the JSON response.
    *  In the refreshWatchList method, replace the TODO comments with the following code  .

    ```java
    // Send request to server and catch any errors.
    RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
    
    try {
      Request request = builder.sendRequest(null, new RequestCallback() {
          public void onError(Request request, Throwable exception) {
            displayError("Couldn't retrieve JSON");
          }
    
          public void onResponseReceived(Request request, Response response) {
            if (200 == response.getStatusCode()) {
              updateTable(JsonUtils.<JsArray<StockData>>safeEval(response.getText()));
            } else {
              displayError("Couldn't retrieve JSON (" + response.getStatusText()
                  + ")");
            }
          }
      });
    } catch (RequestException e) {
      displayError("Couldn't retrieve JSON");
    }
    ```

    *  This is where we use `JsonUtils.safeEval()` to convert the JSON string into JavaScript objects.

2.  You receive two compile errors which you will resolve in a minute.

#### Modify the updateTable method

To fix the compile errors, modify the updateTable method.
<ul>
    <li>Change: StockPrice[] prices
 To: JsArray<StockData> prices</li>
    <li>Change: prices.length
 To: prices.length()</li>
    <li>Change: prices[i]
 To: prices.get(i)</li>
</ul>

1.
    *  Replace the existing update updateTable(StockPrice[]) method with the following code.

```java
updateTable(JsArray<StockData> prices)
```

*  Eclipse flags JsArray.
2.  Declare the import.

```java
import com.google.gwt.core.client.JsArray;
```

4.  Make a corresponding change in the updateTable(StockPrice price) method.
    *  Change the reference to the StockPrice class to StockData class.

```java
private void updateTable(StockData price) {
  // Make sure the stock is still in the stock table.
  if (!stocks.contains(price.getSymbol())) {
    return;
  }

  ...

}

```

##  Handling GET errors <a id="errors"></a>

If something breaks along the way (for example, if the server is offline, or the JSON is malformed), you'll trap the error and display a message to the user. To do this you'll create a Label widget and write a new method, displayError(String).

1.  Clear the compiler error by creating a stub method in the StockWatcher class.
    *  Set the text for the error message and make the Label widget visible.

    ```java
    /**
     * If can't get JSON, display error message.
     * @param error
     */
    private void displayError(String error) {
      errorMsgLabel.setText("Error: " + error);
      errorMsgLabel.setVisible(true);
    }
    ```

2.  Eclipse flags errorMsgLabel.
      *  Ignore the compile errors for a moment; you'll implement a Label widget to display the error text in the next step.
3.  If the error is corrected, hide the Label widget.
    *  In the updateTable(JsArray<StockData> prices) method, clear any error messages.

```java
private void updateTable(JsArray<StockData> prices) {
  for (int i=0; i < prices.length(); i++) {
    updateTable(prices.get(i));
  }

  // Display timestamp showing last refresh.
  lastUpdatedLabel.setText("Last update : " +
      DateTimeFormat.getMediumDateTimeFormat().format(new Date()));

  // Clear any errors.
  errorMsgLabel.setVisible(false);
}
```

#### Displaying error messages

In order to display the error, you will need a new UI component; you'll implement a Label widget.

1.  Define a style for the error message so that it will attract the user's attention.
    *  In StockWatcher.css, create a style rule that applies to any element with a class attribute of errorMessage.

```css
.negativeChange {
  color: red;
}

.errorMessage {
  color: red;
}
```

2.  To hold the text of the error message, add a Label widget.
    *  In StockWatcher.java, add the following instance field.

```java
private ArrayList<String> stocks = new ArrayList<String>();
private Label errorMsgLabel = new Label();
```

3.  Initialize the errorMsgLabel when StockWatcher launches.
    *  In the onModuleLoad method, add a secondary class attribute to errorMsgLabel and do not display it when StockWatcher loads.
    *  Add the error message to the Main panel above the stocksFlexTable.

    ```java
    // Assemble Add Stock panel.
    addPanel.add(newSymbolTextBox);
    addPanel.add(addButton);
    addPanel.addStyleName("addPanel");

    // Assemble Main panel.
    errorMsgLabel.setStyleName("errorMessage");
    errorMsgLabel.setVisible(false);

    mainPanel.add(errorMsgLabel);
    mainPanel.add(stocksFlexTable);
    mainPanel.add(addPanel);
    mainPanel.add(lastUpdatedLabel);
    ```

### Test the HTTP request and error handling

1.  Refresh StockWatcher in development mode.
2.  Enter several stock codes.
        <div class="client">StockWatcher should display Price and Change data for each stock. Rather than being generated from within the program the stock data is coming from you local server in JSON format.
3.  Test the HTTP error messages by calling an invalid URL.
    * In StockWatcher,java, edit the URL.
    * Change: `private static final String JSON_URL = GWT.getModuleBaseURL() + "stockPrices?q=";`

      To: `private static final String JSON_URL = GWT.getModuleBaseURL() + "BADURL?q=";`
4.  Refresh StockWatcher in development mode.
5.  Enter a stock code.
    *  StockWatcher displays a red error message. Valid stock code data stops being refreshed.
    *  ![screenshot: JSON error message](images/JSONerrormessage.png)
6.  In StockWatcher,java, correct the URL.
7.  Refresh StockWatcher in development mode.
    *  Enter several stock codes.
    *  StockWatcher should display Price and Change data for each stock again.

## More about JSON, JSNI, Overlay types, and HTTP <a id="more"></a>

At this point you've retrieved JSON-encoded stock data from a local server and used it to update the Price and Change fields for the stocks in your watch list. If you'd like to see how to retrieve JSON from web server on another domain, see [Making cross-site requests](Xsite.html).

To learn more about client-server communication, see the Developer's Guide, Communicating with the Server. 
Topics include:

*   [Making HTTP Requests](../DevGuideServerCommunication.html#DevGuideHttpRequests)
*   [Working with JSON](../DevGuideCodingBasics.html#DevGuideJSON)

To learn more about JSNI, see the Developer's Guide, [JavaScript Native Interface (JSNI)](../DevGuideCodingBasics.html#DevGuideJavaScriptNativeInterface). 
Topics include:

*   Writing Native JavaScript Method
*   Accessing Java Methods and Fields from JavaScript
*   Sharing objects between Java source and JavaScript
*   Exceptions and JSNI
*   JavaScript Overlay Types
