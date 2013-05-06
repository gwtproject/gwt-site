<p>Creating a battery of good unit test cases is an important part of ensuring
the quality of your application over its lifecycle. To aid developers with
their testing efforts, GWT provides integration with the popular <a
href="http://www.junit.org">JUnit</a> unit testing framework and
<a href="http://emma.sourceforge.net">Emma</a> code coverage
tool. GWT allows JUnit test cases to run in either development mode or production mode.
</p>

<p class="note" style="margin-left: 1.5em; margin-right: 1.5em;">
<b>Note:</b> To run through the steps to add JUnit tests to a sample GWT app, see the tutorial <a href="tutorial/JUnit.html">Unit Testing GWT Applications with JUnit</a>.
</p>

<ol class="toc" id="pageToc">
  <li><a href="#DevGuideArchitecting">Architecting Your App for Testing</a></li>
  <li><a href="#DevGuideJUnitCreation">Creating &amp; Running a Test Case</a></li>
  <li><a href="#DevGuideAsynchronousTesting">Asynchronous Testing</a></li>
  <li><a href="#DevGuideJUnitSuites">Combining TestCase classes into a TestSuite</a></li>
  <li><a href="#DevGuideJUnitSetUp">Setting up and tearing down JUnit test cases that use GWT code</a></li>
  <li><a href="#DevGuideRunningTestsInEclipse">Running tests in Eclipse</a></li>
</ol>

<h2 id="DevGuideArchitecting">Architecting Your App for Testing</h2>

<p>
The bulk of this page is dedicated to explaining how to unit test your
GWT code via the GWTTestCase class, which at the end of the day must
pay performance penalties for running in a browser. But that's not always
what you want to do.
</p>

<p>
It will be well worth your effort to architect your app so that the
bulk of your code has no idea that it will live in a browser. Code
that you isolate this way can be tested in plain old JUnit test cases
running in a JRE, and so execute much faster. The same good habits of
separation of concerns, dependency injection and the like will benefit
your GWT app just as they would any other, perhaps even more than usual.
</p>

<p>
For some tips along these lines take a look at
the <a href="http://code.google.com/events/io/2009/sessions/GoogleWebToolkitBestPractices.html">Best
Practices For Architecting Your GWT App</a> talk given at Google I/O
in May of 2009. And keep an eye on this site for more more articles in
the same vein.
</p>

<h2 id="DevGuideJUnitCreation">Creating a Test Case</h2>

<p>This section will describe how to create and run a set of unit test cases for your GWT project. In order to use this facility, you must have the <a href="http://sourceforge.net/projects/junit/">JUnit</a> library installed on your system.</p>

<h3>The GWTTestCase Class</h3>

<p>GWT includes a special <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/junit/client/GWTTestCase.html">GWTTestCase</a> base class
that provides <a href="http://www.junit.org">JUnit</a> integration. Running a compiled <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/junit/client/GWTTestCase.html">GWTTestCase</a> subclass under JUnit launches the <a href="DevGuideTestingHtmlUnit.html">HtmlUnit browser</a> which serves to emulate your application behavior during test execution.</p>

<p>GWTTestCase is derived from JUnit's <a href="http://junit.sourceforge.net/junit3.8.1/javadoc/junit/framework/TestCase.html">TestCase</a>. The typical way to setup a JUnit
test case class is to have it extend <tt>TestCase</tt>, and then run the it with the JUnit <a href="http://junit.sourceforge.net/junit3.8.1/javadoc/junit/awtui/TestRunner.html">TestRunner</a>. <tt>TestCase</tt> uses reflection to discover the test methods defined in your derived class. It is convention to begin the name of all test methods
with the prefix <tt>test</tt>.</p>

<h3 id="webAppCreator">Using webAppCreator</h3>

<p>The webAppCreator that GWT includes can generate a starter test case for you, plus ant targets and eclipse launch configs for testing in both development mode and production mode.</p>

<p>For example, to create a starter application along with test cases in the directory <tt>fooApp</tt>, where module name is 
<tt>com.example.foo.Foo</tt>:</p>

<pre>
 ~/Foo&gt; webAppCreator -out fooApp 
        -junit /opt/eclipse/plugins/org.junit_3.8.1/junit.jar
        com.example.foo.Foo
Created directory fooApp/src
Created directory fooApp/war
Created directory fooApp/war/WEB-INF
Created directory fooApp/war/WEB-INF/lib
Created directory fooApp/src/com/example/foo
Created directory fooApp/src/com/example/foo/client
Created directory fooApp/src/com/example/foo/server
Created directory fooApp/test/com/example/foo/client
Created file fooApp/src/com/example/foo/Foo.gwt.xml
Created file fooApp/war/Foo.html
Created file fooApp/war/Foo.css
Created file fooApp/war/WEB-INF/web.xml
Created file fooApp/src/com/example/foo/client/Foo.java
Created file fooApp/src/com/example/foo/client/GreetingService.java
Created file fooApp/src/com/example/foo/client/GreetingServiceAsync.java
Created file fooApp/src/com/example/foo/server/GreetingServiceImpl.java
Created file fooApp/build.xml
Created file fooApp/README.txt
Created file fooApp/test/com/example/foo/client/FooTest.java
Created file fooApp/.project
Created file fooApp/.classpath
Created file fooApp/Foo.launch
Created file fooApp/FooTest-dev.launch
Created file fooApp/FooTest-prod.launch
Created file fooApp/war/WEB-INF/lib/gwt-servlet.jar
</pre>

<p>Follow the instructions in the generated fooApp/README.txt file. You have
two ways to run your tests: using ant or using Eclipse. There are ant targets
<tt>ant test.dev</tt> and <tt>ant test.web</tt> for running your tests in
development and production mode, respectively. Similarly, you can follow the
instructions in the README.txt file to import your project in Eclipse or your
favorite IDE, and use the launch configs <tt>FooTest-dev</tt> and
<tt>FooTest-prod</tt> to run your tests in development and production mode
using eclipse. As you keep adding your testing logic to the skeleton
<tt>FooTest.java</tt>, you can continue using the above techniques to run your
tests.

<h3>Creating a Test Case by Hand</h3>

<p>If you prefer not to use webAppCreator, you may create a test case suite by hand by following the instructions below:</p>

<p/>

<ol>
<li><strong>Define a class that extends <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/junit/client/GWTTestCase.html">GWTTestCase</a>.</strong> Make sure your test class is on the module source path (e.g. in the <tt>client</tt> subpackage of your module.) You can add new source
paths by editing the <a href="DevGuideOrganizingProjects.html#DevGuideModuleXml">module XML file</a> and adding a <tt>&lt;source&gt;</tt> element.</li>

<li><strong>If you do not have a GWT module yet, create a <a href="DevGuideOrganizingProjects.html#DevGuideModules">module</a> that causes the source for your test case to be
included.</strong> If you are adding a test case to an existing GWT app, you can just use the existing module.</li>

<li><strong>Implement the method <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/junit/client/GWTTestCase.html#getModuleName()">GWTTestCase.getModuleName()</a> to return the fully-qualified name of the module.</strong> This is the glue that tells the JUnit test case which module to
instantiate.</li>

<li><strong>Compile your test case class to bytecode.</strong> You can use the Java compiler directly using <a href="http://java.sun.com/j2se/1.5.0/docs/tooldocs/windows/javac.html">javac</a> or a Java IDE such as <a href="http://eclipse.org">Eclipse</a>.</li>

<li><strong>Run your test case.</strong> Use the class <tt>junit.textui.TestRunner</tt> as your main class and pass the full name of your test class as the command line argument,
e.g. <tt>com.example.foo.client.FooTest</tt>. When running the test case, make sure your classpath includes:</li>

<li style="list-style: none">
<ul>
<li>Your project's <tt>src</tt> directory</li>

<li>Your project's <tt>bin</tt> directory</li>

<li>The <tt>gwt-user.jar</tt> library</li>

<li>The <tt>gwt-dev.jar</tt> library</li>

<li>The <tt>junit.jar</tt> library</li>
</ul>
</li>
</ol>



<h3>Client side Example</h3>

<p>First of all, you will need a valid GWT module to host your test case class. Usually, you do not need to create a new <a href="DevGuideOrganizingProjects.html#DevGuideModuleXml">module
XML file</a> - you can just use the one you have already created to develop your GWT module. But if you did not already have a module, you might create one like this:

<pre class="prettyprint">
&lt;module&gt;
  &lt;!-- Module com.example.foo.Foo --&gt;

  &lt;!-- Standard inherit.                                           --&gt;
  &lt;inherits name='com.google.gwt.user.User'/&gt;

  &lt;!-- implicitly includes com.example.foo.client package          --&gt;

  &lt;!-- OPTIONAL STUFF FOLLOWS --&gt;

  &lt;!-- It's okay for your module to declare an entry point.        --&gt;
  &lt;!-- This gets ignored when running under JUnit.                 --&gt;
  &lt;entry-point class='com.example.foo.FooModule'/&gt;

  &lt;!-- You can also test remote services during a JUnit run.       --&gt;
  &lt;servlet path='/foo' class='com.example.foo.server.FooServiceImpl'/&gt;
&lt;/module&gt;
</pre>

<p class="note"><strong>Tip:</strong> You do not need to create a
separate module for every test case, and in fact will pay a startup penalty
for every module you do use. In the example above, any test
cases in <tt>com.example.foo.client</tt> (or any subpackage) can share
the <tt>com.example.foo.Foo</tt> module.</p>

<p>Suppose you had created a widget under the <tt>foo</tt> package,
<tt>UpperCasingLabel</tt>, which ensures that the text it shows is all upper
case. Here is how you might test it.

<pre class="prettyprint">
package com.example.foo.client;
import com.google.gwt.junit.client.GWTTestCase;

public class UpperCasingLabelTest extends GWTTestCase {

  /**
   * Specifies a module to use when running this test case. The returned
   * module must include the source for this class.
   * 
   * @see com.google.gwt.junit.client.GWTTestCase#getModuleName()
   */
  @Override
  public String getModuleName() {
    return "com.example.foo.Foo";
  }
 
  public void testUpperCasingLabel() {
    UpperCasingLabel upperCasingLabel = new UpperCasingLabel();
    
    upperCasingLabel.setText("foo");
    assertEquals("FOO", upperCasingLabel.getText());

    upperCasingLabel.setText("BAR");
    assertEquals("BAR", upperCasingLabel.getText());

    upperCasingLabel.setText("BaZ");
    assertEquals("BAZ", upperCasingLabel.getText());
  }
}
</pre>

<p>Now, there are several ways to run your tests. Just look at the sample ant scripts or launch configs generated by webAppCreator, as in the previous subsection.</p>

<h3 id="passingTestArguments">Passing Arguments to the Test Infrastructure</h3>
<p>The main class in the test infrastructure is <tt>JUnitShell</tt>.
To control aspects of how your tests execute, you must pass arguments to this
class.  Arguments cannot be passed directly through the command-line because
normal command-line arguments go directly to the JUnit runner. Instead, define
the system property <tt>gwt.args</tt> to pass arguments to <tt>JUnitShell</tt>.</p>

<p>For example, to run tests in production mode (that is, run the tests afer they
have been compiled into JavaScript), declare
<tt>-Dgwt.args="-prod"</tt> as a JVM argument when invoking JUnit. To
get a full list of supported options, declare
<tt>-Dgwt.args="-help"</tt> (instead of running the test, help is
printed to the console).</p>

<h3>Running your test in Production Mode</h3>

<p>When using the webAppCreator tool, you get the ability to launch your tests
in either development mode or production mode. Make sure you test in both modes
- although rare, there are <a
href="DevGuideCodingBasics.html#DevGuideJavaCompatibility">some differences
between Java and JavaScript</a> that could cause your code to produce different
results when deployed.</p>

<p>If you instead decide to run the JUnit TestRunner from command line, you must add some additional arguments to get your unit tests running in production mode. By
default, tests run in <a href="DevGuideCompilingAndDebugging.html#DevGuideDevMode">development mode</a> are run as normal Java bytecode in a JVM. To override this default behavior, you need to <a href="#passingTestArguments">pass arguments</a> to <tt>JUnitShell</tt>
<pre>-Dgwt.args="-prod"</pre>

<h3 id="Manual_Mode">Running your test in Manual Mode</h3>

<p>Manual-mode tests allow you to run unit tests manually on any browser. In this mode, the <tt>JUnitShell</tt> main class runs as
usual on a specified GWT module, but instead of running the test immediately, it prints out a URL and waits for a browser to connect. You can manually cut and paste this URL into the browser of your choice, and the unit tests will run in that browser.</p>

<p>For example, if you want to run a test in a single browser, you would use the following arguments:</p>

<pre class="prettyprint">-runStyle Manual:1</pre>

<p>GWT will then show a console message like the following: </p>

<pre class="prettyprint">Please navigate your browser to this URL:
http://172.29.212.75:58339/com.google.gwt.user.User.JUnit/junit.html?gwt.codesvr=172.29.212.75:42899</pre>

<p>Point your browser to the specified URL, and the test will run. You may be prompted by the GWT Developer Plugin to accept the connection the first time the test is run. </p>

<p>Manual-mode test targets are not generated by the webAppCreator tool, but you can easily create one by copying the <tt>test.prod</tt> ant target in the build.xml file to <tt>test.manual</tt> and adding <tt>-runStyle Manual:1</tt> to the <tt>-Dgwt.args</tt> part. Manual mode can also be used for <a href="/web-toolkit/doc/latest/DevGuideTestingRemoteTesting#Manual">remote browser testing</a>. </p>

<h3>Running your test on Remote Systems</h3>

<p>Since different browsers can often behave in unexpected ways, it is important for developers to test their applications on all browsers they plan to support. GWT simplifies remote browser testing by enabling you to run tests on remote systems, as explained in <a href="/web-toolkit/doc/latest/DevGuideTestingRemoteTesting">the Remote Browser Testing page</a>.</p>

<h3>Automating your Test Cases</h3>

<p>When developing a large project, a good practice is to integrate the running of your test cases with your regular build process. When you build manually, such as using
<tt>ant</tt> from the command line or using your desktop IDE, this is as simple as just adding the invocation of JUnit into your regular build process. As mentioned before, when you run <tt>GWTTestCase</tt> tests, an HtmlUnit browser runs the tests. However, all tests might not run successfully on HtmlUnit, as explained earlier. GWT provides remote testing solutions that allow you to use a selenium server to run tests. Also, consider organizing your tests into <a href="/web-toolkit/doc/latest/DevGuideTesting#DevGuideJUnitSuites">GWTTestSuite</a> classes to get the best performance from your unit tests.</p>

<h3>Server side testing</h3>

<p>The tests described above are intended to assist with testing client side code. The test case wrapper <tt>GWTTestCase</tt> will launch either a development mode session or a web
browser to test the generated JavaScript. On the other hand, server side code runs as native Java in a JVM without being translated to JavaScript, so it is not necessary to run
tests of server side code using <tt>GWTTestCase</tt> as the base class for your tests. Instead, use JUnit's <tt>TestCase</tt> and other related classes directly when writing tests for
your application's server side code. That said, you may want both GWTTestCase and TestCase coverage of code that will be used on both the client and the server. </p>

<h2 id="DevGuideAsynchronousTesting">Asynchronous Testing</h2>

<p>GWT's <a href="http://www.junit.org">JUnit</a> integration provides special support for testing functionality that cannot be executed in straight-line code. For
example, you might want to make an <a href="DevGuideServerCommunication.html#DevGuideRemoteProcedureCalls">RPC</a> call to a server and then validate the response. However, in a normal
JUnit test run, the test stops as soon as the test method returns control to the caller, and GWT does not support multiple threads or blocking. To support this use case, <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/junit/client/GWTTestCase.html">GWTTestCase</a> has extended the <tt>TestCase</tt> API.
The two key methods are <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/junit/client/GWTTestCase.html#delayTestFinish(int)">GWTTestCase.delayTestFinish(int)</a> and <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/junit/client/GWTTestCase.html#finishTest()">GWTTestCase.finishTest()</a>. Calling
<tt>delayTestFinish()</tt> during a test method's execution puts that test in asynchronous mode, which means the test will not finish when the test method returns control to the
caller. Instead, a <i>delay period</i> begins, which lasts the amount of time specified in the call to <tt>delayTestFinish()</tt>. During the delay period, the test system will
wait for one of three things to happen:</p>

<ol>
<li>If <tt>finishTest()</tt> is called before the delay period expires, the test will succeed.</li>

<li>If any exception escapes from an event handler during the delay period, the test will error with the thrown exception.</li>

<li>If the delay period expires and neither of the above has happened, the test will error with a <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/junit/client/TimeoutException.html">TimeoutException</a>.</li>
</ol>



<p>The normal use pattern is to setup an event in the test method and call <tt>delayTestFinish()</tt> with a timeout significantly longer than the event is expected to take. The
event handler validates the event and then calls <tt>finishTest()</tt>.</p>

<h4>Example</h4>

<pre class="prettyprint">
public void testTimer() {
  // Setup an asynchronous event handler.
  Timer timer = new Timer() {
    public void run() {
      // do some validation logic

      // tell the test system the test is now done
      finishTest();
    }
  };

  // Set a delay period significantly longer than the
  // event is expected to take.
  delayTestFinish(500);

  // Schedule the event and return control to the test system.
  timer.schedule(100);
}
</pre>

<p>
The recommended pattern is to test one asynchronous event per test method. 
If you need to test multiple events in the same method, here are a couple of techniques:
<ul>
<li>"Chain" the events together. Trigger the first event during the test method's execution; when that event fires, call <tt>delayTestFinish()</tt> again with a new timeout and
trigger the next event. When the last event fires, call <tt>finishTest()</tt> as normal.</li>
<li>Set a counter containing the number of events to wait for. As each event comes in, decrement the counter. Call <tt>finishTest()</tt> when the counter reaches
<tt>0.</tt></li>
</ul>


<h2 id="DevGuideJUnitSuites">Combining TestCase classes into a TestSuite</h2>

<p>The <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/junit/tools/GWTTestSuite.html">GWTTestSuite</a>
mechanism has the overhead of having to start a development mode shell
and servlet or compile your code. There is also overhead for each test
module within a suite.

<p>Ideally you should group your tests into as few modules as is
practical, and should avoid having tests in a particular module run by
more than one suite. (Tests are in the same module if they return
return the same value
from <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/junit/client/GWTTestCase.html#getModuleName()">getModuleName()</a>.)

<p>GWTTestSuite class re-orders the test cases so that all cases that
share a module are run back to back.</p>

<p>Creating a suite is simple if you have already defined individual JUnit <a href="http://junit.sourceforge.net/junit3.8.1/javadoc/junit/framework/TestCase.html">TestCases</a> or <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/junit/client/GWTTestCase.html">GWTTestCases</a>.
Here is an example:</p>

<pre class="prettyprint">
public class MapsTestSuite extends GWTTestSuite {
  public static Test suite() {
    TestSuite suite = new TestSuite("Test for a Maps Application");
    suite.addTestSuite(MapTest.class); 
    suite.addTestSuite(EventTest.class);
    suite.addTestSuite(CopyTest.class);
    return suite;
  }
}
</pre>

<p>The three test cases <tt>MapTest</tt>, <tt>EventTest</tt>, and <tt>CopyTest</tt> can now all run in the same instance of JUnitShell.</p>

<pre class="prettyprint">
java -Xmx256M -cp "./src:./test:./bin:./junit.jar:/gwt/gwt-user.jar:/gwt/gwt-dev.jar:/gwt/gwt-maps.jar" junit.textui.TestRunner com.example.MapsTestSuite
</pre>


<h2 id="DevGuideJUnitSetUp">Setting up and tearing down
JUnit test cases that use GWT code</h2>

<p>When using a test method in a JUnit <a href="http://junit.sourceforge.net/junit3.8.1/javadoc/junit/framework/TestCase.html">TestCase</a>, any objects your test creates and
leaves a reference to will remain active. This could interfere with future test methods. You can override two new methods to prepare for and/or clean up
after each test method.</p>

<ul>
<li><a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/junit/client/GWTTestCase.html#gwtSetUp()" >gwtSetUp()</a> runs before each test
method in a test case.</li>

<li><a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/junit/client/GWTTestCase.html#gwtTearDown()">gwtTearDown()</a> runs after each
test method in a test case.</li>
</ul>

<p>The following example shows how to defensively cleanup the DOM before the next test run using <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/junit/client/GWTTestCase.html#gwtSetUp()">gwtSetUp()</a>. It skips over <tt>&lt;iframe&gt;</tt> and <tt>&lt;script&gt;</tt> tags so that the GWT test infrastructure is not accidentally removed.</p>

<pre class="prettyprint">
  import com.google.gwt.junit.client.GWTTestCase;
  import com.google.gwt.user.client.DOM;
  import com.google.gwt.user.client.Element;

  private static native String getNodeName(Element elem) /*-{
     return (elem.nodeName || "").toLowerCase();
  }-*/;

  /**
   * Removes all elements in the body, except scripts and iframes.
   */
  public void gwtSetUp () {
    Element bodyElem = RootPanel.getBodyElement();

    List&lt;Element&gt; toRemove = new ArrayList&lt;Element&gt;();
    for (int i = 0, n = DOM.getChildCount(bodyElem); i &lt; n; ++i) {
      Element elem = DOM.getChild(bodyElem, i);
      String nodeName = getNodeName(elem);
      if (!"script".equals(nodeName) &amp;&amp; !"iframe".equals(nodeName)) {
        toRemove.add(elem);
      }
    }

    for (int i = 0, n = toRemove.size(); i &lt; n; ++i) {
      DOM.removeChild(bodyElem, toRemove.get(i));
    }
  }
</pre>

<h2 id="DevGuideRunningTestsInEclipse">Running Tests in Eclipse</h2>
<p>
The <a href="#webAppCreator">webAppCreator</a> tool provides a simple way to
generate example launch configurations that can be used to run both development
and production mode tests in Eclipse. You can generate additional launch configurations
by copying it and replacing the project name appropriately.

<p>Alternatively, one can also directly generate launch configurations. Create a
normal JUnit run configuration by right-clicking on the Test file that extends
<tt>GWTTestCase</tt> and selecting <tt>Run as</tt> &gt; <tt>JUnit Test</tt>. Though the
first run will fail, a new JUnit run configuration will be generated. 
Modify the run configuration by adding the project's <tt>src</tt> and
<tt>test</tt> directories to the classpath, like so:
<ul>
<li>click the <tt>Classpath tab</tt>
<li>select <tt>User Entries</tt>
<li>click the <tt>Advanced</tt> button
<li>select the <tt>Add Folders</tt> radio button
<li>add your <tt>src</tt> and <tt>test</tt> directories
</ul>

<p> Launch the run config to see the tests running in development
mode.

<p>To run tests in production mode, copy the development mode launch
configuration and pass VM arguments (by clicking the <tt>Arguments</tt> tab and
adding to the VM arguments textarea) <pre>-Dgwt.args="-prod"</pre> 