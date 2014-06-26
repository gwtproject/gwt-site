<style type="text/css">
  #gc-pagecontent h1.smaller  {
    font-size: 145%;
    border-top: thin black solid;
    padding-top: 3px;
    margin-top: 1.8em;
  }
</style>

<p>
A cell table (data presentation table) provides high-performance rendering of large data sets
in a tabular view. You can check out the
<a href="http://samples.gwtproject.org/samples/Showcase/Showcase.html#!CwCellTable">Cell Table example</a>
in the GWT Showcase to see it in action.
</p>

<p>
This developer guide will walk you through some advanced features specific to CellTable, such as
column sorting. If you are not familiar with the cell widgets, you should read the
<a href="DevGuideUiCellWidgets.html">Cell Widgets Developer Guide</a>
before continuing.
</p>

<ol class="toc" style="margin-top: 0;">
  <li><a href="#columnSorting">Column Sorting</a></li>
  <li><a href="#columnWidths">Controlling Column Widths</a></li>
</ol>

<h2 id="columnSorting">Column Sorting</h2>

<p>
CellTable has built-in support for column sorting. Use
<a href="/javadoc/latest/com/google/gwt/user/cellview/client/Column.html#setSortable(boolean)">Column.setSortable(boolean)</a>
to make a column sortable. Users will then be able to click on the column header and trigger a
<a href="/javadoc/latest/com/google/gwt/user/cellview/client/ColumnSortEvent.html">ColumnSortEvent</a>.
How you handle the event depends on how you push data into your CellTable.

<h3>ColumnSorting with ListDataProvider</h3>

<p>
GWT provides a default implementation of 
<a href="/javadoc/latest/com/google/gwt/user/cellview/client/ColumnSortEvent.Handler.html">ColumnSortEvent.Handler</a>
called <a href="/javadoc/latest/com/google/gwt/user/cellview/client/ColumnSortEvent.ListHandler.html">ColumnSortEvent.ListHandler</a>
that is designed to perform local sorting of a java.util.List.
</p>

<p>
<b>Code Example</b> - The example below adds column sorting support to a CellTable backed by a ListDataProvider.
</p>

<pre class="prettyprint">
/**
 * Entry point classes define &lt;code&gt;onModuleLoad()&lt;/code&gt;.
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
  private static List&lt;Contact&gt; CONTACTS = Arrays.asList(new Contact("John",
      "123 Fourth Road"), new Contact("Mary", "222 Lancer Lane"), new Contact(
      "Zander", "94 Road Street"));

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

    // Make the name column sortable.
    nameColumn.setSortable(true);

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

    // Create a data provider.
    ListDataProvider&lt;Contact&gt; dataProvider = new ListDataProvider&lt;Contact&gt;();

    // Connect the table to the data provider.
    dataProvider.addDataDisplay(table);

    // Add the data to the data provider, which automatically pushes it to the
    // widget.
    List&lt;Contact&gt; list = dataProvider.getList();
    for (Contact contact : CONTACTS) {
      list.add(contact);
    }

    // Add a ColumnSortEvent.ListHandler to connect sorting to the
    // java.util.List.
    ListHandler&lt;Contact&gt; columnSortHandler = new ListHandler&lt;Tester.Contact&gt;(
        list);
    columnSortHandler.setComparator(nameColumn,
        new Comparator&lt;Tester.Contact&gt;() {
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
</pre>

<h3>ColumnSorting with AsyncDataProvider</h3>

<p>
GWT provides a default implementation of 
<a href="/javadoc/latest/com/google/gwt/user/cellview/client/ColumnSortEvent.Handler.html">ColumnSortEvent.Handler</a>
called <a href="/javadoc/latest/com/google/gwt/user/cellview/client/ColumnSortEvent.AsyncHandler.html">ColumnSortEvent.AsyncHandler</a>
that helps with asynchronous (server-side) column sorting. When the user sorts a Column, AsyncHandler calls
<a href="/javadoc/latest/com/google/gwt/view/client/HasData.html#setVisibleRangeAndClearData(Range, boolean)">HasData.setVisibleRangeAndClearData(Range, boolean)</a>,
which triggers a RangeChangeEvent to the AsyncDataProvider.
</p>

<p>
<b>Code Example</b> - The example below adds column sorting support to a CellTable backed by an AsyncDataProvider.
</p>

<pre class="prettyprint">
/**
 * Entry point classes define &lt;code&gt;onModuleLoad()&lt;/code&gt;.
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
  private static List&lt;Contact&gt; CONTACTS = Arrays.asList(new Contact("John",
      "123 Fourth Road"), new Contact("Mary", "222 Lancer Lane"), new Contact(
      "Zander", "94 Road Street"));

  public void onModuleLoad() {

    // Create a CellTable.
    final CellTable&lt;Contact&gt; table = new CellTable&lt;Contact&gt;();

    // Create name column.
    TextColumn&lt;Contact&gt; nameColumn = new TextColumn&lt;Contact&gt;() {
      @Override
      public String getValue(Contact contact) {
        return contact.name;
      }
    };

    // Make the name column sortable.
    nameColumn.setSortable(true);

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

    // Set the total row count. You might send an RPC request to determine the
    // total row count.
    table.setRowCount(CONTACTS.size(), true);

    // Set the range to display. In this case, our visible range is smaller than
    // the data set.
    table.setVisibleRange(0, 3);

    // Create a data provider.
    AsyncDataProvider&lt;Contact&gt; dataProvider = new AsyncDataProvider&lt;Contact&gt;() {
      @Override
      protected void onRangeChanged(HasData&lt;Contact&gt; display) {
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
            Collections.sort(CONTACTS, new Comparator&lt;Tester.Contact&gt;() {
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
            List&lt;Contact&gt; dataInRange = CONTACTS.subList(start, end);

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
</pre>

<h2 id="columnWidths">Controlling Column Widths</h2>

<p>
By default, columns in a CellTable expand to fit the contents of the Cells. This works fine for a static table, but
if the content changes due to paging or user interaction, the columns might change width and appear jumpy. CellTable
provides an API that gives you fine grain control of how the available width is distributed between columns.
</p>

<p>
In order to gain fine-grain control over the width of columns, you must set the table layout to "fixed" by passing <code>true</code> into
<a href="/javadoc/latest/com/google/gwt/user/cellview/client/CellTable.html#setWidth(java.lang.String, boolean)">CellTable.setWidth(String, boolean)</a>.
Once in fixed-width mode, tables behave differently than they normally would. The following sections describe
recipes for achieving various effects.
</p>

<p>
<b>Code Example</b> - The example below creates a CellTable with fixed-width columns that expand to fill the available space.
</p>

<pre class="prettyprint">
/**
 * Entry point classes define &lt;code&gt;onModuleLoad()&lt;/code&gt;.
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
  private static List&lt;Contact&gt; CONTACTS = Arrays.asList(new Contact("John",
      "123 Fourth Road"), new Contact("Mary", "222 Lancer Lane"));

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
</pre>

<h3>Specify Exact Width of All Columns</h3>

<p>
If you want to assign a specific width to every column, then you must set the
table width to "auto" and assign an absolute width to every column.
</p>

<p class="warning"><b>WARNING:</b> If you set the width of the table to "auto" and do not
set the width of a column, the column will not be visible. Columns default to
a width of 0.</p>

<pre class="prettyprint">
    table.setWidth("auto", true);
    table.setColumnWidth(col0, 100.0, Unit.PX);
    table.setColumnWidth(col1, 150.0, Unit.PX);
    table.setColumnWidth(col2, 250.0, Unit.PX);
    table.setColumnWidth(col3, 100.0, Unit.PX);
</pre>

<h3>Specify Relative Width of All Columns</h3>

<p>
You can assign relative widths to every column by setting the width of the table
to a non-zero value (such as "100%" or "600px") and setting the width of each
Column in percentages.
</p>

<p class="warning"><b>WARNING:</b> Whenever you set the width of one or more columns using
percentages, the percentages should add up to 100%. Failure to do so may result
in unintended layout issues.</p>

<pre class="prettyprint">
    table.setWidth("100%", true);
    table.setColumnWidth(col0, 10.0, Unit.PCT);
    table.setColumnWidth(col1, 25.0, Unit.PCT);
    table.setColumnWidth(col2, 25.0, Unit.PCT);
    table.setColumnWidth(col3, 40.0, Unit.PCT);
</pre>

<h3>Mix Fixed and Relative Column Widths</h3>

<p>
One of the great features of fixed-width tables is that you can specify that some
columns should have a fixed width, while others should resize based on the width
of the table.  For example, you might fix the width of a checkbox column because
a checkbox is always about 25px wide, but let other columns resize.
</p>

<p>
In order to achieve this affect, set the width of the table to a non-zero value,
set the width of the fixed columns using non-relative units (such as px, em, or ex), and specify
the width of the remaining columns in percentages. If you only want one column
to be resizable, then set its width to 100%, and set the width of all other
columns in pixels.
</p>

<p class="warning"><b>WARNING:</b> The width of the columns specified in percentages should
add up to 100%. If they do not, then columns specified in pixels will also resize
with the table in an unpredictable way.</p>

<pre class="prettyprint">
    table.setWidth("100%", true);
    table.setColumnWidth(checkboxCol, 10.0, Unit.PX);
    table.setColumnWidth(nameCol, 35.0, Unit.PCT);
    table.setColumnWidth(descriptionCol, 65.0, Unit.PCT);
</pre>