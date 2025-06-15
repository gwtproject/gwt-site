Glossary
===

<dl>

<dt>asynchronous RPC</dt>
<dd>A remote procedure call that returns control to the calling function immediately after making a request to the server. A callback method
will be called later to return the result (or an exception) from the server. GWT-RPC methods are always asynchronous.</dd>

<dt>code server</dt>
<dd>Development runs a <i>code server</i>, which listens for connections from
a browser with the developer plugin to run Java code in a real JVM.</dd>

<dt>code splitting</dt>
<dd>A <a href="DevGuideCodeSplitting.html">compiler feature</a> that automatically splits your compiled application into multiple fragments that are downloaded on-demand.</dd>

<dt>composite widgets</dt>
<dd>Widgets that are composed of one or more panels or widgets. Composite widgets are an easy way to <a href="DevGuideUiCustomWidgets.html">extend GWT's existing widgets</a>.</dd>

<dt>cross-browser</dt>
<dd>Refers to the ability to operate on multiple browser platforms (Internet Explorer, Mozilla, Safari, etc...). See the following FAQ for the
full list of browsers supported by GWT.</dd>

<dt>deferred binding</dt>
<dd>A technique used by the GWT compiler to create or select a specific implementation of a class based on a set of environment parameters. The
method <a href="/javadoc/latest/com/google/gwt/core/client/GWT.html#create-java.lang.Class-">GWT.create()</a> triggers
deferred binding, allowing Java code to work with a declared Java interface. Some GWT features that use <a href="DevGuideCodingBasics.html#DevGuideDeferredBinding">deferred
binding</a> include <a href="DevGuideI18n.html#DevGuideStaticStringInternationalization">static string internationalization</a>, ImageBundle localization as well as <a href="DevGuideServerCommunication.html#DevGuideRemoteProcedureCalls">GWT-RPC</a>. For more details on deferred binding, check out the <a href="DevGuideCodingBasics.html#DevGuideDeferredBinding">deferred binding</a> chapter in the development guide.</dd>

<dt>deployment</dt>
<dd>The act of copying your GWT compiled application files and public path contents to a web server, as well as porting your servlet implementations
and configurations to a non-GWT servlet container.</dd>

<dt>developer plugin</dt>
<dd>To run development mode, the browser you use needs a special plugin that
communicates with the <i>code server</i> to execute Java code.</dd>

<dt>development mode</dt>
<dd>Refers to running your application in a regular browser that has the
developer plugin installed.  Development mode runs your application directly
in Java so that you can use a Java IDE debugger to help test and debug your
application.</dd>

<dt>dynamic string internationalization</dt>
<dd>Allows you to look up localized strings defined in a <a href="DevGuideOrganizingProjects.html#DevGuideHostPage">HTML host page</a> at runtime using string-based keys.</dd>

<dt>ClientBundle</dt>
<dd>A <a href="DevGuideClientBundle.html">mechanism</a> for automatically including static resources, such as CSS and images, in compiled output.</dd>

<dt>code server</dt>
<dd>The embedded development mode web server that supports development and debugging.</dd>

<dt>compile report</dt>
<dd>A <a href="DevGuideCompileReport.html">report</a> that helps you understand the details of the compiled output &mdash; which classes, packages, and methods exist in the output, and how they are distributed across fragments.</dd>

<dt>CssResource</dt>
<dd><a href="DevGuideClientBundle.html#CssResource">CssResource</a> allows you to use Client bundle to process and include CSS in compiled output.</dd>

<dt>development mode</dt>
<dd>Refers to running your application under a special GWT supplied browser.
Development mode runs your application directly in Java so that you can use
a Java IDE debugger to help test and debug your application.</dd>

<dt>Google Plugin for Eclipse</dt>
<dd>An Eclipse <a href="https:developers.google.com/eclipse/index">plugin</a> that supports development of GWT and App Engine applications.</dd>

<dt>GWT compiler</dt>
<dd>A part of the GWT toolkit that translates Java into JavaScript.</dd>

<dt>hosted mode</dt>
<dd>Prior to GWT 2.0, development mode was called hosted mode, but used
a special GWT supplied browser.</dd>

<dt>GWT developer plugin</dt>
<dd>A <a href="DevGuideCompilingAndDebugging.html#DevGuideDevMode">browser plugin</a> that supports debugging of GWT applications in development mode.</dd>

<dt>HTML host page</dt>
<dd>
  An HTML page that includes a reference to a GWT `<module>.nocache.js` in a
  `<script>` HTML tag.
  This page may also contain HTML elements in its body, some of which your GWT module may reference or modify.
</dd>

<dt>development mode</dt>
<dd>Refers to running your application under a special GWT supplied browser.
Development mode runs your application directly in Java so that you can use
a Java IDE debugger to help test and debug your application.</dd>

<dt>ImageResource</dt>
<dd><a href="DevGuideClientBundle.html#ImageResource">ImageResource</a> allows you to use ClientBundle to include images in compiled output.</dd>

<dt>layout panels</dt>
<dd>A <a href="DevGuideUiPanels.html#LayoutPanels">set of widgets</a> introduced in GWT 2.0 that can be used to layout your application predictably and efficiently.</dd>

<dt>module XML file</dt>
<dd>A file that contains settings the GWT compiler and development mode use to
find your projects resources, such as Java Source code, static HTML,
stylesheets, and image files, and servlet classes.</dd>

<dt>native methods</dt>
<dd>Java methods that have a body implemented in JavaScript. The GWT compiler creates interface code to the Java method's parameters and return
values.</dd>

<dt>overlay types</dt>
<dd>A Java class that directly models a JavaScript object, giving you the development-time benefits of static types for JavaScript without adding
any memory or speed costs at runtime.</dd>

<dt>production mode</dt>
<dd>Refers to running the client-side of your application entirely in a web
browser (nothing in a JVM like in development mode).  In production mode, your
application runs from JavaScript generated by the GWT compiler.</dd>

<dt>public path</dt>
<dd>A directory or list of directories that contains static files that should be served by the web server. All files in the static path are copied
to the same directory as the compiled JavaScript output from the GWT compiler. If no public path is specified in the <a href="DevGuideOrganizingProjects.html#DevGuideModuleXml">module XML
file</a>, the default public path is `<module-name>/public`.</dd>

<dt>resource inclusion</dt>
<dd><a href="DevGuideOrganizingProjects.html#DevGuideAutomaticResourceInclusion">A technique</a> you can use in your <a href="DevGuideOrganizingProjects.html#DevGuideHostPage">host HTML page</a> to reference external JavaScript and stylesheets.</dd>

<dt>serializable types</dt>
<dd>Types which are capable of being encoded and moved outside the application in order to be stored or transmitted to another application.
GWT has specific rules for <a href="DevGuideServerCommunication.html#DevGuideSerializableTypes">serializable types</a> which must be used for RPC method parameters and return values.</dd>

<dt>service proxy</dt>
<dd>An implementation of an asynchronous interface in client-side code that links the GWT RPC <a href="/javadoc/latest/com/google/gwt/user/server/rpc/RemoteServiceServlet.html">RemoteServiceServlet</a> implementation
with your client code. Service proxy interface names always have the same name as the server-side synchronous class and end with the suffix `Async`.</dd>

<dt>source path</dt>
<dd>A subpackage or list of subpackages that contain your module's GWT application code. These subpackages are the ones that the GWT compiler will
translate at compile time. If no source path is specified in the <a href="DevGuideOrganizingProjects.html#DevGuideModuleXml">module XML file</a>, the default source path is `<module-name>/client`.</dd>

<dt>speed tracer</dt>
<dd>Speed Tracer is a Google Chrome extension that helps you identify and fix performance problems in your web applications.</dd>

<dt>static string internationalization</dt>
<dd>A technique of defining language specific strings in `.properties` files that GWT compiles into different
implementations of your <a href="/javadoc/latest/com/google/gwt/i18n/client/Localizable.html">Localizable</a> subclasses.
Using deferred binding, the language strings will map to the correct locale at runtime.</dd>

<dt>translatable</dt>
<dd>Refers to Java code that can be translated from Java into JavaScript. In order to be translatable, code must only use the <a href="DevGuideCodingBasics.html#DevGuideJavaCompatibility">supported subset</a> of the Java Runtime Library.</dd>

<dt>web mode</dt>
<dd>Prior to GWT 2.0, production mode was called web mode.</dd>

<dt>uibinder</dt>
<dd>A <a href="DevGuideUiBinder.html">system</a> for building user interfaces using a declarative XML syntax, which simplifies widget construction and makes it easier to use HTML in widget hierarchies.</dd>

</dl>
