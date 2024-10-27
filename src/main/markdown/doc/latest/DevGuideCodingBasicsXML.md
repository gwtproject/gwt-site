XML
===

[Extensible Markup Language (XML)](http://www.w3.org/XML/) is a data format commonly used in modern web applications. XML uses custom tags to describe
data and is encoded as plain text, making it both flexible and easy to work with. The GWT class library contains a set of types designed for processing XML data.

1.  [XML types](#types)
2.  [Parsing XML](#parsing)
3.  [Building an XML document](#building)

## XML types<a id="types"></a>

The XML types provided by GWT can be found in the [com.google.gwt.xml.client](/javadoc/latest/com/google/gwt/xml/client/package-summary.html) package. In order to use these in your application, you'll need to add the following `<inherits>` tag to your [module XML file](DevGuideOrganizingProjects.html#DevGuideModuleXml):

```xml
<inherits name="com.google.gwt.xml.XML" />
```

## Parsing XML<a id="parsing"></a>

To demonstrate how to parse XML with GWT, we'll use the following XML document that contains an email message:

```xml
<?xml version="1.0" ?>
<message>
  <header>
    <to displayName="Richard" address="rick@school.edu" />
    <from displayName="Joyce" address="joyce@website.com" />
    <sent>2007-05-12T12:03:55Z</sent>
    <subject>Re: Flight info</subject>
  </header>
  <body>I'll pick you up at the airport at 8:30.  See you then!</body>
</message>
```

Suppose that you're writing an email application and need to extract the name of the sender, the subject line, and the message body from the XML. Here is sample code that will
do just that (we'll explain the code in just a bit):

```java
private void parseMessage(String messageXml) {
  try {
    // parse the XML document into a DOM
    Document messageDom = XMLParser.parse(messageXml);

    // find the sender's display name in an attribute of the <from> tag
    Node fromNode = messageDom.getElementsByTagName("from").item(0);
    String from = ((Element)fromNode).getAttribute("displayName");
    fromLabel.setText(from);

    // get the subject using Node's getNodeValue() function
    String subject = messageDom.getElementsByTagName("subject").item(0).getFirstChild().getNodeValue();
    subjectLabel.setText(subject);

    // get the message body by explicitly casting to a Text node
    Text bodyNode = (Text)messageDom.getElementsByTagName("body").item(0).getFirstChild();
    String body = bodyNode.getData();
    bodyLabel.setText(body);

  } catch (DOMException e) {
    Window.alert("Could not parse XML document.");
  }
}
```

The first step is to parse the raw XML text into an [XML DOM](http://www.w3schools.com/dom/default.asp) structure we can use to navigate the data.
GWT's XML parser is contained in the [XMLParser](/javadoc/latest/com/google/gwt/xml/client/XMLParser.html)
class. Call its [parse(String)](/javadoc/latest/com/google/gwt/xml/client/XMLParser.html#parse-java.lang.String-) static method to parse the XML and return a [Document](/javadoc/latest/com/google/gwt/xml/client/Document.html) object. If an error occurs during parsing (for
example, if the XML is not [well-formed](http://en.wikipedia.org/wiki/Well-formed_XML_document)), the XMLParser will throw a [DOMException](/javadoc/latest/com/google/gwt/xml/client/DOMException.html).

If parsing succeeds, the Document object we receive represents the XML document in memory. It is a tree composed of generic [Node](/javadoc/latest/com/google/gwt/xml/client/Node.html) objects. A node in the XML DOM is the basic unit of
data in an XML document. GWT contains several subinterfaces of Node which provide specialized methods for processing the various types of nodes:

*   [Element](/javadoc/latest/com/google/gwt/xml/client/Element.html) - represents DOM elements, which are specified by tags in XML: `<someElement></someElement>`.

*   [Text](/javadoc/latest/com/google/gwt/xml/client/Text.html) - represents the text between the opening and closing tag of an element: `<someElement>Here is some text.</someElement>`.

*   [Comment](/javadoc/latest/com/google/gwt/xml/client/Comment.html) - represents an XML comment: `<!-- notes about this data -->`.

*   [Attr](/javadoc/latest/com/google/gwt/xml/client/Attr.html) - represents an attribute of an element: `<someElement myAttribute="123" />`.

Refer to the documentation for the [Node](/javadoc/latest/com/google/gwt/xml/client/Node.html) interface for
a complete list of types that derive from Node.

To get to the DOM nodes from the Document object, we can use one of three methods. The [getDocumentElement()](/javadoc/latest/com/google/gwt/xml/client/Document.html#getDocumentElement--) method
retrieves the _document element_ (the top element at the root of the DOM tree) as an [Element](/javadoc/latest/com/google/gwt/xml/client/Element.html). We can then use the navigation methods of the [Node](/javadoc/latest/com/google/gwt/xml/client/Node.html) class from which Element derives (e.g., [getChildNodes()](/javadoc/latest/com/google/gwt/xml/client/Node.html#getChildNodes--), [getNextSibling()](/javadoc/latest/com/google/gwt/xml/client/Node.html#getNextSibling--), [getParentNode()](/javadoc/latest/com/google/gwt/xml/client/Node.html#getParentNode--), etc.) to drill down and
retrieve the data we need.

We can also go directly to a particular node or list of nodes using the [getElementById(String)](/javadoc/latest/com/google/gwt/xml/client/Document.html#getElementById-java.lang.String-)
and [getElementsByTagName(String)](/javadoc/latest/com/google/gwt/xml/client/Document.html#getElementsByTagName-java.lang.String-) methods. The getElementById(String) method will retrieve the Element with the specified ID. If you want to use ID's in your XML,
you'll need to supply the name of the attribute to use as the ID in the [DTD](http://www.w3schools.com/dtd/default.asp) of the XML document (just setting
an attribute named `id` will not work). The getElementsByTagName(String) method is useful if you want to retrieve one or more elements with a particular tag name. The list
of elements will be returned in the form of a [NodeList](/javadoc/latest/com/google/gwt/xml/client/NodeList.html) object, which can be iterated over to get the individual Nodes it contains.

In the example code, we use the getElementsByTagName(String) method to retrieve the necessary elements from the XML containing the email message. The sender's name is stored as
an attribute of the `<from>` tag, so we use [getAttribute(String)](/javadoc/latest/com/google/gwt/xml/client/Element.html#getAttribute-java.lang.String-). The
subject line is stored as text inside the `<subject>` tag, so we first find the subject element, and then retrieve its first (and only) child node and call [getNodeValue()](/javadoc/latest/com/google/gwt/xml/client/Node.html#getNodeValue--) on it to get the text.
Finally, the message body is stored in the same way (text within the `<body>` tag), but this time we explicitly cast the [Node](/javadoc/latest/com/google/gwt/xml/client/Node.html) to a [Text](/javadoc/latest/com/google/gwt/xml/client/Text.html) object and extract the text using [getData()](/javadoc/latest/com/google/gwt/xml/client/CharacterData.html#getData--).

## Building an XML document<a id="building"></a>

In addition to parsing existing documents, the GWT XML types can also be used to create and modify XML. To create a new XML document, call the static [createDocument()](/javadoc/latest/com/google/gwt/xml/client/XMLParser.html#createDocument--) method of the [XMLParser](/javadoc/latest/com/google/gwt/xml/client/XMLParser.html) class. You can then use the methods of the
resulting [Document](/javadoc/latest/com/google/gwt/xml/client/Document.html) to [create elements](/javadoc/latest/com/google/gwt/xml/client/Document.html#createElement-java.lang.String-), [text nodes](/javadoc/latest/com/google/gwt/xml/client/Document.html#createTextNode-java.lang.String-), and other XML nodes. These nodes can be added to the DOM tree using the [appendChild(Node)](/javadoc/latest/com/google/gwt/xml/client/Node.html#appendChild-com.google.gwt.xml.client.Node-) and [insertBefore(Node, Node)](/javadoc/latest/com/google/gwt/xml/client/Node.html#insertBefore-com.google.gwt.xml.client.Node-com.google.gwt.xml.client.Node-) methods. [Node](/javadoc/latest/com/google/gwt/xml/client/Node.html) also has methods for replacing and removing child nodes ([replaceChild(Node, Node)](/javadoc/latest/com/google/gwt/xml/client/Node.html#replaceChild-com.google.gwt.xml.client.Node-com.google.gwt.xml.client.Node-) and [removeChild(Node)](/javadoc/latest/com/google/gwt/xml/client/Node.html#removeChild-com.google.gwt.xml.client.Node-), respectively).
