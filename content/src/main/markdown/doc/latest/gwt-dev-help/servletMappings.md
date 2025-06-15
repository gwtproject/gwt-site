# Servlet Mappings, GWT Modules, and web.xml

GWT modules may declare one or more `<servlet>` tags. These define Java Servlets that implement the server-side component of a GWT-enabled web application.

In modern GWT applications, these are only used for testing, and it is discouraged to use them in application code.

During hosted mode startup, the set of *expected* servlets (from GWT module `<servlet>` tags) is validated against the set of actual servlets (from the `WEB-INF/web.xml`) and a warning is issued for each *expected* servlet which does not match an actual servlet.
