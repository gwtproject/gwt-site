Testing HTML Unit
===

[HtmlUnit](http://htmlunit.sourceforge.net) is an open-source
GUI-less browser written in 100% Java. Because HtmlUnit does not involve any
native code, debugging GWT Tests in development mode can be done entirely in a
Java debugger. HtmlUnit does not require firing up a new browser process; the
HtmlUnit browser instances just run as new threads.

## Limitations and Workarounds

Because HtmlUnit is a GUI-less browser, layout cannot be tested on HtmlUnit.
You can annotate such test methods or classes that must not be run by HtmlUnit
as `@DoNotRunWith(Platform.HtmlUnit)`. Additionally, correct tests can
sometimes fail on HtmlUnit, either because the HtmlUnit support for that
feature is lacking or because of HtmlUnit's issues with flakiness when running
asynchronous tests. In addition to sending us bug reports, you can annotate
such tests with @DoNotRunWith so that your build does not keep on breaking.
There is also a temporary option for reducing the flakiness that HtmlUnit might
cause with asynchronous tests (while we fix the fundamental problem). You can
specify how many times GWT should attempt to run a test in case of a failure.
For example, with `-Xtries 3`, GWT will attempt to run a test up to three
times.

## RunStyle HtmlUnit

The HtmlUnit runstyle enables you to specify other browser emulations. By
default, GWT runs HtmlUnit in the Firefox 38 emulation mode. As of the 2.8
release, GWT has not been extensively tested on the other emulations that
HtmlUnit supports, namely Edge, Chrome, IE8, and IE11. Still, to use them, you can
define the system property `gwt.args`, as explained before. For example,
to cause tests to run both in FF38 and IE11 emulation mode, set
[`gwt.args`](DevGuideTesting.html#passingTestArguments) to:

`-runStyle HtmlUnit:FF38,IE11`
