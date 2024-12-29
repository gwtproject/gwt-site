Deferred
===

Deferred binding is a feature of the GWT compiler that works by generating many versions of code at compile time, only one of which needs to be loaded by a particular client
during bootstrapping at runtime. Each version is generated on a per browser basis, along with any other axis that your application defines or uses. For example, if you were to
internationalize your application using [GWT's Internationalization module](DevGuideI18n.html), the GWT compiler would generate
various versions of your application per browser environment, such as "Firefox in English", "Firefox in French", "Internet Explorer in English", etc... As a result, the deployed
JavaScript code is compact and quicker to download than hand coded JavaScript, containing only the code and resources it needs for a particular browser environment.

1.  [Deferred Binding Benefits](#benefits)
2.  [Defining Deferred Binding Rules](#rules)
3.  [Directives in Module XML files](#directives)
4.  [Deferred Binding Using Replacement](#replacement)
5.  [Example Class Hierarchy using Replacement](#example)
6.  [Deferred Binding using Generators](#generators)
7.  [Generator Configuration in Module XML](#generator)
8.  [Generator Implementation](#generator-implementation)

## Deferred Binding Benefits<a id="benefits"></a>

Deferred Binding is a technique used by the GWT compiler to create and select a specific implementation of a class based on a set of parameters. In essence, deferred binding is
the GWT answer to Java reflection. It allows the GWT developer to produce several variations of their applications custom to each browser environment and have only
one of them actually downloaded and executed in the browser.

Deferred binding has several benefits:

*   Reduces the size of the generated JavaScript code that a client will need to download by only including the code needed to run a particular browser/locale instance (used by
the [Internationalization module](DevGuideI18n.html))
*   Saves development time by automatically generating code to implement an interface or create a proxy class (used by the [GWT RPC module](DevGuideServerCommunication.html#DevGuideRemoteProcedureCalls))
*   Since the implementations are pre-bound at compile time, there is no run-time penalty to look up an implementation in a data structure as with dynamic binding or using virtual
functions.

Some parts of the toolkit make implicit use of deferred binding, that is, they use the technique as a part of their implementation, but it is not visible to the user of the
API. For example, many [widgets and <a href="DevGuideUiPanels.html">panels](DevGuideUiWidgets.html) as well as the [DOM](/javadoc/latest/com/google/gwt/user/client/DOM.html) class use this technique to implement browser specific
logic. Other GWT features require the API user to explicitly invoke deferred binding by designing classes that follow specific rules and instantiating instances of the classes with [GWT.create(Class)](/javadoc/latest/com/google/gwt/core/client/GWT.html#create-java.lang.Class-), including [GWT RPC](DevGuideServerCommunication.html#DevGuideRemoteProcedureCalls) and [I18N](DevGuideI18n.html).

As a user of the GWT, you may never need to create a new interface that uses deferred binding. If you follow the instructions in the guide for creating
internationalized applications or GWT RPC calls you will be using deferred binding, but you will not have to actually write any browser dependent or locale dependent code.

The rest of the deferred binding section describes how to create new rules and classes using deferred binding. If you are new to the toolkit or only intend to use pre-packaged
widgets, you will probably want to skip on to the next topic. If you are interested in programming entirely new widgets from the ground up or other functionality that requires
cross-browser dependent code, the next sections should be of interest.

## Defining Deferred Binding Rules<a id="rules"></a>

There are two ways in which types can be replaced via deferred binding:

*   Replacement: A type is replaced with another depending on a set of configurable rules.
*   Code generation: A type is substituted by the result of invoking a code generator at compile time.

## Directives in Module XML files<a id="directives"></a>

The deferred binding mechanism is completely configurable and does not require editing the GWT distributed source code. Deferred binding is configured through the
`<replace-with>` and `<generate-with>` elements in the [module XML files](DevGuideOrganizingProjects.html#DevGuideModuleXml). The deferred binding
rules are pulled into the module build through `<inherits>` elements.

For example, the following configuration invokes deferred binding for the [PopupPanel](/javadoc/latest/com/google/gwt/user/client/ui/PopupPanel.html) widget:

*   Top level `<module>.gwt.xml` _**inherits**_
[com.google.gwt.user.User](https://github.com/gwtproject/gwt/blob/main/user/src/com/google/gwt/user/User.gwt.xml)
*   [com/google/gwt/user/User.gwt.xml](https://github.com/gwtproject/gwt/blob/main/user/src/com/google/gwt/user/User.gwt.xml) _**inherits**_ [com.google.gwt.user.Popup](https://github.com/gwtproject/gwt/blob/main/user/src/com/google/gwt/user/Popup.gwt.xml)
*   [com/google/gwt/user/Popup.gwt.xml](https://github.com/gwtproject/gwt/blob/main/user/src/com/google/gwt/user/Popup.gwt.xml) _**contains**_ `<replace-with>` elements to define deferred binding rules for the [PopupPanel](/javadoc/latest/com/google/gwt/user/client/ui/PopupPanel.html) class.

Inside the [PopupPanel](/javadoc/latest/com/google/gwt/user/client/ui/PopupPanel.html) module XML file, there
happens to be some rules defined for deferred binding. In this case, we're using a replacement rule.

## Deferred Binding Using Replacement<a id="replacement"></a>

The first type of deferred binding uses _replacement_.
Replacement means overriding the implementation of one java class with another that is determined at compile time.
For example, this technique is used to conditionalize the implementation of some widgets, such as the [PopupPanel](/javadoc/latest/com/google/gwt/user/client/ui/PopupPanel.html).
The use of `<inherits>` for the `PopupPanel` class is shown in the previous section describing the deferred binding rules.
The actual replacement rules are specified in `Popup.gwt.xml`, as shown below:

```xml
<module>

  <!--  ... other configuration omitted ... -->

  <!-- Fall through to this rule is the browser isn't IE or Mozilla -->
  <replace-with class="com.google.gwt.user.client.ui.impl.PopupImpl">
    <when-type-is class="com.google.gwt.user.client.ui.impl.PopupImpl"/>
  </replace-with>

  <!-- Mozilla needs a different implementation due to issue #410 -->
  <replace-with class="com.google.gwt.user.client.ui.impl.PopupImplMozilla">
    <when-type-is class="com.google.gwt.user.client.ui.impl.PopupImpl" />
    <any>
      <when-property-is name="user.agent" value="gecko"/>
      <when-property-is name="user.agent" value="gecko1_8" />
    </any>
  </replace-with>

  <!-- IE has a completely different popup implementation -->
  <replace-with class="com.google.gwt.user.client.ui.impl.PopupImplIE6">
    <when-type-is class="com.google.gwt.user.client.ui.impl.PopupImpl"/>
    <when-property-is name="user.agent" value="ie6" />
  </replace-with>
</module>
```

These directives tell the GWT compiler to swap out the `PopupImpl` class code with different class implementations according to the `user.agent` property. The
`Popup.gwt.xml` file specifies a default implementation for the `PopupImpl` class, an override for the Mozilla browser (`PopupImplMozilla` is substituted for
`PopupImpl`), and an override for Internet Explorer version 6 (`PopupImplIE6` is substituted for `PopupImpl`). Note that `PopupImpl` class or its
derived classes cannot be instantiated directly. Instead, the `PopupPanel` class is used and the [GWT.create(Class)](/javadoc/latest/com/google/gwt/core/client/GWT.html#create-java.lang.Class-) technique is used
under the hood to instruct the compiler to use deferred binding.

## Example Class Hierarchy using Replacement<a id="example"></a>

To see how this is used when designing a widget, we will examine the case of the `PopupPanel` widget further. The `PopupPanel` class implements the user visible
API and contains logic that is common to all browsers. It also instantiates the proper implementation specific logic using the [GWT.create(Class)](/javadoc/latest/com/google/gwt/core/client/GWT.html#create-java.lang.Class-) as follows:

```java
private static final PopupImpl impl = GWT.create(PopupImpl.class);
```

The two classes PopupImplMozilla and PopupImplIE6 extend the PopupImpl class and override some `PopupImpl`'s methods to implement browser specific behavior.

Then, when the `PopupPanel` class needs to switch to some browser dependent code, it accesses a member function inside the `PopupImpl` class:

```java
public void setVisible(boolean visible) {
    // ... common code for all implementations of PopupPanel ...

    // If the PopupImpl creates an iframe shim, it's also necessary to hide it
    // as well.
    impl.setVisible(getElement(), visible);
  }
```

The default implementation of `PopupImpl.setVisible()` is empty, but `PopupImplIE6` has some special logic implemented as a [JSNI](DevGuideCodingBasics.html#DevGuideJavaScriptNativeInterface) method:

```java
public native void setVisible(Element popup, boolean visible) /*-{
    if (popup.__frame) {
      popup.__frame.style.visibility = visible ? 'visible' : 'hidden';
    }
  }-*/;{
```

After the GWT compiler runs, it prunes out any unused code. If your application references the `PopupPanel` class, the compiler will create a separate JavaScript output
file for each browser, each containing only one of the implementations: `PopupImpl`, `PopupImplIE6` or `PopupImplMozilla`. This means that each browser only
downloads the implementation it needs, thus reducing the size of the output JavaScript code and minimizing the time needed to download your application from the server.

## Deferred Binding using Generators<a id="generators"></a>

The second technique for deferred binding consists of using _generators_. Generators are classes that are invoked by the GWT compiler to generate a Java implementation of
a class during compilation. When compiling for production mode, this generated implementation is directly translated to one of the versions of your application in JavaScript code that a
client will download based on its browser environment.

The following is an example of how a deferred binding generator is specified to the compiler in the [module XML file](DevGuideOrganizingProjects.html#DevGuideModuleXml)
hierarchy for the `RemoteService` class - used for GWT-RPC:

*   Top level `<module>.gwt.xml` _**inherits**_ [com.google.gwt.user.User](https://github.com/gwtproject/gwt/blob/main/user/src/com/google/gwt/user/User.gwt.xml)
*   [com/google/gwt/user/User.gwt.xml](https://github.com/gwtproject/gwt/blob/main/user/src/com/google/gwt/user/User.gwt.xml) _**inherits**_ [com.googl.gwt.user.RemoteService](https://github.com/gwtproject/gwt/blob/main/user/src/com/google/gwt/user/RemoteService.gwt.xml)
*   [com/google/gwt/user/RemoteService.gwt.xml](https://github.com/gwtproject/gwt/blob/main/user/src/com/google/gwt/user/RemoteService.gwt.xml) _**contains**_ `<generates-with>` elements to define deferred binding rules for the `RemoteService` class.

## Generator Configuration in Module XML<a id="generator"></a>

The XML element `<generate-with>` tells the compiler to use a `Generator` class. Here are the contents of the `RemoteService.gwt.xml` file relevant
to deferred binding:

```xml
<module>

 <!--  ... other configuration omitted ... -->

 <!-- Default warning for non-static, final fields enabled -->
 <set-property name="gwt.suppressNonStaticFinalFieldWarnings" value="false" />

 <generate-with class="com.google.gwt.user.rebind.rpc.ServiceInterfaceProxyGenerator">
   <when-type-assignable class="com.google.gwt.user.client.rpc.RemoteService" />
 </generate-with>
</module>
```

These directives instruct the GWT compiler to invoke methods in a [Generator](/javadoc/latest/com/google/gwt/core/ext/Generator.html) subclass (`ServiceInterfaceProxyGenerator`) in order to generate special code when the deferred binding mechanism [GWT.create()](/javadoc/latest/com/google/gwt/core/client/GWT.html#create-java.lang.Class-) is encountered while
compiling. In this case, if the [GWT.create()](/javadoc/latest/com/google/gwt/core/client/GWT.html#create-java.lang.Class-) call references an instance of `RemoteService` or one of its subclasses, the `ServiceInterfaceProxyGenerator`'s generate() method
will be invoked.

## Generator Implementation<a id="generator-implementation"></a>

Defining a subclass of the `Generator` class is much like defining a plug-in to the GWT compiler. The `Generator` gets called to generate a Java class definition
before the Java to JavaScript conversion occurs. The implementation consists of one method that must output Java code to a file and return the name of the generated class as a
string.

The following code shows the `Generator` that is responsible for deferred binding of a `RemoteService` interface:

```java
/**
 * Generator for producing the asynchronous version of a
 * {@link com.google.gwt.user.client.rpc.RemoteService RemoteService} interface.
 */
public class ServiceInterfaceProxyGenerator extends Generator {

  /**
   * Generate a default constructible subclass of the requested type. The
   * generator throws <code>UnableToCompleteException</code> if for any reason
   * it cannot provide a substitute class
   *
   * @return the name of a subclass to substitute for the requested class, or
   *         return <code>null</code> to cause the requested type itself to be
   *         used
   *
   */
  public String generate(TreeLogger logger, GeneratorContext ctx,
      String requestedClass) throws UnableToCompleteException {

    TypeOracle typeOracle = ctx.getTypeOracle();
    assert (typeOracle != null);

    JClassType remoteService = typeOracle.findType(requestedClass);
    if (remoteService == null) {
      logger.log(TreeLogger.ERROR, "Unable to find metadata for type '"
          + requestedClass + "'", null);
      throw new UnableToCompleteException();
    }

    if (remoteService.isInterface() == null) {
      logger.log(TreeLogger.ERROR, remoteService.getQualifiedSourceName()
          + " is not an interface", null);
      throw new UnableToCompleteException();
    }

    ProxyCreator proxyCreator = new ProxyCreator(remoteService);

    TreeLogger proxyLogger = logger.branch(TreeLogger.DEBUG,
        "Generating client proxy for remote service interface '"
            + remoteService.getQualifiedSourceName() + "'", null);

    return proxyCreator.create(proxyLogger, ctx);
  }
}
```

The `typeOracle` is an object that contains information about the Java code that has already been parsed that the generator may need to consult. In this case, the
`generate()` method checks it arguments and the passes off the bulk of the work to another class (`ProxyCreator`).
