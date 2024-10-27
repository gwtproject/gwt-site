Security RPC XSRF
===

Cross-Site Request Forgery (XSRF or CSRF) is a type of web attack where an
attacker can perform actions on behalf of an authenticated user without user's
knowledge.  Typically, it involves crafting a malicious HTML page, which, once
visited by a victim, will cause the victim's browser to issue an
attacker-controlled request to a third-party domain. If the victim is
authenticated to the third-party domain, the request will be sent with the
browser's cookies for that domain, and could potentially trigger an undesirable
action on behalf of the victim and without victim's consent - for example,
delete or modify a blog or add a mail forward rule.

This document explains how developers can protect GWT RPCs against XSRF
attacking using GWT's builtin XSRF protection introduced in GWT 2.3

1.  [Overview](#Overview)
2.  [Server-side changes](#ServerSide)
3.  [Client-side changes](#ClientSide)

## Overview<a id="Overview"></a>

RPC XSRF protection is built using [`RpcToken`](/javadoc/latest/com/google/gwt/user/client/rpc/RpcToken.html)
feature, which lets a developer set a token on a RPC endpoint using [
`HasRpcToken`](/javadoc/latest/com/google/gwt/user/client/rpc/HasRpcToken.html) interface and have that token included with each
RPC call made via that endpoint.

Default XSRF protection implementation derives XSRF token from a session
authentication cookie by generating an MD5 hash of the session cookie value and
using the resulting hash as XSRF token. This stateless XSRF protection
implementation relies on the fact that attacker doesn't have access to the
session cookie and thus is unable to generate valid XSRF token.

## Server-side changes<a id="ServerSide"></a>

### Configure `XsrfTokenServiceServlet`

Client-side code will obtain XSRF tokens by calling
[`XsrfTokenService.getNewXsrfToken()`](/javadoc/latest/com/google/gwt/user/client/rpc/XsrfTokenService.html) server-side implementation
configured in `web.xml`:

```xml
<servlet>
  <servlet-name>xsrf</servlet-name>
  <servlet-class>
    com.google.gwt.user.server.rpc.XsrfTokenServiceServlet
  </servlet-class>
</servlet>
<servlet-mapping>
  <servlet-name>xsrf</servlet-name>
  <url-pattern>/gwt/xsrf</url-pattern>
</servlet-mapping>
```

Since XSRF token is tied to an authentication session cookie, the name of that
cookie must be passed to the `XsrfTokenServiceServlet` as well as
all XSRF-protected RPC service servlets. This is done via context parameter in
`web.xml`:

```xml
<context-param>
  <param-name>gwt.xsrf.session_cookie_name</param-name>
  <param-value>JSESSIONID</param-value>
</context-param>
```

**Note:** Servlet initialization parameter
(`<init-param>`) can also be used to pass the name of the
session cookie to each servlet individually.

### Make RPC servlets XSRF protected

All server-side implementations of RPC services must extend [`XsrfProtectedServiceServlet`](/javadoc/latest/com/google/gwt/user/server/rpc/XsrfProtectedServiceServlet.html):

```java
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
```

## Client-side changes<a id="ClientSide"></a>

### Make client RPC interfaces XSRF protected

Client-side RPC interfaces can be marked as XSRF protected using one of the following ways:

*   by extending [`XsrfProtectedService`](/javadoc/latest/com/google/gwt/user/client/rpc/XsrfProtectedService.html), in which case all methods calls will
  require `XsrfToken`:

```java
package com.example.foo.client;

  import com.google.gwt.user.client.rpc.XsrfProtectedService;

  public interface MyService extends XsrfProtectedService {
    public String myMethod(String s);
  }
```

*   by explicitly annotating interface or methods with [`@XsrfProtect`](/javadoc/latest/com/google/gwt/user/server/rpc/XsrfProtect.html) annotation. 
[`@NoXsrfProtect`](/javadoc/latest/com/google/gwt/user/server/rpc/NoXsrfProtect.html) annotation can be used to disable XSRF
protection on a method or service to disable XSRF protection:

```java
package com.example.foo.client;

  import com.google.gwt.user.client.rpc.RemoteService;
  import com.google.gwt.user.server.rpc.XsrfProtect

  @XsrfProtect
  public interface MyService extends RemoteService {
    public String myMethod(String s);
  }
```

Method level annotations override RPC interface level annoatations. If no
  annotations are present and the RPC interface contains a method that returns
  [`RpcToken`](/javadoc/latest/com/google/gwt/user/client/rpc/RpcToken.html) or its implementation, then XSRF token validation is
  performed on all methods of that interface except for the method returning
  RpcToken.

**Tip:** To specify which `RpcToken` implementation GWT should generate serializers for use [`@RpcTokenImplementation`](/javadoc/latest/com/google/gwt/user/client/rpc/RpcToken.RpcTokenImplementation.html) annotation.

### Include XsrfToken with RPC calls

To make a call to an XSRF protected service client must obtain a valid
`XsrfToken` and set it on the service endpoint by casting the 
service's asynchronous interface to [`HasRpcToken`](/javadoc/latest/com/google/gwt/user/client/rpc/HasRpcToken.html) and calling `setRpcToken()` method:

```java
XsrfTokenServiceAsync xsrf = (XsrfTokenServiceAsync)GWT.create(XsrfTokenService.class);
((ServiceDefTarget)xsrf).setServiceEntryPoint(GWT.getModuleBaseURL() + "xsrf");
xsrf.getNewXsrfToken(new AsyncCallback<XsrfToken>() {

  public void onSuccess(XsrfToken token) {
    MyServiceAsync rpc = (MyServiceAsync)GWT.create(MyService.class);
    ((HasRpcToken) rpc).setRpcToken(token);

    // make XSRF protected RPC call
    rpc.doStuff(new AsyncCallback<Void>() {
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
```

**Tip:** If you would like to register a special handler for exceptions generated during
`XsrfToken` validation use [`HasRpcToken.setRpcTokenExceptionHandler()`](/javadoc/latest/com/google/gwt/user/client/rpc/HasRpcToken.html#setRpcTokenExceptionHandler-com.google.gwt.user.client.rpc.RpcTokenExceptionHandler-)
