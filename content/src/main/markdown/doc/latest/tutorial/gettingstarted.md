Build a GWT app: Introduction
===

In this tutorial, you'll write this simple AJAX application, StockWatcher.

Go ahead and try StockWatcher out. Add a few stock codes and see how it works.

<iframe name="StockWatcher" src="gettingstarted/StockWatcher.html"
        style="border: none; width: 450px; height: 300px"></iframe>

In the process of building StockWatcher, you'll learn how GWT provides the tools for you to:

*   Write browser applications in Java using the Java IDE of your choice
*   Debug Java in GWT development mode
*   Cross-compile your Java code into highly optimized JavaScript
*   Maintain one code base (Java) for multiple browser implementations (JavaScript)

### AJAX application development process using GWT

This Build a Sample GWT Application tutorial is divided into 8 sections following a typical application development cycle. Each section builds on the previous sections. In this basic implementation of StockWatcher, all functionality is coded on the client-side. Server-side coding and client/server communications are covered in [other tutorials](index.html).

| Tasks: What you'll do                                  | Concepts: What you'll learn                                                                                                                                                                                 | GWT Tools and APIs: What you'll use |
| ------------------------------------------------------ | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ----------------------------------- |
| 1. [Create a GWT Project](create.html)                 | Generate the files and directories you need to get started.                                                                                                                                                 | Google Plugin for Eclipse; GWT command-line tool webAppCreator; Development Mode |
| 2. [Design the Application](design.html)               | Identify requirements, constraints, and implementation strategies.                                                                                                                                          | Language constraints |
| 3. [Build the User Interface](buildui.html)            | Lay out the visual design and add user interface components.                                                                                                                                                | GWT widgets and panels, the Root panel |
| 4. [Manage Events on the Client](manageevents.html)    | Handling mouse and keyboard events.                                                                                                                                                                         | ClickHandler and KeyPressHandler interfaces |
| 5. [Code Functionality on the Client](codeclient.html) | Maintain one code base for multiple browser implementations. Leveraging your Java IDE's features such as refactoring and code completion.                                                                   | various GWT methods |
| 6. [Debug a GWT Application](debug.html)               | Debug the Java code before compiling it into JavaScript. Leverage your Java IDE's debugging tools by running the application in development mode.                                                           | Development Mode |
| 7. [Apply Style](style.html)                           | Apply visual style to the application. Define the visual style in CSS. Set the class attributes on HTML elements programmatically. Change styles dynamically. Include static elements, such as image files. | GWT module; GWT themes; application style sheet; GWT methods: addStyleName, addStyleDependentName, setStyleName; automatic resource inclusion |
| 8. [Compile a GWT Application](compile.html)           | Compile your client-side Java code into JavaScript. Test in production mode. Learn about the benefits of deferred binding.                                                                                  | GWT compiler |

## What's Next

If you have not set up your development environment with the Java SDK, a Java IDE such as Eclipse, and the latest distribution of Google Web Toolkit, do that [before you begin](index.html#prerequisites).

You're ready to create a GWT project.

[Step 1: Creating a GWT Project](create.html)
