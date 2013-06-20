<p>Your application is sent across a network to a user where it runs as JavaScript inside their web browser. Everything that happens within the user's web browser is
referred to as <i>client-side processing</i>. When you write client-side code that is intended to run in the web browser, remember that it ultimately becomes JavaScript. Thus, it
is important to use only <a href="DevGuideCodingBasics.html#DevGuideJavaCompatibility">libraries and Java language constructs</a> that can be translated into JavaScript.</p>

<ol class="toc" id="pageToc">
  <li><a href="#creating">Creating an EntryPoint Class</a></li>
  <li><a href="#hello">Hello World Example</a></li>
</ol>

<h2 id="creating">Creating an EntryPoint Class</h2>

<p>To begin writing a GWT module, subclass the <a href="/javadoc/latest/com/google/gwt/core/client/EntryPoint.html">EntryPoint</a> class.</p>

<p><strong>Tip:</strong> GWT applicationCreator creates a starter application for you with a sample EntryPoint subclass defined.</p>

<pre class="prettyprint">
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
    GWT.log(&quot;Hello World!&quot;, null);
  }
}
</pre>

<h3>Writing the entry point method</h3>

<p>The entry point method is onModuleLoad(). It contains the code that executes when you launch the application. Typically, the types of things you do in the onModuleLoad() method
are:</p>

<ul>
<li>create new user interface components</li>

<li>set up <a href="DevGuideUiHandlers.html">handlers</a> for events</li>

<li>modify the browser DOM in some way</li>
</ul>



<p>
The example above logs a message to the development mode console.
If you try to run this example application in production mode, you won't see anything because the GWT.log() method is compiled away when the client-side code is translated into JavaScript.
</p>

<h2 id="hello">Hello World Example</h2>

<p>Included with the GWT distribution is a sample &quot;Hello World&quot;
program that looks like this when run in development mode:</p>

<p><img src="images/HelloWorld.png"/></p>

<pre class="prettyprint">
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
    Button b = new Button(&quot;Click me&quot;, new ClickHandler() {
      public void onClick(ClickEvent event) {
        Window.alert(&quot;Hello, AJAX&quot;);
      }
    });

    RootPanel.get().add(b);
  }
}
</pre>

<p>In the entry point method for the Hello World application, the following actions were taken:</p>

<ul>
<li>a new Button widget was created with the text &quot;Click me&quot;</li>

<li>a handler was created to respond to the user clicking the button</li>

<li>the handler pops up an Alert dialog</li>

<li>the button is added to the <a href="/javadoc/latest/com/google/gwt/user/client/ui/RootPanel.html">Root panel</a></li>
</ul>


