
<style>
code, .code {font-size: 9pt; font-family: Courier, Courier New, monospace; color:#007000;}
.highlight {background-color: #ffc;}
.strike {text-decoration:line-through; color:red;}
.header {margin-top: 1.5ex;}
.details {margin-top: 1ex;}
</style>

<p>
At this point, you've built the user interface from GWT widgets and panels and wired in the event handlers. StockWatcher accepts input but it doesn't yet add the stock to the stock table or update any stock data.
</p>
<p>
In this section, you'll finish implementing all of StockWatcher's client-side functionality. Specifically, you'll write the code to the following:
</p>
<ol>
    <li><a href="#addStock">Add and remove stocks from the stock table.</a></li>
    <li><a href="#refreshPrice">Refresh the Prices and Change fields for each stock in the table.</a></li>
    <li><a href="#timestamp">Implement the timestamp showing the time of last update.</a></li>
</ol>
<p>
Your initial implementation of StockWatcher is simple enough that your can code all its functionality on the client side. Later you'll add calls to the server to retrieve the stock data.
</p>

<a name="addStock"></a>
<h2>1. Adding and removing stocks from the stock table</h2>
<p>Your first task is to add the stock code and a Remove button to the stock table. Remember, the FlexTable will automatically resize to hold the data, so you don't have to worry about writing code to handle that.</p>
<ol type="A">
    <li>Create a data structure.</li>
    <li>Add rows to the stock table.</li>
    <li>Add a button to remove stocks from the stock table.</li>
    <li>Test in development mode.</li>
</ol>
<h3>A. Create a data structure</h3>
<p>
You need a data structure to hold the list of stock symbols the user has entered. Use the standard Java ArrayList and call the list stocks.
</p>
<ol class="instructions">
    <li>
    <div class="header">Create a data structure.</div>
    <div class="details">In StockWatcher.java, in the StockWatcher class, create a new instance of a Java ArrayList.</div>
    <div class="details"><pre class="code">
public class StockWatcher implements EntryPoint {

  private VerticalPanel mainPanel = new VerticalPanel();
  private FlexTable stocksFlexTable = new FlexTable();
  private HorizontalPanel addPanel = new HorizontalPanel();
  private TextBox newSymbolTextBox = new TextBox();
  private Button addStockButton = new Button("Add");
  private Label lastUpdatedLabel = new Label();<span class="highlight">
  private ArrayList&lt;String&gt; stocks = new ArrayList&lt;String&gt;();</span>
    </pre></div>
    </li>
    <li>
    <div class="header">Eclipse flags ArrayList and suggests you include the import declaration.</div>
    </li>
    <li>
    <div class="header">Include the import declaration.</div>
    <div class="details"><pre class="code"><span class="highlight">import java.util.ArrayList;</span></pre></div>
    </li>
</ol>

<h3>B. Add rows to the flex table</h3>
<p>
After the user enters a stock code, first check to make sure it's not a duplicate. If the stock code doesn't exist, add a new row to the FlexTable and populate the cell in the first column (column 0) with the stock symbol that the user entered. To add text to a cell in the FlexTable, call the setText method.
</p>
<ol class="instructions">
    <li>
        <div class="header">Check the stock to see if it exists and if it does, don't add it again.</div>
        <div class="details">In the addStock method, replace the TODO comment with this code.</div>
        <div class="details"><pre class="code">
<span class="highlight">    // Don't add the stock if it's already in the table.
    if (stocks.contains(symbol))
      return;</span></pre></div>
    </li>
    <li>
        <div class="header">If the stock doesn't exist, add it.</div>
        <div class="details">In the addStock method, replace the TODO comment with this code.</div>
        <div class="details"><pre class="code">
<span class="highlight">    // Add the stock to the table.
    int row = stocksFlexTable.getRowCount();
    stocks.add(symbol);
    stocksFlexTable.setText(row, 0, symbol);</span></pre></div>
        <div class="details">When you call the setText method, the FlexTable automatically creates new cells as needed; therefore, you don't need to resize the table explicitly.</div>
    </li>
</ol>

<h3>C. Add a button to remove stocks from the stock list</h3>
<p>
So that users can delete a specific stock from the list, insert a Remove button in the last cell of the table row. To add a widget to a cell in the FlexTable, call the setWidget method. Subscribe to click events with the addClickHandler method. If the Remove Stock button publishes a click event, remove the stock from the FlexTable and the ArrayList.
</p>

<ol class="instructions">
    <li>
    <div class="header">Add the button for deleting the stock from the list.</div>
    <div class="details">In the addStock method, replace the TODO comment with this code.</div>
    <div class="details"><pre class="code">
<span class="highlight">    // Add a button to remove this stock from the table.
    Button removeStockButton = new Button("x");
    removeStockButton.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        int removedIndex = stocks.indexOf(symbol);
        stocks.remove(removedIndex);        stocksFlexTable.removeRow(removedIndex + 1);
      }
    });
    stocksFlexTable.setWidget(row, 3, removeStockButton);</span></pre></div>
    </li>
</ol>

<h3>D. Test Add/Remove Stock Functionality</h3>
<p>
You have one more TODO to code: get the stock price. But first do a quick check in development mode to see if Add Stock and Remove Stock functionality is working as expected.
</p>

<p>
At this point when you enter a stock code, StockWatcher should add it to the stock table. Try it and see.
</p>
<ol class="instructions">
    <li>
        <div class="header">Run StockWatcher in development mode.</div>
        <div class="details">Press Refresh in the already open browser.</div>
    </li>
    <li>
        <div class="header">Add a stock.</div>
        <div class="details">Enter stock codes in the input box.</div>
        <div class="details">StockWatcher should add the stock to the table. The table resizes to hold the new data. However, the Price and Change fields are still empty. If you enter a stock code in lowercase, it converts the letters to uppercase.</div>
    </li>
     <li>
        <div class="header">Verify that you can't add duplicate stocks to the table.</div>
        <div class="details">Add a stock code that already exist in the table.</div>
        <div class="details">StockWatcher should clear the input box but not add the same stock code again.</div>
    </li>
    <li>
        <div class="header">Delete a stock.</div>
        <div class="details">Click the Remove button.</div>
        <div class="details">The stock is deleted from the table and the table resizes.</div>
    </li>
</ol>
<p>
<img src="images/CodeClientAddStock.png" alt="StockWatcher, Add/Remove Functionality" />
</p>
<p>
Now you'll tackle that last TODO: get the stock price.
</p>

<a name="refreshPrice"></a>
<h2>2. Refreshing the Price and Change fields</h2>
<p>
The most important feature of StockWatcher is updating the prices of the stocks the users are watching. If you were writing StockWatcher using traditional web development techniques, you would have to rely on full page reloads every time you wanted to update the prices. You could accomplish this either manually (making the user click the browser's Refresh button) or automatically (for example, using a &lt;meta http-equiv="refresh" content="5"&gt; tag in the HTML header). But in this age of Web 2.0, that's simply not efficient enough. StockWatcher's users want their stock price updates and they want them now...without waiting for a full page refresh.
</p>
<p>In this section, you'll:</p>
<ol type="A">
    <li>Automatically refresh the Price and Change fields by implementing a timer and specifying a refresh rate.</li>
    <li>Encapsulate the stock price data by creating a class, StockPrice.</li>
    <li>Generate the stock data for the Price and Change fields by implementing the refreshWatchList method.</li>
    <li>Load the Price and Change fields with the stock data by implementing the updateTable method.</li>
    <li>Test the random generation of stock prices and change values</li>
</ol>

<h3>A. Automatically refresh stock data</h3>
<p>
GWT makes it easy to update an application's content on the fly. For StockWatcher, you'll automatically update stock prices by using the GWT Timer class.
</p>
<p>
Timer is a single-threaded, browser-safe timer class. It enables you to schedule code to run at some point in the future, either once using the schedule() method or repeatedly using the scheduleRepeating() method. Because you want StockWatcher to automatically update the stock prices every five seconds, you'll use scheduleRepeating().
</p>
<p>
When a Timer fires, the run method executes. For StockWatcher you'll override the run method with a call to the refreshWatchList method which refreshes the Price and Change fields. For now, just put in a stub for the refreshWatchList method; later in this section, you'll implement it.
</p>
<ol class="instructions">
    <li>
        <div class="header">Implement the timer.</div>
        <div class="details">Modify the onModuleLoad method to create a new instance of Timer, as follows:
        <pre class="code">
  public void onModuleLoad() {

    ...

    // Move cursor focus to the text box.
    newSymbolTextBox.setFocus(true);

<span class="highlight">    // Setup timer to refresh list automatically.
    Timer refreshTimer = new Timer() {
      @Override
      public void run() {
        refreshWatchList();
      }
    };
    refreshTimer.scheduleRepeating(REFRESH_INTERVAL);</span>

    ...

  }</pre></div>
        <div class="header">Eclipse flags Timer, REFRESH_INTERVAL and refreshWatchList.</div>
    </li>
    <li>
    <div class="header">Declare the import for Timer.</div>
    <div class="details">If you are using Eclipse shortcuts, be sure to select the GWT Timer.</div>
    <div class="details"><pre class="code"><span class="highlight">import com.google.gwt.user.client.Timer;</span></pre></div>
    </li>
    <li>
    <div class="header">Specify the refresh rate.</div>
    <div class="details">If you are using Eclipse shortcuts, select <code>Create constant 'REFRESH_INTERVAL'</code> then specify the refresh interval in milliseconds, <code>5000</code>.</div>
    <div class="details">Otherwise, just cut and paste from the highlighted code below.</div>
    <div class="details"><pre class="code">
public class StockWatcher implements EntryPoint {<span class="highlight">

  private static final int REFRESH_INTERVAL = 5000; // ms</span>
  private VerticalPanel mainPanel = new VerticalPanel();
    </pre></div>
    </li>
    <li>
    <div class="header">Populate the price and change values as soon as a new stock is added.</div>
    <div class="details">In the addStock method, replace the TODO comment with the highlighted code.</div>
    <div class="details"><pre class="code">
  private void addStock() {

    ...

    // Add a button to remove a stock from the table.
    Button removeStockButton = new Button("x");
    removeStockButton.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        int removedIndex = stocks.indexOf(symbol);
        stocks.remove(removedIndex);
        stocksFlexTable.removeRow(removedIndex + 1);
      }
    });
    stocksFlexTable.setWidget(row, 3, removeStockButton);

<span class="highlight">    // Get the stock price.
    refreshWatchList();</span>

  }
    </pre></div>
        <div class="header">Eclipse flags refreshWatchList.</div>

    </li>
    <li>
        <div class="header">In the StockWatcher class, create a stub for the refreshWatchList method.</div>
        <div class="details"><pre class="code">
<span class="highlight">  private void refreshWatchList() {
    // TODO Auto-generated method stub

  }</span></pre></div>


    </li>
</ol>



<h3>B. Encapsulate stock price data</h3>
<div style="float:right;"><img alt="screenshot: Eclipse New Class Window" src="images/CodeClientNewJavaClass.jpg" /></div>
<h4>Creating a Java class using Eclipse</h4>
<p>
One of the primary ways GWT speeds AJAX development is by allowing you to write your applications in the Java language. Because of this, you can take advantage of static type checking and time-tested patterns of object-oriented programming. These, when combined with modern IDE features like code completion and automated refactoring, make it easier than ever to write robust AJAX applications with well-organized code bases.
</p>
<p>
For StockWatcher, you'll take advantage of this capability by factoring stock price data into its own class.
</p>

<ol class="instructions">
    <li>
        <div class="header">Create a new Java class named StockPrice.</div>
        <div class="details">In Eclipse, in the Package Explorer pane, select the package <code>com.google.gwt.sample.stockwatcher.client</code></div>
        <div class="details">From the Eclipse menu bar, select <code>File &gt; New &gt; Class</code></div>
        <div class="details">Eclipse opens a New Java Class window.</div>
    </li>
    <li>
        <div class="header">Fill in the New Class Window.</div>
        <div class="details">At Name enter <code>StockPrice</code></div>
        <div class="details">Accept the defaults for the other fields.</div>
        <div class="details">Press <code>Finish</code></div>
    </li>
    <li style="clear:both;">
        <div class="header">Eclipse creates stub code for the StockPrice class.</div>
        <div class="details"><pre class="code">
<span class="highlight">package com.google.gwt.sample.stockwatcher.client;

public class StockPrice {

}</span></pre></div>
    </li>
    <li>
        <div class="header">Replace the stub with following code.</div>
        <div class="details"><pre class="code">
package com.google.gwt.sample.stockwatcher.client;

public class StockPrice {

<span class="highlight">  private String symbol;
  private double price;
  private double change;

  public StockPrice() {
  }

  public StockPrice(String symbol, double price, double change) {
    this.symbol = symbol;
    this.price = price;
    this.change = change;
  }

  public String getSymbol() {
    return this.symbol;
  }

  public double getPrice() {
    return this.price;
  }

  public double getChange() {
    return this.change;
  }

  public double getChangePercent() {
    return 10.0 * this.change / this.price;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public void setChange(double change) {
    this.change = change;
  }</span>
}</pre></div>
    </li>
</ol>

<h3>C. Generate the stock data</h3>
<p>
Now that you have a StockPrice class to encapsulate stock price data, you can generate the actual data. To do this you'll implement the refreshWatchList method. Remember the refreshWatchList method is called both when the user adds a stock to the stock table, and then every 5 seconds when the timer fires.
</p>
<h4>Randomly generating the data</h4>
<p>
In lieu of retrieving real-time stock prices from an online data source, you'll create pseudo-random price and change values. To do this, use the GWT Random class. Then populate an array of StockPrice objects with these values and pass them on to the updateTable method.
</p>
<ol class="instructions">
    <li>
        <div class="header">Generate random stock prices.</div>
        <div class="details">In the StockWatcher class, replace the stub refreshWatchList method with the following code.</div>
        <div class="details"><pre class="code">
<span class="highlight">  /**
   * Generate random stock prices.
   */</span>
  private void refreshWatchList() {
<span class="highlight">    final double MAX_PRICE = 100.0; // $100.00
    final double MAX_PRICE_CHANGE = 0.02; // +/- 2%

    StockPrice[] prices = new StockPrice[stocks.size()];
    for (int i = 0; i &lt; stocks.size(); i++) {
      double price = Random.nextDouble() * MAX_PRICE;
      double change = price * MAX_PRICE_CHANGE
          * (Random.nextDouble() * 2.0 - 1.0);

      prices[i] = new StockPrice(stocks.get(i), price, change);
    }

    updateTable(prices);</span>
  }</pre></div>
        <div class="details">Eclipse flags Random and updateTable.</div>
    </li>
    <li>
        <div class="header">Include the import declaration.</div>
        <div class="details"><pre class="code"><span class="highlight">import com.google.gwt.user.client.Random;</span></pre></div>
    </li>
    <li>
        <div class="header">Create a stub for the updateTable(StockPrice[]) method.</div>
        <div class="details"><pre class="code">
<span class="highlight">  private void updateTable(StockPrice[] prices) {
    // TODO Auto-generated method stub

  }</span></pre></div>
    </li>
</ol>

<h3>D. Populate the Price and Change fields</h3>
<p>
Finally, load the randomly generated price and change data into the StockWatcher table. For each stock, format the Price and Change columns, then load the data. To do this you'll implement two methods in the StockWatcher class.
</p>
<ul>
    <li>Format the values in the Price field to two decimal places (1,956.00).</li>
    <li>Prefix the values in the Change field with a sign (+/-).</li>
</ul>
<ol class="instructions">
    <li>
        <div class="header">Implement the method updateTable(StockPrices[]).</div>
        <div class="details">Replace the stub with the following code.</div>
        <div class="details"><pre class="code">
<span class="highlight">  /**
   * Update the Price and Change fields all the rows in the stock table.
   *
   * @param prices Stock data for all rows.
   */</span>
  private void updateTable(StockPrice[] prices) {
<span class="highlight">    for (int i = 0; i &lt; prices.length; i++) {
      updateTable(prices[i]);
    }</span>

  }</pre></div>
        <div class="header">Eclipse flags updateTable.</div>
        <div class="details">Create a stub for the updateTable(StockPrice) method.</div>

     </li>
     <li>
        <div class="header">Implement the method updateTable(StockPrice).</div>
        <div class="details">Replace the stub with the following code.</div>
        <div class="details"><pre class="code">
<span class="highlight">  /**
   * Update a single row in the stock table.
   *
   * @param price Stock data for a single row.
   */
  private void updateTable(StockPrice price) {
    // Make sure the stock is still in the stock table.
    if (!stocks.contains(price.getSymbol())) {
      return;
    }

    int row = stocks.indexOf(price.getSymbol()) + 1;

    // Format the data in the Price and Change fields.
    String priceText = NumberFormat.getFormat("#,##0.00").format(
        price.getPrice());
    NumberFormat changeFormat = NumberFormat.getFormat("+#,##0.00;-#,##0.00");
    String changeText = changeFormat.format(price.getChange());
    String changePercentText = changeFormat.format(price.getChangePercent());

    // Populate the Price and Change fields with new data.
    stocksFlexTable.setText(row, 1, priceText);
    stocksFlexTable.setText(row, 2, changeText + " (" + changePercentText
        + "%)");
  }</span></pre></div>
    <div class="details">Eclipse flags NumberFormat.</div>
    </li>
    <li>
        <div class="header">Include the import declaration.</div>
        <div class="details"><pre class="code">
<span class="highlight">import com.google.gwt.i18n.client.NumberFormat;</span></pre></div>
    </li>
</ol>

<h3>E. Test the random generation of stock prices and change values</h3>
<p>
At this point, the Price and Change fields should be populated with the stock data you randomly generated. Try it and see.
</p>
<ol class="instructions">
    <li>
        <div class="header">Run StockWatcher in development mode.</div>
    </li>
    <li>
        <div class="header">Add a stock.</div>
        <div class="details">The Price and Change fields should have data.</div>
        <div class="details">Every 5 seconds, the data should refresh.</div>
    </li>
</ol>

<a name="timestamp"></a>
<h2>3. Adding the timestamp</h2>
<p>
The final piece of functionality you need to implement is the timestamp. You used a Label widget, lastUpdatedLabel, to create the timestamp in the UI. Now set the text for the Label widget. Add this code to the updateTable(StockPrice[]) method.
</p>
<ol class="instructions" type="A">
    <li>
        <div class="header">Implement the timestamp.</div>
        <div class="details">In the updateTable(StockPrice[]) method, copy and paste the highlighted code.</div>
        <div class="details"><pre class="code">
  /**
   * Update the Price and Change fields all the rows in the stock table.
   *
   * @param prices Stock data for all rows.
   */
  private void updateTable(StockPrice[] prices) {
    for (int i = 0; i &lt; prices.length; i++) {
      updateTable(prices[i]);
    }

<span class="highlight">    // Display timestamp showing last refresh.
    lastUpdatedLabel.setText("Last update : "
        + DateTimeFormat.getMediumDateTimeFormat().format(new Date()));</span>

  }</pre></div>
        <div class="details">Eclipse flags DateTimeFormat and Date.</div>
    </li>
    <li>
        <div class="header">Include the imports.</div>
        <div class="details"><pre class="code">
<span class="highlight">import com.google.gwt.i18n.client.DateTimeFormat;

import java.util.Date;</span>
        </pre></div>
    </li>
    <li>
        <div class="header">Test the timestamp.</div>
        <div class="details">Save your changes. In the browser, press Refresh to load the changes.</div>
        <div class="details">The timestamp label should be displayed beneath the stock table. As the Price and Change fields refresh, the timestamp should display the date and time of the last update.</div>
    </li>
</ol>
<p class="note">
<b>Implementation Note:</b> You may have noticed that the classes DateTimeFormat and NumberFormat live in a subpackage of com.google.gwt.i18n, which suggest that they deal with internationalization in some way. And indeed they do: both classes will automatically use your application's locale setting when formatting numbers and dates. You'll learn more about localizing and translating your GWT application into other languages in the tutorial <a href="i18n.html">Internationalizing a GWT Application</a>.
</p>

<h2>What's Next</h2>
<p>
At this point you've built the interface components and coded all the underlying client-side functionality for the StockWatcher application. The user can add and remove stocks. The Price and Change fields update every 5 seconds. A timestamp shows when the last refresh occurred.</p>
<img src="images/CodeClientBug.png" alt="screenshot StockWatcher Bug" />
<p>
Although you have not compiled StockWatcher yet, you can test it in production mode here: Run StockWatcher
</p>
<h3>A Bug</h3>
<p>
For the sake of this tutorial, we introduced an error into the code. Can you detect it?
</p>
<p>
Look at the change percentages. Don't they seem a bit small? If you do the math, you'll discover that they appear to be exactly an order of magnitude smaller than they should be. There's an arithmetic error hiding somewhere in the StockWatcher code. Using the tools provided by GWT and your Java IDE, your next step is to find and fix the error.
</p>
<p>
<a href="debug.html">Step 6: Debugging a GWT Application</a>
</p>

