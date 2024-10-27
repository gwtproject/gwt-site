UiCellTable
===

A cell table (data presentation table) provides high-performance rendering of large data sets
in a tabular view. You can check out the
[Cell Table example](https://samples.gwtproject.org/samples/Showcase/Showcase.html#!CwCellTable)
in the GWT Showcase to see it in action.

This developer guide will walk you through some advanced features specific to CellTable, such as
column sorting. If you are not familiar with the cell widgets, you should read the
[Cell Widgets Developer Guide](DevGuideUiCellWidgets.html)
before continuing.

1.  [Column Sorting](#columnSorting)
2.  [Controlling Column Widths](#columnWidths)

## Column Sorting<a id="columnSorting"></a>

CellTable has built-in support for column sorting. Use
[Column.setSortable(boolean)](/javadoc/latest/com/google/gwt/user/cellview/client/Column.html#setSortable-boolean-)
to make a column sortable. Users will then be able to click on the column header and trigger a
[ColumnSortEvent](/javadoc/latest/com/google/gwt/user/cellview/client/ColumnSortEvent.html).
How you handle the event depends on how you push data into your CellTable.

### ColumnSorting with ListDataProvider

GWT provides a default implementation of 
[ColumnSortEvent.Handler](/javadoc/latest/com/google/gwt/user/cellview/client/ColumnSortEvent.Handler.html)
called [ColumnSortEvent.ListHandler](/javadoc/latest/com/google/gwt/user/cellview/client/ColumnSortEvent.ListHandler.html)
that is designed to perform local sorting of a java.util.List.

**Code Example** - The example below adds column sorting support to a CellTable backed by a ListDataProvider.

```java
/**
 * Entry point classes define <code>onModuleLoad()</code>.
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
  private static List<Contact> CONTACTS = Arrays.asList(new Contact("John",
      "123 Fourth Road"), new Contact("Mary", "222 Lancer Lane"), new Contact(
      "Zander", "94 Road Street"));

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

    // Make the name column sortable.
    nameColumn.setSortable(true);

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

    // Create a data provider.
    ListDataProvider<Contact> dataProvider = new ListDataProvider<Contact>();

    // Connect the table to the data provider.
    dataProvider.addDataDisplay(table);

    // Add the data to the data provider, which automatically pushes it to the
    // widget.
    List<Contact> list = dataProvider.getList();
    for (Contact contact : CONTACTS) {
      list.add(contact);
    }

    // Add a ColumnSortEvent.ListHandler to connect sorting to the
    // java.util.List.
    ListHandler<Contact> columnSortHandler = new ListHandler<Tester.Contact>(
        list);
    columnSortHandler.setComparator(nameColumn,
        new Comparator<Tester.Contact>() {
          public int compare(Contact o1, Contact o2) {
            if (o1 == o2) {
              return 0;
            }

            // Compare the name columns.
            if (o1 != null) {
              return (o2 != null) ? o1.name.compareTo(o2.name) : 1;
            }
            return -1;
          }
        });
    table.addColumnSortHandler(columnSortHandler);

    // We know that the data is sorted alphabetically by default.
    table.getColumnSortList().push(nameColumn);

    // Add it to the root panel.
    RootPanel.get().add(table);
  }
}
```

### ColumnSorting with AsyncDataProvider

GWT provides a default implementation of 
[ColumnSortEvent.Handler](/javadoc/latest/com/google/gwt/user/cellview/client/ColumnSortEvent.Handler.html)
called [ColumnSortEvent.AsyncHandler](/javadoc/latest/com/google/gwt/user/cellview/client/ColumnSortEvent.AsyncHandler.html)
that helps with asynchronous (server-side) column sorting. When the user sorts a Column, AsyncHandler calls
[HasData.setVisibleRangeAndClearData(Range, boolean)](/javadoc/latest/com/google/gwt/view/client/HasData.html#setVisibleRangeAndClearData-com.google.gwt.view.client.Range-boolean-),
which triggers a RangeChangeEvent to the AsyncDataProvider.

**Code Example** - The example below adds column sorting support to a CellTable backed by an AsyncDataProvider.

```java
/**
 * Entry point classes define <code>onModuleLoad()</code>.
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
  private static List<Contact> CONTACTS = Arrays.asList(new Contact("John",
      "123 Fourth Road"), new Contact("Mary", "222 Lancer Lane"), new Contact(
      "Zander", "94 Road Street"));

  public void onModuleLoad() {

    // Create a CellTable.
    final CellTable<Contact> table = new CellTable<Contact>();

    // Create name column.
    TextColumn<Contact> nameColumn = new TextColumn<Contact>() {
      @Override
      public String getValue(Contact contact) {
        return contact.name;
      }
    };

    // Make the name column sortable.
    nameColumn.setSortable(true);

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

    // Set the total row count. You might send an RPC request to determine the
    // total row count.
    table.setRowCount(CONTACTS.size(), true);

    // Set the range to display. In this case, our visible range is smaller than
    // the data set.
    table.setVisibleRange(0, 3);

    // Create a data provider.
    AsyncDataProvider<Contact> dataProvider = new AsyncDataProvider<Contact>() {
      @Override
      protected void onRangeChanged(HasData<Contact> display) {
        final Range range = display.getVisibleRange();

        // Get the ColumnSortInfo from the table.
        final ColumnSortList sortList = table.getColumnSortList();

        // This timer is here to illustrate the asynchronous nature of this data
        // provider. In practice, you would use an asynchronous RPC call to
        // request data in the specified range.
        new Timer() {
          @Override
          public void run() {
            int start = range.getStart();
            int end = start + range.getLength();
            // This sorting code is here so the example works. In practice, you
            // would sort on the server.
            Collections.sort(CONTACTS, new Comparator<Tester.Contact>() {
              public int compare(Contact o1, Contact o2) {
                if (o1 == o2) {
                  return 0;
                }

                // Compare the name columns.
                int diff = -1;
                if (o1 != null) {
                  diff = (o2 != null) ? o1.name.compareTo(o2.name) : 1;
                }
                return sortList.get(0).isAscending() ? diff : -diff;
              }
            });
            List<Contact> dataInRange = CONTACTS.subList(start, end);

            // Push the data back into the list.
            table.setRowData(start, dataInRange);
          }
        }.schedule(2000);
      }
    };

    // Connect the list to the data provider.
    dataProvider.addDataDisplay(table);

    // Add a ColumnSortEvent.AsyncHandler to connect sorting to the
    // AsyncDataPRrovider.
    AsyncHandler columnSortHandler = new AsyncHandler(table);
    table.addColumnSortHandler(columnSortHandler);

    // We know that the data is sorted alphabetically by default.
    table.getColumnSortList().push(nameColumn);

    // Add it to the root panel.
    RootPanel.get().add(table);
  }
}
```

## Controlling Column Widths<a id="columnWidths"></a>

By default, columns in a CellTable expand to fit the contents of the Cells. This works fine for a static table, but
if the content changes due to paging or user interaction, the columns might change width and appear jumpy. CellTable
provides an API that gives you fine grain control of how the available width is distributed between columns.

In order to gain fine-grain control over the width of columns, you must set the table layout to "fixed" by passing `true` into
[CellTable.setWidth(String, boolean)](/javadoc/latest/com/google/gwt/user/cellview/client/CellTable.html#setWidth-java.lang.String-boolean-).
Once in fixed-width mode, tables behave differently than they normally would. The following sections describe
recipes for achieving various effects.

**Code Example** - The example below creates a CellTable with fixed-width columns that expand to fill the available space.

```java
/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Tester implements EntryPoint {
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
  private static List<Contact> CONTACTS = Arrays.asList(new Contact("John",
      "123 Fourth Road"), new Contact("Mary", "222 Lancer Lane"));

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

    // Set the width of the table and put the table in fixed width mode.
    table.setWidth("100%", true);

    // Set the width of each column.
    table.setColumnWidth(nameColumn, 35.0, Unit.PCT);
    table.setColumnWidth(addressColumn, 65.0, Unit.PCT);

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

### Specify Exact Width of All Columns

If you want to assign a specific width to every column, then you must set the
table width to "auto" and assign an absolute width to every column.

**WARNING:** If you set the width of the table to "auto" and do not
set the width of a column, the column will not be visible. Columns default to
a width of 0.

```java
table.setWidth("auto", true);
    table.setColumnWidth(col0, 100.0, Unit.PX);
    table.setColumnWidth(col1, 150.0, Unit.PX);
    table.setColumnWidth(col2, 250.0, Unit.PX);
    table.setColumnWidth(col3, 100.0, Unit.PX);
```

### Specify Relative Width of All Columns

You can assign relative widths to every column by setting the width of the table
to a non-zero value (such as "100%" or "600px") and setting the width of each
Column in percentages.

**WARNING:** Whenever you set the width of one or more columns using
percentages, the percentages should add up to 100%. Failure to do so may result
in unintended layout issues.

```java
table.setWidth("100%", true);
    table.setColumnWidth(col0, 10.0, Unit.PCT);
    table.setColumnWidth(col1, 25.0, Unit.PCT);
    table.setColumnWidth(col2, 25.0, Unit.PCT);
    table.setColumnWidth(col3, 40.0, Unit.PCT);
```

### Mix Fixed and Relative Column Widths

One of the great features of fixed-width tables is that you can specify that some
columns should have a fixed width, while others should resize based on the width
of the table.  For example, you might fix the width of a checkbox column because
a checkbox is always about 25px wide, but let other columns resize.

In order to achieve this affect, set the width of the table to a non-zero value,
set the width of the fixed columns using non-relative units (such as px, em, or ex), and specify
the width of the remaining columns in percentages. If you only want one column
to be resizable, then set its width to 100%, and set the width of all other
columns in pixels.

**WARNING:** The width of the columns specified in percentages should
add up to 100%. If they do not, then columns specified in pixels will also resize
with the table in an unpredictable way.

```java
table.setWidth("100%", true);
    table.setColumnWidth(checkboxCol, 10.0, Unit.PX);
    table.setColumnWidth(nameCol, 35.0, Unit.PCT);
    table.setColumnWidth(descriptionCol, 65.0, Unit.PCT);
```

