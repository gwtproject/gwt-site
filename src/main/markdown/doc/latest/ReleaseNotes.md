<p>
The latest release of GWT, version 2.5, includes the new features and
functionality listed below. See the 2.5 <a href="../../release-notes.html#Release_Notes_2_5_0">Release Notes</a> for bug fixes and other changes.</p>

<table class="columns">
  <tr>
    <td>
      <ol class="toc" id="pageToc">
        <li><a href="#NewFeatures">New Features</a>
          <ul class="toc" >
            <li><a href="#superdevmode">Super Dev Mode (experimental)</a></li>
            <li><a href="#elemental">Elemental (experimental)</a></li>
            <li><a href="#optimization">New compiler optimizations</a></li>
            <li><a href="#aria">Updated ARIA support</a></li>
            <li><a href="#uirenderer">UIBinder Enhancements</a></li>
            <li><a href="#validation">Validation Enhancements</a></li>
          </ul>
        </li>
        <li><a href="#GettingStarted">Getting Started</a></li>
      </ol>
    </td>
  </tr>
</table>

<h2 id="NewFeatures">New Features</h2>

<h3 id="superdevmode">Super Dev Mode (experimental)</h3>
Super Dev Mode is an experimental replacement for Development Mode. For more,
see <a href="../../articles/superdevmode.html">Introducing Super Dev Mode</a>.

<h3 id="elemental">Elemental (experimental)</h3>
Elemental is an experimental new library for fast, lightweight, and "to the metal"
web programming in GWT. It's intended for developers who are comfortable working
with the browser API's that JavaScript programmers use. For more information,
please see the <a href="../../articles/elemental.html">Introducing Elemental</a>
article.

<h3 id="optimization">New compiler optimizations</h3>
<ul>
  <li>The GWT compiler can optionally use the
<a href="http://code.google.com/p/closure-compiler/">Closure compiler</a> to provide
additional JavaScript optimizations. The Closure compiler has a collection of Javascript
optimizations that can benefit code size, including a graph-coloring-based variable
allocator, comprehensive JavaScript function and variable inlining, cross-module code
motion, statement fusing, name shadowing and many more. However, this makes the GWT
compiler slower, so it's not enabled by default. Simply add the -XenableClosureCompiler
to the list of compiler flags to enable optimization.</li>

  <li>Large projects that use <a href="DevGuideCodeSplitting.html">Code Splitting</a> and have many
split points can take advantage of <i>Fragment Merging</i>. The GWT compiler can automatically
merge fragments to reduce the size of the "leftover" fragment. For more, please see the article,
<a href="../../articles/fragment_merging.html">Fragment Merging in the GWT Compiler</a>.</li>
</ul>

<h3 id="aria">Updated ARIA support</h3>
We added a new accessibility ARIA library that has a full coverage of the <a
  href="http://www.w3.org/TR/wai-aria/">W3C ARIA standard</a>. This makes it
easier to correctly set ARIA roles, states, and properties on DOM elements.
For more details have a look at the updated <a
  href="DevGuideA11y.html">GWT accessibility
  documentation</a>.

<h3 id="uirenderer">UIBinder Enhancements</h3>
GWT 2.5 adds extensions to UiBinder that allow it to support Cell rendering and
event handling. In particular, this design enables UiBinder to generate a UiRenderer
implementation to assist with rendering SafeHtml, and dispatching events to
methods specified by @UiHandler tags. See
<a href="DevGuideUiBinder.html#Rendering_HTML_for_Cells">
Rendering HTML for Cells</a> for more information.
<br/>
<br/>
We’ve also introduced the IsRenderable/RenderablePanel types. When used by an
application instead of HTMLPanel, they can significantly improve rendering time
and reduce the latency of complex UiBinder UIs.

<h3 id="validation">Validation Enhancements</h3>
GWT 2.5 adds support for more features of JSR-303 Bean Validation specification and
this support is no longer considered "experimental". For more, see the
<a href="DevGuideValidation.html">Validation documentation</a>.

<h2 id="GettingStarted">Getting Started</h2>
<br/>Instructions for installing this new release of GPE and the GWT SDK can be found here: <a href="https://developers.google.com/eclipse/docs/getting_started">Getting Started with Google Plugin for Eclipse</a>.
<br/>
<br/>
If you’re simply looking for the GWT 2.5 SDK, you can find it here:
<a href="http://code.google.com/p/google-web-toolkit/downloads/list">GWT SDK Downloads</a>.

<h3>Problems?</h3>

<p>Any problems using these new features? As always, let us know on the <a href="https://groups.google.com/group/google-web-toolkit/topics">GWT Developer Forum</a> and our great
community or GWT team members will be happy to help out.</p>