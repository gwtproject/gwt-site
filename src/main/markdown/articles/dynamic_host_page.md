<p><i>Jason Hall, Software Engineer</i></p>

<p>It's a common enough problem: you want to show your GWT-based app
only to users who are logged in. In this article, we'll take a look at
several ways to accomplish this with a preference for those that make
efficient use of the network.</p>

<ol class="toc" id="pageToc">
  <li><a href="#static">Static host page with RPC</a></li>
  <li><a href="#webxml">Security constraint in web.xml</a></li>
  <li><a href="#servlet">Servlet as host page</a></li>
  <li><a href="#template">Template-based host page</a></li>
</ol>

<h2 id="static">Static host page with RPC</h2>

<p>A common solution is to call a GWT-RPC
service in the onModuleLoad() method of your EntryPoint class to check
if the user is logged in. This initiates a GWT-RPC request as soon as
the GWT module loads.</p>

<pre class="prettyprint">
public void onModuleLoad() {
  // loginService is a GWT-RPC service that checks if the user is logged in
  loginService.checkLoggedIn(new AsyncCallback&lt;Boolean&gt; {
    public void onSuccess(Boolean loggedIn) {
      if (loggedIn) {
        showApp();
      } else {
        Window.Location.assign(&quot;/login&quot;);
      }
    }
    // ...onFailure()
  }
}
</pre>

<p>Let's examine everything that happens here if the user isn't logged in:</p>

<ol>
<li>Your app is requested and your GWT host page (YourModule.html) is downloaded</li>
<li><var>module</var>.nocache.js is requested by the page and is downloaded</li>
<li><var>MD5</var>.cache.html is selected based on the browser and is downloaded</li>
<li>Your module loads and makes a GWT-RPC call to check if the user is logged in -- since they're not, they are redirected to the login page</li>
</ol>

<p>That's up to <b>four</b> server requests (depending on what is cached)
just to send your user to the login page. And step 3 consists of
downloading your entire GWT app, just to send your user away. Even if
you take advantage of <a href="../doc/latest/DevGuideCodeSplitting.html">code-splitting</a>,
at least some of your code has to be downloaded in order to check if the user is logged in.</p>

<p>The ideal solution would be to only serve your GWT code if the
user is authenticated. That is, never get to step 2 unless the user is
logged in.</p>

<h2 id="webxml">Security constraint in web.xml</h2>
<p>One way to do this is to use a security constraint in web.xml.
For example, using Google App Engine, you can define a security
constraint that restricts access to all pages (including the static GWT host page)
to logged-in Google Accounts users (see
<a href="https://developers.google.com/appengine/docs/java/config/webxml#Security_and_Authentication">Security and Authentication</a>). If the user is not logged in, App Engine redirects the user
to the Google Accounts login page.</p>

<h2 id="servlet">Servlet as host page</h2>
<p>Another more powerful way is to serve your HTML host page from a Java 
servlet instead of a static HTML page. This more flexible approach allows 
for custom authentication schemes and the ability to vary the content of 
the host page based on the user. Here's an example of a simple host page
written as a servlet:</p>

<pre class="prettyprint">
public class GwtHostingServlet extends HttpServlet {

 @Override
 protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

   resp.setContentType(&quot;text/html&quot;);
   resp.setCharacterEncoding(&quot;UTF-8&quot;);

   // Print a simple HTML page including a &lt;script&gt; tag referencing your GWT module as the response
   PrintWriter writer = resp.getWriter();
   writer.append(&quot;&lt;html&gt;&lt;head&gt;&quot;)
       .append(&quot;&lt;script type=\&quot;text/javascript\&quot; src=\&quot;sample/sample.nocache.js\&quot;&gt;&lt;/script&gt;&quot;)
       .append(&quot;&lt;/head&gt;&lt;body&gt;&lt;p&gt;Hello, world!&lt;/p&gt;&lt;/body&gt;&lt;/html&gt;&quot;);
  }
}
</pre>

<p>The response this servlet sends will load and execute your GWT
code just as if it had been referenced in a static HTML host page. But
now that we're writing the HTML in a servlet, we can change the content
of the page being served on a request-by-request basis. This lets us
start doing some more interesting things.</p>

<p>The following example uses the App Engine
<a href="https:developers.google.com/appengine/docs/java/users/overview">Users API</a> to see if the
user is logged in. Even if you're not using App Engine, you can imagine
how the code would look slightly different in your servlet environment.</p>

<pre class="prettyprint">
// In GwtHostingServlet's doGet() method...
PrintWriter writer = resp.getWriter();
writer.append(&quot;&lt;html&gt;&lt;head&gt;&quot;);

UserService userService = UserServiceFactory.getUserService();
if (userService.isUserLoggedIn()) {
  // Add a &lt;script&gt; tag to serve your app's generated JS code
  writer.append(&quot;&lt;script type=\&quot;text/javascript\&quot; src=\&quot;sample/sample.nocache.js\&quot;&gt;&lt;/script&gt;&quot;);
  writer.append(&quot;&lt;/head&gt;&lt;body&gt;&quot;);
  // Add a link to log out
  writer.append(&quot;&lt;a href=\&quot;&quot; + userService.createLogoutURL(&quot;/&quot;) + &quot;\&quot;&gt;Log out&lt;/a&gt;&quot;);
} else {
  writer.append(&quot;&lt;/head&gt;&lt;body&gt;&quot;);
  // Add a link to log in
  writer.append(&quot;&lt;a href=\&quot;&quot; + userService.createLoginURL(&quot;/&quot;) + &quot;\&quot;&gt;Log in&lt;/a&gt;&quot;);
}
writer.append(&quot;&lt;/body&gt;&lt;/html&gt;&quot;);
</pre>

<p>This servlet will now serve your GWT code only to logged-in
users, and will show a link on the page to log in or out.</p>

<p>But there's even more fun stuff we can do with this dynamic
hosting servlet. Suppose you want to pass some data like the user's
email address from the servlet to the GWT code so that it is available
as soon as the GWT module loads.</p>

<p>You could make a GWT-RPC call in your onModuleLoad() method to
get this data, but that means you're making one request to download your
GWT module, then immediately making another request to get this data. A
more efficient way is to write the initial data as a Javascript variable
into the host page itself.</p>

<pre class="prettyprint">
// In GwtHostingServlet's doGet() method...
writer.append(&quot;&lt;html&gt;&lt;head&gt;&quot;);
writer.append(&quot;&lt;script type=\&quot;text/javascript\&quot; src=\&quot;sample/sample.nocache.js\&quot;&gt;&lt;/script&gt;&quot;);

// Open a second &lt;script&gt; tag where we will define some extra data
writer.append(&quot;&lt;script type=\&quot;text/javascript\&quot;&gt;&quot;);

// Define a global JSON object called &quot;info&quot; which can contain some simple key/value pairs
writer.append(&quot;var info = { &quot;);

// Include the user's email with the key &quot;email&quot;
writer.append(&quot;\&quot;email\&quot; : \&quot;&quot; + userService.getCurrentUser().getEmail() + &quot;\&quot;&quot;);

// End the JSON object definition
writer.append(&quot; };&quot;);

// End the &lt;script&gt; tag
writer.append(&quot;&lt;/script&gt;&quot;);
writer.append(&quot;&lt;/head&gt;&lt;body&gt;Hello, world!&lt;/body&gt;&lt;/html&gt;&quot;);
</pre>

<p>Now your GWT code can access the data using JSNI, like so:</p>
<pre class="prettyprint">
public native String getEmail() /*-{
  return $wnd.info['email'];
}-*/;
</pre>

<p>Alternatively, you can take advantage of GWT's
<a href="/javadoc/latest/com/google/gwt/i18n/client/Dictionary.html">Dictionary</a> class:</p>

<pre class="prettyprint">
public void onModuleLoad() {
  // Looks for a JS variable called &quot;info&quot; in the global scope
  Dictionary info = Dictionary.getDictionary(&quot;info&quot;);
  String email = info.get(&quot;email&quot;);
  Window.alert(&quot;Welcome, &quot; + email + &quot;!&quot;);
}
</pre>

<h2 id="template">Template-based host page</h2>
<p>As you add more dynamism to your hosting page, it may be
worthwhile to consider using a templating language like JSP to make your
code more readable. Here's our example as a JSP page instead of a
servlet:</p>

<pre class="prettyprint">
&lt;!-- gwt-hosting.jsp --&gt;
&lt;html&gt;
 &lt;head&gt;
&lt;%
   UserService userService = UserServiceFactory.getUserService();
   if (userService.isUserLoggedIn()) {
%&gt;
    &lt;script type=&quot;text/javascript&quot; src=&quot;sample/sample.nocache.js&quot;&gt;&lt;/script&gt;
    &lt;script type=&quot;text/javascript&quot;&gt;
      var info = { &quot;email&quot; : &quot;&lt;%= userService.getCurrentUser().getEmail() %&gt;&quot; };
    &lt;/script&gt;
  &lt;/head&gt;
  &lt;body&gt;
  &lt;a href=&quot;&lt;%= userService.createLogoutURL(request.getRequestURI()) %&gt;&quot;&gt;Log out&lt;/a&gt;
&lt;%
   } else {
%&gt;
  &lt;/head&gt;
  &lt;body&gt;
    &lt;a href=&quot;&lt;%= userService.createLoginURL(request.getRequestURI()) %&gt;&quot;&gt;Log in&lt;/a&gt;
&lt;%
   }
%&gt;
 &lt;/body&gt;
&lt;/html&gt;
</pre>

<p>You can make this JSP page your welcome file by specifying it in your web.xml file:</p>
<pre class="prettyprint">
&lt;welcome-file-list&gt;
  &lt;welcome-file&gt;gwt-hosting.jsp&lt;/welcome-file&gt;
&lt;/welcome-file-list&gt;
</pre>

<p>These are some basic examples of how to minimize HTTP requests by
hosting your GWT app dynamically. With these techniques, you should be
able to eliminate GWT-RPC requests being made as soon as the module
loads, which means less waiting for the user and a noticeably faster GWT
application.</p>

