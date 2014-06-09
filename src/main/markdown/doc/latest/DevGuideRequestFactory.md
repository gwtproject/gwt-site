<style type="text/css">
  #gwt-content th,
  #gwt-content td {
    border: 1px solid #ccc;
    font-size: 95%;
  }
</style>

<p>RequestFactory is an alternative to GWT-RPC for creating data-oriented
services. RequestFactory and its related interfaces (RequestContext and
EntityProxy) make it easy to build data-oriented (CRUD) apps with an
ORM-like interface on the client. It is designed to be used with an ORM
layer like JDO or JPA on the server, although this is not required.</p>

<h2>Overview</h2>
<h3>Benefits</h3>

<p>RequestFactory makes it easy to implement a data
access layer on both client and server. It allows you to structure your
server-side code in a data-centric way and provides a higher level of
abstraction than GWT-RPC, which is service-oriented rather than
data-oriented. On the client side, RequestFactory keeps track of objects
that have been modified and sends only changes to the server, which
results in very lightweight network payloads. In addition,
RequestFactory provides a solid foundation for automatic batching and
caching of requests in the future.</p>

<h3>How does it relate to GWT-RPC?</h3>

<p>RequestFactory uses its own servlet, RequestFactoryServlet, and
implements its own protocol for data exchange between client and server.
RequestFactory takes a more prescriptive approach to the client-server
programming model than GWT-RPC does.  GWT-RPC uses remote methods as its
basic building blocks (similar to Java RMI), whereas RequestFactory uses
entities (data with a persistent identity) and services. See this external
site for a <a href="http://stackoverflow.com/q/4119867/607050">discussion on
RequestFactory versus GWT-RPC</a>.


<h2>Coding with RequestFactory</h2>
<p>Let's take a look at the moving parts in an application that uses RequestFactory.</p>
<ol class="toc">
  <li><a href="#entities">Entity</a></li>
  <li><a href="#proxies">Entity Proxies</a></li>
  <li><a href="#valueProxies">Value Proxies</a></li>
  <li><a href="#interface">RequestFactory Interface</a></li>
  <li><a href="#transportable">Transportable types</a></li>
  <li><a href="#impl">Server Implementations</a></li>
    <li><a href="#entity_service">Implementing a service in an entity class</a></li>
    <li><a href="#locators">Using Locator and ServiceLocator</a></li>
</ol>

<p>Then we'll take a look at how to put it all together.</p>
<ol class="toc">
  <li><a href="#wiring">Wiring</a></li>
  <li><a href="#using">Using RequestFactory</a></li>
  <li><a href="#relationships">Entity Relationships</a></li>
  <li><a href="#validation">Validating Entities</a></li>
</ol>

<h3 id="entities">Entities</h3> <p>An entity is a domain class in your
application that has concept of a persistent identity. Generally speaking, an
entity can be persisted to a data store such as a relational database or the
Google App Engine Datastore. In persistence frameworks like JDO and JPA,
entities are annotated with @Entity. RequestFactory does not require the use of
any particular framework or annotations on your domain classes.  Here's part of
an entity definition from the <a
href="http://code.google.com/p/google-web-toolkit/source/browse/#svn/trunk/samples/expenses">Expenses
sample application</a> found in the GWT distribution.</p>

<pre class="prettyprint">
package com.google.gwt.sample.expenses.server.domain;

/**
 * The Employee domain object.
 */
@Entity
public class Employee {

  @Size(min = 3, max = 30)
  private String userName;

  private String department;

  @NotNull
  private String displayName;

  private String password;

  @JoinColumn
  private Long supervisorKey;

  @Id
  @Column(name = &quot;id&quot;)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Version
  @Column(name = &quot;version&quot;)
  private Integer version;

  @Transient
  private Employee supervisor;

  public String getDepartment() {
    return department;
  }

  public String getDisplayName() {
    return this.displayName;
  }
  ...
}
</pre>

<h3 id="proxies">Entity Proxies</h3> <p>An entity proxy is a client-side
representation of a server-side entity. The proxy interfaces are implemented by
RequestFactory and they fill the role of a DTO (Data Transfer Object) in
GWT-RPC.  RequestFactory automatically propagates bean-style properties between
entities on the server and the corresponding EntityProxy on the client.
Furthermore, the EntityProxy interface enables RequestFactory to compute and
send only changes (&quot;deltas&quot;) to the server. Here's the EntityProxy
corresponding to the Employee shown above.</p>

<pre class="prettyprint">
@ProxyFor(Employee.class)
public interface EmployeeProxy extends EntityProxy {

  String getDepartment();

  String getDisplayName();

  Long getId();

  String getPassword();

  EmployeeProxy getSupervisor();

  String getUserName();

  void setDepartment(String department);

  void setDisplayName(String displayName);

  void setPassword(String password);

  void setSupervisor(EmployeeProxy supervisor);

  void setUserName(String userName);
}
</pre>

<p>Entity proxies simply extend the EntityProxy interface and use the @ProxyFor
or @ProxyForName annotation to reference the server-side entity being
represented. It is not necessary to represent every property and method from
the server-side entity in the EntityProxy, only getters and setters for
properties that should be exposed to the client. Note that while getId() is
shown in this example, most client code will want to refer to
EntityProxy.stableId() instead, as the EntityProxyId returned by this method is
used throughout RequestFactory-related classes.</p>

<p>Also note that the
getSupervisor() method returns another proxy class (EmployeeProxy). All
client-side code must reference EntityProxy subclasses. RequestFactory
automatically converts proxy types to their corresponding entity types
on the server.</p>
<h3 id="valueProxies">Value Proxies</h3>
<p>A value proxy can be used to represent any type. Unlike an EntityProxy, a ValueProxy is not required to expose an ID and version. ValueProxy  is often used to represent  embedded object types within entities. For example, a Person entity in a contact management application might represent Address as an embedded type so it will be persisted as a serialized object within the person entity.</p>
<pre class="prettyprint">
@Entity
public class Person {
  @Id
  private Long id;
  private Integer version = 0;
  private String firstName, lastName;
  @Embedded
  private Address address;
  ...
}
</pre>
<p>The Address type is just a POJO with no persistence annotations:</p>
<pre class="prettyprint">
public class Address {
  private String street1;
  private String street2;
  private String city;
  private String st;
  private String zip;
  ...
}
</pre>
<p>In the client, Address is represented as a ValueProxy and referenced by the containing EntityProxy:</p>
<pre class="prettyprint">
public interface AddressProxy extends ValueProxy {
  public String getStreet1();
  public String getStreet2();
  public String getCity();
  public String getSt();
  public String getZip();
  ...
}

public interface PersonProxy extends EntityProxy {
  Long getId();
  Integer getVersion();
  String getFirstName();
  String getLastName();
  AddressProxy getAddress();
   ...
}
</pre>
<p>ValueProxy can be used to pass any bean-like type to and from the server with RequestFactory.</p>

<h3 id="interface">RequestFactory Interface</h3>
<p>As with GWT-RPC, you define the
interface between your client and server code by extending an interface.
You define one RequestFactory interface for your application, and it
consists of methods that return service stubs. Here's an example from
the Expenses sample app:</p>

<pre class="prettyprint">
public interface ExpensesRequestFactory extends RequestFactory {

  EmployeeRequest employeeRequest();

  ExpenseRequest expenseRequest();

  ReportRequest reportRequest();

}
</pre>

<p>The EmployeeRequest service stub looks like this:</p>

<pre class="prettyprint">
@Service(Employee.class)
public interface EmployeeRequest extends RequestContext {

  Request&lt;Long&gt; countEmployees();

  Request&lt;Long&gt; countEmployeesByDepartment(
      String department);

  Request&lt;List&lt;EmployeeProxy&gt;&gt; findAllEmployees();

  Request&lt;EmployeeProxy&gt; findEmployee(Long id);

  Request&lt;List&lt;EmployeeProxy&gt;&gt; findEmployeeEntries(int firstResult,
      int maxResults);

  Request&lt;List&lt;EmployeeProxy&gt;&gt; findEmployeeEntriesByDepartment(
      String department, int firstResult, int maxResults);

  InstanceRequest&lt;EmployeeProxy, Void&gt; persist();

  InstanceRequest&lt;EmployeeProxy, Void&gt; remove();

}
</pre>

<p>The RequestFactory service stubs must extend RequestContext and
use the @Service or @ServiceName annotation to name the associated service
implementation class on the server. The methods in a service stub do not
return entities directly, but rather return subclasses of
com.google.web.bindery.requestfactory.shared.Request. This allows the methods on
the interface to be invoked asynchronously with Request.fire() similar
to passing an AsyncCallback object to each service method in GWT-RPC.</p>

<pre class="prettyprint">
requestFactory.employeeRequest().findEmployee(employeeId).fire(
    new Receiver&lt;EmployeeProxy&gt;() {
      @Override
      public void onSuccess(EmployeeProxy employee) {
      ...
      }
    });
</pre>

<p>Just like GWT-RPC callers pass an AsyncCallback that implements
onSuccess() and onFailure(), Request.fire() takes a Receiver which must
implement onSuccess(). Note that Receiver is an abstract class having a
default implementation of onFailure() so that you don't have to handle
the failure case each time a Request is invoked. To change the default
implementation, which simply throws a RuntimeException, you can extend
Receiver and override onFailure() with a suitable default implementation
for your application. You may also want to override the default
implementation of onConstraintViolation(), which returns any constraint violations
on the server (see <a href="#validation">Validating Entities</a> below).</p>

<p>The Request type returned
from each method is parameterized with the return type of the service
method. The type parameter becomes the type expected by the Receiver's
onSuccess() method as in the example above. Methods that have no return
value should return type Request&lt;Void&gt;. Requests can be parameterized with the following types:</p>

<ul>
  <li>Built-in value types: BigDecimal, BigInteger, Boolean, Byte, Enum, Character, Date, Double, Float, Integer, Long, Short, String, Void</li>
  <li>Custom value types: any subclass of ValueProxy</li>
  <li>Entity types: any subclass of EntityProxy</li>
  <li>Collections: List&lt;T&gt; or Set&lt;T&gt;, where T is one of the above value or entity types, or Map&lt;K,V&gt; where K and V are one of the above value or entity types</li>
</ul>

<p> Instance methods that operate on an entity itself, like persist() and
remove(), return objects of type InstanceRequest rather than Request. This is
explained further in the next section.</p>

<h3 id="transportable">Transportable types</h3>
<p>RequestFactory restricts the types that may be used as proxy properties and service method parameters.  Collectively, these types are referred to
as <em>transportable types</em>.  Each client-side transportable type is mapped to a server-side domain type.  The mapping rules are as follows:

<table width="100%">
  <caption style="font-weight:bold; text-align:center; margin-bottom:8px;">
    Mapping of transportable types to domain types
  </caption>
  <tr>
    <th>Client type</th>
    <th>Domain type</th>
  </tr>
  <tr>
    <td>Primitive type (e.g. <code>int</code>)</td>
    <td>Primitive type</td>
  </tr>
  <tr>
    <td>Boxed primitive type (e.g. <code>Integer</code>)</td>
    <td>Boxed primitive type</td>
  </tr>
  <tr>
    <td>Other value types: Enums, BigInteger, BigDecimal, Date</td>
    <td>Other value type</td>
  <tr>
    <td><code>@ProxyFor(Foo.class) FooProxy extends EntityProxy</code></td>
    <td>A <code>Foo</code> entity</td>
  </tr>
  <tr>
    <td><code>@ProxyFor(Bar.class) BarProxy extends ValueProxy</code></td>
    <td>A <code>Bar</code> value object</td> 
  </tr>
  <tr>
    <td><code>Set&lt;T&gt;</code> or <code>List&lt;T&gt;</code> where <code>T</code> is a transportable type</td>
    <td><code>Set&lt;T&gt;</code> or <code>List&lt;T&gt;</code></td>
  <tr>
    <td><code>Map&lt;K,V&gt;</code> where <code>K</code> and <code>V</code> are transportable types</td>
    <td><code>Map&lt;K,V&gt;</code></td>
</table>

<p>In determining whether or not a client-side method defined in a proxy or
context type is a valid mapping for a domain method, the client types are
converted to ther domain equivalent and regular Java type assignability rules
are considered.

<p>A proxy type will be available on the client if it is:
<ul><li>
Referenced from a RequestContext as a Request parameter or return type.
</li><li>
Referenced from a referenced proxy.
</li><li>
A supertype of a referenced proxy that is also a proxy (i.e. assignable to EntityProxy or ValueProxy and has an @ProxyFor(Name) annotation).
</li><li>
Referenced via an @ExtraTypes annotation placed on the RequestFactory, RequestContext, or a referenced proxy. Adding an @ExtraTypes annotation on the RequestFactory or RequestContext allows you to add subtypes to "some else's" proxy types.
</li></ul>

<p>Polymorphic type-mapping rules:
<ul><li>
All properties defined in a proxy type or inherited from super-interfaces must be available on the domain type.
This allows a proxy interface to extend a "mix-in" interface.
</li><li>
All proxies must map to a single domain type via a @ProxyFor(Name) annotation.
</li><li>
The @ProxyFor of the proxy instance is used to determine which concrete type on the server to instantiate.
</li><li>
Any supertypes of a proxy interface that are assignable to EntityProxy or ValueProxy and have an @ProxyFor(Name) annotation must be valid proxies.
Given BProxy extends AProxy: if only BProxy is referenced (e.g. via @ExtraTypes), it is still permissible to create an AProxy.
</li><li>
Type relationships between proxy interfaces do not require any particular type relationship between the mapped domain types.
Given BProxy extends AProxy: it is allowable for BEntity not to be a subclass of AEntity.
This allows for duck-type-mapping of domain objects to proxy interfaces.
</li><li>
To return a domain object via a proxy interface, the declared proxy return type must map to a domain type assignable to the returned domain object.
</li><li>
The specific returned proxy type will be the most-derived type assignable to the declared proxy type that also maps to the returned domain type or one of its supertypes.</li></ul>

<h3 id="impl">Server Implementations</h3>

<p>Services can be implemented on the server in one of two ways: as static
methods in a type or as instance methods in a service class accompanied by a
ServiceLocator.</p>

<p>In either case, the methods defined in a service interface are implemented
in the class named in the @Service or @ServiceName annotation. Unlike GWT-RPC,
service implementations do not directly implement the RequestContext interface.
The server-side service must implement each method defined in the
service's RequestContext interface even though the implementation does not
formally implement the RequestContext interface. The name and argument list for
each method is the same on client and server, with the following mapping
rules:</p>

<ul>
  <li>Client side methods that return Request&lt;T&gt; return only T on the server. For example, a method that returns Request&lt;String&gt; in the client interface simply returns String on the server.</li>
  <li>EntityProxy types become the domain entity type on the server, so a method that returns Request&lt;List&lt;EmployeeProxy&gt;&gt; will just return List&lt;Employee&gt; on the server.</li>
  <li>Methods that return a Request object in the client interface are implemented as static methods in the service class. Alternatively, they may be implemented as instance methods in a service object returned by a ServiceLocator.</li>
  <li>Methods that operate on an instance of an entity, like persist() and remove(), return an InstanceRequest object in the client interface. Instance methods do not pass the instance directly, but rather via the <code>using()</code> method on the InstanceRequest. On the server, instance methods must be implemented as non-static methods in the entity type.</li>
</ul>
<p>The RequestFactory servlet requires four special methods for all entities. They may be implemented either in the entity itself or in a default-instantiable type  that implements the <code>Locator</code> interface. The required methods are summarized in the table below.
<table width="100%">
  <caption style="font-weight:bold; text-align:center; margin-bottom:8px;">
    Entity locator methods can be implemented in the entity or a Locator class
    </caption>
  <tr>
    <th width="8%">Method</td>
    <th width="15%">Directly in entity</td>
    <th width="18%">Locator&lt;T,I&gt; impl</td>
    <th width="59%">Description</td>
  </tr>
  <tr>
    <td>Constructor</td>
    <td>no-arg constructor</td>
    <td>T create(Class&lt;? extends T&gt; clazz)</td>
    <td>Returns  a new entity instance</td>
  </tr>
  <tr>
    <td>getId</td>
    <td>id_type getId()</td>
    <td>I getId(T domainObject)</td>
    <td>Returns an entity's persisted ID, which may be any transportable type. The ID is typically auto-generated by the
  persistence engine (JDO, JPA, Objectify, etc.)</td>
  </tr>
  <tr>
    <td>find by ID</td>
    <td>static findEntity(id_type id)</td>
    <td>T find(Class&lt;? extends T&gt; clazz, I id)</td>
    <td>Returns the persisted entity for an ID. When implemented directly in the entity, this method has a special naming convention. On the
  client side, there is a find() method defined in the RequestFactory
  interface which service interfaces extend. On the server, the method is
  named &quot;find&quot; plus the type's simple name, like findEmployee, and takes
  an argument of the entity's ID type.</td>
  </tr>
  <tr>
    <td>getVersion</td>
    <td>Integer getVersion()</td>
    <td>Object getVersion(T domainObject)</td>
    <td>Used by RequestFactory to infer if an entity has changed. The
  backing store (JDO, JPA, etc.) is responsible for updating the version
  each time the object is persisted, and RequestFactory calls getVersion()
  to learn of changes. This information is used in two places. First, the
  RequestFactoryServlet sends an UPDATE event to the client if an entity
  changes as a result of the method invocation on the server, for example,
  when a call to persist an editable entity results in an updated version
  on the server. Second, the client maintains a version cache of recently
  seen entities. Whenever it sees an entity whose version has changed, it
  fires UPDATE events on the event bus so that listeners can update the view.</td>
  </tr>
</table>

<h3 id="entity_service">Implementing a service in an entity class</h3> <p>When
mapping a service, methods that return a Request object in the client interface
are implemented as static methods in a service class, like
Employee.findAllEmployees() in the example below. Here is more of the Employee
entity in the Expenses sample project:</p>

<pre class="prettyprint">
// The Employee domain object
@Entity public class Employee {

// properties, getters, and setters omitted
  public static List&lt;Employee&gt; findAllEmployees() {
    EntityManager em = entityManager();
    try {
      List&lt;Employee&gt; list = em.createQuery(&quot;select o from Employee o&quot;).getResultList();
      // force to get all the employees
      list.size();
      return list;
    } finally {
      em.close();
    }
  }

  public static Employee findEmployee(Long id) {
    if (id == null) {
      return null;
    }
    EntityManager em = entityManager();
    try {
      Employee employee = em.find(Employee.class, id);
      return employee;
    } finally {
      em.close();
    }
  }

  public static final EntityManager entityManager() {
    return EMF.get().createEntityManager();
  }

  public void persist() {
    EntityManager em = entityManager();
    try {
      em.persist(this);
    } finally {
      em.close();
    }
  }

  public void remove() {
    EntityManager em = entityManager();
    try {
      Employee attached = em.find(Employee.class, this.id);
      em.remove(attached);
    } finally {
      em.close();
    }
  }

  ...

}
</pre>
<h3 id="locators">Using Locator and ServiceLocator</h3>
<p>What if you don't want to implement persistence code in  an entity itself? To implement the required entity locator methods, create an entity locator class that extends Locator&lt;T,I&gt;:</p>
<pre class="prettyprint">
public class EmployeeLocator extends Locator&lt;Employee, Long&gt; {
  @Override
  public Employee create(Class&lt;? extends Employee&gt; clazz)
  {
    return new Employee();
  }
    ...
}
</pre>
<p>Then associate it with the entity in the @ProxyFor annotation:</p>
<pre class="prettyprint">
@ProxyFor(value = Employee.class, locator = EmployeeLocator.class)
  public interface EmployeeProxy extends EntityProxy {
    ...
  }
</pre>
<p>Since many persistence frameworks offer generic find/get/query methods, it's also possible to create a generic Locator class and specify it in the @ProxyFor annotation for each entity type.
To do this, all your entities can extend a base class that provides getId() and getVersion(). Alternatively, the generic Locator can use reflection to call getId() and getVersion() when needed.</p>
<p>Many persistence frameworks also  make it possible to utilize a generic DAO class where entity DAOs extend the generic DAO and override methods as needed. One of the benefits of  RequestFactory is that it's possible to expose DAO classes directly as services. However, inheriting service methods from a base class doesn't work if services are implemented as static methods in an entity class. Fortunately, you can use a ServiceLocator to tell RequestFactory how to obtain an instance of a service. When using a ServiceLocator, RequestFactory will invoke methods that return a Request type as instance methods instead of static methods.</p>
<p>To use a ServiceLocator, simply implement the ServiceLocator interface. It may be as simple as this:</p>
<pre class="prettyprint">
public class MyServiceLocator implements ServiceLocator {
  @Override
  public Object getInstance(Class&lt;?&gt; clazz) {
    try {
      return clazz.newInstance();
    } catch (InstantiationException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
}
</pre>
<p>Then annotate the service interface with the name of the service and locator class:</p>
<pre class="prettyprint">
@Service(value = EmployeeDao.class, locator = MyServiceLocator.class)
interface EmployeeRequestContext extends RequestContext
</pre>
<p class="note">Note: RequestFactory caches ServiceLocator and service instances, so make sure both are thread-safe. </p>

<h2 id="allTogether">Putting it all together</h2>

<h3 id="wiring">Wiring</h3>

<p>In order to use RequestFactory, add the following line to your .gwt.xml:</p>

<pre class="prettyprint">
&lt;inherits name='com.google.web.bindery.requestfactory.RequestFactory' /&gt;
</pre>

<p>Add the following jars to your <code>WEB-INF/lib</code> directory:
<ul> <li>
<code>requestfactory-server.jar</code>
</li><li>
<code>javax/validation/validator-api-1.0.0.GA.jar</code>
</li><li>
A JSR 303 Validator of your choice, such as <code>hibernate-validator</code>
</li></ul>

<p>Map RequestFactoryServlet in web.xml:</p>

<pre>
&lt;servlet&gt;
	&lt;servlet-name&gt;requestFactoryServlet&lt;/servlet-name&gt;
	&lt;servlet-class&gt;com.google.web.bindery.requestfactory.server.RequestFactoryServlet&lt;/servlet-class&gt;
	&lt;init-param&gt;
		&lt;param-name&gt;symbolMapsDirectory&lt;/param-name&gt;
		&lt;!-- You'll need to compile with -extras and move the symbolMaps directory 
			to this location if you want stack trace deobfuscation to work --&gt;
		&lt;param-value&gt;WEB-INF/classes/symbolMaps/&lt;/param-value&gt;
	&lt;/init-param&gt;
&lt;/servlet&gt;

&lt;servlet-mapping&gt;
	&lt;servlet-name&gt;requestFactoryServlet&lt;/servlet-name&gt;
	&lt;url-pattern&gt;/gwtRequest&lt;/url-pattern&gt;
&lt;/servlet-mapping&gt;
</pre>

<p>Once you've
created your entities, EntityProxy types, and RequestFactory with its
service interfaces, you bring it to life with GWT.create() and
initialize it with your application's EventBus:</p>

<pre class="prettyprint">
final EventBus eventBus = new SimpleEventBus();
requestFactory = GWT.create(ExpensesRequestFactory.class);
requestFactory.initialize(eventBus);
</pre>

<h3 id="using">Using RequestFactory</h3>

<p>Now we're
ready to put RequestFactory to work. To create a new entity on the
client, call RequestContext.create() on the EntityProxy type, then
persist it using a method defined in the entity's service interface.
Using the example code above to create a new Employee object and persist
it to the database, we would write:</p>

<pre class="prettyprint">
EmployeeRequest request = requestFactory.employeeRequest();
EmployeeProxy newEmployee = request.create(EmployeeProxy.class);
newEmployee.setDisplayName(...);
newEmployee.setDepartment(...);
...
Request&lt;Void&gt; createReq = request.persist().using(newEmployee);
</pre>

<p>All client-side code should use the EmployeeProxy, not the
Employee entity itself. This way, the domain object need not be
GWT-compatible, unlike GWT-RPC, where the same concrete type is used on
both client and server. EmployeeProxy has no constructor because it's an
interface, not a class, so you must instantiate it with
requestContext.create(EmployeeProxy.class). The benefit of this approach
is that it allows RequestFactory to track object creation and
modification so it can send only changes to the server.</p>

<p>Note that the persist() method has no arguments, which is
consistent with the method's implementation in the Employee entity. In
the EmployeeRequest interface, it returns type InstanceRequest, which in
turn gets the instance on which the method will be invoked from the
using() method as shown above. Alternatively, if using a ServiceLocator, the persist() method could be declared as Request&lt;Void&gt; persist(Employee emp), in which case  newEmployee would be passed as an argument to the persist() method in place of the using() method.</p>

<p>Now let's add the code to save the newly created employee to the server:</p>

<pre class="prettyprint">
createReq.fire(new Receiver&lt;Void&gt;()
{
  @Override
    public void onSuccess(Void arg0)
    {
        // Update display
    }
});
</pre>

<p>We fire the request with a Receiver, which calls us back when the request has completed.</p>

<p>Any objects not returned from RequestContext.create(), such as
those received from the server, must be enabled for changes by calling
the RequestFactory's edit() method. Any EntityProxies returned from the
getters of an editable proxy are also editable.</p>

<pre class="prettyprint">
EmployeeProxy editableEmployee = request.edit(returnedEmployee);
editableEmployee.setDepartment(newDepartment);
...
Request&lt;Void&gt; updateReq = request.persist().using(editableEmployee);
</pre>

<p>The edit() method returns a new copy of the immutable object, and
the original can be discarded. To send changes to the server, you create
a new server request using a method defined in the service interface
method and fire it as shown above. All edits occur with a particular
context, and when the context is fired, all of those edits are sent, so
any method invocation will result in changes being sent to the server.</p>

<h3 id="relationships">Entity Relationships</h3>

<p>Changes to related entities can be persisted in a single request.
For example, this code from the 
<a href="http://code.google.com/p/google-web-toolkit/source/browse/#svn/trunk/samples/dynatablerf">DynatableRF sample app</a> in GWT trunk creates a new Person and Address at the same time:</p>

<pre class="prettyprint">
PersonRequest context = requestFactory.personRequest();
AddressProxy address = context.create(AddressProxy.class);
PersonProxy person = context.create(PersonProxy.class);
person.setAddress(address);
context.persist().using(person).fire(...);
</pre>

<p>RequestFactory automatically sends the whole object graph in a
single request. In this case, the implementation of Person.persist() on
the server is responsible for persisting the related Address also, which
may or may not happen automatically, depending on the ORM framework and
how the relationship is defined. Note that RequestFactory does not
currently support embedded objects (@Embedded in various ORM frameworks)
because it expects every entity to exist independently with its own ID.</p>

<p>When querying the server, RequestFactory does not automatically
populate relations in the object graph. To do this, use the with()
method on a request and specify the related property name as a String:</p>

<pre class="prettyprint">
Request<Person> findReq = requestFactory.personRequest().find(personId).with(&quot;address&quot;);
</pre>

<p>It is also necessary to use the with() method to retrieve any properties with types extending ValueProxy. The with() method takes multiple String arguments, so you can specify multiple property names at once. To specify nested properties, use dot notation. Putting it all together, you might have</p>

<pre class="prettyprint">
Request<Person> findReq = find(personId).with("phone","address.city","address.zip")
</pre>

<h3 id="validation">Validating Entities</h3>

<p>RequestFactory supports JSR 303 bean validation. This makes it
possible to keep validation rules on the server and notify the client
when an entity cannot be persisted due to validation failures. To use
it, ensure that a JSR 303 implementation is available in the server
classpath and annotate your entities with the java.validation
annotations like @Size and @NotNull as shown in the Employee entity
above. Prior to invoking a service method on the server, RequestFactory
will call the validation framework and send any ConstraintViolations
from the server to the client, which will then call the onViolation()
method on the Receiver for the request.</p>

<h2>Conclusion</h2>

<p>RequestFactory is the heart of the new &quot;Bindery&quot; features in GWT
2.1. In future articles, we'll look at integration with cell widgets,
Editors, the event bus, and Activities and Places.</p>
