DOM Memory Leaks
===

_Joel Webber, GWT Team_

_Updated January 2009_

You may ask yourself, "Why do I have to use bitfields to sink DOM events?", and you may ask yourself, "Why can I not add event listeners directly to elements?" If you find yourself asking these questions, it's probably time to dig into the murky depths of DOM events and memory leaks.

If you're creating a widget from scratch (using DOM elements directly, as opposed to simply creating a composite widget), the setup for event handling generally looks something like this:

```java
class MyWidget extends Widget {
  public MyWidget() {
    setElement(DOM.createDiv());
    sinkEvents(Event.ONCLICK);
  }

  public void onBrowserEvent(Event evt) {
    switch (DOM.eventGetType(evt)) {
      case Event.ONCLICK:
        // Do something insightful.
        break;
    }
  }
}
```

This may seem a bit obtuse, but there's a good reason for it. To understand this, you may first need to brush up on browser memory leaks. There are some good resources on the web:

*   [http://www.quirksmode.org/blog/archives/2005/02/javascript_memo.html](http://www.quirksmode.org/blog/archives/2005/02/javascript_memo.html)
*   [http://www.ibm.com/developerworks/library/wa-memleak/](http://www.ibm.com/developerworks/library/wa-memleak/)

The upshot of all this is that in some browsers, any reference cycle that involves a JavaScript object and a DOM element (or other native object) has a nasty tendency to never get garbage-collected. The reason this is so insidious is that this is an extremely common pattern to create in JavaScript UI libraries.

Imagine the following (raw JavaScript) example:

```javascript
function makeWidget() {
  var widget = {};
  widget.someVariable = "foo";
  widget.elem = document.createElement ('div');
  widget.elem.onclick = function() {
    alert(widget.someVariable);
  };
}
```

Now, I'm not suggesting that you'd really build a JavaScript library quite this way, but it serves to illustrate the point. The reference cycle created here is:

```text
widget -> elem(native) -> closure -> widget

```

There are many different ways to run into the same problem, but they all tend to form a cycle that looks something like this. This cycle will never get broken unless you do it manually (often by clearing the `onclick` handler).

There are a number of ways developers try to deal with this issue. One of the more common is to walk the DOM when `window.onunload` is fired, clearing out all event listeners. This is problematic for two reasons:

*   It doesn't clear events on elements that are no longer in the DOM.
*   It doesn't deal with long-running applications, which are becoming more and more common.

## GWT's Solution

When designing GWT, we decided that leaks were simply unacceptable. You wouldn't tolerate egregious memory leaks in a desktop application, and a browser application should be no different. This raises some interesting problems, though. In order to avoid ever creating leaks, any widget that **might** need to get garbage collected must not be involved in a reference cycle with a native element. There's no way to find out "when a widget would have been collected had it not been involved in a reference cycle". So in GWT terms, a widget must not be involved in a cycle when it is detached from the DOM.

How do we enforce this? Each widget has a single "root" element. Whenever the widget becomes attached, we create exactly one "back reference" from the element to the widget (that is, `elem.__listener = widget`, performed in [DOM.setEventListener()](/javadoc/latest/com/google/gwt/user/client/DOM.html#setEventListener-com.google.gwt.user.client.Element-com.google.gwt.user.client.EventListener-)). This is set whenever the widget is attached, and cleared whenever it is detached.

Which brings is back to that odd bitfield used in the sinkEvents() method. If you look at the implementation of [DOM.sinkEvents()](/javadoc/latest/com/google/gwt/user/client/DOM.html#sinkEvents-com.google.gwt.user.client.Element-int-), you'll see that it does something like this:

```javascript
elem.onclick = (bits & 0x00001) ? $wnd.__dispatchEvent : null;

```

Each element's events point back to a central dispatch function, which looks for the target element's `__listener` expando, in order to call `onBrowserEvent()`. The beauty of this is that it allows us to set and clear a single expando to clean up any potential event leaks.

What this means in practice is that, as long as you don't set up any reference cycles on your own using JSNI, you can't write an application in GWT that will leak. We test carefully with every release to make sure we haven't done anything in the low-level code to introduce new leaks as well.

The downside, of course, is that you can't hook event listeners directly to elements that are children of a widget's element. Rather, you have to receive the event on the widget itself, and figure out which child element it came from.

But that's better than leaking gobs of memory on your users' machines, right?
