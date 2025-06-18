Client Bundle
===

The resources in a deployed GWT application can be roughly categorized into resources to never cache (`.nocache.js`), to cache forever (`.cache.html`), and everything else (`myapp.css`).  The <a href="/javadoc/latest/index.html?com/google/gwt/resources/client/ClientBundle.html">ClientBundle</a> interface moves entries from the everything-else category into the cache-forever category.

1.  [Overview](#ClientBundle)
2.  [DataResource](#DataResource)
3.  [TextResource and ExternalTextResource](#TextResource)
4.  [ImageResource](#ImageResource)
5.  [GwtCreateResource](#GwtCreateResource)
6.  [CssResource](#CssResource)
7.  [CssResourceCookbook](#CssResourceCookbook)

## Overview<a id="ClientBundle"></a>

*   [Goals](#ClientBundle_Goals)
*   [Examples](#ClientBundleExamples)
*   [I18N](#I18N)
*   [Pluggable Resource Generation](#Pluggable_Resource_Generation)
*   [Levers and knobs](#Levers_and_knobs)
*   [Resource types](#Resource_types)

### Goals<a id="ClientBundle_Goals"></a>

*   No more uncertainty about whether your application is getting the right contents for program resources.
*   Decrease non-determinism caused by intermediate proxy servers.
*   Enable more aggressive caching headers for program resources.
*   Eliminate mismatches between physical filenames and constants in Java code by performing consistency checks during the compile.
*   Use 'data:' URLs, JSON bundles, or other means of embedding resources in compiled JS when browser- and size-appropriate to eliminate unneeded round trips.
*   Provide an extensible design for adding new resource types.
*   Ensure there is no penalty for having multiple `ClientBundle` resource functions refer to the same content.

#### Non-Goals

*   To provide a file-system abstraction

### Examples<a id="ClientBundleExamples"></a>

To use [ClientBundle](/javadoc/latest/index.html?com/google/gwt/resources/client/ClientBundle.html), add an `inherits` tag to your `gwt.xml` file:

```xml
<inherits name="com.google.gwt.resources.Resources" />
```

If you write this interface:

```java
public interface MyResources extends ClientBundle {
  public static final MyResources INSTANCE =  GWT.create(MyResources.class);

  @Source("my.css")
  public CssResource css();

  @Source("config.xml")
  public TextResource initialConfiguration();

  @Source("manual.pdf")
  public DataResource ownersManual();
}
```

You can then say:

```  // Inject the contents of the CSS file
  MyResources.INSTANCE.css().ensureInjected();

  // Display the manual file in an iframe
  new Frame(MyResources.INSTANCE.ownersManual().getSafeUri().asString());
```

### I18N<a id="I18N"></a>

`ClientBundle` is compatible with GWT's I18N module.

Suppose you defined a resource:

```java
@Source("default.txt")
public TextResource defaultText();
```

For each possible value of the `locale` deferred-binding property, the `ClientBundle` generator will look for variations of the specified filename in a manner similar to that of Java's `ResourceBundle`.

Suppose the `locale` were set to `fr_FR`.  The generator would look for files in the following order:

1.  default_fr_FR.txt
2.  default_fr.txt
3.  default.txt

This will work equally well with all resource types, which can allow you to provide localized versions of other resources, say `ownersManual_en.pdf` versus `ownersManual_fr.pdf`.

### Pluggable Resource Generation<a id="Pluggable_Resource_Generation"></a>

Each subtype of [`ResourcePrototype`](/javadoc/latest/index.html?com/google/gwt/resources/client/ResourcePrototype.html) must define a [`@ResourceGeneratorType`](/javadoc/latest/index.html?com/google/gwt/resources/ext/ResourceGeneratorType.html) annotation whose value is a concrete Java class that extends [`ResourceGenerator`](/javadoc/latest/index.html?com/google/gwt/resources/ext/ResourceGenerator.html).  The instance of the `ResourceGenerator` is responsible for accumulation (or bundling) of incoming resource data as well as a small degree of code generation to assemble the concrete implementation of the `ClientBundle` class.  Implementors of `ResourceGenerator` subclasses can expect that only one `ResourceGenerator` will be created for a given type of resource within a `ClientBundle` interface.

The methods on a `ResourceGenerator` are called in the following order

1.  `init` to provide the `ResourceGenerator` with a [`ResourceContext`](/javadoc/latest/index.html?com/google/gwt/resources/ext/ResourceContext.html)
2.  `prepare` is called for each `JMethod` the `ResourceGenerator` is expected to handle
3.  `createFields` allows the `ResourceGenerator` to add code at the class level
4.  `createAssignment` is called for each `JMethod`.  The generated code should be suitable for use as the right-hand side of an assignment expression.
5.  `finish` is called after all assignments should have been written.

`ResourceGenerators` are expected to make use of the [`ResourceGeneratorUtil`](/javadoc/latest/index.html?com/google/gwt/resources/ext/ResourceGeneratorUtil.html) class.

### Levers and knobs<a id="Levers_and_knobs"></a>

*   `ClientBundle.enableInlining` is a deferred-binding property that can be used to disable the use of `data:` URLs in browsers that would otherwise support inlining resource data into the compiled JS.*   `ClientBundle.enableRenaming` is a configuration property that will disable the use of strongly-named cache files.

### Resource types<a id="Resource_types"></a>

These resource types are valid return types for methods defined in a ClientBundle:

*   [ImageResource](#ImageResource)
*   [CssResource](#CssResource)
*   [DataResource](#DataResource)
*   [ExternalTextResource](#TextResource)
*   [GwtCreateResource](#GwtCreateResource)
*   [TextResource](#TextResource)
*   Subclasses of ClientBundle (for nested bundles)

## DataResource<a id="DataResource"></a>

A [DataResource](/javadoc/latest/index.html?com/google/gwt/resources/client/DataResource.html) is the simplest of the resource types, offering a URL by which the contents of a file can be retrieved at runtime.  The main optimization offered is to automatically rename files based on their contents in order to make the resulting URL strongly-cacheable by the browser.  Very small files may be converted into `data:` URLs on those browsers that support them.

```java
interface Resources extends ClientBundle {
  Resources INSTANCE = GWT.create(Resources.class);

  @Source("mycursor.cur")
  DataResource customCursor();
}

// Elsewhere
someDiv.getStyle().setProperty("cursor", "url(" + Resources.INSTANCE.customCursor().getUrl() + ")");
```

Resources that are not appropriate for being inlined into the compiled JavaScript as `data:` URLs will be emitted into the compilation output with strong names, based on the contents of the file.  For example, `foo.pdf` will be given a name similar to `ABC1234.cache.pdf`.  The webserver should be configured to serve any files matching the `*.cache.*` glob with publicly-cacheable headers and a far-future `Expires` header.

## TextResource and ExternalTextResource<a id="TextResource"></a>

The related resource types [TextResource](/javadoc/latest/index.html?com/google/gwt/resources/client/TextResource.html) and [ExternalTextResource](/javadoc/latest/index.html?com/google/gwt/resources/client/ExternalTextResource.html) provide access to static text content.  The main difference between these two types is that the former interns the text into the compiled JavaScript, while the latter bundles related text resources into a single file, which is accessed asynchronously.

```java
interface Resources extends ClientBundle {
  Resources INSTANCE = GWT.create(Resources.class);

  @Source("a.txt")
  TextResource synchronous();

  @Source("b.txt")
  ExternalTextResource asynchronous();
}

// Using a TextResource
myTextArea.setInnerText(Resources.INSTANCE.synchronous().getText());

// Using an ExternalTextResource
Resources.INSTANCE.asynchronous().getText(new ResourceCallback<TextResource>() {
  public void onError(ResourceException e) { ... }
  public void onSuccess(TextResource r) {
    myTextArea.setInnerText(r.getText());
  }
});
```

## ImageResource<a id="ImageResource"></a>

This section describes how [ImageResource](/javadoc/latest/index.html?com/google/gwt/resources/client/ImageResource.html) can tune the compile-time processing of image data and provide efficient access to image data at runtime. 

*   [Goal](#ImageResourceGoal)
*   [Overview](#ImageResourceOverview)
    *   [ImageOptions](#ImageOptions)
    *   [Supported formats](#Supported_formats)
*   [Converting from ImageBundle](#Converting_from_ImageBundle)

### Goal<a id="ImageResourceGoal"></a>

*   Provide access to image data at runtime in the most efficient way possible*   Do not bind the `ImageResource` API to a particular widget framework

#### Non-Goals

*   To provide a general-purpose image manipulation framework.

### Overview<a id="ImageResourceOverview">

1.  Define a ClientBundle with one or more <a href="/javadoc/latest/index.html?com/google/gwt/resources/client/ImageResource.html">ImageResource</a> accessors.
For each accessor method, add an @Source annotation with the path of the new image you want to add to your program.
The ClientBundle generator combines all of the images defined in your interface into a single, optimized image.

```java
interface Resources extends ClientBundle {
  @Source("logo.png")
  ImageResource logo();

  @Source("arrow.png")
  @ImageOptions(flipRtl = true)
  ImageResource pointer();
}
```
2.  Instantiate the `ClientBundle` via a call to `GWT.create()`. </li>
3.  Instantiate one or more Image widget or use with the <a href="#Image_Sprites">CssResource @sprite</a> directive.

For example, the code:

```java
Resources resources = GWT.create(Resources.class);
Image img = new Image(resources.logo());
```

causes GWT to load the composite image generated for Resources and then creates an Image that is the correct subregion for just the logo image.</li>

#### `ImageOptions`<a id="ImageOptions"></a>

The `@ImageOptions` annotation can be applied to the `ImageResource` accessor method in order to tune the compile-time processing of the image data:

*   `flipRtl` is a boolean value that will cause the image to be mirrored about its Y-axis when `LocaleInfo.isRTL()` returns `true`.
*   `repeatStyle` is an enumerated value that is used in combination with the`@sprite` directive to indicate that the image is intended to be tiled.

#### Supported formats<a id="Supported_formats"></a>

`ImageBundleBuilder` uses the Java [ImageIO](http://java.sun.com/javase/6/docs/api/javax/imageio/package-summary.html) framework to read image data. This includes support for all common web image formats.

Animated GIF files are supported by `ImageResource`.  While the image data will not be incorporated into an image strip, the resource is still served in a browser-optimal fashion by the larger `ClientBundle` framework.

### Converting from ImageBundle<a id="Converting_from_ImageBundle"></a>

Only minimal changes are required to convert existing code to use `ImageResource`.

*   `ImageBundle` becomes `ClientBundle`
*   `AbstractImagePrototype` becomes `ImageResource`
*   `AbstractImagePrototype.createImage()` becomes `new Image(imageResource)`
*   Other methods on `AbstractImagePrototype` can continue to be used by calling `AbstractImagePrototype.create(imageResource)` to provide an API wrapper.

## GwtCreateResource<a id="GwtCreateResource"></a>

The [GwtCreateResource](/javadoc/latest/index.html?com/google/gwt/resources/client/GwtCreateResource.html) is a bridge type between `ClientBundle` and any other (resource) type that is default-instantiable. The instance of the `GwtCreateResource` acts as a factory for some other type.

```java
interface Resources extends ClientBundle {
  Resources INSTANCE = GWT.create(Resources.class);

  @ClassType(SomeClass.class)
  GwtCreateResource<ReturnType> factory();
}

// Elsewhere
ReturnType obj = Resources.INSTANCE.factory().create();
```

While the above is equivalent to

```java
ReturnType obj = GWT.<ReturnType> create(SomeClass.class);
```

it allows the consuming classes to be ignorant of the specific class literal passed into `GWT.create()`.  It is not necessary for there to be a specific deferred-binding rule in place for `SomeClass` as long as that type is default-instantiable.

## CssResource<a id="CssResource"></a>

This section describes [CssResource](/javadoc/latest/index.html?com/google/gwt/resources/client/CssResource.html) and the compile-time processing of CSS. 

1.  [Goals](#CssResource_Goals)
2.  [Overview](#CssResource_Overview)
3.  [Features](#Features)
    *   [Constants](#Constants)
    *   [Runtime substitution](#Runtime_substitution)
    *   [Value function](#Value_function)
    *   [Literal function](#Literal_function)
    *   [Conditional CSS](#Conditional_CSS)
    *   [Image Sprites](#Image_Sprites)
    *   [References to Data Resources](#References_to_Data_Resources)
    *   [RTL support](#RTL_support)
    *   [Selector obfuscation](#Selector_obfuscation)
4.  [Optimizations](#Optimizations)
    *   [Basic minification](#Basic_minification)
    *   [Selector merging](#Selector_merging)
    *   [Property merging](#Property_merging)
6.  [Levers and Knobs](#Levers_and_Knobs)
7.  [Selector obfuscation details](#Selector_obfuscation_details)
    *   [Strict scoping](#Strict_scoping)
    *   [Scope](#Scope)
    *   [Shared scopes](#Shared_scopes)
    *   [Imported scopes](#Imported_scopes)
    *   [External and legacy scopes](#External_and_legacy_scopes)
    *   [Automatically generating interfaces](#Automatically_generating_interfaces)

See also the [CssResourceCookbook](#CssResourceCookbook) and [StyleInjector](/javadoc/latest/index.html?com/google/gwt/dom/client/StyleInjector.html). 

### Goals<a id="CssResource_Goals"></a>

*   Primary
    *   Compatibility with non-GWT-aware CSS parsers (i.e. any extensions should be valid CSS syntax)
    *   This does not imply that the stylesheet would necessarily make sense if you just displayed it in a browser
    *   Syntax validation
    *   Minification
    *   Leverage GWT compiler
        *   Different CSS for different browsers, automatically
        *   Static evaluation of content
*   Secondary
    *   Basic CSS Modularization
        *   Via dependency-injection API style
        *   Widgets can inject their own CSS only when it's needed
    *   BiDi (Janus-style?)
    *   CSS image strips
    *   "Improve CSS"
        *   Constants
        *   Simple expressions
*   Tertiary
    *   Runtime manipulation (StyleElement.setEnabled() handles many cases)
    *   Compile-time class-name checking (Java/CSS)
    *   Obfuscation

#### Non-Goals

*   Server-side manipulation
    *   All features in CssResource must be implemented with compile-time and runtime code only.  No features may depend on runtime support from server-side code.

### Overview<a id="CssResource_Overview"></a>

1.  Write a CSS file, with or without GWT-specific extensions
2.  If GWT-specific extensions are used, define a custom subtype of CssResource
3.  Declare a method that returns CssResource or a subtype in an ClientBundle
4.  When the bundle type is generated with `GWT.create()` a Java expression that evaluates to the contents of the stylesheets will be created
    *   Except in the simplest case where the Java expression is a string literal, it is generally not the case that a CSS file could be generated into the module output
5.  At runtime, call `CssResource.ensureInjected()` to inject the contents of the stylesheet
    *   This method is safe to call multiple times, as subsequent invocations will be a no-op
    *   The recommended pattern is to call `ensureInjected()` in the static initializer of your various widget types

### Features<a id="Features"></a>

#### Constants<a id="Constants"></a>

```css
@def small 1px;
@def black #000;
border: small solid black;
```

*   The parse rules make it difficult to use delimiting tokens for substitutions
*   Redefining built-in sizes allows users to write plain CSS to draft a style and then tweak it.
*   Suggest that users use upper-case names, similar to static final members.
*   Any legal property value or expression may be used with `@def`
*   `@def` rules that define a single numeric value may be accessed in a manner similar to obfuscated class names by defining an accessor method on the CssResource type that returns a primitive numeric value.

```java
interface MyResources extends CssResource {
  int small();
}
```

*   Calling `small()` would return the value `1`.

*   @def` rules can be accessed as a String as well.  You can retrieve the two definitions above with:

```java
interface MyResources extends CssResource {
  String small();
  String black();
}
```

*   Calling `small()` returns "1px"
*   Calling `black()` returns "#000"
*   The Generator will not allow you to declare an `@def` rule with the same name as a class, unless you annotate method to retrieve the class with the `@ClassName` annotation.

```css
@def myIdent 10px;
.myIdent {
  ...
}
```

```java
interface MyResources extends CssResource {
  String myIdent();

  @ClassName("myIdent")
  String myIdentClass();
}
```

*   Calling `myIdent()` returns @def value "10px"
*   Calling `myIdentClass()` returns the obfuscated class name for .myIdent

#### Runtime substitution<a id="Runtime_substitution"></a>

```css
@eval userBackground com.module.UserPreferences.getUserBackground();
div {
  background: userBackground;
}
```

*   Provides runtime support for evaluating static methods when the stylesheet is injected.  Triggered / dynamic updates could be added in the future if we allow programmatic manipulation of the style elements.

*   If the user-defined function can be statically evaluated by the compiler, then the implementation of the specific CssResource should collapse to just a string literal.
*   This allows easy support for non-structural skinning changes.

#### Value function<a id="Value_function"></a>

```css
.myDiv {
  offset-left: value('imageResource.getWidth', 'px');
}
```

*   The `value()` function takes a sequence of dot-separated identifiers and an optional suffix.  The identifiers are interpreted as zero-arg method invocations, using the interface passed to GWT.create() as the root namespace.  By only allowing zero-arg methods, there's no need to attempt to perform type checking in the Generator.  The only validation necessary is to ensure that the sequence of methods exists.  There may be arbitrarily many identifiers in the chain.
*   The `value()` function may be combined with `@def`

```css
@def SPRITE_WIDTH value('imageResource.getWidth', 'px')

.selector {
  width: SPRITE_WIDTH;
}
```

#### Literal function<a id="Literal_function"></a>

Some user agents make use of property values that do not conform to the CSS grammar. The `literal()` function exists to allow these non-standard property values to be used.

```css
div-with-literal {
  top: literal("expression(document.compatMode==\"CSS1Compat\" ? documentElement.scrollTop : document.body.scrollTop \\ 2)");
}
```

Note that it is necessary to escape the backslash (`\`) and double-quote (`"`) characters.

#### Conditional CSS<a id="Conditional_CSS"></a>

```css
/* Runtime evaluation in a static context */
@if (com.module.Foo.staticBooleanFunction()) {
  ... css rules ...
}

/* Compile-time evaluation */
@if <deferred-binding-property> <space-separated list of values> {
  ... css rules ...
}
@if user.agent safari gecko1_8 { ... }
@if locale en { ... }

/* Negation is supported */
@if !user.agent ie6 opera {
  ...
}

/* Chaining is also supported */
@if (true) {
} @elif (false) {
} @else {
}
```

*   This allows for more advanced skinning / theming / browser quirk handling by allowing for structural changes in the CSS.
*   The contents of an @if block can be anything that would be a top-level rule in a CSS stylesheet.
*   @if blocks can be arbitrarily nested.
*   What does it mean to have an @def or @eval in an @if block?  Easy to make this work for property-based @if statements; would have to generate pretty gnarly runtime code to handle the expression-based @if statement.  Could have block-level scoping; but this seems like a dubious use-case.
*   If the function in the first form can be statically evaluated by the compiler in a permutation, there is no runtime cost. The second form will never have a runtime cost because it is evaluated during compilation.

#### Image Sprites<a id="Image_Sprites"></a>

```css

@sprite .mySpriteClass {gwt-image: "imageAccessor"; other: property;} => generates =>

  .mySpriteClass {
    background-image: url(gen.png);
    clip: ...;
    width: 27px;
    height: 42px;
    other: property;
  }
```

```java
interface MyCssResource extends CssResource {
  String mySpriteClass();
}

class MyResources extends ClientBundle {
  @Source("my.css")
  MyCssResource css();

  @Source("some.png")
  ImageResource imageAccessor();

  @Source("some.png")
  @ImageOptions(repeatStyle=RepeatStyle.Horizontal)
  ImageResource repeatingImage();
}
```

*   @sprite is sensitive to the FooBundle in which the CSSResource is declared; a sibling [ImageResource](/javadoc/latest/index.html?com/google/gwt/resources/client/ImageResource.html) method named in the @sprite declaration will be used to compose the background sprite.
*   @sprite entries will be expanded to static CSS rules, possibly with data: urls.
*   The expansion is sensitive to any RepeatStyle value defined on the `ImageResource` accessor function.  The appropriate `repeat-x` or `repeat-y` properties will be added to the @sprite selector.
*   Any CSS selector can be specified for @sprite.
*   Support for IE6 isn't feasible in this format, because structural changes to the DOM are necessary to implement a "windowing" effect.  Once it's possible to distinguish ie6 and ie7 in user.agent, we could revisit support for ie6.  In the current implementation, the ie6 code won't render correctly, although is a purely cosmetic issue.

#### References to Data Resources<a id="References_to_Data_Resources"></a>

```css
/* @url <constant name> <DataResource method name> */
@url myCursorUrl fancyCursorResource;

.myClass {
  cursor: myCursorUrl, pointer;
}
```

```java
interface MyResources extends ClientBundle {
  @Source("myCursor.cur")
  DataResource fancyCursorResource();

  @Source("my.css")
  CssResource css();
}
```

*   The identifier will be expanded to `url('some_url')` based on the return value of `DataResource.getUrl()`.

#### RTL support<a id="RTL_support"></a>

*   CssResource supports automatic transformations of CSS code into a right-to-left variant at compile time.
*   The use of the RTL variant is keyed by `com.google.gwt.i18n.client.LocaleInfo.getCurrentLocale().isRTL()`
*   Transformations applied:
    *   The `left` and `right` properties are flipped. </li>
    *   Any properties that have values `left` or `right` are flipped: `clear float text-align page-break-before page-break-after`
    *   The `background`/`background-position` property is flipped. Attachments expressed in percentage points are mirrored: 40% becomes 60%
    *   `margin padding border-color border-style` and `border-width` four-valued properties are flipped: `1px 2px 3px 4px` becomes `1px 4px 3px 2px`
    *   Any `xyz-right` or `xzy-right-abc` property is flipped to `xzy-left` or `xzy-left-abc`
    *   The `direction` property on a `body` selector will be flipped from `ltr` to `rtl`; on any other selectors, the `direction` property is unchanged
    *   When the cursor property has an `resize` value, it will be flipped: `ne-resize` becomes `nw-resize`
*   Sections of CSS can be exempted from automatic flipping by enclosing it in a `@noflip` block:

```css
@noflip {
  .selector {
    left: 10;
  }
}
```

*   A `background` property value that uses pixel-based offsets, such as `background-position: 4px 10px;` will not be transformed automatically.
    *   The four-valued CSS3 `background-position` property will be automatically flipped by the RTL support

         ```css
         background-position: left 4px top 10px;
         ```

    *   For CSS2 browsers, it will be necessary to use an `@sprite` rule:

    ```css
    @sprite .bgImage {
        gwt-image: 'background-image';
        position: absolute;
        left: 4px;
        top: 10px;
    }
    ```

*   `ImageResources` can be automatically flipped in RTL contexts via the use of the `@ImageOptions` annotation:

```java
@Source("icon128.png")
@ImageOptions(flipRtl = true)
ImageResource logo();
```

*   [Current auto-RTL test cases](https://github.com/gwtproject/gwt/blob/main/user/test/com/google/gwt/resources/rg)

#### Selector obfuscation<a id="Selector_obfuscation"></a>

java:

```java
    class Resources {
      MyCSSResource myCSSResource();
    }
    class MyCSSResource extends CSSResource {
      Sprite mySpriteClass();
      String someOtherClass();
      String hookClass();
    }
    myWidget.addStyleName(resource.mySpriteClass());
```

css:

```css
    @sprite mySpriteClass mySpriteImage;
    .someOtherClass {
      /* ... */
    }
    .hookClass{} /* Empty and stripped, but left for future expansion */
```

*   The function just returns the CSS class name, but verifies that the CSS class exists in the stylesheet.
*   Accessing class names through the interface ensures that there can be no typos in code that consumes the `CssResource`.
*   For obfuscation, we'll use a Adler32 checksum of the source css file expressed in base36 as a prefix (7 chars). The developer can override this with the `CssResource.obfuscationPrefix` deferred-binding property.

*   `<set-configuration-property name="CssResource.obfuscationPrefix" value="empty" />` can be used for minimal-length selector names, but this is only recommended when the GWT module has total control over the page.
*   The `@external` at-rule can be used to selectively disable obfuscation for named selectors; see [external and legacy scopes](#External_and_legacy_scopes) for additional detail.

### Optimizations<a id="Optimizations"></a>

#### Basic minification<a id="Basic_minification"></a>

Basic minification of the CSS input results in the minimum number of bytes required to retain the original structure of the input.  In general, this means that comments, unnecessary whitespace, and empty rules are removed.

```css
.div {
  /* This is the default background color */
  background: blue;
}
.empty {}
```

would be transformed into

```css
.div{background:blue;}
```

#### Selector merging<a id="Selector_merging"></a>

Rules with identical selectors can be merged together.

```css
.div {prop: value;}
.div {foo: bar;}
```

becomes

```css
.div {prop:value;foo:bar;}
```

However, it is necessary that the original semantic ordering of the properties within the CSS is preserved.  To ensure that all selector merges are correct, we impose the restriction that <strong>no rule can be promoted over another if the two rules define a common property</strong>.  We consider `border` and `border-top` to be equivalent properties, however `padding-left` and `padding-right` are not equivalent.

Thus

```css
.a {background: green;}
.b {border: thin solid blue;}
.a {border-top: thin solid red;}
```

cannot be merged because an element whose CSS class matches both `.a` and `.b` would be rendered differently based on the exactly order of the CSS rules.

When working with `@if` statements, it is preferable to work with the form that operates on deferred-binding properties because the CSS compiler can evaluate these rules statically, before the merge optimizations.  Consider the following:

```css
.a {
  background: red;
}

@if user.agent safari {
  .a {
    \-webkit-border-radius: 5px;
  }
} @else {
  .a {
    background: url('picture_of_border.png');
  }
}
```

In the safari permutation, the rule becomes `.a{background:red;\-webkit-border-radius:5px;}` while in other permutations, the `background` property is merged.

#### Property merging<a id="Property_merging"></a>

Rules with identical properties can be merged together.

```css
.a {background: blue;}
.b {background: blue;}
```

can be transformed into

```css
.a,.b{background:blue;}
```

Promotion of rules follows the previously-established rule of not promoting a rule over other rules with common properties.

### Levers and Knobs<a id="Levers_and_Knobs"></a>

*   The configuration property `CssResource.style` may be set to `pretty` which will disable class-name obfuscation as well as pretty-print the CSS content.  Combine this with a `ClientBundle.enableInlining` value of `false` to produce a CSS expression which is amenable to client-side editing.
*   The configuration property `CssResource.mergeEnabled` can be set to `false` to disable modifications that re-order rules.  This should be considered a temporary measure until the merge logic has been fully vetted.
*   To allow for client-side tweaking of the effective (i.e. permutation-specific) style rules, you can store the value of `CssResource`.getText() into a TextArea.  Wire some UI action to pass the contents of the TextArea into `StyleInjector.setContents()` to overwrite the original, injected stylesheet.

### Selector obfuscation details<a id="Selector_obfuscation_details"></a>

#### Strict scoping<a id="Strict_scoping"></a>

In the normal case, any class selectors that do not match String accessor functions is an error.  This behavior can be disabled by adding a `@NotStrict` annotation to the CSS accessor method.  Enabling `@NotStrict` behavior is only recommended for applications that are transitioning from external CSS files to `CssResource`.

```java
interface MyCssResource extends CssResource {
  String foo();
}

interface Resources {
  @Source("my.css")
  @CssResource.NotStrict
  MyCssResource css();
}
```

```css
/* This is ok */
.foo {}

/* This would normally generate a compile error in strict mode */
.other {}
```

#### Scope<a id="Scope"></a>

Scoping of obfuscated class names is defined by the return type of the `CssResource` accessor method in the resource bundle.  Each distinct return type will return a wholly separate collection of values for String accessor methods.

```java
interface A extends CssResource {
  String foo();
}

interface B extends A {
  String foo();
}

interface C extends A {
  String foo();
}

interface D extends C {
  // Intentionally not defining foo()
}

interface Resources {
  A a();
  A a2();
  B b();
  C c();
  D d();
  D d2();
```

It will be true that a().foo() != b().foo() != c().foo() != d().foo().  However, a().foo() == a2().foo() and d().foo() == d2().foo(). 

#### Shared scopes<a id="Shared_scopes"></a>

In the case of "stateful" CSS classes like `focused` or `enabled`, it is convenient to allow for certain String accessor functions to return the same value, regardless of the `CssResource` type returned from the accessor method.

```java
@Shared
interface FocusCss extends CssResource {
  String focused();
  String unfocused();
}

interface A extends FocusCss {
  String widget();
}

interface B extends FocusCss {
  String widget();
}

interface C extends B {
  // Intentionally empty
}

interface Resources {
  A a();
  B b();
  C c();
  FocusCss f();
}
```

In this example, a().focused() == b().focused() == c().focused == f().focused().  However, a().widget() != b().widget != c.widget(), as in the previous example.

The short version is that if distinct CSS types need to share obfuscated class names, the `CssResource` subtypes to which they are attached must share a common supertype that defines accessors for those names and has the `@Shared` annotation.

#### Imported scopes<a id="Imported_scopes"></a>

The Java type system can be somewhat ambiguous when it comes to multiple inheritance of interfaces that define methods with identical signatures, although there exist a number of cases where it is necessary to refer to multiple, unrelated `CssResource` types.  Consider the case of a Tree that contains Checkboxes.

```java
@ImportedWithPrefix("tree")
interface TreeCss extends CssResource {
  String widget();
}

@ImportedWithPrefix("checkbox")
interface CbCss extends CssResource {
  String widget();
}

interface MyCss extends CssResource {
  String other();
}

interface Resources extends ClientBundle {
  @Import({TreeCss.class, CbCss.class})
  MyCss css();
}
```

```css
/* Now we can write a descendant selector using the prefixes defined on the CssResource types */
.tree-widget .checkbox-widget {
  color: red;
}

.other {
  something: else;
}
```

Composing a "TreeCbCss" interface would be insufficient because consumers of the TreeCss interface and CbCss interface would receive the same value from the widget method.  Moreover, the use of just `.widget` in the associated CSS file would also be insufficient without the use of some kind of class selector prefix.  The prefix is defined on the `CssResource` type (instead of on the `CssResource` accessor method) In the interest of uniformity across all CSS files that import a given scope.  It is a compile-time error to import multiple classes that have the same prefix or simple name.

The case of shared scopes could be handled solely with importing scopes, however this form is somewhat more verbose and relationships between unrelated scopes is less common than the use of stateful selectors.

##### Example: StackPanel inside a StackPanel<a id="Example_StackPanel"></a>

This is a use-case that is currently impossible to style correctly in GWT.

```java
// Assume this interface is provided by the UI library
interface StackPanelCss extends CssResource {
  String widget();
  // and many more class names
}

// App code defines the following interfaces:

@ImportedWithPrefix("inner")
interface StackPanelInner extends StackPanelCss {
  // Empty interface
}

interface StackPanelOuter extends StackPanelCss {
  // Empty interface
}

interface Resources {
  @Source("stackPanel.css")
  StackPanelInner inner();

  @Import(StackPanelInner.class)
  @Source("stackPanel.css", "outer.css")
  StackPanelOuter outer();
}
```

The file `stackPanel.css` defines the basic structure of any given stackPanel:

```css
.widget .title {}
.widget .content {}
/* Other stuff to make a StackPanel work */
```

The `outer()` method can continue to use the base `stackPanel.css` file, because the accessor methods defined in `StackPanelCss` are mapped into the default (no-prefix) namespace.  The inner StackPanel's style members are also available, but in the `inner` prefix.  Here's what `outer.css` might contain:

```css
.widget {color: red;}

.inner-widget {
  color: blue;
  font-size: smaller;
}
```

External and legacy scopes<a id="External_and_legacy_scopes"></a>

In many cases, newly-developed CSS will need to be combined with external or legacy CSS. The `@external` at-rule can be used to suppress selector obfuscation while still allowing programmatic access to the selector name.

```java
interface MyCssResource extends CssResource {
  String obfuscated();
  String legacySelectorA();
}

interface Resource extends ClientBundle {
  @Source("my.css")
  MyCssResource css();
}
```

```css
@external legacySelectorA, legacySelectorB;
.obfuscated .legacySelectorA { .... }
.obfuscated .legacySelectorB { .... }
```

In the above example, the `.obfuscated` class selector will be obfuscated, and the `obfuscated()` method will return the replaced name. Neither of the legacy selectors will be obfuscated and the `legacySelectorA()` method will return the unobfuscated value.  Furthermore, because the `legacySelectorB` is explicitly defined in the `@external` declaration, the inaccessible class name will not trigger an error.

### Automatically generating `CssResource` interfaces<a id="Automatically_generating_interfaces"></a>

A utility is included in the GWT distribution which will analyze a `CssResource`-compatible CSS file and create a corresponding Java interface for accessing the classnames used in the file.

```shell
java -cp gwt-dev.jar:gwt-user.jar com.google.gwt.resources.css.InterfaceGenerator \
  -standalone -typeName some.package.MyCssResource -css input.css
```

The generated interface will be emitted to `System.out`. The `-standalone` option will add the necessary `package` and `import` statements to the output so that it can be used as part of a build process.

## CssResourceCookbook<a id="CssResourceCookbook"></a>

This section contains examples showing how to use [CssResource](#CssResource).

### Browser-specific css<a id="Browser-specific_css"></a>

```css
.foo {
  background: green;
}

@if user.agent ie6 {
  /* Rendering fix */
  .foo {
    position: relative;
  }
} @elif user.agent safari {
  .foo {
    \-webkit-border-radius: 4px;
  }
} @else {
  .foo {
    font-size: x-large;
  }
}
```


### Obfuscated CSS class names<a id="Obfuscated_CSS_class_names"></a>

`CssResource` will use method names as CSS class names to obfuscate at runtime.

```java
interface MyCss extends CssResource {
  String className();
}

interface MyResources extends ClientBundle {
  @Source("my.css")
  MyCss css();
}
```

All instances of a selector with `.className` will be replaced with an obfuscated symbol when the CSS is compiled.  To use the obfuscated name:

```java
MyResources resources = GWT.create(MyResources.class);
Label l = new Label("Some text");
l.addStyleName(resources.css().className());
```

If you have class names in your css file that are not legal Java identifiers, you can use the `@ClassName` annotation on the accessor method:

```java
interface MyCss extends CssResource {
  @ClassName("some-other-name")
  String someOtherName();
}
```


### Background images / Sprites<a id="Background_images_/_Sprites"></a>

`CssResource` reuses the `ImageResource` bundling techniques and applies them to CSS background images.  This is generally known as "spriting" and a special `@sprite` rule is used in `CssResource`.

```java
interface MyResources extends ClientBundle {
  @Source("image.png")
  ImageResource image();

  @Source("my.css");
  CssResource css();
}
```

In `my.css`, sprites are defined using the `@sprite` keyword, followed by an arbitrary CSS selector, and the rule block must include a `gwt-image` property.  The `gwt-image` property should name the `ImageResource` accessor function.

```java
@sprite .myImage {
  gwt-image: 'image';
}
```

The elements that match the given selection will display the named image and have their heights and widths automatically set to that of the image.

#### Tiled images<a id="Tiled_images"></a>

If the `ImageResource` is decorated with an `@ImageOptions` annotation, the source image can be tiled along the X- or Y-axis.  This allows you to use 1-pixel wide (or tall) images to define borders, while still taking advantage of the image bundling optimizations afforded by `ImageResource`.

```java
interface MyResources extends ClientBundle {
  @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
  @Source("image.png")
  ImageResource image();
}
```

The elements that match the `@sprite`'s selector will only have their height or width set, based on the direction in which the image is to be repeated.

#### 9-boxes<a id="9-boxes"></a>

In order to make the content area of a 9-box have the correct size, the height and widths of the border images must be taken into account.  Instead of hard-coding the image widths into your CSS file, you can use the `value()` CSS function to insert the height or width from the associated `ImageResource`.

```java
public interface Resources extends ClientBundle {
    Resources INSTANCE = GWT.create(Resources.class);

    @Source("bt.png")
    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    ImageResource bottomBorder();

    @Source("btl.png")
    ImageResource bottomLeftBorder();

    @Source("btr.png")
    ImageResource bottomRightBorder();

    @Source("StyleInjectorDemo.css")
    CssResource css();

    @Source("lr.png")
    @ImageOptions(repeatStyle = RepeatStyle.Vertical)
    ImageResource leftBorder();

    @Source("rl.png")
    @ImageOptions(repeatStyle = RepeatStyle.Vertical)
    ImageResource rightBorder();

    @Source("tb.png")
    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    ImageResource topBorder();

    @Source("tbl.png")
    ImageResource topLeftBorder();

    @Source("tbr.png")
    ImageResource topRightBorder();
  }
```

```css
.contentArea {
  padding: value('topBorder.getHeight', 'px') value('rightBorder.getWidth', 'px')
      value('bottomBorder.getHeight', 'px') value('leftBorder.getWidth', 'px');
}

@sprite .contentAreaTopLeftBorder {
  gwt-image: 'topLeftBorder';
  position: absolute;
  top:0;
  left: 0;
}

@sprite .contentAreaTopBorder {
  gwt-image: 'topBorder';
  position: absolute;
  top: 0;
  left: value('topLeftBorder.getWidth', 'px');
  right: value('topRightBorder.getWidth', 'px');
}

@sprite .contentAreaTopRightBorder {
  gwt-image: 'topRightBorder';
  position: absolute;
  top:0;
  right: 0;
}

@sprite .contentAreaBottomLeftBorder {
  gwt-image: 'bottomLeftBorder';
  position: absolute;
  bottom: 0;
  left: 0;
}

@sprite .contentAreaBottomBorder {
  gwt-image: 'bottomBorder';
  position: absolute;
  bottom: 0;
  left: value('bottomLeftBorder.getWidth', 'px');
  right: value('bottomRightBorder.getWidth', 'px');
}

@sprite .contentAreaBottomRightBorder {
  gwt-image: 'bottomRightBorder';
  position: absolute;
  bottom: 0;
  right: 0;
}

@sprite .contentAreaLeftBorder {
  gwt-image: 'leftBorder';
  position: absolute;
  top: 0;
  left: 0;
  height: 100%;
}

@sprite .contentAreaRightBorder {
  gwt-image: 'rightBorder';
  position: absolute;
  top: 0;
  right: 0;
  height: 100%;
}
```

```html
<div class="contentArea">

<div class="contentAreaTopLeftBorder"></div>
<div class="contentAreaTopBorder"></div>
<div class="contentAreaTopRightBorder"></div>
<div class="contentAreaBottomLeftBorder"></div>
<div class="contentAreaBottomBorder"></div>

<div class="contentAreaBottomRightBorder"></div>
<div class="contentAreaLeftBorder"></div>
<div class="contentAreaRightBorder"></div>
</div>
```
