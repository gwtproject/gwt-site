<ol class="toc" id="pageToc">
  <li><a href="#language">Language support</a></li>
  <li><a href="#runtime">Runtime library support</a></li>
  <li><a href="#differences">Differences between JRE and emulated classes</a></li>
  <li><a href="#similar">Classes that provide similar functionality</a></li>
</ol>


<h2 id="language">Language support</h2>

<p>GWT supports most of the core Java language syntax and semantics, but there are a few differences you will want to be aware of.</p>

<blockquote>Note: As of GWT 1.5, GWT compiles the Java language syntax that is compatible with J2SE 1.5 or earlier. Versions of GWT prior to GWT 1.5 are limited to Java 1.4 source
compatibility. For example, GWT 2.0 supports generics, whereas GWT 1.4 does not.</blockquote>

<p>It is important to remember that the target language of your GWT application
is ultimately JavaScript, so there are some differences between running your
application in <a
href="DevGuideCompilingAndDebugging.html#DevGuideDevMode">development mode</a>
and <a href="DevGuideCompilingAndDebugging.html#DevGuideProdMode">production
mode</a> (previously known as <i>hosted mode</i> and <i>web mode</i>,
respectively):</p>

<ul>
<li><strong>Intrinsic types</strong>: Primitive types (<tt>boolean</tt>, <tt>byte</tt>, <tt>char</tt>, <tt>short</tt>, <tt>int</tt>, <tt>long</tt>, <tt>float</tt>, and
<tt>double</tt>), <tt>Object</tt>, <tt>String</tt>, arrays, user-defined classes, etc. are all supported, with a couple of caveats.</li>

<li style="list-style: none">
<ul>
<li><strong>Arithmetic</strong>: In JavaScript, the only available numeric type is a 64-bit floating point value. All Java primitive numeric types (except for long, see below),
are therefore implemented in production mode as if on doubles. Primarily, that means overflowing an integral data type (<tt>byte</tt>, <tt>char</tt>, <tt>short</tt>, <tt>int</tt>) will
not wrap the underlying value as Java specifies. Instead, the resulting value will outside of the legal range for that data type. Operations on <tt>float</tt> are performed as
<tt>double</tt> and will result in more-than-expected precision. Integer division is implemented to explicitly round to the correct value.</li>

<li><strong><tt>long</tt></strong>: JavaScript has no 64-bit integral type, so <tt>long</tt> needs special consideration. Prior to GWT 1.5, the <tt>long</tt> type was was simply
mapped to the integral range of a 64-bit JavaScript <i>floating-point</i> value, giving <tt>long</tt> variables an actual range less than the full 64 bits. As of GWT 1.5,
<tt>long</tt> primitives are emulated as a pair of 32-bit integers, and work reliably over the entire 64-bit range. Overflow is emulated to match the expected behavior. There are
a couple of caveats. Heavy use of <tt>long</tt> operations will have a performance impact due to the underlying emulation. Additionally, long primitives cannot be used in <a href="DevGuideCodingBasics.html#DevGuideJavaScriptNativeInterface">JSNI code</a> because they are not a native JavaScript numeric type.</li>
</ul>
</li>
</ul>

<ul>
<li><strong>Exceptions</strong>: <tt>try</tt>, <tt>catch</tt>, <tt>finally</tt> and user-defined exceptions are supported as normal, although <tt>Throwable.getStackTrace()</tt> is
not meaningfully supported in production mode.</li>
</ul>

<blockquote>Note: Several fundamental exceptions implicitly produced by the Java VM, most notably <tt>NullPointerException</tt>, <tt>StackOverflowError</tt>, and
<tt>OutOfMemoryError</tt>, do not occur in production mode as such. Instead, a <tt>JavaScriptException</tt> is produced for any implicitly generated exceptions. This is because the
nature of the underlying JavaScript exception cannot be reliably mapped onto the appropriate Java exception type.</blockquote>

<p/>

<ul>
<li><strong>Assertions</strong>: <tt>assert</tt> statements are always active in
development mode because it's a great way for GWT libraries to provide lots of
helpful error checking while you're debugging. The GWT compiler removes and
ignores all assertions by default, but you can enable them in production mode by
specifying the <tt>-ea</tt> flag to <tt>Compiler</tt>.</li>
</ul>



<ul>
<li><strong>Multithreading and Synchronization</strong>: JavaScript interpreters are single-threaded, so while GWT silently accepts the <tt>synchronized</tt> keyword, it has no
real effect. Synchronization-related library methods are not available, including <tt>Object.wait()</tt>, <tt>Object.notify()</tt>, and <tt>Object.notifyAll().</tt> The compiler
will ignore the synchronized keyword but will refuse to compile your code if the <tt>Object</tt>'s related synchronization methods are invoked.</li>
</ul>

<ul>
<li><strong>Reflection</strong>: For maximum efficiency, GWT compiles your Java source into a monolithic script, and does not support subsequent dynamic loading of classes. This
and other optimizations preclude general support for reflection. However, it is possible to query an object for its class name using Object.getClass().<a href="http://java.sun.com/j2se/1.5.0/docs/api/java/lang/Class.html#getName()">getName()</a>.</li>
</ul>

<ul>
<li><strong>Finalization</strong>: JavaScript does not support object
finalization during garbage collection, so GWT is not able to be honor Java
finalizers in production mode.</li>
</ul>

<ul>
<li><strong>Strict Floating-Point</strong>: The Java language specification precisely defines floating-point support, including single-precision and double-precision numbers as
well as the <tt>strictfp</tt> keyword. GWT does not support the <tt>strictfp</tt> keyword and can not ensure any particular degree of floating-point precision in translated code,
so you may want to avoid calculations in client-side code that require a guaranteed level of floating-point precision.</li>
</ul>

<h2 id="runtime">Runtime library support</h2>

<p>GWT supports only a small subset of the classes available in the Java 2 Standard and Enterprise Edition libraries, as these libraries are quite large and rely on functionality
that is unavailable within web browsers. To find out exactly which classes and methods are supported for core Java runtime packages, see the GWT <a href="RefJreEmulation.html">JRE Emulation Reference</a>.</p>

<blockquote><i>Tip: You will save yourself a lot of frustration if you make sure that you use only <a href="DevGuideCompilingAndDebugging.html#DevGuideJavaToJavaScriptCompiler">translatable</a> classes in your <a href="DevGuideCodingBasics.html#DevGuideClientSide">client-side code</a> from the
very beginning. To help you identify problems early, your code is checked
against the JRE emulation library whenever you run in <a
href="DevGuideCompilingAndDebugging.html#DevGuideDevMode">development mode</a>. As a result, most uses of unsupported libraries will be caught the first time you attempt to run your
application. So,</i> run early and often<i>.</i></blockquote>

<h2 id="differences">Differences between JRE and emulated classes</h2>

<p>Some specific areas in which GWT emulation differs from the standard Java runtime:</p>

<ul>
<li><strong>Regular Expressions</strong>: The syntax of <a href="http://java.sun.com/j2se/1.5.0/docs/api/java/util/regex/Pattern.html">Java regular expressions</a>
is similar, but not identical, to <a href="http://developer.mozilla.org/en/docs/Core_JavaScript_1.5_Guide:Regular_Expressions">JavaScript regular expressions</a>.
For example, the <a href="http://java.sun.com/j2se/1.5.0/docs/api/java/lang/String.html#replaceAll(java.lang.String,%20java.lang.String)">replaceAll</a> and <a href="http://java.sun.com/j2se/1.5.0/docs/api/java/lang/String.html#split(java.lang.String)">split</a> methods use regular expressions. So, you will probably want
to be careful to only use Java regular expressions that have the same meaning in JavaScript.</li>
</ul>

<ul>
<li><strong>Serialization</strong>: Java serialization relies on a few mechanisms that are not available in compiled JavaScript, such as dynamic class loading and reflection. As a
result, GWT does not support standard Java serialization. Instead, GWT has an <a href="DevGuideServerCommunication.html#DevGuideRemoteProcedureCalls">RPC</a> facility that provides
automatic object serialization to and from the server for the purpose of invoking remote methods.</li>
</ul>

<blockquote><i>Note: For a list of JRE classes that GWT can translate out of the box, see the GWT <a href="RefJreEmulation.html">JRE Emulation
Reference</a>.</i></blockquote>

<h2 id="similar">Classes that provide similar functionality</h2>

<p>In some classes, the functionality of the class is too expensive to be emulated entirely, so a similar routine in another package is provided instead. Here are some commonly
used routines that provide a subset of the native JRE functionality:</p>

<ul>
<li><a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/i18n/client/DateTimeFormat.html">com.google.gwt.i18n.client.DateTimeFormat</a> : Supports a subset of <tt>java.util.DateTimeFormat</tt>. See examples in the <a href="DevGuideCodingBasics.html#DevGuideDateAndNumberFormat">date and number formatting</a> section.</li>

<li><a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/i18n/client/NumberFormat.html">com.google.gwt.i18n.client.NumberFormat</a> : Supports a subset of <tt>java.util.NumberFormat</tt>. See examples in the <a href="DevGuideCodingBasics.html#DevGuideDateAndNumberFormat">date and number format</a> section.</li>

<li><a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/user/client/rpc/IsSerializable.html">com.google.gwt.user.client.rpc</a>
: A marker class used similarly to <tt>java.io.Serializable</tt> for GWT RPC.</li>

<li><a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/user/client/Timer.html" >com.google.gwt.user.client.Timer</a> : A
simplified, browser-safe timer class. This class serves the same purpose as <tt>java.util.Timer</tt>, but is simplified because of the single-threaded environment.</li>
</ul>

