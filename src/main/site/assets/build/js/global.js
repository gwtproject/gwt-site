var speed = 200;

$(function() {

    //setTimeout("menuClose()", 500);

    $( "#nav").not(".alwaysOpen")
    .mouseenter(function() {
        $(this).removeClass("closed");
    })
    .mouseleave(function() {
        $(this).addClass("closed");
    });

    // FlexNav
    $(".flexnav").flexNav();

});