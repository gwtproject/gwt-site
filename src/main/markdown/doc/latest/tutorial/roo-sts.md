

<p>
<div style="font-weight: bold; font-size: 120%; padding-below: 2em;">
Note: this document is likely to be outdated and irrelevant
Getting started with GWT, Spring Roo, and SpringSource Tool Suite</div>
</p>

<p>
One of the main features of GWT 2.1 is the integration with SpringSource
developer tools. This collaborative effort is focused on making it easy to
develop rich web apps for business, bringing together
<a href="/">GWT</a>,
<a href="http://www.springsource.org/roo">Spring Roo</a>,
and <a href="http://www.springsource.com/products/sts">SpringSource Tool Suite
(STS)</a>.
</p>

<p>
GWT 2.1 introduces a new set of <a href="../ReleaseNotes.html#CellWidgets">cell widgets</a> that were carefully designed to navigate large data sets efficiently, as well as an <a href=../ReleaseNotes.html#MvpFramework">app framework</a> that makes it simple to connect the new widgets to data from the cloud. This app framework also includes:<br>
</p>

<ul>
  <li><!--a href="/web-toolkit/doc/latest/ReleaseNotes#Editors"-->Data bound views<!--/a--> - With GWT 2.1's Data Editors, developers can create views that are generated from their app's data model. These views are completely customizable, and handle all of the nasty work of syncing change sets between the client and server.</li>
  <li><a href="../ReleaseNotes.html#RequestFactory">Highly optimized communication layer</a> - The new communication layer, RequestFactory, aims to improve both developer and application effiency. It minimizes the payloads being sent between client and server in order to make RPCs as fast as possible, while adding new code generators that build this communication layer based on your app's data model and the backend services it exposes. For many apps, the developer simply needs to provide server-side find and persist methods in order to get things rolling.</li>
  <li><a href="../ReleaseNotes.html#MvpFramework">MVP support</a> - For a while now, GWT developers have been using and advocating the <a href="http://en.wikipedia.org/wiki/Model-view-presenter">MVP design pattern</a>, and the Activities and Place support within GWT 2.1, are a set of components that formalize this pattern and the associated best practices, enabling efficient app navigation, history management, and view creation.</li>
</ul>

<p>
With Roo 1.1, you can create a functioning app from scratch in minutes with a few simple commands, and with all of the GWT tools — the Google Plugin for Eclipse, Speed Tracer, and the GWT and App Engine SDKs — directly available in STS, you have everything you need in a single development environment.
</p>

<p>
The following steps will help you get started with Spring Roo and STS:
</p>

<ol class="toc">
  <li>1. <a href="#install">Download and Install</a></li>
  <li>2. <a href="#build">Build a sample app using Roo</a></li>
  <li>3. <a href="#maven">Run app in Maven</a></li>
  <li>4. <a href="#sts">Run app in SpringSource ToolSuite</a></li>
</ol>


<h2 id="install">1. Download and Install</h2>

<p>
To get started you'll need the GWT 2.1 SDK and Roo 1.1. This will allow you to use Roo's code generation utilities to create an MVP-based GWT web app that harnesses the full capabilities of GWT 2.1.  While you can install these two tools piecemeal, the easiest way to install is to download The STS bundled installer.
</p>

<p class="note"><b>Note</b> - If you have problems with the new set of tools, or simply want to discuss
them with the larger community, the
<a href="http://groups.google.com/group/Google-Web-Toolkit">GWT Developer
Forum</a>  and the <a href="http://forum.springsource.org/forumdisplay.php?f=67">SpringSource
Roo forums</a> are a wealth of knowledge.
</p>

<p>
<b>Piecemeal installers</b>
</p>

<ul>
 <li>Download and install <a href="/download.html">GWT 2.1+ SDK</a></li>
 <li>Download and install <a href="http://www.springsource.org/download">Roo 1.1</a></li>
 <li>Download and install <a href="http://maven.apache.org/run-maven/index.html">Maven</a></li>
 <li>Download and install <a href="http://www.springsource.com/products/eclipse-downloads">STS</a> from the link on the right side
</ul>

<p style="margin-left: 1em;">
&ndash; or &ndash;
</p>

<p>
<b>Bundled installer</b>
</p>

<ul>
  <li>Download and install <a href="http://www.springsource.com/products/eclipse-downloads">SpringSource Tool Suite</a> bundled installer and dashboard mechanism, which includes STS, Roo, tc Server and Maven.  For Mac users, we recommend the Cocoa build.</a>

<p>
At the last step of this installation, launch it by checking the "Launch SpringSource Tool Suite" checkbox and clicking "Finish".  STS launches as follows:
</p>

<p>
<img src="images/spring-dashboard.png" alt="SpringSource dashboard tab" width="525px" height="207px" style="border: thin solid gray">
</p>

<p>
Now that STS is installed, you have access to Roo.  To install the Google developer tools, navigate to the dashboard page.  To install the GWT tools (including the SDK), you simply navigate to the Extensions tab at the bottom of the page.
</p>

<p>
Once you've clicked on the Extensions tab (as in the screenshot below), you should see an option to install the Google Plugin for Eclipse (GPE). Select it and then click "Install".  The next page also lets you install Google App Engine and Google Web Tookit SDK &mdash; install them (unless using Maven, which will already have installed them).
</p>

<p>
<img src="images/spring-extensions.png" alt="SpringSource extensions tab" width="691px" height="206px" style="border: thin solid gray">
</p>

<p>
Once installed, you have all of the tools you need.
</p>

</li>
</ul>

<h2 id="build">2. Build a sample app using Roo</h2>
<p>
Now let's start building an app. The first step is to start up Roo's command line environment. You'll step out of STS to do this, but don't worry in a few steps you'll see how Roo interacts with STS once you've created your app.</p>

<p>
To launch Roo, open up a terminal window and navigate to the directory where you installed the SpringSource tools (let's say ~/springsource). Here you'll see a directory for Roo. Move into that directory, and then create an "expenses" directory to hold the sample app:
</p>

<pre>
$ cd ~/springsource/roo-1.1.0.RELEASE/
$ mkdir expenses
$ cd expenses
</pre>

<p>
You can then launch Roo:
</p>

<pre>
C:> ../bin/roo.bat                  # Windows
</pre>

<p>
or
</p>

<pre>
$ ../bin/roo.sh                   # Mac or Linux
</pre>

<p>
In either case you should see the following Roo prompt:
</p>

<p>
<img src="images/spring-prompt.png" alt="Roo command line prompt" width="600px" height="131px">
</p>

<p>
Roo offers a whole list of commands for building and maintaining apps (type "help" to see them).
With the latest release there is a sample script, expenses.roo, that makes it easy to build a GWT-based web app.
To run this, simply enter at the roo command line:
</p>

<pre>
roo&gt; script ../samples/expenses.roo
</pre>

<p>
This command creates a sample Expense Tracking app (extrack) with all of the necessary source (src/) and configuration files (pom.xml) in the current directory "expenses".
</p>

<p>
As with all Roo projects, you'll use Maven to manage dependencies next.  First quit Roo.
</p>

<pre>
roo&gt; quit
Roo exited with code 0
</pre>

<h2 id="maven">3. Run app in Maven</h2>

<p>
Use Maven to manage dependencies.  You can run Maven from the command line. To see this in action, first run Maven:
</p>

<pre>
$ mvn gwt:run
</pre>

<p>
Maven takes a few minutes to start up GWT's development server:
</p>

<p style="margin-left: 8px;">
<img src="images/spring-devserver.png" alt="GWT development server" width="60%">
</p>

<p>
Launch the app in your browser by clicking "Copy to Clipboard" and pasting that URL into your browser &mdash; in this case <a href="http://127.0.0.1:8888/ApplicationScaffold.html?gwt.codesvr=127.0.0.1:9997">http://127.0.0.1:8888/ApplicationScaffold.html?gwt.codesvr=127.0.0.1:9997</a>.
</p>

<p>
<img src="images/spring-databrowser.png" alt="Data browser screen" width="663px" height="404px">
</p>


<h2 id="sts">4. Run app in SpringSource ToolSuite</h2>

<p>
Now that you've created the sample app and launched it from the command line, you can import it into STS to start customizing it. To do this switch back to STS and click the menu item "File" -&gt; "Import", and then select "Maven" -&gt; "Existing Maven Projects", click "Next" and browse to the "expenses" directory containing pom.xml (from step 2), in our example ~/springsource/expenses, then click "Open".
</p>

<p>
<img src="images/spring-projects.png" alt="Project structure in Eclipse" width="381px" height="230px" style="border: thin solid gray">
</p>

<p>
This will import your project and all of its dependencies into STS, as well as applying the correct GWT and App Engine settings. The resulting project structure will look like the following.<br>
</p>

<p>
<img src="images/spring-package.png" alt="Selecting 'Existing Maven projects'" width="374px" height="319px">
</p>

<p>
Once imported you can now run your app directly from STS by right-clicking on the project and selecting the "Run As" -&gt; "Web Application".<br>
</p>

<p>
<img src="images/spring-runas.png" alt="Menu to choose Run as Web Application" width="419px" height="238px" style="border: thin solid gray">
</p>

<p>
Flip back to your browser from step 3 and reload the application.
</p>

<p>
With the app running, you can see the benefits of having these tools integrated. Let's say you want to start customizing the application by adding a "Mobile Number" field to the Employee data object. Typically this would require an update to the model, view, presenter/controller, and RPC layer. With GWT, Roo, and STS this becomes a simple change to the model, that is then propagated throughout your application by Roo, even when you're running in STS.
</p>

<p>
To make a simple change to the application, first make sure your app is still running in the browser, then:
</p>

<ol style="list-style-type: lower-alpha">
  <li>Go to STS</li>
  <li>Choose "Window" -&gt; "Show View" -&gt; "Roo Shell" to watch the logged changes in the next step
  <li>Open the Employee.java file in extrack/src/main/java, and add a mobileNumber field of type String.</li>
</ol>

<p>
As soon as you save the file, in the Roo Shell you'll see Roo pick up the changes and update the related components in your app.
</p>

<p>
<img src="images/spring-roo-shell.png" alt="Terminal shell showing updates to *.aj, *.java, and *.xml files">
</p>

<p>
Now flip back to your browser. After reloading the app, you'll see the changes you just made.
</p>

<p>
Roo picked up the change to your data model, propagated the changes throughout, and the Google Plugin for Eclipse compiled the resulting Java source into Javascript that is being run in the browser, and all you had to do was make a one line change. Tools handle all of the boilerplate code, letting you focus on bigger features and functionality.<br>
</p>