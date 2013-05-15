    <style type="text/css">
    
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
</style>

    .project {
      position: relative;
      height: 109px;
      margin-bottom: 1.25em;
    }

    .project .screenshot,logo {
      position: absolute;
      top: 0;
      left: 0;
    }

    .project .name {
      font-size: medium;
      padding: 3px;
      padding-left: 20px;
      background-image: url("/webtoolkit/images/newwindow.gif");
      background-repeat: no-repeat;
      background-position: left center;
    }

    .project .info {
      position: absolute;
      top: 0;
      left: 140px;
    }

    .project .screenshot img {
      width: 125px;
      height: 109px;
      border: 1px solid;
    }

    .project .info .apis {
      margin-top: 0.5em;
    }

    .project .info .apis .item {
      margin-left: 10px;
    }
    
    .example {
      margin-top: 1em;
    }
    
    </style>

<p>
If you're like us, the first thing you want to do is see examples of what you can do with GWT.

<ul>
  <li><a href="#samples">Samples</a> are included in the SDK for you to play around with and build off of. A few are below. Some are from the <a href="/appengine/">App Engine SDK</a> that use GWT for their front end.</li>
  <li><a href="#real-world-projects">Real world projects</a> let you see the real power of GWT, with complex applications that developers are building.</li>
</ul>

<p>If you'd like to see who else is using GWT, check out the <a href="http://gwtgallery.appspot.com">GWT Gallery</a>. You'll be able to find other applications and libraries built with GWT, comment on them, rate them, and search for them by tag or by name. You can also <a href="http://gwtgallery.appspot.com/submit">submit your own entry</a> if you have a project that  you want to share.</p>
<p>You can also find a wide variety of open source <a href="http://code.google.com/hosting/search?q=GWT&btn=Search+Projects"> projects related to GWT</a> hosted on Google Code.</p>
<p>Please note that the applications linked from this page are provided by third-parties and are not endorsed by Google.</p>
</p>

<h2 id="samples">Samples</h2>
<div class="section">
  <div class="example">

  <div class="project">
      <div class="screenshot">
        <a href="http://gwt.google.com/samples/Showcase/Showcase.html" target="_blank"><img src="images/showcase.jpg" alt="Screenshot"/></a>
      </div>
      <div class="info">
        <div class="name"><a href="http://gwt.google.com/samples/Showcase/Showcase.html" target="_blank">GWT Showcase</a></div>
        <div class="snippet">A showcase of GWT features with accompanying source code and CSS styles</div>
        <div class="apis">
          <div class="head">GWT features used:</div>
          <div class="item"><a href="doc/latest/DevGuideUiWidgets.html">UI widgets</a></div>
          <div class="item"><a href="doc/latest/DevGuideI18n.html">Internationalization</a></div>
        </div>
      </div>
    </div>  

    <div class="project">
      <div class="screenshot">
        <a href="http://gwt.google.com/samples/Mail/Mail.html" target="_blank"><img src="images/mail.jpg" alt="Screenshot"/></a>
      </div>
      <div class="info">
        <div class="name"><a href="http://gwt.google.com/samples/Mail/Mail.html" target="_blank">Mail Application</a></div>
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
        <a href="http://gwt.google.com/samples/DynaTable/DynaTable.html" target="_blank"><img src="images/dynamictable.jpg" alt="Screenshot"/></a>
      </div>
      <div class="info">
        <div class="name"><a href="http://gwt.google.com/samples/DynaTable/DynaTable.html" target="_blank">Dynamic Table</a></div>
        <div class="snippet">A table of data that demonstrates paging over objects retrieved through RPCs.</div>
        <div class="apis">
          <div class="head">GWT features used:</div>
          <div class="item"><a href="doc/latest/DevGuideUiWidgets.html">UI widgets</a></div>
          <div class="item"><a href="doc/latest/DevGuideServerCommunication#DevGuideRemoteProcedureCalls.html">Polymorphic RPC</a></div>
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
          with the <a href="/appengine/">App Engine SDK</a>. </div>
        <div class="apis">
          <div class="head">GWT features used:</div>
          <div class="item"><a href="doc/latest/DevGuideUiWidgets.html">UI widgets</a></div>
          <div class="item"><a href="doc/latest/DevGuideServerCommunication.html#DevGuideRemoteProcedureCalls">Polymorphic RPC</a></div>
        </div>
      </div>
    </div>

    <br>
    The source for these examples as well as others can be found in your GWT distribution under the <code>samples/</code> directory. If you haven't downloaded GWT yet, you can download it <a href="download.html">here</a>.

  </div>
</div>


<h2 id="real-world-projects">Real world projects</h2>
<p>GWT is being used by tens of thousands of projects around the world. Take a look at just a few example of GWT in action:</p>
<br/>

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
    <!--
    <br/>
    <div><a href="http://www.youtube.com/watch?v=3dMrILwtiMI">GWT Developers - GoGrid</a></div>
    -->
  </div>
</div>

<div class="project">
  <div class="screenshot">
    <a href="https://blueprint.lombardi.com/index.html" target="_blank"><img src="images/lombardi_blueprint.jpg" alt="Lombardi Blueprint"/></a>
  </div>
  <div class="info">
    <div class="name"><a href="https://blueprint.lombardi.com/index.html" target="_blank">Lombardi Blueprint</a></div>
    <div class="snippet">Lombardi Blueprint is a business process mapping tool. Visit our <a href="developer_spotlight.html">Developer Spotlight</a> to see Alex Moffat,
chief engineer for Lombardi Blueprint, share how they used GWT to build
Blueprint, what he likes and would like to see from GWT, and his tips and
learnings from developing with GWT.</div>
    <!--
    <br/>
    <div><a href="http://www.youtube.com/watch?v=7J2u6QZIXsA">GWT Developers - Lombardi Blueprint</a></div>
    -->
  </div>
</div>

<div class="project">
  <div class="screenshot">
    <a href="https://www.scenechronize.com" target="_blank"><img src="images/scenechronize.jpg" alt="scenechronize"/></a>
  </div>
  <div class="info">
    <div class="name"><a href="https://www.scenechronize.com" target="_blank">Scenechronize</a></div>
    <div class="snippet">scenechronize is a production management web application for film,
television and commercials. Visit our <a href="developer_spotlight.html">Developer Spotlight</a> to see Rob Powers, director of engineering, share
how they used GWT to build scenechronize, what he likes and would like to
see from GWT, and his tips and learnings from developing with GWT.</div>
    <!--
    <br/>
    <div><a href="http://www.youtube.com/watch?v=2gqDsi8zRt4">GWT Developers - Scenechronize</a></div>
    -->
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
    <!--
    <br/>
    <div><a href="http://www.youtube.com/watch?v=2gqDsi8zRt4">GWT Developers - Scenechronize</a></div>
    -->
  </div>
</div>

