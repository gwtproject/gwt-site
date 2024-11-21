i18n Locale
===

1.  [Overview](#LocaleOverview)
2.  [Client Properties and the GWT Compilation Process](#LocaleProperty)
3.  [Adding Locale Choices to a Module](#LocaleModule)
4.  [The Default Locale](#LocaleDefault)
5.  [Specifying the Locale to Load](#LocaleSpecifying)
6.  [Runtime Locales](#RuntimeLocales)
7.  [Creating a New Property Provider](#LocaleProvider)
8.  [Programmatic Access to Locale Information](#LocaleInfo)
9.  [Server/Generator Manipulation of Locales](#ServerLocales)

## Overview<a id="LocaleOverview"></a>

GWT represents `locale` as a client property whose value can be set either using a meta tag embedded in the [host
page](DevGuideOrganizingProjects.html#DevGuideHostPage) or in the query string of the host page's URL. Rather than being supplied by GWT, the set of possible values for the `locale` client property is entirely a
function of your [module configuration](DevGuideOrganizingProjects.html#DevGuideModules).

## Client Properties and the GWT Compilation Process<a id="LocaleProperty"></a>

_Client properties_ are key/value pairs that can be used to configure GWT modules. User agent, for example, is represented by a client property. Each client property can
have any number of values, but all of the values must be enumerable when the GWT compiler runs. GWT modules can define and extend the set of available client properties along with
the potential values each property might assume when loaded in an end user's browser using the `<extend-property>` directive. At compile time, the GWT compiler
determines all the possible permutations of a module's client properties, from which it produces multiple _compilations_. Each compilation is optimized for a different set of
client properties and is recorded into a file ending with the suffix `.cache.html`.

In deployment, the end-user's browser only needs one particular compilation,
which is determined by mapping the end user's client properties onto the
available compiled permutations. Thus, only the exact code required by the end
user is downloaded, no more. By making locale a client property, the standard
startup process in `<module>.nocache.js` chooses the appropriate
localized version of an application, providing ease of use, optimized
performance, and minimum script size. See the
[Knowledge Base](FAQ_DebuggingAndCompiling.html#What) for more information about the logic of the `<modulename>.nocache.js` file.

## Adding Locale Choices to a Module<a id="LocaleModule"></a>

In any real-world application, you will define at least one locale in addition to the default locale. "Adding a locale" means extending the set of values of the `locale`
client property using the `<extend-property>` element in your [module XML](DevGuideOrganizingProjects.html#DevGuideModuleXml). For
example, the following module adds multiple locale values:

```xml
<module>
  <inherits name="com.google.gwt.user.User"/>
  <inherits name="com.google.gwt.i18n.I18N"/>
  
  <!-- French language, independent of country -->
  <extend-property name="locale" values="fr"/>

  <!-- French in France -->
  <extend-property name="locale" values="fr_FR"/>

  <!-- French in Canada -->
  <extend-property name="locale" values="fr_CA"/>
  
  <!-- English language, independent of country -->
  <extend-property name="locale" values="en"/>
</module>
```

## The Default Locale<a id="LocaleDefault"></a>

The `com.google.gwt.i18n.I18N` module defines only one locale by default, called `default`. This default locale is used when the `locale` client property
goes unspecified in deployment. The default locale is used internally as a last-resort match between a [Localizable](/javadoc/latest/com/google/gwt/i18n/client/Localizable.html) interface and a localized resource or
class.

In general, you should avoid running the app in the `default` locale
&mdash; many things will produce surprising results.  For example, only a small set of
currencies will be supported, resulting in errors for applications that make use of
other currencies, and no plural forms will be supported (since
the language isn't known).  If you really want to allow the application
to continue running when the user requests an unsupported locale, you
are probably better off choosing some real language as a default, such as
`en`.  You can set what value is used for the default by including
the following in your [module XML](DevGuideOrganizingProjects.html#DevGuideModuleXml):

```xml
<extend-property name="locale" values="en,es,de,fr,it" /> <!-- 1. full list of supported locales --> 
<set-property name="locale" value="en,es,de,fr,it" />     <!-- 2. remove the 'default' locale permutation -->
<set-property-fallback name="locale" value="en" />        <!-- 3. set the fallback locale, in replacement for the default 'default' locale -->
```

## Specifying the Locale to Load<a id="LocaleSpecifying"></a>

The locale client property can be specified using either a meta tag or as part of the query string in the host page's URL. If both are specified, the query string takes
precedence. To specify the `locale` client property using a meta tag in the [host page](DevGuideOrganizingProjects.html#DevGuideHostPage), embed a meta tag for
`gwt:property` as follows:

```html
<meta name="gwt:property" content="locale=x_Y">
```

For example, the following host HTML page sets the locale to "ja_JP":

```html
<html>
  <head>
    <meta name="gwt:property" content="locale=ja_JP">
  </head>
  <body>
    <!-- Load the GWT compiled module code                           -->
    <script src="com.google.gwt.examples.i18n.ColorNameLookupExample.nocache.js " />
  </body>
</html>
```

To specify the `locale` client property using a query string, specify a value for the name `locale`. For example,

```text
http://www.example.org/myapp.html?locale=fr_CA
```

**Tip:** The preferred time to explicitly set `locale` is to do so before your GWT module is invoked. You can change the `locale` from within your GWT
module by adding or changing the `locale` query string in the current URL and reloading the page. Keep in mind that after reloading the page, your module will
restart.

## Runtime Locales<a id="RuntimeLocales"></a>

For cases where the translated values are the same, but you still want
country-specific details, you can use runtime locales to reduce the number
of compiled permutations, but still get country-specific details like the
default currency, number/date formatting rules, etc.

As an example, you might have one set of translations for all of Spanish
as spoken in Latin America (`es_419`), yet allow users to choose
a country-specific locale such as Argentinian Spanish (`es_AR`).

The easy way to use runtime locales is simply to add:

```xml
<inherits name="com.google.gwt.i18n.CldrLocales"/>
```

to your [module XML](DevGuideOrganizingProjects.html#DevGuideModuleXml) file, and all locales that GWT knows about that inherit from
your compile-time locale will be automatically included.  You can see the
result in the [`Showcase` sample application](https://samples.gwtproject.org/samples/Showcase/Showcase.html).

### Caveats

*   All the tables for all included runtime locales are included in the
 each appropriate compiled permutation, so this can increase download size.
*   The tables for non-obvious locale inheritance and aliases are too large
 to be included in the selection script, so inheritance won't work properly
 in all cases.  This means you either need to specifically control the set
 of possible locales, such as in the locale selector in the [`Showcase`
sample application](https://samples.gwtproject.org/samples/Showcase/Showcase.html), or have the server choose the locale using the
proper inheritance tables (`GwtLocaleFactoryImpl` will be helpful here,
and you will need a way to get the set of locales your application was built
with).
*   Only currency data, number format, and date/time formats are affected
 by runtime locales currently &mdash; everything else will only use the compile-time
 locale from the locale deferred binding property.

## Creating a New Property Provider<a id="LocaleProvider"></a>

If you are embedding your module into an existing application, there may be another way of determining locale that does not lend itself to using the `<meta>` tag
or specifying `locale=` as a query string. In this case, you could write your own property provider.

A property provider is specified in the [module XML file](DevGuideOrganizingProjects.html#DevGuideModuleXml) as a JavaScript fragment that will return the value for the
named property at runtime. In this case, you would want to define the locale property using a property provider. To see examples of `<property-provider>` definitions
in action, see the files [I18N.gwt.xml](https://github.com/gwtproject/gwt/blob/main/user/src/com/google/gwt/i18n/I18N.gwt.xml) and
[UserAgent.gwt.xml](https://github.com/gwtproject/gwt/blob/main/user/src/com/google/gwt/user/UserAgent.gwt.xml) in the GWT source code.

## Programmatic Access to Locale Information<a id="LocaleInfo"></a>

To get information about the current locale or the available set of
locales, see the [LocaleInfo](/javadoc/latest/com/google/gwt/i18n/client/LocaleInfo.html)
class.  For example:

*   To check if the current locale is a Right-to-Left locale:

```java
if (LocaleInfo.getCurrentLocale().isRTL()) {
  ...
}
```

*   To get a list of supported locales, such as for a locale selector:

```java
for (String localeName : LocaleInfo.getAvailableLocaleNames()) {
  String displayName = LocaleInfo.getLocaleNativeDisplayName(localeName);
  ...
}
```

## Server/Generator Manipulation of Locales<a id="ServerLocales"></a>

GWT provides two classes to manipulate locales, which fully support aliases
and locale inheritance.

*   [GwtLocale](/javadoc/latest/com/google/gwt/i18n/shared/GwtLocale.html)
represents a GWT locale, and supports converting to canonical form, producing
search lists for locale inheritance and aliases, and provides accessors to
the components of a locale.

*   [GwtLocaleFactory](/javadoc/latest/com/google/gwt/i18n/shared/GwtLocaleFactory.html)
provides a way of creating new `GwtLocale` objects from locale names
or their components (useful for converting from a `java.util.Locale`
object).

*   [LocaleUtils](/javadoc/latest/com/google/gwt/i18n/rebind/LocaleUtils.html)
    provides easy access to GWT's locale infrastructure for a generator.
    *   Get a GwtLocaleFactory instance:

```java
GwtLocaleFactory factory = LocaleUtils.getLocaleFactory();
```

*   Get all locales for this compile, including runtime locales:

```java
Set<GwtLocale> locales = localeUtils.getAllLocales();
```

*   [GwtLocaleFactoryImpl](/javadoc/latest/com/google/gwt/i18n/server/GwtLocaleFactoryImpl.html)
provides a way to create GwtLocale instances on the server.
