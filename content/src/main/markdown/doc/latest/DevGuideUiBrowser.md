UiBrowser
===

GWT shields you from worrying too much about cross-browser incompatibilities. If you stick to built-in [widgets](DevGuideUiWidgets.html)
and [composites](DevGuideUiCustomWidgets.html), your applications will work similarly on the most recent versions of Internet Explorer,
Firefox, Chrome, and Safari. (Opera, too, most of the time.) HTML user interfaces are remarkably quirky, though, so make sure to test your applications thoroughly on every browser.

Whenever possible, GWT defers to browsers' native user interface elements. For example, GWT's [Button](/javadoc/latest/com/google/gwt/user/client/ui/Button.html) widget is a true HTML `<button>`
rather than a synthetic button-like widget built, say, from a `<div>`. That means that GWT buttons render appropriately in different browsers and on different client
operating systems. We like the native browser controls because they're fast, accessible, and most familiar to users.

When it comes to styling web applications, [CSS](http://www.w3.org/Style/CSS/) is ideal. So, instead of attempting to encapsulate UI styling behind a
wall of least-common-denominator APIs, GWT provides very few methods directly related to style. Rather, developers are encouraged to define styles in stylesheets that are linked
to application code using [style names](/javadoc/latest/com/google/gwt/user/client/ui/UIObject.html#setStyleName-java.lang.String-). In addition to cleanly separating style from application logic, this division of labor helps applications load and render more quickly, consume
less memory, and even makes them easier to tweak during edit/debug cycles since there's no need to recompile for style tweaks.

**Tip:** If you find a need to implement a browser specific dependency, you can use a [JSNI](DevGuideCodingBasics.html#DevGuideJavaScriptNativeInterface)
method to retrieve the browser' UserAgent string.

```java
public static native String getUserAgent() /*-{
     return navigator.userAgent.toLowerCase();
  }-*/;
```

