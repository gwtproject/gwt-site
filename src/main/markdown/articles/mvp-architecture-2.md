Building MVP apps: MVP Part II
===

_Chris Ramsdale, Google Developer Relations_

_Updated March 2010_

The sample project, referenced throughout this tutorial, can be downloaded at
[Tutorial-Contacts2.zip](https://storage.googleapis.com/google-code-archive-downloads/v2/code.google.com/google-web-toolkit/Tutorial-Contacts2.zip).

With the foundation of our MVP-based app laid out, you may be asking 
yourself, "Hey, why can't I use that fancy [UiBinder](#uibinder)
feature that was previously discussed?" This answer is, you can with a little
bit of tweaking within the Views and Presenters. On top of that, you may find
that the code flows more smoothly and the techniques we put in place will
dovetail nicely when we address the topic of implementing more
[complex UIs](#complex_uis),
[optimized UIs](#optimized_uis), and
[code splitting](#code_splitting).

## Doing things the UiBinder way <a id="uibinder"></a>

To start things out, let's take a look at the code that constructs our main
ContactList view. Previously we programmatically setup the UI within the
ContactsView constructor:

```java
public class ContactsView extends Composite implements ContactsPresenter.Display {
  ...
  public ContactsView() {
    DecoratorPanel contentTableDecorator = new DecoratorPanel();
    initWidget(contentTableDecorator);
    contentTableDecorator.setWidth("100%");
    contentTableDecorator.setWidth("18em");
    contentTable = new FlexTable();
    contentTable.setWidth("100%");
    contentTable.getCellFormatter().addStyleName(0, 0, "contacts-ListContainer");
    contentTable.getCellFormatter().setWidth(0, 0, "100%");
    contentTable.getFlexCellFormatter().setVerticalAlignment(0, 0, DockPanel.ALIGN_TOP);

    // Create the menu
    //
    HorizontalPanel hPanel = new HorizontalPanel();
    hPanel.setBorderWidth(0);
    hPanel.setSpacing(0);
    hPanel.setHorizontalAlignment(HorizontalPanel.ALIGN_LEFT);
    addButton = new Button("Add");
    hPanel.add(addButton);
    deleteButton = new Button("Delete");
    hPanel.add(deleteButton);
    contentTable.getCellFormatter().addStyleName(0, 0, "contacts-ListMenu");
    contentTable.setWidget(0, 0, hPanel);

    // Create the contacts list
    //
    contactsTable = new FlexTable();
    contactsTable.setCellSpacing(0);
    contactsTable.setCellPadding(0);
    contactsTable.setWidth("100%");
    contactsTable.addStyleName("contacts-ListContents");
    contactsTable.getColumnFormatter().setWidth(0, "15px");
    contentTable.setWidget(1, 0, contactsTable);
    contentTableDecorator.add(contentTable);
  }
  ...
}
```

The first step towards a UiBinder-way of doing things is to move this code
into a Contacts.ui.xml file and perform the associated transformations. As
mentioned in previous chapters, constructing UiBinder-based UIs allows you to
do so in a declarative way that resembles HTML more than straight Java code.
To that extent, the result is the file `ContactsView.ui.xml` with the following content:

```xml
<ui:UiBinder
  xmlns:ui="urn:ui:com.google.gwt.uibinder"
  xmlns:g="urn:import:com.google.gwt.user.client.ui">

  <ui:style>
    .contactsViewButtonHPanel {
      margin: 5px 0px 0x 5px;
    }
    .contactsViewContactsFlexTable {
      margin: 5px 0px 5px 0px;
    }

  </ui:style>

  <g:DecoratorPanel>
    <g:VerticalPanel>
      <g:HorizontalPanel addStyleNames="{style.contactsViewButtonHPanel}">
        <g:Button ui:field="addButton">Add</g:Button>
        <g:Button ui:field="deleteButton">Delete</g:Button>
      </g:HorizontalPanel>
      <g:FlexTable ui:field="contactsTable" addStyleNames="{style.contactsViewContactsFlexTable}"/>
    </g:VerticalPanel>
  </g:DecoratorPanel>
</ui:UiBinder>
```

Here we've laid out a VerticalPanel that wraps our Add/Delete buttons and
FlexTable that contains the list of contacts. This is then wrapped by a
DecoratorPanel for a bit of style. The `<ui:style>` element declares a small
amount of margin so that things aren't placed too close together.
The ContactsView constructor and members are then reduced to the
following:

```java
public class ContactsViewImpl<T> extends Composite implements ContactsView<T> {
  ...
  @UiTemplate("ContactsView.ui.xml")
  interface ContactsViewUiBinder extends UiBinder<Widget, ContactsViewImpl> {}
  private static ContactsViewUiBinder uiBinder =
    GWT.create(ContactsViewUiBinder.class);

  @UiField FlexTable contactsTable;
  @UiField Button addButton;
  @UiField Button deleteButton;

  public ContactsViewImpl() {
    initWidget(uiBinder.createAndBindUi(this));
  }
  ...
}
```

We'll get to why it's templatized, why it's an "impl" class, and why we use
the UiTemplate annotation later on when we discuss "Complex UIs - Dumb Views".
For now it's important to note that a) we'll access the  underlying widgets
via the UiField annotations, and b) the constructor code is significantly
smaller. And by "smaller", I mean a single line. UiBinder does a good job of
removing the boilerplate code necessary to setup UIs, and allows you to further
segment the code that declares the UI from the code that drives the UI.

Now that we have the UI constructed, we need to hook up the associated UI
events, namely Add/Delete button clicks, and interaction with the contact list
FlexTable. This is where we'll start to notice significant changes in the
overall layout of our application design. Mainly due to the fact that we want
to link methods within the view to UI interactions via the UiHandler annotation.
The first major change is that we want our ContactsPresenter to implement a
Presenter interface that allows our ContactsView to callback into the presenter
when it receives a click, select or other event. The Presenter interface defines
the following:

```java
public interface Presenter<T> {
    void onAddButtonClicked();
    void onDeleteButtonClicked();
    void onItemClicked(T clickedItem);
    void onItemSelected(T selectedItem);
  }
```

Again, templatizing the interface will be covered in the next section, but
with this interface in place you can start to see how the ContactsView is going
to communicate with the ContactsPresenter. The first part of wiring everything
up is to have our ContactsPresenter implement the Presenter interface, and then
register itself with the underlying view. To register itself, we'll need our
ContactsView to expose a setPresenter() method:

```java
private Presenter<T> presenter;
  public void setPresenter(Presenter<T> presenter) {
    this.presenter = presenter;
  }
```

Now we can take a look at how we'll wire up the UI interactions within the
ContactsView via the UiHandler annotation:

```java
public class ContactsViewImpl<T> extends Composite implements ContactsView<T> {
  ...
  @UiHandler("addButton")
  void onAddButtonClicked(ClickEvent event) {
    if (presenter != null) {
      presenter.onAddButtonClicked();
    }
  }

  @UiHandler("deleteButton")
  void onDeleteButtonClicked(ClickEvent event) {
    if (presenter != null) {
      presenter.onDeleteButtonClicked();
    }
  }

  @UiHandler("contactsTable")
  void onTableClicked(ClickEvent event) {
    if (presenter != null) {
      HTMLTable.Cell cell = contactsTable.getCellForEvent(event);

      if (cell != null) {
        if (shouldFireClickEvent(cell)) {
          presenter.onItemClicked(rowData.get(cell.getRowIndex()));
        }

        if (shouldFireSelectEvent(cell)) {
          presenter.onItemSelected(rowData.get(cell.getRowIndex()));

        }
      }
    }
  }
  ...
}
```

Using this technique, we've provided the UiBinder generator the corresponding
methods that should be called when a Widget has a "ui:field" attribute set to
"addButton", "deleteButton", and "contactsTable". On the ContactsPresenter side
of the fence we end up with the following:

```java
public class ContactsPresenter implements Presenter {
  ...
  public void onAddButtonClicked() {
    eventBus.fireEvent(new AddContactEvent());
  }

  public void onDeleteButtonClicked() {
    deleteSelectedContacts();
  }

  public void onItemClicked(ContactDetails contactDetails) {
    eventBus.fireEvent(new EditContactEvent(contactDetails.getId()));
  }

  public void onItemSelected(ContactDetails contactDetails) {
    if (selectionModel.isSelected(contactDetails)) {
      selectionModel.removeSelection(contactDetails);
    }
    else {
      selectionModel.addSelection(contactDetails);
    }
  }
  ...
}
```

The resulting method implementations are the same as the non-UiBinder sample,
with the exception of the onItemClicked() and onItemSelected() methods. And
while these methods may seem straightforward, we need to dive into how they
came about, and explain what this "SelectionModel" is in the first place. This
and much, much more is explained in the next section.

## Complex UIs - Dumb Views <a id="complex_uis"></a>

Our current solution is to have our presenters pass a dumbed down version of
the model to our views. In the case of our ContactsView, the presenter takes a
list of DTOs (Data Transfer Objects) and constructs a list of Strings that it
then passes to the view.

```java
public class ContactsPresenter implements Presenter {
  ...
  public void onSuccess(ArrayList<ContactDetails> result) {
    contactDetails = result;
    sortContactDetails();
    List<String> data = new ArrayList<String>();
    for (int i = 0; i < result.size(); ++i) {
      data.add(contactDetails.get(i).getDisplayName());
    }

    display.setData(data);
  }
  ...
}
```

The "data" object that is passed to the view is a very (and I mean very)
simplistic ViewModel &mdash; basically a representation of a more complex data model
using primitives. This is fine for a simplistic view, but as soon as you start to
do something more complex, you quickly realize that something has to give.
Either the presenter needs to know more about the view (making it hard to swap
out views for other platforms), or the view needs to know more about the data
model (ultimately making your view smarter, thus requiring more GwtTestCases).
The solution is to use generics along with a third party that abstracts any
knowledge of a cell's data type, as well as how that data type is rendered.

First, we're going to rely on the fact that data types are typically
homogeneous within column borders. Doing so allows us to define a
ColumnDefinition abstract class that houses the any type-specific code (this is
the third party mentioned above).

```java
public abstract class ColumnDefinition<T> {
    public abstract Widget render(T t);

    public boolean isClickable() {
      return false;
    }

    public boolean isSelectable() {
      return false;
    }
  }
```

By stringing together a list of these classes, and providing the necessary
render() implementations and isClickable()/isSelectable() overrides, you can
start see how we would define our layout. Let's take a look at how we would
make this work with our Contacts sample.

```java
public class ContactsViewColumnDefinitions<ContactDetails> {
    List<ColumnDefinition<ContactDetails>> columnDefinitions =
      new ArrayList<ColumnDefinition<ContactDetails>>();

    private ContactsViewColumnDefinitions() {
      columnDefinitions.add(new ColumnDefinition<ContactDetails>() {
        public Widget render(ContactDetails c) {
          return new CheckBox();
        }

        public boolean isSelectable() {
          return true;
        }
      });

      columnDefinitions.add(new ColumnDefinition<ContactDetails>() {
        public Widget render(ContactDetails c) {
          return new HTML(c.getDisplayName());
        }

        public boolean isClickable() {
          return true;
        }
      });
    }

    public List<ColumnDefinition<ContactDetails>> getColumnDefnitions() {
      return columnDefinitions;
    }
  }
```

These ColumnDefinition(s) would be created outside of the presenter so that
we can reuse its logic regardless of what view we've attached ourself to (be it
an iPhone, Android, desktop, etc... view). This can be done using a
platform-specific ContactsViewColumnDefinitions class that is loaded (or
injected using GIN) on a per-permutation basis. Regardless of the technique,
we'll need to update our views such that we can set their ColumnDefinition(s).

```java
public class ContactsViewImpl<T> extends Composite implements ContactsView<T> {
    ...
    private List<ColumnDefinition<T>> columnDefinitions;
    public void setColumnDefinitions(
        List<ColumnDefinition<T>> columnDefinitions) {
      this.columnDefinitions = columnDefinitions;
    }
    ...
  }
```

Note that our ContactsView is now ContactsViewImpl<T> and implements
ContactsView<T>. This is so that we can pass in a mocked ContactsView instance
when testing our ContactsPresenter. Now in our AppController, when we create the
ContactsView, we can initialize it with the necessary ColumnDefinition(s).

```java
public class AppController implements Presenter, ValueChangeHandler<String> {
    ...
    public void onValueChange(ValueChangeEvent<String> event) {
      String token = event.getValue();
      if (token != null) {
        Presenter presenter = null;
        if (token.equals("list")) {
          // lazily initialize our views, and keep them around to be reused
          //
          if (contactsView == null) {
            contactsView = new ContactsViewImpl<ContactDetails>();
            if (contactsViewColumnDefinitions == null) {
              contcactsViewColumnDefinitions = new ContactsViewColumnDefinitions().getColumnDefinitions();
            }
            contactsView.setColumnDefiniions(contactsViewColumnDefinitions);
         }
        }
        presenter = new ContactsPresenter(rpcService, eventBus, contactsView);
      }
      ...
    }
    ...
  }
```

With the ColumnDefinition(s) in place, we will start to see the fruits of our
labor. Mainly in the way we pass model data to the view. As mentioned above we
were previously dumbing down the model into a list of Strings. With our
ColumnDefinition(s) we can pass the model untouched.

```java
public class ContactsPresenter implements Presenter,
    ...
    private void fetchContactDetails() {
      rpcService.getContactDetails(new AsyncCallback<ArrayList<ContactDetails>>() {
        public void onSuccess(ArrayList<ContactDetails> result) {
            contactDetails = result;
            sortContactDetails();
            view.setRowData(contactDetails);
        }
        ...
      });
    }
    ...
  }
```

And our ContactsViewImpl has the following setRowData() implementation:

```java
public class ContactsViewImpl<T> extends Composite implements ContactsView<T> {
    ...
    public void setRowData(List<T> rowData) {
      contactsTable.removeAllRows();
      this.rowData = rowData;

      for (int i = 0; i < rowData.size(); ++i) {
        T t = rowData.get(i);
        for (int j = 0; j < columnDefinitions.size(); ++j) {
          ColumnDefinition<T> columnDefinition = columnDefinitions.get(j);
          contactsTable.setWidget(i, j, columnDefinition.render(t));
        }
      }
    }
    ...
  }
```

A definite improvement; the presenter can pass the model untouched and the
view has no rendering code that we would otherwise need to test. And the fun
doesn't stop there. Remember the isClickable() and isSelectable() methods? Well,
let's take a look at how they work in conjunction with ClickEvents that are
received within the view.

```java
public class ContactsViewImpl<T> extends Composite implements ContactsView<T> {
    ...
    @UiHandler("contactsTable")
    void onTableClicked(ClickEvent event) {
      if (presenter != null) {
        HTMLTable.Cell cell = contactsTable.getCellForEvent(event);

        if (cell != null) {
          if (shouldFireClickEvent(cell)) {
            presenter.onItemClicked(rowData.get(cell.getRowIndex()));
          }

          if (shouldFireSelectEvent(cell)) {
            presenter.onItemSelected(rowData.get(cell.getRowIndex()));
          }
        }
      }
    }

    private boolean shouldFireClickEvent(HTMLTable.Cell cell) {
      boolean shouldFireClickEvent = false;

      if (cell != null) {
        ColumnDefinition<T> columnDefinition =
          columnDefinitions.get(cell.getCellIndex());

        if (columnDefinition != null) {
          shouldFireClickEvent = columnDefinition.isClickable();
        }
      }

      return shouldFireClickEvent;
    }

    private boolean shouldFireSelectEvent(HTMLTable.Cell cell) {
      boolean shouldFireSelectEvent = false;

      if (cell != null) {
        ColumnDefinition<T> columnDefinition =
          columnDefinitions.get(cell.getCellIndex());

        if (columnDefinition != null) {
          shouldFireSelectEvent = columnDefinition.isSelectable();
        }
      }

      return shouldFireSelectEvent;
    }
    ...
  }
```

The notion here is that you'll want to respond to user interaction in
different ways based upon the cell type that was clicked. Given that our
ColumnDefinition(s) are intertwined with the cell type, we're not only able to
use them for rendering purposes, but for defining how to interpret user
interactions.

To take this one final step further, we're going to remove any application
state from the ContactsView. To do this, we'll replace the view's
getSelectedRows() with a SelectionModel that the presenter holds on to. The
SelectionModel is nothing more than a wrapper around a list of model
objects.

```java
public class SelectionModel<T> {
    List<T> selectedItems = new ArrayList<T>();

    public List<T> getSelectedItems() {
      return selectedItems;
    }

    public void addSelection(T item) {
      selectedItems.add(item);
    }

    public void removeSelection(T item) {
      selectedItems.remove(item);
    }

    public boolean isSelected(T item) {
      return selectedItems.contains(item);
    }
  }
```

The ContactsPresenter holds on to an instance of this class and updates it
accordingly, based on calls to onItemSelected().

```java
public class ContactsPresenter implements Presenter,
    ...
    public void onItemSelected(ContactDetails contactDetails) {
      if (selectionModel.isSelected(contactDetails)) {
        selectionModel.removeSelection(contactDetails);
      }

      else {
        selectionModel.addSelection(contactDetails);
      }
    }
    ...
  }
```

When it needs to grab the list of selected items, for example when the user
clicks the "Delete" button, it has them right at its disposal.

```java
public class ContactsPresenter implements Presenter,
    ...

    public void onDeleteButtonClicked() {
      deleteSelectedContacts();
    }

    private void deleteSelectedContacts() {
      List<ContactDetails> selectedContacts = selectionModel.getSelectedItems();
      ArrayList<String> ids = new ArrayList<String>();

      for (int i = 0; i < selectedContacts.size(); ++i) {
        ids.add(selectedContacts.get(i).getId());
      }

      rpcService.deleteContacts(ids, new AsyncCallback<ArrayList<ContactDetails>>() {
        public void onSuccess(ArrayList<ContactDetails> result) {
           ...
        }
        ...
      });
    }
    ...
  }
```

Alright, so that was a fair amount to digest, and describing it in code
snippets might lead to some being "lost in translation". If that's the case,
be sure to check out the full source that is available
[here](http://code.google.com/p/google-web-toolkit/downloads/detail?name=Tutorial-Contacts2.zip).

## Optimized UIs - Dumb Views <a id="optimized_uis"></a>

We've figured out how to create the foundation for complex UIs while sticking
to our requirement that the view remain as dumb (and minimally testable) as
possible, but that's no reason to stop. While functionality is decoupled, there
is still room for optimization. Having the ColumnDefinition create a new widget
for each cell is too heavy, and can quickly lead to performance degradation as
your application grows. The two leading factors of this degradation are:

*   Inefficiencies related to inserting new elements via DOM manipulation

*   Overhead associated with sinking events per Widget

To overcome this we will update our application to do the following (in
respective order):

*   Replace our FlexTable implementation with an HTML widget that we'll
populate by calling setHTML(), effectively batching all DOM manipulation into
a single call.
*   Reduce the event overhead by sinking events on the HTML widget, rather than
the individual cells.

The changes are encompassed within our ContactsView.ui.xml file, as well as
our setRowData() and onTableClicked() methods. First we'll need to update our
ContactsView.ui.xml file to use a HTML widget rather than a FlexTable widget.

```xml
<ui:UiBinder>
  ...
  <g:DecoratorPanel>
    <g:VerticalPanel>
      <g:HorizontalPanel addStyleNames="{style.contactsViewButtonHPanel}">
        <g:Button ui:field="addButton">Add</g:Button>
        <g:Button ui:field="deleteButton">Delete</g:Button>
      </g:HorizontalPanel>
      <g:HTML ui:field="contactsTable"></g:HTML>
    </g:VerticalPanel>
  </g:DecoratorPanel>
</ui:UiBinder>
```

We'll also need to change the widget that we reference within our
ContactsViewImpl class.

```java
public class ContactsViewImpl<T> extends Composite implements ContactsView<T> {
  ...
  @UiField HTML contactsTable;
  ...
```

Next we'll make the necessary changes to our setRowData() method.

```java
public class ContactsViewImpl<T> extends Composite implements ContactsView<T> {
  ...
  public void setRowData(List<T> rowData) {
    this.rowData = rowData;

    TableElement table = Document.get().createTableElement();
    TableSectionElement tbody = Document.get().createTBodyElement();
    table.appendChild(tbody);

    for (int i = 0; i < rowData.size(); ++i) {
      TableRowElement row = tbody.insertRow(-1);
      T t = rowData.get(i);

      for (int j = 0; j < columnDefinitions.size(); ++j) {
        TableCellElement cell = row.insertCell(-1);
        StringBuilder sb = new StringBuilder();
        columnDefinitions.get(j).render(t, sb);
        cell.setInnerHTML(sb.toString());

        // TODO: Really total hack! There's gotta be a better way...
        Element child = cell.getFirstChildElement();
        if (child != null) {
          Event.sinkEvents(child, Event.ONFOCUS | Event.ONBLUR);
        }
      }
    }

    contactsTable.setHTML(table.getInnerHTML());
  }
  ...
}
```

The above code is similar to our original setRowData() method, we iterate
through the rowData and for each item ask our column definitions to render
accordingly. The main differences being that a) we're expecting each column
definition to render itself into the StringBuilder rather than passing back a
full-on widget, and b) we're calling setHTML on a HTML widget rather than
calling setWidget on a FlexTable. This will decrease your load time, especially
as your tables start to grow.

Now let's take a look at the code used to sink events on the table.

```java
public class ContactsViewImpl<T> extends Composite implements ContactsView<T> {
  ...
  @UiHandler("contactsTable")
  void onTableClicked(ClickEvent event) {
    if (presenter != null) {
      EventTarget target = event.getNativeEvent().getEventTarget();
      Node node = Node.as(target);
      TableCellElement cell = findNearestParentCell(node);
      if (cell == null) {
        return;
      }

      TableRowElement tr = TableRowElement.as(cell.getParentElement());
      int row = tr.getSectionRowIndex();

      if (cell != null) {
        if (shouldFireClickEvent(cell)) {
          presenter.onItemClicked(rowData.get(row));
        }
        if (shouldFireSelectEvent(cell)) {
          presenter.onItemSelected(rowData.get(row));
        }
      }
  ...
}
```

Here our onTableClicked() code gets a bit more complicated, but nothing that
would raise a red flag when compared to the rest of our application. To
reiterate, we're reducing the overhead of sinking events on per-cell widgets,
and instead sinking on a single container, our HTML widget. The ClickEvents are
still wired up via our UiHandler annotations, but with this approach, we're
going to get the Element that was clicked on and walk the DOM until we find a
parent TableCellElement. From there we can determine the row, and thus the
corresponding rowData.

The other tweak we need to make is to update our shouldFireClickEvent() and
shouldFireSelectEvent() to take as a parameter a TableCellElement rather than
a HTMLTable.Cell. The implementation remains the same, as you can see below.

```java
public class ContactsViewImpl<T> extends Composite implements ContactsView<T> {
  ...
  private boolean shouldFireClickEvent(TableCellElement cell) {
    boolean shouldFireClickEvent = false;

    if (cell != null) {
      ColumnDefinition<T> columnDefinition =
        columnDefinitions.get(cell.getCellIndex());

      if (columnDefinition != null) {
        shouldFireClickEvent = columnDefinition.isClickable();
      }
    }

    return shouldFireClickEvent;
  }

  private boolean shouldFireSelectEvent(TableCellElement cell) {
    boolean shouldFireSelectEvent = false;

    if (cell != null) {
      ColumnDefinition<T> columnDefinition =
        columnDefinitions.get(cell.getCellIndex());

      if (columnDefinition != null) {
        shouldFireSelectEvent = columnDefinition.isSelectable();
      }
    }

    return shouldFireSelectEvent;
  }
  ...
 }
```

## Code Splitting &mdash; Only the relevant parts please <a id="code_splitting"></a>

Up to this point we've discussed how code maintainability and testing are
benefits of an MVP-based application. One other benefit that may go overlooked
is faster startup times via Code Splitting. I know, you're probably wondering
what in the world an MVP architecture has to do with code splitting, but the same
techniques that make your app more maintainable, and testable, make it easier to
split with runAsync() points. Let's back up for a quick recap first.
Code Splitting is the act of wrapping segmented pieces of your application into
"split" points by declaring them within a runAsync() call. As long as the split
portion of your code is purely segmented, and not referenced by other parts of
the app, it will be downloaded and executed at the point that it needs to
run.

So take for example the code we wrote in the previous section. It was rather
lengthy, right? Now assume that our application has a login screen, and as usual
once the user logs in they're taken to the main application screen (in this case
their list of Contacts). Do we really want to download all of that code before
the user even logs in? Not really. It would be nice if we could simply grab the
login code, and leave the rest for when we actually need it (e.g. after the user
has logged in). Well we can, and here's how.

```java
public void onValueChange(ValueChangeEvent<String> event) {
    String token = event.getValue();

    if (token != null) {
      if (token.equals("list")) {
        GWT.runAsync(new RunAsyncCallback() {
          ...
          public void onSuccess() {
            // lazily initialize our views, and keep them around to be reused
            //
            if (contactsView == null) {
              contactsView = new ContactsViewImpl<ContactDetails>();
            }
            new ContactsPresenter(rpcService, eventBus, contactsView).go(container);
          }
        });
      }
      ...
   }
```

Here, all we've done is wrap the code that creates the ContactsView and
ContactsPresenter in a runAsync() call, and as a result it won't be downloaded
until the first time we go to show the Contact list. After that, subsequent calls
will realize that the code has already been downloaded, and will use it in lieu
of re-downloading.

A bit anti climatic isn't it? Well, that's not always a bad thing. The amount
of boilerplate code necessary to get an MVP-based app up and running is
generally large. But the time spent is not wasted, as optimizations such as this
one become easier and easier to implement.
