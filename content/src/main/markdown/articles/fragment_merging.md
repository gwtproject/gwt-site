Fragment Merging
===

_Alan Leung, Software Engineer_

_Updated June 2012_

 As the functionality grows in a large GWT application, developers tend to add more
split points (calls to GWT.runAsync), to ensure that the size of the initial fragment is
as small as possible. Keeping the initial fragment small ensures that application starts as
quickly as possible.

 However, adding more split points also increases the likelihood that two fragments share
common code.

![img](../images/fragment_merging_before.jpg)

 As shown in this diagram, the GWT compiler creates an exclusive fragment for each split
point and adds any shared code to the leftovers fragment. The result is that the project's
leftovers fragment gradually gets bigger as developers add more split points. While this has
no effect in the initial loading time of the project, the latency of the first requested
split point usually suffers.

 New to GWT 2.5 is **fragment merging**. We gave the code splitter the ability to
automatically merge multiple exclusive fragments into a single fragment.

![img](../images/fragment_merging_after.jpg)

 This diagram shows the situation after the compiler realized that E1 and E2 share enough code
and automatically bundled those two split points into a single fragment, effectively pulling the
shared code out of the leftovers fragment. When the application needs either E1 or E2, it downloads
the whole bundle. Not only does this decrease the leftovers fragment's size, it also decreases the
total number of HTTP requests in a user session, assuming that many fragments will eventually
be needed.

 To enable this feature, simply add -XfragmentCount x to the GWT compiler command line and the
code splitter will try to limit the number of exclusive fragments to x. Here, x is the lower bound
on the number of exclusive fragments. The actual number might be greater than x when the compiler
decides it might not be beneficial to merge as many split points as suggested.
