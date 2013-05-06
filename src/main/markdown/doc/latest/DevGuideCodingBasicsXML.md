<p><a href="http://www.w3.org/XML/">Extensible Markup Language (XML)</a> is a data format commonly used in modern web applications. XML uses custom tags to describe
data and is encoded as plain text, making it both flexible and easy to work with. The GWT class library contains a set of types designed for processing XML data.</p>

<ol class="toc" id="pageToc">
  <li><a href="#types">XML types</a></li>
  <li><a href="#parsing">Parsing XML</a></li>
  <li><a href="#building">Building an XML document</a></li>
</ol>

<h2 id="types">XML types</h2>

<p>The XML types provided by GWT can be found in the <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/xml/client/package-summary.html">com.google.gwt.xml.client</a> package. In order to use these in your application, you'll need to add the following <tt>&lt;inherits&gt;</tt> tag to your <a href="DevGuideOrganizingProjects.html#DevGuideModuleXml">module XML file</a>:</p>

<pre class="prettyprint">
&lt;inherits name=&quot;com.google.gwt.xml.XML&quot; /&gt;
</pre>

<h2 id="parsing">Parsing XML</h2>

<p>To demonstrate how to parse XML with GWT, we'll use the following XML document that contains an email message:</p>

<pre class="prettyprint">
&lt;?xml version=&quot;1.0&quot; ?&gt;
&lt;message&gt;
  &lt;header&gt;
    &lt;to displayName=&quot;Richard&quot; address=&quot;rick@school.edu&quot; /&gt;
    &lt;from displayName=&quot;Joyce&quot; address=&quot;joyce@website.com&quot; /&gt;
    &lt;sent&gt;2007-05-12T12:03:55Z&lt;/sent&gt;
    &lt;subject&gt;Re: Flight info&lt;/subject&gt;
  &lt;/header&gt;
  &lt;body&gt;I'll pick you up at the airport at 8:30.  See you then!&lt;/body&gt;
&lt;/message&gt;
</pre>

<p>Suppose that you're writing an email application and need to extract the name of the sender, the subject line, and the message body from the XML. Here is sample code that will
do just that (we'll explain the code in just a bit):</p>

<pre class="prettyprint">
private void parseMessage(String messageXml) {
  try {
    // parse the XML document into a DOM
    Document messageDom = XMLParser.parse(messageXml);

    // find the sender's display name in an attribute of the &lt;from&gt; tag
    Node fromNode = messageDom.getElementsByTagName(&quot;from&quot;).item(0);
    String from = ((Element)fromNode).getAttribute(&quot;displayName&quot;);
    fromLabel.setText(from);

    // get the subject using Node's getNodeValue() function
    String subject = messageDom.getElementsByTagName(&quot;subject&quot;).item(0).getFirstChild().getNodeValue();
    subjectLabel.setText(subject);

    // get the message body by explicitly casting to a Text node
    Text bodyNode = (Text)messageDom.getElementsByTagName(&quot;body&quot;).item(0).getFirstChild();
    String body = bodyNode.getData();
    bodyLabel.setText(body);

  } catch (DOMException e) {
    Window.alert(&quot;Could not parse XML document.&quot;);
  }
}
</pre>

<p>The first step is to parse the raw XML text into an <a href="http://www.w3schools.com/dom/default.asp">XML DOM</a> structure we can use to navigate the data.
GWT's XML parser is contained in the <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/xml/client/XMLParser.html">XMLParser</a>
class. Call its <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/xml/client/XMLParser.html#parse(java.lang.String)">parse(String)</a> static method to parse the XML and return a <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/xml/client/Document.html">Document</a> object. If an error occurs during parsing (for
example, if the XML is not <a href="http://en.wikipedia.org/wiki/Well-formed_XML_document">well-formed</a>), the XMLParser will throw a <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/xml/client/DOMException.html">DOMException</a>.</p>

<p>If parsing succeeds, the Document object we receive represents the XML document in memory. It is a tree composed of generic <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/xml/client/Node.html">Node</a> objects. A node in the XML DOM is the basic unit of
data in an XML document. GWT contains several subinterfaces of Node which provide specialized methods for processing the various types of nodes:</p>

<ul>
<li><a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/xml/client/Element.html">Element</a> - represents DOM elements, which are
specified by tags in XML: <tt>&lt;someElement&gt;&lt;/someElement&gt;</tt>.</li>
</ul>

<ul>
<li><a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/xml/client/Text.html">Text</a> - represents the text between the opening and
closing tag of an element: <tt>&lt;someElement&gt;Here is some text.&lt;/someElement&gt;</tt>.</li>
</ul>

<ul>
<li><a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/xml/client/Comment.html">Comment</a> - represents an XML comment: <tt>&lt;!--
notes about this data --&gt;</tt>.</li>
</ul>

<ul>
<li><a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/xml/client/Attr.html">Attr</a> - represents an attribute of an element:
<tt>&lt;someElement myAttribute=&quot;123&quot; /&gt;</tt>.</li>
</ul>

<p>Refer to the documentation for the <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/xml/client/Node.html">Node</a> interface for
a complete list of types that derive from Node.</p>

<p>To get to the DOM nodes from the Document object, we can use one of three methods. The <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/xml/client/Document.html#getDocumentElement()">getDocumentElement()</a> method
retrieves the <i>document element</i> (the top element at the root of the DOM tree) as an <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/xml/client/Element.html">Element</a>. We can then use the navigation methods of the <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/xml/client/Node.html">Node</a> class from which Element derives (e.g., <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/xml/client/Node.html#getChildNodes()">getChildNodes()</a>, <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/xml/client/Node.html#getNextSibling()">getNextSibling()</a>, <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/xml/client/Node.html#getParentNode()">getParentNode()</a>, etc.) to drill down and
retrieve the data we need.</p>

<p>We can also go directly to a particular node or list of nodes using the <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/xml/client/Document.html#getElementById(java.lang.String)">getElementById(String)</a>
and <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/xml/client/Document.html#getElementsByTagName(java.lang.String)">getElementsByTagName(String)</a> methods. The getElementById(String) method will retrieve the Element with the specified ID. If you want to use ID's in your XML,
you'll need to supply the name of the attribute to use as the ID in the <a href="http://www.w3schools.com/dtd/default.asp">DTD</a> of the XML document (just setting
an attribute named <tt>id</tt> will not work). The getElementsByTagName(String) method is useful if you want to retrieve one or more elements with a particular tag name. The list
of elements will be returned in the form of a <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/xml/client/NodeList.html">NodeList</a> object, which can be iterated over to get the individual Nodes it contains.</p>

<p>In the example code, we use the getElementsByTagName(String) method to retrieve the necessary elements from the XML containing the email message. The sender's name is stored as
an attribute of the <tt>&lt;from&gt;</tt> tag, so we use <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/xml/client/Element.html#getAttribute(java.lang.String)">getAttribute(String)</a>. The
subject line is stored as text inside the <tt>&lt;subject&gt;</tt> tag, so we first find the subject element, and then retrieve its first (and only) child node and call <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/xml/client/Node.html#getNodeValue()">getNodeValue()</a> on it to get the text.
Finally, the message body is stored in the same way (text within the <tt>&lt;body&gt;</tt> tag), but this time we explicitly cast the <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/xml/client/Node.html">Node</a> to a <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/xml/client/Text.html">Text</a> object and extract the text using <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/xml/client/CharacterData.html#getData()">getData()</a>.</p>

<h2 id="building">Building an XML document</h2>

<p>In addition to parsing existing documents, the GWT XML types can also be used to create and modify XML. To create a new XML document, call the static <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/xml/client/XMLParser.html#createDocument()">createDocument()</a> method of the <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/xml/client/XMLParser.html">XMLParser</a> class. You can then use the methods of the
resulting <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/xml/client/Document.html">Document</a> to <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/xml/client/Document.html#createElement(java.lang.String)">create elements</a>, <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/xml/client/Document.html#createTextNode(java.lang.String)">text nodes</a>, and other XML nodes. These nodes can be added to the DOM tree using the <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/xml/client/Node.html#appendChild(com.google.gwt.xml.client.Node)">appendChild(Node)</a> and <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/xml/client/Node.html#insertBefore(com.google.gwt.xml.client.Node,%20com.google.gwt.xml.client.Node)">insertBefore(Node, Node)</a> methods. <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/xml/client/Node.html">Node</a> also has methods for replacing and removing child nodes (<a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/xml/client/Node.html#replaceChild(com.google.gwt.xml.client.Node,%20com.google.gwt.xml.client.Node)">replaceChild(Node, Node)</a> and <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/xml/client/Node.html#removeChild(com.google.gwt.xml.client.Node)">removeChild(Node)</a>, respectively).</p>


