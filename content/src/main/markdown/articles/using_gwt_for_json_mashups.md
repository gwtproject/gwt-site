JSON & Mashups
===

_Dan Morrill, Google Developer Relations Team_

_Updated January 2009_

## Introduction

What's the fun of a web application if you're stuck on your own server? It's much more fun to get out and meet new code, which is where web mashups come in. Mashups let you build powerful applications surprisingly quickly, if you have the right tools. Recently I've been working on a mashup project using the GWT. One of the goals of my project was to let applications written using GWT integrate with other web applications that expose data in JavaScript Object Notation (JSON) format.

Sounds straightforward, right? Well, it was &mdash; almost &mdash; read on to learn why! This article is a case study of how to incorporate mashup-style JSON data into a GWT application. This will be relevant to you if you're a GWT user, but even general Ajax developers may find it interesting.

### A note on security

This article discusses JSON. JSON is very cool, but it can also be quite dangerous to the unwary. Because JSON is a subset of JavaScript, it is executable code; this makes it vulnerable to a variety of attacks. If you're using JSON, it's very important to be aware of these security risks and apply countermeasures.

This is too large a topic for us to discuss here. Fortunately, we've already discussed it elsewhere: check out this link on [Security for GWT Applications](security_for_gwt_applications.html). I urge you to read that document and familiarize yourself with the security risks before you put into practice any of the things discussed here.

## JSON basics

What is JSON, anyway? Well, by a happy coincidence, it turns out that the syntax JavaScript uses for defining data objects is rather broadly compatible with other languages. This makes it a sort of lowest-common-denominator syntax for specifying data. Other folks have covered this better than I can, so I'll just send you straight to the source: [json.org](http://json.org).

One of the key benefits of JSON is that because it is essentially JavaScript syntax, browsers can "parse" JSON data simply by calling the JavaScript eval function. This is both easy and fast because it takes advantage of native code in the browsers to do the parsing. (This is also why JSON can be a security problem; if the JSON string actually contains non-JSON code, then calling eval on it is quite dangerous.)

Once you have a service that can produce JSON data, there are generally three different ways to use it. These methods can be categorized by how you fetch the data.

*   The server can output a string containing raw JSON data that the browser fetches with an `XMLHTTPRequest` and manually passes to the eval function.

    Example server-generated string: `{'data': ['foo', 'bar', 'baz']}`
*   The server can output a string containing JavaScript code that assigns a JSON object to a
variable; the browser would fetch this using a `<script>` tag and then extract the parsed object by referring to the variable by name.

      Example server-generated string: `var result = {'data': ['foo', 'bar', 'baz']};`
*   The server can output a string containing JavaScript code that passes a JSON object to a function specified in the request URL; the browser would fetch this using a `<script>` tag, which will automatically invoke the function as if it were an event callback, as soon as the JavaScript is parsed.

      Example server-generated string: `handle_result({'data': ['foo', 'bar', 'baz']});`

The term JSON technically refers only to the data representation syntax (which is where the "Object Notation" part of its name comes from) and so JSON is a strict subset of JavaScript. Because of this, those last two methods aren't technically JSON &mdash; they're JavaScript code that deals with data in JSON format. They are still close cousins to JSON, though, and frequently "JSON" is used as a blanket term for all such cases. The third method in particular is frequently called "JSON with Padding" (JSONP); the earliest description of this technique that I'm aware of is here: [Remote JSON - JSONP](http://bob.pythonmac.org/archives/2005/12/05/remote-json-jsonp/).

The primary difference between these techniques is how they are fetched. Since the first case &mdash; that is, pure JSON &mdash; contains no executable component, it's generally only useful with XMLHTTPRequests. Because that function is subject to Same-Origin restrictions, that means that pure-JSON can only be used as a data transmission technique between a browser application and its HTTP server.

The latter two techniques, however, fetch the strings using dynamic `<script>` tag insertions. Since that technique is not limited by Same-Origin restrictions, it can be used cross-domain. Coupled with services that expose their data in JavaScript syntax, this allows browsers to make requests for data from several different servers. This is the technique that makes mashups possible. (More specifically &mdash; mashups that run entirely inside the browser. You can also create mashups using server-side proxies if you don't want to use JSONP and don't mind maintaining your own server.)

## High-level design

Now that we have the JSON basics in hand, what about my project? The task I was working on involved mashing up data from another service &mdash; specifically, Google Base. This means that I needed to use the [Google Data API](http://code.google.com/apis/gdata/index.html) for fetching information. Google's GData servers provide XML, JSON, and JSONP-style interfaces, to allow developers maximum flexibility in building applications. For my project, I wanted to build an in-browser mashup, which means I needed to use the [Google Data API's  JSONP-style interface](https://developers.google.com/gdata/docs/json).

Since GWT applications are written in Java, there is a compilation phase that compiles the Java source to JavaScript. The compiler also optimizes the generated code, and one of the optimizations it performs is code obfuscation, which makes the output smaller and thus faster to load. A downside of this, though, is that the function names in the output JavaScript code are unpredictable. This makes it difficult to specify a callback function name to a JSONP service.

There is an effective technique for cases like this that we sometimes refer to as a "function bridge." We've [documented it](../doc/latest/DevGuideCodingBasics.html#DevGuideJavaScriptNativeInterface) in our GWT FAQs, but in a nutshell, the technique involves creating a handle for the obfuscated function and copying it to a well-known variable name in the JavaScript namespace. When outside JavaScript code (such as a Google Data server response) invokes the function under its well-known name, it actually invokes the real function via the copied handle. (Check out the link earlier in this paragraph to see a basic example.)

However, my project made a lot of those Google Data requests. That could conceivably leave quite a few of these function handles lying around, so there was some bookkeeping that needed to be managed.

After taking all those points into consideration, I chose this rough design:

*   Each request for Google Data JSON data is assigned a unique token.
*   A new callback function for each request is created on demand using GWT's JavaScript Native Interface (JSNI); the request's token is included in the function's name to make sure it's unique.
*   The callback is actually a JavaScript function closure over the token; the token is passed along to the inner function when the callback is invoked.
*   The same inner function is used for each callback; this function is actually a method on my GWT class, which uses the token to dispatch the response data to the appropriate GWT code.

This strategy has the following "moving parts":

*   A single dispatch method on my GWT class to handle incoming responses from the Google Data server
*   A second method on my GWT class that uses JSNI to construct the function closures and initiate the Google Data request

Ultimately, there should also be a cleanup phase which removes the callback handles to avoid cluttering the JavaScript namespace, but that's easy enough to add later. To get started, I proceeded with the other design elements above.

Hopefully you've followed along so far, but if not &mdash; never fear, I've included source code below!

## First implementation

The first thing I did was write some code to demonstrate the basic concept. I did have an idea of how the final API itself ought to look, but the objective at this point was to prove the concept, not design the final API. So, I started by implementing a single class that contained all the key parts that I expected my final API to have. First, I thought, I'd prove that it worked, and then I'd refactor it into a real API.

Specifically, here are the key requirements I had:

*   Must abstract the dynamic `<script>` insertion behind a method call
*   Must handle bookkeeping of `<script>` tags (to be able to clean them up later and prevent memory leaks)
*   Must provide a method to generate and reserve a callback function name

At this point, I needed a Google Data feed to test with. I decided to fetch the Google Base "snippets" URL, which is a GData feed in JSON mode. The base URL for this feed is `http://www.google.com/base/feeds/snippets`. To request JSON data as output, you add some GET parameters to the URL: `?alt=json-in-script&callback=foo`. The last value &mdash; foo &mdash; is the name of a callback function (that is, the JSONP hook). The Google Data feed's output will wrap the JavaScript object with a call to that function.

If you want to see a full example of the Google Data output, check out this URL: `http://www.google.com/base/feeds/snippets`. You'll quickly see that there's a lot of data, even for just a single result. To help you visualize the general structure of the feed, here's a much smaller custom-built sample result that contains only the data relevant to this story:

```javascript
{
  'feed': {
    'entry': [
      {'title': {'type': 'text', '$t': 'Some Text'}},
      {'title': {'type': 'text', '$t': 'Some More Text'}}
    ]
  }
}

```

The core structure is fairly simple, as you can see; most of the length of the real Google Data feed comes from the various data fields.

To keep my development simple, I used that minimized example for testing so I wouldn't be overwhelmed by the full Google Data feed. Of course, that meant I needed a web server to serve up my custom version of the JSON data. Normally I would have just served it from the built-in Tomcat instance included in GWT's hosted mode. However, that would have meant that my JSON data and my GWT application would be served from the same site. Since my ultimate goal was to load the real JSON data from a different site, I needed a second, separate local server from which to fetch my JSON data &mdash; otherwise, it wouldn't be an accurate simulation. Since a full web server instance would have been lots of work to set up, I created a tiny custom server with this Python program:

```python
import BaseHTTPServer, SimpleHTTPServer, cgi
class MyHandler(SimpleHTTPServer.SimpleHTTPRequestHandler):
  def do_GET(self):
    form = self.path.find('?') > -1 and dict([x.split('=') for x in self.path.split('?')[1].split('&')]) or {'callback': 'foo'}
    fun_name = form.get('callback', 'foo')
    body = '%s(%s);' % (fun_name, file('json.js').read())
    self.send_response(200)
    self.send_header('Content-Type', 'text/plain')
    self.send_header('Content-Length', len(body))
    self.end_headers()
    self.wfile.write(body)
bhs = BaseHTTPServer.HTTPServer(('', 8000), MyHandler)
bhs.serve_forever()

```

This program returns the contents of the json.js file for each and every request, wrapping it in a function name specified in the `callback` query parameter. It's pretty dumb, but it doesn't need to be smart.

With the server under control, here's the GWT code for my browser application:

```java
public class Hax0r implements EntryPoint {
  protected HashMap scriptTags = new HashMap();
  protected HashMap callbacks = new HashMap();
  protected int curIndex = 0;

  public native static void setup(Hax0r h, String callback) /*-{
    $wnd[callback] = function(someData) {
      h.@com.google.gwt.hax0r.client.Hax0r::handle(Lcom/google/gwt/core/client/JavaScriptObject;)(someData);
    }
  }-*/;

  public String reserveCallback() {
    while (true) {
      if (!callbacks.containsKey(new Integer(curIndex))) {
        callbacks.put(new Integer(curIndex), null);
        return "__gwt_callback" + curIndex++;
      }
    }
  }

  public void addScript(String uniqueId, String url) {
    Element e = DOM.createElement("script");
    DOM.setAttribute(e, "language", "JavaScript");
    DOM.setAttribute(e, "src", url);
    scriptTags.put(uniqueId, e);
    DOM.appendChild(RootPanel.get().getElement(), e);
  }

  public void onModuleLoad() {
    String gdata = "http://www.google.com/base/feeds/snippets?alt=json-in-script&callback=";
    String callbackName = reserveCallback();
    setup(this, callbackName);
    addScript(callbackName, gdata + callbackName);
  }

  public void handle(JavaScriptObject jso) {
    JSONObject json = new JSONObject(jso);
    JSONArray ary = json.get("feed").isObject().get("entry").isArray();
    for (int i = 0; i < ary.size(); ++i) {
      RootPanel.get().add(new Label(ary.get(i).isObject().get("title").isObject().get("$t").toString()));
    }
  }
}
```

This code could use a little explanation. Here are some comments that highlight how the code implements the high-level design I outlined earlier:

*   The `setup()` method is a native method using [GWT's JavaScript Native Interface](../doc/latest/DevGuideCodingBasics.html#DevGuideJavaScriptNativeInterface). JSNI allows developers who need "low level" access to JavaScript to get it. The method simply assigns the `handle()` method to a well-known name on the browser window.
*   `setup()` creates the callback as a function closure because JavaScript will garbage collect plain old function pointers. That is, if we just directly assigned the `handle(`) method to `$wnd[callback]`, it would be immediately garbage collected. To prevent this, we create a new inline anonymous function.
*   Because GWT loads the application's actual code in a child iframe, the global variable `$wnd` is set to point to the `window` handle where the application actually lives. That is, it is set to the `window` handle on the parent frame, rather than the child iframe.
*   The `addScript()` method handles all the DOM munging required to dynamically insert a `<script>` tag into the page. It also tracks the resulting DOM Element handles via unique IDs, so that they can be cleaned up later (though this proof of concept code doesn't actually do any cleanup).
*   The `handle()` method is the actual function that gets called by the JSON response from the server. It contains a loop which just prints out the titles of all the results fetched by the JSON request. Note that this method uses the existing [GWT JSON parsing and manipulation libraries](/javadoc/latest/com/google/gwt/json/client/package-summary.html). The specific sequence of calls is pretty brittle since there is no error checking, but the goal is only to fetch some data to prove the technique worked.
*   Finally, the `onModuleLoad()` method &mdash; which is the main entry point to a GWT application &mdash; simply calls the various other methods to exercise the moving parts.

## One last little thing

By the way, don't bother trying to run the code above; it doesn't work. It may look correct to you &mdash; it certainly did to me, at first &mdash; but it's got a bug in it. The problem is that GWT's JSON library does various checks on the data, including some "instanceof" tests to determine whether parts of the data are objects or arrays. It turns out that all those "instanceof" tests don't work with the code above, causing the application above to fail.

I spent quite a while debugging this, until I finally asked Scott Blum, a GWT Engineer. Scott merely asked a question: "Was the array created in the same window in which you're testing it?"

What Scott knew that I did not, is that the JavaScript classes (like Array) corresponding to the primitive types are constructed along with the window object. Because JavaScript is a prototype-oriented language, the "classes" are really just object instances with special names. These two issues combine to reveal a subtle but important issue: the Array objects from two different windows are not the same object! The expression "x instanceof y" in JavaScript boils down to something like this in pseudocode: "if the "prototype" property of "x" is the same object as "y", return true, else return false."

At this point, you may be wondering how multiple windows entered the discussion. The key is the fact that GWT application code is loaded in a hidden iframe, and so references to objects like `window` refer to that iframe window rather than its parent window. To refer to the browser parent window, GWT defines the `$wnd` variable. The DOM object in GWT also points to the parent window's document object; after all, your application code is interested in manipulating the browser window, not GWT's hidden iframe. As a result, in the code above, the `<script>` tag is added to the parent window, while the code using it resides in a different iframe. This means that the object is created in a window different from where the "instanceof" checks are made, thus causing the issue above.

There are several ways to fix the code: Ultimately, I just needed to make sure that the `<script>` tag and the JSONP callbacks are added to the same iframe in which the GWT application code resides. Here's how I fixed it:

```java
public native static void setup(Hax0r h, String callback) /*-{
    window[callback] = function(someData) {
      h.@com.google.gwt.hax0r.client.Hax0r::handle(Lcom/google/gwt/core/client/JavaScriptObject;)(someData);
    }
  }-*/;

public native void addScript(String uniqueId, String url) /*-{
  var elem = document.createElement("script");
  elem.setAttribute("language", "JavaScript");
  elem.setAttribute("src", url);
  document.getElementsByTagName("body")[0].appendChild(elem);
}-*/;

```

The new version is a rewrite in JSNI of the prior version, coded to use the current `document` object instead of the parent window's `document`. I also had to change the reference to `$wnd` in the `setup()` method to `window`. This ensures that all the relevant pieces exist in the same context, specifically, the child iframe. With these tweaks, the new code works correctly.

## Conclusion

With the change I just described, my proof of concept code works perfectly. Feel free to take this code and try it out &mdash; it really works!

During this project, I learned two things, which I hope I've passed on to you. First of course, is the basic techniques for how to build a mashup using GWT. It's easy to see how you could take the technique I've implemented above and use it to build an application that fetches JSONP data from two (or more!) different sites. Once you can do that, you can do some very interesting things. Mashups are pretty popular these days, and I hope I've given you the know-how, and the excitement, to try building your own.

The second thing I learned from this project is that JavaScript can be very finicky. I'm not trying to say that it isn't a great language; I'm just pointing out that there are a lot of pitfalls for developers, and I walked straight into one. Unfortunately, a lot of the time these pitfalls can get in the way of just doing your work. Though you might argue that GWT itself set the stage for the particular problem I ran into, it is not difficult at all to imagine a pure-JavaScript programmer running into a similar situation when dealing with multiple frames. _Caveat hax0r!_

I hope you found this article useful; but more importantly, I hope you [enjoy using GWT](../download.html)! Happy coding!
