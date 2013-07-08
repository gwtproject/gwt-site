

<style>
code, .code {font-size: 9pt; font-family: Courier, Courier New, monospace; color:#007000;}
.highlight {background-color: #ffc;}
.strike {text-decoration:line-through; color:red;}
.header {margin-top: 1.5ex;}
.details {margin-top: 1ex;}
</style>

<p>
At this point, you've created the initial implementation of the StockWatcher application and it seems pretty stable.
</p>
<p>
As the code base evolves, how can you ensure that you don't break existing functionality? The solution is unit testing.
Creating a battery of good unit test cases is an important part of ensuring the quality of your application over its lifecycle.
</p>
<p>
To aid you with your testing efforts, GWT provides integration with the open-source <a href="http://www.junit.org/">JUnit</a> testing framework.
You'll be able to create units test that you can run in both development mode and production mode.
</p>
<p>
In this section, you'll add a JUnit unit test to the StockWatcher project.
</p>
<ol>
    <li><a href="#create">Creating a JUnit test class for StockWatcher and the scripts to run it</a></li>
    <li><a href="#run">Running unit tests</a></li>
    <li><a href="#write">Writing a unit test</a></li>
    <li><a href="#resolve">Resolving problems identified with unit tests</a></li>
</ol>

<p class="note" style="margin-left: 1.2em; margin-right: 1.5em;">
<b>Note:</b> For a broader guide to JUnit testing, see <a href="../DevGuideTesting.html">JUnit Testing</a>.
</p>

<h2>Before you begin</h2>
<h3>The StockWatcher project</h3>
<p>
This tutorial builds on the GWT concepts and the StockWatcher application created in the <a href="gettingstarted.html">Build a Sample GWT Application</a> tutorial. If you have not completed the Build a Sample GWT Application tutorial and are familiar with basic GWT concepts, you can import the StockWatcher project as coded to this point.
</p>
<ol class="instructions">
    <li>
        <div class="header">Download the <a href="http://code.google.com/p/google-web-toolkit/downloads/detail?name=Tutorial-GettingStarted-2.1.zip">StockWatcher project</a>.</div>
    </li>
    <li>
        <div class="header">Unzip the file.</div>
    </li>
    <li>
        <div class="header">Import the project into Eclipse</div>
        <ol>
            <li>From the <code>File</code> menu, select the  <code>Import...</code> menu option.</li>
            <li>Select the import source General &gt; Existing Projects into Workspace. Click the <code>Next</code> button.</li>
            <li>For the root directory, browse to and select the StockWatcher directory (from the unzipped file). Click the <code>Finish</code> button.</li>
        </ol>
    </li>
</ol>
<p>
If you are using ant, edit the <code>gwt.sdk</code> property in StockWatcher/build.xml to point to where you unzipped GWT.
</p>

If you are using ant, replace all references to <code>path_to_the_junit_jar</code> to point to the location of junit on your system.

<p class="note">
<b>Note:</b> If you just completed the Build a Sample GWT Application tutorial but did not specify the <code>-junit</code> option with webAppCreator, uncomment the <code>javac.tests</code>, <code>test.dev</code>, <code>test.prod</code>, and <code>test</code> targets in StockWatcher/build.xml and 
replace all references to <code>path_to_the_junit_jar</code> to point to the location of junit on your system before continuing.
</p>

<a name="create"></a>
<h2>1. Creating a JUnit test</h2>
<p>
When you specified the <code>-junit</code> option, webAppCreator created all the files you need to begin developing JUnit tests. It generates a starter test class, ant targets to run the tests from the command line, and launch configuration files for Eclipse.
</p>
<p class="note">Starting with GWT 2.0, the former command-line tool junitCreator has been combined into webAppCreator.</p>
<p>
In Eclipse, navigate down to the StockWatcherTest class by expanding the
<code>test/</code> directory. If you are adding JUnit testing to an existing
application you will need add the <code>test/</code> directory as a source
folder within you Eclipse project and update your Build Path to include a
reference to an existing JUnit library.
<p>
<div class="details"><img src="images/JUnitcreatetest.png" alt="screenshot: JUnit tests in Package Explorer" /></div>
</p>
<h3>Examining the test class: StockWatcherTest.java</h3>
<p>
Take a look inside StockWatcherTest.java.
This test class was generated in the com.google.gwt.sample.stockwatcher.client package under the StockWatcher/test directory.
This is where you will write the unit tests for StockWatcher.
Currently it contains a single, simple test: the method testSimple.
</p>
<ol class="instructions">
    <li>
        <div class="header">Open the StockWatcherTest.java file.</div>

        <div class="details"><pre class="code">
package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.junit.client.GWTTestCase;

/**
 * GWT JUnit tests must extend GWTTestCase.
 */
public class StockWatcherTest <span class="highlight">extends GWTTestCase</span> {                       // <span style="color:black;"><b>(1)</b></span>

  /**
   * Must refer to a valid module that sources this class.
   */
  public String getModuleName() {                                         // <span style="color:black;"><b>(2)</b></span>
    <span class="highlight">return "com.google.gwt.sample.stockwatcher.StockWatcher";</span>
  }

  /**
   * Add as many tests as you like.
   */
  public void testSimple() {                                              // <span style="color:black;"><b>(3)</b></span>
    <span class="highlight">assertTrue(true);</span>
  }

}</pre>
    </li>
</ol>
<h4>Notes</h4>
<p>
(1) Like all GWT JUnit test cases, the StockWatcherTest class extends the <a href="/javadoc/latest/com/google/gwt/junit/client/GWTTestCase.html">GWTTestCase</a> class in the com.google.gwt.junit.client package.
You can create additional test cases by extending this class. 
</p>
<p>
(2) The StockWatcherTest class has an abstract method (getModuleName) that must return the name of the GWT module. For StockWatcher, that is com.google.gwt.sample.stockwatcher.StockWatcher.
</p>
<p>
(3) The StockWatcherTest class is generated with one sample test case&mdash;a tautological test, testSimple.
This testSimple method uses one of the many assert* functions that it inherits from the JUnit Assert class, which is an ancestor of GWTTestCase.
The assertTrue(boolean) function asserts that the boolean argument passed in evaluates to true. If not, the testSimple test will fail when run in JUnit.
</p>
<a name="run"></a>
<h2>2. Running unit tests</h2>
<p>
Before you start writing your own unit tests for StockWatcher, make sure the components of the test environment are in place. You can do that by running StockWatcherTest.java which will execute the starter test, testSimple.
</p>
<p>
You can run JUnit tests four ways:
</p>
<ul>
    <li>from the command line&mdash;using the scripts generated by junitCreator</li>
    <li>in Eclipse&mdash;using the Google Plugin for Eclipse</li>
    <li>in Eclipse&mdash;using the Eclipse launch configuration files generated by webAppCreator</li>
    <li>in manual test mode</li>
</ul>

<h3>From the command line</h3>
<p>
The build.xml file generated by GWT webAppCreator includes three generated targets used for testing:
<ul>
    <li>
        <div class="header">test.dev</div>
        <div class="details">Runs all test classes in development mode.</div>
    </li>
    <li>
        <div class="header">test.prod</div>
        <div class="details">Runs all test classes in production mode.</div>
    </li>
    <li>
        <div class="header">test</div>
        <div class="details">Runs both test.dev and test.prod</div>
    </li>
</ul>
</p>
<p class="note">
<b>Note:</b> In order to run tests from the command-line, you will need to have <a href="http://www.junit.org/">JUnit</a> installed on your system.
If you just downloaded the StockWatcher project, you must first open build.xml and replace all references to <code>/path/to/junit-3.8.1.jar</code>
with the path to junit on your system.
</p>
<ol class="instructions">
    <li>
        <div class="header">In the command shell, browse to the StockWatcher directory.</div>
    </li>
    <li>
        <div class="header">Run the JUnit test in development mode.</div>
        <div class="details">At the command line, enter <code>ant test.dev</code></div>
    </li>
    <li>
        <div class="header">The test runs as Java bytecode in a JVM.</div>
        <div class="details">The simpleTest executes without error.</div>
        <div class="details"><pre class="code">
&#91;junit&#93; Running com.google.gwt.sample.stockwatcher.client.StockWatcherTest
&#91;junit&#93; Tests run: 1, Failures: 0, Errors: 0, Time elapsed: 15.851 sec</pre></div>
    </li>
    <li>
        <div class="header">Run the JUnit test in production mode.</div>
        <div class="details">At the command line, enter <code>ant test.prod</code></div>
    </li>
    <li>
        <div class="header">The test runs as compiled JavaScript.</div>
        <div class="details">The simpleTest executes without error.</div>
        <div class="details"><pre class="code">
&#91;junit&#93; Running com.google.gwt.sample.stockwatcher.client.StockWatcherTest
&#91;junit&#93; Tests run: 1, Failures: 0, Errors: 0, Time elapsed: 37.042 sec</pre></div>
    </li>
</ol>

<h3>In Eclipse (using the Google Pluging for Eclipse)</h3>
<p>
The Google Plugin for Eclipse makes it easy to run tests in Eclipse.
</p>
<ol>
    <li>
        <div class="header">Run the JUnit test in development mode.</div>
        <div class="details">From Package Explorer, right click on the test case you want to run, select <code>Run As &gt; GWT Junit Test</code></div>
    </li>
    <li>
        <div class="header">The simpleTest executes without error.</div>
        <div class="details"><img src="images/JUnitSimpleTestDev.png" alt="screenshot: JUnit tests in Eclipse" /></div>
    </li>
    <li>
        <div class="header">Run the JUnit test in production mode.</div>
        <div class="details">From Package Explorer, right click on the test case you want to run, select <code>Run As &gt; GWT Junit Test (production mode)</code></div>
    </li>
    <li>
        <div class="header">The simpleTest executes without error.</div>
    </li>
</ol>

<h3>In Eclipse (using generated launch configurations)</h3>
<p>
You can run unit tests in Eclipse using the launch configurations generated by webAppCreator for both development mode and production mode.
</p>

<ol class="instructions">
    <li>
        <div class="header">Run the JUnit test in development mode.</div>
        <div class="details">From the Eclipse menu bar, select <code>Run &gt; Run Configurations...</code></div>
    </li>
    <li>
        <div class="header">In the Run window, select <code>StockWatcherTest-dev</code></div>
    </li>


    <li>
        <div class="header">If you are using Mac OSX, add the argument to invoke the Java virtual machine.</div>
        <div class="details">Display the Arguments tab.</div>
        <div class="details">At VM argument, enter <code>-XstartOnFirstThread -Xmx256M</code></div>
        <div class="details">To save the changes to the Arguments, press <code>Apply</code></div>
        <div class="details">To run the test, press <code>Run</code></div>
    </li>
    <li>
        <div class="header">The simpleTest executes without error.</div>
        <div class="details"><img src="images/JUnitSimpleTestDev.png" alt="screenshot: JUnit tests in Eclipse" /></div>
    </li>
        <li>
        <div class="header">Run the JUnit test in production mode.</div>
        <div class="details">From the Eclipse menu bar, select <code>Run &gt; Run Configurations...</code></div>
    </li>
    <li>
        <div class="header">In the Run window, select <code>StockWatcherTest-prod</code></div>
    </li>
    <li>
        <div class="header">If you are using Mac OSX, add the argument to invoke the Java virtual machine.</div>
        <div class="details">Display the Arguments tab.</div>
        <div class="details">At VM argument, enter <code>-XstartOnFirstThread -Xmx256M</code></div>
        <div class="details">To save the changes to the Arguments, press <code>Apply</code></div>
        <div class="details">To run the test, press <code>Run</code></div>
    </li>
    <li>
        <div class="header">The simpleTest executes without error.</div>
    </li>

</ol>


<h3>In manual test mode</h3>
<p>
If you want to select the browser on which to run unit tests, use manual test mode.

In manual test mode, the JUnitShell main class runs as usual on a specified GWT module, but instead of running the test immediately, it prints out a URL and waits for a browser to connect. You can manually cut and paste this URL into the browser of your choice, and the unit tests will run in that browser.

<p class="note">
<b>In Depth:</b> To learn how to run unit tests in manual mode, see the Developer's Guide, <a href="../DevGuideTesting.html#DevGuideJUnitCreation">Creating a Test Case</a>.
</p>


<a name="write"></a>
<h2>3. Writing a unit test</h2>
<p>
In a real testing scenario, you would want to verify the behavior of as much of StockWatcher as possible. You could add a number of unit tests to the StockWatcherTest class. You would write each test in the form of a public method.
</p>
<p>
If you had a large number of test cases, you could organize them by grouping them into different test classes.
</p>
<p>
However, to learn the process of setting up JUnit tests in GWT, in this tutorial you'll write just one test and run it.
</p>
<ol class="instructions">
    <li>
        <div class="header">Write a JUnit test to verify that the constructor of the StockPrice class is correctly setting the new object's instance fields.</div>
        <div class="details">To the StockWatcherTest class, add the testStockPriceCtor method as shown below.</div>
        <div class="details"><pre class="code">
/**
 * Verify that the instance fields in the StockPrice class are set correctly.
 */
public void testStockPriceCtor() {
  String symbol = "XYZ";
  double price = 70.0;
  double change = 2.0;
  double changePercent = 100.0 * change / price;

  StockPrice sp = new StockPrice(symbol, price, change);
  assertNotNull(sp);
  assertEquals(symbol, sp.getSymbol());
  assertEquals(price, sp.getPrice(), 0.001);
  assertEquals(change, sp.getChange(), 0.001);
  assertEquals(changePercent, sp.getChangePercent(), 0.001);
}</pre></div>
    </li>
    <li>
        <div class="header">Rerun StockWatcherTest in development mode.</div>
        <div class="details">Both tests should pass.</div>
        <div class="details"><pre class="code">
&#91;junit&#93; Running com.google.gwt.sample.stockwatcher.client.StockWatcherTest
&#91;junit&#93; Tests run: 2, Failures: 0, Errors: 0, Time elapsed: 16.601 sec</pre></div>
    </li>
</ol>


<a name="resolve"></a>
<h2>4. Resolving problems identified in unit tests</h2>
<p>
To see what happens when a unit test fails, you'll reintroduce the arithmetic bug you fixed in the <a href="debug.html">Build a Sample GWT Application</a> tutorial. Originally, the  percentage of change was not calculated correctly in the getChangePercent method. To make the unit test fail, you'll break this bit of code again.
</p>
<ol class="instructions">
    <li>
        <div class="header">Introduce a bug into StockWatcher.</div>
        <div class="details">In StockPrice.java, make the change highlighted below.</div>
        <div class="details"><pre class="code">
  public double getChangePercent() {
    return <span class="highlight">10.0</span> * this.change / this.price;
  }
</pre></div>
    </li>
        <li>
        <div class="header">Run StockWatcherTest in development mode (either in Eclipse of from the command line).</div>
        <div class="details">JUnit identifies the failed test case (testStockPriceCtor).<br />(Output if run in Eclipse.)</div>
        <div class="details"><img src="/web-toolkit/doc/latest/tutorial/images/JUnitFailed.png" alt="screenshot: JUnit tests failed" /></div>
        <div class="details">...and provides a full-stack trace for the exception that resulted&mdash;an AssertionFailedError.<br />(Output if run from the command line.)</div>
        <div class="details"><pre class="code">
Testsuite: com.google.gwt.sample.stockwatcher.client.StockWatcherTest
Tests run: 2, Failures: 1, Errors: 0, Time elapsed: 16.443 sec

Testcase: testSimple took 16.238 sec
Testcase: testStockPriceCtor took 0.155 sec
  FAILED
Remote test failed at 172.29.212.75 / Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.9.0.1) Gecko/2008070208 Firefox/3.0.1
 expected=2.857142857142857 actual=0.2857142857142857 delta=0.0010
junit.framework.AssertionFailedError: Remote test failed at 172.29.212.75 / Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.9.0.1) Gecko/2008070208 Firefox/3.0.1
 expected=2.857142857142857 actual=0.2857142857142857 delta=0.0010
  at com.google.gwt.sample.stockwatcher.client.StockWatcherTest.testStockPriceCtor(StockWatcherTest.java:38)
  at com.google.gwt.sample.stockwatcher.client.__StockWatcherTest_unitTestImpl.doRunTest(__StockWatcherTest_unitTestImpl.java:7)
  ...
</pre></div>
<p class="note">
<b>Note:</b> When running from the command line, complete test reports (including stack traces) will be located in the <code>reports/htmlunit.dev/</code> directory for development mode
tests and <code>reports/htmlunit.prod/</code> directory for production mode tests. 
</p>
    </li>
    <li>
        <div class="header">Correct the error.</div>
    </li>
    <li>
        <div class="header">Rerun the tests.</div>
        <div class="details">Both the JUnit tests should complete successfully again.</div>
        <div class="details"><pre class="code">
&#91;junit&#93; Running com.google.gwt.sample.stockwatcher.client.StockWatcherTest
&#91;junit&#93; Tests run: 2, Failures: 0, Errors: 0, Time elapsed: 16.114 sec</pre></div>
    </li>
</ol>

<p class="note">
<b>Best Practices:</b> Because there can be subtle differences between the way GWT applications work when compiled to JavaScript and when running as Java bytecode, make sure you run your unit tests in both development and production modes as you develop your application. Be aware, though, that if your test cases fail when running in production mode, you won't get the full stack trace that you see in development mode.
</p>

<h2 id="more_about_testing">More about testing</h2>
<p>
At this point, you've created a JUnit test for StockWatcher and added a simple test case to it.
</p>
<p>
To learn more about all kinds of unit testing in GWT, see the Developer's Guide:
</p>
<ul>
    <li><a href="../DevGuideTesting.html#DevGuideJUnitCreation">Creating a Test Case</a></li>
    <li><a href="../DevGuideTesting.html#DevGuideJUnitSuites">Creating Test Suites</a></li>
    <li><a href="../DevGuideTesting.html#DevGuideAsynchronousTesting">Asynchronous testing</a></li>
    <li><a href="../DevGuideTesting.html#DevGuideJUnitSetUp">Setting up and tearing down JUnit test cases that use GWT code</a></li>
</ul>


