JsInterop
===

Starting from version 2.7, JsInterop was introduced in GWT as an experimental feature to replace JSNI. It is now one of the core features of GWT 2.8.x. As the name suggests, JsInterop is a way of interoperating Java with Javascript. it offers a better way of communication between the two using annotations instead of having to write JavaScript in your classes (using JSNI). JsInterop is defined by the following annotations: @JsType, @JsProperty, @JsMethod, @JsConstructor, @JsFunction, @JsOverlay.


## @JsType

@JsType is equivalent to marking an object as a Javascript object. It can be used on classes only. It can be used in both ways: to export a Java type to be used from a javascript external script or to import a type from another script to be used in Java. 

1. Exposing java classes to a javascript script :</p>

```
package com.gwt.example;

@JsType
public class myClass {

public String name;

public myClass(String name){
this.name = name;
}
public void sayHello(){
return 'Hello' + this.name;
}
}
```

From the js script, the object can be used as a js object :

```
//the package name serves as JS namespace
var aClass = new com.gwt.example.myClasse('World');

console.log(aClass.sayHello());

// result: 'Hello World'
```

To customize the object's namespace, the namespace property can be used: `@JsType(namespace="yournamespace")`. For example, the global namespace can be used to make the object accessible globaly: 
`@JsType(namespace=jsinterop.annotations.JsPackage.GLOBAL)`


2. Importing a type from an external script :</p>

To import a javascript native type, the isNative property needs to be marked as true: `@JsType(isNative=true)`. The names and namespaces have to match as well. For example, the following code snippet illustrates the import of the global JSON object:

```
@JsType(isNative=true, namespace=GLOBAL)
public class JSON {
  public native static String stringify(Object obj);
  
  public native static Object parse(String obj);
}
```

additionally the name property can be used to export or import an object with a different name than the Java variable name. `@JsType(name="newName")`

annotating a class @JsType is equivalent to annotating all its methods with @JsMethod and all its properties with @JsProperty, so no need to add them explicity.  

<ul>Important notes: 
    
  <li> constructors of native imported objects should be empty and cannot contain any statement (checked at compile time)</li>
  <li> native objects cannot have non native methods, unless annotated with @JsOverlay (checked at compile time)</li>
  <li> native objects cannot have assigned properties, (int x = 10 should be int x) </li>
  <li> @JsType is not transitive. Child objects need to be annotated as well. </li>
</ul>


## @JsProperty:

@JsProperty is used to annotate a static class property or a property whose class has a @JsConstructor annotation, to be exported/import to/from javascript, without having to expose all the object fields and methods (without @JsType)


```
package com.gwt.example;

  
public class myClass {

@Jsproperty
public static String name = "foo";

public myClass(String name){
this.name = name;
}

}
```


```
//the package name serves as JS namespace
com.gwt.example.myClasse.name;//foo
```

## @JsMethod:

@JsMethod is used to annotate static methods that need to exposed to js without having to expose the object itself
(without @JsType). 

```
package com.gwt.example;

public class Record {
  
  
  public Record() {
    
  }
  
  @JsMethod(namespace="hello.world")
  public static int test(int i) {
    
    return i + 10;
  }
  
}
```
```
hello.world.test(20); //Results in 30

var record = new com.gwt.example.Record(); 

//throws error at run time
Uncaught ReferenceError: com is not defined
    at <anonymous>:1:18
```

If the method is not static then @JsMethod can work if the object can be instantiated from javascript. This can be achieved using @JsConstructor. 

## @JsConstructor:

`@JsConstructor` can be used to expose an object constructor. It can be used along with `@JsProperty` and `@JsMethod` to expose only certain fields or methods. For example: 

```
package com.gwt.example;


public class myClass {

@JsProperty
public String name;


public String id = bar;

@JsConstructor
public myClass(String name){
this.name = name;
}

@JsMethod
public void sayHello(){
return 'Hello' + this.name;
}


public void sayGoodbye(){
return 'Bye ' + this.name;
}
}
```

This results in the method `sayHello()` and the property `name` to be exposed. The method `sayGoodbye()` and the property `id` will not be accessible from a javascript script. 

## @JsFunction:

`@JsFunction` allows mapping between a javascript anonymous function and a java interface. Javascript anonymous functions are usually passed as arguments or used as callbacks. `@JsFunction` should be used on a Java interface with one method only(which is know as a Funtional interface). 

```
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

// in JavaScript

com.acme.Bar.action1(function(x) { return x + 2; }); // will return 42!

var fn = com.acme.Bar.action2();
fn(40); // will return 42!
```

<ul>Important notes: 
    
  <li> A @JsFunction interface cannot extend other interfaces.</li>
  <li> A @JsFunction interface can only have JsOverlay default methods.</li>
  <li> A class that implements a @JsFunction type (directly or indirectly) cannot be a @JsType nor have JsConstructor/JsMethods/JsProperties. </li>
  <li> A class cannot implement other interfaces while implementing @JsFunction interface. </li>
</ul>

## @JsOverlay:

`@JsOverlay` allows adding a method to a native javascript type (annotated with `@JsType(isNative=true)`). The `@JsOverlay` contract specifies that the methods annotated should be final and should not override any existing native method. The annotated methods will not be accessbile from javascript and can be used from Java only. `@JsOverlay` can be useful for adding utilities methods that may not be offered by the native type. For example:

```
@JsType(isNative=true)
public class FancyWidget {
   public native boolean isVisible();
   public native void setVisible(boolean visible);

   @JsOverlay
   public final void toggle() {
     setVisible(!isVisible());
   }
}
```

## Links: 

JsInterop specifications: 
https://docs.google.com/document/d/10fmlEYIHcyead_4R1S5wKGs1t2I7Fnp_PaNaa7XTEk0/edit#
GWT 2.8.0 and JsInterop: http://www.luigibifulco.it/blog/en/blog/gwt-2-8-0-jsinterop