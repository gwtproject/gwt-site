<style>

div.diagram img {
  margin: 20px;
}

.contents {
  border: none;
}

.contents td {
  border: none;
}

.contents .header {
  font-weight: bold;
}

.flow-img {
  float: left;
  margin: 5px 0px 10px 0px;
  width: 80px;
  height: 80px;
}

.flow {
  margin-left: 85px;
}

.gwt-tools {
  margin-left: 90px;
}

.gwt-tools-head {
  font-weight: bold;
  font-size: 110%;
  margin-bottom: 0.2em;
}
</style>

<img src="images/sdk-sm.png" style="float: right; width: 80px; height: 80px;
margin-right: 15px;" />

<div style="margin-top: 10px;">
<p>
Writing web apps for multiple browsers can be a tedious and error-prone process. 
You can spend 90% of your time working around browser quirks. In addition, building,
reusing, and maintaining large JavaScript code bases and AJAX components can be
difficult and fragile. 
</p>
<p>
GWT (GWT) eases this burden by allowing developers to
quickly build and maintain complex yet highly performant JavaScript front-end
applications in the Java programming language.
</p>
</div>

<a name="how"></a>
<h2>How the SDK works</h2>

<div>
<object width="560" height="340"><param name="movie" value="https://www.youtube.com/v/ShkYDPN5Knc&hl=en_US&fs=1&"></param><param name="allowFullScreen" value="true"></param><param name="allowscriptaccess" value="always"></param><embed src="https://www.youtube.com/v/ShkYDPN5Knc&hl=en_US&fs=1&" type="application/x-shockwave-flash" allowscriptaccess="always" allowfullscreen="true" width="560" height="340"></embed></object>
</div>

<p>With the GWT SDK, you write your AJAX front-end in the Java programming language which GWT then cross-compiles into optimized JavaScript that automatically works across all major browsers. During development, you can iterate quickly in the same "edit - refresh - view" cycle you're accustomed to with JavaScript, with the added benefit of being able to debug and step through your Java code line by line. When you're ready to deploy, the GWT compiler compiles your Java source code into optimized, standalone JavaScript files.</p>

<a name="write"></a>
<h4>Write AJAX apps in the Java language, then compile to optimized JavaScript</h4>

<p>Unlike JavaScript minifiers that work only at a textual level, the GWT compiler performs comprehensive static analysis and optimizations across your entire GWT codebase, often producing JavaScript that loads and executes faster than equivalent handwritten JavaScript. For example, the GWT compiler safely eliminates dead code -- aggressively pruning unused classes, methods, fields, and even method parameters -- to ensure that your compiled script is the smallest it can possibly be. Another example: the GWT compiler selectively inlines methods, eliminating the performance overhead of method calls.  </p>

<h2 id="otherthings">Other interesting things</h2>

<h4>In the end, it's just JavaScript</h4>
<p>The GWT SDK provides a core set of Java APIs and libraries that allow you to productively build user interfaces and logic for the browser client. You then compile that source code to JavaScript. All that runs in the end is plain ol' JavaScript in the browser. Oh, and you can mix in and interoperate with JavaScript in your source code as well. 
  
<h4>Don't worry about XMLHttpRequest</h4>
<p>GWT can handle all of the client-server communications for you, whether you use JSON, XML, or GWT's optimized Remote Procedure Call (RPC) system. You don't need to know the lower level details and frustrations of XHR calls.</p>

<h4>Use the backend language of your choice</h4>
<p>You don't have to run Java on your server to use GWT to build your client. Because GWT works with many standard communication protocols, you can easily communicate back and forth. </p>

<h4>Optimize the JavaScript script downloads based on user profile</h4>
<p>GWT creates a separate compiled version of your application that is optimized for a particular user's environment.  This means that a Firefox browser displaying an application in French doesn't need to download extra code for other browsers or languages.</p>

<h4>Reuse UI components across projects</h4>
<p>Create reusable Widgets by compositing other Widgets, then easily lay them out automatically in Panels.  Want to reuse your Widget in another project? Simple package it up for others to use in a JAR file.</p>

<h4>Use other JavaScript libraries and native JavaScript code</h4>
<p>You can mix handwritten JavaScript in your Java source code to interface with existing JavaScript APIs.  You can write individual JavaScript methods directly in your Java source files and even encapsulate JavaScript objects inside a Java class.</p>

<h4>Easily support the browser's back button and history</h4>
<p>No, AJAX applications don't need to break the browser's back button. GWT lets you make your site more usable by easily adding state to the browser's back button history.</p>

<h4>Internationalize your application</h4>
<p>If your application is successful, you'll want to support the world. Making good architecture decisions up front helps. With GWT you can easily create efficient internationalized applications and libraries, including bi-directionality.</p>

<h4>Be productive with your choice of development tools</h4>
<p>Because GWT uses Java, you'll be catching errors like typos and type mismatches as you write the code, not at runtime.  Harness the productivity gains of an IDE's automated Java refactoring and code prompting/completion. Now you can use all of your favorite Java development tools
(<a href="http://www.eclipse.org/">Eclipse</a>, <a href="http://www.jetbrains.com/idea/">IntelliJ</a>, <a href="http://www.ej-technologies.com/products/jprofiler/overview.html">JProfiler</a>) for your AJAX development. </p>

<h4>Test your code with JUnit</h4> 
<p>GWT's direct integration with <a href="http://www.junit.org/">JUnit</a> lets you unit test both in a debugger and in a browser...and you can even unit test asynchronous RPCs.</p>

<h4>It's free and open source</h4>
<p>GWT is free, and all of the <a href="https://gwt.googlesource.com/">code</a> is available under the Apache 2.0 license.</p>


<h2>Ready to get started?</h2>
<a href="gettingstarted.html">
  <img src="images/arrow-md.png" style="float: left; margin:10px 10px 0px 0px; border-style:none; width: 80px; height: 80px" />
</a>
<p>
  <a href="gettingstarted.html">Download the SDK</a> and get a simple web
  application up and running.
</p>


