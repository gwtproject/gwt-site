<p>
The resources in a deployed GWT application can be roughly categorized into resources to never cache (<tt>.nocache.js</tt>), to cache forever (<tt>.cache.html</tt>), and everything else (<tt>myapp.css</tt>).  The <a href="/javadoc/latest/index.html?com/google/gwt/resources/client/ClientBundle.html">ClientBundle</a> interface moves entries from the everything-else category into the cache-forever category.
</p>

<ol class="toc" id="pageToc">
  <li><a href="#ClientBundle">Overview</a></li>
  <li><a href="#DataResource">DataResource</a></li>
  <li><a href="#TextResource">TextResource and ExternalTextResource</a></li>
  <li><a href="#ImageResource">ImageResource</a></li>
  <li><a href="#GwtCreateResource">GwtCreateResource</a></li>
  <li><a href="#CssResource">CssResource</a></li>
  <li><a href="#CssResourceCookbook">CssResourceCookbook</a></li>

</ol>


<h2 id="ClientBundle">Overview</h2>

<ol class="toc">
  <ul>
    <li><a href="#ClientBundle_Goals">Goals</a></li>
    <li><a href="#ClientBundleExamples">Examples</a></li>
    <li><a href="#I18N">I18N</a></li>
    <li><a href="#Pluggable_Resource_Generation">Pluggable Resource Generation</a></li>
    <li><a href="#Levers_and_knobs">Levers and knobs</a></li>
    <li><a href="#Resource_types">Resource types</a></li>
  </ul>
</ol>

<h3 id="ClientBundle_Goals">Goals</h3>

<ul>
  <li>No more uncertainty about whether your application is getting the right contents for program resources. </li>
  <li>Decrease non-determinism caused by intermediate proxy servers. </li>
  <li>Enable more aggressive caching headers for program resources. </li>
  <li>Eliminate mismatches between physical filenames and constants in Java code by performing consistency checks during the compile. </li>
  <li>Use 'data:' URLs, JSON bundles, or other means of embedding resources in compiled JS when browser- and size-appropriate to eliminate unneeded round trips. </li>
  <li>Provide an extensible design for adding new resource types. </li>
  <li>Ensure there is no penalty for having multiple <tt>ClientBundle</tt> resource functions refer to the same content. </li>
</ul>


<h4>Non-Goals</h4>

<ul>
  <li>To provide a file-system abstraction </li>
</ul>


<h3 id="ClientBundleExamples">Examples</h3>

<p>To use <a href="/javadoc/latest/index.html?com/google/gwt/resources/client/ClientBundle.html">ClientBundle</a>, add an <tt>inherits</tt> tag to your <tt>gwt.xml</tt> file: </p>

<pre class="prettyprint">&lt;inherits name=&quot;com.google.gwt.resources.Resources&quot; /&gt;</pre>

<p>If you write this interface:</p>

<pre class="prettyprint">public interface MyResources extends ClientBundle {
  public static final MyResources INSTANCE =  GWT.create(MyResources.class);

  @Source(&quot;my.css&quot;)
  public CssResource css();

  @Source(&quot;config.xml&quot;)
  public TextResource initialConfiguration();

  @Source(&quot;manual.pdf&quot;)
  public DataResource ownersManual();
}</pre>

<p>You can then say: </p>

<pre class="prettyprint">  // Inject the contents of the CSS file
  MyResources.INSTANCE.css().ensureInjected();

  // Display the manual file in an iframe
  new Frame(MyResources.INSTANCE.ownersManual().getURL());</pre>


<h3 id="I18N">I18N</h3>

<p><tt>ClientBundle</tt> is compatible with GWT's I18N module. </p>

<p>Suppose you defined a resource: </p>

<pre class="prettyprint">@Source(&quot;default.txt&quot;)
public TextResource defaultText();</pre>

<p>For each possible value of the <tt>locale</tt> deferred-binding property, the <tt>ClientBundle</tt> generator will look for variations of the specified filename in a manner similar to that of Java's <tt>ResourceBundle</tt>. </p>

<p>Suppose the <tt>locale</tt> were set to <tt>fr_FR</tt>.  The generator would look for files in the following order:

<ol>
  <li>default_fr_FR.txt </li>
  <li>default_fr.txt </li>
  <li>default.txt </li>
</ol>

<p>This will work equally well with all resource types, which can allow you to provide localized versions of other resources, say <tt>ownersManual_en.pdf</tt> versus <tt>ownersManual_fr.pdf</tt>.</p>

<h3 id="Pluggable_Resource_Generation">Pluggable Resource Generation</h3>



<p>Each subtype of <tt><a href="/javadoc/latest/index.html?com/google/gwt/resources/client/ResourcePrototype.html">ResourcePrototype</a></tt> must define a <tt><a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/index.html?com/google/gwt/resources/ext/ResourceGeneratorType.html">@ResourceGeneratorType</a></tt> annotation whose value is a concrete Java class that extends <tt><a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/index.html?com/google/gwt/resources/ext/ResourceGenerator.html">ResourceGenerator</a></tt>.  The instance of the <tt>ResourceGenerator</tt> is responsible for accumulation (or bundling) of incoming resource data as well as a small degree of code generation to assemble the concrete implementation of the <tt>ClientBundle</tt> class.  Implementors of <tt>ResourceGenerator</tt> subclasses can expect that only one <tt>ResourceGenerator</tt> will be created for a given type of resource within a <tt>ClientBundle</tt> interface. </p>

<p>The methods on a <tt>ResourceGenerator</tt> are called in the following order

<ol>
  <li><tt>init</tt> to provide the <tt>ResourceGenerator</tt> with a <tt><a href="/javadoc/latest/index.html?com/google/gwt/resources/ext/ResourceContext.html">ResourceContext</a></tt> </li>
  <li><tt>prepare</tt> is called for each <tt>JMethod</tt> the <tt>ResourceGenerator</tt> is expected to handle </li>
  <li><tt>createFields</tt> allows the <tt>ResourceGenerator</tt> to add code at the class level </li>
  <li><tt>createAssignment</tt> is called for each <tt>JMethod</tt>.  The generated code should be suitable for use as the right-hand side of an assignment expression. </li>
  <li><tt>finish</tt> is called after all assignments should have been written. </li>
</ol>

<p><tt>ResourceGenerators</tt> are expected to make use of the <tt><a href="/javadoc/latest/index.html?com/google/gwt/resources/ext/ResourceGeneratorUtil.html">ResourceGeneratorUtil</a></tt> class. </p>


<h3 id="Levers_and_knobs">Levers and knobs</h3>


<ul>
  <li><tt>ClientBundle.enableInlining</tt> is a deferred-binding property that can be used to disable the use of <tt>data:</tt> URLs in browsers that would otherwise support inlining resource data into the compiled JS. </li>
  <li><tt>ClientBundle.enableRenaming</tt> is a configuration property that will disable the use of strongly-named cache files. </li>
</ul>


<h3 id="Resource_types">Resource types</h3>

<p>These resource types are valid return types for methods defined in a ClientBundle:</p>

<ul>
  <li><a href="#ImageResource">ImageResource</a> </li>
  <li><a href="#CssResource">CssResource</a> </li>
  <li><a href="#DataResource">DataResource</a> </li>
  <li><a href="#TextResource">ExternalTextResource</a> </li>
  <li><a href="#GwtCreateResource">GwtCreateResource</a> </li>
  <li><a href="#TextResource">TextResource</a> </li>
  <li>Subclasses of ClientBundle (for nested bundles)</li>
</ul>

<h2 id="DataResource">DataResource</h2>
<p>A <a href="/javadoc/latest/index.html?com/google/gwt/resources/client/DataResource.html">DataResource</a> is the simplest of the resource types, offering a URL by which the contents of a file can be retrieved at runtime.  The main optimization offered is to automatically rename files based on their contents in order to make the resulting URL strongly-cacheable by the browser.  Very small files may be converted into <tt>data:</tt> URLs on those browsers that support them.</p>

<pre class="prettyprint">interface Resources extends ClientBundle {
  Resources INSTANCE = GWT.create(Resources.class);

  @Source("mycursor.cur")
  DataResource customCursor();
}

// Elsewhere
someDiv.getStyle().setProperty("cursor", "url(" + Resources.INSTANCE.customCursor().getUrl() + ")");
</pre>

<p>Resources that are not appropriate for being inlined into the compiled JavaScript as <tt>data:</tt> URLs will be emitted into the compilation output with strong names, based on the contents of the file.  For example, <tt>foo.pdf</tt> will be given a name similar to <tt>ABC1234.cache.pdf</tt>.  The webserver should be configured to serve any files matching the <tt>*.cache.*</tt> glob with publicly-cacheable headers and a far-future <tt>Expires</tt> header.

<h2 id="TextResource">TextResource and ExternalTextResource</h2>

<p>The related resource types <a href="/javadoc/latest/index.html?com/google/gwt/resources/client/TextResource.html">TextResource</a> and <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/index.html?com/google/gwt/resources/client/ExternalTextResource.html">ExternalTextResource</a> provide access to static text content.  The main difference between these two types is that the former interns the text into the compiled JavaScript, while the latter bundles related text resources into a single file, which is accessed asynchronously.</p>

<pre class="prettyprint">interface Resources extends ClientBundle {
  Resources INSTANCE = GWT.create(Resources.class);

  @Source("a.txt")
  TextResource synchronous();

  @Source("b.txt")
  ExternalTextResource asynchronous();
}

// Using a TextResource
myTextArea.setInnerText(Resources.INSTANCE.synchronous().getText());

// Using an ExternalTextResource
Resources.INSTANCE.asynchronous().getText(new ResourceCallback&lt;TextResource&gt;() {
  public void onError(ResourceException e) { ... }
  public void onSuccess(TextResource r) {
    myTextArea.setInnerText(r.getText());
  }
});
</pre>

<h2 id="ImageResource">ImageResource</h2>

<p>This section describes how <a href="/javadoc/latest/index.html?com/google/gwt/resources/client/ImageResource.html">ImageResource</a> can tune the compile-time processing of image data and provide efficient access to image data at runtime. </p>

<ol class="toc">
  <ul>
    <li><a href="#ImageResourceGoal">Goal</a></li>
    <li><a href="#ImageResourceOverview">Overview</a>
      <ul>
        <li><a href="#ImageOptions">ImageOptions</a></li>
        <li><a href="#Supported_formats">Supported formats</a></li>
      </ul>
    </li>
    <li><a href="#Converting_from_ImageBundle">Converting from ImageBundle</a></li>
  </ul>
</ol>


<h3 id="ImageResourceGoal">Goal</h3>

<ul>
  <li>Provide access to image data at runtime in the most efficient way possible </li>
  <li>Do not bind the <tt>ImageResource</tt> API to a particular widget framework </li>
</ul>


<h4>Non-Goals</h4>

<ul>
  <li>To provide a general-purpose image manipulation framework. </li>
</ul>

<h3 id="ImageResourceOverview">Overview</h3>

<ol>
  <li>Define a ClientBundle with one or more <a href="/javadoc/latest/index.html?com/google/gwt/resources/client/ImageResource.html">ImageResource</a> accessors.
For each accessor method, add an @Source annotation with the path of the new image you want to add to your program.
The ClientBundle generator combines all of the images defined in your interface into a single, optimized image.

<pre class="prettyprint">interface Resources extends ClientBundle {
  @Source("logo.png")
  ImageResource logo();

  @Source("arrow.png")
  @ImageOptions(flipRtl = true)
  ImageResource pointer();
}</pre>
  </li>

  <li>Instantiate the <tt>ClientBundle</tt> via a call to <tt>GWT.create()</tt>. </li>

  <li>Instantiate one or more Image widget or use with the <a href="#Image_Sprites">CssResource @sprite</a> directive.

For example, the code:

<pre class="prettyprint">Resources resources = GWT.create(Resources.class);
Image img = new Image(resources.logo());</pre>

causes GWT to load the composite image generated for Resources and then creates an Image that is the correct subregion for just the logo image.</li>

</ol>


<h4 id="ImageOptions"><tt>ImageOptions</tt></h4>

<p>The <tt>@ImageOptions</tt> annotation can be applied to the <tt>ImageResource</tt> accessor method in order to tune the compile-time processing of the image data:</p>

<ul>
  <li><tt>flipRtl</tt> is a boolean value that will cause the image to be mirrored about its Y-axis when <tt>LocaleInfo.isRTL()</tt> returns <tt>true</tt>. </li>
  <li><tt>repeatStyle</tt> is an enumerated value that is used in combination with the<tt>@sprite</tt> directive to indicate that the image is intended to be tiled. </li>
</ul>

</p>

<h4 id="Supported_formats">Supported formats</h4>

<p><tt>ImageBundleBuilder</tt> uses the Java <a href="http://java.sun.com/javase/6/docs/api/javax/imageio/package-summary.html">ImageIO</a> framework to read image data. This includes support for all common web image formats. </p>



<p>Animated GIF files are supported by <tt>ImageResource</tt>.  While the image data will not be incorporated into an image strip, the resource is still served in a browser-optimal fashion by the larger <tt>ClientBundle</tt> framework. </p>

<h3 id="Converting_from_ImageBundle">Converting from ImageBundle</h3>

<p>Only minimal changes are required to convert existing code to use <tt>ImageResource</tt>.

<ul>
  <li><tt>ImageBundle</tt> becomes <tt>ClientBundle</tt> </li>
  <li><tt>AbstractImageProtoype</tt> becomes <tt>ImageResource</tt> </li>
  <li><tt>AbstractImagePrototype.createImage()</tt> becomes <tt>new Image(imageResource)</tt> </li>
  <li>Other methods on <tt>AbstractImagePrototype</tt> can continue to be used by calling <tt>AbstractImagePrototype.create(imageResource)</tt> to provide an API wrapper. </li>
</ul>

<h2 id="GwtCreateResource">GwtCreateResource</h2>

<p>The <a href="/javadoc/latest/index.html?com/google/gwt/resources/client/GwtCreateResource.html">GwtCreateResource</a> is a bridge type between <tt>ClientBundle</tt> and any other (resource) type that is default-instantiable. The instance of the <tt>GwtCreateResource</tt> acts as a factory for some other type.</p>

<pre class="prettyprint">interface Resources extends ClientBundle {
  Resources INSTANCE = GWT.create(Resources.class);

  @ClassType(SomeClass.class)
  GwtCreateResource&lt;ReturnType&gt; factory();
}

// Elsewhere
ReturnType obj = Resources.INSTANCE.factory().create();
</pre>

<p>While the above is equivalent to</p>
<pre class="prettyprint">
ReturnType obj = GWT.&lt;ReturnType&gt; create(SomeClass.class);
</pre>
<p>it allows the consuming classes to be ignorant of the specific class literal passed into <tt>GWT.create()</tt>.  It is not necessary for there to be a specific deferred-binding rule in place for <tt>SomeClass</tt> as long as that type is default-instantiable.</p>

<h2 id="CssResource">CssResource</h2>

<p>This section describes <a href="/javadoc/latest/index.html?com/google/gwt/resources/client/CssResource.html">CssResource</a> and the compile-time processing of CSS. </p>

<ol class="toc">
  <li><a href="#CssResource_Goals">Goals</a></li>
  <li><a href="#CssResource_Overview">Overview</a></li>
  <li><a href="#Features">Features</a>
    <ul>
      <li><a href="#Constants">Constants</a></li>
      <li><a href="#Runtime_substitution">Runtime substitution</a></li>
      <li><a href="#Value_function">Value function</a></li>
      <li><a href="#Literal_function">Literal function</a></li>
      <li><a href="#Conditional_CSS">Conditional CSS</a></li>
      <li><a href="#Image_Sprites">Image Sprites</a></li>
      <li><a href="#References_to_Data_Resources">References to Data Resources</a></li>
      <li><a href="#RTL_support">RTL support</a></li>
      <li><a href="#Selector_obfuscation">Selector obfuscation</a></li>
    </ul>
  </li>
  <li><a href="#Optimizations">Optimizations</a></li>
    <ul>
      <li><a href="#Basic_minification">Basic minification</a></li>
      <li><a href="#Selector_merging">Selector merging</a></li>
      <li><a href="#Property_merging">Property merging</a></li>
    </ul>
  </li>
  <li><a href="#Levers_and_Knobs">Levers and Knobs</a></li>
  <li><a href="#Selector_obfuscation_details">Selector obfuscation details</a>
    <ul>
      <li><a href="#Strict_scoping">Strict scoping</a></li>
      <li><a href="#Scope">Scope</a></li>
      <li><a href="#Shared_scopes">Shared scopes</a></li>
      <li><a href="#Imported_scopes">Imported scopes</a></li>
      <li><a href="#External_and_legacy_scopes">External and legacy scopes</a></li>
      <li><a href="#Automatically_generating_interfaces">Automatically generating interfaces</a></li>
    </ul>
  </li>
</ol>


<p>See also the <a href="#CssResourceCookbook">CssResourceCookbook</a> and <a href="/javadoc/latest/index.html?com/google/gwt/dom/client/StyleInjector.html">StyleInjector</a>. </p>


<h3 id="CssResource_Goals">Goals</h3>

<ul>
  <li>Primary
    <ul>
      <li>Compatibility with non-GWT-aware CSS parsers (i.e. any extensions should be valid CSS syntax)
        <ul>
          <li>This does not imply that the stylesheet would necessarily make sense if you just displayed it in a browser </li>
        </ul>
      </li>
      <li>Syntax validation </li>
      <li>Minification </li>
      <li>Leverage GWT compiler
        <ul>
          <li>Different CSS for different browsers, automatically </li>
          <li>Static evaluation of content </li>
        </ul>
      </li>
    </ul>
  </li>

  <li>Secondary
    <ul>
      <li>Basic CSS Modularization
        <ul>
          <li>Via dependency-injection API style </li>
          <li>Widgets can inject their own CSS only when it's needed </li>
        </ul>
      </li>
      <li>BiDi (Janus-style?) </li>
      <li>CSS image strips </li>
      <li>"Improve CSS"
        <ul>
          <li>Constants </li>
          <li>Simple expressions </li>
        </ul>
      </li>
    </ul>
  </li>

  <li>Tertiary
    <ul>
      <li>Runtime manipulation (StyleElement.setEnabled() handles many cases) </li>
      <li>Compile-time class-name checking (Java/CSS) </li>
      <li>Obfuscation </li>
    </ul>
  </li>
</ul>


<h4>Non-Goals</h4>

<ul>
  <li>Server-side manipulation
    <ul>
      <li>All features in CssResource must be implemented with compile-time and runtime code only.  No features may depend on runtime support from server-side code. </li>
    </ul>
  </li>
</ul>


<h3 id="CssResource_Overview">Overview</h3>

<ol>
  <li>Write a CSS file, with or without GWT-specific extensions </li>
  <li>If GWT-specific extensions are used, define a custom subtype of CssResource </li>
  <li>Declare a method that returns CssResource or a subtype in an ClientBundle </li>
  <li>When the bundle type is generated with <tt>GWT.create()</tt> a Java expression that evaluates to the contents of the stylesheets will be created
    <ul>
      <li>Except in the simplest case where the Java expression is a string literal, it is generally not the case that a CSS file could be generated into the module output </li>
    </ul>
  </li>
  <li>At runtime, call <tt>CssResource.ensureInjected()</tt> to inject the contents of the stylesheet
    <ul>
      <li>This method is safe to call multiple times, as subsequent invocations will be a no-op </li>
      <li>The recommended pattern is to call <tt>ensureInjected()</tt> in the static initializer of your various widget types </li>
    </ul>
  </li>
</ol>


<h3 id="Features">Features</h3>

<h4 id="Constants">Constants</h4>

<pre class="prettyprint">@def small 1px;
@def black #000;
border: small solid black;</pre>

<ul>
  <li>The parse rules make it difficult to use delimiting tokens for substitutions </li>
  <li>Redefining built-in sizes allows users to write plain CSS to draft a style and then tweak it. </li>
  <li>Suggest that users use upper-case names, similar to static final members. </li>
  <li>Any legal property value or expression may be used with <tt>@def</tt> </li>
  <li><tt>@def</tt> rules that define a single numeric value may be accessed in a manner similar to obfuscated class names by defining an accessor method on the CssResource type that returns a primitive numeric value. </li>

<pre class="prettyprint">interface MyResources extends CssResource {
  int small();
}</pre>

<ul>
  <li>Calling <tt>small()</tt> would return the value <tt>1</tt>. </li></ul>


  <li><tt>@def</tt> rules can be accessed as a String as well.  You can retrieve the two definitions above with: </li>

<pre class="prettyprint">interface MyResources extends CssResource {
  String small();
  String black();
}</pre>

<ul>
  <li>Calling <tt>small()</tt> returns "1px" </li>
  <li>Calling <tt>black()</tt> returns "#000" </li></ul>


  <li>The Generator will not allow you to declare an <tt>@def</tt> rule with the same name as a class, unless you annotate method to retrieve the class with the <tt>@ClassName</tt> annotation. </li>

<pre class="prettyprint">@def myIdent 10px;
.myIdent {
  ...
}

interface MyResources extends CssResource {
  String myIdent();

  @ClassName("myIdent")
  String myIdentClass();
}</pre>

<ul>
  <li>Calling <tt>myIdent()</tt> returns @def value "10px" </li>
  <li>Calling <tt>myIdentClass()</tt> returns the obfuscated class name for .myIdent </li></ul>

</ul>

<h4 id="Runtime_substitution">Runtime substitution</h4>

<pre class="prettyprint">@eval userBackground com.module.UserPreferences.getUserBackground();
div {
  background: userBackground;
}</pre>

<ul>
  <li>Provides runtime support for evaluating static methods when the stylesheet is injected.  Triggered / dynamic updates could be added in the future if we allow programmatic manipulation of the style elements. </li>

<ul>
  <li>If the user-defined function can be statically evaluated by the compiler, then the implementation of the specific CssResource should collapse to just a string literal. </li></ul>


  <li>This allows easy support for non-structural skinning changes. </li></ul>

<h4 id="Value_function">Value function</h4>

<pre class="prettyprint">.myDiv {
  offset-left: value('imageResource.getWidth', 'px');
}</pre>

<ul>
  <li>The <tt>value()</tt> function takes a sequence of dot-separated identifiers and an optional suffix.  The identifiers are interpreted as zero-arg method invocations, using the interface passed to GWT.create() as the root namespace.  By only allowing zero-arg methods, there's no need to attempt to perform type checking in the Generator.  The only validation necessary is to ensure that the sequence of methods exists.  There may be arbitrarily many identifiers in the chain. </li>
  <li>The <tt>value()</tt> function may be combined with <tt>@def</tt> </li>

<pre class="prettyprint">@def SPRITE_WIDTH value('imageResource.getWidth', 'px')

.selector {
  width: SPRITE_WIDTH;
}</pre></ul>

<h4 id="Literal_function">Literal function</h4>


<p>Some user agents make use of property values that do not conform to the CSS grammar.  The <tt>literal()</tt> function exists to allow these non-standard property values to be used. </p>

<pre class="prettyprint">div-with-literal {
  top: literal("expression(document.compatMode==\"CSS1Compat\" ? documentElement.scrollTop : document.body.scrollTop \\ 2)");
}</pre>

<p>Note that it is necessary to escape the backslash (<tt>\</tt>) and double-quote (<tt>"</tt>) characters. </p>

<h4 id="Conditional_CSS">Conditional CSS</h4>

<pre class="prettyprint">/* Runtime evaluation in a static context */
@if (com.module.Foo.staticBooleanFunction()) {
  ... css rules ...
}

/* Compile-time evaluation */
@if &lt;deferred-binding-property&gt; &lt;space-separated list of values&gt; {
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
}</pre>

<ul>
  <li>This allows for more advanced skinning / theming / browser quirk handling by allowing for structural changes in the CSS. </li>
  <li>The contents of an @if block can be anything that would be a top-level rule in a CSS stylesheet. </li>
  <li>@if blocks can be arbitrarily nested. </li>
  <li>What does it mean to have an @def or @eval in an @if block?  Easy to make this work for property-based @if statements; would have to generate pretty gnarly runtime code to handle the expression-based @if statement.  Could have block-level scoping; but this seems like a dubious use-case. </li>
  <li>If the function in the first form can be statically evaluated by the compiler in a permutation, there is no runtime cost.  The second form will never have a runtime cost because it is evaluated during compilation. </li></ul>

<h4 id="Image_Sprites">Image Sprites</h4>

<pre class="prettyprint">@sprite .mySpriteClass {gwt-image: "imageAccessor"; other: property;} =&gt; generates =&gt;

  .mySpriteClass {
    background-image: url(gen.png);
    clip: ...;
    width: 27px;
    height: 42px;
    other: property;
  }</pre>

<pre class="prettyprint">interface MyCssResource extends CssResource {
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
}</pre>

<ul>
  <li>@sprite is sensitive to the FooBundle in which the CSSResource is declared; a sibling <a href="/javadoc/latest/index.html?com/google/gwt/resources/client/ImageResource.html">ImageResource</a> method named in the @sprite declaration will be used to compose the background sprite. </li>
  <li>@sprite entries will be expanded to static CSS rules, possibly with data: urls. </li>
  <li>The expansion is sensitive to any RepeatStyle value defined on the <tt>ImageResource</tt> accessor function.  The appropriate <tt>repeat-x</tt> or <tt>repeat-y</tt> properties will be added to the @sprite selector. </li>
  <li>Any CSS selector can be specified for @sprite. </li>
  <li>Support for IE6 isn't feasible in this format, because structural changes to the DOM are necessary to implement a "windowing" effect.  Once it's possible to distinguish ie6 and ie7 in user.agent, we could revisit support for ie6.  In the current implementation, the ie6 code won't render correctly, although is a purely cosmetic issue. </li></ul>

<h4 id="References_to_Data_Resources">References to Data Resources</h4>

<pre class="prettyprint">/* @url &lt;constant name&gt; &lt;DataResource method name&gt; */
@url myCursorUrl fancyCursorResource;

.myClass {
  cursor: myCursorUrl, pointer;
}</pre>

<pre class="prettyprint">interface MyResources extends ClientBundle {
  @Source("myCursor.cur")
  DataResource fancyCursorResource();

  @Source("my.css")
  CssResource css();
}</pre>

<ul>
  <li>The identifier will be expanded to <tt>url('some_url')</tt> based on the return value of <tt>DataResource.getUrl()</tt>. </li></ul>

<h4 id="RTL_support">RTL support</h4>

<ul>
  <li>CssResource supports automatic transformations of CSS code into a right-to-left variant at compile time. </li>
  <li>The use of the RTL variant is keyed by <tt>com.google.gwt.i18n.client.LocaleInfo.getCurrentLocale().isRTL()</tt> </li>
  <li>Transformations applied: </li>

<ul>
  <li>The <tt>left</tt> and <tt>right</tt> properties are flipped. </li>
  <li>Any properties that have values <tt>left</tt> or <tt>right</tt> are flipped: <tt>clear float text-align page-break-before page-break-after</tt> </li>
  <li>The <tt>background</tt>/<tt>background-position</tt> property is flipped. Attachments expressed in percentage points are mirrored: 40% becomes 60% </li>
  <li><tt>margin padding border-color border-style</tt> and <tt>border-width</tt> four-valued properties are flipped: <tt>1px 2px 3px 4px</tt> becomes <tt>1px 4px 3px 2px</tt> </li>
  <li>Any <tt>xyz-right</tt> or <tt>xzy-right-abc</tt> property is flipped to <tt>xzy-left</tt> or <tt>xzy-left-abc</tt> </li>
  <li>The <tt>direction</tt> property on a <tt>body</tt> selector will be flipped from <tt>ltr</tt> to <tt>rtl</tt>; on any other selectors, the <tt>direction</tt> property is unchanged </li>
  <li>When the cursor property has an <tt>resize</tt> value, it will be flipped: <tt>ne-resize</tt> becomes <tt>nw-resize</tt> </li></ul>


  <li>Sections of CSS can be exempted from automatic flipping by enclosing it in a <tt>@noflip</tt> block: </li></p>

<pre class="prettyprint">@noflip {
  .selector {
    left: 10;
  }
}</pre>

  <li>A <tt>background</tt> property value that uses pixel-based offsets, such as <tt>background-position: 4px 10px;</tt> will not be transformed automatically. </li>

<ul>
  <li>The four-valued CSS3 <tt>background-position</tt> property will be automatically flipped by the RTL support </li>

<pre class="prettyprint">background-position: left 4px top 10px;</pre>

<li>For CSS2 browsers, it will be necessary to use an <tt>@sprite</tt> rule: </li>

<pre class="prettyprint">@sprite .bgImage {
  gwt-image: 'background-image';
  position: absolute;
  left: 4px;
  top: 10px;
}</pre>

</ul>


  <li><tt>ImageResources</tt> can be automatically flipped in RTL contexts via the use of the <tt>@ImageOptions</tt> annotation: </li>

<pre class="prettyprint">@Source("icon128.png")
@ImageOptions(flipRtl = true)
ImageResource logo();</pre>


  <li><a href="http://google-web-toolkit.googlecode.com/svn/releases/2.5/user/test/com/google/gwt/resources/rg">Current auto-RTL test cases</a> </li></ul>

<h4 id="Selector_obfuscation">Selector obfuscation</h4>

<pre class="prettyprint">java:
    class Resources {
      MyCSSResource myCSSResource();
    }
    class MyCSSResource extends CSSResource {
      Sprite mySpriteClass();
      String someOtherClass();
      String hookClass();
    }
    myWidget.addStyleName(resource.mySpriteClass());

css:
    @sprite mySpriteClass mySpriteImage;
    .someOtherClass {
      /* ... */
    }
    .hookClass{} /* Empty and stripped, but left for future expansion */</pre>

<ul>
  <li>The function just returns the CSS class name, but verifies that the CSS class exists in the stylesheet. </li>
  <li>Accessing class names through the interface ensures that there can be no typos in code that consumes the <tt>CssResource</tt>. </li>
  <li>For obfuscation, we'll use a Adler32 checksum of the source css file expressed in base36 as a prefix (7 chars). The developer can override this with the <tt>CssResource.obfuscationPrefix</tt> deferred-binding property. </li>

<ul>
  <li><tt>&lt;set-configuration-property name="CssResource.obfuscationPrefix" value="empty" /&gt;</tt> can be used for minimal-length selector names, but this is only recommended when the GWT module has total control over the page. </li></ul>


  <li>The <tt>@external</tt> at-rule can be used to selectively disable obfuscation for named selectors; see <a href="#External_and_legacy_scopes">external and legacy scopes</a> for additional detail. </li></ul>

<h3 id="Optimizations">Optimizations</h3>

<h4 id="Basic_minification">Basic minification</h4>

<p>Basic minification of the CSS input results in the minimum number of bytes required to retain the original structure of the input.  In general, this means that comments, unnecessary whitespace, and empty rules are removed. </p>

<pre class="prettyprint">.div {
  /* This is the default background color */
  background: blue;
}
.empty {}</pre>

<p>would be transformed into </p>

<pre class="prettyprint">.div{background:blue;}</pre>

<h4 id="Selector_merging">Selector merging</h4>

<p>Rules with identical selectors can be merged together. </p>

<pre class="prettyprint">.div {prop: value;}
.div {foo: bar;}</pre>

<p>becomes </p>

<pre class="prettyprint">.div {prop:value;foo:bar;} </pre>

<p>However, it is necessary that the original semantic ordering of the properties within the CSS is preserved.  To ensure that all selector merges are correct, we impose the restriction that <strong>no rule can be promoted over another if the two rules define a common property</strong>.  We consider <tt>border</tt> and <tt>border-top</tt> to be equivalent properties, however <tt>padding-left</tt> and <tt>padding-right</tt> are not equivalent. </p>



<p>Thus </p>

<pre class="prettyprint">.a {background: green;}
.b {border: thin solid blue;}
.a {border-top: thin solid red;}</pre>

<p>cannot be merged because an element whose CSS class matches both <tt>.a</tt> and <tt>.b</tt> would be rendered differently based on the exactly order of the CSS rules. </p>



<p>When working with <tt>@if</tt> statements, it is preferable to work with the form that operates on deferred-binding properties because the CSS compiler can evaluate these rules statically, before the merge optimizations.  Consider the following: </p>

<pre class="prettyprint">.a {
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
}</pre>

<p>In the safari permutation, the rule becomes <tt>.a{background:red;\-webkit-border-radius:5px;}</tt> while in other permutations, the <tt>background</tt> property is merged. </p>

<h4 id="Property_merging">Property merging</h4>

<p>Rules with identical properties can be merged together. </p>

<pre class="prettyprint">.a {background: blue;}
.b {background: blue;}</pre>

<p>can be transformed into </p>

<pre class="prettyprint">.a,.b{background:blue;}</pre>

<p>Promotion of rules follows the previously-established rule of not promoting a rule over other rules with common properties. </p>

<h3 id="Levers_and_Knobs">Levers and Knobs</h3>



<ul>
  <li>The configuration property <tt>CssResource.style</tt> may be set to <tt>pretty</tt> which will disable class-name obfuscation as well as pretty-print the CSS content.  Combine this with a <tt>ClientBundle.enableInlining</tt> value of <tt>false</tt> to produce a CSS expression which is amenable to client-side editing. </li>
  <li>The configuration property <tt>CssResource.mergeEnabled</tt> can be set to <tt>false</tt> to disable modifications that re-order rules.  This should be considered a temporary measure until the merge logic has been fully vetted. </li>
  <li>To allow for client-side tweaking of the effective (i.e. permutation-specific) style rules, you can store the value of <tt>CssResource</tt>.getText() into a TextArea.  Wire some UI action to pass the contents of the TextArea into <tt>StyleInjector.setContents()</tt> to overwrite the original, injected stylesheet. </li></ul>

<h3 id="Selector_obfuscation_details">Selector obfuscation details</h3>

<h4 id="Strict_scoping">Strict scoping</h4>

<p>In the normal case, any class selectors that do not match String accessor functions is an error.  This behavior can be disabled by adding a <tt>@NotStrict</tt> annotation to the CSS accessor method.  Enabling <tt>@NotStrict</tt> behavior is only recommended for applications that are transitioning from external CSS files to <tt>CssResource</tt>. </p>

<pre class="prettyprint">interface MyCssResource extends CssResource {
  String foo();
}

interface Resources {
  @Source("my.css")
  @CssResource.NotStrict
  MyCssResource css();
}</pre>

<pre class="prettyprint">/* This is ok */
.foo {}

/* This would normally generate a compile error in strict mode */
.other {}</pre>

<h4 id="Scope">Scope</h4>

<p>Scoping of obfuscated class names is defined by the return type of the <tt>CssResource</tt> accessor method in the resource bundle.  Each distinct return type will return a wholly separate collection of values for String accessor methods. </p>

<pre class="prettyprint">interface A extends CssResource {
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
  D d2();</pre>

<p>It will be true that a().foo() != b().foo() != c().foo() != d().foo().  However, a().foo() == a2().foo() and d().foo() == d2().foo(). </p>

<h4 id="Shared_scopes">Shared scopes</h4>

<p>In the case of "stateful" CSS classes like <tt>focused</tt> or <tt>enabled</tt>, it is convenient to allow for certain String accessor functions to return the same value, regardless of the <tt>CssResource</tt> type returned from the accessor method. </p>

<pre class="prettyprint">

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
}</pre>

<p>In this example, a().focused() == b().focused() == c().focused == f().focused().  However, a().widget() != b().widget != c.widget(), as in the previous example. </p>



<p>The short version is that if distinct CSS types need to share obfuscated class names, the <tt>CssResource</tt> subtypes to which they are attached must share a common supertype that defines accessors for those names and has the <tt>@Shared</tt> annotation. </p>

<h4 id="Imported_scopes">Imported scopes</h4>

<p>The Java type system can be somewhat ambiguous when it comes to multiple inheritance of interfaces that define methods with identical signatures, although there exist a number of cases where it is necessary to refer to multiple, unrelated <tt>CssResource</tt> types.  Consider the case of a Tree that contains Checkboxes. </p>

<pre class="prettyprint">@ImportedWithPrefix("tree")
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
}</pre>

<pre class="prettyprint">/* Now we can write a descendant selector using the prefixes defined on the CssResource types */
.tree-widget .checkbox-widget {
  color: red;
}

.other {
  something: else;
}</pre>

<p>Composing a "TreeCbCss" interface would be insufficient because consumers of the TreeCss interface and CbCss interface would receive the same value from the widget method.  Moreover, the use of just <tt>.widget</tt> in the associated CSS file would also be insufficient without the use of some kind of class selector prefix.  The prefix is defined on the <tt>CssResource</tt> type (instead of on the <tt>CssResource</tt> accessor method) In the interest of uniformity across all CSS files that import a given scope.  It is a compile-time error to import multiple classes that have the same prefix or simple name. </p>



<p>The case of shared scopes could be handled solely with importing scopes, however this form is somewhat more verbose and relationships between unrelated scopes is less common than the use of stateful selectors. </p>

<h5 id="Example_StackPanel">Example: StackPanel inside a StackPanel</h5>

<p>This is a use-case that is currently impossible to style correctly in GWT. </p>

<pre class="prettyprint">// Assume this interface is provided by the UI library
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
}</pre>

<p>The file <tt>stackPanel.css</tt> defines the basic structure of any given stackPanel: </p>

<pre class="prettyprint">.widget .title {}
.widget .content {}
/* Other stuff to make a StackPanel work */</pre>

<p>The <tt>outer()</tt> method can continue to use the base <tt>stackPanel.css</tt> file, because the accessor methods defined in <tt>StackPanelCss</tt> are mapped into the default (no-prefix) namespace.  The inner StackPanel's style members are also available, but in the <tt>inner</tt> prefix.  Here's what <tt>outer.css</tt> might contain: </p>

<pre class="prettyprint">.widget {color: red;}

.inner-widget {
  color: blue;
  font-size: smaller;
}</pre>

<h4 id="External_and_legacy_scopes">External and legacy scopes</h4>

<p>In many cases, newly-developed CSS will need to be combined with external or legacy CSS. The <tt>@external</tt> at-rule can be used to suppress selector obfuscation while still allowing programmatic access to the selector name. </p>

<pre class="prettyprint">interface MyCssResource extends CssResource {
  String obfuscated();
  String legacySelectorA();
}

interface Resource extends ClientBundle {
  @Source("my.css")
  MyCssResource css();
}</pre>

<pre class="prettyprint">@external legacySelectorA, legacySelectorB;
.obfuscated .legacySelectorA { .... }
.obfuscated .legacySelectorB { .... }</pre>

<p>In the above example, the <tt>.obfuscated</tt> class selector will be obfuscated, and the <tt>obfuscated()</tt> method will return the replaced name. Neither of the legacy selectors will be obfuscated and the <tt>legacySelectorA()</tt> method will return the unobfuscated value.  Furthermore, because the <tt>legacySelectorB</tt> is explicitly defined in the <tt>@external</tt> declaration, the inaccessible class name will not trigger an error. </p>


<h3 id="Automatically_generating_interfaces">Automatically generating <tt>CssResource</tt> interfaces</h3>

<p>A utility is included in the GWT distribution which will analyze a <tt>CssResource</tt>-compatible CSS file and create a corresponding Java interface for accessing the classnames used in the file. </p>

<pre class="prettyprint">java -cp gwt-dev.jar:gwt-user.jar com.google.gwt.resources.css.InterfaceGenerator \
  -standalone -typeName some.package.MyCssResource -css input.css</pre>

<p>The generated interface will be emitted to <tt>System.out</tt>. The <tt>-standalone</tt> option will add the necessary <tt>package</tt> and <tt>import</tt> statements to the output so that it can be used as part of a build process. </p>


<h2 id="CssResourceCookbook">CssResourceCookbook</h2>

<p>This section contains examples showing how to use <a href="#CssResource">CssResource</a>.</p>

<h3 id="Browser-specific_css">Browser-specific css</h3>

<pre class="prettyprint">.foo {
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
}</pre>


<h3 id="Obfuscated_CSS_class_names">Obfuscated CSS class names</h3>

<p><tt>CssResource</tt> will use method names as CSS class names to obfuscate at runtime. </p>

<pre class="prettyprint">interface MyCss extends CssResource {
  String className();
}

interface MyResources extends ClientBundle {
  @Source("my.css")
  MyCss css();
}</pre>

<p>All instances of a selector with <tt>.className</tt> will be replaced with an obfuscated symbol when the CSS is compiled.  To use the obfuscated name: </p>

<pre class="prettyprint">MyResources resources = GWT.create(MyResources.class);
Label l = new Label("Some text");
l.addStyleName(resources.css().className());</pre>

<p>If you have class names in your css file that are not legal Java identifiers, you can use the <tt>@ClassName</tt> annotation on the accessor method: </p>

<pre class="prettyprint">interface MyCss extends CssResource {
  @ClassName("some-other-name")
  String someOtherName();
}</pre>


<h3 id="Background_images_/_Sprites">Background images / Sprites</h3>

<p><tt>CssResource</tt> reuses the <tt>ImageResource</tt> bundling techniques and applies them to CSS background images.  This is generally known as "spriting" and a special <tt>@sprite</tt> rule is used in <tt>CssResource</tt>. </p>

<pre class="prettyprint">interface MyResources extends ClientBundle {
  @Source("image.png")
  ImageResource image();

  @Source("my.css");
  CssResource css();
}</pre>

<p>In <tt>my.css</tt>, sprites are defined using the <tt>@sprite</tt> keyword, followed by an arbitrary CSS selector, and the rule block must include a <tt>gwt-image</tt> property.  The <tt>gwt-image</tt> property should name the <tt>ImageResource</tt> accessor function. </p>

<pre class="prettyprint">@sprite .myImage {
  gwt-image: 'image';
}</pre>

<p>The elements that match the given selection will display the named image and have their heights and widths automatically set to that of the image. </p>

<h4 id="Tiled_images">Tiled images</h4>

<p>If the <tt>ImageResource</tt> is decorated with an <tt>@ImageOptions</tt> annotation, the source image can be tiled along the X- or Y-axis.  This allows you to use 1-pixel wide (or tall) images to define borders, while still taking advantage of the image bundling optimizations afforded by <tt>ImageResource</tt>. </p>

<pre class="prettyprint">interface MyResources extends ClientBundle {
  @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
  @Source("image.png")
  ImageResource image();
}</pre>

<p>The elements that match the <tt>@sprite</tt>'s selector will only have their height or width set, based on the direction in which the image is to be repeated. </p>

<h4 id="9-boxes">9-boxes</h4>

<p>In order to make the content area of a 9-box have the correct size, the height and widths of the border images must be taken into account.  Instead of hard-coding the image widths into your CSS file, you can use the <tt>value()</tt> CSS function to insert the height or width from the associated <tt>ImageResource</tt>. </p>


<pre class="prettyprint">  public interface Resources extends ClientBundle {
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
  }</pre>

<pre class="prettyprint">.contentArea {
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
}</pre>

<pre class="prettyprint">&lt;div class="contentArea"&gt;

&lt;div class="contentAreaTopLeftBorder"&gt;&lt;/div&gt;
&lt;div class="contentAreaTopBorder"&gt;&lt;/div&gt;
&lt;div class="contentAreaTopRightBorder"&gt;&lt;/div&gt;
&lt;div class="contentAreaBottomLeftBorder"&gt;&lt;/div&gt;
&lt;div class="contentAreaBottomBorder"&gt;&lt;/div&gt;

&lt;div class="contentAreaBottomRightBorder"&gt;&lt;/div&gt;
&lt;div class="contentAreaLeftBorder"&gt;&lt;/div&gt;
&lt;div class="contentAreaRightBorder"&gt;&lt;/div&gt;
&lt;/div&gt;</pre>


