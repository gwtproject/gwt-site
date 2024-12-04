UiPanels
===

1.  [Basic Panels](#BasicPanels)
2.  [Layout Panels](#LayoutPanels)
3.  [Animation](#Animation)
4.  [RequiresResize and ProvidesResize](#Resize)
5.  [Moving to Standards Mode](#Standards)
6.  [Design of the GWT 2.0 layout system](#Design)
7.  [Recipes](#Recipes)

Panels in GWT are much like their layout counterparts in other user interface
libraries. The main difference is that GWT panels use HTML elements to lay out
their child widgets.

Panels contain widgets and other panels. They are used to define the layout of
the user interface in the browser.

## Basic Panels<a id="BasicPanels"></a>

### [RootPanel](/javadoc/latest/com/google/gwt/user/client/ui/RootPanel.html)

A [RootPanel](/javadoc/latest/com/google/gwt/user/client/ui/RootPanel.html) is the top-most panel to which all other widgets are ultimately
attached. [RootPanel.get()](/javadoc/latest/com/google/gwt/user/client/ui/RootPanel.html#get--) gets a singleton panel that wraps the HTML document's
`<body>` element. Use [RootPanel.get(String id)](/javadoc/latest/com/google/gwt/user/client/ui/RootPanel.html#get-java.lang.String-) to get a panel for any other
element on the page.

### [FlowPanel](/javadoc/latest/com/google/gwt/user/client/ui/FlowPanel.html)

A [FlowPanel](/javadoc/latest/com/google/gwt/user/client/ui/FlowPanel.html) is the simplest panel. It creates a single `<div>` element and
attaches children directly to it without modification. Use it in cases where
you want the natural HTML flow to determine the layout of child widgets.

### [HTMLPanel](/javadoc/latest/com/google/gwt/user/client/ui/HTMLPanel.html)

This panel provides a simple way to define an HTML structure, within which
widgets will be embedded at defined points. While you may use it directly, it
is most commonly used in [UiBinder templates](DevGuideUiBinder.html).

### [FormPanel](/javadoc/latest/com/google/gwt/user/client/ui/FormPanel.html)

When you need to reproduce the behavior of an HTML form (e.g., for interacting
with servers that expect form POST requests, or simply to get the default form
keyboard behavior in the browser), you can use a [FormPanel](/javadoc/latest/com/google/gwt/user/client/ui/FormPanel.html). Any widgets wrapped
by this panel will be wrapped in a `<form>` element.

### [ScrollPanel](/javadoc/latest/com/google/gwt/user/client/ui/ScrollPanel.html)

When you wish to create a scrollable area within another panel, you should use
a [ScrollPanel](/javadoc/latest/com/google/gwt/user/client/ui/ScrollPanel.html). This panel works well in layout panels (see below), which
provide it with the explicit size it needs to scroll properly.

### [PopupPanel](/javadoc/latest/com/google/gwt/user/client/ui/PopupPanel.html) and [DialogBox](/javadoc/latest/com/google/gwt/user/client/ui/DialogBox.html)

Use these two panels to create simple popups and modal dialogs. They overlap
existing content in browser window, and can be stacked over one-another.

### [Grid](/javadoc/latest/com/google/gwt/user/client/ui/Grid.html) and [FlexTable](/javadoc/latest/com/google/gwt/user/client/ui/FlexTable.html)

These two widgets are used to create traditional HTML `<table>` elements, and can
also be used as panels, in that arbitrary widgets may be added to their cells.

## Layout Panels<a id="LayoutPanels"></a>

GWT 2.0 introduces a number of new panels, which together form a stable basis
for fast and predictable application-level layout. For background and details,
see ["Design of the GWT 2.0 layout system"](#Design) below.

The bulk of the layout system is embodied in a series of panel widgets. Each
of these widgets uses the underlying layout system to position its children
in a dependable manner.

### [RootLayoutPanel](/javadoc/latest/com/google/gwt/user/client/ui/RootLayoutPanel.html)

This panel is a singleton that serves as a root container to which all other
layout panels should be attached (see RequiresResize and ProvidesResize [below](#Resize) for details). It extends
[LayoutPanel](/javadoc/latest/com/google/gwt/user/client/ui/LayoutPanel.html), and thus you can position any number of children with
arbitrary constraints.

You most commonly use [RootLayoutPanel](/javadoc/latest/com/google/gwt/user/client/ui/RootLayoutPanel.html) as a container for another panel, as in
the following snippet, which causes a [DockLayoutPanel](/javadoc/latest/com/google/gwt/user/client/ui/DockLayoutPanel.html) to fill the browser's
client area:

```java
DockLayoutPanel appPanel = new DockLayoutPanel(Unit.EM);
RootLayoutPanel.get().add(appPanel);
```

### [LayoutPanel](/javadoc/latest/com/google/gwt/user/client/ui/LayoutPanel.html)

Think of [LayoutPanel](/javadoc/latest/com/google/gwt/user/client/ui/LayoutPanel.html) as the most general layout mechanism, and often one upon
which other layouts are built. Its closest analog is [AbsolutePanel](/javadoc/latest/com/google/gwt/user/client/ui/AbsolutePanel.html), but it is
significantly more general in that it allows its children to be positioned
using arbitrary constraints, as in the following example:

```java
Widget child0, child1, child2;
LayoutPanel p = new LayoutPanel();
p.add(child0); p.add(child1); p.add(child2);

p.setWidgetLeftWidth(child0, 0, PCT, 50, PCT);  // Left panel
p.setWidgetRightWidth(child1, 0, PCT, 50, PCT); // Right panel

p.setWidgetLeftRight(child2, 5, EM, 5, EM);     // Center panel
p.setWidgetTopBottom(child2, 5, EM, 5, EM);
```

![LayoutPanel example](images/LayoutPanel.png)

### [DockLayoutPanel](/javadoc/latest/com/google/gwt/user/client/ui/DockLayoutPanel.html)

[DockLayoutPanel](/javadoc/latest/com/google/gwt/user/client/ui/DockLayoutPanel.html) serves the same purpose as the existing [DockPanel](/javadoc/latest/com/google/gwt/user/client/ui/DockPanel.html) widget,
except that it uses the layout system to achieve this structure without using
tables, and in a predictable manner. You would often use to build
application-level structure, as in the following example:

```java
DockLayoutPanel p = new DockLayoutPanel(Unit.EM);
p.addNorth(new HTML("header"), 2);
p.addSouth(new HTML("footer"), 2);
p.addWest(new HTML("navigation"), 10);
p.add(new HTML(content));
```

![DockLayoutPanel example](images/DockLayoutPanel.png)

Note that [DockLayoutPanel](/javadoc/latest/com/google/gwt/user/client/ui/DockLayoutPanel.html) requires the use of consistent units for all
children, specified in the constructor. It also requires that the size of each
child widget (except the last, which consumes all remaining space) be specified
explicitly along its primary axis.

### SplitLayoutPanel

The [SplitLayoutPanel](/javadoc/latest/com/google/gwt/user/client/ui/SplitLayoutPanel.html) is identical to the [DockLayoutPanel](/javadoc/latest/com/google/gwt/user/client/ui/DockLayoutPanel.html) (and indeed
extends it), except that it automatically creates a user-draggable splitter
between each pair of child widgets. It also supports only the use of pixel
units. Use this instead of [HorizontalSplitPanel](/javadoc/latest/com/google/gwt/user/client/ui/HorizontalSplitPanel.html) and [VerticalSplitPanel](/javadoc/latest/com/google/gwt/user/client/ui/VerticalSplitPanel.html).

```java
SplitLayoutPanel p = new SplitLayoutPanel();
p.addWest(new HTML("navigation"), 128);
p.addNorth(new HTML("list"), 384);
p.add(new HTML("details"));
```

![SplitLayoutPanel example](images/SplitLayoutPanel.png)

### StackLayoutPanel

[StackLayoutPanel](/javadoc/latest/com/google/gwt/user/client/ui/StackLayoutPanel.html) replaces the existing [StackPanel](/javadoc/latest/com/google/gwt/user/client/ui/StackPanel.html) (which does not work very
well in standards mode). It displays one child widget at a time, each of which is
associated with a single "header" widget. Clicking on a header widget shows its
associated child widget.

```java
StackLayoutPanel p = new StackLayoutPanel(Unit.EM);
p.add(new HTML("this content"), new HTML("this"), 4);
p.add(new HTML("that content"), new HTML("that"), 4);
p.add(new HTML("the other content"), new HTML("the other"), 4);
```

![StackLayoutPanel example](images/StackLayoutPanel.png)

Note that, as with [DockLayoutPanel](/javadoc/latest/com/google/gwt/user/client/ui/DockLayoutPanel.html), only a single unit type may be used on a
given panel. The length value provided to the [add()](/javadoc/latest/com/google/gwt/user/client/ui/DockLayoutPanel.html#add-com.google.gwt.user.client.ui.Widget-)
method specifies the size of the header widget, which must be of a fixed size.

### TabLayoutPanel

As with the existing [TabPanel](/javadoc/latest/com/google/gwt/user/client/ui/TabPanel.html), [TabLayoutPanel](/javadoc/latest/com/google/gwt/user/client/ui/TabLayoutPanel.html) displays a row of clickable
tabs. Each tab is associated with another child widget, which is shown when a
user clicks on the tab.

```java
TabLayoutPanel p = new TabLayoutPanel(1.5, Unit.EM);
p.add(new HTML("this content"), "this");
p.add(new HTML("that content"), "that");
p.add(new HTML("the other content"), "the other");
```

![TabLayoutPanel example](images/TabLayoutPanel.png)

The length value provided to the [TabLayoutPanel](/javadoc/latest/com/google/gwt/user/client/ui/TabLayoutPanel.html) constructor specifies the
height of the tab bar, which you must explicitly provide.

### When should I _not_ use layout panels?

The panels described above are best used for defining your application's outer
structure &mdash; that is, the parts that are the least "document-like". You
should continue to use basic widgets and HTML structure for those parts for
which the HTML/CSS layout algorithm works well. In particular, consider using
[UiBinder templates](DevGuideUiBinder.html) to directly use HTML wherever that makes sense.

## Animation<a id="Animation"></a>

The GWT 2.0 layout system has direct, built-in support for animation. This is
necessary to support a number of use-cases, because the layout system must
properly handle animation among sets of layout constraints.

Panels that implement [AnimatedLayout](/javadoc/latest/com/google/gwt/user/client/ui/AnimatedLayout.html), such as [LayoutPanel](/javadoc/latest/com/google/gwt/user/client/ui/LayoutPanel.html),
[DockLayoutPanel](/javadoc/latest/com/google/gwt/user/client/ui/DockLayoutPanel.html), and [SplitLayoutPanel](/javadoc/latest/com/google/gwt/user/client/ui/SplitLayoutPanel.html), can animate their child widgets from
one set of constraints to another. Typically this is done by setting up the
state towards which you wish to animate, then calling
[animate()](/javadoc/latest/com/google/gwt/user/client/ui/AnimatedLayout.html#animate-int-). See ["Recipes"](#recipes)
below for specific examples.

## RequiresResize and ProvidesResize<a id="Resize"></a>

Two new characteristic interfaces were introduced in GWT 2.0: [RequiresResize](/javadoc/latest/com/google/gwt/user/client/ui/RequiresResize.html)
and [ProvidesResize](/javadoc/latest/com/google/gwt/user/client/ui/ProvidesResize.html). These are used to propagate notification of resize events
throughout the widget hierarchy.

[RequiresResize](/javadoc/latest/com/google/gwt/user/client/ui/RequiresResize.html) provides a single method,
[onResize()](/javadoc/latest/com/google/gwt/user/client/ui/RequiresResize.html#onResize--), which is called by the widget's parent
whenever the child's size has changed. [ProvidesResize](/javadoc/latest/com/google/gwt/user/client/ui/ProvidesResize.html) is simply a tag
interface indicating that a parent widget will honor this contract. The purpose
of these two interfaces is to form an unbroken hierarchy between all widgets
that implement RequiresResize and the [RootLayoutPanel](/javadoc/latest/com/google/gwt/user/client/ui/RootLayoutPanel.html), which listens for any
changes (such as the browser window resizing) that could affect the size of
widgets in the hierarchy.

### When to use [onResize()](/javadoc/latest/com/google/gwt/user/client/ui/RequiresResize.html#onResize--)

Most widgets should _not_ need to know when they've been resized, as the
browser's layout engine should be doing most of the work. However, there are
times when a widget _does_ need to know. This comes up, for example, when a
widget contains a dynamic list of items depending upon how much room is
available to display them. Because it's almost always faster to let the layout
engine do its work than to run code, you should not lean upon
[onResize()](/javadoc/latest/com/google/gwt/user/client/ui/RequiresResize.html#onResize--) unless you have no alternative.

### When and how to implement ProvidesResize

A panel that implements ProvidesResize is expected to propagate resize events
to any of its child widgets that implement RequiresResize. For a canonical
example of this, see the implementation of [LayoutPanel.onResize()](/javadoc/latest/com/google/gwt/user/client/ui/LayoutPanel.html#onResize--). Most
custom widgets will want to composite an existing layout panel using
[ResizeComposite](/javadoc/latest/com/google/gwt/user/client/ui/ResizeComposite.html), however, as described next.

### ResizeComposite

When creating a custom [Composite](/javadoc/latest/com/google/gwt/user/client/ui/Composite.html) widget that wrap a widget that implements
[RequiresResize](/javadoc/latest/com/google/gwt/user/client/ui/RequiresResize.html), you should use [ResizeComposite](/javadoc/latest/com/google/gwt/user/client/ui/ResizeComposite.html) as its base class. This
subclass of [Composite](/javadoc/latest/com/google/gwt/user/client/ui/Composite.html) automatically propagates resize events to its wrapped
widget.

## Moving to Standards Mode<a id="Standards"></a>

The GWT 2.0 layout system is intended to work only in "standards mode". This
means that you should always place the following declaration at the top of your
HTML pages:
    `<!DOCTYPE html>`

### What won't work in standards mode?

As mentioned above, some of the existing GWT panels do not behave entirely as
expected in standards mode. This stems primarily from differences between the
way standards and quirks modes render tables.

#### CellPanel (HorizontalPanel, VerticalPanel, DockPanel)

These panels all use table cells as their basic structural units. While they
still work in standards mode, they will lay out their children somewhat
differently. The main difference is that their children will not respect width
and height properties (it is common to set children of CellPanels explicitly to
100% width and height). There are also differences in the way that the browser
allocates space to individual table rows and columns that can lead to
unexpected behavior in standards mode.

You should use [DockLayoutPanel](/javadoc/latest/com/google/gwt/user/client/ui/DockLayoutPanel.html) in place of [DockPanel](/javadoc/latest/com/google/gwt/user/client/ui/DockPanel.html). [VerticalPanel](/javadoc/latest/com/google/gwt/user/client/ui/VerticalPanel.html) can
usually be replaced by a simple [FlowPanel](/javadoc/latest/com/google/gwt/user/client/ui/FlowPanel.html) (since block-level elements will
naturally stack up vertically).

[HorizontalPanel](/javadoc/latest/com/google/gwt/user/client/ui/HorizontalPanel.html) is a bit trickier. In some cases, you can simply replace it
with a [DockLayoutPanel](/javadoc/latest/com/google/gwt/user/client/ui/DockLayoutPanel.html), but that requires that you specify its childrens'
widths explicitly. The most common alternative is to use [FlowPanel](/javadoc/latest/com/google/gwt/user/client/ui/FlowPanel.html), and to
use the `float: left;` CSS property on its children. And of course, you can
continue to use [HorizontalPanel](/javadoc/latest/com/google/gwt/user/client/ui/HorizontalPanel.html) itself, as long as you take the caveats above
into account.

#### StackPanel

StackPanels do not work very well in standards mode. Because of the differences
in table rendering mentioned above, StackPanel will almost certainly not do
what you expect in standards mode, and you should replace them with
[StackLayoutPanel](/javadoc/latest/com/google/gwt/user/client/ui/StackLayoutPanel.html).

#### SplitPanel (HorizontalSplitPanel and VerticalSplitPanel)

SplitPanels are very unpredictable in standards mode, and you should almost
invariably replace them with [SplitLayoutPanel](/javadoc/latest/com/google/gwt/user/client/ui/SplitLayoutPanel.html).

## Design of the GWT 2.0 layout system<a id="Design"></a>

Prior to 2.0, GWT's mechanisms for handling application-level layout have a
number of significant problems:

*   They're unpredictable.
*   They often require extra code to fix up their deficiencies:
    *   For example, causing an application to fill the browser's client area with interior scrolling is nearly impossible without extra code.
*   They don't all work well in standards mode.

Their underlying motivation was sound &mdash; the intention was to let the
browser's native layout engine do almost all of the work. But the above
deficiencies can be crippling.

### Goals

*   Perfectly predictable layout behavior. Precision layout should be possible.
    *   It should also work in the presence of CSS decorations (border, margin, and padding) with arbitrary units.
*   Work correctly in standards-mode.
*   Get the browser to do almost all of the work in its layout engine.
    *   Manual adjustments should occur only when strictly necessary.
*   Smooth, automatic animation.

### Non-Goals

*   Work in quirks-mode.
*   Swing-style layout based on "preferred size". This is effectively intractable in the browser.
*   Take over all layout. This design is intended to handle coarse-grained "desktop-like" layout. The individual bits and pieces, such as form elements, buttons, tables, and text should still be laid out naturally.

### The Layout class

The [Layout](/javadoc/latest/com/google/gwt/layout/client/Layout.html) class contains all the underlying logic used by the layout system,
along with all the implementation details used to normalize layout behavior on
various browsers.

It is actually widget-agnostic, operating directly on DOM elements. Most
applications will have no reason to work directly with this class, but it should
prove useful to alternate widget library writers.

### Constraint-based Layout

The GWT 2.0 layout system is built upon the simple constraint system that
exists natively in CSS. This uses the properties `left`, `top`, `width`,
`height`, `right`, and `bottom`. While most developers are familiar with these
properties, it is less well-known that they can be combined in various ways to
form a simple constraint system. Take the following CSS example:

```css
.parent {
  position: relative; /* to establish positioning context */
}

.child {
  position: absolute; left:1em; top:1em; right:1em; bottom:1em;
}
```

In this example, the child will automatically consume the parent's entire
space, minus 1em of space around the edge. Any two of these properties (on each
axis) forms a valid constraint pair (three would be degenerate), producing lots
of interesting possibilities. This is especially true when you consider various
mixtures of relative units, such as "em" and "%".

## Recipes<a id="Recipes"></a>

The following are a series of simple "recipes" for creating various structures
and dealing with different scenarios. Where possible, we'll describe the layout
in terms of [UiBinder](DevGuideUiBinder.html) templates.

### Basic application layout

The following sample shows a simple application-style layout with a header, a
navigation area on the left edge, and a scrollable content area.

```xml
<g:DockLayoutPanel unit='EM'>
  <g:north size='4'>
    <g:Label>Header</g:Label>
  </g:north>

  <g:west size='16'>
    <g:Label>Navigation</g:Label>
  </g:west>

  <g:center>
    <g:ScrollPanel>
      <g:Label>Content Area</g:Label>
    </g:ScrollPanel>
  </g:center>
</g:DockLayoutPanel>
```

You must place this structure in a container that implements [ProvidesResize](/javadoc/latest/com/google/gwt/user/client/ui/ProvidesResize.html),
which is most commonly [RootLayoutPanel](/javadoc/latest/com/google/gwt/user/client/ui/RootLayoutPanel.html). The following code demonstrates how
to do this:

```java
interface Binder extends UiBinder<Widget, BasicApp> { }
private static final Binder binder = GWT.create(Binder.class);

public void onModuleLoad() {
  RootLayoutPanel.get().add(binder.createAndBindUi());
}
```

### Splitters

[SplitLayoutPanel](/javadoc/latest/com/google/gwt/user/client/ui/SplitLayoutPanel.html) works much like [DockLayoutPanel](/javadoc/latest/com/google/gwt/user/client/ui/DockLayoutPanel.html), except that it only
supports pixel units. The basic application structure above can be given a
splitter between the navigation and content areas like so:

```xml
<g:DockLayoutPanel unit='EM'>
  <g:north size='4'>
    <g:Label>Header</g:Label>
  </g:north>

  <g:center>
    <g:SplitLayoutPanel>
      <g:west size='128'>
        <g:Label>Navigation</g:Label>
      </g:west>

      <g:center>
        <g:ScrollPanel>
          <g:Label>Content Area</g:Label>
        </g:ScrollPanel>
      </g:center>
    </g:SplitLayoutPanel>
  </g:center>
</g:DockLayoutPanel>
```

Note how we mix the dock and split panels, so that the header's size can be
specified in `EM` units.

### Layout animation

To use animation with a [LayoutPanel](/javadoc/latest/com/google/gwt/user/client/ui/LayoutPanel.html), you must first create an initial set of
constraints, then animate to a target set of constraints. In the following
example, we start with a child widget positioned at the top, but with no height
so that it is effectively hidden. Calling [LayoutPanel.forceLayout()](/javadoc/latest/com/google/gwt/user/client/ui/LayoutPanel.html#forceLayout--) "fixes"
the initial constraints.

```java
panel.setWidgetTopHeight(child, 0, PX, 0, PX);
panel.forceLayout();
```

Now we give the widget a height of 2em and explicitly call
LayoutPanel.animate(int) to cause it to resize over 500 ms.

```java
panel.setWidgetTopHeight(child, 0, PX, 2, EM);
panel.animate(500);
```

This will work with any constraints and any number of children.

### Implementing a Composite that RequiresResize

Widgets that implement [RequiresResize](/javadoc/latest/com/google/gwt/user/client/ui/RequiresResize.html) expect [RequiresResize.onResize()](/javadoc/latest/com/google/gwt/user/client/ui/RequiresResize.html#onResize--) to
be called whenever the widget's size changes. If you are wrapping such a widget
in a [Composite](/javadoc/latest/com/google/gwt/user/client/ui/Composite.html), you'll need to use [ResizeComposite](/javadoc/latest/com/google/gwt/user/client/ui/ResizeComposite.html) instead to ensure that
this call is propagated correctly, like so:

```java
class MyWidget extends ResizeComposite {
  private LayoutPanel p = new LayoutPanel();

  public MyWidget() {
    initWidget(p);
  }
}
```

### Child widget visibility

The [Layout](/javadoc/latest/com/google/gwt/layout/client/Layout.html) class has to wrap each of its child elements in a "container"
element in order to work properly. One implication of this is that, when you
call [UIObject.setVisible(boolean)](/javadoc/latest/com/google/gwt/user/client/ui/UIObject.html#setVisible-boolean-) on a widget within a [LayoutPanel](/javadoc/latest/com/google/gwt/user/client/ui/LayoutPanel.html), it
won't behave quite as expected: the widget will indeed be made invisible, but
it will tend to consume mouse events (actually, it's the container element that
is doing so).

To work around this, you need to use
[LayoutPanel.setWidgetVisible(Widget,boolean)](/javadoc/latest/com/google/gwt/user/client/ui/LayoutPanel.html#setWidgetVisible-com.google.gwt.user.client.ui.Widget-boolean-):

```java
LayoutPanel panel = ...;
Widget child;
panel.add(child);
panel.setWidgetVisible(child, false);
```

### Using a LayoutPanel without RootLayoutPanel

In most cases you should use layout panels by attaching them to a
[RootLayoutPanel](/javadoc/latest/com/google/gwt/user/client/ui/RootLayoutPanel.html), either directly or via other panels that implement
[ProvidesResize](/javadoc/latest/com/google/gwt/user/client/ui/ProvidesResize.html).

There are, however, instances where you need to use a layout panel within a
normal widget (e.g., [FlowPanel](/javadoc/latest/com/google/gwt/user/client/ui/FlowPanel.html) or [RootPanel](/javadoc/latest/com/google/gwt/user/client/ui/RootPanel.html)). In these cases, you will need
to set the panel's size explicitly, as in the following example:

```java
LayoutPanel panel = new LayoutPanel();
RootPanel.get("someId").add(panel);
panel.setSize("20em", "10em");
```

Note that [RootLayoutPanel](/javadoc/latest/com/google/gwt/user/client/ui/RootLayoutPanel.html) provides no
mechanism for wrapping an arbitrary element like [RootPanel](/javadoc/latest/com/google/gwt/user/client/ui/RootPanel.html)
does. This is because it is impossible to know when an arbitrary element has been resized by the browser. If you want to resize a layout panel in an arbitrary element,
you must do so manually.

This also applies to layout panels used in [PopupPanel](/javadoc/latest/com/google/gwt/user/client/ui/PopupPanel.html) and
[DialogBox](/javadoc/latest/com/google/gwt/user/client/ui/DialogBox.html). The following example shows the use of a [SplitLayoutPanel](/javadoc/latest/com/google/gwt/user/client/ui/SplitLayoutPanel.html) in a
[DialogBox](/javadoc/latest/com/google/gwt/user/client/ui/DialogBox.html):

```java
SplitLayoutPanel split = new SplitLayoutPanel();
split.addWest(new HTML("west"), 128);
split.add(new HTML("center"));
split.setSize("20em", "10em");

DialogBox dialog = new DialogBox();
dialog.setText("caption");
dialog.add(split);
dialog.show();
```

### Tables and Frames

Widgets that are implemented using `<table>` or `<frame>` elements do not
automatically fill the space provided by the layout. In order to fix this, you
will need to explicitly set these widgets `width` and `height` to `100%`. The
following example shows this with a
[RichTextArea](/javadoc/latest/com/google/gwt/user/client/ui/RichTextArea.html),
which is implemented using an `<iframe>` element.

```java
<g:DockLayoutPanel unit='EM'>
  <g:north size='2'>
    <g:HTML>Header</g:HTML>
  </g:north>

  <g:south size='2'>
    <g:HTML>Footer</g:HTML>
  </g:south>

  <g:center>
    <g:RichTextArea width='100%' height='100%'/>
  </g:center>
</g:DockLayoutPanel>
```
