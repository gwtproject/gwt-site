<link href="https://www.google.com/css/modules/buttons/g-button.css" rel="stylesheet" type="text/css" />

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
  vertical-align: middle;
  border: none;
}
</style>



<ul class="toc">
  <li><a href="#prereqs">Prerequisites</a></li>
  <li><a href="#download">Download and Install GWT</a></li>
  <li><a href="#create">Create your first web application</a></li>
  <li><a href="#run">Run locally in development mode</a></li>
  <li><a href="#change">Make a few changes</a></li>
  <li><a href="#compile">Compile and run in production mode</a></li>
  <li><a href="#setup">Set up an IDE</a></li>
</ul>


<h2 id="prereqs">Prerequisites</h2>

<ol class="instructions">
  <li>
    <div class="header">You will need the Java SDK version 1.6 or later. If necessary, <a
    href="http://java.sun.com/javase/downloads/" rel="nofollow">download and
    install the Java SE Development Kit (JDK)</a> for your platform. Mac users,
    see <a href="http://developer.apple.com/java/">Apple's Java developer
    site</a> to download and install the latest version of the Java Developer
    Kit available for Mac OS X.</div>
  </li>
  <li>
    <div class="header">Apache Ant is also necessary to run command line arguments. If
    you don't already have it, install <a href="http://ant.apache.org/" rel="nofollow">Apache Ant</a>.</div>
  </li>

</ol>
<p class="note">If you have problems running Ant on the Mac, try setting the
$JDK_HOME environment variable with export JDK_HOME="/Library/Java/Home"</p>

<h2 id="download">Download and Install the GWT SDK</h2>

<table class="download">
  <tbody><tr>
    <td>
      <img src="images/sdk-sm.png" style="float: left; width: 80px; height:
      80px;" />
    </td>
    <td>
      <p>
        Download and unzip the GWT SDK. This contains the core
        libraries, compiler, and development server that you need to write web
        applications.
      </p>
      <p>
        See <a href="doc/latest/FAQ_GettingStarted.html">FAQ</a>
        for system and browser requirements.
      </p>
    </td>
    <td class="moreinfo" style="vertical-align:top">
      <div class="g-button">
        <div><span><span>
          <a href="download.html">Download GWT SDK</a>
        </span></span></div>
      </div>
      <br/>
    </td>
  </tr>
</tbody></table>


<div class="details">On Windows, extract the files from the compressed folder <code>gwt-2.6.1.zip</code>.  On Mac or Linux, you can unpack the package with a command like:
<pre class="code">unzip gwt-2.6.1.zip</pre></div>

<p>
The GWT SDK doesn't have an installer application.  All the files you  need to
run and use the SDK are located in the extracted directory.
</p>


<h2 id="create">Create your first web application </h2>

<p>GWT ships with a command line utility called <code><a href="doc/latest/RefCommandLineTools.html#webAppCreator">webAppCreator</a></code> that automatically generates all the files you'll need in order to start a GWT project.  It also generates <a href="http://www.eclipse.org/" rel="nofollow">Eclipse</a> project files and launch config files for easy debugging in GWT's development mode.</p>

<p>You can create a new demo application in a new MyWebApp directory by running <code>webAppCreator</code>:
</p>

<ul>
<li><b>Windows</b>

<pre class="code">
cd gwt-2.6.1

webAppCreator -out MyWebApp com.mycompany.mywebapp.MyWebApp
</pre>
</li>

<li><b>Mac or Linux</b> - you may need to make the script executable:

<pre class="code">
cd gwt-2.6.1

chmod u+x webAppCreator

./webAppCreator -out MyWebApp com.mycompany.mywebapp.MyWebApp
</pre>
</li>
</ul>

<p>The <code>webAppCreator</code> script will generate a number of files in
<code>MyWebApp/</code>, including some basic &quot;Hello, world&quot;
functionality in the class
<code>MyWebApp/src/com/mycompany/mywebapp/client/MyWebApp.java</code>.  The
script also generates an Ant build script <code>MyWebApp/build.xml</code>.</p>


<h2 id="run">Run locally in development mode</h2>
<p>To run your newly created application in development mode:

<pre class="code">
cd MyWebApp/

ant devmode
</pre>

<p>
  This command starts GWT's development mode server, a local server used for
  development and debugging, as follows:
</p>

<div class="screenshot"><a href="images/myapplication-devmode.png"><img src="images/myapplication-devmode.png" alt="Screenshot" width="35%"/></a></div>

<p>
  Launch the local server in a browser by either 1) clicking "Launch Default Browser"
  or 2) clicking "Copy to Clipboard" (to copy its URL), then pasting into Firefox, Internet Explorer,
  Chrome, or Safari. Since this is your first time
  hitting the development mode server, it will prompt you to install the GWT
  Developer Plugin. Follow the instructions in the browser to install the plugin, which
  may require restarting the browser.
</p>
<p>
  Once the GWT Developer Plugin is installed in your browser, navigate to
  the URL again and the starter application will load in development mode, as follows:.
</p>
<div class="screenshot"><a href="images/myapplication-browser.png"><img src="images/myapplication-browser.png" alt="Screenshot" width="35%"/></a></div>


<h2 id="change">Make a few changes</h2>
<p>The source code for the starter application is in the
<code>MyWebApp/src/</code> subdirectory, where MyWebApp is the name you gave to
the project above. You'll see two packages,
<code>com.mycompany.mywebapp.client</code> and
<code>com.mycompany.mywebapp.server</code>. Inside the client package is code that will eventually be compiled to JavaScript and run as client code in the browser. The java files in the server package will be run as Java bytecode on a server, in the case of this Quick Start on the App Engine servers.</p>

<p>
  Look inside <code>com/mycompany/mywebapp/client/MyWebApp.java</code>. Line 41 constructs the "Send" button.
</p>

<pre class="code">final Button sendButton = new Button(&quot;Send&quot;);</pre>

<p>
  Change the text from &quot;Send&quot; to &quot;Send to Server&quot;.
</p>

<pre class="code">final Button sendButton = new Button(&quot;Send to Server&quot;);</pre>


<p>Now, save the file and simply click "Refresh" in your browser to see your change. The button should now say &quot;Send to Server&quot; instead of &quot;Send&quot;:</p>


<h2 id="compile">Compile and run in production mode</h2>

<p>To run the application as JavaScript in what GWT calls "production mode", compile the application by executing:

<pre class="code">ant build</pre></p>

<p>The "build" Ant target invokes the GWT compiler which generates a number of
JavaScript and HTML files from the MyWebApp Java source code in the
<code>MyWebApp/war/</code> subdirectory.  To see the application, open the file
<code>MyWebApp/war/MyWebApp.html</code> in your web browser.  The application
should look identical to the development mode above.</p>

<p>Congratulations! You've created your first web application using GWT.
Since you've compiled the project, you're now running pure JavaScript and
HTML that works in IE, Chrome, Firefox, Safari, and Opera. You could now deploy
your application to production by serving the HTML and JavaScript files in your
<code>MyWebApp/war/</code> directory from your web servers.</p>

<h2 id="setup">Set up an IDE</h2>
<p>Now that you've created your first app, you probably want to do something a
bit more interesting. But first, if you normally work with an IDE you'll want to
set up Eclipse to use the GWT SDK:
</p>

<p><a href="usingeclipse.html">Set up Eclipse</a></p>

<p>
If you are going to stick with the command line, check out Speed Tracer	 and then
head over to <a href="doc/latest/tutorial/gettingstarted.html">Build a Sample GWT App</a>.
</p>
