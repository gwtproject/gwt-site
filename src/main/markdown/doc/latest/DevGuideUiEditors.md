<div style="font-style:italic; margin-top: 3px">Data binding for bean-like objects</div> 
 
 <p>
The GWT Editor framework allows data stored in an object graph to be mapped onto a graph of Editors.  The typical scenario is wiring objects returned from an RPC mechanism into a UI.
</p>

<ol class="toc" id="pageToc">  
  <li><a href="#Goals">Goals</a></li>
  <li><a href="#Quickstart">Quickstart</a></li>
  <li><a href="#Definitions">Definitions</a></li>
  <li><a href="#General_workflow">General workflow</a></li>
  <li><a href="#Editor_contract">Editor contract</a></li>
  <li><a href="#Editor_delegates">Editor delegates</a></li>
  <li><a href="#Editor_subtypes">Editor subtypes</a>
    <ol class="toc">
      <li><a href="#LeafValueEditor">LeafValueEditor</a></li>
      <li><a href="#HasEditorDelegate">HasEditorDelegate</a></li>
      <li><a href="#ValueAwareEditor">ValueAwareEditor</a></li>
      <li><a href="#CompositeEditor">CompositeEditor</a></li>
      <li><a href="#HasEditorErrors">HasEditorErrors</a></li>
    </ol>
  </li>
  <li><a href="#Provided_Adapters">Provided Adapters</a></li>
  <li><a href="#Driver_types">Driver types</a></li>
  <li><a href="#FAQ">FAQ</a>
    <ol>
      <li><a href="#Editor_vs._IsEditor">Editor vs. IsEditor</a></li>
      <li><a href="#Read-only_Editors">Read-only Editors</a></li>
      <li><a href="#Very_large_objects">Very large objects</a></li>
    </ol>
  </li>
</ol>


<h2><a name="Goals"></a>Goals</h2>

<ul>
  <li>Decrease the amount of glue code necessary to move data from an object graph into a UI and back.</li>
  <li>Be compatible with any object that looks like a bean, regardless of its implementation mechanism
      (POJO, JSO, RPC, <a href="DevGuideRequestFactory.html">RequestFactory</a>).</li>
  <li>Support arbitrary composition of Editors.</li>
  <li>For post-GWT 2.1 release, establish the following trajectories:</li>
  <ul>
    <li>Create an API that can be used by <a href="DevGuideUiBinder.html">UiBinder</a></li>
    <li>Support client-side JSR 303 Validation when it's available</li>
  </ul>
</ul>

<h2 id="Quickstart"></a>Quickstart</h2>

<p>
Import the <code>com.google.gwt.editor.Editor</code> module in your <code>gwt.xml</code> file.
</p>

<pre class="prettyprint">// Regular POJO, no special types needed
public class Person {
  Address getAddress();
  Person getManager();
  String getName();
  void setManager(Person manager);
  void setName(String name);
}
 
// Sub-editors are retrieved from package-protected fields, usually initialized with UiBinder.
// Many Editors have no interesting logic in them
public class PersonEditor extends Dialog implements Editor&lt;Person&gt; {
  // Many GWT Widgets are already compatible with the Editor framework
  Label nameEditor;
  // Building Editors is usually just composition work
  AddressEditor addressEditor;
  ManagerSelector managerEditor;
 
  public PersonEditor() {
    // Instantiate my widgets, usually through UiBinder
  }
}
 
// A simple demonstration of the overall wiring
public class EditPersonWorkflow{
  // Empty interface declaration, similar to UiBinder
  interface Driver extends SimpleBeanEditorDriver&lt;Person, PersonEditor&gt; {}
 
  // Create the Driver
  Driver driver = GWT.create(Driver.class);
 
  void edit(Person p) {
    // PersonEditor is a DialogBox that extends Editor&lt;Person&gt;
    PersonEditor editor = new PersonEditor();
    // Initialize the driver with the top-level editor
    driver.initialize(editor);
    // Copy the data in the object into the UI
    driver.edit(p);
     // Put the UI on the screen.
    editor.center();
  }
 
  // Called by some UI action
  void save() {
    Person edited = driver.flush();
    if (driver.hasErrors()) {
      // A sub-editor reported errors
    }
    doSomethingWithEditedPerson(edited);
  }
}</pre>

<h2 id="Definitions"></a>Definitions</h2>

<ul>
  <li><i>Bean-like object</i>: (henceforth &quot;bean&quot;) An object that supports retrieval of properties through strongly-typed <code>Foo getFoo()</code> methods with optional <code>void setFoo(Foo foo);</code> methods.</li>
  <li><i>Editor</i>: An object that supports editing zero or more properties of a bean.</li>
  <ul>
    <li>An Editor may be composed of an arbitrary number of sub-Editors that edit the properties of a bean.</li>
    <li>Most Editors are Widgets, but the framework does not require this.  It is possible to create &quot;headless&quot; Editors that perform solely programmatically-driven changes.</li>
  </ul>
  <li><i>Driver</i>: The &quot;top-level&quot; controller used to attach a bean to an Editor.  The driver is responsible for descending into the Editor hierarchy to propagate data.  Examples include the <code>SimpleBeanEditorDriver</code> and the <code>RequestFactoryEditorDriver</code>.</li>
  <li><i>Adapter</i>: One of a number of provided types that provide &quot;canned&quot; behaviors for the Editor framework.</li>
</ul>

<h2 id="General_workflow"></a>General workflow</h2>

<ul>
  <li>Instantiate and initialize the Editors.</li>
  <ul>
    <li>If the Editors are UI based, this is usually the time to call <code>UiBinder.createAndBindUi()</code></li>
  </ul>
  
  <li>Instantiate and initialize the driver.</li>
  <ul>
    <li>Drivers are created through a call to <code>GWT.create()</code> and the specific details of the initialization are driver-dependent, although passing in the editor instance is common.</li>
    <li>Because the driver is stateful, driver instances must be paired with editor hierarchy instances.</li>
  </ul>

  <li>Start the editing process by passing the bean into the driver.</li>
  <li>Allow the user to interact with the UI.</li>
  <li>Call the <code>flush()</code> method on the driver to copy Editor state into the bean hierarchy.</li>
  <li>Optionally check <code>hasErrors()</code> and <code>getErrors()</code> to determine if there are client-side input validation problems.</li>
</ul>

<h2 id="Editor_contract"></a>Editor contract</h2>

<p>
The basic <code>Editor</code> type is simply a parameterized marker interface that indicates that a type conforms to the editor contract or informal protocol.  The only expected behavior of an <code>Editor</code> is that it will provide access to its sub-Editors via one or more of the following mechanisms: 
</p>

<ul>
  <li>An instance field with at least package visibility whose name exactly the property that will be edited or <code>propertyNameEditor</code>.  For example:

<pre class="prettyprint">class MyEditor implements Editor&lt;Foo&gt; {
  // Edits the Foo.getBar() property
  BarEditor bar;
  // Edits the Foo.getBaz() property
  BazEditor bazEditor;
}</pre>
  </li>

  <li>A no-arg method with at least package visibility whose name exactly is the property that will be edited or <code>propertyNameEditor</code>.  This allows the use of interfaces for defining the Editor hierarchy. For example:

<pre class="prettyprint">interface FooEditor extends Editor&lt;Foo&gt; {
  // Edits the Foo.getBar() property
  BarEditor bar();
  // Edits the Foo.getBaz() property
  BazEditor bazEditor();
}</pre>
  </li>

  <li>The <code>@Path</code> annotation may be used on the field or accessor method to specify a dotted property path or to bypass the implicit naming convention.  For example:

<pre class="prettyprint">class PersonEditor implements Editor&lt;Person&gt; {
  // Corresponds to person.getManager().getName()
  @Path(&quot;manager.name&quot;);
  Label managerName;
}</pre>
  </li>

  <li>The <code>@Ignored</code> annotation may be used on a field or accessor method to make the Editor framework ignore something that otherwise appears to be a sub-Editor.</li>

  <li>Sub-Editors may be null. In this case, the Editor framework will ignore these sub-editors.</li>
</ul>

<p>
Where the type <code>Editor&lt;T&gt;</code> is used, the type <code>IsEditor&lt;Editor&lt;T&gt;&gt;</code> may be substituted.  The <code>IsEditor</code> interface allows composition of existing Editor behavior without the need to implement N-many delegate methods in the composed Editor type.  For example, most leaf GWT Widget types implement <code>IsEditor</code> and are immediately useful in an Editor-based UI.  By implementing <code>IsEditor</code>, the Widgets need only implement the single <code>asEditor()</code> method, which isolates the Widgets from any API changes that may occur in the component Editor logic.
</p>

<h2 id="Editor_delegates"></a>Editor delegates</h2>

<p>
Every <code>Editor</code> has a peer <code>EditorDelegate</code> that provides framework-related services to the Editor. 
</p>
<ul>
  <li><code>getPath()</code> returns the current path of the Editor within an attached Editor hierarchy.</li>
  <li><code>recordError()</code> allows an Editor to report input validation errors to its parent Editors and eventually to the driver.  Arbitrary data can be attached to the generated <code>EditorError</code> by using the <code>userData</code> parameter.</li>
  <li><code>subscribe()</code> can be used to receive notifications of external updates to the object being edited.  Not all drivers may support subscription.  In this case, the call to <code>subscribe()</code> may return <code>null</code>.</li>
</ul>

<h2 id="Editor_subtypes"></a>Editor subtypes</h2>

<p>
In addition to the <code>Editor</code> interface, the Editor framework looks for these specific interfaces to provide basic building blocks for more complicated Editor behaviors.  This section will document these interfaces and provide examples of how the Editor framework will interact with the API at runtime.  All of these core Editor sub-interface can be mixed at will.
</p>

<h3><a name="LeafValueEditor"></a>LeafValueEditor</h3>

<p>
<code>LeafValueEditor</code> is used for non-object, immutable, or any type that the Editor framework should not descend into.
</p>
<ol>
  <li><code>setValue()</code> is called with the value that should be edited (e.g. <code>fooEditor.setValue(bean.getFoo());</code>).</li>
  <li><code>getValue()</code> is called when the Driver is flushing the state of the Editors into the bean.  The value returned from this method will be assigned to the bean being edited (e.g. <code>bean.setFoo(fooEditor.getValue());</code>).</li>
</ol>

<h3><a name="HasEditorDelegate"></a>HasEditorDelegate</h3>

<p>
<code>HasEditorDelegate</code> provides an Editor with its peer <code>EditorDelegate</code>.
</p>
<ol>
  <li><code>setEditorDelegate()</code> is called before any value initialization takes place.</li>
</ol>

<h3><a name="ValueAwareEditor"></a>ValueAwareEditor</h3>

<p>
<code>ValueAwareEditor</code> may be used if an Editor's behavior depends on the value that it is editing, or if the Editor requires explicit flush notification.
</p>
<ol>
  <li><code>setEditorDelegate()</code> is called, per <code>HasEditorDelegate</code> super-interface.</li>
  <li><code>setValue()</code> is called with the value that the Editor is responsible for editing.  If the value will affect with sub-editors are or are not provided to the framework, they should be initialized or nullified at this time.</li>
  <li>If <code>EditorDelegate.subscribe()</code> has been called, the Editor may receive subsequent calls to <code>onPropertyChange()</code> or <code>setValue()</code> at any point in time.</li>
  <li><code>flush()</code> is called in a depth-first manner by the driver, so Editors generally do not flush their sub-Editors.  Editors that directly mutate their peer object should do so only when <code>flush()</code> is called in order to allow an edit workflow to be canceled.</li>
</ol>

<h3><a name="CompositeEditor"></a>CompositeEditor</h3>

<p>
<code>CompositeEditor</code> allows an unknown number of homogenous sub-Editors to be added to the Editor hierarchy at runtime.  In addition to the behavior described for <code>ValueAwareEditor</code>, <code>CompositeEditor</code> has the following additional APIs:
</p>
<ol>
  <li><code>createEditorForTraversal()</code> should return a canonical sub-editor instance that will be used by the driver for computing all edited paths.  If the composite editor is editing a Collection, this method solves the problem of having no sub-Editors available to examine for an empty Collection.</li>
  <li><code>setEditorChain()</code> provides the <code>CompositeEditor</code> with access to the <code>EditorChain</code>, which allows the component sub-Editors to be attached and detached from the Editor hierarchy.</li>
  <li><code>getPathElement()</code> is called by the Editor framework for each attached component sub-Editor in order to compute the return value for <code>EditorDelegate.getPath()</code>.  A <code>CompositeEditor</code> that is editing an indexable datastructure, such as a <code>List</code>, might return <code>[index]</code> for this method.</li>
</ol>

<h3><a name="HasEditorErrors"></a>HasEditorErrors</h3>

<p>
<code>HasEditorErrors</code> indicates that the Editor wishes to receive any unconsumed errors reported by sub-Editors through <code>EditorDelegate.recordError()</code>.  The Editor may mark an <code>EditorError</code> as consumed by calling <code>EditorError.setConsumed()</code>.
</p>

<h2 id="Provided_Adapters"></a>Provided Adapters</h2>

<p>
The GWT distribution provides the following Editor adapter classes that provide reusable logic.  To reduce the amount of generics boilerplate, most types are equipped with a static <code>of()</code> method to instantiate the adapter type.
</p>

<ul>
  <li><code>HasDataEditor</code> adapts a <code>List&lt;T&gt;</code> to a <code>HasData&lt;T&gt;</code>.</li>
  <li><code>HasTextEditor</code> adapts the <code>HasText</code> interface to <code>LeafValueEditor&lt;String&gt;</code>.</li>
  <ul>
    <li>New widgets should prefer <code>TakesValue&lt;String&gt;</code> over <code>HasText</code>.</li>
  </ul>

  <li><code>ListEditor</code> keeps a <code>List&lt;T&gt;</code> in sync with a list of sub-Editors.</li>
  <ul>
    <li>The <code>ListEditor</code> is created with a user-provided <code>EditorSource</code> which vends sub-Editors (usually Widget subtypes).</li>
    <li>Changes made to the structure of the <code>List</code> returned by <code>ListEditor.getList()</code> will be reflected in calls made to the <code>EditorSource</code>.</li>
    <li><a href="http://code.google.com/p/google-web-toolkit/source/browse/trunk/samples/dynatablerf/#dynatablerf/src/main/java/com/google/gwt/sample/dynatablerf/client/widgets/FavoritesWidget.java" rel="nofollow">Sample code</a>.</li>
  </ul>

  <li><code>OptionalFieldEditor</code> can be used with nullable or resettable bean properties.</li>
  <li><code>SimpleEditor</code> can be used as a headless property Editor</li>
  <li><code>TakesValueEditor</code> adapts a <code>TakesValue&lt;T&gt;</code> to a <code>LeafValueEditor&lt;T&gt;</code></li>
  <li><code>ValueBoxEditor</code> adapts a <code>ValueBoxBase&lt;T&gt;</code> to a <code>LeafValueEditor&lt;T&gt;</code>.  If the <code>getValueOrThrow()</code> method throws a <code>ParseException</code>, the exception will reported via an <code>EditorError</code>.</li>
  <li><code>ValueBoxEditorDecorator</code> is a simple UI decorator that combines a <code>ValueBoxBase</code> with a <code>Label</code> to show any parse errors from the contained <code>ValueBoxBase</code>.</li>
</ul>

<h2 id="Driver_types"></a>Driver types</h2>

<p>
The GWT Editor framework provides the following top-level drivers: 
</p>
<ul>
  <li><code>SimpleBeanEditorDriver</code> can be used with any bean-like object.</li>
  <ul>
    <li>It does not provide support for update subscriptions.</li>
  </ul>

  <li><code>RequestFactoryEditorDriver</code> is designed to integrate with <code>RequestFactory</code> and edit <code>EntityProxy</code> subtypes.</li>
  <ul>
    <li>This driver type requires a <code>RequestContext</code> in order to automatically call <code>RequestContext.edit()</code> on any <code>EntityProxy</code> instances that are encountered.</li>
    <li>Subscriptions are supported by listening for <code>EntityProxyChange</code> events on the <code>RequestFactory</code>'s <code>EventBus</code>.</li>
  </ul>
</ul>

<h2 id="FAQ"></a>FAQ</h2>

<h3><a name="Editor_vs._IsEditor"></a>Editor vs. IsEditor</h3>

<p>
<strong>Q:</strong> Should my <code>Widget</code> implement an <code>Editor</code> interface or <code>IsEditor</code>?
</p>

<p>
<strong>A:</strong> If the <code>Widget</code> contains multiple sub-Editors is a simple, static hierarchy, use the <code>Editor</code> interface.
</p>

<p>
The <code>IsEditor</code> interface is intended to be used when a view type is reusing an Editor behavior provided by an external type.  For instance, a <code>LabelDecorator</code> type would implement <code>IsEditor</code> because it re-uses its Label's existing Editor behavior:
</p>

<pre class="prettyprint">class LabelDecorator extends Composite implements IsEditor&lt;LeafValueEditor&lt;String&gt;&gt; {
  private final Label wrapped = new Label();
 
  public LabelDecorator() {
    // Construct a pretty UI around the wrapped label
    initWidget(prettyContents);
  }
 
  public LeafValueEditor&lt;String&gt; asEditor() {
    return wrapped.asEditor();
  }
}</pre>

<p>
Similarly a <code>WorkgroupMembershipEditor</code> might implement <code>IsEditor&lt;ListEditor&lt;Person, PersonNameLabel&gt;&gt;</code>.
</p>

<h3><a name="Read-only_Editors"></a>Read-only Editors</h3>

<p>
<strong>Q:</strong> Can I use Editors to view read-only data?
</p>

<p>
<strong>A:</strong> Yes, just don't call the <code>flush()</code> method on the driver type.   <code>RequestFactoryEditorDriver</code> has a convenience <code>display()</code> method as well.
</p>

<h3><a name="Very_large_objects"></a>Very large objects</h3>

<p>
<strong>Q:</strong> How can I edit objects with a large number of properties?
</p>

<p>
<strong>A:</strong> An Editor doesn't have to edit all of the properties of its peer domain object. If you had a <code>BagOfState</code> type with many properties, it might make sense to write several Editor types that edit conceptually-related subsets of the properties:
</p>

<pre class="prettyprint">
class BagOfStateBiographicalEditor implements Editor&lt;BagOfState&gt; {
  AddressEditor address;
  Label name; 
}
 
class BagOfStateUserPreferencesEditor implements Editor&lt;BagOfState&gt; {
  CheckBox likesCats;
  CheckBox likesDogs;
}</pre>

<p>
Whether or not these editors are displayed all at the same time or sequentially is a user experience issue.  The Editor framework allows multiple Editors to edit the same object:
</p>

<pre class="prettyprint">class HasBagOfStateEditor implements Editor&lt;HasBagOfState&gt; {
 @Editor.Path(&quot;state&quot;)
 BagOfStateBiographicalEditor bio;
 
 @Editor.Path(&quot;state&quot;)
 BagOfStateUserPreferencesEditor prefs;
}</pre>