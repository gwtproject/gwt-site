<ol class="toc" id="pageToc"><li><a href="#Project_Architecture">Project Architecture</a><ol class="toc"><li><a href="#What_is_a_GWT_Module?">What is a GWT Module?</a></li></ol></li><li><a href="#Writing_Java_code">Writing Java code</a><ol class="toc"><li><a href="#How_do_I_enable_assertions?">How do I enable assertions?</a></li></ol></li><li><a href="#JavaScript_Native_Interface">JavaScript Native Interface</a><ol class="toc"><li><a href="#How_do_I_call_Java_methods_from_handwritten_JavaScript_or_third">How do I call Java methods
from handwritten JavaScript or third party libraries?</a></li><li><a href="#Help!_I'm_having_problems_with_eval()_in_my_JSNI_method!">Help! I'm having problems with eval() in my
JSNI method!</a></li><li><a href="#Why_doesn't_the_bridge_call_to_my_JSNI_method_work_in_onclick">Why doesn't the bridge call to my JSNI method work in <code>&lt;some_obj&gt;.onclick</code>?<li><a href="#How_do_I_pass_a_Java_method_as_a_callback_function?">How do I pass a Java method as a callback
function?</a></li></ol></li><li><a href="#Deferred_Binding">Deferred Binding</a><ol class="toc"><li><a href="#What_is_Deferred_Binding?">What is Deferred Binding?</a></li></ol></li></ol><div id="FAQ_Client"/>


<h2 id="Project_Architecture">Project Architecture</h2>

<p/>

<h3 id="What_is_a_GWT_Module?">What is a GWT Module?</h3>

<p>A GWT module is simply an encapsulation of functionality. It shares some similarities with a Java package but is not the same thing. 
</p>

<p>A GWT module is named similarly to a Java package in that it follows the usual dotted-path naming convention. For example, most of the standard GWT modules are located
underneath &quot;com.google.gwt&quot; However, the similarity between GWT modules and Java packages ends with this naming convention.</p>

<p>A module is defined by an XML descriptor file ending with the extension &quot;.gwt.xml&quot;, and the name of that file determines the name of the module. For example, if you have a file
named <tt>src/com/mycompany/apps/MyApplication.gwt.xml</tt>, then that will create a GWT module named <tt>com.mycompany.apps.MyApplication</tt>. The contents of the
<tt>.gwt.xml</tt> file specify the precise list of Java classes and other resources that are included in the GWT module. 
</p>

<h2 id="Writing_Java_code">Writing Java code</h2>

<p/>

<h3 id="How_do_I_enable_assertions?">How do I enable assertions?</h3>

<p>In order to use the assert() feature in Java, you must enable them with the <tt>-enableassertions</tt> or <tt>-ea</tt> command line argument. See the article on the Java
website <a href="http://java.sun.com/j2se/1.4.2/docs/guide/lang/assert.html">Programming with Assertions</a> for more details. If you are using an IDE, add this
argument to the VM argument list.</p>

<p>The GWT compiler also recognizes the <tt>-ea</tt> flag to generate code for assertions in the compiled JavaScript.</p>

<p>Only use assertions for debugging purposes, not production logic because
assertions will only work under GWT's development mode. By default, they are compiled away by the GWT
compiler so do not have any effect in production mode unless you explicitly enable them.</p>

<h2 id="JavaScript_Native_Interface">JavaScript Native Interface</h2>

<p/>

<h3 id="How_do_I_call_Java_methods_from_handwritten_JavaScript_or_third">How do I call Java methods
from handwritten JavaScript or third party libraries?</h3>

<p>You can get this to work by assigning the method via JSNI to an external, globally visible JavaScript name that can be referenced by your hand-crafted JavaScript code. For example,</p>

<pre class="prettyprint">
package mypackage;
public MyUtilityClass
{
    public static int computeLoanInterest(int amt, float interestRate, int term) { ... }
    public static native void exportStaticMethod() /*-{
       $wnd.computeLoanInterest = 
         $entry(@mypackage.MyUtilityClass::computeLoanInterest(IFI));
    }-*/;
}
</pre>
<p>Notice that the reference to the exported method has been wrapped in a call to the $entry function. This implicitly-defined function ensures that the Java-derived method is executed with the uncaught exception handler installed and pumps a number of other utility services. The $entry function is reentrant-safe and should be used anywhere that GWT-derived JavaScript may be called into from a non-GWT context.</p>
<p>On application initialization, call <tt>MyUtilityClass.exportStaticMethod()</tt> (e.g. from your GWT Entry Point). This will create a function on the window object called
<tt>computeLoanInterest()</tt> which will invoke, via JSNI, the compiled Java method of the same name. The bridge method is needed because the GWT compiler will
obfuscate/compress/rename the names of Java methods when it translates them to JavaScript.</p>

<p/>

<h3 id="Help!_I'm_having_problems_with_eval()_in_my_JSNI_method!">Help! I'm having problems with eval() in my
JSNI method!</h3>

<p>Occasionally you might want to use this idiom for a GWT class method:</p>

<pre class="prettyprint">
public static native String myMethod(String arg) /*-{
    eval(&quot;var myVar = 'arg is ' + arg;&quot;);
    return myVar;
}-*/;
</pre>

<p>The code above will work in development mode, but not in production mode. The reason is that when GWT compiles the Java source code to JavaScript, it obfuscates variable names. In this
case, it will change the name of the <tt>arg</tt> variable. However, GWT can't see into JavaScript string literals in a JSNI method, and so it can't update the corresponding
embedded references to <tt>arg</tt> to also use the new varname.</p>

<p>The fix is to tweak the <tt>eval()</tt> statement so that the variable names are visible to the GWT compiler:</p>

<pre class="prettyprint">
public static native String myMethod(String arg) /*-{
    eval(&quot;var myVar = 'arg is &quot; + arg + &quot;';&quot;);
    return myVar;
}-*/;
</pre>

<p/>

<h3 id="Why_doesn't_the_bridge_call_to_my_JSNI_method_work_in_onclick">Why doesn't the bridge call to my JSNI
method work in <code>&lt;some_obj&gt;.onclick</code>?</h3>

<p>While there are many reasons why your bridge call may not seem to be called in <tt>onclick</tt>, one of the most common causes has to do with function closures and how
JavaScript variables are stored.</p>

<p>The code below illustrates this point:</p>

<pre class="prettyprint">
public native void doSomething() /*-{
    this.@com.company.app.client.MyClass::doSomethingElse(Ljava/lang/String;)(&quot;immediate&quot;);
    someObj.onclick = function() {
        this.@com.company.app.client.MyClass::doSomethingElse(Ljava/lang/String;)(&quot;on click&quot;);
    }
}-*/;

public void doSomethingElse(String foo) {
    Window.alert(foo);
}
</pre>

<p>Someone new to JavaScript looking at this code may expect the &quot;immediate&quot; alert to display as soon as the code runs, and the &quot;on click&quot; alert to pop up when <tt>someObj</tt> is
clicked. However, what actually will happen is that the first alert will display, but never the &quot;on click.&quot; This problem occurs because &quot;<tt>this.@com...</tt>&quot; idioms create
function closures over <tt>this</tt>. However, in JavaScript function closures, variables like <tt>this</tt> are stored by reference, not by value. Since the state of
<tt>this</tt> isn't guaranteed once the JSNI <tt>doSomething</tt> function goes out of scope, by the time the <tt>onclick</tt> anonymous function callback gets run, this either
points to some different object (which may not have a <tt>doSomethingElse</tt> method) or is even null or undefined. The fix is to create a local var that stores a copy of
<tt>this</tt>, and creating the function closure over that. The snippet below shows the correct way to have the bridge call to <tt>doSomethingElse</tt> run when <tt>someObj</tt>
is clicked.</p>

<pre class="prettyprint">
public native void doSomething() /*-{
    var foo = this;
    this.@com.company.app.client.MyClass::doSomethingElse(Ljava/lang/String;)(&quot;immediate&quot;);
    someObj.onclick = function() {
        foo.@com.company.app.client.MyClass::doSomethingElse(Ljava/lang/String;)(&quot;on click&quot;);
    }
}-*/;
</pre>

<p>In this way, the variable over which we have created the closure is stored by value, and hence will retain that value even after the <tt>doSomething</tt> function goes out of
scope.</p>

<p/>

<h3 id="How_do_I_pass_a_Java_method_as_a_callback_function?">How do I pass a Java method as a callback
function?</h3>

<p>It is quite common for a JavaScript api to return a value asynchronously through a callback function. You can refer to Java functions as first-class objects with <a href="/web-toolkit/doc/latest/DevGuideCodingBasics#DevGuideJavaScriptNativeInterface">JSNI</a> syntax. Assuming a JavaScript function <tt>externalJsFunction</tt> that takes a data value and a callback
function, here is an example of how to code this:</p>

<pre class="prettyprint">
package p;

class C {
  void doCallback(String callbackData) { ..... }
  native void invokeExternal(String data) /*-{
    $wnd.externalJsFunction(data, @p.C::doCallback(Ljava/lang/String;));
  }-*/;
}
</pre>

<p>Depending on the nature of the callback, it's sometimes helpful to use an anonymous JavaScript function to create a wrapper callback when you invoke the API method. When the
callback is invoked, wrapper will forward the parameter values to the Java method:</p>

<pre class="prettyprint">
package p;

class D {
  void someCallback(int param1, int param2, String param3) { ..... }
  native void invokeExternal(String data) /*-{
    $wnd.externalJsFunction(data, function(int1, int2, string3) {
      @p.D::someCallback(IILjava/lang/String;)(int1, int2, string3);
    });
  }-*/
}
</pre>

<h2 id="Deferred_Binding">Deferred Binding</h2>

<h3 id="What_is_Deferred_Binding?">What is Deferred Binding?</h3>

<p>Deferred Binding is GWT's answer to Java reflection.</p>

<p>It's easiest to explain Deferred Binding by starting with a use case. Every web browser has its own idiosyncrasies, usually lots of them. (The sheer unmanageable number of them
is the problem GWT was created to solve in the first place.) The standard Java way of dealing with idiosyncrasies would be to encapsulate the custom code into subclasses, with one
subclass for each supported browser. At runtime, the application would use reflection and dynamic classloading to select the appropriate subclass for the current environment, load
the class, create an instance, and then use that instance as the service provider for the duration of the program.</p>

<p>This is indeed what GWT does. However, the JavaScript environment in which GWT applications ultimately run simply does not support dynamic classloading (also known as
<i>dynamic binding</i>.) You can certainly include code to support each browser in your generated JavaScript code, but to do so you must include support for all browsers is in the
single application file. Why should an Opera user have to download code specific to Firefox, when there is no chance at all that she will ever need it?</p>

<p>Because <i><strong>dynamic</strong> binding</i> is unavailable as a technique to GWT, GWT instead uses <i><strong>deferred</strong> binding</i>. One way to think of this is as
&quot;dynamic class-loading that occurs at compile time instead of execution time.&quot; When the GWT Compiler compiles your Java application, it determines all the different
&quot;idiosyncrasies&quot; that it must support, and generates a separate, tightly streamlined version of the application for that specific configuration. For example, it generates a
different version of the application file for Firefox than it does for Opera.</p>

<p>There's more to it than just browser detection, though. Deferred Binding is a fully generic mechanism for handling features that vary according to some context. Another classic
example of Deferred Binding is internationalization: the GWT Compiler uses Deferred Binding to generate a completely separate version of the application for each language. Why
should an English speaker have to download the French text of your application?</p>

<p>Browser version and language represent two &quot;axes&quot; of variance for your application. It is possible to define your own axis, if you need to, and the GWT compiler handles all the
gory details of generating all the possible permutations. For instance, if GWT supports 4 browsers, and you write your application in 3 languages, then GWT will generate a total
of 12 different permutations of your application. During bootstrapping at runtime, GWT picks the appropriate permutation to show the user.</p>

<p>It's probably pretty clear by now that Deferred Binding works a little differently than standard dynamic binding. Conceptually, though, the two concepts are fairly similar, and
in practice all you usually need to do is just substitute a GWT method for a Java Reflection method. Instead of <tt>Class.forName(&quot;MyClass&quot;)</tt> you use
<tt>GWT.create(MyClass)</tt>. GWT handles all the details of managing the permutations.</p>

<p>Deferred Binding is one of the key features that lets GWT produce high-quality, well-optimized JavaScript code. If you're just using GWT, you usually won't have to get very far
into the details. If you are developing an extension to GWT such as a new widget, you'll want to look into it further. See the GWT Developer's Guide for some concrete examples of
deferred binding. If you have further questions, the <a href="http://groups.google.com/group/Google-Web-Toolkit">GWT Discussion Group</a> is ready to
help!</p>