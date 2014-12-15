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
==

If you're like us, the first thing you want to do is see examples of what you can do with GWT.

*   [Samples](#samples) are included in the SDK for you to play around with and build off of. A few are below. Some are from the [App Engine SDK](//developers.google.com/appengine/) that use GWT for their front end.
*   [Real world projects](#real-world-projects) let you see the real power of GWT, with complex applications that developers are building.

If you'd like to see who else is using GWT, check out the [GWT Gallery](http://gwtgallery.appspot.com). You'll be able to find other applications and libraries built with GWT, comment on them, rate them, and search for them by tag or by name. You can also [submit your own entry](http://gwtgallery.appspot.com/submit) if you have a project that  you want to share.

You can also find a wide variety of open source [projects related to GWT](http://code.google.com/hosting/search?q=GWT&btn=Search+Projects) hosted on Google Code.

Please note that the applications linked from this page are provided by third-parties and are not endorsed by Google.

## Samples<a id="samples"></a>

<div class="section">
  <div class="example">

  <div class="project">
      <div class="screenshot">
        <a href="http://samples.gwtproject.org/samples/Showcase/Showcase.html" target="_blank"><img src="images/showcase.jpg" alt="Screenshot"/></a>
      </div>
      <div class="info">
        <div class="name"><a href="http://samples.gwtproject.org/samples/Showcase/Showcase.html" target="_blank">GWT Showcase</a></div>
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
        <a href="http://samples.gwtproject.org/samples/Mail/Mail.html" target="_blank"><img src="images/mail.jpg" alt="Screenshot"/></a>
      </div>
      <div class="info">
        <div class="name"><a href="http://samples.gwtproject.org/samples/Mail/Mail.html" target="_blank">Mail Application</a></div>
        <div class="snippet">A replica of the UI of a desktop email application.</div>
        <div class="apis">
          <div class="head">GWT features used:</div>
          <div class="item"><a href="doc/latest/DevGuideUiWidgets.html">UI widgets</a></div>
          <div class="item"><a href="doc/latest/DevGuideCodingBasics.html#DevGuideHistory">History management</a></div>
        </div>
      </div>
    </div>

    <div class="project">
      <div class="screenshot">
        <a href="http://sticky.appspot.com" target="_blank"><img src="images/sticky.png" alt="Screenshot"/></a>
      </div>
      <div class="info">
        <div class="name"><a href="http://sticky.appspot.com" target="_blank">Sticky</a></div>
        <div class="snippet">A sticky note application built using GWT and Google App Engine. The source for this sample is included
          with the <a href="//developers.google.com/appengine/">App Engine SDK</a>. </div>
        <div class="apis">
          <div class="head">GWT features used:</div>
          <div class="item"><a href="doc/latest/DevGuideUiWidgets.html">UI widgets</a></div>
          <div class="item"><a href="doc/latest/DevGuideServerCommunication.html#DevGuideRemoteProcedureCalls">Polymorphic RPC</a></div>
        </div>
      </div>
    </div>

    <br>
    The source for these examples as well as others can be found in your GWT distribution under the `samples/` directory. If you haven't downloaded GWT yet, you can download it <a href="download.html">here</a>.

  </div>
</div>

## Real world projects<a id="real-world-projects"></a>

GWT is being used by tens of thousands of projects around the world. Take a look at just a few example of GWT in action:

<div class="project">
  <div class="screenshot">
    <a href="http://www.google.com/moderator" target="_blank"><img src="images/moderator.jpg" alt="Google Moderator"/></a>
  </div>
  <div class="info">
    <div class="name"><a href="http://www.google.com/moderator" target="_blank">Google Moderator</a></div>
    <div class="snippet">Google Moderator is a tool that allows distributed communities to submit and vote on questions for talks, 
presentations and events. Suggested questions as well as user votes are seen in real-time. Google Moderator's front-end is built
using GWT and runs on top of Google's App Engine.</div>
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
