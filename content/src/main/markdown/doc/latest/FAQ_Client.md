FAQ - Client
===

1.  [Project Architecture](#Project_Architecture)
    1.  [What is a GWT Module?](#What_is_a_GWT_Module?)
2.  [Writing Java code](#Writing_Java_code)
    1.  [How do I enable assertions?](#How_do_I_enable_assertions?)
3.  [JavaScript Native Interface](#JavaScript_Native_Interface)
    1.  [How do I call Java methods from handwritten JavaScript or third party libraries?](#How_do_I_call_Java_methods_from_handwritten_JavaScript_or_third)
    2.  [Help! I'm having problems with eval() in my JSNI method!](#Help!_I)
    3.  [Why doesn't the bridge call to my JSNI method work in `<some_obj>.onclick`?<li><a href="#How_do_I_pass_a_Java_method_as_a_callback_function?">How do I pass a Java method as a callback function?](#Why_doesn)
4.  [Deferred Binding](#Deferred_Binding)
    1.  [What is Deferred Binding?](#What_is_Deferred_Binding?)<div id="FAQ_Client"/>

## Project Architecture<a id="Project_Architecture"></a>

### What is a GWT Module?<a id="What_is_a_GWT_Module?"></a>

A GWT module is simply an encapsulation of functionality. It shares some similarities with a Java package but is not the same thing. 

A GWT module is named similarly to a Java package in that it follows the usual dotted-path naming convention. For example, most of the standard GWT modules are located
underneath "com.google.gwt" However, the similarity between GWT modules and Java packages ends with this naming convention.

A module is defined by an XML descriptor file ending with the extension ".gwt.xml", and the name of that file determines the name of the module. For example, if you have a file
named `src/com/mycompany/apps/MyApplication.gwt.xml`, then that will create a GWT module named `com.mycompany.apps.MyApplication`. The contents of the
`.gwt.xml` file specify the precise list of Java classes and other resources that are included in the GWT module.

## Writing Java code<a id="Writing_Java_code"></a>

### How do I enable assertions?<a id="How_do_I_enable_assertions?"></a>

In order to use the assert() feature in Java, you must enable them with the `-enableassertions` or `-ea` command line argument. See the article on the Java
website [Programming with Assertions](http://java.sun.com/j2se/1.4.2/docs/guide/lang/assert.html) for more details. If you are using an IDE, add this
argument to the VM argument list.

The GWT compiler also recognizes the `-ea` flag to generate code for assertions in the compiled JavaScript.

Only use assertions for debugging purposes, not production logic because
assertions will only work under GWT's development mode. By default, they are compiled away by the GWT
compiler so do not have any effect in production mode unless you explicitly enable them.

## JavaScript Native Interface<a id="JavaScript_Native_Interface"></a>

### How do I call Java methods from handwritten JavaScript or third party libraries?<a id="How_do_I_call_Java_methods_from_handwritten_JavaScript_or_third"></a>

You can get this to work by assigning the method via JSNI to an external, globally visible JavaScript name that can be referenced by your hand-crafted JavaScript code. For example,

```java
package mypackage;
public MyUtilityClass
{
    public static int computeLoanInterest(int amt, float interestRate, int term) { ... }
    public static native void exportStaticMethod() /*-{
       $wnd.computeLoanInterest = 
         $entry(@mypackage.MyUtilityClass::computeLoanInterest(IFI));
    }-*/;
}
```

Notice that the reference to the exported method has been wrapped in a call to the $entry function. This implicitly-defined function ensures that the Java-derived method is executed with the uncaught exception handler installed and pumps a number of other utility services. The $entry function is reentrant-safe and should be used anywhere that GWT-derived JavaScript may be called into from a non-GWT context.

On application initialization, call `MyUtilityClass.exportStaticMethod()` (e.g. from your GWT Entry Point). This will create a function on the window object called
`computeLoanInterest()` which will invoke, via JSNI, the compiled Java method of the same name. The bridge method is needed because the GWT compiler will
obfuscate/compress/rename the names of Java methods when it translates them to JavaScript.

### Help! I'm having problems with eval() in my JSNI method!<a id="Help!_I'm_having_problems_with_eval()_in_my_JSNI_method!"></a>

Occasionally you might want to use this idiom for a GWT class method:

```java
public static native String myMethod(String arg) /*-{
    eval("var myVar = 'arg is ' + arg;");
    return myVar;
}-*/;
```

The code above will work in development mode, but not in production mode. The reason is that when GWT compiles the Java source code to JavaScript, it obfuscates variable names. In this
case, it will change the name of the `arg` variable. However, GWT can't see into JavaScript string literals in a JSNI method, and so it can't update the corresponding
embedded references to `arg` to also use the new varname.

The fix is to tweak the `eval()` statement so that the variable names are visible to the GWT compiler:

```java
public static native String myMethod(String arg) /*-{
    eval("var myVar = 'arg is " + arg + "';");
    return myVar;
}-*/;
```

### Why doesn't the bridge call to my JSNI method work in `<some_obj>.onclick`?<a id="Why_doesn't_the_bridge_call_to_my_JSNI_method_work_in_onclick"></a>

While there are many reasons why your bridge call may not seem to be called in `onclick`, one of the most common causes has to do with function closures and how
JavaScript variables are stored.

The code below illustrates this point:

```java
public native void doSomething() /*-{
    this.@com.company.app.client.MyClass::doSomethingElse(Ljava/lang/String;)("immediate");
    someObj.onclick = function() {
        this.@com.company.app.client.MyClass::doSomethingElse(Ljava/lang/String;)("on click");
    }
}-*/;

public void doSomethingElse(String foo) {
    Window.alert(foo);
}
```

Someone new to JavaScript looking at this code may expect the "immediate" alert to display as soon as the code runs, and the "on click" alert to pop up when `someObj` is
clicked. However, what actually will happen is that the first alert will display, but never the "on click." This problem occurs because "`this.@com...`" idioms create
function closures over `this`. However, in JavaScript function closures, variables like `this` are stored by reference, not by value. Since the state of
`this` isn't guaranteed once the JSNI `doSomething` function goes out of scope, by the time the `onclick` anonymous function callback gets run, this either
points to some different object (which may not have a `doSomethingElse` method) or is even null or undefined. The fix is to create a local var that stores a copy of
`this`, and creating the function closure over that. The snippet below shows the correct way to have the bridge call to `doSomethingElse` run when `someObj`
is clicked.

```java
public native void doSomething() /*-{
    var foo = this;
    this.@com.company.app.client.MyClass::doSomethingElse(Ljava/lang/String;)("immediate");
    someObj.onclick = function() {
        foo.@com.company.app.client.MyClass::doSomethingElse(Ljava/lang/String;)("on click");
    }
}-*/;
```

In this way, the variable over which we have created the closure is stored by value, and hence will retain that value even after the `doSomething` function goes out of
scope.

### How do I pass a Java method as a callback function?<a id="How_do_I_pass_a_Java_method_as_a_callback_function?"></a>

It is quite common for a JavaScript api to return a value asynchronously through a callback function. You can refer to Java functions as first-class objects with [JSNI](DevGuideCodingBasics.html#DevGuideJavaScriptNativeInterface) syntax. Assuming a JavaScript function `externalJsFunction` that takes a data value and a callback
function, here is an example of how to code this:

```java
package p;

class C {
  void doCallback(String callbackData) { ..... }
  native void invokeExternal(String data) /*-{
    $wnd.externalJsFunction(data, @p.C::doCallback(Ljava/lang/String;));
  }-*/;
}
```

Depending on the nature of the callback, it's sometimes helpful to use an anonymous JavaScript function to create a wrapper callback when you invoke the API method. When the
callback is invoked, wrapper will forward the parameter values to the Java method:

```java
package p;

class D {
  void someCallback(int param1, int param2, String param3) { ..... }
  native void invokeExternal(String data) /*-{
    $wnd.externalJsFunction(data, function(int1, int2, string3) {
      @p.D::someCallback(IILjava/lang/String;)(int1, int2, string3);
    });
  }-*/
}
```

## Deferred Binding<a id="Deferred_Binding"></a>

### What is Deferred Binding?<a id="What_is_Deferred_Binding?"></a>

Deferred Binding is GWT's answer to Java reflection.

It's easiest to explain Deferred Binding by starting with a use case. Every web browser has its own idiosyncrasies, usually lots of them. (The sheer unmanageable number of them
is the problem GWT was created to solve in the first place.) The standard Java way of dealing with idiosyncrasies would be to encapsulate the custom code into subclasses, with one
subclass for each supported browser. At runtime, the application would use reflection and dynamic classloading to select the appropriate subclass for the current environment, load
the class, create an instance, and then use that instance as the service provider for the duration of the program.

This is indeed what GWT does. However, the JavaScript environment in which GWT applications ultimately run simply does not support dynamic classloading (also known as
_dynamic binding_.) You can certainly include code to support each browser in your generated JavaScript code, but to do so you must include support for all browsers is in the
single application file. Why should an Opera user have to download code specific to Firefox, when there is no chance at all that she will ever need it?

Because _**dynamic** binding_ is unavailable as a technique to GWT, GWT instead uses _**deferred** binding_. One way to think of this is as
"dynamic class-loading that occurs at compile time instead of execution time." When the GWT Compiler compiles your Java application, it determines all the different
"idiosyncrasies" that it must support, and generates a separate, tightly streamlined version of the application for that specific configuration. For example, it generates a
different version of the application file for Firefox than it does for Opera.

There's more to it than just browser detection, though. Deferred Binding is a fully generic mechanism for handling features that vary according to some context. Another classic
example of Deferred Binding is internationalization: the GWT Compiler uses Deferred Binding to generate a completely separate version of the application for each language. Why
should an English speaker have to download the French text of your application?

Browser version and language represent two "axes" of variance for your application. It is possible to define your own axis, if you need to, and the GWT compiler handles all the
gory details of generating all the possible permutations. For instance, if GWT supports 4 browsers, and you write your application in 3 languages, then GWT will generate a total
of 12 different permutations of your application. During bootstrapping at runtime, GWT picks the appropriate permutation to show the user.

It's probably pretty clear by now that Deferred Binding works a little differently than standard dynamic binding. Conceptually, though, the two concepts are fairly similar, and
in practice all you usually need to do is just substitute a GWT method for a Java Reflection method. Instead of `Class.forName("MyClass")` you use
`GWT.create(MyClass)`. GWT handles all the details of managing the permutations.

Deferred Binding is one of the key features that lets GWT produce high-quality, well-optimized JavaScript code. If you're just using GWT, you usually won't have to get very far
into the details. If you are developing an extension to GWT such as a new widget, you'll want to look into it further. See the GWT Developer's Guide for some concrete examples of
deferred binding. If you have further questions, the [GWT Discussion Group](http://groups.google.com/group/Google-Web-Toolkit) is ready to
help!
