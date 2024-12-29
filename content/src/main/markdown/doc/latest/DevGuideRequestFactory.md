Request Factory
===

RequestFactory is an alternative to GWT-RPC for creating data-oriented
services. RequestFactory and its related interfaces (RequestContext and
EntityProxy) make it easy to build data-oriented (CRUD) apps with an
ORM-like interface on the client. It is designed to be used with an ORM
layer like JDO or JPA on the server, although this is not required.

## Overview

### Benefits

RequestFactory makes it easy to implement a data
access layer on both client and server. It allows you to structure your
server-side code in a data-centric way and provides a higher level of
abstraction than GWT-RPC, which is service-oriented rather than
data-oriented. On the client side, RequestFactory keeps track of objects
that have been modified and sends only changes to the server, which
results in very lightweight network payloads. In addition,
RequestFactory provides a solid foundation for automatic batching and
caching of requests in the future.

### How does it relate to GWT-RPC?

RequestFactory uses its own servlet, RequestFactoryServlet, and
implements its own protocol for data exchange between client and server.
RequestFactory takes a more prescriptive approach to the client-server
programming model than GWT-RPC does.  GWT-RPC uses remote methods as its
basic building blocks (similar to Java RMI), whereas RequestFactory uses
entities (data with a persistent identity) and services. See this external
site for a [discussion on
RequestFactory versus GWT-RPC](http://stackoverflow.com/q/4119867/607050).

## Coding with RequestFactory

Let's take a look at the moving parts in an application that uses RequestFactory.

1.  [Entity](#entities)
2.  [Entity Proxies](#proxies)
3.  [Value Proxies](#valueProxies)
4.  [RequestFactory Interface](#interface)
5.  [Transportable types](#transportable)
6.  [Server Implementations](#impl)
7.  [Implementing a service in an entity class](#entity_service)
8.  [Using Locator and ServiceLocator](#locators)

Then we'll take a look at how to put it all together.

1.  [Wiring](#wiring)
2.  [Using RequestFactory](#using)
3.  [Entity Relationships](#relationships)
4.  [Validating Entities](#validation)

### Entities

An entity is a domain class in your
application that has concept of a persistent identity. Generally speaking, an
entity can be persisted to a data store such as a relational database or the
Google App Engine Datastore. In persistence frameworks like JDO and JPA,
entities are annotated with @Entity. RequestFactory does not require the use of
any particular framework or annotations on your domain classes.  Here's part of
an entity definition from the [Mobile
web application](https://github.com/gwtproject/gwt/blob/main/samples/mobilewebapp) found in the GWT distribution.

```java
package com.google.gwt.sample.mobilewebapp.server.domain;

/**
 * A task used in the task list. 
 */
@Entity
public class Task {

  @Id
  Long id;

  private Date dueDate;

  @NotNull(message = "You must specify a name")
  @Size(min = 3, message = "Name must be at least 3 characters long")
  private String name;

  private String notes;

  /**
   * The unique ID of the user who owns this task.
   */
  @Index
  private String userId;

  /**
   * Get the due date of the Task.
   */
  public Date getDueDate() {
    return dueDate;
  }

  /**
   * Get the unique ID of the Task.
   */
  public Long getId() {
    return id;
  }

  /**
   * Get the name of the Task.
   */
  public String getName() {
    return name;
  }

  /**
   * Get the notes associated with the task.
   */
  public String getNotes() {
    return notes;
  }

  ...
}
```

### Entity Proxies

An entity proxy is a client-side
representation of a server-side entity. The proxy interfaces are implemented by
RequestFactory and they fill the role of a DTO (Data Transfer Object) in
GWT-RPC.  RequestFactory automatically propagates bean-style properties between
entities on the server and the corresponding EntityProxy on the client.
Furthermore, the EntityProxy interface enables RequestFactory to compute and
send only changes ("deltas") to the server. Here's the EntityProxy
corresponding to the Employee shown above.

```java
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
```

Entity proxies simply extend the EntityProxy interface and use the @ProxyFor
or @ProxyForName annotation to reference the server-side entity being
represented. It is not necessary to represent every property and method from
the server-side entity in the EntityProxy, only getters and setters for
properties that should be exposed to the client. Note that while getId() is
shown in this example, most client code will want to refer to
EntityProxy.stableId() instead, as the EntityProxyId returned by this method is
used throughout RequestFactory-related classes.

Also note that the
getSupervisor() method returns another proxy class (EmployeeProxy). All
client-side code must reference EntityProxy subclasses. RequestFactory
automatically converts proxy types to their corresponding entity types
on the server.

### Value Proxies

A value proxy can be used to represent any type. Unlike an EntityProxy, a ValueProxy is not required to expose an ID and version. ValueProxy  is often used to represent  embedded object types within entities. For example, a Person entity in a contact management application might represent Address as an embedded type so it will be persisted as a serialized object within the person entity.

```java
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
```

The Address type is just a POJO with no persistence annotations:

```java
public class Address {
  private String street1;
  private String street2;
  private String city;
  private String st;
  private String zip;
  ...
}
```

In the client, Address is represented as a ValueProxy and referenced by the containing EntityProxy:

```java
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
```

ValueProxy can be used to pass any bean-like type to and from the server with RequestFactory.

### RequestFactory Interface

As with GWT-RPC, you define the
interface between your client and server code by extending an interface.
You define one RequestFactory interface for your application, and it
consists of methods that return service stubs. Here's an example from
the Expenses sample app:

```java
public interface ExpensesRequestFactory extends RequestFactory {

  EmployeeRequest employeeRequest();

  ExpenseRequest expenseRequest();

  ReportRequest reportRequest();

}
```

The EmployeeRequest service stub looks like this:

```java
@Service(Employee.class)
public interface EmployeeRequest extends RequestContext {

  Request<Long> countEmployees();

  Request<Long> countEmployeesByDepartment(
      String department);

  Request<List<EmployeeProxy>> findAllEmployees();

  Request<EmployeeProxy> findEmployee(Long id);

  Request<List<EmployeeProxy>> findEmployeeEntries(int firstResult,
      int maxResults);

  Request<List<EmployeeProxy>> findEmployeeEntriesByDepartment(
      String department, int firstResult, int maxResults);

  InstanceRequest<EmployeeProxy, Void> persist();

  InstanceRequest<EmployeeProxy, Void> remove();

}
```

The RequestFactory service stubs must extend RequestContext and
use the @Service or @ServiceName annotation to name the associated service
implementation class on the server. The methods in a service stub do not
return entities directly, but rather return subclasses of
com.google.web.bindery.requestfactory.shared.Request. This allows the methods on
the interface to be invoked asynchronously with Request.fire() similar
to passing an AsyncCallback object to each service method in GWT-RPC.

```java
requestFactory.employeeRequest().findEmployee(employeeId).fire(
    new Receiver<EmployeeProxy>() {
      @Override
      public void onSuccess(EmployeeProxy employee) {
      ...
      }
    });
```

Just like GWT-RPC callers pass an AsyncCallback that implements
onSuccess() and onFailure(), Request.fire() takes a Receiver which must
implement onSuccess(). Note that Receiver is an abstract class having a
default implementation of onFailure() so that you don't have to handle
the failure case each time a Request is invoked. To change the default
implementation, which simply throws a RuntimeException, you can extend
Receiver and override onFailure() with a suitable default implementation
for your application. You may also want to override the default
implementation of onConstraintViolation(), which returns any constraint violations
on the server (see [Validating Entities](#validation) below).

The Request type returned
from each method is parameterized with the return type of the service
method. The type parameter becomes the type expected by the Receiver's
onSuccess() method as in the example above. Methods that have no return
value should return type `Request<Void>` Requests can be parameterized with the following types:

*   Built-in value types: BigDecimal, BigInteger, Boolean, Byte, Enum, Character, Date, Double, Float, Integer, Long, Short, String, Void
*   Custom value types: any subclass of ValueProxy
*   Entity types: any subclass of EntityProxy
*   Collections: `List<T>`or `Set<T>` where T is one of the above value or entity types, or
`Map<K,V>`where K and V are one of the above value or entity types

Instance methods that operate on an entity itself, like persist() and
remove(), return objects of type InstanceRequest rather than Request. This is
explained further in the next section.

### Transportable types

RequestFactory restricts the types that may be used as proxy properties and service method parameters.  Collectively, these types are referred to
as _transportable types_.  Each client-side transportable type is mapped to a server-side domain type.  The mapping rules are as follows:

<table width="100%">
  <caption style="font-weight:bold; text-align:center; margin-bottom:8px;">
    Mapping of transportable types to domain types
  </caption>
  <tr>
    <th>Client type</th>
    <th>Domain type</th>
  </tr>
  <tr>
    <td>Primitive type (e.g. `int`)</td>
    <td>Primitive type</td>
  </tr>
  <tr>
    <td>Boxed primitive type (e.g. `Integer`)</td>
    <td>Boxed primitive type</td>
  </tr>
  <tr>
    <td>Other value types: Enums, BigInteger, BigDecimal, Date</td>
    <td>Other value type</td>
  <tr>
    <td>`@ProxyFor(Foo.class) FooProxy extends EntityProxy`</td>
    <td>A `Foo` entity</td>
  </tr>
  <tr>
    <td>`@ProxyFor(Bar.class) BarProxy extends ValueProxy`</td>
    <td>A `Bar` value object</td>
  </tr>
  <tr>
    <td>`Set<T>` or `List<T>` where `T` is a transportable type</td>
    <td>`Set<T>` or `List<T>`</td>
  <tr>
    <td>`Map<K,V>` where `K` and `V` are transportable types</td>
    <td>`Map<K,V>`</td>
</table>

In determining whether or not a client-side method defined in a proxy or
context type is a valid mapping for a domain method, the client types are
converted to ther domain equivalent and regular Java type assignability rules
are considered.

A proxy type will be available on the client if it is:

*   Referenced from a RequestContext as a Request parameter or return type.
*   Referenced from a referenced proxy.
*   A supertype of a referenced proxy that is also a proxy (i.e. assignable to EntityProxy or ValueProxy and has an @ProxyFor(Name) annotation).
*   Referenced via an @ExtraTypes annotation placed on the RequestFactory, RequestContext, or a referenced proxy. Adding an @ExtraTypes annotation on the RequestFactory or RequestContext allows you to add subtypes to "some else's" proxy types.

Polymorphic type-mapping rules:

*   All properties defined in a proxy type or inherited from super-interfaces must be available on the domain type. This allows a proxy interface to extend a "mix-in" interface.
*   All proxies must map to a single domain type via a @ProxyFor(Name) annotation.
*   The @ProxyFor of the proxy instance is used to determine which concrete type on the server to instantiate.
*   Any supertypes of a proxy interface that are assignable to EntityProxy or ValueProxy and have an @ProxyFor(Name) annotation must be valid proxies. Given BProxy extends AProxy: if only BProxy is referenced (e.g. via @ExtraTypes), it is still permissible to create an AProxy.
*   Type relationships between proxy interfaces do not require any particular type relationship between the mapped domain types. Given BProxy extends AProxy: it is allowable for BEntity not to be a subclass of AEntity. This allows for duck-type-mapping of domain objects to proxy interfaces.
*   To return a domain object via a proxy interface, the declared proxy return type must map to a domain type assignable to the returned domain object.
*   The specific returned proxy type will be the most-derived type assignable to the declared proxy type that also maps to the returned domain type or one of its supertypes.</li></ul>

### Server Implementations<a id="impl"></a>

Services can be implemented on the server in one of two ways: as static
methods in a type or as instance methods in a service class accompanied by a
ServiceLocator.

In either case, the methods defined in a service interface are implemented
in the class named in the @Service or @ServiceName annotation. Unlike GWT-RPC,
service implementations do not directly implement the RequestContext interface.
The server-side service must implement each method defined in the
service's RequestContext interface even though the implementation does not
formally implement the RequestContext interface. The name and argument list for
each method is the same on client and server, with the following mapping
rules:

*   Client side methods that return `Reques<T>`return only T on the server. For example, a method
that returns `Request<String>` in the client interface simply returns String on the server.
*   EntityProxy types become the domain entity type on the server, so a method that returns
`Request<List<EmployeeProxy>>` will just return `List<Employee>` on the server.
*   Methods that return a Request object in the client interface are implemented as static methods in the service class. Alternatively, they may be implemented as instance methods in a service object returned by a ServiceLocator.
*   Methods that operate on an instance of an entity, like persist() and remove(), return an InstanceRequest object in the client interface. Instance methods do not pass the instance directly, but rather via the `using()` method on the InstanceRequest. On the server, instance methods must be implemented as non-static methods in the entity type.

The RequestFactory servlet requires four special methods for all entities. They may be implemented either in the entity itself or in a default-instantiable type  that implements the `Locator` interface. The required methods are summarized in the table below.

<table width="100%">
  <caption style="font-weight:bold; text-align:center; margin-bottom:8px;">
    Entity locator methods can be implemented in the entity or a Locator class
    </caption>
  <tr>
    <th width="8%">Method</td>
    <th width="15%">Directly in entity</td>
    <th width="18%">`Locator<T,I> impl`</td>
    <th width="59%">Description</td>
  </tr>
  <tr>
    <td>Constructor</td>
    <td>no-arg constructor</td>
    <td>`T create(Class<? extends T> clazz)`</td>
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
    <td>`T find(Class<? extends T> clazz, I id)`</td>
    <td>Returns the persisted entity for an ID. When implemented directly in the entity, this method has a special naming convention. On the
  client side, there is a find() method defined in the RequestFactory
  interface which service interfaces extend. On the server, the method is
  named "find" plus the type's simple name, like findEmployee, and takes
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

### Implementing a service in an entity class<a id="entity_service"></a> 

When mapping a service, methods that return a Request object in the client interface
are implemented as static methods in a service class, like
Employee.findAllEmployees() in the example below. Here is more of the Employee
entity in the Expenses sample project:

```java
// The Employee domain object
@Entity public class Employee {

// properties, getters, and setters omitted
  public static List<Employee> findAllEmployees() {
    EntityManager em = entityManager();
    try {
      List<Employee> list = em.createQuery("select o from Employee o").getResultList();
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
```

### Using Locator and ServiceLocator<a id="locators"></a>

What if you don't want to implement persistence code in  an entity itself? To implement the
required entity locator methods, create an entity locator class that extends `Locator<T,I>`

```java
public class EmployeeLocator extends Locator<Employee, Long> {
  @Override
  public Employee create(Class<? extends Employee> clazz)
  {
    return new Employee();
  }
    ...
}
```

Then associate it with the entity in the @ProxyFor annotation:

```java
@ProxyFor(value = Employee.class, locator = EmployeeLocator.class)
  public interface EmployeeProxy extends EntityProxy {
    ...
  }
```

Since many persistence frameworks offer generic find/get/query methods, it's also possible to create a generic Locator class and specify it in the @ProxyFor annotation for each entity type.
To do this, all your entities can extend a base class that provides getId() and getVersion(). Alternatively, the generic Locator can use reflection to call getId() and getVersion() when needed.

Many persistence frameworks also  make it possible to utilize a generic DAO class where entity DAOs extend the generic DAO and override methods as needed. One of the benefits of  RequestFactory is that it's possible to expose DAO classes directly as services. However, inheriting service methods from a base class doesn't work if services are implemented as static methods in an entity class. Fortunately, you can use a ServiceLocator to tell RequestFactory how to obtain an instance of a service. When using a ServiceLocator, RequestFactory will invoke methods that return a Request type as instance methods instead of static methods.

To use a ServiceLocator, simply implement the ServiceLocator interface. It may be as simple as this:

```java
public class MyServiceLocator implements ServiceLocator {
  @Override
  public Object getInstance(Class<?> clazz) {
    try {
      return clazz.newInstance();
    } catch (InstantiationException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
}
```

Then annotate the service interface with the name of the service and locator class:

```java
@Service(value = EmployeeDao.class, locator = MyServiceLocator.class)
interface EmployeeRequestContext extends RequestContext
```

Note: RequestFactory caches ServiceLocator and service instances, so make sure both are thread-safe.

## Putting it all together<a id="allTogether"></a>

### Wiring<a id="wiring"></a>

In order to use RequestFactory, add the following line to your .gwt.xml:

```java
<inherits name='com.google.web.bindery.requestfactory.RequestFactory' />
```

Add the following jars to your `WEB-INF/lib` directory:

*   `requestfactory-server.jar`
*   `javax/validation/validator-api-1.0.0.GA.jar`
*   A JSR 303 Validator of your choice, such as `hibernate-validator`

Map RequestFactoryServlet in web.xml:

```xml
<servlet>
    <servlet-name>requestFactoryServlet</servlet-name>
    <servlet-class>com.google.web.bindery.requestfactory.server.RequestFactoryServlet</servlet-class>
    <init-param>
        <param-name>symbolMapsDirectory</param-name>
        <!-- You'll need to compile with -extras and move the symbolMaps directory
            to this location if you want stack trace deobfuscation to work -->
        <param-value>WEB-INF/classes/symbolMaps/</param-value>
    </init-param>
</servlet>

<servlet-mapping>
    <servlet-name>requestFactoryServlet</servlet-name>
    <url-pattern>/gwtRequest</url-pattern>
</servlet-mapping>
```

Once you've
created your entities, EntityProxy types, and RequestFactory with its
service interfaces, you bring it to life with GWT.create() and
initialize it with your application's EventBus:

```java
final EventBus eventBus = new SimpleEventBus();
requestFactory = GWT.create(ExpensesRequestFactory.class);
requestFactory.initialize(eventBus);
```

### Using RequestFactory<a id="using"></a>

Now we're
ready to put RequestFactory to work. To create a new entity on the
client, call RequestContext.create() on the EntityProxy type, then
persist it using a method defined in the entity's service interface.
Using the example code above to create a new Employee object and persist
it to the database, we would write:

```java
EmployeeRequest request = requestFactory.employeeRequest();
EmployeeProxy newEmployee = request.create(EmployeeProxy.class);
newEmployee.setDisplayName(...);
newEmployee.setDepartment(...);
...
Request<Void> createReq = request.persist().using(newEmployee);
```

All client-side code should use the EmployeeProxy, not the
Employee entity itself. This way, the domain object need not be
GWT-compatible, unlike GWT-RPC, where the same concrete type is used on
both client and server. EmployeeProxy has no constructor because it's an
interface, not a class, so you must instantiate it with
requestContext.create(EmployeeProxy.class). The benefit of this approach
is that it allows RequestFactory to track object creation and
modification so it can send only changes to the server.

Note that the persist() method has no arguments, which is
consistent with the method's implementation in the Employee entity. In
the EmployeeRequest interface, it returns type InstanceRequest, which in
turn gets the instance on which the method will be invoked from the
using() method as shown above. Alternatively, if using a ServiceLocator, the persist() method could be declared as Request<Void> persist(Employee emp), in which case  newEmployee would be passed as an argument to the persist() method in place of the using() method.

Now let's add the code to save the newly created employee to the server:

```java
createReq.fire(new Receiver<Void>()
{
  @Override
    public void onSuccess(Void arg0)
    {
        // Update display
    }
});
```

We fire the request with a Receiver, which calls us back when the request has completed.

Any objects not returned from RequestContext.create(), such as
those received from the server, must be enabled for changes by calling
the RequestFactory's edit() method. Any EntityProxies returned from the
getters of an editable proxy are also editable.

```java
EmployeeProxy editableEmployee = request.edit(returnedEmployee);
editableEmployee.setDepartment(newDepartment);
...
Request<Void> updateReq = request.persist().using(editableEmployee);
```

The edit() method returns a new copy of the immutable object, and
the original can be discarded. To send changes to the server, you create
a new server request using a method defined in the service interface
method and fire it as shown above. All edits occur with a particular
context, and when the context is fired, all of those edits are sent, so
any method invocation will result in changes being sent to the server.

### Entity Relationships<a id="relationships"></a>

Changes to related entities can be persisted in a single request.
For example, this code from the 
[DynatableRF sample app](https://github.com/gwtproject/gwt/tree/main/samples/dynatablerf) in GWT trunk creates a new Person and Address at the same time:

```java
PersonRequest context = requestFactory.personRequest();
AddressProxy address = context.create(AddressProxy.class);
PersonProxy person = context.create(PersonProxy.class);
person.setAddress(address);
context.persist().using(person).fire(...);
```

RequestFactory automatically sends the whole object graph in a
single request. In this case, the implementation of Person.persist() on
the server is responsible for persisting the related Address also, which
may or may not happen automatically, depending on the ORM framework and
how the relationship is defined. Note that RequestFactory does not
currently support embedded objects (@Embedded in various ORM frameworks)
because it expects every entity to exist independently with its own ID.

When querying the server, RequestFactory does not automatically
populate relations in the object graph. To do this, use the with()
method on a request and specify the related property name as a String:

```java
Request<Person> findReq = requestFactory.personRequest().find(personId).with("address");
```

It is also necessary to use the with() method to retrieve any properties with types extending ValueProxy. The with() method takes multiple String arguments, so you can specify multiple property names at once. To specify nested properties, use dot notation. Putting it all together, you might have

```java
Request<Person> findReq = find(personId).with("phone","address.city","address.zip")
```

### Validating Entities<a id="validation"></a>

RequestFactory supports JSR 303 bean validation. This makes it
possible to keep validation rules on the server and notify the client
when an entity cannot be persisted due to validation failures. To use
it, ensure that a JSR 303 implementation is available in the server
classpath and annotate your entities with the java.validation
annotations like @Size and @NotNull as shown in the Employee entity
above. Prior to invoking a service method on the server, RequestFactory
will call the validation framework and send any ConstraintViolations
from the server to the client, which will then call the onViolation()
method on the Receiver for the request.

## Conclusion

RequestFactory is the heart of the new "Bindery" features in GWT
2.1. In future articles, we'll look at integration with cell widgets,
Editors, the event bus, and Activities and Places.
