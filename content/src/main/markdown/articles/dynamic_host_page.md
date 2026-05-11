Dynamic Host Page
===

_Jason Hall, Software Engineer_

It's a common enough problem: you want to show your GWT-based app
only to users who are logged in. In this article, we'll take a look at
several ways to accomplish this with a preference for those that make
efficient use of the network.

1.  [Static host page with RPC](#static)
2.  [Security constraint in web.xml](#webxml)
3.  [Servlet as host page](#servlet)
4.  [Template-based host page](#template)

## Static host page with RPC <a id="static"></a>

A common solution is to call a GWT-RPC
service in the onModuleLoad() method of your EntryPoint class to check
if the user is logged in. This initiates a GWT-RPC request as soon as
the GWT module loads.

```java
public void onModuleLoad() {
  // loginService is a GWT-RPC service that checks if the user is logged in
  loginService.checkLoggedIn(new AsyncCallback<Boolean> {
    public void onSuccess(Boolean loggedIn) {
      if (loggedIn) {
        showApp();
      } else {
        Window.Location.assign("/login");
      }
    }
    // ...onFailure()
  }
}
```

Let's examine everything that happens here if the user isn't logged in:

1.  Your app is requested and your GWT host page (YourModule.html) is downloaded
2.  <var>module</var>.nocache.js is requested by the page and is downloaded
3.  <var>MD5</var>.cache.html is selected based on the browser and is downloaded
4.  Your module loads and makes a GWT-RPC call to check if the user is logged in &mdash; since they're not, they are redirected to the login page

That's up to **four** server requests (depending on what is cached)
just to send your user to the login page. And step 3 consists of
downloading your entire GWT app, just to send your user away. Even if
you take advantage of [code-splitting](../doc/latest/DevGuideCodeSplitting.html),
at least some of your code has to be downloaded in order to check if the user is logged in.

The ideal solution would be to only serve your GWT code if the
user is authenticated. That is, never get to step 2 unless the user is
logged in.

## Security constraint in web.xml <a id="webxml"></a>

One way to do this is to use a security constraint in web.xml.
For example, using Google App Engine, you can define a security
constraint that restricts access to all pages (including the static GWT host page)
to logged-in Google Accounts users (see
[Security and Authentication](https://developers.google.com/appengine/docs/java/config/webxml#Security_and_Authentication)). If the user is not logged in, App Engine redirects the user
to the Google Accounts login page.

## Servlet as host page <a id="servlet"></a>

Another more powerful way is to serve your HTML host page from a Java 
servlet instead of a static HTML page. This more flexible approach allows 
for custom authentication schemes and the ability to vary the content of 
the host page based on the user. Here's an example of a simple host page
written as a servlet:

```java
public class GwtHostingServlet extends HttpServlet {

 @Override
 protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

   resp.setContentType("text/html");
   resp.setCharacterEncoding("UTF-8");

   // Print a simple HTML page including a <script> tag referencing your GWT module as the response
   PrintWriter writer = resp.getWriter();
   writer.append("<html><head>")
       .append("<script type=\"text/javascript\" src=\"sample/sample.nocache.js\"></script>")
       .append("</head><body><p>Hello, world!</p></body></html>");
  }
}
```

The response this servlet sends will load and execute your GWT
code just as if it had been referenced in a static HTML host page. But
now that we're writing the HTML in a servlet, we can change the content
of the page being served on a request-by-request basis. This lets us
start doing some more interesting things.

The following example uses the App Engine
[Users API](https:developers.google.com/appengine/docs/java/users/overview) to see if the
user is logged in. Even if you're not using App Engine, you can imagine
how the code would look slightly different in your servlet environment.

```java
// In GwtHostingServlet's doGet() method...
PrintWriter writer = resp.getWriter();
writer.append("<html><head>");

UserService userService = UserServiceFactory.getUserService();
if (userService.isUserLoggedIn()) {
  // Add a <script> tag to serve your app's generated JS code
  writer.append("<script type=\"text/javascript\" src=\"sample/sample.nocache.js\"></script>");
  writer.append("</head><body>");
  // Add a link to log out
  writer.append("<a href=\"" + userService.createLogoutURL("/") + "\">Log out</a>");
} else {
  writer.append("</head><body>");
  // Add a link to log in
  writer.append("<a href=\"" + userService.createLoginURL("/") + "\">Log in</a>");
}
writer.append("</body></html>");
```

This servlet will now serve your GWT code only to logged-in
users, and will show a link on the page to log in or out.

But there's even more fun stuff we can do with this dynamic
hosting servlet. Suppose you want to pass some data like the user's
email address from the servlet to the GWT code so that it is available
as soon as the GWT module loads.

You could make a GWT-RPC call in your onModuleLoad() method to
get this data, but that means you're making one request to download your
GWT module, then immediately making another request to get this data. A
more efficient way is to write the initial data as a Javascript variable
into the host page itself.

```java
// In GwtHostingServlet's doGet() method...
writer.append("<html><head>");
writer.append("<script type=\"text/javascript\" src=\"sample/sample.nocache.js\"></script>");

// Open a second <script> tag where we will define some extra data
writer.append("<script type=\"text/javascript\">");

// Define a global JSON object called "info" which can contain some simple key/value pairs
writer.append("var info = { ");

// Include the user's email with the key "email"
writer.append("\"email\" : \"" + userService.getCurrentUser().getEmail() + "\"");

// End the JSON object definition
writer.append(" };");

// End the <script> tag
writer.append("</script>");
writer.append("</head><body>Hello, world!</body></html>");
```

Now your GWT code can access the data using JSNI, like so:

```java
public native String getEmail() /*-{
  return $wnd.info['email'];
}-*/;
```

Alternatively, you can take advantage of GWT's
[Dictionary](/javadoc/latest/com/google/gwt/i18n/client/Dictionary.html) class:

```java
public void onModuleLoad() {
  // Looks for a JS variable called "info" in the global scope
  Dictionary info = Dictionary.getDictionary("info");
  String email = info.get("email");
  Window.alert("Welcome, " + email + "!");
}
```

## Template-based host page <a id="template"></a>

As you add more dynamism to your hosting page, it may be
worthwhile to consider using a templating language like JSP to make your
code more readable. Here's our example as a JSP page instead of a
servlet:

```jsp
<!-- gwt-hosting.jsp -->
<html>
 <head>
<%
   UserService userService = UserServiceFactory.getUserService();
   if (userService.isUserLoggedIn()) {
%>
    <script type="text/javascript" src="sample/sample.nocache.js"></script>
    <script type="text/javascript">
      var info = { "email" : "<%= userService.getCurrentUser().getEmail() %>" };
    </script>
  </head>
  <body>
  <a href="<%= userService.createLogoutURL(request.getRequestURI()) %>">Log out</a>
<%
   } else {
%>
  </head>
  <body>
    <a href="<%= userService.createLoginURL(request.getRequestURI()) %>">Log in</a>
<%
   }
%>
 </body>
</html>
```

You can make this JSP page your welcome file by specifying it in your web.xml file:

```xml
<welcome-file-list>
  <welcome-file>gwt-hosting.jsp</welcome-file>
</welcome-file-list>
```

These are some basic examples of how to minimize HTTP requests by
hosting your GWT app dynamically. With these techniques, you should be
able to eliminate GWT-RPC requests being made as soon as the module
loads, which means less waiting for the user and a noticeably faster GWT
application.
