Super Dev Mode
===

*Revised June 19, 2020 for GWT 2.9*

## Introduction <a id="Introduction"></a>

Super Dev Mode replaces the internals of
<a href="../doc/latest/DevGuideCompilingAndDebugging.html#dev_mode">Dev Mode</a> with
a different approach that works better in modern browsers. Like its predecessor
(which I'll call <i>classic</i> Dev Mode), Super Dev Mode allows GWT
developers to quickly recompile their code and see the results in a browser.
It also allows developers to use a debugger to inspect a running GWT application.
However, it works differently.

## Prerequisites <a id="Prerequisites"></a>

Super Dev Mode requires the xsiframe linker.

To enable Super Dev Mode for your application, you will need to add these settings to your
module.xml file:

*   GWT 2.5.1

    ```xml
    <add-linker name="xsiframe"/>
    <set-configuration-property name="devModeRedirectEnabled" value="true"/>
    <set-property name="compiler.useSourceMaps" value="true"/>
    ``` 

*   GWT 2.6.1

    ```xml
    <add-linker name="xsiframe"/>
    ```

*   GWT 2.7 and above

    From GWT 2.7, the xsiframe linker is the default.

## Launching Super Dev Mode <a id="Launch"></a>

### Via Dev Mode <a id="LaunchDevMode"></a>

Starting with GWT 2.7, Dev Mode launches Super Dev Mode automatically. Just
[start Dev Mode](../doc/latest/DevGuideCompilingAndDebugging.html#launching_in_dev_mode)
and reload the page, and it will recompile automatically when necessary.

#### How it works <a id="HowItWorksWithDevMode"></a>

At startup, Dev Mode overwrites the GWT application's nocache.js files
with stub files that automatically recompile the GWT application if necessary.

The GWT application itself is loaded from a separate web server running
on a different port (9876 by default).

### Without Dev Mode <a id="Starting"></a>

You can also run Super Dev Mode without Dev Mode.
This is required in older versions of GWT and may also be useful
when debugging a GWT application running a different server.

If you are not using Dev Mode, first you need to compile the GWT
application and launch a web server that serves its output files.
Then launch the Super Dev Mode code server as a separate process.

The code server is an ordinary Java program that you can run from
the command line. Here are some details you'll need to start it:

* The jar file is gwt-codeserver.jar

* You will also need gwt-dev.jar in your classpath, along with anything else
needed to compile your GWT app (most likely gwt-user.jar).

* The main method is in com.google.gwt.dev.codeserver.CodeServer

If you run CodeServer without any arguments, it will print out its
command line arguments. For 2.12.1, here is the output:

```text
Google Web Toolkit 2.12.1
CodeServer [-[no]allowMissingSrc] [-[no]compileTest] [-compileTestRecompiles count] [-[no]failOnError] [-[no]precompile] [-port port] [-src dir] [-workDir dir] [-launcherDir] [-bindAddress host-name-or-address] [-style (DETAILED|OBFUSCATED|PRETTY)] [-setProperty name=value,value...] [-[no]incremental] [-sourceLevel [auto, 1.8, 9, 10, 11, 17]] [-logLevel (ERROR|WARN|INFO|TRACE|DEBUG|SPAM|ALL)] [-[no]generateJsInteropExports] [-includeJsInteropExports/excludeJsInteropExports regex] [-XmethodNameDisplayMode (NONE|ONLY_METHOD_NAME|ABBREVIATED|FULL)] [-X[no]closureFormattedOutput] [module]

where
  -[no]allowMissingSrc                              Allows -src flags to reference missing directories. (defaults to OFF)
  -[no]compileTest                                  Exits after compiling the modules. The exit code will be 0 if the compile succeeded. (defaults to OFF)
  -compileTestRecompiles                            The number of times to recompile (after the first one) during a compile test.
  -[no]failOnError                                  Stop compiling if a module has a Java file with a compile error, even if unused. (defaults to OFF)
  -[no]precompile                                   Precompile modules. (defaults to ON)
  -port                                             The port where the code server will run.
  -src                                              A directory containing GWT source to be prepended to the classpath for compiling.
  -workDir                                          The root of the directory tree where the code server willwrite compiler output. If not supplied, a temporary directorywill be used.
  -launcherDir                                      An output directory where files for launching Super Dev Mode will be written. (Optional.)
  -bindAddress                                      Specifies the bind address for the code server and web server (defaults to 127.0.0.1)
  -style                                            Script output style: DETAILED, OBFUSCATED or PRETTY
  -setProperty                                      Set the values of a property in the form of propertyName=value1[,value2...].
  -[no]incremental                                  Compiles faster by reusing data from the previous compile. (defaults to ON)
  -sourceLevel                                      Specifies Java source level (defaults to 1.8)
  -logLevel                                         The level of logging detail: ERROR, WARN, INFO, TRACE, DEBUG, SPAM or ALL (defaults to INFO)
  -[no]generateJsInteropExports                     Generate exports for JsInterop purposes. If no -includeJsInteropExport/-excludeJsInteropExport provided, generates all exports. (defaults to OFF)
  -includeJsInteropExports/excludeJsInteropExports  Include/exclude members and classes while generating JsInterop exports. Flag could be set multiple times to expand the pattern. (The flag has only effect if exporting is enabled via -generateJsInteropExports)
  -XmethodNameDisplayMode                           EXPERIMENTAL: Specifies method display name mode for chrome devtools: NONE, ONLY_METHOD_NAME, ABBREVIATED or FULL (defaults to NONE)
  -X[no]closureFormattedOutput                      EXPERIMENTAL: Enables Javascript output suitable for post-compilation by Closure Compiler (defaults to OFF)
and
  module                                            The GWT modules that the code server should compile. (Example: com.example.MyApp)
```

At a minimum, you need to give it the name of the GWT module to compile.

After the server starts up, it prints its URL:

```text
The code server is ready.
Next, visit: http://localhost:9876/
```
Load this URL in a browser. It provides two bookmarklets that you can drag and drop
to the browser's bookmarklet bar.

Then go to the web page containing the GWT application you want to debug.
(You should start this server if needed; it isn't provided for you.)

Click the `Dev Mode On` bookmarklet and it will show the a dialog:

<img src="../images/superdevmode_dialog.png"/>

You can then click `Compile` for the application that you want to recompile,
and it will recompile and then reload the page.

Tip: each `Compile` button is also a bookmarklet, so you can drag it the toolbar
if you like.

#### How the bookmarklet works <a id="HowItWorks"></a>

Pressing the "Compile" button sends a request to recompile the GWT application
to the code server.

The code server runs the GWT compiler in draft mode (with most optimizations turned
off) and makes the compiler's output available at a URL that looks something like this:

```text
http://localhost:9876/hello/hello.nocache.js
```

 and then sets a special value in
[session storage](https://developer.mozilla.org/en/DOM/Storage) and reloads the page.

Here's what the session storage variable looks like:

```javascript
window.sessionStorage["__gwtDevModeHook:hello"]
// returns  "http://localhost:9876/hello/hello.nocache.js"
```

After reloading the page, the original hello.nocache.js script will look for this special key
(with the same module name) in session storage and automatically redirect to the
provided URL.

## Debugging <a id="Debugging"></a>

Super Dev Mode compiles entire GWT applications to JavaScript, similar to production mode. This
means we can't use a normal Java debugger like in classic Dev Mode. Instead, we will use the
browser's debugger. (I recommend using Chrome for now.)

Browser debuggers are designed for debugging JavaScript. However, the
[SourceMaps standard](https://docs.google.com/a/google.com/document/d/1U1RGAehQwRypUTovF1KRlpiOFze0b-_2gc6fAH0KY0k/edit)
allows the debugger to display source code for Java (and other languages) instead of
JavaScript files.

In Chrome, you might need to turn on Source Maps in the debugger settings:

<img src="../images/superdevmode_enablesourcemaps.png"/>

Once this setting is on, you should be able to browse all the Java source in your GWT app in
the "Sources" panel in Chrome's developer tools. (You can also press Control-P to search for
a source file.)

#### How Sourcemap-based debugging works <a id="HowSourceMapsWork"></a>

When using Super Dev Mode, the browser's debugger will download Java files
from the code server. Here's an example URL that a browser debugger might use to download a file:

```text
http://localhost:9876/sourcemaps/hello/com/google/gwt/core/client/GWT.java
```

## Differences <a id="Differences"></a>

Compared to classic Dev Mode, Super Dev Mode's approach has some advantages:

* It supports mobile browsers. (No browser plugins are needed.)

* GWT applications run faster, because calls between Java and JavaScript no longer
require a network round trip.

* Developers can debug Java, JavaScript, and possibly other client-side languages in the
same debugging session. This is especially useful when calling JavaScript libraries from GWT.

* Browser debuggers have advanced features (such as DOM breakpoints) that can be useful for
debugging GWT code.

However, there are also some disadvantages you should be aware of:

* The code server has no authentication. It also serves all your Java source code to any user who
asks. Therefore, we recommend only running it on localhost or behind a firewall. (It uses localhost
by default.)

* https is not supported. See
[issue 7535](https://github.com/gwtproject/gwt/issues/7535)
for updates.

* Although all modern browser have some support for sourcemaps, sourcemap-based debugging currently
works best in Chrome. We hope browser support will improve, but for now you'll have to rely on other
debugging tricks for some browsers. For example, you can use `GWT.log` and `java.util.logging` to log a
message to the browser console add a call in the Java code to `GWT.debugger` to add a breakpoint.

* When inspecting variables in the Chrome debugger, the field names and values are JavaScript,
not Java.

* Super Dev Mode doesn't support running Java web apps (war files) like Dev Mode. In 2.7 and above,
you can use Dev Mode and it will automatically start Super Dev Mode, or else you need to
run a separate Java servlet engine.

* Since Super Dev Mode doesn't run GWT applications in a JVM, some runtime checks don't
happen. For example, there won't be any IndexOutOfBoundsException when an array index
is out of range.

Happy hacking!
