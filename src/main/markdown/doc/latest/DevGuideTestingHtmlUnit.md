Testing HTML Unit
===

<p><a href="http://htmlunit.sourceforge.net">HtmlUnit</a> is an open-source
GUI-less browser written in 100% Java. Because HtmlUnit does not involve any
native code, debugging GWT Tests in development mode can be done entirely in a
Java debugger. HtmlUnit does not require firing up a new browser process; the
HtmlUnit browser instances just run as new threads.


<h2>Limitations and Workarounds</h2>
<p>Because HtmlUnit is a GUI-less browser, layout cannot be tested on HtmlUnit.
You can annotate such test methods or classes that must not be run by HtmlUnit
as <tt>@DoNotRunWith(Platform.HtmlUnit)</tt>. Additionally, correct tests can
sometimes fail on HtmlUnit, either because the HtmlUnit support for that
feature is lacking or because of HtmlUnit's issues with flakiness when running
asynchronous tests. In addition to sending us bug reports, you can annotate
such tests with @DoNotRunWith so that your build does not keep on breaking.
There is also a temporary option for reducing the flakiness that HtmlUnit might
cause with asynchronous tests (while we fix the fundamental problem). You can
specify how many times GWT should attempt to run a test in case of a failure.
For example, with <tt>-Xtries 3</tt>, GWT will attempt to run a test up to three
times.

<h2>RunStyle HtmlUnit</h2>
<p>The HtmlUnit runstyle enables you to specify other browser emulations. By
default, GWT runs HtmlUnit in the Firefox3 emulation mode. As of the 2.0
release, GWT has not been extensively tested on the other emulations that
HtmlUnit supports, namely FF2, IE6, IE7, and IE8. Still, to use them, you can
define the system property <tt>gwt.args</tt>, as explained before. For example,
to cause tests to run both in FF3 and IE8 emulation mode, set
<a href="DevGuideTesting.html#passingTestArguments"><tt>gwt.args</tt></a> to:
<pre>-runStyle HtmlUnit:FF3,IE6</pre>