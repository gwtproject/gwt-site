i18n Constants
===

1.  [Constants](#Constants)
2.  [ConstantsWithLookup](#ConstantsWithLookup)
3.  [Using Annotations](#ConstantsAnnotations)
4.  [Using Properties Files](#ConstantsProperties)

## Constants<a id="Constants"></a>

This example will walk through the process of creating a class of internationalized constant strings "hello, world" and "goodbye, world" in your GWT application. The example
will create a Java interface named `MyConstants` that abstracts those strings. You can reference the `MyConstants` methods in your GWT code when you want to display
one of those strings to the user and they will be translated appropriately for all locales that have matching `.properties` files.

Begin by creating a default properties file called `MyConstants.properties` in your GWT project. You can place the file anywhere in your module's source path, but the
`.properties` file and corresponding interface must be in the same package. It's fine to place the file in the same package as your module's entry point class.

```properties
helloWorld = hello, world
goodbyeWorld = goodbye, world
```

You can also create a localized translation for each supported locale in separate properties files. The properties file must be named the same as our interface name, in our
case `MyConstants`, with the appropriate suffix that indicates the [locale setting](DevGuideI18nLocale.html#LocaleSpecifying). In this case, we localize
for Spanish using the filename `MyConstants_es.properties`:

```properties
helloWorld = hola, mundo
goodbyeWorld = adiós, mundo
```

Now define an interface that abstracts those strings by extending the built-in `Constants` interface. Create a new Java interface in the same package where the
`.properties` files were created. The method names must match the tag names uses in the `.properties` files:

```java
public interface MyConstants extends Constants {
  String helloWorld();
  String goodbyeWorld();
}
```

**Tip:** The i18nCreator tool automates the generation of Constants interface subinterfaces like the one above. The tool generates Java code so that you only need to
maintain the `.properties` files. It also works for `ConstantsWithLookup` and `Messages` classes.

Note that `MyConstants` is declared as an interface, so you cannot instantiate it directly with `new`. To use the internationalized constants, you create a Java
instance of `MyConstants` using the [GWT.create(Class)](/javadoc/latest/com/google/gwt/core/client/GWT.html#create-java.lang.Class-) facility:

```java
public void useMyConstants() {
  MyConstants myConstants = GWT.create(MyConstants.class);
  Window.alert(myConstants.helloWorld());
}
```

You don't need to worry about the Java implementation of your static string classes. Static string initialization uses a [deferred binding generator](DevGuideCodingBasics.html#DevGuideDeferredBinding) which allows the GWT
compiler to take care of automatically generating the Java code necessary to implement your `Constants` subinterface depending on the [locale](DevGuideI18nLocale.html).

## ConstantsWithLookup<a id="ConstantsWithLookup"></a>

The [ConstantsWithLookup](/javadoc/latest/com/google/gwt/i18n/client/ConstantsWithLookup.html) interface is
identical to `Constants` except that the interface also includes a method to look up strings by property name, which facilitates dynamic binding to constants by name at
runtime. `ConstantsWithLookup` can sometimes be useful in highly data-driven applications. One caveat: `ConstantsWithLookup` is less efficient than
`Constants` because the compiler cannot discard unused constant methods,
resulting in larger applications.

## Using Annotations<a id="ConstantsAnnotations"></a>

The annotations discussed here are the ones specific to `Constants`
and `ConstantsWithLookup` &mdash; for shared annotations see the [main Internationalization
page](DevGuideI18n.html#DevGuideAnnotations).

### Method Annotations

The following annotations apply to methods in a `Constants` subtype
and must correspond to the return type of the method.  They provide a type-safe
way to reference constants, and can include Java compile-time constant
references.

*   **[`@DefaultBooleanValue(boolean val)`](/javadoc/latest/com/google/gwt/i18n/client/Constants.DefaultBooleanValue.html)** Sets the default value for a method which returns a `boolean`.
*   **[`@DefaultDoubleValue(double val)`](/javadoc/latest/com/google/gwt/i18n/client/Constants.DefaultDoubleValue.html)** Sets the default value for a method which returns a `double`.
*   **[`@DefaultFloatValue(float val)`](/javadoc/latest/com/google/gwt/i18n/client/Constants.DefaultFloatValue.html)** Sets the default value for a method which returns a `float`.
*   **[`@DefaultIntValue(int val)`](/javadoc/latest/com/google/gwt/i18n/client/Constants.DefaultIntValue.html)** Sets the default value for a method which returns a `int`.
*   **[`@DefaultStringArrayValue({String str, ...})`](/javadoc/latest/com/google/gwt/i18n/client/Constants.DefaultStringArrayValue.html)** Sets the default value for a method which returns a `String` array.
*   **[`@DefaultStringMapValue({String key, String value, ...})`](/javadoc/latest/com/google/gwt/i18n/client/Constants.DefaultStringMapValue.html)**
    Sets the default value for a method which returns a
`Map<String,String>` or a raw map (which will still be a
`String=>String` map).  The number of supplied values must be even,
and the first entry of each pair is the key and the second is the value.
*   **[`@DefaultStringValue(String str)`](/javadoc/latest/com/google/gwt/i18n/client/Constants.DefaultStringValue.html)** Sets the default value for a method which returns a `String`.

## Using Property Files<a id="ConstantsProperties"></a>

The properties file format for `Constants` and
`ConstantsWithLookup` is simply `key=value`, but there are a few
points to remember:

*   `#` must be escaped as it is a comment character
*   When the type of the method is `String[]`, an ASCII comma is used
  to separate values, which means that any commas included in a value must be
  escaped with a backslash.  Also, beware of translators using a different
  character to separate translated values.
*   In the case of a `Map`-valued method, the entry in the properties
  file for that method will be a comma-separated set of keys, and then those
  keys have their own entries with their associated value.  Example:

```properties
  Map<String,String> colorMap();

  colors=header, body, footer
  header=red
  body=white
  footer=blue
  
```

produces a map `{ header=>red, body=>white, footer=>blue
}`
