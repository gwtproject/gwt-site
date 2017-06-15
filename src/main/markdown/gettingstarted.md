Getting Started
===

*  [Prerequisites](#prereqs)
*  [Download and Install GWT](#download)
*  [Create your first web application using WebAppCreator](#createWebAppCreator)
*  [Run locally in development mode](#run)
*  [Make a few changes](#change)
*  [Compile and run in production mode](#compile)
*  [Set up an IDE](#setup)
*  [Creating modular GWT application using maven Archetype](#createArchetype)

## Prerequisites<a id="prereqs"></a>

1.  You will need the Java SDK version 1.6 or later. If necessary, <a
    href="http://www.oracle.com/technetwork/java/javase/downloads/index.html" rel="nofollow">download and
    install the Java SE Development Kit (JDK)</a> for your platform. Mac users,
    see <a href="http://developer.apple.com/java/">Apple's Java developer
    site</a> to download and install the latest version of the Java Developer
    Kit available for Mac OS X.
2.  Apache Maven is also necessary to run command line arguments. If
    you don't already have it, install <a href="https://maven.apache.org/" rel="nofollow">Apache Maven</a>.


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

On Windows, extract the files from the compressed folder `gwt-2.8.01.zip`.  On Mac or Linux, you can unpack the package with a command like:

```
unzip gwt-2.8.1.zip
```

The GWT SDK doesn't have an installer application.  All the files you  need to
run and use the SDK are located in the extracted directory.

## Create your first web application<a id="createWebAppCreator"></a>

GWT ships with a command line utility called [webAppCreator](http://www.gwtproject.org/doc/latest/RefCommandLineTools.html#webAppCreator) that automatically generates all the files you'll need in order to start a GWT project.  It also generates [Eclipse](http://www.eclipse.org/) project files and launch config files for easy debugging in GWT's development mode.

You can create a new demo application in a new MyWebApp directory by running `webAppCreator`:

*   **Windows**

```
cd gwt-2.8.1

./webAppCreator -out MyWebApp -templates maven,sample,readme com.mycompany.mywebapp.MyWebApp
```

*   **Mac or Linux** - you may need to make the script executable:

```
cd gwt-2.8.1

chmod u+x webAppCreator

./webAppCreator -out MyWebApp -templates maven,sample,readme com.mycompany.mywebapp.MyWebApp
```

The `webAppCreator` script will generate a number of files in
`MyWebApp/`, including some basic "Hello, world"
functionality in the class
`MyWebApp/src/main/java/com/mycompany/mywebapp/client/MyWebApp.java`.  The
script also generates maven pom file `MyWebApp/pom.xml`.

## Run locally in development mode<a id="run"></a>

To run your newly created application in development mode:

```
cd MyWebApp/

mvn clean install
```

Wait until the maven task completion then run the following command :

```
mvn gwt:devmode
```
This command starts GWT's development mode server, a local server used for development and debugging, as follows:

<div class="screenshot"><a href="images/myapplication-devmode.png"><img src="images/myapplication-devmode.png" alt="Screenshot" width="35%"/></a></div>

Launch the local server in a browser by either 1) clicking "Launch Default Browser"
or 2) clicking "Copy to Clipboard" (to copy its URL), then pasting into Firefox, Internet Explorer,
Chrome, or Safari.

The starter application will load in Super Dev Mode as follows:

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
mvn clean install
```

The maven "clean" and "install" tasks invokes the GWT compiler which generates a number of
JavaScript and HTML files from the MyWebApp Java source code in the
`MyWebApp/target/MyWebApp-1.0-SNAPSHOT/mywebapp` subdirectory.  To see the application, open the file
`MyWebApp/target/MyWebApp-1.0-SNAPSHOT/MyWebApp.html` in your web browser. The application
should look identical to the development mode above. a war should be created on `MyWebApp/target/MyWebApp-1.0-SNAPSHOT.war` which can be deployed to your choice of application server.

Congratulations! You've created your first web application using GWT.
Since you've compiled the project, you're now running pure JavaScript and
HTML that works in IE, Chrome, Firefox, Safari, and Opera. You could now deploy
your application to production by serving the HTML and JavaScript files in your
`MyWebApp/target/MyWebApp-1.0-SNAPSHOT` directory from your web servers.

## Set up an IDE<a id="setup"></a>

Now that you've created your first app, you probably want to do something a
bit more interesting. But first, if you normally work with an IDE you'll want to
set up Eclipse to use the GWT SDK:

[Set up Eclipse](usingeclipse.html)

If you are going to stick with the command line, check out Speed Tracer     and then
head over to [Build a Sample GWT App](doc/latest/tutorial/gettingstarted.html).

## Creating modular GWT application using maven Archetype<a id="createArchetype"></a>

There is a maven archetype that generates a multi module maven project in which the GWT application has the source for each of the client, shared and server packages in its own maven module or project.

To generate a GWT modular application open or create any folder in which you want to create the project. e.g: `GWTProjects'
run the command

```
mvn org.apache.maven.plugins:maven-archetype-plugin:2.4:generate \
   -DarchetypeCatalog=https://oss.sonatype.org/content/repositories/snapshots/ \
   -DarchetypeGroupId=net.ltgt.gwt.archetypes \
   -DarchetypeArtifactId=modular-webapp \
   -DarchetypeVersion=HEAD-SNAPSHOT
```

You will be prompt to enter few information that helps creating your project, enter the following values:

```
group id:com.mycompany.mywebapp

artifact id:MyModularGwtApp

version :1.0-SNAPSHOT

package :com.mycompany.mywebapp

module-shor-name:MyModularGwtApp
```

answer any other prompts with the default values.

Wait until the maven task completes, by then you should have a maven project with submodules:

`MyModularGwtApp/MyModularGwtApp-client`, `MyModularGwtApp/MyModularGwtApp-shared`, `MyModularGwtApp/MyModularGwtApp-server`

this is the same as the application we create using the WebAppCreator, but with each package `client`,`shared`,`server` being moved into its own maven submodule.

With this setup we can run the web application server, and the client code server separately.

Running gwt code server :

In one terminal :

```
cd MyModularGwtApp
mvn gwt:codeserver -pl *-client -am
```

Running the web application server:

In another terminal
```
cd MyModularGwtApp
mvn tomcat7:run -pl *-server -am -Denv=dev
```

Open a browser and point the url to <a href="http://localhost:8080" target="_blank">http://localhost:8080</a>

Wait until the compilation is is completed and you should see something like this :

<div class="screenshot"><a href="images/myapplication-browser.png"><img src="images/myapplication-browser.png" alt="Screenshot" width="35%"/></a></div>

Now open the java file `MyModularGwtApp-client/src/main/java/com/mycompany/mywebapp/App.java`

Edit line 42 and change the button text from `Send` to `Send to server`

```
final Button sendButton = new Button("Send to server");
```

Save the file.

on the browser refresh the page, then button text should be changed.

For more information about the GWT modular archetype please refer to the github project <a href="https://github.com/tbroyer/gwt-maven-archetypes" target="_blank">tbroyer/gwt-maven-archetypes</a>