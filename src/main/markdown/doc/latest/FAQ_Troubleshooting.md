<ol class="toc" id="pageToc">
  <li><a href="#Compiler">Compiler</a>
  <ol class="toc">
    <li><a href="#AssertionError_:_Element_may_only_be_set_once">AssertionError: Element may only be set once</a></li>
    <li><a href="#The_import_cannot_be_resolved">The import cannot be resolved</a></li>
    <li><a href="#InternalError_:_Can't_connect_to_X11_window">InternalError: Can't connect to X11 window</a></li>
    <li><a href="#OutOfMemoryError_:_Java_heap_space">OutOfMemoryError: Java heap space</a></li>
    <li><a href="#Undefined_DISPLAY_errors">Undefined DISPLAY errors</a></li>
  </ol>
  </li>
  <li><a href="#RPC">RPC</a>
  <ol class="toc">
    <li><a href="#Resource_not_found">Resource not found</a></li>
    <li><a href="#ServletException_:_Content-Type_must_be_'text/plain'">ServletException: Content-Type must be 'text/plain'</a></li>
  </ol>
  </li>
  <li><a href="#History">History</a>
  <ol class="toc">
    <li><a href="#GWT_History_feature_is_broken_in_Safari_2.0">GWT History feature is broken in Safari 2.0</a></li>
  </ol>
  </li>
  <li><a href="#Hosted_Mode">Development Mode</a>
  <ol class="toc">
    <li><a href="#Failed_to_getGlobalExecState">Failed to getGlobalExecState</a></li>
    <li><a href="#Resource_not_found">Resource not found</a></li>
    <li><a href="#RuntimeException_:_Installation_problem_detected,_please_reinsta">RuntimeException: Installation problem detected, please reinstall GWT</a></li>
    <li><a href="#Unable_to_find_type_'com.foo.client._MyApp_'">Unable to find type 'com.foo.client.MyApp'</a></li>
    <li><a href="#Invalid_memory_access_of_location">Invalid memory access of location 00000000 rip=01160767, or similar</a></li>
    <li><a href="#Navigation_to_the_page_was_canceled">Navigation to the page was canceled</a></li>
    <li><a href="#AccessControlException_access_denied">java.security.AccessControlException: access denied</a></li>
  </ol>
  </li>
  <li><a href="#Image_Bundles">Image Bundles</a>
  <ol class="toc">
    <li><a href="#ImageBundle_images_not_showing_in_Internet_Explorer">ImageBundle images not showing in Internet Explorer</a></li>
  </ol>
  </li>
  <li><a href="#Internationalization">Internationalization</a>
  <ol class="toc">
    <li><a href="#International_characters_don't_display_correctly">International characters don't display correctly</a></li>
  </ol>
  </li>
  <li><a href="#JUnit_Testing">JUnit Testing</a>
  <ol class="toc">
    <li><a href="#JUnit_tests_fail_on_Mac">JUnit tests fail on Mac</a></li>
  </ol>
  </li>
</ol>


<h2 id="Compiler">Compiler</h2>

<h3 id="AssertionError_:_Element_may_only_be_set_once">AssertionError: Element may only be set once</h3>

<p>After upgrading to GWT 1.5, you might see this exception:</p>


<pre>
[ERROR] Uncaught exception escaped
java.lang.AssertionError: Element may only be set once
</pre>


<p>The message indicates that some widget is calling the setElement() method more than once. Trying to call the setElement() method on a widget after it's already been initialized
is illegal.</p>

<p>This assertion was loosened in GWT 1.4 but is back in GWT 1.5. Setting a widget's element to an arbitrary new element would break all sorts of assumptions made by most widgets;
that is, it makes it impossible for a widget to make any assumptions about the stability of its underlying element. If the element can change arbitrarily, all kinds of things can
go wrong.</p>

<p>Calling the setElement() method more than once almost always happens unintentionally (for example, extending a widget that calls setElement() in its constructor then calling it
again from the subclass constructor). Thus the message generally reveals an actual problem that was going unnoticed.</p>

<h4>Workaround</h4>

<p>Rather than write a new widget from scratch, here's a shortcut for constructing a widget from an existing DOM element.</p>

<p>First subclass <a href="/javadoc/latest/com/google/gwt/user/client/ui/AbsolutePanel.html">AbsolutePanel</a>. Then use
the protected <a href="/javadoc/latest/com/google/gwt/user/client/ui/AbsolutePanel.html#AbsolutePanel(com.google.gwt.user.client.Element)">AbsolutePanel(Element)</a> constructor to change the underlying element.</p>

<h3 id="The_import_cannot_be_resolved">The import cannot be resolved</h3>

<p>Although you have successfully compiled the project class files in an IDE, when invoking the GWT compiler for the first time on a new project using one of the APIs provided
with the GWT Google API, you come across an error similar to the following:</p>


<pre>
$ ./gearsTest-compile
Analyzing source in module 'com.example.gearsTest'
   [ERROR] Errors in '/Users/zundel/Documents/workspace2/galgwt-issue3/src/com/example/client/gearsTest.java'
      [ERROR] Line 9:  The import com.google.gwt.gears cannot be resolved
      [ERROR] Line 26:  Gears cannot be resolved
</pre>


<p>The problem is that there is no package <tt>com.google.gwt.gears</tt> on the module source path.</p>

<h4>Workaround</h4>

<p>This can be resolved by updating the module XML file (.gwt.xml) in either of two ways.</p>

<ul>
<li>Add a <tt>&lt;</tt>source<tt>&gt;</tt> tag that adds the package to the module source path.</li>

<li>If your third party library provides one, add an <tt>&lt;</tt>inherits<tt>&gt;</tt> tag.</li>
</ul>

For using the Gears package in GWT Google API, the appropriate line is: 

<pre class="prettyprint">
   &lt;inherits name='com.google.gwt.gears.Gears'&gt;
</pre>

<h3 id="InternalError_:_Can't_connect_to_X11_window">InternalError: Can't connect to X11 window</h3>

<p>Does the GWT compiler need an X11 Window in Linux? No, the GWT compiler can run &quot;headless&quot; (that is, accessing the AWT library without needing to load a Graphics Environment
Window).</p>

<p>You might run into the problem in the first place because the <a href="DevGuideUiImageBundles.html">ImageBundle</a> feature triggers the GWT compiler to
connect to an X11 Graphics Environment Window at compile time. If you don't have a DISPLAY environment variable set, it will issue the following error message:</p>

<pre>
java.lang.InternalError: Can't connect to X11 window server using ':0.0' as the value of the DISPLAY variable.
</pre>

<p>Even if you are not using the ImageBundle explicitly in your client code, you may still encounter this error message because GWT uses the ImageBundle internally for widgets
(like the Tree widget) to further optimize the number of HTTP roundtrips for images and speed up your web application. This means if you're using any widgets that use ImageBundle
in their underlying implementations, the GWT compiler will search for the DISPLAY environment variable and try to connect to an X11 Graphics Window. 
</p>

<h4>Workaround</h4>

<p>To avoid this error message, run the GWT compiler with the headless AWT option.</p>

<pre>
-Djava.awt.headless=true
</pre>

<h5>Examples</h5>

<p><strong>Ant.</strong> To the GWT compilation build target, add the element: <tt>&lt;jvmarg value=&quot;-Djava.awt.headless=true&quot;/&gt;</tt></p>

<p><strong>Command-line.</strong> Pass in the argument: <tt>-Djava.awt.headless=true</tt></p>

<p><strong>Eclipse.</strong> In your Run Configuration window, select the Arguments tab and in the VM arguments section, enter: <tt>-Djava.awt.headless=true</tt></p>

<p>If you use a custom build process, you can also set the AWT headless option there.</p>


<h3 id="OutOfMemoryError_:_Java_heap_space">OutOfMemoryError: Java heap space</h3>

<p>When compiling a project in GWT 1.5 or later, you may encounter problems with running out of memory.</p>


<pre>
Compiling permutations
   Analyzing permutation #1
      [ERROR] An internal compiler exception occurred
com.google.gwt.dev.jjs.InternalCompilerException: Unexpected error during visit.
        at ...
Caused by: java.lang.OutOfMemoryError: Java heap space
</pre>


<p>GWT 1.5 provides a larger library (in gwt-user.jar) that must be analyzed during compilation and a number of compiler optimizations that require more memory and processing
power. Thus, the GWT compiler and development mode use more memory internally. This can cause the compiler to exceed the default JVM heap size.</p>

<p>Some projects need to increase JVM max memory to successfully compile; others may see improved compile times by increasing the limit.</p>

<h4>Workaround</h4>


<p>Increase the Java heap limit using the Java VM argument -Xmx and specifying the amount of memory in megabytes. For example, <tt>java -Xmx512M -cp [args...]</tt> would set the
heap max to 512 megabytes.</p>



<h3 id="Undefined_DISPLAY_errors">Undefined DISPLAY errors</h3>

<p>You might receive undefined DISPLAY errors when compiling or testing in a console or headless environment.</p>

<p>In a UNIX environment, GWT tests (and in some cases the GWT compiler) require an X display. If the continuous build is being run from a cron job, the running process may not
have access to a &quot;real&quot; display.</p>

<h4>Workaround</h4>

<p>You can provide a virtual display by using a program named Xvfb. (Xvfb stands for X11 Virtual FrameBuffer.)</p>

<p>To provide a virtual display:</p>

<ol>
<li>Install Xvfb.</li>

<li>Start the Xvfb server and set the environment variable DISPLAY to use it.</li>
</ol>



<pre>
export DISPLAY=:2

# Run Xvfb without access control on display :2  

# This command will only start Xvfb if it is not already started.
# Any GWT compiles and tests should set DISPLAY=:2 to use the virtual frame buffer.
ps -ef | grep Xvfb | grep -v grep &gt;&gt; /dev/null || Xvfb :2 -ac &amp;
</pre>

<h2 id="RPC">RPC</h2>


<h3 id="Resource_not_found">Resource not found</h3>

<p>When testing a new RPC interface in development mode, receive a &quot;resource not found&quot; error.</p>

<pre>
The development shell servlet received a request for 'rpcs/myService' in module 'com.example.RPCExample' 
   Resource not found: rpcs/myService
</pre>

<p>The servlet engine could not find the server-side definition of your RPC method.</p>

<h4>Troubleshooting checklist</h4>

<ul>
<li>Is the server side code defined and in the appropriate directory?</li>
</ul>

<blockquote>By convention, services are located in the <i>server</i> directory in the <a href="DevGuideOrganizingProjects.html#DevGuideDirectoriesPackageConventions">standard project
structure.</a></blockquote>

<ul>
<li>Is the server code path listed in the module XML file (<i>myApp</i>.gwt.xml)?</li>

<li style="list-style: none">
<pre class="prettyprint">
&lt;!-- Example servlet loaded into development mode web server       --&gt;
&lt;servlet path=&quot;/myService&quot; class=&quot;com.example.server.MyServiceImpl&quot; /&gt;
</pre>
</li>
</ul>

<ul>
<li>Have you complied the server side code?</li>
</ul>

<blockquote>
The ant build scripts created by the <a href="RefCommandLineTools.html#webAppCreator">webAppCreator tool</a> should by default compile all your source files into bytecode, but it's a good idea to double-check.
</blockquote>

<ul>
<li>Are the compiled server classes are available to development mode?</li>
</ul>

<blockquote>If your server classes are in a separate jar file or directory tree,
when launching the development mode shell you may need to modify the classpath (for example, by editing
the ant build file).</blockquote>


<h3 id="ServletException_:_Content-Type_must_be_'text/plain'">ServletException: Content-Type must be
'text/plain'</h3>

<p>If you are using GWT RPC and upgrading from GWT 1.4 to GWT 1.5, you may see a stack trace similar to the following:</p>

<pre>
javax.servlet.ServletException: Content-Type must be 'text/plain' with 'charset=utf-8' (or unspecified charset)
        at com.google.gwt.user.server.rpc.RemoteServiceServlet.readPayloadAsUtf8(RemoteServiceServlet.java: 119)
        at com.google.gwt.user.server.rpc.RemoteServiceServlet.doPost(RemoteServiceServlet.java: 178)
        at javax.servlet.http.HttpServlet.service(HttpServlet.java: 738)
        at javax.servlet.http.HttpServlet.service(HttpServlet.java: 831)
        ...
</pre>

<p>In GWT 1.5, the RPC content type was switched from &quot;text/plain&quot; to &quot;text/x-gwt-rpc&quot;.</p>

<p>If you receive this exception, then your server is still using a GWT 1.4 gwt-servlet.jar.</p>

<h4>Workaround</h4>

<p>Update your server to use the GWT 1.5 gwt-servlet.jar.</p>

<h2 id="History">History</h2>


<h3 id="GWT_History_feature_is_broken_in_Safari_2.0">GWT History feature is broken in Safari 2.0</h3>

<ul>
<li>Q: The GWT History feature is great, but it has been driving me bananas on Safari 2.0. What's the history between GWT and Safari 2.0?</li>

<li>A: Unfortunately, the GWT History system does not play well with Safari 2.0.</li>
</ul>

<p>A number of strange bugs in Safari 2, which are particularly difficult to work around, make it hard to use the same tricks GWT uses for history support in IE and Firefox. Some
messy solutions do exist but these fail in a number of significant edge cases and are not likely to be good long-term solutions.</p>

<p>The current implementation provides fall-back functionality for Safari 2 so that the library works but no history entries are generated.</p>

<p>Although the current GWT solution for history support doesn't completely work on Safari 3 beta, it has been tested on the latest nightly WebKit builds and is looking good.</p>

<p>So, hold on to your hats! History support for Safari will work properly when Safari 3 is officially released.</p>

<h2 id="Hosted_Mode">Development Mode</h2>

<h3 id="Failed_to_getGlobalExecState">Failed to getGlobalExecState</h3>

<p>On Mac when launching development mode, receive the error &quot;Failed to getGlobalExecState&quot;.</p>

<p>This problem is related to the Google Gears enabler.</p>

<h4>Workaround</h4>

<p>Check the /Library/InputManagers folder. If there is a file named GearsEnabler.bundle, removing it should fix this issue.</p>

<h3 id="RuntimeException_:_Installation_problem_detected,_please_reinsta">RuntimeException:
Installation problem detected, please reinstall GWT</h3>

<p>When launching the GWT compiler or development mode, you may see the following exception.</p>

<pre>
Exception in thread &quot;main&quot; java.lang.ExceptionInInitializerError
Caused by: java.lang.RuntimeException: Installation problem detected, please reinstall GWT
    at com.google.gwt.util.tools.Utility.computeInstallationPath(Utility.java:322)
    at com.google.gwt.util.tools.Utility.getInstallPath(Utility.java:223)
    at com.google.gwt.util.tools.ToolBase.&lt;clinit&gt;(ToolBase.java:55)
Caused by: java.io.IOException: Cannot determine installation directory; apparently not running from a jar
    at com.google.gwt.util.tools.Utility.computeInstallationPath(Utility.java:307)
  ...
</pre>

<p>This message occurs when the runtime binary installation of GWT cannot be located. In general, the runtime binary installation path is expected to be in the same directory
where the gwt-dev-<tt>&lt;</tt>platform<tt>&gt;</tt>.jar file is located.</p>

<p>The GWT installation path is embedded in the scripts (created from applicationCreator and projectCreator) as the path specified to the
gwt-dev-<tt>&lt;</tt>platform<tt>&gt;</tt>.jar file. If you look in this directory, it should contain not only the gwt-user.jar and gwt-dev-<tt>&lt;</tt>platform<tt>&gt;</tt>.jar
files, but also the platform-specific shared library files, such as libgwt_ll.so or libgwt_ll.jnilib.</p>

<h4>Workaround</h4>

<p>Make sure that the path specified to the gwt-dev-<tt>&lt;</tt>platform<tt>&gt;</tt>.jar file on the Java classpath still contains the shared libraries. If not, you may need to
re-download and unpack the GWT distribution, making sure your download is complete.</p>

<h5 id="Note_to_contributors">Note to contributors</h5>

<p>If you are a contributor and checked out the source code via svn, you might encounter this problem if you are running GWT from source.</p>

<p>Make sure you have:</p>

<ul>
<li>a binary distribution unpacked or a compiled distribution ready</li>

<li>set the system property <i>gwt.devjar</i> configured in your launch configurations as specified in the eclipse/README.txt file.</li>
</ul>


<h3 id="Unable_to_find_type_'com.foo.client._MyApp_'">Unable to find type 'com.foo.client.MyApp'</h3>

<p>When running in development mode, receive the error: Unable to find type 'com.foo.client.MyApp'.</p>

<pre>
Starting HTTP on port 8888
Finding entry point classes
  Unable to find type 'com.google.gwt.sample.stockwatcher.client.StockWatcher'
    Hint: Check that the type name 'com.google.gwt.sample.stockwatcher.client.StockWatcher' is really what you meant
    Hint: Check that your classpath includes all required source roots
Failure to load module 'com.google.gwt.sample.stockwatcher.StockWatcher'   
</pre>

<p>This problem is most often seen when creating a launch configuration manually.  The <a href="//developers.google.com/eclipse">Google Plugin for Eclipse</a> handles adding source roots to the classpath automatically.</p>

<p>One of the most common problems when creating launch configurations manually in GWT is to omit the application source code path from the Java classpath. The <a href="DevGuideCompilingAndDebugging.html#DevGuideJavaToJavaScriptCompiler">GWT compiler</a> and <a href="DevGuideCompilingAndDebugging.html#DevGuideDevMode">development mode</a> shell rely on the module
source code to build your application, and both use the Java classpath to find the .java source files.</p>

<h4>Workaround</h4>

<p>If you are using Eclipse, open the launch configuration, navigate to the Classpath tab, and use the Advanced button to add the <tt>src</tt> folders to the classpath.</p>

<p>If you are launching from the command line, add the <tt>src</tt> folders to the list of paths following the <tt>-cp</tt> argument to the Java command.</p>

<h3 id="Invalid_memory_access_of_location">Invalid memory access of location 00000000 rip=01160767, or similar</h3>

<p>If you're using a version of GWT prior to 2.0 on the Mac platform along with Java 6, you're likely seeing this error message when you try running hosted mode. This problem no longer exists in GWT 2.0's <a href="DevGuideCompilingAndDebugging.html#DevGuideDevMode">Development Mode</a>.</p>

<p>The reason why this is occurring is because hosted mode uses 32-bit SWT bindings, which are tightly coupled with Carbon on the Mac platform.. Unfortunately, Java 6 on the Mac uses a 64-bit JVM, and  64-bit support for Carbon is currently unavailable. This means that in order to use GWT hosted mode on the Mac, you'll need to stick to Java 5 for 32-bit JVM support.</p>

<h4>Workaround</h4>

<p>For any other platform, using a 64-bit JVM will result in a similar error message when trying to start hosted mode. Fortunately, Java 6 comes in 32-bit flavors for these platforms, and so you can continue using the 64-bit JVM for GWT compilation and use the 32-bit JVM for hosted mode.</p>

<h3 id="Navigation_to_the_page_was_canceled">Navigation to the page was canceled</h3>

<p>Some developers have experienced this issue running GWT 1.6 hosted mode on Windows Vista. The reason why this is occurring is because for many applications and services running on Windows Vista, the IPv6 convention is used, including the endpoint IP address defined for localhost.</p>

<h4>Workaround</h4>

<p>If you take a peek at your <tt>C:\Windows\System32\drivers\etc\hosts</tt> file, you will notice the endpoint <tt>::1 localhost</tt> defined in the hosts file. To have localhost working in the IPv4 case, which is used in hosted mode, simply change this line to read <tt>127.0.0.1 localhost</tt>.</p>

<h3 id="AccessControlException_access_denied">java.security.AccessControlException: access denied</h3>

<p>If you created your application using the <a href="https://developers.google.com/eclipse">Google Plugin for Eclipse</a>, you may have encountered the following stack trace:</p>

<pre class="code">
WARNING: Nested in java.lang.ExceptionInInitializerError:
java.security.AccessControlException: access denied
(java.lang.RuntimePermission modifyThreadGroup)
       at java.security.AccessControlContext.checkPermission(AccessControlContext.java:323)
       at java.security.AccessController.checkPermission(AccessController.java:546)
       at java.lang.SecurityManager.checkPermission(SecurityManager.java:532)
       at com.google.appengine.tools.development.DevAppServerFactory
</pre>

<p>The problem occurs if you mistakenly enabled the <a href="https://developers.google.com/appengine">Google App Engine</a> feature for your project when you created the project, or at a later time during development, and have server-side libraries or classes packaged with your GWT application war folder that violate the <a href="//developers.google.com/appengine/docs/whatisgoogleappengine">Java App Engine sandbox</a>.</p>

<h4>Workaround</h4>

<p>If you don't intend on using Google App Engine for your project, simply remove the Google App Engine functionality from it following the steps below:</p>

<ol>
  <li>In Eclipse, right-click on your project and select <b>Google &gt; App Engine Settings...</b></li>
  <li>In the App Engine property menu, deselect the <b>Use Google App Engine</b> checkbox.</li>
  <li>Click <b>OK</b>.</li>
</ol>

<p>If you intend to use Google App Engine, you will need to remove any use of server-side libraries that are violating the security constraints of the App Engine sandbox from your project. For more information on the support available in the App Engine sandbox, check out the <a href="//developers.google.com/appengine/docs">App Engine documentation.</a></p>

<h2 id="Image_Bundles">Image Bundles</h2>


<h3 id="ImageBundle_images_not_showing_in_Internet_Explorer">ImageBundle images not showing in Internet
Explorer</h3>

<p>ImageBundle images show up in Firefox, Opera and Safari, but aren't showing up in Internet Explorer.</p>

<h4 id="Security">Security</h4>

<p>The first thing to verify is whether or not your web application uses HTTPS, or if any of the images included in the generated image bundle file are under a security
constraint.</p>

<p>If that is the case, you may be running into a problem rooted in your web server setting certain response headers (such <tt>Pragma: No-cache, Expires: Thu, 1 Jan 1970
00:00:00,</tt> ...) and Internet Explorer respecting those headers when when using the HTTPS protocol.</p>

<h5>Workaround</h5>

<p>You can read more details about this security issue and how to work around it in the API Reference, <a href="/javadoc/latest/com/google/gwt/user/client/ui/ImageBundle.html">Image Bundles and the HTTPS Protocol</a>.</p>

<h4 id="Size">Size</h4>

<p>On the other hand, if you're not using HTTPS but are still running into this problem, check out the size of your image bundle file.</p>

<p>A number of developers have run into the problem of Internet Explorer consuming large amounts of memory when extracting images out of a larger image bundle, and subsequently
failing to properly display the images.</p>

<h5>Workaround</h5>

<p>You might want to consider splitting up one large image bundle into several smaller bundles. This technique has worked for most developers who have experienced this
problem.</p>

<h2 id="Internationalization">Internationalization</h2>


<h3 id="International_characters_don't_display_correctly">International characters don't display correctly</h3>

<p>If you've embedded international characters into Java source files, these characters might not display correctly when you run the application and view it in a web browser.</p>

<p>Java requires UTF-8 for source code files containing international characters. Frequently the editor used to generate the files (such as an IDE) is not configured to save the
files as UTF-8. Thus your source files are not properly encoded in UTF-8.</p>

<h4>Troubleshooting checklist</h4>

<ul>
<li>Check your editor to see if it is capable of saving files as UTF-8 and that it is currently configured to do so.</li>

<li>If you have embedded international characters directly into your Java source files, check to see if they are saved in UTF-8 format.</li>

<li>Check any associated Java properties files (or other files where you've stored translations) to see if they are saved in UTF-8 format.</li>

<li>Check the HTML host page. If your web page content contains localized data, encode it as UTF-8 by adding the following tag to the <tt>&lt;</tt>head<tt>&gt;</tt> element.</li>
</ul>

<pre class="prettyprint">
&lt;meta http-equiv=&quot;content-type&quot; content=&quot;text/html;charset=utf-8&quot; /&gt;`
</pre>

<h2 id="JUnit_Testing">JUnit Testing</h2>


<h3 id="JUnit_tests_fail_on_Mac">JUnit tests fail on Mac</h3>

<p>Some JUnit tests fail on the Mac platform.</p>

<p>When the Safari/Webkit web browser runs in headless mode it is able to optimize some rendering operations. One result of these optimizations is that the position or size of
some elements may be different when running a unit test than when running an actual application. A common symptom is to see the position or size of an widget set to
<i>(0,0)</i>.</p>

<h4>Workaround</h4>

<p>To work around this problem, pass the <tt>-notHeadless</tt> argument in the <tt>-Dgwt.args=</tt> system property setting. Note: When using the ant build, this argument is
enabled by default as of the GWT 1.5 release.</p>

<p>When <tt>-notHeadless</tt> is specified for running unit tests, the log window and hosted mode browser will pop up on the screen while the test is running. Dismissing or
manipulating the elements in these windows may cause the unit tests to fail.</p>