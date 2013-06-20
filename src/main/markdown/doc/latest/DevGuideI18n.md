<style type="text/css"> .TODO { background-color: yellow; } </style>

<p>GWT includes a flexible set of tools to help you internationalize your applications and libraries. GWT internationalization support provides a variety of techniques to
internationalize strings, typed values, and classes.</p>

<p class="note" style="margin-left: 1.5em; margin-right: 1.5em;">
<b>Note:</b> To run through the steps to internationalize a sample GWT app, see the tutorial <a href="tutorial/i18n.html">Internationalizing a GWT Application</a>.
</p>

<ol class="toc" id="pageToc">
  <li><a href="#DevGuideLocale">Locales in GWT</a></li>
  <li><a href="#DevGuideStaticStringInternationalization">Static String Internationalization</a></li>
  <li><a href="#DevGuideDynamicStringInternationalization">Dynamic String Internationalization</a></li>
  <li><a href="#DevGuideAnnotations">Java Annotations</a></li>
  <li><a href="#DevGuidePropertiesFiles">Localized Properties Files</a></li>
</ol>


<h3>Quick Start with Internationalization</h3>

<p>GWT supports a variety of ways of internationalizing your code. Start by researching which approach best matches your development requirements.</p>

<ul>
<li><strong>Are you using UiBinder?</strong><br/>
If so, you will probably want to read up on <a
href="DevGuideUiBinderI18n.html">UiBinder's I18n</a> support.</li>
</ul>

<ul>
<li><strong>Are you writing code from scratch?</strong><br/>
If so, you will probably want to read up on GWT's <a href="DevGuideI18n.html#DevGuideStaticStringInternationalization">static string internationalization</a>
techniques.</li>
</ul>

<ul>
<li><strong>Do you want to want to store non-String localized
values?</strong><br/>
Use the <a
href="/javadoc/latest/com/google/gwt/i18n/client/Constants.html">Constants</a>
or <a
href="/javadoc/latest/com/google/gwt/i18n/client/ConstantsWithLookup.html">ConstantsWithLookup</a> interfaces, which
allow types such as primitives, String arrays, and String maps.</li>
</ul>

<ul>
<li><strong>Do you need to substitute parameters into the translated
messages?</strong><br/>
Use <a href="/javadoc/latest/com/google/gwt/i18n/client/Messages.html">Messages</a>.</li>
</ul>

<ul>
<li><strong>Do you have existing localized properties files you'd like to reuse?</strong><br/>
The i18nCreator tool can automatically generate interfaces that extend either <a href="/javadoc/latest/com/google/gwt/i18n/client/Constants.html">Constants</a>, <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/i18n/client/ConstantsWithLookup.html">ConstantsWithLookup</a> or <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/i18n/client/Messages.html">Messages</a>.</li>
</ul>

<ul>
<li><strong>Are you adding GWT functionality to an existing web application that already has a localization process defined?</strong><br/>
<a href="/javadoc/latest/com/google/gwt/i18n/client/Dictionary.html">Dictionary</a> will help you interoperate with
existing pages without requiring you to use <a
href="DevGuideI18nLocale.html#LocaleSpecifying">GWT's concept of
locale</a>.</li>
</ul>

<ul>
<li><strong>Do you really just want a simple way to get properties files down to the client regardless of localization?</strong><br/>
You can do that, too. Try using <a href="/javadoc/latest/com/google/gwt/i18n/client/Constants.html">Constants</a> and
just not having any locale-specific property files.</li>
</ul>

<h3>Internationalization Techniques</h3>

<p>GWT offers multiple internationalization techniques to afford maximum flexibility to GWT developers and to make it possible to design for efficiency, maintainability,
flexibility, and interoperability in whichever combinations are most useful.</p>

<ul>
<li><strong><a href="DevGuideI18n.html#DevGuideStaticStringInternationalization">Static string internationalization</a></strong><br/>
A family of efficient and type-safe techniques that rely on strongly-typed Java interfaces, <a href="DevGuideI18n.html#DevGuidePropertiesFiles">properties files</a>, and
code generation to provide locale-aware messages and configuration settings.
These techniques depend on the interfaces <a
href="/javadoc/latest/com/google/gwt/i18n/client/Constants.html">Constants</a>,
<a href="/javadoc/latest/com/google/gwt/i18n/client/ConstantsWithLookup.html">ConstantsWithLookup</a>,
and <a href="/javadoc/latest/com/google/gwt/i18n/client/Messages.html">Messages</a>.</li>
</ul>

<ul>
<li><strong><a href="DevGuideI18n.html#DevGuideDynamicStringInternationalization">Dynamic string internationalization</a></strong><br/>
A simple and flexible technique for looking up localized values defined in a module's <a href="DevGuideOrganizingProjects.html#DevGuideHostPage">host page</a> without needing to recompile
your application. This technique is supported by the class <a href="/javadoc/latest/com/google/gwt/i18n/client/Dictionary.html">Dictionary</a>.</li>
</ul>

<ul>
<li><strong>Extending or implementing Localizable</strong><br/>
Provides a method for internationalizing sets of algorithms using locale-sensitive type substitution. This is an advanced technique that you probably will not need to use
directly, although it is useful for implementing complex internationalized libraries. For details on this technique, see the <a href="/javadoc/latest/com/google/gwt/i18n/client/Localizable.html">Localizable</a> class documentation.</li>
</ul>

<h3>The I18N Module</h3>

<p>Core types related to internationalization:</p>

<ul>
<li><a href="/javadoc/latest/com/google/gwt/i18n/client/LocaleInfo.html">LocaleInfo</a>
Provides information about the current locale.</li>
</ul>

<ul>
<li><a href="/javadoc/latest/com/google/gwt/i18n/client/Constants.html">Constants</a> Useful for localizing typed constant
values</li>
</ul>

<ul>
<li><a href="/javadoc/latest/com/google/gwt/i18n/client/Messages.html">Messages</a> Useful for localizing messages
requiring arguments</li>
</ul>

<ul>
<li><a href="/javadoc/latest/com/google/gwt/i18n/client/ConstantsWithLookup.html">ConstantsWithLookup</a> Like <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/i18n/client/Constants.html">Constants</a> but with extra lookup flexibility for highly
data-driven applications</li>
</ul>

<ul>
<li><a href="/javadoc/latest/com/google/gwt/i18n/client/Dictionary.html">Dictionary</a> Useful when adding a GWT module to
existing localized web pages</li>
</ul>

<ul>
<li><a href="/javadoc/latest/com/google/gwt/i18n/client/Localizable.html">Localizable</a> Useful for localizing algorithms
encapsulated in a class or when the classes above don't provide sufficient
control</li>
</ul>
<p/>

<ul>
<li><a href="/javadoc/latest/com/google/gwt/i18n/client/DateTimeFormat.html">DateTimeFormat</a> Formatting dates as
strings. See the section on <a href="DevGuideCodingBasics.html#DevGuideDateAndNumberFormat">date and number formatting</a>.</li>
</ul>

<ul>
<li><a href="/javadoc/latest/com/google/gwt/i18n/client/NumberFormat.html">NumberFormat</a> Formatting numbers as strings.
See the section on <a href="DevGuideCodingBasics.html#DevGuideDateAndNumberFormat">date and number formatting</a>.</li>
</ul>

<p>The GWT internationalization types reside in the com.google.gwt.i18n package. To use any of these types, your module must inherit from the I18N module
(com.google.gwt.i18n.I18N).</p>

<pre class="prettyprint">
&lt;module&gt;
  &lt;inherits name=&quot;com.google.gwt.i18n.I18N&quot;/&gt;
&lt;/module&gt;
</pre>

<p>As of GWT 1.5, the User module (com.google.gwt.user.User) inherits the I18N module. So if your project's module XML file inherits the User module (which generally it does), it
does not need to specify explicitly an inherit for the I18N module.</p>

<h2 id="DevGuideLocale">Locales in GWT</h2>

<p>GWT is different than most toolkits by performing most locale-related work
at compile time rather than runtime.  This allows GWT to do compile-time
error checking, such as when a parameter is left out or the translated
value is not of the correct type, and for optimizations to take into account
known facts about the locale.  This also allows an end user to download
only the translations that are relevant for them.</p>

<p>For details on configuring locales in your GWT application, see the
<a href="DevGuideI18nLocale.html">detailed locale documentation</a>.</p>

<h2 id="DevGuideStaticStringInternationalization">Static String Internationalization</h2>

<p>Static string internationalization is the most efficient way to localize your application for different locales in terms of runtime performance. This approach is called
&quot;static&quot; because it refers to creating tags that are matched up with human readable strings at compile time. At compile time, mappings between tags and strings are created for all
languages defined in the module. The module startup sequence maps the appropriate implementation based on the locale setting using <a href="DevGuideCodingBasics.html#DevGuideDeferredBinding">deferred binding</a>.</p>

<p>Static string localization relies on code generation from standard Java <a
href="DevGuideI18n.html#DevGuidePropertiesFiles">properties files</a> or
<a href="DevGuideI18n.html#DevGuideAnnotations">annotations in the Java
source</a>. GWT supports static string localization through three tag interfaces
(that is, interfaces having no methods that represent a functionality contract)
and a code generation library to generate implementations of those
interfaces.</p>

<h3>Extending the Constants Interface</h3>

<p>The <a href="DevGuideI18nConstants.html"><tt>Constants</tt></a> interface
allows you to localize constant values in a type-safe manner, all resolved
at compile time.  At some cost of runtime overhead, you can also allow runtime
lookup by key names with the <tt>ConstantsWithLookup</tt> interface.</p>

<h3>Using the Messages Interface</h3>

<p>The <a href="DevGuideI18nMessages.html"><tt>Messages</tt></a> interface
allows you to substitute parameters into messages and to even re-order those
parameters for different locales as needed. The format of the messages in the
properties files follows the specification in Java <a
href="http://java.sun.com/j2se/1.5.0/docs/api/java/text/MessageFormat.html">MessageFormat</a>.
The interface you create will contain a Java
method with parameters matching those specified in the format string.</p>

<p>In addition, the <tt>Messages</tt> interface supports <a
href="DevGuideI18nPluralForms.html">Plural Forms</a> to allow your application
to accurately reflect text changes based on the count of something.

<h3>Which Interface to Use?</h3>

<p>Here are some guidelines to help choose the right interface for your application's needs:</p>

<ul>
<li>Extend <a href="/javadoc/latest/com/google/gwt/i18n/client/Constants.html">Constants</a>
to create a collection of constant values of a variety of types that can be
accessed by calling methods (called <i>constant accessors</i>) on an interface.
Constant accessors may return a variety of types, including strings, numbers,
booleans, and even maps. A compile-time check is done to ensure that the value
in a properties file matches the return type declared by its corresponding
constant accessor. In other words, if a constant accessor is declared to
return an <tt>int</tt>, its associated property is guaranteed to be a valid
<tt>int</tt> value &mdash; avoiding a potential source of runtime errors.</li>
</ul>

<ul>
<li>The <a
href="/javadoc/latest/com/google/gwt/i18n/client/ConstantsWithLookup.html">ConstantsWithLookup</a>
interface is identical to <tt>Constants</tt> except that the interface also
includes a method to look up values by property name, which facilitates
dynamic binding to constants by name at runtime. <tt>ConstantsWithLookup</tt>
can sometimes be useful in highly data-driven applications. One caveat:
<tt>ConstantsWithLookup</tt> is less efficient than <tt>Constants</tt>
because the compiler cannot discard unused constant methods, resulting in
larger applications and the lookup cannot be resolved at compile-time.</li>
</ul>

<ul>
<li>Extend <a href="/javadoc/latest/com/google/gwt/i18n/client/Messages.html">Messages</a> to create a collection of
formatted messages that can accept parameters. You might think of the
<tt>Messages</tt> interface as a statically verifiable equivalent of the
traditional Java combination of <tt>Properties</tt>, <tt>ResourceBundle</tt>,
and <tt>MessageFormat</tt> rolled into a single mechanism.</li>
</ul>

<h3>Properties Files</h3>

<p>All of the types above use properties files based on the traditional <a
href="http://java.sun.com/j2se/1.5.0/docs/api/java/util/Properties.html#load(java.io.InputStream)">Java
properties file format</a>, although GWT uses <a
href="DevGuideI18n.html#DevGuidePropertiesFiles">an enhanced properties file
format</a> that allows for UTF-8 and therefore allows properties files to
contain Unicode characters directly.</p>

<h2 id="DevGuideDynamicStringInternationalization">Dynamic String Internationalization</h2>

<p>For existing applications that may not support the GWT <tt>locale</tt>
client property, GWT offers dynamic string internationalization to easily
integrate GWT internationalization.</p>

<p>The <a href="/javadoc/latest/com/google/gwt/i18n/client/Dictionary.html">Dictionary</a> class lets your GWT application
consume strings supplied by the <a href="DevGuideOrganizingProjects.html#DevGuideHostPage">host HTML page</a>. This approach is convenient if your existing web server has a localization
system that you do not wish to integrate with the <a href="DevGuideI18n.html#DevGuideStaticStringInternationalization">static string internationalization</a> methods.
Instead, simply print your strings within the body of your HTML page as a JavaScript structure, and your GWT application can reference and display them to end users. Since it
binds directly to the key/value pairs in the host HTML, whatever they may be,
the <a
href="/javadoc/latest/com/google/gwt/i18n/client/Dictionary.html">Dictionary</a>
class is not sensitive to the <a
href="DevGuideI18nLocale.html#LocaleSpecifying">GWT locale setting</a>. Thus,
the burden of generating localized strings is on your web server.</p>

<p>Dynamic string localization allows you to look up localized strings defined in a <a href="DevGuideOrganizingProjects.html#DevGuideHostPage">host HTML</a> page at runtime using
string-based keys. This approach is typically slower and larger than the static string approach, but does not require application code to be recompiled when messages are altered
or the set of locales changes.</p>

<p class="note"><strong>Tip:</strong> The <tt>Dictionary</tt> class is completely dynamic, so it provides no static type checking, and invalid keys cannot be checked by the compiler. This is
another reason we recommend using <a href="DevGuideI18n.html#DevGuideStaticStringInternationalization">static string internationalization</a> where
possible.</p>

<h2 id="DevGuideAnnotations">Java Annotations</h2>

<p>The recommended approach for specifying the default values for
<tt>Constants</tt> or <tt>Messages</tt> interfaces is using Java annotations.
The advantage of this approach is that you can keep the values with the
source, so when refactoring the interface or creating new methods in your IDE
it is easier to keep things up to date.  Also, if you are using a
custom key generator or generating output files for translation, you
need to use annotations.</p>

<p>The annotations that apply everywhere are discussed here &mdash; for annotations
that are only used on <a
href="DevGuideI18nConstants.html#ConstantsAnnotations"><tt>Constants</tt></a>
and <a
href="DevGuideI18nMessages.html#MessagesAnnotations"><tt>Messages</tt></a> are
discussed there.</p>

<h3>Class Annotations</h3>
<p>The following annotations apply to classes or interfaces:
<ul>
<li><strong><tt><a
href="/javadoc/latest/com/google/gwt/i18n/client/LocalizableResource.DefaultLocale.html">@DefaultLocale(String
localeName)</a></tt></strong><br/>
Specifies that text contained in this file is of the specified locale.  If not
specified, the default is <tt>en</tt>.</li>
<li><strong><tt><a
href="/javadoc/latest/com/google/gwt/i18n/client/LocalizableResource.GeneratedFrom.html">@GeneratedFrom(String
fileName)</a></tt></strong><br/>
Indicates that this file was generated from the supplied file. Note that it
is not required that this file name be resolvable at compile time, as this
file may have been generated on a different machine, etc. &mdash; if the generator
does check the source file, such as for staleness, it must not give any
warning if the file is not present or if the name is not resolvable.</li>
<li><strong><tt><a
href="/javadoc/latest/com/google/gwt/i18n/client/LocalizableResource.GenerateKeys.html">@GenerateKeys(String
generatorFQCN)</a></tt></strong><br/>
Requests that the keys for each method be generated with the specified
generator (see below). If this annotation is not supplied, keys will be the
name of the method, and if specified without a parameter it will default to
the MD5 implementation. The specified generator class must implement the
<tt><a
href="/javadoc/latest/com/google/gwt/i18n/rebind/keygen/KeyGenerator.html">KeyGenerator</a></tt> interface. By
specifying a fully-qualified class name,
this will be extensible to other formats not in the GWT namespace &mdash; the
user just has to make sure the specified class is on the class path at
compilation time. This
allows integration with non-standard or internal tools that may use their own
hash functions to coalesce duplicate translation strings between multiple
applications or otherwise needed for compatibility with external tools.<p/>
A string containing the fully-qualified class name is used instead of a
class literal because the key generation algorithm is likely to pull in code
that is not translatable, so cannot be seen directly in client code.<p/>
If this annotation is not supplied, the key will be the simple name of the
method.
<li><strong><tt><a
href="/javadoc/latest/com/google/gwt/i18n/client/LocalizableResource.Generate.html">@Generate(String[]
formatFQCN, String filename, String[] locales)</a></tt></strong><br/>
Requests that a message catalog file is generated during the compilation
process. If the filename is not supplied, a default name based on the
interface name is used. The output file is created under the -out directory.
The format names are the fully-qualified class names which implement the <a
href="/javadoc/latest/com/google/gwt/i18n/rebind/format/MessageCatalogFormat.html"><tt>MessageCatalogFormat</tt></a>
interface. For example, this could generate an
XLIFF or properties file based on the information contained in this file.
Specific <tt>MessageCatalogFormat</tt> implementations may define additional
annotations for additional parameters needed for that
<tt>MessageCatalogFormat</tt>.<p/>
If any locales are specified, only the listed
locales are generated. If exactly one locale is listed, the filename supplied
(or generated) will be used exactly; otherwise </tt>_<i>locale</i></tt> will
be added before the file extension.<p/>
A string containing the fully-qualified class name is used instead of a
class literal because the message catalog implementation is likely to pull in
code that is not translatable, so cannot be seen directly in client code.
</ul></p>

<h3>Method Annotations</h3>
<p>The following annotations apply to methods:
<ul>
<li><strong><tt><a
href="/javadoc/latest/com/google/gwt/i18n/client/LocalizableResource.Key.html">@Key(String
key)</a></tt></strong><br/>
Specifies the key to use in the external format for this particular method.
If not supplied, it will be generated based on the <tt>@GenerateKeys</tt>
annotation, discussed above.</li>
<li><strong><tt><a
href="/javadoc/latest/com/google/gwt/i18n/client/LocalizableResource.Description.html">@Description(String
desc)</a></tt></strong><br/>
A description of the text. Note that this is not included in a hash of the
text and depending on the file format may not be included in a way visible to
a translator.</li>
<li><strong><tt><a
href="/javadoc/latest/com/google/gwt/i18n/client/LocalizableResource.Meaning.html">@Meaning(String
meaning)</a></tt></strong><br/>
Supplies a meaning associated with this text. This information is provided to
the translator to distinguish between different possible translations &mdash;
for example, orange might have meaning supplied as "the fruit" or "the color".
Note that two messages with identical text but different meanings should have
different keys, as they may be translated differently.</li>
</ul></p>

<h2 id="DevGuidePropertiesFiles">Localized Properties Files</h2>

<p><a href="DevGuideI18n.html#DevGuideStaticStringInternationalization">Static string internationalization</a> uses traditional Java <tt>.properties</tt> files to manage
translating tags into localized values. These files may be placed into the same package as your main module class. They must be placed in the same package as their corresponding
<tt>Constants</tt>/<tt>Messages</tt> subinterface definition file.</p>

<p class="note"><strong>Tip:</strong> Use the i18nCreator script to get started.</p>

<pre class="prettyprint"> $ i18nCreator -eclipse Foo com.example.foo.client.FooConstants
 Created file src/com/example/foo/client/FooConstants.properties
 Created file FooConstants-i18n.launch
 Created file FooConstants-i18n
</pre>

<p/>

<p>Both <a href="/javadoc/latest/com/google/gwt/i18n/client/Constants.html">Constants</a> and <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/i18n/client/Messages.html">Messages</a> use traditional Java properties files, with
one notable difference: properties files used with GWT should be encoded as UTF-8 and may contain Unicode characters directly, avoiding the need for <tt>native2ascii</tt>. See the
API documentation for the above interfaces for examples and formatting details. Many thanks to the <a href="http://tapestry.apache.org/">Tapestry</a> project for
solving the problem of reading UTF-8 properties files in Tapestry's <tt>LocalizedProperties</tt> class.</p>

<p>In order to use internationalized characters, make sure that your host HTML file contains the <tt>charset=utf8</tt> content type in the meta tag in the header:</p>

<pre class="prettyprint">
&lt;meta http-equiv=&quot;content-type&quot; content=&quot;text/html;charset=utf-8&quot; /&gt;
</pre>

<p>You must also ensure that all relevant source and <tt>.properties</tt> files are set to be in the UTF-8 charset in your IDE.</p>