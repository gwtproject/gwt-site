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

<img src="images/gwt-sm.png" style="float: left;" height="80" width="80" />

<div style="margin-left: 112px; ">
  <div style="padding-top: 5px; line-height: 125%;">
    GWT is a development toolkit for building and optimizing
    complex browser-based applications. Its goal is to enable productive development of high-performance web
    applications without the developer having to be an expert in browser quirks,
    XMLHttpRequest, and JavaScript. GWT is used by many products at Google,
    including AdWords, AdSense, Flights, Hotel Finder, Offers, Wallet, Blogger. It's open source,
    completely free, and used by thousands of developers around the world.
  </div>
  <br/>
  <div>
    <a href="release-notes.html#Release_Notes_2_7_0">What's New in GWT 2.7.0</a>
  </div>
</div>

## What's inside the toolbox?

<table width="100%" class="columns">
  <tr>
    <td width="50%">
      <a href="learnmore-sdk.html">
        <img src="images/sdk-sm.png" style="float: right; width: 80px; height: 80px; border: 0px none;"/>
      </a>
      <div class="gwt-tools" style="margin-left: 10px;">
        <div class="gwt-tools-head"><a class="gwt-tools-head" href="learnmore-sdk.html">SDK</a></div>
        The GWT SDK contains the Java API libraries, compiler, and development
        server. It lets you write client-side applications in Java and deploy
        them as JavaScript.
        <ul style="font-size: 90%; margin-bottom: -1em;">
          <li><a href="gettingstarted.html">Get Started</a></li>
          <li><a href="doc/latest/tutorial/index.html">Tutorials</a></li>
          <li><a href="doc/latest/DevGuide.html">Developer Guide</a></li>
        </ul>
      </div>
    </td>
    <td width="50%">
      <a href="https://developers.google.com/eclipse/index">
      <img src="https://developers.google.com/eclipse/images/google-plugin.png" style="float: right; width:
      80px; height: 80px; border: 0px none;">
      </a>
      <div class="gwt-tools" style="margin-left: 10px;">
        <div class="gwt-tools-head"><a href="https://developers.google.com/eclipse/index">Plugin for Eclipse</a></div>
        The Plugin for Eclipse provides IDE support for GWT and
        App Engine web projects. 
        <ul style="font-size: 90%;">
          <li><a href="usingeclipse.html">Get Started</a></li>
        </ul>
      </div>
    </td>
  </tr>
</table>

## Developing with GWT <a id="how"></a>

<div>
  <img src="images/flow1-sm.png" class="flow-img"/>
  <div class="flow">
    <h4>Write</h4>
    <p>
    The GWT SDK provides a set of core Java APIs and Widgets. These allow you to
    write AJAX applications in Java and then compile the source to highly optimized JavaScript that runs across
    all browsers, including mobile browsers for Android and the iPhone.
    </p>
    <p>
    Constructing AJAX applications in this manner is more productive thanks
    to a higher level of abstraction on top of common concepts like DOM
    manipulation and XHR communication.
    </p>
    <p>
    You aren't limited to pre-canned widgets either. Anything you can do with the
    browser's DOM and JavaScript can be done in GWT, including interacting with
    hand-written JavaScript.
    </p>
  </div>
</div>

<div>
  <img src="images/flow3-sm.png" class="flow-img"/>
  <div class="flow">
    <h4>Debug</h4>
    <p>
    You can debug AJAX applications in your favorite IDE just like
    you would a desktop application, and in your favorite browser just like you
    would if you were coding JavaScript. The GWT developer plugin spans
    the gap between Java bytecode in the  debugger and the browser's JavaScript.
    </p>
    <p>
    Thanks to the GWT developer plugin, there's no compiling of code to
    JavaScript to view it in the browser. You can
    use the same edit-refresh-view cycle you're used to with JavaScript,
    while at the same time inspect variables, set breakpoints,
    and utilize all the other debugger tools available to you with Java. And because GWT's
    development mode is now in the browser itself, you can use tools like Firebug and Inspector
    as you code in Java.
    </p>
  </div>
</div>
<br/>

<div>
  <img src="images/flow2-sm.png" class="flow-img"/>
  <div class="flow">
    <h4>Optimize</h4>
    <p>
      GWT contains two powerful tools for creating optimized
      web applications. The GWT compiler performs comprehensive optimizations
      across your codebase &mdash; in-lining methods, removing dead code, optimizing
      strings, and more. By setting split-points in the code, it can also segment your download
      into multiple JavaScript fragments, splitting up large applications for faster startup time.
   </p>
   <p>
     Performance bottlenecks aren't limited to JavaScript. Browser layout
     and CSS often behave in strange ways that are hard to diagnose.
     Speed Tracer is a new Chrome Extension in GWT that
     enables you to diagnose performance problems in the browser.
   </p>
  </div>
</div>
<br/>

<div>
  <img src="images/flow4-sm.png" class="flow-img"/>
  <div class="flow">
    <h4>Run</h4>
    <p>
    When you're ready to deploy, GWT compiles your Java source code
    into optimized, stand-alone JavaScript files that automatically
    run on all major browsers, as well as mobile browsers for Android and the iPhone. 
    </p>
  </div>
</div>
<br/>

## Ready to get started?

[![img](images/arrow-md.png)](gettingstarted.html)

[Download the SDK](gettingstarted.html) and get a simple web application up and running. From there, work through the fundamentals of GWT development with the in-depth [tutorials](doc/latest/tutorial/index.html).
