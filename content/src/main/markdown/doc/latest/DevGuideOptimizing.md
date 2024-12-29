Optimizing
===

Once you have your application basically working, it's time to
improve its performance. You can use Speed Tracer to
find out how your application is performing, and you can use a number
of tools to address the specific performance problems that you find.

## Code Splitting

As an AJAX app develops, the JavaScript part of it tends to grow,
eventually to the point that downloading and installing
the JavaScript code adds significant time to the application's startup.
[GWT's code splitter](DevGuideCodeSplitting.html) can speed
up the application's startup by allowing the application to start
running before all of its code is installed.

## Compile Report

When programming in GWT, it can sometimes be difficult to understand
the compiled output. This is especially true for users
of [Code Splitting](DevGuideCodeSplitting.html): why are
some fragments bigger, some smaller? Our answer to these questions are
[Compile Reports](DevGuideCompileReport.html). Compile
Reports let GWT programmers gain insight into what happens in their
application during the compile: how much output their code leads to,
what Java packages and classes lead to large JavaScript output, and
how the code is split up during Code Splitting.  Equipped with this
information, programmers can then modify their application in a
targeted way in order to reduce the size of the entire compiled
application or the size of certain fragments.

## Client Bundle

The resources in a deployed GWT application can be roughly categorized
into resources to never cache (`.nocache.js`), to cache forever
(`.cache.html`), and everything else (`myapp.css`).
[Client Bundles](DevGuideClientBundle.html) allow you to
move resources from the everything-else category into the
cache-forever category.

## Lightweight Metrics

The [Lightweight Metrics](DevGuideLightweightMetrics.html) system is a tool to find key areas where latency may be noticeable to your end users. It has very little overhead, can report metrics on application load time and RPC calls, you can profile multiple GWT modules at the same time, and can be extended for your own measurement needs.  The Debug Panel for GWT uses the Lightweight metrics system. It provides an easy way to collect metrics and test your GWT application.
