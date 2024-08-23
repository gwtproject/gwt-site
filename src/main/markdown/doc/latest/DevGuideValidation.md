Validation
===

**CAUTION: Validation API is unmaintained and will be moved out of GWT SDK into a separate project after GWT 2.8.**

1.  [Introduction](#ValidationIntro)
2.  [Setup Instructions](#SetupInstructions)
3.  [Getting Started with GWT Validation](#GettingStarted)
4.  [Best Practices](#BestPractices)
5.  [Unsupported Features](#Unsupported)

## Introduction<a id="ValidationIntro"></a>

An important part of any application that accepts input is the process of validating that input to ensure it is correct
and valid. This validation can be done in several ways, including manually inserting conditionals every time any input is
received, but this process can be tedious and difficult to maintain.

To make data validation simpler, GWT supports [JSR-303 Bean Validation](http://jcp.org/en/jsr/detail?id=303),
which provides a framework for specifying "constraints" that define what data is valid or allowed for your
application.

For example, by specifying the following annotations on your class:

```java
public class Person implements Serializable {
  @NotNull
  @Size(min = 4, message = "Name must be at least 4 characters long.")
  private String name;
}
```

You can ensure these conditions are met, as well as generate meaningful errors when they are not:

![img](images/ValidationScreenshot.png)

At compile time GWT validation creates a Validator for all your objects that can be used to do client-side validation.
If you are using a Java-based server the very same constraints can be used to do server-side data validation as well.

## Setup Instructions<a id="SetupInstructions"></a>

In order to use GWT validation you must ensure that the following libraries are on your system classpath:

*   Validation API (classes and sources) **(already included in the GWT SDK)**
    *   validation-api-1.0.0.GA.jar
*   [Hibernate Validator](http://www.hibernate.org/subprojects/validator.html) 4.1.0 (classes and sources)
    *   hibernate-validator-4.1.0.Final.jar
    *   hibernate-validator-4.1.0.Final-sources.jar
*   [Simple Logging Facade for Java](http://www.slf4j.org/) and  [log4j](http://logging.apache.org/log4j/1.2/) (Needed for Hibernate Validator)
    *   slf4j-api-1.6.1.jar
    *   slf4j-log4j12-1.6.1.jar
    *   log4j-1.2.16.jar

If you are using the validator for server-side validation be sure to configure your web app classpath properly as well.

## Getting Started with GWT Validation<a id="GettingStarted"></a>

### Setting Constraints

GWT validation is done by annotating beans, getters, and properties with constraint annotations. See the
[JSR-303 specification](http://jcp.org/en/jsr/detail?id=303) for more information on how to define and use
constraints.

```java
public class Person {
  @Size(min = 4)
  private String name;
```

### Creating a Validator Factory

A validator factory is required to bootstrap the validation process. To create a validator factory you must make a
class which extends `AbstractGwtValidatorFactory` and implements the `createValidator()` method. This method
should return the validator object which will be used to perform the validation.

Luckily, you do not need to implement the validator yourself because GWT can generate one for you. In order to generate
a validator simply define an interface which extends `Validator` and contains a `@GwtValidation` annotation.
This annotation requires you to list all classes that will be validated on the client side.

```java
public final class SampleValidatorFactory extends AbstractGwtValidatorFactory {

  /**
   * Validator marker for the Validation Sample project. Only the classes and groups listed
   * in the {@link GwtValidation} annotation can be validated.
   */
  @GwtValidation(Person.class)
  public interface GwtValidator extends Validator {
  }

  @Override
  public AbstractGwtValidator createValidator() {
    return GWT.create(GwtValidator.class);
  }
}
```

Lastly, we must tell GWT to use deferred binding to generate our validator object, adding the following snippet
to your `module.gwt.xml`.

```xml
<inherits name="org.hibernate.validator.HibernateValidator" />
<replace-with
  class="com.google.gwt.sample.validation.client.SampleValidatorFactory">
  <when-type-is class="javax.validation.ValidatorFactory" />
</replace-with>
```

### Validating Constraints

Use the standard validation bootstrap with the default factory to get the generated `Validator` for your objects.
You may use this validator to validate an entire bean object or just specific properties of a bean.

```java
Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
Set<ConstraintViolation<Person>> violations = validator.validate(person);
```

### Validation Groups

You may also put constraints on your objects into "validation groups," and then perform the validation only
on certain subsets of constraints.

All constraints are automatically a part of the `Default` group unless you specify otherwise. Creating a new group
is as simple as making an empty interface:

```java
/** Validates a minimal set of constraints */
public interface Minimal { }
```

If you are using any validation groups other than `Default` in client code, it is important that you list them in
the "groups" parameter of the `@GwtValidation` annotation.

```java
@GwtValidation(value = Person.class, groups = {Default.class, Minimal.class})
public interface GwtValidator extends Validator {
}
```

After that, you can specify this group in the "groups" parameter of any constraint:

```java
public class Address {
  @NotEmpty(groups = Minimal.class)
  @Size(max=50)
  private String street;
  
  @NotEmpty
  private String city;
  
  @NotEmpty(groups = {Minimal.class, Default.class})
  private String zipCode;
  ...
}
```

From here you can validate an `Address` object using the `Default` group, which contains three constraints
(`@Size` on "street", `@NotEmpty` on "city", and `@NotEmpty` on "zipCode"):

```java
Address address = new Address();
validator.validate(address);
```

Or validate using the `Minimal` group, which contains `@NotEmpty` on "street" and `@NotEmpty` on
"zipCode":

```java
validator.validate(address, Minimal.class);
```

## Best Practices<a id="BestPractices"></a>

Validation groups can be used to specify what constraints to run on the client and what to run on the server. You can even make
server-side constraints which do not work with GWT&mdash;just be sure to omit any server-side-only groups from your validator
factory's `@GwtValidation` annotation to avoid compilation issues.

```java
@ServerConstraint(groups = ServerGroup.class)
public class Person {
  @NotNull(groups = ClientGroup.class)
  private String name;
}

Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
// validate on the client
Set<ConstraintViolation<Person>> violations = validator.validate(person, Default.class, ClientGroup.class);
if (!violations.isEmpty()) {
  // client-side violation(s) occurred
} else {
  // client-side validation passed so check server-side
  greetingService.serverSideValidate(person, new AsyncCallback<SafeHtml>() {
    @Override
    public void onFailure(Throwable caught) {
      if (caught instanceof ConstraintViolationException) {
        // server-side violation
      }
      // some other issue
    }
    @Override
    public void onSuccess(SafeHtml result) {
      // server-side validations passed
    }
  }
}
```

## Unsupported Features<a id="Unsupported"></a>

The following features are not supported in GWT validation:

*   XML configuration
*   Validating non-GWT compatible classes like `Calendar` on the client side
*   `ConstraintValidatorFactory`. Constraint validators are generated using `GWT.create()` instead.
*   Validation providers other than the default provider. Always use `Validation.buildDefaultValidatorFactory()` or
    `Validation.byDefaultProvider()` and not `Validation.byProvider()`.
*   Validation provider resolvers. Because there is only one provider the use of provider resolvers is unnecessary.
