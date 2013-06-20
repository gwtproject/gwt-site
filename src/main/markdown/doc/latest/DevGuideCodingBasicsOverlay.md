<p>Suppose you're happily using JSNI to call bits of handwritten JavaScript from within your GWT module. It works well, but JSNI only works at the level of individual methods.
Some integration scenarios require you to more deeply intertwine JavaScript and Java objects &mdash; DOM and JSON programming are two good examples &mdash; and so what we really want is a
way to interact directly with JavaScript objects from our Java source code. In other words, we want JavaScript objects that <i>look like</i> Java objects when we're coding.</p>

<p>GWT 1.5 introduces <strong>JavaScript overlay types</strong> to make it easy to integrate entire families of JavaScript objects into your GWT project. There are many benefits
of this technique, including the ability to use your Java IDE's code completion and refactoring capabilities even as you're working with untyped JavaScript objects.</p>

<ol class="toc" id="pageToc">
  <li><a href="#example-json">Example: Easy, efficient JSON</a></li>
  <li><a href="#javascript-objects">JavaScriptObjects and Interfaces</a></li>
  <li><a href="#example-collections">Example: Lightweight collections</a></li>
  <li><a href="#together">Putting it all together</a></li>
</ol>


<h2 id="example-json">Example: Easy, efficient JSON</h2>

<p>Overlay types are easiest to understand with examples. Suppose we want to access an array of JSON objects representing a set of &quot;customer&quot; entities. The JavaScript structure
might look like this:</p>


<pre class="prettyprint">
var jsonData = [
  { &quot;FirstName&quot; : &quot;Jimmy&quot;, &quot;LastName&quot; : &quot;Webber&quot; },
  { &quot;FirstName&quot; : &quot;Alan&quot;,  &quot;LastName&quot; : &quot;Dayal&quot; },
  { &quot;FirstName&quot; : &quot;Keanu&quot;, &quot;LastName&quot; : &quot;Spoon&quot; },
  { &quot;FirstName&quot; : &quot;Emily&quot;, &quot;LastName&quot; : &quot;Rudnick&quot; }
];
</pre>


<p>To superimpose a Java type onto the above structure, you start by subclassing <tt>JavaScriptObject</tt>, a marker type that GWT uses to denote JavaScript objects. Let's go
ahead and add some getters, too.</p>

<pre class="prettyprint">
// An overlay type
class Customer extends JavaScriptObject {

  // Overlay types always have protected, zero-arg ctors
  protected Customer() { }

  // Typically, methods on overlay types are JSNI
  public final native String getFirstName() /*-{ return this.FirstName; }-*/;
  public final native String getLastName()  /*-{ return this.LastName;  }-*/;

  // Note, though, that methods aren't required to be JSNI
  public final String getFullName() {
    return getFirstName() + &quot; &quot; + getLastName();
  }
}
</pre>

<p>GWT will now understand that any instance of <tt>Customer</tt> is actually a true JavaScript object that comes from outside your GWT module. This has useful implications. For
example, notice the <tt>this</tt> reference inside <tt>getFirstName()</tt> and <tt>getLastName()</tt>. That <tt>this</tt> is truly the identity of the JavaScript object, so you
interact with it exactly as it exists in JavaScript. In this example, we can directly access the JSON fields we know exist, <tt>this.FirstName</tt> and <tt>this.LastName</tt>.</p>

<p>So, how do you actually get a JavaScript object on which to overlay a Java type? You can't construct it by writing <tt>new Customer()</tt> because the whole point is to
<i>overlay</i> a Java type onto an <i>already existing</i> JavaScript object. Thus, we have to get such an object from the wild using JSNI:</p>


<pre class="prettyprint">
class MyModuleEntryPoint implements EntryPoint {
  public void onModuleLoad() {
    Customer c = getFirstCustomer();
    // Yay! Now I have a JS object that appears to be a Customer
    Window.alert(&quot;Hello, &quot; + c.getFirstName());
  }

  // Use JSNI to grab the JSON object we care about
  // The JSON object gets its Java type implicitly
  // based on the method's return type
  private native Customer getFirstCustomer() /*-{
    // Get a reference to the first customer in the JSON array from earlier
    return $wnd.jsonData[0];
  }-*/;
}
</pre>


<p>Let's clarify what we've done here. We've taken a plain-old-JSON-object (POJSONO, anyone? no?) and created a normal-looking Java type that can be used to interact with it
within your GWT code. You get code completion, refactoring, and compile-time checking as you would with any Java code. Yet, you have the flexibility of interacting with arbitrary
JavaScript objects, which makes things like accessing JSON services via <a href="/javadoc/latest/com/google/gwt/http/client/package-summary.html">RequestBuilder</a> a breeze.</p>

<p>A quick digression for compiler geeks. Another neat thing about overlay types is that you can augment the Java type without disturbing the underlying JavaScript object. In the
example above, notice that we added the <tt>getFullName()</tt> method. It's purely Java code &mdash; it doesn't exist on the underlying JavaScript object &mdash; and yet the method is
written in terms of the underlying JavaScript object. In other words, the Java view of the JavaScript object can be richer in functionality than the JavaScript view of the same
object but without having to modify the underlying JS object, neither the instance nor its <tt>prototype</tt>.</p>

<p>(This is still part of the digression.) This cool wackiness of adding new methods to overlay types is possible because the rules for overlay types by design disallow
polymorphic calls; all methods must be <tt>final</tt> and/or <tt>private</tt>. Consequently, every method on an overlay type is statically resolvable by the compiler, so there is
never a need for dynamic dispatch at runtime. That's why we don't have to muck about with an object's function pointers; the compiler can generate a direct call to the method as
if it were a global function, external to the object itself. It's easy to see that a direct function call is faster than an indirect one. Better still, since calls to methods on
overlay types can be statically resolved, they are all candidates for automatic inlining, which is a Very Good Thing when you're fighting for performance in a scripting language.
Below we'll revisit this to show you just how much this regimen pays off.</p>

<h2 id="javascript-objects">JavaScriptObjects and Interfaces</h2>

<p>Starting with GWT 2.0, it is permissible for JavaScriptObject subtypes to implement interfaces.  Every method defined in an interface may map to at most one method declared in a JavaScriptObject subtype.  Practically speaking, this means that only one JavaScriptObject type may implement any given interface, but any number of non-JavaScriptObject types may also implement that interface.</p>

<pre class="prettyprint">
interface Person {
  String getName();
}

/** The JSO implementation of Person. */
class PersonJso extends JavaScriptObject implements Person {
  protected PersonJso() {}

  public static native PersonJso create(String name) /*-{
    return {name: name};
  }-*/;

  public final native String getName() /*-{
    return this.name;
  }-*/;
}

/** Any number of non-JSO types may implement the Person interface. */
class PersonImpl implements Person {
  private final String name;

  public PersonImpl(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}

// Elsewhere
class Troll {
  /** This method doesn't care about whether p is a JSO or not, this makes testing easier. */
  public void grindBones(Person p) {
    String name = p.getName();
    ...
  }
}
</pre>

<p>In the above example, the <tt>Person.getName()</tt> will be mapped to <tt>PersonJso.getName()</tt>.  Because JavaScriptObject methods must be final, subclasses of <tt>PersonJso</tt> are allowed since they cannot override <tt>getName()</tt>. It would be an error to declare <tt>class SomeOtherJso extends JavaScriptObject implements Person{}</tt> because JavaScriptObjects have no type information at runtime, so  <tt>Person.getName()</tt> could not be unambiguously dispatched.</p>

<h2 id="example-collections">Example: Lightweight collections</h2>

<p>We glossed over something in the example above. The method <tt>getFirstCustomer()</tt> is pretty unrealistic. You're certainly going to want to be able to access the entire
array of customers. Thus, we need an overlay type representing the JavaScript array itself. Fortunately, that's easy:</p>


<pre class="prettyprint">
// w00t! Generics work just fine with overlay types
class JsArray&lt;E extends JavaScriptObject&gt; extends JavaScriptObject {
  protected JsArray() { }
  public final native int length() /*-{ return this.length; }-*/;
  public final native E get(int i) /*-{ return this[i];     }-*/;
}
</pre>


<p>Now we can write more interesting code:</p>

<pre class="prettyprint">
class MyModuleEntryPoint implements EntryPoint {
  public void onModuleLoad() {
    JsArray&lt;Customer&gt; cs = getCustomers();
    for (int i = 0, n = cs.length(); i &lt; n; ++i) {
      Window.alert(&quot;Hello, &quot; + cs.get(i).getFullName());
    }
  }

  // Return the whole JSON array, as is
  private final native JsArray&lt;Customer&gt; getCustomers() /*-{
    return $wnd.jsonData;
  }-*/;
}
</pre>

<p>This is nice clean code, especially considering the flexibility of the plumbing it's built upon. As hinted at earlier, the compiler can do pretty fancy stuff to make this quite
efficient. Take a look at the unobfuscated compiled output for the <tt>onModuleLoad()</tt> method:</p>


<pre class="prettyprint">
function $onModuleLoad(){
  var cs, i, n;
  cs = $wnd.jsonData;
  for (i = 0, n = cs.length; i &lt; n; ++i) {
    $wnd.alert('Hello, ' + (cs[i].FirstName + ' ' + cs[i].LastName));
  }
}
</pre>


<p>This is pretty darn optimized. Even the overhead of the <tt>getFullName()</tt> method went away. In fact, <i>all</i> of the Java method calls went away. When we say that &quot;GWT
gives you affordable abstractions,&quot; this is the kind of thing we're talking about. Not only does inlined code run significantly faster, we no longer had to include the function
definitions themselves, thus shrinking the script a litte, too. (To be fair, though, inlining can also easily increase script size, so we're careful to strike a balance between
size and speed.) It's pretty fun to look back at the original Java source above and try to reason about the sequence of optimizations the compiler had to perform to end up
here.</p>

<p>Of course, we can't resist showing you the corresponding obfuscated code:</p>


<pre class="prettyprint">
function B(){var a,b,c;a=$wnd.jsonData;for(b=0,c=a.length;b&lt;c;++b){
  $wnd.alert(l+(a[b].FirstName+m+a[b].LastName))}}
</pre>


<p>Notice in this version that the only bits that <i>aren't</i> obfuscated are the identifiers that originated in JavaScript, such as <tt>FirstName</tt>, <tt>LastName</tt>,
<tt>jsonData</tt>, etc. That's why, although GWT strives to make it easy to do lots of JavaScript interop, we try hard to persuade people to write as much of their code as
possible as pure Java source instead of mixing with JavaScript. Hopefully now when you hear us say that, you'll understand that we aren't bashing JavaScript &mdash; it's just that we
can't optimize it as much, which makes us sad.</p>

<h2 id="together">Putting it all together</h2>

<p>Overlay types are a key feature, made available in GWT 1.6. At its simplest, the technique makes direct interop with JavaScript libraries much easier. Hopefully after this post you could
imagine how to almost directly port any JavaScript library into GWT as a set of Java types, thus allowing the use of a Java IDE for productive development and debugging without
impacting size or speed due to any sort of GWT overhead. At the same time, overlay types serve as a powerful abstraction tool for delivering more elegant low-level APIs such as
the the <a href="/javadoc/latest/com/google/gwt/dom/client/package-tree.html">new GWT DOM package</a>.</p>

<p>For more information...</p>

<ul>
<li><a href="http://sites.google.com/site/io/surprisingly-rockin-javascript-and-dom-programming-in-gwt">Surprisingly Rockin' JavaScript and DOM Programming</a> -
This video (or the associated slides) from Google I/O is the best place to get an end-to-end explanation of overlay types in context. The presentation demonstrates the new GWT DOM
classes and explains how we used overlay types to implement everything. It also specifies more detail about constructing your own overlay types.</li>

<li><a href="http://sites.google.com/site/io/gwt-and-client-server-communication">GWT and Client-Server Communication</a> - Also from Google I/O, Miguel Mendez
explains various ways in which you can access data from the browser, including how to combine <tt>RequestBuilder</tt> and overlay types for really convenient JSON access.</li>

<li><a href="http://code.google.com/p/google-web-toolkit/wiki/OverlayTypes">Design: Overlay Types</a> - Read at your own risk :-) These are the excruciating
technical details. It's fairly interesting but not necessarily instructive.</li>
</ul>

