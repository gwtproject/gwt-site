JsInterop
===

JsInterop is one of the core features of GWT 2.8. As the name suggests, JsInterop is a way of interoperating Java with JavaScript. It offers a better way of communication between the two using annotations instead of having to write JavaScript in your classes (using JSNI). More details about the annotations can be found in GWT javadoc: https://www.gwtproject.org/javadoc/latest/jsinterop/annotations/package-summary.html


## Exporting a Java type to JavaScript

JsInterop can be used to expose a Java type to be used externally from a JavaScript script (aka a non-native type). This can be achieved by annotating the type with `@JsType`. This annotation exposes all the public non-static fields and methods, and tells the GWT compiler that the type is to be exported to a JavaScript type. Annotating a class with `@JsType` is equivalent to annotating all its public non-static methods with `@JsMethod`, its constructor with `@JsConstructor` (only one `@JsConstructor` is allowed to exist, more details can be found [in the javadoc](https://javadoc.io/doc/com.google.jsinterop/jsinterop-annotations/latest/index.html)), and all its public non-static fields with `@JsProperty`, so no need to add them explicitly.

Additionally, `@JsType` can be fine-tuned using the following properties:

 * name: customizes the name of the type. The default is to keep the Java type name.
 * namespace: specifies the JavaScript namespace of the type. The default is the current package of the type. To export a top-level type, the `JsPackage.GLOBAL` constant can be used.

The below example illustrates how `@JsType` can be used to export a Java type.


```java
package com.gwt.example;

@JsType
public class MyClass {

    public String name;

    public MyClass(String name) {
        this.name = name;
    }

    public void sayHello() {
        return "Hello" + this.name;
    }
}
```

From the JS script, the object can be used as a JS object:

```javascript
//the package name serves as a JS namespace
var aClass = new com.gwt.example.MyClass('World');

console.log(aClass.sayHello());

// result: 'Hello World'
```


## Importing a type from an external JavaScript script

Another usage of JsInterop annotations is to _import_ a type from an external script (aka a native type). To achieve that, the `isNative` property needs to be marked as true: `@JsType(isNative=true)`. The name and namespace have to match as well; the namespace `JsPackage.GLOBAL` and name `"?"` can be used to map a JS API without a well-defined type (duck-typing or private/hidden constructor). For example, the following code snippet illustrates the import of the global JSON object:

```java
@JsType(isNative = true, namespace = JsPackage.GLOBAL)
public class JSON {
    public static native String stringify(Object obj);

    public static native Object parse(String obj);
}
```


Important notes: 
      
 * constructors of native types must be empty and cannot contain any statement, except the call to the super class: `super()` (checked at compile time).
 * native types cannot have non-native methods, unless annotated with `@JsOverlay` (checked at compile time).
 * `@JsType` is not transitive. If the child objects are to be exposed to JavaScript, they need to be annotated as well.
  

## Consuming a JavaScript function with a callback argument

JsInterop can also be used to map a JavaScript function to a Java interface using `@JsFunction`. Unlike Java, methods can be used as arguments to other methods in JavaScript (known as callback argument). A JavaScript callback can be mapped to a Java functional interface (an interface with only one method) annotated with `@JsFunction`. The example below is inspired from Elemental 2 source code:

```java
@JsFunction
public interface EventListenerCallback {

    void callEvent(Object event);
}
```

```java
@JsType(isNative = true)
public class Element {
    // other methods

    public native void addEventListener(String eventType, EventListenerCallback fn);
}
```

```java
Element element = DomGlobal.document.createElement("button");
// using Java 8 syntax
element.addEventListener("click", (event) -> {

    GWT.log("clicked!");
});
```

is (more or less) equivalent to the following JavaScript code:

```javascript
var element = document.createElement("button");
element.addEventListener("click", (event) => {
  
  console.log("clicked!");
});
```

## Exposing a function/method to JS with a callback argument

In the same fashion, if a Java type is to be passed as a callback, `@JsFunction` is to be used. The implementation of the callback can be done directly from JavaScript. For example:

```java
package com.example;

@JsType
public class Bar {
    @JsFunction
    public interface Foo {
        int exec(int x);
    }

    public static int action1(Foo foo) {
        return foo.exec(40);
    }

    public static Foo action2() {
        return (x) -> x + 2;
    }
}
```

can be used in JavaScript as:

```javascript

com.example.Bar.action1((x) => x + 2); // will return 42!

var fn = com.example.Bar.action2();
fn(40); // will return 42!
```

## Adding additional utility methods to a native type

The JsInterop contract specifies that a native type may contain only native methods except the ones annotated with `@JsOverlay`. 
`@JsOverlay` allows adding a method to a native type (annotated with `@JsType(isNative=true)`) or on a default method of a `@JsFunction` annotated interface . The `@JsOverlay` contract specifies that the methods annotated should be final and should not override any existing native method. The annotated methods will not be accessble from JavaScript and can be used from Java only. `@JsOverlay` can be useful for adding utilities methods that may not be offered by the native type. For example:

```java
@JsType(isNative = true)
public class FancyWidget {

    public boolean visible;

    public native boolean isVisible();

    public native void setVisible(boolean visible);

    @JsOverlay
    public final void toggle() {
        visible = !visible;
    }
}
```

## Example: using Leaflet from Java

Suppose we want to use Leaflet from our GWT project. [Leaflet](http://leafletjs.com/) is a JS library to manipulate maps. All we need to do is wrap the methods that we need using JsInterop annotations:

```java
@JsType(isNative = true, namespace = JsPackage.GLOBAL)
public class L {

    public static native Map map(String id);
}
```

```java
@JsType(isNative = true, namespace = "L")
public class Map {

    public native L setView(double[] center, int zoom);
}
```

Please note that we used the same variable names as those in the source code of the Leaflet library. Class names, in our example L and Map, and method names are very important when wrapping native types in JsInterop.

Now we can initialize a Leafet map in our GWT application without writing any JavaScript code:

```java
public class Leafletwrapper implements EntryPoint {

    double[] positions = { 51.505, -0.09 };

    public void onModuleLoad() {
        // it works
        L.map("map").setView(positions, 13);
    }
}
```

The full example is available at https://github.com/zak905/jsinterop-leaflet

## Links: 

JsInterop specifications: 
https://docs.google.com/document/d/10fmlEYIHcyead_4R1S5wKGs1t2I7Fnp_PaNaa7XTEk0/edit#
GWT 2.8.0 and JsInterop: http://www.luigibifulco.it/blog/en/blog/gwt-2-8-0-jsinterop
