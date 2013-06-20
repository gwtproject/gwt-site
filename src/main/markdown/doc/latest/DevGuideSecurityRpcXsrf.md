<p>
Cross-Site Request Forgery (XSRF or CSRF) is a type of web attack where an
attacker can perform actions on behalf of an authenticated user without user's
knowledge.  Typically, it involves crafting a malicious HTML page, which, once
visited by a victim, will cause the victim's browser to issue an
attacker-controlled request to a third-party domain. If the victim is
authenticated to the third-party domain, the request will be sent with the
browser's cookies for that domain, and could potentially trigger an undesirable
action on behalf of the victim and without victim's consent - for example,
delete or modify a blog or add a mail forward rule.
</p>

<p>
This document explains how developers can protect GWT RPCs against XSRF
attacking using GWT's builtin XSRF protection introduced in GWT 2.3
</p>

<ol class="toc" id="pageToc">
  <li><a href="#Overview">Overview</a></li>
  <li><a href="#ServerSide">Server-side changes</code></a></li>
  <li><a href="#ClientSide">Client-side changes</a></li>
</ol>

<h2 id="Overview">Overview</h2>
<p>
RPC XSRF protection is built using <a href="/javadoc/latest/com/google/gwt/user/client/rpc/RpcToken.html"><code>RpcToken</code></a>
feature, which lets a developer set a token on a RPC endpoint using <a
href="/javadoc/latest/com/google/gwt/user/client/rpc/HasRpcToken.html">
<code>HasRpcToken</code></a> interface and have that token included with each
RPC call made via that endpoint.
</p>
<p>
Default XSRF protection implementation derives XSRF token from a session
authentication cookie by generating an MD5 hash of the session cookie value and
using the resulting hash as XSRF token. This stateless XSRF protection
implementation relies on the fact that attacker doesn't have access to the
session cookie and thus is unable to generate valid XSRF token.
</p>

<h2 id="ServerSide">Server-side changes</h2>
<h3>Configure <code>XsrfTokenServiceServlet</code></h3>
<p>
Client-side code will obtain XSRF tokens by calling
<a href="/javadoc/latest/com/google/gwt/user/client/rpc/XsrfTokenService.html">
<code>XsrfTokenService.getNewXsrfToken()</code></a> server-side implementation
configured in <code>web.xml</code>:
</p>

<pre class="prettyprint">
&lt;servlet&gt;
  &lt;servlet-name&gt;xsrf&lt;/servlet-name&gt;
  &lt;servlet-class&gt;
    com.google.gwt.user.server.rpc.XsrfTokenServiceServlet
  &lt;/servlet-class&gt;
&lt;/servlet&gt;
&lt;servlet-mapping&gt;
  &lt;servlet-name&gt;xsrf&lt;/servlet-name&gt;
  &lt;url-pattern&gt;/gwt/xsrf&lt;/url-pattern&gt;
&lt;/servlet-mapping&gt;
</pre>

<p>
Since XSRF token is tied to an authentication session cookie, the name of that
cookie must be passed to the <code>XsrfTokenServiceServlet</code> as well as
all XSRF-protected RPC service servlets. This is done via context parameter in
<code>web.xml</code>:
</p>

<pre class="prettyprint">
&lt;context-param&gt;
  &lt;param-name&gt;gwt.xsrf.session_cookie_name&lt;/param-name&gt;
  &lt;param-value>JSESSIONID&lt;/param-value&gt;
&lt;/context-param&gt;
</pre>

<p class="note">
<strong>Note:</strong> Servlet initialization parameter
(<code>&lt;init-param&gt;</code>) can also be used to pass the name of the
session cookie to each servlet individually.
</p>

<h3>Make RPC servlets XSRF protected</h3>
<p>
All server-side implementations of RPC services must extend <a href="/javadoc/latest/com/google/gwt/user/server/rpc/XsrfProtectedServiceServlet.html"><code>XsrfProtectedServiceServlet</code></a>:
</p>

<pre class="prettyprint">
package com.example.foo.server;

import com.google.gwt.user.server.rpc.XsrfProtectedServiceServlet; 

import com.example.client.MyService;

public class MyServiceImpl extends XsrfProtectedServiceServlet implements
    MyService {

  public String myMethod(String s) {
    // Do something interesting with 's' here on the server.
    return s;
  }
}
</pre>

<h2 id="ClientSide">Client-side changes</h2>
<h3>Make client RPC interfaces XSRF protected</h3>
<p>
Client-side RPC interfaces can be marked as XSRF protected using one of the following ways:
</p>
<ul>
  <li>by extending <a
  href="/javadoc/latest/com/google/gwt/user/client/rpc/XsrfProtectedService.html">
  <code>XsrfProtectedService</code></a>, in which case all methods calls will
  require <code>XsrfToken</code>:
  <pre class="prettyprint">
  package com.example.foo.client;

  import com.google.gwt.user.client.rpc.XsrfProtectedService;

  public interface MyService extends XsrfProtectedService {
    public String myMethod(String s);
  }
  </pre>
  </li>
  <li>by explicitly annotating interface or methods with <a href="/javadoc/latest/com/google/gwt/user/server/rpc/XsrfProtect.html"><code>@XsrfProtect</code></a> annotation. 
<a href="/javadoc/latest/com/google/gwt/user/server/rpc/NoXsrfProtect.html">
<code>@NoXsrfProtect</code></a> annotation can be used to disable XSRF
protection on a method or service to disable XSRF protection:
  <pre class="prettyprint">
  package com.example.foo.client;

  import com.google.gwt.user.client.rpc.RemoteService;
  import com.google.gwt.user.server.rpc.XsrfProtect

  @XsrfProtect
  public interface MyService extends RemoteService {
    public String myMethod(String s);
  }
  </pre>
  Method level annotations override RPC interface level annoatations. If no
  annotations are present and the RPC interface contains a method that returns
  <a href="/javadoc/latest/com/google/gwt/user/client/rpc/RpcToken.html">
  <code>RpcToken</code></a> or its implementation, then XSRF token validation is
  performed on all methods of that interface except for the method returning
  RpcToken.
  </li>
</ul>
<p class="note">
<strong>Tip:</strong> To specify which <code>RpcToken</code> implementation GWT should generate serializers for use <a href="/javadoc/latest/com/google/gwt/user/client/rpc/RpcToken.RpcTokenImplementation.html"><code>@RpcTokenImplementation</code></a> annotation.
</p>

<h3>Include <code>XsrfToken</code> with RPC calls</h3>
<p>
To make a call to an XSRF protected service client must obtain a valid
<code>XsrfToken</code> and set it on the service endpoint by casting the 
service's asynchronous interface to <a href="/javadoc/latest/com/google/gwt/user/client/rpc/HasRpcToken.html"><code>HasRpcToken</code></a> and calling <code>setRpcToken()</code> method:
</p>

<pre class="prettyprint">
XsrfTokenServiceAsync xsrf = (XsrfTokenServiceAsync)GWT.create(XsrfTokenService.class);
((ServiceDefTarget)xsrf).setServiceEntryPoint(GWT.getModuleBaseURL() + "xsrf");
xsrf.getNewXsrfToken(new AsyncCallback&lt;XsrfToken&gt;() {

  public void onSuccess(XsrfToken token) {
    MyServiceAsync rpc = (MyServiceAsync)GWT.create(MyService.class);
    ((HasRpcToken) rpc).setRpcToken(token);

    // make XSRF protected RPC call
    rpc.doStuff(new AsyncCallback&lt;Void&gt;() {
      // ...
    });
  }

  public void onFailure(Throwable caught) {
    try {
      throw caught;
    } catch (RpcTokenException e) {
      // Can be thrown for several reasons:
      //   - duplicate session cookie, which may be a sign of a cookie
      //     overwrite attack
      //   - XSRF token cannot be generated because session cookie isn't
      //     present
    } catch (Throwable e) {
      // unexpected
    }
});
</pre>

<p class="note">
<strong>Tip:</strong> If you would like to register a special handler for exceptions generated during
<code>XsrfToken</code> validation use <a href="/javadoc/latest/com/google/gwt/user/client/rpc/HasRpcToken.html#setRpcTokenExceptionHandler(com.google.gwt.user.client.rpc.RpcTokenExceptionHandler)"><code>HasRpcToken.setRpcTokenExceptionHandler()</code></a>
</p>