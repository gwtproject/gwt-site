<p>Often, you will need to integrate GWT with existing handwritten JavaScript or with a third-party JavaScript library. Occasionally you may need to access low-level browser
functionality not exposed by the GWT class API's. The JavaScript Native Interface (JSNI) feature of GWT can
solve both of these problems by allowing you to integrate JavaScript directly into your application's Java source code.</p>

<p>The <a href="DevGuideCompilingAndDebugging.html#DevGuideJavaToJavaScriptCompiler">GWT compiler</a> translates Java source into JavaScript. Sometimes it's very useful to mix handwritten
JavaScript into your Java source code. For example, the lowest-level functionality of certain core GWT classes are handwritten in JavaScript. GWT borrows from the Java Native
Interface (JNI) concept to implement JavaScript Native Interface (JSNI). Writing JSNI methods is a powerful technique, but should be used sparingly because writing bulletproof
JavaScript code is notoriously tricky. JSNI code is potentially less portable across browsers, more likely to leak memory, less amenable to Java tools, and harder for the compiler
to optimize.</p>

<p>We think of JSNI as the web equivalent of inline assembly code. You can use it in many ways:</p>

<ul>
<li>Implement a Java method directly in JavaScript</li>

<li>Wrap type-safe Java method signatures around existing JavaScript</li>

<li>Call from JavaScript code into Java code and vice-versa</li>

<li>Throw exceptions across Java/JavaScript boundaries</li>

<li>Read and write Java fields from JavaScript</li>

<li>Use development mode to debug both Java source (with a Java debugger) and JavaScript (with a script debugger)</li>
</ul>

<br>

<ol class="toc" id="pageToc">
  <li><a href="#writing">Writing Native JavaScript Methods</a></li>
  <li><a href="#methods-fields">Accessing Java Methods and Fields from JavaScript</a></li>
  <li><a href="#calling">Calling a Java Method from Handwritten JavaScript</a></li>
  <li><a href="#sharing">Sharing objects between Java source and JavaScript</a></li>
  <li><a href="#passing-java">Passing Java values into JavaScript</a></li>
  <li><a href="#passing-javascript">Passing JavaScript values into Java code</a></li>
  <li><a href="#important">Important Notes</a></li>
  <li><a href="#exceptions">Exceptions and JSNI</a></li>
</ol>

<h2 id="writing">Writing Native JavaScript Methods</h2>

<p>JSNI methods are declared <tt>native</tt> and contain JavaScript code in a specially formatted comment block between the end of the parameter list and the trailing semicolon. A
JSNI comment block begins with the exact token <tt>/*-{</tt> and ends with the exact token <tt>}-*/</tt>. JSNI methods are called just like any normal Java method. They can be
static or instance methods.</p>

<p>The JSNI syntax is a directive to the Java-to-JavaScript Compiler to accept any text between the comment statements as valid JS code and inject it inline in the generated GWT
files. At compile time, the GWT compiler performs some syntax checks on the JavaScript inside the method, then generates interface code for converting method arguments and return
values properly.</p>

<p>As of the GWT 1.5 release, the Java varargs construct is supported. The GWT compiler will translate varargs calls between 2 pieces of Java code. However, calling a varargs
JavaScript method from Java will result in the callee receiving the arguments in an array.</p>

<h4 id="examples-writing">Examples</h4>

<p>Here is a simple example of how to code a JSNI method that puts up a JavaScript alert dialog:</p>

<pre class="prettyprint">
public static native void alert(String msg) /*-{
  $wnd.alert(msg);
}-*/;
</pre>

<p>Note that the code did not reference the JavaScript <tt>window</tt> object directly inside the method. When accessing the browser's window and document objects from JSNI, you
must reference them as <tt>$wnd</tt> and <tt>$doc</tt>, respectively. Your compiled script runs in a nested frame, and <tt>$wnd</tt> and <tt>$doc</tt> are automatically
initialized to correctly refer to the host page's window and document.</p>

<p>Here is another example with a problem:</p>

<pre class="prettyprint">
public static native int badExample() /*-{
  return &quot;Not A Number&quot;;
}-*/;

 public void onClick () {
   try {
      int myValue = badExample();
      GWT.log(&quot;Got value &quot; + myValue, null);
   } catch (Exception e) {
      GWT.log(&quot;JSNI method badExample() threw an exception:&quot;, e);
   }
 }
</pre>

<p>This example compiles as Java, and its syntax is verified by the GWT compiler
as valid JavaScript. But when you run the example code in <a
href="DevGuideCompilingAndDebugging.html#DevGuideDevMode">development mode</a>,
it returns an exception. Click on the line in the log window to display the exception in the message
area below:</p>

<pre>
com.google.gwt.dev.shell.HostedModeException: invokeNativeInteger(@com.example.client.GWTObjectNotifyTest::badExample()): JS value of type string, expected int
    at com.google.gwt.dev.shell.JsValueGlue.getIntRange(JsValueGlue.java:343)
    at com.google.gwt.dev.shell.JsValueGlue.get(JsValueGlue.java:179)
    at com.google.gwt.dev.shell.ModuleSpace.invokeNativeInt(ModuleSpace.java:233)
    at com.google.gwt.dev.shell.JavaScriptHost.invokeNativeInt(JavaScriptHost.java:97)
    at com.example.client.GWTObjectNotifyTest.badExample(GWTObjectNotifyTest.java:29)
    at com.example.client.GWTObjectNotifyTest$1.onClick(GWTObjectNotifyTest.java:52)
    ...
</pre>

<p>In this case, neither the Java IDE nor the GWT compiler could tell that there was a type mismatch between the code inside the JSNI method and the Java declaration. The GWT
generated interface code caught the problem at runtime in development mode. When
running in <a
href="DevGuideCompilingAndDebugging.html#DevGuideProdMode">production mode</a>, you will not see an
exception. JavaScript's dynamic typing obscures this kind of problem.</p>

<blockquote><i>Tip: Since JSNI code is just regular JavaScript, you will not be
able to use Java debugging tools inside your JSNI methods when running in
development mode. However, you
can set a breakpoint on the source line containing the opening brace of a JSNI method, allowing you to see invocation arguments. Also, the Java compiler and GWT compiler do not
perform any syntax or semantic checks on JSNI code, so any errors in the JavaScript body of the method will not be seen until run time.</i></blockquote>

<h2 id="methods-fields">Accessing Java Methods and Fields from JavaScript</h2>

<p>It can be very useful to manipulate Java objects from within the JavaScript implementation of a JSNI method. However, since JavaScript uses dynamic typing and Java uses static
typing, you must use a special syntax.</p>

<blockquote><i>When writing JSNI code, it is helpful to occasionally run in <a
href="DevGuideCompilingAndDebugging.html#DevGuideProdMode">production mode</a>. The <a href="DevGuideCompilingAndDebugging.html#DevGuideJavaToJavaScriptCompiler">JavaScript compiler</a> checks your JSNI code and can flag errors at compile time that you would not catch until
runtime in <a
href="DevGuideCompilingAndDebugging.html#DevGuideDevMode">development mode</a>.</i></blockquote>

<h3 id="methods">Invoking Java methods from JavaScript</h3>

<p>Calling Java methods from JavaScript is somewhat similar to calling Java methods from C code in <a href="http://download.oracle.com/javase/1.5.0/docs/guide/jni/index.html">JNI</a>. In particular, JSNI borrows the JNI mangled method signature approach to distinguish among overloaded methods. JavaScript calls into Java methods are of
the following form:</p>


<pre>
[instance-expr.]@class-name::method-name(param-signature)(arguments)
</pre>


<ul>
<li><strong>instance-expr.</strong> : must be present when calling an instance method and must be absent when calling a static method</li>

<li><strong>class-name</strong> : is the fully-qualified name of the class in which the method is declared (or a subclass thereof)</li>

<li><strong>param-signature</strong> : is the internal Java method signature as specified at <a href="http://download.oracle.com/javase/1.5.0/docs/guide/jni/spec/types.html#wp16432">JNI Type Signatures</a> but without the trailing signature of the method return type since it is not needed to choose the overload</li>

<li><strong>arguments</strong> : is the actual argument list to pass to the called method</li>
</ul>

<h3 id="constructors">Invoking Java constructors from JavaScript</h3>

<p>Calling Java constructors from JavaScript is identical to the above use case, except that the method name is alway <strong>new</strong>.</p>

<p>Given the following Java classes:</p>

<pre class="prettyprint">
package pkg;
class TopLevel {
  public TopLevel() { ... }
  public TopLevel(int i) { ... }

  static class StaticInner {
    public StaticInner() { ... }
  }

  class InstanceInner {
    public InstanceInner(int i) { ... }
  }
}
</pre>

<p>We compare the Java expression versus the JSNI expression:</p>

<ul>
<li><tt>new TopLevel()</tt> becomes <tt>@pkg.TopLevel::new()()</tt></li>

<li><tt>new StaticInner()</tt> becomes <tt>@pkg.TopLevel.StaticInner::new()()</tt></li>

<li><tt>someTopLevelInstance.new InstanceInner(123)</tt> becomes <tt>@pkg.TopLevel.InstanceInner::new(Lpkg/TopLevel;I)(someTopLevelInstance, 123)</tt></li>

<li style="list-style: none">
<ul>
<li>The enclosing instance of a non-static class is implicitly defined as the first parameter for constructors of a non-static class. Regardless of how deeply-nested a non-static
class is, it only needs a reference to an instance of its immediately-enclosing type.</li>
</ul>
</li>
</ul>

<h3 id="fields">Accessing Java fields from JavaScript</h3>

<p>Static and instance fields can be accessed from handwritten JavaScript. Field references are of the form</p>


<pre>
[instance-expr.]@class-name::field-name
</pre>



<h4 id="example-fields">Example</h4>

<p>Here's an example of accessing static and instance fields from JSNI.</p>

<pre class="prettyprint">
public class JSNIExample {

  String myInstanceField;
  static int myStaticField;

  void instanceFoo(String s) {
    // use s
  }

  static void staticFoo(String s) {
    // use s
  }

  public native void bar(JSNIExample x, String s) /*-{
    // Call instance method instanceFoo() on this
    this.@com.google.gwt.examples.JSNIExample::instanceFoo(Ljava/lang/String;)(s);

    // Call instance method instanceFoo() on x
    x.@com.google.gwt.examples.JSNIExample::instanceFoo(Ljava/lang/String;)(s);

    // Call static method staticFoo()
    @com.google.gwt.examples.JSNIExample::staticFoo(Ljava/lang/String;)(s);

    // Read instance field on this
    var val = this.@com.google.gwt.examples.JSNIExample::myInstanceField;

    // Write instance field on x
    x.@com.google.gwt.examples.JSNIExample::myInstanceField = val + &quot; and stuff&quot;;

    // Read static field (no qualifier)
    @com.google.gwt.examples.JSNIExample::myStaticField = val + &quot; and stuff&quot;;
  }-*/;

}
</pre>

<blockquote><i>Tip: As of the GWT 1.5 release, the Java varargs construct is supported. The GWT compiler will translate varargs calls between two pieces of Java code, however,
calling a varargs Java method from JSNI will require the JavaScript caller to pass an array of the appropriate type.</i></blockquote>

<h2 id="calling">Calling a Java Method from Handwritten JavaScript</h2>

<p>Sometimes you need to access a method or constructor defined in GWT from outside JavaScript code. This code might be hand-written and included in another java script file, or
it could be a part of a third party library. In this case, the GWT compiler will not get a chance to build an interface between your JavaScript code and the GWT generated
JavaScript directly.</p>

<p>A way to make this kind of relationship work is to assign the method via JSNI to an external, globally visible JavaScript name that can be referenced by your hand-crafted
JavaScript code.</p>

<pre class="prettyprint">
package mypackage;

public MyUtilityClass
{
    public static int computeLoanInterest(int amt, float interestRate,
                                          int term) { ... }
    public static native void exportStaticMethod() /*-{
       $wnd.computeLoanInterest =
          $entry(@mypackage.MyUtilityClass::computeLoanInterest(IFI));
    }-*/;
}
</pre>

<p>Notice that the reference to the exported method has been wrapped in a call to the <tt>$entry</tt> function. This implicitly-defined function ensures that the Java-derived method is executed with the uncaught exception handler installed and pumps a number of other utility services.  The <tt>$entry</tt> function is reentrant-safe and should be used anywhere that GWT-derived JavaScript may be called into from a non-GWT context.</p>

<p>On application initialization, call <tt>MyUtilityClass.exportStaticMethod()</tt> (e.g. from your GWT Entry Point). This will assign the function to a variable in the window
object called <tt>computeLoanInterest</tt>.</p>


<h2 id="sharing">Sharing objects between Java source and JavaScript</h2>

<p>Parameters and return types in JSNI methods are declared as Java types. There are very specific rules for how values passing in and out of JavaScript code must be treated.
These rules must be followed whether the values enter and leave through normal Java method call semantics or through the special syntax by which Java methods are invoked from JSNI code.</p>

<h2 id="passing-java">Passing Java values into JavaScript</h2>

<table>
<tr>
<th width="30%">Incoming Java type</th>
<th>How it appears to JavaScript code</th>
</tr>

<tr>
<td><code>String</code> </td>
<td>JavaScript string, as in <code>var s = &quot;my string&quot;;</code> </td>
</tr>

<tr>
<td><code>boolean</code> </td>
<td>JavaScript boolean value, as in <code>var b = true;</code> </td>
</tr>

<tr>
<td><code>long</code> </td>
<td>disallowed (see notes)</td>
</tr>

<tr>
<td>other numeric primitives</td>
<td>JavaScript numeric value, as in <code>var x = 42;</code> </td>
</tr>

<tr>
<td><a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/core/client/JavaScriptObject.html">JavaScriptObject</a> </td>
<td><code>JavaScriptObject</code> that must have originated from JavaScript code, typically as the return value of some other JSNI method
(see notes)</td>
</tr>

<tr>
<td>Java array</td>
<td>opaque value that can only be passed back into Java code</td>
</tr>

<tr>
<td>any other Java <code>Object</code> </td>
<td>opaque value accessible through special syntax</td>
</tr>
</table>



<h2 id="passing-javascript">Passing JavaScript values into Java code</h2>

<table>
<tr>
<th width="30%">Outgoing Java type</th>
<th>What must be passed</th>
</tr>

<tr>
<td><code>String</code> </td>
<td>JavaScript string, as in <code>return &quot;boo&quot;;</code> </td>
</tr>

<tr>
<td><code>boolean</code> </td>
<td>JavaScript boolean value, as in <code>return false;</code> </td>
</tr>

<tr>
<td><code>long</code> </td>
<td>disallowed (see notes)</td>
</tr>

<tr>
<td>Java numeric primitive</td>
<td>JavaScript numeric value, as in <code>return 19;</code> </td>
</tr>

<tr>
<td><a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/core/client/JavaScriptObject.html">JavaScriptObject</a> </td>
<td>native JavaScript object, as in <code>return document.createElement(&quot;div&quot;)</code> (see notes)</td>
</tr>

<tr>
<td>any other Java <code>Object</code> (including arrays)</td>
<td>Java <code>Object</code> of the correct type that must have originated in Java code; Java objects cannot be constructed from &quot;thin
air&quot; in JavaScript</td>
</tr>
</table>



<h2 id="important">Important Notes</h2>

<ul>
<li>The Java <tt>long</tt> type cannot be represented in JavaScript as a numeric type, so GWT emulates it using an opaque data structure. This means that JSNI methods cannot
process a <tt>long</tt> as a numeric type. The compiler therefore disallows, by default, directly accessing a <tt>long</tt> from JSNI: JSNI methods cannot have <tt>long</tt> as a
parameter type or a return type, and they cannot access a <tt>long</tt> using a <a href="DevGuideCodingBasics.html#DevGuideJavaScriptNativeInterface">JSNI reference</a>. If you find
yourself wanting to pass a <tt>long</tt> into or out of a JSNI method, here are some options:</li>

<li style="list-style: none">
<ol>
<li>For numbers that fit into type <tt>double</tt>, use type <tt>double</tt> instead of type <tt>long</tt>.</li>

<li>For computations that require the full <tt>long</tt> semantics, rearrange the code so that the computations happen in Java instead of in JavaScript. That way they will use the
<tt>long</tt> emulation.</li>

<li>For values meant to be passed through unchanged to Java code, wrap the value in a <tt>Long</tt>. There are no restrictions on type <tt>Long</tt> with JSNI methods.</li>

<li>If you are sure you know what you are doing, you can add the annotation <tt>com.google.gwt.core.client.UnsafeNativeLong</tt> to the method. The compiler will then allow you to
pass a <tt>long</tt> into and out of JavaScript. It will still be an opaque data type, however, so the only thing you will be able to do with it will be to pass it back to
Java.</li>
</ol>
</li>
</ul>

<ul>
<li>Violating any of these marshaling rules in <a
href="DevGuideCompilingAndDebugging.html#DevGuideDevMode">development mode</a> will generate a
<tt>com.google.gwt.dev.shell.HostedModeException</tt> detailing the problem. This exception is not <a href="DevGuideCodingBasics.html#DevGuideClientSide">translatable</a>
and never thrown in <a
href="DevGuideCompilingAndDebugging.html#DevGuideProdMode">production mode</a>.</li>
</ul>

<ul>
<li><a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/core/client/JavaScriptObject.html">JavaScriptObject</a> gets special treatment
from the GWT compiler and development mode. Its purpose is to provide an opaque representation of native JavaScript objects to Java code.</li>
</ul>

<p/>

<ul>
<li>Although Java arrays are not directly usable in JavaScript, there are some helper classes that efficiently achieve a similar effect: <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/core/client/JsArray.html">JsArray</a>, <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/core/client/JsArrayBoolean.html">JsArrayBoolean</a>, <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/core/client/JsArrayInteger.html">JsArrayInteger</a>, <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/core/client/JsArrayNumber.html">JsArrayNumber</a>, and <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/core/client/JsArrayString.html">JsArrayString</a>. These classes are wrappers around a native JavaScript array.</li>
</ul>



<ul>
<li>Java <tt>null</tt> and JavaScript <tt>null</tt> are identical and always legal values for any non-primitive Java type. JavaScript <tt>undefined</tt> is also considered equal
to <tt>null</tt> when passed into Java code (the rules of JavaScript dictate that in JavaScript code, <tt>null == undefined</tt> is <tt>true</tt> but <tt>null === undefined</tt>
is <tt>false</tt>). In previous versions of GWT, <tt>undefined</tt> was not a legal value to pass into Java.</li>
</ul>

<h2 id="exceptions">Exceptions and JSNI</h2>

<p>An exception can be thrown during the execution of either normal Java code or the JavaScript code within a JSNI method. When an exception generated within a JSNI method
propagates up the call stack and is caught by a Java catch block, the thrown JavaScript exception is wrapped as a <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/core/client/JavaScriptException.html">JavaScriptException</a> object at the time it is
caught. This wrapper object contains only the class name and description of the JavaScript exception that occurred. The recommended practice is to handle JavaScript exceptions in
JavaScript code and Java exceptions in Java code.</p>

<p>A Java exception can safely retain identity while propagating through a JSNI method.</p>

<p>For example,</p>

<ol>
<li>Java method <tt>doFoo()</tt> calls JSNI method <tt>nativeFoo()</tt></li>

<li><tt>nativeFoo()</tt> internally calls Java method <tt>fooImpl()</tt></li>

<li><tt>fooImpl()</tt> throws an exception</li>
</ol>

<p>The exception thrown from <tt>fooImpl()</tt> will propagate through <tt>nativeFoo()</tt> and can be caught in <tt>doFoo()</tt>. The exception will retain its type and
identity.</p>


