<p>GWT shields you from worrying too much about cross-browser incompatibilities. If you stick to built-in <a href="DevGuideUiWidgets.html">widgets</a>
and <a href="DevGuideUiCustomWidgets.html">composites</a>, your applications will work similarly on the most recent versions of Internet Explorer,
Firefox, Chrome, and Safari. (Opera, too, most of the time.) DHTML user interfaces are remarkably quirky, though, so make sure to test your applications thoroughly on every browser.</p>

<p>Whenever possible, GWT defers to browsers' native user interface elements. For example, GWT's <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/user/client/ui/Button.html">Button</a> widget is a true HTML <tt>&lt;button&gt;</tt>
rather than a synthetic button-like widget built, say, from a <tt>&lt;div&gt;</tt>. That means that GWT buttons render appropriately in different browsers and on different client
operating systems. We like the native browser controls because they're fast, accessible, and most familiar to users.</p>

<p>When it comes to styling web applications, <a href="http://www.w3.org/Style/CSS/">CSS</a> is ideal. So, instead of attempting to encapsulate UI styling behind a
wall of least-common-denominator APIs, GWT provides very few methods directly related to style. Rather, developers are encouraged to define styles in stylesheets that are linked
to application code using <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/user/client/ui/UIObject.html#setStyleName(java.lang.String)">style names</a>. In addition to cleanly separating style from application logic, this division of labor helps applications load and render more quickly, consume
less memory, and even makes them easier to tweak during edit/debug cycles since there's no need to recompile for style tweaks.</p>

<p class="note"><strong>Tip:</strong> If you find a need to implement a browser specific dependency, you can use a <a href="DevGuideCodingBasics.html#DevGuideJavaScriptNativeInterface">JSNI</a>
method to retrieve the browser' UserAgent string.</p>

<pre class="prettyprint">
  public static native String getUserAgent() /*-{
     return navigator.userAgent.toLowerCase();
  }-*/
</pre>

