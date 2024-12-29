Overview
===

GWT is a development toolkit for building and optimizing complex browser-based applications. Its goal is to enable productive development of high-performance web applications without the developer having to be an expert in browser quirks, XMLHttpRequest, and JavaScript. It's open source, completely free, and used by thousands of developers around the world.

[What's New in GWT 2.12.1](release-notes.html#Release_Notes_2_12_1)

## What's inside the toolbox?

### SDK

The [GWT SDK](learnmore-sdk.html) contains the Java API libraries, compiler, and development server. It lets you write client-side applications in Java and deploy them as JavaScript.

*   [Get Started](gettingstarted-v2.html)
*   [Tutorials](doc/latest/tutorial/index.html)
*   [Developer Guide](doc/latest/DevGuide.html)

### Plugin for Eclipse

The [Plugin for Eclipse](https://developers.google.com/eclipse/index) provides IDE support for GWT and App Engine web projects.

*   [Get Started](usingeclipse.html)

## Developing with GWT

### <i class="icon_write"></i> Write

The GWT SDK provides a set of core Java APIs and Widgets. These allow you to write AJAX applications in Java and then compile the source to highly optimized JavaScript that runs across all browsers, including mobile browsers for Android and the iPhone.

Constructing AJAX applications in this manner is more productive thanks to a higher level of abstraction on top of common concepts like DOM manipulation and XHR communication.

You aren't limited to pre-canned widgets either. Anything you can do with the browser's DOM and JavaScript can be done in GWT, including interacting with hand-written JavaScript.

### <i class="icon_debug"></i> Debug

You can debug AJAX applications in your favorite IDE just like you would a desktop application, and in your favorite browser just like you would if you were coding JavaScript. The GWT developer plugin spans the gap between Java bytecode in the  debugger and the browser's JavaScript.

Thanks to the GWT developer plugin, there's no compiling of code to JavaScript to view it in the browser. You can use the same edit-refresh-view cycle you're used to with JavaScript, while at the same time inspect variables, set breakpoints, and utilize all the other debugger tools available to you with Java. And because GWT's development mode is now in the browser itself, you can use browser dev tools as you code in Java.

### <i class="icon_optimise"></i> Optimize

GWT contains two powerful tools for creating optimized web applications. The GWT compiler performs comprehensive optimizations across your codebase &mdash; in-lining methods, removing dead code, optimizing strings, and more. By setting split-points in the code, it can also segment your download into multiple JavaScript fragments, splitting up large applications for faster startup time.

Performance bottlenecks aren't limited to JavaScript. Browser layout and CSS often behave in strange ways that are hard to diagnose. Speed Tracer is a new Chrome Extension in GWT that enables you to diagnose performance problems in the browser.

### <i class="icon_run"></i> Run

When you're ready to deploy, GWT compiles your Java source code into optimized, stand-alone JavaScript files that automatically run on all major browsers, as well as mobile browsers for Android and the iPhone.

## Ready to get started?

[Download the SDK](gettingstarted-v2.html) and get a simple web application up and running. From there, work through the fundamentals of GWT development with the in-depth [tutorials](doc/latest/tutorial/index.html).
