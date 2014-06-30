Like most web applications, GWT applications use cascading style sheets (CSS) for visual styling.
<ul>
  <li><a href="#widgets">Styling Existing Widgets</a></li>
  <li><a href="#complex">Complex Styles</a></li>
  <li><a href="#cssfiles">Associating CSS Files</a></li>
  <li><a href="#themes">GWT Visual Themes</a></li>
  <li><a href="#documentation">Documentation</a></li>
</ul>

<h2 id="widgets">Styling Existing Widgets</h2>

<p>GWT widgets rely on cascading style sheets (CSS) for visual styling.</p>

<p>In GWT, each class of widget has an associated style name that binds it to a CSS rule. Furthermore, you can assign an id to a particular component to create a CSS rule that
applies just to that one component. By default, the class name for each component is <tt>gwt-&lt;classname&gt;</tt>. For example, the <a href="/javadoc/latest/com/google/gwt/user/client/ui/Button.html">Button widget</a> has a default style of
<tt>gwt-Button</tt>.</p>

<p>In order to give all buttons a larger font, you could put the following rule in your application's CSS file:</p>

<pre>
  .gwt-Button { font-size: 150%; }
</pre>

<p>All of the widgets created with the GWT toolkit will have a default class name, but a widget's style name can be set using <a href="/javadoc/latest/com/google/gwt/user/client/ui/UIObject.html#setStyleName(java.lang.String)">setStyleName()</a>.
Static elements can have their class set in the HTML source code for your application.</p>

<p>Another way to use style sheets is to refer to a single widget. For that, you would need to know the value of the <tt>id</tt> attribute for the widget or DOM element.</p>

<p>By default, neither the browser nor GWT creates default <tt>id</tt> attributes for widgets. You must explicitly create an <tt>id</tt> for the elements you want to refer to in
this manner, and you must insure that each &quot;id&quot; value is unique. A common way to do this is to set them on static elements in your <a href="DevGuideOrganizingProjects.html#DevGuideHostPage">HTML host page</a></p>

<pre class="prettyprint">
  &lt;div id=&quot;my-button-id&quot;/&gt;
</pre>

<p>To set the id for a GWT widget, retrieve its DOM Element and then set the <tt>id</tt> attribute as follows:</p>

<pre class="prettyprint">
  Button b = new Button();
  DOM.setElementAttribute(b.getElement(), &quot;id&quot;, &quot;my-button-id&quot;)
</pre>

<p>This would allow you to reference a specific widget in a style sheet as follows:</p>

<pre>
  #my-button-id { font-size: 100%; }
</pre>

<h2 id="complex">Complex Styles</h2>

<p>Some widgets have multiple styles associated with them. <a href="/javadoc/latest/com/google/gwt/user/client/ui/MenuBar.html">MenuBar</a>, for example, has the following styles:</p>

<pre>
   .gwt-MenuBar { 
       /* properties applying to the menu bar itself */ 
   }
   .gwt-MenuBar .gwt-MenuItem { 
       /* properties applying to the menu bar's menu items */ 
   }
   .gwt-MenuBar .gwt-MenuItem-selected { 
       /* properties applying to the menu bar's selected menu items */
   }
 </pre>

<p>In the above style sheet code, there are two style rules that apply to menu items. The first applies to all menu items (both selected and unselected), while the second (with
the -selected suffix) applies only to selected menu items. A selected menu item's style name will be set to <tt>&quot;gwt-MenuItem gwt-MenuItem-selected&quot;</tt>, specifying that both
style rules will be applied. The most common way of doing this is to use <a href="/javadoc/latest/com/google/gwt/user/client/ui/UIObject.html#setStyleName(java.lang.String)">setStyleName</a> to set
the base style name, then <a href="/javadoc/latest/com/google/gwt/user/client/ui/UIObject.html#addStyleName(java.lang.String)">addStyleName()</a> and <a href="/javadoc/latest/com/google/gwt/user/client/ui/UIObject.html#removeStyleName(java.lang.String)">removeStyleName()</a>
to add and remove the second style name.</p>

<h2 id="cssfiles">Associating CSS Files</h2>

<p>There are multiple approaches for associating CSS files with your module:</p>

<ul>
<li>Using a <tt>&lt;link&gt;</tt> tag in the <a href="DevGuideOrganizingProjects.html#DevGuideHostPage">host HTML page</a>.</li>

<li>Using the <tt>&lt;stylesheet&gt;</tt> element in the <a href="DevGuideOrganizingProjects.html#DevGuideModuleXml">module XML file</a>.</li>
<li>Using a <tt><a href="DevGuideClientBundle.html#CssResource">CssResource</a></tt> contained within a <tt><a href="DevGuideClientBundle.html">ClientBundle</a></tt>.</li>
<li>Using an inline <tt><a href="DevGuideUiBinder.html#Hello_Stylish_World">&lt;ui:style&gt;</a></tt> element in a UiBinder template.</li>
</ul>

<p>Modern GWT applications typically use a combination of <tt>CssResource</tt> and UiBinder. Older applications should use only one of the first two choices.</p>

<h3>Including Style sheets in the HTML Host Page (Deprecated)</h3>

<p>Typically, style sheets are placed in a package that is part of your module's <a href="DevGuideOrganizingProjects.html#DevGuideModules">public path</a>. All you need to do to reference
them is simply include a <tt>&lt;link&gt;</tt> to the style sheet in your <a href="DevGuideOrganizingProjects.html#DevGuideHostPage">host page</a>, such as:</p>

<pre class="prettyprint">
  &lt;link rel=&quot;stylesheet&quot; href=&quot;mystyles.css&quot; type=&quot;text/css&quot;/&gt;
</pre>

<h3>Including Style sheets in the Module XML file (Deprecated)</h3>

<p>Another way to include your style sheet within your module is to use the <tt>&lt;stylesheet&gt;</tt> element in your <a href="DevGuideOrganizingProjects.html#DevGuideModuleXml">module
XML file</a>. This uses <a href="DevGuideOrganizingProjects.html#DevGuideAutomaticResourceInclusion">automatic resource inclusion</a> to bundle the <tt>.css</tt> file with your
module.</p>

<p>The difference between using a <tt>&lt;link&gt;</tt> tag in HTML and the <tt>&lt;stylesheet&gt;</tt> element in your module XML file is that with the mdoule XML file approach,
the style sheet will always follow your module, no matter which host HTML page you deploy it from.</p>

<p>Why does this matter? Because if you create and share a module, it does not include a host page and therefore, you cannot guarantee the style sheet's availability. Automatic
Resource Inclusion solves this problem. If you do not care about sharing or re-using your module then you can just use the standard HTML link rel stuff in the host page.</p>

<p class="note"><strong>Tip:</strong> Use a unique name for the .css file with included resources to avoid collisions. If you automatically include &quot;styles.css&quot; and share your module and someone
puts it on a page that already has &quot;styles.css&quot; there will be problems.</p>

<h2 id="themes">GWT Visual Themes</h2>

<p>GWT comes with three default visual themes that you can choose from: standard, chrome, and dark. The standard theme uses subtle shades of blue to create an lively user
interface. The chrome theme uses gray scale backgrounds for a refined, professional look. The dark theme uses dark shades of gray and black with bright indigo highlights for a
bold, eye catching experience. When you inherit a visual theme, almost all widgets will have some default styles associated with them. The visual themes allow you to focus more
time on application development and less time on styling your application.</p>

<p>By default, new GWT applications use the standard theme, but you can select any one of the themes mentioned above. Open your module XML file (gwt.xml) and uncomment the line
that inherits the theme of your choice.</p>

<pre class="prettyprint">
&lt;!-- Inherit the default GWT style sheet. You can change       --&gt;
&lt;!-- the theme of your GWT application by uncommenting          --&gt;
&lt;!-- any one of the following lines.                           --&gt;
&lt;!-- &lt;inherits name='com.google.gwt.user.theme.standard.Standard'/&gt; --&gt;
&lt;!-- &lt;inherits name=&quot;com.google.gwt.user.theme.chrome.Chrome&quot;/&gt; --&gt;
&lt;inherits name=&quot;com.google.gwt.user.theme.dark.Dark&quot;/&gt;
</pre>

<p>GWT visual themes also come in RTL (right-to-left) versions if you are designing a website for a language that is written right-to-left, such as Arabic. You can include the RTL
version by adding RTL to the end of the module name:</p>

<pre class="prettyprint">
&lt;inherits name=&quot;com.google.gwt.user.theme.dark.DarkRTL&quot;/&gt;
</pre>

<h3>Bandwidth Sensitive Applications</h3>

<p>If you are program a bandwidth sensitive application, such as a phone application, you may not want to require that users download the entire style sheet associated with your
favorite theme (about 27k). Alternatively, you can create your own stripped down version of the style sheet that only defines the styles applicable to your application. To do
this, first include the public resources associated with one of the themes by adding the following line to your <tt>gwt.xml</tt> file:</p>

<pre class="prettyprint">
&lt;inherits name='com.google.gwt.user.theme.standard.StandardResources'/&gt;
</pre>

<p>Each theme has a &quot;Resources&quot; version that only includes the public resources associated with the theme, but does not inject a style sheet into the page. You will need to create
a new style sheet and inject it into the page as described in the sections above.</p>

<p>Finally, copy the contents of the file <tt>public/gwt/standard/standard.css</tt> style sheet located in the package <tt>com.google.gwt.user.theme.standard</tt> into your new
style sheet. Strip out any styles you do not want to include, reducing the size of the file. When you run your application, GWT will inject your stripped down version of the style
sheet, but you can still reference the files associate with the standard visual theme.</p>

<h2 id="documentation">Documentation</h2>

<p>It is standard practice to document the relevant CSS style names for each widget class as part of its documentation comment. For a simple example, see <a href="/javadoc/latest/com/google/gwt/user/client/ui/Button.html">Button</a>. For a more complex example, see <a href="/javadoc/latest/com/google/gwt/user/client/ui/MenuBar.html">MenuBar</a>.</p>