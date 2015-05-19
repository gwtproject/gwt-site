The GWT Release Notes
=====================

* [2.7.0](#Release_Notes_2_7_0)
* [2.7.0 (RC1)](#Release_Notes_2_7_0_RC1)
* [2.6.1](#Release_Notes_2_6_1)
* [2.6.0](#Release_Notes_2_6_0)
* [2.6.0 (RC4)](#Release_Notes_2_6_0_RC4)
* [2.6.0 (RC3)](#Release_Notes_2_6_0_RC3)
* [2.6.0 (RC2)](#Release_Notes_2_6_0_RC2)
* [2.6.0 (RC1)](#Release_Notes_2_6_0_RC1)
* [2.5.1](#Release_Notes_2_5_1)
* [2.5.1 (RC1)](#Release_Notes_2_5_1_RC1)
* [2.5.0](#Release_Notes_2_5_0)
* [2.5.0 (RC2)](#Release_Notes_2_5_0_RC2)
* [2.5.0 (RC1)](#Release_Notes_2_5_0_RC1)
* [2.4.0](#Release_Notes_2_4_0)
* [2.3.0](#Release_Notes_2_3_0)
* [2.3.0 (M1)](#Release_Notes_2_3_0_M1)
* [2.2.0](#Release_Notes_2_2_0)
* [2.1.1](#Release_Notes_2_1_1)
* [2.1.0](#Release_Notes_2_1_0)
* [2.1.0 (RC1)](#Release_Notes_2_1_0_RC1)
* [2.1.0 (M3)](#Release_Notes_2_1_0_M3)
* [2.1.0 (M2)](#Release_Notes_2_1_0_M2)
* [2.1.0 (M1)](#Release_Notes_2_1_0_M1)
* [2.0.4](#Release_Notes_2_0_4)
* [2.0.3](#Release_Notes_2_0_3)
* [2.0.2](#Release_Notes_2_0_2)
* [2.0.1](#Release_Notes_2_0_1)
* [2.0.0](#Release_Notes_2_0_0)
* [2.0.0 (RC2)](#Release_Notes_2_0_0_rc2)
* [2.0.0 (RC1)](#Release_Notes_2_0_0_rc1)
* [1.7.1](#Release_Notes_1_7_1)
* [1.7.0](#Release_Notes_1_7_0)
* [1.6.4](#Release_Notes_1_6_4)
* [1.6.3](#Release_Notes_1_6_3)
* [1.6.2](#Release_Notes_1_6_2)
* [1.5.3](#Release_Notes_1_5_3)
* [1.5.2](#Release_Notes_1_5_2)
* [1.5.1 (RC2)](#Release_Notes_1_5_1)
* [1.5.0 (RC)](#Release_Notes_1_5_0)
* [1.4.60](#Release_Notes_1_4_60)
* [1.4.59 (RC2)](#Release_Notes_1_4_59)
* [1.4.10 (RC)](#Release_Notes_1_4_10)
* [1.3.3](#Release_Notes_1_3_3)
* [1.3.1 (RC)](#Release_Notes_1_3_1)
* [1.2.22](#Release_Notes_1_2_22)
* [1.2.21 (RC)](#Release_Notes_1_2_11)
* [1.1.10](#Release_Notes_1_1_10)
* [1.1.0 (RC)](#Release_Notes_1_1_0)
* [1.0.21](#Release_Notes_1_0_21)

**Note** - M1 = first milestone, RC1 = first release candidate

* * *



## <a id="Release_Notes_2_7_0"></a> Release Notes for 2.7.0

This release fixes issues found while testing RC1.

For details, see the
[commit logs.](https://gwt.googlesource.com/gwt/+log/2.7.0-rc1..2.7.0)

### Known Issues

DevMode always starts in Super Dev Mode, so it's not as easy to test in
production mode. See
[issue 9004](https://code.google.com/p/google-web-toolkit/issues/detail?id=9004)
for workarounds.

## <a id="Release_Notes_2_7_0_RC1"></a> Release Notes for 2.7.0 (RC1)

### Highlights

- Super Dev Mode is now the default. DevMode automatically starts Super Dev Mode
and reloading a web page automatically runs the compiler when necessary.
(The `-nosuperDevMode` flag may be used to revert to the old behavior.)

- Compiling in Super Dev Mode is much faster after the first compile.

- Experimental support for GSS, also known as
  [Closure Stylesheets](https://code.google.com/p/closure-stylesheets/). (See below.)

### Known Issues

- gwttar files are incorrect. (Fixed in the next version.)

### Deprecations

- GWT Designer doesn't work with 2.7 and is no longer supported.
([Source code](https://code.google.com/p/gwt-designer/) is available
if someone wishes to revive this project.)

- IFrameLinker and XSLinker are deprecated because they don't work in
Super Dev Mode. However, we don't have suitable replacements for all
use cases yet. For updates and possible workarounds, see
[issue 8997](https://code.google.com/p/google-web-toolkit/issues/detail?id=8997).

### Compiler changes

- In draft mode and Super Dev Mode, all compiler optimizations are turned off for
better debugging. For example, null pointers are detected sooner.

- JSNI references no longer require fully qualified class names when this
wouldn't be necessary in Java. (For example, imports work.)

- We've started implementing [JS Interop annotations](http://goo.gl/nGeiq7),
which will make it much easier to use GWT with JavaScript libraries.
The specification is not final and there are bugs, so production GWT apps and
libraries should continue to use JSNI for now. If you wish to experiment,
you can enable JS Interop using the `-XjsInteropMode` flag, which is available
for the compiler and Super Dev Mode. (It doesn't work with old DevMode.)

- The experimental `-XmethodNameDisplayMode` flag adds a `displayName`
property to each JavaScript function containing the name of the Java method.
This makes Java method names available in browser debuggers at the expense
of code size. (Also available in Super Dev Mode.)

- Boxed JavaScript strings (created in JavaScript using `new String(...)`)
are no longer considered equivalent to Java strings. They should be unboxed
before being passed to Java.

- Many bugfixes.

### Library Changes

#### JDK emulation

- Significant performance improvements in `String`, `ArrayList`, `HashMap`,
and `Exception`.

- New emulated classes: `Locale`, `NavigableSet`, and `NavigableMap`.

- New emulated methods in `Class`, `String`, `Exception`, `RuntimeException`,
`Logger`, `Arrays`, `Collections`, and `Map.Entry`.

- `LinkedList` extends `Deque` and handles incorrect usage better.

#### Logging and Stack Traces

- Better wrapping of exceptions thrown from JavaScript.

- GWT apps that inherit the `com.google.gwt.logging.Logging` module
have different default behavior for messages logged using the
`java.util.logging` package. The new default is to log messages at
level `SEVERE` and above to the browser's console. `PopupLogHandler`
and `SystemHandler` are no longer enabled by default.

- `FirebugLogHandler` and `NullLoggingPopup` have been removed. ()

#### Experimental GSS support

The `CssResource.enableGss` configuration property turns on GSS support.

- When enabled, resource files with a 'gss' extension are parsed as a
[Closure Stylesheet](https://code.google.com/p/closure-stylesheets/).

- When enabled, GSS can be used in a UiBinder file by setting
`gss="true"` on a `ui:style` tag.

- If the `CssResource.legacy` configuration property is set,
.css resources and `ui:style` tags without `gss=true` will first be converted to GSS
and then parsed as GSS.

#### UiBinder

- The `ui:data` tag has new attributes: `mimeType` and `doNotEmbed`.

#### GWT-RPC

- The `rpc.XserializeFinalFields` configuration property turns on experimental
support for serializing final fields.

- `LinkedHashSet` may be serialized without a serialization policy.

- deRPC is removed.

#### RequestFactory

- Support overridden methods and generics better.

- Fix support for `@SkipInterfaceValidation` on `RequestContext` methods.

#### Internationalization

- Upgraded to CLDR 25.

#### Browser API changes

- Updated support for typed arrays.

- Added `History.replaceItem()`.

- Fixed an issue with `Window.addWindowScrollHandler` on Chrome.

#### Widgets

- The deprecated `com.google.gwt.widgets` package is removed.

- Various bugfixes and minor improvements.

### Developer Tool Changes

#### Dev Mode

- The `-nosuperDevMode` flag may be used to turn off Super Dev Mode and
revert to old Dev Mode. (However, most current browsers no longer support
Dev Mode plugins.)

- The `-modulePathPrefix` flag may be used to move DevMode's output to a
subdirectory of the war directory.

#### Super Dev Mode

- Compiling is much faster after the first compile. (Compiling is skipped
altogether if no files have changed.)

- The first compile no longer happens at startup.

- Chrome reloads the page faster while debugging. (Sourcemap file size is
reduced.)

- The `-launcherDir` flag may be used to avoid running the GWT compiler
before starting Super Dev Mode. When enabled, Super Dev Mode writes
stub .nocache.js files that automatically recompile the GWT app before
loading it. Therefore the bookmarklets aren't needed.
(This feature is automatically enabled when launched via DevMode.)

- The `-logLevel` flag may be used to adjust how compile errors are reported.

- The `Dev Mode On` bookmarklet dialog shows whether Super Dev Mode is turned on
for each module on the page.

- Messages logged using `java.util.logging` at level `SEVERE` and above are
written to the browser console by default.

- Fixed a startup failure caused by locked directories on Windows.

#### Testing

- Better error reporting for compile errors while running tests.

- Messages logged using `java.util.logging` at level `SEVERE` and above are written
to the browser console and test output by default.

- `-Dgwt.htmlunit.debug` may be used to open a JavaScript debugger window when
running a test using HtmlUnit.

- Removed `RunStyleRemoteWeb` and the `BrowserManager` tool.

- Removed flags: `-standardsMode`, `-nostandardsMode`, `-quirksMode`. (GWTTestCase
tests are always run in an HTML page in standards mode.)

For even more detail, see the [Issue Tracker.]
(https://code.google.com/p/google-web-toolkit/issues/list?can=1&q=Milestone%3D2_7+status%3AFixed)

<a id="Release_Notes_Current"></a>

## <a id="Release_Notes_2_6_1"></a> Release Notes for 2.6.1

This release contains bug fixes for
[GWT 2.6.0](#Release_Notes_2_6_0).

### Highlights

- Fixed classloader problems when running web apps in Development Mode.
    - [Issue 8526](https://code.google.com/p/google-web-toolkit/issues/detail?id=8526)
    - [Issue 8585](https://code.google.com/p/google-web-toolkit/issues/detail?id=8585)
- Restored GWT Designer support.
- Fixed JSNI calls that return primitive values when in Development Mode.
[Issue 8548](https://code.google.com/p/google-web-toolkit/issues/detail?id=8548)
- Fixed Super Dev Mode for Windows and Internet Explorer (The page must have an HTML5 doctype).
    - [Issue 8587](https://code.google.com/p/google-web-toolkit/issues/detail?id=8587)
    - [Issue 8618](https://code.google.com/p/google-web-toolkit/issues/detail?id=8618)
    - [Updated cache headers](https://gwt-review.googlesource.com/#/c/7510/)

A complete list of changes can be found
[here](https://gwt.googlesource.com/gwt/+log/2.6.0..2.6.1).

## <a id="Release_Notes_2_6_0"></a> Release Notes for 2.6.0

This release includes minor updates to silence unnecessary debugging warnings.
See the release notes for [2.6.0 (RC1)](#Release_Notes_2_6_0_RC1)
for the full list of features and bugs fixes included in the GWT 2.6.0 release.

## <a id="Release_Notes_2_6_0_RC4"></a> Release Notes for 2.6.0 (RC4)

This release enabled the Super Dev Mode hook by default, updated the sample
Maven POMs, included a Firefox memory leak fix for Dev Mode, and a few other
minor regressions noted during release candidate testing.

## <a id="Release_Notes_2_6_0_RC3"></a> Release Notes for 2.6.0 (RC3)

This release fixed an incompatibility with the Google Plugin for Eclipse,
improved uncaught exception handling, and reverted some backwards-incompatible
changes made since GWT 2.5.1.

## <a id="Release_Notes_2_6_0_RC2"></a> Release Notes for 2.6.0 (RC2)

This release disabled the Opera permutation, added a more maintainable
DOM event dispatch mechanism, and fixed a few GWT-RPC and IE11 issues
raised during release candidate testing.

## <a id="Release_Notes_2_6_0_RC1"></a> Release Notes for 2.6.0 (RC1)

### Highlights

- Java 7 is supported and is now the default. (This can be overridden using 
`-sourceLevel 6`)
- GWT Development Mode will [no longer be available for Chrome](http://blog.chromium.org/2013/09/saying-goodbye-to-our-old-friend-npapi.html)
sometime in 2014, so we improved alternate ways of debugging. There are improvements to Super Dev Mode, asserts,
console logging, and error messages.
- Internet Explorer cleanup: IE6/7 support is disabled by default in this release and will be gone in the next
major release. IE10 is now a separate permutation.

### Compiler changes

- A `@GwtIncompatible` annotation may be used to mark classes, methods, and fields
that the GWT compiler should ignore. (Any annotation with this name can be used,
regardless of package.)

#### Flag cleanup

Flags have been cleaned up for consistency, but the old flags are still supported for backward
compatibility.

- Arguments may be specified multiple times on the command line and the last one wins.
- Boolean flags can consistently be disabled using '-no'.
- Experimental flags consistently start with '-X' or '-Xno' to disable.
- The deprecated `-out` flags were removed
- Added flags for turning specific optimizations on and off. 
- The `-saveSource` and `-saveSourceOutput *dest*` options may be used to write source files used
by the GWT app to an output directory. (Combined with the `includeSourceMapUrl` config property,
it is possible to set up source-level debugging outside Super Dev Mode.)

#### Changes to generated JavaScript

- Catching and rethrowing a JavaScript exception no longer wraps it in a Java exception
   (so the console will print it correctly, etc.)
- Failed assertions stop in the browser's debugger (if open)
- Various code size improvements.

#### Code Splitting

- Fragment merging is more reliable, works with soyc reports
- GWT.runAsync: passing the same class is allowed, puts the code in the same fragment
- GWT.runAsync always runs asynchronously; before it would sometimes be synchronous.
(This behavior can be reverted by inheriting SynchronousFragmentLoadCallback.gwt.xml)
- The `compiler.splitpoint.leftovermerge.size` configuration property sets a minimum size
for fragments
- AsyncProxy is deprecated
- Various bugfixes

#### JavaScript Interoperability

- JavaScriptObject: added createArray(size)
- JsMixedArray: getString() fixed for nulls
- JSNI: Allow line breaks (and other whitespace) within JSNI method references
- JSNI: Don't discard unary '+' when it's used to cast to a double [issue 6373](https://code.google.com/p/google-web-toolkit/issues/detail?id=6373),
[3942](https://code.google.com/p/google-web-toolkit/issues/detail?id=3942)

#### Generator API

- PropertyOracle: removed deprecated methods
- JRawType.getImplementedMethods: fixed type parameters for inherited methods

#### Bugfixes

- Fixed the stale persistentUnitCache bug [issue 7794](https://code.google.com/p/google-web-toolkit/issues/detail?id=7794)
- Various fixes to code generation [4830](https://code.google.com/p/google-web-toolkit/issues/detail?id=4830), [7088](https://code.google.com/p/google-web-toolkit/issues/detail?id=7088), [7253](https://code.google.com/p/google-web-toolkit/issues/detail?id=7253), [8304](https://code.google.com/p/google-web-toolkit/issues/detail?id=8304), [6638](https://code.google.com/p/google-web-toolkit/issues/detail?id=6638)

### Libraries

#### JDK Emulation

- java.lang.Class: added getSimpleName()
- ArrayList.removeRange(): fixed
- StringBuilder: added appendCodePointInt()
- StringBuffer/Builder: added reverse()
- Number subclasses:
    - integer parsing accepts initial '+' (for Java 7 compatibility)
    - add compare methods (for Java 7)
    - fixed isFinite/isInfinite [issue 8073](https://code.google.com/p/google-web-toolkit/issues/detail?id=8073)
- java.lang.reflect.Type: added
- java.util.Objects: added (Java 7)
- java.sql.Timestamp: fixed NullPointerException in equals() [issue 6992](https://code.google.com/p/google-web-toolkit/issues/detail?id=6992)

#### Core library fixes

- GWT.debugger() emits a JavaScript `debugger` statement
- GWT.maybeReportUncaughtException() sends an exception to the
uncaught exception handler (if any)
- About: cleaned up
- ConsoleLogHandler: fixed for IE and Firefox [issue 6916](https://code.google.com/p/google-web-toolkit/issues/detail?id=6916), [issue 8040](https://code.google.com/p/google-web-toolkit/issues/detail?id=8040)
- Stack traces fixed on iOS.
- Timer: cancel() fixed for IE6-8 [issue 8101](https://code.google.com/p/google-web-toolkit/issues/detail?id=8101)

#### Browser permutation changes

- The `ie6` permutation (which also handles IE 7) is now disabled by default.
Support for IE6 and IE7 will be removed in the next major GWT release.

- Added the `ie10` permutation. There's no fallback value, so deferred bindings
and conditional CSS that explicitly checks `user.agent` may need to be updated. (However, note
that workarounds needed for previous versions of IE may no longer be
necessary.)

- UserAgent: new class to access `user.agent`

#### Browser API changes

- Element methods that return sizes in pixels automatically convert subpixel
values to int (for backward compatibility).

- All API's that used to take `com.google.gwt.user.client.Element` (which
has long been deprecated) now take a `com.google.gwt.dom.client.Element` instead.

- DOM methods that take a URL now accept a SafeUri object as well.

- Node: added removeAllChildren()
- Element: added toggleClassName()
- Element.hasTagName() is now case-insensitive
- Element subclasses: added is() methods, for example DivElement.is(elem)
- user.client.DOM: deprecate old methods
- KeyCodes: many more key codes, added isArrowKey()
- HandlerRegistrations: added compose() method
- Canvas: added drawImage() overloads, wrap() method
- Animation: added isRunning()

- DOM events not known to GWT can handled using Widget.addBitlessDomHandler().
This allows third-party libraries to handle events that GWT itself doesn't know
about (such as MsPointerEvents). [issue 8379](https://code.google.com/p/google-web-toolkit/issues/detail?id=8379)

#### HTML generation changes

- HtmlElementBuilder: supports the `<col>` tag
- HtmlBuilderFactory: return types are more specific
- Builder methods that take a URL now accept SafeUri as well

#### CSS changes

- @url now supports ImageResource
- user.agent values have changed (see Browser permutation changes)

#### UiBinder changes

- @UiHandler works with parameterized types [issue 6091](https://code.google.com/p/google-web-toolkit/issues/detail?id=6091)
- @UiHandler works with bindery.event.shared.HandlerRegistration [issue 7079](https://code.google.com/p/google-web-toolkit/issues/detail?id=7079)

#### Widget changes

- CellView: focus fix [issue 8359](https://code.google.com/p/google-web-toolkit/issues/detail?id=8359)
- DataGrid fixes: style name, scroll bars [issue 8309](https://code.google.com/p/google-web-toolkit/issues/detail?id=8309)
- DatePicker: lots of improvements.
- DialogBox: fix auto-hide memory leak
- FileUpload: fixed wrap() method [issue 5055](https://code.google.com/p/google-web-toolkit/issues/detail?id=5055)
- HtmlTable,FlexTable,Grid: fixed memory leak in IE9/IE10 [issue 6938](https://code.google.com/p/google-web-toolkit/issues/detail?id=6938)
- ListBox.setMultipleSelect undeprecated
- MenuBar: focus fix [issue 3884](https://code.google.com/p/google-web-toolkit/issues/detail?id=3884)
- RootPanel: added clear()
- SimpleCheckBox: implement HasValue
- SimplePager: added constructor to hide "first page" button
- SingleSelectionModel: fixed getSelectedSet
- SplitLayoutPanel: resizing fix [issue 4755](https://code.google.com/p/google-web-toolkit/issues/detail?id=4755)
- SuggestBox:
    - changed to avoid firing events twice [issue 3533](https://code.google.com/p/google-web-toolkit/issues/detail?id=3533)
    - after selecting, move focus to the next field [issue 8051](https://code.google.com/p/google-web-toolkit/issues/detail?id=8051)
- Tree/TreeItem: deprecated methods removed
- ValueListBox: implement Focusable
- ValuePicker: fixed setValue() to not fire events [issue 7330](https://code.google.com/p/google-web-toolkit/issues/detail?id=7330)

#### Accessibility

- UiObject.setVisible() only adds aria-hidden for hidden objects

#### Internationalization

- Document.{get,set}ScrollLeft(): fixed RTL for Safari and IE9
- Plural rules updated for some Slavic languages
- Fixed currency formatting when the currency symbol comes last
  (the symbol was added twice)
- Number format constants upgraded to CLDR 21

#### Editor framework

- CompositeEditor and subclasses take `Editor<? super C>`

#### RequestFactory

- Added support for Maps [issue 5524](https://code.google.com/p/google-web-toolkit/issues/detail?id=5524)
- Added support for enums as type parameters in requests
- Added support for EntityProxyId as a request parameter
- Fixed a NullPointerException when server returns null [issue 8104](https://code.google.com/p/google-web-toolkit/issues/detail?id=8104)

#### Server-side classes

- StackTraceDeobfuscator cleaned up and moved to gwt.core.server

### Developer Tools

#### Developer Mode

- Starts the user's preferred browser on Linux
- Fixed primitive class references in JSNI code (e.g. @int::class)
- Reduced memory leakage

#### Super Dev Mode

- Security: with 2.6 we believe it's safe to turn on the Super Dev Mode
hook and leave it on in production. As an extra precaution, we recommend
setting the devModeUrlWhitelistRegexp configuration property to ensure that
it can only load JavaScript from localhost and your developers' machines in
your own domain.

- Security: automatically disable Super Dev Mode on https pages (Another
precaution, and it doesn't work anyway due to mixed-content restrictions.)

- WebAppCreator now creates Ant projects with a "superdevmode" target and
Super Dev Mode enabled.
- Most sample apps have a "superdevmode" target
- GWT.log() works and prints basic stack traces
- sourceUrl comment fixed so that in a JavaScript debugger, the JavaScript source shows
up as '{module}-0.js' instead of just '0.js'.
- Grey out unused Java in source code listings on the code server.
- RemoteServiceServlet: the gwt.codeserver.port parameter may be used to
download serialization policies from a Super Dev Mode code server's
/policies/ directory. This can be used to avoid some server recompiles when
working on GWT-RPC code.

#### Testing

- Benchmarking and Profile support removed
- GWTTestCase reports better error messages in many cases
- GWTTestCase: always use an UTF-8 HTML page
- GWTTestCase: removed supportsAsync, addCheckpoint, clearCheckpoint, getCheckpoints
- GWTTestCase now uses its own uncaught exception handler to avoid
conflicts when testing code that calls GWT.setUncaughtExceptionHandler.

### Upgraded dependencies

- HtmlUnit 2.13
- Jetty 8.1
- Servlet 3.0
- Guava 15.0
- Protobuf 2.5.0
- ASM 4.1
- JDT 3.8.3
- Closure compiler 20131014

For even more detail, see [Changelists up to 2.6.0-rc1](https://gwt.googlesource.com/gwt/+log/2.5.1..2.6.0-rc1)
and the [issue tracker](https://code.google.com/p/google-web-toolkit/issues/list?q=Milestone%3D2_6).

## <a id="Release_Notes_2_5_1"></a> Release Notes for 2.5.1

This release includes an update to the sample application's Maven POM
files. See the release notes for [2.5.1
(RC1)](#Release_Notes_2_5_1_RC1) for the full list of features and bugs fixes included in the GWT
2.5.1 release.

## <a id="Release_Notes_2_5_1_RC1"></a> Release Notes for 2.5.1 (RC1)

GWT 2.5.1 is a maintenance release including many bugfixes and
minor improvements since GWT 2.5.

### Changes since 2.5.0

#### Security Fixes

*   Fixed an XSS vulnerability in html files used by GWTTestCase [(patch)](https://code.google.com/p/google-web-toolkit/source/detail?r=11385).
These files will only be included in a GWT app if it depends on the
JUnit module. Despite the fix, this is not recommended.

#### Compiler and Linkers

*   Minor optimization improvements.
<li>Optimization bugfixes: (
[5739](https://code.google.com/p/google-web-toolkit/issues/detail?id=5739),
[7818](https://code.google.com/p/google-web-toolkit/issues/detail?id=7818),
[7683](https://code.google.com/p/google-web-toolkit/issues/detail?id=7683)).
*   Apps built with DirectInstallLinker should work in a page
where inline scripts are forbidden (e.g. a Chrome extension)

#### Developer Mode

*   Fixed compatibility with OpenJDK 7.
*   Removed GWTShell, an obsolete way of starting dev mode.
*   Added a checkbox to automatically scroll the log.

Also, the Chrome plugin is now in the Chrome store.

#### Super Dev Mode

*   Added a -noprecompile option for faster startup.
*   Added support for IE8 (recompiles only, not debugging).
*   Reduced download size by using gzip.
*   Fixed an incompatibility with the debugger in Chrome 24+.
*   Fixed an incompatibility with third-party JavaScript that
modifies the Array prototype.

#### Core libraries and JDK emulation

*   Changed Window.Location to return the new parameter values
after a call to window.history.pushState
*   Added an option to ScriptInjector that removes the `<script>` tag from the page when done.
*   Added RequestBuilder.withCredentials().
*   Fixed a performance issue in RepeatingCommand ([7307](https://code.google.com/p/google-web-toolkit/issues/detail?id=7307))
*   Fixed stack trace deobfuscation on iOS ([7902](https://code.google.com/p/google-web-toolkit/issues/detail?id=7902))
*   Added the "webApiUsage" property. The default setting is
"modern". If set to "stable", GWT libraries will avoid using prefixed
web API's. Currently, the only difference is that animation will use a
timer. ([7895](https://code.google.com/p/google-web-toolkit/issues/detail?id=7895))
*   Fixed Double.parseDouble() when called with Nan, Infinity, and
leading zeros ([7713](https://code.google.com/p/google-web-toolkit/issues/detail?id=7713),
[7834](https://code.google.com/p/google-web-toolkit/issues/detail?id=7834))
*   Fixed Long.parseLong when called with MIN_VALUE ([7308](https://code.google.com/p/google-web-toolkit/issues/detail?id=7308))
*   Fixed a bug in AbstractMap.remove ([7856](https://code.google.com/p/google-web-toolkit/issues/detail?id=7856))

#### HTML, CSS, resource files

*   Element's addClassName and removeClassName methods now return
a boolean.
*   Bugfix for getBodyOffsetLeft and Top: [(patch))(https://code.google.com/p/google-web-toolkit/source/detail?r=11432)
*   Added CSS support for media types ([4911](https://code.google.com/p/google-web-toolkit/issues/detail?id=4911))
*   Added "debug" obfuscation style for CSS [(change))(https://code.google.com/p/google-web-toolkit/source/detail?r=11442)
*   Improved the quality of resized image resources ([7193](https://code.google.com/p/google-web-toolkit/issues/detail?id=7193))

#### Widgets and Editors

*   Added Tree.setScrollIntoViewEnabled ([2467](https://code.google.com/p/google-web-toolkit/issues/detail?id=2467))
*   Fixed a bug in SplitLayoutPanel.setWidgetHidden ([7715](https://code.google.com/p/google-web-toolkit/issues/detail?id=7715))
*   Improved click handling in CellTable ([7508](https://code.google.com/p/google-web-toolkit/issues/detail?id=7508))
*   Editor framework bugfixes: ([5589](https://code.google.com/p/google-web-toolkit/issues/detail?id=5589),
[6959](https://code.google.com/p/google-web-toolkit/issues/detail?id=6959))

#### Internationalization

*   Upgrades for [ICU4J](https://code.google.com/p/google-web-toolkit/source/detail?r=11474)
and [CLDR](https://code.google.com/p/google-web-toolkit/source/detail?r=11412)
*   Fixed a bug in DateTimeFormat.parse() ([7823](https://code.google.com/p/google-web-toolkit/issues/detail?id=7823))

#### RPC and server-side Java

*   Fixed incorrect GWT-RPC deserialization when an app is
deployed after changing an enum and nothing else. ([7836](https://code.google.com/p/google-web-toolkit/issues/detail?id=7836))
*   Fixed a GWT-RPC generator bug that can cause too many policy
files to be generated, resulting in excessive server-side memory
usage. ([7791](https://code.google.com/p/google-web-toolkit/issues/detail?id=7791))
*   Fixed an infinite loop in server-side GWT-RPC when
deserializing complicated generic types. ([7779](https://code.google.com/p/google-web-toolkit/issues/detail?id=7779))
*   RequestFactory bug fix: ([7900](https://code.google.com/p/google-web-toolkit/issues/detail?id=7900))
*   Added a constructor option to StackTraceDeobfuscator for
conserving server memory.
*   Fixed an exception when using shared code that uses GWTBridge
on the server ([7527](https://code.google.com/p/google-web-toolkit/issues/detail?id=7527))

## <a id="Release_Notes_2_5_0"></a> Release Notes for 2.5.0

This release includes some minor bug fixes found in the release
candidate. See [What's New in
GWT 2.5](doc/latest/ReleaseNotes.html) plus the release notes for [2.5.0
(RC1)](#Release_Notes_2_5_0_RC1) and [2.5.0 (RC2)](#Release_Notes_2_5_0_RC2) for the
full list of features and bugs fixes included in the GWT 2.5.0 release.

### Security vulnerability from 2.4 to 2.5 Final

The GWT team recently learned that the Security vulnerability
discovered in the 2.4 Beta and Release Candidate releases was only
partially fixed in the 2.4 GA release. A more complete fix was added to
the 2.5 GA release. If you have an app that's been built with GWT 2.4
or one of the 2.5 RCs, then you'll need to get the latest 2.5 release,
recompile your app, and redeploy.

## <a id="Release_Notes_2_5_0_RC2"></a> Release Notes for 2.5.0 (RC2)

This is release candidate 2 of GWT 2.5. See the [What's new in GWT 2.5](doc/latest/ReleaseNotes.html) page as
well as release notes below for the full list of features and bugfixes
in this release.

### Changes since RC1

*   The GWT tools can now run on JDK 7. (However, no JDK 7
language or library features are available in GWT code yet.)
*   The accessibility library introduced in RC1 has been cleaned
up for release.
*   Support for [validation](doc/latest/DevGuideValidation.html)
is improved, documented, and no longer considered experimental.
*   Other fixes; see the [issue
tracker](https://code.google.com/p/google-web-toolkit/issues/list?q=FixedIn2_5RC2) for more.

## <a id="Release_Notes_2_5_0_RC1"></a> Release Notes for 2.5.0 (RC1)

This is release candidate 1 of GWT 2.5. See the [What's new in GWT 2.5](doc/latest/ReleaseNotes.html) page as
well as release notes below for the full list of features and bugfixes
in this release.

### Major Enhancements

*   Super Dev Mode, a replacement for Development Mode
(experimental)
*   The Elemental library (experimental): efficient DOM access and
HTML5 API's (experimental)
*   New compiler optimizations from Closure
*   The fragment merging optimization, for reducing latency in
large apps
*   Development mode performance and refresh time speedups
*   A new accessibility library for setting [ARIA](http://www.w3.org/TR/wai-aria/) roles, states and
properties on DOM elements
*   UIBinder enhancements for rendering cells and accessing inline
styles

### Other Fixes and Enhancements

*   Significant speedups in development refresh time as a result
of generator result caching for the ClientBundle and RPC generators.
Addition of a result caching API that generator writers can use
(r10412, r10476).
*   Drastic improvement in RPC performance in development mode by
providing a development-mode-only implementation for RPC.
*   Addition of source map support. See [this
document](http://code.google.com/p/google-web-toolkit/wiki/SourceMaps) for more details.
*   Reduction of overhead of Java types in compiler output
(r10435).
*   Addition of CellTableBuilder API, and an API for custom
headers and footers in CellTables (r10435, r10581).
*   Addition of pre-deserialization type checking to reduce the
number of error messages that occur from attempts to invoke an RPC
method with type mismatches (r10518).
*   DynaTableRF and Validation samples now use Maven instead of
ant.
*   DateCell now accepts a TimeZone as a constructor arg.
*   `<ui:with>` now supports the nested `<ui:attribute>`
element for setting property values on a provided type (r10675).
*   Improvements in reporting of JavaScriptExceptions in
development mode to include the method name and arguments.
*   Added ability to mock out Messages and CSSResources for better
unit testing (r10723, r10768).
*   Added SafeHtml support to parts of the GWT user library.
*   CssResource @defs now support multiple values (r10667).
*   Added option to compile out logging calls below SEVERE and
WARN (r10753).
*   CLDR data updated to v2.1.
*   Added simple and global currency patterns (r10742).
*   Update ISO-8601 time format to accept a timezone of a literal
"Z" to mean GMT+0 (r10773).
*   Various fixes to stabilize the compiler output on null (no
change) recompiles. Added the -Dgwt.normalizeTimestamps system
property to zero out the date on output jar files to stabilize builds
in build systems which rely on content-addressability (r10810).
*   Added GWT emulation for Float.intBitsToFloat,
Float.floatToIntBits, Double.longBitsToDouble, and
Double.doubleToLongBits.
*   Added support for typed arrays (r10839).
*   Greatly reduce symbol map size by removing already-pruned
symbols from the map.
*   Allow absolute paths in ui:style's src attribute.
*   Request methods that use Collections as parameters in
RequestFactory's JSON-RPC mode now properly encode Collections.

### Breaking Changes and Other Changes of Note

*   If you previously linked against gwt-user.jar from pure Java
code and now get ClassNotFoundExceptions, you may need to add
gwt-dev.jar to your classpath as well.
*   Deprecated methods
AbsractCellTable.doSelection(Event,Object,int,int),
AbstractHasData.onUpdateSelection(), and CellList.doSelection(Event,
Object, int) have been removed.
*   Major refactoring of IsRenderable API. See r10352 and r10421
for details.
<li>The LEFT/RIGHT placement of text in SimplePager has been
fixed, which may swap it for people who already worked around the
issue. See r10907.
*   Tree[Item].addItem/insertItem(String html) have been
deprecated due to potential XSS issues. Use
Tree[Item].addTextItem/insertTextItem(String text) instead.
*   @GwtTransient will now work for any annotation that with that
simple name (for example, com.foo.GwtTransient). This prevents library
writers from having their library depend on the GWT distribution.
*   As part of the GWT compilation, an output file mapping full
class names to CSS obfuscated names is generated. This is useful for
debugging and testing (r10686).
*   Client-side JUnit classes are now available as a separate
module without having to pull in GWTTestCase (r10689).
*   The permutation mapping file is now generated as part of every
compile.
*   UIBinder.useLazyWidgets is now set to true by default. See
r10730 for more details.
*   The following dependencies have been updated to the following
versions:

    *   Eclipse JDT 3.4.2_r894
    *   Guava 10.0.1
    *   HTMLUnit 2.9
    *   Apache HTTP Client 4.1.2 (for HTMLUnit)
    *   Apache Commons Lang 2.6 (for HTMLUnit)
    *   NekoHTML 1.9.15 (for HTMLUnit)
*   json-1.5.jar is now bundled as a part of gwt-dev.jar.
streamhtmlparser (rebased) has been removed from gwt-dev.jar and has
been bundled as part of gwt-user.jar and gwt-servlet.jar.
*   The compiler metrics aspect of the Compile Report has been
disabled by default. The compile report results in instability in the
Compile Report. To add compiler metrics to the compilation report, use
the -XcompilerMetrics flag.
*   The compiler.emulatedStack property should no longer be used.
compiler.stackMode is what should be used instead.

In addition to the items mentioned above, see [for
a list of bug fixes and enhancements for 2.5.0](https://code.google.com/p/google-web-toolkit/issues/list?can=1&q=Milestone%3A2_5+status%3AFixed) in the GWT issue
tracker.

### Known Issues

*   Clicking in the navigation area (but not on an actual item) of
the Showcase sample results in the navigation area going blank.
*   Importing samples/Expenses into Eclipse fails with "Main Type
Not Specified" dialog.
*   Mismatch in symbol map format between SpeedTracer and GWT
results in SpeedTracer not being able to deobfuscate stack traces for
GWT applications.

* * *

## <a id="Release_Notes_2_4_0"></a> Release Notes for 2.4.0

This is the General Availability release of GWT 2.4. See the release notes below for the full list of features and bug
fixes included in this release.

The 2.4 General Availability release of GWT contains new App
Engine tools for Android, incremental RPC tooling, Apps Marketplace
support, a faster UI Designer with better UiBinder support, a
persistent unit cache for faster iterative development, a scrolling
DataGrid with fixed header, Beans.isDesignTime() support, and bundled
installers that make it easier to install and configure the GPE, GWT
and GAE.

### General Enhancements

*   App Engine tools for Android: Build installable Android apps
that rely on App Engine for server-side support.
*   Incremental RPC Tooling: Add server-side methods to App Engine
code and GPE will generate the necessary serialization and Android
code on the fly.
*   Apps Marketplace Support: Deploy apps to the Google Apps
Marketplace as easily as to App Engine.
*   UI Designer: Faster startup and editing times, split-mode
editing support for UiBinder, simplified CSS property editing,
UiBinder morphing, IsWidget support, and more.
*   Persistent Unit Cache: GWT Compiler and Development mode now
cache compilation artifacts between runs. This results in faster
startup time for iterative development.
*   Scrolling DataGrid ([#188](https://code.google.com/p/google-web-toolkit/issues/detail?id=188)):
The new DataGrid widget supports vertical scrolling with a fixed
header (above) and footer (below).
*   Design Time Support ([#226](https://code.google.com/p/google-web-toolkit/issues/detail?id=226)):
The Beans.isDesignTime() method was added to the GWT emulation library
in order to better isolate runtime-only code when a UI is edited in
the UI Designer.

### Noteworthy Fixed Issues

*   Polymorphism not supported by RequestFactory ([#5367](https://code.google.com/p/google-web-toolkit/issues/detail?id=5367))
*   Support RequestFactory service inheritance on the client ([#6234](https://code.google.com/p/google-web-toolkit/issues/detail?id=6234))
*   ListEditor subeditors' value is not flushed when used with a
RequestFactoryEditorDriver ([#6081](https://code.google.com/p/google-web-toolkit/issues/detail?id=6081))
*   Memory-leak in pure-Java's c.g.g.core.client.impl.WeakMapping
([#6193](https://code.google.com/p/google-web-toolkit/issues/detail?id=6193))
*   GWT compiler dropping clinits ([#5707](https://code.google.com/p/google-web-toolkit/issues/detail?id=5707))
*   Make RequestFactory type tokens more compact ([#5394](https://code.google.com/p/google-web-toolkit/issues/detail?id=5394))
*   Editor framework does not support is / has methods ([#6040](https://code.google.com/p/google-web-toolkit/issues/detail?id=6040))

See [the
complete list of bug fixes and enhancements for 2.4.0](https://code.google.com/p/google-web-toolkit/issues/list?can=1&q=Milestone%3A2_4+status%3AFixed) in the GWT issue
tracker.

See [the
complete list of breaking changes](https://code.google.com/p/google-web-toolkit/issues/list?can=1&q=Milestone%3A2_4+ReleaseNote%3DBreakingChange+status%3AFixed) in the GWT issue tracker.

### Security vulnerability in GWT 2.4

Recently, the GWT team discovered a cross-site scripting
vulnerability in the 2.4 Beta and Release Candidate releases (not in
v2.3 GA or v2.4 GA). This vulnerability was partially fixed in the 2.4
GA release and completely fixed in the 2.5 GA release. If you have an
app that's been built with 2.4 then you'll need to get the latest 2.5
release, recompile your app, and redeploy.

### Notes and Known Issues

*   GPE's [Maven
support](http://code.google.com/p/google-web-toolkit/wiki/WorkingWithMaven) in Eclipse Indigo requires that the m2Eclipse WTP Integration
plugin be installed. It can be installed via the [JBoss
Update](http://download.jboss.org/jbosstools/updates/m2eclipse-wtp/) site. See the [Google
Maven wiki page](http://code.google.com/p/google-web-toolkit/wiki/WorkingWithMaven) for the canonical reference for working with Maven in
the latest GWT and GPE releases.
*   GWT's new [RequestFactory
configuration](http://code.google.com/p/google-web-toolkit/wiki/RequestFactory_2_4) is not enabled by default for plain GWT, App Engine, or
GWT + App Engine projects. It is enabled by default for new
Cloud-Connected Android Projects only. See the [RequestFactoryInterfaceValidation](http://code.google.com/p/google-web-toolkit/wiki/RequestFactoryInterfaceValidation)
wiki page for information on how to use the new
RequestFactoryInterfaceValidator within Eclipse, Maven, or the command
line.

* * *

## <a id="Release_Notes_2_3_0"></a> Release Notes for 2.3.0

This is the General Availability release of GWT 2.3. See the
release notes below for the full list of features and bug fixes
included in this release.

### General Enhancements

*   Added IE9 support. See the [IE9 - Tips and Tricks](doc/latest/DevGuideIE9.html) doc for
more information.
*   [2.3.0 (M1) - General
Enhancements](#Release_Notes_2_3_0_M1)

### Noteworthy Fixed Issues

*   [2.3.0 (M1) - Noteworthy
Fixed Issues](#Release_Notes_2_3_0_M1)

### Known Issues

*   At compile time, you may see a warning similar to the
following: "Configuration property UiBinder.useSafeHtmlTemplates is
false! UiBinder SafeHtml integration is off, leaving your users more
vulnerable to cross-site scripting attacks". This warning occurs
because although UiBinder HTML rendering has been updated to support [SafeHtml](doc/latest/DevGuideSecuritySafeHtml.html), by
default this is turned off (set to false), due to some minor bugs. If
you wish, you can change the default by setting the
"useSafeHtmlTemplates" property to true in UiBinder.gwt.xml. You can
determine whether you are affected by the known bugs by checking the
public bugs [6145](https://code.google.com/p/google-web-toolkit/issues/detail?id=6145),
[6149](https://code.google.com/p/google-web-toolkit/issues/detail?id=6149),
and [6198](https://code.google.com/p/google-web-toolkit/issues/detail?id=6198).

See [the
complete list of bug fixes and enhancements for 2.3.0](https://code.google.com/p/google-web-toolkit/issues/list?can=2&q=Milestone%3A2_3+status%3AFixedNotReleased) in the GWT issue
tracker.

* * *

## <a id="Release_Notes_2_3_0_M1"></a> Release Notes for 2.3.0 (M1)

This is milestone 1 of GWT 2.3.

### General Enhancements

*   Added the following functionality to the Google Plugin for
Eclipse:

    *   Google API integration
    *   Project import from Google Project Hosting
    *   Single sign on, for accessing Project Hosting and App Engine

*   Added GWT SDK support for HTML5 local storage

### Noteworthy Fixed Issues

*   Updated GPE's UIBinder editor to provide support for attribute
auto-completion based on getter/setters in the owner type
*   Optimizations to speed up GPE launch configuration UI
*   "Check for Updates", within GPE, will now detect updates to
GWT and GAE SDKs
*   Launching against an external URL that contains a port number
now works properly in Eclipse 3.6
*   Updated IE9 support [(#5125)](https://code.google.com/p/google-web-toolkit/issues/detail?id=5125)
*   Fixed iFrame loading issues within Internet Explorer [(#1720)](https://code.google.com/p/google-web-toolkit/issues/detail?id=1720)

See [the
complete list of bug fixes and enhancements for 2.3.0 M1](https://code.google.com/p/google-web-toolkit/issues/list?can=2&q=Milestone%3A2_3+status%3AFixedNotReleased) in the GWT
issue tracker.

* * *

## <a id="Release_Notes_2_2_0"></a> Release Notes for 2.2.0

The 2.2 release of GWT contains an integrated UI designer (part
of the Google Plugin for Eclipse), support for HTML5 functionality,
such as the Canvas/Audio/Video tags, an updated CellTable widget that
now supports sortable columns and fixed column widths, and a more
lenient SafeHtml template parser.

Also see [What's New in GWT 2.2?](doc/2.2/ReleaseNotes.html)

### General Enhancements

*   Touchstart, touchmove, touchend, touchcancel have been
integrated into the GWT event framework ([#5148](https://code.google.com/p/google-web-toolkit/issues/detail?id=5148))
*   Built-in bidi text support for widgets such as Label, HTML,
Anchor, Hyperlink and ListBox. More information on this feature can be
found [here](http://googlewebtoolkit.blogspot.com/2011/01/gwt-goes-bidirectional.html).

### General Changes

*   GWT Designer v8.1.1 and earlier versions do not support GWT
2.2. To use GWT Designer with GWT 2.2, you need to uninstall the older
version of GWT Designer and install the latest one.
*   Version 2.2 has deprecated support for Java 1.5, resulting in
warnings when building applications. While Java 1.5 will still work
for at least the next release of GWT, developers should upgrade their
version of Java to correct these warnings and ensure compatibility
with future versions of GWT.

### Noteworthy Fixed Issues

*   RC releases are now being deployed as non-SNAPSHOTs in the
maven repository ([#5429](https://code.google.com/p/google-web-toolkit/issues/detail?id=5429))
*   Date/Time patterns are correct in the "sv" local ([#5890](https://code.google.com/p/google-web-toolkit/issues/detail?id=5890))

See [the
complete list of bug fixes and enhancements for 2.2.0 M1, RC1 and
final release](https://code.google.com/p/google-web-toolkit/issues/list?can=1&q=Milestone%202_2%20status:Fixed,FixedNotReleased) in the GWT issue tracker.

* * *

## <a id="Release_Notes_2_1_1"></a> Release Notes for 2.1.1

### General Enhancements

*   Add a service layer to RequestFactory ([#5111](https://code.google.com/p/google-web-toolkit/issues/detail?id=5111))

### Noteworthy Fixed Issues

*   GPE flags `<g:MenuItem>` as an error ([#5453](https://code.google.com/p/google-web-toolkit/issues/detail?id=5453))
*   Unable to disable a MenuItem ([#1649](https://code.google.com/p/google-web-toolkit/issues/detail?id=1649))
*   No way to set vertical/horizontal alignment of Cells in
CellTable ([#5623](https://code.google.com/p/google-web-toolkit/issues/detail?id=5632))

See the GWT issue tracker for
[the complete list of bug fixes and enhancements](https://code.google.com/p/google-web-toolkit/issues/list?can=1&q=Milestone=2_1_1%20status:Fixed&sort=type&colspec=ID%20Type%20Status%20Owner%20Milestone%20Summary%20Stars)
in this release.

* * *

## <a id="Release_Notes_2_1_0"></a> Release Notes for 2.1.0

This release includes some minor bug fixes found in the release
candidate. See [What's New in
GWT 2.1](doc/2.1/ReleaseNotes.html) plus the release notes for [2.1.0
(RC1)](#Release_Notes_2_1_0_RC1) for the full list of features and bugs fixes included in the GWT
2.1.0 release.

* * *

## <a id="Release_Notes_2_1_0_RC1"></a> Release Notes for 2.1.0 (RC1)

This is release candidate 1 of GWT 2.1.

### Fixed Issues

*   Creation broken if the entity id is of type String ([#1430](https://jira.springsource.org/browse/ROO-1430))
*   addon-gwt is putting a boolean isChanged method in each proxy,
and those don't work ([#1457](https://jira.springsource.org/browse/ROO-1457))
*   ValueListBox showing redundant entries ([#1287](https://jira.springsource.org/browse/ROO-1287))
*   Implement new update / create / acquire / delete events ([#1238](https://jira.springsource.org/browse/ROO-1238))
*   Ensure that DefaultValueStore is always responsive ([#1217](https://jira.springsource.org/browse/ROO-1217))
*   Allow both String and Long keys ([#951](https://jira.springsource.org/browse/ROO-951))
*   Does the mobile.user.agent property provider actually work? ([#1468](https://jira.springsource.org/browse/ROO-1468))
*   Banging on the UI can produce NPEs in DevMode window ([#1282](https://jira.springsource.org/browse/ROO-1282))
*   NPE on resume in AbstractRecordListActivity([#1230](https://jira.springsource.org/browse/ROO-1230))
*   RequestFactoryServlet always throws when debugging with Chrome
([#1229](https://jira.springsource.org/browse/ROO-1229))
*   Implement java.math.BigDecimal support for GWT
*   Update the Expenses sample to pull dependencies in via a Maven
repo ([#991](https://jira.springsource.org/browse/ROO-991))
*   Add the custom Expense Report as a sample ([#965](https://jira.springsource.org/browse/ROO-965))
*   Find replacement for velocity ([#956](https://jira.springsource.org/browse/ROO-956))
*   Hard coded refusal to send fields named "password" in servlet
([#1262](https://jira.springsource.org/browse/ROO-1262))
*   No uncaught exception handler in scaffold app ([#1250](https://jira.springsource.org/browse/ROO-1250))
*   Remove web.xml welcome file handling from GWT addon ([#1512](https://jira.springsource.org/browse/ROO-1512))
*   Use a publicly accessible DTD for ApplicationCommon.gwt.xml ([#1271](https://jira.springsource.org/browse/ROO-1271))

### Known Issues

*   RequestFactory does not fail gracefully for primitive types [(5357)](https://code.google.com/p/google-web-toolkit/issues/detail?id=5357)
*   Allow CellTable headers/footers to be refreshed [(5360)](https://code.google.com/p/google-web-toolkit/issues/detail?id=5360)
*   Need to document what situations source change events [(5361)](https://code.google.com/p/google-web-toolkit/issues/detail?id=5361)
*   Enable tests for RequestFactoryPolymorphicTest [(5364)](https://code.google.com/p/google-web-toolkit/issues/detail?id=5364)
*   Bring history management to Expenses sample [(5366)](https://code.google.com/p/google-web-toolkit/issues/detail?id=5366)
*   Polymorphism not supported by Request Factory [(5367)](https://code.google.com/p/google-web-toolkit/issues/detail?id=5367)
*   Server cannot return unpersisted objects [(5374)](https://code.google.com/p/google-web-toolkit/issues/detail?id=5373)
*   Javadoc polymorphism rules [(5374)](https://code.google.com/p/google-web-toolkit/issues/detail?id=5374)
*   Stopping an ActivityManager from a PlaceChangeEvent might
cause an NPE [(5375)](https://code.google.com/p/google-web-toolkit/issues/detail?id=5375)
*   Why is gwt-servlet.jar a compile-time dependency in the
pom.xml generated by the expenses.roo script? [(5376)](https://code.google.com/p/google-web-toolkit/issues/detail?id=5376)
*   Why does gwt-user.jar have scope "provided" in the pom.xml
generated by the expenses.roo script? [(5377)](https://code.google.com/p/google-web-toolkit/issues/detail?id=5377)
*   Server side domain classes cannot be resolved [(5378)](https://code.google.com/p/google-web-toolkit/issues/detail?id=5378)
*   Confirm logging compiles out of generated scaffold apps [(5379)](https://code.google.com/p/google-web-toolkit/issues/detail?id=5379)
*   Many classes added in 2.1 still have the experimental API
warnings in their javadoc that need to be removed [(5380)](https://code.google.com/p/google-web-toolkit/issues/detail?id=5380)
*   Showcase Cell List show {2} in entries with JDK 1.5 [(5385)](https://code.google.com/p/google-web-toolkit/issues/detail?id=5385)
*   Many stale javadoc warnings of experimental API [(5380)](https://code.google.com/p/google-web-toolkit/issues/detail?id=5380)
*   Instance methods are not looked up properly in the
OperationRegistry [(5425)](https://code.google.com/p/google-web-toolkit/issues/detail?id=5425)

* * *

## <a id="Release_Notes_2_1_0_M3"></a> Release Notes for 2.1.0 (M3)

This is milestone 3 release of 2.1.

### Fixed Issues

*   Add History support to the generated GWT scaffold app ([#883](https://jira.springsource.org/browse/ROO-883))
*   Logging Implementation for M3 ([#888](https://jira.springsource.org/browse/ROO-888))
*   Implement remaining primitive property types, including List
and Long ([#933](https://jira.springsource.org/browse/ROO-933))
*   Add enum support ([#935](https://jira.springsource.org/browse/ROO-935))
*   Introduce setter methods for Record objects ([#938](https://jira.springsource.org/browse/ROO-938))
*   Spec the minimum work needed for user auth, sign in ([#955](https://jira.springsource.org/browse/ROO-955))
*   gwt project won't import properly in eclipse with m2eclipse,
has errors ([#1122](https://jira.springsource.org/browse/ROO-1122))
*   RequestFactoryServlet assumes content-length is known, which
it may not be ([#1150](https://jira.springsource.org/browse/ROO-1150))
*   Selecting "Delete" from an entity in the Scaffold app takes
you to a list view that contains no data. ([#1164](https://jira.springsource.org/browse/ROO-1164))
*   Allow client code to call instance methods ([#1185](https://jira.springsource.org/browse/ROO-1185))
*   Creates are clobbering existing records ([#1194](https://jira.springsource.org/browse/ROO-1194))
*   Update generated pom.xml to reference 2.1-SNAPSHOT jars ([#1200](https://jira.springsource.org/browse/ROO-1200))
*   Cleanup the implementation hack in DeltaValueStoreJsonImpl...
([#1209](https://jira.springsource.org/browse/ROO-1209))
*   Hard coded exit points for AbstractRecordEditActivity ([#1225](https://jira.springsource.org/browse/ROO-1225))
*   Hard coded exit points for AbstractRecordListActivity ([#1226](https://jira.springsource.org/browse/ROO-1226))
*   Can't create child resources from AbstractRecordEditActivity
anymore ([#1227](https://jira.springsource.org/browse/ROO-1227))
*   Update addon-gwt to reference GWT's M3 repo ([#1236](https://jira.springsource.org/browse/ROO-1236))
*   GWT addon has links to MVC scaffolded view artifacts ([#1249](https://jira.springsource.org/browse/ROO-1249))
*   RootPanel.get(id) fails to set 'position:relative' and
'overflow:hidden' ([#1813](https://code.google.com/p/google-web-toolkit/issues/detail?id=1813))
*   UncaughtExceptionHandler not triggered for exceptions
occurring in onModuleLoad() ([#1617](https://code.google.com/p/google-web-toolkit/issues/detail?id=1617))

* * *

## <a id="Release_Notes_2_1_0_M2"></a> Release Notes for 2.1.0 (M2)

This is milestone 2 release of 2.1.

* * *

## <a id="Release_Notes_2_1_0_M1"></a> Release Notes for 2.1.0 (M1)

This is a preview release of 2.1, that contains a new set of Cell
Widgets and an app framework that make it easier to build large scale
business applications.

### Known Issues

*   For App Engine-based apps, you will need to run Maven once
from the command line before importing the app into STS. This could be
as simple as running "mvn validate" or "mvn compile", the point is to
prep your local repo with an unzipped GAE SDK.
*   Compiling a new app and/or running it in dev mode, will
generate error messages complaining about missing dependencies. These
errors can be ignored as they are related to classes that are most
likely not used by your app, and will be omitted during compilation,
or while running in dev mode.

### Fixed Issues

*   Image.onload event does not fire on Internet Explorer when
image is in cache ([#863](https://code.google.com/p/google-web-toolkit/issues/detail?id=863))
*   Image should provide method to set alternative text ([#1333](https://code.google.com/p/google-web-toolkit/issues/detail?id=1333))
*   setWordWrap() for CheckBox ([#1483](https://code.google.com/p/google-web-toolkit/issues/detail?id=1483))
*   RichTextArea - setEnabled does not work ([#1488](https://code.google.com/p/google-web-toolkit/issues/detail?id=1488))
*   ImageSrc6 throws native NPE exception ([#1700](https://code.google.com/p/google-web-toolkit/issues/detail?id=1700))
*   Array returned inside Serializable field causes
ClassCastException in web mode ([#1822](https://code.google.com/p/google-web-toolkit/issues/detail?id=1822))
*   BigDecimal Support ([#1857](https://code.google.com/p/google-web-toolkit/issues/detail?id=1857))
*   When a MenuBar loses focus, the MenuItem remains selected ([#2458](https://code.google.com/p/google-web-toolkit/issues/detail?id=2458))
*   KeyPressEvent contains improper UTF codes ([#3753](https://code.google.com/p/google-web-toolkit/issues/detail?id=3753))
*   Make RemoteServiceRelativePath annotation
RetentionPolicy.RUNTIME ([#3803](https://code.google.com/p/google-web-toolkit/issues/detail?id=3803))
*   TextBox fires JSException in IE ([(#4027]())
*   Format number wrong result ([(#4173]())
*   file:line citations in ui.xml files ([#4194](https://code.google.com/p/google-web-toolkit/issues/detail?id=4194))
*   Remove method in SplitLayoutPanel is broken ([#4217](https://code.google.com/p/google-web-toolkit/issues/detail?id=4217))
*   Refactor SessionHandler and BrowserChannelClient to allow
other OOPHM clients than HtmlUnit ([#4287](https://code.google.com/p/google-web-toolkit/issues/detail?id=4287))
*   FinallyCommand scheduled from within another FinallyCommand
sometimes gets stuck ([#4293](https://code.google.com/p/google-web-toolkit/issues/detail?id=4293))
*   Wrong result after formatting of a big number in the
NumberFormat ([#4473](https://code.google.com/p/google-web-toolkit/issues/detail?id=4473))
*   Dictionary.keySet() contains "__gwt_ObjectId" in DevMode ([#4486](https://code.google.com/p/google-web-toolkit/issues/detail?id=4486))
*   JsStackEmulator may break up JsInvocation ([#4512](https://code.google.com/p/google-web-toolkit/issues/detail?id=4512))
*   DOMImpl.g/setInnerText() use unnecessarily expensive node
manipulation ([#4586](https://code.google.com/p/google-web-toolkit/issues/detail?id=4586))
*   NumberFormat error formatting more than 6 decimal places ([#4598](https://code.google.com/p/google-web-toolkit/issues/detail?id=4598))
*   DateBox can generate an untrapped exception if a non-default
format is used ([#4633](https://code.google.com/p/google-web-toolkit/issues/detail?id=4633))
*   PopupPanel.removeFromParent() dosen't remove glass panel ([#4720](https://code.google.com/p/google-web-toolkit/issues/detail?id=4720))
*   ClassNotFoundException from web.xml for configured listeners
during devmode servlet validation ([#4760](https://code.google.com/p/google-web-toolkit/issues/detail?id=4760))
*   Converting ImageBundle to ResourceBundle causes a regression
if bundle is used on the server side as well ([#4797](https://code.google.com/p/google-web-toolkit/issues/detail?id=4797))
*   Add java.util.logging emulation ([#4954](# https://code.google.com/p/google-web-toolkit/issues/detail?id=4954))

* * *

## <a id="Release_Notes_2_0_4"></a> Release Notes for 2.0.4

This 2.0.4 release contains fixes that were not included in the
2.0.3 release.

### Noteworthy Fixed Issues

*   Whole UI disappear in IE 7 when we Hover over the menubar menu
item ([#4532](https://code.google.com/p/google-web-toolkit/issues/detail?id=4532))
*   List.subList not fully compatible with java.util.List
interface ([#4993](https://code.google.com/p/google-web-toolkit/issues/detail?id=4993))
*   Safari 5 fails to execute non-integral right-shifts correctly
([#5056](https://code.google.com/p/google-web-toolkit/issues/detail?id=5056))

* * *

## <a id="Release_Notes_2_0_3"></a> Release Notes for 2.0.3

This 2.0.3 release contains fixes that were not included in the
2.0.2 release.

### Noteworthy Fixed Issues

*   Using a PopupPanel in Internet Explorer without a history
IFrame throws a NullPointerException ([#4584](https://code.google.com/p/google-web-toolkit/issues/detail?id=4584))
*   Opera support for History is not working ([#3956](https://code.google.com/p/google-web-toolkit/issues/detail?id=3956))

* * *

## <a id="Release_Notes_2_0_2"></a> Release Notes for 2.0.2

This 2.0.2 release contains a fix that was not included in the
2.0.1 release.

### Noteworthy Fixed Issues

*   Standard.css missing new layout styles ([#4429](https://code.google.com/p/google-web-toolkit/issues/detail?id=4429))

* * *

## <a id="Release_Notes_2_0_1"></a> Release Notes for 2.0.1

This 2.0.1 release contains fixes for bugs found in the 2.0.0
release.

### Potentially breaking changes and fixes

*   Fixed a bug in how code generators collect method arguments
from generated source, which impacted the Messages interfaces
generated for UiBinder template files. In GWT 2.0, such argument names
were incorrectly changed to ARGn. Most GWT applications will be
unaffected, but external systems relying on these names will need to
be updated.
*   The development mode server will, by default, only bind to
localhost which will break cross-machine debugging. You can get the
old behavior by specifying `-bindAddress 0.0.0.0`. Please
see issue (#[4322](https://code.google.com/p/google-web-toolkit/issues/detail?id=4322))
for more details. For webAppCreator-generated ant files, you can pass
this with `ant -Dgwt.args="-bindAddress 0.0.0.0" devmode`.
*   The CurrencyList/CurrencyData APIs are now public - if you
were relying upon these classes in their non-public location, you
should only need to update your imports.

### Noteworthy Fixed Issues

*   UiBinder Image class with resource attribute, removes styles
on that image ([#4415](https://code.google.com/p/google-web-toolkit/issues/detail?id=4415))
*   Widgets lose focus if its placed on FocusPanel (Opera, Safari)
([#1471](https://code.google.com/p/google-web-toolkit/issues/detail?id=1471))
*   Remove method in SplitLayoutPanel is broken ([#4217](https://code.google.com/p/google-web-toolkit/issues/detail?id=4217))
*   Splitter constructor hard codes the background color of the
splitter to white ([#4335](https://code.google.com/p/google-web-toolkit/issues/detail?id=4335))
*   Image should provide method to set alternative text ([#4335](https://code.google.com/p/google-web-toolkit/issues/detail?id=4335))
*   CssResource cannot parse unescaped '-', '_' in class selectors
and unknown at-rules ([#3946](https://code.google.com/p/google-web-toolkit/issues/detail?id=3946))
*   Focusable implementation breaks ScrollPanels in Safari ([#1313](https://code.google.com/p/google-web-toolkit/issues/detail?id=1313))
*   RequestBuilder restricted to GET and POST ([#3388](https://code.google.com/p/google-web-toolkit/issues/detail?id=3388))
*   HTMLTable.Cell.getElement() calls
getCellFormatter().getElement() with row and column swapped
RequestBuilder restricted to GET and POST ([#3757](https://code.google.com/p/google-web-toolkit/issues/detail?id=3757))
*   MenuBar steals focus when hovered ([#3884](https://code.google.com/p/google-web-toolkit/issues/detail?id=3884))
*   TabLayoutPanel tabs don't line up properly on IE ([#4447](https://code.google.com/p/google-web-toolkit/issues/detail?id=4447))
*   webAppCreator produces ant build files which support the
gwt.args property for passing additional flags to the gwtc and devmode
rules, such as `ant -Dgwt.args="-style PRETTY" gwtc`.

See the GWT issue tracker for [
the complete list of bug fixes and enhancements](https://code.google.com/p/google-web-toolkit/issues/list?can=1&q=status:Fixed,FixedNotReleased%20milestone:2_0_1&num=1000) in this release.

* * *

## <a id="Release_Notes_2_0_0"></a> Release Notes for 2.0.0

This release includes some minor bug fixes found in the release
candidate. See [What's New
in GWT 2.0](doc/2.0/ReleaseNotes_2_0.html) plus the release notes for [2.0.0 (RC1)](#Release_Notes_2_0_0_rc1) and [2.0.0-rc2](#Release_Notes_2_0_0_rc2) for the full list of
features and bugs fixes included in the GWT 2.0.0 release.

* * *

## <a id="Release_Notes_2_0_0_rc2"></a> Release Notes for 2.0.0 (RC2)

### New Features

*   GWT 2.0 introduces a number of new panels, which together form
a stable basis for fast and predictable application-level layout. The
official doc is still in progress, but for an overview please see [Layout
Design](http://code.google.com/p/google-web-toolkit/wiki/LayoutDesign) on the wiki. The new set of panels includes [RootLayoutPanel](/javadoc/latest/com/google/gwt/user/client/ui/RootLayoutPanel.html),
[LayoutPanel](/javadoc/latest/com/google/gwt/user/client/ui/LayoutPanel.html),
[DockLayoutPanel](/javadoc/latest/com/google/gwt/user/client/ui/DockLayoutPanel.html),
[SplitLayoutPanel](/javadoc/latest/com/google/gwt/user/client/ui/SplitLayoutPanel.html),
[StackLayoutPanel](/javadoc/latest/com/google/gwt/user/client/ui/StackLayoutPanel.html),
and [TabLayoutPanel](/javadoc/latest/com/google/gwt/user/client/ui/TabLayoutPanel.html).
*   UiBinder now directly supports `LayoutPanel`. For example:

```
<g:LayoutPanel>
  <g:layer left='1em' width='20px'><g:Label>left-width</g:Label></g:Layer>
  <g:layer right='1em' width='20px'><g:Label>right-width</g:Label></g:Layer>
  <g:layer><g:Label>nada</g:Label></g:Layer>
</g:LayoutPanel>
```

*   [Window.Navigator](/javadoc/latest/com/google/gwt/user/client/Window.Navigator.html)
now provides access to the native browser's navigator object.

### Breaking changes and known issues/bugs/problems

*   Windows users who have previously installed the _GWT
Developer Plugin for IE_ will have to uninstall the old version. Use
the following steps:

    1.  From the Windows "Start" Menu, open "Control Panel"
    2.  Select "Add/Remove Programs"
    3.  Select "GWT Developer Plugin for IE" then click "Uninstall"
    4.  Run Internet Explorer and browse to [http://gwtproject.org/missing-plugin/](/missing-plugin/)
to install the new version of the plugin
*   Running a `GWTTestCase` as compiled script was
previously done using `-Dgwt.args="-web"`. The `-web`
argument is now deprecated in favor of `-prod`, consistent
with the terminology change from _web mode_ to _production
mode_.
*   The `-portHosted` command line argument for `DevMode`
and `GWTTestCase` has changed to `-codeServerPort`
to be consistent with the new term _code server_.
*   The `junitCreator` command line utility has been
removed. Instead, the `webAppCreator` utility takes new
argument: `-junit _<path-to-junit-jar>_`
, which incorporates the functionality previously in junitCreator and
generates `ant test` targets.
*   When running development mode on on Chrome, any JavaScript
objects that pass into Java code will be assigned a new property `__gwt_ObjectId`.
This could break native code that looks iterates through the
properties of such an object. To work around this issue, see this 
[example](http://code.google.com/p/google-web-toolkit/source/diff?old=4807&r=7063&format=side&path=/trunk/user/src/com/google/gwt/json/client/JSONObject.java)
of our changes to `JSONObject` (scroll to the bottom).
*   Compile reports (formerly SOYC reports) are now generated with
the `-compileReport` command line flag to `Compiler`.
The generated reports are now written to the private _extra_
directory. If no `-extra` argument is specified, this
directory defaults to `extras/`. This eliminates an
unlikely security risk of accidentally deploying compile reports to a
publicly accessible location.

### Fixed Issues

*   In UiBinder `<ui:style>` blocks, css class
names may contain dashes.
*   Non-Java method safe characters in inline <ui:style> class names doesn't work
          ([#4052](https://code.google.com/p/google-web-toolkit/issues/detail?id=4052))
*   @external does not work reliably for inline styles in <ui:style>
          ([#4053](https://code.google.com/p/google-web-toolkit/issues/detail?id=4053))
*   Various false alarm warnings about invalid JSNI references
have been fixed.
*   Various Swing UI improvements.
*   RPC calls leaking memory for IE ([#4133](https://code.google.com/p/google-web-toolkit/issues/detail?id=4133))
*   deRPC raise an Error 500 instead of propagating the correct
RuntimeException in ProdMode ([#4237](https://code.google.com/p/google-web-toolkit/issues/detail?id=4237))

* * *

## <a id="Release_Notes_2_0_0_rc1"></a> Release Notes for 2.0.0 (RC1)

This release contains big changes to improve developer
productivity, make cross-browser development easier, and produce faster
web applications.

### Things that are changing with GWT 2.0 that might otherwise be
confusing without explanation

*   Terminology changes: We're going to start using the term
"development mode" rather than the old term "hosted mode." The term
"hosted mode" was sometimes confusing to people, so we'll be using the
more descriptive term "development mode" from now on. For similar
reasons, we'll be using the term "production mode" rather than "web
mode" when referring to compiled script.
*   Changes to the distribution: Note that there's only one
download, and it's no longer platform-specific. You download the same
zip file for every development platform. This is made possible by the
new plugin approach used to implement development mode (see below).
The distribution file does not include the browser plugins themselves;
those are downloaded separately the first time you use development
mode in a browser that doesn't have the plugin installed.

### Major New Features

*   In-Browser Development Mode: Prior to 2.0, GWT hosted mode
provided a special-purpose "hosted browser" to debug your GWT code. In
2.0, the web page being debugged is viewed within a regular-old
browser. Development mode is supported through the use of a
native-code plugin called the _GWT Developer Plugin_ for many
popular browsers. In other words, you can use development mode
directly from Safari, Firefox, Internet Explorer, and Chrome.
*   Code Splitting: Developer-guided code splitting with [GWT.runAsync()](/javadoc/latest/com/google/gwt/core/client/GWT.html#runAsync\(com.google.gwt.core.client.RunAsyncCallback\))
allows you to chunk your GWT code into multiple fragments for faster
startup. Imagine having to download a whole movie before being able to
watch it. Well, that's what you have to do with most Ajax apps these
days -- download the whole thing before using it. With code splitting,
you can arrange to load just the minimum script needed to get the
application running and the user interacting, while the rest of the
app is downloaded as needed.
*   Declarative User Interface: GWT's [UiBinder](/javadoc/latest/com/google/gwt/uibinder/client/UiBinder.html)
now allows you to create user interfaces mostly declaratively.
Previously, widgets had to be created and assembled programmatically,
requiring lots of code. Now, you can use XML to declare your UI,
making the code more readable, easier to maintain, and faster to
develop. The Mail sample has been updated to show a practical example
of using UiBinder.
*   Bundling of resources via [ClientBundle](/javadoc/latest/com/google/gwt/resources/client/ClientBundle.html).
GWT introduced [ImageBundle](/javadoc/latest/com/google/gwt/user/client/ui/ImageBundle.html)
in 1.4 to provide automatic spriting of images. ClientBundle
generalizes this technique, bringing the power of combining and
optimizing resources into one download to things like text files, CSS,
and XML. This means fewer network round trips, which in turn can
decrease application latency -- especially on mobile applications.
*   Using HtmlUnit for running test cases based on [GWTTestCase](/javadoc/latest/com/google/gwt/junit/client/GWTTestCase.html):
Prior to 2.0, `GWTTestCase` relied on SWT and native code
versions of actual browsers to run unit tests. As a result, running
unit tests required starting an actual browser. As of 2.0, `GWTTestCase`
no longer uses SWT or native code. Instead, it uses _HtmlUnit_ as
the built-in browser. Because HtmlUnit is written entirely in the Java
language, there is no longer any native code involved in typical
test-driven development. Debugging GWT Tests in development mode can
be done entirely in a Java debugger.

### Breaking changes and known issues/bugs/problems

*   Prior to 2.0, GWT tools such as the compiler were provide in a
platform-specific jar (that is, with names like `gwt-dev-windows.jar`).
As of 2.0, GWT tools are no longer platform specific and they reside
in generically-named `gwt-dev.jar`. You are quite likely to
have to update build scripts to remove the platform-specific suffix,
but that's the extent of it.
*   The development mode entry point has changed a few times since
GWT 1.0. It was originally called `GWTShell`, and in GWT
1.6 a replacement entry point called `HostedMode` was
introduced. As of GWT 2.0, to reflect the new "development mode"
terminology, the new entry point for development mode is `com.google.gwt.dev.DevMode`.
Sorry to keep changing that on ya, but the good news is that the prior
entry point still works. But, to really stay current, we recommend you
switch to the new `DevMode` entry point.
*   Also due to the "development mode" terminology change, the
name of the ant build target produced by `webAppCreator`
has changed from `hosted` to `devmode`. In other
words, to start development mode from the command-line, type `ant devmode`.
*   HtmlUnit does not attempt to emulate authentic browser layout.
Consequently, tests that are sensitive to browser layout are very
likely to fail. However, since `GWTTestCase` supports other
methods of running tests, such as Selenium, that do support accurate
layout testing, it can still make sense to keep layout-sensitive tests
in the same test case as non-layout-sensitive tests. If you want such
tests to be ignored by HtmlUnit, simply annotate the test methods with
`@DoNotRunWith({Platform.Htmlunit})`.
*   Versions of Google Plugin for Eclipse prior to 1.2 will only
allow you to add GWT release directories that include a file with a
name like `gwt-dev-windows.jar`. You can fool it by sym
linking or copying gwt-dev.jar to the appropriate name.
*   The way arguments are passed to the GWT testing infrastructure
has been revamped. There is now a consistent syntax to support
arbitrary "run styles", including user-written, with no changes to GWT
itself. For example, `-selenium FF3` has become `-runStyle
selenium:FF3`. This change likely does not affect typical test
invocation scripts, but if you do use `-Dgwt.args` to pass
arguments to `GWTTestCase`, be aware that you may need to
make some changes.

* * *

## <a id="Release_Notes_1_7_1"></a> Release Notes for 1.7.1

This release adds support for Mac OS X version 10.6 (Snow
Leopard) by allowing hosted mode to run with a 1.6 JRE in 32-bit mode
(using the -d32 flag).

### Fixed Issues

*   Allow hosted mode using a 1.6 JRE with the -d32 flag ([#3843](https://code.google.com/p/google-web-toolkit/issues/detail?id=3843),
[#3998](https://code.google.com/p/google-web-toolkit/issues/detail?id=3998))

* * *

## <a id="Release_Notes_1_7_0"></a> Release Notes for 1.7.0

This release adds explicit support for Internet Explorer 8,
Firefox 3.5, and Safari 4 as well as a few high-priority bug fixes. In
all other respects, it is very similar to GWT 1.6. Note, however, that
this release is version 1.7.0 rather than version 1.6.5 to signify a
potentially breaking change for libraries that use deferred binding to
specialize code based on user agent (see the next section for technical
details).

Also see [What's New in 1.7?](doc/1.7/ReleaseNotes_1_7.html)

### Potentially breaking changes and fixes

*   This release includes explicit support for IE8, which has some
significant behavioral changes from prior versions of IE. These
changes are great enough that the new value `ie8` has been
added for the `user.agent` deferred binding client
property. If you have deferred binding rules (i.e. `<replace-with>`
or `<generate-with>`) or property providers that are
sensitive to `user.agent`, you may need to update them to
account for the `ie8` value. For more information, see the
[technical notes](http://code.google.com/p/google-web-toolkit/wiki/IE8Support).

### Fixed Issues

*   Updated GWT libraries to support IE8 ([#3558](https://code.google.com/p/google-web-toolkit/issues/detail?id=3558),
[#3329](https://code.google.com/p/google-web-toolkit/issues/detail?id=3329))
*   Native exception in Node.is() ([#3644](https://code.google.com/p/google-web-toolkit/issues/detail?id=3644))
*   Incorrect firing of two click events from CheckBox and a
related issue ([#3508](https://code.google.com/p/google-web-toolkit/issues/detail?id=3508),
[#3679](https://code.google.com/p/google-web-toolkit/issues/detail?id=3679))
*   Compiler java.lang.StackOverflowError if you don't use -Xss to
set a stack size ([#3510](https://code.google.com/p/google-web-toolkit/issues/detail?id=3510))
*   Mouse wheel in FF3 ([#2902](https://code.google.com/p/google-web-toolkit/issues/detail?id=2902))
*   GWT outputs expressions too long for WebKit ([#3455](https://code.google.com/p/google-web-toolkit/issues/detail?id=3455))
*   java.sql.Date.valueOf error ([#3731](https://code.google.com/p/google-web-toolkit/issues/detail?id=3731))
*   Added a workaround for Firefox 3.5 regression ([bugzilla #497780](https://bugzilla.mozilla.org/show_bug.cgi?id=497780))

* * *

## <a id="Release_Notes_1_6_4"></a> Release Notes for 1.6.4

### Fixed Issues

*   The classpath in the scripts created by junitCreator was
updated to refer to `/war/WEB-INF/classes` rather than `/bin`.

* * *

## <a id="Release_Notes_1_6_3"></a> Release Notes for 1.6.3 (RC2)

### Fixed Issues

*   Various [servlet classpath issues](https://code.google.com/p/google-web-toolkit/issues/detail?id=3496)
introduced in 1.6.2 are resolved.
*   JSP compilation should work out of the box in hosted mode.

* * *

## <a id="Release_Notes_1_6_2"></a> Release Notes for 1.6.2 (RC)

Please see [What's new in GWT 1.6?](doc/1.6/ReleaseNotes_1_6.html) (online)

* * *

## <a id="Release_Notes_1_5_3"></a> Release Notes for 1.5.3

### Fixed Issues

*   RPC requests no longer fail on the embedded Android web
browser
*   Leaf `TreeItems` now line up with their non-leaf
siblings
*   Removing the last child node from a `TreeItem` no
longer creates extra margins on the left
*   `HTTPRequest` no longer uses POST instead of GET on
some IE installs because of incorrect XHR selection
*   Compiler now uses a more reliable check to prevent methods
with local variables from being inlined
*   `getAbsoluteTop()/Left()` can no longer return
non-integral values
*   `Time.valueOf()` no longer fails to parse `"08:00:00"`
or incorrectly accepts `"0xC:0xB:0xA"`.

See the GWT issue tracker for [
the complete list of bug fixes and enhancements](https://code.google.com/p/google-web-toolkit/issues/list?can=1&q=status%3AFixed%2CFixedNotReleased%20milestone%3A1_5_3&num=1000) in this release.

* * *

## <a id="Release_Notes_1_5_2"></a> Release Notes for 1.5.2

### Potentially breaking changes and fixes

*   `History.onHistoryChanged()` has been added back
(it was missing from 1.5 RC2) but is now deprecated. Application
startup should be handled by calling the new `History.fireCurrentHistoryState()`.
*   Fields marked `final` in serializable types will
now generate a warning; the fact that they were not being serialized
was a source of confusion. Mark such fields both `final`
and `transient` to avoid the warning.
*   Instance methods on overlay types cannot be accessed from
JSNI. (This used to work in hosted mode, but failed at runtime in web
mode.)
*   The hosted mode server no longer serves `hosted.html`
from a module's public path; instead the file is read directly from
the classpath. This file is tightly coupled with the hosted mode
implementation and was not meant to be user overridable.

### General Enhancements

*   `Collections.unmodifiableSortedSet()` and `Collections.unmodifiableSortedMap()`
are now implemented.
*   The new `Accessibility` class enables widget
authors to add ARIA support to their widgets. Many GWT widgets come
with ARIA support by default.
*   Improved exception stack traces in hosted mode when JSNI stack
frames are present.

### Fixed Issues

*   Fixed the relationship between the coordinates returned by `Element.getAbsoluteLeft/Top()`
and `Event.getClientX/Y()`. `Document.getBodyOffsetLeft/Top()`
can be used to account for the difference between these two coordinate
systems.
*   **Ctrl-Z** should correctly perform an undo operation in
RichTextArea on IE.

See the GWT issue tracker for [
the complete list of bug fixes and enhancements](https://code.google.com/p/google-web-toolkit/issues/list?can=1&q=status%3AFixed%2CFixedNotReleased%20milestone%3A1_5_Final&num=1000) in this release.

* * *

## <a id="Release_Notes_1_5_1"></a> Release Notes for 1.5.1 (RC2)

### Support for Standards Mode

GWT 1.5 adds significantly more support for standards mode
applications, but some widgets (especially those with table based
layouts) may not behave as expected. The low level standards mode bugs
(such as with
`getAbsoluteLeft/Top()`
) have been addressed, but some of the constructs that our widgets rely
on do not work in standards mode. For example, you cannot set the
height and width of a widget relative to its parent if its parent is a
table cell, and
`StackPanel`
takes up much more vertical space than it should in Internet Explorer.
All of our samples have been reverted back to quirks mode, and the
`applicationCreator`
defaults to quirks mode when creating a new GWT app.

You can still use standards mode for your GWT app, but please be
aware that you may notice some layout issues. If you are switching an
app from quirks mode to standards mode, your CSS styles might be
applied differently, which could also affect your application. We will
continue to address standards mode support in future GWT releases.

### Potentially breaking changes and fixes

*   `DOM.eventGetClientX/Y()` now takes into account
the margin and border of the body element
*   In hosted mode, all `DOM.eventGetXXX()` methods now
assert that the requested attribute is reliable across all supported
browsers. This means that attempting to retrieve an attribute for an
event that does not support that attribute will now throw an assertion
error instead of returning a coerced value. Most notably, the click
event throws an assertion error if you attempt to get the mouse button
that was clicked.
*   The return value of `DOM.eventGetXXX()` methods are
now coerced to 0 instead of -1 in web mode. In hosted mode, an
assertion error will be thrown if the attribute is not defined for the
given event, as described in the previous bullet.
*   Opera specific code has been upgraded to work with Opera 9.5,
but may not work with older versions of Opera as we only support the
most recent release. Specifically, some widgets may not be able to
receive focus.
*   Calls to `History.newItem()` now trigger an `onHistoryChanged()`
event synchronously instead of asynchronously

### General Enhancements

*   Added support for the `contextmenu` event, which
allows users to detect and override the browser's default context menu
*   Improved performance of `NumberFormat`
*   Added support for altering the number of decimals in a
currency in `NumberFormat`
*   Improved performance of Animations
*   Improved the appearance of the default GWT style themes
*   Improved the Showcase sample with more robust examples and
more language translations
*   `FormPanel` can now wrap an existing form and still
submit it to a hidden iframe

### Fixed Issues

*   `DOM.getAbsoluteLeft/Top()` and `DOM.eventGetClientX/Y()`
no longer log an exception to the console in Firefox 3
*   Fixed a memory leak in Internet Explorer
*   `DOM.getAbsoluteLeft/Top()` now takes into account
the margin and border of the target element in Safari 3
*   Fixed some bugs associated with history support

See the GWT issue tracker for [
the complete list of bug fixes and enhancements](https://code.google.com/p/google-web-toolkit/issues/list?can=1&q=status%3AFixed%20milestone%3A1_5_RC2%20type%3Adefect&num=1000) in this release.

* * *

## <a id="Release_Notes_1_5_0"></a> Release Notes for 1.5.0 (RC)

This release candidate is, in a word, huge. Rather than including all
the details here, please see [What's
New in GWT 1.5?](http://code.google.com/p/google-web-toolkit-doc-1-5/wiki/ReleaseNotes_1_5_ImportantNotes) for full details. The main thing you'll want to know
is that GWT 1.5 supports the Java 5 language features (generics,
enumerated types, annotations, etc.). But check out the full notes,
because there's a lot of great stuff!

* * *

## <a id="Release_Notes_1_4_60"></a> Release Notes for 1.4.60

This release has only a couple of minor changes from [1.4.59](#Release_Notes_1_4_59).

*   Fixed a bug in the benchmarking that prevented source code
from showing up in reports.
*   Fixed a bug in the hosted mode servlet context emulation where
getResource() would fail to find a file in a module's public path.
*   Compiler output files of the form `_module_.cache.html` used to contain html intended as a 
helpful note to a developer. This message has now been removed because screen readers and some 
browsers would display this content to end users.

* * *

## <a id="Release_Notes_1_4_59"></a> Release Notes for 1.4.59 (RC2)

This release includes numerous bugfixes and a few important changes. If
you are upgrading from GWT 1.3.3, you are strongly encouraged to read
the [release notes for 1.4.10](#Release_Notes_1_4_10) first.

### New Features

*   [DOM.eventGetCurrentEvent()](doc/html/com.google.gwt.user.client.DOM#eventGetCurrentEvent\(\))
now provides global access to the current Event object. ([#1309](https://code.google.com/p/google-web-toolkit/issues/detail?id=1309))
*   [PopupPanel.setPopupPositionAndShow(PopupCallback
callback)](doc/html/com.google.gwt.user.client.ui.PopupPanel#setPopupPositionAndShow\(com.google.gwt.user.client.ui.PopupPanel.PositionCallback\)) now provides now provides a simpler and bulletproof way to
control the layout of popups. ([#1120](https://code.google.com/p/google-web-toolkit/issues/detail?id=1120),
[#1243](https://code.google.com/p/google-web-toolkit/issues/detail?id=1243))
*   The [SuggestionHandler](doc/html/com.google.gwt.user.client.ui.SuggestionHandler)
interface can be used to respond to the user selecting a suggstion in
the [SuggestBox](doc/html/com.google.gwt.user.client.ui.SuggestBox).
([#1086](https://code.google.com/p/google-web-toolkit/issues/detail?id=1086))
*   [Collection.toArray(Object[])](doc/html/java.util.Collection)
is now implemented. ([#695](https://code.google.com/p/google-web-toolkit/issues/detail?id=695))
*   If you have it installed, [Google
Gears](http://gears.google.com/) is now accessible in hosted mode (Windows only). ([#1252](https://code.google.com/p/google-web-toolkit/issues/detail?id=1252))

### General Changes

*   Startup is now faster and more reliable. In particular, [onModuleLoad()](doc/html/com.google.gwt.core.client.EntryPoint#onModuleLoad\(\))
is now called as soon as the DOM is ready, which will generally be
before the page's body.onload() event is fired. This allows your
application to startup before certain resources (such as images) are
fully loaded.
*   Linux hosted mode should be less crashy. ([#1105](https://code.google.com/p/google-web-toolkit/issues/detail?id=1105),
[#1281](https://code.google.com/p/google-web-toolkit/issues/detail?id=1281),
[#1358](https://code.google.com/p/google-web-toolkit/issues/detail?id=1358))
*   An important discussion of HTTP headers, caching, and HTTPS
has been added to the [ImageBundle](doc/html/com.google.gwt.user.client.ui.ImageBundle)
documentation. ([#1172](https://code.google.com/p/google-web-toolkit/issues/detail?id=1172))
*   [PopupPanel.center()](doc/html/com.google.gwt.user.client.ui.PopupPanel#center\(\))
now causes the popup to be shown as well as centered. ([#1120](https://code.google.com/p/google-web-toolkit/issues/detail?id=1120))
*   [RichTextArea](doc/html/com.google.gwt.user.client.ui.RichTextArea)
underwent number of bugfixes and should be stable now. ([#1130](https://code.google.com/p/google-web-toolkit/issues/detail?id=1130),
[#1214](https://code.google.com/p/google-web-toolkit/issues/detail?id=1214),
[#1276](https://code.google.com/p/google-web-toolkit/issues/detail?id=1276))
*   New [RPC](doc/html/com.google.gwt.doc.DeveloperGuide.RemoteProcedureCalls)
warnings

    *   Warn if a non-checked exception is used in the throws clause
of a RemoteService method.
    *   Warn if no concrete, serializable subclasses can be found
for a given type declared in a RemoteService interface.
*   [RPC](doc/html/com.google.gwt.doc.DeveloperGuide.RemoteProcedureCalls)
now generates a serialization policy file during compilation. The
serialization policy file contains a whitelist of allowed types which
may be serialized. Its name is a strong hash name followed by `.gwt.rpc`.
This file must be deployed to your web server as a public resource,
accessible from a [RemoteServiceServlet](doc/html/com.google.gwt.user.server.rpc.RemoteServiceServlet)
via `ServletContext.getResource()`. If it is not deployed
properly, RPC will run in 1.3.3 compatibility mode and refuse to
serialize types implementing [Serializable](doc/html/java.io.Serializable).
([#1297](https://code.google.com/p/google-web-toolkit/issues/detail?id=1297))
*   [Panel.adopt(Widget,
Element)](doc/html/com.google.gwt.user.client.ui.Panel#adopt\(com.google.gwt.user.client.ui.Widget,%20com.google.gwt.user.client.Element\)) and [Panel.disown(Widget)](doc/html/com.google.gwt.user.client.ui.Panel#disown\(com.google.gwt.user.client.ui.Widget\))
have been deprecated. If you have subclassed Panel, please carefully
review the new documentation for [Panel.add(Widget)](doc/html/com.google.gwt.user.client.ui.Panel#add\(com.google.gwt.user.client.ui.Widget\))
and [Panel.remove(Widget)](doc/html/com.google.gwt.user.client.ui.Panel#remove\(com.google.gwt.user.client.ui.Widget\))
for details on the correct way to add and remove Widgets from Panels.
([#1121](https://code.google.com/p/google-web-toolkit/issues/detail?id=1121))
*   The benchmark viewer application is now faster, prettier, and
a bit more user friendly.

### Retractions from 1.4.10

*   Breaking changes to the semantics of [UIObject.setStyleName()](doc/html/com.google.gwt.user.client.ui.UIObject#setStyleName\(java.lang.String\))
have been backed out. All changes relative to 1.3.3 should now be
backwards-compatible. ([#1079](https://code.google.com/p/google-web-toolkit/issues/detail?id=1079))
*   The linux distribution of 1.4.10 bundled Mozilla 1.7.13
instead of version 1.7.12, which is bundled in previous releases. This
change caused problems on some systems, so it's been reverted back to
Mozilla 1.7.12 again. ([#1105](https://code.google.com/p/google-web-toolkit/issues/detail?id=1105))
*   Numerous RPC warnings were added in 1.4.10. One of these
warnings would be issued when a class containing native methods was
found to be serializable. This warning now only applies to
automatically serialized types; types with custom serializers will no
longer trigger this warning. ([#1161](https://code.google.com/p/google-web-toolkit/issues/detail?id=1161))
*   A change to RPC in 1.4.10 would cause an error to be issued if
a serializable type had any subtypes that were not serializable. This
change caused code that worked in 1.3.3 to fail in 1.4.10. In this
release, the error has been downgraded to a warning. ([#1163](https://code.google.com/p/google-web-toolkit/issues/detail?id=1163))
*   A potentially breaking change to event bubbling in 1.4.10 has
been backed out in favor of the 1.3.3 behavior. ([#1159](https://code.google.com/p/google-web-toolkit/issues/detail?id=1159))

### Fixed Issues

See the GWT issue tracker for [the
complete list of bug fixes](https://code.google.com/p/google-web-toolkit/issues/list?can=1&q=status%3AFixed%20milestone%3A1_4_RC2%20type%3Adefect&num=1000) in this release.

* * *

## <a id="Release_Notes_1_4_10"></a> Release Notes for 1.4.10 (RC)

This is the Release Candidate for GWT 1.4, the first GWT release
developed with major participation from GWT open source contributors.
It's been a long time coming, but we hope it's been worth the wait. In
addition to tons of new features, optimizations, and performance
enhancements, we've fixed more than 150 bugs. There are some important
behavioral and potentially breaking API changes; if you read nothing
else, please read these following sections!

### Behavioral Changes

Important changes in the behavior of existing GWT features.

#### Critical Changes to RPC

In previous versions, the RPC subsystem was too lenient and failed to
warn at compile time about potential (though unusual) edge cases that
could in theory cause problems at runtime. Beginning with version 1.4,
the RPC subsystem emits additional warnings and errors to help you
identify error-prone constructs. While this new behavior may seem
annoying at first, rest assured that fixing your code to avoid RPC
warnings will result in a smaller, faster, and more reliable app.

*   **Bad code that happened to work before might not now**

    Previously, if you declared one particular component type via
@gwt.typeArgs at compile time, you could often get away with passing a
different type at runtime. For example, placing a Date in an ArrayList
of String might work. This type of code is less likely to work now and
will likely become more strict in the future. Bottom line: don't do
this. Make sure collections only contain the declared item type (or
subtypes thereof).
*   **[Serializable](doc/html/java.io.Serializable)
equivalent to [IsSerializable](doc/html/com.google.gwt.user.client.rpc.IsSerializable)**

    Although GWT's RPC mechanism doesn't purport to honor the semantics of
Java serialization, by popular demand, Serializable and IsSerializable
are now equivalent for the purposes of RPC. This should improve
server-side interoperability and remove much of the need for DTOs.
*   **Warn about missing gwt.typeArgs**
 Every Collection
or Map type should have an associated [gwt.typeArgs
javadoc annotation](doc/html/com.google.gwt.doc.DeveloperGuide.RemoteProcedureCalls.SerializableTypes). In the past, a missing @gwt.typeArgs would
generally have no noticeable effect, because a bug in the RPC system
would generate code for all available serializable types, even if they
weren't used in your service interface. Now that this bug has been
fixed, you can achieve **significant** reduction in the size of
your compiled output by fixing these warnings.
*   **Warn about serializable subclasses that violate the
serialization restrictions**
 An RPC warning is emitted for
classes that are assignable to IsSerializable or Serializable but that
lack a default constructor or contain fields which cannot be
serialized. It is important to resolve these warnings to avoid rare
but confusing situations in which exceptions would be thrown at
runtime.
*   **Warn about non-transient, final instance fields**

    RPC has never actually serialized `final` instance fields,
but now it explicitly warns about the existence of such fields unless
they are also `transient`. Thus, the warning can be
addressed by making `final` instance fields `transient`
as well, or it can be suppressed via a module property.
*   **Warn about local and non-static nested types that
implement IsSerializable or Serializable**
 RPC has never
serialized these kinds of classes and will now generate a warning.
*   **Warn about native methods in serializable classes**

    Attempting to serialize classes that contain `native`
methods will cause UnsatisfiedLinkErrors if such methods are called in
server-side code.

#### Module Script Tags

*   In previous versions of GWT, including external JavaScript
files via a module `<script>` tag required a nested JavaScript
expression &mdash; called a _script-ready function_ &mdash; that
would determine when the script had been successfully loaded. Script
load order is now handled automatically and these expressions are
ignored. A warning will be issued in hosted mode. For reference, see [here](doc/html/com.google.gwt.doc.DeveloperGuide.Fundamentals.Modules.AutomaticResourceInjection).

#### Additional Hosted Mode Checks Related to JSNI

*   Previously, when passing values from JavaScript into Java,
hosted mode would silently coerce a JavaScript value of an incorrect
type into the declared Java type. Unfortunately, this would allow code
to work in hosted mode that could fail in unexpected ways in web mode.
Hosted mode will now throw a HostedModeException if you try to pass an
incompatible type. See [here](doc/html/com.google.gwt.doc.DeveloperGuide.JavaScriptNativeInterface.Marshaling)
for more details.

### Breaking API Changes

This release also includes API changes that may require minor tweaks to
existing code. Any such changes that affect you should only take a few
minutes to rectify.

#### 
[JavaScriptObject](doc/html/com.google.gwt.core.client.JavaScriptObject)

*   Although subclassing JavaScriptObject is not supported, some
people do so anyway at their own risk :) Please note that the existing
(int) constructor has been removed in favor of a protected no-arg
constructor. Read the source code for [Element](doc/html/com.google.gwt.user.client.Element) for an
example of how JavaScriptObject must be subclassed now (that is, if
subclassing were supported...which, of course, it isn't).

#### 
[DeferredCommand](doc/html/com.google.gwt.user.client.DeferredCommand)

*   The add() method is deprecated in favor of addCommand() in
order to support the new [IncrementalCommand](doc/html/com.google.gwt.user.client.IncrementalCommand)
interface. Had we simply added a new method overload, existing code
that passed in a null literal would have failed to compile.
*   The new addPause() method should be used instead of add(null).

#### 
[UIObject](doc/html/com.google.gwt.user.client.ui.UIObject)

*   The intended use and behavior of style names has been
formalized in UIObject (and therefore in all widgets). All style names
are now classified as "primary", "secondary", and "dependent" styles,
the meanings of which are detailed in the UIObject documentation. The
relevant method signatures remain unchanged (get/setStyleName(),
add/removeStyleName()), and most widgets should be unaffected. One
potentially breaking change, however, is that an exception is thrown
if an attempt is made to remove the primary style name of a widget
using removeStyleName(). See the UIObject documentation for a full
explanation.

### New Features

Here are a few of the coolest new features and enhancements in GWT 1.4.

*   **Size and Speed Optimizations**

    *   New [size](https://code.google.com/p/google-web-toolkit/issues/detail?id=610)
[improvements](https://code.google.com/p/google-web-toolkit/issues/detail?id=599)
in the GWT compiler produce JavaScript that is 10-20% smaller; just
recompile your app with 1.4.
    *   An enhanced startup sequence reduces the size of your
module's startup script by 80%. More importantly, the new startup
sequence removes an HTTP round-trip, making startup latency about
33% faster.
    *   The above optimizations combined with [ImageBundle](#ImageBundle),
make it possible for GWT-based applications to load surprisingly
quickly. To see for yourself, check out startup time of the [Mail](samples/Mail/www/com.google.gwt.sample.mail.Mail/Mail)
sample.
*   **Deployment Enhancements**

    *   GWT RPC is no longer tied to exclusively to servlets. New [modularized
RPC](https://code.google.com/p/google-web-toolkit/issues/detail?id=389) server code makes it easy to connect GWT RPC to your choice of
Java back-ends.
    *   Adding GWT modules to an HTML page has been simplified.
Instead of adding a `<meta name='gwt:module'>` and `<script
src='gwt.js'>`, you just add a single script element for your
module.
    *   Cross-site script inclusion is now supported. The compiler
produces a "-xs" (meaning "cross-site") version of your module's
startup script that can be included without being restricted by the
same-origin policy. WARNING: including scripts from other sites that
you don't fully trust is a [big
security risk](http://groups.google.com/group/Google-Web-Toolkit/web/security-for-gwt-applications).
*   **Widget and Library Enhancements**

    *   [RichTextArea](doc/html/com.google.gwt.user.client.ui.RichTextArea)
allows "drop in" functionality for rich text editing.
    *   [SuggestBox](doc/html/com.google.gwt.user.client.ui.SuggestBox)
makes it easy to add auto-complete functionality.
    *   Splitters! [HorizontalSplitPanel](doc/html/com.google.gwt.user.client.ui.HorizontalSplitPanel)
and [VerticalSplitPanel](doc/html/com.google.gwt.user.client.ui.VerticalSplitPanel)
enable you to resize portions of the user interface.
    *   [PushButton](doc/html/com.google.gwt.user.client.ui.PushButton)
and [ToggleButton](doc/html/com.google.gwt.user.client.ui.ToggleButton)
are easy-to-customize button widgets that can enhance the
look-and-feel of your UI.
    *   [DisclosurePanel](doc/html/com.google.gwt.user.client.ui.DisclosurePanel)
is a simple, nice-looking panel that lets users easily hide and show
portions of your application UI.
    *   [DateTimeFormat](doc/html/com.google.gwt.i18n.client.DateTimeFormat)
and [NumberFormat](doc/html/com.google.gwt.i18n.client.NumberFormat)
make it easy to format and parse dates, times, and numbers for users
all over the world.
    *   [IncrementalCommand](doc/html/com.google.gwt.user.client.IncrementalCommand)
helps you implement long-running tasks in your client code without
triggering "slow script" warnings.
    *   A new [benchmarking
subsystem](http://docs.google.com/View?docid=d9s6nb7_1d723ft) integrates with JUnit to let you record and compare the
speed of code snippets across multiple browsers and multiple
parameter ranges. Benchmarking is a powerful way to identify
bottlenecks and compare performance of alternative implementations.
    *   The oft-requested [java.io.Serializable](https://code.google.com/p/google-web-toolkit/issues/detail?id=21)
is now included in the JRE emulation library and is synonymous with
IsSerializable for the purpose of GWT RPC.
    *   [Mouse
wheel events](https://code.google.com/p/google-web-toolkit/issues/detail?id=844) are now available on a variety of widgets.
*   **[ImageBundle](doc/html/com.google.gwt.user.client.ui.ImageBundle)**

    *   ImageBundle is the single biggest
have-to-see-it-to-believe-it feature in this release. Image bundles
make it trivially easy to combine dozens of images into a single
"image strip", collapsing what would have been dozens of HTTP
requests into one: a single, permanently-cacheable image file.
    *   Image bundles manage everything for you automatically, from
computing clipping rectangles to making transparent PNGs work in
IE6. You can even choose to get the clipped image as an Image widget
or as pure HTML for inclusion in a larger HTML template.
    *   In addition to enabling a blazing-fast startup, image
bundles help make the UI look better during startup, too. Typical
AJAX apps exhibit "bouncy relayout" as individual images are loaded
one-at-a-time. Fixing this problem has historically required
laboriously pre-initializing the width and height of each individual
image ahead of time. Image bundles do the same thing automatically.
The dimensions of each clipped image are computed at compile time
while the bundled image file is being created. Voila! The result is
a fast, non-ugly user startup experience that requires no extra work
on the part of the GWT developer to keep up-to-date.
    *   See the [doc
section](doc/html/com.google.gwt.doc.DeveloperGuide.UserInterface.ImageBundles) for more details.

See the GWT issue tracker for [the
complete list of enhancements](https://code.google.com/p/google-web-toolkit/issues/list?can=1&q=status%3AFixed%20milestone%3A1_4_RC%20type%3Aenhancement&num=1000) in this release.

### Fixed Issues

See the GWT issue tracker for [the
complete list of bug fixes](https://code.google.com/p/google-web-toolkit/issues/list?can=1&q=status%3AFixed%20milestone%3A1_4_RC%20type%3Adefect&num=1000) in this release.

* * *

## <a id="Release_Notes_1_3_3"></a> Release Notes for 1.3.3

This version has only minor functional changes from 1.3.1, listed
below.

### Fixed Issues

*   [Issue #319 - Calling native super method in implementation class results in infinite loop in web mode](https://code.google.com/p/google-web-toolkit/issues/detail?id=319)
*   [Issue #496 - gwt.js in gwt-user.jar lacks Apache 2.0 license header](https://code.google.com/p/google-web-toolkit/issues/detail?id=496)
*   [Issue #497 - Unexpected internal compiler error - Analyzing permutation #1](https://code.google.com/p/google-web-toolkit/issues/detail?id=497)

* * *

## <a id="Release_Notes_1_3_1"></a> Release Notes for 1.3.1 (RC)

This is the Release Candidate for GWT 1.3, the first completely
open source version of GWT. This version has no new functionality, but
we did make a lot of changes to get the source code and build scripts
into presentable shape to prepare for ongoing open source development.
Although the changes were relatively harmless -- formatting, sorting,
more documentation, and a new build system -- there's always a small
chance of problems, so we plan to call this a Release Candidate until
we've convinced ourselves it's reliable.

### Useful Links

*   **[Making GWT Better](makinggwtbetter)**This
is our new GWT open source charter that describes how we plan to
operate the project and how you can access the GWT source, compile it
yourself, and contribute.
*   **[The GWT Issue Tracker](https://code.google.com/p/google-web-toolkit/issues/list)**Please
report any bugs in 1.3 RC that
weren't in 1.2.22 in the GWT issue tracker. These would be likely
related to the new build, and we want to know ASAP so we can fix them.
*   **[The GWT Subversion Repository](http://google-web-toolkit.googlecode.com/svn/)**Visit the
online repository to
browse the GWT source without a Subversion client.

* * *

## <a id="Release_Notes_1_2_22"></a> Release Notes for 1.2.22

This is the official GWT 1.2 release, the follow up to the [GWT 1.2 Release Candidate](#Release_Notes_1_2_11). It
includes all of the enhancements and bug fixes from GWT 1.2 RC as well
as a few additional bug fixes that were reported against GWT 1.2 RC.

### About OS X Hosted Mode Support

GWT's hosted mode support is available only on OS X 1.4 (Tiger) or
later.

### Useful Links

*   **[Changes included in GWT 1.2 since the RC](https://code.google.com/p/google-web-toolkit/issues/list?can=1&q=status%3AFixed%20milestone%3A1_2_Final)**Also see the GWT Blog for
a discussion of the [noteworthy issues related to 1.2 RC](http://googlewebtoolkit.blogspot.com/2006/11/wrapping-up-gwt-12-soon.html)
*   **[New features and bug fixes in GWT 1.2 RC](https://code.google.com/p/google-web-toolkit/issues/list?can=1&q=status%3AFixed%20milestone%3A1_2_RC)**

### Breaking API Changes

There are no breaking changes to pre-1.2 APIs, but one method has been
renamed in a class that was new in 1.2 RC.

#### com.google.gwt.http.client.RequestBuilder

The method
`addHeader()`
was renamed to
`setHeader()`
to more clearly reflect its intent. You will only be affected by this
change if you are using the new HTTP functionality available as of build
1.2.11.

* * *

## <a id="Release_Notes_1_2_11"></a> Release Notes for 1.2.11 (RC)

This is the Release Candidate for GWT 1.2. Between this build and
the subsequent GWT 1.2 official release, changes are limited to issues
unique to GWT 1.2 RC.

See the GWT issue tracker for [the complete list of enhancements and bug fixes](https://code.google.com/p/google-web-toolkit/issues/list?can=1&q=status%3AFixed%20milestone%3A1_2_RC) in this release.

### New Features

*   **[Full support for OS X development](https://code.google.com/p/google-web-toolkit/issues/detail?id=91)**Develop with GWT on OS X as
easily as on Linux and Windows
*   **[Much faster hosted mode](https://code.google.com/p/google-web-toolkit/issues/detail?id=93)**Hosted mode startup time has improved
significantly, but, even better, refreshes are now lightning fast &mdash; even when your source
changes
*   **[New HTTP request module](https://code.google.com/p/google-web-toolkit/issues/detail?id=52)
**The HTTP functionality that GWT users
have been asking for (custom headers, status code, timeouts, and
more), all wrapped up in an API that's easier to use than the
JavaScript XMLHttpRequest object
*   **[Widgets in TreeItems](https://code.google.com/p/google-web-toolkit/issues/detail?id=10)
**Tree items can now contain arbitrary
widgets...finally, you can easily create trees with checkboxes :-)

* * *

## <a id="Release_Notes_1_1_10"></a> Release Notes for 1.1.10

### Fixed Issues

*   Normalized behavior of GWT.getModuleBaseURL() with respect to
hosted mode, web mode, RPC, and automatic resource injection (
[post #1](http://groups.google.com/group/Google-Web-Toolkit/browse_thread/thread/6c2b23e90008b6b9),
[post #2](http://groups.google.com/group/Google-Web-Toolkit/browse_thread/thread/1bb47f2cff671ef0),
[post #3](http://groups.google.com/group/Google-Web-Toolkit/browse_thread/thread/3408c38464c57d4a))
*   Clarified message in Grid class related to row/column out of
bounds error ([post](http://groups.google.com/group/Google-Web-Toolkit/browse_thread/thread/e5e130cba766d126))
*   i18nCreator fixed to work with Java 5.0 ([post](http://groups.google.com/group/Google-Web-Toolkit/browse_thread/thread/3b6452a068ebec63))
*   I18NSync (and therefore -i18n scripts) changed to replace dots
with underscores when generating method names ([post](http://groups.google.com/group/Google-Web-Toolkit/browse_thread/thread/6a4e05beb094a5a2))
*   Additional character escaping in JSON strings ([post](http://groups.google.com/group/Google-Web-Toolkit/browse_thread/thread/5f437d12ba83fff0))
*   Fixed bug calling toString() on nested JSON objects ([post](http://groups.google.com/group/Google-Web-Toolkit/browse_thread/thread/ee81aa5411dece71))
*   Fixed bug that caused the default font size of text in a
FocusPanel to be zero [post](http://groups.google.com/group/Google-Web-Toolkit/browse_thread/thread/2f5abf147c5550c4)
*   Fixed TabPanel.insert() with asHTML argument ([post](http://groups.google.com/group/Google-Web-Toolkit/browse_thread/thread/8f427dbe76ce2c49/))
*   Popups and DialogBoxes no longer underlap lists and combos in
IE6 ([post](http://groups.google.com/group/Google-Web-Toolkit/browse_thread/thread/6815ec29d2c404e2))
*   DialogBoxes can no longer be dragged beoynd the upper left
corner of the browser window ([post](http://groups.google.com/group/Google-Web-Toolkit/browse_thread/thread/28390f32c42f7940))
*   Buttons inside of FormPanels no longer automatically submit on
Firefox; this is still a problem some versions of Safari and Opera ([post](http://groups.google.com/group/Google-Web-Toolkit/browse_thread/thread/72bea3a6d4feeaeb))
*   TabPanel now sets the height of the internal DeckPanel to 100%
to ensure all available space is used ([post](http://groups.google.com/group/Google-Web-Toolkit/browse_thread/thread/4cd09f04bc515696))
*   Fixed bug in Mozilla that was causing
DialogBox.onKeyPressPreview() to see key as 0 ([post](http://groups.google.com/group/Google-Web-Toolkit/browse_thread/thread/8147b4b219a8fbc7))
*   DockPanel no longer lays out with a DeferredCommand; this
makes it possible to correctly measure the size of PopupPanel ([post](http://groups.google.com/group/Google-Web-Toolkit/browse_thread/thread/7bbb89e4c97ae1e6))
*   SimplePanel is no longer abstract
*   Double click now fires correctly on IE6 ([post](http://groups.google.com/group/Google-Web-Toolkit/browse_thread/thread/2d16242dc7fb830f))
*   Fixed RPC bug that caused deserialization errors or infinite
loops with self-referential object graphs ([post](http://groups.google.com/group/Google-Web-Toolkit/browse_thread/thread/2a8658e93e2a8de3))
*   Fixed RPC bug that caused deserialization to fail on character
arrays containing null characters ([post](http://groups.google.com/group/Google-Web-Toolkit/browse_thread/thread/f60f5d5552a3d26b))
*   Serializable classes whose superclass is serialized by a
custom field serializer are now correctly deserialized on the server
*   Fixed bug related to FocusPanel that sometimes manifested
during RPC async responses ([post](http://groups.google.com/group/Google-Web-Toolkit/browse_thread/thread/edd16ede4f891db8))
*   Fixed bug in JUnit assertEquals() for floating point values
(delta was not honored correctly) ([post](http://groups.google.com/group/Google-Web-Toolkit/browse_thread/thread/b0e06cc253915b86))
*   Fixed internal compiler errors related to nested local
subclasses, empty for loop expressions, and no-op unary plus operator.
([post #1](http://groups.google.com/group/Google-Web-Toolkit/browse_thread/thread/d8ecf70acc4e5b0e),
[post #2](http://groups.google.com/group/Google-Web-Toolkit/browse_thread/thread/4da8dcbab9479a80),
[post #3](http://groups.google.com/group/Google-Web-Toolkit/browse_thread/thread/a9f17bf30d0116b))
*   Fixed infinite loop in Integer.toHexString() ([post](http://groups.google.com/group/Google-Web-Toolkit/browse_thread/thread/3f9c3f4df08fb523))
*   Compiler now handling filesystem symbolic links in project
structure ([post](http://groups.google.com/group/Google-Web-Toolkit/browse_thread/thread/1597c73888d1acd9))
*   Fixed rare JVM crash in Windows hosted mode related to JSNI
function pointers

* * *

## <a id="Release_Notes_1_1_0"></a> Release Notes for 1.1.0 (RC)

<p />

### New Features

*   **[FileUpload widget](doc/html/com.google.gwt.user.client.ui.FileUpload)**
The much-requested file upload widget
*   **[FormPanel widget](doc/html/com.google.gwt.user.client.ui.FormPanel)**
Easily submit traditional HTML forms from GWT apps
*   **[RPC optimizations](doc/html/com.google.gwt.doc.DeveloperGuide.RemoteProcedureCalls)**
Client and server performance improvements and
a more compact wire format
*   **[Automatic Resource Injection](doc/html/com.google.gwt.doc.DeveloperGuide.Fundamentals.Modules.AutomaticResourceInjection)**
Modules can contain references to
external JavaScript and CSS files, causing them to be automatically
loaded when the module itself is loaded
*   **[Internationalization](doc/html/com.google.gwt.doc.DeveloperGuide.Internationalization)**
Easily
localize strings and formatted messages
*   **[XML classes](doc/html/com.google.gwt.xml.client)**
An XML library based on the W3C DOM
*   **[JSON classes](doc/html/com.google.gwt.json.client)**
JSON has moved into `gwt-user.jar`, and
it's much faster than the sample that shipped with 1.0.21
*   **[JUnit
enhancements](doc/html/com.google.gwt.doc.DeveloperGuide.JUnitIntegration)**
Unit tests are much, much faster than in
1.0.21, easier to configure, and you can now test asynchronous things
like RPCs and timers
*   **[Javadoc](doc/javadoc/index)**
Now
included for your convenience, with sample code fragments
*   **`gwt-servlet.jar`**
Although you
should continue to build against `gwt-user.jar` as always,
you only need to deploy `gwt-servlet.jar` with your
webapps; it contains the subset of `gwt-user.jar` you'll
need to support RPC
There are also a significant number of bug fixes from all the great
feedback we've gotten from the developer forum. Please see the
[additional release notes](#Release_Notes)
for detailed information about other important changes in GWT since the
previous release, including a few breaking API changes that we don't
want to catch you off guard.

### Fixed Issues

*   [Issue #4794724 - Servlet container problems due to gwt-user.jar including javax.servlet
classes](http://code.google.com/webtoolkit/issues/4794724.html)
*   [Issue #1676686 - Hosted mode problems in Windows 2000](http://code.google.com/webtoolkit/issues/1676686.html)
*   [Issue #6606675 - ListBox and Image widgets are missing CSS style names](http://code.google.com/webtoolkit/issues/6606675.html)
*   [Issue #5922226 - Casts from interface types to concrete class types can
fail](http://code.google.com/webtoolkit/issues/5922226.html)
*   [Issue #4137736 - Referencing an outer local from a field initializer causes
NullPointerException](http://code.google.com/webtoolkit/issues/4137736.html)
*   [Issue #2518888 - Problem with "return" statements in
constructors](http://code.google.com/webtoolkit/issues/2518888.html)
*   [Issue #9984353 - Hosted Mode server throws IllegalArgumentException when system is set to
non-english locale](http://code.google.com/webtoolkit/issues/9984353.html)
*   [Issue #3733199 - Shrinking Grid via resizeRows() leads to inconsistent
state](http://code.google.com/webtoolkit/issues/3733199.html)
*   [Issue #7659250 - Hosted mode on default Fedora Core 5 complains of missing
libstdc++.so.5](http://code.google.com/webtoolkit/issues/7659250.html)
*   [Issue #6531240 - Empty if, while, do, and for statements cause Compiler
Error](http://code.google.com/webtoolkit/issues/6531240.html)
*   [Issue #4927592 - Multiple initializations in for loop initializer causes
internal compiler error.](http://code.google.com/webtoolkit/issues/4927592.html)

See the [appendix of fixed issues](#Appendix) for the
nitty-gritty list of things that we have fixed in this release,
including smaller issues.

### Behavioral Changes

Important changes in the behavior of existing GWT features.

#### Module Source and Public Paths

*   In previous versions of GWT, source and public path inclusions
were based on physical directory structure; only files physically
located with the module would be included. Going forward, source and
public path inclusions are based on logical package structure. Once a
package has been included, any files in that package become visible no
matter where they are physically located.

#### JUnit Modules

*   GWT test modules (that is, modules intended to run
GWTTestCase-derived JUnit test cases) no longer need to inherit the `com.google.gwt.junit.JUnit`
module. Additionally, it is no longer an error to declare entry points
within a test module (they will be ignored when running under JUnit).
Most test cases can now simply use the existing application module,
which should simplify test case configuration.

### Breaking API Changes

Based on user feedback, we've made a few API changes in this release
that may require minor tweaks to your existing code when you upgrade.
Any such changes that affect you should only take a few minutes to
rectify.

#### com.google.gwt.user.client.ui.HasWidgets

*   We've moved add(), remove(), and clear() into this interface,
so that any widget that can contain other widgets will be bound to
this contract.
*   The add() method no longer returns a boolean. If a panel
either cannot add a child widget without extra arguments, or cannot
accept further widgets, it will throw an exception. This is in keeping
with the fact that this is usually the result of an error in the code.
*   Its iterator is now required to support the remove() method.

#### com.google.gwt.user.client.ui.Composite

*   Composites must now call initWidget() in their constructors,
rather than setWidget(). This is more indicative of its actual
purpose, and also serves to clear up any confusion with SimplePanel's
setWidget() method. Composite.setWidget() is now deprecated.

#### com.google.gwt.user.client.ui.SimplePanel and subclasses

*   We have added setWidget() to SimplePanel, which has more
appropriate semantics for a panel that can contain only one child. The
add() method is still present through the HasWidgets interface, but
will fail if a widget is already present. This change is most likely
to affect you if you use DialogBox or PopupPanel. To fix it, simply
change your call to add() to setWidget() instead.

#### com.google.gwt.user.client.Cookies

*   Cookies.getCookie() is now static, as it should have been from
the beginning. There is no need to instantiate this class now.
*   You can now set cookies as well!

### Appendix: Complete List of Fixed Issues <a id="Appendix"></a>

The list of issues below is a short synopsis of all of the major and
minor issues fixed in this release. See the [online GWT issues
database](http://code.google.com/webtoolkit/issues/) for the important common issues.

*   String.matches(regex) should exist and doesn't
*   Need a way to set individual List items selected/unselected
(applies to multi-select listboxes)
*   DOM needs setBooleanAttribute, getBooleanAttribute.
*   HTMLTable.CellFormatter needs getStyleName() to match
setStyleName().
*   FlexTable's internal widget map does not correctly adjust for
the user inserting rows and cells.

*   Window.getTitle/setTitle should be static
*   # characters in filenames cause compilation to fail.
*   DynaTable has incorrect HTML
*   Change Timer API to use int not long
*   In hosted mode JSNI, marshall Java longs as VT_R8
*   Popups are not always positioned properly on Safari.
*   SWT source inclusion is wrong.
*   Safari crashes on exit under some circumstances.
*   TreeLogger throws away exception info in console mode.
*   Window needs a private ctor
*   Phone home version checking should actually compare ordering
of version number
*   Hosted Mode server throws IllegalArgumentException when system
is set to non-english locale
*   Trees have an unsightly 16-pixel left margin.
*   SimplePanel.remove() broken.
*   ScrollPanel doesn't implement SourcesScrollEvents.
*   Make junit-web output to www dir
*   Add "hidden" feature to ArgHandler system.
*   JUnitShell could hang forever.
*   1.5 VM fails to run junit because StackTraceElement 0-arg
constructor disappeared.
*   Panel and ComplexPanel still have methods from old version of
HasWidgets.
*   Nested tables can fire events from the wrong one.
*   Make sure JSNI refs to functions can be passed around and used
as real function pointers.
*   AbsolutePanel doesn't position its children consistently.
*   JSONParser does not handle generic JSONValues in the encoded
json string correctly; always assumes its a JSONObject
*   Remove -notHeadless from GWTShell (only applies to
GWTUnitTestShell)
*   Number of results returned from split() differs in
Java/JavaScript (see description)
*   Helper scripts don't work for base package.
*   Grid fails to update row count when removing.
*   JSONString, toString does not enclose its characters in double
quotes
*   Selection issue when removing widgets from TabPanel.
*   JSONParser always assumes root type is JSONObject
*   ClassSourceFileComposer should not handle Class objects.
*   Modules cannot supercede files from inherited modules
*   Simple &amp; ComplexPanel shouldn't implement getChildCount(),
getChild(), etc.
*   Negative byte values passed into JavaScript become positive
*   The rpc servlet needs a thread-local HttpServletResponse to
match the thread-local request.
*   Appending char to a String behaves incorrectly.
*   Remove STL dependency from gwt-ll
*   Using xhtml doctype causes popups to be misplaced on Mozilla
browsers.
*   JsniInjector fails to match lines when there are Javadoc
comments.
*   Add whitelist bypass for hosted browser
*   PopupPanel example is wrong.
*   UIObject needs a title property.
*   JSNI methods in local classes don't work in hosted mode.
*   HashMap throws a JavaScript error under some circumstances.
*   Source and Public module tags should be logical instead of
physical.
*   Document that module source and public tags are now logical
rather than physical.

*   Default .launch file fails to use project's full classpath.
*   RemoteServiceServlet sends back HTTP 200 OK but no content
under WebSphere.
*   Widget.onLoad() is called too early sometimes.
*   Is it really a good idea to have add(Widget) on Panel?
*   Web-mode JUnit that reports via RPC
*   Tree fires onTreeItemStateChanged twice.
*   Make JSON APIs part of gwt-user.jar
*   Async JUnit
*   String.equalsIgnoreCase(null) throws exception in web mode
*   Using a class literal for a pruned typed causes ICE
*   Make all built-in implementations of HasWidgets.iterator()
support remove().
*   JSON is slow in Web mode
*   StringBuffer uses string concatenation, and is n-squared as a
result
*   Identical Strings can compare false in web mode.
*   Tweaks to the property provider environment to support locale
and improve code uniformity b/w hosted and web mode

*   Server-side serialization is unusably slow for large data sets
*   Client side serialization is unusably slow for large datasets
*   Format source for JUnitTestCaseStubGeneratorm,
ServerSerializationStream
*   MethodDispatch not working correctly on IE.
*   JavaScriptObject rescuing is incomplete.
*   Reduce RPC wire size by not quoting non-strings.
*   Cyclic object graphs can be corrupted during deserialization
on the server
*   Test methods that throw checked exceptions cause the generated
code to fail to compile
*   Allow RemoteServiceResponse compression to be controlled by
subclasses
*   Startup timing bug makes RootPanel.get(id) throw an NPE
*   Change whitelist/blacklist settings to be command-line
switches rather than system properties
*   KeyCode is always 0 for keypress events on Mozilla.
*   Add Panel.remove(int) convenience method.
*   File Upload Widget
*   PopupPanel needs to deal better with being empty.
*   CheckBox.setEnabled() has reversed sense.
*   History tokens have problems with URL encoding.
*   Loosen restriction on when DockPanel.CENTER child may be
added.
*   AbsolutePanel needs getWidgetLeft() and getWidgetTop().
*   Decision: how should FlowPanel behave?
*   Samples with composites need to call initWidget() instead of
the deprecated setWidget().
*   RootPanel.get(String) should not be clearing the div's
contents.
*   ListBox, Image, and Hyperlink are missing style names, despite
doc
*   Hyperlink.removeClickListener is broken.
*   Don't allow tabs to word-wrap internally on TabPanel
*   Turkish locale problem with the RPC generated code - probably
affects others too
*   StackPanel.add() totally screwy.
*   Referencing a field that could cause static initialization
fails to cause a side effect.

* * *

## <a id="Release_Notes_1_0_21"></a> Release Notes for 1.0.21

### Fixed Issues
