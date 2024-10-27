# Using GWT RPC

At this point, you've created the initial implementation of the StockWatcher application, simulating stock data in the client-side code.

In this section, you'll make a GWT remote procedure call to a server-side method which returns the stock data. The server-side code that gets invoked from the client is also known as a _service_; the act of making a remote procedure call is referred to as _invoking a service_.
You'll learn to:

1.  [Create a service on the server.](#services)
2.  [Invoke the service from the client.](#invoke)
3.  [Serialize the data objects.](#serialize)
4.  [Handle exceptions: checked and unexpected.](#exceptions)

**Note:** For a broader guide to RPC communication in a GWT application, see [Communicate with a Server - Remote Procedure Calls](../DevGuideServerCommunication.html#DevGuideRemoteProcedureCalls).

## What is GWT RPC?

The GWT RPC framework makes it easy for the client and server components of your web application to exchange Java objects over HTTP.
The server-side code that gets invoked from the client is often referred to as a _service_.
The implementation of a GWT RPC service is based on the well-known Java servlet architecture.
Within the client code, you'll use an automatically-generated proxy class to make calls to the service.
GWT will handle serialization of the Java objects passing back and forth&mdash;the arguments in the method calls and the return value.

**Important:** GWT RPC services are not the same as web services based on SOAP or [REST](http://java.sun.com/developer/technicalArticles/WebServices/restful/). They are simply as a lightweight method for transferring data between your server and the GWT application on the client. To compare single and multi-tier deployment options for integrating GWT RPC services into your application, see the Developer's Guide, [Architectural Perspectives](../DevGuideServerCommunication.html#DevGuideArchitecturalPerspectives).

## Java components of the GWT RPC Mechanism

When setting up GWT RPC, you will focus on these three elements involved in calling procedures running on remote servers.

*   the service that runs on the server (the method you are calling)
*   the client code that invokes the service
*   the Java data objects that pass between the client and server

Both the server and the client have the ability to serialize and deserialize data so the data objects can be passed between them as ordinary text.

![GWT RPC Plumbing](images/AnatomyOfServices.png)

In order to define your RPC interface, you need to write three components:

1.  Define an interface (StockPriceService) for your service that extends RemoteService and lists all your RPC methods.
2.  Create a class (StockPriceServiceImpl) that extends RemoteServiceServlet and implements the interface you created above.
3.  Define an asynchronous interface (StockPriceServiceAsync) to your service to be called from the client-side code.

A service implementation must extend RemoteServiceServlet and must implement the associated service interface.
Notice that the service implementation does not implement the asynchronous version of the service interface.
Every service implementation is ultimately a servlet, but rather than extending HttpServlet, it extends RemoteServiceServlet instead.
RemoteServiceServlet automatically handles serialization of the data being passed between the client and the server and invoking the intended method in your service implementation.

## Creating a service <a id="services"></a>

In this tutorial, you are going to take the functionality in the refreshWatchList method and move it from the client to the server. Currently you pass the  refreshWatchList method  an array of stock symbols and it returns the corresponding stock data. It then calls the updateTable method to populate the FlexTable with the stock data.

The current client-side implementation:

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

To create the service, you will:

1.  define the service interface: StockPriceService
2.  implement the service: StockPriceServiceImpl

## Defining the service: the StockPriceService interface

In GWT, an RPC service is defined by an interface that extends the GWT [RemoteService](/javadoc/latest/com/google/gwt/user/client/rpc/RemoteService.html)  interface. For the StockPriceService interface, you need to define only one method: a method that will accept an array of stock symbols and return the data associated with each stock as an array of StockPrice objects.

1.  In the client subpackage, create an interface and name it StockPriceService.
    *  In Eclipse, in the Package Explorer pane, select the package `com.google.gwt.sample.stockwatcher.client`
    *  From the Eclipse menu bar, select `File > New > Interface`
    *  Eclipse opens a New Java Interface window.
2.  Fill in the New Java Interface window.
    *  At Name enter `StockPriceService`
    *  Accept the defaults for the other fields.
    *  Press `Finish`
    *  Eclipse creates stub code for the StockPriceService interface.
3.  Replace the stub with following code.

```java
package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("stockPrices")
public interface StockPriceService extends RemoteService {

  StockPrice[] getPrices(String[] symbols);
}
```

*  **Implementation Note:** Notice the @RemoteServiceRelativePath annotation. This associates the service with a default path relative to the module base URL.

## Implementing the service: the StockPriceServiceImpl class

Now create the class (StockPriceServiceImpl) that lives on the server. As defined in the interface, StockPriceServiceImpl will contain only one method, the method that returns the stock price data.

To do this, implement the service interface by creating a class that also extends the GWT [RemoteServiceServlet](/javadoc/latest/com/google/gwt/user/server/rpc/RemoteServiceServlet.html) class. RemoteServiceServlet does the work of deserializing incoming requests from the client and serializing outgoing responses.

### Packaging server-side code

The service implementation runs on the server as Java bytecode; it's not translated into JavaScript. Therefore, the service implementation does not have the same language constraints as the client-side code. To keep the code for the client separate from the code for the server, you'll put it in a separate package (com.google.gwt.sample.stockwatcher.server).

### Create a new class

1.  Create the service implementation for StockPriceService.
    *  In Eclipse, open the New Java Class wizard (File > New > Class).
2.  At Package, change the name from `.client` to `.server`
    *  Eclipse will create a package for the server-side code.
3.  At Name, enter `StockPriceServiceImpl`
    *  **Implementation Note:** By convention, the service implementation class is named after the service interface with the suffix Impl, so call the new class StockPriceServiceImpl.
4.  Extend the RemoteServiceServlet class.
    *  At Superclass, enter `com.google.gwt.user.server.rpc.RemoteServiceServlet`
5.  Implement the StockPriceService interface.
    *  At Interfaces, add an interface `com.google.gwt.sample.stockwatcher.client.StockPriceService`
6.  Inherit abstract methods.
7.  Press `Finish`
    *  Eclipse creates the package `com.google.gwt.sample.stockwatcher.server`
    *  Eclipse creates a stub StockPriceServiceImpl class.

        ```java
        package com.google.gwt.sample.stockwatcher.server;

        import com.google.gwt.sample.stockwatcher.client.StockPrice;
        import com.google.gwt.sample.stockwatcher.client.StockPriceService;
        import com.google.gwt.user.server.rpc.RemoteServiceServlet;

        public class StockPriceServiceImpl extends RemoteServiceServlet implements StockPriceService {

          public StockPrice[] getPrices(String[] symbols) {
            // TODO Auto-generated method stub
            return null;
          }
        }
        ```

### Write the server-side implementation

Replace the client-side implementation that returns random stock prices.

1.  Create instance variables to initialize the price and change data.
    ```java
    private static final double MAX_PRICE = 100.0; // $100.00
    private static final double MAX_PRICE_CHANGE = 0.02; // +/- 2%
    ```

2.  Replace the TODO with the following code. Return prices rather than null.
    ```java
    public StockPrice[] getPrices(String[] symbols) {
      Random rnd = new Random();

      StockPrice[] prices = new StockPrice[symbols.length];
      for (int i=0; i<symbols.length; i++) {
        double price = rnd.nextDouble() * MAX_PRICE;
        double change = price * MAX_PRICE_CHANGE * (rnd.nextDouble() * 2f - 1f);

        prices[i] = new StockPrice(symbols[i], price, change);
      }

      return prices;
    }
    ```

    Eclipse flags Random and suggests you include the import declaration.

3.  Include the import declaration from `java.util`, not from `com.google.gwt.user.client`.
    ```java
    import java.util.Random;
    ```

**Implementation Note:**
     Remember the service implementation runs on the server as Java bytecode, so you can use any Java class or library without worrying about whether it's translatable to JavaScript. In this case, you can use the Random class from the Java runtime library (java.util.Random) instead of the emulated GWT version (com.google.gwt.user.client.Random).

4.  This listing shows the completed StockPriceServiceImpl class.
    ```java
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
        for (int i=0; i<symbols.length; i++) {
          double price = rnd.nextDouble() * MAX_PRICE;
          double change = price * MAX_PRICE_CHANGE * (rnd.nextDouble() * 2f - 1f);

          prices[i] = new StockPrice(symbols[i], price, change);
        }

        return prices;
      }

    }
    ```

### Include the server-side code in the GWT module

The embedded servlet container (Jetty) can host the servlets that contain your service implementation.
This means you can take advantage of running your application in development mode while testing and debugging the server-side Java code.

To set this up, add `<servlet>` and `<servlet-mapping>` elements to the web application
deployment descriptor (web.xml) and point to the implementation class (StockPriceServiceImpl).

Starting with GWT 1.6, servlets should be defined in the web application deployment descriptor (web.xml) instead of the GWT module (StockWatcher.gwt.xml).

In the `<servlet-mapping>` element, the url-pattern can be in the form of an absolute directory path (for example, `/spellcheck` or `/common/login`).
If you specify a default service path with a @RemoteServiceRelativePath annotation on the service interface (as you did with StockPriceService), then make sure the url-pattern matches the annotation value.

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
            <servlet-name>stockPriceServiceImpl</servlet-name>
            <servlet-class>com.google.gwt.sample.stockwatcher.server.StockPriceServiceImpl</servlet-class>
          </servlet>
        
          <servlet-mapping>
            <servlet-name>stockPriceServiceImpl</servlet-name>
            <url-pattern>/stockwatcher/stockPrices</url-pattern>
          </servlet-mapping>
        
        </web-app>
        ```

##  Invoking the service from the client <a id="invoke"></a>

### Making asynchronous calls to the server

You need to add an AsyncCallback parameter to all your service methods.

To add an AsyncCallback parameter to all of our service methods, you must define a new interface as follows:

*   It must have the same name as the service interface, appended with _Async_ (for example, StockPriceServiceAsync).
*   It must be located in the same package as the service interface.
*   Each method must have the same name and signature as in the service interface with an important difference: the method has no return type and the last parameter is an AsyncCallback object.

1.  In the client subpackage, create an interface and name it StockPriceServiceAsync.
2.  Replace the stub with following code.

```java
package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface StockPriceServiceAsync {

  void getPrices(String[] symbols, AsyncCallback<StockPrice[]> callback);

}
```

**Tip:** The Google Plugin for Eclipse will generate an error when it finds a synchronous remote service without a matching asynchronous interface. You can right click on the error in Eclipse, select Quick Fix,
and choose the "Create asynchronous RemoteService interface" option to automatically generate the asynchronous interface.  

### Making the remote procedure call

#### Callback methods

When you call a remote procedure, you specify a callback method which executes when the call completes.

You specify the callback method by passing an [AsyncCallback](/javadoc/latest/com/google/gwt/user/client/rpc/AsyncCallback.html) object to the service proxy class.

The AsyncCallback object contains two methods, one of which is called depending on whether the call failed or succeeded: onFailure(Throwable) and onSuccess(T).

1.  Create the service proxy class.
    
    In the StockWatcher class, create an instance of the service proxy class by calling GWT.create(Class).

    ```java
    private ArrayList<String> stocks = new ArrayList<String>();
    private StockPriceServiceAsync stockPriceSvc = GWT.create(StockPriceService.class);
    ```

    Eclipse flags GWT.
    
2.  Initialize the service proxy class, set up the callback object, and make the call to the remote procedure.
    
    Replace the existing refreshWatchList method with the following code.

    ```java
    private void refreshWatchList() {
      // Initialize the service proxy.
      if (stockPriceSvc == null) {
        stockPriceSvc = GWT.create(StockPriceService.class);
      }

      // Set up the callback object.
      AsyncCallback<StockPrice[]> callback = new AsyncCallback<StockPrice[]>() {
        public void onFailure(Throwable caught) {
          // TODO: Do something with errors.
        }

        public void onSuccess(StockPrice[] result) {
          updateTable(result);
        }
      };

      // Make the call to the stock price service.
      stockPriceSvc.getPrices(stocks.toArray(new String[0]), callback);
    }
    ```
    
    Eclipse flags AsyncCallback.
    
3. Include the import declarations.

    ```java
    import com.google.gwt.core.client.GWT;
    import com.google.gwt.user.client.rpc.AsyncCallback;
    ```

### Test the Remote Procedure Call

At this point, you've created a service and pointed to it in the module XML file, set up the mechanism for making asynchronous calls, and generated a service proxy on the client side to call the service.  However there's a problem.

1.  Launch StockWatcher in development mode using the Eclipse debugger.
2.  Examine the error log in the Development Shell window.

```text
[ERROR] Type 'com.google.gwt.sample.stockwatcher.client.StockPrice' was not serializable
     and has no concrete serializable subtypes
    
```

In the service implementation (StockPriceServiceImpl), you inherited the code that serializes and deserializes Java objects by extending the RemoteServiceServlet class. 
However the problem is that we did not also edit the StockPrice class to indicate it was serializable.

##  Serializing Java objects <a id="serialize"></a>

Serialization is the process of packaging the contents of an object so that it can moved from one application to another application or stored for later use.
Anytime you transfer an object over the network via GWT RPC, it must be serialized.
Specifically, GWT RPC requires that all service method parameters and return types be serializable.

A type is serializable and can be used in a service interface if one of the following is true:

*   All primitive types (int, char, boolean, etc.) and their wrapper objects are serializable by default.
*   An array of serializable types is serializable by extension.
*   A class is serializable if it meets these three requirements:

    *   It implements either Java [Serializable](http://java.sun.com/j2se/1.5.0/docs/api/java/io/Serializable.html) or GWT [IsSerializable](/javadoc/latest/com/google/gwt/user/client/rpc/IsSerializable.html) interface, either directly, or because it derives from a superclass that does.
    *   Its non-final, non-transient instance fields are themselves serializable, and
    *   It has a default (zero argument) constructor with any access modifier (e.g. private Foo(){} will work)

GWT honors the _transient_ keyword, so values in those fields are not serialized (and thus, not sent over the wire when used in RPC calls).

**Note:** GWT serialization is not the same as serialization based on the Java Serializable interface.

For more information on what is and is not serializable in GWT, see the Developer's Guide, [Serializable Types](../DevGuideServerCommunication.html#DevGuideSerializableTypes).

#### Serializing StockPrice

Based on the requirements for serialization, what do you need to do to make the StockPrice class ready for GWT RPC?
Because all its instance fields are primitive types, all you need to do in this
case is implement the Serializable or IsSerializable interface.

```java
package com.google.gwt.sample.stockwatcher.client;
import java.io.Serializable;

public class StockPrice implements Serializable {

  private String symbol;
  private double price;
  private double change;

  ...
```

1.  Refresh StockWatcher in development mode.
2.  Add a stock.
3.  StockWatcher adds the stock to the table; the Price and Change fields contain data and there are no errors.

       *  Although StockWatcher doesn't look any different on the surface, underneath it is now retrieving its stock price updates from the server-side StockPriceService servlet on the embedded servlet container instead of generating them on the client-side.

At this point, the basic RPC mechanism is working. However, there is one TODO left. You need to code the error handling in case the callback fails.

##  Handling Exceptions <a id="exceptions"></a>

When a remote procedure call fails, the cause falls into one of two categories: an unexpected exception or a checked exception. In either case you want to handle the exception and, if necessary, provide feedback to the user.

**Unexpected exceptions:** Any number of unexpected occurrences could cause the call to a remote procedure to fail: the network could be down; the HTTP server on the other end might not be listening; the DNS server could be on fire, and so forth.

Another type of unexpected exception can occur if GWT is able to invoke the service method, but the service implementation throws an undeclared exception. For example, a bug may cause a NullPointerException.

When unexpected exceptions occur in the service implementation, you can find the full stack trace in the development mode log.
On the client-side, the onFailure(Throwable) callback method will receive an InvocationException with the generic message: The call failed on the server; see server log for details.

**Checked exceptions:** If you know a service method might throw a particular type of exception and you want the client-side code to be able to handle it, you can use checked exceptions. GWT supports the _throws_ keyword so you can add it to your service interface methods as needed. When checked exceptions occur in an RPC service method, GWT will serialize the exception and send it back to the caller on the client for handling.

### Creating a checked exception

To learn how to handle errors in RPC, you will throw an error when the user adds a stock code which you've defined as "delisted"&mdash;causing the call to fail. Then you'll handle the failure by displaying an error message to the user.

#### Checking for and throwing the exception

##### Create a class: DelistedException

1.  Create a class to identify delisted stock codes.
    *  In Eclipse, open the New Java Class wizard (File > New > Class).
2.  At Name, enter `DelistedException`
3.  Extend the java.lang.Exception class.
4.  Implement the java.io.Serializable interface.
    *  This exception is designed to be sent by RPC; therefore, this class must be serializable.
5.  Inherit abstract methods.
6.  Press `Finish`
    *  Eclipse creates a stub DelistedException class.
7.  Replace the stub with the following code.

    ```java
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
    }
    ```

##### Update the stock price service interface: StockPriceService

1.  In the service interface (StockPriceService), append a _throws_ declaration to the getPrices(String[])  method.
    
    In StockPriceService.java, make the changes highlighted below.

```java
package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("stockPrices")
public interface StockPriceService extends RemoteService {

  StockPrice[] getPrices(String[] symbols) throws DelistedException;
}
```

##### Update the stock price service implementation: StockPriceServiceImpl

You need to make two changes to the service implementation (StockPriceServiceImpl).

1.  Make the corresponding change to the getPrices(String[]) method.
    *  Append a _throws_ declaration.
    *  Include the import declaration for DelistedException.
2.  Add the code to throw the Delisted Exception.
    *  For the sake of this example, keep it simple and only throw the exception when the stock symbol ERR is added to the watch list.
3.  This listing shows the completed StockPriceServiceImpl class.

```java
import com.google.gwt.sample.stockwatcher.client.DelistedException;
    
...

public StockPrice[] getPrices(String[] symbols) throws DelistedException {
  Random rnd = new Random();

  StockPrice[] prices = new StockPrice[symbols.length];

  for (int i=0; i<symbols.length; i++) {
    if (symbols[i].equals("ERR")) {
      throw new DelistedException("ERR");
    }

    double price = rnd.nextDouble() * MAX_PRICE;
    double change = price * MAX_PRICE_CHANGE * (rnd.nextDouble() * 2f - 1f);

    prices[i] = new StockPrice(symbols[i], price, change);
  }

  return prices;
}
```

At this point you've created the code that will throw the exception. You don't need to add the throws declaration to the service method in StockPriceServiceAsync.java. That method will always return immediately (remember, it's asynchronous). Instead you'll receive any thrown checked exceptions when GWT calls the onFailure(Throwable) callback method.

#### Displaying error messages

In order to display the error, you will need a new UI component. First think a moment about how you want to display the error. An obvious solution would be to display a pop-up alert using Window.alert(String). This could work in the case where the user receives the error while entering stock codes. But in a more real-world example, what would you want to happen if stock was delisted after the user had added it to the watch list? or if you need to display multiple errors? In these cases, a pop-up alert is not the best approach.

So, to display any messages about failing to retrieve stock data, you'll implement a Label widget.

1.  Define a style for the error message so that it will attract the user's attention. 

    Serializing StockPrice
rule that applies to any element with a class attribute of errorMessage.

    ```css
    .negativeChange {
      color: red;
    }
    
    .errorMessage {
      color: red;
    }
    ```

2.  To hold the text of the error message, add a Label widget.

    In StockWatcher.java, add the following instance field.

    ```java
    private StockPriceServiceAsync stockPriceSvc = GWT.create(StockPriceService.class);
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

#### Handling the error

Now that you've built a Label widget to display the error, you can finish writing the error handling code.

You will also want to hide the error message if the error is corrected (for example, if the user removes the ERR code from the stock table).

1.  Specify the action to take if the callback fails.
    *  In StockWatcher.java, in the refreshWatchList method, fill in the onFailure method as follows.

        ```java
        public void onFailure(Throwable caught) {
          // If the stock code is in the list of delisted codes, display an error message.
          String details = caught.getMessage();
          if (caught instanceof DelistedException) {
            details = "Company '" + ((DelistedException) caught).getSymbol() + "' was delisted";
          }

          errorMsgLabel.setText("Error: " + details);
          errorMsgLabel.setVisible(true);
        }
        ```

2.  If the error is corrected, hide the error message widget.
    *  In the updateTable(StockPrice[] prices) method, clear any errors.

        ```java
        private void updateTable(StockPrice[] prices) {
          for (int i=0; i < prices.length; i++) {
            updateTable(prices[i]);
          }

          // Display timestamp showing last refresh.
          lastUpdatedLabel.setText("Last update : " +
              DateTimeFormat.getMediumDateTimeFormat().format(new Date()));

          // Clear any errors.
          errorMsgLabel.setVisible(false);
        }
        ```

### Test Exception Handling in RPC

At this point, you've created and checked for an exception&mdash;the delisted stock code "ERR". When StockWatcher makes a remote procedure call to retrieve stock data for the delisted stock code, the call fails, an exception is thrown, and then handled by displaying an error message.

1.  Refresh StockWatcher in development mode.
2.  Add the stock code, ERR.
3.  StockWatcher displays a red error message. Valid stock code data stops being refreshed.
    *  ![screenshot: RPC error message](images/RPCerrormessage.png)

4.  Delete the stock code, ERR.
5.  The error message is removed. Valid stock code data resumes being refreshed.

## What's Next

At this point, you've made remote procedure calls to get stock data from the server.

For more information about GWT RPC, see the Developer's Guide, [Remote Procedure Calls](../DevGuideServerCommunication.html#DevGuideRemoteProcedureCalls).

### Deploying services into production <a id="deploy"></a>

During testing, you can use development mode's built-in servlet container to test the server-side code for your RPC services&mdash;just as you did in this tutorial. When you deploy your GWT application, you can use any servlet container to host your services. Make sure your client code invokes the service using the URL the servlet is mapped to in the web.xml configuration file.

To learn how to deploy GWT RPC servlets to a production server, see the Developer's Guide, [Deploying RPC](../DevGuideServerCommunication.html#DevGuideRPCDeployment)
