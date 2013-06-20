<p>GWT does not provide full emulation for the date and number formatting classes (java.text.DateFormat, java.text.DecimalFormat, java.text.NumberFormat, java.TimeFormat, et
cetera). Instead, a subset of the functionality of the JRE classes is provided by <a href="/javadoc/latest/com/google/gwt/i18n/client/NumberFormat.html">com.google.gwt.i18n.client.NumberFormat</a> and <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/i18n/client/DateTimeFormat.html">com.google.gwt.i18n.client.DateTimeFormat</a>.</p>

<p>The major difference between the standard Java classes and the GWT classes is the ability to switch between different locales for formating dates and numbers at runtime. In
GWT, the <a href="DevGuideCodingBasics.html#DevGuideDeferredBinding">deferred binding</a> mechanism is used to load only the logic needed for the current locale into the
application.</p>

<p>In order to use the <tt>NumberFormat</tt> or <tt>DateTimeFormat</tt> classes, you should update your <a href="DevGuideOrganizingProjects.html#DevGuideModuleXml.html">module XML file</a> with
the following <i>inherits</i> line:</p>

<pre class="prettyprint">
  &lt;inherits name=&quot;com.google.gwt.i18n.I18N&quot;/&gt;
</pre>

<p>See the <a href="DevGuideI18n.html">internationalization topic</a> for more information about setting up locale.</p>
<br>

<ol class="toc" id="pageToc">
  <li><a href="#numberformat">Using NumberFormat</a></li>
  <li><a href="#datetimeformat">Using DateTimeFormat</a></li>
</ol>

<h2 id="numberformat">Using NumberFormat</h2>

<p>When using the <tt>NumberFormat</tt> class, you do not instantiate it directly. Instead, you retrieve an instance by calling one of its static <tt>get...Format()</tt> methods.
For most cases, you probably want to use the default decimal format:</p>

<pre class="prettyprint">
    NumberFormat fmt = NumberFormat.getDecimalFormat();
    double value = 12345.6789;
    String formatted = fmt.format(value);
    // Prints 1,2345.6789 in the default locale
    GWT.log(&quot;Formatted string is&quot; + formatted, null);
</pre>

<p>The class can also be used to convert a numeric string back into a double:</p>

<pre class="prettyprint">
    double value = NumberFormat.getDecimalFormat().parse(&quot;12345.6789&quot;);
    GWT.log(&quot;Parsed value is&quot; + value, null);
</pre>

<p>The <tt>NumberFormat</tt> class also provides defaults for scientific notation:</p>

<pre class="prettyprint">
    double value = 12345.6789;
    String formatted = NumberFormat.getScientificFormat().format(value);
    // prints 1.2345E4 in the default locale
    GWT.log(&quot;Formatted string is&quot; + formatted, null);
</pre>

<p>Note that you can also specify your own pattern for formatting numbers. In the example below, we want to show 6 digits of precision on the right hand side of the decimal and
format the left hand side with zeroes up to the hundred thousands place:</p>

<pre class="prettyprint">
    double value = 12345.6789;
    String formatted = NumberFormat.getFormat(&quot;000000.000000&quot;).format(value);
    // prints 012345.678900 in the default locale
    GWT.log(&quot;Formatted string is&quot; + formatted, null);
</pre>

<p>Here are the most commonly used pattern symbols for decimal formats:</p>

<table>
<tr>
<td style="border: 1px solid #aaa; padding: 5px;">Symbol</td>
<td style="border: 1px solid #aaa; padding: 5px;">Meaning</td>
</tr>

<tr>
<td style="border: 1px solid #aaa; padding: 5px;">0</td>
<td style="border: 1px solid #aaa; padding: 5px;">Digit, zero forced</td>
</tr>

<tr>
<td style="border: 1px solid #aaa; padding: 5px;">#</td>
<td style="border: 1px solid #aaa; padding: 5px;">Digit, zero shows as absent</td>
</tr>

<tr>
<td style="border: 1px solid #aaa; padding: 5px;">.</td>
<td style="border: 1px solid #aaa; padding: 5px;">Decimal separator or monetary decimal separator</td>
</tr>

<tr>
<td style="border: 1px solid #aaa; padding: 5px;">-</td>
<td style="border: 1px solid #aaa; padding: 5px;">Minus sign</td>
</tr>

<tr>
<td style="border: 1px solid #aaa; padding: 5px;">,</td>
<td style="border: 1px solid #aaa; padding: 5px;">Grouping separator</td>
</tr>
</table>



<p>Specifying an invalid pattern will cause the <tt>NumberFormat.getFormat()</tt> method to throw an <tt>java.lang.IllegalArgumentException</tt>. The <tt>pattern</tt>
specification is very rich. Refer to the <a href="/javadoc/latest/com/google/gwt/i18n/client/NumberFormat.html">class
documentation</a> for the full set of features.</p>

<p>If you will be using the same number format pattern more than once, it is most efficient to cache the format handle returned from <a href="/javadoc/latest/com/google/gwt/i18n/client/NumberFormat.html#getFormat(java.lang.String)">NumberFormat.getFormat(pattern)</a>.</p>

<h2 id="datetimeformat">Using DateTimeFormat</h2>

<p>GWT provides the <a href="/javadoc/latest/com/google/gwt/i18n/client/DateTimeFormat.html">DateTimeFormat</a> class to
replace the functionality of the <tt>DateFormat</tt> and <tt>TimeFormat</tt> classes from the JRE.</p>

<p>For the <tt>DateTimeFormat</tt> class, there are a large number of default formats defined.</p>

<pre class="prettyprint">
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
</pre>

<p>Like the <tt>NumberFormat</tt> class, you can also use this class to parse a date from a <tt>String</tt> into a <tt>Date</tt> representation. You also have the option of using
the default formats for date and time combinations, or you may build your own using a pattern string. See the <a href="/javadoc/latest/com/google/gwt/i18n/client/DateTimeFormat.html">DateTimeFormat</a> class documentation for specifics on how to create your own patterns.</p>

<p>Be cautious when straying from the default formats and defining your own patterns. Displaying dates and times incorrectly can be extremely aggravating to international users.
Consider the date:</p>

<blockquote><tt>12/04/07</tt></blockquote>

<p>In some countries this is understood to mean the date December 4th, 2007 in others, it would be April 12th, 2007, in yet another locale, it might mean April 7th, 2012. For
displaying in a common format such as this, use the default formats and let the localization mechanism in the DateTimeFormat do the work for you.</p>

