<p>Browsers provide an interface to examine and manipulate the on-screen elements using the <a href="http://w3c.org/DOM/">DOM</a> (Document Object Model).
Traditionally, JavaScript programmers use the DOM to program the user interface portion of their logic, and traditionally, they have had to account for the many differences in the
implementation of the DOM on different browsers.</p>

<p>So that you don't have to worry (generally) about cross-browser support when implementing user interfaces, GWT provides a set of <a href="DevGuideUiWidgets.html">widget</a> and <a href="DevGuideUiPanels.html">panel</a> classes that wrap up this functionality. But sometimes you need to access the DOM. For example, if you
want to:</p>

<ul>
<li>provide a feature in your user interface that GWT does not support</li>

<li>write a new Widget class</li>

<li>access an HTML element defined directly in the host page</li>

<li>handle browser Events at a low level</li>

<li>perform some filtering or other processing on an HTML document loaded into the browser</li>
</ul>



<p>GWT provides the classes in the <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/dom/client/package-summary.html">DOM</a>
package for interacting with the DOM directly. These classes provide statically-typed interfaces for interacting with DOM objects, as well as a degree of
cross-browser abstraction.</p>

<h2>Using the DOM to manipulate a widget</h2>

<p>Each widget and panel has an underlying DOM element that you can access with the <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/user/client/ui/UIObject.html#getElement()">getElement()</a> method. You can use the
getElement() method to get the underlying element from the DOM.</p>

<p>The following example shows how to set a style attribute to change a widget's background color.</p>

<pre class="prettyprint">
private HTML htmlWidget;

// Other code to instantiate the widget...

// Change the description background color.
htmlWidget.getElement().getStyle().setBackgroundColor("#ffee80");
</pre>

<p>Here, the getElement() method derived from the <tt>Widget</tt> superclass returns a DOM <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/dom/client/Element.html">Element</a> object representing a node in the DOM tree
structure and adds a style attribute to it.</p>

<p>This is an example where using the DOM isn't absolutely necessary. An alternative approach is to use <a href="DevGuideUiCss.html">style sheets</a> and
associate different style classes to the widget using the <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/user/client/ui/UIObject.html#setStylePrimaryName(java.lang.String)">setStylePrimaryName()</a> or <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/user/client/ui/UIObject.html#setStyleName(java.lang.String)">setStyleName()</a> method
instead.</p>

<h2>Finding an element in the DOM</h2>

<p>The following example shows how to combine a JSNI method with Java code to manipulate the DOM. First, we have a JSNI routine that will retrieve all the child elements that are
Anchor tags. The element objects are assigned a unique ID for easy access from Java:</p>

<pre class="prettyprint">
/**
 * Find all child elements that are anchor tags,
 * assign a unique id to them, and return a list of
 * the unique ids to the caller.
 */
private native void putElementLinkIDsInList(Element elt, ArrayList&lt;String&gt; list) /*-{
  var links = elt.getElementsByTagName(&quot;a&quot;);

  for (var i = 0; i &lt; links.length; i++ ) {
    var link = links.item(i);
    link.id = (&quot;uid-a-&quot; + i);
    list.@java.util.ArrayList::add(Ljava/lang/Object;) (link.id);
  }
}-*/;
</pre>

<p>And what could you possibly do with a DOM element once you have found it? This code iterates through all the anchor tags returned from the above method and then rewrites where
it points to:</p>

<pre class="prettyprint">
/**
 * Find all anchor tags and if any point outside the site, 
 * redirect them to a &quot;blocked&quot; page.
 */
 private void rewriteLinksIterative() {
   ArrayList&lt;String&gt; links = new ArrayList&lt;String>();
   putElementLinkIDsInList(this.getElement(), links);
   for (int i = 0; i &lt; links.size(); i++) {
     Element elt = Document.get().getElementById(links.get(i));
     rewriteLink(elt, &quot;www.example.com&quot;);
   }
 }

/**
 * Block all accesses out of the website that don't match 'sitename'
 * @param element An anchor link element
 * @param sitename name of the website to check.  e.g. &quot;www.example.com&quot;
 */
private void rewriteLink(Element element, sitename) {
  String href = element.getPropertyString(&quot;href&quot;);
  if (null == href) {
    return;
  }

  // We want to re-write absolute URLs that go outside of this site
  if (href.startsWith(&quot;http://&quot;) &amp;&amp;
      !href.startsWith(&quot;http://&quot;+sitename+&quot;/&quot;) {
    element.setPropertyString(&quot;href&quot;, &quot;http://&quot;+sitename+&quot;/Blocked.html&quot;);
  }
}
</pre>

<p>The JSNI method sets an ID on each element which we then used as an argument to <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/dom/client/Document.html#getElementById(java.lang.String)">Document.getElementById(id)</a> to
fetch the <tt>Element</tt> in Java.</p>

<h2>Using the DOM to capture a browser event</h2>

<p>GWT contains an <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/dom/client/NativeEvent.html">Event</a> class as a typed interface to the
native DOM Event.</p>

<p>This example shows how to use the DOM methods to catch a keyboard event for particular elements and handle them before the <a href="DevGuideUiHandlers.html">event</a> gets dispatched:</p>

<pre class="prettyprint">
private ArrayList&lt;Element&gt; keyboardEventReceivers = new ArrayList&lt;Element&gt;();

/**
 * Widgets can register their DOM element object if they would like to be a
 * trigger to intercept keyboard events
 */
public void registerForKeyboardEvents(Element e) {
  this.keyboardEventReceivers.add(e);
}

/**
 * Returns true if this is one of the keys we are interested in
 */
public boolean isInterestingKeycode(int keycode) {
  // ...
  return false;
}

/**
 * Setup the event preview class when the module is loaded.
 */
private void setupKeyboardShortcuts() {
  // Define an inner class to handle the event
  Event.addNativePreviewHandler(new NativePreviewHandler() {
    public void onPreviewNativeEvent(NativePreviewEvent preview) {
      NativeEvent event = preview.getNativeEvent();

      Element elt = event.getEventTarget().cast();
      int keycode = event.getKeyCode();
      boolean ctrl = event.getCtrlKey();
      boolean shift = event.getShiftKey();
      boolean alt = event.getAltKey();
      boolean meta = event.getMetaKey();
      if (event.getType().equalsIgnoreCase(&quot;keypress&quot;) || ctrl || shift
          || alt || meta || keyboardEventReceivers.contains(elt)
          || !isInterestingKeycode(keycode)) {
        // Tell the event handler to continue processing this event.
        return;
      }

      GWT.log(&quot;Processing Keycode&quot; + keycode, null);
      handleKeycode(keycode);

      // Tell the event handler that this event has been consumed
      preview.consume();
    }
  });
}

/**
 * Perform the keycode specific processing
 */
private void handleKeycode(int keycode) {
  switch (keycode) {
  // ...
  }
}
</pre>