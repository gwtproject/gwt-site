UiPanels
===

<ol class="toc" id="pageToc">
  <li><a href="#BasicPanels">Basic Panels</a></li>
  <li><a href="#LayoutPanels">Layout Panels</a></li>
  <li><a href="#Animation">Animation</a></li>
  <li><a href="#Resize">RequiresResize and ProvidesResize</a></li>
  <li><a href="#Standards">Moving to Standards Mode</a></li>
  <li><a href="#Design">Design of the GWT 2.0 layout system</a></li>
  <li><a href="#Recipes">Recipes</a></li>
</ol>

<p>Panels in GWT are much like their layout counterparts in other user interface
libraries. The main difference is that GWT panels use HTML elements to lay out
their child widgets.</p>

<p>Panels contain widgets and other panels. They are used to define the layout of
the user interface in the browser.</p>

<h2 id="BasicPanels">Basic Panels</h2>

<h3><a href="/javadoc/latest/com/google/gwt/user/client/ui/RootPanel.html">RootPanel</a></h3>

<p>A <a href="/javadoc/latest/com/google/gwt/user/client/ui/RootPanel.html">RootPanel</a> is the top-most panel to which all other widgets are ultimately
attached. <a href="/javadoc/latest/com/google/gwt/user/client/ui/RootPanel.html#get()">RootPanel.get()</a> gets a singleton panel that wraps the HTML document's
<code>&lt;body&gt;</code> element. Use <a href="/javadoc/latest/com/google/gwt/user/client/ui/RootPanel.html#get(java.lang.String)">RootPanel.get(String id)</a> to get a panel for any other
element on the page.</p>

<h3><a href="/javadoc/latest/com/google/gwt/user/client/ui/FlowPanel.html">FlowPanel</a></h3>

<p>A <a href="/javadoc/latest/com/google/gwt/user/client/ui/FlowPanel.html">FlowPanel</a> is the simplest panel. It creates a single <code>&lt;div&gt;</code> element and
attaches children directly to it without modification. Use it in cases where
you want the natural HTML flow to determine the layout of child widgets.</p>

<h3><a href="/javadoc/latest/com/google/gwt/user/client/ui/HTMLPanel.html">HTMLPanel</a></h3>

<p>This panel provides a simple way to define an HTML structure, within which
widgets will be embedded at defined points. While you may use it directly, it
is most commonly used in <a href="DevGuideUiBinder.html">UiBinder templates</a>.</p>

<h3><a href="/javadoc/latest/com/google/gwt/user/client/ui/FormPanel.html">FormPanel</a></h3>

<p>When you need to reproduce the behavior of an HTML form (e.g., for interacting
with servers that expect form POST requests, or simply to get the default form
keyboard behavior in the browser), you can use a <a href="/javadoc/latest/com/google/gwt/user/client/ui/FormPanel.html">FormPanel</a>. Any widgets wrapped
by this panel will be wrapped in a <code>&lt;form&gt;</code> element.</p>

<h3><a href="/javadoc/latest/com/google/gwt/user/client/ui/ScrollPanel.html">ScrollPanel</a></h3>

<p>When you wish to create a scrollable area within another panel, you should use
a <a href="/javadoc/latest/com/google/gwt/user/client/ui/ScrollPanel.html">ScrollPanel</a>. This panel works well in layout panels (see below), which
provide it with the explicit size it needs to scroll properly.</p>

<h3><a href="/javadoc/latest/com/google/gwt/user/client/ui/PopupPanel.html">PopupPanel</a> and <a href="/javadoc/latest/com/google/gwt/user/client/ui/DialogBox.html">DialogBox</a></h3>

<p>Use these two panels to create simple popups and modal dialogs. They overlap
existing content in browser window, and can be stacked over one-another.</p>

<h3><a href="/javadoc/latest/com/google/gwt/user/client/ui/Grid.html">Grid</a> and <a href="/javadoc/latest/com/google/gwt/user/client/ui/FlexTable.html">FlexTable</a></h3>

<p>These two widgets are used to create traditional HTML <code>&lt;table&gt;</code> elements, and can
also be used as panels, in that arbitrary widgets may be added to their cells.</p>

<h2 id="LayoutPanels">Layout Panels</h2>

<p>GWT 2.0 introduces a number of new panels, which together form a stable basis
for fast and predictable application-level layout. For background and details,
see <a href="#Design">"Design of the GWT 2.0 layout system"</a> below.</p>

<p>The bulk of the layout system is embodied in a series of panel widgets. Each
of these widgets uses the underlying layout system to position its children
in a dependable manner.</p>

<h3><a href="/javadoc/latest/com/google/gwt/user/client/ui/RootLayoutPanel.html">RootLayoutPanel</a></h3>

<p>This panel is a singleton that serves as a root container to which all other
layout panels should be attached (see RequiresResize and ProvidesResize <a
href="#Resize">below</a> for details). It extends
<a href="/javadoc/latest/com/google/gwt/user/client/ui/LayoutPanel.html">LayoutPanel</a>, and thus you can position any number of children with
arbitrary constraints.</p>

<p>You most commonly use <a href="/javadoc/latest/com/google/gwt/user/client/ui/RootLayoutPanel.html">RootLayoutPanel</a> as a container for another panel, as in
the following snippet, which causes a <a href="/javadoc/latest/com/google/gwt/user/client/ui/DockLayoutPanel.html">DockLayoutPanel</a> to fill the browser's
client area:</p>

<pre class='prettyprint'>DockLayoutPanel appPanel = new DockLayoutPanel(Unit.EM);
RootLayoutPanel.get().add(appPanel);
</pre>

<h3><a href="/javadoc/latest/com/google/gwt/user/client/ui/LayoutPanel.html">LayoutPanel</a></h3>

<p>Think of <a href="/javadoc/latest/com/google/gwt/user/client/ui/LayoutPanel.html">LayoutPanel</a> as the most general layout mechanism, and often one upon
which other layouts are built. Its closest analog is <a href="/javadoc/latest/com/google/gwt/user/client/ui/AbsolutePanel.html">AbsolutePanel</a>, but it is
significantly more general in that it allows its children to be positioned
using arbitrary constraints, as in the following example:</p>

<pre class='prettyprint'>Widget child0, child1, child2;
LayoutPanel p = new LayoutPanel();
p.add(child0); p.add(child1); p.add(child2);

p.setWidgetLeftWidth(child0, 0, PCT, 50, PCT);  // Left panel
p.setWidgetRightWidth(child1, 0, PCT, 50, PCT); // Right panel

p.setWidgetLeftRight(child2, 5, EM, 5, EM);     // Center panel
p.setWidgetTopBottom(child2, 5, EM, 5, EM);
</pre>

<p><img src="images/LayoutPanel.png" alt="LayoutPanel example" title="" /></p>

<h3><a href="/javadoc/latest/com/google/gwt/user/client/ui/DockLayoutPanel.html">DockLayoutPanel</a></h3>

<p><a href="/javadoc/latest/com/google/gwt/user/client/ui/DockLayoutPanel.html">DockLayoutPanel</a> serves the same purpose as the existing <a href="/javadoc/latest/com/google/gwt/user/client/ui/DockPanel.html">DockPanel</a> widget,
except that it uses the layout system to achieve this structure without using
tables, and in a predictable manner. You would often use to build
application-level structure, as in the following example:</p>

<pre class='prettyprint'>DockLayoutPanel p = new DockLayoutPanel(Unit.EM);
p.addNorth(new HTML("header"), 2);
p.addSouth(new HTML("footer"), 2);
p.addWest(new HTML("navigation"), 10);
p.add(new HTML(content));
</pre>

<p><img src="images/DockLayoutPanel.png" alt="DockLayoutPanel example" title="" /></p>

<p>Note that <a href="/javadoc/latest/com/google/gwt/user/client/ui/DockLayoutPanel.html">DockLayoutPanel</a> requires the use of consistent units for all
children, specified in the constructor. It also requires that the size of each
child widget (except the last, which consumes all remaining space) be specified
explicitly along its primary axis.</p>

<h3>SplitLayoutPanel</h3>

<p>The <a href="/javadoc/latest/com/google/gwt/user/client/ui/SplitLayoutPanel.html">SplitLayoutPanel</a> is identical to the <a href="/javadoc/latest/com/google/gwt/user/client/ui/DockLayoutPanel.html">DockLayoutPanel</a> (and indeed
extends it), except that it automatically creates a user-draggable splitter
between each pair of child widgets. It also supports only the use of pixel
units. Use this instead of <a href="/javadoc/latest/com/google/gwt/user/client/ui/HorizontalSplitPanel.html">HorizontalSplitPanel</a> and <a href="/javadoc/latest/com/google/gwt/user/client/ui/VerticalSplitPanel.html">VerticalSplitPanel</a>.</p>

<pre class='prettyprint'>SplitLayoutPanel p = new SplitLayoutPanel();
p.addWest(new HTML("navigation"), 128);
p.addNorth(new HTML("list"), 384);
p.add(new HTML("details"));
</pre>

<p><img src="images/SplitLayoutPanel.png" alt="SplitLayoutPanel example" title="" /></p>

<h3>StackLayoutPanel</h3>

<p><a href="/javadoc/latest/com/google/gwt/user/client/ui/StackLayoutPanel.html">StackLayoutPanel</a> replaces the existing <a href="/javadoc/latest/com/google/gwt/user/client/ui/StackPanel.html">StackPanel</a> (which does not work very
well in standards mode). It displays one child widget at a time, each of which is
associated with a single "header" widget. Clicking on a header widget shows its
associated child widget.</p>

<pre class='prettyprint'>StackLayoutPanel p = new StackLayoutPanel(Unit.EM);
p.add(new HTML("this content"), new HTML("this"), 4);
p.add(new HTML("that content"), new HTML("that"), 4);
p.add(new HTML("the other content"), new HTML("the other"), 4);
</pre>

<p><img src="images/StackLayoutPanel.png" alt="StackLayoutPanel example" title="" /></p>

<p>Note that, as with <a href="/javadoc/latest/com/google/gwt/user/client/ui/DockLayoutPanel.html">DockLayoutPanel</a>, only a single unit type may be used on a
given panel. The length value provided to the <a href="/javadoc/latest/com/google/gwt/user/client/ui/DockLayoutPanel.html#add(com.google.gwt.user.client.ui.Widget)">add()</a>
method specifies the size of the header widget, which must be of a fixed size.</p>

<h3>TabLayoutPanel</h3>

<p>As with the existing <a href="/javadoc/latest/com/google/gwt/user/client/ui/TabPanel.html">TabPanel</a>, <a href="/javadoc/latest/com/google/gwt/user/client/ui/TabLayoutPanel.html">TabLayoutPanel</a> displays a row of clickable
tabs. Each tab is associated with another child widget, which is shown when a
user clicks on the tab.</p>

<pre class='prettyprint'>TabLayoutPanel p = new TabLayoutPanel(1.5, Unit.EM);
p.add(new HTML("this content"), "this");
p.add(new HTML("that content"), "that");
p.add(new HTML("the other content"), "the other");
</pre>

<p><img src="images/TabLayoutPanel.png" alt="TabLayoutPanel example" title="" /></p>

<p>The length value provided to the <a href="/javadoc/latest/com/google/gwt/user/client/ui/TabLayoutPanel.html">TabLayoutPanel</a> constructor specifies the
height of the tab bar, which you must explicitly provide.</p>

<h3>When should I <em>not</em> use layout panels?</h3>

<p>The panels described above are best used for defining your application's outer
structure &mdash; that is, the parts that are the least "document-like". You
should continue to use basic widgets and HTML structure for those parts for
which the HTML/CSS layout algorithm works well. In particular, consider using
<a href="DevGuideUiBinder.html">UiBinder templates</a> to directly use HTML wherever that makes sense.</p>

<h2 id="Animation">Animation</h2>

<p>The GWT 2.0 layout system has direct, built-in support for animation. This is
necessary to support a number of use-cases, because the layout system must
properly handle animation among sets of layout constraints.</p>

<p>Panels that implement <a href="/javadoc/latest/com/google/gwt/user/client/ui/AnimatedLayout.html">AnimatedLayout</a>, such as <a href="/javadoc/latest/com/google/gwt/user/client/ui/LayoutPanel.html">LayoutPanel</a>,
<a href="/javadoc/latest/com/google/gwt/user/client/ui/DockLayoutPanel.html">DockLayoutPanel</a>, and <a href="/javadoc/latest/com/google/gwt/user/client/ui/SplitLayoutPanel.html">SplitLayoutPanel</a>, can animate their child widgets from
one set of constraints to another. Typically this is done by setting up the
state towards which you wish to animate, then calling
<a href="/javadoc/latest/com/google/gwt/user/client/ui/AnimatedLayout.html#animate(int)">animate()</a>. See <a href="#recipes">"Recipes"</a>
below for specific examples.</p>

<h2 id="Resize">RequiresResize and ProvidesResize</h2>

<p>Two new characteristic interfaces were introduced in GWT 2.0: <a href="/javadoc/latest/com/google/gwt/user/client/ui/RequiresResize.html">RequiresResize</a>
and <a href="/javadoc/latest/com/google/gwt/user/client/ui/ProvidesResize.html">ProvidesResize</a>. These are used to propagate notification of resize events
throughout the widget hierarchy.</p>

<p><a href="/javadoc/latest/com/google/gwt/user/client/ui/RequiresResize.html">RequiresResize</a> provides a single method,
<a href="/javadoc/latest/com/google/gwt/user/client/ui/RequiresResize.html#onResize()">onResize()</a>, which is called by the widget's parent
whenever the child's size has changed. <a href="/javadoc/latest/com/google/gwt/user/client/ui/ProvidesResize.html">ProvidesResize</a> is simply a tag
interface indicating that a parent widget will honor this contract. The purpose
of these two interfaces is to form an unbroken hierarchy between all widgets
that implement RequiresResize and the <a href="/javadoc/latest/com/google/gwt/user/client/ui/RootLayoutPanel.html">RootLayoutPanel</a>, which listens for any
changes (such as the browser window resizing) that could affect the size of
widgets in the hierarchy.</p>

<h3>When to use <a href="/javadoc/latest/com/google/gwt/user/client/ui/RequiresResize.html#onResize()">onResize()</a></h3>

<p>Most widgets should <em>not</em> need to know when they've been resized, as the
browser's layout engine should be doing most of the work. However, there are
times when a widget <em>does</em> need to know. This comes up, for example, when a
widget contains a dynamic list of items depending upon how much room is
available to display them. Because it's almost always faster to let the layout
engine do its work than to run code, you should not lean upon
<a href="/javadoc/latest/com/google/gwt/user/client/ui/RequiresResize.html#onResize()">onResize()</a> unless you have no alternative.</p>

<h3>When and how to implement ProvidesResize</h3>

<p>A panel that implements ProvidesResize is expected to propagate resize events
to any of its child widgets that implement RequiresResize. For a canonical
example of this, see the implementation of <a href="/javadoc/latest/com/google/gwt/user/client/ui/LayoutPanel.html#onResize()">LayoutPanel.onResize()</a>. Most
custom widgets will want to composite an existing layout panel using
<a href="/javadoc/latest/com/google/gwt/user/client/ui/ResizeComposite.html">ResizeComposite</a>, however, as described next.</p>

<h3>ResizeComposite</h3>

<p>When creating a custom <a href="/javadoc/latest/com/google/gwt/user/client/ui/Composite.html">Composite</a> widget that wrap a widget that implements
<a href="/javadoc/latest/com/google/gwt/user/client/ui/RequiresResize.html">RequiresResize</a>, you should use <a href="/javadoc/latest/com/google/gwt/user/client/ui/ResizeComposite.html">ResizeComposite</a> as its base class. This
subclass of <a href="/javadoc/latest/com/google/gwt/user/client/ui/Composite.html">Composite</a> automatically propagates resize events to its wrapped
widget.</p>

<h2 id="Standards">Moving to Standards Mode</h2>

<p>The GWT 2.0 layout system is intended to work only in "standards mode". This
means that you should always place the following declaration at the top of your
HTML pages:
    <code>&lt;!DOCTYPE html&gt;</code></p>

<h3>What won't work in standards mode?</h3>

<p>As mentioned above, some of the existing GWT panels do not behave entirely as
expected in standards mode. This stems primarily from differences between the
way standards and quirks modes render tables.</p>

<h4>CellPanel (HorizontalPanel, VerticalPanel, DockPanel)</h4>

<p>These panels all use table cells as their basic structural units. While they
still work in standards mode, they will lay out their children somewhat
differently. The main difference is that their children will not respect width
and height properties (it is common to set children of CellPanels explicitly to
100% width and height). There are also differences in the way that the browser
allocates space to individual table rows and columns that can lead to
unexpected behavior in standards mode.</p>

<p>You should use <a href="/javadoc/latest/com/google/gwt/user/client/ui/DockLayoutPanel.html">DockLayoutPanel</a> in place of <a href="/javadoc/latest/com/google/gwt/user/client/ui/DockPanel.html">DockPanel</a>. <a href="/javadoc/latest/com/google/gwt/user/client/ui/VerticalPanel.html">VerticalPanel</a> can
usually be replaced by a simple <a href="/javadoc/latest/com/google/gwt/user/client/ui/FlowPanel.html">FlowPanel</a> (since block-level elements will
naturally stack up vertically).</p>

<p><a href="/javadoc/latest/com/google/gwt/user/client/ui/HorizontalPanel.html">HorizontalPanel</a> is a bit trickier. In some cases, you can simply replace it
with a <a href="/javadoc/latest/com/google/gwt/user/client/ui/DockLayoutPanel.html">DockLayoutPanel</a>, but that requires that you specify its childrens'
widths explicitly. The most common alternative is to use <a href="/javadoc/latest/com/google/gwt/user/client/ui/FlowPanel.html">FlowPanel</a>, and to
use the <code>float: left;</code> CSS property on its children. And of course, you can
continue to use <a href="/javadoc/latest/com/google/gwt/user/client/ui/HorizontalPanel.html">HorizontalPanel</a> itself, as long as you take the caveats above
into account.</p>

<h4>StackPanel</h4>

<p>StackPanels do not work very well in standards mode. Because of the differences
in table rendering mentioned above, StackPanel will almost certainly not do
what you expect in standards mode, and you should replace them with
<a href="/javadoc/latest/com/google/gwt/user/client/ui/StackLayoutPanel.html">StackLayoutPanel</a>.</p>

<h4>SplitPanel (HorizontalSplitPanel and VerticalSplitPanel)</h4>

<p>SplitPanels are very unpredictable in standards mode, and you should almost
invariably replace them with <a href="/javadoc/latest/com/google/gwt/user/client/ui/SplitLayoutPanel.html">SplitLayoutPanel</a>.</p>

<h2 id="Design">Design of the GWT 2.0 layout system</h2>

<p>Prior to 2.0, GWT's mechanisms for handling application-level layout have a
number of significant problems:</p>

<ul>
<li>They're unpredictable.</li>
<li>They often require extra code to fix up their deficiencies:
<ul><li>For example, causing an application to fill the browser's client area with
interior scrolling is nearly impossible without extra code.</li></ul></li>
<li>They don't all work well in standards mode.</li>
</ul>

<p>Their underlying motivation was sound &mdash; the intention was to let the
browser's native layout engine do almost all of the work. But the above
deficiencies can be crippling.</p>

<h3>Goals</h3>

<ul>
<li>Perfectly predictable layout behavior. Precision layout should be possible.
<ul><li>It should also work in the presence of CSS decorations (border, margin, and
padding) with arbitrary units.</li></ul></li>
<li>Work correctly in standards-mode.</li>
<li>Get the browser to do almost all of the work in its layout engine.
<ul><li>Manual adjustments should occur only when strictly necessary.</li></ul></li>
<li>Smooth, automatic animation.</li>
</ul>

<h3>Non-Goals</h3>

<ul>
<li>Work in quirks-mode.</li>
<li>Swing-style layout based on "preferred size". This is effectively
intractable in the browser.</li>
<li>Take over all layout. This design is intended to handle coarse-grained
"desktop-like" layout. The individual bits and pieces, such as form elements,
buttons, tables, and text should still be laid out naturally.</li>
</ul>

<h3>The Layout class</h3>

<p>The <a href="/javadoc/latest/com/google/gwt/layout/client/Layout.html">Layout</a> class contains all the underlying logic used by the layout system,
along with all the implementation details used to normalize layout behavior on
various browsers.</p>

<p>It is actually widget-agnostic, operating directly on DOM elements. Most
applications will have no reason to work directly with this class, but it should
prove useful to alternate widget library writers.</p>

<h3>Constraint-based Layout</h3>

<p>The GWT 2.0 layout system is built upon the simple constraint system that
exists natively in CSS. This uses the properties <code>left</code>, <code>top</code>, <code>width</code>,
<code>height</code>, <code>right</code>, and <code>bottom</code>. While most developers are familiar with these
properties, it is less well-known that they can be combined in various ways to
form a simple constraint system. Take the following CSS example:</p>

<pre class='prettyprint'>.parent {
  position: relative; /* to establish positioning context */
}

.child {
  position: absolute; left:1em; top:1em; right:1em; bottom:1em;
}
</pre>

<p>In this example, the child will automatically consume the parent's entire
space, minus 1em of space around the edge. Any two of these properties (on each
axis) forms a valid constraint pair (three would be degenerate), producing lots
of interesting possibilities. This is especially true when you consider various
mixtures of relative units, such as "em" and "%".</p>

<h2 id="Recipes">Recipes</h2>

<p>The following are a series of simple "recipes" for creating various structures
and dealing with different scenarios. Where possible, we'll describe the layout
in terms of <a href="DevGuideUiBinder.html">UiBinder</a> templates.</p>

<h3>Basic application layout</h3>

<p>The following sample shows a simple application-style layout with a header, a
navigation area on the left edge, and a scrollable content area.</p>

<pre class='prettyprint'>&lt;g:DockLayoutPanel unit='EM'&gt;
  &lt;g:north size='4'&gt;
    &lt;g:Label&gt;Header&lt;/g:Label&gt;
  &lt;/g:north&gt;

  &lt;g:west size='16'&gt;
    &lt;g:Label&gt;Navigation&lt;/g:Label&gt;
  &lt;/g:west&gt;

  &lt;g:center&gt;
    &lt;g:ScrollPanel&gt;
      &lt;g:Label&gt;Content Area&lt;/g:Label&gt;
    &lt;/g:ScrollPanel&gt;
  &lt;/g:center&gt;
&lt;/g:DockLayoutPanel&gt;
</pre>

<p>You must place this structure in a container that implements <a href="/javadoc/latest/com/google/gwt/user/client/ui/ProvidesResize.html">ProvidesResize</a>,
which is most commonly <a href="/javadoc/latest/com/google/gwt/user/client/ui/RootLayoutPanel.html">RootLayoutPanel</a>. The following code demonstrates how
to do this:</p>

<pre class='prettyprint'>interface Binder extends UiBinder&lt;Widget, BasicApp&gt; { }
private static final Binder binder = GWT.create(Binder.class);

public void onModuleLoad() {
  RootLayoutPanel.get().add(binder.createAndBindUi());
}
</pre>

<h3>Splitters</h3>

<p><a href="/javadoc/latest/com/google/gwt/user/client/ui/SplitLayoutPanel.html">SplitLayoutPanel</a> works much like <a href="/javadoc/latest/com/google/gwt/user/client/ui/DockLayoutPanel.html">DockLayoutPanel</a>, except that it only
supports pixel units. The basic application structure above can be given a
splitter between the navigation and content areas like so:</p>

<pre class='prettyprint'>&lt;g:DockLayoutPanel unit='EM'&gt;
  &lt;g:north size='4'&gt;
    &lt;g:Label&gt;Header&lt;/g:Label&gt;
  &lt;/g:north&gt;

  &lt;g:center&gt;
    &lt;g:SplitLayoutPanel&gt;
      &lt;g:west size='128'&gt;
        &lt;g:Label&gt;Navigation&lt;/g:Label&gt;
      &lt;/g:west&gt;

      &lt;g:center&gt;
        &lt;g:ScrollPanel&gt;
          &lt;g:Label&gt;Content Area&lt;/g:Label&gt;
        &lt;/g:ScrollPanel&gt;
      &lt;/g:center&gt;
    &lt;/g:SplitLayoutPanel&gt;
  &lt;/g:center&gt;
&lt;/g:DockLayoutPanel&gt;
</pre>

<p>Note how we mix the dock and split panels, so that the header's size can be
specified in <code>EM</code> units.</p>

<h3>Layout animation</h3>

<p>To use animation with a <a href="/javadoc/latest/com/google/gwt/user/client/ui/LayoutPanel.html">LayoutPanel</a>, you must first create an initial set of
constraints, then animate to a target set of constraints. In the following
example, we start with a child widget positioned at the top, but with no height
so that it is effectively hidden. Calling <a href="/javadoc/latest/com/google/gwt/user/client/ui/LayoutPanel.html#forceLayout()">LayoutPanel.forceLayout()</a> "fixes"
the initial constraints.</p>

<pre class='prettyprint'>panel.setWidgetTopHeight(child, 0, PX, 0, PX);
panel.forceLayout();
</pre>

<p>Now we give the widget a height of 2em and explicitly call
LayoutPanel.animate(int) to cause it to resize over 500 ms.</p>

<pre class='prettyprint'>panel.setWidgetTopHeight(child, 0, PX, 2, EM);
panel.animate(500);
</pre>

<p>This will work with any constraints and any number of children.</p>

<h3>Implementing a Composite that RequiresResize</h3>

<p>Widgets that implement <a href="/javadoc/latest/com/google/gwt/user/client/ui/RequiresResize.html">RequiresResize</a> expect <a href="/javadoc/latest/com/google/gwt/user/client/ui/RequiresResize.html#onResize()">RequiresResize.onResize()</a> to
be called whenever the widget's size changes. If you are wrapping such a widget
in a <a href="/javadoc/latest/com/google/gwt/user/client/ui/Composite.html">Composite</a>, you'll need to use <a href="/javadoc/latest/com/google/gwt/user/client/ui/ResizeComposite.html">ResizeComposite</a> instead to ensure that
this call is propagated correctly, like so:</p>

<pre class='prettyprint'>class MyWidget extends ResizeComposite {
  private LayoutPanel p = new LayoutPanel();

  public MyWidget() {
    initWidget(p);
  }
}
</pre>

<h3>Child widget visibility</h3>

<p>The <a href="/javadoc/latest/com/google/gwt/layout/client/Layout.html">Layout</a> class has to wrap each of its child elements in a "container"
element in order to work properly. One implication of this is that, when you
call <a href="/javadoc/latest/com/google/gwt/user/client/ui/UIObject.html#setVisible(boolean)">UIObject.setVisible(boolean)</a> on a widget within a <a href="/javadoc/latest/com/google/gwt/user/client/ui/LayoutPanel.html">LayoutPanel</a>, it
won't behave quite as expected: the widget will indeed be made invisible, but
it will tend to consume mouse events (actually, it's the container element that
is doing so).</p>

<p>To work around this, you can get the container element directly using
<a href="/javadoc/latest/com/google/gwt/user/client/ui/LayoutPanel.html#getWidgetContainerElement(com.google.gwt.user.client.ui.Widget)">LayoutPanel.getWidgetContainerElement(Widget)</a>, and set its visibility
directly:</p>

<pre class='prettyprint'>LayoutPanel panel = ...;
Widget child;
panel.add(child);
UIObject.setVisible(panel.getWidgetContainerElement(child), false);
</pre>

<h3>Using a LayoutPanel without RootLayoutPanel</h3>

<p>In most cases you should use layout panels by attaching them to a
<a href="/javadoc/latest/com/google/gwt/user/client/ui/RootLayoutPanel.html">RootLayoutPanel</a>, either directly or via other panels that implement
<a href="/javadoc/latest/com/google/gwt/user/client/ui/ProvidesResize.html">ProvidesResize</a>.</p>

<p>There are, however, instances where you need to use a layout panel within a
normal widget (e.g., <a href="/javadoc/latest/com/google/gwt/user/client/ui/FlowPanel.html">FlowPanel</a> or <a href="/javadoc/latest/com/google/gwt/user/client/ui/RootPanel.html">RootPanel</a>). In these cases, you will need
to set the panel's size explicitly, as in the following example:</p>

<pre class='prettyprint'>LayoutPanel panel = new LayoutPanel();
RootPanel.get("someId").add(panel);
panel.setSize("20em", "10em");
</pre>

<p>Note that <a href="/javadoc/latest/com/google/gwt/user/client/ui/RootLayoutPanel.html">RootLayoutPanel</a> provides no
mechanism for wrapping an arbitrary element like <a href="/javadoc/latest/com/google/gwt/user/client/ui/RootPanel.html">RootPanel</a>
does. This is because it is impossible to know when an arbitrary element has been resized by the browser. If you want to resize a layout panel in an arbitrary element,
you must do so manually.</p>

<p>This also applies to layout panels used in <a href="/javadoc/latest/com/google/gwt/user/client/ui/PopupPanel.html">PopupPanel</a> and
<a href="/javadoc/latest/com/google/gwt/user/client/ui/DialogBox.html">DialogBox</a>. The following example shows the use of a <a href="/javadoc/latest/com/google/gwt/user/client/ui/SplitLayoutPanel.html">SplitLayoutPanel</a> in a
<a href="/javadoc/latest/com/google/gwt/user/client/ui/DialogBox.html">DialogBox</a>:</p>

<pre class='prettyprint'>SplitLayoutPanel split = new SplitLayoutPanel();
split.addWest(new HTML("west"), 128);
split.add(new HTML("center"));
split.setSize("20em", "10em");

DialogBox dialog = new DialogBox();
dialog.setText("caption");
dialog.add(split);
dialog.show();
</pre>

<h3>Tables and Frames</h3>

<p>Widgets that are implemented using &lt;table&gt; or &lt;frame&gt; elements do not
automatically fill the space provided by the layout. In order to fix this, you
will need to explicitly set these widgets <code>width</code> and <code>height</code> to <code>100%</code>. The
following example shows this with a <a href="/javadoc/latest/com/google/gwt/user/client/ui/RichTextArea.html">RichTextArea</a>, which is implemented using
an &lt;iframe&gt; element.</p>

<pre style='prettyprint'>
&lt;g:DockLayoutPanel unit='EM'&gt;
  &lt;g:north size='2'&gt;
    &lt;g:HTML&gt;Header&lt;/g:HTML&gt;
  &lt;/g:north&gt;

  &lt;g:south size='2'&gt;
    &lt;g:HTML&gt;Footer&lt;/g:HTML&gt;
  &lt;/g:south&gt;

  &lt;g:center&gt;
    &lt;g:RichTextArea width='100%' height='100%'/&gt;
  &lt;/g:center&gt;
&lt;/g:DockLayoutPanel&gt;
</pre>