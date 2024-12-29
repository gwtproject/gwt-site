# AutoBean Framework

The GWT AutoBean framework provides automatically-generated implementations of bean-like interfaces and a low-level serialization mechanism for those interfaces.
AutoBeans can be used in *both client and server code* to improve code re-use.  For example, the [RequestFactory](./DevGuideRequestFactory.html) system uses AutoBeans extensively in both the client and server code.

* Goals

    * Decrease boilerplate in model-rich applications
    * Support easy encoding of AutoBeans to JSON structures
    * Provide support code for common operations on data-model objects
    * Usable in non-GWT (e.g. server) code

* Non-goals

    * Support for non-DAG datastructures
    * Support for object identity semantics
    * If higher-order model semantics are needed, the non-goals for AutoBeans are key features of the RequestFactory framework.

### Contents

* [QuickStart](#quick)
* [Property types](#properties)
* [AutoBean](#autobean)
* [AutoBeanFactory](#autobeanfactory)
* [AutoBeanCodex](#autobeancodex)
* [AutoBeanVisitor](#autobeanvisitor)
* [AutoBeanUtils](#autobeanutils)
* [JSON structures](#json)
* [Categories](#categories)


## Quickstart<a id='quick'></a>

In your module.xml file, add

```xml
<inherits name="com.google.web.bindery.autobean.AutoBean"/>
```
then in your sources:

```java
// Declare any bean-like interface with matching getters and setters, no base type is necessary
interface Person {
  Address getAddress();
  String getName();
  void setName(String name);
  void setAddress(Address a);
}

interface Address {
  // Other properties, as above
}

// Declare the factory type
interface MyFactory extends AutoBeanFactory {
  AutoBean<Address> address();
  AutoBean<Person> person();
}

class DoSomething() {
  // Instantiate the factory
  MyFactory factory = GWT.create(MyFactory.class);
  // In non-GWT code, use AutoBeanFactorySource.create(MyFactory.class);

  Person makePerson() {
    // Construct the AutoBean
    AutoBean<Person> person = factory.person();

    // Return the Person interface shim
    return person.as();
  }

  String serializeToJson(Person person) {
    // Retrieve the AutoBean controller
    AutoBean<Person> bean = AutoBeanUtils.getAutoBean(person);

    return AutoBeanCodex.encode(bean).getPayload();
  }

  Person deserializeFromJson(String json) {
    AutoBean<Person> bean = AutoBeanCodex.decode(factory, Person.class, json);
    return bean.as();
  }
}
```

## Property types<a id='properties'></a>

The following types may be used to compose AutoBean interfaces:

* Value types:

    * Primitive types and their boxed counterparts
    * `BigInteger`, `BigDecimal`
    * `java.util.Date`
    * enum types
    * Strings

* Reference types:

    * Bean-like interfaces
    * Lists or Sets of any supported property type
    * Maps of any supported property type


## AutoBean<a id='autobean'></a>

An AutoBean must be parameterized with an interface type (e.g. `AutoBean<Person>`).  This interface type may have any type hierarchy and need not extend any particular type in order to be usable with AutoBeans.  A distinction is made as to whether or not the target interface is "simple."

A simple interface satisfies the following properties:

* Has only getter and setter methods
* Any non-property methods must be implemented by a [Category](#categories)

A simple AutoBean can be constructed by the AutoBeanFactory without providing a delegate instance.

If a reference interface is returned from a method in a target interface, that instance will be automatically wrapped by an AutoBean instance.  This behavior can be disabled by placing a `@NoWrap` annotation on the AutoBeanFactory.

#### accept()

The AutoBean controller provides a visitor API to allow the properties of an AutoBean to be examined by code that has no prior knowledge of the interface being wrapped.

#### as()

The AutoBean acts as a controller for a shim object that implements the interface with which the AutoBean is parameterized.  For instance, in order to get the `Person` interface for an `AutoBean<Person>` it is necessary to call the `as()` method.  The reason for this level of indirection is to avoid any potential for method signature conflicts if the AutoBean were to also implement its target interface.

#### clone()

An AutoBean and the property values stored within it can be cloned.  The `clone()` method has a boolean parameter that will trigger a deep or a shallow copy.  Any tag values associated with the AutoBean will not be cloned.  AutoBeans that wrap a delegate object cannot be cloned.

#### getTag() / setTag()

Arbitrary metadata of any type can be associated with an AutoBean by using the AutoBean as a map-like object.  The tag values do not participate in cloning or serialization operations.

#### isFrozen() / setFrozen()

Property mutations can be disabled by calling `setFrozen()`.  Any attempts to call a setter on a frozen AutoBean will result in an `IllegalStateException`. 

#### isWrapper() / unwrap()

If the factory method used to instantiate the AutoBean provided a delegate object, the AutoBean can be detached by calling the `unwrap()` object.  The `isWrapper()` method will indicate 

## AutoBeanFactory<a id='autobeanfactory'></a>

Instead of requiring a call to `GWT.create()` for every instance of an AutoBean, only the `AutoBeanFactory` must be constructed with a call to `GWT.create()` or `AutoBeanFactorySource.create()` (in 2.1.1 and 2.2, `AutoBeanMagicSource` was named `AutoBeanFactoryMagic`).  This allows the AutoBeanFactory to be provided to consuming code by any dependency-injection pattern desired.

Methods in an AutoBeanFactory interface must return `AutoBean<Foo>`, where `Foo` is any interface type compatible with AutoBeans.  The methods may optionally declare a single parameter of type `Foo` which allows construction of an AutoBean around an existing object.

```java
interface MyFactory extends AutoBeanFactory {
  // Factory method for a simple AutoBean
  AutoBean<Person> person();

  // Factory method for a non-simple type or to wrap an existing instance
  AutoBean<Person> person(Person toWrap);
}
```

#### create()

The `create()` method accepts a `Class` object of any interface type reachable from the AutoBeanFactory interface.  An optional parameter allows a delegate object to be supplied that will be wrapped by the returned AutoBean.

## AutoBeanCodex<a id='autobeancodex'></a>

The AutoBeanCodex provides general-purpose encoding and decoding of AutoBeans into a JSON-formatted payload.

#### decode()

This method accepts an AutoBeanFactory, a `Class` object representing the top-level AutoBean interface type to be returned, and a JSON-formatted payload.  The provided AutoBeanFactory must be capable of producing AutoBeans for all interface types reachable from the provided interface.

#### encode()

This method accepts an AutoBean and returns a Splittable representing a JSON payload that contains the properties of the AutoBean and its associated object graph.

#### Splittable

The `Splittable` type is an abstraction around the low-level wire format and library used to manipulate the wire format.  For example, in the client-side code `Splittable` is a `JavaScriptObject`, while on the server it is backed by the `org.json` library.  The interface offers methods that allow the underlying data model to be queried.

Whenever the `AutoBeanCodex` encounters a `Splittable` property or collection of `Splittable`, the contents returned by the `Splittable.getPayload()` method will be injected directly into the wire format.

The `Splittable` type allows message objects backed by different `AutoBeanFactory` types to combined in a single payload, since the `Splittable` must be explicitly decoded via `AutoBeanCodex.decode()`.

## AutoBeanVisitor<a id='autobeanvisitor'></a>

AutoBeanVisitor is a concrete, no-op, base type that is intended to be extended by developers that wish to write reflection-like code over an AutoBean's target interface.

#### visit() / endVisit()

Regardless of the reference structure of an AutoBean graph, a visitor will visit any given AutoBean exactly once.  Users of the AutoBeanVisitor should not need to implement cycle-detection themselves.

As of GWT 2.1.1,  the Context interface is empty and exists to allow for future expansion.

#### visitReferenceProperty() / visitValueProperty()

The property visitation methods in an `AutoBeanVisitor` type will receive a `PropertyContext` object that allows the value of the property to be mutated as well as providing type information about the field.  Calling the `canSet()` method before calling `set()` promotes good code hygiene.

#### visitCollectionProperty() / visitMapProperty()

These visitation methods behave similarly to `visitReferenceProperty()` however the `PropertyContext` passed into these methods is specialized to provide the parameterization of the `Collection` or `Map` object.

## AutoBeanUtils<a id='autobeanutils'></a>

#### diff()

Performs a shallow comparison between the properties in two AutoBeans and returns a map of the properties that are not equal to one another.  The beans do not need to be of the same interface type, which allows for a degree of duck-typing.

#### getAllProperties()

Creates a shallow copy of the properties in the AutoBean.  Modifying the structure of the returned map will not have any effect on the state of the AutoBean.  The reference values in the map are not cloned, but are the same instances held by the AutoBean's properties.

## JSON structures<a id='json'></a>

The AutoBean framework can be used as a JSON interoperability layer to provide a Java typesystem wrapper around an existing JSON api or to create JSON payloads to interact with a remote service.  This is accomplished by designing the Java APIs according to the JSON schema.  The `@PropertyName` annotation can be applied to  getters and setters where the Java naming convention does not align with the JSON schema.

Generally speaking, the serialized form of an object emitted by AutoBeanCodex mirrors the interface declaration.  For instance, the example Person interface described in the quickstart section of this document might be serialized follows (whitespace added for clarity):

```json
{ "name" : "John Doe", "address" : { "street" : "1234 Maple St", "city" : "Nowhere" } }
```

List and Set properties are encoded as JSON lists. For example, a `List<Person>` would be encoded as:

```json
[ { "name" : "John Doe" } , { "name" : "Jim Smith" } ]
```
Maps are serialized in two forms based on whether or not the key type is a value or reference type.  Value maps are encoded as a typical JSON object.  For example, a `Map<Integer, Foo>` would be encoded as

```json
{ "1" : { "property" : "value"}, "55" : { "property" : "value" } }
```
A map that uses a reference object as a key will instead be encoded as a list of two lists. This allows object-identity maps that contain keys with identical serialized froms to be deserialized correctly. For example, a `Map<Person, Address>` would be encoded as: 

```json
[ 
  [ { "name" : "John Doe" } , { "name" : "Jim Smith" } ],
  [ { "street" : "1234 Maple Ave" }, { "street" : "5678 Fair Oaks Lane" } ]
]
```
Java enum values are written out as the string name of the enum value.  This can be overridden by applying the `PropertyName` annotation to the enum field declaration.  The use of names instead of ordinal values will allow the payloads to be robust against endpoint schema skew.

## Categories<a id='categories'></a>

Pure bean interfaces only go so far to producing a useful system.  For example, the `EntityProxy` type used by RequestFactory is an AutoBean interface, save for the addition of the `stableId()` method.  An AutoBeanFactory can produce non-wrapper (aka "simple") instances of a non-simple interface if an implementation of any non-property interface is provided by a category.

```java
interface Person {
  String getName();
  void setName(String name);
  boolean marry(Person spouse);
}

@Category(PersonCategory.class)
interface MyFactory {
  // Would be illegal without a category providing an implementation of marry(AutoBean<Person> person, Person spouse)
  AutoBean<Person> person();
}

class PersonCategory {
  public static boolean marry(AutoBean<Person> instance, Person spouse) {
    return new Marriage(instance.as(), spouse).accepted();
  }
}
```
For any non-property method, the category must declare a public, static method which has an additional 0-th parameter which accepts the AutoBean backing the instance.  Another example from RequestFactory demonstrating the implementation of the `stableId()` method:

```java
class EntityProxyCategory {
  EntityProxyId<?> stableId(AutoBean<EntityProxy> instance) {
    return (EntityProxyId<?>) instance.getTag("stableId");
  }
}
```

The `@Category` annotation may specify more than one category type.  The first method in the first category whose name matches the non-property method that is type-assignable will be selected.  The parameterization of the 0-th parameter AutoBean is examined when making this decision.

### Interceptors

A category implementation may additionally declare an interceptor method to examine and possible replace the return values of all non-void methods in the target interface:

```java
public static <T> T __intercept(AutoBean<?> bean, T returnValue) {
  // Do stuff
  return maybeAlteredReturnValue;
}
```

RequestFactory uses this to make `EntityProxy` objects returned from an editable object editable.
