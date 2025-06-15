Client
===

Your application is sent across a network to a user where it runs as JavaScript inside their web browser. Everything that happens within the user's web browser is
referred to as _client-side processing_. When you write client-side code that is intended to run in the web browser, remember that it ultimately becomes JavaScript. Thus, it
is important to use only [libraries and Java language constructs](DevGuideCodingBasics.html#DevGuideJavaCompatibility) that can be translated into JavaScript.

1.  [Creating an EntryPoint Class](#creating)
2.  [Hello World Example](#hello)

## Creating an EntryPoint Class<a id="creating"></a>

To begin writing a GWT module, subclass the [EntryPoint](/javadoc/latest/com/google/gwt/core/client/EntryPoint.html) class.

**Tip:** GWT applicationCreator creates a starter application for you with a sample EntryPoint subclass defined.

```java
package com.example.foo.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

/**
 * Entry point classes define onModuleLoad().
 */
public class Foo implements EntryPoint {

  /**
   * This is the entry point method. Initialize you GWT module here.
   */
  public void onModuleLoad() {

    // Writes Hello World to the module log window.
    GWT.log("Hello World!", null);
  }
}
```

### Writing the entry point method

The entry point method is onModuleLoad(). It contains the code that executes when you launch the application. Typically, the types of things you do in the onModuleLoad() method
are:

*   create new user interface components
*   set up [handlers](DevGuideUiHandlers.html) for events
*   modify the browser DOM in some way

The example above logs a message to the development mode console.
If you try to run this example application in production mode, you won't see anything because the GWT.log() method is compiled away when the client-side code is translated into JavaScript.

## Hello World Example<a id="hello"></a>

Included with the GWT distribution is a sample "Hello World"
program that looks like this when run in development mode:

![img](images/HelloWorld.png)

```java
package com.google.gwt.sample.hello.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Hello World application.
 */
public class Hello implements EntryPoint {

  public void onModuleLoad() {
    Button b = new Button("Click me", new ClickHandler() {
      public void onClick(ClickEvent event) {
        Window.alert("Hello, AJAX");
      }
    });

    RootPanel.get().add(b);
  }
}
```

In the entry point method for the Hello World application, the following actions were taken:

*   a new Button widget was created with the text "Click me"
*   a handler was created to respond to the user clicking the button
*   the handler pops up an Alert dialog
*   the button is added to the [Root panel](/javadoc/latest/com/google/gwt/user/client/ui/RootPanel.html)
