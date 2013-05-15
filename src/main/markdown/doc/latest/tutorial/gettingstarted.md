
<style>
code, .code {font-size: 9pt; font-family: Courier, Courier New, monospace; color:#007000;}
.highlight {background-color: #ffc;}
.strike {text-decoration:line-through; color:red;}
.header {margin-top: 1.5ex;}
.details {margin-top: 1ex;}
</style>

<h3>Introduction</h3>
<p>
In this tutorial, you'll write this simple AJAX application, StockWatcher.
</p>
<p>
<iframe name="StockWatcher" height="400" width="400" src="gettingstarted/StockWatcher"></iframe>
<p>
Go ahead and try StockWatcher out. Add a few stock codes and see how it works.
</p>
<p>
In the process of building StockWatcher, you'll learn how GWT provides the tools for you to:
</p>
<ul>
    <li>Write browser applications in Java using the Java IDE of your choice</li>
    <li>Debug Java in GWT development mode</li>
    <li>Cross-compile your Java code into highly optimized JavaScript</li>
    <li>Maintain one code base (Java) for multiple browser implementations (JavaScript)</li>
</ul>

<h4>AJAX application development process using GWT</h4>
<p>
This Build a Sample GWT Application tutorial is divided into 8 sections following a typical application development cycle. Each section builds on the previous sections. In this basic implementation of StockWatcher, all functionality is coded on the client-side. Server-side coding and client/server communications are covered in <a href="/web-toolkit/doc/latest/tutorial/index">other tutorials</a>.
</p>

<table>
    <tr>
        <th>Tasks <br />What you'll do</th>
        <th>Concepts <br />What you'll learn</th>
        <th>GWT Tools and APIs<br />What you'll use</th>
    </tr>
    <tr>
        <td width="300">1. <a href="create.html">Create a GWT Project</a></td>
        <td width="350">Generate the files and directories you need to get started.</td>
        <td width="350"><ul>
            <li>Google Plugin for Eclipse</li>
            <li>GWT command-line tool webAppCreator</li>
            <li>Development Mode</li>
        </ul>
        </td>
    </tr>
    <tr>
        <td>2. <a href="design.html">Design the Application</a></td>
        <td>Identify requirements, constraints, and implementation strategies.</td>
        <td>Language constraints</td>
    </tr>
    <tr>
        <td>3. <a href="buildui.html">Build the User Interface</a></td>
        <td>Lay out the visual design and add user interface components.</td>
        <td>GWT widgets and panels, the Root panel</td>
    </tr>
    <tr>
        <td>4. <a href="manageevents.html">Manage Events on the Client</a></td>
        <td>Handling mouse and keyboard events.</td>
        <td>ClickHandler and KeyPressHandler interfaces</td>
    </tr>
    <tr>
        <td>5. <a href="codeclient.html">Code Functionality on the Client</a></td>
        <td>Maintain one code base for multiple browser implementations. Leveraging your Java IDE's features such as refactoring and code completion.</td>
        <td>various GWT methods</td>
    </tr>
    <tr>
        <td>6. <a href="debug.html">Debug a GWT Application</a></td>
        <td>Debug the Java code before compiling it into JavaScript. Leverage
          your Java IDE's debugging tools by running the application in
          development mode.</td>
        <td>Development Mode</td>
    </tr>
    <tr>
        <td>7. <a href="style.html">Apply Style</a></td>
        <td>Apply visual style to the application. Define the visual style in CSS. Set the class attributes on HTML elements programmatically. Change styles dynamically. Include static elements, such as image files.</td>
        <td>
        <ul>
            <li>GWT module</li>
            <li>GWT themes</li>
            <li>application style sheet</li>
            <li>GWT methods: addStyleName, addStyleDependentName,setStyleName</li>
            <li>automatic resource inclusion</li>
        </ul>
        </td>
    </tr>
    <tr>
        <td>8. <a href="compile.html">Compile a GWT Application</a></td>
        <td>Compile your client-side Java code into JavaScript. Test in production mode. Learn about the benefits of deferred binding.</td>
        <td>GWT compiler</td>
    </tr>
</table>


<h2>What's Next</h2>
<p>
If you have not set up your development environment with the Java SDK, a Java IDE such as Eclipse, and the latest distribution of Google Web Toolkit, do that <a href="index.html#prerequisites">before you begin</a>.
</p>
<p>
You're ready to create a GWT project.
</p>
<p>
<a href="create.html">Step 1: Creating a GWT Project</a>
</p>
