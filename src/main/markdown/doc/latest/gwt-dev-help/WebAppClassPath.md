# Web App Classpath Problem

You were directed to this help topic because your server code needed a class or resource that was not found on the *web app classpath*, but **was** found on the *system classpath*. The *system classpath* is the classpath you specify when launching the Java VM to run hosted mode. The *web app classpath* is different — it consists of classes that live in your web application's *war directory*. All server classes and dependencies should be placed in your war directory: libraries (jars) should be placed in `war/WEB-INF/lib/` and classes that don't live in jars should be placed in `war/WEB-INF/classes/`.

GWT hosted mode helpfully works around this problem by mapping these outside resources into your web app classpath. This warning reminds you that failing to address the issue can lead to problems when you actually deploy your web app to a real server.

## Tips

- The most common reason to encounter this problem with a new project is using RPC, which tries to load `com.google.gwt.user.client.rpc.RemoteService`. The solution is to copy `gwt-servlet.jar` from the GWT install directory into your web app's `war/WEB-INF/lib/` directory.

- If you have a good reason for not following the recommended configuration, you can suppress the warning by setting the Java system property `gwt.nowarn.webapp.classpath`. Specify `-Dgwt.nowarn.webapp.classpath` as a JVM argument when launching hosted mode.
