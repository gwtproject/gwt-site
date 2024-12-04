<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
        <meta name="viewport" content="width=device-width, initial-scale=1"/>

        <title>GWT Project</title>

        <link rel="stylesheet" href="assets/build/css/style.css">
        <link rel="stylesheet" href="assets/build/fonts/icons/icons.css">
        <link rel="shortcut icon" type="image/x-icon" href="/images/favicon.png">

        <script type="text/javascript" src="https://use.typekit.net/wwv3dxt.js"></script>
        <script type="text/javascript">try{Typekit.load();}catch(e){}</script>

        <script type="text/javascript" src="/gwtproject/gwtproject.nocache.js"></script>
        <script>
            (function () {
                var cx = '008698606704604874043:or32ypbnrfu';
                var gcse = document.createElement('script');
                gcse.type = 'text/javascript';
                gcse.async = true;
                gcse.src = (document.location.protocol == 'https:' ? 'https:' : 'http:') +
                        '//www.google.com/cse/cse.js?cx=' + cx;
                var s = document.getElementsByTagName('script')[0];
                s.parentNode.insertBefore(gcse, s);
            })();
        </script>
    </head>

    <body>

        <!--[if lt IE 7]>
            <p class="browsehappy">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade your browser</a> to improve your experience.</p>
        <![endif]-->

        <nav role="navigation" id="nav" class="closed">
            <div class="wrapper">
                <ul>
                    <li class="logo"><a href="/">GWT homepage</a></li>
                    <li><a href="overview.html"><i class="icon_overview"></i>Overview</a></li>
                    <li class="sep"><a href="gettingstarted-v2.html"><i class="icon_getstarted"></i>Get started</a></li>
                    <li><a href="doc/latest/tutorial/index.html" class="active"><i class="icon_tutorials"></i>Tutorials</a></li>
                    <li><a href="doc/latest/DevGuide.html"><i class="icon_doc"></i>Docs</a></li>
                    <li><a href="examples.html"><i class="icon_ressources"></i>Resources</a></li>
                    <li><a href="makinggwtbetter.html"><i class="icon_makeGWTBetter"></i>Make GWT Better</a></li>
                    <li class="sep"><a href="terms.html"><i class="icon_terms"></i>Terms</a></li>
                    <li class="btn"><a href="download.html"><i class="icon_download"></i>Download</a></li>
                    <li class="btn"><a href="https://www.gwtproject.org/javadoc/latest/"><i class="icon_jd"></i>Java Doc</a></li>
                </ul>
                <div id="social">
                    <a href="https://gitter.im/gwtproject/gwt" target="_blank" title="Access our Gitter community">
                        <img src="images/gitter-logo-mark-white.png" height="100%" alt="GWT Gitter community"></img>
                    </a>
                </div>
                <a id="cc" href="#">Creative Commons Attribution 3.0 License.</a>
            </div>
        </nav>

        <div class="holder">
            <aside id="submenu">
                <nav>
                    $toc
                </nav>
            </aside>

            <a id="logo-mobile" href="#"><span>>Back to GWT homepage</span></a>

            <div class="menu-button">Menu</div>

            <nav id="nav-mobile" class="flexnav">
                $toc
            </nav>

            <div id="search">
                <form onsubmit="javascript:void(0)">
                    <input type="text"/>
                    <button type="submit"><i class="icon_search"></i> <span>Search</span></button>
                </form>
            </div>

            <div style="visibility:hidden">
                <gcse:searchresults-only gname="searchresults"></gcse:searchresults-only>
            </div>

            <div id="content" role="main">
                <div class="container">
                    <span id="editLink" title="Edit this page on GitHub">
                        $editLink
                    </span>
                    $content

                </div>
            </div>
        </div>

        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
        <script>window.jQuery || document.write("<script src='assets/build/js/vendor/jquery-1.11.0.min.js'><\/script>")</script>

        <script src="/assets/build/js/vendor/jquery.flexnav.js"></script>
        <script src="/assets/build/js/global.min.js"></script>

    </body>
</html>
