
<style>
code, .code {font-size: 9pt; font-family: Courier, Courier New, monospace; color:#007000;}
.highlight {background-color: #ffc;}
.strike {text-decoration:line-through; color:red;}
.header {margin-top: 1.5ex;}
.details {margin-top: 1ex;}
</style>

<p>At this point, you've created the components of the StockWatcher project and reviewed its functional requirements and UI design. In this section, you'll build the user interface out of GWT widgets and panels.</p>
<ol>
  <li><a href="#widgets">Select the GWT widgets needed to implement the UI elements.</a></li>
  <li><a href="#panels">Select the GWT panels needed to lay out the UI elements.</a></li>
  <li><a href="#hostpage">Embed the application in the host page, StockWatcher.html.</a></li>
  <li><a href="#implement">Implement the widgets and panels in StockWatcher.java.</a></li>
  <li><a href="#test">Test the layout in development mode.</a></li>
</ol>
<p>
GWT shields you from worrying too much about cross-browser
incompatibilities. If you construct the interface with GWT widgets and
composites, your application will work on the most recent versions of Chrome, Firefox, Internet Explorer, Opera, and Safari. However, DHTML user interfaces remain remarkably quirky; therefore, you still must test your applications thoroughly on every browser.
</p>
<a name="widgets"></a>
<h1>1. Selecting GWT widgets to implement the UI elements</h1>
<p>
First, look at the <a href="../RefWidgetGallery.html">Widget Gallery</a> and select the GWT widget for each UI element.
</p>
<p>
In the Widget Gallery the widgets have a default style and so they
don't look exactly as they will in the final implementation of
StockWatcher. Don't worry about that now. First you'll focus on
getting the widgets working. Later, you will change their appearance
with CSS in the <a href="style.html">Applying Syles</a> section.
</p>

<h2>Stock Data Table</h2>
<p>
GWT provides a special table widget called a <a href="../RefWidgetGallery.html#flextable">FlexTable</a>. The FlexTable widget creates cells on demand. This is just what you need for the table containing the stock data because you don't know how many stocks the user will add. A table implemented with the FlexTable widget will expand or collapse as the user adds or removes stocks.
</p>
<h2>Buttons</h2>
<p>
Whenever possible, GWT defers to a browser's native user interface elements. For instance, a Button widget becomes a true HTML &lt;button&gt; rather than a synthetic button-like widget that's built, for example, from a &lt;div&gt;. This means that GWT buttons render as designed by the browser and client operating system. The benefit of using native browser controls is that they are fast, accessible, and most familiar to users. Also, they can be styled with CSS.
</p>

<h2>Input Box</h2>
<p>
GWT provides several widgets to create fields that users can type in:
</p>
<ul>
    <li>TextBox widget, a single-line text box</li>
    <li>PassWordTextBox widget, a text box that visually masks input</li>
    <li>TextArea widget, a multi-line text box</li>
    <li>SuggestBox, a text box that displays a pre-configured set of items</li>
</ul>
<p>
StockWatcher users will type in a stock code which is single line of text; therefore,
implement a <a href="../RefWidgetGallery.html#textbox">TextBox</a> widget.
</p>
<h2>Label</h2>
<p>
In contrast with the Button widget, the Label widget does not map to
the HTML &lt;label&gt; element, used in HTML forms. Instead it maps to
a &lt;div&gt; element that contains arbitrary text that is not interpreted as HTML. As a &lt;div&gt; element, it is a block-level element rather than an inline element.
</p>
<pre class="code">
&lt;div class="gwt-Label"&gt;Last update : Oct 1, 2008 1:31:48 PM&lt;/div&gt;
</pre>
<p>
If you're interested in taking a peek at the API reference for the GWT widgets you'll use to build the StockWatcher interface, click on the links in the table below.
</p>
<table>
    <tr>
        <th>UI element</th>
        <th>GWT implementation</th>
    </tr>
    <tr>
        <td>a table to hold the stock data</td>
        <td><a href="/javadoc/latest/com/google/gwt/user/client/ui/FlexTable.html">FlexTable</a> widget</td>
    </tr>
    <tr>
        <td>two buttons, one to add stocks and one to remove them</td>
        <td><a href="/javadoc/latest/com/google/gwt/user/client/ui/Button.html">Button</a> widget</td>
    </tr>
    <tr>
        <td>an input box to enter the stock code</td>
        <td><a href="/javadoc/latest/com/google/gwt/user/client/ui/TextBox.html">TextBox</a> widget</td>
    </tr>
    <tr>
        <td>a timestamp to show the time and date of the last refresh</td>
        <td><a href="/javadoc/latest/com/google/gwt/user/client/ui/Label.html">Label</a> widget</td>
    </tr>
    <tr>
        <td>a logo</td>
        <td>image file referenced from HTML host page</td>
    </tr>
    <tr>
        <td>a header</td>
        <td>static HTML in HTML host page</td>
    </tr>
    <tr>
        <td>colors to indicate whether the change in price was positive or negative</td>
        <td>dynamic CSS</td>
    </tr>
</table>
<p class="note"><b>In Depth:</b> If you don't find a widget that meets the functional requirements of your application, you can create your own. For details on creating composite widgets or widgets from scratch using Java or JavaScript, see the Developer's Guide, <a href="../DevGuideUiCustomWidgets.html">Creating Custom Widgets</a>.
</p>

<a name="panels"></a>
<h1>2. Selecting GWT panels to lay out the UI elements</h1>
<p>
Now that you know what widgets you'll use, you'll decide how to lay them out using GWT panels. GWT provides several types of panels to manage the layout. Panels can be nested within other panels. This is analogous to laying out your web page in HTML using nested div elements or tables. For StockWatcher, you'll use a horizontal panel nested within a vertical panel.
</p>
<img src="images/StockWatcherUIpanel4.jpg" alt="screenshot: StockWatcher UI" />

<h2>Horizontal Panel</h2>
<p>
The two elements used to add a stock&mdash;the input box for typing in a new stock symbol and the Add button&mdash;are closely related functionally and you want keep them together visually. To lay them out side-by-side, you'll put the TextBox widget and a Button widget in a horizontal panel. In the Java code, you'll create a new instance of <a href="/javadoc/latest/com/google/gwt/user/client/ui/HorizontalPanel.html">HorizontalPanel</a> and name it addPanel. <!-- newSymbolTextBox and addStockButton -->
</p>

<h2>Vertical Panel</h2>
<p>
You want to lay out the remaining elements vertically:
</p>
<ul>
    <li>the FlexTable widget for the stock table</li>
    <li>the Add Stock panel, which contains the input box and Add button</li>
    <li>the Label widget for the timestamp</li>
</ul>
<p>
You'll do this with a vertical panel. In the Java code, you'll create a new instance of <a href="/javadoc/latest/com/google/gwt/user/client/ui/VerticalPanel.html">VerticalPanel</a> and name it mainPanel.
</p>
<h2>Root Panel</h2>
<p>
There is one more panel you need that is not visible in the user interface: a Root panel. A Root panel is the container for the dynamic elements of your application. It is at the top of any GWT user interface hierarchy. There are two ways you can use a Root panel, either to generate the entire body of the page or to generate specific elements embedded in the body.
</p>
<p>
The Root panel works by wrapping the &lt;body&gt; or other element in the HTML host page. By default
(that is, if you don't add any placeholders in the host page) the Root panel
wraps the &lt;body&gt; element. However, you can wrap any element if you give it an id and
then, when you call the Root panel, pass the id as a parameter. You'll see how this works in the next two sections when you do it for StockWatcher.
</p>
<pre class="code">
RootPanel.get()             // Default. Wraps the HTML body element.
RootPanel.get("stockList")  // Wraps any HTML element with an id of "stockList"
</pre>
<p>
A host page can contain multiple Root panels. For example, if you're embedding multiple GWT widgets or panels into a host page, each one can be implemented independently of the others, wrapped in its own Root panel.
</p>

<a name="hostpage"></a>
<h1>3. Embedding the application in the host page</h1>
<p>
To get the StockWatcher application to run in the browser, you need to embed it in an HTML file, the HTML host page. The host page for the StockWatcher project, StockWatcher.html, was generated by webAppCreator. For the <a href="create.html#starter">starter application</a>, StockWatcher.html had an empty body element. As a result, the Root panel wrapped the entire body element. The text input box, label ("Please enter your name:") and "Send" button were build dynamically with GWT. If your application has no static elements, you wouldn't need to edit the HTML host page at all.
</p>
<p>
However, for StockWatcher you will use some static HTML text (for the header) and an image (for the logo) in addition to the dynamic elements. You will embed the GWT application in the browser page using a placeholder, a &lt;div&gt; element with an id of "stockList". This implementation strategy is especially useful for embedding GWT into an existing application.
</p>
<p>
As shown in the following code, do the following:
</p>
<ol>
    <li>Open the host page, <b>StockWatcher/war/StockWatcher.html</b>.</li>
    <li>In the head element, change the title text to StockWatcher.</li>
    <li>In the body element, add an &lt;h1&gt; heading, StockWatcher.</li>
    <li>In the body element, add a &lt;div&gt; element and give it an id of stockList.</li>
    <li>Delete the unneeded elements from the starter project application.</li>
    <li>Save the file StockWatcher.html.</li>
</ol>
<pre class="code">
&lt;!DOCTYPE html&gt;
&lt;html&gt;
  &lt;head&gt;
    &lt;meta http-equiv="content-type" content="text/html; charset=UTF-8"&gt;
    &lt;link type="text/css" rel="stylesheet" href="StockWatcher.css"&gt;
    &lt;title&gt;<span class="highlight">StockWatcher</span>&lt;/title&gt;
    &lt;script type="text/javascript" language="javascript" src="stockwatcher/stockwatcher.nocache.js"&gt;&lt;/script&gt;
  &lt;/head&gt;
  &lt;body&gt;

<span class="highlight">    &lt;h1&gt;StockWatcher&lt;/h1&gt;</span>
<span class="highlight">    &lt;div id="stockList"&gt;&lt;/div&gt;</span>
    &lt;iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1' style="position:absolute;width:0;height:0;border:0"&gt;&lt;/iframe&gt;
    &lt;noscript&gt;
      &lt;div style="width: 22em; position: absolute; left: 50%; margin-left: -11em; color: red; background-color: white; border: 1px solid red; padding: 4px; font-family: sans-serif"&gt;
        Your web browser must have JavaScript enabled
        in order for this application to display correctly.
      &lt;/div&gt;
    &lt;/noscript&gt;

    <span class="strike">&lt;h1&gt;Web Application Starter Project&lt;/h1&gt;</span>
    <span class="strike">&lt;table align="center"&gt;</span>
      <span class="strike">&lt;tr&gt;</span>
        <span class="strike">&lt;td colspan="2" style="font-weight:bold;"&gt;Please enter your name:&lt;/td&gt;</span>

      <span class="strike">&lt;/tr&gt;</span>
      <span class="strike">&lt;tr&gt;</span>
        <span class="strike">&lt;td id="nameFieldContainer"&gt;&lt;/td&gt;</span>
        <span class="strike">&lt;td id="sendButtonContainer"&gt;&lt;/td&gt;</span>
      <span class="strike">&lt;/tr&gt;</span>
    <span class="strike">&lt;/table&gt;</span>
  &lt;/body&gt;
&lt;/html&gt;
</pre>
<p><b>Note:</b> HTML comments have been omitted for brevity.</p>

<a name="implement"></a>
<h1>4. Implementing widgets and panels</h1>
<p>
Next you will construct the user interface from GWT widgets and panels.
</p>
<p>
You want the UI to display as soon as StockWatcher starts up, so you'll implement them in the onModuleLoad method. In this section, you will:
</p>
<ol>
    <li>Instantiate each widget and panel.</li>
    <li>Create the table that holds the stock data.</li>
    <li>Lay out the widgets using the Add Stock (horizontal) panel and the Main (vertical) panel.</li>
    <li>Associate the Main panel with the Root panel.</li>
    <li>Move the cursor focus to the input box.</li>
</ol>
<p>
You can follow this section of the tutorial step-by-step, or you can cut and paste the entire block of code from the <a href="#summary">Summary</a> at the end.
</p>
<h2>1. Instantiate each widget and panel</h2>
<ol class="instructions">
    <li>
    <div class="header">Instantiate each widget and panel using class field initializers.</div>
    <div class="details">Open <b>StockWatcher/src/com/google/gwt/sample/stockwatcher/client/StockWatcher.java</b>.</div>
    <div class="details">In StockWatcher.java, replace <em>all</em> the existing code for the <a href="create.html#starter">starter application</a> (from the imports down to the handler) with the following code.</div>
    <div class="details">
<pre class="code">
<span class="highlight">package com.google.gwt.sample.stockwatcher.client;

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
    // TODO Create table for stock data.
    // TODO Assemble Add Stock panel.
    // TODO Assemble Main panel.
    // TODO Associate the Main panel with the HTML host page.
    // TODO Move cursor focus to the input box.

  }

}</span></pre></div>
        <div class="details">Along the left edge, Eclipse flags the variable definitions with a red "x" because their types are undefined.</div>
        <p class="note"><b>Tip:</b> One way you can leverage Eclipse is to use its "suggest" feature to add the required import declarations, as follows.</p>
    </li>
    <li>
        <div class="header">Display suggested corrections by clicking on the first red "x".</div>
        <div class="details">Select "import EntryPoint (com.google.gwt.core.client.EntryPoint)" by pressing return.</div>
        </li>
        <li>
        <div class="header">Resolve all the other errors by declaring the import declarations in the same way. If you are not using Eclipse, cut and paste from the highlighted code below.</div>
        <div class="details"><pre class="code">
package com.google.gwt.sample.stockwatcher.client;

<span class="highlight">import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;</span>

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
    // TODO Create table for stock data.
    // TODO Assemble Add Stock panel.
    // TODO Assemble Main panel.
    // TODO Associate the Main panel with the HTML host page.
    // TODO Move cursor focus to the input box.

  }

}</pre></div>
    </li>
</ol>
<h2>2. Create a table for stock data</h2>
<p>
Implement the table that will hold the stock data. Set up the header row of the table. To do this, use the setText method to create labels in the heading of each column: Symbol, Price, Change, Remove.
</p>
<ol class="instructions">
    <li>
        <div class="header">Create a table for stock data.</div>
        <div class="details">In the onModuleLoad method, replace the TODO comment with the highlighted code.</div>
        <div class="details"><pre class="code">
package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
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
<span class="highlight">    // Create table for stock data.</span>
<span class="highlight">    stocksFlexTable.setText(0, 0, "Symbol");</span>
<span class="highlight">    stocksFlexTable.setText(0, 1, "Price");</span>
<span class="highlight">    stocksFlexTable.setText(0, 2, "Change");</span>
<span class="highlight">    stocksFlexTable.setText(0, 3, "Remove");</span>

    // TODO Assemble Add Stock panel.
    // TODO Assemble Main panel.
    // TODO Associate the Main panel with the HTML host page.
    // TODO Move cursor focus to the input box.

  }

}</pre></div>
    </li>
</ol>

You can see that adding to a table can be accomplished with a call to
the setText method. The first parameter indicates the row, the second
the column, and the final parameter is the text that will be
displayed in the table cell.


<h2>3. Lay out the widgets</h2>
<p>
To lay out the  widgets, you'll assemble two panels, the Add Stock panel and the Main panel. First assemble the Add Stock panel, a horizontal panel that wraps the input box and Add button. Then assemble the Main panel, a vertical panel that specifies the layout of the stock list table, the Add Stock panel, and the timestamp.
</p>
<ol class="instructions">
    <li>
        <div class="header">Lay out the widgets in the Add Stock panel and the Main panel.</div>
        <div class="details">In the onModuleLoad method, replace the TODO comment with the highlighted code.</div>
        <div class="details"><pre class="code">
package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
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

<span class="highlight">    // Assemble Add Stock panel.</span>
<span class="highlight">    addPanel.add(newSymbolTextBox);</span>
<span class="highlight">    addPanel.add(addStockButton);</span>

<span class="highlight">    // Assemble Main panel.</span>
<span class="highlight">    mainPanel.add(stocksFlexTable);</span>
<span class="highlight">    mainPanel.add(addPanel);</span>
<span class="highlight">    mainPanel.add(lastUpdatedLabel);</span>

    // TODO Associate the Main panel with the HTML host page.
    // TODO Move cursor focus to the input box.

  }

}</pre></div>

    </li>
</ol>


<h2>4. Associate the Main panel with the Root panel</h2>
<p>
In order for any GWT widget or panel to be embedded in the HTML host page, it must be contained within a Root panel. Associate the Root panel with Vertical panel assigned to mainPanel. The Root panel wraps the HTML element (in StockWatcher's host page) that has an id of "stocklist". In this case, it is a &lt;div&gt; element.
</p>
<ol class="instructions">
    <li>
        <div class="header">Associate the Main panel with the host page via the Root panel.</div>
        <div class="details">In the onModuleLoad method, replace the TODO comment with the highlighted code.</div>
        <div class="details"><pre class="code">
package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
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

<span class="highlight">    // Associate the Main panel with the HTML host page.</span>
<span class="highlight">    RootPanel.get("stockList").add(mainPanel);</span>

    // TODO Move cursor focus to the input box.

  }

}</pre></div>
        <div class="details">Eclipse flags RootPanel and suggests the correct import declaration.</div>
    </li>
    <li>
        <div class="header">Include the import declaration.</div>
        <div class="details"><pre class="code">
<span class="highlight">import com.google.gwt.user.client.ui.RootPanel;</span></pre></div>
    </li>
</ol>

<h2>5. Move cursor focus to the input box</h2>
<p>
Finally, move the cursor focus to the input box so, when StockWatcher loads, the user can begin adding stocks.
</p>
<ol class="instructions">
    <li>
        <div class="header"></div>
        <div class="details">In the onModuleLoad method, replace the TODO comment with the highlighted code.</div>
        <div class="details">
<pre class="code">
package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.core.client.EntryPoint;
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

<span class="highlight">    // Move cursor focus to the input box.</span>
<span class="highlight">    newSymbolTextBox.setFocus(true);</span>

  }

}</pre></div>
    </li>
</ol>


<a name="summary"></a>
<h2>Summary</h2>
<p>
Here's what you've done to this point.
</p>
<pre class="code">
package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.core.client.EntryPoint;
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

  }

}</pre>

<a name="test"></a>
<h1>5. Testing the layout</h1>
<p>
One benefit of using GWT in your AJAX application development is that you can see the effects of your code changes as soon as you refresh the browser running development mode. So that you can see your changes whether you are developing or debugging, in Eclipse, run StockWatcher in debug mode. Then you'll be able to switch between Java and Debug perspectives without having to relaunch StockWatcher.
</p>

<ol class="instructions">
    <li>
    <div class="header">Save the edited file:</div>
    <div class="details">Save <code>StockWatcher.java</code></div>
    </li>
    <li>
    <div class="header">If the StockWatcher project is still running from the startup application, stop it by going to the Development Mode tab an clicking on the red square in its upper right corner, whose tooltip says "Terminate Selected Launch", and then the gray "XX" to its right, whose tooltip says "Remove All Terminated Launches".  It may take a minute for it to complete, before you can do the next step. </div>
    </li>
    <li>
    <div class="header">Launch StockWatcher in development mode.</div>
    <div class="details">From the Eclipse menu bar, select <code>Run &gt; Debug As &gt; Web Application</code></div>
    <div class="details">If you are not using Eclipse, from the command line enter <code>ant devmode</code></div>
    </li>
    <li>
    <div class="header">The browser displays your first iteration of the StockWatcher application.  The button will not work until we later implement it.</div>
    <div class="details"><img class="screenshot" src="images/BuildUI.png" alt="StockWatcher: Building the UI Elements" /></div>
    <div class="details">StockWatcher displays the header of the flex
    table, the input box, and the Add button. You haven't yet set the text for the Label, so it isn't displayed. You'll do that after you've implemented the stock refresh mechanism.</div>
    </li>
    <li>
        <div class="header">Leave StockWatcher running in development mode.</div>
        <div class="details">In the rest of this tutorial, you'll frequently be testing changes in development mode.</div>
    </li>
</ol>
<h2>Refreshing Development Mode</h2>
<p>
You do not always need to relaunch your application in development mode after modifying your source code. Instead, just click the browser's Refresh button after saving your changes, and the code server will automatically recompile your application and open the new version.
</p>
<p class="note">
<b>Best Practices:</b> You may notice that your changes take effect sometimes even if you do not refresh development mode. This behavior is a result of the way development mode interacts with the compiled code, but it is not always reliable. Specifically, it happens only when you make minor changes to existing functions. To ensure your changes are included, make it a habit to always refresh the browser after making changes.
</p>



<h1>What's Next</h1>
<p>
At this point you've built the basic UI components of StockWatcher by implementing GWT widgets and panels. The widgets don't respond to any input yet.
</p>
<p>
Now you're ready to code event handling on the client. You'll wire up the widgets to listen for events and write the code that responds to those events.
</p>
<p>
<a href="manageevents.html">Step 4: Managing Events on the Client</a>
</p>


