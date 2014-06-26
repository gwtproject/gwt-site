<ol class="toc" id="pageToc">
  <li><a href="#Layout">Layout</a>
    <ol class="toc">
      <li><a href="#I'm_having_trouble_with_my_layout.__How_do_I_tell_where_my">I'm having trouble with my layout. How
do I tell where my panels are?</a></li>
      <li><a href="#How_do_I_create_an_app_that_fills_the_page_vertically_when_the_b">How do I create an app that
fills the page vertically when the browser window resizes?</a></li>
      <li><a href="#Why_doesn't_setting_a_widget's_height_as_a_percentage">Why doesn't setting a widget's height as a
percentage work?</a></li>
      <li><a href="#Does_GWT_support_standards_mode?">Does GWT support standards mode?</a></li>
    </ol>
  </li>
  <li><a href="#Event_Handling_and_Performance">Event Handling and Performance</a>
    <ol class="toc">
      <li><a href="#How_do_I_display_a_big_list_of_items_in_a_table_with_good_perfor">How do I display a big list
of items in a table with good performance?</a></li>
      <li><a href="#What_can_I_do_to_make_images_and_borders_appear_to_load_more_qui">What can I do to make images
and borders appear to load more quickly the first time they are used?</a></li>
      <li><a href="#Why_do_I_see_poor_performance_when_using_a_one_pixel_tall_backgr">Why do I see poor
performance when using a one pixel tall background image?</a></li>
      <li><a href="#As_the_application_grows,_event_handlers_seem_to_fire_more_slowl">As the application grows,
event handlers seem to fire more slowly</a></li>
      <li><a href="#How_can_I_efficiently_handle_events_from_many_interior_Widgets?">How can I efficiently handle
events from many interior Widgets?</a></li>
    </ol>
  </li>
</ol>

<div id="FAQ_UI"/>

<h2 class="h2-group" id="Layout">Layout</h2>

<p/>

<h2 id="I'm_having_trouble_with_my_layout.__How_do_I_tell_where_my">I'm having trouble with my layout. How
do I tell where my panels are?</h2>

<p>When you are working with complex layouts, it is sometimes difficult to understand why your widgets aren't showing up where you think they should.</p>

<p>As an example, suppose you were trying to create a layout with two buttons, one on the left side of the browser, and the other on the right side of the browser. Here's some
code that attempts to do that:</p>

<pre class="prettyprint">
    VerticalPanel vertPanel = new VerticalPanel();

    DockPanel dockPanel = new DockPanel();
    dockPanel.setWidth(&quot;100%&quot;);
    dockPanel.add(new Button(&quot;leftButton&quot;), DockPanel.WEST);
    dockPanel.add(new Button(&quot;rightButton&quot;), DockPanel.EAST);

    vertPanel.add(dockPanel);

    RootPanel.get().add(vertPanel);
</pre>

<p>And here is how the screen ends up:</p>

<blockquote><img src="images/FAQ_UILayout1.png"/></blockquote>

<p>What went wrong? We set the Dock Panel to fill up 100% of the space, and then each button to the left or right of the screen. At this point, it can be helpful to probe and see
what the boundaries of your widgets are, because they may not be where you think.</p>

<p>The first technique is to use a DOM inspection tool, such as <a href="http://www.getfirebug.com">Firebug</a> for the <a href="http://www.mozilla.com">Firefox</a> browser. As you hover over elements in the DOM they will highlight in the browser window.</p>

<p>Another technique you can use is to modify your GWT code to turn on borders on your panels (or other widgets.)</p>

<pre class="prettyprint">
VerticalPanel vertPanel = new VerticalPanel();

    DockPanel dockPanel = new DockPanel();
    dockPanel.setWidth(&quot;100%&quot;);
    dockPanel.add(new Button(&quot;leftButton&quot;), DockPanel.WEST);
    dockPanel.add(new Button(&quot;rightButton&quot;), DockPanel.EAST);
    dockPanel.setStylePrimaryName(&quot;dockPanel&quot;);
    dockPanel.setBorderWidth(5);

    vertPanel.add(dockPanel);
    vertPanel.setStylePrimaryName(&quot;vertPanel&quot;);
    vertPanel.setBorderWidth(5);

    RootPanel.get().add(vertPanel);
</pre>

<p>You can also use the associated CSS stylesheet to change the border properties. In this case, the stylesheet changes the colors of the borders to make them easier to tell
apart:</p>

<pre>
.dockPanel {
    border-color: orange;
}
.vertPanel {
    border-color: blue;
}
</pre>

<p>Now you can see clearly where the outlines of each panel are:</p>

<blockquote><img src="images/FAQ_UILayout2.png"/></blockquote>

<p>In this case, the outermost panel needs to be 100% width as well, so we
change the code, hit <i>Refresh</i> in the browser window:</p>

<pre class="prettyprint">
    vertPanel.setWidth(&quot;100%&quot;);
</pre>

<blockquote><img src="images/FAQ_UILayout3.png"/></blockquote>

<p>This is closer to what we want, but it still isn't right. Now we need to change the cell alignment in the <a href="/javadoc/latest/com/google/gwt/user/client/ui/DockPanel.html">DockPanel</a> instance:</p>

<pre class="prettyprint">
    Button rightButton = new Button(&quot;rightButton&quot;);
    dockPanel.add(rightButton, DockPanel.EAST);
    dockPanel.setCellHorizontalAlignment(rightButton, HasHorizontalAlignment.ALIGN_RIGHT);
</pre>

<blockquote><img src="images/FAQ_UILayout4.png"/></blockquote>

<p>(Don't forget to turn off these styles when you are finished!)</p>

<p/>

<h2 id="How_do_I_create_an_app_that_fills_the_page_vertically_when_the_b">How do I create an app that
fills the page vertically when the browser window resizes?</h2>

<p>
As of GWT 2.0, creating an application that fills the browser is easy using <a href="DevGuideUiPanels.html#LayoutPanels">Layout Panels</a>.  LayoutPanels
such as <a href="/javadoc/latest/com/google/gwt/user/client/ui/DockLayoutPanel.html">DockLayoutPanel</a> and
<a href="/javadoc/latest/com/google/gwt/user/client/ui/SplitLayoutPanel.html">SplitLayoutPanel</a> automatically resize to the size of the window when 
the browser resizes.
</p>

<p>The <a href="http://samples.gwtproject.org/samples/Mail/Mail.html">Mail sample application</a> demonstrates SplitLayoutPanel in action.</p>

<p>See the Developer's Guide on <a href="DevGuideUiPanels.html#LayoutPanels">Layout Panels</a> for more details and code snippets.

<h2 id="Why_doesn't_setting_a_widget's_height_as_a_percentage">Why doesn't setting a widget's height as a
percentage work?</h2>

<p>In some cases, the Widget.setHeight() and Widget.setSize() methods do not seem to work with sizes expressed as percentages. The GWT widgets do not do any calculations with
these values. These methods set the widget's CSS height and/or and width properties and depend on the behavior of the enclosing widget.</p>

<p>In standards mode, if an element (such as a <tt>&lt;</tt>div<tt>&gt;</tt>) is inside a table cell <tt>&lt;</tt>td<tt>&gt;</tt>, you cannot set the <tt>&lt;</tt>div<tt>&gt;</tt>
height and width attributes as percentages. They are ignored. For example, the styles in the <tt>&lt;</tt>div<tt>&gt;</tt> element below is ignored in standards mode:</p>

<pre class="prettyprint">
    &lt;td&gt;
      &lt;div style=&quot;height:100%;width:100%;&gt;test&lt;/div&gt;
    &lt;/td&gt;
</pre>

<p>Workarounds include</p>

<ul>
<li>Try changing the enclosing outer widget to a different type. For example, VerticalPanel is implemented with an HTML <tt>&lt;</tt>table<tt>&gt;</tt> element and might behave
differently than AbsolutePanel which is implemented with an HTML <tt>&lt;</tt>div<tt>&gt;</tt> element.</li>

<li>If the <tt>&lt;</tt>DOCTYPE...<tt>&gt;</tt> declaration in the HTML host page is set to standards mode (such as HTML 4.01 Transitional), try removing the <tt>&lt;</tt>DOCTYPE
...<tt>&gt;</tt> declaration or changing it to put the browser rendering engine into quirks mode.</li>
</ul>



<p/>

<h2 id="Does_GWT_support_standards_mode?">Does GWT support standards mode?</h2>

<p>As of GWT 1.5, yes. Standards mode is supported. For backwards compatibility, quirks mode is also still supported. Support for quirks mode, however, may be dropped in future
versions of GWT, and it is recommended that you update your GWT-enabled pages to use standards mode.</p>

<h2 class="h2-group" id="Event_Handling_and_Performance">Event Handling and Performance</h2>

<p/>

<h2 id="How_do_I_display_a_big_list_of_items_in_a_table_with_good_perfor">How do I display a big list
of items in a table with good performance?</h2>

<p>Here are a few pointers for addressing the need to display lots of data through a table widget efficiently.</p>

<h3 id="Fixed_width_tables">Fixed width tables</h3>

<p>Performance wise, you are better off using a fixed-width table than one that dynamically resizes.</p>

<h3 id="RPC_and_other_network_transaction_considerations">RPC and other network transaction considerations</h3>

<p>Most applications are loading the data for their table over the network. Try to reduce the number of round trips your application has to make to populate the table. Two main
strategies come to mind:</p>

<ul>
<li>Fetch multiple rows in the same request.</li>

<li>Combine activity from the different subsystems of the app into a single RPC call. From the combined data structure so you can fetch data relevant to different parts of the
application in the same RPC call and reduce user wait time.</li>
</ul>



<h3 id="Testing_for_Performance">Testing for Performance</h3>

<p>If you have a large table, make sure you run a test with the largest number of rows you want to support. On some browsers it has been observed that table performance slows
worse than linearly with respect to the number of rows.</p>

<h3 id="Try_using_PagingScrollTable">Try using PagingScrollTable</h3>

<p>If you find yourself with a table performance issue, the best way to address it might be not display most of your data. A table implementation called the <a href="http://code.google.com/p/google-web-toolkit-incubator/wiki/PagingScrollTable">PagingScrollTable</a> can help you display data a page at a time instead of
having to wait for the entire dataset to download to the browser. This breed of table is very common in web apps and won't be foreign to users. As a rule of thumb, consider using
PagingScrollTable if you have more than 50 rows of data to display.</p>

<p>Note that the PagingScrollTable is a part of the <a href="http://code.google.com/p/google-web-toolkit-incubator/">GWT Incubator</a> project which you will need
to download separately from the core GWT distribution.</p>

<p>If you can use the PagingScrollTable, then consider the <a href="http://code.google.com/p/google-web-toolkit-incubator/wiki/PagingScrollTable">CachedTableModel</a> (also a part of the <a href="http://code.google.com/p/google-web-toolkit-incubator/">GWT Incubator</a>) you can use to
automatically prefetch rows, such as the next page. This can increase the perceived speed considerably.</p>

<h3 id="Try_using_the_BulkTableRenderer_and_PreloadedTable_classes">Try using the BulkTableRenderer and
PreloadedTable classes</h3>

<p>A <a href="http://code.google.com/p/google-web-toolkit-incubator/wiki/BulkTableRenderers">BulkTableRenderer</a> will render all rows in a table at once.
BulkTableRenderer has derived types for different purposes. As long as your table contents are not widgets, you provide a table model and the BulkRenderer creates the entire table
rendered as a single HTML string and set with <a href="/javadoc/latest/com/google/gwt/dom/client/Element.html#setInnerHTML(java.lang.String)">setInnerHtml()</a> which can
be 2-10x faster.</p>

<p>Note that after being loaded, widgets, cell spans, row spans etc. may be added to the table, but there will be no speed advantage for them.</p>

<p/>

<h2 id="What_can_I_do_to_make_images_and_borders_appear_to_load_more_qui">What can I do to make images
and borders appear to load more quickly the first time they are used?</h2>

<p>The first time your application accesses an image, you may notice that it may only partially load, or load more slowly than it displays on subsequent invocations. Since first
impressions are important, you may want to try this trick.</p>

<p>The underlying issue is that the images needed for the background, border, or animation are fetched from the server on demand. A solution is to pre-fetch the images so that
they are sitting in the browser cache by the time the animation is actually invoked. Go through your CSS files for the images used in your styling that are running slowly, and use
the <a href="/javadoc/latest/com/google/gwt/user/client/ui/Image.html#prefetch(java.lang.String)">Image.prefetch()</a>
method.</p>

<pre class="prettyprint">
    Image.prefetch(&quot;images/corner-bl.png&quot;);
    Image.prefetch(&quot;images/corner-br.png&quot;);
    Image.prefetch(&quot;images/corner-tl.png&quot;);
    Image.prefetch(&quot;images/corner-tr.png&quot;);
</pre>

<p/>

<h2 id="Why_do_I_see_poor_performance_when_using_a_one_pixel_tall_backgr">Why do I see poor
performance when using a one pixel tall background image?</h2>

<p>It is common to use a one pixel image for a repeating background image. Be advised, however, that there can be a performance penalty for covering a large area with a small
image (in particular, we noticed the problem on Internet Explorer). This is particularly noticeable when using GWT animations.</p>

<p>One way to work around the problem is to increase the size of your image so that the rendering image doesn't have to repeat the image as often. So, instead of using a 20 x 1
sized image, you might try a 20 x 100 image. The tradeoff here is that the browser will have to download a larger image over the network.</p>

<p/>

<h2 id="As_the_application_grows,_event_handlers_seem_to_fire_more_slowl">As the application grows,
event handlers seem to fire more slowly</h2>

<p>If you are creating an interface with many widgets that need to respond to events, such as <tt>click</tt> events, you may find that the application handles events more and more
slowly. One example of this might be having a list of widgets in a table.</p>

<p>One contributor to poor performance can be the overhead of managing many click handler instances. Consider a common way of handling click events:</p>

<pre class="prettyprint">
  Button newButton = new Button(titleText);
  newButton.addClickHandler(new ClickHandler() {
    public void onClick(ClickEvent event) {
      dialogBox.hide();
    }
  });
</pre>

<p>Each widget has its own click handler instance created by adding an anonymous inner class when the widget is created. This construct is convenient and causes no performance
issues for most cases, but when creating many widgets that need event handling, consider creating a single click handler and sharing it between multiple widgets:</p>

<pre class="prettyprint">
 class MyApp implements EntryPoint, ClickHandler {

   /* Be careful with saving UI components in datastructures like this:
    * if you remove a button from the app, make sure you also remove
    * its reference from buttonMap HashMap to avoid memory leaks.
    */
   Map buttonMap&lt;Button, Integer&gt; = new HashMap&lt;Button,Integer&gt;();

   public void onModuleLoad() {
     FlowPanel panel = new FlowPanel();
     for (int i = 1; i &lt; 100; ++i) {
        Button newButton = new Button(&quot;Option &quot; + i);
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
</pre>

<p/>

<h2 id="How_can_I_efficiently_handle_events_from_many_interior_Widgets?">How can I efficiently handle
events from many interior Widgets?</h2>

<p>Quirksmode has a writeup of the background behind <a href="http://www.quirksmode.org/js/events_order.html">Event Bubbling</a> in how different browsers deliver
events. When using a GWT event handler, GWT hides all this away from you and delivers events only for the element you are interested in. This is a useful programming abstraction,
but can lead to slow performance when many handlers are added to the system.</p>

<p>If you are building a widget, you may want to consider handling events in a different way, called Event Bubbling. Essentially, this technique means you have one handler for the
entire widget, and you have one handler callback that executes on behalf of all interior widgets.</p>

<p>See the <a href="/javadoc/latest/com/google/gwt/user/client/ui/Tree.html">implementation of the Tree</a> class
for an example of using Event Bubbling in GWT.</p>
<p/>