<style type="text/css">
  #gc-pagecontent h1.smaller  {
    font-size: 145%;
    border-top: thin black solid;
    padding-top: 3px;
    margin-top: 1.8em;
  }
</style>

<p>
If you use a CellTable or other Cell Widgets in your applications, you will probably want to create custom
Cells tailored for your data. Cells are like widget flyweights, so a single Cell can render multiple
DOM instances of itself, and it can handle events from multiple rendered instances. This patterns lends itself
well to an MVP design, as we'll show here.
</p>

<p>
This developer guide describes patterns for creating simple one-off Cells, as well as creating re-usable cells
that can be "skinned".  If you are not familiar with the cell widgets, you should read the
<a href="DevGuideUiCellWidgets.html">Cell Widgets Developer Guide</a>
before continuing.
</p>

<ol style="list-style: none; margin-top: 12px;">
  <li><a href="#Cell_Basics">Cell Basics</a>
    <ol class="toc" style="margin-top: 0;">
      <li><a href="#cell-render">Implementing the render() Method</a></li>
      <li><a href="#cell-onBrowserEvent">Handling Events from Cells</a></li>
    </ol>
  </li>
</ol>

<h2 class="h2-group" id="Cell_Basics">Cell Basics</h2>

<h2 id="cell-render">Implementing the render() Method</h2>

<p>
The core method that all Cells must implement is the
<a href="/javadoc/latest/com/google/gwt/cell/client/Cell.html#render(com.google.gwt.cell.client.Cell.Context, C, com.google.gwt.safehtml.shared.SafeHtmlBuilder)">
Cell#render(Context, C value, SafeHtmlBuilder)</a>
method, which renders the parameterized <code>value</code> into the <code>SafeHtmlBuilder</code>.  In some cases, you will only need to render
simple HTML, such as a single <code>div</code> with some content.  In other cases, you might need to render a complex HTML structure, such as the one
in the <a href="http://gwt.google.com/samples/Showcase/Showcase.html#!CwCellList">CellList</a> example, which contains an image and two lines of
text. The basic principles are the same in either case.
</p>

<p>
<b>Code Example</b>
</p>

<p>
ColorCell is a custom Cell that extends AbstractCell&lt;String&gt; and generates a single <code>div</code> containing the name of a color, styled
with specified color as the text color. The generated HTML for each rendered value is <code>&lt;div style="color: value"&gt;value&lt;/div&gt;</code>,
where value is the color of the cell. In the example below, ColorCell is passed into a CellList to render various colors. The first row contains
the string "red" colored red, the second is "green" colored green, and so forth, as seen below.
</p>

<p>
<blockquote><img style="border:1px solid #555;margin-left:10px;" src="images/CustomCells-BasicRender.png"/></blockquote>
</p>

<p>
When implementing a render method, you should follow these steps:
<ol>
  <li>Create a subclass of
      <a href="/javadoc/latest/com/google/gwt/cell/client/AbstractCell.html">AbstractCell</a>,
      parameterized with the type of value you want to render.
      <p class="warning">
        <b>WARNING:</b> Implement the Cell interface directly at your own risk. The Cell interface may change in subtle but breaking ways as we
        continuously seek to improve performance. We provide AbstractCell so we can modify the Cell interface without breaking your code.
      </p>
  </li>
  <li>
    Within the render method, always check if the value is <code>null</code>. Even if you do not include null values in your data, Cell widgets
    might pass a <code>null</code> value into your Cell to render "filler" Cells, such as when you pass data in the middle of the visible range.
    You can either exit early from the render method, or you can render some placeholder value.
  </li>
  <li>
    If the value comes from user data, make sure that you escape it using
    <a href="/javadoc/latest/com/google/gwt/safehtml/shared/SafeHtmlUtils.html">SafeHtmlUtils</a> or
    one of the other escaping libraries. The rendered cell will be added to the document without additional escaping, so if your Cell renders
    malicious javascript code, it will be included in the document.
    <a href="/javadoc/latest/com/google/gwt/safehtml/shared/SafeHtmlBuilder.html">SafeHtmlBuilder</a>
    tries to enforce this.
  </li>
  <li>
    Render the HTML that represents the Cell into the SafeHtmlBuilder.
  </li>
</ol>
</p>

<p>
You can download this example at <a href="http://google-web-toolkit.googlecode.com/svn/trunk/user/javadoc/com/google/gwt/examples/cell/CellExample.java">CellExample.java</a>.
</p>

<pre class="prettyprint">
/**
 * Example of creating a custom {@link Cell}.
 */
public class CellExample implements EntryPoint {

  /**
   * A custom {@link Cell} used to render a string that contains the name of a
   * color.
   */
  static class ColorCell extends AbstractCell&lt;String&gt; {

    /**
     * The HTML templates used to render the cell.
     */
    interface Templates extends SafeHtmlTemplates {
      /**
       * The template for this Cell, which includes styles and a value.
       * 
       * @param styles the styles to include in the style attribute of the div
       * @param value the safe value. Since the value type is {@link SafeHtml},
       *          it will not be escaped before including it in the template.
       *          Alternatively, you could make the value type String, in which
       *          case the value would be escaped.
       * @return a {@link SafeHtml} instance
       */
      @SafeHtmlTemplates.Template("&lt;div style=\"{0}\"&gt;{1}&lt;/div&gt;")
      SafeHtml cell(SafeStyles styles, SafeHtml value);
    }

    /**
     * Create a singleton instance of the templates used to render the cell.
     */
    private static Templates templates = GWT.create(Templates.class);

    @Override
    public void render(Context context, String value, SafeHtmlBuilder sb) {
      /*
       * Always do a null check on the value. Cell widgets can pass null to
       * cells if the underlying data contains a null, or if the data arrives
       * out of order.
       */
      if (value == null) {
        return;
      }

      // If the value comes from the user, we escape it to avoid XSS attacks.
      SafeHtml safeValue = SafeHtmlUtils.fromString(value);

      // Use the template to create the Cell's html.
      SafeStyles styles = SafeStylesUtils.forTrustedColor(safeValue.asString());
      SafeHtml rendered = templates.cell(styles, safeValue);
      sb.append(rendered);
    }
  }

  /**
   * The list of data to display.
   */
  private static final List&lt;String&gt; COLORS = Arrays.asList("red", "green", "blue", "violet",
      "black", "gray");

  @Override
  public void onModuleLoad() {
    // Create a cell to render each value.
    ColorCell cell = new ColorCell();

    // Use the cell in a CellList.
    CellList&lt;String&gt; cellList = new CellList&lt;String&gt;(cell);

    // Push the data into the widget.
    cellList.setRowData(0, COLORS);

    // Add it to the root panel.
    RootPanel.get().add(cellList);
  }
}
</pre>

<h2 id="cell-onBrowserEvent">Handling events from Cells</h2>

<p>
The other core method to implement (if you want to handle events in the Cell) is
<a href="/javadoc/latest/com/google/gwt/cell/client/Cell.html#onBrowserEvent(com.google.gwt.cell.client.Cell.Context, com.google.gwt.dom.client.Element, C, com.google.gwt.dom.client.NativeEvent, com.google.gwt.cell.client.ValueUpdater)">
Cell#onBrowserEvent(Context, Element, C value, NativeEvent, ValueUpdater)</a>.  <code>onBrowserEvent</code> is called when an event occurs on any DOM instance created with the
HTML output of this Cell.  Since Cells are flyweights that handle events from multiple instances, contextual information about the instance is passed into the method.
For example, the Context tells you where the Cell is located (row and column indexes) in its containing CellTable.
</p>

<p>
The parent element refers to the DOM element that <em>contains</em> the rendered HTML from your Cell. It is <em>not</em> the outer most element of your Cell. Cells
are not required to have only one outer most element.  For example TextCell does not even render an HTML element, it renders only the text string.
</p>

<h3>
<b>Specifying which events to handle</b>
</h3>

<p>
Cells must declare which event types they want to handle, and the Cell Widget that contains the Cell is responsible for ensuring that only those events are passed to the Cell.
In order to specify which events your cell will handle, you pass the event types into the
<a href="/javadoc/latest/com/google/gwt/cell/client/AbstractCell.html#AbstractCell(java.lang.String...)">AbstractCell(String...)</a>
constructor. AbstractCell will return the events to the Cell Widget when
<a href="/javadoc/latest/com/google/gwt/cell/client/Cell.html#getConsumedEvents()">Cell#getConsumedEvents()</a>
is called.
</p>

<p>
Alternatively, you can override 
<a href="/javadoc/latest/com/google/gwt/cell/client/Cell.html#getConsumedEvents()">Cell#getConsumedEvents()</a>
directly. For example, if you are extending an existing Cell that does not expose the event type constructor, you can override <code>getConsumedEvents()</code> to return the
events that your subclass uses.
</p>

<p class="warning"><b>WARNING:</b> The return value of <code>getConsumedEvents()</code> should not change on subsequent calls. Cell Widgets are only required to call the
method once, when the Cell is first added.</p>

<p>
<b>Code Example</b>
</p>

<p>
The code example below extends ColorCell to add support for handling <code>click</code> and <code>keydown</code> events. If the user clicks on the Cell, we
show an alert box that tells us which color we selected.

<p>
<blockquote><img style="border:1px solid #555;margin-left:10px;" src="images/CustomCells-HandlingEvents.png"/></blockquote>
</p>

The process for adding event support is as follows:
<ol>
  <li>
    Specify the events that you want to handle by passing them into the
    <a href="/javadoc/latest/com/google/gwt/cell/client/AbstractCell.html#AbstractCell(java.lang.String...)">AbstractCell(String...)</a>
    constructor. See the note about the <code>keydown</code> event below.
  </li>
  <li>
    Override the
    <a href="/javadoc/latest/com/google/gwt/cell/client/Cell.html#onBrowserEvent(com.google.gwt.cell.client.Cell.Context, com.google.gwt.dom.client.Element, C, com.google.gwt.dom.client.NativeEvent, com.google.gwt.cell.client.ValueUpdater)">
    Cell#onBrowserEvent()</a> method to handle the event. For some events, you may want to look at the event target before deciding how to handle the event.
    In the example below, we only respond to click events that actually occur on the rendered div.
  </li>
  <li>
    As described in the example comments below, the <code>onEnterKeyDown()</code> method is a special case convience method in AbstractCell that helps to provide
    a unified user experience, such that all Cells that respond to events can be activated by selecting the Cell with the keyboard and pressing enter.  Otherwise,
    users would not be able to interact with the Cell without using the mouse. The convention followed by GWT cells is to toggle editing when the enter key is
    pressed, but you are free to define the behavior for your app. Your cell's getConsumedEvents() method must include "keydown" in order for AbstractCell to call
    <code>onEnterKeyDown()</code>.
  </li>
  <li>
    Optionally call 
    <a href="/javadoc/latest/com/google/gwt/cell/client/ValueUpdater.html#update(C)">ValueUpdater#update(C)</a>
    to indicate that the value of the cell has been modified. For example, you might call the <code>update()</code> method if the user types a new value in a text
    box, or if the user clicks on a button.
  </li>
</ol>
</p>

<pre class="prettyprint">
/**
 * Example of creating a custom {@link Cell} that responds to events.
 */
public class CellWithEventsExample implements EntryPoint {

  /**
   * A custom {@link Cell} used to render a string that contains the name of a
   * color.
   */
  static class ColorCell extends AbstractCell&lt;String&gt; {

    /**
     * The HTML templates used to render the cell.
     */
    interface Templates extends SafeHtmlTemplates {
      /**
       * The template for this Cell, which includes styles and a value.
       * 
       * @param styles the styles to include in the style attribute of the div
       * @param value the safe value. Since the value type is {@link SafeHtml},
       *          it will not be escaped before including it in the template.
       *          Alternatively, you could make the value type String, in which
       *          case the value would be escaped.
       * @return a {@link SafeHtml} instance
       */
      @SafeHtmlTemplates.Template("&lt;div style=\"{0}\"&gt;{1}&lt;/div&gt;")
      SafeHtml cell(SafeStyles styles, SafeHtml value);
    }

    /**
     * Create a singleton instance of the templates used to render the cell.
     */
    private static Templates templates = GWT.create(Templates.class);

    public ColorCell() {
      /*
       * Sink the click and keydown events. We handle click events in this
       * class. AbstractCell will handle the keydown event and call
       * onEnterKeyDown() if the user presses the enter key while the cell is
       * selected.
       */
      super("click", "keydown");
    }

    /**
     * Called when an event occurs in a rendered instance of this Cell. The
     * parent element refers to the element that contains the rendered cell, NOT
     * to the outermost element that the Cell rendered.
     */
    @Override
    public void onBrowserEvent(Context context, Element parent, String value, NativeEvent event,
        ValueUpdater&lt;String&gt; valueUpdater) {
      // Let AbstractCell handle the keydown event.
      super.onBrowserEvent(context, parent, value, event, valueUpdater);

      // Handle the click event.
      if ("click".equals(event.getType())) {
        // Ignore clicks that occur outside of the outermost element.
        EventTarget eventTarget = event.getEventTarget();
        if (parent.getFirstChildElement().isOrHasChild(Element.as(eventTarget))) {
          doAction(value, valueUpdater);
        }
      }
    }

    @Override
    public void render(Context context, String value, SafeHtmlBuilder sb) {
      /*
       * Always do a null check on the value. Cell widgets can pass null to
       * cells if the underlying data contains a null, or if the data arrives
       * out of order.
       */
      if (value == null) {
        return;
      }

      // If the value comes from the user, we escape it to avoid XSS attacks.
      SafeHtml safeValue = SafeHtmlUtils.fromString(value);

      // Use the template to create the Cell's html.
      SafeStyles styles = SafeStylesUtils.forTrustedColor(safeValue.asString());
      SafeHtml rendered = templates.cell(styles, safeValue);
      sb.append(rendered);
    }

    /**
     * onEnterKeyDown is called when the user presses the ENTER key will the
     * Cell is selected. You are not required to override this method, but its a
     * common convention that allows your cell to respond to key events.
     */
    @Override
    protected void onEnterKeyDown(Context context, Element parent, String value, NativeEvent event,
        ValueUpdater&lt;String&gt; valueUpdater) {
      doAction(value, valueUpdater);
    }

    private void doAction(String value, ValueUpdater&lt;String&gt; valueUpdater) {
      // Alert the user that they selected a value.
      Window.alert("You selected the color " + value);

      // Trigger a value updater. In this case, the value doesn't actually
      // change, but we use a ValueUpdater to let the app know that a value
      // was clicked.
      valueUpdater.update(value);
    }
  }

  /**
   * The list of data to display.
   */
  private static final List&lt;String&gt; COLORS = Arrays.asList("red", "green", "blue", "violet",
      "black", "gray");

  @Override
  public void onModuleLoad() {
    // Create a cell to render each value.
    ColorCell cell = new ColorCell();

    // Use the cell in a CellList.
    CellList&lt;String&gt; cellList = new CellList&lt;String&gt;(cell);

    // Push the data into the widget.
    cellList.setRowData(0, COLORS);

    // Add it to the root panel.
    RootPanel.get().add(cellList);
  }
}
</pre>