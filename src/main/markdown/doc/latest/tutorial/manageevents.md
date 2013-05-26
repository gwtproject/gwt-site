

<style>
code, .code {font-size: 9pt; font-family: Courier, Courier New, monospace; color:#007000;}
.highlight {background-color: #ffc;}
.strike {text-decoration:line-through; color:red;}
.header {margin-top: 1.5ex;}
.details {margin-top: 1ex;}
</style>

<p>
At this point, you've created all the elements of the interface. Like many user interface frameworks, GWT is event-based. This means that the code executes in response to some event occurring. Most often, that event is triggered by the user, who uses the mouse or keyboard to interact with the application interface.
</p>
<p>
In this section, you'll wire up your widgets to listen for and handle mouse and keyboard events.
</p>
<ol>
    <li><a href="#requirements">Review the functional requirements.</a></li>
    <li><a href="#listening">Listen for events.</a></li>
    <li><a href="#responding">Respond to events.</a></li>
    <li><a href="#testing">Test event handling.</a></li>
</ol>
<a name="requirements"></a>
<h2>1. Reviewing the requirements for event handling</h2>
<p>
Let's review the StockWatcher requirements to see what events occur.
</p>
    <table>
    <tr>
        <th>Task</th>
        <th>UI Event (Trigger mechanism)</th>
        <th>Response</th>
    </tr>
    <tr>
        <td>User enters a stock code.</td>
        <td>Clicks the Add button<br />or presses return in the input box.</td>
        <td><ul><li>Verify input.</li><li>Check if stock already exists.</li><li>Add a new row.</li><li>Create a delete button.</li></ul></td>
    </tr>
    <tr>
        <td>User deletes stock from the table.</td>
        <td>Presses the Remove button.</td>
        <td><ul><li>Remove row from table.</li></ul></td>
    </tr>
</table>
<p>
GWT provides a number of different event handler interfaces. To handle click events on the Add and Remove buttons, you'll use the <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/event/dom/client/ClickHandler.html">ClickHandler</a> interface. To handle keyboard events in the input box, you'll use the <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/event/dom/client/KeyPressHandler.html">KeyPressHandler</a> interface.
</p>

<p class="note">
Starting with GWT 1.6, the ClickHandler, KeyDownHandler, KeyPressHandler, and KeyUpHandler interfaces have replaced the now deprecated ClickListener and KeyBoardListener interfaces.
</p>

<a name="listening"></a>
<h2>2. Listening for events</h2>

<h3>Event Handler Interfaces</h3>
<p>
Events in GWT use the event handler interface model similar to other user interface frameworks. To subscribe to an event, you pass a particular event handler interface to the appropriate widget. An event handler interface defines one or more methods that the widget then calls to announce (publish) an event.
</p>

<h3>Handling Mouse Events</h3>
<p>One way StockWatcher users can enter a stock code is by using their mouse to click on the Add button.</p>
<p>
You'll handle the Add button's click event by passing it an object that implements the ClickHandler interface. In this case, you'll use an anonymous inner class to implement ClickHandler. The ClickHandler interface has one method, onClick, which fires when the user clicks on the widget.
</p>
<p>
When the user clicks on the Add button, StockWatcher should respond by adding the stock to the stock table. So, to handle the click event, call the addStock method. You haven't written the addStock method yet; you'll create a stub and then code it in the next section.
</p>
<ol class="instructions">
    <li>
        <div class="header">Add an event handler to the Add button so it can receive click events.</div>
        <div class="details">In Stockwatcher.java, in the onModuleLoad method, cut and paste the code commented "Listen for mouse events on the Add button." that is highlighted below.</div>
        <div class="details">Eclipse flags ClickHandler and suggests you include the import declaration.</div>
    </li>
    <li>
        <div class="header">Include the import declarations for ClickHandler and ClickEvent.</div>
        <div class="details">Eclipse flags addStock.</div>
    </li>
    <li>
        <div class="header">In StockWatcher.java, create the stub for the addStock method.</div>
        <div class="details">Select the Eclipse shortcut, <code>Create method addStock() in type 'StockWatcher'</code>. Or copy and paste from the code highlighted below.</div>
        <div class="details"><b>Note:</b> Depending on your Eclipse configuration, it might create the addStock method with an access modifier of protected. You aren't going to subclass StockWatcher, so later when you implement the addStock method, you'll change its access to private.</div>
<div class="details"><pre class="code">
package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.core.client.EntryPoint;
<span class="highlight">import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;</span>
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class StockWatcher implements EntryPoint {

  private VerticalPanel mainPanel = new VerticalPanel();
  private FlexTable stocksFlexTable = new FlexTable();
  private HorizontalPanel addPanel = new HorizontalPanel();
  private TextBox newSymbolTextBox = new TextBox();
  private Button addStockButton = new Button("Add");
  private Label lastUpdatedLabel = new Label();

  /**
   * Entry point method.
   */
  public void onModuleLoad() {
    // Create table for stock data.
    stocksFlexTable.setText(0, 0, "Symbol");
    stocksFlexTable.setText(0, 1, "Price");
    stocksFlexTable.setText(0, 2, "Change");
    stocksFlexTable.setText(0, 3, "Remove");

    // Assemble Add Stock panel.
    addPanel.add(newSymbolTextBox);
    addPanel.add(addStockButton);

    // Assemble Main panel.
    mainPanel.add(stocksFlexTable);
    mainPanel.add(addPanel);
    mainPanel.add(lastUpdatedLabel);

    // Associate the Main panel with the HTML host page.
    RootPanel.get("stockList").add(mainPanel);

    // Move cursor focus to the input box.
    newSymbolTextBox.setFocus(true);

<span class="highlight">    // Listen for mouse events on the Add button.
    addStockButton.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        addStock();
      }
    });</span>

  }

<span class="highlight">  /**
   * Add stock to FlexTable. Executed when the user clicks the addStockButton or
   * presses enter in the newSymbolTextBox.
   */
  private void addStock() {
    // TODO Auto-generated method stub

  }</span>

}</pre></div>
    </li>
</ol>
<p class="note">
<b>Implementation Note:</b>  For smaller applications, such as StockWatcher, that handle relatively few events, using anonymous inner classes gets the job done with minimal coding. However, if you have large number of event handlers subscribing to events, this approach can be inefficient because it could result in the creation of many separate event handler objects. In that case, it's better to have a class implement the event handler interface and handle events coming from multiple event publishers.  You can distinguish the source of the event by calling its getSource() method.  This makes better use of memory but requires slightly more code. For a code example, see the Developer's Guide, <a href="../DevGuideUiHandlers.html">Event Handlers</a>.
</p>


<h3>Handling Keyboard Events</h3>
<p>
In addition to using the Add button, StockWatcher users can enter a stock code without taking their hands from the keyboard by pressing return in the input box.
</p>
<p>
To subscribe to keyboard events, you can call the addKeyPressHandler(KeyPressHandler) method and pass it a KeyPressHandler.
</p>
<p class="note">
For the enter key, you however have to use either KeyDownHandler or KeyUpHandler as Firefox won't correctly populate the KeyPressEvent; see <a href="https://code.google.com/p/google-web-toolkit/issues/detail?id=5003">issue 5003</a>.
</p>


<ol class="instructions">
    <li>
        <div class="header">Hook up the keydown event handler for the input box, newSymbolTextBox.</div>
        <div class="details">In the onModuleLoad method, cut and paste the code commented "Listen for keyboard events in the input box." that is highlighted below.</div>
        <div class="details"><pre class="code">
    // Listen for mouse events on the Add button.
    addStockButton.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        addStock();
      }
    });

<span class="highlight">    // Listen for keyboard events in the input box.
    newSymbolTextBox.addKeyDownHandler(new KeyDownHandler() {
      public void onKeyDown(KeyDownEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
          addStock();
        }
      }
    });</span>

  }

  /**
   * Add stock to FlexTable. Executed when the user clicks the addStockButton or
   * presses enter in the newSymbolTextBox.
   */
  private void addStock() {
    // TODO Auto-generated method stub

  }

}</pre></div>
        <div class="details">Eclipse flags KeyDownHandler and suggests you include the import declaration.</div>
    </li>
    <li>
        <div class="header">Include the import declarations.</div>
        <div class="details"><pre class="code"><span class="highlight">import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;</span></pre></div>
    </li>
</ol>

<p>
The event handlers are now wired up and ready for an event. Your next step is to fill out the stub addStock method.
</p>

<a name="responding"></a>
<h2>3. Responding to user events</h2>
<p>
At this point, StockWatcher should be listening for user input, a mouse or keyboard event that signals the user has entered a stock code. So next you'll test whether or not the event handler interfaces are working by coding the response that StockWatcher should make when it detects an event: add the stock. StockWatcher responds on the client side without sending any requests back to server or reloading the HTML page.
</p>

<h3>Adding the stock to the stock table</h3>
<p>
In StockWatcher, users will enter the stock codes of the stocks they want to monitor one at a time into the input box. When they press Enter or click on the Add button, you want StockWatcher to respond as follows:
</p>
<ol>
    <li>Validate the input.</li>
    <li>Check for duplicates.</li>
    <li>Add the stock.</li>
    <li>Add a button for removing the stock from the list.</li>
</ol>
<p>
In this section, you'll code the first response, validating the input, just to see if the event handler interfaces are working. In the next section, <a href="codeclient.html">Coding Functionality on the Client</a>, you'll code the rest of the steps to add the stock.
</p>
<p>
You'll implement this functionality in the addStock method.
</p>

<h3>Validating input in a text box</h3>
<p>
You want verify that the stock code entered is valid. Rather than verify whether the user input matches an actual stock code, for the purposes of this tutorial, you'll just perform a simple character validity check.
</p>
<p>
First, extract the stock code. To retrieve the text in the TextBox widget use its getText method.
</p>
<p>
Next, ensure that the characters are not within the set of illegal characters you specify. After you've converted the user input to a standard form, use a regular expression to check its format. Remember to use regular expressions that have the same meaning in both Java and JavaScript.
</p>
<p>
If the input is valid, clear the text box so the user can add another stock code.
</p>
<p>
Finally, if the input is not valid, warn users via a dialog box.
</p>

<ol class="instructions">
    <li>
        <div class="header">Validate user input of the stock code.</div>
        <div class="details">In StockWatcher.java. replace the stub addStock method with following code.</div>
        <div class="details"><pre class="code">
  private void addStock() {
<span class="highlight">    final String symbol = newSymbolTextBox.getText().toUpperCase().trim();
    newSymbolTextBox.setFocus(true);

    // Stock code must be between 1 and 10 chars that are numbers, letters, or dots.
    if (!symbol.matches("^[0-9A-Z&#92;&#92;.]{1,10}$")) {
      Window.alert("'" + symbol + "' is not a valid symbol.");
      newSymbolTextBox.selectAll();
      return;
    }

    newSymbolTextBox.setText("");

    // TODO Don't add the stock if it's already in the table.

    // TODO Add the stock to the table.

    // TODO Add a button to remove this stock from the table.

    // TODO Get the stock price.</span>

  }</pre></div>
        <div class="details">Eclipse flags Window and suggests you include the import declaration.</div>
    </li>
        <li>
        <div class="header">Include the import declaration.</div>
        <div class="details"><pre class="code"><span class="highlight">import com.google.gwt.user.client.Window;</span></pre></div>
    </li>

</ol>


<a name="testing"></a>
<h2>4. Testing event handling</h2>
<p>
At this point you should be able to enter text in the input box. If you use an illegal character, a dialog box should pop up and display a warning. Try it and see.
</p>
<ol class="instructions">
    <li>
        <div class="header">Test event handling in development mode.</div>
        <div class="details">Press Refresh in the already open browser.</div>
    </li>
    <li>
        <div class="header">Test that both event handler interfaces work.</div>
        <div class="details">Enter stock codes in the input box. Enter using both methods, by pressing return and by using the mouse to click on the Add button.</div>
        <div class="details">At this point, the stock is not added to the table. However, the input box should clear so that you can add another stock.</div>
    </li>
    <li>
        <div class="header">Test the validity checking and error message.</div>
        <div class="details">Make some typos that include illegal characters.</div>
    </li>
</ol>
<p>
<img src="images/ManageEvents.png" alt="Validation Error Message" />
</p>

<p class="note">
<b>Tip:</b> Changes made to your Java code are immediately shown in the browser after pressing refresh. If development mode is already running, you don't need to restart it. Just click the Refresh button in your browser to reload your updated GWT code.
</p>
<p>
Although you have not compiled StockWatcher yet, you can test it in production mode here:
</p>


<h2>What's Next</h2>
<p>
At this point you've implemented event handler interfaces for mouse and keyboard events that signal the user has entered a stock. Stockwatcher responds by validating the input.
</p>
<p>
Now you're ready to implement the code on the client that adds the stock to the table and provide a button to remove it. You will also display the stock prices and display data and a timestamp showing when the data was last updated.
</p>
<p>
<a href="codeclient.html">Step 5: Coding Functionality on the Client</a>
</p>

