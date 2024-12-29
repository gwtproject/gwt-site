Deploy to GAE
===

At this point, you've created the initial implementation of the StockWatcher application, simulating stock data in the client-side code.

In this section, you'll deploy this application on [Google App Engine](https://cloud.google.com/appengine/docs).  Also, you'll learn about some of the App Engine service APIs and use them to personalize the StockWatcher application so that users can log into their Google Account and retrieve their list of stocks.

1.  [Get started with App Engine](#intro)
2.  [Deploy the application to App Engine](#deploy)
3.  [Personalize the application with the User Service](#user)
4.  [Store data in the datastore](#data)

**Note:** For a broader guide to deploying, see [Deploy a GWT Application](../DevGuideDeploying.html).

This tutorial builds on the GWT concepts and the StockWatcher application created in the [Build a Sample GWT Application](gettingstarted.html) tutorial.  It also uses techniques covered in the [GWT RPC](RPC.html) tutorial.  If you have not completed these tutorials and are familiar with basic GWT concepts, you can import the StockWatcher project as coded to this point, as instructed below.

## Get started with App Engine <a id="intro"></a>

### Sign up for an App Engine account

[Sign up](https://console.cloud.google.com/appengine) for an App Engine account.  After your account is activated, sign in and create an application.  Make a note of the application ID you choose because you will need this information when you configure the StockWatcher project.  After you've finished with this tutorial you will be able to reuse this application ID for other applications.

### Download the App Engine SDK

If you plan to use Eclipse, you can download the App Engine SDK with the [Google Plugin for Eclipse](https://developers.google.com/appengine/docs/java/tools/eclipse). Or [download](https://developers.google.com/appengine/downloads) the App Engine SDK for Java separately.


### Set up a project

#### Set up a project (with Eclipse)

If you initially created your StockWatcher Eclipse project using the Google Plugin for Eclipse with both GWT and Google App Engine enabled, your project is already ready to run on App Engine.  If not:

1.  If you haven't yet, install the [Google Plugin for Eclipse](https://developers.google.com/appengine/docs/java/tools/eclipse) with both GWT and App Engine SDK and restart Eclipse.
2.  Complete the [Build a Sample GWT Application](gettingstarted.html) tutorial, making sure to create a project with both GWT and Google App Engine enabled.  Alternatively, if you would like to skip the Build a Sample GWT Application tutorial, then [download](http://code.google.com/p/google-web-toolkit/downloads/detail?name=Tutorial-GettingStartedAppEngine-2.1.zip), unzip and import the StockWatcher Eclipse project.  To import the project:

    1.  In the File menu, select the Import... menu option.
    2.  Select the import source General > Existing Projects into Workspace.  Click the Next button.
    3.  At "Select root directory", browse to and select the StockWatcher directory (from the unzipped file).  Click the Finish button.
    4.  Add the Google Web Toolkit and App Engine functionality to your newly created project (right-click on your project > Google > Web Toolkit / App Engine Settings...). This will add Google Plugin functionality to your project as well as copy required libraries to your project `WEB-INF/lib` directory automatically.

#### Set up a project (without Eclipse)

1.  If you haven't yet, download the [App Engine SDK](https://cloud.google.com/appengine/downloads) for Java.
2.  Complete the [Build a Sample GWT Application](gettingstarted.html) tutorial,  using webAppCreator to create a GWT application.  Alternatively, If you would like to skip the Build a Sample GWT Application tutorial, then download and unzip [this file](http://code.google.com/p/google-web-toolkit/downloads/detail?name=Tutorial-GettingStarted-2.1.zip).  Edit the gwt.sdk property in the StockWatcher/build.xml, then proceed with the modifications below.
3.  App Engine requires its own web application deployment descriptor.  Create a file StockWatcher/war/WEB-INF/appengine-web.xml with these contents:
    ```xml
    <?xml version="1.0" encoding="utf-8"?>
    <appengine-web-app xmlns="http://appengine.google.com/ns/1.0">
      <application><!-- Your App Engine application ID goes here --></application>
      <version>1</version>
    </appengine-web-app>
    ```

    Substitute your App Engine application ID on the second line. Read more about
[appengine-web.xml](https://cloud.google.com/appengine/docs/java/config/appref).

4.  As we will be using [Java Data Objects (JDO)](https://cloud.google.com/appengine/docs/java/gettingstarted/using-datastore-objectify) later for storing data, create a file StockWatcher/src/META-INF/jdoconfig.xml with these contents:
    ```xml
    <?xml version="1.0" encoding="utf-8"?>
    <jdoconfig xmlns="http://java.sun.com/xml/ns/jdo/jdoconfig"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:noNamespaceSchemaLocation="http://java.sun.com/xml/ns/jdo/jdoconfig">
      <persistence-manager-factory name="transactions-optional">
        <property name="javax.jdo.PersistenceManagerFactoryClass" value="org.datanucleus.store.appengine.jdo.DatastoreJDOPersistenceManagerFactory"/>
        <property name="javax.jdo.option.ConnectionURL" value="appengine"/>
        <property name="javax.jdo.option.NontransactionalRead" value="true"/>
        <property name="javax.jdo.option.NontransactionalWrite" value="true"/>
        <property name="javax.jdo.option.RetainValues" value="true"/>
        <property name="datanucleus.appengine.autoCreateDatastoreTxns" value="true"/>
      </persistence-manager-factory>
    </jdoconfig>
    ```
    You will reference this configuration later by its name "transactions-optional". Read more about
[jdoconfig.xml](https://developers.google.com/appengine/docs/java/datastore/usingjdo).

5.  The GWT ant build file needs to be modified to support DataNucleus JDO compilation and use of the App Engine development server.  Edit StockWatcher/build.xml and add the following:

6.  Add a property for the App Engine SDK directory.
    ```xml
    <!-- Configure path to GWT SDK -->
    <property name="gwt.sdk" location="_Path to GWT_" />
    <!-- Configure path to App Engine SDK -->
    <property name="appengine.sdk" location="_Path to App Engine SDK_" />
    ```
7.  Add a property for a App Engine tools class path.
    ```xml
    <path id="project.class.path">
      <pathelement location="war/WEB-INF/classes"/>
      <pathelement location="${gwt.sdk}/gwt-user.jar"/>
      <fileset dir="${gwt.sdk}" includes="gwt-dev*.jar"/>
        <!-- Add any additional non-server libs (such as JUnit) -->
      <fileset dir="war/WEB-INF/lib" includes="**/*.jar"/>
    </path>

    <path id="tools.class.path">
      <path refid="project.class.path"/>
      <pathelement location="${appengine.sdk}/lib/appengine-tools-api.jar"/>
      <fileset dir="${appengine.sdk}/lib/tools">
        <include name="**/asm-*.jar"/>
        <include name="**/datanucleus-enhancer-*.jar"/>
      </fileset>
    </path>
    ```
8.  Modify the "libs" ant target so that the required jar files are copied to WEB-INF/lib.
    ```xml
    <target name="libs" description="Copy libs to WEB-INF/lib">
      <mkdir dir="war/WEB-INF/lib" />
      <copy todir="war/WEB-INF/lib" file="${gwt.sdk}/gwt-servlet.jar" />
      <!-- Add any additional server libs that need to be copied -->
      <copy todir="war/WEB-INF/lib" flatten="true">
        <fileset dir="${appengine.sdk}/lib/user" includes="**/*.jar"/>
      </copy>
    </target>
    ```
9.  JDO is implemented with DataNucleus Java byte-code enhancement.  Modify the "javac" ant
target to add byte-code enhancement.
    ```xml
    <target name="javac" depends="libs" description="Compile java source">
      <mkdir dir="war/WEB-INF/classes"/>
      <javac srcdir="src" includes="**" encoding="utf-8"
          destdir="war/WEB-INF/classes"
          source="1.5" target="1.5" nowarn="true"
          debug="true" debuglevel="lines,vars,source">
        <classpath refid="project.class.path"/>
      </javac>
      <copy todir="war/WEB-INF/classes">
        <fileset dir="src" excludes="**/*.java"/>
      </copy>
      <taskdef name="datanucleusenhancer"
          classpathref="tools.class.path"
          classname="org.datanucleus.enhancer.tools.EnhancerTask" />
      <datanucleusenhancer classpathref="tools.class.path"
          failonerror="true">
        <fileset dir="war/WEB-INF/classes" includes="**/*.class" />
      </datanucleusenhancer>
    </target>
    ```
10.  Modify the "devmode" ant target to use the App Engine development server instead of the
servlet container which comes with GWT.
        ```xml
        <target name="devmode" depends="javac" description="Run development mode"">
          <java failonerror="true" fork="true" classname="com.google.gwt.dev.DevMode"">
            <classpath>
              <pathelement location="src"/>
              <path refid="project.class.path"/>
              <path refid="tools.class.path"/>
            </classpath>
            <jvmarg value="-Xmx256M"/>
            <arg value="-startupUrl"/>
            <arg value="StockWatcher.html"/>
            <!-- Additional arguments like -style PRETTY or -logLevel DEBUG -->
            <arg value="-server"/>
            <arg value="com.google.appengine.tools.development.gwt.AppEngineLauncher"/>
            <arg value="com.google.gwt.sample.stockwatcher.StockWatcher"/>
          </java>
        </target>
        ```
### Test locally

We will run the application in GWT development mode to verify the project was set up successfully.  However, instead of using the servlet container which comes with GWT, the application will run in the App Engine development server, the servlet container which comes with the App Engine SDK.  What's the difference?  The App Engine development server is configured to mimic the App Engine production environment.

#### Run the application in development mode (with Eclipse)

1.  In the Package Explorer view, select the StockWatcher project.
2.  In the toolbar, click the Run button (Run as Web Application).

#### Run the application in development mode (without Eclipse)

1.  From the command-line, change to the StockWatcher directory.
2.  Execute:

```shell
ant devmode
```

## Deploy the application to App Engine <a id="deploy"></a>

Now that we've verified the StockWatcher project is running locally in GWT development mode and with the App Engine development server, we can run the application on App Engine.

#### Deploy the application to App Engine (with Eclipse)

1.  In the Package Explorer view, select the StockWatcher project.
2.  In the toolbar, click the Deploy App Engine Project button ![icon](images/DeployAppEngineProject.png).
3.  (First time only) Click the "App Engine project settings..." link to specify your application ID.  Click the OK button when you're finished.
4.  Enter your Google Accounts email and password.  Click the Deploy button.  You can watch the deployment progress in the Eclipse Console.

#### Deploy the application to App Engine (without Eclipse)

1.  From the command-line, change to the StockWatcher directory.
2.  Compile the application by executing:

```shell
ant build
```

**Tip:** Add the ant bin directory to your environment PATH to avoid having to specify the full path to ant.

3.  appcfg is a command-line tool which comes with the App Engine SDKs.  Upload the application by executing:

```shell
appcfg.sh update war
```

From the Windows command prompt, the command is `appcfg update war`.  The first parameter is the action to perform.  The second parameter is the directory with the update, which in this case is a relative directory containing the static files and output from the GWT compiler.  Enter your Google Accounts email and password when prompted.

**Tip:** Add the App Engine SDK bin directory to your environment PATH to avoid having to specify the full path to appcfg.sh.

### Test on App Engine

Test your uploaded application by opening a web browser to http://_application-id_.appspot.com/ where _application-id_ is the App Engine application ID that you created earlier.  The StockWatcher application is now running on App Engine under your application ID.

## Personalize the application with the User Service <a id="user"></a>

### Overview

Now that the StockWatcher is deployed on App Engine, we can start using some of the available services to enrich the application. We'll start by persisting stock quote listings on a per user basis. This is possible due to the datastore service, which allows us to save application data, as well as the User Service, which allows us to have users login and save stock quote listings for each user. For persistence, we'll use the Java Data Objects (JDO) interface provided by the App Engine SDK.

To implement login functionality we'll use the User Service. With this service in place, any user with a Google Account will be able to login using their account to access the StockWatcher application. In this section, you'll use the App Engine User API to add user login to the application.

The App Engine User Service is very easy to use. First, you need to instantiate the UserService class, as shown in the code snippet below:

```java
UserService userService = UserServiceFactory.getUserService();

```

Next, you need to get the current user who is accessing the StockWatcher application:

```java
User user = userService.getCurrentUser();

```

The UserService returns an instantiated User object if the current user who is accessing the application is logged into their Google Account. The User object contains useful information such as the email address associated with the account, as well as the account nickname. If the person accessing the application is not logged into their account, or doesn't have a Google Account, the returned User object will be null. In this case we have a number of options available to us in how we want to handle the situation, but for the purposes of the StockWatcher application, we will point the user to a login URL where they will be able to log into their Google Account.

The User API offers an easy way to generate the login URL. Simply calling the UserService `createLoginURL(String requestUri)` method, which gives you the redirect login URL to send the user to the Google Account login screen. Once they log in, the App Engine container will know where to redirect the user based on the `requestUri` that you provide when making the `createLoginURL()` call.

### Define the Login RPC service

To make this more concrete, let's create a login RPC service for the StockWatcher application. If you're not familiar with GWT RPC, see the previous [tutorial](RPC.html).

First, create the LoginInfo object which will contain the login info from the User service.

#### LoginInfo.java:

```java
package com.google.gwt.sample.stockwatcher.client;

import java.io.Serializable;

public class LoginInfo implements Serializable {

  private boolean loggedIn = false;
  private String loginUrl;
  private String logoutUrl;
  private String emailAddress;
  private String nickname;

  public boolean isLoggedIn() {
    return loggedIn;
  }

  public void setLoggedIn(boolean loggedIn) {
    this.loggedIn = loggedIn;
  }

  public String getLoginUrl() {
    return loginUrl;
  }

  public void setLoginUrl(String loginUrl) {
    this.loginUrl = loginUrl;
  }

  public String getLogoutUrl() {
    return logoutUrl;
  }

  public void setLogoutUrl(String logoutUrl) {
    this.logoutUrl = logoutUrl;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }
}

```

LoginInfo is serializable since it is the return type of an RPC method.

Next, create the LoginService and LoginServiceAsync interfaces.

#### LoginService.java:

```java
package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("login")
public interface LoginService extends RemoteService {
  public LoginInfo login(String requestUri);
}

```

The path annotation "login" will be configured below.

#### LoginServiceAsync.java:

```java
package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LoginServiceAsync {
  public void login(String requestUri, AsyncCallback<LoginInfo> async);
}

```

Create the LoginServiceImpl class in the com.google.gwt.sample.stockwatcher.server package as follows:

#### LoginServiceImpl.java:

```java
package com.google.gwt.sample.stockwatcher.server;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.sample.stockwatcher.client.LoginInfo;
import com.google.gwt.sample.stockwatcher.client.LoginService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class LoginServiceImpl extends RemoteServiceServlet implements
    LoginService {

  public LoginInfo login(String requestUri) {
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    LoginInfo loginInfo = new LoginInfo();

    if (user != null) {
      loginInfo.setLoggedIn(true);
      loginInfo.setEmailAddress(user.getEmail());
      loginInfo.setNickname(user.getNickname());
      loginInfo.setLogoutUrl(userService.createLogoutURL(requestUri));
    } else {
      loginInfo.setLoggedIn(false);
      loginInfo.setLoginUrl(userService.createLoginURL(requestUri));
    }
    return loginInfo;
  }

}

```

Lastly, configure the servlet in your web.xml file.  The mapping is composed of the rename-to attribute in the GWT module definition (stockwatcher) and the RemoteServiceRelativePath annotation(login).  Also, because the greetServlet is not needed for this application, its configuration can be deleted.

#### web.xml:

```xml
<?xml version="1.0" encoding="UTF-8"?>

<web-app>

  <!-- Default page to serve -->
  <welcome-file-list>
    <welcome-file>StockWatcher.html</welcome-file>
  </welcome-file-list>

  <!-- Servlets -->
  <servlet>
    <servlet-name>loginService</servlet-name>
    <servlet-class>com.google.gwt.sample.stockwatcher.server.LoginServiceImpl</servlet-class>
  </servlet>

  <servlet-mapping>
    <servlet-name>loginService</servlet-name>
    <url-pattern>/stockwatcher/login</url-pattern>
  </servlet-mapping>

</web-app>
```

### Update the StockWatcher UI

Now that the login RPC service is in place, the last thing to do is to make the call to the service from the StockWatcher entry point class. However, we must consider how the application flow changes now that we've added login functionality. In the previous version of the application, you could load the StockWatcher unconditionally because it didn't require any login. Now that we are requiring user login, we have to change the loading logic a bit.

For one, if the user is already logged in, the application can proceed and load the StockWatcher. If, however, the user is not logged in, we have to redirect them to the login page. Once logged in, they will be redirected back to the StockWatcher host page where we'll need to check once more that they have indeed been authenticated. If the authentication check passes, then we can load the stock watcher.

The key thing to notice is that loading the stock watcher is contingent on the result of the login. This means the logic that loads the StockWatcher must be called only once login has passed. This will require a bit of refactoring. If you're using Eclipse, this will be easy to do. Simply select the code in the StockWatcher `onModuleLoad()` method, select the "Refactor" menu, and click on the "Extract Method..." function. From there you can declare the extracted method something suitable, like `private void loadStockWatcher()`.

You should end up with something similar to the following:

#### StockWatcher.java:

```java
public void onModuleLoad() {
    loadStockWatcher();
  }

  private void loadStockWatcher() {
    // Create table for stock data.
    stocksFlexTable.setText(0, 0, "Symbol");
    stocksFlexTable.setText(0, 1, "Price");
    stocksFlexTable.setText(0, 2, "Change");
    stocksFlexTable.setText(0, 3, "Remove");
    ...
  }
```

Now that you've refactored the StockWatcher loading logic to a callable method, we can make the login RPC service call in the `onModuleLoad()` method and call the `loadStockWatcher()` method when login passes. However, if the user isn't logged in, you'll need to give them some kind of indication that they need to log in to proceed. For this, it makes sense to use a Login panel along with accompanying label and button to instruct the user to proceed to login.

Considering all of these, you should add something similar to the following to your StockWatcher entry point class:

#### StockWatcher.java

```java
import com.google.gwt.user.client.ui.Anchor;

...

  private LoginInfo loginInfo = null;
  private VerticalPanel loginPanel = new VerticalPanel();
  private Label loginLabel = new Label(
      "Please sign in to your Google Account to access the StockWatcher application.");
  private Anchor signInLink = new Anchor("Sign In");

  public void onModuleLoad() {
    // Check login status using login service.
    LoginServiceAsync loginService = GWT.create(LoginService.class);
    loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
      public void onFailure(Throwable error) {
      }

      public void onSuccess(LoginInfo result) {
        loginInfo = result;
        if(loginInfo.isLoggedIn()) {
          loadStockWatcher();
        } else {
          loadLogin();
        }
      }
    });
  }

  private void loadLogin() {
    // Assemble login panel.
    signInLink.setHref(loginInfo.getLoginUrl());
    loginPanel.add(loginLabel);
    loginPanel.add(signInLink);
    RootPanel.get("stockList").add(loginPanel);
  }
```

Another important point about login functionality is the ability to sign out of the application. This is something you should add to the StockWatcher application as well. Fortunately, the User Service provides us with a logout URL through a similar call as the createLoginURL(String requestUri) method. We can add this to the StockWatcher sample application by adding the following snippets:

#### StockWatcher.java

```java
private Anchor signInLink = new Anchor("Sign In");
  private Anchor signOutLink = new Anchor("Sign Out");

...

  private void loadStockWatcher() {
    // Set up sign out hyperlink.
    signOutLink.setHref(loginInfo.getLogoutUrl());

    // Create table for stock data.
    stocksFlexTable.setText(0, 0, "Symbol");
    stocksFlexTable.setText(0, 1, "Price");
    stocksFlexTable.setText(0, 2, "Change");
    stocksFlexTable.setText(0, 3, "Remove");

  ...

    // Assemble Main panel.
    mainPanel.add(signOutLink);
    mainPanel.add(stocksFlexTable);
    mainPanel.add(addPanel);
    mainPanel.add(lastUpdatedLabel);
```

### Test User Service features

You can repeat the instructions above to run the application [locally](#test) or on [App Engine](#deploy).

If you run the application in development mode with the App Engine development server, the sign in page will allow you to enter any email address (for ease in testing).  If you deploy your application to App Engine, the sign in page will require users to sign in to a Google Account in order to access the application.

## Store data in the datastore<a id="data"></a>

### Overview

The datastore service available to the App Engine Java runtime is the same service available to the Python runtime.  To access this service in Java, you may use the low-level [datastore API](https://developers.google.com/appengine/docs/java/javadoc/com/google/appengine/api/datastore/package-summary), [Java Data Objects (JDO)](https://developers.google.com/appengine/docs/java/datastore/usingjdo), or [Java Persistence API (JPA)](https://developers.google.com/appengine/docs/java/datastore/usingjpa).  For this sample we will use JDO.

### Define the Stock RPC service

We will create a basic stock service to handle the persistence of users' stocks.  We will also expose this service as a GWT RPC service.

#### StockService.java

```java
package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("stock")
public interface StockService extends RemoteService {
  public void addStock(String symbol) throws NotLoggedInException;
  public void removeStock(String symbol) throws NotLoggedInException;
  public String[] getStocks() throws NotLoggedInException;
}

```

#### StockServiceAsync.java

```java
package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface StockServiceAsync {
  public void addStock(String symbol, AsyncCallback<Void> async);
  public void removeStock(String symbol, AsyncCallback<Void> async);
  public void getStocks(AsyncCallback<String[]> async);
}

```

#### StockWatcher.java

```java
public class StockWatcher implements EntryPoint {

  private static final int REFRESH_INTERVAL = 5000; // ms
  private VerticalPanel mainPanel = new VerticalPanel();
  private FlexTable stocksFlexTable = new FlexTable();
  private HorizontalPanel addPanel = new HorizontalPanel();
  private TextBox newSymbolTextBox = new TextBox();
  private Button addStockButton = new Button("Add");
  private Label lastUpdatedLabel = new Label();
  private ArrayList<String> stocks = new ArrayList<String>();
    private LoginInfo loginInfo = null;
    private VerticalPanel loginPanel = new VerticalPanel();
    private Label loginLabel = new Label("Please sign in to your Google Account to access the StockWatcher application.");
    private Anchor signInLink = new Anchor("Sign In");
    private final StockServiceAsync stockService = GWT.create(StockService.class);
```

A checked exception will indicate that the user is not logged in yet.  Such a scenario is possible since a RPC call can be received by the stock service even if there is no current user.  The class is serializable so that it may be returned by the RPC call via the AsyncCallback `onFailure(Throwable error)` method.  You could also implement security with a servlet filter or Spring security.

#### NotLoggedInException.java

```java
package com.google.gwt.sample.stockwatcher.client;

import java.io.Serializable;

public class NotLoggedInException extends Exception implements Serializable {

  public NotLoggedInException() {
    super();
  }

  public NotLoggedInException(String message) {
    super(message);
  }

}

```

The Stock class is what is persisted with JDO.  The specifics of how it is persisted are dictated by the JDO annotations.  In particular:

*   The PersistenceCapable annotation tells the DataNucleus byte-code enhancer to process this class.
*   The PrimaryKey annotation designates an `id` attribute for storing its primary key.
*   In this class, every attribute is persisted.  However you can designate attributes as not being persisted with the NotPersistent annotation.
*   The User attribute is a special App Engine type which can allow you to identify users across
email address changes.

#### Stock.java

```java
package com.google.gwt.sample.stockwatcher.server;

import java.util.Date;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.google.appengine.api.users.User;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Stock {

  @PrimaryKey
  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
  private Long id;
  @Persistent
  private User user;
  @Persistent
  private String symbol;
  @Persistent
  private Date createDate;

  public Stock() {
    this.createDate = new Date();
  }

  public Stock(User user, String symbol) {
    this();
    this.user = user;
    this.symbol = symbol;
  }

  public Long getId() {
    return this.id;
  }

  public User getUser() {
    return this.user;
  }

  public String getSymbol() {
    return this.symbol;
  }

  public Date getCreateDate() {
    return this.createDate;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }
}
```

This class implements the stock service and includes calls to the JDO API for persisting stock data.  Things to notice:

*   The messages logged by the logger are viewable when you inspect your application in the [App Engine Administration Console](https://appengine.google.com/).</ol>
*   The PersistenceManagerFactory singleton is created from the properties named
"transactions-optional" in jdoconfig.xml above.
*   The checkedLoggedIn method is called whenever we want to make sure a user is logged in.
*   The getUser method uses the UserService.

#### StockServiceImpl.java

```java
package com.google.gwt.sample.stockwatcher.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.sample.stockwatcher.client.NotLoggedInException;
import com.google.gwt.sample.stockwatcher.client.StockService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class StockServiceImpl extends RemoteServiceServlet implements
StockService {

  private static final Logger LOG = Logger.getLogger(StockServiceImpl.class.getName());
  private static final PersistenceManagerFactory PMF =
      JDOHelper.getPersistenceManagerFactory("transactions-optional");

  public void addStock(String symbol) throws NotLoggedInException {
    checkLoggedIn();
    PersistenceManager pm = getPersistenceManager();
    try {
      pm.makePersistent(new Stock(getUser(), symbol));
    } finally {
      pm.close();
    }
  }

  public void removeStock(String symbol) throws NotLoggedInException {
    checkLoggedIn();
    PersistenceManager pm = getPersistenceManager();
    try {
      long deleteCount = 0;
      Query q = pm.newQuery(Stock.class, "user == u");
      q.declareParameters("com.google.appengine.api.users.User u");
      List<Stock> stocks = (List<Stock>) q.execute(getUser());
      for (Stock stock : stocks) {
        if (symbol.equals(stock.getSymbol())) {
          deleteCount++;
          pm.deletePersistent(stock);
        }
      }
      if (deleteCount != 1) {
        LOG.log(Level.WARNING, "removeStock deleted "+deleteCount+" Stocks");
      }
    } finally {
      pm.close();
    }
  }

  public String[] getStocks() throws NotLoggedInException {
    checkLoggedIn();
    PersistenceManager pm = getPersistenceManager();
    List<String> symbols = new ArrayList<String>();
    try {
      Query q = pm.newQuery(Stock.class, "user == u");
      q.declareParameters("com.google.appengine.api.users.User u");
      q.setOrdering("createDate");
      List<Stock> stocks = (List<Stock>) q.execute(getUser());
      for (Stock stock : stocks) {
        symbols.add(stock.getSymbol());
      }
    } finally {
      pm.close();
    }
    return (String[]) symbols.toArray(new String[0]);
  }

  private void checkLoggedIn() throws NotLoggedInException {
    if (getUser() == null) {
      throw new NotLoggedInException("Not logged in.");
    }
  }

  private User getUser() {
    UserService userService = UserServiceFactory.getUserService();
    return userService.getCurrentUser();
  }

  private PersistenceManager getPersistenceManager() {
    return PMF.getPersistenceManager();
  }
}

```

Now that the GWT RPC service is implemented, we'll make sure the servlet container knows about it.  The mapping /stockwatcher/stock is composed of the rename-to attribute in the GWT module definition (stockwatcher) and the RemoteServiceRelativePath annotation (stock).

#### web.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>

<web-app>

  <!-- Default page to serve -->
  <welcome-file-list>
    <welcome-file>StockWatcher.html</welcome-file>
  </welcome-file-list>

  <!-- Servlets -->
  <servlet>
    <servlet-name>loginService</servlet-name>
    <servlet-class>com.google.gwt.sample.stockwatcher.server.LoginServiceImpl</servlet-class>
  </servlet>

  <servlet>
    <servlet-name>stockService</servlet-name>
    <servlet-class>com.google.gwt.sample.stockwatcher.server.StockServiceImpl</servlet-class>
  </servlet>

  <servlet-mapping>
    <servlet-name>loginService</servlet-name>
    <url-pattern>/stockwatcher/login</url-pattern>
  </servlet-mapping>

  <servlet-mapping>
    <servlet-name>stockService</servlet-name>
    <url-pattern>/stockwatcher/stock</url-pattern>
  </servlet-mapping>

</web-app>
```

### Update the StockWatcher UI

#### Retrieving stocks

When the StockWatcher application loads, it should be prepopulated with the user's stocks.  In order to reuse the existing code which displays a stock, we will refactor StockWatcher `addStock()` so that the logic for displaying the new stock is in a new `displayStock(String symbol)` method.

```java
private void addStock() {
    final String symbol = newSymbolTextBox.getText().toUpperCase().trim();
    newSymbolTextBox.setFocus(true);

    // Stock code must be between 1 and 10 chars that are numbers, letters, or dots.
    if (!symbol.matches("^[0-9a-zA-Z\\.]{1,10}$")) {
      Window.alert("'" + symbol + "' is not a valid symbol.");
      newSymbolTextBox.selectAll();
      return;
    }

    newSymbolTextBox.setText("");

    // Don't add the stock if it's already in the table.
    if (stocks.contains(symbol))
      return;

    displayStock(symbol);
  }

  private void displayStock(final String symbol) {
    // Add the stock to the table.
    int row = stocksFlexTable.getRowCount();
    stocks.add(symbol);
    stocksFlexTable.setText(row, 0, symbol);
    stocksFlexTable.setWidget(row, 2, new Label());
    stocksFlexTable.getCellFormatter().addStyleName(row, 1, "watchListNumericColumn");
    stocksFlexTable.getCellFormatter().addStyleName(row, 2, "watchListNumericColumn");
    stocksFlexTable.getCellFormatter().addStyleName(row, 3, "watchListRemoveColumn");

    // Add a button to remove this stock from the table.
    Button removeStockButton = new Button("x");
    removeStockButton.addStyleDependentName("remove");
    removeStockButton.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        int removedIndex = stocks.indexOf(symbol);
        stocks.remove(removedIndex);
        stocksFlexTable.removeRow(removedIndex + 1);
      }
    });
    stocksFlexTable.setWidget(row, 3, removeStockButton);

    // Get the stock price.
    refreshWatchList();

  }
```

After the stock table is set up is an appropriate time to load the stocks.

```java
private void loadStockWatcher() {

  ...

    stocksFlexTable.setCellPadding(5);
    stocksFlexTable.addStyleName("watchList");
    stocksFlexTable.getRowFormatter().addStyleName(0, "watchListHeader");
    stocksFlexTable.getCellFormatter().addStyleName(0, 1, "watchListNumericColumn");
    stocksFlexTable.getCellFormatter().addStyleName(0, 2, "watchListNumericColumn");
    stocksFlexTable.getCellFormatter().addStyleName(0, 3, "watchListRemoveColumn");

    loadStocks();

  ...

  }
```

The `loadStocks()` method calls the StockService defined earlier.  The RPC returns an array of stock symbols, which are displayed individually using the method `displayStock(String symbol)`.

```java
private void loadStocks() {
    stockService.getStocks(new AsyncCallback<String[]>() {
      public void onFailure(Throwable error) {
      }
      public void onSuccess(String[] symbols) {
        displayStocks(symbols);
      }
    });
  }

  private void displayStocks(String[] symbols) {
    for (String symbol : symbols) {
      displayStock(symbol);
    }
  }
```

#### Adding stocks

Instead of just displaying stocks when they are added, we will call the StockService to save the new stock symbol to the datastore.

```java
private void addStock() {
    final String symbol = newSymbolTextBox.getText().toUpperCase().trim();
    newSymbolTextBox.setFocus(true);

    // Stock code must be between 1 and 10 chars that are numbers, letters, or dots.
    if (!symbol.matches("^[0-9a-zA-Z\\.]{1,10}$")) {
      Window.alert("'" + symbol + "' is not a valid symbol.");
      newSymbolTextBox.selectAll();
      return;
    }

    newSymbolTextBox.setText("");

    // Don't add the stock if it's already in the table.
    if (stocks.contains(symbol))
      return;

    addStock(symbol);
  }

  private void addStock(final String symbol) {
    stockService.addStock(symbol, new AsyncCallback<Void>() {
      public void onFailure(Throwable error) {
      }
      public void onSuccess(Void ignore) {
        displayStock(symbol);
      }
    });
  }

  private void displayStock(final String symbol) {
    // Add the stock to the table.
    int row = stocksFlexTable.getRowCount();
    stocks.add(symbol);

...

  }
```

#### Removing stocks

And instead of simply removing stocks from display, we will call the StockService to remove the stock symbol from the datastore.

```java
private void displayStock(final String symbol) {

  ...

    // Add a button to remove this stock from the table.
    Button removeStock = new Button("x");
    removeStock.addStyleDependentName("remove");

    removeStock.addClickHandler(new ClickHandler(){
      public void onClick(ClickEvent event) {
        removeStock(symbol);
      }
    });
    stocksFlexTable.setWidget(row, 3, removeStock);

    // Get the stock price.
    refreshWatchList();

  }

  private void removeStock(final String symbol) {
    stockService.removeStock(symbol, new AsyncCallback<Void>() {
      public void onFailure(Throwable error) {
      }
      public void onSuccess(Void ignore) {
        undisplayStock(symbol);
      }
    });
  }

  private void undisplayStock(String symbol) {
    int removedIndex = stocks.indexOf(symbol);
    stocks.remove(removedIndex);
    stocksFlexTable.removeRow(removedIndex+1);
  }
```

### Error handling

When one of the RPC calls results in an error, we want to display the message to the user.

Furthermore, recall that the StockService throws a NotLoggedInException if for some reason the user is no longer logged in to his Google Account:

```java
private void checkLoggedIn() throws NotLoggedInException {
    if (getUser() == null) {
      throw new NotLoggedInException("Not logged in.");
    }
  }

```

If we receive this error, we will redirect the user to the logout URL.

Here's a helper method to accomplish these two error handling requirements.

```java
private void handleError(Throwable error) {
    Window.alert(error.getMessage());
    if (error instanceof NotLoggedInException) {
      Window.Location.replace(loginInfo.getLogoutUrl());
    }
  }
```

We can add this to each AsyncCallback `onFailure(Throwable error)` method.

```java
loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
      public void onFailure(Throwable error) {
        handleError(error);
      }

    ...

    }
```



```java
stockService.getStocks(new AsyncCallback<String[]>() {
      public void onFailure(Throwable error) {
        handleError(error);
      }

    ...

    });
```



```java
stockService.addStock(symbol, new AsyncCallback<Void>() {
      public void onFailure(Throwable error) {
        handleError(error);
      }

    ...

    });
```



```java
stockService.removeStock(symbol, new AsyncCallback<Void>() {
      public void onFailure(Throwable error) {
        handleError(error);
      }

    ...

    });
```

### Test Datastore features

You can repeat the instructions above to run the application [locally](#test) or on [App Engine](#deploy).

If you encounter runtime errors, examine the logs in the [App Engine Administration Console](https://console.cloud.google.com/appengine).

## More resources

### Further exercises

Users can now sign in to Google Account and manage their own stock lists in the StockWatcher application running on App Engine.  Here are some suggested enhancements you can try as exercises:

*   Loading the stock list has a noticeable delay.  Add a UI element to indicate that the stock list is loading.
*   Add more attributes to the Stock class.  What happens to the data which was saved before these attributes were added?
*   The StockService does not detect when one user has signed out and another user has signed in.  How would you modify the application to handle this edge case?

### Learn more about App Engine

The App Engine [Java Getting Started tutorial](https://cloud.google.com/appengine/docs/java/gettingstarted/creating-guestbook) gives more details on building an App Engine application including topics such as creating a project from scratch, using JSPs, managing different application versions, and more details on the web application descriptor files.

The App Engine [Java documentation](https://cloud.google.com/appengine/docs/java/) covers the User service and datastore service in greater detail.  In particular, it documents how to use JPA to access the datastore service.  Other services documented include Memcache, HTTP client, and, Java Mail.  The limitations of the App Engine Java runtime are also itemized.
