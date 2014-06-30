<p>Deferred binding is a feature of the GWT compiler that works by generating many versions of code at compile time, only one of which needs to be loaded by a particular client
during bootstrapping at runtime. Each version is generated on a per browser basis, along with any other axis that your application defines or uses. For example, if you were to
internationalize your application using <a href="DevGuideI18n.html">GWT's Internationalization module</a>, the GWT compiler would generate
various versions of your application per browser environment, such as &quot;Firefox in English&quot;, &quot;Firefox in French&quot;, &quot;Internet Explorer in English&quot;, etc... As a result, the deployed
JavaScript code is compact and quicker to download than hand coded JavaScript, containing only the code and resources it needs for a particular browser environment.</p>

<ol class="toc" id="pageToc">
  <li><a href="#benefits">Deferred Binding Benefits</a></li>
  <li><a href="#rules">Defining Deferred Binding Rules</a></li>
  <li><a href="#directives">Directives in Module XML files</a></li>
  <li><a href="#replacement">Deferred Binding Using Replacement</a></li>
  <li><a href="#example">Example Class Hierarchy using Replacement</a></li>
  <li><a href="#generators">Deferred Binding using Generators</a></li>
  <li><a href="#generator">Generator Configuration in Module XML</a></li>
  <li><a href="#generator-implementation">Generator Implementation</a></li>
</ol>

<h2 id="benefits">Deferred Binding Benefits</h2>

<p>Deferred Binding is a technique used by the GWT compiler to create and select a specific implementation of a class based on a set of parameters. In essence, deferred binding is
the GWT answer to Java reflection. It allows the GWT developer to produce several variations of their applications custom to each browser environment and have only
one of them actually downloaded and executed in the browser.</p>

<p>Deferred binding has several benefits:</p>

<ul>
<li>Reduces the size of the generated JavaScript code that a client will need to download by only including the code needed to run a particular browser/locale instance (used by
the <a href="DevGuideI18n.html">Internationalization module</a>)</li>

<li>Saves development time by automatically generating code to implement an interface or create a proxy class (used by the <a href="DevGuideServerCommunication.html#DevGuideRemoteProcedureCalls">GWT RPC module</a>)</li>

<li>Since the implementations are pre-bound at compile time, there is no run-time penalty to look up an implementation in a data structure as with dynamic binding or using virtual
functions.</li>
</ul>

<p>Some parts of the toolkit make implicit use of deferred binding, that is, they use the technique as a part of their implementation, but it is not visible to the user of the
API. For example, many <a href="DevGuideUiWidgets.html">widgets and <a href="DevGuideUiPanels.html">panels</a> as well as the <a href="/javadoc/latest/com/google/gwt/user/client/DOM.html">DOM</a> class use this technique to implement browser specific
logic. Other GWT features require the API user to explicity invoke deferred binding by designing classes that follow specific rules and instantiating instances of the classes with <a href="/javadoc/latest/com/google/gwt/core/client/GWT.html#create(java.lang.Class)">GWT.create(Class)</a>, including <a href="DevGuideServerCommunication.html#DevGuideRemoteProcedureCalls">GWT RPC</a> and <a href="DevGuideI18n.html">I18N</a>.</p>

<p>As a user of the GWT, you may never need to create a new interface that uses deferred binding. If you follow the instructions in the guide for creating
internationalized applications or GWT RPC calls you will be using deferred binding, but you will not have to actually write any browser dependent or locale dependent code.</p>

<p>The rest of the deferred binding section describes how to create new rules and classes using deferred binding. If you are new to the toolkit or only intend to use pre-packaged
widgets, you will probably want to skip on to the next topic. If you are interested in programming entirely new widgets from the ground up or other functionality that requires
cross-browser dependent code, the next sections should be of interest.</p>

<h2 id="rules">Defining Deferred Binding Rules</h2>

<p>There are two ways in which types can be replaced via deferred binding:</p>

<ul>
<li>Replacement: A type is replaced with another depending on a set of configurable rules.</li>
<li>Code generation: A type is substituted by the result of invoking a code generator at compile time.</li>
</ul>

<h2 id="directives">Directives in Module XML files</h2>

<p>The deferred binding mechanism is completely configurable and does not require editing the GWT distributed source code. Deferred binding is configured through the
<tt>&lt;replace-with&gt;</tt> and <tt>&lt;generate-with&gt;</tt> elements in the <a href="DevGuideOrganizingProjects.html#DevGuideModuleXml">module XML files</a>. The deferred binding
rules are pulled into the module build through <tt>&lt;inherits&gt;</tt> elements.</p>

<p>For example, the following configuration invokes deferred binding for the <a href="/javadoc/latest/com/google/gwt/user/client/ui/PopupPanel.html">PopupPanel</a> widget:</p>

<ul>
<li>Top level <i>&lt;module&gt;</i>.gwt.xml <i><strong>inherits</strong></i> <a href="https://gwt.googlesource.com/gwt/+/master/user/src/com/google/gwt/user/User.gwt.xml">com.google.gwt.user.User</a></li>

<li><a href="https://gwt.googlesource.com/gwt/+/master/user/src/com/google/gwt/user/User.gwt.xml">com/google/gwt/user/User.gwt.xml</a>
<i><strong>inherits</strong></i> <a href="https://gwt.googlesource.com/gwt/+/master/user/src/com/google/gwt/user/Popup.gwt.xml">com.google.gwt.user.Popup</a></li>

<li><a href="https://gwt.googlesource.com/gwt/+/master/user/src/com/google/gwt/user/Popup.gwt.xml">com/google/gwt/user/Popup.gwt.xml</a>
<i><strong>contains</strong></i> <tt>&lt;replace-with&gt;</tt> elements to define deferred binding rules for the <a href="/javadoc/latest/com/google/gwt/user/client/ui/PopupPanel.html">PopupPanel</a> class.</li>
</ul>

<p>Inside the <a href="/javadoc/latest/com/google/gwt/user/client/ui/PopupPanel.html">PopupPanel</a> module XML file, there
happens to be some rules defined for deferred binding. In this case, we're using a replacement rule.</p>


<h2 id="replacement">Deferred Binding Using Replacement</h2>

<p>
The first type of deferred binding uses <i>replacement</i>.
Replacement means overriding the implementation of one java class with another that is determined at compile time.
For example, this technique is used to conditionalize the implementation of some widgets, such as the <a href="/javadoc/latest/com/google/gwt/user/client/ui/PopupPanel.html">PopupPanel</a>.
The use of <tt>&lt;inherits&gt;</tt> for the <tt>PopupPanel</tt> class is shown in the previous section describing the deferred binding rules.
The actual replacement rules are specified in <tt>Popup.gwt.xml</tt>, as shown below:
</p>

<pre class="prettyprint">
&lt;module&gt;

  &lt;!--  ... other configuration omitted ... --&gt;

  &lt;!-- Fall through to this rule is the browser isn't IE or Mozilla --&gt;
  &lt;replace-with class=&quot;com.google.gwt.user.client.ui.impl.PopupImpl&quot;&gt;
    &lt;when-type-is class=&quot;com.google.gwt.user.client.ui.impl.PopupImpl&quot;/&gt;
  &lt;/replace-with&gt;

  &lt;!-- Mozilla needs a different implementation due to issue #410 --&gt;
  &lt;replace-with class=&quot;com.google.gwt.user.client.ui.impl.PopupImplMozilla&quot;&gt;
    &lt;when-type-is class=&quot;com.google.gwt.user.client.ui.impl.PopupImpl&quot; /&gt;
    &lt;any&gt;
      &lt;when-property-is name=&quot;user.agent&quot; value=&quot;gecko&quot;/&gt;
      &lt;when-property-is name=&quot;user.agent&quot; value=&quot;gecko1_8&quot; /&gt;
    &lt;/any&gt;
  &lt;/replace-with&gt;

  &lt;!-- IE has a completely different popup implementation --&gt;
  &lt;replace-with class=&quot;com.google.gwt.user.client.ui.impl.PopupImplIE6&quot;&gt;
    &lt;when-type-is class=&quot;com.google.gwt.user.client.ui.impl.PopupImpl&quot;/&gt;
    &lt;when-property-is name=&quot;user.agent&quot; value=&quot;ie6&quot; /&gt;
  &lt;/replace-with&gt;
&lt;/module&gt;
</pre>

<p>These directives tell the GWT compiler to swap out the <tt>PopupImpl</tt> class code with different class implementations according to the <tt>user.agent</tt> property. The
<tt>Popup.gwt.xml</tt> file specifies a default implementation for the <tt>PopupImpl</tt> class, an overide for the Mozilla browser (<tt>PopupImplMozilla</tt> is substituted for
<tt>PopupImpl</tt>), and an override for Internet Explorer version 6 (<tt>PopupImplIE6</tt> is substituted for <tt>PopupImpl</tt>). Note that <tt>PopupImpl</tt> class or its
derived classes cannot be instantiated directly. Instead, the <tt>PopupPanel</tt> class is used and the <a href="/javadoc/latest/com/google/gwt/core/client/GWT.html#create(java.lang.Class)">GWT.create(Class)</a> technique is used
under the hood to instruct the compiler to use deferred binding.</p>

<h2 id="example">Example Class Hierarchy using Replacement</h2>

<p>To see how this is used when designing a widget, we will examine the case of the <tt>PopupPanel</tt> widget further. The <tt>PopupPanel</tt> class implements the user visible
API and contains logic that is common to all browsers. It also instantiates the proper implementation specific logic using the <a href="/javadoc/latest/com/google/gwt/core/client/GWT.html#create(java.lang.Class)">GWT.create(Class)</a> as follows:</p>

<pre class="prettyprint">
  private static final PopupImpl impl = GWT.create(PopupImpl.class);
</pre>

<p>The two classes PopupImplMozilla and PopupImplIE6 extend the PopupImpl class and override some <tt>PopupImpl</tt>'s methods to implement browser specific behavior.</p>

<p>Then, when the <tt>PopupPanel</tt> class needs to switch to some browser dependent code, it accesses a member function inside the <tt>PopupImpl</tt> class:</p>

<pre class="prettyprint">
  public void setVisible(boolean visible) {
    // ... common code for all implementations of PopupPanel ...

    // If the PopupImpl creates an iframe shim, it's also necessary to hide it
    // as well.
    impl.setVisible(getElement(), visible);
  }
</pre>

<p>The default implementation of <tt>PopupImpl.setVisible()</tt> is empty, but <tt>PopupImplIE6</tt> has some special logic implemented as a <a href="DevGuideCodingBasics.html#DevGuideJavaScriptNativeInterface">JSNI</a> method:</p>

<pre class="prettyprint">
  public native void setVisible(Element popup, boolean visible) /*-{
    if (popup.__frame) {
      popup.__frame.style.visibility = visible ? 'visible' : 'hidden';
    }
  }-*/;{
</pre>

<p>After the GWT compiler runs, it prunes out any unused code. If your application references the <tt>PopupPanel</tt> class, the compiler will create a separate JavaScript output
file for each browser, each containing only one of the implementations: <tt>PopupImpl</tt>, <tt>PopupImplIE6</tt> or <tt>PopupImplMozilla</tt>. This means that each browser only
downloads the implementation it needs, thus reducing the size of the output JavaScript code and minimizing the time needed to download your application from the server.</p>

<h2 id="generators">Deferred Binding using Generators</h2>

<p>The second technique for deferred binding consists of using <i>generators</i>. Generators are classes that are invoked by the GWT compiler to generate a Java implementation of
a class during compilation. When compiling for production mode, this generated implementation is directly translated to one of the versions of your application in JavaScript code that a
client will download based on its browser environment.</p>

<p>The following is an example of how a deferred binding generator is specified to the compiler in the <a href="DevGuideOrganizingProjects.html#DevGuideModuleXml">module XML file</a>
hierarchy for the <tt>RemoteService</tt> class - used for GWT-RPC:</p>

<ul>
<li>Top level <i>&lt;module&gt;</i>.gwt.xml <i><strong>inherits</strong></i> <a href="https://gwt.googlesource.com/gwt/+/master/user/src/com/google/gwt/user/User.gwt.xml">com.google.gwt.user.User</a></li>

<li><a href="https://gwt.googlesource.com/gwt/+/master/user/src/com/google/gwt/user/User.gwt.xml">com/google/gwt/user/User.gwt.xml</a>
<i><strong>inherits</strong></i> <a href="https://gwt.googlesource.com/gwt/+/master/user/src/com/google/gwt/user/RemoteService.gwt.xml">com.googl.gwt.user.RemoteService</a></li>

<li><a href="https://gwt.googlesource.com/gwt/+/master/user/src/com/google/gwt/user/RemoteService.gwt.xml">com/google/gwt/user/RemoteService.gwt.xml</a>
<i><strong>contains</strong></i> <tt>&lt;generates-with&gt;</tt> elements to define deferred binding rules for the <tt>RemoteService</tt> class.</li>
</ul>

<h2 id="generator">Generator Configuration in Module XML</h2>

<p>The XML element <tt>&lt;generate-with&gt;</tt> tells the compiler to use a <tt>Generator</tt> class. Here are the contents of the <tt>RemoteService.gwt.xml</tt> file relevant
to deferred binding:</p>

<pre class="prettyprint">
&lt;module&gt;

 &lt;!--  ... other configuration omitted ... --&gt;

 &lt;!-- Default warning for non-static, final fields enabled --&gt;
 &lt;set-property name=&quot;gwt.suppressNonStaticFinalFieldWarnings&quot; value=&quot;false&quot; /&gt;

 &lt;generate-with class=&quot;com.google.gwt.user.rebind.rpc.ServiceInterfaceProxyGenerator&quot;&gt;
   &lt;when-type-assignable class=&quot;com.google.gwt.user.client.rpc.RemoteService&quot; /&gt;
 &lt;/generate-with&gt;
&lt;/module&gt;
</pre>

<p>These directives instruct the GWT compiler to invoke methods in a <a href="/javadoc/latest/com/google/gwt/core/ext/Generator.html">Generator</a> subclass (<tt>ServiceInterfaceProxyGenerator</tt>) in order to generate special code when the deferred binding mechanism <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/core/client/GWT.html#create(java.lang.Class)">GWT.create()</a> is encountered while
compiling. In this case, if the <a href="/javadoc/latest/com/google/gwt/core/client/GWT.html#create(java.lang.Class)">GWT.create()</a> call references an instance of <tt>RemoteService</tt> or one of its subclasses, the <tt>ServiceInterfaceProxyGenerator</tt>'s generate() method
will be invoked.</p>

<h2 id="generator-implementation">Generator Implementation</h2>

<p>Defining a subclass of the <tt>Generator</tt> class is much like defining a plug-in to the GWT compiler. The <tt>Generator</tt> gets called to generate a Java class definition
before the Java to JavaScript conversion occurs. The implementation consists of one method that must output Java code to a file and return the name of the generated class as a
string.</p>

<p>The following code shows the <tt>Generator</tt> that is responsible for deferred binding of a <tt>RemoteService</tt> interface:</p>

<pre class="prettyprint">
/**
 * Generator for producing the asynchronous version of a
 * {@link com.google.gwt.user.client.rpc.RemoteService RemoteService} interface.
 */
public class ServiceInterfaceProxyGenerator extends Generator {

  /**
   * Generate a default constructible subclass of the requested type. The
   * generator throws &lt;code&gt;UnableToCompleteException&lt;/code&gt; if for any reason
   * it cannot provide a substitute class
   *
   * @return the name of a subclass to substitute for the requested class, or
   *         return &lt;code&gt;null&lt;/code&gt; to cause the requested type itself to be
   *         used
   *
   */
  public String generate(TreeLogger logger, GeneratorContext ctx,
      String requestedClass) throws UnableToCompleteException {

    TypeOracle typeOracle = ctx.getTypeOracle();
    assert (typeOracle != null);

    JClassType remoteService = typeOracle.findType(requestedClass);
    if (remoteService == null) {
      logger.log(TreeLogger.ERROR, &quot;Unable to find metadata for type '&quot;
          + requestedClass + &quot;'&quot;, null);
      throw new UnableToCompleteException();
    }

    if (remoteService.isInterface() == null) {
      logger.log(TreeLogger.ERROR, remoteService.getQualifiedSourceName()
          + &quot; is not an interface&quot;, null);
      throw new UnableToCompleteException();
    }

    ProxyCreator proxyCreator = new ProxyCreator(remoteService);

    TreeLogger proxyLogger = logger.branch(TreeLogger.DEBUG,
        &quot;Generating client proxy for remote service interface '&quot;
            + remoteService.getQualifiedSourceName() + &quot;'&quot;, null);

    return proxyCreator.create(proxyLogger, ctx);
  }
}
</pre>

<p>The <tt>typeOracle</tt> is an object that contains information about the Java code that has already been parsed that the generator may need to consult. In this case, the
<tt>generate()</tt> method checks it arguments and the passes off the bulk of the work to another class (<tt>ProxyCreator</tt>).</p>


