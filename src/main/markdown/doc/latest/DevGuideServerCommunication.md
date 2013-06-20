<p>At some point, most GWT applications will need to interact with a backend server. GWT provides a couple of different ways to communicate with a server via HTTP. You can use the
<a href="DevGuideServerCommunication.html#DevGuideRemoteProcedureCalls">GWT RPC</a> framework to transparently make calls to Java servlets and let GWT take care of low-level details like
object serialization. Alternatively, you can use GWT's <a href="/javadoc/latest/com/google/gwt/http/client/package-summary.html">HTTP client classes</a> to build and send custom HTTP requests.</p>

<p class="note" style="margin-left: 1.5em; margin-right: 1.5em;">
<b>Note:</b> To run through the steps to communicate with a server in a sample GWT application, see the tutorial <a href="tutorial/clientserver.html">Communicating with the server</a>.
</p>

<ol class="toc" id="pageToc">
  <li><a href="#DevGuideServerSide">Server-side Code</a></li>
  <li><a href="#DevGuideRemoteProcedureCalls">Remote Procedure Calls</a></li>
  <li><a href="#DevGuidePlumbingDiagram">RPC Plumbing Diagram</a></li>
  <li><a href="#DevGuideCreatingServices">Creating Services</a></li>
  <li><a href="#DevGuideImplementingServices">Implementing Services</a></li>
  <li><a href="#DevGuideMakingACall">Actually Making a Call</a></li>
  <li><a href="#DevGuideSerializableTypes">Serializable Types</a></li>
  <li><a href="#DevGuideCustomSerialization">Customizing Serialization</a></li>
  <li><a href="#DevGuideHandlingExceptions">Handling Exceptions</a></li>
  <li><a href="#DevGuideArchitecturalPerspectives">Architectural Perspectives</a></li>
  <li><a href="#DevGuideRPCDeployment">Deploying RPC</a></li>
  <li><a href="#DevGuideHttpRequests">Making HTTP requests</a></li>
  <li><a href="#DevGuideGettingUsedToAsyncCalls">Getting Used to Asynchronous Calls</a></li>
  <li><a href="#DevGuideDeRPC">Direct-Eval RPC</a></li>
</ol>

<p>See also the <a href="DevGuideRequestFactory.html">Request Factory</a>
documentation.</p>


<h2 id="DevGuideServerSide">Server-side Code</h2>

<p>Everything that happens within your web server is referred to as <i>server-side</i> processing. When your application running in the user's browser needs to interact with your
server (for example, to load or save data), it makes an HTTP request across the network using a <a href="DevGuideServerCommunication.html#DevGuideRemoteProcedureCalls">remote procedure
call (RPC)</a>. While processing an RPC, your server is executing server-side code.</p>

<p>GWT provides an RPC mechanism based on Java Servlets to provide access to server side resources. This mechanism includes generation of efficent client and server side code to
<a href="DevGuideServerCommunication.html#DevGuideSerializableTypes">serialize</a> objects across the network using <a href="DevGuideCodingBasics.html#DevGuideDeferredBinding">deferred
binding</a>.</p>

<p class="note"><strong>Tip:</strong> Although GWT translates Java into JavaScript for client-side code, GWT does not meddle with your ability to run Java bytecode on your server whatsoever.
Server-side code doesn't need to be translatable, so you're free to use any Java library you find useful.</p>

<p>GWT does not limit you to this one RPC mechanism or server side development environment. You are free to integrate with other RPC mechanisms, such as JSON using the GWT
supplied <a href="/javadoc/latest/com/google/gwt/http/client/RequestBuilder.html">RequestBuilder</a> class, <a href="DevGuideCodingBasics.html#DevGuideJavaScriptNativeInterface">JSNI</a> methods or a third party library.</p>


<h2 id="DevGuideRemoteProcedureCalls">Remote Procedure Calls</h2>

<p>A fundamental difference between AJAX applications and traditional HTML web applications is that AJAX applications do not need to fetch new HTML pages while they execute.
Because AJAX pages actually run more like applications within the browser, there is no need to request new HTML from the server to make user interface updates. However, like all
client/server applications, AJAX applications usually <i>do</i> need to fetch data from the server as they execute. The mechanism for interacting with a server across a network is
called making a remote procedure call (RPC), also sometimes referred to as a <i>server call</i>. GWT RPC makes it easy for the client and server to pass Java objects back and
forth over HTTP. When used properly, RPCs give you the opportunity to move all of your UI logic to the client, resulting in greatly improved performance, reduced bandwidth,
reduced web server load, and a pleasantly fluid user experience.</p>

<p>The <a href="DevGuideServerCommunication.html#DevGuideServerSide">server-side</a> code that gets invoked from the client is often referred to as a <i>service</i>, so the act of making a
remote procedure call is sometimes referred to as invoking a service. To be clear, though, the term <i>service</i> in this context is not the same as the more general &quot;web
service&quot; concept. In particular, GWT services are not related to the Simple Object Access Protocol (SOAP).</p>


<h2 id="DevGuidePlumbingDiagram">RPC Plumbing Diagram</h2>

<p>This section outlines the moving parts required to invoke a service. Each service has a small family of helper interfaces and classes. Some of these classes, such as the
service proxy, are automatically generated behind the scenes and you generally will never realize they exist. The pattern for helper classes is identical for every service that
you implement, so it is a good idea to spend a few moments to familiarize yourself with the terminology and purpose of each layer in server call processing. If you are familiar
with traditional remote procedure call (RPC) mechanisms, you will recognize most of this terminology already. <img src="images/AnatomyOfServices.png"/></p>


<h2 id="DevGuideCreatingServices">Creating Services</h2>

<p>In order to define your RPC interface, you need to:</p>

<ol>
<li>Define an interface for your service that extends <a href="/javadoc/latest/com/google/gwt/user/client/rpc/RemoteService.html">RemoteService</a> and lists all your RPC methods.</li>

<li>Define a class to implement the server-side code that extends <a href="/javadoc/latest/com/google/gwt/user/server/rpc/RemoteServiceServlet.html">RemoteServiceServlet</a> and implements the
interface you created above.</li>

<li>Define an asynchronous interface to your service to be called from the client-side code.</li>
</ol>

<h3>Synchronous Interface</h3>

<p>To begin developing a new service interface, create a <a href="DevGuideCodingBasics.html#DevGuideClientSide">client-side</a> Java interface that extends the <a href="/javadoc/latest/com/google/gwt/user/client/rpc/RemoteService.html">RemoteService</a> tag interface.</p>

<pre class="prettyprint">
package com.example.foo.client;

import com.google.gwt.user.client.rpc.RemoteService;

public interface MyService extends RemoteService {
  public String myMethod(String s);
}
</pre>

<p>This synchronous interface is the definitive version of your service's specification. Any implementation of this service on the <a href="DevGuideServerCommunication.html#DevGuideServerSide">server-side</a> must extend <a href="/javadoc/latest/com/google/gwt/user/server/rpc/RemoteServiceServlet.html">RemoteServiceServlet</a> and implement this
service interface.</p>

<pre class="prettyprint">
package com.example.foo.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.example.client.MyService;


public class MyServiceImpl extends RemoteServiceServlet implements
    MyService {

  public String myMethod(String s) {
    // Do something interesting with 's' here on the server.
    return s;
  }
}
</pre>

<p class="note"><strong>Tip:</strong> It is not possible to call this version of the RPC directly from the client. You must create an asynchronous interface to all your services as shown
below.</p>

<h3>Asynchronous Interfaces</h3>

<p>Before you can actually attempt to make a remote call from the client, you must create another client interface, an asynchronous one, based on your original service interface.
Continuing with the example above, create a new interface in the client subpackage:</p>

<pre class="prettyprint">
package com.example.foo.client;

interface MyServiceAsync {
  public void myMethod(String s, AsyncCallback&lt;String&gt; callback);
}
</pre>

<p>The nature of asynchronous method calls requires the caller to pass in a callback object that can be notified when an asynchronous call completes, since by definition the
caller cannot be blocked until the call completes. For the same reason, asynchronous methods do not have return types; they generally return void. Should you wish to have more
control over the state of a pending request, return <a href="/javadoc/latest/com/google/gwt/http/client/Request.html">Request</a> instead. After an asynchronous call is made, all communication back to the caller is via the passed-in callback object.</p>

<h3>Naming Standards</h3>

<p>Note the use of the suffix <tt>Async</tt> and argument referencing the <tt>AsyncCallback</tt> class in the examples above. The relationship between a service interface and its
asynchronous counterpart must follow certain naming standards. The GWT compiler depends on these naming standards in order to generate the proper code to implement RPC.</p>

<ul>
<li>A service interface must have a corresponding asynchronous interface with the same package and name with the Async suffix appended. For example, if a service interface is
named <tt>com.example.cal.client.SpellingService</tt>, then the asynchronous interface must be called <tt>com.example.cal.client.SpellingServiceAsync</tt>.</li>

<li>Each method in the synchronous service interface must have a coresponding method in the asynchronous service interface with an extra <a href="/javadoc/latest/com/google/gwt/user/client/rpc/AsyncCallback.html">AsyncCallback</a> parameter as the last
argument.</li>
</ul>

<p>See <a href="/javadoc/latest/com/google/gwt/user/client/rpc/AsyncCallback.html">AsyncCallback</a> for additional details
on how to implement an asynchronous callback.</p>


<h2 id="DevGuideImplementingServices">Implementing Services</h2>

<p>Every service ultimately needs to perform some processing to order to respond to client requests. Such <a href="DevGuideServerCommunication.html#DevGuideServerSide">server-side</a>
processing occurs in the <i>service implementation</i>, which is based on the well-known <a href="http://java.sun.com/products/servlet/">servlet</a> architecture. A
service implementation must extend <a href="/javadoc/latest/com/google/gwt/user/server/rpc/RemoteServiceServlet.html">RemoteServiceServlet</a> and must implement the associated service interface. Note that the service implementation does <i>not</i> implement the asynchronous
version of the service interface.</p>

<p>Every service implementation is ultimately a servlet, but rather than extending <a href="http://java.sun.com/j2ee/sdk_1.3/techdocs/api/javax/servlet/http/HttpServlet.html">HttpServlet</a>, it extends <a href="/javadoc/latest/com/google/gwt/user/server/rpc/RemoteServiceServlet.html">RemoteServiceServlet</a> instead. <tt>RemoteServiceServlet</tt> automatically handles serialization of the data being passed between the client and the server and
invoking the intended method in your service implementation.</p>

<h3>Testing Services During Development</h3>

<p>GWT development mode includes an embedded version of Jetty which acts as a development-time servlet container for testing. This allows you to debug both server-side code and
client-side code when your run your application in development mode using a Java debugger. To automatically load your service implementation, configure your servlet in your
<tt>web.xml</tt>.</p>

<p>For example, suppose you have a module <tt>com.example.foo.Foo</tt>, and you define an RPC interface <tt>com.example.foo.client.MyService</tt>, annotated with
<tt>@RemoteServiceRelativePath(&quot;myService&quot;)</tt>. You then implement a servlet for the interface you created for <tt>com.example.foo.client.MyService</tt> with the class
<tt>com.example.foo.server.MyServiceImpl</tt> which extends <tt>RemoteServiceServlet</tt>. Finally, you add the following lines to your <tt>web.xml</tt>:</p>

<pre class="prettyprint">
&lt;!-- Example servlet loaded into servlet container --&gt;
&lt;servlet&gt;
  &lt;servlet-name&gt;myServiceImpl&lt;/servlet-name&gt;
  &lt;servlet-class&gt;
    com.example.foo.server.MyServiceImpl
  &lt;/servlet-class&gt;
&lt;/servlet&gt;
&lt;servlet-mapping&gt;
  &lt;servlet-name&gt;myServiceImpl&lt;/servlet-name&gt;
  &lt;url-pattern&gt;/com.example.foo.Foo/myService&lt;/url-pattern&gt;
&lt;/servlet-mapping&gt;
</pre>

<p>Take a look at the value in <tt>url-pattern</tt>. The first part must match the name of your GWT module. If your module has a <tt>rename-to</tt> attribute, you would use the
renamed value instead; either way it must match the actual subdirectory within your war directory where your GWT module lives (the module base URL). The second part must match the
value you specified in the <a href="/javadoc/latest/com/google/gwt/user/client/rpc/RemoteServiceRelativePath.html">RemoteServiceRelativePath</a> annotation you annotated <tt>com.example.foo.client.MyService</tt> with.</p>

<p>When testing out both the client and server side code in development mode, make sure to place a copy of the <tt>gwt-servlet.jar</tt> into your <tt>war/WEB-INF/lib</tt> directory,
and make sure your Java output directory is set to <tt>war/WEB-INF/classes</tt>. Otherwise the embedded Jetty server will not be able to load your servlet properly.</p>

<h3>Common Pitfalls</h3>

<p>Here are some commonly seen errors trying to get RPC running:</p>

<ul>
<li>When you start development mode, you see a <tt>ClassNotFoundException</tt> exception on the console, and the embedded server returns an error. This most likely means that the class
referenced by the servlet element in your GWT module is not in <tt>war/WEB-INF/classes</tt>. Be sure to compile your server classes into this location. If you are using an Ant
<tt>build.xml</tt> generated by webAppCreator, it should do this for you automatically.</li>

<li>When you start development mode, you see a <tt>NoClassDefFoundError: com/google/gwt/user/client/rpc/RemoteService</tt> exception on the console, and the embedded server returns an
error. This most likely means you forgot to copy <tt>gwt-servlet.jar</tt> into <tt>war/WEB-INF/lib</tt>. If you are using an Ant <tt>build.xml</tt> generated by webAppCreator, it
should do this for you automatically. Later on, if you need additional server-side libraries, you will need to add copies of those libraries into <tt>war/WEB-INF/lib</tt>
also.</li>

<li>When running your RPC call, development mode displays an excaption <tt>NoServiceEntryPointSpecifiedException: Service implementation URL not specified</tt>. This error means that
you did not specify a <tt>@RemoteServiceRelativePath</tt> in your service interface, and you also did not manually set target path by calling <a href="/javadoc/latest/com/google/gwt/user/client/rpc/ServiceDefTarget.html#setServiceEntryPoint(java.lang.String)">ServiceDefTarget.setServiceEntryPoint()</a>.</li>

<li>If invoking your RPC call fails with a 404 <a href="/javadoc/latest/com/google/gwt/user/client/rpc/StatusCodeException.html">StatusCodeException</a>, your web.xml may be misconfigured. Make sure you specified a <tt>@RemoteServiceRelativePath</tt> and that the <tt>&lt;url-pattern&gt;</tt>
specified in your <tt>web.xml</tt> matches this value, prepended with the location of your GWT output directory within the war directory.</li>
</ul>

<h3>Deploying Services Into Production</h3>

<p>If development mode works correctly, and you've compiled your application
with the GWT compiler and tested that production mode works correctly, you
should be able to deploy the contents of your war directory into any servlet
container that is appropriate for your application. See the <a
href="DevGuideServerCommunication.html#DevGuideRPCDeployment">example
deployment</a> in this guide for an example of how to deploy to a servlet
container.</p>


<h2 id="DevGuideMakingACall">Actually Making a Call</h2>

<p>The process of making an RPC from the client always involves the same steps:</p>

<ol>
<li>Instantiate the service interface using <a href="/javadoc/latest/com/google/gwt/core/client/GWT.html#create(java.lang.Class)">GWT.create()</a>.</li>

<li>Create an asynchronous callback object to be notified when the RPC has completed.</li>

<li>Make the call.</li>
</ol>

<h3>Example</h3>

<p>Suppose you want to call a method on a service interface defined as follows:</p>

<pre class="prettyprint">
// The RemoteServiceRelativePath annotation automatically calls setServiceEntryPoint()
@RemoteServiceRelativePath(&quot;email&quot;)
public interface MyEmailService extends RemoteService {
  void emptyMyInbox(String username, String password);
}
</pre>

<p>Its corresponding asynchronous interface will look like this:</p>

<pre class="prettyprint">
public interface MyEmailServiceAsync {
  void emptyMyInbox(String username, String password,
      AsyncCallback&lt;Void&gt; callback);
}
</pre>

<p>The client-side call will look like this:</p>

<pre class="prettyprint">
public void menuCommandEmptyInbox() {
  // (1) Create the client proxy. Note that although you are creating the
  // service interface proper, you cast the result to the asynchronous
  // version of the interface. The cast is always safe because the
  // generated proxy implements the asynchronous interface automatically.
  //
  MyEmailServiceAsync emailService = (MyEmailServiceAsync) GWT.create(MyEmailService.class);

  // (2) Create an asynchronous callback to handle the result.
  //
  AsyncCallback callback = new AsyncCallback() {
    public void onSuccess(Void result) {
      // do some UI stuff to show success
    }

    public void onFailure(Throwable caught) {
      // do some UI stuff to show failure
    }
  };

  // (3) Make the call. Control flow will continue immediately and later
  // 'callback' will be invoked when the RPC completes.
  //
  emailService.emptyMyInbox(fUsername, fPassword, callback);
}
</pre>

<p>It is safe to cache the instantiated service proxy to avoid creating it for subsequent calls. For example, you can instantiate the service proxy in the module's
<tt>onModuleLoad()</tt> method and save the resulting instance as a class member.</p>

<pre class="prettyprint">
    public class Foo implements EntryPoint {
      private MyEmailServiceAsync myEmailService = (MyEmailServiceAsync) GWT.create(MyEmailService.class);

      public void onModuleLoad() {
        // ... other initialization
      }

      /**
       * Make a GWT-RPC call to the server.  The myEmailService class member
       * was initalized when the module started up.
       */
      void sendEmail (String message) {
          myEmailService.sendEmail(message, new AsyncCallback&lt;String&gt;() {

            public void onFailure(Throwable caught) {
              Window.alert(&quot;RPC to sendEmail() failed.&quot;);
            }

            public void onSuccess(String result) {
              label.setText(result);
            }
          });
      }
    }
</pre>

<h2 id="DevGuideSerializableTypes">Serializable Types</h2>

<p>GWT supports the concept of <i>serialization</i>, which means allowing the contents of a data object to be moved out of one piece of running code and either transmitted to
another application or stored outside the appliation for later use. GWT RPC method parameters and return types must be transmitted across a network between client and server
applications and therefore they must be <i>serializable</i>.</p>

<p>Serializable types must conform to certain restrictions. GWT tries really hard to make serialization as painless as possible. While the rules regarding serialization are
subtle, in practice the behavior becomes intuitive very quickly.</p>

<p class="note">
<strong>Tip:</strong>
Although the terminology is very similar, GWT's concept of &quot;serializable&quot; is slightly different than serialization based on the standard Java interface <a href="http://java.sun.com/j2se/1.5.0/docs/api/java/io/Serializable.html">Serializable</a>. All references to serialization are referring to the GWT concept.
For some background, see the FAQ topic <a href="FAQ_Server.html#Does_the_GWT_RPC_system_support_the_use_of_java.io.Serializable">Does the GWT RPC system support the use of java.io.Serializable?</a>
</p>

<p>A type is serializable and can be used in a service interface if one of the following is true:</p>

<ul>
<li>The type is primitive, such as <tt>char</tt>, <tt>byte</tt>, <tt>short</tt>, <tt>int</tt>, <tt>long</tt>, <tt>boolean</tt>, <tt>float</tt>, or <tt>double</tt>.</li>

<li>The type an instance of the <tt>String</tt>, <tt>Date</tt>, or a primitive wrapper such as <tt>Character</tt>, <tt>Byte</tt>, <tt>Short</tt>, <tt>Integer</tt>, <tt>Long</tt>,
<tt>Boolean</tt>, <tt>Float</tt>, or <tt>Double</tt>.</li>

<li>The type is an enumeration. Enumeration constants are serialized as a name only; none of the field values are serialized.</li>

<li>The type is an array of serializable types (including other serializable arrays).</li>

<li>The type is a serializable user-defined class.</li>

<li>The type has at least one serializable subclass.</li>

<li>The type has a <a href="#DevGuideCustomSerialization">Custom Field Serializer</a></li>
</ul>

<p>
The class <code>java.lang.Object</code> is not serializable, therefore you cannot expect that a collection of Object types will be
serialized across the wire. As of GWT 1.5, most use cases can utilize Java
generics to replace the use of Object instances. This is strongly encouraged,
both to reduce client code size and to provide security against certain denial
of service attacks.
</p>

<h3>Serializable User-defined Classes</h3>

<p>A user-defined class is serializable if all of the following apply:</p>

<ol>
<li>It is assignable to <a href="/javadoc/latest/com/google/gwt/user/client/rpc/IsSerializable.html">IsSerializable</a> or
<a href="http://java.sun.com/j2se/1.5.0/docs/api/java/io/Serializable.html">Serializable</a>, either because it directly implements one of these interfaces or
because it derives from a superclass that does</li>

<li>All non-<tt>final</tt>, non-<tt>transient</tt> instance fields are themselves serializable, and</li>

<li>As of GWT 1.5, it must have a default (zero argument) constructor (with any access modifier) or no constructor at all.</li>
</ol>

<p>The <tt>transient</tt> keyword is honored, so values in transient fields are not exchanged during RPCs. Fields that are declared <tt>final</tt> are also not exchanged during
RPCs, so they should generally be marked <tt>transient</tt> as well.</p>

<h3>Polymorphism</h3>

<p>GWT RPC supports polymorphic parameters and return types. To make the best use of polymorphism, however, you should still try to be as specific as your design allows when
defining service interfaces. Increased specificity allows the <a href="DevGuideCompilingAndDebugging.html#DevGuideJavaToJavaScriptCompiler">compiler</a> to do a better job of removing
unnecessary code when it optimizes your application for size reduction.
Furthermore, server-side deserialization uses generic type information extracted
from method signatures to verify that the values it is deserializing are of the
correct type (to prevent some security attacks).</p>

<h3>Raw Types</h3>

<p>Collection classes such as <tt>java.util.Set</tt> and
<tt>java.util.List</tt> are tricky because they operate in terms of
<tt>Object</tt> instances. To make collections serializable, you should
specify the particular type of objects they are expected to contain through
normal type parameters (for example, <tt>Map&lt;Foo,Bar&gt;</tt> rather than
just <tt>Map</tt>).  If you use raw collections or maps you will get bloated
code and be vulnerable to denial of service attacks.</p>

<h3>Serializing Enhanced Classes</h3>

<p>Server-side persistence APIs, such as <a href="http://java.sun.com/jdo/">Java Data Objects (JDO)</a> and the <a href="http://java.sun.com/javaee/technologies/persistence.jsp">Java Persistence API</a> allow server-side code to store Java objects in a persistent data store.  When objects are attached to the data store, changes to the object field values are reflected in the data store and remain available even after the object is destroyed or the Java VM is restarted.</p>

<p>A number of persistence APIs are commonly implemented by passing the class source code or bytecode through a tool known as an "enhancer."  Enhancers may add additional static or instance fields to the class definition in order to implement the persistence features. Note that these enhancements are relevant on the server-side only; GWT does not provide persistence for client code.  However, the use of server-side enhancement causes class definitions to differ between the client and server, since the enhancer is run only on the server.  Due to these differences, previous versions of GWT were unable to perform RPC on enhanced classes.</p>

<p>As of GWT version 2.0, some common forms of persistence are now handled by the GWT RPC mechanism.  GWT considers a class to be enhanced if any of the following are true:</p>

<ul>
<li>The class is annotated with a JDO <code>javax.jdo.annotations.PersistenceCapable</code> annotation with <code>detachable=true</code>.</li>
<li>The class is annotated with a JPA <code>javax.persistence.Entity</code> annotation. </li>
<li>The fully-qualified class name is listed as one of the values of the <code>rpc.enhancedClasses</code> configuration property in a <code>.gwt.xml</code> module file that is part of the application.</li>
</ul>

<p>The GWT RPC mechanism for enhanced classes makes several assumptions regarding the persistence implementation:</p>
<ul>
<li>The object must be in a detached state (that is, changes to its fields must not affect the persistent store, and vice versa) at the time it is passed to or returned from a method of a <code>RemoteService</code>.</li>
<li>Enhanced fields that are neither static nor transient must be serializable using the ordinary Java serialization mechanism.</li>
<li>If the persistence implementation requires changes to an enhanced
instance field to have additional side effects (for example, if static
fields need to be updated at the same time), a suitably named setter
method (as described below) must exist for the instance field.</li>
</ul>

<p>When transmitting an enhanced object from server to client, the normal GWT RPC mechanism is used for the non-enhanced fields. Non-transient, non-static fields that exist on the server but not the client are serialized on the server using Java serialization, and the field names and values are combined into a single encoded value which is passed to the client. The client stores this encoded value, but does not make use of it except when transmitting the object back to the server.</p>

<p>When an enhanced object is transmitted from client to server, the encoded value (if present) is sent to the server, where it is decoded into its separate field names and values.  For each field 'xxxYyy', if a setter method named 'setXxxYyy' exists (note capitalization of the first letter following 'set'), it is called with the field value as its argument.  Otherwise, the field is set
directly.</p>

<h2 id="DevGuideCustomSerialization">Custom Serialization</h2>

<p>Classes with specific serialization requirements may take advantage of custom
field serializers. Examples include classes with constructors that require
arguments, or classes where data is more efficiently serialized in a form that
differs from the class structure. Among existing GWT classes, most Java
collection classes have custom serializers, as do some logging and exception
handling classes.</p>

<p>The custom field serializer for class <code>Foo</code> must be named
<code>Foo_CustomFieldSerializer</code>, and must be located in the same
package as the class that it serializes. In cases where it is not possible to
put the serializer in the appropriate package, the serializer may be placed in
the specific GWT serializer package, <code>com.google.gwt.user.client.rpc.core</code>.
For example, the custom field serializer for <code>java.util.HashMap</code> is
<code>com.google.gwt.user.client.rpc.core.java.util.HashMap_CustomFieldSerializer</code>.</p>

<p>Custom field serializers should extend the 
<code><a
href="/javadoc/latest/com/google/gwt/user/client/rpc/CustomFieldSerializer.html">
CustomFieldSerializer&lt;T&gt;</a></code> class, with the class that is being
serialized as the type parameter. For example:</p>

<pre class="code">
public final class HashMap_CustomFieldSerializer extends CustomFieldSerializer&lt;HashMap&gt;
</pre>

<p>All custom field serializer classes must implement the 
<code><a
href="/javadoc/latest/com/google/gwt/user/client/rpc/CustomFieldSerializer.html#serializeInstance(com.google.gwt.user.client.rpc.SerializationStreamWriter, T)">serializeInstance</a></code> and
<code><a
href="/javadoc/latest/com/google/gwt/user/client/rpc/CustomFieldSerializer.html#deserializeInstance(com.google.gwt.user.client.rpc.SerializationStreamReader, T)">deserializeInstance</a></code>
methods. Optionally, a class may override the default
<code><a
href="/javadoc/latest/com/google/gwt/user/client/rpc/CustomFieldSerializer.html#instantiateInstance(com.google.gwt.user.client.rpc.SerializationStreamReader)">instantiateInstance</a></code>
and
<code><a
href="/javadoc/latest/com/google/gwt/user/client/rpc/CustomFieldSerializer.html#hasCustomInstantiateInstance()">hasCustomInstantiateInstance</a></code>
methods if it cannot use default instantiation (for example, it does not have a
default constructor) or wants custom instantiation (for example, to instantiate
an immutable object). See the built-in custom serializers for examples of these methods.</p>

<h4>Type Checked Custom Serialization</h4>

<p>Some Denial of Service attacks exploit the fact that method parameters in
an RPC message must be deserialized before the method is invoked, and hence
before an exception can be thrown by the method for invalid parameters. As of
GWT version 2.4, support is provided for server-side type checking of method
parameters as they are deserialized. The correct use of type checking can
protect against parameter substitution attacks.</p>

<p>To support type checking, server-specific versions of custom field serializers
must be provided. The custom serializer class must be in a server package with
the same qualified name as the client serializer, with &quot;client&quot;
replaced by &quot;server&quot;. The actual serializer class for <code>Foo</code>
must be named <code>Foo_ServerCustomFieldSerializer</code>.
As an example, the client serializer for the method
<code>test.com.google.gwt.user.client.rpc.TypeCheckedGenericClass</code> is
<code>test.com.google.gwt.user.client.rpc.TypeCheckedGenericClass_CustomFieldSerializer</code>
and the server type-checked version is
<code>test.com.google.gwt.user.server.rpc.TypeCheckedGenericClass_ServerCustomFieldSerializer</code>.</p>

<p>Server custom field serializers should extend the 
<code><a
href="/javadoc/latest/com/google/gwt/user/server/rpc/ServerCustomFieldSerializer.html">
ServerCustomFieldSerializer&lt;T&gt;</a></code> class, with the class that is being
serialized as the type parameter. For example:</p>

<pre class="code">
public final class HashMap_ServerCustomFieldSerializer extends ServerCustomFieldSerializer&lt;HashMap&gt;
</pre>

<p>All server custom field serializer classes must implement the
client side
<code><a
href="/javadoc/latest/com/google/gwt/user/client/rpc/CustomFieldSerializer.html">
CustomFieldSerializer&lt;T&gt;</a></code> methods, the additional
<code><a
href="/javadoc/latest/com/google/gwt/user/server/rpc/ServerCustomFieldSerializer.html#deserializeInstance(com.google.gwt.user.server.rpc.impl.ServerSerializationStreamReader,
T, java.lang.reflect.Type[], com.google.gwt.user.server.rpc.impl.DequeMap)">deserializeInstance</a></code> method. Furthermore, a class must override the default
<code><a
href="/javadoc/latest/com/google/gwt/user/server/rpc/ServerCustomFieldSerializer.html#instantiateInstance(com.google.gwt.user.server.rpc.impl.ServerSerializationStreamReader,
java.lang.reflect.Type[], com.google.gwt.user.server.rpc.impl.DequeMap)">instantiateInstance</a></code>
method when the client code overrides it.</p>

<p>See the existing serializers, such as <code>HashMap_ServerCustomFieldSerializer</code>, for examples of type checking.</p>

<h2 id="DevGuideHandlingExceptions">Handling Exceptions</h2>

<p>Making RPCs opens up the possibility of a variety of errors. Networks fail, servers crash, and problems occur while processing a server call. GWT lets you handle these
conditions in terms of Java exceptions. RPC-related exceptions fall into two categories: checked exceptions and unchecked exceptions.</p>

<p>Keep in mind that any custom exceptions that you want to define, like any other piece of client-side code, must only be composed of types supported by <a href="RefJreEmulation.html">GWT's emulated JRE library</a>.</p>

<h3>Checked Exceptions</h3>

<p><a href="DevGuideServerCommunication.html#DevGuideCreatingServices">Service interface</a> methods support <tt>throws</tt> declarations to indicate which exceptions may be thrown back to
the client from a service implementation. Callers should implement <a href="/javadoc/latest/com/google/gwt/user/client/rpc/AsyncCallback.html#onFailure(java.lang.Throwable)">AsyncCallback.onFailure(Throwable)</a> to check for any exceptions specified in the service interface.</p>

<h3>Unexpected Exceptions</h3>

<h4>InvocationException</h4>

<p>An RPC may not reach the <a href="DevGuideServerCommunication.html#DevGuideImplementingServices">service implementation</a> at all. This can happen for many reasons: the network may be
disconnected, a DNS server might not be available, the HTTP server might not be listening, and so on. In this case, an <a href="/javadoc/latest/com/google/gwt/user/client/rpc/InvocationException.html">InvocationException</a> is passed to your
implementation of <a href="/javadoc/latest/com/google/gwt/user/client/rpc/AsyncCallback.html#onFailure(java.lang.Throwable)">AsyncCallback.onFailure(Throwable)</a>. The class is called <tt>InvocationException</tt> because the problem was with the invocation attempt itself rather than with
the service implementation.</p>

<p>An RPC can also fail with an invocation exception if the call does reach the server, but an undeclared exception occurs during normal processing of the call. There are many
reasons such a situation could arise: a necessary server resource, such as a database, might be unavailable, a <tt>NullPointerException</tt> could be thrown due to a bug in the
service implementation, and so on. In these cases, a <a href="/javadoc/latest/com/google/gwt/user/client/rpc/InvocationException.html">InvocationException</a> is thrown in application code.</p>

<h4>IncompatibleRemoteServiceException</h4>

<p>Another type of failure can be caused by an incompatibility between the client and the server. This most commonly occurs when a change to a <a href="DevGuideServerCommunication.html#DevGuideImplementingServices">service implementation</a> is deployed to a server but out-of-date clients are still active. For more details please
see <a href="/javadoc/latest/com/google/gwt/user/client/rpc/IncompatibleRemoteServiceException.html">IncompatibleRemoteServiceException</a>.</p>

<p>When the client code receives an <a href="/javadoc/latest/com/google/gwt/user/client/rpc/IncompatibleRemoteServiceException.html">IncompatibleRemoteServiceException</a>, it should ultimately attempt to refresh the browser in order to pick up the latest client.</p>


<h2 id="DevGuideArchitecturalPerspectives">Architectural Perspectives</h2>

<p>There are various ways to approach services within your application architecture. Understand first of all that <a href="DevGuideServerCommunication.html#DevGuideRemoteProcedureCalls">GWT RPC services</a> are not intended to replace J2EE servers, nor are they intended to provide a public web service
(e.g. SOAP) layer for your application. GWT RPCs, fundamentally, are simply a method of &quot;getting from the client to the server.&quot; In other words, you use RPCs to accomplish tasks
that are part of your application but that cannot be done on the client computer.</p>

<p>Architecturally, you can make use of RPC two alternative ways. The difference is a matter of taste and of the architectural needs of your application.</p>

<h3>Simple Client/Server Deployment</h3>

<p>The first and most straightforward way to think of service definitions is to treat them as your application's entire back end. From this perspective, <a href="DevGuideCodingBasics.html#DevGuideClientSide">client-side code</a> is your &quot;front end&quot; and all service code that runs on the server is &quot;back end.&quot; If you take this approach,
your service implementations would tend to be more general-purpose APIs that are not tightly coupled to one specific application. Your service definitions would likely directly
access databases through JDBC or Hibernate or even files in the server's file system. For many applications, this view is appropriate, and it can be very efficient because it
reduces the number of tiers.</p>

<h3>Multi-Tier Deployment</h3>

<p>In more complex, multi-tiered architectures, your GWT service definitions could simply be lightweight gateways that call through to back-end server environments such as J2EE
servers. From this perspective, your services can be viewed as the &quot;server half&quot; of your application's user interface. Instead of being general-purpose, services are created for
the specific needs of your user interface. Your services become the &quot;front end&quot; to the &quot;back end&quot; classes that are written by stitching together calls to a more general-purpose
back-end layer of services, implemented, for example, as a cluster of J2EE servers. This kind of architecture is appropriate if you require your back-end services to run on a
physically separate computer from your HTTP server.</p>

<h2 id="DevGuideRPCDeployment">Deploying RPC</h2>

<p>The GWT development mode embedded web server is only intended for debugging your web app. Once your web app is ready for deployment, it is time to deploy to a production server. If
your web app consists only of static content, pretty much any web server will do. However, most GWT web apps will use RPC and Java servlets. For these kinds of applications, you
will need to select a servlet container (also called a web container or web engine) to run the backend. GWT does not provide a servlet container to use in production, but there
are many different products available. The following lists just a few commercial and non-commercial offerings:</p>

<ul>
<li><a href="http://tomcat.apache.org/">Apache Tomcat</a></li>

<li><a href="http://www.eclipse.org/jetty/">Jetty</a></li>

<li><a href="http://labs.jboss.com/">JBoss</a></li>

<li><a href="http://www-306.ibm.com/software/websphere/">IBM Web Sphere</a></li>

<li><a href="http://www.oracle.com/appserver/index.html">Oracle Application Server</a></li>
</ul>



<p>In the examples below, we'll show how to deploy your server-side components to a production server using Apache HTTPD and Apache Tomcat as a web engine. Although there are many
different implementations of web servers and servlet containers, the Servlet API Specifications
define a standard structure for project directories which most web engines follow, and as of 1.6, GWT's native output format follows this specification.</p>

<p class="note"><strong>Tip:</strong> For the latest servlet specification documentation see the <a href="http://jcp.org/">Java Community Process</a> website. It will describe the
general workings of the servlet and the conventions used in configuring your servlets for deployment.</p>

<p/>

<h3>Simple Example with Apache Tomcat</h3>

<p>Before you get started, make sure your application works correctly in
development mode.</p>

<p>The simplest way to deploy your application is to use a single server for both your static content and your servlet classes. If you created your project with webAppCreator, you
can simply run <tt>ant war</tt> in your project directory. The Ant <tt>build.xml</tt> file should do the following things automatically:</p>

<ol>
<li>Copy any necessary libraries into <tt>war/WEB-INF/lib</tt>. You may need to update your <tt>build.xml</tt> file if you've added additional library dependencies beyond
<tt>gwt-servlet.jar</tt>.</li>

<li>Compile your Java source files into <tt>war/WEB-INF/classes</tt>. This is necessary to run your server code on the web server.</li>

<li>Run the GWT compiler on your GWT module. This produces all of the GWT output files you need.</li>

<li>Zip up the contents of your war directory into a <tt>.war</tt> file.</li>
</ol>



<p>Now just copy your <tt>.war</tt> file into Tomcat's <tt>/webapps</tt> folder. If you have default configuration settings it should automatically unzip the .war file.</p>

<p>If Tomcat is in its default configuration to run on port 8080, you should be able to run your application by entering the url
<tt>http://&lt;hostname&gt;:8080/MyApp/MyApp.html</tt> into your web browser.</p>

<p>If you encounter any problems, take look in the Tomcat log file, which can be found in the <tt>logs</tt> directory of your Tomcat installation. If your web pages display but
the RPC calls don't seem to be going through, try turning on access logging on Tomcat. You may find that the URL used by the client side has not been registered by Tomcat, or that
there is a misconfiguration between the URL path set in the <tt>setServiceEntryPoint(URL)</tt> call when declaring your RPC service and the <tt>&lt;url-pattern&gt;</tt> URL
mapping the the <tt>web.xml</tt> file.</p>

<h3>Using Tomcat with Apache HTTPD and a proxy</h3>

<p>The above example is a good way to test your application from client to server, but may not be the best production setup. For one, the Jakarta/Tomcat server is not the fastest
web server for static files. Also, you may already have a web infrastructure setup for serving static content that you would like to leverage. The following example will show you
how to split the web engine deployment from from the static content.</p>

<p>If you look at the war directory of a built web application, you'll notice two kinds of files:</p>

<ol>
<li>Everything other than the <tt>WEB-INF/</tt> directory (and subdirectories) is static content.</li>

<li>The contents of the <tt>WEB-INF/</tt> directory contain server configuration and code.</li>
</ol>



<p>Once you've built your web application, copy the contents of the war directory <i>except for <tt>WEB-INF/</tt> directory</i> into your apache configuration's document root. We
will call that directory <tt>/var/www/doc/MyApp</tt> and the resulting URL is <tt>http://www.example.com/MyApp/MyApp.html</tt>. (An easier method might be to just copy the entire
war contents and then delete <tt>WEB-INF/</tt> out of the destination.)</p>

<p>To setup your Tomcat server, just deploy the entire war, including both kinds of content. The reason for including static content is that servlets may need to use <a href="http://java.sun.com/products/servlet/2.3/javadoc/javax/servlet/ServletContext.html#getResource(java.lang.String)">ServletContext.getResource()</a> to access
static content programmatically. This is always true for GWT RPC servlets, which need to load a generated serialization policy file.</p>

<h4>Example</h4>

<p>Let's assume that the Tomcat server is on a different host. In this case, there is going to be a problem. Browsers' Single Origin Policy (SOP) will prevent connections to ports
or machines that differ from the original URL. The strategy we are going to use to satisfy the SOP is to configure Apache to proxy a URL to another URL.</p>

<p>Specific directions for configuring Apache and Tomcat for such a proxy setup are at <a href="http://tomcat.apache.org/tomcat-6.0-doc/proxy-howto.html">the Apache
website</a>. The general idea is to setup Apache to redirect ONLY requests to your sevlets. That way Apache will serve all the static content, and your Tomcat server will only be
used for service calls. For this example, assume that:</p>

<ul>
<li>Your Apache server is running on <tt>www.example.com</tt></li>

<li>Your Tomcat server is running on <tt>servlet.example.com:8080</tt></li>

<li>Your GWT module has a <tt>&lt;rename-to=&quot;myapp&quot;&gt;</tt></li>

<li>You have one RPC servlet, mapped into <tt>/myapp/myService</tt></li>
</ul>



<p>The idea is to have Apache proxy requests to the servlet to the other server such that:</p>

<p><tt>http://www.example.com/MyApp/myapp/myService --&gt; http://servlet.example.com:8080/MyApp/myapp/myService</tt></p>

<p>The following Apache configuration sets up such a rule using a Proxy:</p>

<pre class="prettyprint">
ProxyPass        /MyApp/myapp/myService  http://servlet.example.com:8080/MyApp/myapp/myService
ProxyPassReverse /MyApp/myapp/myService  http://servlet.example.com:8080/MyApp/myapp/myService
</pre>

<p>To verify this is working, use a web browser to hit both <tt>http://www.example.com/MyApp/myapp/myService</tt> and
<tt>http://servlet.example.com:8080/MyApp/myapp/myService</tt>. You should get the same result in both cases (typically a <tt>405: HTTP method GET is not supported by this
URL</tt>, which is good). If you get something different hitting the second URL, you may have a configuration issue.</p>

<ul>
<li>If you get a 404, there is most likely an error in the left hand side of your URL mapping.</li>

<li>If you get a &quot;Bad Gateway&quot;, there is most likely an error in the right hand side of your URL mapping.</li>

<li>If you get a 403 permission error, check the Apache configuration files to for <tt>&lt;Proxy&gt;</tt> tags to see if the permissions are wrong. You may need to add a section
like this:</li>

<li style="list-style: none">
<pre class="prettyprint">
   &lt;Proxy *&gt;
     Order deny,allow
     Allow from all
   &lt;/Proxy&gt;
</pre>
</li>
</ul>

<h3>Other Deployment Methods</h3>

<p>It should be mentioned that these are just two examples of deployment scenarios. There are many ways to configure your application for deployment:</p>

<ul>
<li>Some web engines take special directives in <tt>web.xml</tt> that need to be taken into consideration.</li>

<li>Some web servers have optimized ways to route servlet calls to a servlet engine (e.g. mod_jk on Apache)</li>

<li>Some web servers and web engines have facilities for replication so you can load balance your servlet across many nodes.</li>
</ul>

<p class="note"><strong>Tip:</strong> You can also use a real production server
while debugging in development mode. This can be useful if you are adding GWT to an existing application, or if your
server-side requirements have become more than the embedded web server can
handle. See this article on how to use an external server in development mode.</p>

<h2 id="DevGuideHttpRequests">Making HTTP requests</h2>

<p>
If your GWT application needs to communicate with a server, but you can't use <a href="http://java.sun.com/products/servlet/">Java servlets</a> on the backend &mdash; or if you simply prefer not to use <a href="DevGuideServerCommunication.html#DevGuideRemoteProcedureCalls">RPC</a> &mdash; you can still perform HTTP requests manually.
GWT contains a number of HTTP client classes that simplify making custom HTTP requests to your server and optionally processing a <a href="http://www.json.org/">JSON</a>- or <a href="http://www.w3.org/XML/">XML</a>-formatted response.</p>

<p>GWT contains a set of <a href="/javadoc/latest/com/google/gwt/http/client/package-summary.html">HTTP client classes</a>
that allow your application to make generic HTTP requests.</p>

<h3>Using HTTP in GWT</h3>

<p>Making HTTP requests in GWT works much like it does in any language or framework, but there are a couple of important differences you should be aware of.</p>

<p>First, because of the single-threaded execution model of most web browsers, long synchronous operations such as server calls can cause a JavaScript application's interface (and
sometimes the browser itself) to become unresponsive. To prevent network or server communication problems from making the browser &quot;hang&quot;, GWT allows only <i>asynchronous</i>
server calls. When sending an HTTP request, the client code must register a callback method that will handle the response (or the error, if the call fails). For more information
about the exclusion of synchronous server connections, you may want to check out this <a href="FAQ_Server.html#Why_doesn't_GWT_provide_a_synchronous_server_connection_opt">FAQ article</a>.</p>

<p>Second, because GWT applications run as JavaScript within a web page, they are subject to the browser's <a href="http://en.wikipedia.org/wiki/Same_origin_policy">same origin policy (SOP)</a>. SOP prevents client-side JavaScript code from interacting with untrusted (and potentially harmful) resources loaded from other
websites. In particular, SOP makes it difficult (although not impossible) to send HTTP requests to servers other than the one that served the HTML host page into which your GWT module is laoded.
</p>

<h3>HTTP client types</h3>

<p>To use the <a href="/javadoc/latest/com/google/gwt/http/client/package-summary.html">HTTP types</a> in your application,
you'll need to first inherit the GWT HTTP module by adding the following <tt>&lt;inherits&gt;</tt> tag to your <a href="DevGuideOrganizingProjects.html#DevGuideModuleXml">module XML file</a>:</p>

<pre>
&lt;inherits name=&quot;com.google.gwt.http.HTTP&quot; /&gt;
</pre>

<p><a href="/javadoc/latest/com/google/gwt/http/client/RequestBuilder.html">RequestBuilder</a> is the core class you'll
need for constructing and sending HTTP requests. Its <a href="/javadoc/latest/com/google/gwt/http/client/RequestBuilder.html#RequestBuilder(com.google.gwt.http.client.RequestBuilder.Method,%20java.lang.String)">constructor</a> has parameters for specifying the HTTP method of the request (GET, POST, etc.) and the URL (the <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/http/client/URL.html">URL</a> utility class is handy for escaping invalid characters).
Once you have a RequestBuilder object, you can use its methods to set the <a href="/javadoc/latest/com/google/gwt/http/client/RequestBuilder.html#setUser(java.lang.String)">username</a>, <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/http/client/RequestBuilder.html#setPassword(java.lang.String)">password</a>, and <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/http/client/RequestBuilder.html#setTimeoutMillis(int)">timeout interval</a>. You can
also set any number of <a href="/javadoc/latest/com/google/gwt/http/client/RequestBuilder.html#setHeader(java.lang.String,%20java.lang.String)">headers</a> in the HTTP request.</p>

<p>Once the HTTP request is ready, call the server using the <a href="/javadoc/latest/com/google/gwt/http/client/RequestBuilder.html#sendRequest(java.lang.String,%20com.google.gwt.http.client.RequestCallback)">sendRequest(String, RequestCallback)</a> method. The <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/http/client/RequestCallback.html">RequestCallback</a> argument you pass will handle
the response or the error that results. When a request completes normally, your <a href="/javadoc/latest/com/google/gwt/http/client/RequestCallback.html#onResponseReceived(com.google.gwt.http.client.Request,%20com.google.gwt.http.client.Response)">onResponseReceived(Request, Response)</a> method is invoked. Details of the response (for example, <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/http/client/Response.html#getStatusCode()">status code</a>, <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/http/client/Response.html#getHeaders()">HTTP headers</a>, and <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/http/client/Response.html#getText()">response text</a>) can be retrieved from the <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/http/client/Response.html">Response</a> argument. Note that the
onResponseReceived(Request, Response) method is called even if the HTTP status code is something other than 200 (success). If the call <i>does not</i> complete normally, the <a href="/javadoc/latest/com/google/gwt/http/client/RequestCallback.html#onError(com.google.gwt.http.client.Request,%20java.lang.Throwable)">onError(Request, Throwable)</a> method gets called, with the second parameter describing the type error that occurred.</p>

<p>As noted before, all HTTP calls in GWT are asynchronous, so the code following the call to <a href="/javadoc/latest/com/google/gwt/http/client/RequestBuilder.html#sendRequest(java.lang.String,%20com.google.gwt.http.client.RequestCallback)">sendRequest(String, RequestCallback)</a> will be executed immediately, <i>not</i> after the server responds to the HTTP request. You can use the <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/http/client/Request.html">Request</a> object that is returned from sendRequest(String,
RequestCallback) to <a href="/javadoc/latest/com/google/gwt/http/client/Request.html#isPending()">monitor the status</a> of
the call, and <a href="/javadoc/latest/com/google/gwt/http/client/Request.html#cancel()">cancel it</a> if necessary.</p>

<p>Here's a brief example of making an HTTP request to a server:</p>

<pre class="prettyprint">
import com.google.gwt.http.client.*;
...

String url = &quot;http://www.myserver.com/getData?type=3&quot;;
RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, URL.encode(url));

try {
  Request request = builder.sendRequest(null, new RequestCallback() {
    public void onError(Request request, Throwable exception) {
       // Couldn't connect to server (could be timeout, SOP violation, etc.)
    }

    public void onResponseReceived(Request request, Response response) {
      if (200 == response.getStatusCode()) {
          // Process the response in response.getText()
      } else {
        // Handle the error.  Can get the status text from response.getStatusText()
      }
    }
  });
} catch (RequestException e) {
  // Couldn't connect to server
}
</pre>

<h3>Processing the response</h3>

<p>Once you receive the response from the server using <a href="/javadoc/latest/com/google/gwt/http/client/Response.html#getText()">Response.getText()</a>, it's up to you to process it. If your response is encoded in XML or JSON, you can use the <a href="DevGuideCodingBasics.html#DevGuideXML">XML library</a> and the <a href="DevGuideCodingBasics.html#DevGuideJSON">JSON library</a> or <a href="DevGuideCodingBasics.html#DevGuideOverlayTypes">overlay types</a>, respectively, to process it.</p>


<h2 id="DevGuideGettingUsedToAsyncCalls">Getting Used to Asynchronous Calls</h2>

<p>Using the <a href="/javadoc/latest/com/google/gwt/user/client/rpc/AsyncCallback.html">AsyncCallback</a> interface is new
to many developers. Code is not necessarily executed sequentially, and it forces developers to handle situations where a server call is in progress but has not completed. Despite
these seeming drawbacks, the designers of GWT felt it was an essential part of creating usable interfaces in AJAX for several reasons:</p>

<ul>
<li>The JavaScript engine in most browsers is singly threaded, meaning that:</li>

<li style="list-style: none">
<ul>
<li>The JavaScript engine will hang while waiting for a synchronous RPC call to return.</li>

<li>Most browsers will popup a dialog if any JavaScript entry point takes longer than a few moments.</li>
</ul>
</li>

<li>A server could be inaccessible or otherwise non responsive. Without an asynchronous mechanism, the application could be left waiting on a server call to return
indefinitely.</li>

<li>Asynchronous mechanisms allow server calls to run in parallel with logic inside the application, shortening the overall response time to the user.</li>

<li>Asynchronous mechanisms can service multiple server calls in parallel.</li>
</ul>

<p>Browser mechanics aside, asynchronous RPC gives your application the ability to achieve true parallelism in your application, even without multi-threading. For example, suppose
your application displays a large <a href="/javadoc/latest/com/google/gwt/user/client/ui/HTMLTable.html">table</a>
containing many widgets. Constructing and laying out all those widgets can be time consuming. At the same time, you need to fetch data from the server to display inside the table.
This is a perfect reason to use asynchronous calls. Initiate an asynchronous call to request the data immediately before you begin constructing your table and its widgets. While
the server is fetching the required data, the browser is executing your user interface code. When the client finally receives the data from the server, the table has been
constructed and laid out, and the data is ready to be displayed.</p>

<p>To give you an idea of how effective this technique can be, suppose that building the table takes one second and fetching the data takes one second. If you make the server call
synchronously, the whole process will require at least two seconds.</p>

<blockquote><img src="images/GettingUsedToAsyncCalls1.png"/></blockquote>

<p>But if you fetch the data asynchronously, the whole process still takes just one second, even though you are doing two seconds' worth of work.</p>

<blockquote><img src="images/GettingUsedToAsyncCalls2.png"/></blockquote>

<p class="note"><strong>Tip:</strong> Most browsers restrict the number of outgoing network connections to two at a time, restricting the amount of parallelism you can expect from multiple
simultaneous RPCs.</p>

<p>The hardest thing to get used to about asynchronous calls is that the calls are non-blocking, however, Java inner classes go a long way toward making this manageable. Consider
the following implementation of an asynchronous call adapted from the Dynamic Table sample
application. It uses a slightly different syntax to define the required interface for the <a href="/javadoc/latest/com/google/gwt/user/client/rpc/AsyncCallback.html">AsyncCallback</a> object that is the last
parameter to the <tt>getPeople</tt> RPC call:</p>


<pre class="prettyprint">
 // This code is called before the RPC starts
 //
  if (startRow == lastStartRow) {
    ...
  }

  // Invoke the RPC call, implementing the callback methods inline:
  //
  calService.getPeople(startRow, maxRows, new AsyncCallback&lt;Person[]&gt;() {

    // When the RPC returns, this code will be called if the RPC fails
    public void onFailure(Throwable caught) {
       statusLabel.setText(&quot;Query failed: &quot; + caught.getMessage());
       acceptor.failed(caught);
    }

    // When the RPC returns, this code is called if the RPC succeeds
    public void onSuccess(Person[] result) {
      lastStartRow = startRow;
      lastMaxRows = maxRows;
      lastPeople = result;
      pushResults(acceptor, startRow, result);
      statusLabel.setText(&quot;Query reutrned &quot; + result.length + &quot; rows.&quot;);
    }
  });

  // The above method call will not block, but return immediately.
  // The following code will execute while the RPC is in progress,
  // before either of onFailure() or onSuccess() are executed.
  //
  statusLabel.setText(&quot;Query in progress...&quot;);
  ...
</pre>


<p>The important issue to understand is that that the code that follows the RPC call invocation will be executed while the actual round trip to the server is still in progress.
Although the code inside the <tt>onSuccess()</tt> method is defined inline with the call, it will not be executed until both the calling code returns back to the JavaScript main
loop, and the result message from the server returns.</p>

<h2 id="DevGuideDeRPC">Direct-Eval RPC</h2>

<p>This feature did not work out as planned, and the GWT team strongly discourages its use.
Try the <a href="DevGuideRequestFactory.html">Request Factory</a> feature when
you have non-trivial server-domain objects.</p>