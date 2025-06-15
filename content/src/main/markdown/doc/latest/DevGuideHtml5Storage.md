HTML5 Storage
===

1.  [What is HTML5 Storage?](#WhatIsStorage)
2.  [Why Use HTML5 Storage?](#WhyUse)
3.  [Details You Should Know About HTML5 Storage](#Overview)
4.  [HTML5 Storage Support in GWT](#GwtStorage)
5.  [How to Use  HTML5 Storage in Your Web Application](#UsingStorage)

Starting with GWT 2.3, the GWT SDK provides support for HTML5 client-side storage, also known as "web storage". Using the GWT library's Storage capability enables your web application to take advantage of these storage features when running in an HTML5-compliant browser.

## What is HTML5 Storage?<a id='WhatIsStorage'></a>

The HTML5 (web) storage spec is a standardized way of providing larger amounts of client-side storage and of more appropriately "partitioning" session storage and locally persistent storage. The HTML5 spec also provides for storage events to be generated and handled by interested listeners. The full impact of these features provided by HTML5 storage can best be seen by looking at client-side storage in the non-HTML5 world. 

Without HTML5, client-side storage for web applications is limited to the tiny storage provided by cookies (4KB per cookie, 20 cookies per domain) unless proprietary storage schemes are used, such as Flash local shared objects or Google Gears. If cookies are used they provide both session and locally persistent storage at the same time, and are accessible by all browser windows and tabs. Domain cookies are sent with every request to that domain, which consumes bandwidth. The "mechanics" of processing cookies are also a bit cumbersome. 

In contrast, HTML5 storage provides a much larger initial local storage (5MB per domain), unlimited session storage (limited only by system resources) and successfully partitions local and session storage so that only the data you want to persist is persisted in local storage and data you want to be transient stays transient.  Moreover, session storage is only accessible from its originating tab or window; it is not shared between all browser windows and tabs. Accessing session and local storage is simple, consisting in simple reads and writes of key-value strings. Finally, local and session storage are client-side only; they are not sent with requests. 

## Why Use HTML5 Storage?<a id='WhyUse'></a>

With HTML5 local storage, a larger amount of data (initially, 5MB per application per browser) can be persistently cached client-side, which provides an alternative to server downloads.   A web application can achieve better performance and provide a better user experience if it uses this local storage.  For example, your web application can use local storage to cache data from RPC calls for faster startup times and for a more responsive user interface.  Other uses include saving the application state locally for a faster restore when the user re-enters the application, and saving the user's work if there is a network outage, and so forth.

**Note:** The 5MB maximum applies to local storage only, not to session storage, which is limited only by system memory.

Here is a short list of some of the benefits and uses of local storage:

*   Reduce network traffic
*   Significantly speed up display times
*   Cache data from RPC calls
*   Load cached data on startup (faster startup)
*   Save temporary state
*   Restore state upon app reentry
*   Prevent work loss from network disconnects

**Note:** unlike cookies, items in Storage are not sent along in requests, which helps reduce network traffic.

## Details You Should Know About HTML5 Storage<a id="Overview"></a>

To use HTML5 storage features, you  need to know about lifespan (persistence) of local and session storage, about their scope &mdash; which windows and tabs can access the storage, and which tabs and windows can listen for storage events.

### LocalStorage and SessionStorage

HTML5 Web Storage defines two types of key-value storage types: sessionStorage and localStorage. The primary behavioral difference is how long the values persist and how they are shared. The following table shows the differences between the two types of storage.

| Storage Type   | Max Size                                                                                                                                                                                                                        | Persistence                                                | Availability to other Windows/tabs                                     | Data Type Supported             |
| -------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ---------------------------------------------------------- | ---------------------------------------------------------------------- | ------------------------------- |
| LocalStorage   | 5MB per app per browser. According to the [HTML5 spec](http://www.w3.org/TR/2009/WD-webstorage-20091222/#the-storage-interface), this limit can be increased by the user when needed; however, only a few browsers support this | On disk until deleted by user (delete cache) or by the app | Shared across every window and tab of one browser running same web app | String only, as key-value pairs |
| SessionStorage | Limited only by system memory                                                                                                                                                                                                   | Survives only as long as its originating window or tab     | Accessible only within the window or tab that created it               | String only, as key-value pairs |

### How Local Storage Is Shared by the Browser

One LocalStorage per web application, with a max size of 5MB, is available for a given browser and is shared by all windows and tabs of that browser.  For example, suppose you have MyWebApp running in a Chrome browser on the client. If you run MyWebApp in multiple tabs and windows, they all share the same LocalStorage data , subject to a max limit of 5MB.  If you were to then open that same application in another browser, say FireFox, then the new browser would get its own LocalStorage to share with all its own tabs and windows. This is shown in the following figure:

![GWT Local Storage](images/localStorage.png) 

### Local Storage is String Storage 

HTML5 local storage saves data in string form as key-value pairs.  If the data you wish to save is not string data, you are responsible for conversion to and from string when using LocalStorage. For proxy classes used with the GWT RequestFactory, you can use RequestFactory#getSerializer() to do string serializing. For non-proxy objects, you could use [JSON stringify and parse](http://www.json.org/js.html).

**Note: **Just like cookies, LocalStorage and sessionStorage can be inspected using browser tools such as Developer Tools in Chrome,  Web Inspector in Safari and so forth.  These tools allow a user to remove storage values and see what values are being recorded by a web site the user is visiting.

### LocalStorage is Not Secure Storage

HTML5 local storage saves data unencrypted in string form in the regular browser cache. It is not secure storage. It should not be used for sensitive data, such as social security numbers, credit card numbers, logon credentials, and so forth.

### How Storage Events Work

When data is added to, modified, or removed from LocalStorage or SessionStorage, a StorageEvent is fired within the current browser tab or window. That Storage event contains the storage object in which the event occurred, the URL of the document to which this storage applies, and both the old and the new values of the key that was changed.  Any listener registered for this event can handle it.

**Note: **Although the HTML5 spec calls for Storage events to be fired in all tabs of the same browser or all windows of the same browser, few browsers currently implement this.

## HTML5 Storage Support in GWT<a id="GwtStorage"></a>

GWT support for the HTML5 storage feature consists of the following:

*   com.google.gwt.storage.client.Storage (required import)
*   LocalStorage (local storage)
*   SessionStorage (session storage)
*   StorageEvent (event generated by session or local storage changes)
*   StorageEvent.Handler (interface for storage event handlers)
*   StorageMap (exposes the Storage object as a standard Map)

Syntactic details for these can be found in the [Storage feature javadoc](/javadoc/latest/com/google/gwt/storage/client/package-summary.html).

## How to Use  HTML5 Storage in Your Web Application<a id='UsingStorage'></a>

You get the storage object by invoking Storage.getLocalStorageIfSupported() or Storage.getSessionStorageIfSupported(), depending on the type of storage you want to access.

Because your web app might be accessed from a browser that does not support HTML5, you should always check before accessing any of the HTML5 storage features.
 
If the storage feature is supported, you get the storage object and then write data to it or read data from it, depending on your needs. If you want to delete one key-value pair from the storage, you can do that or you can clear all of the data from the storage object. 

1.  [Check for browser support](#BrowserSupport)
2.  [Get the Storage object for your browser](#GetStorage)
3.  [Read data from Storage](#ReadStorage)
4.  [Write data to Storage](#WriteStorage)
5.  [Delete data from  Storage ](#DelStorage)
6.  [Handle Storage Events](#HandleEvents)

### Checking for Browser Support<a id='BrowserSupport'></a>

GWT provides a simple way to determine whether the browser supports HTML5 storage &mdash; a built-in check when you get the storage object. You use Storage.getLocalStorageIfSupported() or Storage.getSessionStorageIfSupported(), depending on which type of storage you want to use. The storage object is returned if the feature is supported, or, if not, null is returned.

```java
import com.google.gwt.storage.client.Storage;
  private Storage stockStore = null;
  stockStore = Storage.getLocalStorageIfSupported();
```

### Getting the Storage Object<a id='GetStorage'></a>

If the browser supports HTML5 storage, the Storage.getLocalStorageIfSupported method creates the storage object if it doesn't exist yet, and returns the object. If the storage object already exists, it simply returns the already-existing object. If the browser doesn't support HTML5 storage, this returns null. The Storage.getSessionStorageIfSupported() method works in the same way. 

Getting the storage object and checking for browser support of HTML5 storage are done at the same time, so the code snippet for doing this should look familiar:

```java
import com.google.gwt.storage.client.Storage;
  private Storage stockStore = null;
  stockStore = Storage.getLocalStorageIfSupported();
```

### Reading Data from Storage<a id='ReadStorage'></a>

Data is stored as key-value string pairs, so you need to use the key to get the data. You either have to know what the key is, or you'll need to iterate through the storage using indexes to get keys. Picking good naming conventions for keys can help, and the use of StorageMap can be useful as well. If the data needs to be converted from string, you are responsible for doing that.

The following snippet shows an iteration through the contents of storage, with each item in storage then being written to a separate row in a FlexTable. For simplicity, this assumes all of storage is used only for that FlexTable.

```java
import com.google.gwt.storage.client.Storage;

private FlexTable stocksFlexTable = new FlexTable();
private Storage stockstore = null;

stockStore = Storage.getLocalStorageIfSupported();
if (stockStore != null){
  for (int i = 0; i < stockStore.getLength(); i++){
    String key = stockStore.key(i);
    stocksFlexTable.setText(i+1, 0, stockStore.getItem(key));
    stocksFlexTable.setWidget(i+1, 2, new Label());
  }
}
```

#### Using StorageMap to do a Quick Check for Specific Key or Value

If you want to quickly check whether a specific key or a specific value is present in the storage, you can use the StorageMap object by supplying the storage to the StorageMap constructor, then using its `containsValue()` or `containsKey()` methods.

In the following snippet, we use a StorageMap to see if a certain value is already in the storage, and if it is not yet stored, we write the data to storage.

```java
stockStore = Storage.getLocalStorageIfSupported();
if (stockStore != null) {
  stockMap = new StorageMap(stockStore);
  if (stockMap.containsValue(symbol)!= true){
    int numStocks = stockStore.getLength();
    stockStore.setItem("Stock."+numStocks, symbol);
}
```

### Writing Data to Storage<a id='WriteStorage'></a>

To write data, you supply a key name and the string value you wish to save. You can only write string data, so you need to do any conversions from other data types or from objects. (If the object is a proxy used with the GWT RequestFactory, you can use RequestFactory#getSerializer() to do string serializing. For non-proxy objects, you could use [JSON stringify and parse](http://www.json.org/js.html).).

Judicious use of naming conventions can help with processing storage data. For example, in a web app named MyWebApp,  key-value data associated with rows in a  UI table named Stock could have key names prefixed with MyWebApp.Stock.

In the following snippet, which is part of an Add button click handler, a text value is read from a textbox and saved, with the key name concatenated from a prefix and the current number of items in the storage.

```java
import com.google.gwt.storage.client.Storage;

final String symbol = newSymbolTextBox.getText().toUpperCase().trim();
stockStore = Storage.getLocalStorageIfSupported();
if (stockStore != null) {
  int numStocks = stockStore.getLength();
  stockStore.setItem("Stock."+numStocks, symbol);
}
```

### Deleting Data from Storage<a id='DelStorage'></a>

You can delete a single key-value pair of data from the storage or you can delete all the data all at once.

#### Deleting a Specific Key-Value Pair

If you want to delete specific piece of data and you know the key name, you simply supply the key name to the removeItem method like this: `myStorage.removeItem(myKey);`  

If you don't know the key, or need to process a list of keys, you can iterate through the storage using the `key` method, like this: `myStorage.key(myIndexValue);` 

#### Clearing the Entire Storage

To clear the storage used by your web app, invoke the `clear()` method, like this: `myStorage.clear();` 

The following sample snippet provides an example of one way to integrate this method with a UI, in this case a FlexTable that displays items from the storage. The user clears the UI and the storage by clicking on a Clear All button. In the button-click handler we just use the count of items in the storage to iterate through and remove rows from the UI, and when that is done, we delete all the storage data. (To keep things simple, we used the storage only for populating the FlexTable.)

```java
import com.google.gwt.storage.client.Storage;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

// Listen for mouse events on the Clear all button.
clearAllButton.addClickHandler(new ClickHandler() {
  public void onClick(ClickEvent event) {
  // note that in general, events can have sources that are not Widgets.
  Widget sender = (Widget) event.getSource();
  //If HTML5 storage is supported, clear all rows from the FlexTable UI,
  //then clear storage
  if (sender == clearAllButton) {
    stockStore = Storage.getLocalStorageIfSupported();
    if (stockStore !=null) {
      for (int ix =0; ix < stockStore.getLength(); ix++) {
        stocksFlexTable.removeRow(1);
      }

      stockStore.clear();}
    }
  } // if sender is the clear all button
});
```

### Handling Storage Events<a id='HandleEvents'></a>

You can register storage event handlers with a storage object, and these are invoked **for the current window or tab** when data is written to or deleted from the storage. Although the HTML5 spec states that storage events fire in all windows and tabs of the browser, this should not be assumed because few browsers implement this. Notice that if the storage is cleared, the event does not contain any information about the deleted key-value pairs.

The storage event handlers get a storage event object that contains various useful information, such as the old value and the new value, in the case of an update to an existing key-value pair. The following can be obtained from the StorageEvent object:

| Method         | Description                                                                                                                   |
| -------------- | ----------------------------------------------------------------------------------------------------------------------------- |
| getKey         | Returns the key being changed.                                                                                                |
| getNewValue    | Returns the value of the key after the change, or null if not changed or if it is the result of a Storage.clear() operation.  |
| getOldValue    | Returns the value of the key before the change, or null if not changed or if it is the result of a Storage.clear() operation. |
| getStorageArea | Returns the SessionStorage or LocalStorage object where the event occurred.                                                   |
| getURL         | The address of the document in which the change occurred.                                                                     |

The following snippet shows a sample event handler registered with a storage, where the changes from the incoming events are displayed in a UI label.

```java
import com.google.gwt.storage.client.Storage;
import com.google.gwt.storage.client.StorageEvent;
private Storage stockstore = null;
stockStore = Storage.getLocalStorageIfSupported();
if (stockStore != null) {
  stockStore.addStorageEventHandler(new StorageEvent.Handler() {
  public void onStorageChange(StorageEvent event) {
    lastStockLabel.setText("Last Update: "+event.getNewValue() +": " +event.getOldValue() +": " +event.getUrl());
  }
});
```

