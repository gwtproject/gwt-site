FAQ - Troubleshooting
===

1.  [Compiler](#Compiler)
    1.  [AssertionError: Element may only be set once](#AssertionError_:_Element_may_only_be_set_once)
    2.  [The import cannot be resolved](#The_import_cannot_be_resolved)
    3.  [InternalError: Can't connect to X11 window](#InternalError_:_Can)
    4.  [OutOfMemoryError: Java heap space](#OutOfMemoryError_:_Java_heap_space)
    5.  [Undefined DISPLAY errors](#Undefined_DISPLAY_errors)
2.  [RPC](#RPC)
    1.  [Resource not found](#Resource_not_found)
    2.  [ServletException: Content-Type must be 'text/plain'](#ServletException_:_Content-Type_must_be_)
3.  [History](#History)
    1.  [GWT History feature is broken in Safari 2.0](#GWT_History_feature_is_broken_in_Safari_2.0)
4.  [Development Mode](#Hosted_Mode)
    1.  [Failed to getGlobalExecState](#Failed_to_getGlobalExecState)
    2.  [Resource not found](#Resource_not_found)
    3.  [RuntimeException: Installation problem detected, please reinstall GWT](#RuntimeException_:_Installation_problem_detected,_please_reinsta)
    4.  [Unable to find type 'com.foo.client.MyApp'](#Unable_to_find_type_)
    5.  [Invalid memory access of location 00000000 rip=01160767, or similar](#Invalid_memory_access_of_location)
    6.  [Navigation to the page was canceled](#Navigation_to_the_page_was_canceled)
    7.  [java.security.AccessControlException: access denied](#AccessControlException_access_denied)
5.  [Image Bundles](#Image_Bundles)
    1.  [ImageBundle images not showing in Internet Explorer](#ImageBundle_images_not_showing_in_Internet_Explorer)
6.  [Internationalization](#Internationalization)
    1.  [International characters don't display correctly](#International_characters_don)
7.  [JUnit Testing](#JUnit_Testing)
    1.  [JUnit tests fail on Mac](#JUnit_tests_fail_on_Mac)

## Compiler<a id="Compiler"></a>

### AssertionError: Element may only be set once<a id="AssertionError_:_Element_may_only_be_set_once"></a>

After upgrading to GWT 1.5, you might see this exception:

```text
[ERROR] Uncaught exception escaped
java.lang.AssertionError: Element may only be set once
```

The message indicates that some widget is calling the setElement() method more than once. Trying to call the setElement() method on a widget after it's already been initialized
is illegal.

This assertion was loosened in GWT 1.4 but is back in GWT 1.5. Setting a widget's element to an arbitrary new element would break all sorts of assumptions made by most widgets;
that is, it makes it impossible for a widget to make any assumptions about the stability of its underlying element. If the element can change arbitrarily, all kinds of things can
go wrong.

Calling the setElement() method more than once almost always happens unintentionally (for example, extending a widget that calls setElement() in its constructor then calling it
again from the subclass constructor). Thus the message generally reveals an actual problem that was going unnoticed.

#### Workaround

Rather than write a new widget from scratch, here's a shortcut for constructing a widget from an existing DOM element.

First subclass [AbsolutePanel](/javadoc/latest/com/google/gwt/user/client/ui/AbsolutePanel.html). Then use
the protected [AbsolutePanel(Element)](/javadoc/latest/com/google/gwt/user/client/ui/AbsolutePanel.html#AbsolutePanel-com.google.gwt.user.client.Element-) constructor to change the underlying element.

### The import cannot be resolved<a id="The_import_cannot_be_resolved"></a>

Although you have successfully compiled the project class files in an IDE, when invoking the GWT compiler for the first time on a new project using one of the APIs provided
with the GWT Google API, you come across an error similar to the following:

```text
$ ./gearsTest-compile
Analyzing source in module 'com.example.gearsTest'
   [ERROR] Errors in '/Users/zundel/Documents/workspace2/galgwt-issue3/src/com/example/client/gearsTest.java'
      [ERROR] Line 9:  The import com.google.gwt.gears cannot be resolved
      [ERROR] Line 26:  Gears cannot be resolved
```

The problem is that there is no package `com.google.gwt.gears` on the module source path.

#### Workaround

This can be resolved by updating the module XML file (.gwt.xml) in either of two ways.

*   Add a `<`source`>` tag that adds the package to the module source path.
*   If your third party library provides one, add an `<`inherits`>` tag.

For using the Gears package in GWT Google API, the appropriate line is:

```xml
<inherits name='com.google.gwt.gears.Gears'>
```

### InternalError: Can't connect to X11 window<a id="InternalError_:_Can't_connect_to_X11_window"></a>

Does the GWT compiler need an X11 Window in Linux? No, the GWT compiler can run "headless" (that is, accessing the AWT library without needing to load a Graphics Environment
Window).

You might run into the problem in the first place because the [ImageBundle](DevGuideUiImageBundles.html) feature triggers the GWT compiler to
connect to an X11 Graphics Environment Window at compile time. If you don't have a DISPLAY environment variable set, it will issue the following error message:

```text
java.lang.InternalError: Can't connect to X11 window server using ':0.0' as the value of the DISPLAY variable.
```

Even if you are not using the ImageBundle explicitly in your client code, you may still encounter this error message because GWT uses the ImageBundle internally for widgets
(like the Tree widget) to further optimize the number of HTTP roundtrips for images and speed up your web application. This means if you're using any widgets that use ImageBundle
in their underlying implementations, the GWT compiler will search for the DISPLAY environment variable and try to connect to an X11 Graphics Window. 

#### Workaround

To avoid this error message, run the GWT compiler with the headless AWT option.

```text
-Djava.awt.headless=true
```

##### Examples

**Ant.** To the GWT compilation build target, add the element: `<jvmarg value="-Djava.awt.headless=true"/>`

**Command-line.** Pass in the argument: `-Djava.awt.headless=true`

**Eclipse.** In your Run Configuration window, select the Arguments tab and in the VM arguments section, enter: `-Djava.awt.headless=true`

If you use a custom build process, you can also set the AWT headless option there.

### OutOfMemoryError: Java heap space<a id="OutOfMemoryError_:_Java_heap_space"></a>

When compiling a project in GWT 1.5 or later, you may encounter problems with running out of memory.

```text
Compiling permutations
   Analyzing permutation #1
      [ERROR] An internal compiler exception occurred
com.google.gwt.dev.jjs.InternalCompilerException: Unexpected error during visit.
        at ...
Caused by: java.lang.OutOfMemoryError: Java heap space
```

GWT 1.5 provides a larger library (in gwt-user.jar) that must be analyzed during compilation and a number of compiler optimizations that require more memory and processing
power. Thus, the GWT compiler and development mode use more memory internally. This can cause the compiler to exceed the default JVM heap size.

Some projects need to increase JVM max memory to successfully compile; others may see improved compile times by increasing the limit.

#### Workaround

Increase the Java heap limit using the Java VM argument -Xmx and specifying the amount of memory in megabytes. For example, `java -Xmx512M -cp [args...]` would set the
heap max to 512 megabytes.

### Undefined DISPLAY errors<a id="Undefined_DISPLAY_errors"></a>

You might receive undefined DISPLAY errors when compiling or testing in a console or headless environment.

In a UNIX environment, GWT tests (and in some cases the GWT compiler) require an X display. If the continuous build is being run from a cron job, the running process may not
have access to a "real" display.

#### Workaround

You can provide a virtual display by using a program named Xvfb. (Xvfb stands for X11 Virtual FrameBuffer.)

To provide a virtual display:

*   Install Xvfb.  
*   Start the Xvfb server and set the environment variable DISPLAY to use it.

```shell
export DISPLAY=:2
```
*   Run Xvfb without access control on display :2  

This command will only start Xvfb if it is not already started.
Any GWT compiles and tests should set DISPLAY=:2 to use the virtual frame buffer.

```shell
ps -ef | grep Xvfb | grep -v grep >> /dev/null || Xvfb :2 -ac &
```

## RPC<a id="RPC"></a>

### Resource not found<a id="Resource_not_found"></a>

When testing a new RPC interface in development mode, receive a "resource not found" error.

```text
The development shell servlet received a request for 'rpcs/myService' in module 'com.example.RPCExample' 
   Resource not found: rpcs/myService
```

The servlet engine could not find the server-side definition of your RPC method.

#### Troubleshooting checklist

*   Is the server-side code defined and in the appropriate directory?

> By convention, services are located in the _server_ directory in the [standard project
> structure.](DevGuideOrganizingProjects.html#DevGuideDirectoriesPackageConventions)

*   Is the server code path listed in the module XML file (_myApp_.gwt.xml)?
*

```xml
<!-- Example servlet loaded into development mode web server       -->
<servlet path="/myService" class="com.example.server.MyServiceImpl" />
```

*   Have you compiled the server-side code?

> The ant build scripts created by the [webAppCreator tool](RefCommandLineTools.html#webAppCreator) should by default compile all your source files into bytecode, but it's a good idea to double-check.

*   Are the compiled server classes are available to development mode?

> If your server classes are in a separate jar file or directory tree,
> when launching the development mode shell you may need to modify the classpath (for example, by editing
> the ant build file).

### ServletException: Content-Type must be 'text/plain'<a id="ServletException_:_Content-Type_must_be_'text/plain'"></a>

If you are using GWT RPC and upgrading from GWT 1.4 to GWT 1.5, you may see a stack trace similar to the following:

```text
javax.servlet.ServletException: Content-Type must be 'text/plain' with 'charset=utf-8' (or unspecified charset)
        at com.google.gwt.user.server.rpc.RemoteServiceServlet.readPayloadAsUtf8(RemoteServiceServlet.java: 119)
        at com.google.gwt.user.server.rpc.RemoteServiceServlet.doPost(RemoteServiceServlet.java: 178)
        at javax.servlet.http.HttpServlet.service(HttpServlet.java: 738)
        at javax.servlet.http.HttpServlet.service(HttpServlet.java: 831)
        ...
```

In GWT 1.5, the RPC content type was switched from "text/plain" to "text/x-gwt-rpc".

If you receive this exception, then your server is still using a GWT 1.4 gwt-servlet.jar.

#### Workaround

Update your server to use the GWT 1.5 gwt-servlet.jar.

##  History<a id="History"></a>

### GWT History feature is broken in Safari 2.0<a id="GWT_History_feature_is_broken_in_Safari_2.0"></a>

*   Q: The GWT History feature is great, but it has been driving me bananas on Safari 2.0. What's the history between GWT and Safari 2.0?
*   A: Unfortunately, the GWT History system does not play well with Safari 2.0.

A number of strange bugs in Safari 2, which are particularly difficult to work around, make it hard to use the same tricks GWT uses for history support in IE and Firefox. Some
messy solutions do exist but these fail in a number of significant edge cases and are not likely to be good long-term solutions.

The current implementation provides fall-back functionality for Safari 2 so that the library works but no history entries are generated.

Although the current GWT solution for history support doesn't completely work on Safari 3 beta, it has been tested on the latest nightly WebKit builds and is looking good.

So, hold on to your hats! History support for Safari will work properly when Safari 3 is officially released.

## Development Mode<a id="Hosted_Mode"></a>

### Failed to getGlobalExecState<a id="Failed_to_getGlobalExecState"></a>

On Mac when launching development mode, receive the error "Failed to getGlobalExecState".

This problem is related to the Google Gears enabler.

#### Workaround

Check the /Library/InputManagers folder. If there is a file named GearsEnabler.bundle, removing it should fix this issue.

### RuntimeException: Installation problem detected, please reinstall GWT<a id="RuntimeException_:_Installation_problem_detected,_please_reinsta"></a>

When launching the GWT compiler or development mode, you may see the following exception.

```text
Exception in thread "main" java.lang.ExceptionInInitializerError
Caused by: java.lang.RuntimeException: Installation problem detected, please reinstall GWT
    at com.google.gwt.util.tools.Utility.computeInstallationPath(Utility.java:322)
    at com.google.gwt.util.tools.Utility.getInstallPath(Utility.java:223)
    at com.google.gwt.util.tools.ToolBase.<clinit>(ToolBase.java:55)
Caused by: java.io.IOException: Cannot determine installation directory; apparently not running from a jar
    at com.google.gwt.util.tools.Utility.computeInstallationPath(Utility.java:307)
  ...
```

This message occurs when the runtime binary installation of GWT cannot be located. In general, the runtime binary installation path is expected to be in the same directory
where the gwt-dev-`<`platform`>`.jar file is located.

The GWT installation path is embedded in the scripts (created from applicationCreator and projectCreator) as the path specified to the
gwt-dev-`<`platform`>`.jar file. If you look in this directory, it should contain not only the gwt-user.jar and gwt-dev-`<`platform`>`.jar
files, but also the platform-specific shared library files, such as libgwt_ll.so or libgwt_ll.jnilib.

#### Workaround

Make sure that the path specified to the gwt-dev-`<`platform`>`.jar file on the Java classpath still contains the shared libraries. If not, you may need to
re-download and unpack the GWT distribution, making sure your download is complete.

##### Note to contributors<a id="Note_to_contributors"></a>

If you are a contributor and checked out the source code via svn, you might encounter this problem if you are running GWT from source.

Make sure you have:

*   a binary distribution unpacked or a compiled distribution ready
*   set the system property _gwt.devjar_ configured in your launch configurations as specified in the eclipse/README.txt file.

### Unable to find type 'com.foo.client.MyApp'<a id="Unable_to_find_type_'com.foo.client._MyApp_'"></a>

When running in development mode, receive the error: Unable to find type 'com.foo.client.MyApp'.

```text
Starting HTTP on port 8888
Finding entry point classes
  Unable to find type 'com.google.gwt.sample.stockwatcher.client.StockWatcher'
    Hint: Check that the type name 'com.google.gwt.sample.stockwatcher.client.StockWatcher' is really what you meant
    Hint: Check that your classpath includes all required source roots
Failure to load module 'com.google.gwt.sample.stockwatcher.StockWatcher'
```

This problem is most often seen when creating a launch configuration manually.  The [Google Plugin for Eclipse](https://developers.google.com/eclipse) handles adding source roots to the classpath automatically.

One of the most common problems when creating launch configurations manually in GWT is to omit the application source code path from the Java classpath. The [GWT compiler](DevGuideCompilingAndDebugging.html#DevGuideJavaToJavaScriptCompiler) and [development mode](DevGuideCompilingAndDebugging.html#DevGuideDevMode) shell rely on the module
source code to build your application, and both use the Java classpath to find the .java source files.

#### Workaround

If you are using Eclipse, open the launch configuration, navigate to the Classpath tab, and use the Advanced button to add the `src` folders to the classpath.

If you are launching from the command line, add the `src` folders to the list of paths following the `-cp` argument to the Java command.

### Invalid memory access of location 00000000 rip=01160767, or similar<a id="Invalid_memory_access_of_location"></a>

If you're using a version of GWT prior to 2.0 on the Mac platform along with Java 6, you're likely seeing this error message when you try running hosted mode. This problem no longer exists in GWT 2.0's [Development Mode](DevGuideCompilingAndDebugging.html#DevGuideDevMode).

The reason why this is occurring is because hosted mode uses 32-bit SWT bindings, which are tightly coupled with Carbon on the Mac platform.. Unfortunately, Java 6 on the Mac uses a 64-bit JVM, and  64-bit support for Carbon is currently unavailable. This means that in order to use GWT hosted mode on the Mac, you'll need to stick to Java 5 for 32-bit JVM support.

#### Workaround

For any other platform, using a 64-bit JVM will result in a similar error message when trying to start hosted mode. Fortunately, Java 6 comes in 32-bit flavors for these platforms, and so you can continue using the 64-bit JVM for GWT compilation and use the 32-bit JVM for hosted mode.

### Navigation to the page was canceled<a id="Navigation_to_the_page_was_canceled"></a>

Some developers have experienced this issue running GWT 1.6 hosted mode on Windows Vista. The reason why this is occurring is because for many applications and services running on Windows Vista, the IPv6 convention is used, including the endpoint IP address defined for localhost.

#### Workaround

If you take a peek at your `C:\Windows\System32\drivers\etc\hosts` file, you will notice the endpoint `::1 localhost` defined in the hosts file. To have localhost working in the IPv4 case, which is used in hosted mode, simply change this line to read `127.0.0.1 localhost`.

### java.security.AccessControlException: access denied<a id="AccessControlException_access_denied"></a>

If you created your application using the [Google Plugin for Eclipse](https://developers.google.com/eclipse), you may have encountered the following stack trace:

```text
WARNING: Nested in java.lang.ExceptionInInitializerError:
java.security.AccessControlException: access denied
(java.lang.RuntimePermission modifyThreadGroup)
       at java.security.AccessControlContext.checkPermission(AccessControlContext.java:323)
       at java.security.AccessController.checkPermission(AccessController.java:546)
       at java.lang.SecurityManager.checkPermission(SecurityManager.java:532)
       at com.google.appengine.tools.development.DevAppServerFactory

```

The problem occurs if you mistakenly enabled the [Google App Engine](https://developers.google.com/appengine) feature for your project when you created the project, or at a later time during development, and have server-side libraries or classes packaged with your GWT application war folder that violate the [Java App Engine sandbox](https://developers.google.com/appengine/docs/whatisgoogleappengine).

#### Workaround

If you don't intend on using Google App Engine for your project, simply remove the Google App Engine functionality from it following the steps below:

1.  In Eclipse, right-click on your project and select **Google > App Engine Settings...**
2.  In the App Engine property menu, deselect the **Use Google App Engine** checkbox.
3.  Click **OK**.

If you intend to use Google App Engine, you will need to remove any use of server-side libraries that are violating the security constraints of the App Engine sandbox from your project. For more information on the support available in the App Engine sandbox, check out the [App Engine documentation.](https://developers.google.com/appengine/docs)

## Image Bundles<a id="Image_Bundles"></a>

### ImageBundle images not showing in Internet Explorer<a id="ImageBundle_images_not_showing_in_Internet_Explorer"></a>

ImageBundle images show up in Firefox, Opera and Safari, but aren't showing up in Internet Explorer.

#### Security<a id="Security"></a>

The first thing to verify is whether or not your web application uses HTTPS, or if any of the images included in the generated image bundle file are under a security
constraint.

If that is the case, you may be running into a problem rooted in your web server setting certain response headers (such `Pragma: No-cache, Expires: Thu, 1 Jan 1970
00:00:00,` ...) and Internet Explorer respecting those headers when when using the HTTPS protocol.

##### Workaround

You can read more details about this security issue and how to work around it in the API Reference, [Image Bundles and the HTTPS Protocol](/javadoc/latest/com/google/gwt/user/client/ui/ImageBundle.html).

#### Size<a id="Size"></a>

On the other hand, if you're not using HTTPS but are still running into this problem, check out the size of your image bundle file.

A number of developers have run into the problem of Internet Explorer consuming large amounts of memory when extracting images out of a larger image bundle, and subsequently
failing to properly display the images.

##### Workaround

You might want to consider splitting up one large image bundle into several smaller bundles. This technique has worked for most developers who have experienced this
problem.

## Internationalization<a id="Internationalization"></a>

### International characters don't display correctly<a id="International_characters_don't_display_correctly"></a>

If you've embedded international characters into Java source files, these characters might not display correctly when you run the application and view it in a web browser.

Java requires UTF-8 for source code files containing international characters. Frequently the editor used to generate the files (such as an IDE) is not configured to save the
files as UTF-8. Thus your source files are not properly encoded in UTF-8.

#### Troubleshooting checklist

*   Check your editor to see if it is capable of saving files as UTF-8 and that it is currently configured to do so.
*   If you have embedded international characters directly into your Java source files, check to see if they are saved in UTF-8 format.
*   Check any associated Java properties files (or other files where you've stored translations) to see if they are saved in UTF-8 format.
*   Check the HTML host page. If your web page content contains localized data, encode it as UTF-8 by adding the following tag to the `<`head`>` element.

```html
<meta http-equiv="content-type" content="text/html;charset=utf-8" />`
```

## JUnit Testing<a id="JUnit_Testing"></a>

### JUnit tests fail on Mac<a id="JUnit_tests_fail_on_Mac"></a>

Some JUnit tests fail on the Mac platform.

When the Safari/Webkit web browser runs in headless mode it is able to optimize some rendering operations. One result of these optimizations is that the position or size of
some elements may be different when running a unit test than when running an actual application. A common symptom is to see the position or size of an widget set to
_(0,0)_.

#### Workaround

To work around this problem, pass the `-notHeadless` argument in the `-Dgwt.args=` system property setting. Note: When using the ant build, this argument is
enabled by default as of the GWT 1.5 release.

When `-notHeadless` is specified for running unit tests, the log window and hosted mode browser will pop up on the screen while the test is running. Dismissing or
manipulating the elements in these windows may cause the unit tests to fail.
