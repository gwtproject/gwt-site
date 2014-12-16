UI
===

GWT user interface classes are similar to those in existing UI frameworks such as [Swing](http://java.sun.com/javase/6/docs/api/javax/swing/package-summary.html) and [SWT](http://www.eclipse.org/swt/) except that the widgets are rendered using dynamically-created HTML rather than pixel-oriented graphics.   

In traditional JavaScript programming, dynamic user interface creation is done by manipulating the browser's DOM. While GWT provides access to the browser's DOM directly using the [DOM package](/javadoc/latest/com/google/gwt/dom/client/package-summary.html), it is far easier to use classes from the [Widget](/javadoc/latest/com/google/gwt/user/client/ui/Widget.html) hierarchy. The Widget classes make it easier to quickly build interfaces that will work correctly on all browsers. 

1.  [Cross-Browser Support](DevGuideUiBrowser.html) -- Use widgets and composites for cross-browser compatibility
2.  [Layout Using Panels](DevGuideUiPanels.html) -- Explore the various panels available for layout
3.  [Widgets](DevGuideUiWidgets.html) -- Create user controls with widgets4.  [Creating Custom Widgets](DevGuideUiCustomWidgets.html) -- Create new widgets, composite widgets, or native JavaScript widgets
5.  [Cell Widgets](DevGuideUiCellWidgets.html) <sup style="color: red; vertical-align: 2px; font-size: 85%;">New 2.1</sup> -- Work with widgets, panels, the DOM, events, CSS, declarative UI and images.
6.  [Editors](DevGuideUiEditors.html)  <sup style="color: red; vertical-align: 2px; font-size: 85%">New 2.1</sup> -- Allows data stored in an object graph to be mapped onto a graph of Editors.
7.  [Working with the DOM](DevGuideUiDom.html) -- When necessary, manipulate the browser's DOM directly
8.  [Events and Handlers](DevGuideUiHandlers.html) -- Handle events published by widgets9.  [Working with CSS](DevGuideUiCss.html) -- Style widgets with cascading style sheets
10.  [Declarative UI with UiBinder](DevGuideUiBinder.html) -- Build widget and DOM structures from XML markup
11.  [Bundling Image Resources](DevGuideUiImageBundles.html) -- Optimize image loading by reducing the number of HTTP requests for images
