The GWT Release Notes
=====================

* <a href="#Release_Notes_2_5_1">2.5.1</a>
* <a href="#Release_Notes_2_5_1_RC1">2.5.1 (RC1)</a>
* <a href="#Release_Notes_2_5_0">2.5.0</a>
* <a href="#Release_Notes_2_5_0_RC2">2.5.0 (RC2)</a>
* <a href="#Release_Notes_2_5_0_RC1">2.5.0 (RC1)</a>
* <a href="#Release_Notes_2_4_0">2.4.0</a>
* <a href="#Release_Notes_2_3_0">2.3.0</a>
* <a href="#Release_Notes_2_3_0_M1">2.3.0 (M1)</a>
* <a href="#Release_Notes_2_2_0">2.2.0</a>
* <a href="#Release_Notes_2_1_1">2.1.1</a>
* <a href="#Release_Notes_2_1_0">2.1.0</a>
* <a href="#Release_Notes_2_1_0_RC1">2.1.0 (RC1)</a>
* <a href="#Release_Notes_2_1_0_M3">2.1.0 (M3)</a>
* <a href="#Release_Notes_2_1_0_M2">2.1.0 (M2)</a>
* <a href="#Release_Notes_2_1_0_M1">2.1.0 (M1)</a>
* <a href="#Release_Notes_2_0_3">2.0.3</a>
* <a href="#Release_Notes_2_0_2">2.0.2</a>
* <a href="#Release_Notes_2_0_1">2.0.1</a>
* <a href="#Release_Notes_2_0_0">2.0.0</a>
* <a href="#Release_Notes_2_0_0_rc2">2.0.0 (RC2)</a>
* <a href="#Release_Notes_2_0_0_rc1">2.0.0 (RC1)</a>
* <a href="#Release_Notes_1_7_1">1.7.1</a>
* <a href="#Release_Notes_1_7_0">1.7.0</a>
* <a href="#Release_Notes_1_6_4">1.6.4</a>
* <a href="#Release_Notes_1_6_3">1.6.3</a>
* <a href="#Release_Notes_1_6_2">1.6.2</a>
* <a href="#Release_Notes_1_5_3">1.5.3</a>
* <a href="#Release_Notes_1_5_2">1.5.2</a>
* <a href="#Release_Notes_1_5_1">1.5.1 (RC2)</a>
* <a href="#Release_Notes_1_5_0">1.5.0 (RC)</a>
* <a href="#Release_Notes_1_4_60">1.4.60</a>
* <a href="#Release_Notes_1_4_59">1.4.59 (RC2)</a>
* <a href="#Release_Notes_1_4_10">1.4.10 (RC)</a>
* <a href="#Release_Notes_1_3_3">1.3.3</a>
* <a href="#Release_Notes_1_3_1">1.3.1 (RC)</a>
* <a href="#Release_Notes_1_2_22">1.2.22</a>
* <a href="#Release_Notes_1_2_11">1.2.21 (RC)</a>
* <a href="#Release_Notes_1_1_10">1.1.10</a>
* <a href="#Release_Notes_1_1_0">1.1.0 (RC)</a>
* <a href="#Release_Notes_1_0_21">1.0.21</a>


<p style="font-size: 85%;">
	<b>Note</b> - M1 = first milestone, RC1 = first release candidate
</p>

<hr />

<a name="Release_Notes_Current"></a>
<h2 id="Release_Notes_2_5_1">Release Notes for 2.5.1</h2>
<p>
	This release includes an update to the sample application's Maven POM
	files. See the release notes for <a href="#Release_Notes_2_5_1_RC1">2.5.1
		(RC1)</a> for the full list of features and bugs fixes included in the GWT
	2.5.1 release.
</p>

<h2 id="Release_Notes_2_5_1_RC1">Release Notes for 2.5.1 (RC1)</h2>
<p>GWT 2.5.1 is a maintenance release including many bugfixes and
	minor improvements since GWT 2.5.</p>

<h3>Changes since 2.5.0</h3>

<h4>Security Fixes</h4>

<ul>
	<li>Fixed an XSS vulnerability in html files used by GWTTestCase <a
		href="https://code.google.com/p/google-web-toolkit/source/detail?r=11385">(patch)</a>.
		These files will only be included in a GWT app if it depends on the
		JUnit module. Despite the fix, this is not recommended.
	</li>
</ul>

<h4>Compiler and Linkers</h4>

<ul>
	<li>Minor optimization improvements.
	<li>Optimization bugfixes: [<a
		href="https://code.google.com/p/google-web-toolkit/issues/detail?id=5739">5739</a>,
		<a
		href="https://code.google.com/p/google-web-toolkit/issues/detail?id=7818">7818</a>,
		<a
		href="https://code.google.com/p/google-web-toolkit/issues/detail?id=7683">7683</a>].
	</li>
	<li>Apps built with DirectInstallLinker should work in a page
		where inline scripts are forbidden (e.g. a Chrome extension)</li>
</ul>

<h4>Developer Mode</h4>
<ul>
	<li>Fixed compatibility with OpenJDK 7.</li>
	<li>Removed GWTShell, an obsolete way of starting dev mode.</li>
	<li>Added a checkbox to automatically scroll the log.</li>
</ul>

<p>Also, the Chrome plugin is now in the Chrome store.</p>

<h4>Super Dev Mode</h4>
<ul>
	<li>Added a -noprecompile option for faster startup.</li>
	<li>Added support for IE8 (recompiles only, not debugging).</li>
	<li>Reduced download size by using gzip.</li>
	<li>Fixed an incompatibility with the debugger in Chrome 24+.</li>
	<li>Fixed an incompatibility with third-party JavaScript that
		modifies the Array prototype.</li>
</ul>

<h4>Core libraries and JDK emulation</h4>
<ul>
	<li>Changed Window.Location to return the new parameter values
		after a call to window.history.pushState</li>
	<li>Added an option to ScriptInjector that removes the
		&lt;script&gt; tag from the page when done.</li>
	<li>Added RequestBuilder.withCredentials().</li>
	<li>Fixed a performance issue in RepeatingCommand [<a
		href="https://code.google.com/p/google-web-toolkit/issues/detail?id=7307">7307</a>]
	</li>
	<li>Fixed stack trace deobfuscation on iOS [<a
		href="https://code.google.com/p/google-web-toolkit/issues/detail?id=7902">7902</a>]
	</li>
	<li>Added the "webApiUsage" property. The default setting is
		"modern". If set to "stable", GWT libraries will avoid using prefixed
		web API's. Currently, the only difference is that animation will use a
		timer. [<a
		href="https://code.google.com/p/google-web-toolkit/issues/detail?id=7895">7895</a>]
	</li>
	<li>Fixed Double.parseDouble() when called with Nan, Infinity, and
		leading zeros [<a
		href="https://code.google.com/p/google-web-toolkit/issues/detail?id=7713">7713</a>,
		<a
		href="https://code.google.com/p/google-web-toolkit/issues/detail?id=7834">7834</a>]
	</li>
	<li>Fixed Long.parseLong when called with MIN_VALUE [<a
		href="https://code.google.com/p/google-web-toolkit/issues/detail?id=7308">7308</a>]
	</li>
	<li>Fixed a bug in AbstractMap.remove [<a
		href="https://code.google.com/p/google-web-toolkit/issues/detail?id=7856">7856</a>]
	</li>
</ul>

<h4>HTML, CSS, resource files</h4>

<ul>
	<li>Element's addClassName and removeClassName methods now return
		a boolean.</li>
	<li>Bugfix for getBodyOffsetLeft and Top: <a
		href="https://code.google.com/p/google-web-toolkit/source/detail?r=11432">(patch)</a></li>
	<li>Added CSS support for media types [<a
		href="https://code.google.com/p/google-web-toolkit/issues/detail?id=4911">4911</a>]
	</li>
	<li>Added "debug" obfuscation style for CSS <a
		href="https://code.google.com/p/google-web-toolkit/source/detail?r=11442">(change)</a></li>
	<li>Improved the quality of resized image resources [<a
		href="https://code.google.com/p/google-web-toolkit/issues/detail?id=7193">7193</a>]
	</li>
</ul>

<h4>Widgets and Editors</h4>

<ul>
	<li>Added Tree.setScrollIntoViewEnabled [<a
		href="https://code.google.com/p/google-web-toolkit/issues/detail?id=2467">2467</a>]
	</li>
	<li>Fixed a bug in SplitLayoutPanel.setWidgetHidden [<a
		href="https://code.google.com/p/google-web-toolkit/issues/detail?id=7715">7715</a>]
	</li>
	<li>Improved click handling in CellTable [<a
		href="https://code.google.com/p/google-web-toolkit/issues/detail?id=7508">7508</a>]
	</li>
	<li>Editor framework bugfixes: [<a
		href="https://code.google.com/p/google-web-toolkit/issues/detail?id=5589">5589</a>,
		<a
		href="https://code.google.com/p/google-web-toolkit/issues/detail?id=6959">6959</a>]
	</li>
</ul>

<h4>Internationalization</h4>
<ul>
	<li>Upgrades for <a
		href="https://code.google.com/p/google-web-toolkit/source/detail?r=11474">ICU4J</a>
		and <a
		href="https://code.google.com/p/google-web-toolkit/source/detail?r=11412">CLDR</a>
	</li>
	<li>Fixed a bug in DateTimeFormat.parse() [<a
		href="https://code.google.com/p/google-web-toolkit/issues/detail?id=7823">7823</a>]
	</li>
</ul>

<h4>RPC and server-side Java</h4>
<ul>
	<li>Fixed incorrect GWT-RPC deserialization when an app is
		deployed after changing an enum and nothing else. [<a
		href="https://code.google.com/p/google-web-toolkit/issues/detail?id=7836">7836</a>]
	</li>
	<li>Fixed a GWT-RPC generator bug that can cause too many policy
		files to be generated, resulting in excessive server-side memory
		usage. [<a
		href="https://code.google.com/p/google-web-toolkit/issues/detail?id=7791">7791</a>]
	</li>
	<li>Fixed an infinite loop in server-side GWT-RPC when
		deserializing complicated generic types. [<a
		href="https://code.google.com/p/google-web-toolkit/issues/detail?id=7779">7779</a>]
	</li>
	<li>RequestFactory bug fix: [<a
		href="https://code.google.com/p/google-web-toolkit/issues/detail?id=7900">7900</a>]
	</li>
	<li>Added a constructor option to StackTraceDeobfuscator for
		conserving server memory.</li>
	<li>Fixed an exception when using shared code that uses GWTBridge
		on the server [<a
		href="https://code.google.com/p/google-web-toolkit/issues/detail?id=7527">7527</a>]
	</li>
</ul>

<h2 id="Release_Notes_2_5_0">Release Notes for 2.5.0</h2>
<p>
	This release includes some minor bug fixes found in the release
	candidate. See <a href="doc/latest/ReleaseNotes.html">What's New in
		GWT 2.5</a> plus the release notes for <a href="#Release_Notes_2_5_0_RC1">2.5.0
		(RC1)</a> and <a href="#Release_Notes_2_5_0_RC2">2.5.0 (RC2)</a> for the
	full list of features and bugs fixes included in the GWT 2.5.0 release.
</p>

<h3>Security vulnerability from 2.4 to 2.5 Final</h3>

<p>The GWT team recently learned that the Security vulnerability
	discovered in the 2.4 Beta and Release Candidate releases was only
	partially fixed in the 2.4 GA release. A more complete fix was added to
	the 2.5 GA release. If you have an app that's been built with GWT 2.4
	or one of the 2.5 RCs, then you'll need to get the latest 2.5 release,
	recompile your app, and redeploy.</p>


<h2 id="Release_Notes_2_5_0_RC2">Release Notes for 2.5.0 (RC2)</h2>
<p>
	This is release candidate 2 of GWT 2.5. See the <a
		href="doc/latest/ReleaseNotes.html">What's new in GWT 2.5</a> page as
	well as release notes below for the full list of features and bugfixes
	in this release.
</p>

<h3>Changes since RC1</h3>
<ul>
	<li>The GWT tools can now run on JDK 7. (However, no JDK 7
		language or library features are available in GWT code yet.)</li>
	<li>The accessibility library introduced in RC1 has been cleaned
		up for release.</li>
	<li>Support for <a href="doc/latest/DevGuideValidation.html">validation</a>
		is improved, documented, and no longer considered experimental.
	</li>
	<li>Other fixes; see the <a
		href="http://code.google.com/p/google-web-toolkit/issues/list?q=FixedIn2_5RC2">issue
			tracker</a> for more.
	</li>
</ul>

<h2 id="Release_Notes_2_5_0_RC1">Release Notes for 2.5.0 (RC1)</h2>
<p>
	This is release candidate 1 of GWT 2.5. See the <a
		href="doc/latest/ReleaseNotes.html">What's new in GWT 2.5</a> page as
	well as release notes below for the full list of features and bugfixes
	in this release.
</p>

<h3>Major Enhancements</h3>
<ul>
	<li>Super Dev Mode, a replacement for Development Mode
		(experimental)</li>
	<li>The Elemental library (experimental): efficient DOM access and
		HTML5 API's (experimental)</li>
	<li>New compiler optimizations from Closure</li>
	<li>The fragment merging optimization, for reducing latency in
		large apps</li>
	<li>Development mode performance and refresh time speedups</li>
	<li>A new accessibility library for setting <a
		href="http://www.w3.org/TR/wai-aria/">ARIA</a> roles, states and
		properties on DOM elements
	</li>
	<li>UIBinder enhancements for rendering cells and accessing inline
		styles</li>
</ul>

<h3>Other Fixes and Enhancements</h3>
<ul>
	<li>Significant speedups in development refresh time as a result
		of generator result caching for the ClientBundle and RPC generators.
		Addition of a result caching API that generator writers can use
		(r10412, r10476).</li>
	<li>Drastic improvement in RPC performance in development mode by
		providing a development-mode-only implementation for RPC.</li>
	<li>Addition of source map support. See <a
		href="http://code.google.com/p/google-web-toolkit/wiki/SourceMaps">this
			document</a> for more details.
	</li>
	<li>Reduction of overhead of Java types in compiler output
		(r10435).</li>
	<li>Addition of CellTableBuilder API, and an API for custom
		headers and footers in CellTables (r10435, r10581).</li>
	<li>Addition of pre-deserialization type checking to reduce the
		number of error messages that occur from attempts to invoke an RPC
		method with type mismatches (r10518).</li>
	<li>DynaTableRF and Validation samples now use Maven instead of
		ant.</li>
	<li>DateCell now accepts a TimeZone as a constructor arg.</li>
	<li>&lt;ui:with&gt; now supports the nested &lt;ui:attribute&gt;
		element for setting property values on a provided type (r10675).</li>
	<li>Improvements in reporting of JavaScriptExceptions in
		development mode to include the method name and arguments.</li>
	<li>Added ability to mock out Messages and CSSResources for better
		unit testing (r10723, r10768).</li>
	<li>Added SafeHtml support to parts of the GWT user library.</li>
	<li>CssResource @defs now support multiple values (r10667).</li>
	<li>Added option to compile out logging calls below SEVERE and
		WARN (r10753).</li>
	<li>CLDR data updated to v2.1.</li>
	<li>Added simple and global currency patterns (r10742).</li>
	<li>Update ISO-8601 time format to accept a timezone of a literal
		"Z" to mean GMT+0 (r10773).</li>
	<li>Various fixes to stabilize the compiler output on null (no
		change) recompiles. Added the -Dgwt.normalizeTimestamps system
		property to zero out the date on output jar files to stabilize builds
		in build systems which rely on content-addressability (r10810).</li>
	<li>Added GWT emulation for Float.intBitsToFloat,
		Float.floatToIntBits, Double.longBitsToDouble, and
		Double.doubleToLongBits.</li>
	<li>Added support for typed arrays (r10839).</li>
	<li>Greatly reduce symbol map size by removing already-pruned
		symbols from the map.</li>
	<li>Allow absolute paths in ui:style's src attribute.</li>
	<li>Request methods that use Collections as parameters in
		RequestFactory's JSON-RPC mode now properly encode Collections.</li>
</ul>

<h3>Breaking Changes and Other Changes of Note</h3>
<ul>
	<li>If you previously linked against gwt-user.jar from pure Java
		code and now get ClassNotFoundExceptions, you may need to add
		gwt-dev.jar to your classpath as well.</li>
	<li>Deprecated methods
		AbsractCellTable.doSelection(Event,Object,int,int),
		AbstractHasData.onUpdateSelection(), and CellList.doSelection(Event,
		Object, int) have been removed.</li>
	<li>Major refactoring of IsRenderable API. See r10352 and r10421
		for details.
	<li>The LEFT/RIGHT placement of text in SimplePager has been
		fixed, which may swap it for people who already worked around the
		issue. See r10907.</li>
	<li>Tree[Item].addItem/insertItem(String html) have been
		deprecated due to potential XSS issues. Use
		Tree[Item].addTextItem/insertTextItem(String text) instead.</li>
	<li>@GwtTransient will now work for any annotation that with that
		simple name (for example, com.foo.GwtTransient). This prevents library
		writers from having their library depend on the GWT distribution.</li>
	<li>As part of the GWT compilation, an output file mapping full
		class names to CSS obfuscated names is generated. This is useful for
		debugging and testing (r10686).</li>
	<li>Client-side JUnit classes are now available as a separate
		module without having to pull in GWTTestCase (r10689).</li>
	<li>The permutation mapping file is now generated as part of every
		compile.</li>
	<li>UIBinder.useLazyWidgets is now set to true by default. See
		r10730 for more details.</li>
	<li>The following dependencies have been updated to the following
		versions:
		<ul>
			<li>Eclipse JDT 3.4.2_r894</li>
			<li>Guava 10.0.1</li>
			<li>HTMLUnit 2.9</li>
			<li>Apache HTTP Client 4.1.2 (for HTMLUnit)</li>
			<li>Apache Commons Lang 2.6 (for HTMLUnit)</li>
			<li>NekoHTML 1.9.15 (for HTMLUnit)</li>
		</ul>
	</li>
	<li>json-1.5.jar is now bundled as a part of gwt-dev.jar.
		streamhtmlparser (rebased) has been removed from gwt-dev.jar and has
		been bundled as part of gwt-user.jar and gwt-servlet.jar.</li>
	<li>The compiler metrics aspect of the Compile Report has been
		disabled by default. The compile report results in instability in the
		Compile Report. To add compiler metrics to the compilation report, use
		the -XcompilerMetrics flag.</li>
	<li>The compiler.emulatedStack property should no longer be used.
		compiler.stackMode is what should be used instead.</li>
</ul>

<p>
	In addition to the items mentioned above, see <a
		href="http://code.google.com/p/google-web-toolkit/issues/list?can=1&q=Milestone%3A2_5+status%3AFixed">for
		a list of bug fixes and enhancements for 2.5.0</a> in the GWT issue
	tracker.
</p>

<h3>Known Issues</h3>

<ul>
	<li>Clicking in the navigation area (but not on an actual item) of
		the Showcase sample results in the navigation area going blank.</li>
	<li>Importing samples/Expenses into Eclipse fails with "Main Type
		Not Specified" dialog.</li>
	<li>Mismatch in symbol map format between SpeedTracer and GWT
		results in SpeedTracer not being able to deobfuscate stack traces for
		GWT applications.</li>
</ul>

<hr />
<h2 id="Release_Notes_2_4_0">Release Notes for 2.4.0</h2>
<p>
	This is the General Availability release of GWT 2.4. See the release notes below for the full list of features and bug
	fixes included in this release.
</p>
<p>The 2.4 General Availability release of GWT contains new App
	Engine tools for Android, incremental RPC tooling, Apps Marketplace
	support, a faster UI Designer with better UiBinder support, a
	persistent unit cache for faster iterative development, a scrolling
	DataGrid with fixed header, Beans.isDesignTime() support, and bundled
	installers that make it easier to install and configure the GPE, GWT
	and GAE.</p>

<h3>General Enhancements</h3>
<ul>
	<li>App Engine tools for Android: Build installable Android apps
		that rely on App Engine for server-side support.</li>
	<li>Incremental RPC Tooling: Add server-side methods to App Engine
		code and GPE will generate the necessary serialization and Android
		code on the fly.</li>
	<li>Apps Marketplace Support: Deploy apps to the Google Apps
		Marketplace as easily as to App Engine.</li>
	<li>UI Designer: Faster startup and editing times, split-mode
		editing support for UiBinder, simplified CSS property editing,
		UiBinder morphing, IsWidget support, and more.</li>
	<li>Persistent Unit Cache: GWT Compiler and Development mode now
		cache compilation artifacts between runs. This results in faster
		startup time for iterative development.</li>
	<li>Scrolling DataGrid (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=188">#188</a>):
		The new DataGrid widget supports vertical scrolling with a fixed
		header (above) and footer (below).
	</li>
	<li>Design Time Support (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=226">#226</a>):
		The Beans.isDesignTime() method was added to the GWT emulation library
		in order to better isolate runtime-only code when a UI is edited in
		the UI Designer.
	</li>

</ul>

<h3>Noteworthy Fixed Issues</h3>
<ul>
	<li>Polymorphism not supported by RequestFactory (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=5367">#5367</a>)
	</li>
	<li>Support RequestFactory service inheritance on the client (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=6234">#6234</a>)
	</li>
	<li>ListEditor subeditors' value is not flushed when used with a
		RequestFactoryEditorDriver (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=6081">#6081</a>)
	</li>
	<li>Memory-leak in pure-Java's c.g.g.core.client.impl.WeakMapping
		(<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=6193">#6193</a>)
	</li>
	<li>GWT compiler dropping clinits (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=5707">#5707</a>)
	</li>
	<li>Make RequestFactory type tokens more compact (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=5394">#5394</a>)
	</li>
	<li>Editor framework does not support is / has methods (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=6040">#6040</a>)
	</li>

</ul>

<p>
	See <a
		href="http://code.google.com/p/google-web-toolkit/issues/list?can=1&q=Milestone%3A2_4+status%3AFixed">the
		complete list of bug fixes and enhancements for 2.4.0</a> in the GWT issue
	tracker.
</p>

<p>
	See <a
		href="http://code.google.com/p/google-web-toolkit/issues/list?can=1&q=Milestone%3A2_4+ReleaseNote%3DBreakingChange+status%3AFixed">the
		complete list of breaking changes</a> in the GWT issue tracker.
</p>

<h3>Security vulnerability in GWT 2.4</h3>

<p>Recently, the GWT team discovered a cross-site scripting
	vulnerability in the 2.4 Beta and Release Candidate releases (not in
	v2.3 GA or v2.4 GA). This vulnerability was partially fixed in the 2.4
	GA release and completely fixed in the 2.5 GA release. If you have an
	app that's been built with 2.4 then you'll need to get the latest 2.5
	release, recompile your app, and redeploy.</p>


<h3>Notes and Known Issues</h3>
<ul>
	<li>GPE's <a
		href="http://code.google.com/p/google-web-toolkit/wiki/WorkingWithMaven">Maven
			support</a> in Eclipse Indigo requires that the m2Eclipse WTP Integration
		plugin be installed. It can be installed via the <a
		href="http://download.jboss.org/jbosstools/updates/m2eclipse-wtp/">JBoss
			Update</a> site. See the <a
		href="http://code.google.com/p/google-web-toolkit/wiki/WorkingWithMaven">Google
			Maven wiki page</a> for the canonical reference for working with Maven in
		the latest GWT and GPE releases.
	</li>
	<li>GWT's new <a
		href="http://code.google.com/p/google-web-toolkit/wiki/RequestFactory_2_4">RequestFactory
			configuration</a> is not enabled by default for plain GWT, App Engine, or
		GWT + App Engine projects. It is enabled by default for new
		Cloud-Connected Android Projects only. See the <a
		href="http://code.google.com/p/google-web-toolkit/wiki/RequestFactoryInterfaceValidation">RequestFactoryInterfaceValidation</a>
		wiki page for information on how to use the new
		RequestFactoryInterfaceValidator within Eclipse, Maven, or the command
		line.
	</li>
</ul>


<hr />
<h2 id="Release_Notes_2_3_0">Release Notes for 2.3.0</h2>
<p>This is the General Availability release of GWT 2.3. See the
	release notes below for the full list of features and bug fixes
	included in this release.</p>

<h3>General Enhancements</h3>
<ul>
	<li>Added IE9 support. See the <a
		href="doc/latest/DevGuideIE9.html">IE9 - Tips and Tricks</a> doc for
		more information.
	</li>
	<li><a href="#Release_Notes_2_3_0_M1">2.3.0 (M1) - General
			Enhancements</a></li>
</ul>

<h3>Noteworthy Fixed Issues</h3>
<ul>
	<li><a href="#Release_Notes_2_3_0_M1">2.3.0 (M1) - Noteworthy
			Fixed Issues</a></li>
</ul>
<h3>Known Issues</h3>
<ul>
	<li>At compile time, you may see a warning similar to the
		following: "Configuration property UiBinder.useSafeHtmlTemplates is
		false! UiBinder SafeHtml integration is off, leaving your users more
		vulnerable to cross-site scripting attacks". This warning occurs
		because although UiBinder HTML rendering has been updated to support <a
		href="doc/latest/DevGuideSecuritySafeHtml.html">SafeHtml</a>, by
		default this is turned off (set to false), due to some minor bugs. If
		you wish, you can change the default by setting the
		"useSafeHtmlTemplates" property to true in UiBinder.gwt.xml. You can
		determine whether you are affected by the known bugs by checking the
		public bugs <a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=6145">6145</a>,
		<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=6149">6149</a>,
		and <a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=6198">6198</a>.
	</li>
</ul>


<p>
	See <a
		href="http://code.google.com/p/google-web-toolkit/issues/list?can=2&q=Milestone%3A2_3+status%3AFixedNotReleased">the
		complete list of bug fixes and enhancements for 2.3.0</a> in the GWT issue
	tracker.
</p>

<hr />
<h2 id="Release_Notes_2_3_0_M1">Release Notes for 2.3.0 (M1)</h2>
<p>This is milestone 1 of GWT 2.3.</p>

<h3>General Enhancements</h3>
<ul>
	<li>Added the following functionality to the Google Plugin for
		Eclipse:
		<ul>
			<li>Google API integration</li>
			<li>Project import from Google Project Hosting</li>
			<li>Single sign on, for accessing Project Hosting and App Engine</li>
		</ul>

	</li>
	<br />
	<li>Added GWT SDK support for HTML5 local storage</li>
</ul>

<h3>Noteworthy Fixed Issues</h3>
<ul>
	<li>Updated GPE's UIBinder editor to provide support for attribute
		auto-completion based on getter/setters in the owner type</li>
	<li>Optimizations to speed up GPE launch configuration UI</li>
	<li>"Check for Updates", within GPE, will now detect updates to
		GWT and GAE SDKs</li>
	<li>Launching against an external URL that contains a port number
		now works properly in Eclipse 3.6</li>
	<li>Updated IE9 support <a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=5125">(#5125)</a></li>
	<li>Fixed iFrame loading issues within Internet Explorer <a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=1720">(#1720)</a></li>
</ul>

<p>
	See <a
		href="http://code.google.com/p/google-web-toolkit/issues/list?can=2&q=Milestone%3A2_3+status%3AFixedNotReleased">the
		complete list of bug fixes and enhancements for 2.3.0 M1</a> in the GWT
	issue tracker.
</p>

<hr />
<h2 id="Release_Notes_2_2_0">Release Notes for 2.2.0</h2>
<p>The 2.2 release of GWT contains an integrated UI designer (part
	of the Google Plugin for Eclipse), support for HTML5 functionality,
	such as the Canvas/Audio/Video tags, an updated CellTable widget that
	now supports sortable columns and fixed column widths, and a more
	lenient SafeHtml template parser.</p>
<p>
	Also see <a href="doc/2.2/ReleaseNotes.html">What's New in GWT 2.2?</a>
</p>

<h3>General Enhancements</h3>
<ul>
	<li>Touchstart, touchmove, touchend, touchcancel have been
		integrated into the GWT event framework (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=5148">#5148</a>)
	</li>
	<li>Built-in bidi text support for widgets such as Label, HTML,
		Anchor, Hyperlink and ListBox. More information on this feature can be
		found <a
		href="http://googlewebtoolkit.blogspot.com/2011/01/gwt-goes-bidirectional.html">here</a>.
	</li>
</ul>
<h3>General Changes</h3>
<ul>
	<li>GWT Designer v8.1.1 and earlier versions do not support GWT
		2.2. To use GWT Designer with GWT 2.2, you need to uninstall the older
		version of GWT Designer and install the latest one.</li>
	<li>Version 2.2 has deprecated support for Java 1.5, resulting in
		warnings when building applications. While Java 1.5 will still work
		for at least the next release of GWT, developers should upgrade their
		version of Java to correct these warnings and ensure compatibility
		with future versions of GWT.</li>
</ul>
<h3>Noteworthy Fixed Issues</h3>
<ul>
	<li>RC releases are now being deployed as non-SNAPSHOTs in the
		maven repository (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=5429">#5429</a>)
	</li>
	<li>Date/Time patterns are correct in the "sv" local (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=5890">#5890</a>)
	</li>
</ul>
<p>
	See <a
		href="http://code.google.com/p/google-web-toolkit/issues/list?can=1&q=Milestone%202_2%20status:Fixed,FixedNotReleased">the
		complete list of bug fixes and enhancements for 2.2.0 M1, RC1 and
		final release</a> in the GWT issue tracker.
</p>

<hr />
<h2 id="Release_Notes_2_1_1">Release Notes for 2.1.1</h2>
<h3>General Enhancements</h3>
<ul>
	<li>Add a service layer to RequestFactory (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=5111">#5111</a>)
	</li>
</ul>
<h3>Noteworthy Fixed Issues</h3>
<ul>
	<li>GPE flags &lt;g:MenuItem&gt; as an error (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=5453">#5453</a>)
	</li>
	<li>Unable to disable a MenuItem (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=1649">#1649</a>)
	</li>
	<li>No way to set vertical/horizontal alignment of Cells in
		CellTable (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=5632">#5623</a>)
	</li>
</ul>
See the GWT issue tracker for
<a
	href="http://code.google.com/p/google-web-toolkit/issues/list?can=1&q=Milestone=2_1_1%20status:Fixed&sort=type&colspec=ID%20Type%20Status%20Owner%20Milestone%20Summary%20Stars">the
	complete list of bug fixes and enhancements</a>
in this release.
</p>

<hr />
<h2 id="Release_Notes_2_1_0">Release Notes for 2.1.0</h2>
<p>
	This release includes some minor bug fixes found in the release
	candidate. See <a href="doc/2.1/ReleaseNotes.html">What's New in
		GWT 2.1</a> plus the release notes for <a href="#Release_Notes_2_1_0_RC1">2.1.0
		(RC1)</a> for the full list of features and bugs fixes included in the GWT
	2.1.0 release.
</p>

<hr />
<h2 id="Release_Notes_2_1_0_RC1">Release Notes for 2.1.0 (RC1)</h2>
<p>This is release candidate 1 of GWT 2.1.</p>
<h3>Fixed Issues</h3>
<ul>
	<li>Creation broken if the entity id is of type String (<a
		href="https://jira.springsource.org/browse/ROO-1430">#1430</a>)
	</li>
	<li>addon-gwt is putting a boolean isChanged method in each proxy,
		and those don't work (<a
		href="https://jira.springsource.org/browse/ROO-1457">#1457</a>)
	</li>
	<li>ValueListBox showing redundant entries (<a
		href="https://jira.springsource.org/browse/ROO-1287">#1287</a>)
	</li>
	<li>Implement new update / create / acquire / delete events (<a
		href="https://jira.springsource.org/browse/ROO-1238">#1238</a>)
	</li>
	<li>Ensure that DefaultValueStore is always responsive (<a
		href="https://jira.springsource.org/browse/ROO-1217">#1217</a>)
	</li>
	<li>Allow both String and Long keys (<a
		href="https://jira.springsource.org/browse/ROO-951">#951</a>)
	</li>
	<li>Does the mobile.user.agent property provider actually work? (<a
		href="https://jira.springsource.org/browse/ROO-1468">#1468</a>)
	</li>
	<li>Banging on the UI can produce NPEs in DevMode window (<a
		href="https://jira.springsource.org/browse/ROO-1282">#1282</a>)
	</li>
	<li>NPE on resume in AbstractRecordListActivity(<a
		href="https://jira.springsource.org/browse/ROO-1230">#1230</a>)
	</li>
	<li>RequestFactoryServlet always throws when debugging with Chrome
		(<a href="https://jira.springsource.org/browse/ROO-1229">#1229</a>)
	</li>
	<li>Implement java.math.BigDecimal support for GWT</li>
	<li>Update the Expenses sample to pull dependencies in via a Maven
		repo (<a href="https://jira.springsource.org/browse/ROO-991">#991</a>)
	</li>
	<li>Add the custom Expense Report as a sample (<a
		href="https://jira.springsource.org/browse/ROO-965">#965</a>)
	</li>
	<li>Find replacement for velocity (<a
		href="https://jira.springsource.org/browse/ROO-956">#956</a>)
	</li>
	<li>Hard coded refusal to send fields named "password" in servlet
		(<a href="https://jira.springsource.org/browse/ROO-1262">#1262</a>)
	</li>
	<li>No uncaught exception handler in scaffold app (<a
		href="https://jira.springsource.org/browse/ROO-1250">#1250</a>)
	</li>
	<li>Remove web.xml welcome file handling from GWT addon (<a
		href="https://jira.springsource.org/browse/ROO-1512">#1512</a>)
	</li>
	<li>Use a publicly accessible DTD for ApplicationCommon.gwt.xml (<a
		href="https://jira.springsource.org/browse/ROO-1271">#1271</a>)
	</li>
</ul>
<h3>Known Issues</h3>
<ul>

	<li>RequestFactory does not fail gracefully for primitive types <a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=5357">(5357)</a></li>
	<li>Allow CellTable headers/footers to be refreshed <a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=5360">(5360)</a></li>
	<li>Need to document what situations source change events <a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=5361">(5361)</a></li>
	<li>Enable tests for RequestFactoryPolymorphicTest <a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=5364">(5364)</a></li>
	<li>Bring history management to Expenses sample <a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=5366">(5366)</a></li>
	<li>Polymorphism not supported by Request Factory <a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=5367">(5367)</a></li>
	<li>Server cannot return unpersisted objects <a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=5373">(5374)</a></li>
	<li>Javadoc polymorphism rules <a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=5374">(5374)</a></li>
	<li>Stopping an ActivityManager from a PlaceChangeEvent might
		cause an NPE <a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=5375">(5375)</a>
	</li>
	<li>Why is gwt-servlet.jar a compile-time dependency in the
		pom.xml generated by the expenses.roo script? <a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=5376">(5376)</a>
	</li>
	<li>Why does gwt-user.jar have scope "provided" in the pom.xml
		generated by the expenses.roo script? <a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=5377">(5377)</a>
	</li>
	<li>Server side domain classes cannot be resolved <a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=5378">(5378)</a></li>
	<li>Confirm logging compiles out of generated scaffold apps <a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=5379">(5379)</a></li>
	<li>Many classes added in 2.1 still have the experimental API
		warnings in their javadoc that need to be removed <a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=5380">(5380)</a>
	</li>
	<li>Showcase Cell List show {2} in entries with JDK 1.5 <a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=5385">(5385)</a></li>
	<li>Many stale javadoc warnings of experimental API <a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=5380">(5380)</a></li>
	<li>Instance methods are not looked up properly in the
		OperationRegistry <a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=5425">(5425)</a>
	</li>
</ul>

<hr />
<h2 id="Release_Notes_2_1_0_M3">Release Notes for 2.1.0 (M3)</h2>
<p>This is milestone 3 release of 2.1.</p>
<h3>Fixed Issues</h3>
<ul>
	<li>Add History support to the generated GWT scaffold app (<a
		href="https://jira.springsource.org/browse/ROO-883">#883</a>)
	</li>
	<li>Logging Implementation for M3 (<a
		href="https://jira.springsource.org/browse/ROO-888">#888</a>)
	</li>
	<li>Implement remaining primitive property types, including List
		and Long (<a href="https://jira.springsource.org/browse/ROO-933">#933</a>)
	</li>
	<li>Add enum support (<a
		href="https://jira.springsource.org/browse/ROO-935">#935</a>)
	</li>
	<li>Introduce setter methods for Record objects (<a
		href="https://jira.springsource.org/browse/ROO-938">#938</a>)
	</li>
	<li>Spec the minimum work needed for user auth, sign in (<a
		href="https://jira.springsource.org/browse/ROO-955">#955</a>)
	</li>
	<li>gwt project won't import properly in eclipse with m2eclipse,
		has errors (<a href="https://jira.springsource.org/browse/ROO-1122">#1122</a>)
	</li>
	<li>RequestFactoryServlet assumes content-length is known, which
		it may not be (<a href="https://jira.springsource.org/browse/ROO-1150">#1150</a>)
	</li>
	<li>Selecting "Delete" from an entity in the Scaffold app takes
		you to a list view that contains no data. (<a
		href="https://jira.springsource.org/browse/ROO-1164">#1164</a>)
	</li>
	<li>Allow client code to call instance methods (<a
		href="https://jira.springsource.org/browse/ROO-1185">#1185</a>)
	</li>
	<li>Creates are clobbering existing records (<a
		href="https://jira.springsource.org/browse/ROO-1194">#1194</a>)
	</li>
	<li>Update generated pom.xml to reference 2.1-SNAPSHOT jars (<a
		href="https://jira.springsource.org/browse/ROO-1200">#1200</a>)
	</li>
	<li>Cleanup the implementation hack in DeltaValueStoreJsonImpl...
		(<a href="https://jira.springsource.org/browse/ROO-1209">#1209</a>)
	</li>
	<li>Hard coded exit points for AbstractRecordEditActivity (<a
		href="https://jira.springsource.org/browse/ROO-1225">#1225</a>)
	</li>
	<li>Hard coded exit points for AbstractRecordListActivity (<a
		href="https://jira.springsource.org/browse/ROO-1226">#1226</a>)
	</li>
	<li>Can't create child resources from AbstractRecordEditActivity
		anymore (<a href="https://jira.springsource.org/browse/ROO-1227">#1227</a>)
	</li>
	<li>Update addon-gwt to reference GWT's M3 repo (<a
		href="https://jira.springsource.org/browse/ROO-1236">#1236</a>)
	</li>
	<li>GWT addon has links to MVC scaffolded view artifacts (<a
		href="https://jira.springsource.org/browse/ROO-1249">#1249</a>)
	</li>
	<li>RootPanel.get(id) fails to set 'position:relative' and
		'overflow:hidden' (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=1813">#1813</a>)
	</li>
	<li>UncaughtExceptionHandler not triggered for exceptions
		occurring in onModuleLoad() (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=1617">#1617</a>)
	</li>
</ul>

<hr />
<h2 id="Release_Notes_2_1_0_M2">Release Notes for 2.1.0 (M2)</h2>
<p>This is milestone 2 release of 2.1.</p>

<hr />
<h2 id="Release_Notes_2_1_0_M1">Release Notes for 2.1.0 (M1)</h2>
<p>This is a preview release of 2.1, that contains a new set of Cell
	Widgets and an app framework that make it easier to build large scale
	business applications.</p>
<h3>Known Issues</h3>
<ul>
	<li>For App Engine-based apps, you will need to run Maven once
		from the command line before importing the app into STS. This could be
		as simple as running "mvn validate" or "mvn compile", the point is to
		prep your local repo with an unzipped GAE SDK.</li>
	<li>Compiling a new app and/or running it in dev mode, will
		generate error messages complaining about missing dependencies. These
		errors can be ignored as they are related to classes that are most
		likely not used by your app, and will be omitted during compilation,
		or while running in dev mode.</li>
</ul>
<h3>Fixed Issues</h3>
<ul>
	<li>Image.onload event does not fire on Internet Explorer when
		image is in cache (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=863">#863</a>)
	</li>
	<li>Image should provide method to set alternative text (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=1333">#1333</a>)
	</li>
	<li>setWordWrap() for CheckBox (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=1483">#1483</a>)
	</li>
	<li>RichTextArea - setEnabled does not work (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=1488">#1488</a>)
	</li>
	<li>ImageSrc6 throws native NPE exception (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=1700">#1700</a>)
	</li>
	<li>Array returned inside Serializable field causes
		ClassCastException in web mode (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=1822">#1822</a>)
	</li>
	<li>BigDecimal Support (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=1857">#1857</a>)
	</li>
	<li>When a MenuBar loses focus, the MenuItem remains selected (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=2458">#2458</a>)
	</li>
	<li>KeyPressEvent contains improper UTF codes (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=3753">#3753</a>)
	</li>
	<li>Make RemoteServiceRelativePath annotation
		RetentionPolicy.RUNTIME (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=3803">#3803</a>)
	</li>
	<li>TextBox fires JSException in IE (<a href=""
		http:/code.google.com/p/google-web-toolkit/issues/detail?id=4027“">(#4027</a>)
	</li>
	<li>Format number wrong result (<a href=""
		http:/code.google.com/p/google-web-toolkit/issues/detail?id=4173“">(#4173</a>)
	</li>
	<li>file:line citations in ui.xml files (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=4194">#4194</a>)
	</li>
	<li>Remove method in SplitLayoutPanel is broken (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=4217">#4217</a>)
	</li>
	<li>Refactor SessionHandler and BrowserChannelClient to allow
		other OOPHM clients than HtmlUnit (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=4287">#4287</a>)
	</li>
	<li>FinallyCommand scheduled from within another FinallyCommand
		sometimes gets stuck (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=4293">#4293</a>)
	</li>
	<li>Wrong result after formatting of a big number in the
		NumberFormat (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=4473">#4473</a>)
	</li>
	<li>Dictionary.keySet() contains "__gwt_ObjectId" in DevMode (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=4486">#4486</a>)
	</li>
	<li>JsStackEmulator may break up JsInvocation (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=4512">#4512</a>)
	</li>
	<li>DOMImpl.g/setInnerText() use unnecessarily expensive node
		manipulation (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=4586">#4586</a>)
	</li>
	<li>NumberFormat error formatting more than 6 decimal places (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=4598">#4598</a>)
	</li>
	<li>DateBox can generate an untrapped exception if a non-default
		format is used (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=4633">#4633</a>)
	</li>
	<li>PopupPanel.removeFromParent() dosen't remove glass panel (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=4720">#4720</a>)
	</li>
	<li>ClassNotFoundException from web.xml for configured listeners
		during devmode servlet validation (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=4760">#4760</a>)
	</li>
	<li>Converting ImageBundle to ResourceBundle causes a regression
		if bundle is used on the server side as well (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=4797">#4797</a>)
	</li>
	<li>Add java.util.logging emulation (<a
		href="# http://code.google.com/p/google-web-toolkit/issues/detail?id=4954">#4954</a>)
	</li>
</ul>

<hr />
<h2 id="Release_Notes_2_0_4">Release Notes for 2.0.4</h2>
<p>This 2.0.4 release contains fixes that were not included in the
	2.0.3 release.</p>
<h3>Noteworthy Fixed Issues</h3>
<ul>
	<li>Whole UI disappear in IE 7 when we Hover over the menubar menu
		item (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=4532">#4532</a>)
	</li>
	<li>List.subList not fully compatible with java.util.List
		interface (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=4993">#4993</a>)
	</li>
	<li>Safari 5 fails to execute non-integral right-shifts correctly
		(<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=5056">#5056</a>)
	</li>
</ul>

<hr />
<h2 id="Release_Notes_2_0_3">Release Notes for 2.0.3</h2>
<p>This 2.0.3 release contains fixes that were not included in the
	2.0.2 release.</p>
<h3>Noteworthy Fixed Issues</h3>
<ul>
	<li>Using a PopupPanel in Internet Explorer without a history
		IFrame throws a NullPointerException (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=4584">#4584</a>)
	</li>
	<li>Opera support for History is not working (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=3956">#3956</a>)
	</li>
</ul>

<hr />
<h2 id="Release_Notes_2_0_2">Release Notes for 2.0.2</h2>
<p>This 2.0.2 release contains a fix that was not included in the
	2.0.1 release.</p>
<h3>Noteworthy Fixed Issues</h3>
<ul>
	<li>Standard.css missing new layout styles (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=4429">#4429</a>)
	</li>
</ul>

<hr />
<h2 id="Release_Notes_2_0_1">Release Notes for 2.0.1</h2>
<p>This 2.0.1 release contains fixes for bugs found in the 2.0.0
	release.</p>
<h3>Potentially breaking changes and fixes</h3>
<ul>
	<li>Fixed a bug in how code generators collect method arguments
		from generated source, which impacted the Messages interfaces
		generated for UiBinder template files. In GWT 2.0, such argument names
		were incorrectly changed to ARGn. Most GWT applications will be
		unaffected, but external systems relying on these names will need to
		be updated.</li>
	<li>The development mode server will, by default, only bind to
		localhost which will break cross-machine debugging. You can get the
		old behavior by specifying <code>-bindAddress 0.0.0.0</code>. Please
		see issue (#<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=4322">4322</a>)
		for more details. For webAppCreator-generated ant files, you can pass
		this with <tt>ant -Dgwt.args="-bindAddress 0.0.0.0" devmode</tt>.
	</li>
	<li>The CurrencyList/CurrencyData APIs are now public - if you
		were relying upon these classes in their non-public location, you
		should only need to update your imports.</li>
</ul>

<h3>Noteworthy Fixed Issues</h3>
<ul>
	<li>UiBinder Image class with resource attribute, removes styles
		on that image (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=4415">#4415</a>)
	</li>
	<li>Widgets lose focus if its placed on FocusPanel (Opera, Safari)
		(<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=1471">#1471</a>)
	</li>
	<li>Remove method in SplitLayoutPanel is broken (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=4217">#4217</a>)
	</li>
	<li>Splitter constructor hard codes the background color of the
		splitter to white (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=4335">#4335</a>)
	</li>
	<li>Image should provide method to set alternative text (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=4335">#4335</a>)
	</li>
	<li>CssResource cannot parse unescaped '-', '_' in class selectors
		and unknown at-rules (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=3946">#3946</a>)
	</li>
	<li>Focusable implementation breaks ScrollPanels in Safari (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=1313">#1313</a>)
	</li>
	<li>RequestBuilder restricted to GET and POST (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=3388">#3388</a>)
	</li>
	<li>HTMLTable.Cell.getElement() calls
		getCellFormatter().getElement() with row and column swapped
		RequestBuilder restricted to GET and POST (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=3757">#3757</a>)
	</li>
	<li>MenuBar steals focus when hovered (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=3884">#3884</a>)
	</li>
	<li>TabLayoutPanel tabs don't line up properly on IE (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=4447">#4447</a>)
	</li>
	<li>webAppCreator produces ant build files which support the
		gwt.args property for passing additional flags to the gwtc and devmode
		rules, such as <tt>ant -Dgwt.args="-style PRETTY" gwtc</tt>.
	</li>
</ul>
<p>
	See the GWT issue tracker for <a
		href="http://code.google.com/p/google-web-toolkit/issues/list?can=1&q=status:Fixed,FixedNotReleased%20milestone:2_0_1&num=1000">
		the complete list of bug fixes and enhancements</a> in this release.
</p>
<hr />
<h2 id="Release_Notes_2_0_0">Release Notes for 2.0.0</h2>
<p>
	This release includes some minor bug fixes found in the release
	candidate. See <a href="doc/2.0/ReleaseNotes_2_0.html">What's New
		in GWT 2.0</a> plus the release notes for <a
		href="#Release_Notes_2_0_0_rc1">2.0.0 (RC1)</a> and <a
		href="#Release_Notes_2_0_0_rc2">2.0.0-rc2</a> for the full list of
	features and bugs fixes included in the GWT 2.0.0 release.
</p>

<hr />
<h2 id="Release_Notes_2_0_0_rc2">Release Notes for 2.0.0 (RC2)</h2>
<h3>New Features</h3>
<ul>
	<li>GWT 2.0 introduces a number of new panels, which together form
		a stable basis for fast and predictable application-level layout. The
		official doc is still in progress, but for an overview please see <a
		href="http://code.google.com/p/google-web-toolkit/wiki/LayoutDesign">Layout
			Design</a> on the wiki. The new set of panels includes <a
		href="/javadoc/latest/com/google/gwt/user/client/ui/RootLayoutPanel.html">RootLayoutPanel</a>,
		<a
		href="/javadoc/latest/com/google/gwt/user/client/ui/LayoutPanel.html">LayoutPanel</a>,
		<a
		href="/javadoc/latest/com/google/gwt/user/client/ui/DockLayoutPanel.html">DockLayoutPanel</a>,
		<a
		href="/javadoc/latest/com/google/gwt/user/client/ui/SplitLayoutPanel.html">SplitLayoutPanel</a>,
		<a
		href="/javadoc/latest/com/google/gwt/user/client/ui/StackLayoutPanel.html">StackLayoutPanel</a>,
		and <a
		href="/javadoc/latest/com/google/gwt/user/client/ui/TabLayoutPanel.html">TabLayoutPanel</a>.
	</li>
	<li>UiBinder now directly supports <code>LayoutPanel</code>. For
		example:<pre style="margin-left: 2em;">
&lt;g:LayoutPanel&gt;
  &lt;g:layer left='1em' width='20px'&gt;&lt;g:Label&gt;left-width&lt;/g:Label&gt;&lt;/g:Layer&gt;
  &lt;g:layer right='1em' width='20px'&gt;&lt;g:Label&gt;right-width&lt;/g:Label&gt;&lt;/g:Layer&gt;
  &lt;g:layer&gt;&lt;g:Label&gt;nada&lt;/g:Label&gt;&lt;/g:Layer&gt;
&lt;/g:LayoutPanel&gt;</pre>
	</li>
	<li><a
		href="/javadoc/latest/com/google/gwt/user/client/Window.Navigator.html">Window.Navigator</a>
		now provides access to the native browser's navigator object.</li>
</ul>

<h3>Breaking changes and known issues/bugs/problems</h3>
<ul>
	<li>Windows users who have previously installed the <i>GWT
			Developer Plugin for IE</i> will have to uninstall the old version. Use
		the following steps:
		<ol>
			<li>From the Windows "Start" Menu, open "Control Panel"</li>
			<li>Select "Add/Remove Programs"</li>
			<li>Select "GWT Developer Plugin for IE" then click "Uninstall"</li>
			<li>Run Internet Explorer and browse to <a
				href="http://gwt.google.com/samples/MissingPlugin">http://gwt.google.com/samples/MissingPlugin</a>
				to install the new version of the plugin
			</li>
		</ol>
	</li>
	<li>Running a <code>GWTTestCase</code> as compiled script was
		previously done using <code>-Dgwt.args="-web"</code>. The <code>-web</code>
		argument is now deprecated in favor of <code>-prod</code>, consistent
		with the terminology change from <i>web mode</i> to <i>production
			mode</i>.
	</li>
	<li>The <code>-portHosted</code> command line argument for <code>DevMode</code>
		and <code>GWTTestCase</code> has changed to <code>-codeServerPort</code>
		to be consistent with the new term <i>code server</i>.
	</li>
	<li>The <code>junitCreator</code> command line utility has been
		removed. Instead, the <code>webAppCreator</code> utility takes new
		argument: <code>
			-junit <i>&lt;path-to-junit-jar&gt;</i>
		</code>, which incorporates the functionality previously in junitCreator and
		generates <code>ant test</code> targets.
	</li>
	<li>When running development mode on on Chrome, any JavaScript
		objects that pass into Java code will be assigned a new property <code>__gwt_ObjectId</code>.
		This could break native code that looks iterates through the
		properties of such an object. To work around this issue, see this <a
		href="http://code.google.com/p/google-web-toolkit/source/diff?old=4807&r=7063&format=side&path=/trunk/user/src/com/google/gwt/json/client/JSONObject.java">example</a>
		of our changes to <code>JSONObject</code> (scroll to the bottom).
	</li>
	<li>Compile reports (formerly SOYC reports) are now generated with
		the <code>-compileReport</code> command line flag to <code>Compiler</code>.
		The generated reports are now written to the private <i>extra</i>
		directory. If no <code>-extra</code> argument is specified, this
		directory defaults to <code>extras/</code>. This eliminates an
		unlikely security risk of accidentally deploying compile reports to a
		publicly accessible location.
	</li>
</ul>
<h3>Fixed Issues</h3>
<ul>
	<li>In UiBinder <code>&lt;ui:style&gt;</code> blocks, css class
		names may contain dashes.
	</li>
	<li>Non-Java method safe characters in inline <ui:style> class names doesn't work
          (<a
				href="http://code.google.com/p/google-web-toolkit/issues/detail?id=4052">#4052</a>)
        </li>
	<li>@external does not work reliably for inline styles in <ui:style>
          (<a
				href="http://code.google.com/p/google-web-toolkit/issues/detail?id=4053">#4053</a>)
        </li>
	<li>Various false alarm warnings about invalid JSNI references
		have been fixed.</li>
	<li>Various Swing UI improvements.</li>
	<li>RPC calls leaking memory for IE (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=4133">#4133</a>)
	</li>
	<li>deRPC raise an Error 500 instead of propagating the correct
		RuntimeException in ProdMode (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=4237">#4237</a>)
	</li>
</ul>

<hr />
<h2 id="Release_Notes_2_0_0_rc1">Release Notes for 2.0.0 (RC1)</h2>
<p>This release contains big changes to improve developer
	productivity, make cross-browser development easier, and produce faster
	web applications.</p>

<h3>Things that are changing with GWT 2.0 that might otherwise be
	confusing without explanation</h3>
<ul>
	<li>Terminology changes: We're going to start using the term
		"development mode" rather than the old term "hosted mode." The term
		"hosted mode" was sometimes confusing to people, so we'll be using the
		more descriptive term "development mode" from now on. For similar
		reasons, we'll be using the term "production mode" rather than "web
		mode" when referring to compiled script.</li>
	<li>Changes to the distribution: Note that there's only one
		download, and it's no longer platform-specific. You download the same
		zip file for every development platform. This is made possible by the
		new plugin approach used to implement development mode (see below).
		The distribution file does not include the browser plugins themselves;
		those are downloaded separately the first time you use development
		mode in a browser that doesn't have the plugin installed.</li>
</ul>

<h3>Major New Features</h3>
<ul>
	<li>In-Browser Development Mode: Prior to 2.0, GWT hosted mode
		provided a special-purpose "hosted browser" to debug your GWT code. In
		2.0, the web page being debugged is viewed within a regular-old
		browser. Development mode is supported through the use of a
		native-code plugin called the <i>GWT Developer Plugin</i> for many
		popular browsers. In other words, you can use development mode
		directly from Safari, Firefox, Internet Explorer, and Chrome.
	</li>
	<li>Code Splitting: Developer-guided code splitting with <a
		href="/javadoc/latest/com/google/gwt/core/client/GWT.html#runAsync(com.google.gwt.core.client.RunAsyncCallback)">GWT.runAsync()</a>
		allows you to chunk your GWT code into multiple fragments for faster
		startup. Imagine having to download a whole movie before being able to
		watch it. Well, that's what you have to do with most Ajax apps these
		days -- download the whole thing before using it. With code splitting,
		you can arrange to load just the minimum script needed to get the
		application running and the user interacting, while the rest of the
		app is downloaded as needed.
	</li>
	<li>Declarative User Interface: GWT's <a
		href="/javadoc/latest/com/google/gwt/uibinder/client/UiBinder.html">UiBinder</a>
		now allows you to create user interfaces mostly declaratively.
		Previously, widgets had to be created and assembled programmatically,
		requiring lots of code. Now, you can use XML to declare your UI,
		making the code more readable, easier to maintain, and faster to
		develop. The Mail sample has been updated to show a practical example
		of using UiBinder.
	</li>
	<li>Bundling of resources via <a
		href="/javadoc/latest/com/google/gwt/resources/client/ClientBundle.html">ClientBundle</a>.
		GWT introduced <a
		href="/javadoc/latest/com/google/gwt/user/client/ui/ImageBundle.html">ImageBundle</a>
		in 1.4 to provide automatic spriting of images. ClientBundle
		generalizes this technique, bringing the power of combining and
		optimizing resources into one download to things like text files, CSS,
		and XML. This means fewer network round trips, which in turn can
		decrease application latency -- especially on mobile applications.
	</li>
	<li>Using HtmlUnit for running test cases based on <a
		href="/javadoc/latest/com/google/gwt/junit/client/GWTTestCase.html">GWTTestCase</a>:
		Prior to 2.0, <code>GWTTestCase</code> relied on SWT and native code
		versions of actual browsers to run unit tests. As a result, running
		unit tests required starting an actual browser. As of 2.0, <code>GWTTestCase</code>
		no longer uses SWT or native code. Instead, it uses <i>HtmlUnit</i> as
		the built-in browser. Because HtmlUnit is written entirely in the Java
		language, there is no longer any native code involved in typical
		test-driven development. Debugging GWT Tests in development mode can
		be done entirely in a Java debugger.
	</li>
</ul>

<h3>Breaking changes and known issues/bugs/problems</h3>
<ul>
	<li>Prior to 2.0, GWT tools such as the compiler were provide in a
		platform-specific jar (that is, with names like <code>gwt-dev-windows.jar</code>).
		As of 2.0, GWT tools are no longer platform specific and they reside
		in generically-named <code>gwt-dev.jar</code>. You are quite likely to
		have to update build scripts to remove the platform-specific suffix,
		but that's the extent of it.
	</li>
	<li>The development mode entry point has changed a few times since
		GWT 1.0. It was originally called <code>GWTShell</code>, and in GWT
		1.6 a replacement entry point called <code>HostedMode</code> was
		introduced. As of GWT 2.0, to reflect the new "development mode"
		terminology, the new entry point for development mode is <code>com.google.gwt.dev.DevMode</code>.
		Sorry to keep changing that on ya, but the good news is that the prior
		entry point still works. But, to really stay current, we recommend you
		switch to the new <code>DevMode</code> entry point.
	</li>
	<li>Also due to the "development mode" terminology change, the
		name of the ant build target produced by <code>webAppCreator</code>
		has changed from <code>hosted</code> to <code>devmode</code>. In other
		words, to start development mode from the command-line, type <code>ant
			devmode</code>.
	</li>
	<li>HtmlUnit does not attempt to emulate authentic browser layout.
		Consequently, tests that are sensitive to browser layout are very
		likely to fail. However, since <code>GWTTestCase</code> supports other
		methods of running tests, such as Selenium, that do support accurate
		layout testing, it can still make sense to keep layout-sensitive tests
		in the same test case as non-layout-sensitive tests. If you want such
		tests to be ignored by HtmlUnit, simply annotate the test methods with
		<code>@DoNotRunWith({Platform.Htmlunit})</code>.
	</li>
	<li>Versions of Google Plugin for Eclipse prior to 1.2 will only
		allow you to add GWT release directories that include a file with a
		name like <code>gwt-dev-windows.jar</code>. You can fool it by sym
		linking or copying gwt-dev.jar to the appropriate name.
	</li>
	<li>The way arguments are passed to the GWT testing infrastructure
		has been revamped. There is now a consistent syntax to support
		arbitrary "run styles", including user-written, with no changes to GWT
		itself. For example, <code>-selenium FF3</code> has become <code>-runStyle
			selenium:FF3</code>. This change likely does not affect typical test
		invocation scripts, but if you do use <code>-Dgwt.args</code> to pass
		arguments to <code>GWTTestCase</code>, be aware that you may need to
		make some changes.
	</li>
</ul>

<hr />
<h2 id="Release_Notes_1_7_1">Release Notes for 1.7.1</h2>
<p>This release adds support for Mac OS X version 10.6 (Snow
	Leopard) by allowing hosted mode to run with a 1.6 JRE in 32-bit mode
	(using the -d32 flag).</p>
<h3>Fixed Issues</h3>
<ul>
	<li>Allow hosted mode using a 1.6 JRE with the -d32 flag (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=3843">#3843</a>,
		<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=3998">#3998</a>)
	</li>
</ul>

<hr />
<h2 id="Release_Notes_1_7_0">Release Notes for 1.7.0</h2>
<p>This release adds explicit support for Internet Explorer 8,
	Firefox 3.5, and Safari 4 as well as a few high-priority bug fixes. In
	all other respects, it is very similar to GWT 1.6. Note, however, that
	this release is version 1.7.0 rather than version 1.6.5 to signify a
	potentially breaking change for libraries that use deferred binding to
	specialize code based on user agent (see the next section for technical
	details).</p>
<p>
	Also see <a href="doc/1.7/ReleaseNotes_1_7.html">What's New in 1.7?</a>
</p>

<h3>Potentially breaking changes and fixes</h3>
<ul>
	<li>This release includes explicit support for IE8, which has some
		significant behavioral changes from prior versions of IE. These
		changes are great enough that the new value <code>ie8</code> has been
		added for the <code>user.agent</code> deferred binding client
		property. If you have deferred binding rules (i.e. <code>&lt;replace-with&gt;</code>
		or <code>&lt;generate-with&gt;</code>) or property providers that are
		sensitive to <code>user.agent</code>, you may need to update them to
		account for the <code>ie8</code> value. For more information, see the
		<a href="http://code.google.com/p/google-web-toolkit/wiki/IE8Support">technical
			notes</a>.
	</li>
</ul>

<h3>Fixed Issues</h3>
<ul>
	<li>Updated GWT libraries to support IE8 (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=3558">#3558</a>,
		<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=3329">#3329</a>)
	</li>
	<li>Native exception in Node.is() (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=3644">#3644</a>)
	</li>
	<li>Incorrect firing of two click events from CheckBox and a
		related issue (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=3508">#3508</a>,
		<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=3679">#3679</a>)
	</li>
	<li>Compiler java.lang.StackOverflowError if you don't use -Xss to
		set a stack size (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=3510">#3510</a>)
	</li>
	<li>Mouse wheel in FF3 (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=2902">#2902</a>)
	</li>
	<li>GWT outputs expressions too long for WebKit (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=3455">#3455</a>)
	</li>
	<li>java.sql.Date.valueOf error (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=3731">#3731</a>)
	</li>
	<li>Added a workaround for Firefox 3.5 regression (<a
		href="https://bugzilla.mozilla.org/show_bug.cgi?id=497780">bugzilla
			#497780</a>)
	</li>
</ul>

<hr />
<h2 id="Release_Notes_1_6_4">Release Notes for 1.6.4</h2>
<h3>Fixed Issues</h3>
<ul>
	<li>The classpath in the scripts created by junitCreator was
		updated to refer to <code>/war/WEB-INF/classes</code> rather than <code>/bin</code>.
	</li>
</ul>

<hr />
<h2 id="Release_Notes_1_6_3">Release Notes for 1.6.3 (RC2)</h2>
<h3>Fixed Issues</h3>
<ul>
	<li>Various <a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=3496">servlet
			classpath issues</a> introduced in 1.6.2 are resolved.
	</li>
	<li>JSP compilation should work out of the box in hosted mode.</li>
</ul>

<hr />
<h2 id="Release_Notes_1_6_2">Release Notes for 1.6.2 (RC)</h2>
<p>
	Please see <a href="doc/1.6/ReleaseNotes_1_6.html">What's new in
		GWT 1.6?</a> (online)
</p>

<hr />
<h2 id="Release_Notes_1_5_3">Release Notes for 1.5.3</h2>
<h3>Fixed Issues</h3>
<ul>
	<li>RPC requests no longer fail on the embedded Android web
		browser</li>
	<li>Leaf <code>TreeItems</code> now line up with their non-leaf
		siblings
	</li>
	<li>Removing the last child node from a <code>TreeItem</code> no
		longer creates extra margins on the left
	</li>
	<li><code>HTTPRequest</code> no longer uses POST instead of GET on
		some IE installs because of incorrect XHR selection</li>
	<li>Compiler now uses a more reliable check to prevent methods
		with local variables from being inlined</li>
	<li><code>getAbsoluteTop()/Left()</code> can no longer return
		non-integral values</li>
	<li><code>Time.valueOf()</code> no longer fails to parse <code>"08:00:00"</code>
		or incorrectly accepts <code>"0xC:0xB:0xA"</code>.</li>
</ul>
<p>
	See the GWT issue tracker for <a
		href="http://code.google.com/p/google-web-toolkit/issues/list?can=1&q=status%3AFixed%2CFixedNotReleased%20milestone%3A1_5_3&num=1000">
		the complete list of bug fixes and enhancements</a> in this release.
</p>

<hr />
<h2 id="Release_Notes_1_5_2">Release Notes for 1.5.2</h2>
<h3>Potentially breaking changes and fixes</h3>
<ul>
	<li><code>History.onHistoryChanged()</code> has been added back
		(it was missing from 1.5 RC2) but is now deprecated. Application
		startup should be handled by calling the new <code>History.fireCurrentHistoryState()</code>.</li>
	<li>Fields marked <code>final</code> in serializable types will
		now generate a warning; the fact that they were not being serialized
		was a source of confusion. Mark such fields both <code>final</code>
		and <code>transient</code> to avoid the warning.
	</li>
	<li>Instance methods on overlay types cannot be accessed from
		JSNI. (This used to work in hosted mode, but failed at runtime in web
		mode.)</li>
	<li>The hosted mode server no longer serves <code>hosted.html</code>
		from a module's public path; instead the file is read directly from
		the classpath. This file is tightly coupled with the hosted mode
		implementation and was not meant to be user overridable.
	</li>
</ul>
<h3>General Enhancements</h3>
<ul>
	<li><code>Collections.unmodifiableSortedSet()</code> and <code>Collections.unmodifiableSortedMap()</code>
		are now implemented.</li>
	<li>The new <code>Accessibility</code> class enables widget
		authors to add ARIA support to their widgets. Many GWT widgets come
		with ARIA support by default.
	</li>
	<li>Improved exception stack traces in hosted mode when JSNI stack
		frames are present.</li>
</ul>
<h3>Fixed Issues</h3>
<ul>
	<li>Fixed the relationship between the coordinates returned by <code>Element.getAbsoluteLeft/Top()</code>
		and <code>Event.getClientX/Y()</code>. <code>Document.getBodyOffsetLeft/Top()</code>
		can be used to account for the difference between these two coordinate
		systems.
	</li>
	<li><b>Ctrl-Z</b> should correctly perform an undo operation in
		RichTextArea on IE.</li>
</ul>
<p>
	See the GWT issue tracker for <a
		href="http://code.google.com/p/google-web-toolkit/issues/list?can=1&q=status%3AFixed%2CFixedNotReleased%20milestone%3A1_5_Final&num=1000">
		the complete list of bug fixes and enhancements</a> in this release.
</p>

<hr />
<h2 id="Release_Notes_1_5_1">Release Notes for 1.5.1 (RC2)</h2>
<h3>Support for Standards Mode</h3>
<p>
	GWT 1.5 adds significantly more support for standards mode
	applications, but some widgets (especially those with table based
	layouts) may not behave as expected. The low level standards mode bugs
	(such as with
	<code>getAbsoluteLeft/Top()</code>
	) have been addressed, but some of the constructs that our widgets rely
	on do not work in standards mode. For example, you cannot set the
	height and width of a widget relative to its parent if its parent is a
	table cell, and
	<code>StackPanel</code>
	takes up much more vertical space than it should in Internet Explorer.
	All of our samples have been reverted back to quirks mode, and the
	<code>applicationCreator</code>
	defaults to quirks mode when creating a new GWT app.
</p>
<p>You can still use standards mode for your GWT app, but please be
	aware that you may notice some layout issues. If you are switching an
	app from quirks mode to standards mode, your CSS styles might be
	applied differently, which could also affect your application. We will
	continue to address standards mode support in future GWT releases.</p>
<h3>Potentially breaking changes and fixes</h3>
<ul>
	<li><code>DOM.eventGetClientX/Y()</code> now takes into account
		the margin and border of the body element</li>
	<li>In hosted mode, all <code>DOM.eventGetXXX()</code> methods now
		assert that the requested attribute is reliable across all supported
		browsers. This means that attempting to retrieve an attribute for an
		event that does not support that attribute will now throw an assertion
		error instead of returning a coerced value. Most notably, the click
		event throws an assertion error if you attempt to get the mouse button
		that was clicked.
	</li>
	<li>The return value of <code>DOM.eventGetXXX()</code> methods are
		now coerced to 0 instead of -1 in web mode. In hosted mode, an
		assertion error will be thrown if the attribute is not defined for the
		given event, as described in the previous bullet.
	</li>
	<li>Opera specific code has been upgraded to work with Opera 9.5,
		but may not work with older versions of Opera as we only support the
		most recent release. Specifically, some widgets may not be able to
		receive focus.</li>
	<li>Calls to <code>History.newItem()</code> now trigger an <code>onHistoryChanged()</code>
		event synchronously instead of asynchronously
	</li>
</ul>
<h3>General Enhancements</h3>
<ul>
	<li>Added support for the <code>contextmenu</code> event, which
		allows users to detect and override the browser's default context menu
	</li>
	<li>Improved performance of <code>NumberFormat</code></li>
	<li>Added support for altering the number of decimals in a
		currency in <code>NumberFormat</code>
	</li>
	<li>Improved performance of Animations</li>
	<li>Improved the appearance of the default GWT style themes</li>
	<li>Improved the Showcase sample with more robust examples and
		more language translations</li>
	<li><code>FormPanel</code> can now wrap an existing form and still
		submit it to a hidden iframe</li>
</ul>
<h3>Fixed Issues</h3>
<ul>
	<li><code>DOM.getAbsoluteLeft/Top()</code> and <code>DOM.eventGetClientX/Y()</code>
		no longer log an exception to the console in Firefox 3</li>
	<li>Fixed a memory leak in Internet Explorer</li>
	<li><code>DOM.getAbsoluteLeft/Top()</code> now takes into account
		the margin and border of the target element in Safari 3</li>
	<li>Fixed some bugs associated with history support</li>
</ul>
<p>
	See the GWT issue tracker for <a
		href="http://code.google.com/p/google-web-toolkit/issues/list?can=1&q=status%3AFixed%20milestone%3A1_5_RC2%20type%3Adefect&num=1000">
		the complete list of bug fixes and enhancements</a> in this release.
</p>

<hr />
<h2 id="Release_Notes_1_5_0">Release Notes for 1.5.0 (RC)</h2>
<p>
	This release candidate is, in a word, huge. Rather than including all
	the details here, please see <a
		href="http://code.google.com/p/google-web-toolkit-doc-1-5/wiki/ReleaseNotes_1_5_ImportantNotes">What's
		New in GWT 1.5?</a> for full details. The main thing you'll want to know
	is that GWT 1.5 supports the Java 5 language features (generics,
	enumerated types, annotations, etc.). But check out the full notes,
	because there's a lot of great stuff!
</p>

<hr />
<h2 id="Release_Notes_1_4_60">Release Notes for 1.4.60</h2>
<p>
	This release has only a couple of minor changes from <a
		href="#Release_Notes_1_4_59">1.4.59</a>.
</p>
<ul>
	<li>Fixed a bug in the benchmarking that prevented source code
		from showing up in reports.</li>
	<li>Fixed a bug in the hosted mode servlet context emulation where
		getResource() would fail to find a file in a module's public path.</li>
	<li>Compiler output files of the form <code>
			<i>module</i>.cache.html
		</code> used to contain html intended as a helpful note to a developer. This
		message has now been removed because screen readers and some browsers
		would display this content to end users.
	</li>
</ul>

<hr />
<h2 id="Release_Notes_1_4_59">Release Notes for 1.4.59 (RC2)</h2>
<p>
	This release includes numerous bugfixes and a few important changes. If
	you are upgrading from GWT 1.3.3, you are strongly encouraged to read
	the <a href="#Release_Notes_1_4_10">release notes for 1.4.10</a> first.
</p>

<h3>New Features</h3>
<ul>
	<li><a
		href="doc/html/com.google.gwt.user.client.DOM#eventGetCurrentEvent()">DOM.eventGetCurrentEvent()</a>
		now provides global access to the current Event object. (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=1309">#1309</a>)</li>
	<li><a
		href="doc/html/com.google.gwt.user.client.ui.PopupPanel#setPopupPositionAndShow(com.google.gwt.user.client.ui.PopupPanel.PositionCallback)">PopupPanel.setPopupPositionAndShow(PopupCallback
			callback)</a> now provides now provides a simpler and bulletproof way to
		control the layout of popups. (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=1120">#1120</a>,
		<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=1243">#1243</a>)</li>
	<li>The <a
		href="doc/html/com.google.gwt.user.client.ui.SuggestionHandler">SuggestionHandler</a>
		interface can be used to respond to the user selecting a suggstion in
		the <a href="doc/html/com.google.gwt.user.client.ui.SuggestBox">SuggestBox</a>.
		(<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=1086">#1086</a>)
	</li>
	<li><a href="doc/html/java.util.Collection">Collection.toArray(Object[])</a>
		is now implemented. (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=695">#695</a>)</li>
	<li>If you have it installed, <a href="http://gears.google.com/">Google
			Gears</a> is now accessible in hosted mode (Windows only). (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=1252">#1252</a>)
	</li>
</ul>

<h3>General Changes</h3>
<ul>
	<li>Startup is now faster and more reliable. In particular, <a
		href="doc/html/com.google.gwt.core.client.EntryPoint#onModuleLoad()">onModuleLoad()</a>
		is now called as soon as the DOM is ready, which will generally be
		before the page's body.onload() event is fired. This allows your
		application to startup before certain resources (such as images) are
		fully loaded.
	</li>
	<li>Linux hosted mode should be less crashy. (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=1105">#1105</a>,
		<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=1281">#1281</a>,
		<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=1358">#1358</a>)
	</li>
	<li>An important discussion of HTTP headers, caching, and HTTPS
		has been added to the <a
		href="doc/html/com.google.gwt.user.client.ui.ImageBundle">ImageBundle</a>
		documentation. (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=1172">#1172</a>)
	</li>
	<li><a
		href="doc/html/com.google.gwt.user.client.ui.PopupPanel#center()">PopupPanel.center()</a>
		now causes the popup to be shown as well as centered. (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=1120">#1120</a>)</li>
	<li><a href="doc/html/com.google.gwt.user.client.ui.RichTextArea">RichTextArea</a>
		underwent number of bugfixes and should be stable now. (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=1130">#1130</a>,
		<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=1214">#1214</a>,
		<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=1276">#1276</a>)</li>
	<li>New <a
		href="doc/html/com.google.gwt.doc.DeveloperGuide.RemoteProcedureCalls">RPC</a>
		warnings
		<ul>
			<li>Warn if a non-checked exception is used in the throws clause
				of a RemoteService method.</li>
			<li>Warn if no concrete, serializable subclasses can be found
				for a given type declared in a RemoteService interface.</li>
		</ul>
	</li>
	<li><a
		href="doc/html/com.google.gwt.doc.DeveloperGuide.RemoteProcedureCalls">RPC</a>
		now generates a serialization policy file during compilation. The
		serialization policy file contains a whitelist of allowed types which
		may be serialized. Its name is a strong hash name followed by <code>.gwt.rpc</code>.
		This file must be deployed to your web server as a public resource,
		accessible from a <a
		href="doc/html/com.google.gwt.user.server.rpc.RemoteServiceServlet">RemoteServiceServlet</a>
		via <code>ServletContext.getResource()</code>. If it is not deployed
		properly, RPC will run in 1.3.3 compatibility mode and refuse to
		serialize types implementing <a href="doc/html/java.io.Serializable">Serializable</a>.
		(<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=1297">#1297</a>)</li>
	<li><a
		href="doc/html/com.google.gwt.user.client.ui.Panel#adopt(com.google.gwt.user.client.ui.Widget,%20com.google.gwt.user.client.Element)">Panel.adopt(Widget,
			Element)</a> and <a
		href="doc/html/com.google.gwt.user.client.ui.Panel#disown(com.google.gwt.user.client.ui.Widget)">Panel.disown(Widget)</a>
		have been deprecated. If you have subclassed Panel, please carefully
		review the new documentation for <a
		href="doc/html/com.google.gwt.user.client.ui.Panel#add(com.google.gwt.user.client.ui.Widget)">Panel.add(Widget)</a>
		and <a
		href="doc/html/com.google.gwt.user.client.ui.Panel#remove(com.google.gwt.user.client.ui.Widget)">Panel.remove(Widget)</a>
		for details on the correct way to add and remove Widgets from Panels.
		(<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=1121">#1121</a>)</li>
	<li>The benchmark viewer application is now faster, prettier, and
		a bit more user friendly.</li>
</ul>

<h3>Retractions from 1.4.10</h3>
<ul>
	<li>Breaking changes to the semantics of <a
		href="doc/html/com.google.gwt.user.client.ui.UIObject#setStyleName(java.lang.String)">UIObject.setStyleName()</a>
		have been backed out. All changes relative to 1.3.3 should now be
		backwards-compatible. (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=1079">#1079</a>)
	</li>
	<li>The linux distribution of 1.4.10 bundled Mozilla 1.7.13
		instead of version 1.7.12, which is bundled in previous releases. This
		change caused problems on some systems, so it's been reverted back to
		Mozilla 1.7.12 again. (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=1105">#1105</a>)
	</li>
	<li>Numerous RPC warnings were added in 1.4.10. One of these
		warnings would be issued when a class containing native methods was
		found to be serializable. This warning now only applies to
		automatically serialized types; types with custom serializers will no
		longer trigger this warning. (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=1161">#1161</a>)
	</li>
	<li>A change to RPC in 1.4.10 would cause an error to be issued if
		a serializable type had any subtypes that were not serializable. This
		change caused code that worked in 1.3.3 to fail in 1.4.10. In this
		release, the error has been downgraded to a warning. (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=1163">#1163</a>)
	</li>
	<li>A potentially breaking change to event bubbling in 1.4.10 has
		been backed out in favor of the 1.3.3 behavior. (<a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=1159">#1159</a>)
	</li>
</ul>

<h3>Fixed Issues</h3>
<p>
	See the GWT issue tracker for <a
		href="http://code.google.com/p/google-web-toolkit/issues/list?can=1&q=status%3AFixed%20milestone%3A1_4_RC2%20type%3Adefect&num=1000">the
		complete list of bug fixes</a> in this release.
</p>

<hr />
<h2 id="Release_Notes_1_4_10">Release Notes for 1.4.10 (RC)</h2>
<p>This is the Release Candidate for GWT 1.4, the first GWT release
	developed with major participation from GWT open source contributors.
	It's been a long time coming, but we hope it's been worth the wait. In
	addition to tons of new features, optimizations, and performance
	enhancements, we've fixed more than 150 bugs. There are some important
	behavioral and potentially breaking API changes; if you read nothing
	else, please read these following sections!</p>

<h3>Behavioral Changes</h3>
<p>Important changes in the behavior of existing GWT features.</p>
<h4>Critical Changes to RPC</h4>
In previous versions, the RPC subsystem was too lenient and failed to
warn at compile time about potential (though unusual) edge cases that
could in theory cause problems at runtime. Beginning with version 1.4,
the RPC subsystem emits additional warnings and errors to help you
identify error-prone constructs. While this new behavior may seem
annoying at first, rest assured that fixing your code to avoid RPC
warnings will result in a smaller, faster, and more reliable app.
<ul>
	<li><b>Bad code that happened to work before might not now</b><br />
		Previously, if you declared one particular component type via
		@gwt.typeArgs at compile time, you could often get away with passing a
		different type at runtime. For example, placing a Date in an ArrayList
		of String might work. This type of code is less likely to work now and
		will likely become more strict in the future. Bottom line: don't do
		this. Make sure collections only contain the declared item type (or
		subtypes thereof).</li>
	<li><b><a href="doc/html/java.io.Serializable">Serializable</a>
			equivalent to <a
			href="doc/html/com.google.gwt.user.client.rpc.IsSerializable">IsSerializable</a></b><br />
		Although GWT's RPC mechanism doesn't purport to honor the semantics of
		Java serialization, by popular demand, Serializable and IsSerializable
		are now equivalent for the purposes of RPC. This should improve
		server-side interoperability and remove much of the need for DTOs.</li>
	<li><b>Warn about missing gwt.typeArgs</b><br /> Every Collection
		or Map type should have an associated <a
		href="doc/html/com.google.gwt.doc.DeveloperGuide.RemoteProcedureCalls.SerializableTypes">gwt.typeArgs
			javadoc annotation</a>. In the past, a missing @gwt.typeArgs would
		generally have no noticeable effect, because a bug in the RPC system
		would generate code for all available serializable types, even if they
		weren't used in your service interface. Now that this bug has been
		fixed, you can achieve <b>significant</b> reduction in the size of
		your compiled output by fixing these warnings.</li>
	<li><b>Warn about serializable subclasses that violate the
			serialization restrictions</b><br /> An RPC warning is emitted for
		classes that are assignable to IsSerializable or Serializable but that
		lack a default constructor or contain fields which cannot be
		serialized. It is important to resolve these warnings to avoid rare
		but confusing situations in which exceptions would be thrown at
		runtime.</li>
	<li><b>Warn about non-transient, final instance fields</b><br />
		RPC has never actually serialized <code>final</code> instance fields,
		but now it explicitly warns about the existence of such fields unless
		they are also <code>transient</code>. Thus, the warning can be
		addressed by making <code>final</code> instance fields <code>transient</code>
		as well, or it can be suppressed via a module property.</li>
	<li><b>Warn about local and non-static nested types that
			implement IsSerializable or Serializable</b><br /> RPC has never
		serialized these kinds of classes and will now generate a warning.</li>
	<li><b>Warn about native methods in serializable classes</b><br />
		Attempting to serialize classes that contain <code>native</code>
		methods will cause UnsatisfiedLinkErrors if such methods are called in
		server-side code.</li>
</ul>
<h4>Module Script Tags</h4>
<ul>
	<li>In previous versions of GWT, including external JavaScript
		files via a module &lt;script&gt; tag required a nested JavaScript
		expression &mdash; called a <i>script-ready function</i> &mdash; that
		would determine when the script had been successfully loaded. Script
		load order is now handled automatically and these expressions are
		ignored. A warning will be issued in hosted mode. For reference, see <a
		href="doc/html/com.google.gwt.doc.DeveloperGuide.Fundamentals.Modules.AutomaticResourceInjection">here</a>.
	</li>
</ul>
<h4>Additional Hosted Mode Checks Related to JSNI</h4>
<ul>
	<li>Previously, when passing values from JavaScript into Java,
		hosted mode would silently coerce a JavaScript value of an incorrect
		type into the declared Java type. Unfortunately, this would allow code
		to work in hosted mode that could fail in unexpected ways in web mode.
		Hosted mode will now throw a HostedModeException if you try to pass an
		incompatible type. See <a
		href="doc/html/com.google.gwt.doc.DeveloperGuide.JavaScriptNativeInterface.Marshaling">here</a>
		for more details.
	</li>
</ul>

<h3>Breaking API Changes</h3>
This release also includes API changes that may require minor tweaks to
existing code. Any such changes that affect you should only take a few
minutes to rectify.

<h4>
	<a href="doc/html/com.google.gwt.core.client.JavaScriptObject">JavaScriptObject</a>
</h4>
<ul>
	<li>Although subclassing JavaScriptObject is not supported, some
		people do so anyway at their own risk :) Please note that the existing
		(int) constructor has been removed in favor of a protected no-arg
		constructor. Read the source code for <a
		href="doc/html/com.google.gwt.user.client.Element">Element</a> for an
		example of how JavaScriptObject must be subclassed now (that is, if
		subclassing were supported...which, of course, it isn't).
	</li>
</ul>
<h4>
	<a href="doc/html/com.google.gwt.user.client.DeferredCommand">DeferredCommand</a>
</h4>
<ul>
	<li>The add() method is deprecated in favor of addCommand() in
		order to support the new <a
		href="doc/html/com.google.gwt.user.client.IncrementalCommand">IncrementalCommand</a>
		interface. Had we simply added a new method overload, existing code
		that passed in a null literal would have failed to compile.
	</li>
	<li>The new addPause() method should be used instead of add(null).</li>
</ul>
<h4>
	<a href="doc/html/com.google.gwt.user.client.ui.UIObject">UIObject</a>
</h4>
<ul>
	<li>The intended use and behavior of style names has been
		formalized in UIObject (and therefore in all widgets). All style names
		are now classified as "primary", "secondary", and "dependent" styles,
		the meanings of which are detailed in the UIObject documentation. The
		relevant method signatures remain unchanged (get/setStyleName(),
		add/removeStyleName()), and most widgets should be unaffected. One
		potentially breaking change, however, is that an exception is thrown
		if an attempt is made to remove the primary style name of a widget
		using removeStyleName(). See the UIObject documentation for a full
		explanation.</li>
</ul>

<h3>New Features</h3>
Here are a few of the coolest new features and enhancements in GWT 1.4.
<ul>
	<li><b>Size and Speed Optimizations</b>
	<ul>
			<li>New <a
				href="http://code.google.com/p/google-web-toolkit/issues/detail?id=610">size</a>
				<a
				href="http://code.google.com/p/google-web-toolkit/issues/detail?id=599">improvements</a>
				in the GWT compiler produce JavaScript that is 10-20% smaller; just
				recompile your app with 1.4.
			</li>
			<li>An enhanced startup sequence reduces the size of your
				module's startup script by 80%. More importantly, the new startup
				sequence removes an HTTP round-trip, making startup latency about
				33% faster.</li>
			<li>The above optimizations combined with <a href="#ImageBundle">ImageBundle</a>,
				make it possible for GWT-based applications to load surprisingly
				quickly. To see for yourself, check out startup time of the <a
				href="samples/Mail/www/com.google.gwt.sample.mail.Mail/Mail">Mail</a>
				sample.
			</li>
		</ul></li>
	<li><b>Deployment Enhancements</b>
	<ul>
			<li>GWT RPC is no longer tied to exclusively to servlets. New <a
				href="http://code.google.com/p/google-web-toolkit/issues/detail?id=389">modularized
					RPC</a> server code makes it easy to connect GWT RPC to your choice of
				Java back-ends.
			</li>
			<li>Adding GWT modules to an HTML page has been simplified.
				Instead of adding a &lt;meta name='gwt:module'&gt; and &lt;script
				src='gwt.js'&gt;, you just add a single script element for your
				module.</li>
			<li>Cross-site script inclusion is now supported. The compiler
				produces a "-xs" (meaning "cross-site") version of your module's
				startup script that can be included without being restricted by the
				same-origin policy. WARNING: including scripts from other sites that
				you don't fully trust is a <a
				href="http://groups.google.com/group/Google-Web-Toolkit/web/security-for-gwt-applications">big
					security risk</a>.
			</li>
		</ul></li>
	<li><b>Widget and Library Enhancements</b>
	<ul>
			<li><a
				href="doc/html/com.google.gwt.user.client.ui.RichTextArea">RichTextArea</a>
				allows "drop in" functionality for rich text editing.</li>
			<li><a href="doc/html/com.google.gwt.user.client.ui.SuggestBox">SuggestBox</a>
				makes it easy to add auto-complete functionality.</li>
			<li>Splitters! <a
				href="doc/html/com.google.gwt.user.client.ui.HorizontalSplitPanel">HorizontalSplitPanel</a>
				and <a
				href="doc/html/com.google.gwt.user.client.ui.VerticalSplitPanel">VerticalSplitPanel</a>
				enable you to resize portions of the user interface.
			</li>
			<li><a href="doc/html/com.google.gwt.user.client.ui.PushButton">PushButton</a>
				and <a href="doc/html/com.google.gwt.user.client.ui.ToggleButton">ToggleButton</a>
				are easy-to-customize button widgets that can enhance the
				look-and-feel of your UI.</li>
			<li><a
				href="doc/html/com.google.gwt.user.client.ui.DisclosurePanel">DisclosurePanel</a>
				is a simple, nice-looking panel that lets users easily hide and show
				portions of your application UI.</li>
			<li><a href="doc/html/com.google.gwt.i18n.client.DateTimeFormat">DateTimeFormat</a>
				and <a href="doc/html/com.google.gwt.i18n.client.NumberFormat">NumberFormat</a>
				make it easy to format and parse dates, times, and numbers for users
				all over the world.</li>
			<li><a
				href="doc/html/com.google.gwt.user.client.IncrementalCommand">IncrementalCommand</a>
				helps you implement long-running tasks in your client code without
				triggering "slow script" warnings.</li>
			<li>A new <a
				href="http://docs.google.com/View?docid=d9s6nb7_1d723ft">benchmarking
					subsystem</a> integrates with JUnit to let you record and compare the
				speed of code snippets across multiple browsers and multiple
				parameter ranges. Benchmarking is a powerful way to identify
				bottlenecks and compare performance of alternative implementations.
			</li>
			<li>The oft-requested <a
				href="http://code.google.com/p/google-web-toolkit/issues/detail?id=21">java.io.Serializable</a>
				is now included in the JRE emulation library and is synonymous with
				IsSerializable for the purpose of GWT RPC.
			</li>
			<li><a
				href="http://code.google.com/p/google-web-toolkit/issues/detail?id=844">Mouse
					wheel events</a> are now available on a variety of widgets.</li>
		</ul></li>
	<li id="ImageBundle"><b><a
			href="doc/html/com.google.gwt.user.client.ui.ImageBundle">ImageBundle</a></b>
	<ul>
			<li>ImageBundle is the single biggest
				have-to-see-it-to-believe-it feature in this release. Image bundles
				make it trivially easy to combine dozens of images into a single
				"image strip", collapsing what would have been dozens of HTTP
				requests into one: a single, permanently-cacheable image file.</li>
			<li>Image bundles manage everything for you automatically, from
				computing clipping rectangles to making transparent PNGs work in
				IE6. You can even choose to get the clipped image as an Image widget
				or as pure HTML for inclusion in a larger HTML template.</li>
			<li>In addition to enabling a blazing-fast startup, image
				bundles help make the UI look better during startup, too. Typical
				AJAX apps exhibit "bouncy relayout" as individual images are loaded
				one-at-a-time. Fixing this problem has historically required
				laboriously pre-initializing the width and height of each individual
				image ahead of time. Image bundles do the same thing automatically.
				The dimensions of each clipped image are computed at compile time
				while the bundled image file is being created. Voila! The result is
				a fast, non-ugly user startup experience that requires no extra work
				on the part of the GWT developer to keep up-to-date.</li>
			<li>See the <a
				href="doc/html/com.google.gwt.doc.DeveloperGuide.UserInterface.ImageBundles">doc
					section</a> for more details.
			</li>
		</ul></li>
</ul>

<p>
	See the GWT issue tracker for <a
		href="http://code.google.com/p/google-web-toolkit/issues/list?can=1&q=status%3AFixed%20milestone%3A1_4_RC%20type%3Aenhancement&num=1000">the
		complete list of enhancements</a> in this release.
</p>

<h3>Fixed Issues</h3>
<p>
	See the GWT issue tracker for <a
		href="http://code.google.com/p/google-web-toolkit/issues/list?can=1&q=status%3AFixed%20milestone%3A1_4_RC%20type%3Adefect&num=1000">the
		complete list of bug fixes</a> in this release.
</p>

<hr />
<h2 id="Release_Notes_1_3_3">Release Notes for 1.3.3</h2>
<p>This version has only minor functional changes from 1.3.1, listed
	below.</p>

<h3>Fixed Issues</h3>
<ul>
	<li><a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=319">Issue
			#319 - Calling native super method in implementation class results in
			infinite loop in web mode</a></li>
	<li><a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=496">Issue
			#496 - gwt.js in gwt-user.jar lacks Apache 2.0 license header</a></li>
	<li><a
		href="http://code.google.com/p/google-web-toolkit/issues/detail?id=497">Issue
			#497 - Unexpected internal compiler error - Analyzing permutation #1</a></li>
</ul>

<hr />
<h2 id="Release_Notes_1_3_1">Release Notes for 1.3.1 (RC)</h2>
<p>This is the Release Candidate for GWT 1.3, the first completely
	open source version of GWT. This version has no new functionality, but
	we did make a lot of changes to get the source code and build scripts
	into presentable shape to prepare for ongoing open source development.
	Although the changes were relatively harmless -- formatting, sorting,
	more documentation, and a new build system -- there's always a small
	chance of problems, so we plan to call this a Release Candidate until
	we've convinced ourselves it's reliable.</p>

<h3>Useful Links</h3>
<ul>
	<li><b><a href="makinggwtbetter">Making GWT Better</a></b> <br>This
		is our new GWT open source charter that describes how we plan to
		operate the project and how you can access the GWT source, compile it
		yourself, and contribute.</li>
	<li><b><a
			href="http://code.google.com/p/google-web-toolkit/issues/list">The
				GWT Issue Tracker</a></b> <br>Please report any bugs in 1.3 RC that
		weren't in 1.2.22 in the GWT issue tracker. These would be likely
		related to the new build, and we want to know ASAP so we can fix them.</li>
	<li><b><a href="http://google-web-toolkit.googlecode.com/svn/">The
				GWT Subversion Repository</a></b> <br>Visit the online repository to
		browse the GWT source without a Subversion client.</li>
</ul>

<hr />
<h2 id="Release_Notes_1_2_22">Release Notes for 1.2.22</h2>
<p>
	This is the official GWT 1.2 release, the follow up to the <a
		href="#Release_Notes_1_2_11">GWT 1.2 Release Candidate</a>. It
	includes all of the enhancements and bug fixes from GWT 1.2 RC as well
	as a few additional bug fixes that were reported against GWT 1.2 RC.
</p>

<h3>About OS X Hosted Mode Support</h3>
GWT's hosted mode support is available only on OS X 1.4 (Tiger) or
later.

<h3>Useful Links</h3>
<ul>

	<li><b><a
			href="http://code.google.com/p/google-web-toolkit/issues/list?can=1&q=status%3AFixed%20milestone%3A1_2_Final">Changes
				included in GWT 1.2 since the RC</a></b> <br>Also see the GWT Blog for
		a discussion of the <a
		href="http://googlewebtoolkit.blogspot.com/2006/11/wrapping-up-gwt-12-soon.html">noteworthy
			issues related to 1.2 RC</a></li>
	<li><b><a
			href="http://code.google.com/p/google-web-toolkit/issues/list?can=1&q=status%3AFixed%20milestone%3A1_2_RC">New
				features and bug fixes in GWT 1.2 RC</a></b></li>
</ul>

<h3>Breaking API Changes</h3>
There are no breaking changes to pre-1.2 APIs, but one method has been
renamed in a class that was new in 1.2 RC.

<h4>com.google.gwt.http.client.RequestBuilder</h4>
The method
<code>addHeader()</code>
was renamed to
<code>setHeader()</code>
to more clearly reflect its intent. You will only be affected by this
change if you are using the new HTTP functionality available as of build
1.2.11.

<hr />
<h2 id="Release_Notes_1_2_11">Release Notes for 1.2.11 (RC)</h2>
<p>This is the Release Candidate for GWT 1.2. Between this build and
	the subsequent GWT 1.2 official release, changes are limited to issues
	unique to GWT 1.2 RC.
<p>
	See the GWT issue tracker for <a
		href="http://code.google.com/p/google-web-toolkit/issues/list?can=1&q=status%3AFixed%20milestone%3A1_2_RC">the
		complete list of enhancements and bug fixes</a> in this release.
</p>

<h3>New Features</h3>
<ul>
	<li><b><a
			href="http://code.google.com/p/google-web-toolkit/issues/detail?id=91">Full
				support for OS X development</a></b> <br>Develop with GWT on OS X as
		easily as on Linux and Windows</li>
	<li><b><a
			href="http://code.google.com/p/google-web-toolkit/issues/detail?id=93">Much
				faster hosted mode</a></b> <br>Hosted mode startup time has improved
		significantly, but, even better, refreshes are now lightning fast
		&mdash; even when your source changes</li>
	<li><b><a
			href="http://code.google.com/p/google-web-toolkit/issues/detail?id=52">New
				HTTP request module</a></b> <br>The HTTP functionality that GWT users
		have been asking for (custom headers, status code, timeouts, and
		more), all wrapped up in an API that's easier to use than the
		JavaScript XMLHttpRequest object</li>
	<li><b><a
			href="http://code.google.com/p/google-web-toolkit/issues/detail?id=10">Widgets
				in TreeItems</a></b> <br>Tree items can now contain arbitrary
		widgets...finally, you can easily create trees with checkboxes :-)</li>
</ul>
</p>

<hr />
<h2 id="Release_Notes_1_1_10">Release Notes for 1.1.10</h2>
<p />
<h3>Fixed Issues</h3>
<ul>
	<li>Normalized behavior of GWT.getModuleBaseURL() with respect to
		hosted mode, web mode, RPC, and automatic resource injection [<a
		href="http://groups.google.com/group/Google-Web-Toolkit/browse_thread/thread/6c2b23e90008b6b9">post
			#1</a>, <a
		href="http://groups.google.com/group/Google-Web-Toolkit/browse_thread/thread/1bb47f2cff671ef0">post
			#2</a>, <a
		href="http://groups.google.com/group/Google-Web-Toolkit/browse_thread/thread/3408c38464c57d4a">post
			#3</a>]
	</li>
	<li>Clarified message in Grid class related to row/column out of
		bounds error [<a
		href="http://groups.google.com/group/Google-Web-Toolkit/browse_thread/thread/e5e130cba766d126">post</a>]
	</li>
	<li>i18nCreator fixed to work with Java 5.0 [<a
		href="http://groups.google.com/group/Google-Web-Toolkit/browse_thread/thread/3b6452a068ebec63">post</a>]
	</li>
	<li>I18NSync (and therefore -i18n scripts) changed to replace dots
		with underscores when generating method names [<a
		href="http://groups.google.com/group/Google-Web-Toolkit/browse_thread/thread/6a4e05beb094a5a2">post</a>]
	</li>
	<li>Additional character escaping in JSON strings [<a
		href="http://groups.google.com/group/Google-Web-Toolkit/browse_thread/thread/5f437d12ba83fff0">post</a>]
	</li>
	<li>Fixed bug calling toString() on nested JSON objects [<a
		href="http://groups.google.com/group/Google-Web-Toolkit/browse_thread/thread/ee81aa5411dece71">post</a>]
	</li>
	<li>Fixed bug that caused the default font size of text in a
		FocusPanel to be zero [<a
		href="http://groups.google.com/group/Google-Web-Toolkit/browse_thread/thread/2f5abf147c5550c4">post</a>]
	</li>
	<li>Fixed TabPanel.insert() with asHTML argument [<a
		href="http://groups.google.com/group/Google-Web-Toolkit/browse_thread/thread/8f427dbe76ce2c49/">post</a>]
	</li>
	<li>Popups and DialogBoxes no longer underlap lists and combos in
		IE6 [<a
		href="http://groups.google.com/group/Google-Web-Toolkit/browse_thread/thread/6815ec29d2c404e2">post</a>]
	</li>
	<li>DialogBoxes can no longer be dragged beoynd the upper left
		corner of the browser window [<a
		href="http://groups.google.com/group/Google-Web-Toolkit/browse_thread/thread/28390f32c42f7940">post</a>]
	</li>
	<li>Buttons inside of FormPanels no longer automatically submit on
		Firefox; this is still a problem some versions of Safari and Opera [<a
		href="http://groups.google.com/group/Google-Web-Toolkit/browse_thread/thread/72bea3a6d4feeaeb">post</a>]
	</li>
	<li>TabPanel now sets the height of the internal DeckPanel to 100%
		to ensure all available space is used [<a
		href="http://groups.google.com/group/Google-Web-Toolkit/browse_thread/thread/4cd09f04bc515696">post</a>]
	</li>
	<li>Fixed bug in Mozilla that was causing
		DialogBox.onKeyPressPreview() to see key as 0 [<a
		href="http://groups.google.com/group/Google-Web-Toolkit/browse_thread/thread/8147b4b219a8fbc7">post</a>]
	</li>
	<li>DockPanel no longer lays out with a DeferredCommand; this
		makes it possible to correctly measure the size of PopupPanel [<a
		href="http://groups.google.com/group/Google-Web-Toolkit/browse_thread/thread/7bbb89e4c97ae1e6">post</a>]
	</li>
	<li>SimplePanel is no longer abstract</li>
	<li>Double click now fires correctly on IE6 [<a
		href="http://groups.google.com/group/Google-Web-Toolkit/browse_thread/thread/2d16242dc7fb830f">post</a>]
	</li>
	<li>Fixed RPC bug that caused deserialization errors or infinite
		loops with self-referential object graphs [<a
		href="http://groups.google.com/group/Google-Web-Toolkit/browse_thread/thread/2a8658e93e2a8de3">post</a>]
	</li>
	<li>Fixed RPC bug that caused deserialization to fail on character
		arrays containing null characters [<a
		href="http://groups.google.com/group/Google-Web-Toolkit/browse_thread/thread/f60f5d5552a3d26b">post</a>]
	</li>
	<li>Serializable classes whose superclass is serialized by a
		custom field serializer are now correctly deserialized on the server</li>
	<li>Fixed bug related to FocusPanel that sometimes manifested
		during RPC async responses [<a
		href="http://groups.google.com/group/Google-Web-Toolkit/browse_thread/thread/edd16ede4f891db8">post</a>]
	</li>
	<li>Fixed bug in JUnit assertEquals() for floating point values
		(delta was not honored correctly) [<a
		href="http://groups.google.com/group/Google-Web-Toolkit/browse_thread/thread/b0e06cc253915b86">post</a>]
	</li>
	<li>Fixed internal compiler errors related to nested local
		subclasses, empty for loop expressions, and no-op unary plus operator.
		[<a
		href="http://groups.google.com/group/Google-Web-Toolkit/browse_thread/thread/d8ecf70acc4e5b0e">post
			#1</a>, <a
		href="http://groups.google.com/group/Google-Web-Toolkit/browse_thread/thread/4da8dcbab9479a80">post
			#2</a>, <a
		href="http://groups.google.com/group/Google-Web-Toolkit/browse_thread/thread/a9f17bf30d0116b">post
			#3</a>]
	</li>
	<li>Fixed infinite loop in Integer.toHexString() [<a
		href="http://groups.google.com/group/Google-Web-Toolkit/browse_thread/thread/3f9c3f4df08fb523">post</a>]
	</li>
	<li>Compiler now handling filesystem symbolic links in project
		structure [<a
		href="http://groups.google.com/group/Google-Web-Toolkit/browse_thread/thread/1597c73888d1acd9">post</a>]
	</li>
	<li>Fixed rare JVM crash in Windows hosted mode related to JSNI
		function pointers</li>
</ul>

<hr />
<h2 id="Release_Notes_1_1_0">Release Notes for 1.1.0 (RC)</h2>
<p />
<h3>New Features</h3>
<ul>
	<li><b><a
			href="doc/html/com.google.gwt.user.client.ui.FileUpload">FileUpload
				widget</a></b><br>The much-requested file upload widget</li>
	<li><b><a
			href="doc/html/com.google.gwt.user.client.ui.FormPanel">FormPanel
				widget</a></b><br>Easily submit traditional HTML forms from GWT apps</li>
	<li><b><a
			href="doc/html/com.google.gwt.doc.DeveloperGuide.RemoteProcedureCalls">RPC
				optimizations</a></b><br>Client and server performance improvements and
		a more compact wire format</li>
	<li><b><a
			href="doc/html/com.google.gwt.doc.DeveloperGuide.Fundamentals.Modules.AutomaticResourceInjection">Automatic
				Resource Injection</a></b><br>Modules can contain references to
		external JavaScript and CSS files, causing them to be automatically
		loaded when the module itself is loaded</li>
	<li><b><a
			href="doc/html/com.google.gwt.doc.DeveloperGuide.Internationalization">Internationalization</a></b><br>Easily
		localize strings and formatted messages</li>
	<li><b><a href="doc/html/com.google.gwt.xml.client">XML
				classes</a></b><br>An XML library based on the W3C DOM</li>
	<li><b><a href="doc/html/com.google.gwt.json.client">JSON
				classes</a></b><br>JSON has moved into <code>gwt-user.jar</code>, and
		it's much faster than the sample that shipped with 1.0.21</li>
	<li><b><a
			href="doc/html/com.google.gwt.doc.DeveloperGuide.JUnitIntegration">JUnit
				enhancements</a></b><br>Unit tests are much, much faster than in
		1.0.21, easier to configure, and you can now test asynchronous things
		like RPCs and timers</li>
	<li><b><a href="doc/javadoc/index">Javadoc</a></b><br>Now
		included for your convenience, with sample code fragments</li>
	<li><b><code>gwt-servlet.jar</code></b><br>Although you
		should continue to build against <code>gwt-user.jar</code> as always,
		you only need to deploy <code>gwt-servlet.jar</code> with your
		webapps; it contains the subset of <code>gwt-user.jar</code> you'll
		need to support RPC</li>
</ul>
There are also a significant number of bug fixes from all the great
feedback we've gotten from the developer forum. Please see the
<a href="#Release_Notes">additional release notes</a>
for detailed information about other important changes in GWT since the
previous release, including a few breaking API changes that we don't
want to catch you off guard.
</p>

<h3>Fixed Issues</h3>
<ul>
	<li><a
		href="http://code.google.com/webtoolkit/issues/4794724.html">Issue
			#4794724 - Servlet container problems due to gwt-user.jar including
			javax.servlet classes</a></li>
	<li><a
		href="http://code.google.com/webtoolkit/issues/1676686.html">Issue
			#1676686 - Hosted mode problems in Windows 2000</a></li>
	<li><a
		href="http://code.google.com/webtoolkit/issues/6606675.html">Issue
			#6606675 - ListBox and Image widgets are missing CSS style names</a></li>
	<li><a
		href="http://code.google.com/webtoolkit/issues/5922226.html">Issue
			#5922226 - Casts from interface types to concrete class types can
			fail</a></li>
	<li><a
		href="http://code.google.com/webtoolkit/issues/4137736.html">Issue
			#4137736 - Referencing an outer local from a field initializer causes
			NullPointerException</a></li>
	<li><a
		href="http://code.google.com/webtoolkit/issues/2518888.html">Issue
			#2518888 - Problem with "return" statements in constructors</a></li>
	<li><a
		href="http://code.google.com/webtoolkit/issues/9984353.html">Issue
			#9984353 - Hosted Mode server throws IllegalArgumentException when
			system is set to non-english locale</a></li>
	<li><a
		href="http://code.google.com/webtoolkit/issues/3733199.html">Issue
			#3733199 - Shrinking Grid via resizeRows() leads to inconsistent
			state</a></li>
	<li><a
		href="http://code.google.com/webtoolkit/issues/7659250.html">Issue
			#7659250 - Hosted mode on default Fedora Core 5 complains of missing
			libstdc++.so.5</a></li>
	<li><a
		href="http://code.google.com/webtoolkit/issues/6531240.html">Issue
			#6531240 - Empty if, while, do, and for statements cause Compiler
			Error</a></li>
	<li><a
		href="http://code.google.com/webtoolkit/issues/4927592.html">Issue
			#4927592 - Multiple initializations in for loop initializer causes
			internal compiler error.</a></li>
</ul>

<p>
	See the <a href="#Appendix">appendix of fixed issues</a> for the
	nitty-gritty list of things that we have fixed in this release,
	including smaller issues.
</p>

<h3>Behavioral Changes</h3>
<p>Important changes in the behavior of existing GWT features.</p>
<h4>Module Source and Public Paths</h4>
<ul>
	<li>In previous versions of GWT, source and public path inclusions
		were based on physical directory structure; only files physically
		located with the module would be included. Going forward, source and
		public path inclusions are based on logical package structure. Once a
		package has been included, any files in that package become visible no
		matter where they are physically located.</li>
</ul>
<h4>JUnit Modules</h4>
<ul>
	<li>GWT test modules (that is, modules intended to run
		GWTTestCase-derived JUnit test cases) no longer need to inherit the <code>com.google.gwt.junit.JUnit</code>
		module. Additionally, it is no longer an error to declare entry points
		within a test module (they will be ignored when running under JUnit).
		Most test cases can now simply use the existing application module,
		which should simplify test case configuration.
	</li>
</ul>

<h3>Breaking API Changes</h3>
Based on user feedback, we've made a few API changes in this release
that may require minor tweaks to your existing code when you upgrade.
Any such changes that affect you should only take a few minutes to
rectify.

<h4>com.google.gwt.user.client.ui.HasWidgets</h4>
<ul>
	<li>We've moved add(), remove(), and clear() into this interface,
		so that any widget that can contain other widgets will be bound to
		this contract.</li>
	<li>The add() method no longer returns a boolean. If a panel
		either cannot add a child widget without extra arguments, or cannot
		accept further widgets, it will throw an exception. This is in keeping
		with the fact that this is usually the result of an error in the code.</li>
	<li>Its iterator is now required to support the remove() method.</li>
</ul>
<h4>com.google.gwt.user.client.ui.Composite</h4>
<ul>
	<li>Composites must now call initWidget() in their constructors,
		rather than setWidget(). This is more indicative of its actual
		purpose, and also serves to clear up any confusion with SimplePanel's
		setWidget() method. Composite.setWidget() is now deprecated.</li>
</ul>
<h4>com.google.gwt.user.client.ui.SimplePanel and subclasses</h4>
<ul>
	<li>We have added setWidget() to SimplePanel, which has more
		appropriate semantics for a panel that can contain only one child. The
		add() method is still present through the HasWidgets interface, but
		will fail if a widget is already present. This change is most likely
		to affect you if you use DialogBox or PopupPanel. To fix it, simply
		change your call to add() to setWidget() instead.</li>
</ul>
<h4>com.google.gwt.user.client.Cookies</h4>
<ul>
	<li>Cookies.getCookie() is now static, as it should have been from
		the beginning. There is no need to instantiate this class now.</li>
	<li>You can now set cookies as well!</li>
</ul>

<h3 id="Appendix">Appendix: Complete List of Fixed Issues</h3>

<p>
	The list of issues below is a short synopsis of all of the major and
	minor issues fixed in this release. See the <a
		href="http://code.google.com/webtoolkit/issues/">online GWT issues
		database</a> for the important common issues.
</p>
<ul>
	<li>String.matches(regex) should exist and doesn't</li>
	<li>Need a way to set individual List items selected/unselected
		(applies to multi-select listboxes)</li>
	<li>DOM needs setBooleanAttribute, getBooleanAttribute.</li>
	<li>HTMLTable.CellFormatter needs getStyleName() to match
		setStyleName().</li>
	<li>FlexTable's internal widget map does not correctly adjust for
		the user inserting rows and cells.<br>
	</li>
	<li>Window.getTitle/setTitle should be static</li>
	<li># characters in filenames cause compilation to fail.</li>
	<li>DynaTable has incorrect HTML</li>
	<li>Change Timer API to use int not long</li>
	<li>In hosted mode JSNI, marshall Java longs as VT_R8</li>
	<li>Popups are not always positioned properly on Safari.</li>
	<li>SWT source inclusion is wrong.</li>
	<li>Safari crashes on exit under some circumstances.</li>
	<li>TreeLogger throws away exception info in console mode.</li>
	<li>Window needs a private ctor</li>
	<li>Phone home version checking should actually compare ordering
		of version number</li>
	<li>Hosted Mode server throws IllegalArgumentException when system
		is set to non-english locale</li>
	<li>Trees have an unsightly 16-pixel left margin.</li>
	<li>SimplePanel.remove() broken.</li>
	<li>ScrollPanel doesn't implement SourcesScrollEvents.</li>
	<li>Make junit-web output to www dir</li>
	<li>Add "hidden" feature to ArgHandler system.</li>
	<li>JUnitShell could hang forever.</li>
	<li>1.5 VM fails to run junit because StackTraceElement 0-arg
		constructor disappeared.</li>
	<li>Panel and ComplexPanel still have methods from old version of
		HasWidgets.</li>
	<li>Nested tables can fire events from the wrong one.</li>
	<li>Make sure JSNI refs to functions can be passed around and used
		as real function pointers.</li>
	<li>AbsolutePanel doesn't position its children consistently.</li>
	<li>JSONParser does not handle generic JSONValues in the encoded
		json string correctly; always assumes its a JSONObject</li>
	<li>Remove -notHeadless from GWTShell (only applies to
		GWTUnitTestShell)</li>
	<li>Number of results returned from split() differs in
		Java/JavaScript (see description)</li>
	<li>Helper scripts don't work for base package.</li>
	<li>Grid fails to update row count when removing.</li>
	<li>JSONString, toString does not enclose its characters in double
		quotes</li>
	<li>Selection issue when removing widgets from TabPanel.</li>
	<li>JSONParser always assumes root type is JSONObject</li>
	<li>ClassSourceFileComposer should not handle Class objects.</li>
	<li>Modules cannot supercede files from inherited modules</li>
	<li>Simple &amp; ComplexPanel shouldn't implement getChildCount(),
		getChild(), etc.</li>
	<li>Negative byte values passed into JavaScript become positive</li>
	<li>The rpc servlet needs a thread-local HttpServletResponse to
		match the thread-local request.</li>
	<li>Appending char to a String behaves incorrectly.</li>
	<li>Remove STL dependency from gwt-ll</li>
	<li>Using xhtml doctype causes popups to be misplaced on Mozilla
		browsers.</li>
	<li>JsniInjector fails to match lines when there are Javadoc
		comments.</li>
	<li>Add whitelist bypass for hosted browser</li>
	<li>PopupPanel example is wrong.</li>
	<li>UIObject needs a title property.</li>
	<li>JSNI methods in local classes don't work in hosted mode.</li>
	<li>HashMap throws a JavaScript error under some circumstances.</li>
	<li>Source and Public module tags should be logical instead of
		physical.</li>
	<li>Document that module source and public tags are now logical
		rather than physical.<br>
	</li>
	<li>Default .launch file fails to use project's full classpath.</li>
	<li>RemoteServiceServlet sends back HTTP 200 OK but no content
		under WebSphere.</li>
	<li>Widget.onLoad() is called too early sometimes.</li>
	<li>Is it really a good idea to have add(Widget) on Panel?</li>
	<li>Web-mode JUnit that reports via RPC</li>
	<li>Tree fires onTreeItemStateChanged twice.</li>
	<li>Make JSON APIs part of gwt-user.jar</li>
	<li>Async JUnit</li>
	<li>String.equalsIgnoreCase(null) throws exception in web mode</li>
	<li>Using a class literal for a pruned typed causes ICE</li>
	<li>Make all built-in implementations of HasWidgets.iterator()
		support remove().</li>
	<li>JSON is slow in Web mode</li>
	<li>StringBuffer uses string concatenation, and is n-squared as a
		result</li>
	<li>Identical Strings can compare false in web mode.</li>
	<li>Tweaks to the property provider environment to support locale
		and improve code uniformity b/w hosted and web mode<br>
	</li>
	<li>Server-side serialization is unusably slow for large data sets</li>
	<li>Client side serialization is unusably slow for large datasets</li>
	<li>Format source for JUnitTestCaseStubGeneratorm,
		ServerSerializationStream</li>
	<li>MethodDispatch not working correctly on IE.</li>
	<li>JavaScriptObject rescuing is incomplete.</li>
	<li>Reduce RPC wire size by not quoting non-strings.</li>
	<li>Cyclic object graphs can be corrupted during deserialization
		on the server</li>
	<li>Test methods that throw checked exceptions cause the generated
		code to fail to compile</li>
	<li>Allow RemoteServiceResponse compression to be controlled by
		subclasses</li>
	<li>Startup timing bug makes RootPanel.get(id) throw an NPE</li>
	<li>Change whitelist/blacklist settings to be command-line
		switches rather than system properties</li>
	<li>KeyCode is always 0 for keypress events on Mozilla.</li>
	<li>Add Panel.remove(int) convenience method.</li>
	<li>File Upload Widget</li>
	<li>PopupPanel needs to deal better with being empty.</li>
	<li>CheckBox.setEnabled() has reversed sense.</li>
	<li>History tokens have problems with URL encoding.</li>
	<li>Loosen restriction on when DockPanel.CENTER child may be
		added.</li>
	<li>AbsolutePanel needs getWidgetLeft() and getWidgetTop().</li>
	<li>Decision: how should FlowPanel behave?</li>
	<li>Samples with composites need to call initWidget() instead of
		the deprecated setWidget().</li>
	<li>RootPanel.get(String) should not be clearing the div's
		contents.</li>
	<li>ListBox, Image, and Hyperlink are missing style names, despite
		doc</li>
	<li>Hyperlink.removeClickListener is broken.</li>
	<li>Don't allow tabs to word-wrap internally on TabPanel</li>
	<li>Turkish locale problem with the RPC generated code - probably
		affects others too</li>
	<li>StackPanel.add() totally screwy.</li>
	<li>Referencing a field that could cause static initialization
		fails to cause a side effect.</li>
</ul>

<hr />
<h2 id="Release_Notes_1_0_21">Release Notes for 1.0.21</h2>
<p />
<h3>Fixed Issues</h3>
<ul>
	<li><a
		href="http://code.google.com/webtoolkit/issues/5823700.html">Issue
			#5823700 - GWT hosted mode does not work when IE 7 is installed</a>
	<li><a
		href="http://code.google.com/webtoolkit/issues/8840603.html">Issue
			#8840603 - GWT applications do not run in IE 7</a>
	<li><a
		href="http://code.google.com/webtoolkit/issues/3844117.html">Issue
			#3844117 - Cannot reference a final local variable from a deep inner
			class</a>
	<li><a
		href="http://code.google.com/webtoolkit/issues/9157420.html">Issue
			#9157420 - Compiler reports errors for non-ASCII characters (UTF-8
			not supported)</a>
	<li><a
		href="http://code.google.com/webtoolkit/issues/1524429.html">Issue
			#1524429 - RPC exception due to empty strings</a>
	<li><a
		href="http://code.google.com/webtoolkit/issues/6000056.html">Issue
			#6000056 - ArrayStoreException initializing multi-dimensional arrays</a>
	<li><a
		href="http://code.google.com/webtoolkit/issues/3490506.html">Issue
			#3490506 - Tree widget remove() bug</a>
	<li><a
		href="http://code.google.com/webtoolkit/issues/8201889.html">Issue
			#8201889 - Generated RPC proxy code fails to pull in String
			intrinsically</a>
	<li><a
		href="http://code.google.com/webtoolkit/issues/1600857.html">Issue
			#1600857 - TabPanel getWidgetIndex() recursion</a>
	<li><a
		href="http://code.google.com/webtoolkit/issues/2705290.html">Issue
			#2705290 - TabPanel widget does not remove tabs properly</a>
	<li><a
		href="http://code.google.com/webtoolkit/issues/9377889.html">Issue
			#9377889 - TabBar method insertTab() not honoring 'asHTML' parameter</a>
	<li><a
		href="http://code.google.com/webtoolkit/issues/4387606.html">Issue
			#4387606 - FlexTable and Grid issues</a>
</ul>
