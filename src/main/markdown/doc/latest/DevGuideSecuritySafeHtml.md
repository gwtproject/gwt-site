<style type="text/css">
  #gc-pagecontent h1.smaller  {
    font-size: 145%;
    border-top: thin black solid;
    padding-top: 3px;
    margin-top: 1.8em;
  }
</style>

<p>
Cross-Site-Scripting (XSS) vulnerabilities are a class of web application
security bugs that allow an attacker to execute arbitrary malicious JavaScript
in the context of a victim's browser session. In turn, such malicious script can
for example steal the user's session credentials (resulting in hijacking and
full compromise of the user's session), extract and leak sensitive or
confidential data from the victim's account, or execute transactions chosen by
the attacker in the name of the victim.
</p>

<p>
In GWT applications, many aspects of a web-application's UI are expressed in
terms of abstractions (such as Widgets) that do not expose a potential for
untrusted data to be interpreted as HTML markup or script. As such, GWT apps are
inherently less prone to XSS vulnerabilities than applications built on top of
frameworks where UI is rendered directly as HTML (such as server-side templating
systems, with the exception of templating systems that <a
href="http://googleonlinesecurity.blogspot.com/2009/03/reducing-xss-by-way-of-automatic.html">automatically
escape template variables</a> according to the HTML context the variable appears
in).
</p>

<p>
However, GWT applications are not inherently safe from XSS vulnerabilities.
</p>

<p>
A large class of potential XSS vulnerabilities in GWT applications arises from
the use of methods that cause the browser to evaluate their argument as HTML,
for example, <code>setInnerHTML(String)</code>, <code>setHTML(String)</code>, as
well as the constructors of HTML-containing widgets such as <code>HTML</code>.
If an application passes a string to such a method where the string is even
partially derived from untrusted input, the application is vulnerable to XSS.  In
this context, untrusted input includes immediate user input, data the client app
has received from the server and which may have been provided to the server by a
different, malicious user, as well as values obtained from a history token read
from a URL fragment.
</p>

<p>
This document introduces a new security package and accompanying coding guidelines that help
developers avoid this class of XSS vulnerabilities, while minimizing overhead in
run-time and development effort. A primary goal of the coding guidelines is to
facilitate high-confidence code-reviews of applications for the absence of this
class of XSS bugs.
</p>

<p>
Note that this document does not address other classes of XSS vulnerabilities
that GWT applications may be vulnerable to, such as server-side XSS, as well as
other classes of client-side XSS (for example, calling <code>eval()</code> on an
untrusted string in native JavaScript.)
</p>

<ol class="toc" id="pageToc">
<li><a href="#Coding_guidelines">Coding Guidelines</a></li>
<li><a href="#Coding_guidelines_for_client_code">Coding Guidelines for Developers of Widget Client Code</a></li>
<ol style="list-style: none">
  <li><a href="#Prefer_Plain_Text">Prefer Plain-Text Widgets</a></li>
  <li><a href="#Avoid_ad_hoc_generation_of_HTML_">Use UiBinder for Declarative Layout</a></li>
  <li><a href="#Use_the_SafeHtml_type">Use the SafeHtml Type to Represent XSS-Safe HTML</a></li>
  <li><a href="#Creating_SafeHtml_Values">Creating SafeHtml Values</a></li>
</ol>

<li><a href="#Coding_Guidelines_for_Widget_Developers">Coding Guidelines for Widget Developers</a></li>
<ol style="list-style: none">
  <li><a href="#Provide_SafeHtml">Provide SafeHtml Methods and Constructors</a></li>
  <li><a href="#Unwrap_SafeHtml">"Unwrap" SafeHtml Close to the Value's Use</a></li>
</ol>

<li><a href="#Caveats and Limitations">Caveats and Limitations</a></li>
</ol>

<h2 id="Coding_guidelines">Coding Guidelines</h2>

<p>
The goals of these guidelines are two-fold:
</p>
<ol>
<li> A GWT application whose code-base these guidelines have been consistently
and comprehensively applied to should be free of the class of XSS
vulnerabilities due to attacker-controlled strings being evaluated as HTML in
the browser.
</li>
<li> The code should be structured such that it is easily reviewable for absence
of this class of XSS -- for each use of a potentially XSS-prone method such as
<code>Element.setInnerHTML</code> it should be "obvious" that this use can't
result in an XSS vulnerability.
</li>
</ol>

<p>
The second goal is based on the desire to achieve a high degree of confidence in
the absence of this class of bugs. When reviewing code, it is often difficult
and error-prone to determine whether or not a value passed to some
method may be controlled by an attacker, especially if the value is received via
a long chain of assignments and method calls.
</p>

<p>
Hence, the central idea behind these guidelines is to use a type to encapsulate
strings that are safe to use in HTML context, construct safe HTML into
instances of this type, and use this type to "transport" strings as close as
possible to a code site where they are used as HTML. Such a use is then easily
apparent to be free of XSS vulnerabilities, given the type's contract (as well
as Java's type-safety) as an assumption.  </p>

<h2 id="Coding_guidelines_for_client_code">Coding Guidelines for Developers of Widget Client Code</h2>

<p>
The following guidelines are aimed at developers of client-code that uses
existing widget libraries, in particular the core widget library that is
distributed with GWT.
</p>

<h3 id="Prefer_Plain_Text">Prefer Plain-Text Widgets</h3>

<p>
The best way to avoid XSS bugs, and to write code that is easily seen to be free
of XSS vulnerabilities, is to simply not use API methods and widgets that
interpret parameters as HTML unless strictly necessary.  </p>

<p>
For example, it is not uncommon to see GWT application code such as:
</p>
<pre class="prettyprint">
HTML widget = new HTML("Some text in the widget");
</pre>

<p>
or
</p>

<pre class="prettyprint">
widget.setHTML(someText);
</pre>
</p>

<p>
In the first example, it's obvious that the value passed to the
<code>HTML</code> constructor cannot result in an XSS vulnerability: The value
doesn't contain HTML markup, and furthermore is a compile-time constant and
hence cannot possibly be manipulated by an attacker.  In the second example, it
may be obvious to a reviewer that the call is safe if the variable
<code>someText</code> is assigned to "nearby" in the code; however if the
supplied value is passed in via a parameter through a few layers of method
calls this is much less obvious.  Also, such a scenario may well result in a bug
in a future code iteration if calling code is changed by a developer who doesn't
realize that the value will be used in an HTML context.
</p>

<p>
In such situations, it is preferred to use the non-HTML equivalent, that is, the
<code>Label</code> widget and the <code>setText</code> method, respectively,
both of which are always safe from XSS even if the string passed to the
<code>Label</code> constructor or the <code>setText</code> method is under the
control of an attacker. Similarly, use <code>setInnerText</code> instead of
<code>setInnerHTML</code> on DOM elements.
</p>

<h3 id="Avoid_ad_hoc_generation_of_HTML_">Use UiBinder for Declarative Layout</h3>
<p>
Using <a href="DevGuideUiBinder.html">GWT UiBinder</a> is the preferred approach
to declaratively creating Widget and DOM structures in GWT applications. In
addition to the primary benefits of UiBinder (clean separation of code and
layout, performance, built-in internationalization support), using UiBinder also
typically results in code that is much less prone to XSS vulnerabilities than
code that uses an ad-hoc approach to assembling HTML markup to, say, be placed
into a <code>HTML</code> widget.
</p>

<p>
Often, the "leaf nodes" in the UiBinder-declared layout will be widgets whose
data content is plain text rather than HTML markup. As such, they are naturally
populated via <code>setText</code> rather than <code>setHTML</code> or
equivalent, which completely avoids the potential for an XSS vulnerability.
</p>

<h3 id="Use_the_SafeHtml_type">Use the SafeHtml Type to Represent XSS-Safe HTML</h3>
<p>
There will be occasions where using <code>UiBinder</code> or similar approaches
is not practical or too inconvenient. The <code>com.google.gwt.safehtml</code>
package provides types and classes that can be used to write such code and yet
have confidence that it is free of XSS.
</p>
<p>
The package provides an interface, <a
href="/javadoc/latest/com/google/gwt/safehtml/shared/SafeHtml.html"><code>SafeHtml</code></a>,
to represent the subset of
strings that are safe to use in an HTML context, in the sense that evaluating
the string as HTML in a browser will not result in script execution. More
specifically, all implementations of this interface must adhere to the type
contract that invoking the <code>asString()</code> method on an instance will
always return a string that is HTML-safe in the above sense. In addition, the
type's contract requires that the concatenation of any two
<code>SafeHtml</code>-wrapped strings must itself be safe to use in an HTML
context.
</p>

<p>
With the introduction of the <code>com.google.gwt.safehtml</code> package, all
of the core GWT library's widgets that take <code>String</code> arguments that
are interpreted as HTML have been augmented with corresponding methods that take
a <code>SafeHtml</code>-typed value. In particular, all widgets that implement
the <a href="/javadoc/latest/com/google/gwt/user/client/ui/HasHTML.html"><code>HasHTML</code></a> (or <a
href="/javadoc/latest/com/google/gwt/user/client/ui/HasDirectionalHtml.html"><code>HasDirectionalHtml</code></a>) interface also
implement the <a href="/javadoc/latest/com/google/gwt/safehtml/client/HasSafeHtml.html"><code>HasSafeHtml</code></a> (or <a
href="/javadoc/latest/com/google/gwt/user/client/ui/HasDirectionalSafeHtml.html"><code>HasDirectionalSafeHtml</code></a>, respectively)
interface.  These interfaces define:
</p>
<pre class="prettyprint">
public void setHTML(SafeHtml html);
public void setHTML(SafeHtml html, Direction dir);
</pre>
<p>
as safe alternatives to <code>setHTML(String)</code> and
<code>setHTML(String,&nbsp;Direction)</code>.
</p>

<p>
For example, the <a href="/javadoc/latest/com/google/gwt/user/client/ui/HTML.html"><code>HTML</code></a> widget has been
augmented with the following constructors and methods:
</p>
<pre class="prettyprint">
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
</pre>

<p>
A central aspect of these coding guidelines is that developers of GWT
applications should not use constructors and methods with
<code>String</code>-typed parameters whose values are interpreted as HTML, and
instead use the <code>SafeHtml</code> equivalent.
</p>

<h3 id="Creating_SafeHtml_Values">Creating SafeHtml Values</h3>

<p>
The <code>safehtml</code> package provides a number of tools to create instances
of <code>SafeHtml</code> that cover many common scenarios in which GWT
applications typically manipulate strings containing HTML markup:
</p>
<ul>
<li>A <a href="#SafeHtmlBuilder">builder class</a> that facilitates
the creation of <code>SafeHtml</code> values by safely combining
developer-controlled snippets of HTML markup with (possibly attacker-controlled)
values.</li>
<li>A <a href="#SafeHtmlTemplates">templating mechanism</a> that allows
the definition of snippets of structured HTML markup, into which values are
safely interpolated at run-time.</li>
<li><a href="DevGuideI18nMessages.html#SafeHtmlMessages">I18N
<code>Messages</code></a> can return localized messages in the form of a
<code>SafeHtml</code>.</li>
<li>A number of <a href="#Convenience_Methods">convenience methods</a> are
available to create <code>SafeHtml</code> values from strings.</li>
<li>A <a href="#SimpleHtmlSanitizer">simple HTML sanitizer</a> that
accepts a limited subset of HTML markup in its input, and HTML-escapes any HTML
markup not within that subset.</li>
</ul>

<p>
Each of the above mechanisms has been carefully reviewed with respect to
adherence to the <code>SafeHtml</code> type contract.
</p>

<h4 id="SafeHtmlBuilder">SafeHtmlBuilder</h4>
<p>
In many scenarios, the strings that will be used in an HTML context are
concatenated partially from <i>trusted</i> strings (for example, snippets of HTML
markup defined within the application's source) and <i>untrusted</i> strings
that may be under the control of a potential attacker. The
<a
href="/javadoc/latest/com/google/gwt/safehtml/shared/SafeHtmlBuilder.html"><code>SafeHtmlBuilder</code></a> class provides a builder interface that supports
this use-case while ensuring that the untrusted parts of the string are
appropriately escaped.
</p>
<p>
Consider this usage example:
</p>
<pre class="prettyprint">
public void showItems(List&lt;String&gt; items) {
  SafeHtmlBuilder builder = new SafeHtmlBuilder();
  for (String item : items) {
    builder.appendEscaped(item).appendHtmlConstant("&lt;br/&gt;");
  }
  itemsListHtml.setHTML(builder.toSafeHtml());
}
</pre>
<p>
<code>SafeHtmlBuilder</code>'s <code>appendHtmlConstant</code> method is used to
append a constant snippet of HTML to the builder, without escaping the argument.
The <code>appendEscaped</code> method in contrast will HTML-escape its string
argument before appending.
</p>

<p>
To allow <code>SafeHtmlBuilder</code> to adhere to the <code>SafeHtml</code>
contract, code using it <b>must</b> in turn adhere to the following rules:
</p>
<ol>
<li>
The argument of <code>appendHtmlConstant</code> must be a string literal (or,
more generally, must be fully determined at compile time).
</li>
<li>
The string provided must not end within an HTML tag. For example, the
following use would be illegal because the argument of the first
<code>appendHtmlConstant</code> contains an incomplete <code>&lt;a&gt;</code>
tag; the string ends in the context of the value of the <code>href</code>
attribute of that tag:
<pre class="prettyprint">
builder.appendHtmlConstant("&lt;a href='").appendEscaped(url).appendHtmlConstant("'&gt;")
</pre>
</li>
</ol>

<p>
The first rule is necessary to ensure that strings passed to
<code>appendHtmlConstant</code> cannot possibly be under the control of an
attacker. The second rule is necessary because untrusted strings used inside an
HTML tag attribute can result in script execution even if they are HTML-escaped.
In the above example, script execution could occur if the value of
<code>url</code> is <code>javascript:evil_js_code()</code>.
</p>

<p>
When executing client-side in hosted mode, or server-side with assertions
enabled, <code>appendHtmlConstant</code> parses its argument and checks that it
satisfies the second constraint. For performance reasons, this check is not
performed in production mode in client code, and with assertions disabled on the
server.</p>

<p>
<code>SafeHtmlBuilder</code> also provides the <code>append(SafeHtml)</code>
method. The contents of the provided <code>SafeHtml</code> will be appended to
the builder without prior escaping (due to the <code>SafeHtml</code>
contract, it can be assumed to be HTML-safe).  This method allows HTML snippets
wrapped as <code>SafeHtml</code> to be composed into larger
<code>SafeHtml</code> snippets.
</p>

<h4 id="SafeHtmlTemplates">Creating HTML using the SafeHtmlTemplates Interface</h4>
<p>
To facilitate the creation of SafeHtml instances containing more complex HTML
markup, the <code>safehtml</code> package provides a compile-time bound template
mechanism which can be used as in this example:
</p>
<pre class="prettyprint">
public class MyWidget ... {
// ...
  public interface MyTemplates extends SafeHtmlTemplates {
    @Template("&lt;span class=\"{3}\"&gt;{0}: &lt;a href=\"{1}\"&gt;{2}&lt;/a&gt;&lt;/span&gt;")
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
</pre>
<p>
Instantiating a <a
href="/javadoc/latest/com/google/gwt/safehtml/client/SafeHtmlTemplates.html"><code>SafeHtmlTemplates</code></a> interface with
<code>GWT.create()</code> returns an instance of an implementation that is
generated at compile time. The code generator parses the value of each template
method's <code>@Template</code> annotation as an (X)HTML template, with template
variables denoted by curly-brace placeholders that refer by index to the
corresponding template method parameter.
</p>
<p>
All methods in <code>SafeHtmlTemplates</code> interfaces must have a return type
of <code>SafeHtml</code>. The compile-time generated implementations of such
methods are constructed such that they return instances of <code>SafeHtml</code>
that indeed honor the <code>SafeHtml</code> type contract. The code generator
accomplishes this guarantee by a combination of compile-time checks and run-time 
checks in the generated code (see however the note below with regards to a 
limitation of the current implementation): 
</p>
<ul>
<li>
The template is parsed with a lenient HTML stream parser that accepts HTML 
similar to what would typically be accepted by a web browser. The parser does 
not require that templates consist of balanced HTML tags. However, the parser 
and template code generator enforce the following constraints on input 
templates: Template parameters may not appear in HTML comments, 
parameters may not appear in a Javascript context (e.g., inside a 
<code>&lt;script&gt;</code> tag, or in an <code>onclick</code> 
handler), parameters in HTML attributes can only appear in the value and must be 
enclosed in quotes (e.g., <code>&lt;tag attribute="{0}"&gt;</code> would be allowed, 
<code>&lt;tag {0} attribute={1}&gt;</code> would not), and the template cannot end
inside a tag or inside an attribute. For example, the following is not a valid 
template:
<pre class="prettyprint">
&lt;span&gt;&lt;{0} class="xyz" {1}="..."/&gt;&lt;/span&gt;
</pre>
</li>
<li>
The generated code passes actual template parameters through appropriate
sanitizer and escaping methods depending on the HTML context that the template
parameter appears in and the declared type of the corresponding template method
parameter, as described below.
</li>
</ul>

<p>
<b>Limitation:</b> The current implementation of the parser cannot
guarantee the <code>SafeHtml</code> contract for templates with template 
variables in a CSS context (that is, within a <code>style</code>
attribute or tag). When the parser encounters encounters a template with a 
variable in a style attribute or tag (e.g., <code>&lt;div style="{0}"&gt;</code>), 
a warning will be emitted. Developers are advised to carefully review these 
cases to ensure that parameters passed to the template are from a trusted 
source or suitably sanitized.
</p>

<h5>Template Processing</h5>
<p>
The choice of escaping and/or sanitization applied to template parameters is
made according to the following rules:
</p>
<h6>Parameters in inner HTML context</h6>
<p>
Parameters appearing in plain inner HTML context (for example, parameter
<code>{0}</code> and <code>{2}</code> in the MyWidget example) are processed as
follows:
</p>
<ul>
<li>
If the declared type of the corresponding template method parameter is
<code>SafeHtml</code> (for example, parameter <code>message</code> in the
MyWidget example), the parameter's actual value is emitted without further 
validation or escaping.
</li>
<li>
If the declared type is <code>String</code> (for example, parameter
<code>linkText</code> in the example), the parameter's actual value is
HTML-escaped at run-time before being emitted.
</li>
<li>
If the declared type is a primitive type (for example, a numeric or Boolean
type), the value is converted to <code>String</code> and emitted, but not passed
through an escape method because the String representation of primitive types is
always free of HTML special characters.
</li>
<li>
If the declared type is any other type, the parameter's value is first converted
to <code>String</code> and then HTML escaped.
</li>
</ul>

<h6>Parameters in attribute context </h6>
<p>
Parameters appearing in an attribute context (for example, <code>{1}</code> and
<code>{3}</code> in the example) are treated as follows:
</p>
<ul>
<li>
If the declared type of the corresponding template method parameter is
<em>not</em> <code>String</code>, the parameter's value is first converted to
<code>String</code>.
</li>
<li>
The parameter is then HTML-escaped before it is emitted.
</li>
</ul>
<p>
Note that parameters with a declared type of <code>SafeHtml</code> are not
treated specially if they occur in an attribute context (that is, such
parameters will not skip escaping).  This is because <code>SafeHtml</code>
strings can contain non-escaped HTML special characters (as long as such HTML
markup is safe); however no non-escaped HTML special characters are allowed
within an attribute's value.
</p>

<h6> Parameters in URI-valued attribute context </h6>
<p>
Parameters that appear at the start of a URI-valued attribute such as
<code>src</code> or <code>href</code>, for example parameter <code>{1}</code>
but not <code>{3}</code> in the example, are treated specially:
</p>
<ul>
<li>
Before HTML escaping, the parameter's value is sanitized to ensure it is safe to
use as the value of a URI-valued HTML attribute. This sanitization is performed
as follows (see <a
href="/javadoc/latest/com/google/gwt/safehtml/shared/UriUtils.html#sanitizeUri(java.lang.String)"><code>UriUtils.sanitizeUri(String)</code></a>):
<ul>
<li>
URIs that don't have a scheme are considered safe and are used as is.
</li>
<li>
URIs whose scheme equals one of <code>http, https, ftp, mailto</code> are
considered safe and are used as is.
</li>
<li>
Any other URI is considered unsafe and discarded; instead the "void" URI
"<code>#</code>" is inserted into the template. 
</li>
</ul>
</li>
</ul>

<p>
<b>Limitation:</b> There is no escaping mechanism for the parameter syntax. For 
example, it is impossible to write a template that results in a literal output 
containing a substring of the form <code>{0}</code>.
</p>

<h4 id="Convenience_Methods">Convenience Methods</h4>

<p>
The <a href="/javadoc/latest/com/google/gwt/safehtml/shared/SafeHtmlUtils.html"><code>SafeHtmlUtils</code></a> class provides a
number of convenience methods to create <code>SafeHtml</code> values from
strings:

<ul>
<li><strong><tt><a
href="/javadoc/latest/com/google/gwt/safehtml/shared/SafeHtmlUtils.html#fromString(java.lang.String)">SafeHtmlUtils.fromString(String
s)</a></tt></strong><br/>
HTML-escapes its argument and returns the result wrapped as a
<code>SafeHtml</code>.
</li>

<li><strong><tt><a
href="/javadoc/latest/com/google/gwt/safehtml/shared/SafeHtmlUtils.html#fromSafeConstant(java.lang.String)">SafeHtmlUtils.fromSafeConstant(String
s)</a></tt></strong><br/>
Returns a compile-time constant string wrapped as a <code>SafeHtml</code>,
without escaping the value.  
<p>
To allow <code>fromSafeConstant</code> to adhere to the <code>SafeHtml</code>
contract, code using it <b>must</b> in turn adhere to the same constraints that
apply to <code>SafeHtmlBuilder.appendHtmlConstant</code>:
</p>
<ol>
<li>
The argument of <code>fromSafeConstant</code> must be a string literal (or,
more generally, must be fully determined at compile time).
</li>
<li>
The string provided must not end within an HTML tag. For example, the
following use would be illegal because the value passed to
<code>fromSafeConstant</code> contains an incomplete <code>&lt;a&gt;</code>
tag; the string ends in the context of the value of the <code>href</code>
attribute of that tag:
<pre class="prettyprint">
SafeHtml safeHtml = SafeHtmlUtils.fromSafeConstant("&lt;a href='");
</pre>
</li>
</ol>
</li>

<li><strong><tt><a
href="/javadoc/latest/com/google/gwt/safehtml/shared/SafeHtmlUtils.html#fromTrustedString(java.lang.String)">SafeHtmlUtils.fromTrustedString(String
s)</a></tt></strong><br/>
Returns its argument as a <code>SafeHtml</code>, without performing any form of
validation or escaping.  It is the developer's responsibility to ensure that
values passed to this method adhere to the <code>SafeHtml</code> contract.

<p>
Use of this method is strongly discouraged; it is intended to only be used in
scenarios where existing code produces values that are known to satisfy the
<code>SafeHtml</code> type contract, but such code cannot be easily refactored
to itself produce <code>SafeHtml</code>-typed values. 
</p>
</li>

</ul>


<h4 id="SimpleHtmlSanitizer">SimpleHtmlSanitizer</h4>
<p>
<a
href="/javadoc/latest/com/google/gwt/safehtml/shared/SimpleHtmlSanitizer.html"><code>SimpleHtmlSanitizer</code></a> produces instances of <code>SafeHtml</code>
from input strings by applying a simple sanitization algorithm at run-time.
</p>
<p>
It is intended for scenarios where code receives strings containing simple HTML
markup, for example, from a server-side backend. An example might be search
snippets with query terms marked by <code>&lt;b&gt;</code> tags, as in
"<code>&lt;b&gt;Flowers&lt;/b&gt;, roses, plants &amp;amp; gift baskets delivered.
Order &lt;b&gt;flowers&lt;/b&gt; from ..."</code>.
</p>
<p>
A GWT application that needs to render such strings can't simply HTML-escape
them, since they do contain legitimate HTML. At the same time, the developer of
the application might not want to rely on the backend that produced the snippets
to be entirely bug free, and to never produce strings that may contain
third-party controlled and potentially malicious HTML markup. Instead, such
strings can be wrapped as a <code>SafeHtml</code> by passing them through
<code>SimpleHtmlSanitizer</code>, for example:
</p>
<pre class="prettyprint">
SafeHtml snippetHtml = SimpleHtmlSanitizer.sanitizeHtml(snippet);
</pre>

<p>
<code>SimpleHtmlSanitizer</code> uses a simple sanitization algorithm that
accepts the following markup:
<ul>
<li>
A white-list of basic HTML tags without attributes, including <code>&lt;b&gt;,
&lt;em&gt;, &lt;i&gt;, &lt;h1&gt;, ..., &lt;h5&gt;, &lt;hr&gt;, &lt;ul&gt;,
&lt;ol&gt;, &lt;li&gt;</code> and the corresponding end tags.
</li>
<li>
HTML entities and entity references, such as <code>&amp;#39;, &amp;#x2F;,
&amp;amp;, &amp;quot;</code>, etc.
</li>
</ul>

<p>
HTML markup in this subset will not be escaped; HTML meta-characters that are not
part of a sub-string in the above set will be escaped.
</p>

<p>
For example, the string:
</p>
<pre class="prettyprint">
foo &lt; bar &amp;amp; that is &lt;em&gt;good&lt;/em&gt;, &lt;span style="foo: bar"&gt;...
</pre>

<p>
will be sanitized into:
</p>
<pre class="prettyprint">
foo &amp;lt; bar &amp;amp; that is &lt;em&gt;good&lt;/em&gt;, &amp;lt;span style=&amp;quot;foo: bar&amp;quot;&amp;gt;...
</pre>
<p>
Note that <code>SimpleHtmlSanitizer</code> does not make any guarantees that the
resulting HTML will be well-formed, and that, for example, all remaining tags in
the HTML are balanced.
</p>

<p>
The result of sanitization is returned as a <code>SafeHtml</code> and can be
appended to a <code>SafeHtmlBuilder</code> without getting escaped.
</p>


<h2 id="Coding_Guidelines_for_Widget_Developers">Coding Guidelines for Widget Developers</h2>

<p>
Developers of widgets (or other library components) should consider the HTML safety
of the widget under development.  If possible, widgets should be designed
and implemented such that their use cannot result in XSS vulnerabilities under
any circumstance.  If doing so is not possible, the widget should be designed
and implemented such that it is straightforward and natural for developers of
client code to use it safely, and furthermore such that it is straightforward
for a code reviewer to determine if a given use of the widget is safe.
</p>

<p>
For example, a given instantiation of GWT's <code>HTML</code> widget cannot
result in an XSS vulnerability as long as its use does not involve calls to the
<code>HTML(String)</code> and related constructors, or the
<code>HTML.setHTML(String)</code> or <code>HTML.setHTML(String,&nbsp;Direction)</code>
methods. Code that uses the equivalent <code>SafeHtml</code> constructors and
methods is always safe.  </p>


<h3 id="Provide_SafeHtml">Provide SafeHtml Methods and Constructors</h3>

<p>
Widgets with constructors or methods with an argument that is interpreted as
HTML should provide equivalent constructors and methods that take
<code>SafeHtml</code> values.  In particular, widgets that implement
<code>HasHTML</code> or <code>HasDirectionalHtml</code> should also implement
<code>HasSafeHtml</code> or <code>HasDirectionalSafeHtml</code>, respectively.
</p>

<h3 id="Unwrap_SafeHtml">"Unwrap" SafeHtml Close to the Value's Use</h3>
<p>
The values wrapped inside a <code>SafeHtml</code> will eventually be used in an
HTML context and, for example, used to set a DOM element's <code>innerHTML</code>.
</p>
<p>
To make widget implementations as "obviously safe" as possible, the
<code>String</code> content of a <code>SafeHtml</code> should be extracted as
close as possible to such a use.  For example, a <code>SafeHtml</code> value
should be unwrapped immediately before it is assigned to <code>innerHTML</code>,
and no earlier:
</p>
<pre class="prettyprint">
element.setInnerHTML(safeHtml.asString());
</pre>

<p>
Widgets that are composed of other widgets should <em>not</em> unwrap
<code>SafeHtml</code> values when initializing sub-widgets, and instead
pass the <code>SafeHtml</code> to the sub-widget. For example, write:
<pre class="prettyprint">
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
</pre>

<p>
instead of:
</p>
<pre class="prettyprint">
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
</pre>

<p>
While both implementations provide a safe external interface, the second
implementation is not as obviously free from XSS vulnerabilities as the first:
It involves a call to <code>setHTML(String)</code> that is not inherently safe.
The value passed to <code>setHTML(String)</code> is obtained from a field.  To
determine if this widget is free from XSS vulnerabilities, a code reviewer would
have to inspect every assignment to the field and verify that it can only be
assigned safe HTML.  In a trivial example as the above this is quite
straightforward, but in more complex, real-world code this a potentially time
consuming and error prone process.  </p>

<h2 id="Caveats and Limitations">Caveats and Limitations</h2>

<p>
Comprehensive and consistent use of <code>SafeHtml</code> during development of
a GWT application can substantially reduce the incidence of XSS vulnerabilities
in that application.  However, it does not guarantee the absence of XSS
vulnerabilities.
</p>

<p>
XSS vulnerabilities may be present in such an application for a number of
reasons, including the following:
<ul>
  <li>There may be a bug in code that creates <code>SafeHtml</code> values that
  causes it to sometimes produce values that violate the type contract. If such
  a value is used as HTML (for instance, assigned to a DOM element's
  <code>innerHTML</code> property), an XSS vulnerability may be present.  </li>

  <li>Application code may be incorrectly using
  <code>SafeHtmlBuilder.appendHtmlConstant</code> or
  <code>SafeHtmlUtils.fromSafeConstant</code>.  For example, if one of these
  methods is passed a value that is not program-controlled as required, but
  rather is derived from an external input, an XSS vulnerability may be
  present.</li>

  <li>Application code may be incorrectly using
  <code>SafeHtmlUtils.fromTrustedString</code>. If this method is used to
  <code>SafeHtml</code>-wrap a value that is based on third-party inputs and is
  not strictly guaranteed to adhere to the <code>SafeHtml</code> type contract,
  an XSS vulnerability may arise.</li>

  <li>Application code may have XSS bugs that are not due the use of external
  inputs in HTML contexts, and are beyond the scope of the <code>SafeHtml</code>
  library and guidelines.</li>

</ul>