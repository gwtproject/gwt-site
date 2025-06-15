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

Learn more
===

<img src="images/sdk-sm.png" style="float: right; width: 80px; height: 80px;
margin-right: 15px;" />

Writing web apps for multiple browsers can be a tedious and error-prone process. You can spend 90% of your time working around browser quirks. In addition, building, reusing, and maintaining large JavaScript code bases and AJAX components can be difficult and fragile. 

GWT (GWT) eases this burden by allowing developers to quickly build and maintain complex yet highly performant JavaScript front-end applications in the Java programming language.

## How the SDK works <a id="how"></a>

<div>
<object width="560" height="340"><param name="movie" value="https://www.youtube.com/v/ShkYDPN5Knc&hl=en_US&fs=1&"></param><param name="allowFullScreen" value="true"></param><param name="allowscriptaccess" value="always"></param><embed src="https://www.youtube.com/v/ShkYDPN5Knc&hl=en_US&fs=1&" type="application/x-shockwave-flash" allowscriptaccess="always" allowfullscreen="true" width="560" height="340"></embed></object>
</div>

With the GWT SDK, you write your AJAX front-end in the Java programming language which GWT then cross-compiles into optimized JavaScript that automatically works across all major browsers. During development, you can iterate quickly in the same "edit - refresh - view" cycle you're accustomed to with JavaScript, with the added benefit of being able to debug and step through your Java code line by line. When you're ready to deploy, the GWT compiler compiles your Java source code into optimized, standalone JavaScript files.

<a id="write"></a>

#### Write AJAX apps in the Java language, then compile to optimized JavaScript

Unlike JavaScript minifiers that work only at a textual level, the GWT compiler performs comprehensive static analysis and optimizations across your entire GWT codebase, often producing JavaScript that loads and executes faster than equivalent handwritten JavaScript. For example, the GWT compiler safely eliminates dead code &mdash; aggressively pruning unused classes, methods, fields, and even method parameters &mdash; to ensure that your compiled script is the smallest it can possibly be. Another example: the GWT compiler selectively inlines methods, eliminating the performance overhead of method calls.  

## Other interesting things

### In the end, it's just JavaScript

The GWT SDK provides a core set of Java APIs and libraries that allow you to productively build user interfaces and logic for the browser client. You then compile that source code to JavaScript. All that runs in the end is plain ol' JavaScript in the browser. Oh, and you can mix in and interoperate with JavaScript in your source code as well. 

### Don't worry about XMLHttpRequest

GWT can handle all of the client-server communications for you, whether you use JSON, XML, or GWT's optimized Remote Procedure Call (RPC) system. You don't need to know the lower level details and frustrations of XHR calls.

### Use the backend language of your choice

You don't have to run Java on your server to use GWT to build your client. Because GWT works with many standard communication protocols, you can easily communicate back and forth. 

### Optimize the JavaScript script downloads based on user profile

GWT creates a separate compiled version of your application that is optimized for a particular user's environment.  This means that a Firefox browser displaying an application in French doesn't need to download extra code for other browsers or languages.

### Reuse UI components across projects

Create reusable Widgets by compositing other Widgets, then easily lay them out automatically in Panels.  Want to reuse your Widget in another project? Simply package it up for others to use in a JAR file.

### Use other JavaScript libraries and native JavaScript code

You can mix handwritten JavaScript in your Java source code to interface with existing JavaScript APIs.  You can write individual JavaScript methods directly in your Java source files and even encapsulate JavaScript objects inside a Java class.

### Easily support the browser's back button and history

No, AJAX applications don't need to break the browser's back button. GWT lets you make your site more usable by easily adding state to the browser's back button history.

### Internationalize your application

If your application is successful, you'll want to support the world. Making good architecture decisions up front helps. With GWT you can easily create efficient internationalized applications and libraries, including bi-directionality.

### Be productive with your choice of development tools

Because GWT uses Java, you'll be catching errors like typos and type mismatches as you write the code, not at runtime.  Harness the productivity gains of an IDE's automated Java refactoring and code prompting/completion. Now you can use all of your favorite Java development tools
([Eclipse](http://www.eclipse.org/), [IntelliJ](http://www.jetbrains.com/idea/), [JProfiler](http://www.ej-technologies.com/products/jprofiler/overview.html)) for your AJAX development. 

### Test your code with JUnit

GWT's direct integration with [JUnit](http://www.junit.org/) lets you unit test both in a debugger and in a browser...and you can even unit test asynchronous RPCs.

### It's free and open source

GWT is free, and all of the [code](https://gwt.googlesource.com/) is available under the Apache 2.0 license.

## Ready to get started?

<a href="gettingstarted-v2.html">
  <img src="images/arrow-md.png" style="float: left; margin:10px 10px 0px 0px; border-style:none; width: 80px; height: 80px" />
</a>

[Download the SDK](gettingstarted-v2.html) and get a simple web application up and running.
