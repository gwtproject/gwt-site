Steering Committee Meeting Notes
================================

## <a id="05/10/2017"></a> 05/10/2017

Present: Christian, Goktug, Julien, Justin, Leif, Thomas, Colin, Bhaskar

* Removing Sencha affiliation for Justin, no replacement from Sencha being considered at this time
* Christian has moved on from RedHat
    * nominated Max Barkley from RedHat to join the committee, agreed on by all present, Colin will reach out for the next meeting.
* Julien: Elemental2 and generator
    * in the process of being open sourced
    * will be on the google github org
    * jsinterop base/annotations will be managed together, removed from gwt codebase
    * tentatively expected to be done by end of Q2
* GWT 2.9 planning - topics will be followed up in gwt-contrib
* Discussion around removing dev mode as part of 2.9
    * For:
        * while present, JsInterop cannot be used within gwt-user beyond jre emul
        * in addition to never supporting jsinterop, dev mode will also not support new jre emul (e.g. Objects.equals)
    * Against:
        * We presently don't backport fixes - only latest GWT 2 will contain fixes, and removing dev mode may discourage projects from updating
        * Maintenance costs are fairly minimal
        * Forces substantal improvements to gwt-user to take place outside of the gwt repository, as we expect to be the default in gwt 3+
    * Discussion will continue

## <a id="04/12/2017"></a> 04/12/2017

Present: Leif, Colin, Maxime, Thomas, Bhaskar

* GWT 2.8.1 about to start release process
* Testing on jsinterop.base and elemental2 process unclear, Bhaskar will follow up
* jsinterop.base vs annotations - possibly should be one project, probably external to GWT? more discussion next call

## <a id="03/08/2017"></a> 03/08/2017

Not enough members present.

## <a id="02/08/2017"></a> 02/08/2017

Present: Leif, Justin, Bhaskar, Daniel, Thomas, Colin

* Colin will take over as the next SC chair going forward
* GWT 2.8.1:
    * Colin will send an email to gwt-contrib, ask for more patches, volunteers for testing
    * Tentative start date two weeks from now to start
    * Known outstanding patches:
        * jsinterop fixes from google, some still forthcoming
        * System.getProperty may not work correctly?
        * Stream NPE fix

## <a id="01/11/2017"></a> 01/11/2017

Present: Christian, Daniel, Leif, Stephen, Thomas, Colin

* J2CL open source process started
* Elemental2 and generator: bugs remaining, then beta open source process will start
* Patches coming in for GWT 2.8, will look at severity and plan release soon

## <a id="07/20/2016"></a> 07/20/2016

* GWT 2.8
    * Daniel did some pre-testing & smoke tests. 
    * Re-did initial tests, seems good. 
    * Candidate RC1 going into smoke testing.
    * Plan to push it out to Maven soon. 
    * Thomas ran against his local RC1, things look good. 
    * Likely results available later this week, if everything looks good, we’ll publish RC1.
    * No known bugs
    * Colin: Will file bugs against documentation.
    * Need additional releases: Gin & Guava, before we make it final. Need to add this in RC1 release notes.
    * Plan to freeze master after RC1, til final. 

## <a id="04/13/2016"></a> 04/13/2016
 
* Java8
    * Good progress in adding support. 
    * Need more tests for Java8: Soliciting contributions from community. 
* GWT 2.8
    * Main criteria: Get new Guava working with GWT 2.8 before releasing 2.8.
* Github issues
    * Being used regularly
    * Thomas is driving a plan to handle this better. Will wait for this to be finalized.
* Refresh of beta
    * Check how much work is left to be done to get guava building before deciding on this.
    * If 2.8 RC1 doesn’t look imminent, put out a new beta.
 
## <a id="02/10/2016"></a> 02/10/2016

GWT 2.8 Status:

* JDT updated to Java8
* Build server updated to Java8
* Maintainers need to submit patches that were waiting for the above updates
* Plan:
    * Get things checked in by next week, followed by verification.
    * Daniel will cut 2.8 RC1 likely toward end of Feb.
    * Testing: Please test JsInterop to flush out further bugs.
    
## <a id="01/10/2016"></a> 01/10/2016

* No quorum, we only have 4 people show up. May want to plan a follow-up meeting. Bhaskar will ask SC members if they want one.
* GWT 2.8
    * Beta version is working for RH.
    * Working on fixing Interop bugs, should be getting close to the end
    * Memory problem with JDT, now down to 1.5x memory usage compared to earlier.
    * Plan to do RC1 once interop problems are fixed.
     
## <a id="10/28/2015"></a> 10/28/2015

* GWT 2.8
    * Plan to create an RC1 on Monday, Nov 2.
    * Daniel will cut RC1 and make it available for testing to volunteers. 
    * Plan to have two weeks for testing, Daniel will put out a call for volunteers. 
    * JDT has a bug with memory use & Java8, this would be a problem for large apps. 
    * Direct the community to up-vote the bug.
    * Plan to have final release in early December.
* GWT Q&A
    * Christian - Planning to host GWT Q&A
    * Looking for volunteers who can participate.
* GWT course
    * ArcBees plans to have on-line courses for GWT.
* GWT issues on github
    * how are issues assigned to maintainers? We have many issues that are not being looked at.
    * Thomas: plan to write up a proposal , discuss with Bhaskar
    
## <a id="08/19/2015"></a> 08/19/2015

* Dev mode discussion
    * Daniel is migrating test cases to compile-mode, no more need to support dev-mode within Google.
    * Since 2.8 is a compatible release, removing dev-mode will go against what we said earlier.
    * People using 2.7 are still using dev mode, super dev mode is just barely working. 
    * Daniel: make compile-test default in 2.8, 
        * Also let people know what limitations come with JsInterop and dev-mode. 
    * Drop it in trunk after 2.8.
* GWT 2.8:
    * Main missing feature is JsInterop
        * Expectation is that this will be complete by end of Sept.
    * Plan to release after JsInterop is done. Will accept Java8 patches from community, but those are not blockers for right now. 
* Update on new compiler prototype:
    * Still working through compilation of Java features.
    * Plan is to have it capable of building complete apps by the end of the year.
    
## <a id="06/17/2015"></a> 06/17/2015

Agenda:

* 2.8 and follow-up
* Issues list

2.8 and follow-up:

* See meet-up vidoes for details.
* GWT 2.8: 90% done. mainly waiting for Interop to finish
    * Christian: waiting for jsinterop bug fixes
    * GSS migration done for all of Google apps
        * Fixed a few small bugs
        * External folks can use a tool to convert CSS files to GSS: need to finish up this tool.
    * Java8 emulation patches - need to get these in
        * Bhaskar to ping reviewers to get these reviewed
* Google internally will sync to 2.8 branch after 2.8 is released, which will be used by internal apps that are not moving to the new tool.
    * See Daniel’s presentation on ‘Modernizing GWT Applications’ for the migration story.
* GWT 2.8 will be a long-lived maintenance branch
* Master will be open for checkins, vetted by GWT Maintainers. 
* Christian to look into writing a blog post.

Issues List:

* Issues moved to github. Downloads moved to Google Compute Engine.
    * Daniel plans to write a summary of what changed and send it to contrib.
* Thomas: Life of an issue on github
    * No direct mapping for closed but not fixed.
    * Few other issues
    * Thomas: plan to write up a short policy for issues and circulate with SC.
    
## <a id="02/11/2015"></a> 02/11/2015

* GWT 2.8 release
    * Manuel and Julien will do the release. Julien will be the point person.
    * Colin also volunteered to help
    * Features:
        * GSS as default, new migration guide
            * Compiler will error if CSS is used, need to use special flag for CSS
            * Next release beyond 2.8 will not support CSS
        * Java8 - full support for emulation is a question.
        * Interop v1 - Google GWT engineers are working on deciding what should be included in v1.
        * Unlikely to happen before April
* Eclipse plug-ins for GWT
    * GPE is still not buildable externally
    * Plan is to super-cede GPE with GWT plug-in
    * Both plug-ins should work side-by-side
    * Why is GWT GPE plug-in different from other plug-ins? Should it be externally hosted similar to maven and gradle?
    * Decision on this deferred.
