FAQ - Getting Started
===

<ol class="toc" id="pageToc">
  <li><a href="#Installation_and_Upgrades">Installation and Upgrades</a>
    <ol class="toc">
      <li><a href="#What_are_the_system_requirements_for_GWT?">What are the system requirements for GWT?</a></li>
      <li><a href="#How_do_I_install_GWT?">How do I install GWT?</a></li>
      <li><a href="#Is_GWT_available_in_my_country?_Does_it_work_for_my_language?">Is GWT available in my country?
    Does it work for my language?</a></li>
      <li><a href="#Does_GWT_cost_anything?">Does GWT cost anything?</a></li>
      <li><a href="#What_type_of_information_will_GWT_collect">What type of information will GWT collect?</a></li>
      <li><a href="#Will_I_have_to_upgrade_my_application_when_a_new_version_of_GWT">Will I have to upgrade my
    application when a new version of GWT is released?</a></li>
      <li><a href="#How_do_I_get_my_project_to_run_again_after_upgrading_GWT?">How do I get my project to run again after
  upgrading GWT?</a></li>
    </ol>
  </li>
  <li><a href="#Licensing">Licensing</a>
    <ol class="toc">
      <li><a href="#Can_I_use_GWT_to_develop_commercial/enterprise_applications?">Can I use GWT to develop commercial/enterprise applications?</a></li>
      <li><a href="#Can_I_redistribute_the_GWT_binaries_with_my_product?">Can I redistribute the GWT binaries with my product?</a></li>
    </ol>
  </li>
  <li><a href="#Support">Support</a>
    <ol class="toc">
      <li><a href="#Does_GWT_have_a_blog?">Does GWT have a blog?</a></li>
      <li><a href="#Whom_do_I_contact_if_I_have_questions?">Whom do I contact if I have questions?</a></li>
      <li><a href="#Where_should_I_report_bugs?">Where should I report bugs?</a></li>
      <li><a href="#Where_should_I_submit_suggestions_to_improve_GWT?">Where should I submit suggestions to improve GWT?</a></li>
      <li><a href="#Where_can_I_find_the_GWT_source_code?_Can_I_submit_a_patch?">Where can I find the GWT source code?
  Can I submit a patch?</a></li>
    </ol>
  </li>
  <li><a href="#Browsers_and_Servers">Browsers and Servers</a>
    <ol class="toc">
      <li><a href="#What_browsers_does_GWT_support?">What browsers does GWT support?</a></li>
      <li><a href="#Will_my_app_break_when_a_new_browser_comes_out?">Will my app break when a new browser comes out?</a></li>
      <li><a href="#Can_I_use_GWT_with_my_favorite_server-side_templating_tool?">Can I use GWT with my favorite
  server-side templating tool?</a></li>
    </ol>
  </li>
</ol>

<div id="FAQ_GettingStarted"/>


<h2 id="Installation_and_Upgrades">Installation and Upgrades</h2>

<h3 id="What_are_the_system_requirements_for_GWT?">What are the system requirements for GWT?</h3>

<p>GWT is designed to run on systems that meet the following requirements:</p>

<ul>
<li>Java: Oracle Java 2 Runtime Environment 1.5</li>
</ul>

<ul>
<li>Operating system: Windows Vista/XP/2000, Mac OS X 10.4+ (Tiger or Leopard), or Linux with GTK+ 2.2.1+</li>

<li>Hardware: ~100MB of free disk space, 512MB RAM</li>
</ul>



<p>If you have trouble running GWT, and your system meets the requirements above, let us know on the <a href="http://groups.google.com/group/Google-Web-Toolkit">GWT
developer discussion group</a>.</p>

<p/>

<h3 id="How_do_I_install_GWT?">How do I install GWT?</h3>

<p>For step-by-step instructions, see Getting Started: Quick Start <a href="../../gettingstarted.html">Installing Google Web
Toolkit</a>.</p>

<p/>

<h3 id="Is_GWT_available_in_my_country?_Does_it_work_for_my_language?">Is GWT available in my country?
Does it work for my language?</h3>

<p>GWT is available in all countries and should work for most languages, though documentation is currently only available in U.S. English.</p>

<p/>

<h3 id="Does_GWT_cost_anything?">Does GWT cost anything?</h3>

<p>No, GWT is completely free. In fact, all of <a href="http://code.google.com/p/google-web-toolkit/">GWT's source code</a> is available under the <a href="../../terms.html">Apache 2.0 open source license</a>.</p>

<p/>

<h3 id="What_type_of_information_will_GWT_collect">What type of information will GWT collect?</h3>

<p>When you use GWT's development mode server or compiler, the application periodically sends a unique timestamp ID and version number of your product back to Google's servers so that the application can notify you of new versions. The timestamp ID is generated the first time you use the complier or development mode server. As a part of this request, Google will log the timestamp ID and the version number.</p>

<p>The GWT developer plugin checks for updates once a day or as configured on the respective browser, sending the current plugin version number. As a part of this request, Google will log the plugin version number.</p>

<p>We won't log cookies or personal information about you, and we will only use any data we log in the aggregate to operate and improve GWT. We use this information internally to determine usage volume of the development mode server and compiler in the aggregate.</p>

<p>You may choose to not use the development mode server and uninstall the browser plugin at any time. You may use just the GWT compiler with the flag "-XdisableUpdateCheck" to compile your code directly to JavaScript. This does not send a request for the most recent version of GWT.</p>

<p/>

<h3 id="Will_I_have_to_upgrade_my_application_when_a_new_version_of_GWT">Will I have to upgrade my
application when a new version of GWT is released?</h3>

<p>We'll release new versions of GWT on the <a href="/">GWT web site</a>. Barring any unforeseen circumstances, we expect to keep
all previous versions of GWT available on the web site as well. As releases become outdated, we'll no longer actively support them on the <a href="http://groups.google.com/group/Google-Web-Toolkit">GWT discussion group</a>, but we'll do our best to keep old releases and documentation around as long as
possible.</p>

<p/>

<h3 id="How_do_I_get_my_project_to_run_again_after_upgrading_GWT?">How do I get my project to run again after
upgrading GWT?</h3>

<p>If you are working on a project, you may notice problems compiling and running your project if you upgrade the GWT library from one version to another. Sometimes you may see
GWT compiler internal errors, or you may see odd runtime errors you've never seen before.</p>

<p>Before you file a bug, try stopping development mode and erasing your
compiler output (usually in the directory <tt>www/</tt><i>&lt;package
  name&gt;</i>. Then, restart development mode.
If you're still having problems, though, we definitely want to hear about it!</p>

<h2 id="Licensing">Licensing</h2>

<p/>

<h3 id="Can_I_use_GWT_to_develop_commercial/enterprise_applications?">Can I use GWT to develop
commercial/enterprise applications?</h3>

<p>Yes and yes! GWT is available for non-commercial, commercial, and enterprise applications. All of GWT's code is available under the <a href="../../terms.html">Apache 2.0 open source license</a>.</p>

<p/>

<h3 id="Can_I_redistribute_the_GWT_binaries_with_my_product?">Can I redistribute the GWT binaries with my
product?</h3>

GWT has been released under the Apache 2.0 license, so you're allowed to distribute binaries as per the [Apache 2.0 terms](../../terms.html). Please note that some libraries used in the GWT are under other licenses as referenced [here](../../terms.html#licenses), so you must comply with their terms as well.

## Support<a id="Support"></a>

### Does GWT have a blog?<a id="Does_GWT_have_a_blog?"></a>

Glad you asked! Our blog is [http://googlewebtoolkit.blogspot.com/](http://googlewebtoolkit.blogspot.com/)

### Whom do I contact if I have questions?<a id="Whom_do_I_contact_if_I_have_questions?"></a>

You can post your questions to the [GWT developer discussion group](http://groups.google.com/group/Google-Web-Toolkit) on Google
Groups. The GWT engineering and operations team will participate in the group and try to answer questions as they come up.

### Where should I report bugs?<a id="Where_should_I_report_bugs?"></a>

You can file a bug in the [GWT Issue Tracker](http://code.google.com/p/google-web-toolkit/issues/list). Please be sure to search for
your problem before reporting a new issue, since someone else might have already reported it. If you would like to be notified about activities on your issue, star the report and
you will receive emails at your account's email address.

### Where should I submit suggestions to improve GWT?<a id="Where_should_I_submit_suggestions_to_improve_GWT?"></a>

If you have an idea for an enhancement to GWT, please search through the [Issue Tracker](http://code.google.com/p/google-web-toolkit/issues/list). If
it isn't there, you might want to first suggest it to the [GWT developer discussion
group](http://groups.google.com/group/Google-Web-Toolkit).

You can submit requests for enhancements at the [GWT Issue Tracker](http://code.google.com/p/google-web-toolkit/issues/list). When you
save your report, it will be labeled as a &quot;Defect&quot; even though you want to submit an RFE (Request for Enhancement). Don't worry! We review each item submitted to our Issue
Tracker, and will mark enhancement requests appropriately when we receive them.

### Where can I find the GWT source code? Can I submit a patch?<a id="Where_can_I_find_the_GWT_source_code?_Can_I_submit_a_patch?"></a>

The GWT source code, licensed under Apache 2.0, is available [here](http://code.google.com/p/google-web-toolkit/). If you are interested in
contributing a patch, please visit the [Making GWT Better](../../makinggwtbetter.html) guide.

## Browsers and Servers<a id="Browsers_and_Servers"></a>

### What browsers does GWT support?<a id="What_browsers_does_GWT_support?"></a>

GWT supports the following browsers:

*   Firefox
*   Internet Explorer 8, 9, 10, 11
*   Safari 5, 6
*   Chromium and Google Chrome
*   Opera latest version

### Will my app break when a new browser comes out?<a id="Will_my_app_break_when_a_new_browser_comes_out?"></a>

Generally, no. GWT was designed in such a way to have all browser-specific code in user-level libraries. What's more, JavaScript itself has very consistent support across
browsers, so when the GWT compiler performs its Java-to-JavaScript compilation, it doesn't need to worry about which browser the JavaScript is being generated for.

That said, what makes compliance to newer browsers tricky is the DOM API. For backwards-compatible browsers, it just works. For other cases, it's straightforward to change the
user-level libraries. All that's required is to implement a version of DOMImpl for the desired browser. In some cases an entirely new browser may require a bit more work to be
supported by GWT, but this would be the exception rather than the rule.

### Can I use GWT with my favorite server-side templating tool?<a id="Can_I_use_GWT_with_my_favorite_server-side_templating_tool?"></a>

Yes, you are free to use GWT with any server-side templating tool such as XSLT, Frontpage, Dreamweaver, WebObjects, PHP, Active Server Pages, or JavaServer Pages to name a
few.

With GWT development, your Java client code gets compiled into equivalent JavaScript that is loaded into your host pages. The generated product is totally independent from the
server-side technology that you choose to use in your web application.

You can go ahead and use your favorite server-side templating tool and include your template directives into your host pages along with your GWT-generated JavaScript files. The
server-side technology you're using to realize the templates is invisible to the browser and works just as it does without your GWT modules.
