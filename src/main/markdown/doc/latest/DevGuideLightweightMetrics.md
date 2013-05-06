<p>The Lightweight Metrics system is a useful tool to find key areas where latency may be noticeable to your end users. Some of the advantages of the system are:</p>
<ul>
  <li>It's a negligible cost system with very little overhead</li>
  <li>It's already wired for reporting metrics on application load time and RPC calls</li>
  <li>You can profile multiple GWT modules at the same time</li>
  <li>It can be extended for your own measurement needs</li>
</ul>

<p>The <a href="http://code.google.com/p/gwt-debug-panel/">Debug Panel for GWT</a>, developed by Google engineers and GWT contributors, uses the Lightweight Metrics system.  It provides an easy way to collect metrics as well as test your GWT application. You can read more details about the features of the tool in this <a href="http://googlewebtoolkit.blogspot.com/2009/07/introducing-debug-panel-for-gwt.html">blog post</a>.</p>

<ol class="toc" id="pageToc">
  <li><a href="#how">How it works</a></li>
  <li><a href="#already">Measurable events already in-place</a></li>
  <li><a href="#extending">Extending the Lightweight Metrics system for your own events</a></li>
  <li><a href="#multiple">Measuring multiple modules simultaneously</a></li>
</ol>


<h2 id="how">How it works</h2>

<h3>Lightweight Metrics System Events</h3>

<p>The Lightweight Metrics system is composed of sets of events that you're interested in tracking and a global collector method that is responsible for evaluating these events and reporting metrics.</p>

<p>For example, when loading a GWT application, the steps involved in the process consist of bootstrapping the application, loading external references, and starting up the GWT module. Each of these steps further break down into dowloading the bootstrap script, selecting the right permutation of your application to load, fetching the permutation, and so on. This is illustrated in the <a href="http://code.google.com/p/google-web-toolkit/wiki/LightweightMetricsDesign">Lightweight Metrics design doc</a> (see GWT Startup Process diagram). Each of the smaller steps, like selecting the correct permutation of the application to load, can be represented as events you would like to measure in the overall application load time. The events themselves are standard JSON objects that contain the following information:</p>

<pre class="prettyprint">
{ 
  moduleName : &lt;Module name&gt;,
  subSystem : &lt;Subsystem name&gt;,
  evtGroup : &lt;Event group&gt;,
  millis : &lt;Current time in millis&gt;,
  type : &lt;Event type&gt;
}
</pre>

<p>The <code>moduleName</code> is the name of your <a href="DevGuideOrganizingProjects.html#DevGuideModules">GWT module</a>. The <code>subSystem</code> refers to the specific component that is emitting these events in your GWT application (for example, the GWT RPC subsystem). The <code>evtGroup</code> is analogous to a grouping of related events that can be assumed to follow a serial order. The <code>millis</code> field contains the timestamp when the event was emitted, and the <code>type</code> field indicates the actual method or step that was run and emitted the event. Each <code>(moduleName, subSystem, evtGroup, type)</code> tuple can be interpreted as a checkpoint in an event group.</p>

<p>In the GWT Startup Process, the event for selecting a permutation might look something like:</p>

<pre class="prettyprint">
{ 
  moduleName : 'Showcase',
  subSystem : 'startup',
  evtGroup : 'bootstrap',
  millis : new Date().getTime();
  type : 'selectingPermutation'
}
</pre>

<h3>Global Collector Function</h3>

<p>The global collector function, named <code>__gwtStatsEvent()</code>, is called whenever you want to report an event to measure. It can either be defined directly in your host HTML page, or injected dynamically. As long as it gets defined before your GWT bootstrap script is executed, it will be receive event metrics. From within the global collection function implementation you can decide how you want to display the metrics collected for the given processes you measured. The collector function must return <code>true</code> or <code>false</code> to indicate whether or not the event was successfully recorded.</p>

<p>Here's an example of what the <code>__gwtStatsEvent()</code> function might look like if you wanted to log all the events you have timed in your GWT application:</p>

<pre class="prettyprint">
&lt;head&gt;
  &lt;title&gt;Hello&lt;/title&gt;

  &lt;script language='javascript'&gt;
    function eventToString(event) {
      // return some string representation of this event
      return event.evtGroup + " | " + event.moduleName + " | " + event.subSystem + " | " + event.type + " | " + event.millis;
    }

    window.__gwtStatsEvent = function(event) {
      var loggingDiv = document.getElementById('log');
      if (!loggingDiv) {
        // Our logging div is not yet attached to the DOM
        // Initialize a temporary buffer if needed
        this.buffer = (this.buffer) ? this.buffer : [];
        // log data here
        this.buffer.push(event);
      } else {
        if (this.buffer) {
        // We have some data that was reported before the div was connected
          for (var i = 0; i &lt; buffer.length; i++) {
            // print it all to the div
            var bufferedEvent = buffer[i];
            var logline = document.createElement("div");
            logline.id = "logline";
            logline.innerHTML = eventToString(bufferedEvent);
            loggingDiv.appendChild(logline);
          }
          this.buffer = null;
        }
        // log the current event to the div
        var logline = document.createElement("div");
        logline.id = "logline";
        logline.innerHTML = eventToString(event);
        loggingDiv.appendChild(logline);
      }
      // The collector function should indicate success
      return true;
    }
  &lt;/script&gt;
&lt;/head&gt;
&lt;body&gt;
  &lt;div id="log"&gt;&lt;h3&gt;Statistics for Events Logged&lt;/h3&gt;&lt;/div&gt;
  &lt;script type="text/javascript" language="javascript" src="hello/hello.nocache.js"&gt;&lt;/script&gt;
  &lt;iframe src="javascript:''" id="__gwt_historyFrame" style="position:absolute;width:0;height:0;border:0"&gt;&lt;/iframe&gt;
&lt;/body&gt;
</pre>

<h2 id="already">Measurable events already in-place</h2>

<p>The GWT bootstrap sequence and the GWT RPC mechanism are already instrumented. You can start collecting information about these events simply by defining the global collector function. To see the system in action, add the global collector function in the snippet above to your host HTML page.</p>

<h2 id="extending">Extending the Lightweight Metrics system for your own events</h2>

<p>You can use the Lightweight Metrics system to measure important events that are specific to your own application. For example, suppose you have a potentially expensive method call somewhere in your entry point <code>onModuleLoad()</code> called <code>createWidget()</code>. Create the following method that calls the global stats collector function to measure the time it takes for <code>createWidget()</code> to execute:</p>

<pre class="prettyprint">
public class StatsEventLogger {
  public static native void logEvent(String moduleName, String subSystem,
      String eventGroup, double millis, String type) /*-{
    $wnd.__gwtStatsEvent({
      'moduleName' : moduleName,
      'subSystem' : subSystem,
      'evtGroup' : eventGroup,
      'millis' : millis,
      'type' : type
    });
  }-*/;
}
</pre>

<p>Next, add calls before and after the code you want to profile in the createWidget() method, as shown below:</p>

<pre class="prettyprint">
public FlexTable createWidget() {
  FlexTable listings = new FlexTable();
  double startTime = Duration.currentTimeMillis();
  StatsEventLogger.logEvent(GWT.getModuleName(), "listings", "loadListings", startTime, "begin");
  loadListings(listings, range);
  double endTime = Duration.currentTimeMillis();
  StatsEventLogger.logEvent(GWT.getModuleName(), "listings", "loadListings", endTime, "end");
  return listings;
}
</pre>

<h2 id="multiple">Measuring multiple modules simultaneously</h2>

<p>The initial load times as well as RPC execution times for multiple modules can be measured at the same time since they each will make the same call to the <code>__gwtStatsEvent()</code> global collector function and pass in their module name as part of the event information reported. This makes it easier to evaluate varying functional implementations of a method and compare their performance. You can also compare groups of events across various modules by grouping them by the evtGroup field allowing you to compare larger interaction implementations directly against each other.</p>