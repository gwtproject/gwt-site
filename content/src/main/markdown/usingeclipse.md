Using Eclipse
===

GWT provides a set of tools that can simply be used with a
text editor, the command line, and a browser. However, you may also use GWT with your
favorite IDE. [The GWT Plugins Project](https://github.com/gwt-plugins/gwt-eclipse-plugin) provides a plugin for Eclipse that makes development with
GWT even easier.

*   [Download Eclipse](#eclipse)
*   [Install the Plugin](#installing)
*   [Create a Web Application](#creating)
*   [Run locally in Development Mode](#running)
*   [Compile and run in Production Mode](#compiling)
*   [Deploy to App Engine](#deploying)

## Download Eclipse<a id="eclipse"></a>

If you do not already have Eclipse, you may download it from the [Eclipse Website](http://www.eclipse.org/downloads/). 

## Install the Plugin<a id="installing"></a>

Install the GWT Plugin for Eclipse following [these instructions](https://github.com/gwt-plugins/gwt-eclipse-plugin)

The best way to install is by using the [Eclipse Marketplace](https://marketplace.eclipse.org/content/gwt-plugin).

<a href="https://marketplace.eclipse.org/marketplace-client-intro?mpc_install=5576850" class="drag" title="Drag to your running Eclipse* workspace. *Requires Eclipse Marketplace Client"><img style="width:80px;" typeof="foaf:Image" class="img-responsive" src="https://marketplace.eclipse.org/sites/all/themes/solstice/public/images/marketplace/btn-install.svg" alt="Drag to your running Eclipse* workspace. *Requires Eclipse Marketplace Client" /></a>

Alternatively you may download the repository [here https://github.com/gwt-plugins/gwt-eclipse-plugin/releases/v4.0.0](https://github.com/gwt-plugins/gwt-eclipse-plugin/releases/v4.0.0)

After installing, you should configure the GWT settings in the Eclipse Preferences Dialog.


## Create a Web Application<a id="creating"></a>

To create a Web Application, select **File > New > GWT Web Application Project** from the Eclipse menu.

In the **New GWT Application Project** wizard, enter a name for your project
and a java package name, e.g., `com.mycompany.mywebapp` and click **Finish**.

<div class="screenshot">
  <img src="images/eclipse/web-app-wizard.png"
  style="width: 366px; height: 409px;" alt="screenshot"/>
</div>

Congratulations, you now have a GWT enabled web application. The plugin has created a boilerplate project in your workspace.

<h2 id="running">Run locally in Development Mode</h2>

Right-click on your web application project and select **Debug As > GWT Development mode with Jetty** from the popup menu.

This creates a **GWT Development Mode** launch configuration for
you and launches it.  The GWT Development Mode launch configuration will start a
local web server and GWT development mode server.

After the launch completed (which takes some time) Eclipse shows the URL of your Application on the Tab **Development Mode**.
Just Doubleclick to launch the Browser.

## Make a Few Changes

The source code for the starter application is in the 

`MyWebApp/src/`

subdirectory, where MyWebApp is the name you gave to the project. You'll see two packages, 

`com.mycompany.mywebapp.client` 

and

`com.mycompany.mywebapp.server`

Inside the client package is code that will eventually be compiled to JavaScript and run as client code in the browser. The java files in the server package will be run as Java bytecode on a server.

<div class="screenshot">
  <img src="images/eclipse/web-app-src.png"
  style="width: 350px; height: 243px;" alt="screenshot"/>
</div>

Look inside the 

`MyWebApp.java` 

file in the client package. Line 40 constructs the send button.

```java
final Button sendButton = new Button("Send");
```

Change the text from "Send" to "Send to Server".

```java
final Button sendButton = new Button("Send to Server");
```

Now, save the file and simply click "Refresh" back in your browser to see your change. The button should now say "Send to Server" instead of "Send".

## Compile and run in Production Mode <a id="compiling"></a>

To run the application as JavaScript in what GWT calls "production mode", compile the application by right-clicking the project and choosing **GWT** > **Compile**.

This command invokes the GWT compiler which generates a number of JavaScript and HTML files from the MyWebApp Java source code in the

`MyWebApp/war/` 

subdirectory. To see the final application, open the file

`MyWebApp/war/MyWebApp.html` 

in your web browser.

<div class="screenshot"><img src="images/myapplication-browser.png" alt="Screenshot"/></div>

Congratulations! You've created your first web application using GWT. Since you've compiled the project, you're now running pure JavaScript and HTML that works in Edge, Chrome, Firefox, Safari, and Opera. You could now deploy your application to production by serving the HTML and JavaScript files in your 

`MyWebApp/war/` 

directory from your web servers.

