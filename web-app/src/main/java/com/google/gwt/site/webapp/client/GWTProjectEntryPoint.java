/*
 * Copyright 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.gwt.site.webapp.client;

import static elemental2.dom.DomGlobal.*;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.site.webapp.client.highlight.JsHighlight;
import com.google.gwt.user.client.Window;
import elemental2.core.JsArray;
import elemental2.core.JsWeakMap;
import elemental2.dom.*;
import jsinterop.annotations.JsFunction;
import jsinterop.base.JsArrayLike;

public class GWTProjectEntryPoint implements EntryPoint {

  private static final int ANIMATION_TIME = 200;

  // Visible for testing
  // The absolute path to the url root (http://gwtproject.com)
  static String origin = GWT.getModuleBaseForStaticFiles()
      .replaceFirst("^(\\w+://.+?)/.*", "$1").toLowerCase();

  // Visible for testing
  // We discard links with different origin, hashes, starting with protocol, javadocs,
  // and media links.
  static final RegExp isSameOriginRexp = RegExp.compile("^" + origin
                                                        + "|^(?!(#|[a-z#]+:))(?!.*(|/)javadoc/)(?!.*\\.(jpe?g|png|mpe?g|mp[34]|avi)$)",
                                                        "i");

  private static boolean ajaxEnabled = origin.startsWith("http");
  private static String currentPage = Window.Location.getPath();
  private HandlerRegistration resizeHandler;
  private EventListener homeScrollHandler;
  private final JsWeakMap<Element, EventListener> clickHandlers = new JsWeakMap<>();

  private void slideToggle(HTMLElement el, int animationTime, boolean open) {
    if (open) {
      slideDown(el, animationTime);
    } else {
      slideUp(el, animationTime);
    }
  }

  private void slideDown(HTMLElement el, int animationTime) {
    el.style.display = "";
    animateMaxHeight(el, 0, estimateHeight(el), animationTime);
  }

  private void slideUp(HTMLElement el, int animationTime) {
    animateMaxHeight(el, estimateHeight(el), 0, animationTime);
  }

  private int estimateHeight(HTMLElement el) {
    // applied as max-height, so overestimating only makes the animation
    // lag slightly towards the start/end
    return el.childElementCount * 33;
  }

  private void animateMaxHeight(HTMLElement el, int from, int to, int animationTime) {
    if (animationTime == 0) {
      el.style.setProperty("display", to == 0 ? "none" : "");
      return;
    }
    el.style.setProperty("max-height", from + "px");
    el.style.setProperty("overflow", "hidden");
    el.style.setProperty("transition", "max-height " + animationTime + "ms ease-in-out");
    if (!el.dataset.has("transitionListener")) {
      el.addEventListener("transitionend", evt -> {
        String maxHeight = el.style.removeProperty("max-height");
        el.style.removeProperty("overflow");
        if ("0px".equals(maxHeight)) {
          el.style.setProperty("display", "none");
        }
      });
      el.dataset.set("transitionListener", "true");
    }
    Scheduler.get().scheduleDeferred(() -> el.style.setProperty("max-height", to + "px"));
  }

  @Override
  public void onModuleLoad() {
    bindSearch();

    enhancePage();
    enhanceMenu();

    onPageLoaded(false);
    querySelector(".holder").style.display = "";
  }

  private HTMLElement querySelector(String selector) {
    return (HTMLElement) document.querySelector(selector);
  }

  private void highLight() {
    JsHighlight.INSTANCE.initialize();
    forEach("pre > code", JsHighlight.INSTANCE::highlightBlock);
  }

  private void bindSearch() {
    HTMLElement form = querySelector("#search form");
    form.addEventListener("submit", (evt) -> {
      HTMLInputElement input = (HTMLInputElement) form.querySelector("input");
      doSearch(input.value);
      evt.preventDefault();
    });
  }

  private void enhanceMenu() {
    HTMLElement nav = querySelector("#nav");
    nav.addEventListener("mouseenter", evt -> {
      if (!nav.classList.contains("alwaysOpen")) {
        nav.classList.remove("closed");
      }
    });
    nav.addEventListener("mouseleave", evt -> {
      if (!nav.classList.contains("alwaysOpen")) {
        nav.classList.add("closed");
      }
    });
  }

  /*
   * Open the branch and select the item corresponding to the current url.
   */
  private void openMenu() {
    // close all submenus
    // todo hide first anchor with css
    forEach("#submenu > nav > ul > li", el -> {
      el.style.display = "none";
      forEachChild(el, "a", child -> child.style.display = "none");
    });

    String path = Window.Location.getPath();
    JsArrayLike<? extends Element> matchingLinks = document
        .querySelectorAll("#submenu a[href='" + path + "']");
    HTMLElement selectedItem = (HTMLElement) JsArray.from(matchingLinks)
        .find((el, index, parent) -> !"none".equals(((HTMLElement) el).style.display));
    String mainNavigationHref;
    String mainTitle;
    if (selectedItem != null) {

      showBranch(selectedItem);

      JsArray<HTMLElement> liParents = parentMenuItems(selectedItem);

      HTMLElement subMenuItem = liParents.getAt(liParents.length - 1);

      subMenuItem.style.display = "";
      forEach("#submenu .active", el -> {
        if (liParents.indexOf(el) < 0) {
          el.classList.remove("active");
        }
      });
      liParents.push(selectedItem);
      liParents.forEach((el, index, array) -> {
        if (el != selectedItem.parentElement) {
          el.classList.add("active");
        }
        return null;
      });
      mainNavigationHref = subMenuItem.querySelector("a").getAttribute("href");
      mainTitle = subMenuItem.querySelector("a").textContent + " - " + selectedItem.textContent;
    } else {
      mainNavigationHref = Window.Location.getPath();
      mainTitle = isOverviewPage(mainNavigationHref) ? "Overview" : "Project";
    }

    forEach("#nav a.active", el -> el.classList.remove("active"));
    forEach("#nav a[href='" + mainNavigationHref + "']", el -> el.classList.add("active"));


    // Change the page title for easy bookmarking
    querySelector("title").textContent = "[GWT] " + mainTitle;

    boolean homePage = isHomePage(path);
    boolean overviewPage = isOverviewPage(path);
    querySelector("#nav").classList.toggle("alwaysOpen", homePage);
    querySelector("#content").classList.toggle("home", homePage);
    if (querySelector("#submenu") != null) {
      if (homePage || overviewPage) {
        querySelector("#submenu").style.display = "none";
      } else {
        querySelector("#submenu").style.display = "";
      }
    }

    maybeStyleHomepage();
  }

  private JsArray<HTMLElement> parentMenuItems(HTMLElement selectedItem) {
    JsArray<HTMLElement> ret = new JsArray<>();
    Element current = selectedItem;
    while (current != null) {
      if (current.tagName.equalsIgnoreCase("li")) {
        ret.push((HTMLElement) current);
      }
      if ("submenu".equals(current.id)) {
        break;
      }
      current = current.parentElement;
    }
    return ret;
  }

  /*
   * Enhance the page adding handlers and replacing relative by absolute urls
   */
  private void enhancePage() {
    HTMLElement nav = querySelector("#nav");
    nav.addEventListener("mouseenter", evt -> nav.classList.remove("closed"));
    nav.addEventListener("mouseleave", evt -> {
      if (!nav.classList.contains("alwaysOpen")) {
        nav.classList.add("closed");
      }
    });

    enhanceLinks();

    // Do not continue enhancing if Ajax is disabled
    if (!ajaxEnabled) {
      // Select current item from the URL info
      loadPage(null);
      return;
    }

    // Use Ajax instead of default behaviour
    document.body.addEventListener("click", evt -> {
      Element target = (Element) evt.target;
      if (target.closest("a") == null) {
        return;
      }
      if (shouldEnhanceLink(target)
          // Is it a normal click (not ctrl/cmd/shift/right/middle click) ?
          && handleAsClick((MouseEvent) evt)) {

        // In mobile, if menu is visible, close it
        forEach("#submenu.show", el -> el.classList.remove("show"));

        // Load the page using Ajax
        loadPage(target);
        evt.preventDefault();
      }
    });

    // Select the TOC item when URL changes
    window.addEventListener("popstate", evt -> loadPage(null));
  }


  private boolean handleAsClick(MouseEvent event) {
    int mouseButtons = event.button;
    boolean modifiers = event.altKey || event.ctrlKey || event.metaKey || event.shiftKey;
    boolean middle = mouseButtons == 4;
    boolean right = mouseButtons == 2;
    return !modifiers && !middle && !right;
  }

  private boolean shouldEnhanceLink(Element link) {
    return
        // Enhance only local links
        isSameOriginRexp.test(link.getAttribute("href"))
        // Do not load links that are marked as full page reload
        && !Boolean.parseBoolean(link.getAttribute("data-full-load"));
  }

  private void enhanceLinks() {
    // Replace relative paths in anchors by absolute ones
    // exclude all anchors in the conHighlight and collapse menutent area.
    // TODO could be done on server side

    forEach("a:not(#content a)", el -> {
      if (shouldEnhanceLink(el)) {
        // No need to make complicated things for computing
        // the absolute path: anchor.pathname is the way
        HTMLAnchorElement link = (HTMLAnchorElement) el;
        link.href = link.pathname;
      }
    });

    // We add a span with the +/- icon so as the click area is well defined
    // this span is not rendered in server side because it is only needed
    // for enhancing the page.
    JsArray<HTMLElement> parentItems = new JsArray<>();
    forEach("#submenu ul > li li", el -> {
      if (el.querySelector("ul") != null) {
        parentItems.push(el);
        el.parentElement.insertBefore(document.createElement("span"), el);
      }
    });
    String spanOrLocalLink = "span,a[href='#']";
    forEachChild(querySelector("#submenu"), spanOrLocalLink, el -> {
      el.removeEventListener("click", clickHandlers.get(el));
    });
    // Toggle the branch when clicking on the arrow or anchor without content
    parentItems.forEach((item, index, array) -> {
      forEachChild(item, spanOrLocalLink, element -> {
        EventListener eventListener = (evt) ->
            toggleMenu((HTMLElement) ((Element) evt.target).parentElement);
        element.addEventListener("click", eventListener);
        clickHandlers.set(element, eventListener);
      });
      item.classList.add("folder");
      if (!item.classList.contains("open")) {
        forEachChild(item, "ul",
            child -> slideUp(child, 0));
      }
      return null;
    });
  }

  private void toggleMenu(HTMLElement menu) {
    boolean open = menu.classList.toggle("open");
    forEachChild(menu, "ul",
        child -> slideToggle(child, ANIMATION_TIME, open));
  }

  private void showBranch(final HTMLElement item) {
    Scheduler.get().scheduleDeferred(() -> {
      Element parent = item.closest("li");
      while (parent != null) {
        parent.classList.add("open");
        forEachChild(parent, "ul",
            (htmlElement) -> slideDown(htmlElement, ajaxEnabled ? ANIMATION_TIME : 0));
        parent = parent.parentElement.closest("li");
      }
    });
  }

  /*
   * Change URL via pushState and load the page via Ajax.
   */
  private void loadPage(Element link) {
    String pageUrl = link == null ? null : ((HTMLAnchorElement) link).pathname;

    boolean shouldReplaceMenu = shouldReplaceMenu(pageUrl);
    if (!currentPage.equals(pageUrl)) {
      if (pageUrl != null) {
        // Preserve QueryString, useful for the gwt.codesvr parameter in dev-mode.
        pageUrl = pageUrl.replaceFirst("(#.*|)$", Window.Location.getQueryString() + "$1");
        // Set the page to load in the URL
        history.pushState(null, null, pageUrl);
      }

      pageUrl = Window.Location.getPath();
      if (!currentPage.equals(pageUrl)) {
        forEach("#spinner", el -> el.style.display = "");
        ajaxLoad(pageUrl, shouldReplaceMenu);
      } else {
        scrollToHash();
      }
      currentPage = pageUrl;
    }
  }

  private void ajaxLoad(String pageUrl, final boolean shouldReplaceMenu) {
    fetch(pageUrl).then(Response::text).then(responseText -> {
      HTMLElement content = (HTMLElement) document.createElement("div");
      content.innerHTML = responseText;

      if (shouldReplaceMenu) {
        document.querySelector("#submenu")
            .replaceWith(content.querySelector("#submenu"));
      }

      document.querySelector("#content").replaceWith(content.querySelector("#content"));

      onPageLoaded(shouldReplaceMenu);
      return null;
    });
  }

  private boolean shouldReplaceMenu(String pageUrl) {
    String path = Window.Location.getPath();

    boolean isHomeOrOverview = isHomePage(path) || isOverviewPage(path);

    return isHomeOrOverview && (!isHomePage(pageUrl) || !isOverviewPage(pageUrl));
  }

  private void onPageLoaded(boolean menuReplaced) {
    if (menuReplaced) {
      enhanceLinks();
    }
    document.documentElement.style.removeProperty("scroll-behavior");
    openMenu();
    scrollToHash();
    forEach("#spinner", spinner -> spinner.style.display = "none");
    HTMLElement editLink = querySelector("#editLink");
    HTMLElement heading = querySelector("#content h1");
    if (editLink != null && heading != null) {
      heading.appendChild(editLink);
    }
    // highlight loaded page
    highLight();
  }

  /*
   * Move the scroll to the hash fragment in the URL
   */
  private void scrollToHash() {
    String hash = Window.Location.getHash();
    Element anchor = hash.length() > 1 ? document.querySelector(
        hash + ", [name='" + hash.substring(1) + "']") : null;
    if (anchor == null) {
      Window.scrollTo(0, 0);
    } else {
      anchor.scrollIntoView();
    }
  }

  private void maybeStyleHomepage() {
    if (querySelector("#content").classList.contains("home")) {
      document.documentElement.style.setProperty("scroll-behavior", "smooth");
      styleHomepage();
      resizeHandler = Window.addResizeHandler(event -> styleHomepage());

      // Pager
      forEach(".next, .pager a", (element) -> element.addEventListener("click", event -> {
        event.preventDefault();
        document.documentElement.scrollTop =
           getElementOffset(document.querySelector(element.getAttribute("href")));
      }));

      forEach(".pager a", (element) -> element.addEventListener("click", event -> {
        forEach(".pager a", e -> e.classList.remove("active"));
        element.classList.add("active");
      }));
      if (homeScrollHandler == null) {
        homeScrollHandler = evt -> {
          forEach(".pager a", el -> el.classList.remove("active"));
          if (window.scrollY + 100 > getElementOffset(document.querySelector("#letsbegin"))) {
            querySelector(".pager a:nth-child(3)").classList.add("active");
          } else if (window.scrollY + 100 > getElementOffset(document.querySelector("#gwt"))) {
            querySelector(".pager a:nth-child(2)").classList.add("active");
          } else {
            querySelector(".pager a:nth-child(1)").classList.add("active");
          }
        };
      }
      window.addEventListener("scroll", homeScrollHandler);
    } else {
      window.removeEventListener("scroll", homeScrollHandler);
      if (resizeHandler != null) {
        resizeHandler.removeHandler();
        resizeHandler = null;
      }
    }
  }

  private int getElementOffset(Element element) {
    return ((HTMLElement) element).offsetTop;
  }

  private boolean isOverviewPage(String path) {
    return "/overview.html".compareToIgnoreCase(path) == 0;
  }

  private boolean isHomePage(String path) {
    return "/".equals(path);
  }

  private void styleHomepage() {
    final int windowHeight = window.innerHeight;
    int sectionHeight = ((HTMLElement) document.querySelector("#letsbegin")).offsetHeight;

    if (windowHeight > sectionHeight) {
      forEach(".home section", element -> {
        element.style.setProperty("height", windowHeight + "px");
        element.style.setProperty("padding", "0");
        HTMLElement container = (HTMLElement) element.querySelector(".container");
        container.style.setProperty("padding-top",
            (windowHeight - container.offsetHeight) / 2 + "px");
      });
    }
  }

  private void forEach(String selector, ElementConsumer consumer) {
    document.querySelectorAll(selector).forEach((element, index, parent) -> {
      consumer.accept((HTMLElement) element);
      return null;
    });
  }

  private void forEachChild(Element parent, String selector, ElementConsumer consumer) {
    parent.childNodes.forEach((element, index, p) -> {
      if (element instanceof HTMLElement) {
        if (((HTMLElement) element).matches(selector)) {
          consumer.accept((HTMLElement) element);
        }
      }
      return null;
    });
  }

  private native void doSearch(String value) /*-{
      var element = $wnd.google.search.cse.element.getElement('searchresults');
      element.execute(value);
  }-*/;

  @JsFunction
  private interface ElementConsumer {
    void accept(HTMLElement element);
  }
}
