UiCellWidgets
===

Cell widgets (data presentation widgets) are high-performance, lightweight widgets composed of Cells for displaying data.
Examples are [lists](https://samples.gwtproject.org/samples/Showcase/Showcase.html#!CwCellList),
[tables](https://samples.gwtproject.org/samples/Showcase/Showcase.html#!CwCellTable),
[trees](https://samples.gwtproject.org/samples/Showcase/Showcase.html#!CwCellTree) and
[browsers](https://samples.gwtproject.org/samples/Showcase/Showcase.html#!CwCellBrowser).
These widgets are designed to handle and display very large sets of data quickly.
A cell widget renders its user interface as an HTML string, using innerHTML instead of traditional DOM manipulation.
This design follows the flyweight pattern where data is accessed and cached only as needed, and passed to
flyweight Cell objects. A cell widget can accept data from any type of data source.  The data model handles
asynchronous updates as well as push updates. When you change the data, the view is automatically updated.

Cells are the basic blocks of a user interface and come in a variety of [available cell types]
(#available).  They render views of data, interpret browser events and can be selected.  A Cell has
a type based on the data that the cell represents;  for example, DatePickerCell is a `Cell<Date>`
that represents a Date and allows the user to select a new Date.  Cells must implement a render method that renders the typed value as an HTML string. In addition, cells can override onBrowserEvent to act as a flyweight that handles events that are fired on elements that were rendered by the cell.

For example, in the [CellList example](https://samples.gwtproject.org/samples/Showcase/Showcase.html#!CwCellList) of the Showcase, every selectable data record is rendered by a single Cell instance. Notice that the data that a single cell represents can be a composition of different data fields from the data source. In this example, the cell holds data of type ContactInfo, which represents a contact, including name, address and picture.

In the [CellTable example](https://samples.gwtproject.org/samples/Showcase/Showcase.html#!CwCellTable), a different Cell is used to render each Column of a row.  The five columns in this example present data from a boolean and four strings.

1.  [Cell Widgets](#Cell_Widgets)
    1.  [Demos and Code Examples](#demos)
    2.  [Creating a CellList and Setting Data](#celllist)
    3.  [Creating a CellTable](#celltable)
    4.  [Creating a CellTree](#celltree)
    5.  [Creating a CellBrowser](#cellbrowser)
2.  [Cells](#Cells)
    1.  [Available Cell Types](#available)
    2.  [Creating a Custom Cell](#custom-cell)
3.  [Selection, Data and Paging](#Selection_Data_Paging)
    1.  [Adding Selection Support](#selection)
    2.  [Providing Dynamic Data](#data-provider)
    3.  [Adding Paging Controls](#paging)
    4.  [Updating a Database from Changes in a Cell](#updating-database)

NOTE: CellPanel is not a cell widget. CellPanel is an abstract base class for GWT Panel Widgets that are implemented using a table element.

## Cell Widgets<a id="Cell_Widgets"></a>

### Demos and Code Examples<a id="demos"></a>

This document describes or points to three kinds of code examples, so you can jump in at any level.

*   **Live demos** - Visit the GWT Showcase for examples of widgets
[CellList](https://samples.gwtproject.org/samples/Showcase/Showcase.html#!CwCellList),
[CellTable](https://samples.gwtproject.org/samples/Showcase/Showcase.html#!CwCellTable),
[CellTree](https://samples.gwtproject.org/samples/Showcase/Showcase.html#!CwCellTree),
[CellBrowser](https://samples.gwtproject.org/samples/Showcase/Showcase.html#!CwCellBrowser), plus examples of Cells in the[Cell Sampler](http://samples.gwtproject.org/samples/Showcase/Showcase.html#!CwCellSampler).
Note: The prefix "Cw" in showcase class names stands for "ContentWidget", the parent class of each Showcase example.
*   **Simplified examples** - The code examples displayed in-line throughout this documented are short, simplified examples, often pared-down versions of the real-world examples.
*   **Real-world examples** - Most of the cell widgets also have source code examples (.java files) at    [cell widget code examples](https://github.com/gwtproject/gwt/blob/main/user/javadoc/com/google/gwt/examples/cellview).

These files are referenced, where appropriate, in the sections below.

### Creating a CellList and Setting Data<a id="celllist"></a>

CellList is the simplest cell widget, where data is rendered using cells of the same type in a
list.  For instance, you can create a `CellList<String>` that uses a `Cell<String>` to render
a list of Strings. For a fancier list view, you can create a custom cell, described at
[Creating a Custom Cell](#custom-cell).

**Demo** - [CwCellList example](https://samples.gwtproject.org/samples/Showcase/Showcase.html#!CwCellList) shows a `CellList<ContactInfo>` (on the left). Each list item is a custom
type `ContactCell<ContactInfo>`.  The right-hand widget is a normal Composite widget that
renders the data for a selected contact.

**To Create a CellList:**

1.  Create a [standard](#available) or [custom](#custom-cell) Cell to hold the list items.
2.  Create a [CellList](/javadoc/latest/com/google/gwt/user/cellview/client/CellList.html), passing the cell into its constructor.
3.  Access the data to populate the list.
4.  Call [setRowData](/javadoc/latest/com/google/gwt/user/cellview/client/AbstractHasData.html#setRowData-int-java.util.List-) on the CellList to add the data.

The data inserted in the last step is updated by the data provider ([ListDataProvider](/javadoc/latest/com/google/gwt/view/client/ListDataProvider.html) or [AsyncDataProvider](/javadoc/latest/com/google/gwt/view/client/AsyncDataProvider.html)).  If you need to allow the user to modify the content of a cell and update the database, use ValueUpdater instead of setRowData in the last step, as described in [Updating a Database From a CellList](#updating-from-celllist).

**Code Example** - The example below is available at [CellListExample.java](https://github.com/gwtproject/gwt/blob/main/user/javadoc/com/google/gwt/examples/cellview/CellListExample.java)

The following code is a very simple example that creates a CellList widget containing a single TextCell and sets data from the data source.  The list shows names.

```java
/**
 * Example of {@link CellList}. This example shows a list of the days of the week.
 */
public class CellListExample implements EntryPoint {

  // The list of data to display.
  private static final List<String> DAYS = Arrays.asList("Sunday", "Monday",
      "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");

  public void onModuleLoad() {
    // Create a cell to render each value in the list.
    TextCell textCell = new TextCell();

    // Create a CellList that uses the cell.
    CellList<String> cellList = new CellList<String>(textCell);

    // Set the total row count. This isn't strictly necessary, but it affects
    // paging calculations, so its good habit to keep the row count up to date.
    cellList.setRowCount(DAYS.size(), true);

    // Push the data into the widget.
    cellList.setRowData(0, DAYS);

    // Add it to the root panel.
    RootPanel.get().add(cellList);
  }
}
```

You can add a SelectionModel to a CellList, as shown in the [SelectionModel example](#selection) below.

### Creating a CellTable<a id="celltable"></a>

[CellTable](/javadoc/latest/com/google/gwt/user/cellview/client/CellTable.html) renders row values in columns.  A Column represents a single field in a data object. Every column defines getValue(), which retrieves the value for the column from the data object. Each column uses a Cell to render the column-specific data. Note that columns can return whatever object they want for getValue(), including the row object itself (for example, to allow columns that show calculations based on several row values).

A Header represents either a header or a footer in a table. A table can have a header and footer for each column.
A Header can span multiple columns if adjacent headers are equal (==) to each other.

**Demo** - [CwCellTable example](https://samples.gwtproject.org/samples/Showcase/Showcase.html#!CwCellTable)
shows a `CellTable<ContactInfo>`. Each row item has 5 columns rendered
respectively as a CheckboxCell, EditTextCell, EditTextCell, SelectionCell and TextCell.

**To Create a CellTable:**

1.  Create a [standard](#available) or [custom](#custom-cell) Cell for each column of data.
2.  Create a [CellTable](/javadoc/latest/com/google/gwt/user/cellview/client/CellTable.html)
3.  Create and add [Column](/javadoc/latest/com/google/gwt/user/cellview/client/Column.html)s to the CellTable.
4.  Access the data to populate the list.
5.  Add the data to the CellTable by calling [setRowData](/javadoc/latest/com/google/gwt/user/cellview/client/AbstractHasData.html#setRowData-int-java.util.List-) on the CellTable for each Column.

The data inserted in the last step is updated by the data provider ([ListDataProvider](/javadoc/latest/com/google/gwt/view/client/ListDataProvider.html) or [AsyncDataProvider](/javadoc/latest/com/google/gwt/view/client/AsyncDataProvider.html)).  If you need to allow the user to modify the content of a cell and update the database, use FieldUpdater instead of setRowData in the last step, as described in [Updating a Database From a CellTable](#updating-from-celltable).

**More Information** - Read the [Cell Table Developer Guide](DevGuideUiCellTable.html) for more
information about CellTable-specific features, such as column sorting.

**Code Example** - The example below is a pared-down version of [CellTableExample.java](https://github.com/gwtproject/gwt/blob/main/user/javadoc/com/google/gwt/examples/cellview/CellTableExample.java)

```java
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
  private static List<Contact> CONTACTS = Arrays.asList(
    new Contact("John", "123 Fourth Road"),
    new Contact("Mary", "222 Lancer Lane"));

  public void onModuleLoad() {

    // Create a CellTable.
    CellTable<Contact> table = new CellTable<Contact>();

    // Create name column.
    TextColumn<Contact> nameColumn = new TextColumn<Contact>() {
      @Override
      public String getValue(Contact contact) {
        return contact.name;
      }
    };

    // Create address column.
    TextColumn<Contact> addressColumn = new TextColumn<Contact>() {
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
```

You can add a SelectionModel to a CellTable, as shown in the [SelectionModel example](#selection) below.

### Creating a CellTree<a id="celltree"></a>

CellTree renders a hierarchy of nodes, such as this [CwCellTree](https://samples.gwtproject.org/samples/Showcase/Showcase.html#!CwCellTree).  A node can be either a leaf node or have children.  Thus, a CellTree can have levels of nodes that go progressively deeper.  A node is represented by a NodeInfo, which contains all of the information needed to render a single node.

Each node has a Cell of a specific type;  usually, all Cells at a given level are of the same type, but that isn't required.  The example has a top level of nodes with each cell having an image and string.  Likewise, the second and third levels of cells have their own distinct types.  In addition to a cell, a node also has a DataProvider, to provide the data to the children of the NodeInfo, and a SelectionModel, to indicate how it can be selected by the user.  

The TreeViewModel provides the NodeInfo for each child node.  When a node is opened, CellTree will call [getNodeInfo()](/javadoc/latest/com/google/gwt/view/client/TreeViewModel.html#getNodeInfo-T-) on TreeViewModel to get the NodeInfo used to render the children.

A CellTree can have its own CSS styles and its own resources, such as images that the user clicks on to open or close a node.  It can also respond to browser events.  In addition, a CellTree can have built-in animation for progressively revealing or hiding children when its node opens or closes. 

**Demo** - [CwCellTree example](https://samples.gwtproject.org/samples/Showcase/Showcase.html#!CwCellTree) shows a CellTree.  It has three levels rendered respectively as custom types CategoryCell, LetterCountCell and ContactCell (the same type from the [CellList](#celllist) demo).  The checkbox has an update method to select the ContactCell when checked.

**Creating a CellTree:**

1.  Define a [TreeViewModel](/javadoc/latest/com/google/gwt/view/client/TreeViewModel.html) and [getNodeInfo](/javadoc/latest/com/google/gwt/view/client/TreeViewModel.html#getNodeInfo-T-)
    1.  In getNodeInfo, create a data provider for the child nodes.
    2.  Populate the data provider with data.
    3.  Create a [standard](#available) or [custom](#custom-cell) Cell to render the children.
2.  Create an instance of your TreeViewModel class.
3.  Create a [CellTree](/javadoc/latest/com/google/gwt/user/cellview/client/CellTree.html), passing in the TreeViewModel instance.

**Code Example #1** - The example below is a simplified example of CellTree, and is available at  [CellTreeExample.java](https://github.com/gwtproject/gwt/blob/main/user/javadoc/com/google/gwt/examples/cellview/CellTreeExample.java).

**Code Example #2** - For a real-world example of CellTree, see [CellTreeExample2.java](https://github.com/gwtproject/gwt/blob/main/user/javadoc/com/google/gwt/examples/cellview/CellTreeExample2.java).

```java
/**
 * Example of {@link CellTree}.  Shows a Tree consisting of strings.
 */
public class CellTreeExample implements EntryPoint {

  // The model that defines the nodes in the tree.
  private static class CustomTreeModel implements TreeViewModel {

    // Get the NodeInfo that provides the children of the specified value.
    public <T> NodeInfo<?> getNodeInfo(T value) {

      // Create some data in a data provider. Use the parent value as a prefix for the next level.
      ListDataProvider<String> dataProvider = new ListDataProvider<String>();
      for (int i = 0; i < 2; i++) {
        dataProvider.getList().add(value + "." + String.valueOf(i));
      }

      // Return a node info that pairs the data with a cell.
      return new DefaultNodeInfo<String>(dataProvider, new TextCell());
    }

    // Check if the specified value represents a leaf node. Leaf nodes cannot be opened.
    public boolean isLeaf(Object value) {
      // The maximum length of a value is ten characters.
      return value.toString().length() > 10;
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
```

When you instantiate a CellTree, you must pass in an instance of a concrete class that implements interface [TreeViewModel](/javadoc/latest/com/google/gwt/view/client/TreeViewModel.html).  This concrete class gets and organizes the data into a hierarchy in the implementation of method getNodeInfo(value).  When a tree node is opened, the tree calls [getNodeInfo(value)](/javadoc/latest/com/google/gwt/view/client/TreeViewModel.html#getNodeInfo-T-) to get the data provider and Cell used to render the child nodes.

You can add a SelectionModel to a CellTree, as shown in the [SelectionModel example](#selection) below.

## Creating a CellBrowser

CellBrowser is similar to a CellTree but displays the node levels side-by-side.  The only code difference is you use a CellBrowser constructor and use a different [CellBrowser.Resources](/javadoc/latest/com/google/gwt/user/cellview/client/CellBrowser.Resources.html) for CSS style (and images) to create side-by-side levels.

**Demo** - [CwCellBrowser example](https://samples.gwtproject.org/samples/Showcase/Showcase.html#!CwCellBrowser) shows a CellBrowser.  It displays the same data in the same three levels as the above CellTree example, except that it displays the levels side-by-side.

**To Create a CellBrowser**

*   Follow the above procedure for CellTree, but change the CellTree constructor to CellBrowser, as follows:

```java
// Create the browser using the model.
    CellBrowser browser = new CellBrowser(model, "Item 1");
```

**Code Example #1** - For a simple example of CellBrowser, see [CellBrowerExample.java](https://github.com/gwtproject/gwt/blob/main/user/javadoc/com/google/gwt/examples/cellview/CellBrowserExample.java). 

**Code Example #2** - For a real-world example of CellBrowser, see [CellBrowserExample2.java](https://github.com/gwtproject/gwt/blob/main/user/javadoc/com/google/gwt/examples/cellview/CellBrowserExample2.java).

## Cells<a id="Cells"></a>

### Available Cell Types<a id="available"></a>

GWT offers a number of concrete Cell implementations that you can use immediately. See the [Cell Sampler ](https://samples.gwtproject.org/samples/Showcase/Showcase.html#!CwCellSampler)for examples.

* Text <a id="text-cells"></a>
    * [TextCell](/javadoc/latest/com/google/gwt/cell/client/TextCell.html) - A non-editable cell that displays text
    * [ClickableTextCell](/javadoc/latest/com/google/gwt/cell/client/ClickableTextCell.html) - A text field; clicking on the cell causes its ValueUpdater to be called
    * [EditTextCell](/javadoc/latest/com/google/gwt/cell/client/EditTextCell.html) - A cell that initially displays text;  when clicked, the text becomes editable
    * [TextInputCell](/javadoc/latest/com/google/gwt/cell/client/TextInputCell.html) - A field for entering text
* Buttons, Checkboxes and Menus <a id="button-cells"></a>
    * [ActionCell](/javadoc/latest/com/google/gwt/cell/client/ActionCell.html) - A button that takes a delegate to perform actions on mouseUp
    * [ButtonCell](/javadoc/latest/com/google/gwt/cell/client/ButtonCell.html) - A button whose text is the data value
    * [CheckboxCell](/javadoc/latest/com/google/gwt/cell/client/CheckboxCell.html) - A checkbox that can be checked or unchecked
    * [SelectionCell](/javadoc/latest/com/google/gwt/cell/client/SelectionCell.html) - A drop-down menu for selecting one of many choices
* Dates <a id="date-cells"></a>
    * [DateCell](/javadoc/latest/com/google/gwt/cell/client/DateCell.html) - A date that conforms to a specified date format
    * [DatePickerCell](/javadoc/latest/com/google/gwt/cell/client/DatePickerCell.html) - A date picker that displays a month calendar in which the user can select a date
* Images <a id="image-cells"></a>
    * [ImageCell](/javadoc/latest/com/google/gwt/cell/client/ImageCell.html) - A cell used to render an image URL
    * [ImageResourceCell](/javadoc/latest/com/google/gwt/cell/client/ImageResourceCell.html)  - A cell used to render an ImageResource
    * [ImageLoadingCell](/javadoc/latest/com/google/gwt/cell/client/ImageLoadingCell.html) - A cell used to render an image URL.  A loading indicator is initially displayed
* Numbers <a id="number-cells"></a>
    * [NumberCell](/javadoc/latest/com/google/gwt/cell/client/NumberCell.html) - A number that conforms to a specified number format
* Compositions <a id="composition-cells"></a>
    * [CompositeCell](/javadoc/latest/com/google/gwt/cell/client/CompositeCell.html) - A composition of multiple Cells.
* Decorators <a id="decorator-cells"></a>
    * [IconCellDecorator](/javadoc/latest/com/google/gwt/cell/client/IconCellDecorator.html) - A decorator that adds an icon to another Cell

### Creating a Custom Cell<a id="custom-cell"></a>

If you want more control, you can subclass AbstractCell, or you can implement the Cell interface directly to define how your Cell is rendered and how it responds to events.  See
the instructions in the [Creating Custom Cells Dev Guide](DevGuideUiCustomCells.html) for detailed information.

**Demo** - [CwCellList example](https://samples.gwtproject.org/samples/Showcase/Showcase.html#!CwCellList)
shows a `CellList<ContactInfo>` (on the left). Each list item is a custom
type `ContactCell<ContactInfo>`.  The right-hand widget is a normal Composite widget that
renders the data for a selected contact.

## Selection, Data and Paging<a id="Selection_Data_Paging"></a>

### Adding Selection Support<a id="selection"></a>

The [SelectionModel](/javadoc/latest/com/google/gwt/view/client/SelectionModel.html) is a simple interface that views use to determine if an item is selected.  Cell widgets provide several selection models for selecting the children of a node:  DefaultSelectionModel, NoSelectionModel, SingleSelectionModel and MultiSelectionModel.  One of these is likely to fit your need.

For demonstrations of selection, the [CwCellList](https://samples.gwtproject.org/samples/Showcase/Showcase.html#!CwCellList) widget creates a SingleSelectionModel, whereas CwCellTable implements a MultiSelectionModel using checkboxes.

Views or application code can call setSelected() to select an item. Views call isSelected() to determine if an item is selected. Views also subscribe to the SelectionModel so they can be informed of selection changes that arrive from outside the view.  In fact, you can extend DefaultSelectionModel and override isDefaultSelected().

This simple approach offers a lot of flexibility. A complex implementation can handle "select all" across multiple pages using a boolean to indicate that everything is selected, and then keep track of negative selections.

By using a subscription model, we can link selection across multiple views. If multiple views subscribe to a single SelectionModel, then selecting a row in one view will select the row in other views. This behavior is optional and can be avoided by using a single SelectionModel instance per view.

**Demo** - [CwCellList example](https://samples.gwtproject.org/samples/Showcase/Showcase.html#!CwCellList) shows a cell widget that has a SelectionModel added to it. Clicking on an item selects it.

**To Add a Selection to a Cell Widget:**

1.  Create a cell widget.
2.  Choose a standard [SelectionModel](/javadoc/latest/com/google/gwt/view/client/SelectionModel.html) (or roll your own).
3.  Add this SelectionModel to the cell widget using [setSelectionModel](/javadoc/latest/index.html?com/google/gwt/user/cellview/client/AbstractHasData.html)(SelectionModel).
4.  Create a [SelectionChangeEvent.Handler](/javadoc/latest/com/google/gwt/view/client/SelectionChangeEvent.Handler.html) implementing onSelectionChange.
5.  Add this handler to the SelectionModel using [addSelectionChangeHandler](/javadoc/latest/com/google/gwt/view/client/SelectionModel.html#addSelectionChangeHandler-com.google.gwt.view.client.SelectionChangeEvent.Handler-).

**Code Example** - The example of SelectionModel below is available at [CellListExample.java](https://github.com/gwtproject/gwt/blob/main/user/javadoc/com/google/gwt/examples/cellview/CellListExample.java).

```java
/**
 * Example of {@link CellList}. This example shows a list of the days of the week.
 */
public class CellListExample implements EntryPoint {

  // The list of data to display.
  private static final List<String> DAYS = Arrays.asList("Sunday", "Monday",
      "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");

  public void onModuleLoad() {
    // Create a cell to render each value.
    TextCell textCell = new TextCell();

    // Create a CellList that uses the cell.
    CellList<String> cellList = new CellList<String>(textCell);
    cellList.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);

    // Add a selection model to handle user selection.
    final SingleSelectionModel<String> selectionModel = new SingleSelectionModel<String>();
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
```

**Keys**

Every DTO (Data Transfer Object) must have a key associated with it in order to be able to identify it as the same object, even though some of its properties may have changed.  For example, given a table of current stock prices, the stock price may change in one of the columns, but the row represents the same fundamental DTO.

Keys allow us to associate ViewData, such as selection state and validation information, with a DTO.  If you select some items in a table or list, then when the list refreshes with new data, you can maintain the same selection.

**Code Example** - The example of KeyProvider below is available at [KeyProviderExample.java](https://github.com/gwtproject/gwt/blob/main/user/javadoc/com/google/gwt/examples/view/KeyProviderExample.java).

```java
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
```

### Providing Dynamic Data<a id="data-provider"></a>

We saw in a previous section [Creating a CellList and Setting Data](#celllist) how to push data into a CellList.  However, in most applications, you want to display dynamic data or a range of data, not just a static list.  This section explains how to attach a data source to a cell widgets.

Cell widgets do not impose any restrictions on the data source.  Instead, the data source listens to the cell widget for changes in the visible range, then pushes new data to the cell widget.  The data source detects changes in the visible range by adding a
[RangeChangeEvent.Handler](/javadoc/latest/com/google/gwt/view/client/RangeChangeEvent.Handler.html)
via addRangeChangeHandler().  The data source can then access data asynchronously, eventually calling
[HasData#setRowData()](/javadoc/latest/com/google/gwt/view/client/HasData.html#setRowData-int-java.util.List-)
with the new data.

Fortunately, we provide a few convenience classes to make this even easier.  [ListDataProvider](#list-data-provider) is a concrete data source that is backed by a 
`java.util.List`, which is useful if your data lives entirely on the client side.  If your data lives on a server, you can extend the abstract class 
[AsyncDataProvider](#async-data-provider), which you can override to connect to an asynchronous data source, such as a database running on a server.

Alternatively, you can create a [custom data source](#custom-data-provider) by handling RangeChangeEvents directly. If you are
writing your own presenter logic to control a Cell widget, you might find it easier to write your own data source instead of using a data
provider.

### ListDataProvider<a id="list-data-provider"></a>

[ListDataProvider](/javadoc/latest/com/google/gwt/view/client/ListDataProvider.html)
binds your cell widget to a `java.util.List`. Any changes to the internal list, which can be accessed via getList(), will be reflected in
the views. The views are updated at the end of the current event block, so you can make multiple synchronous changes without causing multiple refreshes
of the views.

**Code Example** - The example below updates the view through a ListDataProvider.

```java
/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class CellListExample implements EntryPoint {
  // The list of data to display.
  private static final List<String> DAYS = Arrays.asList("Sunday", "Monday",
      "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");

  public void onModuleLoad() {
    // Create a cell to render each value in the list.
    TextCell textCell = new TextCell();

    // Create a CellList that uses the cell.
    CellList<String> cellList = new CellList<String>(textCell);

    // Set the range to display. In this case, our visible range is smaller than
    // the data set.
    cellList.setVisibleRange(1, 3);

    // Create a data provider.
    ListDataProvider<String> dataProvider = new ListDataProvider<String>();
    
    // Connect the list to the data provider.
    dataProvider.addDataDisplay(cellList);
    
    // Add the data to the data provider, which automatically pushes it to the
    // widget. Our data provider will have seven values, but it will only push
    // the four that are in range to the list.
    List<String> list = dataProvider.getList();
    for (String day : DAYS) {
      list.add(day);
    }

    // Add it to the root panel.
    RootPanel.get().add(cellList);
  }
}
```

### AsyncDataProvider<a id="async-data-provider"></a>

[AsyncListDataProvider](/javadoc/latest/com/google/gwt/view/client/AsyncDataProvider.html)
binds your cell widget to an asynchronous data source. When the cell widget requests new data, the AsyncDataProvider fetches the new data and pushes it to the widget.
Just implement the onRangeChanged() method and request the data in the new Range for the specified cell widget.  When the data is returned, call updateRowCount()
and/or updateRowData() to push the data to the widgets.

**Basic Recipe:**

1.  Create a subclass of [AsyncDataProvider](/javadoc/latest/com/google/gwt/view/client/AsyncDataProvider.html).
2.  Implement [onRangeChanged(HasData)](/javadoc/latest/com/google/gwt/view/client/AbstractDataProvider.html#onRangeChanged-com.google.gwt.view.client.HasData-).
    1.  Get the current range from the display
    2.  Request the data from the server or data source
3.  When the data is returned, call [updateRowData()](/javadoc/latest/com/google/gwt/view/client/AsyncDataProvider.html#updateRowData-int-java.util.List-) to push the data to the widgets.

**Code Example** - The example below updates the view through a AsyncDataProvider.

```java
/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class CellListExample implements EntryPoint {
  // The list of data to display.
  private static final List<String> DAYS = Arrays.asList("Sunday", "Monday",
      "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");

  public void onModuleLoad() {
    // Create a cell to render each value in the list.
    TextCell textCell = new TextCell();

    // Create a CellList that uses the cell.
    final CellList<String> cellList = new CellList<String>(textCell);

    // Set the total row count. You might send an RPC request to determine the
    // total row count.
    cellList.setRowCount(DAYS.size(), true);

    // Set the range to display. In this case, our visible range is smaller than
    // the data set.
    cellList.setVisibleRange(1, 3);

    // Create a data provider.
    AsyncDataProvider<String> dataProvider = new AsyncDataProvider<String>() {
      @Override
      protected void onRangeChanged(HasData<String> display) {
        final Range range = display.getVisibleRange();

        // This timer is here to illustrate the asynchronous nature of this data
        // provider. In practice, you would use an asynchronous RPC call to
        // request data in the specified range.
        new Timer() {
          @Override
          public void run() {
            int start = range.getStart();
            int end = start + range.getLength();
            List<String> dataInRange = DAYS.subList(start, end);

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
```

### Custom Data Source<a id="custom-data-provider"></a>

Cell widgets fire a 
[RangeChangeEvent](/javadoc/latest/com/google/gwt/view/client/RangeChangeEvent.html)
when the user pages through the list. You can handle RangeChangeEvents from the view and push new data into the view accordingly. This is useful if
you are writing a presenter class for your cell widget.

**Code Example** - The example below handlers RangeChangeEvents from the view and pushes new data based on the new range.

```java
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
```

## Adding Paging Controls<a id="paging"></a>

Paging is the operation of loading and bringing into view a range of data that is not currently loaded.  Paging improves initial load time of large data sets by loading only the data that is needed by the current view.

Two procedures follow &mdash; one for adding a standard SimplePager to a cell widget, and the other for adding custom paging controls to a cell widget.

**Demo** - [CwCellTable example](https://samples.gwtproject.org/samples/Showcase/Showcase.html#!CwCellTable) shows a SimplePager control below a table.

**Code Example** - The example below is available at  [SimplePagerExample.java](https://github.com/gwtproject/gwt/blob/main/user/javadoc/com/google/gwt/examples/cellview/SimplePagerExample.java).

**To Add SimplePager to a Cell Widget:**

1.  Create an instance of [SimplePager](/javadoc/latest/com/google/gwt/user/cellview/client/SimplePager.html) widget using its constructor.
2.  Assign the SimplePager to the cell widget you want to control using [setDisplay(HasRows)](/javadoc/latest/com/google/gwt/user/cellview/client/SimplePager.html#setDisplay-com.google.gwt.view.client.HasRows-).
3.  Add the SimplePager instance to the panel.

```java
/**
 * Example of {@link SimplePager}.
 */
public class SimplePagerExample implements EntryPoint {

  public void onModuleLoad() {
    // Create a CellList.
    CellList<String> cellList = new CellList<String>(new TextCell());

    // Add a cellList to a data provider.
    ListDataProvider<String> dataProvider = new ListDataProvider<String>();
    List<String> data = dataProvider.getList();
    for (int i = 0; i < 200; i++) {
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
}
```

**To Add Custom Paging Controls to a Cell Widget:**

1.  Create a custom pager &mdash; extending [AbstractPager](/javadoc/latest/com/google/gwt/user/cellview/client/AbstractPager.html) works for most use cases.  AbstractPager provides many convenience methods that your pager will use to change the visible range, including a method to hook up the cell widget.
    1.  AbstractPager is a Composite, so you need to define the Widget part of the pager and initialize AbstractPager by calling [initWidget(Widget)](/javadoc/latest/com/google/gwt/user/client/ui/Composite.html#initWidget-com.google.gwt.user.client.ui.Widget-).
    2.  You also need to override [onRangeOrRowCountChanged()](/javadoc/latest/com/google/gwt/user/cellview/client/AbstractPager.html#onRangeOrRowCountChanged--) to update the widget when the visible range changes for any reason.
2.  Assign the pager to the cell widget you want to control using [setDisplay(HasRows)](/javadoc/latest/com/google/gwt/user/cellview/client/SimplePager.html#setDisplay-com.google.gwt.view.client.HasRows-)
3.  Add the custom pager to a panel.

### Updating a Database from Changes in a Cell<a id="updating-database"></a>

In most applications, the user takes actions in the user interface that should update the application's current state or send data back to the database (or data source).  These user actions might be clicking a checkbox, pressing a button, or entering text into a field and pressing "Save". 

This process varies slightly for a CellList, CellTree,
<!-- <span style="color: red;">doog3: how does it work for CellTree? --> and CellTable, as described below.

NOTE: In the case of ButtonCell, the value (the button text) doesn't actually change.  Instead, ValueUpdater serves the purpose of informing external code of a change or an important action, such as a click.

#### Updating a Database From a CellList<a id="updating-from-celllist"></a>

Use a [ValueUpdater](/javadoc/latest/com/google/gwt/cell/client/ValueUpdater.html) in a Column to allow the user to modify the content of the Cell (as is possible with TextInputCell).  The example below shows how to update data and handle invalid data.  The FieldUpdater's update method takes three arguments: the row index of the data object,  the data object that represents the field, and the new value of the Cell. 

When the user makes the change to the data, the Cell receives an event in its [onBrowserEvent](/javadoc/latest/com/google/gwt/cell/client/Cell.html#onBrowserEvent-com.google.gwt.dom.client.Element-C-java.lang.Object-com.google.gwt.dom.client.NativeEvent-com.google.gwt.cell.client.ValueUpdater-) method.  For cells that support user interaction, onBrowserEvent calls the update method of the ValueUpdater, passing in the new value.

**Demo** - _(none)_

**Code Example** - The example below is available at [CellListValueUpdaterExample.java](https://github.com/gwtproject/gwt/blob/main/user/javadoc/com/google/gwt/examples/cellview/CellListValueUpdaterExample.java).

**To Update the Database from a CellList:**

1.  Create a class that implements [ValueUpdater](/javadoc/latest/com/google/gwt/cell/client/ValueUpdater.html) to accept a new data value and send it to your database.
2.  Set the ValueUpdater to the CellList using [cellList.setValueUpdater](/javadoc/latest/com/google/gwt/user/cellview/client/CellList.html#setValueUpdater-com.google.gwt.cell.client.ValueUpdater-).

**Code Example - ValueUpdater**

```java
/**
 * Example of using a {@link ValueUpdater} with a {@link CellList}.
 */
public class CellListValueUpdaterExample implements EntryPoint {

  /**
   * The list of data to display.
   */
  private static final List<String> DAYS = Arrays.asList("Sunday", "Monday",
      "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");

  public void onModuleLoad() {
    // Create a cell that will interact with a value updater.
    TextInputCell inputCell = new TextInputCell();

    // Create a CellList that uses the cell.
    CellList<String> cellList = new CellList<String>(inputCell);

    // Create a value updater that will be called when the value in a cell changes.
    ValueUpdater<String> valueUpdater = new ValueUpdater<String>() {
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
```

#### Updating a Database From a CellTable<a id="updating-from-celltable"></a>

Use a [FieldUpdater](/javadoc/latest/com/google/gwt/cell/client/FieldUpdater.html) in a Column to allow the user to modify the content of the Cell (as is possible with TextInputCell).  The example below shows how to update data and handle invalid data.  The FieldUpdater's update method takes three arguments: the row index of the data object,  the data object that represents the field, and the new value of the Cell. 

**Demo** - [CwCellTable example](https://samples.gwtproject.org/samples/Showcase/Showcase.html#!CwCellTable) lets you modify the First and Last names (these columns use [EditTextCell](#text-cells)).

**To Update the Database from a CellTable:**

1.  Create a class that implements [FieldUpdater](/javadoc/latest/com/google/gwt/cell/client/FieldUpdater.html) to accept a new data value and send it to your database.
2.  Set the FieldUpdater in the Column by calling  [column.setFieldUpdater(fieldUpdater)](/javadoc/latest/com/google/gwt/user/cellview/client/Column.html#setFieldUpdater-com.google.gwt.cell.client.FieldUpdater-).

**Code Example** - An example is available at [CellTableFieldUpdaterExample.java](https://github.com/gwtproject/gwt/blob/main/user/javadoc/com/google/gwt/examples/cellview/CellTableFieldUpdaterExample.java).

```java
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
```
