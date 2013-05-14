<style>

div.screenshot img {
margin: 20px;
}
</style>

<p><i>Sumit Chandel, Google Developer Relations
<br>
July 2009
<br>
(with thanks to Bruno Marchesson for his contributions to this article)</i>
</p>
<br>
<p>Many developers have asked how to use <a href="http://www.gwtproject.org">GWT</a> and <a href="https://www.hibernate.org/">Hibernate</a> together. Although you can find numerous discussions about this topic on the <a href="http://groups.google.com/group/Google-Web-Toolkit">GWT Developer Forum</a>, we thought it would be beneficial to sum up some of the most popular strategies, and highlight their advantages and shortcomings in the context of GWT application development.</p>

<p>Before we get into integration strategies, let's get familiar with the basics.</p>

<h2 id="TheBasics">The Basics</h2>

<p>For the purposes of this article, we're going to assume that the reader is already familiar with Hibernate configuration and usage on the server-side. If you'd like to know more about Hibernate and how it works, the <a href="http://docs.jboss.org/hibernate/stable/core/reference/en/html/tutorial.html">getting started tutorial</a> posted on the Hibernate homepage is strongly recommended.</p>

<p>Also, to get up and running with persistence for our Hibernate objects, we'll be using <a href="http://hsqldb.org/">HSQLDB</a> which provides an in-memory Hibernate SQL database. You can download this <a href="http://sourceforge.net/project/showfiles.php?group_id=23316">here</a>. Again, we won't talk too much about how HSQLDB works, but we will cover just enough to get it running for the examples we'll see here.</p>

<p>Let's start out with a simple example. Suppose we're developing an online music record store. An obvious domain object in such a application would be a <code>Record</code> object that we would want to persist on the server-side and display on the client-side. We would also want users to be able to create accounts and add music records to their profiles online. It would also make sense for us to create an <code>Account</code> object that we'll want to persist. So let's create these two classes.</p>

<p><b>Record.java</b></p>

<pre class="code">
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
</pre>


<p><b>Account.java</b></p>

<pre class="code">
public class Account implements Serializable {
  Long id;
  String name;
  String password;
  Set&lt;Record&gt; records;

  public Account() {
  }

  public Account(Long id) {
    this.id = id;
  }

  public void addRecord(Record record) {
    if (records == null) {
      records = new HashSet&lt;Record&gt;();
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
</pre>

<p>Then we need to create the corresponding Hibernate mapping files for each of these persisted types, like so:</p>

<p><b>Record.hbm.xml</b></p>

<pre class="code">
&lt;hibernate-mapping&gt;
  &lt;class name=&quot;com.google.musicstore.domain.Record&quot; table=&quot;RECORD&quot;&gt;
    &lt;id name=&quot;id&quot; column=&quot;RECORD_ID&quot;&gt;
      &lt;generator class=&quot;native&gt;/&gt;
    &lt;/id&gt;
    &lt;property name=&quot;title&quot;/&gt;
    &lt;property name=&quot;year&quot;/&gt;
    &lt;property name=&quot;price&quot;/&gt;

  &lt;/class&gt;
&lt;/hibernate-mapping&gt;
</pre>

<p><b>Account.hbm.xml</b></p>

<pre class="code">
&lt;hibernate-mapping&gt;
  &lt;class name=&quot;com.google.musicstore.domain.Account&quot; table=&quot;ACCOUNT&quot;&gt;
    &lt;id name=&quot;id&quot; column=&quot;ACCOUNT_ID&quot;&gt;
      &lt;generator class=&quot;native&quot;/&gt;
    &lt;/id&gt;
    &lt;property name=&quot;name&quot;/&gt;
    &lt;property name=&quot;password&quot;/&gt;

    &lt;set name=&quot;records&quot; table=&quot;ACCOUNT_RECORD&quot; lazy=&quot;true&quot;&gt;
      &lt;key column=&quot;ACCOUNT_ID&quot;/&gt;
      &lt;many-to-many column=&quot;RECORD_ID&quot; class=&quot;com.google.musicstore.domain.Record&quot;/&gt;
    &lt;/set&gt;
  &lt;/class&gt;
&lt;/hibernate-mapping&gt;
</pre>

<p>Now that we've created our persistent classes, let's create a bare bones UI that will allow us to enter new accounts and records, as well as the GWT RPC services that will persist them on the server-side. Let's start with the RPC services.</p>

<p>We won't go into the specifics of the role each RPC component plays here, but if you're unfamiliar with the GWT RPC subsystem, check out the <a href="../doc/latest/DevGuideServerCommunication#DevGuideRemoteProcedureCalls">GWT RPC docs</a> to get up to speed.</p>

<p>First, we create the client-side service interfaces. If you'd like to avoid the large number of interface methods listed below, consider using the Command pattern, as described <a href="http://www.youtube.com/watch?v=PDuhR18-EdM">here</a>:</p>

<p><b>MusicStoreService.java</b></p>

<pre class="code">
@RemoteServiceRelativePath(&quot;musicservice&quot;)
public interface MusicStoreService extends RemoteService {
  public List&lt;Account&gt; getAccounts();

  public List&lt;Record&gt; getRecords();

  public Long saveAccount(Account account);

  public Long saveRecord(Record record);

  public void saveRecordToAccount(Account account, Record record);
}
</pre>

<p><b>MusicStoreServiceAsync.java</b></p>

<pre class="code">
public interface MusicStoreServiceAsync {
  public void getAccounts(AsyncCallback&lt;List&lt;Account&gt;&gt; callback);

  public void getRecords(AsyncCallback&lt;List&lt;Record&gt;&gt; callback);

  public void saveAccount(Account accountDTO, AsyncCallback&lt;Long&gt; callback);

  public void saveRecord(Record record, AsyncCallback&lt;Long&gt; callback);

  public void saveRecordToAccount(Account accountDTO, Record recordDTO,
AsyncCallback&lt;Void&gt; callback);
}
</pre>

<p>Finally, we create the service implementation class on the server-side.</p>

<p><b>MusicStoreServiceImpl.java</b></p>

<pre class="code">
public class MusicStoreServiceImpl extends RemoteServiceServlet implements
MusicStoreService {

  @Override
  public List&lt;Account&gt; getAccounts() {
    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    session.beginTransaction();
    List&lt;Account&gt; accounts = new ArrayList&lt;Account&gt;(session.createQuery("from Account").list());
    session.getTransaction().commit();
    return accounts;
  }

  @Override
  public List&lt;Record&gt; getRecords() {
    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    session.beginTransaction();
    List&lt;Record&gt; records = new ArrayList&lt;Record&gt;(session.createQuery("from Record").list());
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
</pre>

<p>You may have noticed some <code>HibernateUtil</code> calls in the <code>MusicStoreServiceImpl</code>  method implementations in the code snippet above. This is actually a custom class that was created as a helper utility to retrieve and use the Hibernate session factory, exactly as is done in the <a href="http://docs.jboss.org/hibernate/stable/core/reference/en/html/tutorial.html">Hibernate tutorial</a> mentioned earlier. For convenience, here is the <code>HibernateUtil</code> code pasted below so you can follow along. If you want to learn more details about what the <code>HibernateUtil</code> class is doing, I strongly advise checking out the tutorial for a full explanation.</p>

<pre class="code">
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
</pre>

<p>Finally, our server-side GWT RPC services are ready to <a href="http://en.wikipedia.org/wiki/Create,_read,_update_and_delete">CRUD</a> our Hibernate objects (actually, we skipped the Delete functionality).  Now we just need an interface to actually make the RPC calls. I've created a sample application with a UI that will allow us to add records, add accounts, add records to accounts and of course view all existing accounts and their associated records. The sample code is not representative of best practices, just a quick and dirty implementation to get us up and running. The sample also includes the server-side RPC and Hibernate code we've worked on up to this point. You can download the sample <a href="http://google-web-toolkit.googlecode.com/files/gwt_hibernate_base.zip">here</a>.</p>

<p>In the example source code, you'll find a <code>build.xml</code> file and a <code>build.properties</code> file in the root directory. After properly configuring the <code>gwt.home</code> and <code>gwt.dev.jar</code> properties for your machine,  you can use <a href="http://ant.apache.org/">Ant</a> to build the project, as well as start up hosted mode to see the UI and our Hibernate instance setup in the embedded Jetty server. Just run the following from command line:</p>

<pre class="code">ant build hosted</pre>

<p>Before doing that, though, we'll need to have our in-memory HSQLDB up and running so we can persist our Hibernate objects. In the example project you downloaded, you should find '<code>data</code>' folder under the project root directory. You'll also find the <code>hsqldb.jar</code> in the <code>lib</code> folder. All we need to do to start up the in-memory HSQLDB is invoke the <code>org.hsqldb.Server</code> class contained in the <code>hsqldb.jar</code> file, while in the '<code>data</code>' directory to host the HSQLDB properties and log output. You can do this by running the following from command line (while in the '<code>data</code>' directory):</p>

<pre class="code">java -cp ../lib/hsqldb.jar org.hsqldb.Server</pre>

<p>Now that we have our persistence layer ready, let's compile and run our application in hosted mode using the ant command above. Once both <code>build</code> and <code>hosted</code> ant tasks have completed, you should see the hosted mode browser startup, with the &quot;Add Accounts / Records&quot; tab displayed. Finally, we can start persisting our records (from the GWT client-side to the Hibernate database, using our Hibernate objects!). Go ahead and try adding an account and a a record to our in-memory Hibernate to get our data set started.</p>

<p>Next, try selecting the &quot;Add Records To Account&quot; panel to add our newly created record to the also newly created account. Chances are, you'll get an error message along the lines of the screenshot below.</p>

<div class="screenshot" align="center" width="80%"><img src="../images/hosted_hibernate_error.png"/></div>

<h3>Why Hibernate objects can't be understood when they reach the browser world</h3>

<p>So what went wrong? Looking at the hosted mode console, you'll notice the warning message &quot;Exception while dispatching incoming RPC call&quot; was logged to the console. Selecting the warning message, the lower pane will display a rather long stack trace.</p>

<p>This is the part to pay attention to:</p>

<pre class="code">
Caused by: com.google.gwt.user.client.rpc.SerializationException: Type 'org.hibernate.collection.PersistentSet' was not included in the set of types which can be serialized by this SerializationPolicy or its Class object could not be loaded. For security purposes, this type will not be serialized.
at com.google.gwt.user.server.rpc.impl.StandardSerializationPolicy.validateSerialize(StandardSerializationPolicy.java:83)
at com.google.gwt.user.server.rpc.impl.ServerSerializationStreamWriter.serialize(ServerSerializationStreamWriter.java:591)
</pre>

<p>The key here is the <code>SerializationException</code> that was thrown when we tried to load up and retrieve accounts.</p>

<p>So what exactly went wrong? Well, as you may have read in the <a href="../doc/latest/DevGuideServerCommunication.html#DevGuideRemoteProcedureCalls">GWT RPC docs</a>, a <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/1.6/com/google/gwt/user/client/rpc/SerializationException.html"><code>SerializationException</code></a> is thrown whenever a type transferred over RPC is not &quot;serializable&quot;. The definition of serializable here means that the GWT RPC mechanism knows how to serialize and deserialize the type from bytecode to JSON and vice-versa. To declare a type as serializable to the GWT compiler, you can either make the type to be transferred over RPC implement the <code>IsSerializable</code> interface, especially created for this purpose, or implement the standard <code>java.io.Serializable</code> interface, provided that its members and methods consist of types that are also serializable.</p>

<p>In the case of the <code>Account</code> and <code>Record</code> Hibernate objects, we are implementing the <code>Serializable</code> interface, so these should work, shouldn't they?. As it turns out, the devil is in the details.</p>

<p>When you take an object and turn it into a Hibernate object, the object is now enhanced to be persistent. That persistence does not come without some type of instrumentation of the object. In the case of Hibernate, the Javassist library actually replaces and rewrites the bytecode for these objects by persistent entities to make the Hibernate magic work. What this means for GWT RPC is that by the time the object is ready to be transferred over the wire, it actually isn't the same object that the compiler thought was going to be transferred, so when trying to deserialize, the GWT RPC mechanism no longer knows what the type is and refuses to deserialize it.</p>

<p>In fact, if you were to look deeper to the earlier call to <code>loadAccounts()</code>, and step into the <code>RPC.invokeAndEncodeResponse()</code> method, you would see that the object we're trying to deserialize has now become an <code>ArrayList</code> of <code>Account</code> types with their <code>java.util.Set</code> of records replaced by the <code>org.hibernate.collection.PersistentSet</code> type.</p>

<p>Similar problems arise with other persistence frameworks, such as JDO or JPA, used on <a href="https://developers.google.com/appengine/docs/java/datastore/usingjdo">Google App Engine</a>.</p>

<p>A potential solution would be to replace the types once more in the opposite direction before returning through the server-side RPC call. This is doable, and would solve the problem we encountered here, but we wouldn't be out of harm's way just yet. Another great benefit of using Hibernate is the fact that we can lazily load associated objects when needed. For example, on the server-side, I could load an account, change it around, and only load its associated records when a call to <code>account.getRecords()</code> and some action on those records was taken. The special Hibernate instrumentation will take care of actually fetching the records when I make the call, making them available only when really needed.</p>

<p>As you may imagine, this will translate to strange behaviour in the GWT RPC world where these Hibernate objects traveled from the Java server-side to browser land. If a GWT RPC service tries to access associations lazily, you might see something like a <code>LazyInitializationException</code> being thrown.</p>

<h2 id="IntegrationStrategies">Integration Strategies</h2>

<p>Fortunately, there are a number of workarounds that sidestep these issues, as well as provide other benefits inherent to these approaches.</p>

<h3 id="UsingDTOs">Using Data Transfer Objects</h3>

<p>One of the easiest ways to deal with this issue is to introduce a light object to go between the heavy Hibernate object and its data representation that we care about on the client-side. This go-between is typically referred to as a <a href="http://en.wikipedia.org/wiki/Data_Transfer_Object">Data Transfer Object (DTO)</a>.</p>

<p>The DTO is a simple POJO only containing simple data fields that we can access on the client-side to display on the application page. The Hibernate objects can then be constructed from the data in our data transfer objects. The DTOs themselves will only contain the data we want to persist, but none of the lazy loading or persistence logic added by the Hibernate Javassist to their Hibernate counterparts.</p>

<p>Applying this to our example, we get the following two DTOs:</p>

<p><b>AccountDTO.java</b></p>

<pre class="code">
package com.google.musicstore.client.dto;

import java.io.Serializable;
import java.util.Set;

public class AccountDTO implements Serializable {
  private Long id;
  private String name;
  private String password;
  private Set&lt;RecordDTO&gt; records;

  public AccountDTO() {
  }

  public AccountDTO(Long id) {
    this.id = id;
  }

  public AccountDTO(Long id, String name, String password,
      Set&lt;RecordDTO&gt; records) {
    this.id = id;
    this.name = name;
    this.password = password;
    this.records = records;
  }

  // Along with corresponding getters + setters.
}
</pre>

<p><b>RecordDTO.java</b></p>

<pre class="code">
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
</pre>

<p>Next, let's add constructors that take these new DTOs as arguments to their Hibernate object counterparts:</p>

<p><b>Account.java</b></p>

<pre class"code">
public Account(AccountDTO accountDTO) {
  id = accountDTO.getId();
  name = accountDTO.getName();
  password = accountDTO.getPassword();
  Set&lt;RecordDTO&gt; recordDTOs = accountDTO.getRecords();
  if (recordDTOs != null) {
    Set&lt;Record&gt; records = new HashSet&lt;Record&gt;(recordDTOs.size());
    for (RecordDTO recordDTO : recordDTOs) {
      records.add(new Record(recordDTO));
    }
    this.records = records;
  }
}
</pre>

<p><b>Record.java</b></p>

<pre class="code">
public Record(RecordDTO record) {
  id = record.getId();
  title = record.getTitle();
  year = record.getYear();
  price = record.getPrice();
}
</pre>

<p>And finally, we need to modify the existing GWT RPC components to take the DTO counterparts as arguments:</p>

<p><b>MusicStoreService.java</b></p>

<pre class="code">
@RemoteServiceRelativePath(&quot;musicservice&quot;)
public interface MusicStoreService extends RemoteService {
  public List&lt;AccountDTO&gt; getAccounts();

  public List&lt;RecordDTO&gt; getRecords();

  public Long saveAccount(AccountDTO accountDTO);

  public Long saveRecord(RecordDTO recordDTO);

  public void saveRecordToAccount(AccountDTO accountDTO, RecordDTO recordDTO);

  public List&lt;AccountDTO&gt; getAllAccountRecords();
}
</pre>

<p><b>MusicStoreServiceAsync.java</b></p>

<pre class="code">
public interface MusicStoreServiceAsync {
  public void getAccounts(AsyncCallback&lt;List&lt;AccountDTO&gt;&gt; callback);

  public void getRecords(AsyncCallback&lt;List&lt;RecordDTO&gt;&gt; callback);

  public void saveAccount(AccountDTO accountDTO, AsyncCallback&lt;Long&gt; callback);

  public void saveRecord(RecordDTO record, AsyncCallback&lt;Long&gt; callback);

  public void saveRecordToAccount(AccountDTO accountDTO, RecordDTO recordDTO, AsyncCallback&lt;Void&gt; callback);

  public void getAllAccountRecords(AsyncCallback&lt;List&lt;AccountDTO&gt;&gt; callback);
}
</pre>

<p>And now we modify the <code>MusicStoreServiceImpl.java</code> class.</p>

<p><b>MusicStoreServiceImpl.java</b></p>

<pre class="code">
public class MusicStoreServiceImpl extends RemoteServiceServlet implements
MusicStoreService {

  @Override
  public List&lt;AccountDTO&gt; getAccounts() {
    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    session.beginTransaction();
    List&lt;Account&gt; accounts = new ArrayList&lt;Account&gt;(session.createQuery("from Account").list());
    List&lt;AccountDTO&gt; accountDTOs = new ArrayList&lt;AccountDTO&gt;(
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
  public List&lt;RecordDTO&gt; getRecords() {
    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    session.beginTransaction();
    List&lt;Record&gt; records = new ArrayList&lt;Record&gt;(session.createQuery("from Record").list());
    List&lt;RecordDTO&gt; recordDTOs = new ArrayList&lt;RecordDTO&gt;(records != null ? records.size() : 0);
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
  public List&lt;AccountDTO&gt; getAllAccountRecords() {
    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    session.beginTransaction();
    List&lt;Account&gt; accounts = new ArrayList&lt;Account&gt;(session.createQuery("from Account").list());
    List&lt;AccountDTO&gt; accountDTOs = new ArrayList&lt;AccountDTO&gt;(accounts != null ? accounts.size() : 0);
    if (accounts != null) {
      for (Account account : accounts) {
        accountDTOs.add(createAccountDTO(account));
      }
    }
    session.getTransaction().commit();
    return accountDTOs;
  }

  private AccountDTO createAccountDTO(Account account) {
    Set&lt;Record&gt; records = account.getRecords();
    Set&lt;RecordDTO&gt; recordDTOs = new HashSet&lt;RecordDTO&gt;(records != null ? records.size() : 0);
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
</pre>

<p>And lastly, we need to update the RPC service interface calls from the <code>MusicStore</code> entry point class to use the new DTO parametrized method signatures.</p>

<p>We now have the domain package containing the <code>Account</code> and <code>Record</code> classes where it belongs, isolated to the server-side. We can remove the <code>&lt;source&gt;</code> tag referencing the domain package from the base application module XML file now:</p>

<p><b>MusicStore.gwt.xml</b></p>

<pre class="code">
&lt;!-- Remove the line below --&gt;
&lt;source path="domain"/&gt;
</pre>

<p>Notice that not much changes here. The only thing we need to do after retrieving the Hibernate objects from the database is copy them into their DTO equivalents, add those DTOs to a list and return them back in the client-side callback. However, there is one thing that we'll need to watch out for, and that's the process by which we copy these objects to our DTOs.</p>

<p>We created the <code>createAccountDTO(Account account)</code> method, which contains the logic that we want to transform the Account Hibernate objects into the data-only DTOs that we're going to return.</p>

<p><b>createAccountDTO(Account account)</b></p>

<pre class="code">
private AccountDTO createAccountDTO(Account account) {
  Set&lt;Record&gt; records = account.getRecords();
  Set&lt;RecordDTO&gt; recordDTOs = new HashSet&lt;RecordDTO&gt;(records != null ? records.size() : 0);
  if (records != null) {
    for (Record record : records) {
      recordDTOs.add(createRecordDTO(record));
    }
  }
  return new AccountDTO(account.getId(), account.getName(), account.getPassword(), recordDTOs);
}
</pre>

<p>You'll also notice that we're making a call to another copy method called <code>createRecordDTO(Record record)</code>. As you might imagine, much like we needed to transform Account objects into their DTO equivalents, we need to do the same directional transformation for the Record object.</p>

<p><b>createRecordDTO(Record record)</b></p>

<pre class="code">
private RecordDTO createRecordDTO(Record record) {
  return new RecordDTO(record.getId(), record.getTitle(), record.getYear(), record.getPrice());
}
</pre>

<p>With the DTO solution implemented, try running: <code>ant clean build hosted</code> from command line once more to see the solution in action (all while making sure that the HSQL in-memory DB is still running).</p>

<p>You can download a version of the first sample application with the DTO solution fully implemented <a href="http://google-web-toolkit.googlecode.com/files/gwt_hibernate_dto.zip">here</a>.</p>

<h4>When should you use the DTO approach</h4>

<p>You can see where this is going. The more Hibernate objects we need to transform into DTOs, the more special-cased copy methods we'll need to create to get them transferred over the wire. What's more, because the DTOs that we transfer over the wire won't have the full object graphs loaded as a Hibernate object would, in some situations we need to carefully consider how we're going to copy the Hibernate object to the DTO and how full we want it and its associated objects to be when we send them over the wire, and when we want to leave that to another RPC call. With the DTO approach, all of this has to be handled in code.</p>

<p>There is some good to this approach, though. For one, we now have lightweight data transfer objects that we can send over the wire to the client, leading to leaner payloads. Also, by forcing us to think about copy strategies as we're taking full Hibernate object graphs from the server and optimizing them for what the client needs to see at a given point in time, we reduce the risk of having the browser blowup on an account with a set of five thousand records in it and also make the user experience faster.</p>

<p>The DTOs we created might also double or even triple in use depending on the server-side architecture of our application. For example, something like Java Message Service won't necessarily know how to deal with a Hibernate object passed in as the message. The DTOs can now be used instead to pass in for something much easier to work with in the related JMS components.</p>

<p>All that said, if you have many Hibernate objects that need to be translated, the DTO / copy method creation process can be quite a hassle. Thankfully, there are other strategies that can help with that situation.</p>

<h3 id="UsingDozer">Using Dozer for Hibernate integration</h3>

<p><a href="http://dozer.sourceforge.net/">Dozer</a> is an open source library that can automatically generate DTOs for us by reading and parsing XML files, thereby lightening the load on the developer who would no longer have to create these manually. We can use Dozer to clone our Hibernate entities for us.</p>

<p>First, a little bit of background about how Dozer works. Dozer is based on the Java Bean norm, and uses this to copy data from persistent entities to a new POJO instance.</p>

<p>As mentioned before, Dozer permits us to use XML mappings to tell it which properties to copy to a new DTO instance, as well as which properties to exclude. When Dozer reads these mapping files and copies objects to DTOs, it does so implicitly, meaning that you can expect that any property that hasn't been specifically excluded in the mapping file will be included. This helps keep the Dozer mapping file short and to the point:</p>

<pre class="code">
&lt;mappings&gt;
  &lt;mapping&gt;
    &lt;class-a&gt;com.google.musicstore.domain.Account&lt;/class-a&gt;
    &lt;class-b&gt;com.google.musicstore.dto.AccountDTO&lt;/class-a&gt;
  &lt;/mapping&gt;
&lt;/mappings&gt;
</pre>

<p>Now that we know how Dozer mappings work, let's see how they apply to Hibernate objects. The idea is to simply copy all the properties over to our DTOs while removing any properties marked with <code>lazy=&quot;true&quot;</code>. Dozer will take care of replacing these originally lazily loaded persistent collections by plain collections, which can be transferred and serialized through RPC.</p>

<pre class="code">
&lt;mappings&gt;
  &lt;mapping&gt;
    &lt;class-a&gt;com.google.musicstore.domain.Account&lt;/class-a&gt;
    &lt;class-b&gt;com.google.musicstore.dto.AccountDTO&lt;/class-b&gt;
    &lt;field-exclude&gt;
      &lt;a&gt;records&lt;/a&gt;
      &lt;b&gt;records&lt;/b&gt;
    &lt;/field-exclude&gt;
  &lt;/mapping&gt;

  &lt;mapping&gt;
    &lt;class-a&gt;com.google.musicstore.domain.Record&lt;/class-a&gt;
    &lt;class-b&gt;com.google.musicstore.dto.RecordDTO&lt;/class-b&gt;
  &lt;/mapping&gt;
&lt;/mappings&gt;
</pre>

<p>One of the nice things about Dozer is that it automatically takes care of copying data between two classes for properties that exist in both classes that have the same field type and name. Since the <code>Account</code> / <code>Record</code> objects and their DTO equivalents both use the same property names, we're already done with our Dozer mappings as configured above. Save this mapping file to <code>dozerBeanMapping.xml</code>, and place it on the project classpath. Now all we need to have our previous DTO solution use Dozer is remove the copy logic we added as it is no longer needed, and use the Dozer mappings to copy our Hibernate data to our DTOs, and send them over the wire.</p>

<p>The method signatures for all three GWT RPC <code>MusicStore</code> service components remain the same. What changes is simply the copy logic from Hibernate object to DTO in the <code>MusicStoreServiceImpl</code> method implementations. Anywhere we would have had <code>createAccountDTO()</code> or <code>createRecordDTO()</code> calls, we will now have:</p>

<pre class="code">
DozerBeanMapperSingletonWrapper.getInstance().map(account, AccountDTO.class));
// or
DozerBeanMapperSingletonWrapper.getInstance().map(record, RecordDTO.class));
</pre>

<p>And similarly the other way around when we need to create a Hibernate object from an incoming DTO.</p>

<p>You'll notice this approach forces us to make separate calls to load an Account object's records since we're no longer writing and using our own copy logic. This transitive logic is not necessarily so bad, since the reasons for which we wanted the property to be lazy on the server will probably still be true on the client. When we do want to copy properties with <code>lazy=&quot;true&quot;</code> without running into <code>LazyInitializationExceptions</code>, Dozer does allow us to create our own custom converters - classes which dictate how one type is copied to the next. This becomes similar to the DTO approach, except now all our copy logic would be neatly refactored into a purpose-built converter class.</p>

<p>Try the sample application with the Dozer solution by once again running: <code>ant clean build hosted</code> from command line. You can download a version of the first sample application with the Dozer solution fully implemented <a href="http://google-web-toolkit.googlecode.com/files/gwt_hibernate_dozer.zip">here</a>.</p>

<h4>When you should use the Dozer approach</h4>

<p>This approach suffers from some of the same drawbacks as the DTO approach. You will need to create a DTO or some other type of RPC transferrable class that the Dozer mapper can copy Hibernate object data into. You will also need to create a <code>&lt;mapping&gt;</code> entry for every Hibernate object that needs to be copied to a DTO when transferring data through RPC.</p>

<p>Another downside to using Dozer is that for types with data deeply nested in its properties, the DTO lookup and generation could take a while, especially if we're in the process of mapping many of those entities. Custom converters can help here, but they can become cumbersome as the number of objects continues to grow.</p>

<p>However, the Dozer approach adds some nice automation to the generation of DTOs, and saves some time over manually generating them by hand as we saw earlier. We have saved on a lot of copy logic that would have made our code a lot heftier and less cohesive. It also gives us configurable control over which properties get copied, which helps make sure we aren't sending objects bloated with extraneous data back to the client.</p>

<p>Therefore, if your project counts many Hibernate objects that need to be transferred over RPC, and also contains copy logic that is straightforward enough to be captured in Dozer XML mappings, this might be a good approach for you. On the other hand, when the fact that lazily loaded properties force changes in your load strategies all the way up to the client layer - breaking the benefit of layering in the first place, and when the number of objects starts growing drastically, perhaps the Gilead approach, described below, would be the best choice.</p>

<h3 id="UsingGilead">Using Gilead for Hibernate Integration</h3>

<p><a href="http://noon.gilead.free.fr/gilead/index.php?page=gwt">Gilead</a> (formerly known as Hibernate4Gwt) is <a href="http://sourceforge.net/projects/gilead/">an opensource library</a> that proposes another, more transparent solution to exchange objects between Hibernate and GWT.</p>

<h4>How it works</h4>

<p>The first principle to Gilead is the automatic replacement of uninitialized proxies by null and persistent collections by basic collections present in the emulated JRE. These replacements are both done without requiring any specific mappings to be defined. Gilead also stores the information necessary to recreate the Hibernate proxy or persistent collection in either the server-side object or the cloned object, making it possible to recreate these objects without needing to make calls to the database.</p>

<div class="screenshot" align="center" width="80%"><img src="../images/gilead_diagram.png"/></div>

<p>Gilead also provides a dedicated adapter for Hibernate and GWT to make their integration painless.</p>

<p>In the simplest case, integration between GWT and Hibernate can be realized by following these steps:</p>

<ol>
<li>Make your persistent classes inherit the <code>LightEntity</code> class (class used for stateless mode integration in the Gilead library).</li>
<li>Making your remote RPC services extend <code><a href="http://gilead.svn.sourceforge.net/viewvc/gilead/gilead/branches/1.2/adapter4gwt/src/net/sf/gilead/gwt/PersistentRemoteService.java?revision=896&view=markup">PersistentRemoteService</a></code> instead of <code><a href="http://google-web-toolkit.googlecode.com/svn/javadoc/1.6/com/google/gwt/user/server/rpc/RemoteServiceServlet.html">RemoteServiceServlet</a></code>.</li>
<li>Configuring your beanManager for your GWT RPC service as shown in the code snippet below (see <a href="http://noon.gilead.free.fr/gilead/index.php?page=documentation">Gilead documentation</a> for more on configuring bean managers):

<pre class="code">
public class UserRemoteImpl extends PersistentRemoteService implements UserRemote {
  ...

  /**
   * Constructor
   */
  public UserRemoteImpl() {
    HibernateUtil hibernateUtil = new HibernateUtil();    
    hibernateUtil.setSessionFactory(HibernateUtil.getSessionFactory());

    PersistentBeanManager persistentBeanManager = new PersistentBeanManager();   
    persistentBeanManager.setPersistenceUtil(hibernateUtil); 
    persistentBeanManager.setProxyStore(new StatelessProxyStore());

    setBeanManager(persistentBeanManager);
  }
}
</pre></li></ol>

<p>Once this configuration is in place, our Hibernate entities are automatically converted into types that can be transferred over RPC and used in client-side GWT code, without any other coding or mapping needing to be defined on our part.</p>

<p>Applying these three changes to the base MusicStore application would lead to the following:</p>

<p>1) Make your persistent classes inherit <code>LightEntity</code></p>

<p><b>Account.java</b></p>

<pre class="code">
import net.sf.gilead.pojo.java5.LightEntity;

public class Account extends LightEntity implements Serializable {
  // ...
}
</pre>

<p><b>Record.java</b></p>

<pre class="code">
import net.sf.gilead.pojo.java5.LightEntity;

public class Account extends LightEntity implements Serializable {
  // ...
}
</pre>


<p>2) Making your remote RPC service extend <code>PersistentRemoteService</code></p>

<p><b>MusicStoreServiceImpl.java</b></p>

<pre class="code">
import net.sf.gilead.gwt.PersistentRemoteService;

public class MusicStoreServiceImpl extends PersistentRemoteService implements MusicStoreService {
  // ...
}
</pre> 

<p>3) Configure your <code>beanManager</code> for your GWT RPC service as shown in the code snippet above</p>

<p><b>MusicStoreServiceImpl.java</b></p>

<pre class="code">
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
</pre>

<p>There is a change to note here, aside from the new constructor that sets up the bean manager. We're now using <code>net.sf.gilead.core.hibernate.HibernateUtil</code> in addition to the <code>HibernateUtil</code> class we defined in the util package. This is required to setup Gilead appropriately.</p>

<p>And that's all there is to it. We're ready to go with the original calls we made from our GWT RPC service interfaces on the client-side referring the <code>Account</code> and <code>Record</code> objects. Try executing the command below to compile the application with the Gilead approach and see it running in hosted mode:</p>

<pre class="code">ant clean build hosted</pre>

<p>Once more, you can download a version of the sample application we've been building with the Gilead solution fully implemented <a href="http://google-web-toolkit.googlecode.com/files/gwt_hibernate_gilead.zip">here</a>.</p>

<h4>Transport annotations</h4>

<p>In order to avoid sending sensitive or heavy data over the network, Gilead also provides an <code>@ServerOnly</code> annotation that will exclude the property annotated in the cloned object. Also, if you don't want values that are changed in the clone on the GWT client-side to be reflected and persisted in the persistent entity, you can add a <code>@ReadOnly</code> annotation to properties as well.</p>

<h4>When you should use Gilead</h4>

<p>The main advantage to Gilead is transparency and developer productivity: once the configuration is in place, everything is taken care of to transfer and use your Hibernate entities as you would expect in the client-side (what we tried to do in our initial attempt to get <code>Account</code> and <code>Record</code> transferred over the RPC wire).</p>

<p>There is also a lot of support available on the <a href="http://sourceforge.net/forum/?group_id=239931">Gilead forum</a>, mainly from the actual author of the library, Bruno Marchesson. Having first been announcement two years ago, <a href="http://noon.gilead.free.fr/gilead/">Gilead</a> has matured and is renowned for its effectiveness for GWT and Hibernate integration.</p>

<p>However, Gilead does come with some disadvantages:</p>

<ul>
  <li>The initial project configuration is a crucial initial step that is sometimes difficult to get right for newcomers to the library. Much of the forum posts on the Gilead project are in fact related to issues with configuration and setup.</li>
  <li>The Gilead library works like a black box, much like Hibernate and GWT.</li>
  <li>The '<code>dynamic proxy</code>' feature is still in beta mode.</li>
</ul>

<h2 id="Conclusion">Conclusion</h2>

<p>If you're using Hibernate on the server-side, hopefully the integration strategies discussed above will help get your GWT client-side talking to your Hibernate backend. Each of these have their pluses and minuses, which vary especially with respect to the burden on the developer implementing the interoperation. However, the overarching concern across each of these strategies, as well as other facets of web application development, should always be performance for your users. A number of these approaches can sometimes incur considerable runtime overhead. For example, the Dozer and Gilead approach may become taxing to the user experience when there are larger sets of data to serialize, whereas the DTO solution can be designed to be as concise and effective as needed to improve performance.</p>

<p>There are other aspects of Hibernate and GWT integration that might not have been covered in this article. For any further discussions, I strongly encourage you to come visit us on the <a href="http://groups.google.com/group/Google-Web-Toolkit">GWT Developer Forum</a>.</p>


