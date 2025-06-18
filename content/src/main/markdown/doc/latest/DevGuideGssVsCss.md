# Differences in syntax for GSS in GWT

## GSS features
GssResource support all the features offered by Closure stylesheet. Please refer to the [Closure stylesheet documentation](https://code.google.com/p/closure-stylesheets/#CSS_Extensions)
In addition to the Closure stylesheet feature, GssResource supports also the majority of the features that were introduced in CssResource.

## Constants
You can define constants using the `@def` at-rule. However GSS requires constants to be defined in upper-case.

```css
/* CSS */
@def myConstant 12px

/* GSS */
@def MY_CONSTANT 12px;

/* You can also use any gss function to define a constant */
@def MARGIN mult(divide(100%, 6), 4);
```

## Runtime substitution
In the old CssResource one could call Java code using the `@eval` rule.
In GSS the syntax has slightly changed. You will need to call the `eval()`
method and pass the java expression as an argument surrounded by quotes:

```css
/* CSS */
@eval myColor com.foo.bar.client.resource.Colors.getColor()

/* GSS */
@def MY_COLOR eval("com.foo.bar.client.resource.Colors.getColor()");

/* direct usage */
.red {
    color: eval("com.foo.bar.client.resource.Colors.getColor()");
}
```

## Conditional CSS
GssResource provides `@if`, `@elseif` and `@else` at-rules to create conditional CSS. These
conditions test the value against a deferred binding property.
In order to do that, GssResource provides an `is()` function.
This function takes two parameters as arguments. The first is the name of the
configuration property or binding property to test and the second is the expected value.
The `is()` function will return false if the value of the property is different then the expected value, otherwise true.
If only one argument is passed, the function assumes testing against the `user.agent` property.

```css
/* CSS */
@if user.agent ie8 ie9 {

  .foo {...}

} @else {

  .bar {...}

}

@if locale en {
  .baz {...}
}

/* GSS */
@if (is("ie8") || is("ie9")) {

  .foo {...}

} @else {

  .bar {...}

}

@if (is("locale", "en")) {
  .baz {...}
}
```

You can also use any boolean configuration property without using the `is()` function

```css
/* GSS */
@if (MY_CONFIGURATION_PROPERTY) {

  .foo {...}

} @else {

  .bar {...}

}
```

The configuration property needs to be single value and its value needs to be either `true` or `false`.

```xml
<set-configuration-property name="MY_CONFIGURATION_PROPERTY" is-multi-valued="false" />

<set-configuration-property name="MY_CONFIGURATION_PROPERTY" value="true" />
```

In GSS you can use logical operators (like &&, || and !) for your conditions:

```css
@if (is("ie8") || is("ie9") && !is("locale",  "en")) {

  .foo {padding: 8px;}

} @elseif (is("safari")) {

  .foo {padding: 18px;}

} @else {

  .foo {padding: 28px;}

}
```

### Note

* You can use conditional CSS in order to initialize a constant:

    ```css
    @if (is("ie8") || is("ie9")) {
      @def PADDING 15px;
    }@else {
      @def PADDING 0;
    }
    ```

* The `is()` function cannot be used outside of the conditional css context.
`@def IS_IE8 is("ie8")` will not work.

### Runtime Evaluation in conditional.
With the old syntax it was possible to make decision at runtime.
GSS supports that with a slightly changed syntax.
Like with the runtime evaluation, you have to use the `eval()`
function and pass any valid boolean java expression as argument:

```css
/* CSS */
@if (com.module.Foo.staticBooleanFunction()) {
  .foo {...}
}
/* GSS */
@if (eval("com.module.Foo.staticBooleanFunction()")) {
  .foo{...}
}
```

Note that:

* Boolean operators `&&`,  `||` cannot be used with a runtime condition.
* Unlike conditionals evaluated at compile time, you cannot use runtime
    condition in order to initialize constant. The code below will not compile:

    ```css
    @if (eval("com.module.Foo.staticBooleanFunction()")) {
      @def FOO 5px;
    }
    ```

* You cannot use a constant as condition:

    ```css
    @def MY_BOOLEAN eval("com.module.Foo.staticBooleanFunction()");

    @if (MY_BOOLEAN) {
      ...
    }
    ```
## Image Sprites
You don't need to use an at-rule to define sprite in GssResource. You have to use the special `gwt-sprite` property. As before, this property will be replaced by the properties needed for building the sprite.

```css
/* CSS */
@sprite .logout {
  gwt-image: "iconLogin";
  display: block;
  cursor: pointer;
}

/* GSS */
.logout {
    gwt-sprite: "iconLogin";
    display: block;
    cursor: pointer;
}
```

## References to Data Resources
To get access to the url of your resource bundled in the same `ClientBundle`. GssResource introduces the `resourceUrl()` method. This function will generate a value `url('some_url')` based on the return value of `DataResource.getUrl()`. It can be used in your constant definitions or directly in your css rules.

```css
/* CSS */
@url BACKGROUND_IMAGE myImageName

/* GSS */
@def BACKGROUND_IMAGE resourceUrl("myImageName");
```

```css
/* Direct usage of resourceUrl */
.logout {
    background-image: resourceUrl("logout");
}
```


## RTL support
See https://code.google.com/p/closure-stylesheets/#RTL_Flipping

## External style classes
The `@external` at-rule can be used in `CssResource` to suppress selector obfuscation while still allowing programmatic access to the selector name. This at-rule can be still used with GSS with some constraints:
Do not use a dot (`.`) in front of your style class and you need to surrond the expression with quotes if you use the `*` suffix to match multiple style classes.

```css
/* CSS */
@external .foo;

/* GSS */
@external foo;

/* Don't obfuscate the class myLegacyClass and all classes starting with gwt- */
@external myLegacyClass 'gwt-*';
```
