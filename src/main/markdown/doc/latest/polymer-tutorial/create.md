# Creating the *TodoList* Project

In this section, you'll create from scratch the **TodoList** project using the GWT command-line utility [webAppCreator](http://www.gwtproject.org/doc/latest/RefCommandLineTools.html#webAppCreator).

We assume that you've already downloaded the most recent distribution of GWT, and you have [maven](https://maven.apache.org/) installed in your system.

## Using webAppCreator

The `webAppCreator` is a command-line tool included in the GWT SDK that generates all the project structure you need to get started. It creates a starter application, which you can run to ensure that all the components have been created and are hooked together correctly. As you develop your application, you'll replace the code for the starter application with your code.

For the **TodoList** project, you have to run `webAppCreator` with the following parameters.

| Parameter  | Definition                                     | Example      |
| ---------- | -----------------------------------------------| ------------ |
| -templates | Comma separated templates to use               | maven,sample |
| -out       | The directory to place the generated files.    | TodoList     |
| moduleName | The name of the GWT module you want to create. | org.gwtproject.tutorial.TodoList |


## Setting up a new project.

1. Create the **TodoList** application.

      GWT webAppCreator will generate all the project structure and builder.

        $ /full_path_to_gwt_sdk/webAppCreator \
            -templates maven,sample \
            -out TodoListApp \
            org.gwtproject.tutorial.TodoList

      _**Tip**: If you include the GWT SDK folder in your PATH environment variable, you won't have to invoke them by specifying their full path._


2. Run the application in [SuperDevMode](articles/superdevmode.html).

     To check that the project was created correctly start the new app in SuperDevMode.

        $ mvn gwt:run

      _**Tip**: At this point you might want to import project in your favorite IDE. Since the created project is built with maven you can import it directly in Eclipse, Idea, etc._

3. Launch your Browser.

     In the GWT developer window press the “Launch Default Browser” button to launch our application using your default browser. Or, you can click “Copy to Clipboard” to copy the launch URL and paste it into the browser of your choice.

     If you change something in the code you can recompile the application just reloading the page in your browser. Otherwise if you change some configuration files like `pom.xml`, `webapp` static content, etc, you would have to stop SuperDevMode pushing `Ctrl-C` and running again `mvn gwt:run`.

     If you are running chrome, you will be able to inspect and debug java code opening the developer tools and enabling the support for super sources.
     
## Customizing your project

Once we have our base project, we will add the external dependencies we need in our application, and remove all unneeded example stuff.

1. Add the vaadin `gwt-polymer-elements` dependency to your project editing the `pom.xml` file.

        <dependency>
         <groupId>com.vaadin.polymer</groupId>
         <artifactId>gwt-polymer-elements</artifactId>
         <version>${gwtPolymerVersion}</version>
         <scope>provided</scope>
        </dependency>

     _**Note**: Replace the `${gwtPolymerVersion}` placeholder with current version or add the correspondent property in your pom.xml_

2. Update the gwt-maven-plugin configuration to support the experimental `JsInterop` feature.

        <plugin>
          <groupId>org.codehaus.mojo</groupId>
          <artifactId>gwt-maven-plugin</artifactId>
          ...
          <configuration>
            <jsInteropMode>JS</jsInteropMode>
            ...
          </configuration>
        </plugin>

      _**Note**: JsInterop is an experimental flag in GWT-2.7.0 and you need to enable it explicitely. In future versions of GWT it will be enabled by default though._

3. Update `Todo.gwt.xml` module file so as we can use the new gwt library.

        <module rename-to="todolist">
          ...
          <inherits name="com.vaadin.polymer.Elements"/>
          ...
        </module>

4. Update `TodoList.html`
    * Configure `<meta>` viewport to deal with mobile screens.
    * Import the polyfill `<script>` for non web-component capable browsers.
    * Remove all the content from `<body>`.

            <!doctype html>
            <html>
            <head>
             <meta name="viewport"
                 content="user-scalable=no, initial-scale=1, maximum-scale=1, minimum-scale=1" />
             <script src="todo/bower_components/webcomponentsjs/webcomponents.js"></script>
             <script type="text/javascript" src="todo/todo.nocache.js"></script>
            </head>
            <body>
            </body>
            </html>

5. Remove `greetServlet` and it's mapping from `WEB-INF/web-xml`

        <web-app>
        </web-app>

6. Remove all unnecessary files.

    *  Remove folders `server` and `shared` under the `src/main/java/org/gwtproject/tutorial` folder.
    *  Remove `GreetingService.java` and `GreetingServiceAsync.java` classes fron the client package.
    *  Remove example tests `src/main/test`

7. Update the **EntryPoint** method.

    Replace the content of `TodoList.java` with

        package org.gwtproject.tutorial;

        import com.google.gwt.core.client.EntryPoint;
        import com.google.gwt.user.client.ui.RootPanel;
        import com.vaadin.polymer.paper.widget.PaperButton;

        public class TodoList implements EntryPoint {
          public void onModuleLoad() {
            // Use Widget API to Create a <paper-button>
            PaperButton button = new PaperButton("Press me!");
            button.setRaised(true);
            RootPanel.get().add(button);

            // Use the Element API to create a <paper-button>
            PaperButtonElement buttonElement =
               Polymer.createElement(PaperButtonElement.TAG);
            buttonElement.setTextContent("Click me!");
            Document.get().getBody().appendChild((Element)buttonElement);
          }
        }

    _**Note**: that we have added the PaperButton element here, but using different API's: the classic GWT Widget approach and Elements._

8. Run the application again.

    You should see a page with a couple of material design buttons in your browser.

    _**Tip**: If you get ClassCastException in browser console, ensure that you use `-XjsInteropMode JS` parameter_

## What's next

In this lesson we have learnt

- How to create a new GWT maven project fron scratch.
- To run and debug our application in SuperDevMode
- Adding external dependencies to our application
- Configure our project to use experimental `JsInterop` mode.
- Replacing starter code by our own.

Now you are prepared to design the UI of the **TodoList** application, ther are two options though, you can continue the Tutorial using Classic GWT widgets or the Elements API approach which is the tendency in modern GWT.

[Step 2a: Building the User Interface using Widgets](widgets-buildui.html)

[Step 2b: Building the User Interface using Elements](elements-buildui.html)
