JSON
===

Many AJAX application developers have adopted [JSON](http://www.json.org/) as the data format of choice for server communication. It is a relatively
simple format based on the object-literal notation of JavaScript. If you choose to use JSON-encoded data within your application, you can use GWT classes to parse and manipulate JSON objects, as well as the very useful and elegant concept of [JavaScript Overlay Types](DevGuideCodingBasicsOverlay.html).

The JSON format is based on the syntax and data types of the JavaScript language. It supports strings, numbers, booleans, and null values. You can also combine multiple values
into arrays and objects. JSON objects are simply unordered sets of name/value pairs, where the name is always a string and the value is any other valid JSON type (even another
object). Here's an example of encoding product data in JSON:

```json
{
  "product": {
    "name": "Widget",
    "company": "ACME, Inc",
    "partNumber": "7402-129",
    "prices": [
      { "minQty": 1, "price": 12.49 },
      { "minQty": 10, "price": 9.99 },
      { "minQty": 50, "price": 7.99 }
    ]
  }
}
```

See [json.org/example.html](http://www.json.org/example.html) for more JSON examples.

1.  [Parsing JSON](#parsing)
2.  [Mashups with JSON and JSNI](#mashups)

## Parsing JSON<a id="parsing"></a>

You can parse JSON Strings and convert them to a [JavaScriptObject](/javadoc/latest/com/google/gwt/core/client/JavaScriptObject.html) using [JsonUtils](https://www.gwtproject.org/javadoc/latest/com/google/gwt/core/client/JsonUtils.html).

```java
/*
 * Takes in a JSON String and evals it.
 * @param JSON String that you trust
 * @return JavaScriptObject that you can cast to an Overlay Type
 */
public static <T extends JavaScriptObject> T parseJson(String jsonStr)
{
  return JsonUtils.safeEval(jsonStr);
}
```

Typically, you will receive JSON data as the response text of an [HTTP request](DevGuideServerCommunication.html#DevGuideHttpRequests). Thus, you'll first have to convert
that `String` into a Object that you can work with using a method like the one shown above. The recommended way for interacting with [JavaScriptObjects](/javadoc/latest/com/google/gwt/core/client/JavaScriptObject.html) is to use [JavaScript Overlay Types](DevGuideCodingBasicsOverlay.html).

## Mashups with JSON and JSNI<a id="mashups"></a>

If you're loading JSON-encoded data from your own server, you'll typically use the [RequestBuilder](/javadoc/latest/com/google/gwt/http/client/RequestBuilder.html) and related classes to [make HTTP requests](DevGuideServerCommunication.html#DevGuideHttpRequests). However, you can also retrieve JSON from remote servers in true mashup fashion using GWT's [JavaScript Native Interface (JSNI)](DevGuideCodingBasics.html#DevGuideJavaScriptNativeInterface) functionality. The techniques for cross-site JSON is explained more fully in the getting started tutorial. To see a working example, check out the [Cross-site Client-Server Communication section](tutorial/Xsite.html) of the [Getting Started guide](tutorial/gettingstarted.html).
