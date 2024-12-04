Cross-site Requests
===

At this point, you've modified the initial implementation of the StockWatcher application, which simulated stock data in the client-side code. The current implementation now retrieves JSON-formatted data from your local server.

In this session, you'll make a call to a remote server instead. To do so you will have to work around SOP (Same Origin Policy) constraints.

1.  [Review the requirements and design: access restrictions and asynchronous communication.](#design)
2.  [Create a source of JSON data on a remote server.](#server)
3.  [Request the data from the remote server.](#request)
4.  [Handle the response.](#response)
5.  [Test.](#test)

**Note:** For a broader guide to client-server communication in a GWT application, see [Communicate with a Server](../DevGuideServerCommunication.html).

## Before you begin

### The StockWatcher project

This tutorial builds on the GWT concepts and the StockWatcher application created in the [Build a Sample GWT Application](gettingstarted.html) tutorial. If you have not completed the Build a Sample GWT Application tutorial and are familiar with basic GWT concepts, you can import the StockWatcher project as coded to this point.

1.  Download the [StockWatcher project](http://code.google.com/p/google-web-toolkit/downloads/detail?name=Tutorial-JSON-2.1.zip) (with JSON).
2.  Unzip the file.
3.  Import the project into Eclipse

    1.  From the `File` menu, select the  `Import...` menu option.
    2.  Select the import source General > Existing Projects into Workspace. Click the `Next` button.
    3.  For the root directory, browse to and select the StockWatcher directory (from the unzipped file). Click the `Finish` button.

If you are using ant, edit the `gwt.sdk` property in StockWatcher/build.xml to point to where you unzipped GWT.

In order to actually run this tutorial, you will need either access to a server other than where StockWatcher is running, one that can run a PHP script, or be able to run a Python script on your machine.

##  Reviewing the requirements and design <a id="design"></a>

As you modify the current implementation of StockWatcher to access data on a remote server, there are two issues to address:

*   Access Restrictions: SOP (Same Origin Policy)
*   Asynchronous Communication

### Access Restrictions: Same Origin Policy

The Same Origin Policy (SOP) is a browser security measure that restricts client-side JavaScript code from interacting with resources **not** originating from the same domain name, protocol and port. The browser considers two pages to have the same origin only if these three values are the same. For example, if the StockWatcher application is running in a web page on http://abc.com:80 it cannot interact with stock data loaded from a different domain, http://xyz.com. It can't even load stock data from the same domain if the port is different, for example, http://abc.com:81.

The idea behind SOP is that, for security reasons, the browser should not trust content loaded from arbitrary websites. A malicious web page could inject code that steals data or otherwise compromises security.

As such, for accessing JSON-formatted stock data from another domain or port, the current implementation will not work. The web browser will block the HTTP call to retrieve the JSON.

For a more detailed description of SOP and its effect on GWT, read [What is the Same Origin Policy, and how does it affect GWT?](../FAQ_Server.html#What_is_the_Same_Origin_Policy,_and_how_does_it_affect_GWT?)

#### Working around SOP

There are two options for working around SOP security:

*   Proxy on your own server
*   Load the JSON response into a `<script>` tag

##### Proxy on your own server

The first option is to play by the rules of SOP and create a proxy on your local server.
You can then make HTTP calls to your local server and have it go fetch the data from the remote server.
This works because the code running on your web server is not subject to SOP restrictions. Only the client-side code is.

Specifically for the StockWatcher application, you could implement this strategy by writing server-side code to download (and maybe cache) the JSON-encoded stock quotes from a remote server.
You could then use any mechanism we want for retrieving the data from the local server: GWT RPC or direct HTTP using RequestBuilder.

One downside to this approach is that it requires additional server-side code.
Another is that the extra HTTP call increases the latency of remote calls and adds to the workload on our web server.

##### Load the JSON response into a `<script>` tag

Another option is to dynamically load JavaScript into a `<script>` tag.
Client-side JavaScript can manipulate `<script>` tags, just like any other element in the HTML Document Object Model (DOM).
Client-side code can set the src attribute of a `<script>` tag to automatically download and execute new JavaScript into the page.

This strategy is not subject to SOP restrictions. So you can effectively use it to load JavaScript (and therefore JSON) from remote servers.

This is the strategy you'll use to get the JSON-formatted stock data from a remote server.

### Asynchronous Communication

Dynamically loading the JavaScript into a `<script>` tag solves the SOP issue but introduces another.
When you use this method to load JavaScript, although the browser retrieves the code asynchronously, it doesn't notify you when it's finished.
Instead, it simply executes the new JavaScript.
However, by definition, JSON cannot contain executable code.
Put the two together and you'll realize that you can't load plain JSON data using a `<script>` tag.

#### JSON with Padding (JSONP)

To resolve the callback issue, you can specify the name of a callback function as an input argument of the call itself.
The web server will then wrap the JSON response in a call to that function.
This technique is called JSON with Padding (JSONP).
When the browser finishes downloading the new contents of the `<script>` tag, the callback function executes.

```javascript
callback125([{"symbol":"DDD","price":10.610339195026,"change":0.053085447454327}]);
```

Google Data APIs support this technique.

For StockWatcher, the additional requirement in the client-side code is that you include the name of the JavaScript function you're using as a callback in the HTTP request .

### Implementation Strategies

Now that you understand the SOP issues surrounding cross-site requests, compare this implementation to the implementation for getting JSON data from a local server. You'll have to change some of the existing implementation but you'll be able to reuse some components as well. Most of the work will be in writing the new method, getJSON, which makes the call to the remote server.

| Task                  | Same-Site Implementation                                              | Cross-Site Implementation |
| --------------------- | --------------------------------------------------------------------- | ------------------------- |
| Making the call       | HTTP with Request Builder                                             | JSON-P with Jsonp Request Builder. |
| Server-side code      | Returns JSON string                                                   | Returns a JavaScript callback function with the JSON string |
| Handling the response | Use `JsonUtils.safeEval()` to turn JSON string into JavaScript object | Already a JavaScript object; cast it as a StockData array |
| Data objects          | Create an overlay type: StockData                                     | Reuse the overlay type |
| Handle Errors         | Create a Label widget to display error messages                       | Reuse the Label widget |

##  Creating a data a source <a id="server"></a>

In this tutorial, you have two options for setting up the stock data so that StockWatcher encounters SOP restrictions.

1.  If you have access to a server with PHP installed, you can use the PHP script below to generate the JSON-formatted stock data.
2.  If you don't have a server but have Python installed on your machine, you can use the Python script below to serve the stock data from a different port than StockWatcher is running on.

### Actually using different server

If you have access to a web server, then you can use the following PHP script to return the JSONP.

1.  Create a text file and name it `stockPrices.php`
    *

```php
<?php

  header('Content-Type: text/javascript');
  header('Cache-Control: no-cache');
  header('Pragma: no-cache');

  define("MAX_PRICE", 100.0); // $100.00
  define("MAX_PRICE_CHANGE", 0.02); // +/- 2%

  $callback = trim($_GET['callback']);
  echo $callback;
  echo '([';

  $q = trim($_GET['q']);
  if ($q) {
    $symbols = explode(' ', $q);

    for ($i=0; $i<count($symbols); $i++) {
      $price = lcg_value() * MAX_PRICE;
      $change = $price * MAX_PRICE_CHANGE * (lcg_value() * 2.0 - 1.0);

      echo '{';
      echo "\"symbol\":\"$symbols[$i]\",";
      echo "\"price\":$price,";
      echo "\"change\":$change";
      echo '}';

      if ($i < (count($symbols) - 1)) {
        echo ',';
      }
    }
  }

  echo ']);';
?>
```

2.  Copy the PHP script to another server.
3.  Open a browser and make a request for the JSON data.
    *  `http://_[www.myStockServerDomain.com]_/stockPrices.php?q=ABC`
4.  The JSON string is returned.
    *  `[{"symbol":"ABC","price":81.284083,"change":-0.007986}]`
    *  However, as you'll see in the next section, the StockWatcher application will not be able to make this request from its client-side code.
5.  Make a request for the JSONP by appending the name of a callback function.
    *  `http://_[www.myStockServerDomain.com]_/stockPrices.php?q=ABC&callback=callback125`
6.  The JSON is returned embedded in the callback function.
    *  `callback125([{"symbol":"ABC","price":53.554212,"change":0.584011}]);`

### Simulating a second server

If you do not have access to a remote server, but have Python installed on your local machine, you can simulate a remote server. If you make an HTTP request to a different port, you will hit the same SOP restrictions as if you were trying to access a different domain.

Use the following script to serve data from a different port on your local machine.
For each stock symbol the Python script generates random price and change values in JSON format.
Notice in the BaseHTTPServer.HTTPServer constructor that it will be running on port 8000.
Also notice that the script supports the callback query string parameter.

1.  Create a Python script and save it as `quoteServer.py`
    *

```python
#!/usr/bin/env python2.4
#
# Copyright 2007 Google Inc. All Rights Reserved.

import BaseHTTPServer
import SimpleHTTPServer
import urllib
import random

MAX_PRICE = 100.0
MAX_PRICE_CHANGE = 0.02

class MyHandler(SimpleHTTPServer.SimpleHTTPRequestHandler):

  def do_GET(self):
    form = {}
    if self.path.find('?') > -1:
      queryStr = self.path.split('?')[1]
      form = dict([queryParam.split('=') for queryParam in queryStr.split('&amp;')])

      body = '['

      if 'q' in form:
        quotes = []

        for symbol in urllib.unquote_plus(form['q']).split(' '):
          price = random.random() * MAX_PRICE
          change = price * MAX_PRICE_CHANGE * (random.random() * 2.0 - 1.0)
          quotes.append(('{"symbol":"%s","price":%f,"change":%f}'
                       % (symbol, price, change)))

        body += ','.join(quotes)

      body += ']'

      if 'callback' in form:
        body = ('%s(%s);' % (form['callback'], body))

    self.send_response(200)
    self.send_header('Content-Type', 'text/javascript')
    self.send_header('Content-Length', len(body))
    self.send_header('Expires', '-1')
    self.send_header('Cache-Control', 'no-cache')
    self.send_header('Pragma', 'no-cache')
    self.end_headers()

    self.wfile.write(body)
    self.wfile.flush()
    self.connection.shutdown(1)

bhs = BaseHTTPServer.HTTPServer(('', 8000), MyHandler)
bhs.serve_forever()
```

2.  Save the script to the main StockWatcher directory.
    *
3.  Make sure the Python interpreter is on your PATH.
4.  Launch the script.
    *  From the command shell, enter `python quoteServer.py`
    *  The server will start, although you won't see any output immediately. (It will log each HTTP request).
5.  Open a browser and make a request for the JSON data.
    *  `http://localhost:8000/?q=ABC`
6.  The JSON string is returned.
    *  `[{"symbol":"ABC","price":81.284083,"change":-0.007986}]`
    *  However, as you'll see in the next section, the StockWatcher application will not be able to make this request from its client-side code.
7.  Make a request for the JSONP by appending the name of a callback function.
    *  `http://localhost:8000/?q=ABC&callback=callback125`
8.  The JSON is returned embedded in the callback function.
    *  `callback125([{"symbol":"ABC","price":53.554212,"change":0.584011}]);`

##  Requesting the data from the remote server <a id="request"></a>

Now that you've verified that the server is returning stock data either as a JSON string or as JSONP, you can update StockWatcher to request and then handle the JSONP.

The RequestBuilder code is replaced by a call to JsonpRequestBuilder.

1.  In the StockWatcher class, change the JSON_URL constant as follows:

    *
    *  Change:

```java
private static final String JSON_URL = GWT.getModuleBaseURL() + "stockPrices?q=";
```

*  If your stock data is being served from a different port (the Python script), change JSON_URL to:

```java
private static final String JSON_URL = "http://localhost:8000/?q=";
```

*  If your stock data is being served from a different domain (the PHP script), specify the domain and full path to the stockPrices.php script:

```java
private static final String JSON_URL = "http://_www.myStockServerDomain.com_/stockPrices.php?q=";
```

2.  Try to retrieve plain JSON data from the remote server.
    *  Debug StockWatcher in development mode.
    *  Enter a stock code.
3.  StockWatcher displays the error message: Couldn't retrieve JSON.
    *  To fix the SOP error, in the next step you'll use JsonpRequestBuilder.

### Update the refreshWatchList method

1. Update the refreshWatchList method.

    ```java
    /**
     * Generate random stock prices.
     */
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
  
      JsonpRequestBuilder builder = new JsonpRequestBuilder();
      builder.requestObject(url, new AsyncCallback<JsArray<StockData>>() {
        public void onFailure(Throwable caught) {
          displayError("Couldn't retrieve JSON");
        }
        public void onSuccess(JsArray<StockData> data) {
          // TODO handle JSON response
        }
      });
    }
    ```

   2.  If you haven't already, delete the RequestBuilder code.
       *  The RequestBuilder code is replaced by a call to JsonpRequestBuilder. So you no longer need the following code in the refreshWatchList method:

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
                   updateTable(JsonUtils.safeEval(response.getText()));
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

#### Implement the onSuccess method

If you receive a response from the server, then call the updateTable method to populate the Price and Change fields. You will still use the overlay type (StockData)  and the JsArray that you wrote in the same-site implementation.

If a response does not come back from the server, you display a message. You can use the same displayError method and Label widget you created in the same-site implementation.

1.  To the onSuccess method, replace the TODO comments with the following code.

```java
if (data == null) {
  displayError("Couldn't retrieve JSON");
  return;
}

updateTable(data);
```

##  Testing <a id="test"></a>

Whether you chose to serve the JSON-formatted stock data from a different domain or a different port, the new StockWatcher implementation should work around any SOP access restrictions and be able to retrieve the stock data.

### Test in development mode

#### Stock Data served from a different port

1.  Make sure the Python server is running.
    *  If it's not, at the command line, enter `python quoteServer.py`
2.  In the browser running in development mode, refresh StockWatcher.
3.  Add a stock code.
    *  StockWatcher displays the Price and Change data. The information is now coming from a different port.
4.  Shutdown the Python server.
    *  StockWatcher displays the error: Couldn't retrieve JSON
5.  Restart the Python server.
    *  StockWatcher clears the error and continues displaying Price and Change updates.

#### Stock Data served from a different domain

1.  In the browser running in development mode, refresh StockWatcher.
2.  Add a stock code.
    *  StockWatcher displays the Price and Change data. The information is now coming from a remote server.
3.  In StockWatcher.java, change the JSON_URL so that it is not correct.
4.  In the browser running in development mode, refresh StockWatcher.
    *  Add a stock code.
    *  StockWatcher displays the error: Couldn't retrieve JSON
5.  In StockWatcher.java, correct the JSON_URL.
6.  In the browser running in development mode, refresh StockWatcher.
    *  Add a stock code.
    *  StockWatcher clears the error and continues displaying Price and Change updates.

## What's Next

### Security and Cross-Site Requests

Before you implement mashups of your own, remember that downloading cross-site JSON is powerful, but can also be a security risk. Make sure the servers you interact with are **absolutely trustworthy**, because they will have the ability to execute arbitrary JavaScript code within your application. Take a minute to read [Security for GWT Applications](../../../articles/security_for_gwt_applications.html), which describes the potential threats to GWT applications and how you can combat them.
