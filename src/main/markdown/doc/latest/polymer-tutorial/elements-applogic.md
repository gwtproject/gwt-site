Adding the Application Logic.
===

At this point we have almost the UI of our **TodoList** application designed using `UiBinders` and Elements.

In this lesson we will learn how to add some logic to our UI and deal with native events in elements, and data.

1.  Create the *Add Item dialog* adding the following markup to the Main.ui.xml file:

        <g:HTMLPanel>
          ...
          <paper-dialog ui:field="addItemDialog"
                        entry-animation="fade-in-animation"
                        class="dialog" modal="">
            <h2>Add Item</h2>
            <paper-input ui:field="titleInput" label="Title" required=""
                         auto-validate="" error-message="required input!"/>
            <div class="textarea-container iron-autogrow-textarea">
                <paper-textarea ui:field="descriptionInput" label="Notes"/>
            </div>
            <div class="buttons">
                <paper-button dialog-dismiss="">Cancel</paper-button>
                <paper-button ui:field="confirmAddButton"
                              dialog-confirm="">OK</paper-button>
            </div>
          </paper-dialog>
        </g:HTMLPanel>

     _**Tip**: You can use attributes to quick define certain polymer actions like `entry-animation='fade-in-animation'`._

2.  Add all fields defined in the `Main.ui.xml` file to the `Main.java` class.

        @UiField PaperDrawerPanelElement drawerPanel;
        @UiField HTMLElement content;

        @UiField PaperDialogElement addItemDialog;
        @UiField PaperInputElement titleInput;
        @UiField PaperTextareaElement descriptionInput;
        @UiField PaperButtonElement confirmAddButton;

3.  Add the click handler to the floating action button in the `Main` constructor.

        public Main() {
          initWidget(ourUiBinder.createAndBindUi(this));

          addButton.addEventListener("click", new EventListener() {
            @Override
            public void handleEvent(Event event) {
                addItemDialog.open();
            }
          });
        }

      _**Note** that when using elements we cannot use `@UiHandler` annotations because elements don't have methods to handle GWT events. Thus we have to configure events in the constructor._

4.  Reload the application

    Now you can open dialog by clicking on the round action button at the bottom right corner.

    <img class='polymer-tutorial-screenshot' src='images/todo-list-07.png'>

6.  Create a `UiBinder` widget for displaying items: `Item.ui.xml` and `Item.java`

     * `Item.ui.xml`

            <ui:UiBinder xmlns:ui='urn:ui:com.google.gwt.uibinder'
                         xmlns:g='urn:import:com.google.gwt.user.client.ui'
                         xmlns:p='urn:import:com.vaadin.polymer.paper.widget'>

              <div class="item vertical center-justified layout">
                <style>
                  .title {
                    padding-left: 20px;
                    font-size: 150%;
                    font-weight: normal;
                  }
                  .done {
                    text-decoration: line-through;
                  }
                  .paper-checkbox {
                    top: -2px;
                  }
                </style>
                <div class="vertical-section">
                  <h4>
                    <paper-checkbox ui:field="done"/>
                    <span ui:field="title" class='title'>Go to Google</span>
                  </h4>
                  <div ui:field="description"/>
                </div>
              </div>
            </ui:UiBinder>

     * `Item.java`: For simplicity, we will use this class as the Item POJO

            package org.gwtproject.tutorial.client;

            import com.google.gwt.core.shared.GWT;
            import com.google.gwt.dom.client.DivElement;
            import com.google.gwt.dom.client.Element;
            import com.google.gwt.uibinder.client.UiBinder;
            import com.google.gwt.uibinder.client.UiField;
            import com.vaadin.polymer.elemental.Event;
            import com.vaadin.polymer.elemental.EventListener;
            import com.vaadin.polymer.paper.element.PaperCheckboxElement;

            public class Item {

              private final DivElement element;

              interface ItemUiBinder extends UiBinder<DivElement, Item> {
              }

              private static ItemUiBinder ourUiBinder = GWT.create(ItemUiBinder.class);

              @UiField Element title;
              @UiField Element description;
              @UiField PaperCheckboxElement done;

              public Item() {
                element = ourUiBinder.createAndBindUi(this);

                done.addEventListener("iron-change", new EventListener() {
                  @Override
                  public void handleEvent(Event event) {
                    if (done.getActive()) {
                      title.addClassName("done");
                    } else {
                      title.removeClassName("done");
                    }
                  }
                });
              }

              public String getTitle() {
                return title.getInnerText();
              }
              public void setTitle(String s) {
                title.setInnerText(s);
              }
              public String getDescription() {
                return description.getInnerText();
              }
              public void setDescription(String s) {
                description.setInnerText(s);
              }
              public boolean isDone() {
                return done.getActive();
              }
              public void setDone(boolean b) {
                done.setActive(b);
              }
              public DivElement getElement() {
                return element;
              }
            }


7.  Add the logic to create items when we click the save button.

          ...
          private List<Item> items = new ArrayList<>();

          public Main() {
            ...
            addButton.addEventListener("click", new EventListener() {
              public void handleEvent(Event event) {
                addItemDialog.open();
              }
            });

            confirmAddButton.addEventListener("click", new EventListener() {
              public void handleEvent(Event event) {
                if (!titleInput.getValue().isEmpty()) {
                  addItem(titleInput.getValue(), descriptionInput.getValue());
                  // clear text fields
                  titleInput.setValue("");
                  descriptionInput.setValue("");
                }
              }
            });
          }

          private void addItem(String title, String description) {
            Item item = new Item();
            item.setTitle(title);
            item.setDescription(description);
            content.appendChild(item.getElement());
            items.add(item);
          }
          ...

8.  Reload the application

    Now you can add Todo items and mark them as done using checkboxes.

    <img class='polymer-tutorial-screenshot' src='images/todo-list-08.png'>

9.  Add the **Clear All** and **Clear Done** menu item handlers in the constructor.

          public Main() {
            ...
            menuClearAll.addEventListener("click", new EventListener() {
              public void handleEvent(Event event) {
                closeMenu();
                // remove all child elements
                while (content.hasChildNodes()) {
                  content.removeChild(content.getFirstChild());
                }
              }
            });

            menuClearDone.addEventListener("click", new EventListener() {
              public void handleEvent(Event event) {
                closeMenu();

                for (Item item : items) {
                  if (item.isDone()) {
                    content.removeChild(item.getElement());
                    items.remove(item);
                  }
                }
              }
            });
          }

          private void closeMenu() {
            if (drawerPanel.getNarrow()) {
              drawerPanel.closeDrawer();
            }
          }

    _**Note**: that the closeMenu() method is only useful if you open the application on a mobile device or make your browser window narrow enough to collapse the side menu. We have to hide menu after click._

10. The final `Main.java` should look like this

        package org.gwtproject.tutorial.client;

        import com.google.gwt.core.client.GWT;
        import com.google.gwt.uibinder.client.UiBinder;
        import com.google.gwt.uibinder.client.UiField;
        import com.google.gwt.user.client.ui.Composite;
        import com.google.gwt.user.client.ui.HTMLPanel;
        import com.vaadin.polymer.elemental.*;
        import com.vaadin.polymer.paper.element.*;

        import java.util.ArrayList;
        import java.util.List;

        public class Main extends Composite {
          interface MainUiBinder extends UiBinder<HTMLPanel, Main> {
          }

          private static MainUiBinder ourUiBinder = GWT.create(MainUiBinder.class);

          @UiField PaperDrawerPanelElement drawerPanel;

          @UiField PaperIconItemElement menuClearAll;
          @UiField PaperIconItemElement menuClearDone;

          @UiField HTMLElement content;
          @UiField PaperFabElement addButton;

          @UiField PaperDialogElement addItemDialog;
          @UiField PaperInputElement titleInput;
          @UiField PaperTextareaElement descriptionInput;
          @UiField PaperButtonElement confirmAddButton;

          private List<Item> items = new ArrayList<>();

          public Main() {
            initWidget(ourUiBinder.createAndBindUi(this));

            addButton.addEventListener("click", new EventListener() {
              public void handleEvent(Event event) {
                addItemDialog.open();
              }
            });

            confirmAddButton.addEventListener("click", new EventListener() {
              public void handleEvent(Event event) {
                if (!titleInput.getValue().isEmpty()) {
                  addItem(titleInput.getValue(), descriptionInput.getValue());
                  // clear text fields
                  titleInput.setValue("");
                  descriptionInput.setValue("");
                }
              }
            });

            menuClearAll.addEventListener("click", new EventListener() {
              public void handleEvent(Event event) {
                closeMenu();
                // remove all child elements
                while (content.hasChildNodes()) {
                  content.removeChild(content.getFirstChild());
                }
              }
            });

            menuClearDone.addEventListener("click", new EventListener() {
              public void handleEvent(Event event) {
                closeMenu();

                for (Item item : items) {
                  if (item.isDone()) {
                    content.removeChild(item.getElement());
                    items.remove(item);
                  }
                }
              }
            });
          }

          private void addItem(String title, String description) {
            Item item = new Item();
            item.setTitle(title);
            item.setDescription(description);
            content.appendChild(item.getElement());
            items.add(item);
          }

          private void closeMenu() {
            if (drawerPanel.getNarrow()) {
              drawerPanel.closeDrawer();
            }
          }
        }

11. Reload the application

    * Add several items
    * Mark some of them as done
    * Click on "Clear Done" menu item
    * Click on "Clear All" menu item

## Summary

In this chapter we have learnt:

1. How to add more element based `UiBinder` widgets to our Application.
2. Add events handlers to Elements.
3. Usage of advanced Polymer components like dialog and buttons.
4. How to deal with a very basic Data Model in GWT.
