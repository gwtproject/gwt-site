Build a GWT app: Introduction
===

In this tutorial, you'll write this simple AJAX application, StockWatcher.

Go ahead and try StockWatcher out. Add a few stock codes and see how it works.

<iframe name="StockWatcher" src="gettingstarted/StockWatcher.html"></iframe>

In the process of building StockWatcher, you'll learn how GWT provides the tools for you to:

*   Write browser applications in Java using the Java IDE of your choice
*   Debug Java in GWT development mode
*   Cross-compile your Java code into highly optimized JavaScript
*   Maintain one code base (Java) for multiple browser implementations (JavaScript)

### AJAX application development process using GWT

This Build a Sample GWT Application tutorial is divided into 8 sections following a typical application development cycle. Each section builds on the previous sections. In this basic implementation of StockWatcher, all functionality is coded on the client-side. Server-side coding and client/server communications are covered in [other tutorials](index.html).

<table>
    <tr>
        <th>Tasks: What you'll do</th>
        <th>Concepts: What you'll learn</th>
        <th>GWT Tools and APIs: What you'll use</th>
    </tr>
    <tr>
        <td width="300">1. [Create a GWT Project](create.html)</td>
        <td width="350">Generate the files and directories you need to get started.</td>
        <td width="350">

*   Google Plugin for Eclipse
*   GWT command-line tool webAppCreator
*   Development Mode
        </td>
    </tr>
    <tr>
        <td>2. [Design the Application](design.html)</td>
        <td>Identify requirements, constraints, and implementation strategies.</td>
        <td>Language constraints</td>
    </tr>
    <tr>
        <td>3. [Build the User Interface](buildui.html)</td>
        <td>Lay out the visual design and add user interface components.</td>
        <td>GWT widgets and panels, the Root panel</td>
    </tr>
    <tr>
        <td>4. [Manage Events on the Client](manageevents.html)</td>
        <td>Handling mouse and keyboard events.</td>
        <td>ClickHandler and KeyPressHandler interfaces</td>
    </tr>
    <tr>
        <td>5. [Code Functionality on the Client](codeclient.html)</td>
        <td>Maintain one code base for multiple browser implementations. Leveraging your Java IDE's features such as refactoring and code completion.</td>
        <td>various GWT methods</td>
    </tr>
    <tr>
        <td>6. [Debug a GWT Application](debug.html)</td>
        <td>Debug the Java code before compiling it into JavaScript. Leverage
          your Java IDE's debugging tools by running the application in
          development mode.</td>
        <td>Development Mode</td>
    </tr>
    <tr>
        <td>7. [Apply Style](style.html)</td>
        <td>Apply visual style to the application. Define the visual style in CSS. Set the class attributes on HTML elements programmatically. Change styles dynamically. Include static elements, such as image files.</td>
        <td>

*   GWT module
*   GWT themes
*   application style sheet
*   GWT methods: addStyleName, addStyleDependentName,setStyleName
*   automatic resource inclusion
        </td>
    </tr>
    <tr>
        <td>8. [Compile a GWT Application](compile.html)</td>
        <td>Compile your client-side Java code into JavaScript. Test in production mode. Learn about the benefits of deferred binding.</td>
        <td>GWT compiler</td>
    </tr>
</table>

## What's Next

If you have not set up your development environment with the Java SDK, a Java IDE such as Eclipse, and the latest distribution of Google Web Toolkit, do that [before you begin](index.html#prerequisites).

You're ready to create a GWT project.

[Step 1: Creating a GWT Project](create.html)
