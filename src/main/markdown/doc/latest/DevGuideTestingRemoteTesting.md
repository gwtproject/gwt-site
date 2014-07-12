<div style="font-style:italic; margin-top: 3px">Running JUnit tests on remote systems</div>

<ol class="toc" id="pageToc">  
  <li><a href="#Introduction">Introduction</a></li>
  <li><a href="#Useful_Arguments">Useful Arguments</a>
    <ul>
      <li><a href="#-prod">-prod</a></li>
      <li><a href="#-userAgents">-userAgents</a></li>
    </ul>
  </li>
  <li><a href="#Run_Styles">Run Styles</a>
    <ul>
      <li><a href="#Manual">Manual</a></li>
      <li><a href="#Selenium">Selenium</a>
        <ul>
          <li><a href="#Firefox_Profile">Firefox Profile</a></li>
        </ul>
      </li>
      <li><a href="#Remote_Web">Remote Web (deprecated)</a></li>
    </ul>
  </li>
</ol>


<h2 id="Introduction">Introduction</h2>

<p>This document explains how to run GWT tests on remote systems. </p>

<p>There are three types of remote RunStyles that can help you run remote tests: </p>

<ul>
  <li>Manual </li>
  <li>Selenium </li>
  <li>RemoteWeb </li>
</ul>

<p>To use any of these run styles, you need to pass the <code>-runStyle</code> argument to the test infrastructure
(see <a href="DevGuideTesting.html#passingTestArguments">Passing Arguments to the Test Infrastructure</a>).  The format looks like this
(see specific examples below):</p>

<pre class="prettyprint">-runStyle &lt;NameStartingWithCaps&gt;:arguments</pre>

<p>If you are running a test from Eclipse, you would add something like the following to the VM arguments (Note that the run style name
starts with a capital letter):</p>

<pre class="prettyprint">-Dgwt.args=&quot;-runStyle Selenium:myhost:4444/*firefox&quot;</pre>


<h2 id="Useful_Arguments">Useful Arguments</h2>

<p>The following arguments are useful when running remote tests. </p>

<h3 id="-prod">-prod</h3>

<p>If you are not familiar with <em>development</em> mode versus <em>production mode</em>, you should read the associated tutorials
on <a href="DevGuideCompilingAndDebugging.html">Compiling and Debugging</a> first. All of the following examples assume that you are running
tests in development mode, which requires that you have the
<a href="/missing-plugin/">GWT Developer Plugin</a> installed. Its important to note that URLs must be
whitelisted before this plugin will connect to them.  <font color="red">This means that you must allow the remote connection on the remote
system the first time you run the test, or ahead of time if possible.</font></p>

<p>Tests run in development mode by default. You can run a test in production mode by adding <code>-prod</code> to the GWT arguments. When
running tests in production mode, you do not need to have the GWT Developer Plugin installed on the remote system.</p>

<pre class="prettyprint">-Dgwt.args=&quot;<strong>-prod</strong> -runStyle Selenium:myhost:4444/*firefox&quot;</pre>


<h3 id="-userAgents">-userAgents</h3>

<p>When running tests in production mode, GWT compiles the tests for all browsers, which can take a while. If you know which browsers your test
will run in, you can limit the browser permutations (and reduce compile time), using the <code>-userAgents</code> argument:</p>

<pre class="prettyprint">-Dgwt.args=&quot;-prod <strong>-userAgents ie6,gecko1_8</strong> -runStyle Selenium:myhost:4444/*firefox&quot;</pre>


<h2 id="Run_Styles">Run Styles</h2>


<h3 id="Manual">Manual</h3>

<p>The Manual run style allows you to run JUnit tests in any browser by directing the browser to a URL that GWT provides. For details,
see <a href="DevGuideTesting.html#Manual_Mode">Running tests in manual mode</a>. In particular, manual mode can be used for remote testing &mdash;
the browser may be running on a computer different from the one where the tests were started. 
 
<h3 id="Selenium">Selenium</h3>

<p><i>Recommended for Firefox, Safari, Google Chrome, and Internet Explorer (see note).</i></p>

<p class="note"><b>Internet Explorer:</b> You can try running Internet Explorer in Selenium as it is a supported browser. If the tests work for you,
then you don&#x27;t need to use the RemoteWeb runstyle at all, which should simplify your testing.  However, we&#x27;ve found that
Selenium does not always open Internet Explorer successfully on newer versions of Windows.  If this happens, you can try passing the
<code>-singleWindow</code> argument into Selenium.</p>

<p>GWT can execute tests against a remote system running the <a href="http://seleniumhq.org/projects/remote-control/">Selenium Remote Control</a>.
You do this using the following command: </p>

<pre class="prettyprint">-Dgwt.args=&quot;-runStyle Selenium:myhost:4444/*firefox,myotherhost:4444/*firefox&quot;</pre>

<p>In the above example, we are using the Selenium run style to execute a development mode test on Firefox against two remote systems (myhost and myotherhost).  </p>

<a name="SeleniumInternetExplorerNote"></a>
<p class="note">
  <b>Note:</b> On newer versions of Windows, if you run Selenium as an Administrator, you will not run tests in development mode because the GWT
  Developer Plugin is installed for the current user only.
</p>


<h4 id="Firefox_Profile">Firefox Profile</h4>

<p>By default, Selenium creates a new Firefox profile so it can prevent unnecessary popups that would otherwise mess up the test.  However, you will probably
want to create your own Firefox profile that includes the GWT Developer Plugin.</p>

<p>To do this, run Firefox from the command line and pass in the -ProfileManager argument to open the Profile Manager: </p>

<pre class="prettyprint">firefox.exe -ProfileManager</pre>

<p>Create a new profile (remember the location), and open it. Setup the profile however you want, making sure to install the GWT Developer
Plugin. On our test systems, we use the following settings: </p>

<ul>
  <li>Set a blank homepage
    <ul>
      <li>Edit -&gt; Preferences -&gt; Main </li>
      <li>Set &quot;When Firefox Starts&quot; to &quot;Show a blank page&quot; </li>
    </ul>
  </li>

  <li>Disable warnings
    <ul>
      <li>Edit -&gt; Preferences -&gt; Security </li>
      <li>Under &quot;Warning Messages&quot; click &quot;Settings&quot; </li>
      <li>Uncheck all warnings </li>
    </ul>
  </li>

  <li>Disable auto update
    <ul>
      <li>Edit -&gt; Preferences -&gt; Advanced -&gt; Update </li>
      <li>Uncheck all automatic updates </li>
    </ul>
  </li>

  <li>Disable session restore
    <ul>
      <li>Type &#x27;about:config&#x27; in the browser bar </li>
      <li>Find browser.sessionstore.resume_from_crash and set it to false </li>
     <li>Find browser.sessionstore.enabled and set it to false (if it exists) </li>
    </ul>
  </li>

  <li>Install <a href="http://getfirebug.com/">Firebug</a> (useful for debugging) </li>

  <li>Install the <a href="/missing-plugin/">GWT Developer Plugin</a></li>

  <li>Whitelist the hosts that will launch the development mode code server.  Since Selenium copies the
      profile for each test, you must do this now.  If you do not, you will have to allow the remote connection for every test!
    <ul>
      <li>Restart Firefox </li>
      <li>Tools -&gt; Addons </li>
      <li>Select GWT Developer Plugin for Firefox</li>
      <li>Click &quot;Options&quot; </li>
      <li>Add the <u>IP address</u> that you want to allow the plugin to connect to.</li>
    </ul>
  </li>
</ul>

<p>When starting the selenium server, pass in the following argument to use your firefox profile as a template: </p>

<pre class="prettyprint">--firefoxProfileTemplate /path/to/profile</pre>

<h3 id="Remote_Web">Remote Web</h3>

<p><i>Deprecated. Will be removed in GWT 2.7.</i></p>

<p>The RemoteWeb run style allows you to run tests against systems running the BrowserManagerServer, a server that GWT provides. </p>

<p>First, you need to start the BrowserManagerServer on the remote test system using the following java command.  Note that gwt-user.jar and gwt-dev.jar are on the classpath. </p>

<pre class="prettyprint">java -cp gwt-user.jar;gwt-dev.jar com.google.gwt.junit.remote.BrowserManagerServer ie8 &quot;C:\Program Files\Internet Explorer\IEXPLORE.EXE&quot;</pre>

<p>BrowserManagerServer takes commands in pairs.  In the above example, we are associating the name &quot;ie8&quot; with the executable iexplore.exe. </p>

<pre class="prettyprint">&lt;browser name&gt; &lt;path/to/browser&gt;</pre>

<p>To run a test against IE8, you would use the following argument: </p>

<pre class="prettyprint">-runStyle RemoteWeb:rmi://myhost/ie8</pre>
