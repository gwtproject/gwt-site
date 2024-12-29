Lightweight Metrics
===

The Lightweight Metrics system is a useful tool to find key areas where latency may be noticeable to your end users. Some of the advantages of the system are:

*   It's a negligible cost system with very little overhead
*   It's already wired for reporting metrics on application load time and RPC calls
*   You can profile multiple GWT modules at the same time
*   It can be extended for your own measurement needs

The [Debug Panel for GWT](http://code.google.com/p/gwt-debug-panel/), developed by Google engineers and GWT contributors, uses the Lightweight Metrics system.  It provides an easy way to collect metrics as well as test your GWT application. You can read more details about the features of the tool in this [blog post](http://googlewebtoolkit.blogspot.com/2009/07/introducing-debug-panel-for-gwt.html).

1.  [How it works](#how)
2.  [Measurable events already in-place](#already)
3.  [Extending the Lightweight Metrics system for your own events](#extending)
4.  [Measuring multiple modules simultaneously](#multiple)

## How it works<a id="how"></a>

### Lightweight Metrics System Events

The Lightweight Metrics system is composed of sets of events that you're interested in tracking and a global collector method that is responsible for evaluating these events and reporting metrics.

For example, when loading a GWT application, the steps involved in the process consist of bootstrapping the application, loading external references, and starting up the GWT module. Each of these steps further break down into dowloading the bootstrap script, selecting the right permutation of your application to load, fetching the permutation, and so on. This is illustrated in the [Lightweight Metrics design doc](http://code.google.com/p/google-web-toolkit/wiki/LightweightMetricsDesign) (see GWT Startup Process diagram). Each of the smaller steps, like selecting the correct permutation of the application to load, can be represented as events you would like to measure in the overall application load time. The events themselves are standard JSON objects that contain the following information:

```javascript
{ 
  moduleName : <Module name>,
  subSystem : <Subsystem name>,
  evtGroup : <Event group>,
  millis : <Current time in millis>,
  type : <Event type>
}
```

The `moduleName` is the name of your [GWT module](DevGuideOrganizingProjects.html#DevGuideModules). The `subSystem` refers to the specific component that is emitting these events in your GWT application (for example, the GWT RPC subsystem). The `evtGroup` is analogous to a grouping of related events that can be assumed to follow a serial order. The `millis` field contains the timestamp when the event was emitted, and the `type` field indicates the actual method or step that was run and emitted the event. Each `(moduleName, subSystem, evtGroup, type)` tuple can be interpreted as a checkpoint in an event group.

In the GWT Startup Process, the event for selecting a permutation might look something like:

```javascript
{ 
  moduleName : 'Showcase',
  subSystem : 'startup',
  evtGroup : 'bootstrap',
  millis : new Date().getTime();
  type : 'selectingPermutation'
}
```

### Global Collector Function

The global collector function, named `__gwtStatsEvent()`, is called whenever you want to report an event to measure. It can either be defined directly in your host HTML page, or injected dynamically. As long as it gets defined before your GWT bootstrap script is executed, it will be receive event metrics. From within the global collection function implementation you can decide how you want to display the metrics collected for the given processes you measured. The collector function must return `true` or `false` to indicate whether or not the event was successfully recorded.

Here's an example of what the `__gwtStatsEvent()` function might look like if you wanted to log all the events you have timed in your GWT application:

```html
<head>
  <title>Hello</title>

  <script language='javascript'>
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
          for (var i = 0; i < buffer.length; i++) {
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
  </script>
</head>
<body>
  <div id="log"><h3>Statistics for Events Logged</h3></div>
  <script type="text/javascript" language="javascript" src="hello/hello.nocache.js"></script>
  <iframe src="javascript:''" id="__gwt_historyFrame" style="position:absolute;width:0;height:0;border:0"></iframe>
</body>
```

## Measurable events already in-place<a id="already"></a>

The GWT bootstrap sequence and the GWT RPC mechanism are already instrumented. You can start collecting information about these events simply by defining the global collector function. To see the system in action, add the global collector function in the snippet above to your host HTML page.

## Extending the Lightweight Metrics system for your own events<a id="extending"></a>

You can use the Lightweight Metrics system to measure important events that are specific to your own application. For example, suppose you have a potentially expensive method call somewhere in your entry point `onModuleLoad()` called `createWidget()`. Create the following method that calls the global stats collector function to measure the time it takes for `createWidget()` to execute:

```java
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
```

Next, add calls before and after the code you want to profile in the createWidget() method, as shown below:

```java
public FlexTable createWidget() {
  FlexTable listings = new FlexTable();
  double startTime = Duration.currentTimeMillis();
  StatsEventLogger.logEvent(GWT.getModuleName(), "listings", "loadListings", startTime, "begin");
  loadListings(listings, range);
  double endTime = Duration.currentTimeMillis();
  StatsEventLogger.logEvent(GWT.getModuleName(), "listings", "loadListings", endTime, "end");
  return listings;
}
```

## Measuring multiple modules simultaneously<a id="multiple"></a>

The initial load times as well as RPC execution times for multiple modules can be measured at the same time since they each will make the same call to the `__gwtStatsEvent()` global collector function and pass in their module name as part of the event information reported. This makes it easier to evaluate varying functional implementations of a method and compare their performance. You can also compare groups of events across various modules by grouping them by the evtGroup field allowing you to compare larger interaction implementations directly against each other.
