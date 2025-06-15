package com.google.gwt.site.webapp.client.highlight;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.TextResource;
import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLElement;
import jsinterop.base.Js;

public class JsHighlight {

  interface CssHighlight extends ClientBundle  {
    CssHighlight INSTANCE =  GWT.create(CssHighlight.class);

    @Source("idea.css")
    CssResource idea();

    @Source("highlight.pack.js")
    TextResource highlight();

  }

  private static boolean initialized;
  public static final JsHighlight INSTANCE = GWT.create(JsHighlight.class);


  public void initialize() {
    if (!initialized) {
      initialized = true;
      ScriptInjector.fromString(CssHighlight.INSTANCE.highlight().getText())
          .setWindow(Js.uncheckedCast(DomGlobal.window)).inject();
      CssHighlight.INSTANCE.idea().ensureInjected();
    }
  }

  public native void highlightBlock(HTMLElement e) /*-{
    e && $wnd.hljs && $wnd.hljs.highlightBlock(e);
  }-*/;
}
