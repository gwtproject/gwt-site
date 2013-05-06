<p>The GWT project maintains a <a href="http://code.google.com/p/google-web-toolkit/issues">public issue tracker</a>
where you can report bugs and request features for GWT.</p>

<p>
<ol class="toc" id="pageToc">  
  <li><a href="#guide">Issue reporting guidelines</a></li>
  <li><a href="#life">What happens when you submit an issue</a></li>
</ol>
</p>

<h2><a name="guide"></a>Issue reporting guidelines</h2>

<p>One of the best ways you can help us
improve GWT is to let us know about any problems you find with it.</p> 
<p>Here's how to report issues:</p> 
<ol> 
  <li><a href="http://code.google.com/p/google-web-toolkit/issues/advsearch">Search for
  your issue</a> to see if anyone has already reported it.</li> 
  <li>If you find your issue and it's important to you, star it. That's how we
  know which issues are most important to fix, and you'll get notified of new
  activity on the issue.</li> 
  <li>If no one's reported your issue, <a href="http://code.google.com/p/google-web-toolkit/issues/entry">file it</a>.
  Please fill out the template completely. It is especially important to provide browser version(s) and a small code sample
  which can be used to reproduce the issue.
  </li>
  <li>For feature requests, file an issue using the standard developer defect report template. Under 
  "Found in GWT Release," put "FEATURE REQUEST."</li>
</ol> 
<p>To see what happens to your issue once you report it,
keep reading.</p> 

<h2><a name="life"></a>What happens when you submit an issue</h2>
<p>Here's the Life of an Issue, in a nutshell:</p> 
<ol> 
<li>An issue is filed and has the state "New".</li> 
<li>A GWT contributor periodically reviews and triages issues. The GWT contributor
attempts to verify the issue and assigns a status based on the results.</li>
<li>Once an issue
has been verified, it may be marked Accepted, in which case the GWT team plans to fix it,
or PatchesWelcome, which means that the GWT team likely won't be able
to get to it, but would be happy to review patches from the community. To submit a patch, follow
<a href="makinggwtbetter.html#submittingpatches">these guidelines</a>.</li>
<li>Once an issue has been fixed, its status will be updated to Fixed or 
FixedNotReleased, indicating whether the fix is available in the current
numbered release or, more commonly, in SVN trunk for the next release.</li>
</ol>

<p>The following tables provide detail on all issue status types.</p>

<h3>Open Issues</h3>
<table border="1" cellpadding="5" cellspacing="0" width="50%"> 
<tbody> 
<tr> 
<th>Status</th> 
<th>Description</th> 
</tr> 
<tr> 
<td>New</td>
<td>Issue has not had initial review yet</td>
</tr> 
<tr>
<td>Accepted</td>
<td>Problem reproduced / Need acknowledged</td>
</tr> 
<tr>
<td>Started</td>
<td>Work on this issue has begun</td>
</tr> 
<tr>
<td>PatchesWelcome</td>
<td>Confirmed and triaged, but not assigned. Feel free to submit patches for review.</td>
</tr> 
<tr>
<td>ReviewPending</td>
<td>Commit gated by code review</td>
</tr> 
<tr>
<td>FixedNotReleased</td>
<td>Fixed in an as-yet-unreleased version of GWT</td>
</tr> 
<tr>
<td>NeedsInfo</td>
<td>Additional information is required from submitter</td>
</tr> 
</tbody>
</table>

<h3>Closed Issues</h3>
<table border="1" cellpadding="5" cellspacing="0" width="50%">
<tbody>
<tr> 
<th>Status</th> 
<th>Description</th> 
</tr> 
<tr>
<td>Fixed</td>
<td>Fixed in current release of GWT</td>
</tr> 
<tr>
<td>NotPlanned</td>
<td>No current plans to address this issue</td>
</tr> 
<tr>
<td>Invalid</td>
<td>This was not a valid issue report (e.g. illegible, spam, does not describe a real issue)</td>
</tr> 
<tr>
<td>AssumedStale</td>
<td>Assumed (perhaps incorrectly) to no longer be a problem; please reconfirm with latest release</td>
</tr> 
<tr>
<td>AsDesigned</td>
<td>The feature is behaving as intended</td>
</tr> 
<tr>
<td>KnownQuirk</td>
<td>Not denying it's a real issue, but likely a browser quirk beyond our control to fix efficiently</td>
</tr> 
<tr>
<td>Duplicate</td>
<td>This report duplicates an existing issue</td>
</tr> 
<tr>
<td>CannotReproduce</td>
<td>The issue could not be reproduced based on the report</td>
</tr> 
</tbody> 
</table> 

<h2>Other Stuff</h2> 
<p>The states and lifecycle above are how we generally try to track software.
However, GWT contains a lot of software and gets a correspondingly large
number of issues. As a result, sometimes issues don't make it through all the
states in a formal progression. We do try to keep the system up to date, but
we tend to do so in periodic "issue sweeps" where we review the database and
make updates.</p>
<p>Occasionally, we make tweaks to
the list of issue states and the lifecycle described above.  When we do this,
we'll update this page as well.</p> 


