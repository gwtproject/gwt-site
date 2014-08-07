JSON
===

<p>Many AJAX application developers have adopted <a href="http://www.json.org/">JSON</a> as the data format of choice for server communication. It is a relatively
simple format based on the object-literal notation of JavaScript. If you choose to use JSON-encoded data within your application, you can use GWT classes to parse and manipulate JSON objects, as well as the very useful and elegant concept of <a href="DevGuideCodingBasicsOverlay.html">JavaScript Overlay Types</a>.</p>

<p>The JSON format is based on the syntax and data types of the JavaScript language. It supports strings, numbers, booleans, and null values. You can also combine multiple values
into arrays and objects. JSON objects are simply unordered sets of name/value pairs, where the name is always a string and the value is any other valid JSON type (even another
object). Here's an example of encoding product data in JSON:</p>


<pre class="prettyprint">
{
  &quot;product&quot;: {
    &quot;name&quot;: &quot;Widget&quot;,
    &quot;company&quot;: &quot;ACME, Inc&quot;,
    &quot;partNumber&quot;: &quot;7402-129&quot;,
    &quot;prices&quot;: [
      { &quot;minQty&quot;: 1, &quot;price&quot;: 12.49 },
      { &quot;minQty&quot;: 10, &quot;price&quot;: 9.99 },
      { &quot;minQty&quot;: 50, &quot;price&quot;: 7.99 }
    ]
  }
}
</pre>


<p>See <a href="http://www.json.org/example.html">json.org/example.html</a> for more JSON examples.</p>

<br>

<ol class="toc" id="pageToc">
  <li><a href="#parsing">Parsing JSON</a></li>
  <li><a href="#mashups">Mashups with JSON and JSNI</a></li>
</ol>

<h2 id="parsing">Parsing JSON</h2>

<p>You can parse JSON Strings and convert them to a <a href="/javadoc/latest/com/google/gwt/core/client/JavaScriptObject.html">JavaScriptObject</a> using <a href="http://www.gwtproject.org/javadoc/latest/com/google/gwt/core/client/JsonUtils.html">JsonUtils</a>.

<pre class="prettyprint">
/*
 * Takes in a JSON String and evals it.
 * @param JSON String that you trust
 * @return JavaScriptObject that you can cast to an Overlay Type
 */
public static &lt;T extends JavaScriptObject&gt T parseJson(String jsonStr)
{
  return JsonUtils.safeEval(jsonStr);
}
</pre>

<p>Typically, you will receive JSON data as the response text of an <a href="DevGuideServerCommunication.html#DevGuideHttpRequests">HTTP request</a>. Thus, you'll first have to convert
that <tt>String</tt> into a Object that you can work with using a method like the one shown above. The recommended way for interacting with <a href="/javadoc/latest/com/google/gwt/core/client/JavaScriptObject.html">JavaScriptObjects</a> is to use <a href="DevGuideCodingBasicsOverlay.html">JavaScript Overlay Types</a>.

<h2 id="mashups">Mashups with JSON and JSNI</h2>

<p>If you're loading JSON-encoded data from your own server, you'll typically use the <a href="/javadoc/latest/com/google/gwt/http/client/RequestBuilder.html">RequestBuilder</a> and related classes to <a href="DevGuideServerCommunication.html#DevGuideHttpRequests">make HTTP requests</a>. However, you can also retrieve JSON from remote servers in true mashup fashion using GWT's <a href="DevGuideCodingBasics.html#DevGuideJavaScriptNativeInterface">JavaScript Native Interface (JSNI)</a> functionality. The techniques for cross-site JSON is explained more fully in the getting started tutorial. To see a working example, check out the <a href="tutorial/Xsite.html">Cross-site Client-Server Communication section</a> of the <a href="tutorial/gettingstarted.html">Getting Started guide</a>.</p>

