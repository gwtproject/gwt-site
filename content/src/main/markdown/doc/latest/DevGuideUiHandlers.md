UiHandlers
===

Events in GWT use the _handler_ model similar to other user interface frameworks. A handler interface defines one or more methods that the widget calls to announce an
event. A class wishing to receive events of a particular type implements the associated handler interface and then passes a reference to itself to the widget to _subscribe_
to a set of events. The [Button](/javadoc/latest/com/google/gwt/user/client/ui/Button.html) class, for example,
publishes click events. The associated handler interface is [ClickHandler](/javadoc/latest/com/google/gwt/event/dom/client/ClickHandler.html).

The following example demonstrates how to add a custom ClickHandler subclass to an instance of a Button:

```java
public void anonClickHandlerExample() {
  Button b = new Button("Click Me");
  b.addClickHandler(new ClickHandler() {
    public void onClick(ClickEvent event) {
      // handle the click event
    }
  });
}
```

Using anonymous inner classes as in the above example can use excessive memory for a large number of widgets, since it results in the creation of many handler objects. Instead
of creating separate instances of the ClickHandler object for each widget that needs to be listened to, a single handler can be shared between many widgets. Widgets declare
themselves as the source of an event when they invoke a handler method, allowing a single handler to distinguish between multiple event publishers with an event object's
getSource() method. This makes better use of memory but requires slightly more code, as shown in the following example:

```java
public class HandlerExample extends Composite implements ClickHandler {
  private FlowPanel fp = new FlowPanel();
  private Button b1 = new Button("Button 1");
  private Button b2 = new Button("Button 2");

  public HandlerExample() {
    initWidget(fp);
    fp.add(b1);
    fp.add(b2);
    b1.addClickHandler(this);
    b2.addClickHandler(this);
  }

  public void onClick(ClickEvent event) {
    // note that in general, events can have sources that are not Widgets.
    Widget sender = (Widget) event.getSource();

    if (sender == b1) {
      // handle b1 being clicked
    } else if (sender == b2) {
      // handle b2 being clicked
    }
  }
}
```

