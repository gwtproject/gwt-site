# Building the UI with GWT Widgets.

In this chapter we will build a modern and good looking UI for the **TodoList** application using the [Material Design](http://www.google.es/design/spec/material-design/introduction.html) specifications. We will work with the Polymer Paper elements collection wrapped through the Vaadin gwt-polymer-elements library.

## Main Screen

1.  Create the **main screen** of the application.

    We will create an [UiBinder](DevGuideUiBinder.html) screen composed by a java file  `Main.java` and its visual descriptor `Main.ui.xml`. You can generate these files either copying the following snippets or using the Eclipse GWT plugin.

     * `Main.java`

            package org.gwtproject.tutorial;

            import com.google.gwt.core.client.GWT;
            import com.google.gwt.uibinder.client.UiBinder;
            import com.google.gwt.user.client.ui.Composite;
            import com.google.gwt.user.client.ui.HTMLPanel;

            public class Main extends Composite {
              interface MainUiBinder extends UiBinder<HTMLPanel, Main> {
              }

              private static MainUiBinder ourUiBinder = GWT.create(MainUiBinder.class);

              public Main() {
                initWidget(ourUiBinder.createAndBindUi(this));
              }
            }

     * `Main.ui.xml`

            <ui:UiBinder xmlns:ui='urn:ui:com.google.gwt.uibinder'
                     xmlns:g='urn:import:com.google.gwt.user.client.ui'>

              <g:HTMLPanel>
              </g:HTMLPanel>

            </ui:UiBinder>

2.  Adding some **menu items**.

    Now we can update the `Main.ui.xml` file adding some menu items.

        <ui:UiBinder xmlns:ui='urn:ui:com.google.gwt.uibinder'
                 xmlns:g='urn:import:com.google.gwt.user.client.ui'
                 xmlns:p='urn:import:com.vaadin.polymer.paper.widget'
                 xmlns:i='urn:import:com.vaadin.polymer.iron.widget'>

          <g:HTMLPanel>
            <p:PaperIconItem ui:field="menuClearAll">
                <i:IronIcon icon="delete"/>
                <div>Clear All</div>
            </p:PaperIconItem>
            <p:PaperIconItem ui:field="menuClearDone">
                <i:IronIcon icon="clear"/>
                <div>Clear Done</div>
            </p:PaperIconItem>
            <p:PaperIconItem ui:field="menuSettings">
                <i:IronIcon icon="settings"/>
                <div>Settings</div>
            </p:PaperIconItem>
            <p:PaperIconItem ui:field="menuAbout">
                <i:IronIcon icon="help"/>
                <div>About</div>
            </p:PaperIconItem>
          </g:HTMLPanel>
        </ui:UiBinder>

    _**Note** that in this step we have added the necessary imports for Paper and Iron packages.
    [Here](https://elements.polymer-project.org/elements/paper-item?view=demo:demo/index.html) you can find demos and documentation if you are interested in details._

3.  Update the **entry point** to use our new screen.

        package org.gwtproject.tutorial;

        import com.google.gwt.core.client.EntryPoint;
        import com.google.gwt.user.client.ui.RootPanel;

        public class TodoList implements EntryPoint {

          public void onModuleLoad() {
            RootPanel.get().add(new Main());
          }
        }

4.  Run the application.

    Reload the page in your browser and you should see four menu items. You can notice that icons are missing though.

    <img class='polymer-tutorial-screenshot' src='images/todo-list-03.png'>

## Icons and Effects

1.  Import **icon collections**.

    Because Polymer comes with several icon collections, we have to import the appropriate set before we can use it, hence we must wait until they are ready using the `importHref` utility method:

        package org.gwtproject.tutorial;

        import com.google.gwt.core.client.EntryPoint;
        import com.google.gwt.user.client.ui.RootPanel;
        import com.vaadin.polymer.Polymer;
        import com.vaadin.polymer.elemental.Function;

        public class TodoList implements EntryPoint {

          public void onModuleLoad() {
            // We have to load icon sets before run application
            Polymer.importHref("iron-icons/iron-icons.html", new Function() {
                public Object call(Object arg) {
                    // The app is executed when all imports succeed.
                    startApplication();
                    return null;
                }
            });
          }

          private void startApplication() {
            RootPanel.get().add(new Main());
          }
        }

2.  Reload the application

    You should see all icons in the browser now.

    <img class='polymer-tutorial-screenshot' src='images/todo-list-04.png'>

3.  Add a **Ripple** effect

    It is nice when the user gets some feedback when interacting with UI elements. Read the material design [documentation](http://www.google.com.ua/design/spec/animation/responsive-interaction.html#responsive-interaction-radial-action) about that idea.

    * Add `<p:PaperRipple/>` to each item in the `Main.ui.xml` file.
    * We need to add some styles to items, so as the ripple effect is constrained into the item area.

            <ui:UiBinder xmlns:ui='urn:ui:com.google.gwt.uibinder'
                 xmlns:g='urn:import:com.google.gwt.user.client.ui'
                 xmlns:p='urn:import:com.vaadin.polymer.paper.widget'
                 xmlns:i='urn:import:com.vaadin.polymer.iron.widget'>

                <ui:style>
                  paper-icon-item {
                    position: relative;
                    overflow: hidden;
                  }
                </ui:style>

                <g:HTMLPanel>
                  <p:PaperIconItem ui:field="menuClearAll">
                    <i:IronIcon icon="delete"/>
                    <div>Clear All</div>
                    <p:PaperRipple/>
                  </p:PaperIconItem>
                  <p:PaperIconItem ui:field="menuClearDone">
                    <i:IronIcon icon="clear"/>
                    <div>Clear Done</div>
                    <p:PaperRipple/>
                  </p:PaperIconItem>
                  <p:PaperIconItem ui:field="menuSettings">
                    <i:IronIcon icon="settings"/>
                    <div>Settings</div>
                    <p:PaperRipple/>
                  </p:PaperIconItem>
                  <p:PaperIconItem ui:field="menuAbout">
                    <i:IronIcon icon="help"/>
                    <div>About</div>
                    <p:PaperRipple/>
                  </p:PaperIconItem>
                </g:HTMLPanel>
            </ui:UiBinder>

4.  Reload the application

    Compare click reactions before and after adding `PaperRipple` effects.

## Responsive Layout

1.  Layout the application with a `PaperDrawPanel`.

    The paper elements collection comes with a responsive drawer component to layout modern applications so as they behave nice in both desktop and mobile. If you are interested in details and you want to see a demo visit the [paper-drawer-panel page](https://elements.polymer-project.org/elements/paper-drawer-panel?view=demo:demo/index.html)

        <ui:UiBinder xmlns:ui='urn:ui:com.google.gwt.uibinder'
                 xmlns:g='urn:import:com.google.gwt.user.client.ui'
                 xmlns:p='urn:import:com.vaadin.polymer.paper.widget'
                 xmlns:i='urn:import:com.vaadin.polymer.iron.widget'>

          <ui:style>
             paper-icon-item {
               position: relative;
               overflow: hidden;
             }
          </ui:style>

          <g:HTMLPanel>
            <p:PaperDrawerPanel ui:field="drawerPanel">
                <div drawer="">
                    <p:PaperHeaderPanel mode="seamed">
                        <p:PaperToolbar/>
                        <p:PaperIconItem ui:field="menuClearAll">
                            <i:IronIcon icon="delete"/>
                            <div>Clear All</div>
                            <p:PaperRipple/>
                        </p:PaperIconItem>
                        <p:PaperIconItem ui:field="menuClearDone">
                            <i:IronIcon icon="clear"/>
                            <div>Clear Done</div>
                            <p:PaperRipple/>
                        </p:PaperIconItem>
                        <p:PaperIconItem ui:field="menuSettings">
                            <i:IronIcon icon="settings"/>
                            <div>Settings</div>
                            <p:PaperRipple/>
                        </p:PaperIconItem>
                        <p:PaperIconItem ui:field="menuAbout">
                            <i:IronIcon icon="help"/>
                            <div>About</div>
                            <p:PaperRipple/>
                        </p:PaperIconItem>
                    </p:PaperHeaderPanel>
                </div>
                <div main="">
                    <p:PaperHeaderPanel mode="seamed">
                        <p:PaperToolbar>
                            <p:PaperIconButton ui:field="menu" icon="more-vert"
                                attributes="paper-drawer-toggle"/>
                            <span>Todo List</span>
                        </p:PaperToolbar>
                    </p:PaperHeaderPanel>
                </div>
            </p:PaperDrawerPanel>
          </g:HTMLPanel>
        </ui:UiBinder>

2. Add the *content panel*

    * Add a container for our Todo Items.

            <g:HTMLPanel>
               ...
                <div main="">
                    <p:PaperHeaderPanel mode="seamed">
                        <p:PaperToolbar>
                            <p:PaperIconButton ui:field="menu" icon="more-vert"
                                attributes="paper-drawer-toggle"/>
                            <span>Todo List</span>
                        </p:PaperToolbar>

                        <g:HTMLPanel ui:field="content"
                           addStyleNames="vertical center-justified layout" />

                    </p:PaperHeaderPanel>
                </div>
               ...
            </g:HTMLPanel>

3. Reload the application

    Now the application should look modern and responsive. Try to resize browser window. If you make it narrower than 640 pixels you can see that draw panel hides the menu.

    <img class='polymer-tutorial-screenshot' src='images/todo-list-05.png'>

## Styling the app

### Styling with CssResource
When using polymer widgets you can use GWT [GSS](http://www.gwtproject.org/doc/latest/DevGuideGssVsCss.html) parser as usual, and add styles to widgets or elements using the `{}` operator.

-  Change color of toolbar, style the heading text and the content panel:

            <ui:style>
              .toolbar {
                background: #4285f4 !important;
               }
              .header {
                font-size: 200%;
                margin-left: 50px;
                background: #4285f4 !important;
               }
              .content {
                padding: 15px;
               }
               ...
            </ui:style>
            <g:HTMLPanel>
               ...
                <div main="">
                    <p:PaperHeaderPanel mode="seamed">
                        <p:PaperToolbar addStyleNames="{style.toolbar}">
                            <p:PaperIconButton ui:field="menu" icon="more-vert"
                                attributes="paper-drawer-toggle"/>
                            <span class="{style.header}">Todo List</span>
                        </p:PaperToolbar>
                        <g:HTMLPanel ui:field="content"
                               addStyleNames="{style.content} vertical center-justified layout"/>
                    </p:PaperHeaderPanel>
                </div>
               ...
            </g:HTMLPanel>

_**Note**: that classes `vertical`, `center-justified` and `layout` are provided by Polymer._

### Styling with Polymer
Web Components use [Shadow DOM styling](http://www.html5rocks.com/en/tutorials/webcomponents/shadowdom-201/) rules for providing scoped styling of the element. Additionally Polymer provides [Shady DOM](https://www.polymer-project.org/1.0/articles/shadydom.html) to deal with browsers not implementing native shadow. Hence Polymer monitors and parses each `<style>` block rewriting rules on the fly.

The GSS processor, and the way how GWT loads css resources make impossible to use `shadow` and `polymer` selectors and properties inside `<ui:style>` blocks. Thus you have to use normal `<style>` in your `UiBinder` file.

Another caveat is that GWT deferres `CssResource` loading, so under certain circumstances where you need style rules before the element is in the DOM, you might use `<style>`.

* Add and stylize the **floating action button**

    Material Design applications use the characteristic floating button for the main action. In paper elements this button is called [paper-fab][1].

        <ui:style>
        ...
        </ui:style>
        <g:HTMLPanel>
          <style is='custom-style'>
            .add {
                position: absolute;
                bottom: 20px;
                right: 20px;
                --paper-fab-background: var(--paper-red-500);
            }
          </style>
          <div main="">
            ...
            <p:PaperFab ui:field="addButton" icon="add"
                        addStyleNames="add"/>
          </div>
        </g:HTMLPanel>

_**Tip**: In order to make Polymer process your style block add the `is="custom-style"` attribute_

_**Tip**: Take a look at custom properties and mixins are available for styling on [paper-fab][1] page_

[1]: https://elements.polymer-project.org/elements/paper-fab

## Summary

Finally your `Main.gwt.xml` file should look like:

    <ui:UiBinder xmlns:ui='urn:ui:com.google.gwt.uibinder'
             xmlns:g='urn:import:com.google.gwt.user.client.ui'
             xmlns:p='urn:import:com.vaadin.polymer.paper.widget'
             xmlns:i='urn:import:com.vaadin.polymer.iron.widget'>

      <ui:style>
        paper-icon-item {
          position: relative;
          overflow: hidden;
        }
        .toolbar {
           background: #4285f4 !important;
        }
        .header {
           font-size: 200%;
           margin-left: 50px;
        }
        .content {
           padding: 15px;
        }
      </ui:style>

      <g:HTMLPanel>
        <style is='custom-style'>
          .add {
            position: absolute;
            bottom: 20px;
            right: 20px;
            --paper-fab-background: var(--paper-red-500);
          }
        </style>
        <p:PaperDrawerPanel ui:field="drawerPanel">
           <div drawer="">
              <p:PaperHeaderPanel mode="seamed">
                 <p:PaperToolbar addStyleNames="{style.toolbar}"/>
                 <p:PaperIconItem ui:field="menuClearAll">
                   <i:IronIcon icon="delete"/>
                   <div>Clear All</div>
                   <p:PaperRipple/>
                 </p:PaperIconItem>
                 <p:PaperIconItem ui:field="menuClearDone">
                   <i:IronIcon icon="clear"/>
                   <div>Clear Done</div>
                   <p:PaperRipple/>
                 </p:PaperIconItem>
                 <p:PaperIconItem ui:field="menuSettings">
                   <i:IronIcon icon="settings"/>
                   <div>Settings</div>
                   <p:PaperRipple/>
                 </p:PaperIconItem>
                 <p:PaperIconItem ui:field="menuAbout">
                   <i:IronIcon icon="help"/>
                   <div>About</div>
                   <p:PaperRipple/>
                 </p:PaperIconItem>
              </p:PaperHeaderPanel>
           </div>
           <div main="">
              <p:PaperHeaderPanel mode="seamed">
                <p:PaperToolbar addStyleNames="{style.toolbar}">
                  <p:PaperIconButton ui:field="menu" icon="more-vert"
                                     attributes="paper-drawer-toggle"/>
                  <span class="{style.header}">Todo List</span>
                </p:PaperToolbar>
                <g:HTMLPanel ui:field="content"
                             addStyleNames="{style.content} vertical center-justified layout"/>
              </p:PaperHeaderPanel>
              <p:PaperFab ui:field="addButton" icon="add"
                          addStyleNames="add"/>
           </div>
        </p:PaperDrawerPanel>
      </g:HTMLPanel>
    </ui:UiBinder>

If everything is OK, after reloading your app should look like this:

  <img class='polymer-tutorial-screenshot' src='images/todo-list-06.png'>

## What's next

1. We have learnt how to create new widgets using `UiBinder`.
2. We can add 3rd party widgets to our UI.
3. We know the mechanism to import polymer elements like icon collections and use special components like effects.
3. We can deal with responsive layouts using paper panels and mixing them with conventional GWT widgets.
4. We know how to style elements in `UiBinder` xml files using GSS or Polymer parsers.

[Step 3a: Add the event logic to the application](widgets-applogic.html)
