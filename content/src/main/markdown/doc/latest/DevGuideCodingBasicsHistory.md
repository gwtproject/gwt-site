History
===

Ajax applications sometimes fail to meet user's expectations because they do not interact with the browser in the same way as static web pages. This is often apparent &mdash; and
frustrating for users &mdash; when an Ajax application does not integrate with browser history. For example, users expect browsers to be able to navigate back to previous pages visited
using back and forward actions. Because an Ajax application is a usually single page running JavaScript logic and not a series of pages, the browser history needs help from the
application to support this use case. Thankfully, GWT's history mechanism makes history support fairly straightforward.

1.  [The GWT History Mechanism](#mechanism)
2.  [History Tokens](#tokens)
3.  [Example](#example)
4.  [Hyperlink Widgets](#widgets)
5.  [Stateful applications](#stateful)
6.  [Handling an onValueChange() callback](#onvaluechange)

## The GWT History Mechanism<a id="mechanism"></a>

GWT's History mechanism has a lot in common with other Ajax history implementations, such as [RSH (Really
Simple History)](http://code.google.com/p/reallysimplehistory). The basic premise is to keep track of the application's "internal state" in the url fragment identifier. This works because updating the fragment doesn't
typically cause the page to be reloaded.

This approach has several benefits:

*   It's about the only way to control the browser's history reliably.
*   It provides good feedback to the user.
*   It's "bookmarkable". I.e., the user can create a bookmark to the current state and save it, email it, et cetera.

## History Tokens<a id="tokens"></a>

GWT includes a mechanism to help Ajax developers activate browser history. For each page that is to be navigable in the history, the application should generate a unique
history token. A token is simply a string that the application can parse to return to a particular state. This token will be saved in browser history as a URL fragment (in the
location bar, after the "#"), and this fragment is passed back to the application when the user goes back or forward in history, or follows a link.

For example, a history token named "page1" would be added to a URL as follows:

```text
http://www.example.com/com.example.gwt.HistoryExample/HistoryExample.html#page1
```

When the application wants to push a placeholder onto the browser's history stack, it simply invokes [History.newItem(token)](/javadoc/latest/com/google/gwt/user/client/History.html#newItem-java.lang.String-). When
the user uses the back button, a call will be made to any object that was added as a handler with [History.addValueChangeHandler()](/javadoc/latest/com/google/gwt/user/client/History.html#addValueChangeHandler-com.google.gwt.event.logical.shared.ValueChangeHandler-). It is up to the application to restore the state according to the value of the new token.

## Example<a id="example"></a>

To use GWT History support, you must first embed an iframe into your [host HTML page](DevGuideOrganizingProjects.html#DevGuideHostPage).

```html
<iframe src="javascript:''"
          id="__gwt_historyFrame"
          style="position:absolute;width:0;height:0;border:0"></iframe>
```

Then, in your GWT application, perform the following steps:

*   Add a history token to the history stack when you want to enable a history event.
*   Create an object that implements the [ValueChangeHandler](/javadoc/latest/com/google/gwt/event/logical/shared/ValueChangeHandler.html) interface, parses the new token (available by calling [ValueChangeEvent.getValue()](/javadoc/latest/com/google/gwt/event/logical/shared/ValueChangeEvent.html#getValue--)) and changes the application state to match.

The following short example shows how to add a history event each time the user selects a new tab in a [TabPanel](/javadoc/latest/com/google/gwt/user/client/ui/TabPanel.html).

```java
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
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class BrowserHistoryExample implements EntryPoint {

  TabPanel tabPanel;
  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {
    tabPanel = new TabPanel();

    tabPanel.add(new HTML("<h1>Page 0 Content: Llamas</h1>"), " Page 0 ");
    tabPanel.add(new HTML("<h1>Page 1 Content: Alpacas</h1>"), " Page 1 ");
    tabPanel.add(new HTML("<h1>Page 2 Content: Camels</h1>"), " Page 2 ");

    tabPanel.addSelectionHandler(new SelectionHandler<Integer>(){
      public void onSelection(SelectionEvent<Integer> event) {
        History.newItem("page" + event.getSelectedItem());
     );

    History.addValueChangeHandler(new ValueChangeHandler<String>() {
      public void onValueChange(ValueChangeEvent<String> event) {
        String historyToken = event.getValue();

        // Parse the history token
        try {
          if (historyToken.substring(0, 4).equals("page")) {
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
```

## Hyperlink Widgets<a id="widgets"></a>

Hyperlinks are convenient to use to incorporate history support into an application. Hyperlink widgets are GWT widgets that look like regular HTML anchors. You can associate a
history token with the [Hyperlink](/javadoc/latest/com/google/gwt/user/client/ui/Hyperlink.html), and when it is
clicked, the history token is automatically added to the browser's history stack. The `History.newItem(token)` step is done automatically.

## Stateful applications<a id="stateful"></a>

Special care must be taken in handling history for applications that store state. Enough information must be coded into the history token to restore the application state back
to the point at which the history token was set. The application must also be careful to clear away any state not relevant to navigating back to a previously visited page.

As an example, an application that presents a multi-page questionnaire could encode the page number as a token as well as some other states. When a new page in the
questionnaire is presented, a history token is added to the history stack. Note that with stateful applications, such as a questionnaire, some careful thought needs to be given to
implementing the history callback. When returning to a page using a token, some logic needs to restore the previous state.

| Token | Action                                                                                                                              |
| ------| ----------------------------------------------------------------------------------------------------------------------------------- |
| info  | Navigate to page where user enters biographic info. Restore previously entered data                                                 |
| page1 | Navigate to page 1 in the questionnaire. Restore previous answers.                                                                  |
| page2 | Navigate to page 2 in the questionnaire. Restore previous answers.                                                                  |
| pageN | Navigate to page _N_ ...                                                                                                            |
| end   | Navigate to the end of the questionnaire. Validate that all questions were answered. Make sure not to re-submit the questionnaire.  |

In the above case, navigating back to a page would be possible, but there isn't enough information in the history token to restore the user's previous answers. A better
encoding for the token would be a syntax such as:

```text
page=<pagename>;session=<sessionname>
```

Where `<pagename>` tells the application which page to go to and `<sessionname>` is a key to finding the user's previously entered data in a
database.

## Handling an onValueChange() callback<a id="onvaluechange"></a>

The first step of handling the `onValueChange()` callback method in a `ValueChangeHandler` is to get the new history token with [ValueChangeEvent.getValue()](/javadoc/latest/com/google/gwt/event/logical/shared/ValueChangeEvent.html#getValue--); you'll then want to parse the token. Keep in mind that your parsing needs to be robust! A user may type a URL by hand or have a URL
stored from an old version of your application. Once the token is parsed, you can reset the state of the application.

When the `onValueChange()` method is invoked, your application must handle two cases:

1.  The application was just started and was passed a history token.
2.  The application is already running and was passed a history token.

In the first case, the application must properly initialize itself before handing the state token. In the second case, some parts of the application may need to be
re-initialized.
