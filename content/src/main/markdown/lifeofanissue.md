Life of an issue
===

The GWT project maintains a [public issue tracker](https://github.com/gwtproject/gwt/issues) where you can report bugs and request features for GWT.

1.  [Issue reporting guidelines](#guide)
2.  [What happens when you submit an issue](#life)

## Issue reporting guidelines<a id="guide"></a>

One of the best ways you can help us improve GWT is to let us know about any problems you find with it.

Here's how to report issues:

1.  [Search for your issue](https://github.com/gwtproject/gwt/issues?q=is%3Aissue) to see if anyone has already reported it.
2.  If no one's reported your issue, [file it](https://github.com/gwtproject/gwt/issues/new). Please provide the versions of GWT, the browser(s), and the platform(s) used. It is especially important to provide the browser version(s) and a small code sample which can be used to reproduce the issue. If the issue has been discussed in forums, please provide a link to the discussion too.

To see what happens to your issue once you report it, keep reading.

## What happens when you submit an issue<a id="life"></a>

Here's the Life of an Issue, in a nutshell:

1.  An issue is filed.
2.  A GWT contributor periodically reviews and triages issues. The GWT contributor attempts to verify the issue and assigns a status based on the results.
3.  Once an issue has been verified, it may be marked Accepted, in which case the GWT team plans to fix it, or PatchesWelcome, which means that the GWT team likely won't be able to get to it, but would be happy to review patches from the community. To submit a patch, follow [these guidelines](makinggwtbetter.html#submittingpatches).
4.  Once an issue has been fixed, it'll be closed and assigned to a milestone, indicating whether the fix is available in the current numbered release or, more commonly, in the Git main branch for the next release.

The following tables provide detail on all issue status types.

### Open Issues

<table border="1" cellpadding="5" cellspacing="0" width="50%"> 
<tbody> 
<tr> 
<th>Status</th> 
<th>Description</th> 
</tr> 
<tr> 
<td></td>
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
<td></td>
<td>Fixed in associated milestone (could be a future release of GWT)</td>
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

## Other Stuff

The states and lifecycle above are how we generally try to track software. However, GWT contains a lot of software and gets a correspondingly large number of issues. As a result, sometimes issues don't make it through all the states in a formal progression. We do try to keep the system up to date, but we tend to do so in periodic "issue sweeps" where we review the database and make updates.

Occasionally, we make tweaks to the list of issue states and the lifecycle described above. When we do this, we'll update this page as well.
