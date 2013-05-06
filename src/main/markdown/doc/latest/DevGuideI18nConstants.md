<style type="text/css"> .TODO { background-color: yellow; } </style>

<ol class="toc" id="pageToc">
  <li><a href="#Constants">Constants</a></li>
  <li><a href="#ConstantsWithLookup">ConstantsWithLookup</a></li>
  <li><a href="#ConstantsAnnotations">Using Annotations</a></li>
  <li><a href="#ConstantsProperties">Using Properties Files</a></li>
</ol>


<h2 id="Constants">Constants</h2>

<p>This example will walk through the process of creating a class of internationalized constant strings &quot;hello, world&quot; and &quot;goodbye, world&quot; in your GWT application. The example
will create a Java interface named <tt>MyConstants</tt> that abstracts those strings. You can reference the <tt>MyConstants</tt> methods in your GWT code when you want to display
one of those strings to the user and they will be translated appropriately for all locales that have matching <tt>.properties</tt> files.</p>

<p>Begin by creating a default properties file called <tt>MyConstants.properties</tt> in your GWT project. You can place the file anywhere in your module's source path, but the
<tt>.properties</tt> file and corresponding interface must be in the same package. It's fine to place the file in the same package as your module's entry point class.</p>

<pre>
helloWorld = hello, world
goodbyeWorld = goodbye, world
</pre>

<p>You can also create a localized translation for each supported locale in separate properties files. The properties file must be named the same as our interface name, in our
case <tt>MyConstants</tt>, with the appropriate suffix that indicates the <a href="DevGuideI18nLocale.html#LocaleSpecifying">locale setting</a>. In this case, we localize
for Spanish using the filename <tt>MyConstants_es.properties</tt>:</p>

<pre>
helloWorld = hola, mundo
goodbyeWorld = adiós, mundo
</pre>

<p>Now define an interface that abstracts those strings by extending the built-in <tt>Constants</tt> interface. Create a new Java interface in the same package where the
<tt>.properties</tt> files were created. The method names must match the tag names uses in the <tt>.properties</tt> files:</p>

<pre class="prettyprint">
public interface MyConstants extends Constants {
  String helloWorld();
  String goodbyeWorld();
}
</pre>

<p class="note"><strong>Tip:</strong> The i18nCreator tool automates the generation of Constants interface subinterfaces like the one above. The tool generates Java code so that you only need to
maintain the <tt>.properties</tt> files. It also works for <tt>ConstantsWithLookup</tt> and <tt>Messages</tt> classes.</p>

<p>Note that <tt>MyConstants</tt> is declared as an interface, so you cannot instantiate it directly with <tt>new</tt>. To use the internationalized constants, you create a Java
instance of <tt>MyConstants</tt> using the <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/core/client/GWT.html#create(java.lang.Class)">GWT.create(Class)</a> facility:</p>

<pre class="prettyprint">
public void useMyConstants() {
  MyConstants myConstants = GWT.create(MyConstants.class);
  Window.alert(myConstants.helloWorld());
}
</pre>

<p>You don't need to worry about the Java implementation of your static string classes. Static string initialization uses a <a href="DevGuideCodingBasics.html#DevGuideDeferredBinding">deferred binding generator</a> which allows the GWT
compiler to take care of automatically generating the Java code necessary to implement your <tt>Constants</tt> subinterface depending on the <a href="DevGuideI18nLocale.html">locale</a>.</p>

<h2 id="ConstantsWithLookup">ConstantsWithLookup</h2>

<p>The <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/i18n/client/ConstantsWithLookup.html">ConstantsWithLookup</a> interface is
identical to <tt>Constants</tt> except that the interface also includes a method to look up strings by property name, which facilitates dynamic binding to constants by name at
runtime. <tt>ConstantsWithLookup</tt> can sometimes be useful in highly data-driven applications. One caveat: <tt>ConstantsWithLookup</tt> is less efficient than
<tt>Constants</tt> because the compiler cannot discard unused constant methods,
resulting in larger applications.</p>

<h2 id="ConstantsAnnotations">Using Annotations</h2>

<p>The annotations discussed here are the ones specific to <tt>Constants</tt>
and <tt>ConstantsWithLookup</tt> &mdash; for shared annotations see the <a
href="DevGuideI18n.html#DevGuideAnnotations">main Internationalization
page</a>.</p>

<h3>Method Annotations</h3>
<p>The following annotations apply to methods in a <tt>Constants</tt> subtype
and must correspond to the return type of the method.  They provide a type-safe
way to reference constants, and can include Java compile-time constant
references.
<ul>
<li><strong><tt><a
href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/i18n/client/Constants.DefaultBooleanValue.html">@DefaultBooleanValue(boolean val)</a></tt></strong><br/>
Sets the default value for a method which returns a <tt>boolean</tt>.</li>
<li><strong><tt><a
href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/i18n/client/Constants.DefaultDoubleValue.html">@DefaultDoubleValue(double val)</a></tt></strong><br/>
Sets the default value for a method which returns a <tt>double</tt>.</li>
<li><strong><tt><a
href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/i18n/client/Constants.DefaultFloatValue.html">@DefaultFloatValue(float val)</a></tt></strong><br/>
Sets the default value for a method which returns a <tt>float</tt>.</li>
<li><strong><tt><a
href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/i18n/client/Constants.DefaultIntValue.html">@DefaultIntValue(int val)</a></tt></strong><br/>
Sets the default value for a method which returns a <tt>int</tt>.</li>
<li><strong><tt><a
href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/i18n/client/Constants.DefaultStringArrayValue.html">@DefaultStringArrayValue({String str, ...})</a></tt></strong><br/>
Sets the default value for a method which returns a <tt>String</tt> array.</li>
<li><strong><tt><a
href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/i18n/client/Constants.DefaultStringMapValue.html">@DefaultStringMapValue({String key, String value,
...})</a></tt></strong><br/>
Sets the default value for a method which returns a
<tt>Map&lt;String,String&gt;</tt> or a raw map (which will still be a
<tt>String=&gt;String</tt> map).  The number of supplied values must be even,
and the first entry of each pair is the key and the second is the value.</li>
<li><strong><tt><a
href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/i18n/client/Constants.DefaultStringValue.html">@DefaultStringValue(String str)</a></tt></strong><br/>
Sets the default value for a method which returns a <tt>String</tt>.</li>
</ul>

<h2 id="ConstantsProperties">Using Property Files</h2>

<p>The properties file format for <tt>Constants</tt> and
<tt>ConstantsWithLookup</tt> is simply <tt>key=value</tt>, but there are a few
points to remember:
<ul>
  <li><tt>#</tt> must be escaped as it is a comment character</li>
  <li>When the type of the method is <tt>String[]</tt>, an ASCII comma is used
  to separate values, which means that any commas included in a value must be
  escaped with a backslash.  Also, beware of translators using a different
  character to separate translated values.</li>
  <li>In the case of a <tt>Map</tt>-valued method, the entry in the properties
  file for that method will be a comma-separated set of keys, and then those
  keys have their own entries with their associated value.  Example:
  <pre class="prettyprint">
  Map&lt;String,String&gt; colorMap();

  colors=header, body, footer
  header=red
  body=white
  footer=blue
  </pre><p/>
produces a map <tt>{ header=&gt;red, body=&gt;white, footer=&gt;blue
}</tt>.</li>
</ul>