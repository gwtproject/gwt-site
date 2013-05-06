<style>

div.screenshot img {
  margin: 20px;
}

</style>
<i>Dan Morrill, Google Developer Relations Team</i>
<br>
<i>Updated January 2009</i>


<h2>Introduction</h2>

<p>What's the fun of a web application if you're stuck on your own server? It's much more fun to get out and meet new code, which is where web mashups come in. Mashups let you build powerful applications surprisingly quickly, if you have the right tools. Recently I've been working on a mashup project using the <a href="/web-toolkit/">GWT</a> (GWT). One of the goals of my project was to let applications written using GWT integrate with other web applications that expose data in JavaScript Object Notation (JSON) format.</p>

<p>Sounds straightforward, right? Well, it was--almost--read on to learn why! This article is a case study of how to incorporate mashup-style JSON data into a GWT application. This will be relevant to you if you're a GWT user, but even general Ajax developers may find it interesting.</p>

<h3>A note on security</h3>

<p>This article discusses JSON. JSON is very cool, but it can also be quite dangerous to the unwary. Because JSON is a subset of JavaScript, it is executable code; this makes it vulnerable to a variety of attacks. If you're using JSON, it's very important to be aware of these security risks and apply countermeasures.</p>

<p>This is too large a topic for us to discuss here. Fortunately, we've already discussed it elsewhere: check out this link on <a href="security_for_gwt_applications.html">Security for GWT Applications</a>. I urge you to read that document and familiarize yourself with the security risks before you put into practice any of the things discussed here.</p>

<h2>JSON basics</h2>

<p>What is JSON, anyway? Well, by a happy coincidence, it turns out that the syntax JavaScript uses for defining data objects is rather broadly compatible with other languages. This makes it a sort of lowest-common-denominator syntax for specifying data. Other folks have covered this better than I can, so I'll just send you straight to the source: <a href="http://json.org">json.org</a>.</p>

<p>One of the key benefits of JSON is that because it is essentially JavaScript syntax, browsers can &quot;parse&quot; JSON data simply by calling the JavaScript eval function. This is both easy and fast because it takes advantage of native code in the browsers to do the parsing. (This is also why JSON can be a security problem; if the JSON string actually contains non-JSON code, then calling eval on it is quite dangerous.)</p>

<p>Once you have a service that can produce JSON data, there are generally three different ways to use it. These methods can be categorized by how you fetch the data.</p>

<ul>
  <li>The server can output a string containing raw JSON data that the browser fetches with an <code>XMLHTTPRequest</code> and manually passes to the eval function.

          Example server-generated string: <code>{'data': ['foo', 'bar', 'baz']}</code></li>

  <li>The server can output a string containing JavaScript code that assigns a JSON object to a variable; the browser would fetch this using a &lt;script&gt; tag and then extract the parsed object by referring to the variable by name.

          Example server-generated string: <code>var result = {'data': ['foo', 'bar', 'baz']};</code></li>

  <li>The server can output a string containing JavaScript code that passes a JSON object to a function specified in the request URL; the browser would fetch this using a &lt;script&gt; tag, which will automatically invoke the function as if it were an event callback, as soon as the JavaScript is parsed.

          Example server-generated string: <code>handle_result({'data': ['foo', 'bar', 'baz']});</code></li>
</ul>

<p>The term JSON technically refers only to the data representation syntax (which is where the &quot;Object Notation&quot; part of its name comes from) and so JSON is a strict subset of JavaScript. Because of this, those last two methods aren't technically JSON--they're JavaScript code that deals with data in JSON format. They are still close cousins to JSON, though, and frequently &quot;JSON&quot; is used as a blanket term for all such cases. The third method in particular is frequently called &quot;JSON with Padding&quot; (JSONP); the earliest description of this technique that I'm aware of is here: <a href="http://bob.pythonmac.org/archives/2005/12/05/remote-json-jsonp/">Remote JSON - JSONP</a>.</p>

<p>The primary difference between these techniques is how they are fetched. Since the first case--that is, pure JSON--contains no executable component, it's generally only useful with XMLHTTPRequests. Because that function is subject to Same-Origin restrictions, that means that pure-JSON can only be used as a data transmission technique between a browser application and its HTTP server.</p>

<p>The latter two techniques, however, fetch the strings using dynamic &lt;script&gt; tag insertions. Since that technique is not limited by Same-Origin restrictions, it can be used cross-domain. Coupled with services that expose their data in JavaScript syntax, this allows browsers to make requests for data from several different servers. This is the technique that makes mashups possible. (More specifically--mashups that run entirely inside the browser. You can also create mashups using server-side proxies if you don't want to use JSONP and don't mind maintaining your own server.)</p>

<h2>High-level design</h2>

<p>Now that we have the JSON basics in hand, what about my project? The task I was working on involved mashing up data from another service--specifically, Google Base. This means that I needed to use the <a href="http://code.google.com/apis/gdata/index.html">Google Data API</a> for fetching information. Google's GData servers provide XML, JSON, and JSONP-style interfaces, to allow developers maximum flexibility in building applications. For my project, I wanted to build an in-browser mashup, which means I needed to use the <a href="/gdata/docs/json">Google Data API's  JSONP-style interface</a>.</p>

<p>Since GWT applications are written in Java, there is a compilation phase that compiles the Java source to JavaScript. The compiler also optimizes the generated code, and one of the optimizations it performs is code obfuscation, which makes the output smaller and thus faster to load. A downside of this, though, is that the function names in the output JavaScript code are unpredictable. This makes it difficult to specify a callback function name to a JSONP service.</p>

<p>There is an effective technique for cases like this that we sometimes refer to as a &quot;function bridge.&quot; We've <a href="/web-toolkit/doc/1.6/DevGuideCodingBasics#DevGuideJavaScriptNativeInterface">documented it</a> in our GWT FAQs, but in a nutshell, the technique involves creating a handle for the obfuscated function and copying it to a well-known variable name in the JavaScript namespace. When outside JavaScript code (such as a Google Data server response) invokes the function under its well-known name, it actually invokes the real function via the copied handle. (Check out the link earlier in this paragraph to see a basic example.)</p>

<p>However, my project made a lot of those Google Data requests. That could conceivably leave quite a few of these function handles lying around, so there was some bookkeeping that needed to be managed.</p>

<p>After taking all those points into consideration, I chose this rough design:</p>

<ul>
  <li>Each request for Google Data JSON data is assigned a unique token.</li>
  <li>A new callback function for each request is created on demand using GWT's JavaScript Native Interface (JSNI); the request's token is included in the function's name to make sure it's unique.</li>
  <li>The callback is actually a JavaScript function closure over the token; the token is passed along to the inner function when the callback is invoked.</li>
  <li>The same inner function is used for each callback; this function is actually a method on my GWT class, which uses the token to dispatch the response data to the appropriate GWT code.</li>
</ul>

<p>This strategy has the following &quot;moving parts&quot;:</p>

<ul>
  <li>A single dispatch method on my GWT class to handle incoming responses from the Google Data server</li>
  <li>A second method on my GWT class that uses JSNI to construct the function closures and initiate the Google Data request</li>
</ul>

<p>Ultimately, there should also be a cleanup phase which removes the callback handles to avoid cluttering the JavaScript namespace, but that's easy enough to add later. To get started, I proceeded with the other design elements above.</p>

<p>Hopefully you've followed along so far, but if not--never fear, I've included source code below!</p>

<h2>First implementation</h2>

<p>The first thing I did was write some code to demonstrate the basic concept. I did have an idea of how the final API itself ought to look, but the objective at this point was to prove the concept, not design the final API. So, I started by implementing a single class that contained all the key parts that I expected my final API to have. First, I thought, I'd prove that it worked, and then I'd refactor it into a real API.</p>

<p>Specifically, here are the key requirements I had:</p>

<ul>
  <li>Must abstract the dynamic &lt;script&gt; insertion behind a method call</li>
   <li>Must handle bookkeeping of &lt;script&gt; tags (to be able to clean them up later and prevent memory leaks)</li>
   <li>Must provide a method to generate and reserve a callback function name </li>
</ul>

<p>At this point, I needed a Google Data feed to test with. I decided to fetch the Google Base &quot;snippets&quot; URL, which is a GData feed in JSON mode. The base URL for this feed is <code>http://www.google.com/base/feeds/snippets</code>. To request JSON data as output, you add some GET parameters to the URL: <code>?alt=json-in-script&callback=foo</code>. The last value--foo--is the name of a callback function (that is, the JSONP hook). The Google Data feed's output will wrap the JavaScript object with a call to that function.</p>

<p>If you want to see a full example of the Google Data output, check out this URL: <code>http://www.google.com/base/feeds/snippets</code>. You'll quickly see that there's a lot of data, even for just a single result. To help you visualize the general structure of the feed, here's a much smaller custom-built sample result that contains only the data relevant to this story:</p>

<pre class="code">
{
  'feed': {
    'entry': [
      {'title': {'type': 'text', '$t': 'Some Text'}},
      {'title': {'type': 'text', '$t': 'Some More Text'}}
    ]
  }
}
</pre>

<p>The core structure is fairly simple, as you can see; most of the length of the real Google Data feed comes from the various data fields.</p>

<p>To keep my development simple, I used that minimized example for testing so I wouldn't be overwhelmed by the full Google Data feed. Of course, that meant I needed a web server to serve up my custom version of the JSON data. Normally I would have just served it from the built-in Tomcat instance included in GWT's hosted mode. However, that would have meant that my JSON data and my GWT application would be served from the same site. Since my ultimate goal was to load the real JSON data from a different site, I needed a second, separate local server from which to fetch my JSON data--otherwise, it wouldn't be an accurate simulation. Since a full web server instance would have been lots of work to set up, I created a tiny custom server with this Python program:</p>

<pre class="code">
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
</pre>

<p>This program returns the contents of the json.js file for each and every request, wrapping it in a function name specified in the <code>callback</code> query parameter. It's pretty dumb, but it doesn't need to be smart.</p>

<p>With the server under control, here's the GWT code for my browser application:</p>

<pre class="code">
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
        return &quot;__gwt_callback&quot; + curIndex++;
      }
    }
  }
 
  public void addScript(String uniqueId, String url) {
    Element e = DOM.createElement(&quot;script&quot;);
    DOM.setAttribute(e, &quot;language&quot;, &quot;JavaScript&quot;);
    DOM.setAttribute(e, &quot;src&quot;, url);
    scriptTags.put(uniqueId, e);
    DOM.appendChild(RootPanel.get().getElement(), e);
  }
 
  public void onModuleLoad() {
    String gdata = &quot;http://www.google.com/base/feeds/snippets?alt=json-in-script&callback=&quot;;
    String callbackName = reserveCallback();
    setup(this, callbackName);
    addScript(callbackName, gdata + callbackName);
  }
 
  public void handle(JavaScriptObject jso) {
    JSONObject json = new JSONObject(jso);
    JSONArray ary = json.get(&quot;feed&quot;).isObject().get(&quot;entry&quot;).isArray();
    for (int i = 0; i < ary.size(); ++i) {
      RootPanel.get().add(new Label(ary.get(i).isObject().get(&quot;title&quot;).isObject().get(&quot;$t&quot;).toString()));
    }
  }
}
</pre>

<p>This code could use a little explanation. Here are some comments that highlight how the code implements the high-level design I outlined earlier:</p>

<ul>
  <li>The <code>setup()</code> method is a native method using <a href="../doc/latest/DevGuideCodingBasics.html#DevGuideJavaScriptNativeInterface">GWT's JavaScript Native Interface</a>. JSNI allows developers who need &quot;low level&quot; access to JavaScript to get it. The method simply assigns the <code>handle()</code> method to a well-known name on the browser window.</li>
  <li><code>setup()</code> creates the callback as a function closure because JavaScript will garbage collect plain old function pointers. That is, if we just directly assigned the <code>handle(</code>) method to <code>$wnd[callback]</code>, it would be immediately garbage collected. To prevent this, we create a new inline anonymous function.</li>
  <li>Because GWT loads the application's actual code in a child iframe, the global variable <code>$wnd</code> is set to point to the <code>window</code> handle where the application actually lives. That is, it is set to the <code>window</code> handle on the parent frame, rather than the child iframe.</li>
  <li>The <code>addScript()</code> method handles all the DOM munging required to dynamically insert a &lt;script&gt; tag into the page. It also tracks the resulting DOM Element handles via unique IDs, so that they can be cleaned up later (though this proof of concept code doesn't actually do any cleanup).</li>
  <li>The <code>handle()</code> method is the actual function that gets called by the JSON response from the server. It contains a loop which just prints out the titles of all the results fetched by the JSON request. Note that this method uses the existing <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/1.5/com/google/gwt/json/client/package-summary.html">GWT JSON parsing and manipulation libraries</a>. The specific sequence of calls is pretty brittle since there is no error checking, but the goal is only to fetch some data to prove the technique worked.</li>
  <li>Finally, the <code>onModuleLoad()</code> method--which is the main entry point to a GWT application--simply calls the various other methods to exercise the moving parts.</li>
</ul>

<h2>One last little thing</h2>

<p>By the way, don't bother trying to run the code above; it doesn't work. It may look correct to you--it certainly did to me, at first--but it's got a bug in it. The problem is that GWT's JSON library does various checks on the data, including some &quot;instanceof&quot; tests to determine whether parts of the data are objects or arrays. It turns out that all those "instanceof" tests don't work with the code above, causing the application above to fail.</p>

<p>I spent quite a while debugging this, until I finally asked Scott Blum, a GWT Engineer. Scott merely asked a question: &quot;Was the array created in the same window in which you're testing it?&quot;</p>

<p>What Scott knew that I did not, is that the JavaScript classes (like Array) corresponding to the primitive types are constructed along with the window object. Because JavaScript is a prototype-oriented language, the &quot;classes&quot; are really just object instances with special names. These two issues combine to reveal a subtle but important issue: the Array objects from two different windows are not the same object! The expression &quot;x instanceof y&quot; in JavaScript boils down to something like this in pseudocode: &quot;if the &quot;prototype&quot; property of &quot;x&quot; is the same object as &quot;y&quot;, return true, else return false.&quot;</p>

<p>At this point, you may be wondering how multiple windows entered the discussion. The key is the fact that GWT application code is loaded in a hidden iframe, and so references to objects like <code>window</code> refer to that iframe window rather than its parent window. To refer to the browser parent window, GWT defines the <code>$wnd</code> variable. The DOM object in GWT also points to the parent window's document object; after all, your application code is interested in manipulating the browser window, not GWT's hidden iframe. As a result, in the code above, the &lt;script&gt; tag is added to the parent window, while the code using it resides in a different iframe. This means that the object is created in a window different from where the &quot;instanceof&quot; checks are made, thus causing the issue above.</p>

<p>There are several ways to fix the code: Ultimately, I just needed to make sure that the &lt;script&gt; tag and the JSONP callbacks are added to the same iframe in which the GWT application code resides. Here's how I fixed it:</p>

<pre class="code">
public native static void setup(Hax0r h, String callback) /*-{
    window[callback] = function(someData) {
      h.@com.google.gwt.hax0r.client.Hax0r::handle(Lcom/google/gwt/core/client/JavaScriptObject;)(someData);
    }
  }-*/;

public native void addScript(String uniqueId, String url) /*-{
  var elem = document.createElement(&quot;script&quot;);
  elem.setAttribute(&quot;language&quot;, &quot;JavaScript&quot;);
  elem.setAttribute(&quot;src&quot;, url);
  document.getElementsByTagName(&quot;body&quot;)[0].appendChild(elem);
}-*/;
</pre>

<p>The new version is a rewrite in JSNI of the prior version, coded to use the current <code>document</code> object instead of the parent window's <code>document</code>. I also had to change the reference to <code>$wnd</code> in the <code>setup()</code> method to <code>window</code>. This ensures that all the relevant pieces exist in the same context, specifically, the child iframe. With these tweaks, the new code works correctly.</p>

<h2>Conclusion</h2>

<p>With the change I just described, my proof of concept code works perfectly. Feel free to take this code and try it out--it really works!</p>

<p>During this project, I learned two things, which I hope I've passed on to you. First of course, is the basic techniques for how to build a mashup using GWT. It's easy to see how you could take the technique I've implemented above and use it to build an application that fetches JSONP data from two (or more!) different sites. Once you can do that, you can do some very interesting things. Mashups are pretty popular these days, and I hope I've given you the know-how, and the excitement, to try building your own.</p>

<p>The second thing I learned from this project is that JavaScript can be very finicky. I'm not trying to say that it isn't a great language; I'm just pointing out that there are a lot of pitfalls for developers, and I walked straight into one. Unfortunately, a lot of the time these pitfalls can get in the way of just doing your work. Though you might argue that GWT itself set the stage for the particular problem I ran into, it is not difficult at all to imagine a pure-JavaScript programmer running into a similar situation when dealing with multiple frames. <i>Caveat hax0r!</i></p>

<p>I hope you found this article useful; but more importantly, I hope you <a href="../download.html">enjoy using GWT</a>! Happy coding!</p>

