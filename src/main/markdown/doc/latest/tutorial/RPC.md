
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
In this section, you'll make a GWT remote procedure call to a server-side method which returns the stock data. The server-side code that gets invoked from the client is also known as a <i>service</i>; the act of making a remote procedure call is referred to as <i>invoking a service</i>.<br />You'll learn to:
</p>
<ol>
    <li><a href="#services">Create a service on the server.</a></li>
    <li><a href="#invoke">Invoke the service from the client.</a></li>
    <li><a href="#serialize">Serialize the data objects.</a></li>
    <li><a href="#exceptions">Handle exceptions: checked and unexpected.</a></li>
</ol>

<p class="note" style="margin-left: 1.2em; margin-right: 1.5em;">
<b>Note:</b> For a broader guide to RPC communication in a GWT application, see <a href="../DevGuideServerCommunication.html#DevGuideRemoteProcedureCalls">Communicate with a Server - Remote Procedure Calls</a>.
</p>

<h3>What is GWT RPC?</h3>
<p>
The GWT RPC framework makes it easy for the client and server components of your web application to exchange Java objects over HTTP.
The server-side code that gets invoked from the client is often referred to as a <i>service</i>.
The implementation of a GWT RPC service is based on the well-known Java servlet architecture.
Within the client code, you'll use an automatically-generated proxy class to make calls to the service.
GWT will handle serialization of the Java objects passing back and forth&mdash;the arguments in the method calls and the return value.
</p>
<p class="note">
<b>Important:</b> GWT RPC services are not the same as web services based on SOAP or <a href="http://java.sun.com/developer/technicalArticles/WebServices/restful/">REST</a>. They are simply as a lightweight method for transferring data between your server and the GWT application on the client. To compare single and multi-tier deployment options for integrating GWT RPC services into your application, see the Developer's Guide, <a href="../DevGuideServerCommunication.html#DevGuideArchitecturalPerspectives">Architectural Perspectives</a>.
</p>
<h3>Java components of the GWT RPC Mechanism</h3>

<p>
When setting up GWT RPC, you will focus on these three elements involved in calling procedures running on remote servers.
</p>
<ul>
    <li>the service that runs on the server (the method you are calling)</li>
    <li>the client code that invokes the service</li>
    <li>the Java data objects that pass between the client and server<br />Both the server and the client have the ability to serialize and deserialize data so the data objects can be passed between them as ordinary text.</li>
</ul>

<img src="images/AnatomyOfServices.png" alt="GWT RPC Plumbing" />
<p>
In order to define your RPC interface, you need to write three components:
</p>
<ol>
    <li>Define an interface (StockPriceService) for your service that extends RemoteService and lists all your RPC methods.</li>
    <li>Create a class (StockPriceServiceImpl) that extends RemoteServiceServlet and implements the interface you created above.</li>
    <li>Define an asynchronous interface (StockPriceServiceAsync) to your service to be called from the client-side code.</li>
</ol>
<p>
A service implementation must extend RemoteServiceServlet and must implement the associated service interface.
Notice that the service implementation does not implement the asynchronous version of the service interface.
Every service implementation is ultimately a servlet, but rather than extending HttpServlet, it extends RemoteServiceServlet instead.
RemoteServiceServlet automatically handles serialization of the data being passed between the client and the server and invoking the intended method in your service implementation.
</p>

<a name="services"></a>
<h2>1. Creating a service</h2>
<p>
In this tutorial, you are going to take the functionality in the refreshWatchList method and move it from the client to the server. Currently you pass the  refreshWatchList method  an array of stock symbols and it returns the corresponding stock data. It then calls the updateTable method to populate the FlexTable with the stock data.
</p>
<h4>
The current client-side implementation:
</h4>

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
To create the service, you will:
</p>
<ol>
    <li>define the service interface: StockPriceService</li>
    <li>implement the service: StockPriceServiceImpl</li>
</ol>

<h3>Defining the service: the StockPriceService interface</h3>
<p>
In GWT, an RPC service is defined by an interface that extends the GWT <a href="/javadoc/latest/com/google/gwt/user/client/rpc/RemoteService.html">RemoteService</a>  interface. For the StockPriceService interface, you need to define only one method: a method that will accept an array of stock symbols and return the data associated with each stock as an array of StockPrice objects.
</p>

<ol class="instructions">
    <li>
        <div class="header">In the client subpackage, create an interface and name it StockPriceService.</div>
        <div class="details">In Eclipse, in the Package Explorer pane, select the package<br />
<code>com.google.gwt.sample.stockwatcher.client</code></div>
        <div class="details">From the Eclipse menu bar, select <code>File &gt; New &gt; Interface</code></div>
        <div class="details">Eclipse opens a New Java Interface window.</div>
    </li>
    <li>
        <div class="header">Fill in the New Java Interface window.</div>
        <div class="details">At Name enter <code>StockPriceService</code></div>
        <div class="details">Accept the defaults for the other fields.</div>
        <div class="details">Press <code>Finish</code></div>
        <div class="details">Eclipse creates stub code for the StockPriceService interface.</div>
    </li>
    <li>
        <div class="header">Replace the stub with following code.</div>
        <div class="details"><pre class="code">
package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("stockPrices")
public interface StockPriceService extends RemoteService {

  StockPrice[] getPrices(String[] symbols);
}</pre></div>
        <div class="details"><b>Implementation Note:</b> Notice the @RemoteServiceRelativePath annotation. This associates the service with a default path relative to the module base URL.</div>
    </li>
</ol>


<h3>Implementing the service: the StockPriceServiceImpl class</h3>
<p>
Now create the class (StockPriceServiceImpl) that lives on the server. As defined in the interface, StockPriceServiceImpl will contain only one method, the method that returns the stock price data.
</p>
<p>
To do this, implement the service interface by creating a class that also extends the GWT <a href="/javadoc/latest/com/google/gwt/user/server/rpc/RemoteServiceServlet.html">RemoteServiceServlet</a> class. RemoteServiceServlet does the work of deserializing incoming requests from the client and serializing outgoing responses.
</p>

<h4>Packaging server-side code</h4>
<p>
The service implementation runs on the server as Java bytecode; it's not translated into JavaScript. Therefore, the service implementation does not have the same language constraints as the client-side code. To keep the code for the client separate from the code for the server, you'll put it in a separate package (com.google.gwt.sample.stockwatcher.server).
</p>

<h4>Create a new class</h4>

<ol class="instructions">
    <li>
        <div class="header">Create the service implementation for StockPriceService.</div>
        <div class="details">In Eclipse, open the New Java Class wizard (File &gt; New &gt; Class).</div>
    </li>
    <li>
        <div class="header">At Package, change the name from <code>.client</code> to <code>.server</code></div>
        <div class="details">Eclipse will create a package for the server-side code.</div>
    </li>
    <li>
        <div class="header">At Name, enter <code>StockPriceServiceImpl</code></div>
        <div class="details"><b>Implementation Note:</b> By convention, the service implementation class is named after the service interface with the suffix Impl, so call the new class StockPriceServiceImpl.</div>
    </li>
    <li>
        <div class="header">Extend the RemoteServiceServlet class.</div>
        <div class="details">At Superclass, enter<br />
        <code>com.google.gwt.user.server.rpc.RemoteServiceServlet</code></div>
    </li>
    <li>
        <div class="header">Implement the StockPriceService interface.</div>
        <div class="details">At Interfaces, add an interface.<br />
        <code>com.google.gwt.sample.stockwatcher.client.StockPriceService</code></div>
    </li>
    <li>
        <div class="header">Inherit abstract methods.</div>
    </li>

    <li>
        <div class="header">Press <code>Finish</code></div>
        <div class="details">Eclipse creates the package<br />
        <code>com.google.gwt.sample.stockwatcher.server</code></div>
         <div class="details">Eclipse creates a stub StockPriceServiceImpl class.</div>
       <div class="details"><pre class="code">
package com.google.gwt.sample.stockwatcher.server;

import com.google.gwt.sample.stockwatcher.client.StockPrice;
import com.google.gwt.sample.stockwatcher.client.StockPriceService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class StockPriceServiceImpl extends RemoteServiceServlet implements StockPriceService {

  public StockPrice[] getPrices(String[] symbols) {
    // TODO Auto-generated method stub
    return null;
  }

}</pre></div>
    </li>
</ol>


<h4>Write the server-side implementation</h4>
<p>
Replace the client-side implementation that returns random stock prices.
</p>

<ol class="instructions">
    <li>
        <div class="header">Create instance variables to initialize the price and change data.</div>
        <div class="details"><pre class="code">
  private static final double MAX_PRICE = 100.0; // $100.00
  private static final double MAX_PRICE_CHANGE = 0.02; // +/- 2%
</pre></div>
    </li>
        <li>
        <div class="header">Replace the TODO with the following code. Return prices rather than null.</div>
        <div class="details"><pre class="code">
  public StockPrice[] getPrices(String[] symbols) {
<span class="highlight">    Random rnd = new Random();

    StockPrice[] prices = new StockPrice[symbols.length];
    for (int i=0; i&lt;symbols.length; i++) {
      double price = rnd.nextDouble() * MAX_PRICE;
      double change = price * MAX_PRICE_CHANGE * (rnd.nextDouble() * 2f - 1f);

      prices[i] = new StockPrice(symbols[i], price, change);
    }</span>

    return <span class="highlight">prices</span>;
  }</pre></div>
        <div class="details">Eclipse flags Random and suggests you include the import declaration.</div>
    </li>
    <li>
        <div class="header">Include the import declaration from java.util, not from com.google.gwt.user.client.</div>
        <div class="details"><pre class="code">import java.util.Random;</pre></div>
        <div class="details"><b>Implementation Note:</b>
Remember the service implementation runs on the server as Java bytecode, so you can use any Java class or library without worrying about whether it's translatable to JavaScript. In this case, you can use the Random class from the Java runtime library (java.util.Random) instead of the emulated GWT version (com.google.gwt.user.client.Random).
        </div>
    </li>
    <li>
        <div class="header">This listing shows the completed StockPriceServiceImpl class.</div>
        <div class="details"><pre class="code">
package com.google.gwt.sample.stockwatcher.server;

import com.google.gwt.sample.stockwatcher.client.StockPrice;
import com.google.gwt.sample.stockwatcher.client.StockPriceService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import java.util.Random;

public class StockPriceServiceImpl extends RemoteServiceServlet implements StockPriceService {

  private static final double MAX_PRICE = 100.0; // $100.00
  private static final double MAX_PRICE_CHANGE = 0.02; // +/- 2%

  public StockPrice[] getPrices(String[] symbols) {
    Random rnd = new Random();

    StockPrice[] prices = new StockPrice[symbols.length];
    for (int i=0; i&lt;symbols.length; i++) {
      double price = rnd.nextDouble() * MAX_PRICE;
      double change = price * MAX_PRICE_CHANGE * (rnd.nextDouble() * 2f - 1f);

      prices[i] = new StockPrice(symbols[i], price, change);
    }

    return prices;
  }

}</pre></div>
    </li>
</ol>


<h4>Include the server-side code in the GWT module</h4>
<p>
The embedded servlet container (Jetty) can host the servlets that contain your service implementation.
This means you can take advantage of running your application in development mode while testing and debugging the server-side Java code.
</p>
<p>
To set this up, add &lt;servlet&gt; and &lt;servlet-mapping&gt; elements to the web application deployment descriptor (web.xml) and point to the implementation class (StockPriceServiceImpl).
</p>
<p class="note">
Starting with GWT 1.6, servlets should be defined in the web application deployment descriptor (web.xml) instead of the GWT module (StockWatcher.gwt.xml).
</p>
<p>
In the &lt;servlet-mapping&gt; element, the url-pattern can be in the form of an absolute directory path (for example, <code>/spellcheck</code> or <code>/common/login</code>).
If you specify a default service path with a @RemoteServiceRelativePath annotation on the service interface (as you did with StockPriceService), then make sure the url-pattern matches the annotation value.

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
<span class="highlight">  &lt;servlet&gt;
    &lt;servlet-name&gt;stockPriceServiceImpl&lt;/servlet-name&gt;
    &lt;servlet-class&gt;com.google.gwt.sample.stockwatcher.server.StockPriceServiceImpl&lt;/servlet-class&gt;
  &lt;/servlet&gt;

  &lt;servlet-mapping&gt;
    &lt;servlet-name&gt;stockPriceServiceImpl&lt;/servlet-name&gt;
    &lt;url-pattern&gt;/stockwatcher/stockPrices&lt;/url-pattern&gt;
  &lt;/servlet-mapping&gt;</span>

&lt;/web-app&gt;
</pre></div>
    </li>

</ol>

<a name="invoke"></a>
<h2>2. Invoking the service from the client</h2>

<h3>Making asynchronous calls to the server</h3>
<p>
You need to to add an AsyncCallback parameter to all your service methods.
</p>
<p>
To add an AsyncCallback parameter to all of our service methods, you must define a new interface as follows:
</p>
<ul>
    <li>It must have the same name as the service interface, appended with <i>Async</i> (for example, StockPriceServiceAsync).</li>
    <li>It must be located in the same package as the service interface.</li>
    <li>Each method must have the same name and signature as in the service interface with an important difference: the method has no return type and the last parameter is an AsyncCallback object.</li>
</ul>


<ol class="instructions">
    <li>
        <div class="header">In the client subpackage, create an interface and name it StockPriceServiceAsync.</div>
    </li>
    <li>
        <div class="header">Replace the stub with following code.</div>
        <div class="details"><pre class="code">
package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface StockPriceServiceAsync {

  void getPrices(String[] symbols, AsyncCallback&lt;StockPrice[]&gt; callback);

}</pre></div>
    </li>
</ol>


<p class="note">
<b>Tip:</b> The Google Plugin for Eclipse will generate an error when it finds a synchronous remote service without a matching asynchronous interface. You can right click on the error in Eclipse, select Quick Fix,
and choose the "Create asynchronous RemoteService interface" option to automatically generate the asynchronous interface.  
</p>

<h3>Making the remote procedure call</h3>

<h4>Callback methods</h4>
<p>
When you call a remote procedure, you specify a callback method which executes when the call completes.

You specify the callback method by passing an <a href="/javadoc/latest/com/google/gwt/user/client/rpc/AsyncCallback.html">AsyncCallback</a> object to the service proxy class.

The AsyncCallback object contains two methods, one of which is called depending on whether the call failed or succeeded: onFailure(Throwable) and onSuccess(T).
</p>


<ol class="instructions">
    <li>
        <div class="header">Create the service proxy class.</div>
        <div class="details">In the StockWatcher class, create an instance of the service proxy class by calling GWT.create(Class).</div>
        <div class="details"><pre class="code">
  private ArrayList&lt;String&gt; stocks = new ArrayList&lt;String&gt;();
<span class="highlight">  private StockPriceServiceAsync stockPriceSvc = GWT.create(StockPriceService.class);</span></pre></div>
        <div class="details">Eclipse flags GWT.</div>
    </li>
    <li>
        <div class="header">Initialize the service proxy class, set up the callback object, and make the call to the remote procedure.</div>
        <div class="details">Replace the existing refreshWatchList method with the following code.</div>
        <div class="details"><pre class="code">
  private void refreshWatchList() {
<span class="highlight">    // Initialize the service proxy.
    if (stockPriceSvc == null) {
      stockPriceSvc = GWT.create(StockPriceService.class);
    }

    // Set up the callback object.
    AsyncCallback&lt;StockPrice[]&gt; callback = new AsyncCallback&lt;StockPrice[]&gt;() {
      public void onFailure(Throwable caught) {
        // TODO: Do something with errors.
      }

      public void onSuccess(StockPrice[] result) {
        updateTable(result);
      }
    };

    // Make the call to the stock price service.
    stockPriceSvc.getPrices(stocks.toArray(new String[0]), callback);</span>
  }
</pre></div>
        <div class="details">Eclipse flags AsyncCallback.</div>
    </li>
    <li>
        <div class="header"></div>
        <div class="details">Include the import declarations.</div>
        <div class="details"><pre class="code">
import com.google.gwt.core.client.GWT;

import com.google.gwt.user.client.rpc.AsyncCallback;</pre></div>
    </li>
</ol>


<h3>Test the Remote Procedure Call</h3>
<p>
At this point, you've created a service and pointed to it in the module XML file, set up the mechanism for making asynchronous calls, and generated a service proxy on the client side to call the service.  However there's a problem.
</p>


<ol class="instructions">
    <li>
        <div class="header">Launch StockWatcher in development mode using the Eclipse debugger.</div>
    </li>
    <li>
        <div class="header">Examine the error log in the Development Shell window.</div>
        <div class="details"><pre class="code">
[ERROR] Type 'com.google.gwt.sample.stockwatcher.client.StockPrice' was not serializable<br /> and has no concrete serializable subtypes
</pre></div>
    </li>
</ol>


<p>
In the service implementation (StockPriceServiceImpl), you inherited the code that serializes and deserializes Java objects by extending the RemoteServiceServlet class. However the problem is that we did not also edit the StockPrice class to indicate it was serializable.
</p>

<a name="serialize"></a>
<h2>3. Serializing Java objects</h2>
<p>
Serialization is the process of packaging the contents of an object so that it can moved from one application to another application or stored for later use.
Anytime you transfer an object over the network via GWT RPC, it must be serialized.
Specifically, GWT RPC requires that all service method parameters and return types be serializable.
</p>
<p>
A type is serializable and can be used in a service interface if one of the following is true:
</p>
<ul>
    <li>All primitive types (int, char, boolean, etc.) and their wrapper objects are serializable by default.</li>
    <li>An array of serializable types is serializable by extension. </li>
    <li>A class is serializable if it meets these three requirements:
        <ul>
            <li>It implements either Java <a href="http://java.sun.com/j2se/1.5.0/docs/api/java/io/Serializable.html">Serializable</a> or GWT <a href="/javadoc/latest/com/google/gwt/user/client/rpc/IsSerializable.html">IsSerializable</a> interface, either directly, or because it derives from a superclass that does.</li>
            <li>Its non-final, non-transient instance fields are themselves serializable, and</li>
            <li>It has a default (zero argument) constructor with any access modifier (e.g. private Foo(){} will work)</li>
        </ul>
    </li>
</ul>
<p>
GWT honors the <i>transient</i> keyword, so values in those fields are not serialized (and thus, not sent over the wire when used in RPC calls).
</p>

<p class="note">
<b>Note:</b> GWT serialization is not the same as serialization based on the Java Serializable interface.<br />


For more information on what is and is not serializable in GWT, see the Developer's Guide, <a href="../DevGuideServerCommunication.html#DevGuideSerializableTypes">Serializable Types</a>.
</p>

<h4>Serializing StockPrice</h4>
<p>
Based on the requirements for serialization, what do you need to do to make the StockPrice class ready for GWT RPC?
Because all its instance fields are primitive types, all you need to do in this
case is implement the Serializable or IsSerializable interface.
</p>
<pre class="code">package com.google.gwt.sample.stockwatcher.client;

<span class="highlight">import java.io.Serializable;</span>

public class StockPrice <span class="highlight">implements Serializable</span> {

  private String symbol;
  private double price;
  private double change;

  ...
</pre>
<ol class="instructions">
    <li>
        <div class="header">Refresh StockWatcher in development mode.</div>
    </li>
     <li>
        <div class="header">Add a stock.</div>
    </li>
    <li>
        <div class="header">StockWatcher adds the stock to the table; the Price and Change fields contain data and there are no errors.
</div>
       <div class="details">Atlhough StockWatcher doesn't look any different on the surface, underneath it is now retrieving its stock price updates from the server-side StockPriceService servlet on the embedded servlet container instead of generating them on the client-side.</div>
    </li>
</ol>
<p>
At this point, the basic RPC mechanism is working. However, there is one TODO left. You need to code the error handling in case the callback fails.
</p>

<a name="exceptions"></a>
<h2>4. Handling Exceptions</h2>
<p>
When a remote procedure call fails, the cause falls into one of two categories: an unexpected exception or a checked exception. In either case you want to handle the exception and, if necessary, provide feedback to the user.
</p>
<p>
<b>Unexpected exceptions:</b> Any number of unexpected occurrences could cause the call to a remote procedure to fail: the network could be down; the HTTP server on the other end might not be listening; the DNS server could be on fire, and so forth.
</p>
<p>
Another type of unexpected exception can occur if GWT is able to invoke the service method, but the service implementation throws an undeclared exception. For example, a bug may cause a NullPointerException.
</p>
<p>
When unexpected exceptions occur in the service implementation, you can find the full stack trace in the development mode log.
On the client-side, the onFailure(Throwable) callback method will receive an InvocationException with the generic message: The call failed on the server; see server log for details.
</p>

<p>
<b>Checked exceptions:</b> If you know a service method might throw a particular type of exception and you want the client-side code to be able to handle it, you can use checked exceptions. GWT supports the <i>throws</i> keyword so you can add it to your service interface methods as needed. When checked exceptions occur in an RPC service method, GWT will serialize the exception and send it back to the caller on the client for handling.
</p>

<h3>Creating a checked exception</h3>
<p>
To learn how to handle errors in RPC, you will throw an error when the user adds a stock code which you've defined as "delisted"&mdash;causing the call to fail. Then you'll handle the failure by displaying an error message to the user.
</p>
<h4>Checking for and throwing the exception</h4>

<h5>Create a class: DelistedException</h5>
<ol class="instructions">
    <li>
        <div class="header">Create a class to identify delisted stock codes.</div>
        <div class="details">In Eclipse, open the New Java Class wizard (File &gt; New &gt; Class).</div>
    </li>
    <li>
        <div class="header">At Name, enter <code>DelistedException</code></div>
    </li>
    <li>
        <div class="header">Extend the java.lang.Exception class.</div>
    </li>
    <li>
        <div class="header">Implement the java.io.Serializable interface.</div>
        <div class="details">This exception is designed to be sent by RPC; therefore, this class must be serializable.</div>
    </li>
        <li>
        <div class="header">Inherit abstract methods.</div>
    </li>
    <li>
        <div class="header">Press <code>Finish</code></div>
        <div class="details">Eclipse creates a stub DelistedException class.</div>
    </li>

    <li>
        <div class="header">Replace the stub with the following code.</div>
        <div class="details"><pre class="code">
package com.google.gwt.sample.stockwatcher.client;

import java.io.Serializable;

public class DelistedException extends Exception implements Serializable {

  private String symbol;

  public DelistedException() {
  }

  public DelistedException(String symbol) {
    this.symbol = symbol;
  }

  public String getSymbol() {
    return this.symbol;
  }
}</pre></div>
    </li>
</ol>

<h5>Update the stock price service interface: StockPriceService</h5>

<ol class="instructions">
    <li>
        <div class="header">In the service interface (StockPriceService), append a <i>throws</i> declaration to the getPrices(String[])  method.</div>
        <div class="details">In StockPriceService.java, make the changes highlighted below.</div>
        <div class="details"><pre class="code">
package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("stockPrices")
public interface StockPriceService extends RemoteService {

  StockPrice[] getPrices(String[] symbols) <span class="highlight">throws DelistedException</span>;
}</pre></div>
    </li>
</ol>


<h5>Update the stock price service implementation: StockPriceServiceImpl</h5>

<p>
You need to make two changes to the service implementation (StockPriceServiceImpl).
</p>

<ol class="instructions">
    <li>
        <div class="header">Make the corresponding change to the getPrices(String[]) method.</div>
        <div class="details">Append a <i>throws</i> declaration.</div>
        <div class="details">Include the import declaration for DelistedException.</div>
    </li>
    <li>
        <div class="header">Add the code to throw the Delisted Exception.</div>
        <div class="details">For the sake of this example, keep it simple and only throw the exception when the stock symbol ERR is added to the watch list.</div>
    </li>
    <li>
        <div class="header">This listing shows the completed StockPriceServiceImpl class.</div>
        <div class="details"><pre class="code">
<span class="highlight">import com.google.gwt.sample.stockwatcher.client.DelistedException;</span>

...

  public StockPrice[] getPrices(String[] symbols) <span class="highlight">throws DelistedException </span>{
    Random rnd = new Random();

    StockPrice[] prices = new StockPrice[symbols.length];
    for (int i=0; i&lt;symbols.length; i++) {
<span class="highlight">      if (symbols[i].equals("ERR")) {
        throw new DelistedException("ERR");
      }</span>

      double price = rnd.nextDouble() * MAX_PRICE;
      double change = price * MAX_PRICE_CHANGE * (rnd.nextDouble() * 2f - 1f);

      prices[i] = new StockPrice(symbols[i], price, change);
    }

    return prices;
  }</pre></div>
    </li>
</ol>


<p>
At this point you've created the code that will throw the exception. You don't need to add the throws declaration to the service method in StockPriceServiceAsync.java. That method will always return immediately (remember, it's asynchronous). Instead you'll receive any thrown checked exceptions when GWT calls the onFailure(Throwable) callback method.
</p>

<h4>Displaying error messages</h4>
<p>
In order to display the error, you will need a new UI component. First think a moment about how you want to display the error. An obvious solution would be to display a pop-up alert using Window.alert(String). This could work in the case where the user receives the error while entering stock codes. But in a more real-world example, what would you want to happen if stock was delisted after the user had added it to the watch list? or if you need to display multiple errors? In these cases, a pop-up alert is not the best approach.
</p>
<p>
So, to display any messages about failing to retrieve stock data, you'll implement a Label widget.
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
  private StockPriceServiceAsync stockPriceSvc = GWT.create(StockPriceService.class);
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

<h4>Handling the error</h4>
<p>
Now that you've built a Label widget to display the error, you can finish writing the error handling code.
</p>
<p>
You will also want to hide the error message if the error is corrected (for example, if the user removes the ERR code from the stock table).
</p>

<ol class="instructions">
    <li>
        <div class="header">Specify the action to take if the callback fails.</div>
        <div class="details">In StockWatcher.java, in the refreshWatchList method, fill in the onFailure method as follows.</div>
        <div class="details"><pre class="code">
      public void onFailure(Throwable caught) {
<span class="highlight">        // If the stock code is in the list of delisted codes, display an error message.
        String details = caught.getMessage();
        if (caught instanceof DelistedException) {
          details = "Company '" + ((DelistedException)caught).getSymbol() + "' was delisted";
        }

        errorMsgLabel.setText("Error: " + details);
        errorMsgLabel.setVisible(true);</span>
      }</pre></div>
    </li>
    <li>
        <div class="header">If the error is corrected, hide the error message widget.</div>
        <div class="details">In the updateTable(StockPrice[] prices) method, clear any errors.</div>
        <div class="details"><pre class="code">
  private void updateTable(StockPrice[] prices) {
    for (int i=0; i &lt; prices.length; i++) {
      updateTable(prices[i]);
    }

    // Display timestamp showing last refresh.
    lastUpdatedLabel.setText("Last update : " +
        DateTimeFormat.getMediumDateTimeFormat().format(new Date()));

<span class="highlight">    // Clear any errors.
    errorMsgLabel.setVisible(false);</span>
  }
</pre></div>
    </li>
</ol>


<h3>Test Exception Handling in RPC</h3>
<p>
At this point, you've created and checked for an exception&mdash;the delisted stock code "ERR". When StockWatcher makes a remote procedure call to retrieve stock data for the delisted stock code, the call fails, an exception is thrown, and then handled by displaying an error message.
</p>
<ol class="instructions">
    <li>
        <div class="header">Refresh StockWatcher in development mode.</div>
    </li>
     <li>
        <div class="header">Add the stock code, ERR.</div>
    </li>
    <li>
        <div class="header">StockWatcher displays a red error message. Valid stock code data stops being refreshed.</div>
        <div class="details"><img src="images/RPCerrormessage.png" alt="screenshot: RPC error message" /></div>

    </li>
    <li>
        <div class="header">Delete the stock code, ERR.</div>
    </li>
    <li>
        <div class="header">The error message is removed. Valid stock code data resumes being refreshed.</div>
    </li>
</ol>


<h2>What's Next</h2>
<p>
At this point, you've made remote procedure calls to get stock data from the server.
</p>
<p>
For more information about GWT RPC, see the Developer's Guide, <a href="../DevGuideServerCommunication.html#DevGuideRemoteProcedureCalls">Remote Procedure Calls</a>.
</p>
<a name="deploy"></a>
<h3>Deploying services into production</h3>
<p>
During testing, you can use development mode's built-in servlet container to test the server-side code for your RPC services&mdash;just as you did in this tutorial. When you deploy your GWT application, you can use any servlet container to host your services. Make sure your client code invokes the service using the URL the servlet is mapped to in the web.xml configuration file.
</p>

<p class="note">
To learn how to deploy GWT RPC servlets to a production server, see the Developer's Guide, <a href="../DevGuideServerCommunication.html#DevGuideRPCDeployment">Deploying RPC</a></p>
