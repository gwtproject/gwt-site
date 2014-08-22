<i>Brian Slesinsky, Senior Software Engineer</i>
<br>
<i>Updated September 2014</i>

<p> Super Dev Mode is a replacement for GWT's
<a href="../doc/latest/DevGuideCompilingAndDebugging.html#dev_mode">Development Mode</a>.
Like its predecessor (which I'll call <i>classic</i> Dev Mode), Super Dev Mode allows GWT
developers to quickly recompile their code and see the results in a browser. It also allows
developers to use a debugger to inspect a running GWT application. However, it works differently.
</p>

<h2 id="HowItWorks">How it works</h2>

<p> Super Dev Mode runs the GWT compiler in a web server, which is an ordinary Java application
that developers can run from the command line. After the server starts up, it prints its URL:
</p>

<blockquote><pre>
The code server is ready.
Next, visit: http://localhost:9876/
</pre></blockquote>

<p> This web server provides the user interface for interacting with Super Dev Mode and
also serves the GWT compiler's output. When a developer presses the "Compile" button for a
GWT module, the code server runs the GWT compiler in draft mode (with most optimizations turned
off) and makes the compiler's output available at a URL that looks something like this:
</p>

<blockquote><pre>
http://localhost:9876/hello/hello.nocache.js
</pre></blockquote>

<p> That's nice, but how do we use it on an HTML page? One possibility would be to
temporarily edit the &lt;script&gt; tag in an HTML page that runs a GWT application. But this can be
inconvenient when the HTML page is on a server that's shared with other users, or when
modifying the HTML can only be done via a slow server restart.
</p>

<p> Instead, Super Dev Mode provides another way to change the GWT application's URL, using
the "Dev Mode On" bookmarklet. Clicking the bookmarklet shows a dialog that lists the GWT
applications on the current page and allows you to compile them:
</p>

<img src="../images/superdevmode_dialog.png"/>

<p> Pressing the "Compile" button recompiles the GWT App, sets a special value in
<a href="https://developer.mozilla.org/en/DOM/Storage">session storage</a>, and reloads the page.
</p>

<p> Here's what the session storage variable looks like:
</p>

<blockquote><pre>
> window.sessionStorage["__gwtDevModeHook:hello"]
  "http://localhost:9876/hello/hello.nocache.js"
</pre></blockquote>

<p> The hello.nocache.js script will look for this special key (with the same module name)
in session storage and automatically redirect to the provided URL.
</p>

<p>Add the Super Dev Mode linker to the module.xml file. Older GWT versions require more project module settings.</p> 
<ul> 
<li><p>GWT 2.5.1 project module settings.</p> <pre>
<code>&lt;add-linker name=&quot;xsiframe&quot;/&gt;
&lt;set-configuration-property name=&quot;devModeRedirectEnabled&quot; value=&quot;true&quot;/&gt;
&lt;set-property name=&quot;compiler.useSourceMaps&quot; value=&quot;true&quot;/&gt;</code></pre></li> 
</ul> 
<ul> 
<li><p>GWT 2.6.1 project module settings.</p><pre>
<code>&lt;add-linker name=&quot;xsiframe&quot;/&gt;</code></pre></li> 
</ul> 
<ul> 
<li><p>GWT 2.7 (Future): No linker settings are necessary at this time.</p></li> 
</ul> 

(Currently, only the xsiframe linker supports Super Dev Mode.)

<h2 id="Debugging">Debugging</h2>

<p> Super Dev Mode compiles entire GWT applications to JavaScript, similar to production mode. This
means we can't use a normal Java debugger like in classic Dev Mode. However, there is an emerging
web standard called "Source Maps" that will allow developers to see Java (and other languages) in
the browser's debugger instead. In Chrome, you must enable Source Maps in the debugger settings:
</p>

<img src="../images/superdevmode_enablesourcemaps.png"/>

<p>Once this setting is on, you will be able to browse all the Java source in your GWT app in
the "Scripts" panel in Chrome's developer tools.</p>

<p>Here's how it works: when using Super Dev Mode, the browser's debugger will download Java files
from the code server. Although this isn't a practical way to browse your source code, you can
actually view a source file directly by going to the code server in a browser. Here's an example
URL:</p>

<blockquote><pre>
http://localhost:9876/sourcemaps/hello/com/google/gwt/core/client/GWT.java
</pre></blockquote>

<p>To avoid inadvertently exposing source code to the Internet, the code server runs on localhost
by default, so that the code server is only reachable from a browser running on the same machine.
You should only change this if you're behind a firewall and you don't mind that other people on the
same network might look at the source code.</p>

<h2 id="Differences">Differences</h2>

<p> Compared to classic Dev Mode, Super Dev Mode's approach has some advantages:
</p>

<ul>
  <li>No browser plugins required. This will make it much easier to support more browsers,
  particularly browsers on mobile phones.</li>
  <li>Performance is much faster for GWT code that calls JavaScript frequently, because it no longer
    requires a network round trip.</li>
  <li>Super Dev Mode doesn't automatically recompile after every page reload, making it easier to
  debug multi-page apps.</li>
  <li>Super Dev Mode doesn't suffer from bugs in browser extension API's.</li>
  <li>Developers can debug Java, JavaScript, and possibly other client-side languages in the same
    debugging session. This is especially useful when calling JavaScript libraries from GWT.</li>
  <li>Browser debuggers have advanced features (such as DOM breakpoints) that can be useful for
    debugging GWT code.</li>
</ul>

<p>However, there are also some disadvantages you should be aware of:
</p>

<ul>
  <li>Work to secure Super Dev Mode is incomplete. In the meantime, we recommend only running
    the code server on localhost or behind a firewall. Also, as a safety measure, Super Dev Mode
    should be disabled in production apps. (That is, set devModeRedirectEnabled to false.)</li>
  <li>We expect that Super Dev Mode will be able to support any modern browser, but for
    now, we have only tested it on Chrome and Firefox.</li>
  <li>Currently, Super Dev Mode doesn't work on some very large GWT apps where classic
    Dev Mode works.</li>
  <li>Only one GWT linker supports Super Dev Mode</li>
  <li>Currently, only the Chrome debugger supports Source Maps. We hope browser support will
    improve so that you can easily debug problems that only happen in other browsers, but in the
    meantime, you'll have to resort to other debugging tricks, such as adding logging
    statements and recompiling.</li>
  <li>Many features of Java debuggers aren't available when using Super Dev Mode. For example,
    when inspecting variables in the Chrome debugger, the field names and values are JavaScript,
    not Java.</li>
  <li>Currently, Super Dev Mode doesn't support running Java web apps (war files) like classic Dev
    Mode. The workaround is to run them on a separate server.</li>
  <li>Since Super Dev Mode doesn't run GWT applications in a JVM, some runtime checks don't
  happen. For example, there won't be any IndexOutOfBoundsException when an array index
  is out of range.</li>
</ul>

<h2 id="Starting">Starting the code server</h2>

<p> Here are some details you'll need for starting Super Dev Mode:
</p>

 <ul>
   <li>The jar file is gwt-codeserver.jar</li>
   <li>You will also need gwt-dev.jar in your classpath, along with anything else needed
   to compile your GWT app (most likely gwt-user.jar).</li>
   <li>The main method is in com.google.gwt.dev.codeserver.CodeServer</li>
 </ul>

 Here are the command line arguments that CodeServer currently supports:

<pre>
CodeServer [-bindAddress address] [-port port] [-workDir dir] [-src dir] [module]

where
  -bindAddress  The ip address of the code server. Defaults to 127.0.0.1.
  -port         The port where the code server will run.
  -workDir      The root of the directory tree where the code server will write compiler output. If not supplied, a temporary directory will be used.
  -src          A directory containing GWT source to be prepended to the classpath for compiling.
and
  module        The GWT modules that the code server should compile. (Example: com.example.MyApp)
</pre>

<p>Happy hacking!</p>
