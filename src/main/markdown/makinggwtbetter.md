Making GWT better
===

If you are interested in understanding the internals of Google Web Toolkit (GWT), building from source, or contributing ideas or modifications to the project, then this document is for you.

<ol class="toc">
  <li><a href="#introduction">Introduction</a>
  <ol>
    <li><a href="#licensing">Licensing</a></li>
    <li><a href="#whyopensource">Why is GWT Open Source?</a></li>
    <li><a href="#designaxioms">Design Axioms</a></li>
  </ol>
  </li>

  <li><a href="#community">The GWT Community</a>
  <ol>
    <li><a href="#befriendly">Please Be Friendly</a></li>
    <li><a href="#wheretodiscussgwt">Where to Discuss GWT</a></li>
    <li><a href="#issuetracking">How to Report a Bug</a></li>
  </ol>
  </li>

  <li><a href="#workingoncode">Working with the Code</a>
  <ol>
    <li><a href="#checkingout">Checking out the Source</a></li>
    <li><a href="#compiling">Compiling from Source</a></li>
    <li><a href="#testing">Testing</a></li>
  </ol>
  </li>

  <li><a href="#contributingcode">Contributing Code</a>
  <ol>
    <li><a href="#codestyle">Code Style</a></li>
    <li><a href="#clas">Contributor License Agreement</a></li>
    <li><a href="#submittingpatches">Submitting Patches</a></li>
    <li><a href="#committers">GWT Committers</a></li>
  </ol>
  </li>
  
  <li><a href="#webpage">Contributing to Webpage &amp; Documentation</a>
  
  </li>
  
  
</ol>

## Introduction

To start with the basics, why does GWT exist in the first place? The short answer is that GWT exists to make the web better for users. We've infused a slightly longer answer into our mission statement:

> GWT's mission is to radically improve the web
> experience for users by enabling developers to use existing Java tools
> to build no-compromise AJAX for any modern browser.

We spent a lot of time on that sentence and really tried to pack it with meaning. So, if you'll indulge us, let's explain why our mission statement is formulated the way that it is:

<dl>
  <dt><q>to radically improve</q></dt>
  <dd>The unconventional premise of GWT (i.e. compiling to
  JavaScript) sets the tone for how willing we are to be open-minded
  about approaches that make a big impact. We are willing to invest
  extra consideration and work to find solutions of the
  "order-of-magnitude better" rather than the "good enough" variety.</dd>

  <dt><q>the web experience</q></dt>
  <dd>The experience we want to optimize is the <i>web</i>
  experience. The web has different DNA than desktop applications, and
  GWT was designed to be of the web tradition rather than providing a
  way of slavishly cloning desktop idioms in a browser. Sure, there is
  some overlap. But when there's tension, we prioritize the simple, core
  web concepts like hyperlinks, history, URLs, and bookmarks.</dd>

  <dt><q>for users</q></dt>
  <dd>As in <i>end users</i>. We strongly prioritize features that
  can make the biggest differences to end users. Obviously, we want to make
  developers' lives easier, too, but never at the expense of the user
  experience. A classic example: synchronous RPC is a Bad Idea, even
  though it's so much more convenient for developers.</dd>

  <dt><q>by enabling developers</q></dt>
  <dd>GWT is about <i>enabling</i> developers to do great things,
  not necessarily spoon-feeding them or putting them in a straightjacket.
  GWT strives to be easy to use without sacrificing efficiency. We
  generally prioritize ensuring that the basics of GWT work very well
  rather than adding dozens of special-purpose widgets. It's much better
  to provide a solid platform so that other great libraries can be built
  on top of GWT rather than trying to be all things to all people out of
  the box.</dd>

  <dt><q>existing Java tools</q></dt>
  <dd>We aren't trying to build technology for its own sake! We
  chose the Java language primarily because there already are so many
  great tools out there, not to mention scads of books, articles,
  libraries, and expertise. GWT should take advantage of those tools by
  integrating with them rather than trying to replace them.</dd>

  <dt><q>no-compromise AJAX</q></dt>
  <dd>We want great results, where "great" is defined by how much
  it benefits end users. Sometimes there are conflicts between what is
  easy for us developers and what benefits end users the most. When the
  two are in conflict, end user experience almost always wins. <br />
  <br />
  We definitely do not view development in GWT as a form of compromise.
  We firmly believe that GWT should generate better JavaScript code than
  you would write by hand, and will generally choose to avoid making
  concessions to convenience if they hurt the performance of the
  resulting AJAX code. <br />
  <br />
  GWT is not intended to be an island. Great integration with JavaScript
  is a must-have, as is aggressive use of each browser's best
  capabilities, even if it requires yucky special-case coding (in which
  case, we'll try to hide it behind a no-overhead library).</dd>

  <dt><q>any modern browser</q></dt>
  <dd>We want GWT to be as portable as it can be so long as it
  doesn't involve sacrificing user experience in any significant way.</dd>
</dl>

So that's what GWT is all about. It's also useful to say what kinds of things GWT _isn't_ about...

<dl>
  <dt>Language wars</dt>
  <dd>Why does GWT support the Java programming language instead of
  language X? In a word, tools. There are lots of good Java
  tools. That's the entire explanation. It isn't that we don't like
  language X or that we think the Java programming language is
  somehow superior. We just like the tools.</dd>

  <dt>Being a monolithic framework</dt>
  <dd>We don't like those all-or-nothing heavy "app frameworks" any
  more than you do. That's why we designed GWT to be made up of
  independently useful parts.</dd>

  <dt>Hiding the browser programming model</dt>
  <dd>GWT is not an attempt to abstract away the browser from you.
  Neither is GWT an attempt to prevent you from writing JavaScript when
  you want to. GWT is simply a way to give us developers some extra
  leverage in the form of productivity tools and the ability to create
  higher-level abstractions when it's useful. If you're a JavaScript guru, we hope you'll
  find GWT is a great way for you to package up your best JavaScript
  work and make it easy to reuse.</dd>

  <dt>Bait and switch</dt>
  <dd>Hopefully, this one is self-evident by now. We've been saying
  from the beginning that we had no intention to ever charge for GWT,
  but hopefully now you know we mean it.</dd>

  <dt>Cloning traditional desktop UIs onto the web</dt>
  <dd>We want to foster <i>good</i> UIs, which may or may not be
  the same things as existing desktop UIs. We're trying to be pretty
  circumspect about introducing features "just because" they are used
  often in desktop applications.</dd>
</dl>

<h3 id="licensing">Licensing</h3>

All GWT source and pre-built binaries are provided under the [Apache 2.0 license](http://www.apache.org/licenses/LICENSE-2.0).

<h3 id="whyopensource">Why is GWT Open Source?</h3>

A good first question is, "Why wasn't GWT open source to begin with?" Since GWT's release in May 2006 at JavaOne, we've tried to stay totally focused on lowering the barrier for building AJAX apps. We weren't quite ready to open source the whole thing immediately because we knew we had plans for major infrastructure work (like Mac OS X hosted mode), and we really wanted to encourage everyone to focus on the idea of the product itself and how to write apps instead of creating distractions having to do with GWT's open sourceness.

GWT took off much faster than we expected, and it quickly became clear that the most sensible way to advance GWT would be to open it sooner rather than later. While we've never actually felt particularly stingy about keeping the source closed, now all code for the GWT Java to JavaScript compiler, the hosted mode browser, and so on can progress before your eyes. We're very much looking forward to contributions of ideas, bug reports, and patches.

<h3 id="designaxioms">Design Axioms</h3>

We adopted these on February 17, 2006.

*   User experience is primary
*   Simplify AJAX development
    *   Java debugging is non-negotiable
    *   Key facilities out of the box: history, RPC, localization, and unit testing
*   The big pieces must be independently useful
    *   Simple browser abstraction (i.e. using DOM without using Widget classes)
    *   Widget classes available but not required (i.e. use DOM and/or JSNI)
    *   Application management available but not required (e.g. history support is optional)
    *   RPC available but not required
    *   Java-to-JavaScript compiler is useful without the rest of the user libraries (implies JSNI and debugging aren't tangled with up gwt-user)
*   Interoperate with existing web pages
    *   Other than RPC, components operate without a GWT-specific server
    *   Scriptable from JavaScript and vice-versa
    *   Styling with CSS
*   Facilitate reuse of AJAX code
    *   Sharing of entire components and applications via self-contained JAR files
    *   Ability to publish Google Maps-style JavaScript API from Java classes
*   Optimize performance aggressively
    *   Don't do at run-time what you can do at compile-time
    *   Discourage unoptimizable patterns
*   Be secure by default
    *   Don't expose services automatically

<h2 id="community">The GWT Community</h2>

The GWT community exists primarily through mailing lists, the issue tracker and, to a lesser extent, the source control repository. You are definitely encouraged to contribute to the discussion and you can also help us to keep the effectiveness of the groups high by following and promoting the guidelines listed here.

<h3 id="befriendly">Please Be Friendly</h3>

Showing courtesy and respect to others is a vital part of the Google culture, and we strongly encourage everyone participating in GWT development to join us in accepting nothing less. Of course, being courteous is not the same as failing to constructively disagree with each other, but it does mean that we should be respectful of each other when enumerating the 42 technical reasons that a particular proposal may not be the best choice. There's never a reason to be antagonistic or dismissive toward anyone who is sincerely trying to contribute to a discussion.

Sure, web development is serious business and all that, but it's also a lot of fun. Let's keep it that way. Let's strive to be one of the friendliest communities in all of open source.

<h3 id="wheretodiscussgwt">Where to Discuss GWT</h3>

As always, discuss _using_ GWT in the [official GWT (GWT) developer discussion group](http://groups.google.com/group/Google-Web-Toolkit). There is an additional group for people to discuss any of the stuff in this document (e.g. how to build, how to submit patches) called [GWT Contributors](http://groups.google.com/group/Google-Web-Toolkit-Contributors). You don't have to actually submit code in order to sign up. Your participation itself is a valuable contribution.

<h3 id="issuetracking">How to Report a Bug</h3>

See [Issue Tracking](lifeofanissue.html).

<h2 id="workingoncode">Working with the Code</h2>

If you want to get your hands dirty with the code inside GWT, this is the section for you.

<h3 id="checkingout">Checking Out the Source</h3>

Checking out the GWT source is most useful if you plan to compile GWT yourself. The pre-built GWT distribution already contains all the Java source, so you don't actually need to check it out from the repository just to debug through it. Just tweak your IDE to read source from the GWT jars.

GWT is hosted at [gwt.googlesource.com](https://gwt.googlesource.com/), so you check out the source for GWT using a [Git](http://git-scm.com/) client as you would for any other Git project:

<pre>git clone https://gwt.googlesource.com/gwt trunk</pre>

<h3 id="compiling">Compiling from Source</h3>

<p>Other than a few native libs, everything is Java source that can
be built on any supported platform with the included GWT <a
  href="http://ant.apache.org">Ant</a> build files. At the moment, you
can only build the native code binaries on Linux, and even then with a
bit of effort that is currently beyond the scope of this document. To
keep things simple, we've checked in pre-built native binaries. (Yes, we
know that's unusual. But the source and makefiles are all there... it's
just a pain to doc all that and make it build in a cross-platform way.)</p>

<ol>
  <li>Install <a href="http://ant.apache.org/bindownload.cgi">Apache
  Ant 1.7.0</a> or later. We'll assume that the <code>ant</code> command is in
  your path.</li>
  <li>Check out the GWT prerequisite tools and third-party
  libraries: <pre>
~/gwt$ svn checkout http://google-web-toolkit.googlecode.com/svn/tools/ tools</pre></li>
  <li>Check out the GWT source: <pre>~/gwt$ git clone https://gwt.googlesource.com/gwt trunk</pre>
  </li>
  <li>Optionally, set an environment variable that contains the
  full path to the directory into which you just checked out the tools.
  (If the <code>tools</code> directory is a sibling of the GWT source
  directory, you don't have to specify <code>GWT_TOOLS</code>.) In this
  example, you executed the previous command from the directory <code>~/gwt</code>,
  so you'd type:<pre>
~/gwt$ export GWT_TOOLS=~/gwt/tools</pre></li>
  <li>Switch to the directory into which you checked out the GWT
  source. Let's assume you checked it out into <code>~/gwt/trunk</code>.
  Make sure you're in that directory... <pre>~/gwt$ cd ~/gwt/trunk</pre>
  then just invoke Ant <pre>ant</pre></li>
  <li>The GWT build creates each binary distribution in the <code>build/dist</code>
  subdirectory of the source root directory. In this example, the
  distributions would be in <code>~/gwt/trunk/build</code>.</li>
</ol>

<h3 id="testing">Testing</h3>

Testing is very important to maintaining the quality of GWT. [Unit Tests](#unittesting) should be written for any new code, and changes should be verified to not break existing tests before they are submitted for review.  To perform the tests, simply run `ant test` and verify that there are no failures.

<h4 id="antjunit">Ant JUnit Issues</h4>

There is a problem, detailed at [ant.apache.org](http://ant.apache.org/manual/OptionalTasks/junit.html), where ant cannot find the JUnit classes.  The simple workaround is to either:

<ul>
  <li>Delete ant-junit.jar from the ant lib directory (for example,
  <code>/usr/share/ant/lib/</code>)</li>
  <li>Copy junit.jar into your ant lib directory</li>
</ul>

<h2 id="contributingcode">Contributing Code</h2>

<p>Now that GWT is open source, we're excited that it is now easier
for our users to fix bugs and create new features, and we hope to get
great patches from the community. Before you fire up your favorite IDE
and begin hammering away at that new feature, though, please take the
time to read this section and understand the process. While it seems
rigorous, we want to keep a high standard of quality in the code base.
Also, please be aware that we code contributors must sign a <a
  href="#committers">Contributor License Agreement</a> before we can
accept any code.</p>

<h3 id="codestyle">Code Style</h3>

<p>To keep the source consistent, readable, diffable and easy to
merge, we use a fairly rigid coding style, and all patches will be
expected to conform to the style outlined here. To keep things as simple
as possible, many of these style rules will be automatically verified by
the GWT build; style issues that can't be caught by the build system should be
addressed during code review.</p>

<p>To make it easier to format your GWT code correctly, use the
<a href="https://gwt.googlesource.com/gwt/+/master/eclipse/settings/code-style/gwt-format.xml">GWT style formatter</a>
for Eclipse (Preferences | Java &gt; Code Style &gt; Formatter | Import...).

<p>In general, the GWT style is based on <a
  href="http://java.sun.com/docs/codeconv/html/CodeConvTOC.doc.html">the
standard Java conventions</a>, except for the differences spelled out below.</p>

<p>For the record, we know that coding style is often a
controversial topic. We acknowledge that plenty of great approaches
exist out there. We're simply trying to pick one that is at least
somewhat consistent with Oracle's Java coding conventions, codify it well,
and stick to it.</p>

<h4>Comments and Javadoc</h4>

<p>Every file should have an Apache license header at the top,
prefaced with a copyright notice. A package statement and import
statements should follow, each block separated by a blank line. Next is
the class or interface declaration. In the Javadoc comments, describe
what the class or interface does.</p>

<pre>
/*
 * Copyright 2006 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.foo;

import com.google.bar.Blah;
import com.google.bar.Yada;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Does X and Y and provides an abstraction for Z.
 */
public class Foo {
  ...
} 
</pre>

<h4>Class Structure and Member Sort Order</h4>
<p>Java types should have the following member order:</p>
<ol>
  <li>Nested Types (mixing inner and static classes is okay)</li>
  <li>Static Fields</li>
  <li>Static Initializers</li>
  <li>Static Methods</li>
  <li>Instance Fields</li>
  <li>Instance Initializers</li>
  <li>Constructors</li>
  <li>Instance Methods</li>
</ol>

<p>Members that fall into the same category (e.g. static methods)
should also be sorted in this order based on visibility:</p>
<ol>
  <li>public</li>
  <li>protected</li>
  <li>default</li>
  <li>private</li>
</ol>

<p>All methods should be sorted alphabetically. Sorting is optional
but recommended for fields. For example, the following class excerpt is
correctly sorted:</p>

<pre>
public abstract class Foo {
  // Type declarations.
  public class FooBaz {
  }
 
  private class FooBar {
  }

  // Static field declarations.
  // Remember, fields do NOT need to be sorted.
  static String B;
  static String A;

  // Static initializer declarations.
  static {
  }

  // Static methods declarations.
  // Remember, methods do need to be sorted.
  static void aStatic() {
  }

  static void bStatic() {
  }

  // Instance field declaration.
  String bField;
  String aField;

  // Instance Initializer declarations.
  {
  }

  // Constructors declaration.
  public Foo() {
  }

  protected Foo(String s) {
  }

  Foo(int i) {
  }

  private Foo(boolean b) {
  }

  // Instance method declaration.
  public void b() {
  }

  public void c() {
  }

  protected void a() {
  }

  protected void d() {
  }

  protected void e() {
  }

  protected void f() {
  }

  String h() {
  }

  // The "abstract" keyword does not modify the position of the method.
  abstract String i();

  void j() {
  }

  private void g() {
  }
}
</pre>

<h4>Indentation</h4>
<p>We use 2-space indents for blocks. No tabs at all, anywhere.</p>

<p>We use 4-space indents after line wraps, including function calls
and assignments. For example, this is correct (4 spaces after the
newline):</p>
<pre>
Instrument i =
    new Instrument();
</pre>

and this is not correct (2 spaces after the newline):

<pre>
Instrument i =
  new Instrument();
</pre>

<h4>Imports</h4>
<p>The ordering of import statements is:</p>
<ul>
  <li>GWT imports</li>
  <li>Imports from third parties (com, junit, net, org)</li>
  <li>java and javax</li>
</ul>

<p>To exactly match the IDE settings, the imports should be:</p>
<ul>
  <li>Alphabetical within each grouping. Capital letters are
  considered to come before lower case letter (e.g. Z before a).</li>
  <li>There should be a blank line between each major grouping
  (google, com, junit, net, org, java, javax).</li>
</ul>

<h4>Line Length and Wrapping</h4>
<p>Each line of text in your code should be at most 100 characters
long. Use linefeed characters to break lines (Unix-style). There are
some exceptions:</p>

<ul>
  <li>Java identifiers referenced from within
  <a href="latest/DevGuideCodingBasicsJSNI.html#methods-fields">JSNI
  methods</a> can get quite long and cannot be parsed if split across lines.</li>

  <li>Exception: If a comment line contains an example command or a
  literal URL longer than 100 characters, that line may be longer than 100
  characters for ease of cut and paste.</li>

  <li>Exception: Import lines can go over the limit because humans
  rarely see them. This also simplifies tool writing.</li>
</ul>

<h4>Acronyms in names</h4>
<p>Treat acronyms and abbreviations as words. The names are much more readable:</p>
<table style="margin-left: 2em">
  <tr>
    <th style="width: 16em">Good</th>
    <th>Bad</th>
  </tr>
  <tr>
    <td><code>XmlHttpRequest</code></td>
    <td><code>XMLHTTPRequest</code></td>
  </tr>
  <tr>
    <td><code>getCustomerId</code></td>
    <td><code>getCustomerID</code></td>
  </tr>
</table>

<p>This style rule also applies when an acronym or abbreviation is the entire name:
<table style="margin-left: 2em">
  <tr>
    <th style="width: 16em">Good</th>
    <th>Bad</th>
  </tr>
  <tr>
    <td><code>class Html</code></td>
    <td><code>class HTML</code></td>
  </tr>
  <tr>
    <td><code>String url;</code></td>
    <td><code>String URL;</code></td>
  </tr>
  <tr>
    <td><code>long id;</code></td>
    <td><code>long ID;</code></td>
  </tr>
</table>

<p>Note that this is a reversal of an earlier, regretted decision, and
much code in GWT violates this rule. While it's not worth breaking
existing APIs for stylistic concerns, all new code should treat
acronyms as words.</p>

<p>For further justifications of this style rule, see Effective Java
Item 56 (Item 38 in 1st edition) and Java Puzzlers Number 68.</p>

<h4>Parameterized type names</h4>

<p>Parameterized type names should be one capital letter. However, if
readability demands longer names (particularly due to having multiple
parameters), the name should be capitalized and have suffix "T". In a
nutshell, prefer &lt;T> or &lt;K, V>, and devolve to &lt;KeyT, ValueT> if
necessary.</p>

<table style="margin-left: 2em">
  <tr>
    <th style="width: 16em">Good</th>
    <th>Bad</th>
    <th>Tolerable</th>
  </tr>
  <tr>
    <td><code>Foo&lt;T></code></td>
    <td><code>Foo&lt;FooClientType></code></td>
  </tr>
  <tr>
    <td><code>Bar&lt;K, V></code></td>
    <td><code>Bar&lt;KeyType, ValueType></code></td>
  </tr>
  <tr>
    <td><code>Baz&lt;V, E, X></code></td>
    <td><code>Baz&lt;EventType, ErrorType, ExpressionType></code></td>
    <td><code>Baz&lt;EventT, ErrorT, ExpressionT></code></td>
  </tr>
</table>

<h3 id="unittesting">Unit Testing</h3>

Unit tests are very important, and we strongly encourage submissions that include them, adding new unit tests for new functionality or updating existing unit tests for bug fixes.

<p>Tests for Java classes should be placed in a parallel source tree
under <code>test</code> and the test class name should be suffixed with
<code>Test</code>. For example:</p>

<pre>
src/com/google/gwt/core/client/EntryPoint.java
test/com/google/gwt/core/client/EntryPointTest.java
</pre>

The use of the parallel test tree has two major advantages:

<ul>
  <li>You can do package scope testing (vs. a <code>tests</code>
  subpackage).</li>
  <li>There is a clear separation between production and test code
  making packaging easier. This reduces the number of build breaks and
  the amount of time spent updating build files.</li>
</ul>

Note that there is a problem using JUnit tests with ant &mdash; a [workaround](#antjunit) is described above.

<h3 id="submittingpatches">Submitting Patches</h3>

Please do submit code. Here's what you need to do:

<ol>
  <li>Decide which code you want to submit. A submission should be
  a set of changes that addresses one issue in the <a
    href="http://code.google.com/p/google-web-toolkit/issues/list"
    target="_blank">GWT issue tracker</a>. Please don't mix more than
  one logical change per submittal, because it makes the history hard to
  follow. If you want to make a change that doesn't have a corresponding
  issue in the issue tracker, please create one.</li>

  <li>Also, coordinate with team members that are listed on the
  issue in question. This ensures that work isn't being duplicated and
  communicating your plan early also generally leads to better patches.</li>

  <li>Ensure that your code adheres to the <a href="#codestyle">GWT
  source code style</a>.</li>

  <li>Ensure that there are unit tests for your code.</li>

  <li>Sign in to <a href="https://gwt-review.googlesource.com">GWT's
  Gerrit service</a> and sign the <a
    href="https://gwt-review.googlesource.com/#/settings/agreements">Contributor
  License Agreement</a>.</li>

  <li>Push your changes to Gerrit for review. The first time you will need to do 
  some <a href="#gerritsetup">initial setup</a>. Follow Gerrit's documentation to create a change and upload it for review.</li>
  
  <li>After setting up gerrit on your system, you can push your changes for review using:
  	<pre>git push origin HEAD:refs/for/master</pre>
  </li>
</ol>

<h3 id="gerritsetup">Gerrit setup</h3>

Finally, to actually create an issue is slightly more involved, but most of the steps only need to be done once:

1. Setup your HTTP Password: 
   Go to Settings in Gerrit. Under Settings you will find "HTTP Password" and click "Obtain Password"
   and follow the steps to get your HTTP Password and/or to setup your .netrc file.
2. Setup your Gerrit commit-msg hook (optional, but recommended): 
   Gerrit provides a commit hook at <pre>https://gwt-review.googlesource.com/tools/hooks/commit-msg</pre> 
   to automatically add Change-Id lines to your commits.  
   Download this and add it to your checkout's .git/hooks directory: 
   <pre>curl -o .git/hooks/commit-msg https://gwt-review.googlesource.com/tools/hooks/commit-msg && chmod +x .git/hooks/commit-msg</pre>
3. Make a change and commit it locally using git (e.g., edit a file foo and then run `git commit -m "my first change" foofile`).
   Push the commit to Gerrit for review: <pre>git push origin HEAD:refs/for/master</pre>.

If you have any problems you might want to consult the gerrit documentation on [Uploading Changes](https://gerrit-review.googlesource.com/Documentation/user-upload.html) 

<h3 id="committers">GWT Committers</h3>

The current members of the GWT engineering team are the only committers at present. In the great tradition of eating one's own dogfood, we will be requiring each new GWT engineering team member to earn the right to become a committer by following the procedures in this document, writing consistently great code, and demonstrating repeatedly that he or she truly gets the zen of GWT.

<h3 id="clas">Contributor License Agreements</h3>

Before we can accept a patch from you, you must sign a Contributor License Agreement (CLA). The CLA protects you and us.

*   If you are an individual writing original source code and you're sure you own the intellectual property, then you'll need to sign an [individual CLA](http://code.google.com/legal/individual-cla-v1.0.html).
*   If you work for a company that wants to allow you to contribute your work to GWT, then you'll need to sign a [corporate CLA](http://code.google.com/legal/corporate-cla-v1.0.html).

You can sign either CLA through [GWT's Gerrit service](https://gwt-review.googlesource.com/#/settings/agreements).

<h3 id="webpage">Contributing to webpage and Documentation</h3>

This webpage and the documentation hosted on it are completly written in markdown. If you find an error, want to make an improvement, write an article or do something to the webpage feel free to do so.

Here is how it works:

All the code is stored inside a git repository. You can clone it like this:

<pre>git clone https://gwt.googlesource.com/gwt-site</pre>

The source code for this webpage will be found in *src/main/markdown*. You can edit existing files and add new ones. If you want to see your changes you will need to run:

<pre>mvn clean install</pre>

After that you can go to *target/generated-site* and see the generated site.

If you think your change is ready to be published on gwtproject.org you can send us
your changes for review by using gerrit. Note: you need to have signed a <a href="#clas">CLA Agreement</a>.

This works the same way you would submit a code change to GWT. More information can be found [here](#submittingpatches).

<h3 id="global_gitignore">Global .gitignore</h3>

Since our .gitignore files don't contain IDE or OS specific .gitignore entries you should setup your global .gitignore.

[This](https://help.github.com/articles/ignoring-files) is how you setup a global .gitingore.

[Here](https://github.com/github/gitignore/tree/master/Global) you find a list of IDE specific global .gitignore files.
