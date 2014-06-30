<p>GWT makes it easy to create custom user interface elements. There are three general strategies to follow:</p>

<ul>
<li><a href="#composite">Create a widget that is a composite</a> of existing widgets.</li>

<li><a href="#new">Create an entirely new widget</a> written in the Java language.</li>

<li><a href="#javascript">Create a widget that wraps JavaScript</a> using <a href="DevGuideCodingBasics.html#DevGuideJavaScriptNativeInterface">JSNI</a> methods.</li>
</ul>

<p>There are numerous third party libraries that provide widgets you can integrate into your GWT module that were created using the strategies listed above.</p>

<h2 id="composite">Building Composites</h2>

<p>The most effective way to create new widgets is to extend the <a href="/javadoc/latest/com/google/gwt/user/client/ui/Composite.html">Composite</a> class. A composite is a
specialized widget that can contain another component (typically, a <a href="/javadoc/latest/com/google/gwt/user/client/ui/Panel.html">Panel</a>) but behaves as if it were its contained widget.
You can easily combine groups of existing widgets into a composite that is itself a reusable widget. Some of the UI components provided in GWT are composites:
for example, the <a href="/javadoc/latest/com/google/gwt/user/client/ui/TabPanel.html">TabPanel</a> (a composite of a TabBar and a DeckPanel) and the
<a href="/javadoc/latest/com/google/gwt/user/client/ui/SuggestBox.html">SuggestBox</a>.</p>

<p>Rather than create complex widgets by subclassing <a href="/javadoc/latest/com/google/gwt/user/client/ui/Panel.html">Panel</a> or another
<a href="/javadoc/latest/com/google/gwt/user/client/ui/Widget.html">Widget</a> type,
it's better to create a composite because a composite usually wants to control which methods are publicly accessible without exposing those methods that it
would inherit from its Panel superclass.</p>

<h3>Example Composite Widget</h3>

<p>The following code snippet shows how to create a composite widget composed of a <a href="/javadoc/latest/com/google/gwt/user/client/ui/TextBox.html" >TextBox</a> widget and a
<a href="/javadoc/latest/com/google/gwt/user/client/ui/CheckBox.html">CheckBox</a> widget laid out in a <a href="/javadoc/latest/com/google/gwt/user/client/ui/VerticalPanel.html">VerticalPanel</a>.</p>

<pre class="prettyprint">
package com.google.gwt.examples;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class CompositeExample implements EntryPoint {

  /**
   * A composite of a TextBox and a CheckBox that optionally enables it.
   */
  private static class OptionalTextBox extends Composite implements
      ClickHandler {

    private TextBox textBox = new TextBox();
    private CheckBox checkBox = new CheckBox();

    /**
     * Constructs an OptionalTextBox with the given caption on the check.
     * 
     * @param caption the caption to be displayed with the check box
     */
    public OptionalTextBox(String caption) {
      // Place the check above the text box using a vertical panel.
      VerticalPanel panel = new VerticalPanel();
      panel.add(checkBox);
      panel.add(textBox);

      // Set the check box's caption, and check it by default.
      checkBox.setText(caption);
      checkBox.setChecked(true);
      checkBox.addClickHandler(this);

      // All composites must call initWidget() in their constructors.
      initWidget(panel);

      // Give the overall composite a style name.
      setStyleName(&quot;example-OptionalCheckBox&quot;);
    }

    public void onClick(ClickEvent event) {
      Object sender = event.getSource();
      if (sender == checkBox) {
        // When the check box is clicked, update the text box's enabled state.
        textBox.setEnabled(checkBox.isChecked());
      }
    }

    /**
     * Sets the caption associated with the check box.
     * 
     * @param caption the check box's caption
     */
    public void setCaption(String caption) {
      // Note how we use the use composition of the contained widgets to provide
      // only the methods that we want to.
      checkBox.setText(caption);
    }

    /**
     * Gets the caption associated with the check box.
     * 
     * @return the check box's caption
     */
    public String getCaption() {
      return checkBox.getText();
    }
  }

  public void onModuleLoad() {
    // Create an optional text box and add it to the root panel.
    OptionalTextBox otb = new OptionalTextBox(&quot;Check this to enable me&quot;);
    RootPanel.get().add(otb);
  }
}
</pre>

<h2 id="new">From Scratch in Java Code</h2>

<p>It is also possible to create a widget from scratch, although it is trickier since you have to write code at a lower level. Many of the basic widgets are written this way, such
as <a href="/javadoc/latest/com/google/gwt/user/client/ui/Button.html">Button</a> and <a href="/javadoc/latest/com/google/gwt/user/client/ui/TextBox.html">TextBox</a>. Please refer to the implementations of these
widgets to understand how to create your own.</p>

<p>To understand how to create your own, refer to the implementations of these widgets in the com.google.gwt.user.client.ui package. The source code is in gwt-user.jar.</p>

<h2 id="javascript">Using JavaScript</h2>

<p>When implementing a custom widget that derives directly from the <a href="/javadoc/latest/com/google/gwt/user/client/ui/Widget.html">Widget</a> base class, you may also write some of
the widget's methods using JavaScript. This should generally only be done as a last resort, as it becomes necessary to consider the cross-browser implications
of the native methods that you write, and also becomes more difficult to debug. For an example of this pattern in practice, see the
<a href="/javadoc/latest/com/google/gwt/user/client/ui/TextBox.html">TextBox</a> widget and the underlying JavaScript implementation of some of its methods in the TextBoxImpl class.
You should use <a href="DevGuideCodingBasics.html#DevGuideDeferredBinding">deferred binding</a> to isolate browser specific code.</p>