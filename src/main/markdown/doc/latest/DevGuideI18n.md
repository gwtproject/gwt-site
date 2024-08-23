i18n
===

GWT includes a flexible set of tools to help you internationalize your applications and libraries. GWT internationalization support provides a variety of techniques to
internationalize strings, typed values, and classes.

_**Note:** To run through the steps to internationalize a sample GWT app, see the tutorial [Internationalizing a GWT Application](tutorial/i18n.html)._

1.  [Locales in GWT](#DevGuideLocale)
2.  [Static String Internationalization](#DevGuideStaticStringInternationalization)
3.  [Dynamic String Internationalization](#DevGuideDynamicStringInternationalization)
4.  [Java Annotations](#DevGuideAnnotations)
5.  [Localized Properties Files](#DevGuidePropertiesFiles)

### Quick Start with Internationalization

GWT supports a variety of ways of internationalizing your code. Start by researching which approach best matches your development requirements.

*   **Are you using UiBinder?**

If so, you will probably want to read up on [UiBinder's I18n](DevGuideUiBinderI18n.html) support.

*   **Are you writing code from scratch?**

    If so, you will probably want to read up on GWT's [static string internationalization](DevGuideI18n.html#DevGuideStaticStringInternationalization)
techniques.

*   **Do you want to store non-String localized values
?**

    Use the [Constants](/javadoc/latest/com/google/gwt/i18n/client/Constants.html)
or [ConstantsWithLookup](/javadoc/latest/com/google/gwt/i18n/client/ConstantsWithLookup.html) interfaces, which
allow types such as primitives, String arrays, and String maps.

*   **Do you need to substitute parameters into the translated
messages?**

    Use [Messages](/javadoc/latest/com/google/gwt/i18n/client/Messages.html).

*   **Do you have existing localized properties files you'd like to reuse?**

    The i18nCreator tool can automatically generate interfaces that extend either [Constants](/javadoc/latest/com/google/gwt/i18n/client/Constants.html), [ConstantsWithLookup](/javadoc/latest/com/google/gwt/i18n/client/ConstantsWithLookup.html) or [Messages](/javadoc/latest/com/google/gwt/i18n/client/Messages.html).

*   **Are you adding GWT functionality to an existing web application that already has a localization process defined?**

    [Dictionary](/javadoc/latest/com/google/gwt/i18n/client/Dictionary.html) will help you interoperate with
existing pages without requiring you to use [GWT's concept of locale](DevGuideI18nLocale.html#LocaleSpecifying).

*   **Do you really just want a simple way to get properties files down to the client regardless of localization?**

    You can do that, too. Try using [Constants](/javadoc/latest/com/google/gwt/i18n/client/Constants.html) and just not having any locale-specific property files.

### Internationalization Techniques

GWT offers multiple internationalization techniques to afford maximum flexibility to GWT developers and to make it possible to design for efficiency, maintainability,
flexibility, and interoperability in whichever combinations are most useful.

*   **[Static string internationalization](DevGuideI18n.html#DevGuideStaticStringInternationalization)**

    A family of efficient and type-safe techniques that rely on strongly-typed Java interfaces, [properties files](DevGuideI18n.html#DevGuidePropertiesFiles), and
code generation to provide locale-aware messages and configuration settings.
These techniques depend on the interfaces [Constants](/javadoc/latest/com/google/gwt/i18n/client/Constants.html),
[ConstantsWithLookup](/javadoc/latest/com/google/gwt/i18n/client/ConstantsWithLookup.html),
and [Messages](/javadoc/latest/com/google/gwt/i18n/client/Messages.html).

*   **[Dynamic string internationalization](DevGuideI18n.html#DevGuideDynamicStringInternationalization)**

    A simple and flexible technique for looking up localized values defined in a module's [host page](DevGuideOrganizingProjects.html#DevGuideHostPage) without needing to recompile
your application. This technique is supported by the class [Dictionary](/javadoc/latest/com/google/gwt/i18n/client/Dictionary.html).

*   **Extending or implementing Localizable**

    Provides a method for internationalizing sets of algorithms using locale-sensitive type substitution. This is an advanced technique that you probably will not need to use
directly, although it is useful for implementing complex internationalized libraries. For details on this technique, see the [Localizable](/javadoc/latest/com/google/gwt/i18n/client/Localizable.html) class documentation.

### The I18N Module

Core types related to internationalization:

*   [`LocaleInfo`](/javadoc/latest/com/google/gwt/i18n/client/LocaleInfo.html)
Provides information about the current locale.

*   [`Constants`](/javadoc/latest/com/google/gwt/i18n/client/Constants.html) Useful for localizing typed constant
values

*   [`Messages`](/javadoc/latest/com/google/gwt/i18n/client/Messages.html) Useful for localizing messages
requiring arguments

*   [`ConstantsWithLookup`](/javadoc/latest/com/google/gwt/i18n/client/ConstantsWithLookup.html) Like [`Constants`](/javadoc/latest/com/google/gwt/i18n/client/Constants.html) but with extra lookup flexibility for highly
data-driven applications

*   [`Dictionary`](/javadoc/latest/com/google/gwt/i18n/client/Dictionary.html) Useful when adding a GWT module to
existing localized web pages

*   [`Localizable`](/javadoc/latest/com/google/gwt/i18n/client/Localizable.html) Useful for localizing algorithms
encapsulated in a class or when the classes above don't provide sufficient control

*   [`DateTimeFormat`](/javadoc/latest/com/google/gwt/i18n/client/DateTimeFormat.html) Formatting dates as
strings. See the section on [date and number formatting](DevGuideCodingBasics.html#DevGuideDateAndNumberFormat).

*   [`NumberFormat`](/javadoc/latest/com/google/gwt/i18n/client/NumberFormat.html) Formatting numbers as strings.
See the section on [date and number formatting](DevGuideCodingBasics.html#DevGuideDateAndNumberFormat).

## Locales in GWT<a id="DevGuideLocale"></a>

GWT is different than most toolkits by performing most locale-related work
at compile time rather than runtime.  This allows GWT to do compile-time
error checking, such as when a parameter is left out or the translated
value is not of the correct type, and for optimizations to take into account
known facts about the locale.  This also allows an end user to download
only the translations that are relevant for them.

For details on configuring locales in your GWT application, see the
[detailed locale documentation](DevGuideI18nLocale.html).

## Static String Internationalization<a id="DevGuideStaticStringInternationalization"></a>

Static string internationalization is the most efficient way to localize your application for different locales in terms of runtime performance. This approach is called
"static" because it refers to creating tags that are matched up with human readable strings at compile time. At compile time, mappings between tags and strings are created for all
languages defined in the module. The module startup sequence maps the appropriate implementation based on the locale setting using [deferred binding](DevGuideCodingBasics.html#DevGuideDeferredBinding).

Static string localization relies on code generation from standard Java [properties files](DevGuideI18n.html#DevGuidePropertiesFiles) or
[annotations in the Java source](DevGuideI18n.html#DevGuideAnnotations). GWT supports static string localization through three tag interfaces
(that is, interfaces having no methods that represent a functionality contract)
and a code generation library to generate implementations of those interfaces.

### Extending the Constants Interface

The [`Constants`](DevGuideI18nConstants.html) interface
allows you to localize constant values in a type-safe manner, all resolved
at compile time.  At some cost of runtime overhead, you can also allow runtime
lookup by key names with the `ConstantsWithLookup` interface.

### Using the Messages Interface

The [`Messages`](DevGuideI18nMessages.html) interface
allows you to substitute parameters into messages and to even re-order those
parameters for different locales as needed. The format of the messages in the
properties files follows the specification in Java [MessageFormat](http://java.sun.com/j2se/1.5.0/docs/api/java/text/MessageFormat.html).
The interface you create will contain a Java
method with parameters matching those specified in the format string.

In addition, the `Messages` interface supports [Plural Forms](DevGuideI18nPluralForms.html) to allow your application
to accurately reflect text changes based on the count of something.

### Which Interface to Use?

Here are some guidelines to help choose the right interface for your application's needs:

*   Extend [`Constants`](/javadoc/latest/com/google/gwt/i18n/client/Constants.html)
to create a collection of constant values of a variety of types that can be
accessed by calling methods (called _constant accessors_) on an interface.
Constant accessors may return a variety of types, including strings, numbers,
booleans, and even maps. A compile-time check is done to ensure that the value
in a properties file matches the return type declared by its corresponding
constant accessor. In other words, if a constant accessor is declared to
return an `int`, its associated property is guaranteed to be a valid
`int` value &mdash; avoiding a potential source of runtime errors.

*   The [`ConstantsWithLookup`](/javadoc/latest/com/google/gwt/i18n/client/ConstantsWithLookup.html)
interface is identical to `Constants` except that the interface also
includes a method to look up values by property name, which facilitates
dynamic binding to constants by name at runtime. `ConstantsWithLookup`
can sometimes be useful in highly data-driven applications. One caveat:
`ConstantsWithLookup` is less efficient than `Constants`
because the compiler cannot discard unused constant methods, resulting in
larger applications and the lookup cannot be resolved at compile-time.

*   Extend [`Messages`](/javadoc/latest/com/google/gwt/i18n/client/Messages.html) to create a collection of
formatted messages that can accept parameters. You might think of the
`Messages` interface as a statically verifiable equivalent of the
traditional Java combination of `Properties`, `ResourceBundle`,
and `MessageFormat` rolled into a single mechanism.

### Properties Files

All of the types above use properties files based on the traditional [Java properties file format](http://java.sun.com/j2se/1.5.0/docs/api/java/util/Properties.html#load\(java.io.InputStream\)),
although GWT uses [an enhanced properties file format](DevGuideI18n.html#DevGuidePropertiesFiles)
that allows for UTF-8 and therefore allows properties files to contain Unicode characters directly.

## Dynamic String Internationalization<a id="DevGuideDynamicStringInternationalization"></a>

For existing applications that may not support the GWT `locale`
client property, GWT offers dynamic string internationalization to easily
integrate GWT internationalization.

The [`Dictionary`](/javadoc/latest/com/google/gwt/i18n/client/Dictionary.html) class lets your GWT application
consume strings supplied by the [host HTML page](DevGuideOrganizingProjects.html#DevGuideHostPage). This approach is convenient if your existing web server has a localization
system that you do not wish to integrate with the [static string internationalization](DevGuideI18n.html#DevGuideStaticStringInternationalization) methods.
Instead, simply print your strings within the body of your HTML page as a JavaScript structure, and your GWT application can reference and display them to end users. Since it
binds directly to the key/value pairs in the host HTML, whatever they may be,
the [`Dictionary`](/javadoc/latest/com/google/gwt/i18n/client/Dictionary.html)
class is not sensitive to the [GWT locale setting](DevGuideI18nLocale.html#LocaleSpecifying). Thus,
the burden of generating localized strings is on your web server.

Dynamic string localization allows you to look up localized strings defined in a [host HTML](DevGuideOrganizingProjects.html#DevGuideHostPage) page at runtime using
string-based keys. This approach is typically slower and larger than the static string approach, but does not require application code to be recompiled when messages are altered
or the set of locales changes.

_**Tip:** The `Dictionary` class is completely dynamic, so it provides no static type checking, and invalid keys cannot be checked by the compiler. This is
another reason we recommend using [static string internationalization](DevGuideI18n.html#DevGuideStaticStringInternationalization) where
possible._

## Java Annotations<a id="DevGuideAnnotations"></a>

The recommended approach for specifying the default values for
`Constants` or `Messages` interfaces is using Java annotations.
The advantage of this approach is that you can keep the values with the
source, so when refactoring the interface or creating new methods in your IDE
it is easier to keep things up to date.  Also, if you are using a
custom key generator or generating output files for translation, you
need to use annotations.

The annotations that apply everywhere are discussed here &mdash; for annotations
that are only used on [`Constants`](DevGuideI18nConstants.html#ConstantsAnnotations)
and [`Messages`](DevGuideI18nMessages.html#MessagesAnnotations) are
discussed there.

### Class Annotations

The following annotations apply to classes or interfaces:


* [`@DefaultLocale(String localeName)`](/javadoc/latest/com/google/gwt/i18n/client/LocalizableResource.DefaultLocale.html)
Specifies that text contained in this file is of the specified locale.  If not
specified, the default is `en`.

* [`@GeneratedFrom(String fileName)`](/javadoc/latest/com/google/gwt/i18n/client/LocalizableResource.GeneratedFrom.html)
Indicates that this file was generated from the supplied file. Note that it
is not required that this file name be resolvable at compile time, as this
file may have been generated on a different machine, etc. If the generator
does check the source file, such as for staleness, it must not give any
warning if the file is not present or if the name is not resolvable.

* [`@GenerateKeys(String generatorFQCN)`](/javadoc/latest/com/google/gwt/i18n/client/LocalizableResource.GenerateKeys.html)
Requests that the keys for each method be generated with the specified
generator (see below). If this annotation is not supplied, keys will be the
name of the method, and if specified without a parameter it will default to
the MD5 implementation.

 The specified generator class must implement the
[`KeyGenerator `](/javadoc/latest/com/google/gwt/i18n/rebind/keygen/KeyGenerator.html)interface. By specifying a fully-qualified class name,
this will be extensible to other formats not in the GWT namespace. The
user just has to make sure the specified class is on the class path at
compilation time. 
This allows integration with non-standard or internal tools that may use their own
hash functions to coalesce duplicate translation strings between multiple
applications or otherwise needed for compatibility with external tools.

 A string containing the fully-qualified class name is used instead of a
class literal because the key generation algorithm is likely to pull in code
that is not translatable, so cannot be seen directly in client code.

 If this annotation is not supplied, the key will be the simple name of the
method.

* [`@Generate(String[] formatFQCN, String filename, String[] locales)`](/javadoc/latest/com/google/gwt/i18n/client/LocalizableResource.Generate.html)
Requests that a message catalog file is generated during the compilation
process. If the filename is not supplied, a default name based on the
interface name is used. The output file is created under the -out directory.
The format names are the fully-qualified class names which implement the [`MessageCatalogFormat`](/javadoc/latest/com/google/gwt/i18n/rebind/format/MessageCatalogFormat.html) interface. For example, this could generate an
XLIFF or properties file based on the information contained in this file.
Specific `MessageCatalogFormat` implementations may define additional
annotations for additional parameters needed for that `MessageCatalogFormat`.

 If any locales are specified, only the listed
locales are generated. If exactly one locale is listed, the filename supplied
(or generated) will be used exactly; otherwise `locale` will
be added before the file extension.

 A string containing the fully-qualified class name is used instead of a
class literal because the message catalog implementation is likely to pull in
code that is not translatable, so cannot be seen directly in client code.


### Method Annotations
The following annotations apply to methods:

* [`@Key(String key)`](/javadoc/latest/com/google/gwt/i18n/client/LocalizableResource.Key.html)
Specifies the key to use in the external format for this particular method.
If not supplied, it will be generated based on the `@GenerateKeys`
annotation, discussed above.
* [`@Description(String desc)`](/javadoc/latest/com/google/gwt/i18n/client/LocalizableResource.Description.html)
A description of the text. Note that this is not included in a hash of the
text and depending on the file format may not be included in a way visible to
a translator.
* [`@Meaning(String meaning)`](/javadoc/latest/com/google/gwt/i18n/client/LocalizableResource.Meaning.html)
Supplies a meaning associated with this text. This information is provided to
the translator to distinguish between different possible translations &mdash;
for example, orange might have meaning supplied as "the fruit" or "the color".
Note that two messages with identical text but different meanings should have
different keys, as they may be translated differently.

## Localized Properties Files<a id="DevGuidePropertiesFiles"></a>

[Static string internationalization](DevGuideI18n.html#DevGuideStaticStringInternationalization) uses traditional Java `.properties` files to manage
translating tags into localized values. These files may be placed into the same package as your main module class. They must be placed in the same package as their corresponding
`Constants`/`Messages` subinterface definition file.

**Tip:** Use the [i18nCreator](RefCommandLineTools.html#i18nCreator) script to get started.

```text
$ i18nCreator -eclipse Foo com.example.foo.client.FooConstants
  Created file src/com/example/foo/client/FooConstants.properties
  Created file FooConstants-i18n.launch
  Created file FooConstants-i18n
```

Both [Constants](/javadoc/latest/com/google/gwt/i18n/client/Constants.html) and [Messages](/javadoc/latest/com/google/gwt/i18n/client/Messages.html) use traditional Java properties files, with one notable difference: properties files used with GWT should be encoded as UTF-8 and may contain Unicode characters directly, avoiding the need for `native2ascii`. See the
API documentation for the above interfaces for examples and formatting details.

In order to use internationalized characters, make sure that your host HTML page is served as UTF-8, the easiest way is to include a meta tag in the page's head:

```html
 <meta charset="utf-8" />
```

You must also ensure that all relevant source and `.properties` files are set to be in the UTF-8 charset in your IDE.
