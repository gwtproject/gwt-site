<p>You construct user interfaces in GWT applications using <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/user/client/ui/Widget.html">widgets</a> that
are contained within <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/user/client/ui/Panel.html">panels</a>. Widgets allow you to interact with the user. Panels
control the placement of user interface elements on the page. Widgets and panels work the same way
on all browsers; by using them, you eliminate the need to write specialized code for each browser.</p>

<h2>Widgets</h2>

<p>Widgets define your applications input and output with the user. Examples of widgets include the following:</p>

<ul>
<li><a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/user/client/ui/Button.html">Button</a> A user clicks the mouse button to
activate the button.</li>

<li style="list-style: none">
<blockquote><img src="images/Button.png"/></blockquote>
</li>

<li><a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/user/client/ui/TextBox.html">TextBox</a> The application can display text and
the user can type in the text box.</li>

<li style="list-style: none">
<blockquote><img src="images/TextBox.png"/></blockquote>
</li>

<li><a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/user/client/ui/Tree.html">Tree</a> A collapsible hierarchy of widgets.</li>

<li style="list-style: none">
<blockquote><img src="images/Tree.png"/></blockquote>
</li>

<li><a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/user/client/ui/RichTextArea.html">RichTextArea</a> A text editor that allows
users to apply rich formatting of the text.</li>

<li style="list-style: none">
<blockquote><img src="images/RichTextArea.png"/></blockquote>
</li>
</ul>

<p>
For the complete list of GWT UI elements, see <a href="RefWidgetGallery.html">Widget Gallery</a>.
</p>

<p>You are not limited to the set of widgets provided by GWT. There are a number of ways to <a href="DevGuideUiCustomWidgets.html">create custom
widgets</a>:</p>

<ul>
<li>You can bundle together existing widgets and create a <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/user/client/ui/Composite.html">Composite</a> widget.</li>

<li>You can write GWT bindings to an existing JavaScript widget.</li>

<li>You can create your own widget from scratch using either Java or JavaScript.</li>
</ul>

<p>You can also use one or more of the many third party widget libraries written for GWT.</p>


<h2>Styles</h2>

<p>Visual styles are applied to widgets using <a href="DevGuideUiCss.html">Cascading Style Sheets (CSS)</a>. Besides the default browser supplied
definitions, each GWT widget and panel has pre-defined style sheet class definitions documented in the class reference documentation.</p>

<h2>See Also</h2>

<ul>
<li><a href="DevGuideUiCustomWidgets.html">Creating Custom Widgets</a> Discussion of how to create your own widgets in GWT.</li>
</ul>

<ul>
<li><a href="DevGuideUiPanels.html">Layout Using Panels</a> Examples of how to use panels.</li>
</ul>