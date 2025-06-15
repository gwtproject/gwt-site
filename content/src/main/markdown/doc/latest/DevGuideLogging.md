Logging
===

This document is for developers interested in logging client-side code in their GWT applications.  Logging is the process of recording events in an application to provide an audit trail to understand how the application executes and to diagnose problems.  Logging makes it easier to troubleshoot issues encountered by developers and users.

The following sections walk through a logging example application and introduce the basic functionality of the Logging framework and configuration options.  Developers should already be familiar with developing a GWT application.

1.  [Overview of the logging framework](#Overview_of_the_Logging_Framework)
2.  [Super Simple Recipe for Adding GWT Logging](#Super_Simple_Recipe_for_Adding_Logging)
3.  [Building/Running the Logging Example](#Building_Running_the_Logging_Example)
4.  [Loggers, Handlers and the Root Logger](#Loggers_Handlers_and_the_Root_Logger)
5.  [Configuring GWT Logging](#Configuring_GWT_Logging)
6.  [Different Types of Handlers](#Different_Types_of_Handlers)
7.  [Client vs. Server-side Logging](#Client_vs_Server_side_Logging)
8.  [Remote Logging](#Remote_Logging)
9.  [Making All Logging Code Compile Out](#Making_All_Logging_Code_Compile_Out)
10.  [Emulated and Non-Emulated Classes](#Emulated_And_Non_Emulated)

## Overview of the Logging Framework<a id="Overview_of_the_Logging_Framework"></a>

The logging framework emulates java.util.logging, so it uses the same syntax and has the same behavior as server-side logging code.  This allows you to share logging code between the client-side and server-side code.  A good overview of java logging is here: [http://download.oracle.com/javase/6/docs/technotes/guides/logging/overview.html](http://download.oracle.com/javase/6/docs/technotes/guides/logging/overview.html); you should familiarize yourself with java.util.logging to get a good feel for how to use GWT logging.

Unlike java.util.logging, GWT logging is configured using .gwt.xml files.  You can use these files to enable/disable logging entirely, enable/disable particular handlers, and change the default logging level.  When logging is disabled, the code will compile out, so you can use logging when developing/debugging, and then have your production build compile it out to keep your JavaScript size small.

## Super Simple Recipe for Adding GWT Logging<a id="Super_Simple_Recipe_for_Adding_Logging"></a>

Adding GWT logging is really quite simple, as simple as the following code example. However &mdash; understanding how logging works, and how to correctly configure it is important, so please do take the time to read the rest of this document.

```
# In your .gwt.xml file
    <inherits name="com.google.gwt.logging.Logging"/>

    # In your .java file
    Logger logger = Logger.getLogger("NameOfYourLogger");
    logger.log(Level.SEVERE, "this message should get logged");
```

**Warning about inheriting logging:** If you do not inherit `com.google.gwt.logging.Logging`, then logging will technically work, since the emulation of the Java code is always present. However, you will not get any default Handlers, or any ability to configure the Root Logger (as discussed in this document).  Not inheriting Logging is sometimes done by libraries that want to log errors or information but do not want to control how a customer application would display that information (they don't want to configure or turn on logging, but they do want to make logging information available if a user of the library does turn on logging).

## Building/Running the Logging Example<a id="Building_Running_the_Logging_Example"></a>

You build and run LogExample the same way you would build and run any of the other GWT samples, like Hello.  The eclipse directory in the svn source contains a README.txt file to help.  When you run it, the following "Logging Example" web page appears with popup menus and checkboxes for triggering test exceptions and seeing how they log.

[![Logging Example web page](images/LoggingExample.png)](images/LoggingExample.png)

LogExample is configured using [`LogExample.gwt.xml`](https://github.com/gwtproject/gwt/blob/3c49dc1b8ac1116d3436e87637c1daeb107d7ea3/samples/logexample/src/com/google/gwt/sample/logexample/LogExample.gwt.xml).
The entry point for the app is [`LogExample.java`](https://github.com/gwtproject/gwt/blob/3c49dc1b8ac1116d3436e87637c1daeb107d7ea3/samples/logexample/src/com/google/gwt/sample/logexample/client/LogExample.java) &mdash; it simply creates and adds the various demo modules to the page.  Each of these modules illustrates a different set of logging concepts; this tutorial will walk you through them.

## Loggers, Handlers and the Root Logger<a id="Loggers_Handlers_and_the_Root_Logger"></a>

Loggers are organized in a tree structure, with the Root Logger at the root of the tree.  Parent/Child relationships are determined by the name of the logger, using "." to separate sections of the name. So if we have two loggers Foo.Bar and Foo.Baz, then they are siblings, with their parent being the logger named Foo.  The Foo logger (and any logger with a name which does not contain a dot ".") has the Root Logger as a parent.

When you log a message to a logger, if the Level of the message is high enough, it will pass the message on to its parent, which will pass it on to its parent, and so on, until the Root Logger is reached.  Along the way, any given logger (including the Root Logger) will also pass the message to any of its Handlers, and if the Level of the message is high enough, those handlers will output the message in some way (to a popup, to stderr, etc.). For a much more detailed explanation of this, see [http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/overview.html](http://java.sun.com/j2se/1.4.2/docs/guide/util/logging/overview.html).

If you open [`LogExample.java`](https://github.com/gwtproject/gwt/blob/3c49dc1b8ac1116d3436e87637c1daeb107d7ea3/samples/logexample/src/com/google/gwt/sample/logexample/client/LogExample.java) you can see that we've created 3 loggers:

```
// <a href="https://github.com/gwtproject/gwt/blob/3c49dc1b8ac1116d3436e87637c1daeb107d7ea3/samples/logexample/src/com/google/gwt/sample/logexample/client/LogExample.java">LogExample.java</a>

  private static Logger childLogger = Logger.getLogger("ParentLogger.Child");
  private static Logger parentLogger = Logger.getLogger("ParentLogger");
  private static Logger rootLogger = Logger.getLogger("");
```

We've passed these 3 loggers into [`LoggerController`](https://github.com/gwtproject/gwt/blob/3c49dc1b8ac1116d3436e87637c1daeb107d7ea3/samples/logexample/src/com/google/gwt/sample/logexample/client/LoggerController.java), which in turn, creates an instance of
[`OneLoggerController`](https://github.com/gwtproject/gwt/blob/3c49dc1b8ac1116d3436e87637c1daeb107d7ea3/samples/logexample/src/com/google/gwt/sample/logexample/client/OneLoggerController.java) for each of them.  In `OneLoggerController.java` you can see example code for changing the Level of the logger, logging to the logger, and logging an exception to the logger.

```
// <a href="https://github.com/gwtproject/gwt/blob/3c49dc1b8ac1116d3436e87637c1daeb107d7ea3/samples/logexample/src/com/google/gwt/sample/logexample/client/OneLoggerController.java">OneLoggerController</a>

  // Change the level of the logger
  @UiHandler("levelTextBox")
  void <b>handleLevelClick</b>(ChangeEvent e) {
    Level level = Level.parse(levelTextBox.getItemText(
        levelTextBox.getSelectedIndex()));
    logger.log(Level.SEVERE,
        "Setting level to: " + level.getName());
    <b>logger.setLevel(level);</b>
  }

  // Log a message to the logger
  @UiHandler("logButton")
  void <b>handleLogClick</b>(ClickEvent e) {
    Level level = Level.parse(logTextBox.getItemText(
        logTextBox.getSelectedIndex()));
    <b>logger.log(level, "This is a client log message");</b>
  }

  // Trigger an exception and log it to the logger
  @UiHandler("exceptionButton")
  void <b>handleExceptionClick</b>(ClickEvent e) {
    try {
      Level n = null;
      n.getName();
    } catch (NullPointerException ex) {
      <b>logger.log(Level.SEVERE, "Null Exception Hit", ex);</b>
    }
  }
```

You can play around with these 3 loggers. For now, the easiest place to look for log messages is the popup created in LogExample.java on the web page <!-- doog: add a link --> (different Handlers are discussed in the next section).

## Configuring GWT Logging<a id="Configuring_GWT_Logging"></a>

In most simple logging implementations, we simply deal with the Root Logger.  All messages get propagated up the tree to the Root Logger.  The Level and Handlers attached to the Root Logger are what control which messages get logged and to where.  This is the basic idea behind the default Handler configurations in GWT logging.

The simplest item you can configure is the Level of the Root Logger.  You can do this by adding `logLevel` query parameter to your URL.  Try this now, by adding "&amp;logLevel=SEVERE" to the sample URL.  Notice how the default level of all of the loggers is now set to SEVERE rather than to INFO.

The other way to configure GWT logging is through a .gwt.xml file as follows:

```
# LogExample.gwt.xml

    <set-property name="gwt.logging.logLevel" value="SEVERE"/>          # To change the  default logLevel
    <set-property name="gwt.logging.enabled" value="FALSE"/>            # To disable logging
    <set-property name="gwt.logging.consoleHandler" value="DISABLED"/>  # To disable a default Handler
```

You can experiment with configuring logging in the provided LogExample.gwt.xml file.

## Different Types of Handlers<a id="Different_Types_of_Handlers"></a>

GWT logging comes with a set of Handlers already defined and (by default) attached to the Root Logger. You can disable these handlers in the .gwt.xml file as discussed above, extend them, attach them to other loggers, and so forth.  You can experiment with adding/removing the various Handlers from the Root Logger using the checkboxes &mdash; the code behind this in `HandlerController.java`.  Note that if you disable a Handler using the .gwt.xml file, any instance of it will be replaced with a NullLogHandler (which does nothing and compiles out), so the handler cannot be added/removed using the checkboxes.

Here's an example of how a checkbox adds or removes a handler:

```
// <a href="https://github.com/gwtproject/gwt/blob/3c49dc1b8ac1116d3436e87637c1daeb107d7ea3/samples/logexample/src/com/google/gwt/sample/logexample/client/HandlerController.java">HandlerController.java</a>

    public void onValueChange(ValueChangeEvent<Boolean> event) {
      if (checkbox.getValue()) {
        <b>logger.addHandler(handler);</b>
      } else {
        <b>logger.removeHandler(handler);</b>
      }
    }
```

Most of the default Handlers are very straightforward

*   `SystemLogHandler` - Logs to stdout.  These messages can only be seen in Development Mode &mdash; look for them in the DevMode window
*   `DevelopmentModeLogHandler` - Logs by calling method GWT.log. These messages can only be seen in Development mode &mdash; look for them in the DevMode window
*   `ConsoleLogHandler` - Logs to each browser's own built-in Dev Tools console
*   `PopupLogHandler` - Logs to the popup which appears in the upper left hand corner
*   `SimpleRemoteLogHandler` - Discussed below, in the Remote Logging section

Although the `PopupLogHandler` is easy to use, it is also a
bit invasive.  A better solution for most apps is to disable
the `PopupLogHandler` and instead send the log messages to
a Panel somewhere in your app. GWT logging is set up to make this
easy, and you can see an example of this
in `CustomLogArea.java`.  In this case, we have created
a `VerticalPanel` (although any widget which extends
[HasWidgets](/javadoc/latest/com/google/gwt/user/client/ui/HasWidgets.html)
and supports multiple `add()` calls can be used).
Once we have one of these widgets, we simply pass it into the constructor
of a `HasWidgetsLogHandler` and add that Handler to a logger.

```
// <a href="https://github.com/gwtproject/gwt/blob/3c49dc1b8ac1116d3436e87637c1daeb107d7ea3/user/src/com/google/gwt/user/client/ui/VerticalPanel.java">VerticalPanel.java</a>

    VerticalPanel customLogArea;

    // An example of adding our own custom logging area.  Since VerticalPanel extends HasWidgets,
    // and handles multiple calls to add(widget) gracefully we simply create a new HasWidgetsLogHandler
    //  with it, and add that handler to a logger. In this case, we add it to a particular logger in order
    //  to demonstrate how the logger hierarchy works, but adding it to the root logger would be fine.
    logger.addHandler(new HasWidgetsLogHandler(customLogArea));
```

## Client vs. Server-side Logging<a id="Client_vs_Server_side_Logging"></a>

Although GWT emulates java.util.logging, it is important to understand the difference between server-side and client-side logging.  Client-side logging logs to handlers on the client side, while server-side logging logs to handlers on the server side.

To make this clear, the client-side GWT code has a Root Logger (and logger hierarchy) that is separate from the server-side code;  all of the handlers discussed above are only applicable to client-side code.  If code shared by the client and server makes logging calls, then which Root Logger (and logger hierarchy) it logs to will depend on whether it is being executed on the client or server side.  You should not add or manipulate Handlers in shared code, since this will not work as expected.

In [`ServerLoggingArea.java`](https://github.com/gwtproject/gwt/blob/3c49dc1b8ac1116d3436e87637c1daeb107d7ea3/samples/logexample/src/com/google/gwt/sample/logexample/client/ServerLoggingArea.java), you can experiment with these concepts.  The buttons in that section will trigger logging calls on the server, as well as logging calls in [`SharedClass.java`](https://github.com/gwtproject/gwt/blob/3c49dc1b8ac1116d3436e87637c1daeb107d7ea3/samples/logexample/src/com/google/gwt/sample/logexample/shared/SharedClass.java) from both the client and server side.  Note the slight differences in formatting between client-side and server-side logging, as well as the different handlers each is logged to (in the tutorial, server-side logging will simply log to stderr, while client-side logging will log to all of the Handlers discussed above).

## Remote Logging<a id="Remote_Logging"></a>

In order for events that are logged by client-side code to be stored on the server side, you need to use a `RemoteLogHandler`.  This handler will send log messages to the server, where they will be logged using the server-side logging mechanism. GWT currently contains a `SimpleRemoteLogHandler` which will do this in the simplest possible way (using GWT-RPC) and no intelligent batching, exponential backoffs in case of failure, and so forth.  This logger is disabled by default, but you can enable it in the .gwt.xml file (see the section on Handlers above for more details on configuring the default Handlers).

```
# <a href="https://github.com/gwtproject/gwt/blob/3c49dc1b8ac1116d3436e87637c1daeb107d7ea3/samples/logexample/src/com/google/gwt/sample/logexample/LogExample.gwt.xml">LogExample.gwt.xml</a>

  <set-property name="gwt.logging.simpleRemoteHandler" value="ENABLED" />
```

You will also need to serve the remoteLoggingServlet.

## Making All Logging Code Compile Out<a id="Making_All_Logging_Code_Compile_Out"></a>

When logging is disabled, the compiler will used Deferred Binding to substitute Null implementations for the Logger and Level classes. Since these implementations just return Null, and do nothing, they will generally get trimmed by the GWT compiler (which does a pretty good job of removing useless code).  However, it is not guaranteed that other code you write related to logging will compile out.  If you want to guarantee that some chunk of code is removed when logging is disabled, you can use the `LogConfiguration.loggingIsEnabled()` method:

```
if (LogConfiguration.loggingIsEnabled()) {
    String logMessage = doSomethingExpensiveThatDoesNotNormallyCompileOut();
    logger.severe(logMessage);
  }
```

Code that normally compiles out will still be present in Development mode.  You can  use the same condition as above to hide code from Development Mode, as shown here:

```

// <a href="https://github.com/gwtproject/gwt/blob/3c49dc1b8ac1116d3436e87637c1daeb107d7ea3/samples/logexample/src/com/google/gwt/sample/logexample/client/CustomLogArea.java">VerticalPanel.java</a>

    // Although this code will compile out without this check in web mode, the guard will ensure
    // that the handler does not show up in development mode.
    if (LogConfiguration.loggingIsEnabled()) {
      logger.addHandler(new HasWidgetsLogHandler(customLogArea));
    }

```
## Emulated and Non-Emulated Classes<a id="Emulated_And_Non_Emulated"></a>

The GWT logging framework does not emulate all parts of java.util.logging.
See [JRE Emulation Reference](RefJreEmulation.html#Package_java_util_logging) for a list of the emulated classes and members.

The following Handlers and Formatters are provided:

```

HTMLFormatter
  TextFormatter
  SystemLogHandler
  ConsoleLogHandler
  DevelopmentModeLogHandler
  HasWidgetsLogHandler (and LoggingPopup to use with it)
```

