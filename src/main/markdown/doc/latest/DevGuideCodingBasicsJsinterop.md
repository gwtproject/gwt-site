JsInterop
===

JsInterop is one of the core features of GWT 2.8.x. As the name suggests, JsInterop is a way of interoperating Java with javascript. It offers a better way of communication between the two using annotations instead of having to write javascript in your classes (using JSNI). More details about the annotations can be found in GWT javadocs: http://www.gwtproject.org/javadoc/latest/jsinterop/annotations/package-summary.html


## Using a Java type in javascript:

JsInterop can be used to expose a Java type (or non-native javascript types)  to be used externally from a javascript script. This can be achieved by annotating the type with `@JsType`. This annotation exposes all the fields and methods, and tells the GWT compiler that the type is to be exported as-is to a javascript type. Annotating a class with `@JsType` is equivalent to annotating all its methods with `@JsMethod`, its constructor with `@JsConstructor`( Only one `@JsConstructor` is allowed to exist, more details can be found in the javadocs [page](http://www.gwtproject.org/javadoc/latest/jsinterop/annotations/JsConstructor.html)), and all its properties with `@JsProperty`, so no need to add them explicity.  

Additionally, `@JsType` can be fine tuned using the following properties:
        - name: customizes the name of the type. The default is to keep the Java type name. 
        - namespace: specifies the javascript namespace of the type. The default is the current package of the type. To export a top-level type, the `JsPackage.GLOBAL` constant can be used. 

  The below example illustrates how `@JsType` can used to export a Java type.   


```java
package com.gwt.example;

@JsType
public class MyClass {

    public String name;

    public MyClass(String name) {
        this.name = name;
    }

    public void sayHello() {
        return 'Hello' + this.name;
    }
}
```
  From the js script, the object can be used as a js object :

```javascript
//the package name serves as a JS namespace
var aClass = new com.gwt.example.MyClass('World');

console.log(aClass.sayHello());

// result: 'Hello World'
```


## Importing a type from an external javascript script (A native javascript type):

  Another usage of JsIntrop annotations is to import a javascript native type. To achieve that, the isNative property needs to be marked as true: `@JsType(isNative=true)`. The names and namespaces have to match as well. For example, the following code snippet illustrates the import of the global JSON object:

```java
@JsType(isNative = true, namespace = GLOBAL)
public class JSON {
    public static native String stringify(Object obj);

    public static native Object parse(String obj);
}
```


Important notes: 
      
   * constructors of native imported objects must be empty and cannot contain any statement, except the call to the super class:  super() (checked at compile time).
   * native types cannot have non-native methods, unless annotated with `@JsOverlay` (checked at compile time).
   * `@JsType` is not transitive. If the child objects are to be exposed to javascript, they need to be annotated as well.
  

## Consuming a javascript function with a callback argument:

JsInterop can also be used to map a javascript function to a java interface using `@JsFunction`. Unlike Java, methods can be used as arguments to other methods in javascript (known as callback argument). A javascript callback can be mapped to a Java functional interface (an inteface with only one method) annotated with `@JsFunction`. The example below is inspired from Elemental 2 source code:

```
@JsFunction
public interface EventListenerCallback {

    void callEvent(Object event);
}
```

```
@JsType(isNative = true)
public class Element {
    //other methods

    public native void addEventListener(String eventType, EventListenerCallback fn);
}
```

```
Element element = DomGlobal.document.createElement("button");
//using java 8  syntax
element.addEventListener("click", (event) -> {
  
    GWT.log("clicked!");
})
```
is more or less equivalent to the following javascript code:

```javascript
var element = document.createElement("button");
element.addEventListener("click", (event) => {
  
 console.log("clicked!");
});

```


## How to expose a function/method to JS with a callback argument:

In the same fashion, if a java type is to be passed as a callback, `@JsFunction` is to be used. The implementation of the callback can done directly from javascript. For example:

```java
// in Java
package com.acme;

@JsFunction
public interface Foo {
    int exec(int x);
}

@JsType
public class Bar {
    public static int action1(Foo foo) {
        return foo.exec(40);
    }

    public static Foo action2() {
        return (x) -> x + 2;
    }
}
```


```javascript
// in JavaScript

com.acme.Bar.action1(function(x) { return x + 2; }); // will return 42!

var fn = com.acme.Bar.action2();
fn(40); // will return 42!
```  

## How to add additional utility methods to a native type:

The JsInterop contract specifies that a native type may contain only native methods except the ones annotated with `@JsOverlay`. 
`@JsOverlay` allows adding a method to a native javascript type (annotated with `@JsType(isNative=true)`) or on a default method of a `@JsFunction` annotated interface . The `@JsOverlay` contract specifies that the methods annotated should be final and should not override any existing native method. The annotated methods will not be accessble from javascript and can be used from Java only. `@JsOverlay` can be useful for adding utilities methods that may not be offered by the native type. For example:

```
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

 Suppose we want to use Leaflet from our GWT project. [Leaflet](http://leafletjs.com/) is a JS library to manipulate maps . All we need to do is wrap the methods that we need using JsInterop annotations:

```
@JsType(isNative=true, namespace=JsPackage.GLOBAL)
public class L {


    public static native Map map(String id);

}
```
```
@JsType(isNative = true, namespace="L")
public class Map {


    
    public native L setView(double[] center, int zoom);

}
```
Please note that we used the same variable names as those in the source code of the Leaflet library. Class names, in our example L and Map, and method names are very important when wrapping native types in JsInterop.

Now we can initialize a Leafet map in our GWT application without writing any javascript code:

```
public class Leafletwrapper implements EntryPoint {

    double[] positions = {51.505, -0.09};

    public void onModuleLoad() {

//it works
        L.map("map").setView(positions, 13);
    }
}
```
The full example is available at : https://github.com/zak905/jsinterop-leaflet

## Links: 

JsInterop specifications: 
https://docs.google.com/document/d/10fmlEYIHcyead_4R1S5wKGs1t2I7Fnp_PaNaa7XTEk0/edit#
GWT 2.8.0 and JsInterop: http://www.luigibifulco.it/blog/en/blog/gwt-2-8-0-jsinterop