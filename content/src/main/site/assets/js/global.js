$(function () {
    // FlexNav
    $(".flexnav").flexNav();

    // Ensure homepage is styled correctly when loading full HTML file
    styleHomepage();

    if ("/overview.html" == window.location.pathname) {
        document.getElementById("submenu").style.display = "none";
    }
});

function styleHomepage() {
    var windowHeight = Math.max(document.documentElement.clientHeight, window.innerHeight || 0);

    var sections = document
        .getElementById("content")
        .firstElementChild
        .getElementsByTagName("section");
    for (var i = 0; i < sections.length; i++) {
        var section = sections[i];
        var style = section.style;

        style.height = windowHeight + "px";
        style.padding = "0";

        var containers = section.getElementsByClassName("container");
        var container = containers[0];
        container.style.paddingTop = (windowHeight - container.clientHeight) / 2 + "px";
    }
}
