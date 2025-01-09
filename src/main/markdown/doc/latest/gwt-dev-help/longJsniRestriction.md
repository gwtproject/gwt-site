# Restrictions on Long Values with JSNI

The Java `long` type cannot be represented in JavaScript as a numeric type, so GWT emulates it using an opaque data structure. This means that JSNI methods cannot process a `long` as a numeric type. The compiler, therefore, disallows, by default, directly accessing a `long` from JSNI: JSNI methods cannot have `long` as a parameter type or a return type, and they cannot access a `long` using a JSNI reference. If you find yourself wanting to pass a `long` into or out of a JSNI method, here are some options:

1. **For numbers that fit into type `double`**, use type `double` instead of type `long`.

2. **For computations that require the full `long` semantics**, rearrange the code so that the computations happen in Java instead of in JavaScript. That way, they will use the `long` emulation.

3. **For values meant to be passed through unchanged to Java code**, wrap the value in a `Long`. There are no restrictions on type `Long` with JSNI methods.

4. **If you are sure you know what you are doing**, you can add the annotation `com.google.gwt.core.client.UnsafeNativeLong` to the method. The compiler will then allow you to pass a `long` into and out of JavaScript. It will still be an opaque data type, however, so the only thing you will be able to do with it will be to pass it back to Java.
