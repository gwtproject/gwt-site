Adding the Application Logic.
===

At this point we have almost the UI of our **TodoList** application designed using `UiBinders`.

In this lesson we will learn how to add the logic to our UI and deal with widget events and data.

1.  Create the **Add Item dialog** adding the following markup to the Main.ui.xml file:

        <g:HTMLPanel>
          ...
          <p:PaperDialog ui:field="addItemDialog"
                         entryAnimation="fade-in-animation"
                         addStyleNames="{style.dialog}" modal="true">
            <h2>Add Item</h2>
            <p:PaperInput ui:field="titleInput" label="Title" required="true"
                          autoValidate="true" errorMessage="required input!"/>
            <div class="textarea-container iron-autogrow-textarea">
              <p:PaperTextarea ui:field="descriptionInput" label="Notes"/>
            </div>
            <div class="buttons">
               <p:PaperButton attributes="dialog-dismiss">Cancel</p:PaperButton>
               <p:PaperButton ui:field="confirmAddButton"
                              attributes="dialog-confirm">OK</p:PaperButton>
            </div>
          </p:PaperDialog>
        </g:HTMLPanel>

     _**Tip**: You can use widget attributes to quick define certain actions like `entryAnimation='fade-in-animation'`._

     _**Note**: When there are attributes in the component no mapped to a java method, you can use the `attributes` key, like we do in the `Cancel` and `OK` buttons. Visit [paper-dialog page](https://elements.polymer-project.org/elements/paper-dialog) and pay attention to behaviors in the API Reference section for additional details_

2.  Add all fields we already defined in the `Main.ui.xml` file to the `Main.java` class.

        @UiField PaperDrawerPanel drawerPanel;
        @UiField HTMLPanel content;

        @UiField PaperDialog addItemDialog;
        @UiField PaperInput titleInput;
        @UiField PaperTextarea descriptionInput;

3.  Bind the click handler method to the floating action button in the `Main.java`

        @UiHandler("addButton")
        protected void onAddButtonClick(ClickEvent e) {
          addItemDialog.open();
        }

4.  Reload the application

    Now you can open dialog by clicking on the round action button at the bottom right corner.

    <img class='polymer-tutorial-screenshot' src='images/todo-list-07.png'>

6.  Create a widget for displaying items: `Item.ui.xml` and `Item.java`

     * `Item.ui.xml`

            <ui:UiBinder xmlns:ui='urn:ui:com.google.gwt.uibinder'
              xmlns:g='urn:import:com.google.gwt.user.client.ui'
              xmlns:p='urn:import:com.vaadin.polymer.paper.widget'
              xmlns:i='urn:import:com.vaadin.polymer.iron.widget'>
              <ui:style>
                @external .done;
                .item .done {
                  text-decoration: line-through;
                }
                .title {
                  padding-left: 20px;
                  font-size: 150%;
                  font-weight: normal;
                }
              </ui:style>
              <g:HTMLPanel
                addStyleNames="vertical center-justified layout {style.item}">
                <style>
                </style>
                <div class="vertical-section">
                  <h4>
                    <p:PaperCheckbox ui:field="check"></p:PaperCheckbox>
                    <span ui:field="title" class='{style.title}'>Go to Google</span>
                  </h4>
                  <div ui:field="description" class='{style.description}'></div>
                </div>
              </g:HTMLPanel>
            </ui:UiBinder>

     * `Item.java`: For simplicity, we will use this class as the Item POJO

            package org.gwtproject.tutorial.client;

            import com.google.gwt.core.shared.GWT;
            import com.google.gwt.dom.client.Element;
            import com.google.gwt.uibinder.client.UiBinder;
            import com.google.gwt.uibinder.client.UiField;
            import com.google.gwt.uibinder.client.UiHandler;
            import com.google.gwt.user.client.ui.Composite;
            import com.google.gwt.user.client.ui.HTMLPanel;
            import com.vaadin.polymer.iron.widget.event.IronChangeEvent;
            import com.vaadin.polymer.paper.widget.PaperCheckbox;

            public class Item extends Composite {

              interface ItemUiBinder extends UiBinder<HTMLPanel, Item> {
              }

              private static ItemUiBinder ourUiBinder = GWT.create(ItemUiBinder.class);

              @UiField Element title;
              @UiField Element description;
              @UiField PaperCheckbox done;

              public Item() {
                initWidget(ourUiBinder.createAndBindUi(this));
              }

              @UiHandler("done")
              protected void change(IronChangeEvent ev) {
                if (done.getActive()) {
                  title.addClassName("done");
                } else {
                  title.removeClassName("done");
                }
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
            }


7.  Add the logic to create items when we click the save button.

    At this point your `Main.java` should be like this:

        package org.gwtproject.tutorial.client;

        import com.google.gwt.core.client.GWT;
        import com.google.gwt.event.dom.client.ClickEvent;
        import com.google.gwt.uibinder.client.UiBinder;
        import com.google.gwt.uibinder.client.UiField;
        import com.google.gwt.uibinder.client.UiHandler;
        import com.google.gwt.user.client.ui.Composite;
        import com.google.gwt.user.client.ui.HTMLPanel;
        import com.vaadin.polymer.paper.widget.*;

        public class Main extends Composite {
          interface MainUiBinder extends UiBinder<HTMLPanel, Main> {
          }

          private static MainUiBinder ourUiBinder = GWT.create(MainUiBinder.class);

          @UiField HTMLPanel content;

          @UiField PaperDialog addItemDialog;
          @UiField PaperInput titleInput;
          @UiField PaperTextarea descriptionInput;

          public Main() {
            initWidget(ourUiBinder.createAndBindUi(this));
          }

          @UiHandler("addButton")
          protected void onAddButtonClick(ClickEvent e) {
            addItemDialog.open();
          }

          @UiHandler("confirmAddButton")
          protected void onConfirmAddButtonClick(ClickEvent e) {
            if (!titleInput.getValue().isEmpty()) {
              addItem(titleInput.getValue(), descriptionInput.getValue());
              // clear text fields
              titleInput.setValue("");
              descriptionInput.setValue("");
            }
          }

          private void addItem(String title, String description) {
            Item item = new Item();
            item.setTitle(title);
            item.setDescription(description);
            content.add(item);
          }
        }

8.  Reload the application

    Now you can add Todo items and mark them as done using checkboxes.

    <img class='polymer-tutorial-screenshot' src='images/todo-list-08.png'>

9.  Add the **Clear All** and **Clear Done** menu item handlers

        @UiHandler("menuClearAll")
        protected void menuClearAll(ClickEvent e) {
          closeMenu();
          content.clear();
        }

        @UiHandler("menuClearDone")
        protected void menuClearDone(ClickEvent e) {
          closeMenu();
          for (int i = content.getWidgetCount() - 1; i > -1; i--) {
            Item item = (Item)content.getWidget(i);
            if (item.isDone()) {
                content.remove(item);
            }
          }
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
        import com.google.gwt.event.dom.client.ClickEvent;
        import com.google.gwt.uibinder.client.UiBinder;
        import com.google.gwt.uibinder.client.UiField;
        import com.google.gwt.uibinder.client.UiHandler;
        import com.google.gwt.user.client.ui.Composite;
        import com.google.gwt.user.client.ui.HTMLPanel;
        import com.vaadin.polymer.paper.widget.PaperDialog;
        import com.vaadin.polymer.paper.widget.PaperDrawerPanel;
        import com.vaadin.polymer.paper.widget.PaperInput;
        import com.vaadin.polymer.paper.widget.PaperTextarea;

        public class Main extends Composite {
          interface MainUiBinder extends UiBinder<HTMLPanel, Main> {
          }

          private static MainUiBinder ourUiBinder = GWT.create(MainUiBinder.class);

          @UiField PaperDrawerPanel drawerPanel;
          @UiField HTMLPanel content;

          @UiField PaperDialog addItemDialog;
          @UiField PaperInput titleInput;
          @UiField PaperTextarea descriptionInput;

          public Main() {
            initWidget(ourUiBinder.createAndBindUi(this));
          }

          @UiHandler("addButton")
          protected void onAddButtonClick(ClickEvent e) {
            addItemDialog.open();
          }

          @UiHandler("confirmAddButton")
          protected void onConfirmAddButtonClick(ClickEvent e) {
            if (!titleInput.getValue().isEmpty()) {
              addItem(titleInput.getValue(), descriptionInput.getValue());
              // clear text fields
              titleInput.setValue("");
              descriptionInput.setValue("");
            }
          }

          private void addItem(String title, String description) {
            Item item = new Item();
            item.setTitle(title);
            item.setDescription(description);
            content.add(item);
          }

          @UiHandler("menuClearAll")
          protected void menuClearAll(ClickEvent e) {
            closeMenu();
            content.clear();
          }

          private void closeMenu() {
            if (drawerPanel.getNarrow()) {
                drawerPanel.closeDrawer();
            }
          }

          @UiHandler("menuClearDone")
          protected void menuClearDone(ClickEvent e) {
            closeMenu();
            for (int i = content.getWidgetCount() - 1; i > -1; i--) {
                Item item = (Item)content.getWidget(i);
                if (item.isDone()) {
                    content.remove(item);
                }
            }
          }
        }

11. Reload the application

    * Add several items
    * Mark some of them as done
    * Click on "Clear Done" menu item
    * Click on "Clear All" menu item

## Summary

In this chapter we learnt:

1. How to add more widgets to our Application.
2. Add events handlers to UiBinder components.
3. Usage of advanced Polymer components like dialog and buttons.
4. How to use a very basic Data Model in GWT.
