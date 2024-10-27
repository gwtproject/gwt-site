GWT & Hibernate
===

_Sumit Chandel, Google Developer Relations_

_July 2009_

_(with thanks to Bruno Marchesson for his contributions to this article)_

Many developers have asked how to use [GWT](https://www.gwtproject.org) and [Hibernate](https://www.hibernate.org/) together. Although you can find numerous discussions about this topic on the [GWT Developer Forum](http://groups.google.com/group/Google-Web-Toolkit), we thought it would be beneficial to sum up some of the most popular strategies, and highlight their advantages and shortcomings in the context of GWT application development.

Before we get into integration strategies, let's get familiar with the basics.

## The Basics <a id="TheBasics"></a>

For the purposes of this article, we're going to assume that the reader is already familiar with Hibernate configuration and usage on the server-side. If you'd like to know more about Hibernate and how it works, the getting started tutorial posted on the Hibernate homepage is strongly recommended.

Also, to get up and running with persistence for our Hibernate objects, we'll be using [HSQLDB](http://hsqldb.org/) which provides an in-memory Hibernate SQL database. You can download this [here](http://sourceforge.net/project/showfiles.php?group_id=23316). Again, we won't talk too much about how HSQLDB works, but we will cover just enough to get it running for the examples we'll see here.

Let's start out with a simple example. Suppose we're developing an online music record store. An obvious domain object in such a application would be a `Record` object that we would want to persist on the server-side and display on the client-side. We would also want users to be able to create accounts and add music records to their profiles online. It would also make sense for us to create an `Account` object that we'll want to persist. So let's create these two classes.

**Record.java**

```java
public class Record implements Serializable {
  private Long id;
  private String title;
  private int year;
  private double price;

  public Record() {
  }

  public Record(Long id) {
    this.id = id;
  }

  // Along with corresponding getters + setters.
}

```

**Account.java**

```java
public class Account implements Serializable {
  Long id;
  String name;
  String password;
  Set<Record> records;

  public Account() {
  }

  public Account(Long id) {
    this.id = id;
  }

  public void addRecord(Record record) {
    if (records == null) {
      records = new HashSet<Record>();
    }
    records.add(record);
  }

  public void removeRecord(Record record) {
    if (records == null) {
      return;
    }
    records.remove(record);
  }

  // Along with corresponding getters + setters.
}

```

Then we need to create the corresponding Hibernate mapping files for each of these persisted types, like so:

**Record.hbm.xml**

```xml
<hibernate-mapping>
  <class name="com.google.musicstore.domain.Record" table="RECORD">
    <id name="id" column="RECORD_ID">
      <generator class="native"/>
    </id>
    <property name="title"/>
    <property name="year"/>
    <property name="price"/>

  </class>
</hibernate-mapping>

```

**Account.hbm.xml**

```xml
<hibernate-mapping>
  <class name="com.google.musicstore.domain.Account" table="ACCOUNT">
    <id name="id" column="ACCOUNT_ID">
      <generator class="native"/>
    </id>
    <property name="name"/>
    <property name="password"/>

    <set name="records" table="ACCOUNT_RECORD" lazy="true">
      <key column="ACCOUNT_ID"/>
      <many-to-many column="RECORD_ID" class="com.google.musicstore.domain.Record"/>
    </set>
  </class>
</hibernate-mapping>

```

Now that we've created our persistent classes, let's create a bare bones UI that will allow us to enter new accounts and records, as well as the GWT RPC services that will persist them on the server-side. Let's start with the RPC services.

We won't go into the specifics of the role each RPC component plays here, but if you're unfamiliar with the GWT RPC subsystem, check out the [GWT RPC docs](../doc/latest/DevGuideServerCommunication.html#DevGuideRemoteProcedureCalls) to get up to speed.

First, we create the client-side service interfaces. If you'd like to avoid the large number of interface methods listed below, consider using the Command pattern, as described [here](http://www.youtube.com/watch?v=PDuhR18-EdM):

**MusicStoreService.java**

```java
@RemoteServiceRelativePath("musicservice")
public interface MusicStoreService extends RemoteService {
  public List<Account> getAccounts();

  public List<Record> getRecords();

  public Long saveAccount(Account account);

  public Long saveRecord(Record record);

  public void saveRecordToAccount(Account account, Record record);
}

```

**MusicStoreServiceAsync.java**

```java
public interface MusicStoreServiceAsync {
  public void getAccounts(AsyncCallback<List<Account>> callback);

  public void getRecords(AsyncCallback<List<Record>> callback);

  public void saveAccount(Account accountDTO, AsyncCallback<Long> callback);

  public void saveRecord(Record record, AsyncCallback<Long> callback);

  public void saveRecordToAccount(Account accountDTO, Record recordDTO,
AsyncCallback<Void> callback);
}

```

Finally, we create the service implementation class on the server-side.

**MusicStoreServiceImpl.java**

```java
public class MusicStoreServiceImpl extends RemoteServiceServlet implements
MusicStoreService {

  @Override
  public List<Account> getAccounts() {
    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    session.beginTransaction();
    List<Account> accounts = new ArrayList<Account>(session.createQuery("from Account").list());
    session.getTransaction().commit();
    return accounts;
  }

  @Override
  public List<Record> getRecords() {
    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    session.beginTransaction();
    List<Record> records = new ArrayList<Record>(session.createQuery("from Record").list());
    session.getTransaction().commit();
    return records;
  }

  @Override
  public Long saveAccount(Account account) {
    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    session.beginTransaction();
    session.save(account);
    session.getTransaction().commit();
    return account.getId();
  }

  @Override
  public Long saveRecord(Record record) {
    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    session.beginTransaction();
    session.save(record);
    session.getTransaction().commit();
    return record.getId();
  }

  @Override
  public void saveRecordToAccount(Account account, Record record) {
    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    session.beginTransaction();
    account = (Account) session.load(Account.class, account.getId());
    record = (Record) session.load(Record.class, record.getId());
    account.addRecord(record);
    session.save(account);
    session.getTransaction().commit();
  }
}

```

You may have noticed some `HibernateUtil` calls in the `MusicStoreServiceImpl`  method implementations in the code snippet above. This is actually a custom class that was created as a helper utility to retrieve and use the Hibernate session factory, exactly as is done in the Hibernate tutorial mentioned earlier. For convenience, here is the `HibernateUtil` code pasted below so you can follow along. If you want to learn more details about what the `HibernateUtil` class is doing, I strongly advise checking out the tutorial for a full explanation.

```java
public class HibernateUtil {

  private static final SessionFactory sessionFactory;

  static {
    try {
      // Create the SessionFactory from hibernate.cfg.xml
      sessionFactory = new Configuration().configure().buildSessionFactory();
    } catch (Throwable ex) {
      // Make sure you log the exception, as it might be swallowed
      System.err.println("Initial SessionFactory creation failed." + ex);
      throw new ExceptionInInitializerError(ex);
    }
  }

  public static SessionFactory getSessionFactory() {
    return sessionFactory;
  }
}

```

Finally, our server-side GWT RPC services are ready to [CRUD](http://en.wikipedia.org/wiki/Create,_read,_update_and_delete) our Hibernate objects (actually, we skipped the Delete functionality).  Now we just need an interface to actually make the RPC calls. I've created a sample application with a UI that will allow us to add records, add accounts, add records to accounts and of course view all existing accounts and their associated records. The sample code is not representative of best practices, just a quick and dirty implementation to get us up and running. The sample also includes the server-side RPC and Hibernate code we've worked on up to this point. You can download the sample [here](http://google-web-toolkit.googlecode.com/files/gwt_hibernate_base.zip).

In the example source code, you'll find a `build.xml` file and a `build.properties` file in the root directory. After properly configuring the `gwt.home` and `gwt.dev.jar` properties for your machine,  you can use [Ant](http://ant.apache.org/) to build the project, as well as start up hosted mode to see the UI and our Hibernate instance setup in the embedded Jetty server. Just run the following from command line:

```shell
ant build hosted
```

Before doing that, though, we'll need to have our in-memory HSQLDB up and running so we can persist our Hibernate objects. In the example project you downloaded, you should find '`data`' folder under the project root directory. You'll also find the `hsqldb.jar` in the `lib` folder. All we need to do to start up the in-memory HSQLDB is invoke the `org.hsqldb.Server` class contained in the `hsqldb.jar` file, while in the '`data`' directory to host the HSQLDB properties and log output. You can do this by running the following from command line (while in the '`data`' directory):

```shell
java -cp ../lib/hsqldb.jar org.hsqldb.Server
```

Now that we have our persistence layer ready, let's compile and run our application in hosted mode using the ant command above. Once both `build` and `hosted` ant tasks have completed, you should see the hosted mode browser startup, with the "Add Accounts / Records" tab displayed. Finally, we can start persisting our records (from the GWT client-side to the Hibernate database, using our Hibernate objects!). Go ahead and try adding an account and a a record to our in-memory Hibernate to get our data set started.

Next, try selecting the "Add Records To Account" panel to add our newly created record to the also newly created account. Chances are, you'll get an error message along the lines of the screenshot below.

![img](../images/hosted_hibernate_error.png)

### Why Hibernate objects can't be understood when they reach the browser world

So what went wrong? Looking at the hosted mode console, you'll notice the warning message "Exception while dispatching incoming RPC call" was logged to the console. Selecting the warning message, the lower pane will display a rather long stack trace.

This is the part to pay attention to:

```text
Caused by: com.google.gwt.user.client.rpc.SerializationException: Type 'org.hibernate.collection.PersistentSet' was not included in the set of types which can be serialized by this SerializationPolicy or its Class object could not be loaded. For security purposes, this type will not be serialized.
at com.google.gwt.user.server.rpc.impl.StandardSerializationPolicy.validateSerialize(StandardSerializationPolicy.java:83)
at com.google.gwt.user.server.rpc.impl.ServerSerializationStreamWriter.serialize(ServerSerializationStreamWriter.java:591)

```

The key here is the `SerializationException` that was thrown when we tried to load up and retrieve accounts.

So what exactly went wrong? Well, as you may have read in the [GWT RPC docs](../doc/latest/DevGuideServerCommunication.html#DevGuideRemoteProcedureCalls), a [`SerializationException`](/javadoc/latest/com/google/gwt/user/client/rpc/SerializationException.html) is thrown whenever a type transferred over RPC is not "serializable". The definition of serializable here means that the GWT RPC mechanism knows how to serialize and deserialize the type from bytecode to JSON and vice-versa. To declare a type as serializable to the GWT compiler, you can either make the type to be transferred over RPC implement the `IsSerializable` interface, especially created for this purpose, or implement the standard `java.io.Serializable` interface, provided that its members and methods consist of types that are also serializable.

In the case of the `Account` and `Record` Hibernate objects, we are implementing the `Serializable` interface, so these should work, shouldn't they?. As it turns out, the devil is in the details.

When you take an object and turn it into a Hibernate object, the object is now enhanced to be persistent. That persistence does not come without some type of instrumentation of the object. In the case of Hibernate, the Javassist library actually replaces and rewrites the bytecode for these objects by persistent entities to make the Hibernate magic work. What this means for GWT RPC is that by the time the object is ready to be transferred over the wire, it actually isn't the same object that the compiler thought was going to be transferred, so when trying to deserialize, the GWT RPC mechanism no longer knows what the type is and refuses to deserialize it.

In fact, if you were to look deeper to the earlier call to `loadAccounts()`, and step into the `RPC.invokeAndEncodeResponse()` method, you would see that the object we're trying to deserialize has now become an `ArrayList` of `Account` types with their `java.util.Set` of records replaced by the `org.hibernate.collection.PersistentSet` type.

Similar problems arise with other persistence frameworks, such as JDO or JPA, used on [Google App Engine](https://developers.google.com/appengine/docs/java/datastore/usingjdo).

A potential solution would be to replace the types once more in the opposite direction before returning through the server-side RPC call. This is doable, and would solve the problem we encountered here, but we wouldn't be out of harm's way just yet. Another great benefit of using Hibernate is the fact that we can lazily load associated objects when needed. For example, on the server-side, I could load an account, change it around, and only load its associated records when a call to `account.getRecords()` and some action on those records was taken. The special Hibernate instrumentation will take care of actually fetching the records when I make the call, making them available only when really needed.

As you may imagine, this will translate to strange behaviour in the GWT RPC world where these Hibernate objects traveled from the Java server-side to browser land. If a GWT RPC service tries to access associations lazily, you might see something like a `LazyInitializationException` being thrown.

## Integration Strategies <a id="IntegrationStrategies"></a>

Fortunately, there are a number of workarounds that sidestep these issues, as well as provide other benefits inherent to these approaches.

### Using Data Transfer Objects <a id="UsingDTOs"></a>

One of the easiest ways to deal with this issue is to introduce a light object to go between the heavy Hibernate object and its data representation that we care about on the client-side. This go-between is typically referred to as a [Data Transfer Object (DTO)](http://en.wikipedia.org/wiki/Data_Transfer_Object).

The DTO is a simple POJO only containing simple data fields that we can access on the client-side to display on the application page. The Hibernate objects can then be constructed from the data in our data transfer objects. The DTOs themselves will only contain the data we want to persist, but none of the lazy loading or persistence logic added by the Hibernate Javassist to their Hibernate counterparts.

Applying this to our example, we get the following two DTOs:

**AccountDTO.java**

```java
package com.google.musicstore.client.dto;

import java.io.Serializable;
import java.util.Set;

public class AccountDTO implements Serializable {
  private Long id;
  private String name;
  private String password;
  private Set<RecordDTO> records;

  public AccountDTO() {
  }

  public AccountDTO(Long id) {
    this.id = id;
  }

  public AccountDTO(Long id, String name, String password,
      Set<RecordDTO> records) {
    this.id = id;
    this.name = name;
    this.password = password;
    this.records = records;
  }

  // Along with corresponding getters + setters.
}

```

**RecordDTO.java**

```java
package com.google.musicstore.client.dto;

import java.io.Serializable;

public class RecordDTO implements Serializable {
  private Long id;
  private String title;
  private int year;
  private double price;

  public RecordDTO() {
  }

  public RecordDTO(Long id) {
  this.id = id;
  }

  public RecordDTO(Long id, String title, int year, double price) {
    this.id = id;
    this.title = title;
    this.year = year;
    this.price = price;
  }

  // Along with corresponding getters + setters.
}

```

Next, let's add constructors that take these new DTOs as arguments to their Hibernate object counterparts:

**Account.java**

```java
public Account(AccountDTO accountDTO) {
  id = accountDTO.getId();
  name = accountDTO.getName();
  password = accountDTO.getPassword();
  Set<RecordDTO> recordDTOs = accountDTO.getRecords();
  if (recordDTOs != null) {
    Set<Record> records = new HashSet<Record>(recordDTOs.size());
    for (RecordDTO recordDTO : recordDTOs) {
      records.add(new Record(recordDTO));
    }
    this.records = records;
  }
}

```

**Record.java**

```java
public Record(RecordDTO record) {
  id = record.getId();
  title = record.getTitle();
  year = record.getYear();
  price = record.getPrice();
}

```

And finally, we need to modify the existing GWT RPC components to take the DTO counterparts as arguments:

**MusicStoreService.java**

```java
@RemoteServiceRelativePath("musicservice")
public interface MusicStoreService extends RemoteService {
  public List<AccountDTO> getAccounts();

  public List<RecordDTO> getRecords();

  public Long saveAccount(AccountDTO accountDTO);

  public Long saveRecord(RecordDTO recordDTO);

  public void saveRecordToAccount(AccountDTO accountDTO, RecordDTO recordDTO);

  public List<AccountDTO> getAllAccountRecords();
}

```

**MusicStoreServiceAsync.java**

```java
public interface MusicStoreServiceAsync {
  public void getAccounts(AsyncCallback<List<AccountDTO>> callback);

  public void getRecords(AsyncCallback<List<RecordDTO>> callback);

  public void saveAccount(AccountDTO accountDTO, AsyncCallback<Long> callback);

  public void saveRecord(RecordDTO record, AsyncCallback<Long> callback);

  public void saveRecordToAccount(AccountDTO accountDTO, RecordDTO recordDTO, AsyncCallback<Void> callback);

  public void getAllAccountRecords(AsyncCallback<List<AccountDTO>> callback);
}

```

And now we modify the `MusicStoreServiceImpl.java` class.

**MusicStoreServiceImpl.java**

```java
public class MusicStoreServiceImpl extends RemoteServiceServlet implements
MusicStoreService {

  @Override
  public List<AccountDTO> getAccounts() {
    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    session.beginTransaction();
    List<Account> accounts = new ArrayList<Account>(session.createQuery("from Account").list());
    List<AccountDTO> accountDTOs = new ArrayList<AccountDTO>(
    accounts != null ? accounts.size() : 0);
    if (accounts != null) {
      for (Account account : accounts) {
        accountDTOs.add(createAccountDTO(account));
      }
    }
    session.getTransaction().commit();
    return accountDTOs;
  }

  @Override
  public List<RecordDTO> getRecords() {
    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    session.beginTransaction();
    List<Record> records = new ArrayList<Record>(session.createQuery("from Record").list());
    List<RecordDTO> recordDTOs = new ArrayList<RecordDTO>(records != null ? records.size() : 0);
    if (records != null) {
      for (Record record : records) {
        recordDTOs.add(createRecordDTO(record));
      }
    }
    session.getTransaction().commit();
    return recordDTOs;
  }

  @Override
  public Long saveAccount(AccountDTO accountDTO) {
    Account account = new Account(accountDTO);
    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    session.beginTransaction();
    session.save(account);
    session.getTransaction().commit();
    return account.getId();
  }

  @Override
  public Long saveRecord(RecordDTO recordDTO) {
    Record record = new Record(recordDTO);
    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    session.beginTransaction();
    session.save(record);
    session.getTransaction().commit();
    return record.getId();
  }

  @Override
  public void saveRecordToAccount(AccountDTO accountDTO, RecordDTO recordDTO) {
    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    session.beginTransaction();
    Account account = (Account) session.load(Account.class, accountDTO.getId());
    Record record = (Record) session.load(Record.class, recordDTO.getId());
    account.addRecord(record);
    session.save(account);
    session.getTransaction().commit();
  }

  @Override
  public List<AccountDTO> getAllAccountRecords() {
    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    session.beginTransaction();
    List<Account> accounts = new ArrayList<Account>(session.createQuery("from Account").list());
    List<AccountDTO> accountDTOs = new ArrayList<AccountDTO>(accounts != null ? accounts.size() : 0);
    if (accounts != null) {
      for (Account account : accounts) {
        accountDTOs.add(createAccountDTO(account));
      }
    }
    session.getTransaction().commit();
    return accountDTOs;
  }

  private AccountDTO createAccountDTO(Account account) {
    Set<Record> records = account.getRecords();
    Set<RecordDTO> recordDTOs = new HashSet<RecordDTO>(records != null ? records.size() : 0);
    if (records != null) {
      for (Record record : records) {
        recordDTOs.add(createRecordDTO(record));
      }
    }
    return new AccountDTO(account.getId(), account.getName(), account.getPassword(), recordDTOs);
  }

  private RecordDTO createRecordDTO(Record record) {
    return new RecordDTO(record.getId(), record.getTitle(), record.getYear(), record.getPrice());
  }
}

```

And lastly, we need to update the RPC service interface calls from the `MusicStore` entry point class to use the new DTO parametrized method signatures.

We now have the domain package containing the `Account` and `Record` classes where it belongs, isolated to the server-side. We can remove the `<source>` tag referencing the domain package from the base application module XML file now:

**MusicStore.gwt.xml**

```xml
<!-- Remove the line below -->
<source path="domain"/>

```

Notice that not much changes here. The only thing we need to do after retrieving the Hibernate objects from the database is copy them into their DTO equivalents, add those DTOs to a list and return them back in the client-side callback. However, there is one thing that we'll need to watch out for, and that's the process by which we copy these objects to our DTOs.

We created the `createAccountDTO(Account account)` method, which contains the logic that we want to transform the Account Hibernate objects into the data-only DTOs that we're going to return.

**createAccountDTO(Account account)**

```java
private AccountDTO createAccountDTO(Account account) {
  Set<Record> records = account.getRecords();
  Set<RecordDTO> recordDTOs = new HashSet<RecordDTO>(records != null ? records.size() : 0);
  if (records != null) {
    for (Record record : records) {
      recordDTOs.add(createRecordDTO(record));
    }
  }
  return new AccountDTO(account.getId(), account.getName(), account.getPassword(), recordDTOs);
}

```

You'll also notice that we're making a call to another copy method called `createRecordDTO(Record record)`. As you might imagine, much like we needed to transform Account objects into their DTO equivalents, we need to do the same directional transformation for the Record object.

**createRecordDTO(Record record)**

```java
private RecordDTO createRecordDTO(Record record) {
  return new RecordDTO(record.getId(), record.getTitle(), record.getYear(), record.getPrice());
}

```

With the DTO solution implemented, try running: `ant clean build hosted` from command line once more to see the solution in action (all while making sure that the HSQL in-memory DB is still running).

You can download a version of the first sample application with the DTO solution fully implemented [here](http://google-web-toolkit.googlecode.com/files/gwt_hibernate_dto.zip).

#### When should you use the DTO approach

You can see where this is going. The more Hibernate objects we need to transform into DTOs, the more special-cased copy methods we'll need to create to get them transferred over the wire. What's more, because the DTOs that we transfer over the wire won't have the full object graphs loaded as a Hibernate object would, in some situations we need to carefully consider how we're going to copy the Hibernate object to the DTO and how full we want it and its associated objects to be when we send them over the wire, and when we want to leave that to another RPC call. With the DTO approach, all of this has to be handled in code.

There is some good to this approach, though. For one, we now have lightweight data transfer objects that we can send over the wire to the client, leading to leaner payloads. Also, by forcing us to think about copy strategies as we're taking full Hibernate object graphs from the server and optimizing them for what the client needs to see at a given point in time, we reduce the risk of having the browser blowup on an account with a set of five thousand records in it and also make the user experience faster.

The DTOs we created might also double or even triple in use depending on the server-side architecture of our application. For example, something like Java Message Service won't necessarily know how to deal with a Hibernate object passed in as the message. The DTOs can now be used instead to pass in for something much easier to work with in the related JMS components.

All that said, if you have many Hibernate objects that need to be translated, the DTO / copy method creation process can be quite a hassle. Thankfully, there are other strategies that can help with that situation.

### Using Dozer for Hibernate integration <a id="UsingDozer"></a>

[Dozer](http://dozer.sourceforge.net/) is an open source library that can automatically generate DTOs for us by reading and parsing XML files, thereby lightening the load on the developer who would no longer have to create these manually. We can use Dozer to clone our Hibernate entities for us.

First, a little bit of background about how Dozer works. Dozer is based on the Java Bean norm, and uses this to copy data from persistent entities to a new POJO instance.

As mentioned before, Dozer permits us to use XML mappings to tell it which properties to copy to a new DTO instance, as well as which properties to exclude. When Dozer reads these mapping files and copies objects to DTOs, it does so implicitly, meaning that you can expect that any property that hasn't been specifically excluded in the mapping file will be included. This helps keep the Dozer mapping file short and to the point:

```xml
<mappings>
  <mapping>
    <class-a>com.google.musicstore.domain.Account</class-a>
    <class-b>com.google.musicstore.dto.AccountDTO</class-a>
  </mapping>
</mappings>

```

Now that we know how Dozer mappings work, let's see how they apply to Hibernate objects. The idea is to simply copy all the properties over to our DTOs while removing any properties marked with `lazy="true"`. Dozer will take care of replacing these originally lazily loaded persistent collections by plain collections, which can be transferred and serialized through RPC.

```xml
<mappings>
  <mapping>
    <class-a>com.google.musicstore.domain.Account</class-a>
    <class-b>com.google.musicstore.dto.AccountDTO</class-b>
    <field-exclude>
      <a>records</a>
      <b>records</b>
    </field-exclude>
  </mapping>

  <mapping>
    <class-a>com.google.musicstore.domain.Record</class-a>
    <class-b>com.google.musicstore.dto.RecordDTO</class-b>
  </mapping>
</mappings>

```

One of the nice things about Dozer is that it automatically takes care of copying data between two classes for properties that exist in both classes that have the same field type and name. Since the `Account` / `Record` objects and their DTO equivalents both use the same property names, we're already done with our Dozer mappings as configured above. Save this mapping file to `dozerBeanMapping.xml`, and place it on the project classpath. Now all we need to have our previous DTO solution use Dozer is remove the copy logic we added as it is no longer needed, and use the Dozer mappings to copy our Hibernate data to our DTOs, and send them over the wire.

The method signatures for all three GWT RPC `MusicStore` service components remain the same. What changes is simply the copy logic from Hibernate object to DTO in the `MusicStoreServiceImpl` method implementations. Anywhere we would have had `createAccountDTO()` or `createRecordDTO()` calls, we will now have:

```java
DozerBeanMapperSingletonWrapper.getInstance().map(account, AccountDTO.class));
// or
DozerBeanMapperSingletonWrapper.getInstance().map(record, RecordDTO.class));

```

And similarly the other way around when we need to create a Hibernate object from an incoming DTO.

You'll notice this approach forces us to make separate calls to load an Account object's records since we're no longer writing and using our own copy logic. This transitive logic is not necessarily so bad, since the reasons for which we wanted the property to be lazy on the server will probably still be true on the client. When we do want to copy properties with `lazy="true"` without running into `LazyInitializationExceptions`, Dozer does allow us to create our own custom converters - classes which dictate how one type is copied to the next. This becomes similar to the DTO approach, except now all our copy logic would be neatly refactored into a purpose-built converter class.

Try the sample application with the Dozer solution by once again running: `ant clean build hosted` from command line. You can download a version of the first sample application with the Dozer solution fully implemented [here](http://google-web-toolkit.googlecode.com/files/gwt_hibernate_dozer.zip).

#### When you should use the Dozer approach

This approach suffers from some of the same drawbacks as the DTO approach. You will need to create a DTO or some other type of RPC transferrable class that the Dozer mapper can copy Hibernate object data into. You will also need to create a `<mapping>` entry for every Hibernate object that needs to be copied to a DTO when transferring data through RPC.

Another downside to using Dozer is that for types with data deeply nested in its properties, the DTO lookup and generation could take a while, especially if we're in the process of mapping many of those entities. Custom converters can help here, but they can become cumbersome as the number of objects continues to grow.

However, the Dozer approach adds some nice automation to the generation of DTOs, and saves some time over manually generating them by hand as we saw earlier. We have saved on a lot of copy logic that would have made our code a lot heftier and less cohesive. It also gives us configurable control over which properties get copied, which helps make sure we aren't sending objects bloated with extraneous data back to the client.

Therefore, if your project counts many Hibernate objects that need to be transferred over RPC, and also contains copy logic that is straightforward enough to be captured in Dozer XML mappings, this might be a good approach for you. On the other hand, when the fact that lazily loaded properties force changes in your load strategies all the way up to the client layer - breaking the benefit of layering in the first place, and when the number of objects starts growing drastically, perhaps the Gilead approach, described below, would be the best choice.

### Using Gilead for Hibernate Integration <a id="UsingGilead"></a>

[Gilead](http://noon.gilead.free.fr/gilead/index.php?page=gwt) (formerly known as Hibernate4Gwt) is [an opensource library](http://sourceforge.net/projects/gilead/) that proposes another, more transparent solution to exchange objects between Hibernate and GWT.

#### How it works

The first principle to Gilead is the automatic replacement of uninitialized proxies by null and persistent collections by basic collections present in the emulated JRE. These replacements are both done without requiring any specific mappings to be defined. Gilead also stores the information necessary to recreate the Hibernate proxy or persistent collection in either the server-side object or the cloned object, making it possible to recreate these objects without needing to make calls to the database.

![img](../images/gilead_diagram.png)

Gilead also provides a dedicated adapter for Hibernate and GWT to make their integration painless.

In the simplest case, integration between GWT and Hibernate can be realized by following these steps:

1.  Make your persistent classes inherit the `LightEntity` class (class used for stateless mode integration in the Gilead library).
2.  Making your remote RPC services extend [`PersistentRemoteService`](http://gilead.svn.sourceforge.net/viewvc/gilead/gilead/branches/1.2/adapter4gwt/src/net/sf/gilead/gwt/PersistentRemoteService.java?revision=896&view=markup) instead of [`RemoteServiceServlet`](/javadoc/latest/com/google/gwt/user/server/rpc/RemoteServiceServlet.html).
3.  Configuring your beanManager for your GWT RPC service as shown in the code snippet below (see [Gilead documentation](http://noon.gilead.free.fr/gilead/index.php?page=documentation) for more on configuring bean managers):

```java
public class UserRemoteImpl extends PersistentRemoteService implements UserRemote {
  ...

      /**
       * Constructor
   */
  public UserRemoteImpl() {
    HibernateUtil hibernateUtil = new HibernateUtil();    hibernateUtil.setSessionFactory(HibernateUtil.getSessionFactory());

        PersistentBeanManager persistentBeanManager = new PersistentBeanManager();    persistentBeanManager.setPersistenceUtil(hibernateUtil);    persistentBeanManager.setProxyStore(new StatelessProxyStore());

        setBeanManager(persistentBeanManager);
  }
}

```

Once this configuration is in place, our Hibernate entities are automatically converted into types that can be transferred over RPC and used in client-side GWT code, without any other coding or mapping needing to be defined on our part.

Applying these three changes to the base MusicStore application would lead to the following:

1) Make your persistent classes inherit `LightEntity`

**Account.java**

```java
import net.sf.gilead.pojo.java5.LightEntity;

public class Account extends LightEntity implements Serializable {
  // ...
}

```

**Record.java**

```java
import net.sf.gilead.pojo.java5.LightEntity;

public class Account extends LightEntity implements Serializable {
  // ...
}

```

2) Making your remote RPC service extend `PersistentRemoteService`

**MusicStoreServiceImpl.java**

```java
import net.sf.gilead.gwt.PersistentRemoteService;

public class MusicStoreServiceImpl extends PersistentRemoteService implements MusicStoreService {
  // ...
}

```

3) Configure your `beanManager` for your GWT RPC service as shown in the code snippet above

**MusicStoreServiceImpl.java**

```java
import net.sf.gilead.core.PersistentBeanManager;
import net.sf.gilead.core.hibernate.HibernateUtil;
import net.sf.gilead.core.store.stateless.StatelessProxyStore;
import net.sf.gilead.gwt.PersistentRemoteService;

public class MusicStoreServiceImpl extends PersistentRemoteService implements MusicStoreService {

  /**
   * Constructor
   */
  public MusicStoreServiceImpl() {
    HibernateUtil gileadHibernateUtil = new HibernateUtil();
    gileadHibernateUtil.setSessionFactory(com.google.musicstore.util.HibernateUtil.getSessionFactory());

    PersistentBeanManager persistentBeanManager = new PersistentBeanManager();
    persistentBeanManager.setPersistenceUtil(gileadHibernateUtil);
    persistentBeanManager.setProxyStore(new StatelessProxyStore());

    setBeanManager(persistentBeanManager);
  }
}

```

There is a change to note here, aside from the new constructor that sets up the bean manager. We're now using `net.sf.gilead.core.hibernate.HibernateUtil` in addition to the `HibernateUtil` class we defined in the util package. This is required to setup Gilead appropriately.

And that's all there is to it. We're ready to go with the original calls we made from our GWT RPC service interfaces on the client-side referring the `Account` and `Record` objects. Try executing the command below to compile the application with the Gilead approach and see it running in hosted mode:

```shell
ant clean build hosted
```

Once more, you can download a version of the sample application we've been building with the Gilead solution fully implemented [here](http://google-web-toolkit.googlecode.com/files/gwt_hibernate_gilead.zip).

#### Transport annotations

In order to avoid sending sensitive or heavy data over the network, Gilead also provides an `@ServerOnly` annotation that will exclude the property annotated in the cloned object. Also, if you don't want values that are changed in the clone on the GWT client-side to be reflected and persisted in the persistent entity, you can add a `@ReadOnly` annotation to properties as well.

#### When you should use Gilead

The main advantage to Gilead is transparency and developer productivity: once the configuration is in place, everything is taken care of to transfer and use your Hibernate entities as you would expect in the client-side (what we tried to do in our initial attempt to get `Account` and `Record` transferred over the RPC wire).

There is also a lot of support available on the [Gilead forum](http://sourceforge.net/forum/?group_id=239931), mainly from the actual author of the library, Bruno Marchesson. Having first been announcement two years ago, [Gilead](http://noon.gilead.free.fr/gilead/) has matured and is renowned for its effectiveness for GWT and Hibernate integration.

However, Gilead does come with some disadvantages:

*   The initial project configuration is a crucial initial step that is sometimes difficult to get right for newcomers to the library. Much of the forum posts on the Gilead project are in fact related to issues with configuration and setup.
*   The Gilead library works like a black box, much like Hibernate and GWT.
*   The '`dynamic proxy`' feature is still in beta mode.

## Conclusion <a id="Conclusion"></a>

If you're using Hibernate on the server-side, hopefully the integration strategies discussed above will help get your GWT client-side talking to your Hibernate backend. Each of these have their pluses and minuses, which vary especially with respect to the burden on the developer implementing the interoperation. However, the overarching concern across each of these strategies, as well as other facets of web application development, should always be performance for your users. A number of these approaches can sometimes incur considerable runtime overhead. For example, the Dozer and Gilead approach may become taxing to the user experience when there are larger sets of data to serialize, whereas the DTO solution can be designed to be as concise and effective as needed to improve performance.

There are other aspects of Hibernate and GWT integration that might not have been covered in this article. For any further discussions, I strongly encourage you to come visit us on the [GWT Developer Forum](http://groups.google.com/group/Google-Web-Toolkit).
