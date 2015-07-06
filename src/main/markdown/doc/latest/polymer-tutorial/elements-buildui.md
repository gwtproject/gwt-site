# Building the UI

In this chapter we will build a modern and good looking UI for the **TodoList** application using the [Material Design](http://www.google.es/design/spec/material-design/introduction.html) specifications. We will work with the Polymer Paper elements collection wrapped through the Vaadin gwt-polymer-elements library.

## Main Screen

1.  Create the **main screen** of the application.

    We are going to create an [UiBinder](DevGuideUiBinder.html) screen composed by a java file  `Main.java` and its visual descriptor `Main.ui.xml`.

    You can generate these files either copying the following snippets or using the Eclipse GWT plugin

     * `Main.java`

            package org.gwtproject.tutorial.client;

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

2.  Add **menu items**.

    Now we can update the `Main.ui.xml` file adding some menu items.

        <ui:UiBinder xmlns:ui='urn:ui:com.google.gwt.uibinder'
                     xmlns:g='urn:import:com.google.gwt.user.client.ui'>

            <g:HTMLPanel>
                <paper-icon-item ui:field="menuClearAll">
                    <iron-icon icon="delete"/>
                    <div>Clear All</div>
                </paper-icon-item>
                <paper-icon-item ui:field="menuClearDone">
                    <iron-icon icon="clear"/>
                    <div>Clear Done</div>
                </paper-icon-item>
                <paper-icon-item ui:field="menuSettings">
                    <iron-icon icon="settings"/>
                    <div>Settings</div>
                </paper-icon-item>
                <paper-icon-item ui:field="menuAbout">
                    <iron-icon icon="help"/>
                    <div>About</div>
                </paper-icon-item>
            </g:HTMLPanel>
        </ui:UiBinder>

    _**Note:** [Here](https://elements.polymer-project.org/elements/paper-item?view=demo:demo/index.html) you can find demos and documentation if you are interested in details._

3.  Update the **entry point** to use our new screen.

        package org.gwtproject.tutorial.client;

        import com.google.gwt.core.client.EntryPoint;
        import com.google.gwt.user.client.ui.RootPanel;
        import com.vaadin.polymer.Polymer;
        import com.vaadin.polymer.elemental.Function;

        public class TodoList implements EntryPoint {

          public void onModuleLoad() {
            // We have to load icon sets before run application
            Polymer.importHref(Arrays.asList(PaperIconItemElement.SRC, IronIconElement.SRC), new Function() {
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

    TODO: describe why we load *Element.SRC stuff

4.  Run the application.

    Reload the page in your browser and you should see four menu items, you can notice that icons are missing though.

    <img class='polymer-tutorial-screenshot' src='images/todo-list-03.png'>

## Icons and Effects

1.  Import **icon collections**.

    Because Polymer comes with several icon collections, we have to import the appropriate set before we use it, hence we must wait until they are ready:

        package org.gwtproject.tutorial.client;

        import com.google.gwt.core.client.EntryPoint;
        import com.google.gwt.user.client.ui.RootPanel;
        import com.vaadin.polymer.Polymer;
        import com.vaadin.polymer.elemental.Function;

        public class TodoList implements EntryPoint {

          public void onModuleLoad() {
            // We have to load icon sets before run application
            Polymer.importHref(Arrays.asList("iron-icons/iron-icons.html",
                   PaperIconItemElement.SRC,
                   IronIconElement.SRC), new Function() {
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

    It is nice when the user gets some feedback when interacting with UI elements. Read the material design [documentation](http://www.google.com.ua/design/spec/animation/responsive-interaction.html#responsive-interaction-radial-action) about that.

    * Add `<paper-ripple/>` to each item in the `Main.ui.xml` file.
    * We need to add some styles to items, so as the ripple effect is constrained into the item area.

        <ui:UiBinder xmlns:ui='urn:ui:com.google.gwt.uibinder'
                     xmlns:g='urn:import:com.google.gwt.user.client.ui'>

            <g:HTMLPanel>
                <style>
                    paper-icon-item {
                        position: relative;
                        overflow: hidden;
                    }
                </style>
                <paper-icon-item ui:field="menuClearAll">
                    <iron-icon icon="delete"/>
                    <div>Clear All</div>
                    <paper-ripple/>
                </paper-icon-item>
                <paper-icon-item ui:field="menuClearDone">
                    <iron-icon icon="clear"/>
                    <div>Clear Done</div>
                    <paper-ripple/>
                </paper-icon-item>
                <paper-icon-item ui:field="menuSettings">
                    <iron-icon icon="settings"/>
                    <div>Settings</div>
                    <paper-ripple/>
                </paper-icon-item>
                <paper-icon-item ui:field="menuAbout">
                    <iron-icon icon="help"/>
                    <div>About</div>
                    <paper-ripple/>
                </paper-icon-item>
            </g:HTMLPanel>
        </ui:UiBinder>

4.  Reload the application

    Compare click reactions before and after adding the PaperRipple effects.

    <img class='polymer-tutorial-screenshot' src='images/todo-list-05.png'>

## Responsive Layout

1.  Layout the application with a Paper **Draw Panel**.

    Paper elements collection comes with a responsive drawer component to layout modern applications so as they behaves nice in both desktop and mobile. If you are interested in details and you want to see a demo visit the [paper-drawer-panel page](https://elements.polymer-project.org/elements/paper-drawer-panel?view=demo:demo/index.html)

        <ui:UiBinder xmlns:ui='urn:ui:com.google.gwt.uibinder'
                     xmlns:g='urn:import:com.google.gwt.user.client.ui'>

            <g:HTMLPanel>
                <style>
                    paper-icon-item {
                        position: relative;
                        overflow: hidden;
                    }
                </style>
                <paper-drawer-panel ui:field="drawerPanel">
                    <div drawer="">
                        <paper-header-panel mode="seamed">
                            <paper-toolbar/>
                            <paper-icon-item ui:field="menuClearAll">
                                <iron-icon icon="delete"/>
                                <div>Clear All</div>
                                <paper-ripple/>
                            </paper-icon-item>
                            <paper-icon-item ui:field="menuClearDone">
                                <iron-icon icon="clear"/>
                                <div>Clear Done</div>
                                <paper-ripple/>
                            </paper-icon-item>
                            <paper-icon-item ui:field="menuSettings">
                                <iron-icon icon="settings"/>
                                <div>Settings</div>
                                <paper-ripple/>
                            </paper-icon-item>
                            <paper-icon-item ui:field="menuAbout">
                                <iron-icon icon="help"/>
                                <div>About</div>
                                <paper-ripple/>
                            </paper-icon-item>
                        </paper-header-panel>
                    </div>
                    <div main="">
                        <paper-header-panel mode="seamed">
                            <paper-toolbar>
                                <paper-icon-button ui:field="menu" icon="more-vert" paper-drawer-toggle=""/>
                                <span>Todo List</span>
                            </paper-toolbar>
                        </paper-header-panel>
                    </div>
                </paper-drawer-panel>
            </g:HTMLPanel>
        </ui:UiBinder>

2. Add the *content panel*

    * Add a container for our Todo Items.

            <g:HTMLPanel>
               ...
                <div main="">
                    <paper-header-panel mode="seamed">
                        <paper-toolbar>
                            <paper-icon-button ui:field="menu" icon="more-vert" paper-drawer-toggle=""/>
                            <span>Todo List</span>
                        </paper-toolbar>

                        <div ui:field="content" class="content vertical center-justified layout"/>

                    </paper-header-panel>
                </div>
               ...
            </g:HTMLPanel>

3. Reload the application

    Now the application should look modern and responsive. Try to resize browser window. If you make it narrower than 640 pixels you can see that draw panel hides the menu.

## Styling the app

Web Components use [Shadow DOM styling](http://www.html5rocks.com/en/tutorials/webcomponents/shadowdom-201/) rules for providing scoped styling of the element. Additionally Polymer provides [Shady DOM](https://www.polymer-project.org/1.0/articles/shadydom.html) to deal with browsers not implementing native shadow. Hence Polymer monitors and parses each `<style>` block rewriting rules on the fly.

The GSS processor, and the way how GWT loads css resources asynchronously make impossible to use `shadow` and `polymer` selectors and properties inside `<ui:style>` blocks because first GSS will complain about certain syntaxes and second Polymer will ignore `CssResource` injected blocks. Thus you have to use normal `<style>` in your `UiBinder` file.

So we have two options are:

1.  Include your css in your hosting page.
2.  Utilize normal `<style>` tags in your `UiBinder` files.

In our example we will use the second way, but be aware that it would include a new `<style>` element each time you might create a new widget. In our case we only have an instance of `Main.ui.xml` in our application.

-  Change color of toolbar, style the heading text and the content panel:

            <g:HTMLPanel>
                <style>
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
                </style>
                ...
                <div main="">
                    <paper-header-panel mode="seamed">
                        <paper-toolbar class="toolbar">
                            <paper-icon-button ui:field="menu" icon="more-vert" paper-drawer-toggle=""/>
                            <span class="header">Todo List</span>
                        </paper-toolbar>
                        <div ui:field="content" class="content vertical center-justified layout"/>
                    </paper-header-panel>
                </div>
               ...
            </g:HTMLPanel>

     _**Note**: that names `vertical`, `center-justified` and `layout` are provided by Polymer._

* Add and stylize the **floating action button**

    Material Design applications use the characteristic floating button for the main action. In paper elements this button is called [paper-fab]().

        <g:HTMLPanel>
          <style is='custom-style'>
            ...
            .add {
                position: absolute;
                bottom: 20px;
                right: 20px;
                --paper-fab-background: var(--paper-red-500);
            }
          </style>
          <div main="">
            ...
            <paper-fab ui:field="addButton" icon="add" title="add" class="add"/>
          </div>
        </g:HTMLPanel>

    _**Tip**: In order to make Polymer process your style block add the `is="custom-style"` attribute_

   _**Tip**: Take a look at custom properties and mixins are available for styling on [paper-fab](https://elements.polymer-project.org/elements/paper-fab) page_

## Summary

Finally your `Main.gwt.xml` file should look like:

    <ui:UiBinder xmlns:ui='urn:ui:com.google.gwt.uibinder'
                 xmlns:g='urn:import:com.google.gwt.user.client.ui'>

        <g:HTMLPanel>
            <style is="custom-style">
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
                .add {
                    position: absolute;
                    bottom: 20px;
                    right: 20px;
                    --paper-fab-background: var(--paper-red-500);
                }
            </style>
            <paper-drawer-panel ui:field="drawerPanel">
                <div drawer="">
                    <paper-header-panel mode="seamed">
                        <paper-toolbar class="toolbar"/>
                        <paper-icon-item ui:field="menuClearAll">
                            <iron-icon icon="delete"/>
                            <div>Clear All</div>
                            <paper-ripple/>
                        </paper-icon-item>
                        <paper-icon-item ui:field="menuClearDone">
                            <iron-icon icon="clear"/>
                            <div>Clear Done</div>
                            <paper-ripple/>
                        </paper-icon-item>
                        <paper-icon-item ui:field="menuSettings">
                            <iron-icon icon="settings"/>
                            <div>Settings</div>
                            <paper-ripple/>
                        </paper-icon-item>
                        <paper-icon-item ui:field="menuAbout">
                            <iron-icon icon="help"/>
                            <div>About</div>
                            <paper-ripple/>
                        </paper-icon-item>
                    </paper-header-panel>
                </div>
                <div main="">
                    <paper-header-panel mode="seamed">
                        <paper-toolbar class="toolbar">
                            <paper-icon-button ui:field="menu" icon="more-vert" paper-drawer-toggle=""/>
                            <span class="header">Todo List</span>
                        </paper-toolbar>
                        <div ui:field="content" class="content vertical center-justified layout"/>
                    </paper-header-panel>
                    <paper-fab ui:field="addButton" icon="add" title="add" class="add"/>
                </div>
            </paper-drawer-panel>
        </g:HTMLPanel>
    </ui:UiBinder>

If everything is OK, after reloading your app should look like this screenshot.

  <img class='polymer-tutorial-screenshot' src='images/todo-list-06.png'>


## What's next

1. We have learnt how to use `UiBinder` with pure `Elements` instead of `Widgets`
2. We also know how we can add 3rd party web components to our UI.
3. We understand the mechanism to import polymer elements like icon collections and use special components like effects.
3. We can deal with responsive layouts using paper panels and mixing them with conventional html elements in GWT.
4. We know how to style elements in `UiBinder` xml files using Polymer parsers.


[Step 3: Add logic to the application](elements-applogic.html)
