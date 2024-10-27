Formatting
===

GWT does not provide full emulation for the date and number formatting classes (java.text.DateFormat, java.text.DecimalFormat, java.text.NumberFormat, java.TimeFormat, et
cetera). Instead, a subset of the functionality of the JRE classes is provided by [com.google.gwt.i18n.client.NumberFormat](/javadoc/latest/com/google/gwt/i18n/client/NumberFormat.html) and [com.google.gwt.i18n.client.DateTimeFormat](/javadoc/latest/com/google/gwt/i18n/client/DateTimeFormat.html).

The major difference between the standard Java classes and the GWT classes is the ability to switch between different locales for formatting dates and numbers at runtime. In
GWT, the [deferred binding](DevGuideCodingBasics.html#DevGuideDeferredBinding) mechanism is used to load only the logic needed for the current locale into the
application.

In order to use the `NumberFormat` or `DateTimeFormat` classes, you should update your [module XML file](DevGuideOrganizingProjects.html#DevGuideModuleXml.html) with
the following _inherits_ line:

```xml
<inherits name="com.google.gwt.i18n.I18N"/>
```

See the [internationalization topic](DevGuideI18n.html) for more information about setting up locale.

1.  [Using NumberFormat](#numberformat)
2.  [Using DateTimeFormat](#datetimeformat)

## Using NumberFormat<a id="numberformat"></a>

When using the `NumberFormat` class, you do not instantiate it directly. Instead, you retrieve an instance by calling one of its static `get...Format()` methods.
For most cases, you probably want to use the default decimal format:

```java
NumberFormat fmt = NumberFormat.getDecimalFormat();
    double value = 12345.6789;
    String formatted = fmt.format(value);
    // Prints 1,2345.6789 in the default locale
    GWT.log("Formatted string is" + formatted, null);
```

The class can also be used to convert a numeric string back into a double:

```java
double value = NumberFormat.getDecimalFormat().parse("12345.6789");
    GWT.log("Parsed value is" + value, null);
```

The `NumberFormat` class also provides defaults for scientific notation:

```java
double value = 12345.6789;
    String formatted = NumberFormat.getScientificFormat().format(value);
    // prints 1.2345E4 in the default locale
    GWT.log("Formatted string is" + formatted, null);
```

Note that you can also specify your own pattern for formatting numbers. In the example below, we want to show 6 digits of precision on the right hand side of the decimal and
format the left hand side with zeroes up to the hundred thousands place:

```java
double value = 12345.6789;
    String formatted = NumberFormat.getFormat("000000.000000").format(value);
    // prints 012345.678900 in the default locale
    GWT.log("Formatted string is" + formatted, null);
```

Here are the most commonly used pattern symbols for decimal formats:

| Symbol | Meaning                                         |
| ------ | ----------------------------------------------- |
| 0      | Digit, zero forced                              |
| #      | Digit, zero shows as absent                     |
| .      | Decimal separator or monetary decimal separator |
| \-     | Minus sign                                      |
| ,      | Grouping separator                              |


Specifying an invalid pattern will cause the `NumberFormat.getFormat()` method to throw an `java.lang.IllegalArgumentException`. The `pattern`
specification is very rich. Refer to the [class
documentation](/javadoc/latest/com/google/gwt/i18n/client/NumberFormat.html) for the full set of features.

If you will be using the same number format pattern more than once, it is most efficient to cache the format handle returned from [NumberFormat.getFormat(pattern)](/javadoc/latest/com/google/gwt/i18n/client/NumberFormat.html#getFormat-java.lang.String-).

## Using DateTimeFormat<a id="datetimeformat"></a>

GWT provides the [DateTimeFormat](/javadoc/latest/com/google/gwt/i18n/client/DateTimeFormat.html) class to
replace the functionality of the `DateFormat` and `TimeFormat` classes from the JRE.

For the `DateTimeFormat` class, there are a large number of default formats defined.

```java
Date today = new Date();

    // prints Tue Dec 18 12:01:26 GMT-500 2007 in the default locale.
    GWT.log(today.toString(), null);

    // prints 12/18/07 in the default locale
    GWT.log(DateTimeFormat.getShortDateFormat().format(today), null);

    // prints December 18, 2007 in the default locale
    GWT.log(DateTimeFormat.getLongDateFormat().format(today), null);

    // prints 12:01 PM in the default locale
    GWT.log(DateTimeFormat.getShortTimeFormat().format(today), null);

    // prints 12:01:26 PM GMT-05:00 in the default locale
    GWT.log(DateTimeFormat.getLongTimeFormat().format(today), null);

    // prints Dec 18, 2007 12:01:26 PM in the default locale
    GWT.log(DateTimeFormat.getMediumDateTimeFormat().format(today), null);
```

Like the `NumberFormat` class, you can also use this class to parse a date from a `String` into a `Date` representation. You also have the option of using
the default formats for date and time combinations, or you may build your own using a pattern string. See the [DateTimeFormat](/javadoc/latest/com/google/gwt/i18n/client/DateTimeFormat.html) class documentation for specifics on how to create your own patterns.

Be cautious when straying from the default formats and defining your own patterns. Displaying dates and times incorrectly can be extremely aggravating to international users.
Consider the date:

> `12/04/07`

In some countries this is understood to mean the date December 4th, 2007 in others, it would be April 12th, 2007, in yet another locale, it might mean April 7th, 2012. For
displaying in a common format such as this, use the default formats and let the localization mechanism in the DateTimeFormat do the work for you.
