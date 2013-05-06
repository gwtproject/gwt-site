<p>Ajax applications sometimes fail to meet user's expectations because they do not interact with the browser in the same way as static web pages. This is often apparent &mdash; and
frustrating for users &mdash; when an Ajax application does not integrate with browser history. For example, users expect browsers to be able to navigate back to previous pages visited
using back and forward actions. Because an Ajax application is a usually single page running JavaScript logic and not a series of pages, the browser history needs help from the
application to support this use case. Thankfully, GWT's history mechanism makes history support fairly straightforward.</p>

<ol class="toc" id="pageToc">
  <li><a href="#mechanism">The GWT History Mechanism</a></li>
  <li><a href="#tokens">History Tokens</a></li>
  <li><a href="#example">Example</a></li>
  <li><a href="#widgets">Hyperlink Widgets</a></li>
  <li><a href="#stateful">Stateful applications</a></li>
  <li><a href="#onvaluechange">Handling an onValueChange() callback</a></li>
</ol>


<h2 id="mechanism">The GWT History Mechanism</h2>

<p>GWT's History mechanism has a lot in common with other Ajax history implementations, such as <a href="http://code.google.com/p/reallysimplehistory">RSH (Really
Simple History)</a>. The basic premise is to keep track of the application's &quot;internal state&quot; in the url fragment identifier. This works because updating the fragment doesn't
typically cause the page to be reloaded.</p>

<p>This approach has several benefits:</p>

<ul>
<li>It's about the only way to control the browser's history reliably.</li>

<li>It provides good feedback to the user.</li>

<li>It's &quot;bookmarkable&quot;. I.e., the user can create a bookmark to the current state and save it, email it, et cetera.</li>
</ul>



<h2 id="tokens">History Tokens</h2>

<p>GWT includes a mechanism to help Ajax developers activate browser history. For each page that is to be navigable in the history, the application should generate a unique
history token. A token is simply a string that the application can parse to return to a particular state. This token will be saved in browser history as a URL fragment (in the
location bar, after the &quot;#&quot;), and this fragment is passed back to the application when the user goes back or forward in history, or follows a link.</p>

<p>For example, a history token named &quot;page1&quot; would be added to a URL as follows:</p>

<pre class="prettyprint">
http://www.example.com/com.example.gwt.HistoryExample/HistoryExample.html#page1
</pre>

<p>When the application wants to push a placeholder onto the browser's history stack, it simply invokes <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/user/client/History.html#newItem(java.lang.String)">History.newItem(token)</a>. When
the user uses the back button, a call will be made to any object that was added as a handler with <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/user/client/History.html#addValueChangeHandler(com.google.gwt.event.logical.shared.ValueChangeHandler)">History.addValueChangeHandler()</a>. It is up to the application to restore the state according to the value of the new token.</p>

<h2 id="example">Example</h2>

<p>To use GWT History support, you must first embed an iframe into your <a href="DevGuideOrganizingProjects.html#DevGuideHostPage">host HTML page</a>.</p>

<pre class="prettyprint">
  &lt;iframe src=&quot;javascript:''&quot;
          id=&quot;__gwt_historyFrame&quot;
          style=&quot;width:0;height:0;border:0&quot;&gt;&lt;/iframe&gt;
</pre>

<p>Then, in your GWT application, perform the following steps:</p>

<ul>
<li>Add a history token to the history stack when you want to enable a history event.</li>

<li>Create an object that implements the <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/event/logical/shared/ValueChangeHandler.html">ValueChangeHandler</a> interface, parses the new token (available by calling <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/event/logical/shared/ValueChangeEvent.html#getValue()">ValueChangeEvent.getValue()</a>) and changes the application state to match.</li>
</ul>

<p>The following short example shows how to add a history event each time the user selects a new tab in a <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/user/client/ui/TabPanel.html">TabPanel</a>.</p>

<pre class="prettyprint">
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;

/**
 * Entry point classes define &lt;code&gt;onModuleLoad()&lt;/code&gt;.
 */
public class BrowserHistoryExample implements EntryPoint {

  TabPanel tabPanel;
  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {
    tabPanel = new TabPanel();

    tabPanel.add(new HTML(&quot;&lt;h1&gt;Page 0 Content: Llamas&lt;/h1&gt;&quot;), &quot; Page 0 &quot;);
    tabPanel.add(new HTML(&quot;&lt;h1&gt;Page 1 Content: Alpacas&lt;/h1&gt;&quot;), &quot; Page 1 &quot;);
    tabPanel.add(new HTML(&quot;&lt;h1&gt;Page 2 Content: Camels&lt;/h1&gt;&quot;), &quot; Page 2 &quot;);

    tabPanel.addSelectionHandler(new SelectionHandler&lt;Integer&gt;(){
      public void onSelection(SelectionEvent&lt;Integer&gt; event) {
        History.newItem(&quot;page&quot; + event.getSelectedItem());
     );

    History.addValueChangeHandler(new ValueChangeHandler&lt;String&gt;() {
      public void onValueChange(ValueChangeEvent&lt;String&gt; event) {
        String historyToken = event.getValue();

        // Parse the history token
        try {
          if (historyToken.substring(0, 4).equals(&quot;page&quot;)) {
            String tabIndexToken = historyToken.substring(4, 5);
            int tabIndex = Integer.parseInt(tabIndexToken);
            // Select the specified tab panel
            tabPanel.selectTab(tabIndex);
          } else {
            tabPanel.selectTab(0);
          }

        } catch (IndexOutOfBoundsException e) {
          tabPanel.selectTab(0);
        }
      }
    });

    tabPanel.selectTab(0);
    RootPanel.get().add(tabPanel);
  }
}
</pre>

<h2 id="widgets">Hyperlink Widgets</h2>

<p>Hyperlinks are convenient to use to incorporate history support into an application. Hyperlink widgets are GWT widgets that look like regular HTML anchors. You can associate a
history token with the <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/user/client/ui/Hyperlink.html">Hyperlink</a>, and when it is
clicked, the history token is automatically added to the browser's history stack. The <tt>History.newItem(token)</tt> step is done automatically.</p>

<h2 id="stateful">Stateful applications</h2>

<p>Special care must be taken in handling history for applications that store state. Enough information must be coded into the history token to restore the application state back
to the point at which the history token was set. The application must also be careful to clear away any state not relevant to navigating back to a previously visited page.</p>

<p>As an example, an application that presents a multi-page questionnaire could encode the page number as a token as well as some other states. When a new page in the
questionnaire is presented, a history token is added to the history stack. Note that with stateful applications, such as a questionnaire, some careful thought needs to be given to
implementing the history callback. When returning to a page using a token, some logic needs to restore the previous state.</p>

<table>
<tr>
<td style="border: 1px solid #aaa; padding: 5px;">Token</td>
<td style="border: 1px solid #aaa; padding: 5px;">Action</td>
</tr>

<tr>
<td style="border: 1px solid #aaa; padding: 5px;">&quot;info&quot;</td>
<td style="border: 1px solid #aaa; padding: 5px;">Navigate to page where user enters biographic info. Restore previously entered data</td>
</tr>

<tr>
<td style="border: 1px solid #aaa; padding: 5px;">&quot;page1&quot;</td>
<td style="border: 1px solid #aaa; padding: 5px;">Navigate to page 1 in the questionnaire. Restore previous answers.</td>
</tr>

<tr>
<td style="border: 1px solid #aaa; padding: 5px;">&quot;page2&quot;</td>
<td style="border: 1px solid #aaa; padding: 5px;">Navigate to page 2 in the questionnaire. Restore previous answers.</td>
</tr>

<tr>
<td style="border: 1px solid #aaa; padding: 5px;">&quot;page&quot;

<pre>
<span class="error">&lt;n&gt;</span>
</pre>
</td>
<td style="border: 1px solid #aaa; padding: 5px;">Navigate to page

<pre>
<span class="error">&lt;n&gt;</span>
</pre>

...</td>
</tr>

<tr>
<td style="border: 1px solid #aaa; padding: 5px;">&quot;end&quot;</td>
<td style="border: 1px solid #aaa; padding: 5px;">Navigate to the end of the questionnaire. Validate that all questions were answered. Make sure not to re-submit the
questionnaire.</td>
</tr>
</table>



<p>In the above case, navigating back to a page would be possible, but there isn't enough information in the history token to restore the user's previous answers. A better
encoding for the token would be a syntax such as:</p>

<pre class="prettyprint">
  page=&lt;pagename&gt;;session=&lt;sessionname&gt;
</pre>

<p>Where <tt>&lt;pagename&gt;</tt> tells the application which page to go to and <tt>&lt;sessionname&gt;</tt> is a key to finding the user's previously entered data in a
database.</p>

<h2 id="onvaluechange">Handling an onValueChange() callback</h2>

<p>The first step of handling the <tt>onValueChange()</tt> callback method in a <tt>ValueChangeHandler</tt> is to get the new history token with <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/event/logical/shared/ValueChangeEvent.html#getValue()">ValueChangeEvent.getValue()</a>; you'll then want to parse the token. Keep in mind that your parsing needs to be robust! A user may type a URL by hand or have a URL
stored from an old version of your application. Once the token is parsed, you can reset the state of the application.</p>

<p>When the <tt>onValueChange()</tt> method is invoked, your application must handle two cases:</p>

<ol>
<li>The application was just started and was passed a history token.</li>

<li>The application is already running and was passed a history token.</li>
</ol>

<p>In the first case, the application must properly initialize itself before handing the state token. In the second case, some parts of the application may need to be
re-initialized.</p>


