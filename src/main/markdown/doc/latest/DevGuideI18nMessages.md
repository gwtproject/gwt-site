<ol class="toc" id="pageToc">
  <li><a href="#MessagesOverview">Overview</a></li>
  <li><a href="#GwtFormats">GWT-specific formats</a></li>
  <li><a href="#MessagesAnnotations">Using Annotations</a></li>
  <li><a href="#PluralForms">Plural Forms</a></li>
  <li><a href="#SelectForms">Select Forms</a></li>
  <li><a href="#SafeHtmlMessages">SafeHtml Messages</a></li>
</ol>


<h2 id="MessagesOverview">Overview</h2>

<p>The <a
href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/i18n/client/Messages.html">Messages</a>
interface allows you to substitute parameters into messages and even to
re-order those parameters for different locales as needed. The format of the
messages in the properties files follows the specification in Java <a
href="http://java.sun.com/j2se/1.5.0/docs/api/java/text/MessageFormat.html">MessageFormat</a>
(note that the <tt>choice</tt> format type
is not supported with some extensions). The interface you create will contain a Java method with
parameters matching those specified in the format string.</p>

<p>Here is an example Messages property value:</p>

<pre>
permissionDenied = Error {0}: User {1} Permission denied.
</pre>

<p>The following code implements an alert dialog by substituting values into the message:</p>

<pre class="prettyprint">
 public interface ErrorMessages extends Messages {
   String permissionDenied(int errorCode, String username);
 }
 ErrorMessages msgs = GWT.create(ErrorMessages.class)

 void permissionDenied(int errorVal, String loginId) {
   Window.alert(msgs.permissionDenied(errorVal, loginId));
 }
</pre>

<p class="caution"><strong>Caution:</strong> Be advised that the rules for using quotes may be a bit confusing. Refer to the <a href="http://java.sun.com/j2se/1.5.0/docs/api/java/text/MessageFormat.html">MessageFormat</a> javadoc for more details.</p>

<h3>More Advanced Formatting</h3>

<p>As described in the <a
href="http://java.sun.com/j2se/1.5.0/docs/api/java/text/MessageFormat.html">MessageFormat</a>
javadoc, you can do more formatting with
values besides just inserting the value into the string.  If no format type
is supplied, the value is just appended to the output string at the proper
position.  If you want it formatted as a number, you can use
<tt>{0,number}</tt> which will use the locale's default number format, or
<tt>{0,number,currency}</tt> to use the locale's default currency format
(be careful about which currency you are using though), or create your
own pattern like <tt>{0,number,#,###.0}</tt>.  Dates can be
formatted with <tt>{0,date,medium}</tt> etc., and likewise with times:
<tt>{0,time,full}</tt>.</p>
<p>Note that supplying your own format pattern means you are now responsible
for localizing that pattern &mdash; if you do <tt>{0,date,MM/DD/YY}</tt> for
example, this pattern will be used for all locales and some of them will likely
be confused by the month coming before the day.</p>

<h3>The Benefits of Static String Internationalization</h3>

<p>As you can see from the example above, static internationalization relies
on a very tight binding between internationalized code and its localized
resources. Using explicit method calls in this way has a number of advantages.
The GWT compiler can optimize deeply, removing uncalled methods and inlining
localized strings &mdash; making generated code as efficient as if the strings had
been hard-coded. The value of compile-time checking becomes even more apparent
when applied to messages that take multiple arguments. Creating a Java method
for each message allows the compiler to check both the number and types of
arguments supplied by the calling code against the message template defined in
a properties file. For example, attempting to use the following interface and
.properties files results in a compile-time error:</p>

<pre class="prettyprint">
public interface ErrorMessages extends Messages {
  String permissionDenied(int errorCode, String username);
}
</pre>

<pre>
permissionDenied = Error {0}: User {1} does not have permission to access {2}
</pre>

<p>An error is returned because the message template in the properties file expects three arguments, while the <tt>permissionDenied</tt> method can only supply two.</p>

<h2 id="GwtFormats">GWT-specific formats</h2>

<p>In addition to the formatting supported by <a
href="http://java.sun.com/j2se/1.5.0/docs/api/java/text/MessageFormat.html">MessageFormat</a>,
GWT supports a number of extensions.
<dl>
<dt><tt>{name,text}</tt>
<dd>A "static argument", which is simply <tt>text</tt>, except that it appears
in translation output as if it were a placeholder named <tt>name</tt>.
<tt>text</tt> is always terminated by the next "}".  This is
useful to keep non-translated code out of what the translator sees, for example
HTML markup:
<pre class="prettyprint">
@DefaultMessage("Welcome back, {startBold,&lt;b&gt;}{0}{endBold,&lt;/b&gt;}")
</pre>

<dt><tt>{0,list}</tt> or <tt>{0,list,format...}</tt>
<dd>Format a <tt>List</tt> or array using locale-specific punctuation.  For
example, in English, lists would be formatted like this:
<table>
<tr><th># of Items</th><th>Sample Output</th></tr>
<tr><td align="center">0</td><td><i>(empty string)</i></td></tr>
<tr><td align="center">1</td><td>a</td></tr>
<tr><td align="center">2</td><td>a and b</td></tr>
<tr><td align="center">3</td><td>a, b, and c</td></tr>
</table>
Note that only the locale-default separator and the logical conjuctive form is
supported -- there is currently no way to produce a list like "a; b; or c".

<p>See the <a
href="DevGuideI18nPluralForms.html#Lists">plurals
documentation</a> for how this interacts with plural support.

<p>The format following the <tt>list</tt> tag, if any, describes how each list
element is formatted.  Ie, <tt>{0,list}</tt> means every element is formatted
as if by <tt>{0}</tt>, <tt>{0,list,number,#,##}</tt> as if by
<tt>[0,number,#,##}</tt>, etc.

<dt><tt>{0,localdatetime,skeleton}</tt>
<dd>Format a date/time in a locale-specific format using the supplied skeleton
pattern.  The order of
the pattern characters doesn't matter, and spaces or other separators don't
matter.  The localized pattern will contain the same fields (but may change
<tt>MMM</tt> into <tt>LLL</tt> for example) and the same count of each.

<p>If one of the predefined formats are not sufficient, you will be much
better off using a skeleton pattern so you will include the items you want
but still get a localized format.

<p>For example, if you used <tt>{0,date,MM/dd/yy}</tt> to format a date, you
get exactly that pattern in every locale, which is going to cause confusion for
those users who expect <tt>dd/MM/yy</tt>.  Instead, you can use
<tt>{0,localdatetime,MMddyy}</tt> and you will get properly localized patterns
for each locale.

<dt><tt>{0,localdatetime,predef:PREDEF_NAME}</tt>
<dd>Use a locale-specific predefined format -- see <tt><a
href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/i18n/client/DateTimeFormat.PredefinedFormat.html">DateTimeFormat.PredefinedFormat</a></tt>
for possible values, example: <tt>{0,localdatetime,predef:DATE_SHORT}</tt>.

<dt>extra formatter arguments
<dd>Some formatters accept additional arguments.  These are added to the main format
specification, separated by a colon -- for example:
<tt>{0,list,number:curcode=USD,currency}</tt> says to use the default currency
format for list elements, but use USD (US Dollars) for the currency code.  You
can also supply a dynamic argument, such as
<tt>{0,localdatetime:tz=$tz,predef:DATE_FULL}</tt>, which says the timezone to
use is supplied by a parameter <tt>TimeZone tz</tt> supplied to the method.
Where supported, multiple arguments can be supplied like <tt>{0,format:arg1=val:arg2=val}</tt>.

<p>Currently supported arguments:
<table>
<tr><th>Format</th><th>Argument Name</th><th>Argument Type</th><th>Description</th></tr>
<tr><td>number</td><td>curcode</td><td><tt>String</tt></td><td>Currency code to
use for currency formatting</td></tr>
<tr><td>date, time, or
localdatetime</td><td>tz</td><td><tt>TimeZone</tt></td><td>Time zone to
use for date/time formatting</td></tr>
</table>

</dl>

<h2 id="MessagesAnnotations">Using Annotations</h2>

<p>The annotations discussed here are the ones specific to <tt>Messages</tt> &mdash;
for shared annotations see the <a
href="DevGuideI18n.html#DevGuideAnnotations">main Internationalization
page</a>.</p>

<h3>Method Annotations</h3>
<p>The following annotations apply to methods in a <tt>Messages</tt> subtype:
<ul>
<li><strong><tt><a
href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/i18n/client/Messages.DefaultMessage.html">@DefaultMessage(String
message)</a></tt></strong><br/>
Specifies the message string to be used for the default locale for this
method, with all of the options above.  If an <tt>@AlternateMessage</tt>
annotation is present, this is the default text used when more specific forms
do not apply &mdash; for count messages in English, this would be the plural
form instead of the singular form.
</li>
<li><strong><tt><a
href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/i18n/client/Messages.AlternateMessage.html">@AlternateMessage({String
form, String message, ...})</a></tt></strong><br/>
Specifies the text for alternate forms of the message.  The supplied array of
strings must be in pairs, with the first entry the name of an alternate form
appropriate for the default locale, and the second being the message to use for
that form.
See the <a href="#PluralForms">Plural Forms</a> and <a href="#SelectForm">Select
Forms</a> examples below.
</li>
</ul>

<h3>Parameter Annotations</h3>
<p>The following annotations apply to parameters of methods in a
<tt>Messages</tt> subtype:
<ul>
<li><strong><tt><a
href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/i18n/client/Messages.Example.html">@Example(String
example)</a></tt></strong><br/>
An example for this variable. Many translation tools will show this to the
translator instead of the placeholder &mdash; i.e., <tt>Hello {0}</tt> with
<tt>@Example("John")</tt> will show as </tt>Hello John</tt> with "John"
highlighted to indicate it should not be translated.
</li>
<li><strong><tt><a
href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/i18n/client/Messages.Optional.html">@Optional
</a></tt></strong><br/>
Indicates that this parameter need not be present in all translations. If this
annotation is not supplied, it is a compile-time error if the translated string
being compiled does not include the parameter.
</li>
<li><strong><tt><a
href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/i18n/client/Messages.PluralCount.html">@PluralCount</a></tt></strong>
<br/>
Indicates that this parameter is used to select which form of text to use (ie,
1 widget vs. 2 widgets).<p/>
The argument annotated must be int, short, an array, or a list (in the latter
cases the size of the list is used as the count).
</li>
</ul>

<h2 id="PluralForms">Plural Forms</h2>

<p>The <tt>Messages</tt> interface also supports the use of plural forms.  In
English, you want to adjust the word being counted based on whether the count
is 1 or not.  For example:
<pre class="prettyprint">
You have one tree.
You have 2 trees.
</pre></p>

<p>Other languages may have far more complex plural forms.  Fortunately,
GWT allows you to easily handle this problem as follows:
<pre class="prettyprint">
public interface MyMessages extends Messages {
  @DefaultMessage("You have {0} trees.")
  @AlternateMessage({"one", "You have one tree."})
  String treeCount(@PluralCount int count);
}
</pre></p>

<p>Then, <tt>myMessages.treeCount(1)</tt> returns <tt>&quot;You have one
tree.&quot;</tt> while <tt>myMessages.treeCount(2)</tt> returns <tt>&quot;You
have 2 trees.&quot;</tt> 

<p>See the <a href="DevGuideI18nPluralForms.html">details for using plural
forms</a>.</p>

<h2 id="SelectForms">Select Forms</h2>

<p>Similar to plural forms above, you might need to choose messages based on
something besides a count.  For example, you might know the gender of a person
referenced in the message (<tt>"{0} gave you her credits"</tt>), or you might
want to support abbreviated and full versions of a message based on user
preference.

<pre class="prettyprint">
public enum Gender {
  MALE,
  FEMALE,
  UNKNOWN
}

public interface MyMessages extends Messages {
  @DefaultMessage("{0} gave you their credits.")
  @AlternateMessage({
      "MALE", "{0} gave you his credits.",
      "FEMALE", "{0} gave you her credits."
  })
  String gaveCredits(String name, @Select Gender gender);
}
</pre></p>

The default message is used if no form matches, in this case if <tt>gender</tt>
is <tt>null</tt> or <tt>UNKNOWN</tt>.  <tt>@Select</tt> parameters may be
integral primitives, Stings, booleans, or enums.


<h2 id="SafeHtmlMessages">SafeHtml Messages</h2>

<p>Sometimes, message formats you create include HTML markup, with the resulting
messages intended for use in an HTML context, such as the content of an
<code>InlineHTML</code> widget.  If the message is parameterized and the value
of a <code>String</code>-typed parameter is derived from untrusted input, the
application is vulnerable to Cross-Site-Scripting (XSS) attacks.</p>

<p>To avoid XSS vulnerabilities due to the use of messages in HTML contexts,
you can declare methods in your Messages interfaces with a return type of
<code>SafeHtml</code>: </p>

<pre class="prettyprint">
 public interface ErrorMessages extends Messages {
   @DefaultMessage("A &lt;strong&gt;{0} error&lt;/strong&gt; has occurred: {1}.")
   SafeHtml errorHtml(String error, SafeHtml details);
 }
 ErrorMessages msgs = GWT.create(ErrorMessages.class)

 void showError(String error, SafeHtml details) {
   errorBar.setHTML(msgs.errorHtml(error, details));
   errorBar.setVisible(true);
 }
</pre>

<p>For SafeHtml messages, the code generator generates code that is guaranteed
to produce values that satisfy the <a
  href="DevGuideSecuritySafeHtml.html#Use_the_SafeHtml_type">SafeHtml type
  contract</a> and are safe to use in an HTML context.  Before a parameter's
value is substituted into a message, the parameter's value is automatically
HTML-escaped, unless the parameter's declared type is <code>SafeHtml</code>.  In
the above example, the <code>error</code> parameter is HTML escaped before
substitution into the template, while the <code>details</code> parameter is not.
The <code>details</code> parameter can be substituted into the message without
escaping because the <code>SafeHtml</code> type contract guarantees that its
value is indeed safe to use as HTML without further escaping. For more
information on how to create <code>SafeHtml</code> values, refer to the <a
  href="DevGuideSecuritySafeHtml.html#Creating_SafeHtml_Values">SafeHtml
  Developer's Guide</a>.  </p>

<p> In message formats of SafeHtml messages, parameters are not allowed inside
of an HTML tag.  For example, the following is not a valid SafeHml message
format, because the <code>{0}</code> parameter appears inside a tag's
attribute:</p>

<pre>
errorHtmlWithClass = A &lt;span class="{0}"&gt;{1} error&lt;/span&gt; has occurred.
</pre>

<p>For more information on working with SafeHtml values, see the <a
  href="DevGuideSecuritySafeHtml.html">SafeHtml
  Developer's Guide</a>.
</p>