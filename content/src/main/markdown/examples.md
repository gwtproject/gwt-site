<style>
    div.demo {
      padding: 3px;
      padding-left: 20px;
      background-image: url("images/newwindow.gif");
      background-repeat: no-repeat;
      background-position: left center;
    }

    ul.demo {
      margin-top: 20px;
      margin-bottom: 10px;
    }

    ul.demo li {
      list-style-image: url("images/newwindow.gif");
      list-style-type: disc;
    }

    .project {
      clear: both;
      margin-bottom: 1.25em;
      padding: 10px 0 10px 0;
    }

    .project .screenshot {
      float: left;
    }

    .project .name {
      font-size: medium;
      background-image: url("/webtoolkit/images/newwindow.gif");
      background-repeat: no-repeat;
      background-position: left center;
    }

    .project .screenshot img {
      width: 125px;
      height: 109px;
      border: 1px solid;
      margin-right: 10px;
    }

    .project .info .apis {
      margin-top: 0.5em;
    }

    .project .head {
      font-size: .8em;
    }

    .project .info .apis .item {
      margin-left: 10px;
      font-size: .8em;
    }

    .example {
      margin-top: 1em;
    }

    .project .snippet {
      font-size: .8em;
    }

    .project .snippet a {
      font-size: .8em;
    }
</style>

Examples
===

If you're like us, the first thing you want to do is see examples of what you can do with GWT.

*   [Samples](#samples) are included in the SDK for you to play around with and build off of. A few are below. Some are from the [App Engine SDK](https://developers.google.com/appengine/) that use GWT for their front end.
*   [Real world projects](#real-world-projects) let you see the real power of GWT, with complex applications that developers are building.

You can also find a wide variety of open source [projects related to GWT](https://github.com/search?q=GWT) hosted on Github.

Please note that the applications linked from this page are provided by third-parties and are not endorsed by Google.

## Samples<a id="samples"></a>

<div class="section">
  <div class="example">

  <div class="project">
    <div class="screenshot">
      <a href="https://samples.gwtproject.org/samples/Showcase/Showcase.html" target="_blank"><img src="images/showcase.jpg" alt="Screenshot"/></a>
    </div>
    <div class="info">
      <div class="name"><a href="https://samples.gwtproject.org/samples/Showcase/Showcase.html" target="_blank">GWT Showcase</a></div>
      <div class="snippet">A showcase of GWT features with accompanying source code and CSS styles.</div>
      <div class="apis">
        <div class="head">GWT features used:</div>
        <div class="item"><a href="doc/latest/DevGuideUiWidgets.html">UI widgets</a></div>
        <div class="item"><a href="doc/latest/DevGuideI18n.html">Internationalization</a></div>
      </div>
    </div>
  </div>

  <div class="project">
    <div class="screenshot">
      <a href="https://samples.gwtproject.org/samples/Mail/Mail.html" target="_blank"><img src="images/mail.jpg" alt="Screenshot"/></a>
    </div>
    <div class="info">
      <div class="name"><a href="https://samples.gwtproject.org/samples/Mail/Mail.html" target="_blank">Mail Application</a></div>
      <div class="snippet">A replica of the UI of a desktop email application.</div>
      <div class="apis">
        <div class="head">GWT features used:</div>
        <div class="item"><a href="doc/latest/DevGuideUiWidgets.html">UI widgets</a></div>
        <div class="item"><a href="doc/latest/DevGuideCodingBasics.html#DevGuideHistory">History management</a></div>
      </div>
    </div>
  </div>

  The source for these examples as well as others can be found in your GWT distribution under the `samples/` directory. If you haven't downloaded GWT yet, you can download it <a href="download.html">here</a>.

  </div>
</div>

## Real world projects<a id="real-world-projects"></a>

GWT is being used by tens of thousands of projects around the world. Take a look at just a few example of GWT in action:

<div class="project">
  <div class="screenshot">
    <a href="http://www.google.com/inbox" target="_blank"><img src="images/inbox.png" alt="Google Inbox"/></a>
  </div>
  <div class="info">
    <div class="name"><a href="http://www.google.com/inbox" target="_blank">Google Inbox</a></div>
    <div class="snippet">Google Inbox is a fresh start that goes beyond email to help you get back to what matters.
	Inbox is using a new approach to
	<a href="https://www.youtube.com/watch?feature=player_embedded&v=KdCs85jqcH0#t=747">delivering multiplatform native applications</a>
	using common code base. GWT is used to deliver the Web version.
	<p>Google is using GWT in number of other public services, including:
		<a href="https://docs.google.com/spreadsheets" target="_blank">Spreadsheets</a>,
		<a href="https://groups.google.com/forum/" target="_blank">Groups</a>,
		<a href="https://adwords.google.com " target="_blank">AdWords</a>,
		<a href="https://www.google.com/webmasters/tools/home" target="_blank">Webmaster Tools</a>,
		<a href="https://www.google.com/moderator/" target="_blank">Moderator</a>,
		<a href="https://www.blogger.com/" target="_blank">Blogger&nbsp;Admin</a>,
		<a href="https://www.google.com/wallet/" target="_blank">Wallet</a>,
		<a href="https://www.google.com/fonts/" target="_blank">WebFonts</a>,
		<a href="https://www.google.com/script/" target="_blank">Apps&nbsp;Script</a>,
		<a href="https://www.google.com/producer/" target="_blank">Currents&nbsp;Producer</a>,
		<a href="https://www.google.com/flights/" target="_blank">Flights</a>,
		<a href="https://www.google.com/fusiontables/DataSource?dsrcid=2049253" target="_blank">Fusion&nbsp;Tables</a>,
		<a href="https://www.google.com/hotels/" target="_blank">Hotel&nbsp;Finder</a>.
	</p>
 </div>
  </div>
</div>

<div class="project">
  <div class="screenshot">
    <a href="https://ruxit.com" target="_blank"><img src="images/ruxit.png" alt="ruxit"/></a>
  </div>
  <div class="info">
    <div class="name"><a href="https://ruxit.com" target="_blank">ruxit</a></div>
    <div class="snippet">ruxit is a cloud-based application performance management solution that offers unified monitoring of
user experience, servers, applications, infrastructure and networks &ndash; all with simple auto-configuration. The entire product UI
was built using GWT. Use the free trial to see it in action.</div>
  </div>
</div>

<div class="project">
  <div class="screenshot">
    <a href="https://www.cloudorado.com" target="_blank"><img src="images/cloudorado.png" alt="cloudorado"/></a>
  </div>
  <div class="info">
    <div class="name"><a href="https://www.cloudorado.com" target="_blank">Cloudorado</a></div>
    <div class="snippet">Cloudorado is a free cloud computing comparison tool, which makes cloud provider selection a quick and easy task.
	The interactive comparison is done fully client-side and it is written in GWT. No login required &ndash; just go and try it.</div>
  </div>
</div>

<div class="project">
  <div class="screenshot">
    <a href="http://gaestudio.arcbees.com" target="_blank"><img src="images/gaestudio.png" alt="Gae-Studio"/></a>
  </div>
  <div class="info">
    <div class="name"><a href="http://gaestudio.arcbees.com" target="_blank">Gae-Studio</a></div>
    <div class="snippet">GAE Studio helps you optimize applications hosted on Google App Engine. GAE Studio
    allows datastore exploration, modification, deletion, import and export. Think of it as your swiss army knife for developing GAE applications!</div>
  </div>
</div>

<div class="project">
  <div class="screenshot">
    <a href="https://getbookedin.com/?cid=256" target="_blank"><img src="images/bookedin.png" alt="BookedIN"/></a>
  </div>
  <div class="info">
    <div class="name"><a href="https://getbookedin.com/?cid=256" target="_blank">BookedIN</a></div>
    <div class="snippet">BookedIN is an online appointment booking, scheduling and payment system for small business.
      Customers can book and pay for appointments online, and the business owner can automate their internal booking
      process, reducing their administrative workload. BookedIN uses <a href="https://gwtp.arcbees.com">GWTP</a>. </div>
    </div>
</div>

<div class="project">
  <div class="screenshot">
    <a href="http://www.gogrid.com" target="_blank"><img src="images/go_grid.jpg" alt="Go Grid"/></a>
  </div>
  <div class="info">
    <div class="name"><a href="http://www.gogrid.com" target="_blank">Go Grid</a></div>
    <div class="snippet">GoGrid is a cloud computing infrastructure service provider which
enables you to deploy and scale load-balanced cloud server networks via a
unique multi-server control panel. Visit our <a href="developer_spotlight.html">Developer Spotlight</a> to see Justin Kitagawa, senior product manager and lead
developer on GoGrid, share how they used GWT to build GoGrid, what he
likes and would like to see from GWT, and his tips and learnings from
developing with GWT.</div>
  </div>
</div>

<div class="project">
  <div class="screenshot">
    <a href="https://www.scenechronize.com" target="_blank"><img src="images/scenechronize.jpg" alt="scenechronize"/></a>
  </div>
  <div class="info">
    <div class="name"><a href="https://www.scenechronize.com" target="_blank">Scenechronize</a></div>
    <div class="snippet">Scenechronize is a production management web application for film,
television and commercials. Visit our <a href="developer_spotlight.html">Developer Spotlight</a> to see Rob Powers, director of engineering, share
how they used GWT to build scenechronize, what he likes and would like to
see from GWT, and his tips and learnings from developing with GWT.</div>
  </div>
</div>

<div class="project">
  <div class="screenshot">
    <a href="http://www.whirled.com/" target="_blank"><img src="images/whirled.jpg" alt="Whirled"/></a>
  </div>
  <div class="info">
    <div class="name"><a href="http://www.whirled.com/" target="_blank">Whirled</a></div>
    <div class="snippet">Built by <a href="http://www.threerings.net">Three Rings</a>, Whirled
is a social virtual world website which includes multi-player games.
Visit our <a href="developer_spotlight.html">Developer Spotlight</a> to see Michael Bayne, CTO of Three Rings and lead engineer on Whirled, share
how they used GWT to build Whirled, what he likes and would like to see
from GWT, and his tips and learnings from developing with GWT.</div>
  </div>
</div>
