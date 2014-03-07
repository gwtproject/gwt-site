<p>GWT user interface classes are similar to those in existing UI frameworks such as <a href="http://java.sun.com/javase/6/docs/api/javax/swing/package-summary.html">Swing</a> and <a href="http://www.eclipse.org/swt/">SWT</a> except that the widgets are rendered using dynamically-created HTML rather than pixel-oriented graphics.   </p>

<p>In traditional JavaScript programming, dynamic user interface creation is done by manipulating the browser's DOM. While GWT provides access to the browser's DOM directly using the <a href="/javadoc/latest/com/google/gwt/dom/client/package-summary.html">DOM package</a>, it is far easier to use classes from the <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/user/client/ui/Widget.html">Widget</a> hierarchy. The Widget classes make it easier to quickly build interfaces that will work correctly on all browsers. </p>

<style type="text/css">
   ol.toc li { font-weight: normal; }
   ol.toc li a { font-weight: bold; }
</style>

<ol class="toc" id="pageToc">
  <li><a href="DevGuideUiBrowser.html">Cross-Browser Support</a> &ndash; Use widgets and composites for cross-browser compatibility</li>
  <li><a href="DevGuideUiPanels.html">Layout Using Panels</a> &ndash; Explore the various panels available for layout</li>
  <li><a href="DevGuideUiWidgets.html">Widgets</a> &ndash; Create user controls with widgets </li>
  <li><a href="DevGuideUiCustomWidgets.html">Creating Custom Widgets</a> &ndash; Create new widgets, composite widgets, or native JavaScript widgets</li>
  <li><a href="DevGuideUiCellWidgets.html">Cell Widgets</a> <sup style="color: red; vertical-align: 2px; font-size: 85%;">New 2.1</sup> &ndash; Work with widgets, panels, the DOM, events, CSS, declarative UI and images.</li>
  <li><a href="DevGuideUiEditors.html">Editors</a>  <sup style="color: red; vertical-align: 2px; font-size: 85%">New 2.1</sup> &ndash; Allows data stored in an object graph to be mapped onto a graph of Editors.</li>
  <li><a href="DevGuideUiDom.html">Working with the DOM</a> &ndash; When necessary, manipulate the browser's DOM directly</li>
  <li><a href="DevGuideUiHandlers.html">Events and Handlers</a> &ndash; Handle events published by widgets </li>
  <li><a href="DevGuideUiCss.html">Working with CSS</a> &ndash; Style widgets with cascading style sheets</li>
  <li><a href="DevGuideUiBinder.html">Declarative UI with UiBinder</a> &ndash; Build widget and DOM structures from XML markup</li>
  <li><a href="DevGuideUiImageBundles.html">Bundling Image Resources</a> &ndash; Optimize image loading by reducing the number of HTTP requests for images</li>
</ol>