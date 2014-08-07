Validation
===

<ol class="toc" id="pageToc">
  <li><a href="#ValidationIntro">Introduction</a></li>
  <li><a href="#SetupInstructions">Setup Instructions</a></li>
  <li><a href="#GettingStarted">Getting Started with GWT Validation</a></li>
  <li><a href="#BestPractices">Best Practices</a></li>
  <li><a href="#Unsupported">Unsupported Features</a></li>
</ol>


<h2 id="ValidationIntro">Introduction</h2>

<p>An important part of any application that accepts input is the process of validating that input to ensure it is correct
and valid. This validation can be done in several ways, including manually inserting conditionals every time any input is
received, but this process can be tedious and difficult to maintain.</p>

<p>To make data validation simpler, GWT supports <a href="http://jcp.org/en/jsr/detail?id=303">JSR-303 Bean Validation</a>,
which provides a framework for specifying &quot;constraints&quot; that define what data is valid or allowed for your
application.</p>

<p>For example, by specifying the following annotations on your class:</p>

<pre class="prettyprint">
public class Person implements Serializable {
  @NotNull
  @Size(min = 4, message = &quot;Name must be at least 4 characters long.&quot;)
  private String name;
}
</pre>

<p>You can ensure these conditions are met, as well as generate meaningful errors when they are not:</p>

<img src="images/ValidationScreenshot.png"/>

<p>At compile time GWT validation creates a Validator for all your objects that can be used to do client-side validation.
If you are using a Java-based server the very same constraints can be used to do server-side data validation as well.</p>


<h2 id="SetupInstructions">Setup Instructions</h2>

<p>In order to use GWT validation you must ensure that the following libraries are on your system classpath:</p>
<ul>
  <li>Validation API (classes and sources) <strong>(already included in the GWT SDK)</strong>
    <ul>
      <li>validation-api-1.0.0.GA.jar</li>
    </ul>
  </li>
  <li><a href="http://www.hibernate.org/subprojects/validator.html">Hibernate Validator</a> 4.1.0 (classes and sources)
    <ul>
      <li>hibernate-validator-4.1.0.Final.jar</li>
      <li>hibernate-validator-4.1.0.Final-sources.jar</li>
    </ul>
  </li>
  <li><a href="http://www.slf4j.org/">Simple Logging Facade for Java</a> and 
  <a href="http://logging.apache.org/log4j/1.2/">log4j</a> (Needed for Hibernate Validator)
    <ul>
      <li>slf4j-api-1.6.1.jar</li>
      <li>slf4j-log4j12-1.6.1.jar</li>
      <li>log4j-1.2.16.jar</li>
    </ul>
  </li>
</ul>

<p>If you are using the validator for server-side validation be sure to configure your web app classpath properly as well.</p>


<h2 id="GettingStarted">Getting Started with GWT Validation</h2>

<h3>Setting Constraints</h3>

<p>GWT validation is done by annotating beans, getters, and properties with constraint annotations. See the
<a href="http://jcp.org/en/jsr/detail?id=303">JSR-303 specification</a> for more information on how to define and use
constraints.</p>

<pre class="prettyprint">
public class Person {
  @Size(min = 4)
  private String name;
</pre>

<h3>Creating a Validator Factory</h3>

<p>A validator factory is required to bootstrap the validation process. To create a validator factory you must make a
class which extends <tt>AbstractGwtValidatorFactory</tt> and implements the <tt>createValidator()</tt> method. This method
should return the validator object which will be used to perform the validation.</p>

<p>Luckily, you do not need to implement the validator yourself because GWT can generate one for you. In order to generate
a validator simply define an interface which extends <tt>Validator</tt> and contains a <tt>@GwtValidation</tt> annotation.
This annotation requires you to list all classes that will be validated on the client side.</p>

<pre class="prettyprint">
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
</pre>

<p>Lastly, we must tell GWT to use deferred binding to generate our validator object, adding the following snippet
to your <tt>module.gwt.xml</tt>.</p>

<pre class="prettyprint">
&lt;inherits name=&quot;org.hibernate.validator.HibernateValidator&quot; /&gt;
&lt;replace-with
  class=&quot;com.google.gwt.sample.validation.client.SampleValidatorFactory&quot;&gt;
  &lt;when-type-is class=&quot;javax.validation.ValidatorFactory&quot; /&gt;
&lt;/replace-with&gt;
</pre>

<h3>Validating Constraints</h3>

<p>Use the standard validation bootstrap with the default factory to get the generated <tt>Validator</tt> for your objects.
You may use this validator to validate an entire bean object or just specific properties of a bean.</p>

<pre class="prettyprint">
Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
Set&lt;ConstraintViolation&lt;Person&gt;&gt; violations = validator.validate(person);
</pre>

<h3>Validation Groups</h3>

<p>You may also put constraints on your objects into &quot;validation groups,&quot; and then perform the validation only
on certain subsets of constraints.</p>

<p>All constraints are automatically a part of the <tt>Default</tt> group unless you specify otherwise. Creating a new group
is as simple as making an empty interface:</p>

<pre class="prettyprint">
/** Validates a minimal set of constraints */
public interface Minimal { }
</pre>

<p>If you are using any validation groups other than <tt>Default</tt> in client code, it is important that you list them in
the &quot;groups&quot; parameter of the <tt>@GwtValidation</tt> annotation.

<pre class="prettyprint">
@GwtValidation(value = Person.class, groups = {Default.class, Minimal.class})
public interface GwtValidator extends Validator {
}
</pre>

<p>After that, you can specify this group in the &quot;groups&quot; parameter of any constraint:</p>

<pre class="prettyprint">
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
</pre>

<p>From here you can validate an <tt>Address</tt> object using the <tt>Default</tt> group, which contains three constraints
(<tt>@Size</tt> on &quot;street&quot;, <tt>@NotEmpty</tt> on &quot;city&quot;, and <tt>@NotEmpty</tt> on &quot;zipCode&quot;):</p>

<pre class="prettyprint">
Address address = new Address();
validator.validate(address);
</pre>

<p>Or validate using the <tt>Minimal</tt> group, which contains <tt>@NotEmpty</tt> on &quot;street&quot; and <tt>@NotEmpty</tt> on
&quot;zipCode&quot;:</p>

<pre class="prettyprint">
validator.validate(address, Minimal.class);
</pre>

<h2 id="BestPractices">Best Practices</h2>

<p>Validation groups can be used to specify what constraints to run on the client and what to run on the server. You can even make
server-side constraints which do not work with GWT&mdash;just be sure to omit any server-side-only groups from your validator
factory's <tt>@GwtValidation</tt> annotation to avoid compilation issues.</p>

<pre class="prettyprint">
@ServerConstraint(groups = ServerGroup.class)
public class Person {
  @NotNull(groups = ClientGroup.class)
  private String name;
}

Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
// validate on the client
Set&lt;ConstraintViolation&lt;Person&gt;&gt; violations = validator.validate(person, Default.class, ClientGroup.class);
if (!violations.isEmpty()) {
  // client-side violation(s) occurred
} else {
  // client-side validation passed so check server-side
  greetingService.serverSideValidate(person, new AsyncCallback&lt;SafeHtml&gt;() {
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
</pre>


<h2 id="Unsupported">Unsupported Features</h2>

<p>The following features are not supported in GWT validation:</p>
<ul>
  <li>XML configuration</li>
  <li>Validating non-GWT compatible classes like <tt>Calendar</tt> on the client side</li>
  <li><tt>ConstraintValidatorFactory</tt>. Constraint validators are generated using <tt>GWT.create()</tt> instead.</li>
  <li>Validation providers other than the default provider. Always use <tt>Validation.buildDefaultValidatorFactory()</tt> or
    <tt>Validation.byDefaultProvider()</tt> and not <tt>Validation.byProvider()</tt>.</li>
  <li>Validation provider resolvers. Because there is only one provider the use of provider resolvers is unnecessary.</li>
</ul>