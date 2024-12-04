Adding the Application Logic.
===

Up till now, we've designed our UI using `UiBinders` and `Elements`.

In this section, we'll learn how to add logic to our UI. We'll touch on very basic data modelling and how to handle native element events.

1.  Create the *Add Item dialog* by adding the following markup to the `Main.ui.xml` file:

    ```xml
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
    ```

     _**Tip**: You can use attributes to quickly define certain polymer actions such as `entry-animation='fade-in-animation'`._
     
     _**Note**: Don't forget to import all the new elements in the `Main.java` class._

2.  Add all the fields defined in the `Main.ui.xml` file to the `Main.java` class.

    ```java
    @UiField PaperDrawerPanelElement drawerPanel;
    @UiField HTMLElement content;

    @UiField PaperFabElement addButton;
    @UiField PaperDialogElement addItemDialog;
    @UiField PaperInputElement titleInput;
    @UiField PaperTextareaElement descriptionInput;
    @UiField PaperButtonElement confirmAddButton;
    ``` 

3.  Add a click handler to the floating action button in the `Main` constructor.

    ```java
    public Main() {
      initWidget(ourUiBinder.createAndBindUi(this));

      addButton.addEventListener("click", new EventListener() {
        @Override
        public void handleEvent(Event event) {
            addItemDialog.open();
        }
      });
    }
    ```

    _**Note**: Because elements lack methods for handling GWT events, we cannot use `@UiHandler` annotations. Thus we have to configure events in the constructor._

4.  Reload the application

    Now you can open the dialog by clicking on the floating action button in the bottom right corner.

    <img class='polymer-tutorial-screenshot' src='images/todo-list-07.png'>

5.  Create a `UiBinder` widget for displaying items: `Item.ui.xml` and `Item.java`

     * `Item.ui.xml`

        ```xml
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
        ```
    
     * `Item.java`: For simplicity, we will use this class as the item POJO.

        ```java
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
        ```

6.  Add the logic for creating items when we click the save button.

    ```java
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
    ```

7.  Reload the application

    Now you can add Todo items and mark them as done using checkboxes.

    <img class='polymer-tutorial-screenshot' src='images/todo-list-08.png'>

8.  Add the **Clear All** and **Clear Done** menu item handlers in the constructor.

    ```java
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
    ```
    
    _**Note**: The closeMenu() method is only useful if the application is viewed on a mobile device, or if your browser window is narrow enough for the side menu to collapse. Hence, we use this method to hide the menu after clicking on any menu item._

9. The final `Main.java` should look like this

    ```java
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
    ```

10. Your `TodoList.java` should look like this:

    ```java
    package org.gwtproject.tutorial.client;

    import com.google.gwt.core.client.EntryPoint;
    import com.google.gwt.user.client.ui.RootPanel;
    import com.vaadin.polymer.Polymer;
    import com.vaadin.polymer.elemental.Function;
    import com.vaadin.polymer.iron.element.IronIconElement;
    import com.vaadin.polymer.paper.element.*;

    import java.util.Arrays;

    public class TodoList implements EntryPoint {

        public void onModuleLoad() {
            Polymer.importHref(Arrays.asList(
                    "iron-icons/iron-icons.html",
                    PaperIconItemElement.SRC,
                    PaperRippleElement.SRC,
                    IronIconElement.SRC,
                    PaperDrawerPanelElement.SRC,
                    PaperHeaderPanelElement.SRC,
                    PaperToolbarElement.SRC,
                    PaperFabElement.SRC,
                    PaperDialogElement.SRC,
                    PaperTextareaElement.SRC,
                    PaperInputElement.SRC,
                    PaperButtonElement.SRC,
                    PaperCheckboxElement.SRC
            ), new Function() {
                public Object call(Object arg) {
                    startApplication();
                    return null;
                }
            });

        }

        private void startApplication() {
            RootPanel.get().add(new Main());
        }
    }
    ```
   
11. Reload the application

    * Add several items
    * Mark some of them as done
    * Click on "Clear Done" menu item
    * Click on "Clear All" menu item

## Summary

In this chapter we have learned how to:

1. Add more element-based `UiBinder` widgets to our Application.
2. Add events handlers to Elements.
3. Use good looking Polymer-based components like Paper dialogs and buttons.
4. Handle very basic Data Model in GWT.

[**DISCLAIMER**](introduction.html#pol-disclaimer)
