GWT News
===

This news page is the official source for update from the GWT team or GWT related projects. Anything important will be posted here. This page will give you an impression about things happening in the GWT community. 

In case you have a news worth for showing here, [open an issue on our tracker](https://github.com/gwtproject/gwt-site/issues/new/choose).
We will add the news as soon as possible.

<style>
.container-list-news {
    display: flex;
    flex-direction: column; 
    margin-top: 4em; 
    grid-gap: 1.5em;
    gap: 1.5em;
    box-sizing: border-box;
    /*border: 0 solid white;*/
    width: 100%
}

.container-news {
    padding: 1em;
    border: 1px solid darkgrey;
    border-radius: 1em;
    background: white;
}

.container-headline {
    color: #f93535;
    text-decoration: none;
    font-weight: bold;
    font-size: 160%;    
}

.container-date {
    padding: 1em 0 0 0;
    font-weight: bold;
    color: #9e9d9d;
}

.container-text {
    padding: 1em 0 0 0;
    color: black;
}

.container-link {
    padding: 1em 0 0 0;
    font-weight: bold;
    color: #9e9d9d;
}
</style>
<div class="container-list-news">
    <div class="container-news">
        <a href="https://matrix.to/#/#gwtproject_gwt:gitter.im" target="_blank">
            <div class="container-headline">
                GWT: 2.11.0 released!
            </div>
        </a>
        <div class="container-date">
            January 9, 2023
        </div>
        <div class="container-text">
            This release fixes a security issue. If you use GWT-RPC and JPA/JDO annotations in your project, we strongly suggest updating at least to 2.10.1 as soon as possible. To ensure that vulnerable projects are aware of any problem, compile warnings will be emitted if a problem is detected, and the server will default to not allowing any vulnerable RemoteService instances.<br><br>See <a href="https://github.com/gwtproject/gwt/issues/9709" target="_blank">https://github.com/gwtproject/gwt/issues/9709</a> for information on the issue, how we're responding to it, and how any additional follow-up might look.
            <br><br>
            Other highlights:
            <br>
            <ul style="color:black; margin-left:40px;">
                <li>Transitioned to GitHub pull requests for new contributions, with nightly builds running on GitHub Actions.</li>
                <li>Added release artifacts for jakarta.servlet packages for both RequestFactory and GWT-RPC.</li>
                <li>Tested support for running on Java 21. This is likely to be the final minor release series to support running on Java 8.</li>
                <li>Updated JRE emulation to support Java 11 for Collections, streams, and more.</li>
            </ul>
            See <a href="https://github.com/gwtproject/gwt/releases/tag/2.11.0" target="_blank">https://github.com/gwtproject/gwt/releases/tag/2.11.0</a> or <a href="https://www.gwtproject.org/release-notes.html#Release_Notes_2_11_0" target="_blank">https://www.gwtproject.org/release-notes.html#Release_Notes_2_11_0</a> for complete release notes.
        </div>
        <div class="container-text">
            This is our third release under the new groupId, be sure when you update to change away from "com.google.gwt", as it will not get more updates.
        </div>
       <div class="container-text">
            This release wouldn't have been possible without help from so many contributors, including developers, testers, and sponsors. A short list of the teams and individuals that directly brought us this release: Juan Pablo Gardella, Rocco De Angelis, Frank Hossfeld, Manfred Tremmel, Jim Douglas, Zbynek Konecny, Piotr Lewandowski, Axel Uhl, Thomas Broyer, Filipe Sousa, Sandra Parsick, Jens Nehlmeier, Schubec GmbH, Tom Sawyer Software, Insurance Insight Inc.<br><br>Join us on the <a href="https://github.com/gwtproject/gwt/issues" target="_blank">issue tracker</a> or at our <a href="https://opencollective.com/gwt-project" target="_blank">OpenCollective page</a> to help make future releases possible.
        </div>
    </div>
    <div class="container-news">
        <a href="https://matrix.to/#/#gwtproject_gwt:gitter.im" target="_blank">
            <div class="container-headline">
                GWT: 2.10.1 released!
            </div>
        </a>
        <div class="container-date">
            January 9, 2023
        </div>
        <div class="container-text">
            This release fixes a security issue. If you use GWT-RPC and JPA/JDO annotations in your project, we strongly suggest updating at least to 2.10.1 as soon as possible. To ensure that vulnerable projects are aware of any problem, compile warnings will be emitted if a problem is detected, and the server will default to not allowing any vulnerable RemoteService instances.<br><br>See <a href="https://github.com/gwtproject/gwt/issues/9709" target="_blank">https://github.com/gwtproject/gwt/issues/9709</a> for information on the issue, how we're responding to it, and how any additional follow-up might look.
        </div>
        <div class="container-text">
            This is our second release under the new groupId, be sure when you update to change away from "com.google.gwt", as it will not get more updates.
        </div>
    </div>
    <div class="container-news">
        <a href="https://matrix.to/#/#gwtproject_gwt:gitter.im" target="_blank">
            <div class="container-headline">
                GWT: we are looking for reviewers!
            </div>
        </a>
        <div class="container-date">
            December 9, 2023
        </div>
        <div class="container-text">
            To finish the work on the next GWT release, we need to merge two more pull requests. We are looking for reviewers to approve the changes. Once the pull requests are merged, we will release GWT 2.11.0!
        </div>
           <a href="https://matrix.to/#/#gwtproject_gwt:gitter.im" target="_blank">
            <div class="container-link">
                Let me help 
            </div>
        </a>
    </div>
    <div class="container-news">
        <a href="https://github.com/Vertispan/j2clmavenplugin/releases/tag/v0.22.0" target="_blank">
            <div class="container-headline">
                J2CL: new version of the j2cl-maven-plugin released
            </div>
        </a>
        <div class="container-date">
            November 9, 2023
        </div>
        <div class="container-text">
            This update let the plugin use the latest J2CL version and supports elemental2 vesion 1.2.0. The release fixes a NPE, updates J2CL and the J2CL plugin to the latest version among other improvements.
        </div>
           <a href="https://github.com/Vertispan/j2clmavenplugin/releases/tag/v0.22.0" target="_blank">
            <div class="container-link">
                Read more
            </div>
        </a>
    </div>
    <div class="container-news">
        <a href="https://github.com/gwtproject/gwt-core/releases/tag/v1.0.0-RC2" target="_blank">
            <div class="container-headline">
                GWT Module: gwt-core version 1.0.0-rc2 released
            </div>
        </a>
        <div class="container-date">
            October 20, 2023
        </div>
        <div class="container-text">
            The second release candidate of the gwt-core module has been released today.
        </div>
        <a href="https://github.com/gwtproject/gwt-core/releases/tag/v1.0.0-RC2" target="_blank">
            <div class="container-link">
                Read more
            </div>
        </a>
  </div>
</div>




