Tutorials: Overview
===

These tutorials are intended for developers who wish to write rich AJAX applications using Google Web Toolkit. You might be a Java developer who would like to be able to apply the software engineering principles of object-oriented programming and leverage the tools in your Java IDE when writing applications for the web. Or you might be a JavaScript guru curious about GWT's ability to generate highly optimized JavaScript with permutations for multiple browsers.

Although a knowledge of HTML, CSS, and Java is assumed, it is not required to run these tutorials.

## Before You Begin <a id="prerequisites"></a>

Before you begin these tutorials, we assume that you've done the following:

*   Installed the Java SDK.
    *  If you don't have a recent version of the Java SDK installed, download and install [Sun Java Standard Edition SDK](http://java.sun.com/javase/downloads/index.jsp).
*   Installed Eclipse or your favorite Java IDE.
    *  In these tutorials, we use [Eclipse](http://www.eclipse.org/) because it is open source. However, GWT does not tie you to Eclipse. You can use [IntelliJ](http://www.jetbrains.com/idea/), [NetBeans](http://www.netbeans.org/) or any Java IDE you prefer. If you use a Java IDE other than Eclipse, the screenshots and some of the specific instructions in the tutorial will be different, but the basic GWT concepts will be the same.
    *  If your Java IDE does not include Apache Ant support, you can download and unzip [Ant](http://ant.apache.org) to easily compile and run GWT applications.
*   Installed the Google Plugin for Eclipse.
    *  The [Google Plugin for Eclipse](https://developers.google.com/appengine/docs/java/tools/eclipse) adds functionality to Eclipse for creating and developing GWT applications.
*   Downloaded Google Web Toolkit.
    *  Google Web Toolkit can be downloaded with the Google Plugin for Eclipse.  Alternatively, download the most recent distribution of [Google Web Toolkit](../../../download.html) for your operating system.
*   Unzipped the GWT distribution in directory you want to run it in.
    *  GWT does not have an installation program. All the files you need to run and use GWT are located in the extracted directory.

You may also optionally do the following:

*   Install the Google App Engine SDK.
    *  Google App Engine allows you to run Java web applications, including GWT applications, on Google's infrastructure.  The App Engine SDK can be downloaded with the Google Plugin for Eclipse.  Alternatively, download the [App Engine SDK](https://developers.google.com/appengine/downloads) for Java separately.
*   [Create and run your first web application](../../../gettingstarted.html#create) - A few, simple steps to familiarize you with the command line commands.

## GWT Tutorials <a id="gwt_tutorials"></a>

### Build a Sample GWT Application

*   [Build a Sample GWT Application](gettingstarted.html)
    *  Get started with Google Web Toolkit by developing the StockWatcher application from scratch. You'll learn to create a GWT project, build the UI with GWT wigdets and panels, code the client-side functionality in the Java language, debug in development mode, apply CSS styles, compile the Java into JavaScript, and run the application in production mode.

### Client-Server Communication

*   Communicating with the server via [GWT RPC](RPC.html)
    *  Add a call to a server using GWT RPC. You'll learn how to make asynchronous calls, serialize Java objects, and handle exceptions.
*   [Retrieving JSON data via HTTP](JSON.html)
    *  Make HTTP requests to retrieve JSON data from a server. The same technique can be used to retrieve XML data.
*   [Making cross-site requests](Xsite.html)
    *  Make a call to a remote server, working around SOP (Same Origin Policy) constraints.

### Internationalization

*   [Internationalizing a GWT application](i18n.html)
    *  Translate the user interface of a GWT application into another language using Static String Internationalization.

### JUnit Testing

*   [Unit testing with JUnit](JUnit.html)
    *  Add unit tests to a GWT application using JUnit.

### Deploying to Google App Engine

*   [Deploying to Google App Engine](appengine.html)
    *  Deploy a GWT application to [App Engine](https://developers.google.com/appengine).
