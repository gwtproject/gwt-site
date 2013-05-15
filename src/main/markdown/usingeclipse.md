
<style>
code, .code {font-size: 9pt; font-family: Courier, Courier New, monospace; color:#007000;}
.highlight {background-color: #ffc;}
.strike {text-decoration:line-through; color:red;}
.header {margin-top: 1.5ex;}
.details {margin-top: 1ex;}
</style>

<style>

div.screenshot img {
  margin: 20px;
}


.download {
  border: none;
}

.download td {
  border: none;
}
</style>

<p>GWT provides a set of tools that can simply be used with a
text editor, the command line, and a browser. However, you may also use GWT with your
favorite IDE. Google provides a plugin for Eclipse that makes development with
GWT even easier.
</p>

<ul class="toc">
  <li><a href="#eclipse">Download Eclipse</a></li>
  <li><a href="#installing">Install the Plugin</a></li>
  <li><a href="#creating">Create a Web Application</a></li>
  <li><a href="#running">Run locally in Development Mode</a></li>
  <li><a href="#compiling">Compile and run in Production Mode</a></li>
  <li><a href="#deploying">Deploy to App Engine</a></li>
</ul>

<h2 id="eclipse">Download Eclipse</h2>

<p>
If you do not already have Eclipse, you may download it from the <a
href="http://www.eclipse.org/downloads/" rel="nofollow">Eclipse Website</a>.
We suggest downloading Eclipse 3.7 (Indigo).
</p>

<h2 id="installing">Install the Plugin</h2>

<img src="https://developers.google.com/eclipse/images/google-plugin.png" style="float: right; width: 100px;
margin: 5px 15px 5px 5px;" height="100" width="100" />

<p>
Install the Google Plugin for Eclipse 3.7 by using the following update
site:
<p><code>http://dl.google.com/eclipse/plugin/3.7</code></p>

<p>
If you are using an earlier version of Eclipse, replace the 3.7 version number
with your version (3.6 or 3.5). For detailed instructions on installing plugins
in Eclipse, see instructions for <a href="//developers.google.com/eclipse/docs/install-eclipse-3.7">Eclipse 3.7</a>,
<a href="https://developers.google.com/eclipse/docs/install-eclipse-3.6">Eclipse 3.6</a>
</p>

<p>In the Install dialog, you will see an option to install the Plugin as well
as the GWT and App Engine SDKs. Choosing the SDK options will install a GWT
and/or App Engine SDK within your Eclipse plugin directory as a convinience.
</p>

<p class="note" style="margin-top: 0.7em;">GWT release candidates are not bundled with The Google Plugin For Eclipse. If you're interested in using a GWT RC SDK, download and add it to your workspace as described <a href="https://developers.google.com/eclipse/docs/using_sdks">here</a>.
</p>

<div class="screenshot">
  <img src="images/eclipse/eclipse-install-options.png"
  style="width: 385px; height: 100px;" alt="screenshot"/>
</div>

<h2 id="creating">Create a Web Application</h2>
<p>
  To create a Web Application, select <b>File &gt; New &gt;
  Web Application Project</b> from the Eclipse menu.
</p>
<p>
  In the <b>New Web Application Project</b> wizard, enter a name for your project
  and a java package name, e.g., <code>com.mycompany.mywebapp</code>. If you
  installed the Google App Engine SDK, the wizard
  gives you the option to use App Engine as well. For now, uncheck this
  option and click <b>Finish</b>.
</p>

<div class="screenshot">
  <img src="images/eclipse/web-app-wizard.png"
  style="width: 366px; height: 409px;" alt="screenshot"/>
</div>

<p>
  Congratulations, you now have a GWT enabled web application.
  The plugin has created a boilerplate project in your workspace.
</p>

<h2 id="running">Run locally in Development Mode</h2>
<p>
  Right-click on your web application project and select <b>Debug As &gt; Web
  Application</b> from the popup menu.
</p>
<p>
  This creates a <b>Web Application</b> launch configuration for
  you and launches it.  The web application launch configuration will start a
  local web server and GWT development mode server.
</p>
<p>
  You will find a Web Application view next to the console window.
  Inside you will find the URL for the development mode server. Paste
  this URL into Firefox, Internet Explorer, Chrome, or Safari. If this is your first time
  using that browser with the development mode server, it will prompt you to install
  the GWT Developer Plugin. Follow the instructions in the browser to install.
</p>
<div class="screenshot">
  <img src="images/myapplication-missing-plugin.png" alt="screenshot"/>
</div>
<p>
  Once the browser plugin is installed, navigate to the URL again and the starter application will load in development
  mode.
</p>


<h2>Make a Few Changes</h2>
<p>The source code for the starter application is in the <code>MyWebApp/src/</code> subdirectory, where MyWebApp is the name you gave to the project.
You'll see two packages, <code>com.mycompany.mywebapp.client</code> and
<code>com.mycompany.mywebapp.server</code>. Inside the client package is code that will eventually be compiled to JavaScript and run as client code in the browser. The java files in the server package will be run as Java bytecode on a server.</p>

<div class="screenshot">
  <img src="images/eclipse/web-app-src.png"
  style="width: 350px; height: 243px;" alt="screenshot"/>
</div>

<p>
  Look inside the <code>MyWebApp.java</code> file in the client package. Line 40 constructs the send button.
</p>

<pre class="code">final Button sendButton = new Button(&quot;Send&quot;);</pre>

<p>
  Change the text from &quot;Send&quot; to &quot;Send to Server&quot;.
</p>

<pre class="code">final Button sendButton = new Button(&quot;Send to Server&quot;);</pre>


<p>Now, save the file and simply click "Refresh" back in your browser to see your change. The button should now say &quot;Send to Server&quot; instead of &quot;Send&quot;.</p>

<p>
  At this point, you can also set breakpoints, inspect variables and modify code as
  you would normally expect from a Java Eclipse debugging session.
</p>

<a name="compiling"></a>
<h2>Compile and run in Production Mode</h2>

<p>To run the application as JavaScript in what GWT calls "production mode",
compile the application by right-clicking the project and choosing <b>Google</b>
&gt; <b>GWT Compile</b>.
</p>
<p>This command invokes the GWT compiler which generates a number of
JavaScript and HTML files from the MyWebApp Java source code in the
<code>MyWebApp/war/</code> subdirectory.  To see the final application, open the file
<code>MyWebApp/war/MyWebApp.html</code> in your web browser.</p>

<div class="screenshot"><img src="images/myapplication-browser.png"
  alt="Screenshot"/></div>

<p>Congratulations! You've created your first web application using GWT.
Since you've compiled the project, you're now running pure JavaScript
and
HTML that works in IE, Chrome, Firefox, Safari, and Opera. You could now deploy
your application to production by serving the HTML and JavaScript files in your
<code>MyWebApp/war/</code> directory from your web servers.</p>



<h2 id="deploying">Deploy to App Engine</h2>
<p>
  Using the plugin, you can also easily deploy GWT projects to Google App
  Engine. If you installed the App Engine for Java SDK when you installed the plugin,
  you can now right-click on the project and App Engine "enable" it by choosing
  <b>Google</b> &gt; <b>App Engine Settings</b>. Check the box marked <b>Use
    Google App Engine</b>. This will add the necessary configuration files to
  your project.
</p>
<p>
  To deploy your project to App Engine, you first need to create an application
  ID from the <a href="https://appengine.google.com/">App Engine Administration Console</a>.
</p>

<p>
  Once you have an application ID, right-click on your project, and
  select <b>Google &gt; App Engine Settings...</b> from the context menu.  Enter
  your application ID into the <b>Application ID</b> text box. Click <b>OK</b>.
</p>

<p>
  Right-click on your project and select <b>Google &gt; Deploy to App Engine</b>.
  In the resulting <b>Deploy Project to Google App Engine</b> dialog, enter your
  Google Account email and password.
</p>

<p>
  Click <b>Deploy</b>.
</p>

<p>
Congratulations! You now have a new web application built with GWT live on the web at <code>http://<i>application-id</i>.appspot.com/</code>.
</p>


