FAQ - UI
===

1.  [Layout](#Layout)
    1.  [I'm having trouble with my layout. How do I tell where my panels are?](#I)
    2.  [How do I create an app that fills the page vertically when the browser window resizes?](#How_do_I_create_an_app_that_fills_the_page_vertically_when_the_b)
    3.  [Why doesn't setting a widget's height as a percentage work?](#Why_doesn)
    4.  [Does GWT support standards mode?](#Does_GWT_support_standards_mode?) 
2.  [Event Handling and Performance](#Event_Handling_and_Performance)
    1.  [How do I display a big list of items in a table with good performance?](#How_do_I_display_a_big_list_of_items_in_a_table_with_good_perfor)
    2.  [What can I do to make images and borders appear to load more quickly the first time they are used?](#What_can_I_do_to_make_images_and_borders_appear_to_load_more_qui)
    3.  [Why do I see poor performance when using a one pixel tall background image?](#Why_do_I_see_poor_performance_when_using_a_one_pixel_tall_backgr)
    4.  [As the application grows, event handlers seem to fire more slowly](#As_the_application_grows,_event_handlers_seem_to_fire_more_slowl)
    5.  [How can I efficiently handle events from many interior Widgets?](#How_can_I_efficiently_handle_events_from_many_interior_Widgets?)

<div id="FAQ_UI"/>

## Layout<a id="Layout"></a>

### I'm having trouble with my layout. How do I tell where my panels are?<a id="I'm_having_trouble_with_my_layout.__How_do_I_tell_where_my"></a>

When you are working with complex layouts, it is sometimes difficult to understand why your widgets aren't showing up where you think they should.

As an example, suppose you were trying to create a layout with two buttons, one on the left side of the browser, and the other on the right side of the browser. Here's some
code that attempts to do that:

```java
VerticalPanel vertPanel = new VerticalPanel();

    DockPanel dockPanel = new DockPanel();
    dockPanel.setWidth("100%");
    dockPanel.add(new Button("leftButton"), DockPanel.WEST);
    dockPanel.add(new Button("rightButton"), DockPanel.EAST);

    vertPanel.add(dockPanel);

    RootPanel.get().add(vertPanel);
```

And here is how the screen ends up:

> ![img](images/FAQ_UILayout1.png)

What went wrong? We set the Dock Panel to fill up 100% of the space, and then each button to the left or right of the screen. At this point, it can be helpful to probe and see
what the boundaries of your widgets are, because they may not be where you think.

The first technique is to use your browser's dev tools. As you hover over elements in the DOM they will highlight in the browser window.

Another technique you can use is to modify your GWT code to turn on borders on your panels (or other widgets.)

```java
VerticalPanel vertPanel = new VerticalPanel();

    DockPanel dockPanel = new DockPanel();
    dockPanel.setWidth("100%");
    dockPanel.add(new Button("leftButton"), DockPanel.WEST);
    dockPanel.add(new Button("rightButton"), DockPanel.EAST);
    dockPanel.setStylePrimaryName("dockPanel");
    dockPanel.setBorderWidth(5);

    vertPanel.add(dockPanel);
    vertPanel.setStylePrimaryName("vertPanel");
    vertPanel.setBorderWidth(5);

    RootPanel.get().add(vertPanel);
```

You can also use the associated CSS stylesheet to change the border properties. In this case, the stylesheet changes the colors of the borders to make them easier to tell
apart:

```css
.dockPanel {
    border-color: orange;
}
.vertPanel {
    border-color: blue;
}
```

Now you can see clearly where the outlines of each panel are:

> ![img](images/FAQ_UILayout2.png)

In this case, the outermost panel needs to be 100% width as well, so we
change the code, hit _Refresh_ in the browser window:

```java
vertPanel.setWidth("100%");
```

> ![img](images/FAQ_UILayout3.png)

This is closer to what we want, but it still isn't right. Now we need to change the cell alignment in the [DockPanel](/javadoc/latest/com/google/gwt/user/client/ui/DockPanel.html) instance:

```java
Button rightButton = new Button("rightButton");
    dockPanel.add(rightButton, DockPanel.EAST);
    dockPanel.setCellHorizontalAlignment(rightButton, HasHorizontalAlignment.ALIGN_RIGHT);
```

> ![img](images/FAQ_UILayout4.png)

(Don't forget to turn off these styles when you are finished!)

### How do I create an app that fills the page vertically when the browser window resizes?<a id="How_do_I_create_an_app_that_fills_the_page_vertically_when_the_b"></a>

As of GWT 2.0, creating an application that fills the browser is easy using [Layout Panels](DevGuideUiPanels.html#LayoutPanels).  LayoutPanels
such as [DockLayoutPanel](/javadoc/latest/com/google/gwt/user/client/ui/DockLayoutPanel.html) and
[SplitLayoutPanel](/javadoc/latest/com/google/gwt/user/client/ui/SplitLayoutPanel.html) automatically resize to the size of the window when 
the browser resizes.

The [Mail sample application](https://samples.gwtproject.org/samples/Mail/Mail.html) demonstrates SplitLayoutPanel in action.

See the Developer's Guide on [Layout Panels](DevGuideUiPanels.html#LayoutPanels) for more details and code snippets.

### Why doesn't setting a widget's height as a percentage work?<a id="Why_doesn't_setting_a_widget's_height_as_a_percentage"></a>

In some cases, the Widget.setHeight() and Widget.setSize() methods do not seem to work with sizes expressed as percentages. The GWT widgets do not do any calculations with
these values. These methods set the widget's CSS height and/or and width properties and depend on the behavior of the enclosing widget.

In standards mode, if an element (such as a `<`div`>`) is inside a table cell `<`td`>`, you cannot set the `<`div`>`
height and width attributes as percentages. They are ignored. For example, the styles in the `<`div`>` element below is ignored in standards mode:

```html
<td>
      <div style="height:100%;width:100%;>test</div>
    </td>
```

Workarounds include

*   Try changing the enclosing outer widget to a different type. For example, VerticalPanel is implemented with an HTML `<`table`>` element and might behave
differently than AbsolutePanel which is implemented with an HTML `<`div`>` element.
*   If the `<`DOCTYPE...`>` declaration in the HTML host page is set to standards mode (such as HTML 4.01 Transitional), try removing the `<`DOCTYPE
...`>` declaration or changing it to put the browser rendering engine into quirks mode.

### Does GWT support standards mode?<a id="Does_GWT_support_standards_mode?"></a>

As of GWT 1.5, yes. Standards mode is supported. For backwards compatibility, quirks mode is also still supported. Support for quirks mode, however, may be dropped in future
versions of GWT, and it is recommended that you update your GWT-enabled pages to use standards mode.

## Event Handling and Performance<a id="Event_Handling_and_Performance"></a>

### How do I display a big list of items in a table with good performance?<a id="How_do_I_display_a_big_list_of_items_in_a_table_with_good_perfor"></a>

Here are a few pointers for addressing the need to display lots of data through a table widget efficiently.

#### Fixed width tables<a id="Fixed_width_tables"></a>

Performance wise, you are better off using a fixed-width table than one that dynamically resizes.

#### RPC and other network transaction considerations<a id="RPC_and_other_network_transaction_considerations"></a>

Most applications are loading the data for their table over the network. Try to reduce the number of round trips your application has to make to populate the table. Two main
strategies come to mind:

*   Fetch multiple rows in the same request.
*   Combine activity from the different subsystems of the app into a single RPC call. From the combined data structure so you can fetch data relevant to different parts of the
application in the same RPC call and reduce user wait time.

#### Testing for Performance<a id="Testing_for_Performance"></a>

If you have a large table, make sure you run a test with the largest number of rows you want to support. On some browsers it has been observed that table performance slows
worse than linearly with respect to the number of rows.

#### Try using PagingScrollTable<a id="Try_using_PagingScrollTable"></a>

If you find yourself with a table performance issue, the best way to address it might be not display most of your data. A table implementation called the [PagingScrollTable](http://code.google.com/p/google-web-toolkit-incubator/wiki/PagingScrollTable) can help you display data a page at a time instead of
having to wait for the entire dataset to download to the browser. This breed of table is very common in web apps and won't be foreign to users. As a rule of thumb, consider using
PagingScrollTable if you have more than 50 rows of data to display.

Note that the PagingScrollTable is a part of the [GWT Incubator](http://code.google.com/p/google-web-toolkit-incubator/) project which you will need
to download separately from the core GWT distribution.

If you can use the PagingScrollTable, then consider the [CachedTableModel](http://code.google.com/p/google-web-toolkit-incubator/wiki/PagingScrollTable) (also a part of the [GWT Incubator](http://code.google.com/p/google-web-toolkit-incubator/)) you can use to
automatically prefetch rows, such as the next page. This can increase the perceived speed considerably.

#### Try using the BulkTableRenderer and PreloadedTable classes<a id="Try_using_the_BulkTableRenderer_and_PreloadedTable_classes"></a>

A [BulkTableRenderer](http://code.google.com/p/google-web-toolkit-incubator/wiki/BulkTableRenderers) will render all rows in a table at once.
BulkTableRenderer has derived types for different purposes. As long as your table contents are not widgets, you provide a table model and the BulkRenderer creates the entire table
rendered as a single HTML string and set with [setInnerHtml()](/javadoc/latest/com/google/gwt/dom/client/Element.html#setInnerHTML-java.lang.String-) which can
be 2-10x faster.

Note that after being loaded, widgets, cell spans, row spans etc. may be added to the table, but there will be no speed advantage for them.

### What can I do to make images and borders appear to load more quickly the first time they are used?<a id="What_can_I_do_to_make_images_and_borders_appear_to_load_more_qui"></a>

The first time your application accesses an image, you may notice that it may only partially load, or load more slowly than it displays on subsequent invocations. Since first
impressions are important, you may want to try this trick.

The underlying issue is that the images needed for the background, border, or animation are fetched from the server on demand. A solution is to pre-fetch the images so that
they are sitting in the browser cache by the time the animation is actually invoked. Go through your CSS files for the images used in your styling that are running slowly, and use
the [Image.prefetch()](/javadoc/latest/com/google/gwt/user/client/ui/Image.html#prefetch-java.lang.String-)
method.

```java
Image.prefetch("images/corner-bl.png");
    Image.prefetch("images/corner-br.png");
    Image.prefetch("images/corner-tl.png");
    Image.prefetch("images/corner-tr.png");
```

### Why do I see poor performance when using a one pixel tall background image?<a id="Why_do_I_see_poor_performance_when_using_a_one_pixel_tall_backgr"></a>

It is common to use a one pixel image for a repeating background image. Be advised, however, that there can be a performance penalty for covering a large area with a small
image (in particular, we noticed the problem on Internet Explorer). This is particularly noticeable when using GWT animations.

One way to work around the problem is to increase the size of your image so that the rendering image doesn't have to repeat the image as often. So, instead of using a 20 x 1
sized image, you might try a 20 x 100 image. The tradeoff here is that the browser will have to download a larger image over the network.

### As the application grows, event handlers seem to fire more slowly<a id="As_the_application_grows,_event_handlers_seem_to_fire_more_slowl"></a>

If you are creating an interface with many widgets that need to respond to events, such as `click` events, you may find that the application handles events more and more
slowly. One example of this might be having a list of widgets in a table.

One contributor to poor performance can be the overhead of managing many click handler instances. Consider a common way of handling click events:

```java
Button newButton = new Button(titleText);
  newButton.addClickHandler(new ClickHandler() {
    public void onClick(ClickEvent event) {
      dialogBox.hide();
    }
  });
```

Each widget has its own click handler instance created by adding an anonymous inner class when the widget is created. This construct is convenient and causes no performance
issues for most cases, but when creating many widgets that need event handling, consider creating a single click handler and sharing it between multiple widgets:

```java
class MyApp implements EntryPoint, ClickHandler {

   /* Be careful with saving UI components in datastructures like this:
    * if you remove a button from the app, make sure you also remove
    * its reference from buttonMap HashMap to avoid memory leaks.
    */
   Map buttonMap<Button, Integer> = new HashMap<Button,Integer>();

   public void onModuleLoad() {
     FlowPanel panel = new FlowPanel();
     for (int i = 1; i < 100; ++i) {
        Button newButton = new Button("Option " + i);
        newButton.addClickHandler(this);
        panel.add(newButton);
        buttonmap.add(newButton, Integer.valueOf(i));
     }
     RootPanel.get().add(panel);
   }

   // The shared ClickHandler code.
   public void onClick(ClickEvent event) {
     Object sender = event.getSource();
     if (sender instanceof Button) {
       Button b = (Button) sender;
       Integer context = buttonMap.get(b);
       if (context != null) {
         // ... Handle the button click for this button.
       }
     }
   }
 }
```

### How can I efficiently handle events from many interior Widgets?<a id="How_can_I_efficiently_handle_events_from_many_interior_Widgets?"></a>

Quirksmode has a writeup of the background behind [Event Bubbling](http://www.quirksmode.org/js/events_order.html) in how different browsers deliver
events. When using a GWT event handler, GWT hides all this away from you and delivers events only for the element you are interested in. This is a useful programming abstraction,
but can lead to slow performance when many handlers are added to the system.

If you are building a widget, you may want to consider handling events in a different way, called Event Bubbling. Essentially, this technique means you have one handler for the
entire widget, and you have one handler callback that executes on behalf of all interior widgets.

See the [implementation of the Tree](/javadoc/latest/com/google/gwt/user/client/ui/Tree.html) class
for an example of using Event Bubbling in GWT.
