Getting Started
===

*  [Prerequisites](#prereqs)
*  [Download and Install GWT](#download)
*  [Create your first web application](#create)
*  [Run locally in development mode](#run)
*  [Make a few changes](#change)
*  [Compile and run in production mode](#compile)
*  [Set up an IDE](#setup)

## Prerequisites<a id="prereqs"></a>

1.  You will need the Java SDK version 1.6 or later. If necessary, <a
    href="http://java.sun.com/javase/downloads/" rel="nofollow">download and
    install the Java SE Development Kit (JDK)</a> for your platform. Mac users,
    see <a href="http://developer.apple.com/java/">Apple's Java developer
    site</a> to download and install the latest version of the Java Developer
    Kit available for Mac OS X.
2.  Apache Ant is also necessary to run command line arguments. If
    you don't already have it, install <a href="http://ant.apache.org/" rel="nofollow">Apache Ant</a>.

If you have problems running Ant on the Mac, try setting the
$JDK_HOME environment variable with export JDK_HOME="/Library/Java/Home"

## Download and Install the GWT SDK<a id="download"></a>

<table class="download">
  <tbody><tr>
    <td>
      <img src="images/sdk-sm.png" style="float: left; width: 80px; height:
      80px;" />
    </td>
    <td>
      <p>
        <a href='download.html'>Download</a> and unzip the GWT SDK. This contains the core
        libraries, compiler, and development server that you need to write web
        applications.
      </p>
      <p>
        See <a href="doc/latest/FAQ_GettingStarted.html">FAQ</a>
        for system and browser requirements.
      </p>
    </td>
  </tr>
</tbody></table>

On Windows, extract the files from the compressed folder `gwt-2.7.0.zip`.  On Mac or Linux, you can unpack the package with a command like:

```
unzip gwt-2.7.0.zip
```

The GWT SDK doesn't have an installer application.  All the files you  need to
run and use the SDK are located in the extracted directory.

## Create your first web application<a id="create"></a>

GWT ships with a command line utility called [webAppCreator](doc/latest/RefCommandLineTools.html#webAppCreator) that automatically generates all the files you'll need in order to start a GWT project.  It also generates [Eclipse](http://www.eclipse.org/) project files and launch config files for easy debugging in GWT's development mode.

You can create a new demo application in a new MyWebApp directory by running `webAppCreator`:

*   **Windows**

```
cd gwt-2.7.0

webAppCreator -out MyWebApp com.mycompany.mywebapp.MyWebApp
```

*   **Mac or Linux** - you may need to make the script executable:

```
cd gwt-2.7.0

chmod u+x webAppCreator

./webAppCreator -out MyWebApp com.mycompany.mywebapp.MyWebApp
```

The `webAppCreator` script will generate a number of files in
`MyWebApp/`, including some basic "Hello, world"
functionality in the class
`MyWebApp/src/com/mycompany/mywebapp/client/MyWebApp.java`.  The
script also generates an Ant build script `MyWebApp/build.xml`.

## Run locally in development mode<a id="run"></a>

To run your newly created application in development mode:

```
cd MyWebApp/

ant devmode
```

This command starts GWT's development mode server, a local server used for development and debugging, as follows:

<div class="screenshot"><a href="images/myapplication-devmode.png"><img src="images/myapplication-devmode.png" alt="Screenshot" width="35%"/></a></div>

Launch the local server in a browser by either 1) clicking "Launch Default Browser"
or 2) clicking "Copy to Clipboard" (to copy its URL), then pasting into Firefox, Internet Explorer,
Chrome, or Safari. Since this is your first time
hitting the development mode server, it will prompt you to install the GWT
Developer Plugin. Follow the instructions in the browser to install the plugin, which
may require restarting the browser.

Once the GWT Developer Plugin is installed in your browser, navigate to
the URL again and the starter application will load in development mode, as follows:.
  
<div class="screenshot"><a href="images/myapplication-browser.png"><img src="images/myapplication-browser.png" alt="Screenshot" width="35%"/></a></div>

## Make a few changes<a id="change"></a>

The source code for the starter application is in the
`MyWebApp/src/` subdirectory, where MyWebApp is the name you gave to
the project above. You'll see two packages,
`com.mycompany.mywebapp.client` and
`com.mycompany.mywebapp.server`. Inside the client package is code that will eventually be compiled to JavaScript and run as client code in the browser. The java files in the server package will be run as Java bytecode on a server, in the case of this Quick Start on the App Engine servers.

Look inside `com/mycompany/mywebapp/client/MyWebApp.java`. Line 41 constructs the "Send" button.

```
final Button sendButton = new Button("Send");
```

Change the text from "Send" to "Send to Server".

```
final Button sendButton = new Button("Send to Server");
```

Now, save the file and simply click "Refresh" in your browser to see your change. The button should now say "Send to Server" instead of "Send":

## Compile and run in production mode<a id="compile"></a>

To run the application as JavaScript in what GWT calls "production mode", compile the application by executing:

```
ant build
```

The "build" Ant target invokes the GWT compiler which generates a number of
JavaScript and HTML files from the MyWebApp Java source code in the
`MyWebApp/war/` subdirectory.  To see the application, open the file
`MyWebApp/war/MyWebApp.html` in your web browser.  The application
should look identical to the development mode above.

Congratulations! You've created your first web application using GWT.
Since you've compiled the project, you're now running pure JavaScript and
HTML that works in IE, Chrome, Firefox, Safari, and Opera. You could now deploy
your application to production by serving the HTML and JavaScript files in your
`MyWebApp/war/` directory from your web servers.

## Set up an IDE<a id="setup"></a>

Now that you've created your first app, you probably want to do something a
bit more interesting. But first, if you normally work with an IDE you'll want to
set up Eclipse to use the GWT SDK:

[Set up Eclipse](usingeclipse.html)

If you are going to stick with the command line, check out Speed Tracer     and then
head over to [Build a Sample GWT App](doc/latest/tutorial/gettingstarted.html).
