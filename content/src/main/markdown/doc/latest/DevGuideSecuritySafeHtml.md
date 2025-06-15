Security Safe HTML
===

Cross-Site-Scripting (XSS) vulnerabilities are a class of web application
security bugs that allow an attacker to execute arbitrary malicious JavaScript
in the context of a victim's browser session. In turn, such malicious script can
for example steal the user's session credentials (resulting in hijacking and
full compromise of the user's session), extract and leak sensitive or
confidential data from the victim's account, or execute transactions chosen by
the attacker in the name of the victim.

In GWT applications, many aspects of a web-application's UI are expressed in
terms of abstractions (such as Widgets) that do not expose a potential for
untrusted data to be interpreted as HTML markup or script. As such, GWT apps are
inherently less prone to XSS vulnerabilities than applications built on top of
frameworks where UI is rendered directly as HTML (such as server-side templating
systems, with the exception of templating systems that [automatically
escape template variables](http://googleonlinesecurity.blogspot.com/2009/03/reducing-xss-by-way-of-automatic.html) according to the HTML context the variable appears
in).

However, GWT applications are not inherently safe from XSS vulnerabilities.

A large class of potential XSS vulnerabilities in GWT applications arises from
the use of methods that cause the browser to evaluate their argument as HTML,
for example, `setInnerHTML(String)`, `setHTML(String)`, as
well as the constructors of HTML-containing widgets such as `HTML`.
If an application passes a string to such a method where the string is even
partially derived from untrusted input, the application is vulnerable to XSS.  In
this context, untrusted input includes immediate user input, data the client app
has received from the server and which may have been provided to the server by a
different, malicious user, as well as values obtained from a history token read
from a URL fragment.

This document introduces a new security package and accompanying coding guidelines that help
developers avoid this class of XSS vulnerabilities, while minimizing overhead in
run-time and development effort. A primary goal of the coding guidelines is to
facilitate high-confidence code-reviews of applications for the absence of this
class of XSS bugs.

Note that this document does not address other classes of XSS vulnerabilities
that GWT applications may be vulnerable to, such as server-side XSS, as well as
other classes of client-side XSS (for example, calling `eval()` on an
untrusted string in native JavaScript.)

1.  [Coding Guidelines](#Coding_guidelines)
2.  [Coding Guidelines for Developers of Widget Client Code](#Coding_guidelines_for_client_code)
    1.  [Prefer Plain-Text Widgets](#Prefer_Plain_Text)
    2.  [Use UiBinder for Declarative Layout](#Avoid_ad_hoc_generation_of_HTML_)
    3.  [Use the SafeHtml Type to Represent XSS-Safe HTML](#Use_the_SafeHtml_type)
    4.  [Creating SafeHtml Values](#Creating_SafeHtml_Values)
3.  [Coding Guidelines for Widget Developers](#Coding_Guidelines_for_Widget_Developers)
    1.  [Provide SafeHtml Methods and Constructors](#Provide_SafeHtml)
    2.  ["Unwrap" SafeHtml Close to the Value's Use](#Unwrap_SafeHtml)
4.  [Caveats and Limitations](#Caveats_and_Limitations)

## Coding Guidelines<a id="Coding_guidelines"></a>

The goals of these guidelines are two-fold:

1.  A GWT application whose code-base these guidelines have been consistently
and comprehensively applied to should be free of the class of XSS
vulnerabilities due to attacker-controlled strings being evaluated as HTML in
the browser.
2.  The code should be structured such that it is easily reviewable for absence
of this class of XSS &mdash; for each use of a potentially XSS-prone method such as
`Element.setInnerHTML` it should be "obvious" that this use can't
result in an XSS vulnerability.

The second goal is based on the desire to achieve a high degree of confidence in
the absence of this class of bugs. When reviewing code, it is often difficult
and error-prone to determine whether or not a value passed to some
method may be controlled by an attacker, especially if the value is received via
a long chain of assignments and method calls.

Hence, the central idea behind these guidelines is to use a type to encapsulate
strings that are safe to use in HTML context, construct safe HTML into
instances of this type, and use this type to "transport" strings as close as
possible to a code site where they are used as HTML. Such a use is then easily
apparent to be free of XSS vulnerabilities, given the type's contract (as well
as Java's type-safety) as an assumption.  

## Coding Guidelines for Developers of Widget Client Code<a id="Coding_guidelines_for_client_code"></a>

The following guidelines are aimed at developers of client-code that uses
existing widget libraries, in particular the core widget library that is
distributed with GWT.

### Prefer Plain-Text Widgets<a id="Prefer_Plain_Text"></a>

The best way to avoid XSS bugs, and to write code that is easily seen to be free
of XSS vulnerabilities, is to simply not use API methods and widgets that
interpret parameters as HTML unless strictly necessary.

For example, it is not uncommon to see GWT application code such as:

```java
HTML widget = new HTML("Some text in the widget");
```

or

```java
widget.setHTML(someText);
```

In the first example, it's obvious that the value passed to the
`HTML` constructor cannot result in an XSS vulnerability: The value
doesn't contain HTML markup, and furthermore is a compile-time constant and
hence cannot possibly be manipulated by an attacker.  In the second example, it
may be obvious to a reviewer that the call is safe if the variable
`someText` is assigned to "nearby" in the code; however if the
supplied value is passed in via a parameter through a few layers of method
calls this is much less obvious.  Also, such a scenario may well result in a bug
in a future code iteration if calling code is changed by a developer who doesn't
realize that the value will be used in an HTML context.

In such situations, it is preferred to use the non-HTML equivalent, that is, the
`Label` widget and the `setText` method, respectively,
both of which are always safe from XSS even if the string passed to the
`Label` constructor or the `setText` method is under the
control of an attacker. Similarly, use `setInnerText` instead of
`setInnerHTML` on DOM elements.

### Use UiBinder for Declarative Layout<a id="Avoid_ad_hoc_generation_of_HTML_"></a>

Using [GWT UiBinder](DevGuideUiBinder.html) is the preferred approach
to declaratively creating Widget and DOM structures in GWT applications. In
addition to the primary benefits of UiBinder (clean separation of code and
layout, performance, built-in internationalization support), using UiBinder also
typically results in code that is much less prone to XSS vulnerabilities than
code that uses an ad-hoc approach to assembling HTML markup to, say, be placed
into a `HTML` widget.

Often, the "leaf nodes" in the UiBinder-declared layout will be widgets whose
data content is plain text rather than HTML markup. As such, they are naturally
populated via `setText` rather than `setHTML` or
equivalent, which completely avoids the potential for an XSS vulnerability.

### Use the SafeHtml Type to Represent XSS-Safe HTML<a id="Use_the_SafeHtml_type"></a>

There will be occasions where using `UiBinder` or similar approaches
is not practical or too inconvenient. The `com.google.gwt.safehtml`
package provides types and classes that can be used to write such code and yet
have confidence that it is free of XSS.

The package provides an interface, [`SafeHtml`](/javadoc/latest/com/google/gwt/safehtml/shared/SafeHtml.html),
to represent the subset of
strings that are safe to use in an HTML context, in the sense that evaluating
the string as HTML in a browser will not result in script execution. More
specifically, all implementations of this interface must adhere to the type
contract that invoking the `asString()` method on an instance will
always return a string that is HTML-safe in the above sense. In addition, the
type's contract requires that the concatenation of any two
`SafeHtml`-wrapped strings must itself be safe to use in an HTML
context.

With the introduction of the `com.google.gwt.safehtml` package, all
of the core GWT library's widgets that take `String` arguments that
are interpreted as HTML have been augmented with corresponding methods that take
a `SafeHtml`-typed value. In particular, all widgets that implement
the [`HasHTML`](/javadoc/latest/com/google/gwt/user/client/ui/HasHTML.html) (or [`HasDirectionalHtml`](/javadoc/latest/com/google/gwt/user/client/ui/HasDirectionalHtml.html)) interface also
implement the [`HasSafeHtml`](/javadoc/latest/com/google/gwt/safehtml/client/HasSafeHtml.html) (or [`HasDirectionalSafeHtml`](/javadoc/latest/com/google/gwt/user/client/ui/HasDirectionalSafeHtml.html), respectively)
interface.  These interfaces define:

```java
public void setHTML(SafeHtml html);
public void setHTML(SafeHtml html, Direction dir);
```

as safe alternatives to `setHTML(String)` and
`setHTML(String, Direction)`.

For example, the [`HTML`](/javadoc/latest/com/google/gwt/user/client/ui/HTML.html) widget has been
augmented with the following constructors and methods:

```java
public class HTML extends Label 
    implements HasDirectionalHtml, HasDirectionalSafeHtml {
  // ...
  public HTML(SafeHtml html);
  public HTML(SafeHtml html, Direction dir);
  @Override
  public void setHTML(SafeHtml html);
  @Override
  public void setHTML(SafeHtml html, Direction dir);
}
```

A central aspect of these coding guidelines is that developers of GWT
applications should not use constructors and methods with
`String`-typed parameters whose values are interpreted as HTML, and
instead use the `SafeHtml` equivalent.

### Creating SafeHtml Values<a id="Creating_SafeHtml_Values"></a>

The `safehtml` package provides a number of tools to create instances
of `SafeHtml` that cover many common scenarios in which GWT
applications typically manipulate strings containing HTML markup:

*   A [builder class](#SafeHtmlBuilder) that facilitates
the creation of `SafeHtml` values by safely combining
developer-controlled snippets of HTML markup with (possibly attacker-controlled)
values.
*   A [templating mechanism](#SafeHtmlTemplates) that allows
the definition of snippets of structured HTML markup, into which values are
safely interpolated at run-time.
*   [I18N
`Messages`](DevGuideI18nMessages.html#SafeHtmlMessages) can return localized messages in the form of a
`SafeHtml`.
*   A number of [convenience methods](#Convenience_Methods) are
available to create `SafeHtml` values from strings.
*   A [simple HTML sanitizer](#SimpleHtmlSanitizer) that
accepts a limited subset of HTML markup in its input, and HTML-escapes any HTML
markup not within that subset.

Each of the above mechanisms has been carefully reviewed with respect to
adherence to the `SafeHtml` type contract.

#### SafeHtmlBuilder<a id="SafeHtmlBuilder"></a>

In many scenarios, the strings that will be used in an HTML context are
concatenated partially from _trusted_ strings (for example, snippets of HTML
markup defined within the application's source) and _untrusted_ strings
that may be under the control of a potential attacker. The
[`SafeHtmlBuilder`](/javadoc/latest/com/google/gwt/safehtml/shared/SafeHtmlBuilder.html) class provides a builder interface that supports
this use-case while ensuring that the untrusted parts of the string are
appropriately escaped.

Consider this usage example:

```java
public void showItems(List<String> items) {
  SafeHtmlBuilder builder = new SafeHtmlBuilder();
  for (String item : items) {
    builder.appendEscaped(item).appendHtmlConstant("<br/>");
  }
  itemsListHtml.setHTML(builder.toSafeHtml());
}
```

`SafeHtmlBuilder`'s `appendHtmlConstant` method is used to
append a constant snippet of HTML to the builder, without escaping the argument.
The `appendEscaped` method in contrast will HTML-escape its string
argument before appending.

To allow `SafeHtmlBuilder` to adhere to the `SafeHtml`
contract, code using it **must** in turn adhere to the following rules:

1.  The argument of `appendHtmlConstant` must be a string literal (or,
more generally, must be fully determined at compile time).
2.  The string provided must not end within an HTML tag. For example, the
following use would be illegal because the argument of the first
`appendHtmlConstant` contains an incomplete `<a>`
tag; the string ends in the context of the value of the `href`
attribute of that tag:

```java
builder.appendHtmlConstant("<a href='").appendEscaped(url).appendHtmlConstant("'>")
```

The first rule is necessary to ensure that strings passed to
`appendHtmlConstant` cannot possibly be under the control of an
attacker. The second rule is necessary because untrusted strings used inside an
HTML tag attribute can result in script execution even if they are HTML-escaped.
In the above example, script execution could occur if the value of
`url` is `javascript:evil_js_code()`.

When executing client-side in hosted mode, or server-side with assertions
enabled, `appendHtmlConstant` parses its argument and checks that it
satisfies the second constraint. For performance reasons, this check is not
performed in production mode in client code, and with assertions disabled on the
server.

`SafeHtmlBuilder` also provides the `append(SafeHtml)`
method. The contents of the provided `SafeHtml` will be appended to
the builder without prior escaping (due to the `SafeHtml`
contract, it can be assumed to be HTML-safe).  This method allows HTML snippets
wrapped as `SafeHtml` to be composed into larger
`SafeHtml` snippets.

#### Creating HTML using the SafeHtmlTemplates Interface<a id="SafeHtmlTemplates"></a>

To facilitate the creation of SafeHtml instances containing more complex HTML
markup, the `safehtml` package provides a compile-time bound template
mechanism which can be used as in this example:

```java
public class MyWidget ... {
// ...
  public interface MyTemplates extends SafeHtmlTemplates {
    @Template("<span class=\"{3}\">{0}: <a href=\"{1}\">{2}</a></span>")
    SafeHtml messageWithLink(SafeHtml message, String url, String linkText,
        String style);
  }
 
  private static final MyTemplates TEMPLATES =
      GWT.create(MyTemplates.class);
 
  public void useTemplate(...) {
    SafeHtml message;
    String url;
    String linkText;
    String style;
    // ...
    InlineHTML messageWithLinkInlineHTML = new InlineHTML(
        TEMPLATES.messageWithLink(message, url, linkText, style));
    // ...
  }
```

Instantiating a [`SafeHtmlTemplates`](/javadoc/latest/com/google/gwt/safehtml/client/SafeHtmlTemplates.html) interface with
`GWT.create()` returns an instance of an implementation that is
generated at compile time. The code generator parses the value of each template
method's `@Template` annotation as an (X)HTML template, with template
variables denoted by curly-brace placeholders that refer by index to the
corresponding template method parameter.

All methods in `SafeHtmlTemplates` interfaces must have a return type
of `SafeHtml`. The compile-time generated implementations of such
methods are constructed such that they return instances of `SafeHtml`
that indeed honor the `SafeHtml` type contract. The code generator
accomplishes this guarantee by a combination of compile-time checks and run-time 
checks in the generated code (see however the note below with regards to a 
limitation of the current implementation): 

*   The template is parsed with a lenient HTML stream parser that accepts HTML
similar to what would typically be accepted by a web browser. The parser does
not require that templates consist of balanced HTML tags. However, the parser
and template code generator enforce the following constraints on input
templates: Template parameters may not appear in HTML comments,
parameters may not appear in a Javascript context (e.g., inside a`<script>` tag, or in an `onclick`
handler), parameters in HTML attributes can only appear in the value and must been
closed in quotes (e.g., `<tag attribute="{0}">` would be allowed,
`<tag {0} attribute={1}>` would not), and the template cannot end
inside a tag or inside an attribute. For example, the following is not a valid
template:

```java
<span><{0} class="xyz" {1}="..."/></span>
```

*   The generated code passes actual template parameters through appropriate
sanitizer and escaping methods depending on the HTML context that the template
parameter appears in and the declared type of the corresponding template method
parameter, as described below.

**Limitation:** The current implementation of the parser cannot
guarantee the `SafeHtml` contract for templates with template 
variables in a CSS context (that is, within a `style`
attribute or tag). When the parser encounters encounters a template with a 
variable in a style attribute or tag (e.g., `<div style="{0}">`), 
a warning will be emitted. Developers are advised to carefully review these 
cases to ensure that parameters passed to the template are from a trusted 
source or suitably sanitized.

##### Template Processing

The choice of escaping and/or sanitization applied to template parameters is
made according to the following rules:

###### Parameters in inner HTML context

Parameters appearing in plain inner HTML context (for example, parameter
`{0}` and `{2}` in the MyWidget example) are processed as
follows:

*   If the declared type of the corresponding template method parameter is
`SafeHtml` (for example, parameter `message` in the
MyWidget example), the parameter's actual value is emitted without further
validation or escaping.
*   If the declared type is `String` (for example, parameter
`linkText` in the example), the parameter's actual value is
HTML-escaped at run-time before being emitted.
*   If the declared type is a primitive type (for example, a numeric or Boolean
type), the value is converted to `String` and emitted, but not passed
through an escape method because the String representation of primitive types is
always free of HTML special characters.
*   If the declared type is any other type, the parameter's value is first converted
to `String` and then HTML escaped.

###### Parameters in attribute context 

Parameters appearing in an attribute context (for example, `{1}` and
`{3}` in the example) are treated as follows:

*   If the declared type of the corresponding template method parameter is
_not_ `String`, the parameter's value is first converted to
`String`.
*   The parameter is then HTML-escaped before it is emitted.

Note that parameters with a declared type of `SafeHtml` are not
treated specially if they occur in an attribute context (that is, such
parameters will not skip escaping).  This is because `SafeHtml`
strings can contain non-escaped HTML special characters (as long as such HTML
markup is safe); however no non-escaped HTML special characters are allowed
within an attribute's value.

######  Parameters in URI-valued attribute context 

Parameters that appear at the start of a URI-valued attribute such as
`src` or `href`, for example parameter `{1}`
but not `{3}` in the example, are treated specially:

*   Before HTML escaping, the parameter's value is sanitized to ensure it is safe to
use as the value of a URI-valued HTML attribute. This sanitization is performed
as follows (see [`UriUtils.sanitizeUri(String)`](/javadoc/latest/com/google/gwt/safehtml/shared/UriUtils.html#sanitizeUri-java.lang.String-)):
    *   URIs that don't have a scheme are considered safe and are used as is.
    *   URIs whose scheme equals one of `http, https, ftp, mailto` are considered safe and are used as is.
    *   Any other URI is considered unsafe and discarded; instead the "void" URI "`#`" is inserted into the template.

**Limitation:** There is no escaping mechanism for the parameter syntax. For 
example, it is impossible to write a template that results in a literal output 
containing a substring of the form `{0}`.

#### Convenience Methods<a id="Convenience_Methods"></a>

The [`SafeHtmlUtils`](/javadoc/latest/com/google/gwt/safehtml/shared/SafeHtmlUtils.html) class provides a
number of convenience methods to create `SafeHtml` values from
strings:

*   **[SafeHtmlUtils.fromString(String s)](/javadoc/latest/com/google/gwt/safehtml/shared/SafeHtmlUtils.html#fromString-java.lang.String-)**
    HTML-escapes its argument and returns the result wrapped as a `SafeHtml`.
*   **[SafeHtmlUtils.fromSafeConstant(String s)](/javadoc/latest/com/google/gwt/safehtml/shared/SafeHtmlUtils.html#fromSafeConstant-java.lang.String-)**
    Returns a compile-time constant string wrapped as a `SafeHtml`, without escaping the value. To allow `fromSafeConstant` to adhere to the `SafeHtml` contract, code using it **must** in turn adhere to the same constraints that apply to `SafeHtmlBuilder.appendHtmlConstant`:
    1.  The argument of `fromSafeConstant` must be a string literal (or, more generally, must be fully determined at compile time).
    2.  The string provided must not end within an HTML tag. For example, the following use would be illegal because the value passed to
`fromSafeConstant` contains an incomplete `<a>`
tag; the string ends in the context of the value of the `href`
attribute of that tag:

```java
SafeHtml safeHtml = SafeHtmlUtils.fromSafeConstant("<a href='");
```

*   **[SafeHtmlUtils.fromTrustedString(Strings)](/javadoc/latest/com/google/gwt/safehtml/shared/SafeHtmlUtils.html#fromTrustedString-java.lang.String-)**
    
    Returns its argument as a `SafeHtml`, without performing any form of
    validation or escaping.  It is the developer's responsibility to ensure that
    values passed to this method adhere to the `SafeHtml` contract.
    
    Use of this method is strongly discouraged; it is intended to only be used in
    scenarios where existing code produces values that are known to satisfy the
    `SafeHtml` type contract, but such code cannot be easily refactored
    to itself produce `SafeHtml`-typed values. 

#### SimpleHtmlSanitizer<a id="SimpleHtmlSanitizer"></a>

[SimpleHtmlSanitizer](/javadoc/latest/com/google/gwt/safehtml/shared/SimpleHtmlSanitizer.html)
produces instances of `SafeHtml`
from input strings by applying a simple sanitization algorithm at run-time.

It is intended for scenarios where code receives strings containing simple HTML
markup, for example, from a server-side backend. An example might be search
snippets with query terms marked by `<b>` tags, as in
"`<b>Flowers</b>, roses, plants &amp; gift baskets delivered.
Order <b>flowers</b> from ..."`.

A GWT application that needs to render such strings can't simply HTML-escape
them, since they do contain legitimate HTML. At the same time, the developer of
the application might not want to rely on the backend that produced the snippets
to be entirely bug free, and to never produce strings that may contain
third-party controlled and potentially malicious HTML markup. Instead, such
strings can be wrapped as a `SafeHtml` by passing them through
`SimpleHtmlSanitizer`, for example:

```java
SafeHtml snippetHtml = SimpleHtmlSanitizer.sanitizeHtml(snippet);
```

`SimpleHtmlSanitizer` uses a simple sanitization algorithm that
accepts the following markup:

*   A white-list of basic HTML tags without attributes, including `<b>,<em>, <i>, <h1>, ..., <h5>, <hr>, <ul>,<ol>, <li>` and the corresponding end tags.
*   HTML entities and entity references, such as `&#39;, &#x2F;,&amp;, &quot;,`etc.

HTML markup in this subset will not be escaped; HTML meta-characters that are not
part of a sub-string in the above set will be escaped.

For example, the string:

```text
foo < bar &amp; that is <em>good</em>, <span style="foo: bar">...
```

will be sanitized into:

```text
foo &amp;lt; bar &amp;amp; that is <em>good</em>, &amp;lt;span style=&amp;quot;foo: bar&amp;quot;&amp;gt;...
```

Note that `SimpleHtmlSanitizer` does not make any guarantees that the
resulting HTML will be well-formed, and that, for example, all remaining tags in
the HTML are balanced.

The result of sanitization is returned as a `SafeHtml` and can be
appended to a `SafeHtmlBuilder` without getting escaped.

## Coding Guidelines for Widget Developers<a id="Coding_Guidelines_for_Widget_Developers"></a>

Developers of widgets (or other library components) should consider the HTML safety
of the widget under development.  If possible, widgets should be designed
and implemented such that their use cannot result in XSS vulnerabilities under
any circumstance.  If doing so is not possible, the widget should be designed
and implemented such that it is straightforward and natural for developers of
client code to use it safely, and furthermore such that it is straightforward
for a code reviewer to determine if a given use of the widget is safe.

For example, a given instantiation of GWT's `HTML` widget cannot
result in an XSS vulnerability as long as its use does not involve calls to the
`HTML(String)` and related constructors, or the
`HTML.setHTML(String)` or `HTML.setHTML(String, Direction)`
methods. Code that uses the equivalent `SafeHtml` constructors and
methods is always safe.  

### Provide SafeHtml Methods and Constructors<a id="Provide_SafeHtml"></a>

Widgets with constructors or methods with an argument that is interpreted as
HTML should provide equivalent constructors and methods that take
`SafeHtml` values.  In particular, widgets that implement
`HasHTML` or `HasDirectionalHtml` should also implement
`HasSafeHtml` or `HasDirectionalSafeHtml`, respectively.

### "Unwrap" SafeHtml Close to the Value's Use<a id="Unwrap_SafeHtml"></a>

The values wrapped inside a `SafeHtml` will eventually be used in an
HTML context and, for example, used to set a DOM element's `innerHTML`.

To make widget implementations as "obviously safe" as possible, the
`String` content of a `SafeHtml` should be extracted as
close as possible to such a use.  For example, a `SafeHtml` value
should be unwrapped immediately before it is assigned to `innerHTML`,
and no earlier:

```java
element.setInnerHTML(safeHtml.asString());
```

Widgets that are composed of other widgets should _not_ unwrap
`SafeHtml` values when initializing sub-widgets, and instead
pass the `SafeHtml` to the sub-widget. For example, write:

```java
public class MyPanel extends HorizontalPanel {
  InlineHTML messageWidget;
  SafeHtml currentMessage;

  public void setMessage(SafeHtml newMessage) {
    currentMessage = newMessage;
    updateUi();
  }

  private void updateUi() {
    messageWidget.setHTML(currentMessage);
  }
}
```

instead of:

```java
public class MyPanel extends HorizontalPanel {
  InlineHTML messageWidget;
  String currentMessage;

  public void setMessage(SafeHtml newMessage) {
    currentMessage = newMessage.asString();
    updateUi();
  }

  private void updateUi() {
    // Potentially unsafe call to setHTML(String)
    messageWidget.setHTML(currentMessage);
  }
}
```

While both implementations provide a safe external interface, the second
implementation is not as obviously free from XSS vulnerabilities as the first:
It involves a call to `setHTML(String)` that is not inherently safe.
The value passed to `setHTML(String)` is obtained from a field.  To
determine if this widget is free from XSS vulnerabilities, a code reviewer would
have to inspect every assignment to the field and verify that it can only be
assigned safe HTML.  In a trivial example as the above this is quite
straightforward, but in more complex, real-world code this a potentially time
consuming and error prone process.  

## Caveats and Limitations<a id="Caveats_and_Limitations"></a>

Comprehensive and consistent use of `SafeHtml` during development of
a GWT application can substantially reduce the incidence of XSS vulnerabilities
in that application.  However, it does not guarantee the absence of XSS
vulnerabilities.

XSS vulnerabilities may be present in such an application for a number of
reasons, including the following:

*   There may be a bug in code that creates `SafeHtml` values that
  causes it to sometimes produce values that violate the type contract. If such
  a value is used as HTML (for instance, assigned to a DOM element's
  `innerHTML` property), an XSS vulnerability may be present.
*   Application code may be incorrectly using
  `SafeHtmlBuilder.appendHtmlConstant` or
  `SafeHtmlUtils.fromSafeConstant`.  For example, if one of these
  methods is passed a value that is not program-controlled as required, but
  rather is derived from an external input, an XSS vulnerability may be
  present.
*   Application code may be incorrectly using
  `SafeHtmlUtils.fromTrustedString`. If this method is used to
  `SafeHtml`-wrap a value that is based on third-party inputs and is
  not strictly guaranteed to adhere to the `SafeHtml` type contract,
  an XSS vulnerability may arise.
*   Application code may have XSS bugs that are not due the use of external
  inputs in HTML contexts, and are beyond the scope of the `SafeHtml`
  library and guidelines.
