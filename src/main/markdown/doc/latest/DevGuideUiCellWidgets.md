UiCellWidgets
===

<p>
Cell widgets (data presentation widgets) are high-performance, lightweight widgets composed of Cells for displaying data.
Examples are <a href="http://samples.gwtproject.org/samples/Showcase/Showcase.html#!CwCellList">lists</a>,
<a href="http://samples.gwtproject.org/samples/Showcase/Showcase.html#!CwCellTable">tables</a>,
<a href="http://samples.gwtproject.org/samples/Showcase/Showcase.html#!CwCellTree">trees</a> and
<a href="http://samples.gwtproject.org/samples/Showcase/Showcase.html#!CwCellBrowser">browsers</a>.
These widgets are designed to handle and display very large sets of data quickly.
A cell widget renders its user interface as an HTML string, using innerHTML instead of traditional DOM manipulation.
This design follows the flyweight pattern where data is accessed and cached only as needed, and passed to
flyweight Cell objects. A cell widget can accept data from any type of data source.  The data model handles
asynchronous updates as well as push updates. When you change the data, the view is automatically updated.
</p>

<p>
Cells are the basic blocks of a user interface and come in a variety of <a href="#available">available cell types</a>.  They render views of data, interpret browser events and can be selected.  A Cell has a type based on the data that the cell represents;  for example, DatePickerCell is a Cell&lt;Date&gt; that represents a Date and allows the user to select a new Date.  Cells must implement a render method that renders the typed value as an HTML string. In addition, cells can override onBrowserEvent to act as a flyweight that handles events that are fired on elements that were rendered by the cell. 
</p>

<p>
For example, in the <a href="http://samples.gwtproject.org/samples/Showcase/Showcase.html#!CwCellList">CellList example</a> of the Showcase, every selectable data record is rendered by a single Cell instance. Notice that the data that a single cell represents can be a composition of different data fields from the data source. In this example, the cell holds data of type ContactInfo, which represents a contact, including name, address and picture.
</p>

<p>
In the <a href="http://samples.gwtproject.org/samples/Showcase/Showcase.html#!CwCellTable">CellTable example</a>, a different Cell is used to render each Column of a row.  The five columns in this example present data from a boolean and four strings.
</p>

<ol style="list-style: none; margin-top: 12px;">
  <li><a href="#Cell_Widgets">Cell Widgets</a>
    <ol class="toc" style="margin-top: 0;">
      <li><a href="#demos">Demos and Code Examples</a></li>
      <li><a href="#celllist">Creating a CellList and Setting Data</a></li>
      <li><a href="#celltable">Creating a CellTable</a></li>
      <li><a href="#celltree">Creating a CellTree</a></li>
      <li><a href="#cellbrowser">Creating a CellBrowser</a></li>
    </ol>
  </li>
</ol>

<ol style="list-style: none; margin-top: 12px;">
  <li><a href="#Cells">Cells</a>
    <ol class="toc" style="margin-top: 0;">
      <li><a href="#available">Available Cell Types</a></li>
      <li><a href="#custom-cell">Creating a Custom Cell</a></li>
    </ol>
</ol>

<ol style="list-style: none; margin-top: 12px;">
  <li><a href="#Selection_Data_Paging">Selection, Data and Paging
    <ol class="toc" style="margin-top: 0;">
      <li><a href="#selection">Adding Selection Support</a></li>
      <li><a href="#data-provider">Providing Dynamic Data</a></li>
      <li><a href="#paging">Adding Paging Controls</a></li>
      <li><a href="#updating-database">Updating a Database from Changes in a Cell</a></li>
    </ol>
  </li>
</ol>

<p class="note" style="margin-top: 20px;">
NOTE: CellPanel is not a cell widget. CellPanel is an abstract base class for GWT Panel Widgets that are implemented using a table element.
</p>

<h2 class="h2-group" id="Cell_Widgets">Cell Widgets</h2>

<h2 id="demos">Demos and Code Examples</h2>

<p>
This document describes or points to three kinds of code examples, so you can jump in at any level.
<ul>
  <li><b>Live demos</b> - Visit the GWT Showcase for examples of widgets
<a href="http://samples.gwtproject.org/samples/Showcase/Showcase.html#!CwCellList">CellList</a>,
<a href="http://samples.gwtproject.org/samples/Showcase/Showcase.html#!CwCellTable">CellTable</a>,
<a href="http://samples.gwtproject.org/samples/Showcase/Showcase.html#!CwCellTree">CellTree</a>,
<a href="http://samples.gwtproject.org/samples/Showcase/Showcase.html#!CwCellBrowser">CellBrowser</a>, plus examples of Cells in the 
<a href="http://samples.gwtproject.org/samples/Showcase/Showcase.html#!CwCellSampler">Cell Sampler</a>.
Note: The prefix "Cw" in showcase class names stands for "ContentWidget", the parent class of each Showcase example.</li>
  <li><b>Simplified examples</b> - The code examples displayed in-line throughout this documented are short, simplified examples, often pared-down versions of the real-world examples.</li>
  <li><b>Real-world examples</b> - Most of the cell widgets also have source code examples (.java files) at 
    <a href="https://gwt.googlesource.com/gwt/+/master/user/javadoc/com/google/gwt/examples/cellview">cell widget code examples</a>.</li>
</li>
</ul>
<p>
These files are referenced, where appropriate, in the sections below.
</p>

<h2 id="celllist">Creating a CellList and Setting Data</h2>

<p>
CellList is the simplest cell widget, where data is rendered using cells of the same type in a list.  For instance, you can create a CellList&lt;String&gt; that uses a Cell&lt;String&gt; to render a list of Strings. For a fancier list view, you can create a custom cell, described at <a href="#custom-cell">Creating a Custom Cell</a>.
</p>



<p>
<b>Demo</b> - <a href="http://samples.gwtproject.org/samples/Showcase/Showcase.html#!CwCellList">CwCellList example</a> shows a CellList&lt;ContactInfo&gt; (on the left). Each list item is a custom type ContactCell&lt;ContactInfo&gt;.  The right-hand widget is a normal Composite widget that renders the data for a selected contact.
</p>



<p>
<b>To Create a CellList:</b>
</p>

<ol>
  <li>Create a <a href="#available">standard</a> or <a href="#custom-cell">custom</a> Cell to hold the list items.</li>
  <li>Create a <a href="/javadoc/latest/com/google/gwt/user/cellview/client/CellList.html">CellList</a>, passing the cell into its contructor.</li>
  <li>Access the data to populate the list.</li>
  <li>Call <a href="/javadoc/latest/com/google/gwt/user/cellview/client/AbstractHasData.html#setRowData(int,%20java.util.List)">setRowData</a> on the CellList to add the data.</li>
</ol>

<p>
The data inserted in the last step is updated by the data provider (<a href="/javadoc/latest/com/google/gwt/view/client/ListDataProvider.html">ListDataProvider</a> or <a href="/javadoc/latest/com/google/gwt/view/client/AsyncDataProvider.html">AsyncDataProvider</a>).  If you need to allow the user to modify the content of a cell and update the database, use ValueUpdater instead of setRowData in the last step, as described in <a href="#updating-from-celllist">Updating a Database From a CellList</a>.
</p>

<p>
<b>Code Example</b> - The example below is available at <a href="https://gwt.googlesource.com/gwt/+/master/user/javadoc/com/google/gwt/examples/cellview/CellListExample.java">CellListExample.java</a>
</p>

<p>
The following code is a very simple example that creates a CellList widget containing a single TextCell and sets data from the data source.  The list shows names.
</p>



<pre class="prettyprint">
/**
 * Example of {@link CellList}. This example shows a list of the days of the week.
 */
public class CellListExample implements EntryPoint {

  // The list of data to display.
  private static final List&lt;String&gt; DAYS = Arrays.asList("Sunday", "Monday",
      "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");

  public void onModuleLoad() {
    // Create a cell to render each value in the list.
    TextCell textCell = new TextCell();

    // Create a CellList that uses the cell.
    CellList&lt;String&gt; cellList = new CellList&lt;String&gt;(textCell);

    // Set the total row count. This isn't strictly necessary, but it affects
    // paging calculations, so its good habit to keep the row count up to date.
    cellList.setRowCount(DAYS.size(), true);

    // Push the data into the widget.
    cellList.setRowData(0, DAYS);

    // Add it to the root panel.
    RootPanel.get().add(cellList);
  }
}
</pre>

<p>
You can add a SelectionModel to a CellList, as shown in the <a href="#selection">SelectionModel example</a> below.
</p>


<h2 id="celltable">Creating a CellTable</h2>

<p>
<a href="/javadoc/latest/com/google/gwt/user/cellview/client/CellTable.html">CellTable</a> renders row values in columns.  A Column represents a single field in a data object. Every column defines getValue(), which retrieves the value for the column from the data object. Each column uses a Cell to render the column-specific data. Note that columns can return whatever object they want for getValue(), including the row object itself (for example, to allow columns that show calculations based on several row values).
</p>

<p>
A Header represents either a header or a footer in a table. A table can have a header and footer for each column.
A Header can span multiple columns if adjacent headers are equal (==) to each other.
</p>

<p>
<b>Demo</b> - <a href="http://samples.gwtproject.org/samples/Showcase/Showcase.html#!CwCellTable">CwCellTable example</a> shows a CellTable&lt;ContactInfo&gt;. Each row item has 5 columns rendered respectively as a CheckboxCell, EditTextCell, EditTextCell, SelectionCell and TextCell.
</p>

<p>
<b>To Create a CellTable:</b>
</p>

<ol>
  <li>Create a <a href="#available">standard</a> or <a href="#custom-cell">custom</a> Cell for each column of data.</li>
  <li>Create a <a href="/javadoc/latest/com/google/gwt/user/cellview/client/CellTable.html">CellTable</a></li>
  <li>Create and add <a href="/javadoc/latest/com/google/gwt/user/cellview/client/Column.html">Column</a>s to the CellTable.</li>
  <li>Access the data to populate the list.</li>
  <li>Add the data to the CellTable by calling <a href="/javadoc/latest/com/google/gwt/user/cellview/client/AbstractHasData.html#setRowData(int,%20java.util.List)">setRowData</a> on the CellTable for each Column.</li>
 </ul>
</ol>

<p>
The data inserted in the last step is updated by the data provider (<a href="/javadoc/latest/com/google/gwt/view/client/ListDataProvider.html">ListDataProvider</a> or <a href="/javadoc/latest/com/google/gwt/view/client/AsyncDataProvider.html">AsyncDataProvider</a>).  If you need to allow the user to modify the content of a cell and update the database, use FieldUpdater instead of setRowData in the last step, as described in <a href="#updating-from-celltable">Updating a Database From a CellTable</a>.
</p>

<p>
<b>More Information</b> - Read the <a href="DevGuideUiCellTable.html">Cell Table Developer Guide</a> for more
information about CellTable-specific features, such as column sorting.
</p>

<p>
<b>Code Example</b> - The example below is a pared-down version of <a href="https://gwt.googlesource.com/gwt/+/master/user/javadoc/com/google/gwt/examples/cellview/CellTableExample.java">CellTableExample.java</a>
</p>

<pre class="prettyprint">
/**
 * Example of {@link CellTable} of contacts having a name and address.
 */
public class CellTableExample implements EntryPoint {

  // A simple data type that represents a contact.
  private static class Contact {
    private final String address;
    private final String name;

    public Contact(String name, String address) {
      this.name = name;
      this.address = address;
    }
  }

  // The list of data to display.
  private static List&lt;Contact&gt; CONTACTS = Arrays.asList(
    new Contact("John", "123 Fourth Road"),
    new Contact("Mary", "222 Lancer Lane"));

  public void onModuleLoad() {

    // Create a CellTable.
    CellTable&lt;Contact&gt; table = new CellTable&lt;Contact&gt;();

    // Create name column.
    TextColumn&lt;Contact&gt; nameColumn = new TextColumn&lt;Contact&gt;() {
      @Override
      public String getValue(Contact contact) {
        return contact.name;
      }
    };

    // Create address column.
    TextColumn&lt;Contact&gt; addressColumn = new TextColumn&lt;Contact&gt;() {
      @Override
      public String getValue(Contact contact) {
        return contact.address;
      }
    };

    // Add the columns.
    table.addColumn(nameColumn, "Name");
    table.addColumn(addressColumn, "Address");

    // Set the total row count. This isn't strictly necessary, but it affects
    // paging calculations, so its good habit to keep the row count up to date.
    table.setRowCount(CONTACTS.size(), true);

    // Push the data into the widget.
    table.setRowData(0, CONTACTS);

    // Add it to the root panel.
    RootPanel.get().add(table);
  }
}
</pre>

<p>
You can add a SelectionModel to a CellTable, as shown in the <a href="#selection">SelectionModel example</a> below.
</p>



<h2 id="celltree">Creating a CellTree</h2>

<p>
CellTree renders a hierarchy of nodes, such as this <a href="http://samples.gwtproject.org/samples/Showcase/Showcase.html#!CwCellTree">CwCellTree</a>.  A node can be either a leaf node or have children.  Thus, a CellTree can have levels of nodes that go progressively deeper.  A node is represented by a NodeInfo, which contains all of the information needed to render a single node.
</p>

<p>
Each node has a Cell of a specific type;  usually, all Cells at a given level are of the same type, but that isn't required.  The example has a top level of nodes with each cell having an image and string.  Likewise, the second and third levels of cells have their own distinct types.  In addition to a cell, a node also has a DataProvider, to provide the data to the children of the NodeInfo, and a SelectionModel, to indicate how it can be selected by the user.  
</p>

<p>
The TreeViewModel provides the NodeInfo for each child node.  When a node is opened, CellTree will call <a href="/javadoc/latest/com/google/gwt/view/client/TreeViewModel.html#getNodeInfo(T)">getNodeInfo()</a> on TreeViewModel to get the NodeInfo used to render the children.
</p>

<p>
A CellTree can have its own CSS styles and its own resources, such as images that the user clicks on to open or close a node.  It can also respond to browser events.  In addition, a CellTree can have built-in animation for progressively revealing or hiding children when its node opens or closes. 
</p>

<p>
<b>Demo</b> - <a href="http://samples.gwtproject.org/samples/Showcase/Showcase.html#!CwCellTree">CwCellTree example</a> shows a CellTree.  It has three levels rendered respectively as custom types CategoryCell, LetterCountCell and ContactCell (the same type from the <a href="#celllist">CellList</a> demo).  The checkbox has an update method to select the ContactCell when checked.
</p>

<p>
<b>Creating a CellTree:</b>
</p>

<ol>
 <li>Define a <a href="/javadoc/latest/com/google/gwt/view/client/TreeViewModel.html">TreeViewModel</a> and <a href="/javadoc/latest/com/google/gwt/view/client/TreeViewModel.html#getNodeInfo(T)">getNodeInfo</a>
    <ol style="list-style-type: lower-alpha">
      <li>In getNodeInfo, create a data provider for the child nodes.</li>
      <li>Populate the data provider with data.</li>
      <li>Create a <a href="#available">standard</a> or <a href="#custom-cell">custom</a> Cell to render the children.</li>
    </li>
  </ol>
  <li>Create an instance of your TreeViewModel class.</li>
  <li>Create a <a href="/javadoc/latest/com/google/gwt/user/cellview/client/CellTree.html">CellTree</a>, passing in the TreeViewModel instance.</li>
</ol>

<p>
<b>Code Example #1</b> - The example below is a simplified example of CellTree, and is available at  <a href="https://gwt.googlesource.com/gwt/+/master/user/javadoc/com/google/gwt/examples/cellview/CellTreeExample.java">CellTreeExample.java</a>.
</p>

<p>
<b>Code Example #2</b> - For a real-world example of CellTree, see <a href="https://gwt.googlesource.com/gwt/+/master/user/javadoc/com/google/gwt/examples/cellview/CellTreeExample2.java">CellTreeExample2.java</a>.
</p>

<pre class="prettyprint">
/**
 * Example of {@link CellTree}.  Shows a Tree consisting of strings.
 */
public class CellTreeExample implements EntryPoint {

  // The model that defines the nodes in the tree.
  private static class CustomTreeModel implements TreeViewModel {

    // Get the NodeInfo that provides the children of the specified value.
    public &lt;T&gt; NodeInfo&lt;?&gt; getNodeInfo(T value) {

      // Create some data in a data provider. Use the parent value as a prefix for the next level.
      ListDataProvider&lt;String&gt; dataProvider = new ListDataProvider&lt;String&gt;();
      for (int i = 0; i &lt; 2; i++) {
        dataProvider.getList().add(value + "." + String.valueOf(i));
      }

      // Return a node info that pairs the data with a cell.
      return new DefaultNodeInfo&lt;String&gt;(dataProvider, new TextCell());
    }

    // Check if the specified value represents a leaf node. Leaf nodes cannot be opened.
    public boolean isLeaf(Object value) {
      // The maximum length of a value is ten characters.
      return value.toString().length() &gt; 10;
    }
  }

  public void onModuleLoad() {
    // Create a model for the tree.
    TreeViewModel model = new CustomTreeModel();

    // Create the tree using the model. We specify the default value of the
    // hidden root node as "Item 1".
    CellTree tree = new CellTree(model, "Item 1");

    // Add the tree to the root layout panel.
    RootLayoutPanel.get().add(tree);
  }
}
</pre>

<p>
When you instantiate a CellTree, you must pass in an instance of a concrete class that implements interface <a href="/javadoc/latest/com/google/gwt/view/client/TreeViewModel.html">TreeViewModel</a>.  This concrete class gets and organizes the data into a hierarchy in the implementation of method getNodeInfo(value).  When a tree node is opened, the tree calls <a href="/javadoc/latest/com/google/gwt/view/client/TreeViewModel.html#getNodeInfo(T)">getNodeInfo(value)</a> to get the data provider and Cell used to render the child nodes.
</p>

<p>
You can add a SelectionModel to a CellTree, as shown in the <a href="#selection">SelectionModel example</a> below.
</p>


<h2 id="cellbrowser">Creating a CellBrowser</h2>

<p>
CellBrowser is similar to a CellTree but displays the node levels side-by-side.  The only code difference is you use a CellBrowser constructor and use a different <a href="/javadoc/latest/com/google/gwt/user/cellview/client/CellBrowser.Resources.html">CellBrowser.Resources</a> for CSS style (and images) to create side-by-side levels.
</p>

<p>
<b>Demo</b> - <a href="http://samples.gwtproject.org/samples/Showcase/Showcase.html#!CwCellBrowser">CwCellBrowser example</a> shows a CellBrowser.  It displays the same data in the same three levels as the above CellTree example, except that it displays the levels side-by-side.
</p>

<p>
<b>To Create a CellBrowser</b>
</ul>
  <li>Follow the above procedure for CellTree, but change the CellTree constructor to CellBrowser, as follows:</li>
<pre class="prettyprint">
    // Create the browser using the model.
    CellBrowser browser = new CellBrowser(model, "Item 1");
</pre>
</p>

<p>
<b>Code Example #1</b> - For a simple example of CellBrowser, see <a href="https://gwt.googlesource.com/gwt/+/master/user/javadoc/com/google/gwt/examples/cellview/CellBrowserExample.java">CellBrowerExample.java</a>. 
</p>

<p>
<b>Code Example #2</b> - For a real-world example of CellBrowser, see <a href="https://gwt.googlesource.com/gwt/+/master/user/javadoc/com/google/gwt/examples/cellview/CellBrowserExample2.java">CellBrowserExample2.java</a>.
</p>

<h2 class="h2-group" id="Cells">Cells</h2>

<h2 id="available">Available Cell Types</h2>

<p>
GWT offers a number of concrete Cell implementations that you can use immediately. See the <a href="http://samples.gwtproject.org/samples/Showcase/Showcase.html#!CwCellSampler">Cell Sampler </a>for examples.
</p>

<dl>
  <dt id="text-cells">
    Text
</dt>
<dd>
<a href="/javadoc/latest/com/google/gwt/cell/client/TextCell.html">TextCell</a> - A non-editable cell that displays text
</dd>

<dd>
<a href="/javadoc/latest/com/google/gwt/cell/client/ClickableTextCell.html">ClickableTextCell</a> - A text field; clicking on the cell causes its ValueUpdater to be called
</dd>

<dd>
<a href="/javadoc/latest/com/google/gwt/cell/client/EditTextCell.html">EditTextCell</a> - A cell that initially displays text;  when clicked, the text becomes editable
</dd>

<dd>
<a href="/javadoc/latest/com/google/gwt/cell/client/TextInputCell.html">TextInputCell</a> - A field for entering text
</dd>

<dt id="button-cells">
    Buttons, Checkboxes and Menus
</dt>

<dd>
<a href="/javadoc/latest/com/google/gwt/cell/client/ActionCell.html">ActionCell&lt;C&gt;</a> - A button that takes a delegate to perform actions on mouseUp
</dd>

<dd>
<a href="/javadoc/latest/com/google/gwt/cell/client/ButtonCell.html">ButtonCell</a> - A button whose text is the data value
</dd>

<dd>
<a href="/javadoc/latest/com/google/gwt/cell/client/CheckboxCell.html">CheckboxCell</a> - A checkbox that can be checked or unchecked
</dd>

<dd>
<a href="/javadoc/latest/com/google/gwt/cell/client/SelectionCell.html">SelectionCell</a> - A drop-down menu for selecting one of many choices
</dd>

<dt id="date-cells">
    Dates
</dt>

<dd>
<a href="/javadoc/latest/com/google/gwt/cell/client/DateCell.html">DateCell</a> - A date that conforms to a specified date format
</dd>

<dd>
<a href="/javadoc/latest/com/google/gwt/cell/client/DatePickerCell.html">DatePickerCell</a> - A date picker that displays a month calendar in which the user can select a date
</dd>

<dt id="image-cells">
    Images
</dt>

<dd>
<a href="/javadoc/latest/com/google/gwt/cell/client/ImageCell.html">ImageCell</a> - A cell used to render an image URL
</dd>

<dd>
<a href="/javadoc/latest/com/google/gwt/cell/client/ImageResourceCell.html">ImageResourceCell</a>  - A cell used to render an ImageResource
</dd>

<dd>
<a href="/javadoc/latest/com/google/gwt/cell/client/ImageLoadingCell.html">ImageLoadingCell</a> - A cell used to render an image URL.  A loading indicator is initially displayed
</dd>

<dt id="number-cells">
    Numbers
</dt>

<dd>
<a href="/javadoc/latest/com/google/gwt/cell/client/NumberCell.html">NumberCell</a> - A number that conforms to a specified number format
</dd>

<dt id="composition-cells">
    Compositions
</dt>

<dd>
<a href="/javadoc/latest/com/google/gwt/cell/client/CompositeCell.html">CompositeCell&lt;C&gt;</a> - A composition of multiple Cells.
</dd>

<dt id="decorator-cells">
    Decorators
</dt>

<dd>
<a href="/javadoc/latest/com/google/gwt/cell/client/IconCellDecorator.html">IconCellDecorator&lt;C&gt;</a> - A decorator that adds an icon to another Cell
</dd>

</dl>

<h2 id="custom-cell">Creating a Custom Cell</h2>

<p>
If you want more control, you can subclass AbstractCell, or you can implement the Cell interface directly to define how your Cell is rendered and how it responds to events.  See
the instructions in the <a href="DevGuideUiCustomCells.html">Creating Custom Cells Dev Guide</a> for detailed information.
</p>


<p>
<b>Demo</b> - <a href="http://samples.gwtproject.org/samples/Showcase/Showcase.html#!CwCellList">CwCellList example</a> shows a CellList&lt;ContactInfo&gt; (on the left). Each list item is a custom type ContactCell&lt;ContactInfo&gt;.  The right-hand widget is a normal Composite widget that renders the data for a selected contact.
</p>


<h2 class="h2-group" id="Selection_Data_Paging">Selection, Data and Paging</h2>

<h2 id="selection">Adding Selection Support</h2>

<p>
The <a href="/javadoc/latest/com/google/gwt/view/client/SelectionModel.html">SelectionModel</a> is a simple interface that views use to determine if an item is selected.  Cell widgets provide several selection models for selecting the children of a node:  DefaultSelectionModel, NoSelectionModel, SingleSelectionModel and MultiSelectionModel.  One of these is likely to fit your need.
</p>



<p>
For demonstrations of selection, the <a href="http://samples.gwtproject.org/samples/Showcase/Showcase.html#!CwCellList">CwCellList</a> widget creates a SingleSelectionModel, whereas CwCellTable implements a MultiSelectionModel using checkboxes.
</p>



<p>
Views or application code can call setSelected() to select an item. Views call isSelected() to determine if an item is selected. Views also subscribe to the SelectionModel so they can be informed of selection changes that arrive from outside the view.  In fact, you can extend DefaultSelectionModel and override isDefaultSelected().
</p>



<p>
This simple approach offers a lot of flexibility. A complex implementation can handle "select all" across multiple pages using a boolean to indicate that everything is selected, and then keep track of negative selections.
</p>



<p>
By using a subscription model, we can link selection across multiple views. If multiple views subscribe to a single SelectionModel, then selecting a row in one view will select the row in other views. This behavior is optional and can be avoided by using a single SelectionModel instance per view.
</p>



<p>
<b>Demo</b> - <a href="http://samples.gwtproject.org/samples/Showcase/Showcase.html#!CwCellList">CwCellList example</a> shows a cell widget that has a SelectionModel added to it. Clicking on an item selects it.
</p>



<p>
<b>To Add a Selection to a Cell Widget:</b>
</p>

<ol>
  <li>Create a cell widget.</li>
  <li>Choose a standard <a href="/javadoc/latest/com/google/gwt/view/client/SelectionModel.html">SelectionModel</a> (or roll your own).</li>
  <li>Add this SelectionModel to the cell widget using <a href="/javadoc/latest/index.html?com/google/gwt/user/cellview/client/AbstractHasData.html">setSelectionModel</a>(SelectionModel).</li>
  <li>Create a <a href="/javadoc/latest/com/google/gwt/view/client/SelectionChangeEvent.Handler.html">SelectionChangeEvent.Handler</a> implementing onSelectionChange.</li>
  <li>Add this handler to the SelectionModel using <a href="/javadoc/latest/com/google/gwt/view/client/SelectionModel.html#addSelectionChangeHandler(com.google.gwt.view.client.SelectionChangeEvent.Handler)">addSelectionChangeHandler</a>.</li>
</ol>

<p>
<b>Code Example</b> - The example of SelectionModel below is available at <a href="https://gwt.googlesource.com/gwt/+/master/user/javadoc/com/google/gwt/examples/cellview/CellListExample.java">CellListExample.java</a>.
</p>

<pre class="prettyprint">
/**
 * Example of {@link CellList}. This example shows a list of the days of the week.
 */
public class CellListExample implements EntryPoint {

  // The list of data to display.
  private static final List&lt;String&gt; DAYS = Arrays.asList("Sunday", "Monday",
      "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");

  public void onModuleLoad() {
    // Create a cell to render each value.
    TextCell textCell = new TextCell();

    // Create a CellList that uses the cell.
    CellList&lt;String&gt; cellList = new CellList&lt;String&gt;(textCell);
    cellList.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);

    // Add a selection model to handle user selection.
    final SingleSelectionModel&lt;String&gt; selectionModel = new SingleSelectionModel&lt;String&gt;();
    cellList.setSelectionModel(selectionModel);
    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
      public void onSelectionChange(SelectionChangeEvent event) {
        String selected = selectionModel.getSelectedObject();
        if (selected != null) {
          Window.alert("You selected: " + selected);
        }
      }
    });

    // Set the total row count. This isn't strictly necessary, but it affects
    // paging calculations, so its good habit to keep the row count up to date.
    cellList.setRowCount(DAYS.size(), true);

    // Push the data into the widget.
    cellList.setRowData(0, DAYS);

    // Add it to the root panel.
    RootPanel.get().add(cellList);
  }
}
</pre>


<p id="keys">
<b>Keys</b>
</p>

<!--
<p>
<span style="color: red;">jlabanca: You use the key if a data object can change, but should still be considered the same object for selection purposes.  For example, you can change the address of a ContactInfo, but its the same contact as long as the name is the same, so you would use the name as a key.  Often times, the key would be the unique database id assigned to the object.</span>
</p>
-->

<p>
Every DTO (Data Transfer Object) must have a key associated with it in order to be able to identify it as the same object, even though some of its properties may have changed.  For example, given a table of current stock prices, the stock price may change in one of the columns, but the row represents the same fundamental DTO.
</p>

<p>
Keys allow us to associate ViewData, such as selection state and validation information, with a DTO.  If you select some items in a table or list, then when the list refreshes with new data, you can maintain the same selection.
</p>

<p><b>Code Example</b> - The example of KeyProvider below is available at <a href="https://gwt.googlesource.com/gwt/+/master/user/javadoc/com/google/gwt/examples/view/KeyProviderExample.java">KeyProviderExample.java</a>.</p>

<pre class="prettyprint">
/**
  * Example of using a {@link ProvidesKey}.
  */
public class KeyProviderExample implements EntryPoint {

  // A simple data type that represents a contact.
  private static class Contact {
    private static int nextId = 0;

    private final int id;
    private String name;

    public Contact(String name) {
      nextId++;
      this.id = nextId;
      this.name = name;
    }
  }

  // A custom Cell used to render a Contact.
  private static class ContactCell extends AbstractCell<Contact> {
    @Override
    public void render(Contact value, Object key, SafeHtmlBuilder sb) {
      if (value != null) {
        sb.appendEscaped(value.name);
      }
    }
  }

  // The list of data to display.
  private static final List<Contact> CONTACTS = Arrays.asList(new Contact(
      "John"), new Contact("Joe"), new Contact("Michael"),
      new Contact("Sarah"), new Contact("George"));

  public void onModuleLoad() {
    // Define a key provider for a Contact. We use the unique ID as the key,
    // which allows to maintain selection even if the name changes.
    ProvidesKey<Contact> keyProvider = new ProvidesKey<Contact>() {
      public Object getKey(Contact item) {
        // Always do a null check.
        return (item == null) ? null : item.id;
      }
    };

    // Create a CellList using the keyProvider.
    CellList<Contact> cellList = new CellList<Contact>(new ContactCell(),
        keyProvider);

    // Push data into the CellList.
    cellList.setRowCount(CONTACTS.size(), true);
    cellList.setRowData(0, CONTACTS);

    // Add a selection model using the same keyProvider.
    SelectionModel<Contact> selectionModel = new SingleSelectionModel<Contact>(
        keyProvider);
    cellList.setSelectionModel(selectionModel);

    // Select a contact. The selectionModel will select based on the ID because
    // we used a keyProvider.
    Contact sarah = CONTACTS.get(3);
    selectionModel.setSelected(sarah, true);

    // Modify the name of the contact.
    sarah.name = "Sara";

    // Redraw the CellList. Sarah/Sara will still be selected because we
    // identify her by ID. If we did not use a keyProvider, Sara would not be
    // selected.
    cellList.redraw();

    // Add the widgets to the root panel.
    RootPanel.get().add(cellList);
  }
}
</pre>


<h2 id="data-provider">Providing Dynamic Data</h2>

<p>
We saw in a previous section <a href="#celllist">Creating a CellList and Setting Data</a> how to push data into a CellList.  However, in most applications, you want to display dynamic data or a range of data, not just a static list.  This section explains how to attach a data source to a cell widgets.
</p>

<p>
Cell widgets do not impose any restrictions on the data source.  Instead, the data source listens to the cell widget for changes in the visible range, then pushes new data to the cell widget.  The data source detects changes in the visible range by adding a
<a href="/javadoc/latest/com/google/gwt/view/client/RangeChangeEvent.Handler.html">RangeChangeEvent.Handler</a>
via addRangeChangeHandler().  The data source can then access data asynchronously, eventually calling
<a href="/javadoc/latest/com/google/gwt/view/client/HasData.html#setRowData(int, java.util.List)">HasData#setRowData()</a>
with the new data.
</p>

<p>
Fortunately, we provide a few convenience classes to make this even easier.  <a href="#list-data-provider">ListDataProvider</a> is a concrete data source that is backed by a 
<code>java.util.List</code>, which is useful if your data lives entirely on the client side.  If your data lives on a server, you can extend the abstract class 
<a href="#async-data-provider">AsyncDataProvider</a>, which you can override to connect to an asynchronous data source, such as a database running on a server.
</p>

<p>
Alternatively, you can create a <a href="#custom-data-provider">custom data source</a> by handling RangeChangeEvents directly. If you are
writing your own presenter logic to control a Cell widget, you might find it easier to write your own data source instead of using a data
provider.
</p>

<h3 id="list-data-provider">ListDataProvider</h3>

<p>
<a href="/javadoc/latest/com/google/gwt/view/client/ListDataProvider.html">ListDataProvider</a>
binds your cell widget to a <code>java.util.List</code>. Any changes to the internal list, which can be accessed via getList(), will be reflected in
the views. The views are updated at the end of the current event block, so you can make multiple synchronous changes without causing multiple refreshes
of the views.
</p>

<p>
<b>Code Example</b> - The example below updates the view through a ListDataProvider.
</p>

<pre class="prettyprint">
/**
 * Entry point classes define &lt;code&gt;onModuleLoad()&lt;/code&gt;.
 */
public class CellListExample implements EntryPoint {
  // The list of data to display.
  private static final List&lt;String&gt; DAYS = Arrays.asList("Sunday", "Monday",
      "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");

  public void onModuleLoad() {
    // Create a cell to render each value in the list.
    TextCell textCell = new TextCell();

    // Create a CellList that uses the cell.
    CellList&lt;String&gt; cellList = new CellList&lt;String&gt;(textCell);

    // Set the range to display. In this case, our visible range is smaller than
    // the data set.
    cellList.setVisibleRange(1, 3);

    // Create a data provider.
    ListDataProvider&lt;String&gt; dataProvider = new ListDataProvider&lt;String&gt;();
    
    // Connect the list to the data provider.
    dataProvider.addDataDisplay(cellList);
    
    // Add the data to the data provider, which automatically pushes it to the
    // widget. Our data provider will have seven values, but it will only push
    // the four that are in range to the list.
    List&lt;String&gt; list = dataProvider.getList();
    for (String day : DAYS) {
      list.add(day);
    }

    // Add it to the root panel.
    RootPanel.get().add(cellList);
  }
}
</pre>

<h3 id="async-data-provider">AsyncDataProvider</h3>

<p>
<a href="/javadoc/latest/com/google/gwt/view/client/AsyncDataProvider.html">AsyncListDataProvider</a>
binds your cell widget to an asynchronous data source. When the cell widget requests new data, the AsyncDataProvider fetches the new data and pushes it to the widget.
Just implement the onRangeChanged() method and request the data in the new Range for the specified cell widget.  When the data is returned, call updateRowCount()
and/or updateRowData() to push the data to the widgets.
</p>

<p>
<b>Basic Recipe:</b>
</p>

<ol>
  <li>Create a subclass of <a href="/javadoc/latest/com/google/gwt/view/client/AsyncDataProvider.html">AsyncDataProvider</a>.</li>
  <li>Implement <a href="/javadoc/latest/com/google/gwt/view/client/AbstractDataProvider.html#onRangeChanged(com.google.gwt.view.client.HasData)">onRangeChanged(HasData)</a>. 
    <ol style="list-style-type: lower-alpha">
      <li>Get the current range from the display</li>
      <li>Request the data from the server or data source</li>
    </ol>
  </li>
  <li>When the data is returned, call <a href="/javadoc/latest/com/google/gwt/view/client/AsyncDataProvider.html#updateRowData(int,%20java.util.List)">updateRowData() </a>to push the data to the widgets.</li>
</ol>
<p>
<b>Code Example</b> - The example below updates the view through a AsyncDataProvider.
</p>

<pre class="prettyprint">
/**
 * Entry point classes define &lt;code&gt;onModuleLoad()&lt;/code&gt;.
 */
public class CellListExample implements EntryPoint {
  // The list of data to display.
  private static final List&lt;String&gt; DAYS = Arrays.asList("Sunday", "Monday",
      "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");

  public void onModuleLoad() {
    // Create a cell to render each value in the list.
    TextCell textCell = new TextCell();

    // Create a CellList that uses the cell.
    final CellList&lt;String&gt; cellList = new CellList&lt;String&gt;(textCell);

    // Set the total row count. You might send an RPC request to determine the
    // total row count.
    cellList.setRowCount(DAYS.size(), true);

    // Set the range to display. In this case, our visible range is smaller than
    // the data set.
    cellList.setVisibleRange(1, 3);

    // Create a data provider.
    AsyncDataProvider&lt;String&gt; dataProvider = new AsyncDataProvider&lt;String&gt;() {
      @Override
      protected void onRangeChanged(HasData&lt;String&gt; display) {
        final Range range = display.getVisibleRange();

        // This timer is here to illustrate the asynchronous nature of this data
        // provider. In practice, you would use an asynchronous RPC call to
        // request data in the specified range.
        new Timer() {
          @Override
          public void run() {
            int start = range.getStart();
            int end = start + range.getLength();
            List&lt;String&gt; dataInRange = DAYS.subList(start, end);

            // Push the data back into the list.
            cellList.setRowData(start, dataInRange);
          }
        }.schedule(2000);
      }
    };

    // Connect the list to the data provider.
    dataProvider.addDataDisplay(cellList);

    // Add it to the root panel.
    RootPanel.get().add(cellList);
  }
}
</pre>

<h3 id="custom-data-provider">Custom Data Source</h3>

<p>
Cell widgets fire a 
<a href="/javadoc/latest/com/google/gwt/view/client/RangeChangeEvent.html">RangeChangeEvent</a>
when the user pages through the list. You can handle RangeChangeEvents from the view and push new data into the view accordingly. This is useful if
you are writing a presenter class for your cell widget.
</p>
<p>
<b>Code Example</b> - The example below handlers RangeChangeEvents from the view and pushes new data based on the new range.
</p>

<pre class="prettyprint">
/**
 * Example of using a {@link RangeChangeEvent.Handler} to push data into a
 * {@link CellList} when the range changes.
 */
public class RangeChangeHandlerExample implements EntryPoint {

  @Override
  public void onModuleLoad() {
    // Create a CellList.
    CellList<String> cellList = new CellList<String>(new TextCell());

    // Add a range change handler.
    cellList.addRangeChangeHandler(new RangeChangeEvent.Handler() {
      @Override
      public void onRangeChange(RangeChangeEvent event) {
        Range range = event.getNewRange();
        int start = range.getStart();
        int length = range.getLength();

        // Create the data to push into the view. At this point, you could send
        // an asynchronous RPC request to a server.
        List<String> data = new ArrayList<String>();
        for (int i = start; i < start + length; i++) {
          data.add("Item " + i);
        }

        // Push the data into the list.
        updateRowData(start, data);
      }
    });

    // Force the cellList to fire an initial range change event.
    cellList.setVisibleRangeAndClearData(new Range(0, 25), true);

    // Create paging controls.
    SimplePager pager = new SimplePager();
    pager.setDisplay(cellList);

    // Add the widgets to the root panel.
    VerticalPanel vPanel = new VerticalPanel();
    vPanel.add(pager);
    vPanel.add(cellList);
    RootPanel.get().add(vPanel);
  }
}
</pre>


<h2 id="paging">Adding Paging Controls</h2>

<p>
Paging is the operation of loading and bringing into view a range of data that is not currently loaded.  Paging improves initial load time of large data sets by loading only the data that is needed by the current view.
</p>

<p>
Two procedures follow &mdash; one for adding a standard SimplePager to a cell widget, and the other for adding custom paging controls to a cell widget.
</p>

<p>
<b>Demo</b> - <a href="http://samples.gwtproject.org/samples/Showcase/Showcase.html#!CwCellTable">CwCellTable example</a> shows a SimplePager control below a table.
</p>

<p>
<b>Code Example</b> - The example below is available at  <a href="https://gwt.googlesource.com/gwt/+/master/user/javadoc/com/google/gwt/examples/cellview/SimplePagerExample.java">SimplePagerExample.java</a>.
</p>

<p>
<b>To Add SimplePager to a Cell Widget:</b>
</p>

<ol>
  <li>Create an instance of <a href="/javadoc/latest/com/google/gwt/user/cellview/client/SimplePager.html">SimplePager</a> widget using its constructor.</li>
  <li>Assign the SimplePager to the cell widget you want to control using
      <a href="/javadoc/latest/com/google/gwt/user/cellview/client/SimplePager.html#setDisplay(com.google.gwt.view.client.HasRows)">setDisplay(HasRows)</a>.</li>
  <li>Add the SimplePager instance to the panel.</li>
</ol>

<pre class="prettyprint">
/**
 * Example of {@link SimplePager}.
 */
public class SimplePagerExample implements EntryPoint {

  public void onModuleLoad() {
    // Create a CellList.
    CellList&lt;String> cellList = new CellList&lt;String>(new TextCell());

    // Add a cellList to a data provider.
    ListDataProvider&lt;String> dataProvider = new ListDataProvider&lt;String>();
    List&lt;String> data = dataProvider.getList();
    for (int i = 0; i &lt; 200; i++) {
      data.add("Item " + i);
    }
    dataProvider.addDataDisplay(cellList);

    // Create a SimplePager.
    SimplePager pager = new SimplePager();

    // Set the cellList as the display.
    pager.setDisplay(cellList);

    // Add the pager and list to the page.
    VerticalPanel vPanel = new VerticalPanel();
    vPanel.add(pager);
    vPanel.add(cellList);
    RootPanel.get().add(vPanel);
  }
}</pre>

<p>
<b>To Add Custom Paging Controls to a Cell Widget:</b>
</p>

<ol>
  <li>Create a custom pager &mdash; extending
      <a href="/javadoc/latest/com/google/gwt/user/cellview/client/AbstractPager.html">AbstractPager</a>
      works for most use cases.  AbstractPager provides many convenience methods that your pager will use to
      change the visible range, including a method to hook up the cell widget.
    <ol style="list-style-type: lower-alpha">
      <li>AbstractPager is a Composite, so you need to define the Widget part of the pager and initialize AbstractPager by calling 
          <a href="/javadoc/latest/com/google/gwt/user/client/ui/Composite.html#initWidget(com.google.gwt.user.client.ui.Widget)">initWidget(Widget)</a>.</li>
      <li>You also need to override
          <a href="/javadoc/latest/com/google/gwt/user/cellview/client/AbstractPager.html#onRangeOrRowCountChanged()">onRangeOrRowCountChanged()</a>
          to update the widget when the visible range changes for any reason.
    </ol>
  </li>
  <li>Assign the pager to the cell widget you want to control using
      <a href="/javadoc/latest/com/google/gwt/user/cellview/client/SimplePager.html#setDisplay(com.google.gwt.view.client.HasRows)">setDisplay(HasRows)</a></li>
  <li>Add the custom pager to a panel.</li>
</ol>


<h2 id="updating-database">Updating a Database from Changes in a Cell</h2>

<p>
In most applications, the user takes actions in the user interface that should update the application's current state or send data back to the database (or data source).  These user actions might be clicking a checkbox, pressing a button, or entering text into a field and pressing "Save". 
</p>

<p>
This process varies slightly for a CellList, CellTree,
<!-- <span style="color: red;">doog3: how does it work for CellTree?</span> --> and CellTable, as described below.
</p>


<p class="note">
NOTE: In the case of ButtonCell, the value (the button text) doesn't actually change.  Instead, ValueUpdater serves the purpose of informing external code of a change or an important action, such as a click.
</p>

<h3 id="updating-from-celllist">Updating a Database From a CellList</h3>

<p>
Use a <a href="/javadoc/latest/com/google/gwt/cell/client/ValueUpdater.html">ValueUpdater</a> in a Column to allow the user to modify the content of the Cell (as is possible with TextInputCell).  The example below shows how to update data and handle invalid data.  The FieldUpdater's update method takes three arguments: the row index of the data object,  the data object that represents the field, and the new value of the Cell. 
</p>

<p>
When the user makes the change to the data, the Cell receives an event in its <a href="/javadoc/latest/com/google/gwt/cell/client/Cell.html#onBrowserEvent(com.google.gwt.dom.client.Element, C, java.lang.Object, com.google.gwt.dom.client.NativeEvent, com.google.gwt.cell.client.ValueUpdater)">onBrowserEvent</a> method.  For cells that support user interaction, onBrowserEvent calls the update method of the ValueUpdater, passing in the new value.
</p>

<p>
<b>Demo</b> - <i>(none)</i>
</p>
<p>
<b>Code Example</b> - The example below is available at <a href="https://gwt.googlesource.com/gwt/+/master/user/javadoc/com/google/gwt/examples/cellview/CellListValueUpdaterExample.java">CellListValueUpdaterExample.java</a>.
</p>

<p>
<b>To Update the Database from a CellList:</b>
</p>

<ol>
  <li>Create a class that implements <a href="/javadoc/latest/com/google/gwt/cell/client/ValueUpdater.html">ValueUpdater</a> to accept a new data value and send it to your database.</li>
  <li>Set the ValueUpdater to the CellList using <a href="/javadoc/latest/com/google/gwt/user/cellview/client/CellList.html#setValueUpdater(com.google.gwt.cell.client.ValueUpdater)">cellList.setValueUpdater</a>.</li>
</ol>

<p>
<b>Code Example - ValueUpdater</b>
</p>

<pre class="prettyprint">
/**
 * Example of using a {@link ValueUpdater} with a {@link CellList}.
 */
public class CellListValueUpdaterExample implements EntryPoint {

  /**
   * The list of data to display.
   */
  private static final List&lt;String&gt; DAYS = Arrays.asList("Sunday", "Monday",
      "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");

  public void onModuleLoad() {
    // Create a cell that will interact with a value updater.
    TextInputCell inputCell = new TextInputCell();

    // Create a CellList that uses the cell.
    CellList&lt;String&gt; cellList = new CellList&lt;String&gt;(inputCell);

    // Create a value updater that will be called when the value in a cell changes.
    ValueUpdater&lt;String&gt; valueUpdater = new ValueUpdater&lt;String&gt;() {
      public void update(String newValue) {
        Window.alert("You typed: " + newValue);
      }
    };

    // Add the value updater to the cellList.
    cellList.setValueUpdater(valueUpdater);

    // Set the total row count. This isn't strictly necessary, but it affects
    // paging calculations, so its good habit to keep the row count up to date.
    cellList.setRowCount(DAYS.size(), true);

    // Push the data into the widget.
    cellList.setRowData(0, DAYS);

    // Add it to the root panel.
    RootPanel.get().add(cellList);
  }
}
</pre>

<h3 id="updating-from-celltable">Updating a Database From a CellTable</h3>

<p>
Use a <a href="/javadoc/latest/com/google/gwt/cell/client/FieldUpdater.html">FieldUpdater</a> in a Column to allow the user to modify the content of the Cell (as is possible with TextInputCell).  The example below shows how to update data and handle invalid data.  The FieldUpdater's update method takes three arguments: the row index of the data object,  the data object that represents the field, and the new value of the Cell. 
</p>

<p>
<b>Demo</b> - <a href="http://samples.gwtproject.org/samples/Showcase/Showcase.html#!CwCellTable">CwCellTable example</a> lets you modify the First and Last names (these columns use <a href="#text-cells">EditTextCell</a>).
</p>

<p>
<b>To Update the Database from a CellTable:</b>
</p>

<ol>
  <li>Create a class that implements <a href="/javadoc/latest/com/google/gwt/cell/client/FieldUpdater.html">FieldUpdater</a> to accept a new data value and send it to your database. </li>
  <li>Set the FieldUpdater in the Column by calling  <a href="/javadoc/latest/com/google/gwt/user/cellview/client/Column.html#setFieldUpdater(com.google.gwt.cell.client.FieldUpdater)">column.setFieldUpdater(fieldUpdater)</a>.</li>
</ol>

<p>
<b>Code Example</b> - An example is available at <a href="https://gwt.googlesource.com/gwt/+/master/user/javadoc/com/google/gwt/examples/cellview/CellTableFieldUpdaterExample.java">CellTableFieldUpdaterExample.java</a>.
</p>

<pre class="prettyprint">
/**
 * Example of using a {@link FieldUpdater} with a {@link CellTable}.
 */
public class CellTableFieldUpdaterExample implements EntryPoint {

  /**
   * A simple data type that represents a contact with a unique ID.
   */
  private static class Contact {
    private static int nextId = 0;

    private final int id;
    private String name;

    public Contact(String name) {
      nextId++;
      this.id = nextId;
      this.name = name;
    }
  }

  /**
   * The list of data to display.
   */
  private static final List<Contact> CONTACTS = Arrays.asList(new Contact("John"), new Contact(
      "Joe"), new Contact("George"));

  /**
   * The key provider that allows us to identify Contacts even if a field
   * changes. We identify contacts by their unique ID.
   */
  private static final ProvidesKey<Contact> KEY_PROVIDER =
      new ProvidesKey<CellTableFieldUpdaterExample.Contact>() {
        @Override
        public Object getKey(Contact item) {
          return item.id;
        }
      };

  @Override
  public void onModuleLoad() {
    // Create a CellTable with a key provider.
    final CellTable<Contact> table = new CellTable<Contact>(KEY_PROVIDER);

    // Add a text input column to edit the name.
    final TextInputCell nameCell = new TextInputCell();
    Column<Contact, String> nameColumn = new Column<Contact, String>(nameCell) {
      @Override
      public String getValue(Contact object) {
        // Return the name as the value of this column.
        return object.name;
      }
    };
    table.addColumn(nameColumn, "Name");

    // Add a field updater to be notified when the user enters a new name.
    nameColumn.setFieldUpdater(new FieldUpdater<Contact, String>() {
      @Override
      public void update(int index, Contact object, String value) {
        // Inform the user of the change.
        Window.alert("You changed the name of " + object.name + " to " + value);

        // Push the changes into the Contact. At this point, you could send an
        // asynchronous request to the server to update the database.
        object.name = value;

        // Redraw the table with the new data.
        table.redraw();
      }
    });

    // Push the data into the widget.
    table.setRowData(CONTACTS);

    // Add it to the root panel.
    RootPanel.get().add(table);
  }
}
</pre>
