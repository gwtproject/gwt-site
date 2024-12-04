Overlay
===

Suppose you're happily using JSNI to call bits of handwritten JavaScript from within your GWT module. It works well, but JSNI only works at the level of individual methods.
Some integration scenarios require you to more deeply intertwine JavaScript and Java objects &mdash; DOM and JSON programming are two good examples &mdash; and so what we really want is a
way to interact directly with JavaScript objects from our Java source code. In other words, we want JavaScript objects that _look like_ Java objects when we're coding.

GWT 1.5 introduces **JavaScript overlay types** to make it easy to integrate entire families of JavaScript objects into your GWT project. There are many benefits
of this technique, including the ability to use your Java IDE's code completion and refactoring capabilities even as you're working with untyped JavaScript objects.

1.  [Example: Easy, efficient JSON](#example-json)
2.  [JavaScriptObjects and Interfaces](#javascript-objects)
3.  [Example: Lightweight collections](#example-collections)
4.  [Putting it all together](#together)

## Example: Easy, efficient JSON<a id="example-json"></a>

Overlay types are easiest to understand with examples. Suppose we want to access an array of JSON objects representing a set of "customer" entities. The JavaScript structure
might look like this:

```javascript
var jsonData = [
  { "FirstName" : "Jimmy", "LastName" : "Webber" },
  { "FirstName" : "Alan",  "LastName" : "Dayal" },
  { "FirstName" : "Keanu", "LastName" : "Spoon" },
  { "FirstName" : "Emily", "LastName" : "Rudnick" }
];
```

To superimpose a Java type onto the above structure, you start by subclassing `JavaScriptObject`, a marker type that GWT uses to denote JavaScript objects. Let's go
ahead and add some getters, too.

```java
// An overlay type
class Customer extends JavaScriptObject {

  // Overlay types always have protected, zero-arg ctors
  protected Customer() { }

  // Typically, methods on overlay types are JSNI
  public final native String getFirstName() /*-{ return this.FirstName; }-*/;
  public final native String getLastName()  /*-{ return this.LastName;  }-*/;

  // Note, though, that methods aren't required to be JSNI
  public final String getFullName() {
    return getFirstName() + " " + getLastName();
  }
}
```

GWT will now understand that any instance of `Customer` is actually a true JavaScript object that comes from outside your GWT module. This has useful implications. For
example, notice the `this` reference inside `getFirstName()` and `getLastName()`. That `this` is truly the identity of the JavaScript object, so you
interact with it exactly as it exists in JavaScript. In this example, we can directly access the JSON fields we know exist, `this.FirstName` and `this.LastName`.

So, how do you actually get a JavaScript object on which to overlay a Java type? You can't construct it by writing `new Customer()` because the whole point is to
_overlay_ a Java type onto an _already existing_ JavaScript object. Thus, we have to get such an object from the wild using JSNI:

```java
class MyModuleEntryPoint implements EntryPoint {
  public void onModuleLoad() {
    Customer c = getFirstCustomer();
    // Yay! Now I have a JS object that appears to be a Customer
    Window.alert("Hello, " + c.getFirstName());
  }

  // Use JSNI to grab the JSON object we care about
  // The JSON object gets its Java type implicitly
  // based on the method's return type
  private native Customer getFirstCustomer() /*-{
    // Get a reference to the first customer in the JSON array from earlier
    return $wnd.jsonData[0];
  }-*/;
}
```

Let's clarify what we've done here. We've taken a plain-old-JSON-object (POJSONO, anyone? no?) and created a normal-looking Java type that can be used to interact with it
within your GWT code. You get code completion, refactoring, and compile-time checking as you would with any Java code. Yet, you have the flexibility of interacting with arbitrary
JavaScript objects, which makes things like accessing JSON services via [RequestBuilder](/javadoc/latest/com/google/gwt/http/client/package-summary.html) a breeze.

A quick digression for compiler geeks. Another neat thing about overlay types is that you can augment the Java type without disturbing the underlying JavaScript object. In the
example above, notice that we added the `getFullName()` method. It's purely Java code &mdash; it doesn't exist on the underlying JavaScript object &mdash; and yet the method is
written in terms of the underlying JavaScript object. In other words, the Java view of the JavaScript object can be richer in functionality than the JavaScript view of the same
object but without having to modify the underlying JS object, neither the instance nor its `prototype`.

(This is still part of the digression.) This cool wackiness of adding new methods to overlay types is possible because the rules for overlay types by design disallow
polymorphic calls; all methods must be `final` and/or `private`. Consequently, every method on an overlay type is statically resolvable by the compiler, so there is
never a need for dynamic dispatch at runtime. That's why we don't have to muck about with an object's function pointers; the compiler can generate a direct call to the method as
if it were a global function, external to the object itself. It's easy to see that a direct function call is faster than an indirect one. Better still, since calls to methods on
overlay types can be statically resolved, they are all candidates for automatic inlining, which is a Very Good Thing when you're fighting for performance in a scripting language.
Below we'll revisit this to show you just how much this regimen pays off.

## JavaScriptObjects and Interfaces<a id="javascript-objects"></a>

Starting with GWT 2.0, it is permissible for JavaScriptObject subtypes to implement interfaces.  Every method defined in an interface may map to at most one method declared in a JavaScriptObject subtype.  Practically speaking, this means that only one JavaScriptObject type may implement any given interface, but any number of non-JavaScriptObject types may also implement that interface.

```java
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
```

In the above example, the `Person.getName()` will be mapped to `PersonJso.getName()`.  Because JavaScriptObject methods must be final, subclasses of `PersonJso` are allowed since they cannot override `getName()`. It would be an error to declare `class SomeOtherJso extends JavaScriptObject implements Person{}` because JavaScriptObjects have no type information at runtime, so  `Person.getName()` could not be unambiguously dispatched.

## Example: Lightweight collections<a id="example-collections"></a>

We glossed over something in the example above. The method `getFirstCustomer()` is pretty unrealistic. You're certainly going to want to be able to access the entire
array of customers. Thus, we need an overlay type representing the JavaScript array itself. Fortunately, that's easy:

```java
// w00t! Generics work just fine with overlay types
class JsArray<E extends JavaScriptObject> extends JavaScriptObject {
  protected JsArray() { }
  public final native int length() /*-{ return this.length; }-*/;
  public final native E get(int i) /*-{ return this[i];     }-*/;
}
```

Now we can write more interesting code:

```java
class MyModuleEntryPoint implements EntryPoint {
  public void onModuleLoad() {
    JsArray<Customer> cs = getCustomers();
    for (int i = 0, n = cs.length(); i < n; ++i) {
      Window.alert("Hello, " + cs.get(i).getFullName());
    }
  }

  // Return the whole JSON array, as is
  private final native JsArray<Customer> getCustomers() /*-{
    return $wnd.jsonData;
  }-*/;
}
```

This is nice clean code, especially considering the flexibility of the plumbing it's built upon. As hinted at earlier, the compiler can do pretty fancy stuff to make this quite
efficient. Take a look at the unobfuscated compiled output for the `onModuleLoad()` method:

```javascript
function $onModuleLoad(){
  var cs, i, n;
  cs = $wnd.jsonData;
  for (i = 0, n = cs.length; i < n; ++i) {
    $wnd.alert('Hello, ' + (cs[i].FirstName + ' ' + cs[i].LastName));
  }
}
```

This is pretty darn optimized. Even the overhead of the `getFullName()` method went away. In fact, _all_ of the Java method calls went away. When we say that "GWT
gives you affordable abstractions," this is the kind of thing we're talking about. Not only does inlined code run significantly faster, we no longer had to include the function
definitions themselves, thus shrinking the script a litte, too. (To be fair, though, inlining can also easily increase script size, so we're careful to strike a balance between
size and speed.) It's pretty fun to look back at the original Java source above and try to reason about the sequence of optimizations the compiler had to perform to end up
here.

Of course, we can't resist showing you the corresponding obfuscated code:

```javascript
function B(){var a,b,c;a=$wnd.jsonData;for(b=0,c=a.length;b<c;++b){
  $wnd.alert(l+(a[b].FirstName+m+a[b].LastName))}}
```

Notice in this version that the only bits that _aren't_ obfuscated are the identifiers that originated in JavaScript, such as `FirstName`, `LastName`,
`jsonData`, etc. That's why, although GWT strives to make it easy to do lots of JavaScript interop, we try hard to persuade people to write as much of their code as
possible as pure Java source instead of mixing with JavaScript. Hopefully now when you hear us say that, you'll understand that we aren't bashing JavaScript &mdash; it's just that we
can't optimize it as much, which makes us sad.

## Putting it all together<a id="together"></a>

Overlay types are a key feature, made available in GWT 1.6. At its simplest, the technique makes direct interop with JavaScript libraries much easier. Hopefully after this post you could
imagine how to almost directly port any JavaScript library into GWT as a set of Java types, thus allowing the use of a Java IDE for productive development and debugging without
impacting size or speed due to any sort of GWT overhead. At the same time, overlay types serve as a powerful abstraction tool for delivering more elegant low-level APIs such as
the [new GWT DOM package](/javadoc/latest/com/google/gwt/dom/client/package-tree.html).

For more information...

*   [Surprisingly Rockin' JavaScript and DOM Programming](http://sites.google.com/site/io/surprisingly-rockin-javascript-and-dom-programming-in-gwt) - This video (or the associated slides) from Google I/O is the best place to get an end-to-end explanation of overlay types in context. The presentation demonstrates the new GWT DOM classes and explains how we used overlay types to implement everything. It also specifies more detail about constructing your own overlay types.
*   [GWT and Client-Server Communication](http://sites.google.com/site/io/gwt-and-client-server-communication) - Also from Google I/O, Miguel Mendez explains various ways in which you can access data from the browser, including how to combine `RequestBuilder` and overlay types for really convenient JSON access.
*   [Design: Overlay Types](http://code.google.com/p/google-web-toolkit/wiki/OverlayTypes) - Read at your own risk :-) These are the excruciating technical details. It's fairly interesting but not necessarily instructive.
