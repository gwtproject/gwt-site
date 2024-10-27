JSNI
===

(INFO: For new implementations use the future-proof [JsInterop](DevGuideCodingBasicsJsInterop.html) instead. JSNI will be removed with GWT 3.) 

Often, you will need to integrate GWT with existing handwritten JavaScript or with a third-party JavaScript library. Occasionally you may need to access low-level browser
functionality not exposed by the GWT class API's. The JavaScript Native Interface (JSNI) feature of GWT can
solve both of these problems by allowing you to integrate JavaScript directly into your application's Java source code.

The [GWT compiler](DevGuideCompilingAndDebugging.html#DevGuideJavaToJavaScriptCompiler) translates Java source into JavaScript. Sometimes it's very useful to mix handwritten
JavaScript into your Java source code. For example, the lowest-level functionality of certain core GWT classes are handwritten in JavaScript. GWT borrows from the Java Native
Interface (JNI) concept to implement JavaScript Native Interface (JSNI). Writing JSNI methods is a powerful technique, but should be used sparingly because writing bulletproof
JavaScript code is notoriously tricky. JSNI code is potentially less portable across browsers, more likely to leak memory, less amenable to Java tools, and harder for the compiler
to optimize.

We think of JSNI as the web equivalent of inline assembly code. You can use it in many ways:

*   Implement a Java method directly in JavaScript
*   Wrap type-safe Java method signatures around existing JavaScript
*   Call from JavaScript code into Java code and vice-versa
*   Throw exceptions across Java/JavaScript boundaries
*   Read and write Java fields from JavaScript
*   Use development mode to debug both Java source (with a Java debugger) and JavaScript (with a script debugger)

1.  [Writing Native JavaScript Methods](#writing)
2.  [Accessing Java Methods and Fields from JavaScript](#methods-fields)
3.  [Calling a Java Method from Handwritten JavaScript](#calling)
4.  [Sharing objects between Java source and JavaScript](#sharing)
5.  [Passing Java values into JavaScript](#passing-java)
6.  [Passing JavaScript values into Java code](#passing-javascript)
7.  [Important Notes](#important)
8.  [Exceptions and JSNI](#exceptions)

## Writing Native JavaScript Methods<a id="writing"></a>

JSNI methods are declared `native` and contain JavaScript code in a specially formatted comment block between the end of the parameter list and the trailing semicolon. A
JSNI comment block begins with the exact token `/*-{` and ends with the exact token `}-*/`. JSNI methods are called just like any normal Java method. They can be
static or instance methods.

The JSNI syntax is a directive to the Java-to-JavaScript Compiler to accept any text between the comment statements as valid JS code and inject it inline in the generated GWT
files. At compile time, the GWT compiler performs some syntax checks on the JavaScript inside the method, then generates interface code for converting method arguments and return
values properly.

As of the GWT 1.5 release, the Java varargs construct is supported. The GWT compiler will translate varargs calls between 2 pieces of Java code. However, calling a varargs
JavaScript method from Java will result in the callee receiving the arguments in an array.

### Examples<a id="examples-writing"></a>

Here is a simple example of how to code a JSNI method that puts up a JavaScript alert dialog:

```java
public static native void alert(String msg) /*-{
  $wnd.alert(msg);
}-*/;
```

Note that the code did not reference the JavaScript `window` object directly inside the method. When accessing the browser's window and document objects from JSNI, you
must reference them as `$wnd` and `$doc`, respectively. Your compiled script runs in a nested frame, and `$wnd` and `$doc` are automatically
initialized to correctly refer to the host page's window and document.

Here is another example with a problem:

```java
public static native int badExample() /*-{
  return "Not A Number";
}-*/;

 public void onClick () {
   try {
      int myValue = badExample();
      GWT.log("Got value " + myValue, null);
   } catch (Exception e) {
      GWT.log("JSNI method badExample() threw an exception:", e);
   }
 }
```

This example compiles as Java, and its syntax is verified by the GWT compiler
as valid JavaScript. But when you run the example code in [development mode](DevGuideCompilingAndDebugging.html#DevGuideDevMode),
it returns an exception. Click on the line in the log window to display the exception in the message
area below:

```text
com.google.gwt.dev.shell.HostedModeException: invokeNativeInteger(@com.example.client.GWTObjectNotifyTest::badExample()): JS value of type string, expected int
    at com.google.gwt.dev.shell.JsValueGlue.getIntRange(JsValueGlue.java:343)
    at com.google.gwt.dev.shell.JsValueGlue.get(JsValueGlue.java:179)
    at com.google.gwt.dev.shell.ModuleSpace.invokeNativeInt(ModuleSpace.java:233)
    at com.google.gwt.dev.shell.JavaScriptHost.invokeNativeInt(JavaScriptHost.java:97)
    at com.example.client.GWTObjectNotifyTest.badExample(GWTObjectNotifyTest.java:29)
    at com.example.client.GWTObjectNotifyTest$1.onClick(GWTObjectNotifyTest.java:52)
    ...
```

In this case, neither the Java IDE nor the GWT compiler could tell that there was a type mismatch between the code inside the JSNI method and the Java declaration. The GWT
generated interface code caught the problem at runtime in development mode. When
running in [production mode](DevGuideCompilingAndDebugging.html#DevGuideProdMode), you will not see an
exception. JavaScript's dynamic typing obscures this kind of problem.

> _Tip: Since JSNI code is just regular JavaScript, you will not be
> able to use Java debugging tools inside your JSNI methods when running in
> development mode. However, you
> can set a breakpoint on the source line containing the opening brace of a JSNI method, allowing you to see invocation arguments. Also, the Java compiler and GWT compiler do not
> perform any syntax or semantic checks on JSNI code, so any errors in the JavaScript body of the method will not be seen until run time._

## Accessing Java Methods and Fields from JavaScript<a id="methods-fields"></a>

It can be very useful to manipulate Java objects from within the JavaScript implementation of a JSNI method. However, since JavaScript uses dynamic typing and Java uses static
typing, you must use a special syntax.

> _When writing JSNI code, it is helpful to occasionally run in [production mode](DevGuideCompilingAndDebugging.html#DevGuideProdMode). The [JavaScript compiler](DevGuideCompilingAndDebugging.html#DevGuideJavaToJavaScriptCompiler) checks your JSNI code and can flag errors at compile time that you would not catch until
> runtime in [development mode](DevGuideCompilingAndDebugging.html#DevGuideDevMode)._

### Invoking Java methods from JavaScript<a id="methods"></a>

Calling Java methods from JavaScript is somewhat similar to calling Java methods from C code in [JNI](http://download.oracle.com/javase/1.5.0/docs/guide/jni/index.html). In particular, JSNI borrows the JNI mangled method signature approach to distinguish among overloaded methods. JavaScript calls into Java methods are of
the following form:

```text
[instance-expr.]@class-name::method-name(param-signature)(arguments)
```

*   **instance-expr.** : must be present when calling an instance method and must be absent when calling a static method
*   **class-name** : is the fully-qualified name of the class in which the method is declared (or a subclass thereof)
*   **param-signature** : is the internal Java method signature as specified at [JNI Type Signatures](http://download.oracle.com/javase/1.5.0/docs/guide/jni/spec/types.html#wp16432) but without the trailing signature of the method return type since it is not needed to choose the overload
*   **arguments** : is the actual argument list to pass to the called method

### Invoking Java constructors from JavaScript<a id="constructors"></a>

Calling Java constructors from JavaScript is identical to the above use case, except that the method name is always **new**.

Given the following Java classes:

```java
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
```

We compare the Java expression versus the JSNI expression:

*   `new TopLevel()` becomes `@pkg.TopLevel::new()()`
*   `new StaticInner()` becomes `@pkg.TopLevel.StaticInner::new()()`
*   `someTopLevelInstance.new InstanceInner(123)` becomes `@pkg.TopLevel.InstanceInner::new(Lpkg/TopLevel;I)(someTopLevelInstance, 123)`
    *   The enclosing instance of a non-static class is implicitly defined as the first parameter for constructors of a non-static class. Regardless of how deeply-nested a non-static
class is, it only needs a reference to an instance of its immediately-enclosing type.

### Accessing Java fields from JavaScript<a id="fields"></a>

Static and instance fields can be accessed from handwritten JavaScript. Field references are of the form

```text
[instance-expr.]@class-name::field-name
```

#### Example<a id="example-fields"></a>

Here's an example of accessing static and instance fields from JSNI.

```java
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
    x.@com.google.gwt.examples.JSNIExample::myInstanceField = val + " and stuff";

    // Read static field (no qualifier)
    @com.google.gwt.examples.JSNIExample::myStaticField = val + " and stuff";
  }-*/;

}
```

> _Tip: As of the GWT 1.5 release, the Java varargs construct is supported. The GWT compiler will translate varargs calls between two pieces of Java code, however,
> calling a varargs Java method from JSNI will require the JavaScript caller to pass an array of the appropriate type._

## Calling a Java Method from Handwritten JavaScript<a id="calling"></a>

Sometimes you need to access a method or constructor defined in GWT from outside JavaScript code. This code might be hand-written and included in another java script file, or
it could be a part of a third party library. In this case, the GWT compiler will not get a chance to build an interface between your JavaScript code and the GWT generated
JavaScript directly.

A way to make this kind of relationship work is to assign the method via JSNI to an external, globally visible JavaScript name that can be referenced by your hand-crafted
JavaScript code.

```java
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
```

Notice that the reference to the exported method has been wrapped in a call to the `$entry` function. This implicitly-defined function ensures that the Java-derived method is executed with the uncaught exception handler installed and pumps a number of other utility services.  The `$entry` function is reentrant-safe and should be used anywhere that GWT-derived JavaScript may be called into from a non-GWT context.

On application initialization, call `MyUtilityClass.exportStaticMethod()` (e.g. from your GWT Entry Point). This will assign the function to a variable in the window
object called `computeLoanInterest`.

If you want to export an instance method, and correctly use it from JS, then you need to do something like:
## Sharing objects between Java source and JavaScript<a id="sharing"></a>

```java
package mypackage;

public class Account {
    private int balance = 0;
    public int add(int amt) {
      balance += amt;
    }

    public native void exportAdd() /*-{
        var that = this;
        $wnd.add = $entry(function(amt) {
          that.@mypackage.Account::add(I)(amt);
        });
    }-*/;
}
```

Then you can call it in JS using

```javascript
$wnd.add(5);
```

The above code might look strange for Java developers but is needed because `this` in JavaScript can act differently than `this` in Java. If you are interested, a good introduction to `this` in JavaScript can be found [here](https://justin.harmonize.fm/development/2009/09/26/an-introduction-to-javascripts-this.html) or [here](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Operators/this).

## Sharing objects between Java source and JavaScript <a id="sharing"></a>

Parameters and return types in JSNI methods are declared as Java types. There are very specific rules for how values passing in and out of JavaScript code must be treated.
These rules must be followed whether the values enter and leave through normal Java method call semantics or through the special syntax by which Java methods are invoked from JSNI code.

## Passing Java values into JavaScript<a id="passing-java"></a>

| Incoming Java type                                                                   | How it appears to JavaScript code                                                                                                      |
| ------------------------------------------------------------------------------------ | -------------------------------------------------------------------------------------------------------------------------------------- |
| String                                                                               | JavaScript string, as in `var b = "foo";`                                                                                              |
| boolean                                                                              | JavaScript boolean value, as in `var b = true;`                                                                                        |
| long                                                                                 | disallowed (see notes)                                                                                                                 |
| other numeric primitives                                                             | JavaScript numeric value, as in `var x = 42;`                                                                                          |
| [JavaScriptObject](/javadoc/latest/com/google/gwt/core/client/JavaScriptObject.html) | `JavaScriptObject` that must have originated from JavaScript code, typically as the return value of some other JSNI method (see notes) |
| Java array                                                                           | opaque value that can only be passed back into Java code                                                                               |
| any other Java `Object`                                                              | opaque value accessible through special syntax                                                                                         |


## Passing JavaScript values into Java code<a id="passing-javascript"></a>

| Outgoing Java type                                                                   | What must be passed                                                                                                                        |
| ------------------------------------------------------------------------------------ | ------------------------------------------------------------------------------------------------------------------------------------------ |
| String                                                                               | JavaScript string, as in `return "boo";`                                                                                                   |
| boolean                                                                              | JavaScript boolean value, as in `return false;`                                                                                            |
| long                                                                                 | disallowed (see notes)                                                                                                                     |
| Java numeric primitive                                                               | JavaScript numeric value, as in `return 19;`                                                                                               |
| [JavaScriptObject](/javadoc/latest/com/google/gwt/core/client/JavaScriptObject.html) | native JavaScript object, as in `return document.createElement("div")` (see notes)                                                         |
| any other Java `Object` (including arrays)                                           | Java `Object` of the correct type that must have originated in Java code; Java objects cannot be constructed from "thin air" in JavaScript |


## Important Notes<a id="important"></a>

*   The Java `long` type cannot be represented in JavaScript as a numeric type, so GWT emulates it using an opaque data structure. This means that JSNI methods cannot
process a `long` as a numeric type. The compiler therefore disallows, by default, directly accessing a `long` from JSNI: JSNI methods cannot have `long` as a
parameter type or a return type, and they cannot access a `long` using a [JSNI reference](DevGuideCodingBasics.html#DevGuideJavaScriptNativeInterface). If you find
yourself wanting to pass a `long` into or out of a JSNI method, here are some options:
    1.  For numbers that fit into type `double`, use type `double` instead of type `long`.
    2.  For computations that require the full `long` semantics, rearrange the code so that the computations happen in Java instead of in JavaScript. That way they will use the `long` emulation.</li>
    3.  For values meant to be passed through unchanged to Java code, wrap the value in a `Long`. There are no restrictions on type `Long` with JSNI methods.
    4.  If you are sure you know what you are doing, you can add the annotation `com.google.gwt.core.client.UnsafeNativeLong` to the method. The compiler will then allow you to pass a `long` into and out of JavaScript. It will still be an opaque data type, however, so the only thing you will be able to do with it will be to pass it back to Java.

*   Violating any of these marshaling rules in [development mode](DevGuideCompilingAndDebugging.html#DevGuideDevMode) will generate a
`com.google.gwt.dev.shell.HostedModeException` detailing the problem. This exception is not [translatable](DevGuideCodingBasics.html#DevGuideClientSide)
and never thrown in [production mode](DevGuideCompilingAndDebugging.html#DevGuideProdMode).

*   [JavaScriptObject](/javadoc/latest/com/google/gwt/core/client/JavaScriptObject.html) gets special treatment
from the GWT compiler and development mode. Its purpose is to provide an opaque representation of native JavaScript objects to Java code.

*   Although Java arrays are not directly usable in JavaScript, there are some helper classes that efficiently achieve a similar effect: [JsArray](/javadoc/latest/com/google/gwt/core/client/JsArray.html), [JsArrayBoolean](/javadoc/latest/com/google/gwt/core/client/JsArrayBoolean.html), [JsArrayInteger](/javadoc/latest/com/google/gwt/core/client/JsArrayInteger.html), [JsArrayNumber](/javadoc/latest/com/google/gwt/core/client/JsArrayNumber.html), and [JsArrayString](/javadoc/latest/com/google/gwt/core/client/JsArrayString.html). These classes are wrappers around a native JavaScript array.

*   Java `null` and JavaScript `null` are identical and always legal values for any non-primitive Java type. JavaScript `undefined` is also considered equal
to `null` when passed into Java code (the rules of JavaScript dictate that in JavaScript code, `null == undefined` is `true` but `null === undefined`
is `false`). In previous versions of GWT, `undefined` was not a legal value to pass into Java.

## Exceptions and JSNI<a id="exceptions"></a>

An exception can be thrown during the execution of either normal Java code or the JavaScript code within a JSNI method. When an exception generated within a JSNI method
propagates up the call stack and is caught by a Java catch block, the thrown JavaScript exception is wrapped as a [JavaScriptException](/javadoc/latest/com/google/gwt/core/client/JavaScriptException.html) object at the time it is
caught. This wrapper object contains only the class name and description of the JavaScript exception that occurred. The recommended practice is to handle JavaScript exceptions in
JavaScript code and Java exceptions in Java code.

A Java exception can safely retain identity while propagating through a JSNI method.

For example,

1.  Java method `doFoo()` calls JSNI method `nativeFoo()`
2.  `nativeFoo()` internally calls Java method `fooImpl()`
3.  `fooImpl()` throws an exception

The exception thrown from `fooImpl()` will propagate through `nativeFoo()` and can be caught in `doFoo()`. The exception will retain its type and
identity.
