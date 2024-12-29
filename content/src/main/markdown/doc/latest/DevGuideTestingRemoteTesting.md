Testing Remote Testing
===

_Running JUnit tests on remote systems_

1.  [Introduction](#Introduction)
2.  [Useful Arguments](#Useful_Arguments)
    *   [-prod](#-prod)
    *   [-userAgents](#-userAgents)
3.  [Run Styles](#Run_Styles)
    *   [Manual](#Manual)
    *   [Selenium](#Selenium)
        *   [Firefox Profile](#Firefox_Profile)
    *   [Remote Web (deprecated)](#Remote_Web)

## Introduction<a id="Introduction"></a>

This document explains how to run GWT tests on remote systems.

There are three types of remote RunStyles that can help you run remote tests:

*   Manual
*   Selenium
*   RemoteWeb

To use any of these run styles, you need to pass the `-runStyle` argument to the test infrastructure
(see [Passing Arguments to the Test Infrastructure](DevGuideTesting.html#passingTestArguments)).  The format looks like this
(see specific examples below):

```shell
-runStyle <NameStartingWithCaps>:arguments
```

If you are running a test from Eclipse, you would add something like the following to the VM arguments (Note that the run style name
starts with a capital letter):

```text
-Dgwt.args="-runStyle Selenium:myhost:4444/*firefox"
```

## Useful Arguments<a id="Useful_Arguments"></a>

The following arguments are useful when running remote tests.

### -prod<a id="-prod"></a>

If you are not familiar with _development_ mode versus _production mode_, you should read the associated tutorials
on [Compiling and Debugging](DevGuideCompilingAndDebugging.html) first. All of the following examples assume that you are running
tests in development mode, which requires that you have the
[GWT Developer Plugin](/missing-plugin/) installed. Its important to note that URLs must be
whitelisted before this plugin will connect to them.  <font color="red">This means that you must allow the remote connection on the remote
system the first time you run the test, or ahead of time if possible.</font>

Tests run in development mode by default. You can run a test in production mode by adding `-prod` to the GWT arguments. When
running tests in production mode, you do not need to have the GWT Developer Plugin installed on the remote system.

```shell
-Dgwt.args="<strong>-prod</strong> -runStyle Selenium:myhost:4444/*firefox"
```

### -userAgents<a id="-userAgents"></a>

When running tests in production mode, GWT compiles the tests for all browsers, which can take a while. If you know which browsers your test
will run in, you can limit the browser permutations (and reduce compile time), using the `-userAgents` argument:

```shell
-Dgwt.args="-prod <strong>-userAgents ie6,gecko1_8</strong> -runStyle Selenium:myhost:4444/*firefox"
```

## Run Styles<a id="Run_Styles"></a>

### Manual<a id="Manual"></a>

The Manual run style allows you to run JUnit tests in any browser by directing the browser to a URL that GWT provides. For details,
see [Running tests in manual mode](DevGuideTesting.html#Manual_Mode). In particular, manual mode can be used for remote testing &mdash;
the browser may be running on a computer different from the one where the tests were started. 
 
### Selenium<a id="Selenium"></a>

_Recommended for Firefox, Safari, Google Chrome, and Internet Explorer (see note)._

**Internet Explorer:** You can try running Internet Explorer in Selenium as it is a supported browser. If the tests work for you,
then you don&#x27;t need to use the RemoteWeb runstyle at all, which should simplify your testing.  However, we&#x27;ve found that
Selenium does not always open Internet Explorer successfully on newer versions of Windows.  If this happens, you can try passing the
`-singleWindow` argument into Selenium.

GWT can execute tests against a remote system running the [Selenium Remote Control](http://seleniumhq.org/projects/remote-control/).
You do this using the following command:

```shell
-Dgwt.args="-runStyle Selenium:myhost:4444/*firefox,myotherhost:4444/*firefox"
```

In the above example, we are using the Selenium run style to execute a development mode test on Firefox against two remote systems (myhost and myotherhost).  

<a id="SeleniumInternetExplorerNote"></a>
  **Note:** On newer versions of Windows, if you run Selenium as an Administrator, you will not run tests in development mode because the GWT
  Developer Plugin is installed for the current user only.

#### Firefox Profile<a id="Firefox_Profile"></a>

By default, Selenium creates a new Firefox profile so it can prevent unnecessary popups that would otherwise mess up the test.  However, you will probably
want to create your own Firefox profile that includes the GWT Developer Plugin.

To do this, run Firefox from the command line and pass in the -ProfileManager argument to open the Profile Manager:

```shell
firefox.exe -ProfileManager
```

Create a new profile (remember the location), and open it. Setup the profile however you want, making sure to install the GWT Developer
Plugin. On our test systems, we use the following settings: 

*   Set a blank homepage
    *   Edit -> Preferences -> Main
    *   Set "When Firefox Starts" to "Show a blank page"
*   Disable warnings
    *   Edit -> Preferences -> Security
    *   Under "Warning Messages" click "Settings"
    *   Uncheck all warnings
*   Disable auto update
    *   Edit -> Preferences -> Advanced -> Update
    *   Uncheck all automatic updates
*   Disable session restore
    *   Type &#x27;about:config&#x27; in the browser bar 
    *   Find browser.sessionstore.resume_from_crash and set it to false
    *   Find browser.sessionstore.enabled and set it to false (if it exists)
*   Whitelist the hosts that will launch the development mode code server. Since Selenium copies the profile for each test, you must do this now.  If you do not, you will have to allow the remote connection for every test!
    *   Restart Firefox 
    *   Tools -> Addons
    *   Select GWT Developer Plugin for Firefox
    *   Click "Options" 
    *   Add the <u>IP address</u> that you want to allow the plugin to connect to.

When starting the selenium server, pass in the following argument to use your firefox profile as a template:

```text
--firefoxProfileTemplate /path/to/profile
```

### Remote Web<a id="Remote_Web"></a>

_Deprecated. Will be removed in GWT 2.7._

The RemoteWeb run style allows you to run tests against systems running the BrowserManagerServer, a server that GWT provides. 

First, you need to start the BrowserManagerServer on the remote test system using the following java command.  Note that gwt-user.jar and gwt-dev.jar are on the classpath.

```shell
java -cp gwt-user.jar;gwt-dev.jar com.google.gwt.junit.remote.BrowserManagerServer ie8 "C:\Program Files\Internet Explorer\IEXPLORE.EXE"
```

BrowserManagerServer takes commands in pairs. In the above example, we are associating the name "ie8" with the executable iexplore.exe.

```text
<browser name> <path/to/browser>
```

To run a test against IE8, you would use the following argument:

```text
-runStyle RemoteWeb:rmi://myhost/ie8
```
