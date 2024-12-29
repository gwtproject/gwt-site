i18n Messages
===

1.  [Overview](#MessagesOverview)
2.  [GWT-specific formats](#GwtFormats)
3.  [Using Annotations](#MessagesAnnotations)
4.  [Plural Forms](#PluralForms)
5.  [Select Forms](#SelectForms)
6.  [SafeHtml Messages](#SafeHtmlMessages)

## Overview<a id="MessagesOverview"></a>

The [Messages](/javadoc/latest/com/google/gwt/i18n/client/Messages.html)
interface allows you to substitute parameters into messages and even to
re-order those parameters for different locales as needed. The format of the
messages in the properties files follows the specification in Java [MessageFormat](http://java.sun.com/j2se/1.5.0/docs/api/java/text/MessageFormat.html)
(note that the `choice` format type
is not supported with some extensions). The interface you create will contain a Java method with
parameters matching those specified in the format string.

Here is an example Messages property value:

```properties
permissionDenied = Error {0}: User {1} Permission denied.
```

The following code implements an alert dialog by substituting values into the message:

```java
public interface ErrorMessages extends Messages {
   String permissionDenied(int errorCode, String username);
 }
 ErrorMessages msgs = GWT.create(ErrorMessages.class)

 void permissionDenied(int errorVal, String loginId) {
   Window.alert(msgs.permissionDenied(errorVal, loginId));
 }
```

**Caution:** Be advised that the rules for using quotes may be a bit confusing. Refer to the [MessageFormat](http://java.sun.com/j2se/1.5.0/docs/api/java/text/MessageFormat.html) javadoc for more details.

### More Advanced Formatting

As described in the [MessageFormat](http://java.sun.com/j2se/1.5.0/docs/api/java/text/MessageFormat.html)
javadoc, you can do more formatting with
values besides just inserting the value into the string.  If no format type
is supplied, the value is just appended to the output string at the proper
position.  If you want it formatted as a number, you can use
`{0,number}` which will use the locale's default number format, or
`{0,number,currency}` to use the locale's default currency format
(be careful about which currency you are using though), or create your
own pattern like `{0,number,#,###.0}`.  Dates can be
formatted with `{0,date,medium}` etc., and likewise with times:
`{0,time,full}`.

Note that supplying your own format pattern means you are now responsible
for localizing that pattern &mdash; if you do `{0,date,MM/DD/YY}` for
example, this pattern will be used for all locales and some of them will likely
be confused by the month coming before the day.

### The Benefits of Static String Internationalization

As you can see from the example above, static internationalization relies
on a very tight binding between internationalized code and its localized
resources. Using explicit method calls in this way has a number of advantages.
The GWT compiler can optimize deeply, removing uncalled methods and inlining
localized strings &mdash; making generated code as efficient as if the strings had
been hard-coded. The value of compile-time checking becomes even more apparent
when applied to messages that take multiple arguments. Creating a Java method
for each message allows the compiler to check both the number and types of
arguments supplied by the calling code against the message template defined in
a properties file. For example, attempting to use the following interface and
.properties files results in a compile-time error:

```java
public interface ErrorMessages extends Messages {
  String permissionDenied(int errorCode, String username);
}
```



```properties
permissionDenied = Error {0}: User {1} does not have permission to access {2}
```

An error is returned because the message template in the properties file expects three arguments, while the `permissionDenied` method can only supply two.

## GWT-specific formats<a id="GwtFormats"></a>

In addition to the formatting supported by [MessageFormat](http://java.sun.com/j2se/1.5.0/docs/api/java/text/MessageFormat.html),
GWT supports a number of extensions.

<dl>
<dt> <code>{name,text}</code> </dt>
<dd>    
A "static argument", which is simply <code>text</code>, except that it appears
in translation output as if it were a placeholder named <code>name</code>.
<code>text</code> is always terminated by the next "}".  This is
useful to keep non-translated code out of what the translator sees, for example
HTML markup:

```java
@DefaultMessage("Welcome back, {startBold,<b>}{0}{endBold,</b>}")
```
</dd>
<dt><code>{0,list}</code> or <code>{0,list,format...}</code></dt>
<dd>
Format a <code>List</code> or array using locale-specific punctuation.  For
    example, in English, lists would be formatted like this:

| # of Items | Sample Output |
| :--------: | ------------- |
| 0          | _(empty string)_ |
| 1          | a |
| 2          | a and b |
| 3          | a, b, and c |

Note that only the locale-default separator and the logical conjuctive form is
supported &mdash; there is currently no way to produce a list like "a; b; or c".
See the [plurals documentation](DevGuideI18nPluralForms.html#Lists) for how this interacts with plural support.
The format following the <code>list</code> tag, if any, describes how each list
element is formatted.  Ie, <code>{0,list}</code> means every element is formatted
as if by `{0}`, <code>{0,list,number,#,##}</code> as if by
<code>[0,number,#,##}</code>, etc.
</dd>
<dt><code>{0,localdatetime,skeleton}</code></dt>
<dd> 
Format a date/time in a locale-specific format using the supplied skeleton
pattern.  The order of the pattern characters doesn't matter, and spaces or other separators don't
matter.  The localized pattern will contain the same fields (but may change
<code>MMM</code> into <code>LLL</code> for example) and the same count of each.
If one of the predefined formats are not sufficient, you will be much
better off using a skeleton pattern so you will include the items you want
but still get a localized format.
For example, if you used <code>{0,date,MM/dd/yy}</code> to format a date, you
get exactly that pattern in every locale, which is going to cause confusion for
those users who expect <code>dd/MM/yy</code>.  Instead, you can use
<code>{0,localdatetime,MMddyy}</code> and you will get properly localized patterns
for each locale.
</dd>
<dt><code>{0,localdatetime,predef:PREDEF_NAME}</code></dt>
<dd>
Use a locale-specific predefined format &mdash; see [DateTimeFormat.PredefinedFormat](/javadoc/latest/com/google/gwt/i18n/client/DateTimeFormat.PredefinedFormat.html)"
    for possible values, example: <code>{0,localdatetime,predef:DATE_SHORT}</code>.
</dd>
<dt>extra formatter arguments</dt>
<dd>   Some formatters accept additional arguments.  These are added to the main format specification, separated by a colon &mdash; for example:
    <code>{0,list,number:curcode=USD,currency}</code> says to use the default currency format for list elements, but use USD (US Dollars) for the currency code.  You
    can also supply a dynamic argument, such as <code>{0,localdatetime:tz=$tz,predef:DATE_FULL}</code>, which says the timezone to
    use is supplied by a parameter <code>TimeZone tz</code> supplied to the method.
    Where supported, multiple arguments can be supplied like <code>{0,format:arg1=val:arg2=val}</code>.

Currently supported arguments:

| Format                       | Argument Name | Argument Type | Description |
 | ---------------------------- | ------------- | ------------- | ----------- |
  | number                       | curcode       | `String`      | Currency code to use for currency formatting |
 | date, time, or localdatetime | tz            | `TimeZone`    | Time zone to use for date/time formatting |
</dd>
</dl>

## Using Annotations<a id="MessagesAnnotations"></a>

The annotations discussed here are the ones specific to `Messages` &mdash;
for shared annotations see the [main Internationalization
page](DevGuideI18n.html#DevGuideAnnotations).

### Method Annotations

The following annotations apply to methods in a `Messages` subtype:

*   **[@DefaultMessage(String
message)](/javadoc/latest/com/google/gwt/i18n/client/Messages.DefaultMessage.html)**
    Specifies the message string to be used for the default locale for this
method, with all of the options above.  If an `@AlternateMessage`
annotation is present, this is the default text used when more specific forms
do not apply &mdash; for count messages in English, this would be the plural
form instead of the singular form.

*   **[@AlternateMessage({String
form, String message, ...})](/javadoc/latest/com/google/gwt/i18n/client/Messages.AlternateMessage.html)**
    Specifies the text for alternate forms of the message.  The supplied array of
strings must be in pairs, with the first entry the name of an alternate form
appropriate for the default locale, and the second being the message to use for
that form.
See the [Plural Forms](#PluralForms) and [Select
Forms](#SelectForm) examples below.

### Parameter Annotations

The following annotations apply to parameters of methods in a

`Messages` subtype:

*   **[@Example(String
example)](/javadoc/latest/com/google/gwt/i18n/client/Messages.Example.html)**
    An example for this variable. Many translation tools will show this to the
translator instead of the placeholder &mdash; i.e., `Hello {0}` with
`@Example("John")` will show as `Hello John` with "John"
highlighted to indicate it should not be translated.
*   **[@Optional
](/javadoc/latest/com/google/gwt/i18n/client/Messages.Optional.html)**
    Indicates that this parameter need not be present in all translations. If this
annotation is not supplied, it is a compile-time error if the translated string
being compiled does not include the parameter.
*   **[@PluralCount](/javadoc/latest/com/google/gwt/i18n/client/Messages.PluralCount.html)**
Indicates that this parameter is used to select which form of text to use (ie,
1 widget vs. 2 widgets).<p/>
The argument annotated must be int, short, an array, or a list (in the latter
cases the size of the list is used as the count).

## Plural Forms<a id="PluralForms"></a>

The `Messages` interface also supports the use of plural forms.  In
English, you want to adjust the word being counted based on whether the count
is 1 or not.  For example:

```text
You have one tree.
You have 2 trees.
```

Other languages may have far more complex plural forms.  Fortunately,
GWT allows you to easily handle this problem as follows:

```java
public interface MyMessages extends Messages {
  @DefaultMessage("You have {0} trees.")
  @AlternateMessage({"one", "You have one tree."})
  String treeCount(@PluralCount int count);
}
```

Then, `myMessages.treeCount(1)` returns `"You have one
tree."` while `myMessages.treeCount(2)` returns `"You
have 2 trees."`

See the [details for using plural forms](DevGuideI18nPluralForms.html).

## Select Forms<a id="SelectForms"></a>

Similar to plural forms above, you might need to choose messages based on
something besides a count.  For example, you might know the gender of a person
referenced in the message (`"{0} gave you her credits"`), or you might
want to support abbreviated and full versions of a message based on user
preference.

```java
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
```

The default message is used if no form matches, in this case if `gender`
is `null` or `UNKNOWN`. `@Select` parameters may be
integral primitives, Strings, booleans, or enums.

## SafeHtml Messages<a id="SafeHtmlMessages"></a>

Sometimes, message formats you create include HTML markup, with the resulting
messages intended for use in an HTML context, such as the content of an
`InlineHTML` widget.  If the message is parameterized and the value
of a `String`-typed parameter is derived from untrusted input, the
application is vulnerable to Cross-Site-Scripting (XSS) attacks.

To avoid XSS vulnerabilities due to the use of messages in HTML contexts,
you can declare methods in your Messages interfaces with a return type of
`SafeHtml`:

```java
public interface ErrorMessages extends Messages {
   @DefaultMessage("A <strong>{0} error</strong> has occurred: {1}.")
   SafeHtml errorHtml(String error, SafeHtml details);
 }
 ErrorMessages msgs = GWT.create(ErrorMessages.class)

 void showError(String error, SafeHtml details) {
   errorBar.setHTML(msgs.errorHtml(error, details));
   errorBar.setVisible(true);
 }
```

For SafeHtml messages, the code generator generates code that is guaranteed
to produce values that satisfy the [SafeHtml type
  contract](DevGuideSecuritySafeHtml.html#Use_the_SafeHtml_type) and are safe to use in an HTML context.  Before a parameter's
value is substituted into a message, the parameter's value is automatically
HTML-escaped, unless the parameter's declared type is `SafeHtml`.  In
the above example, the `error` parameter is HTML escaped before
substitution into the template, while the `details` parameter is not.
The `details` parameter can be substituted into the message without
escaping because the `SafeHtml` type contract guarantees that its
value is indeed safe to use as HTML without further escaping. For more
information on how to create `SafeHtml` values, refer to the [SafeHtml
  Developer's Guide](DevGuideSecuritySafeHtml.html#Creating_SafeHtml_Values).  

In message formats of SafeHtml messages, parameters are not allowed inside
of an HTML tag.  For example, the following is not a valid SafeHml message
format, because the `{0}` parameter appears inside a tag's
attribute:

```properties
errorHtmlWithClass = A <span class="{0}">{1} error</span> has occurred.
```

For more information on working with SafeHtml values, see the [SafeHtml
  Developer's Guide](DevGuideSecuritySafeHtml.html).
