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

    // Homepage
    if($('#content').hasClass('home')){
        styleHome();
        $(window).resize(function(){
            styleHome();
        });

        // Pager
        $('.next, .pager a').click(function(e) {
            e.preventDefault();
            $('html, body').animate({
                scrollTop: $($(this).attr('href')).offset().top
            }, 600);
        });

        $('.pager a').click(function(e) {
            e.preventDefault();
            $('.pager a').removeClass("active");
            $(this).addClass("active");
        });

        $(window).scroll(function () {
            $('.pager a').removeClass("active");
            if ($(window).scrollTop() + 100 > $('#letsbegin').offset().top) {
                $('.pager a:nth-child(3)').addClass("active");
            } else if ($(window).scrollTop() + 100 > $('#gwt').offset().top) {
                $('.pager a:nth-child(2)').addClass("active");
            } else {
                $('.pager a:nth-child(1)').addClass("active");
            }
        });
    }

});

function styleHome(){
    var wh = $(window).height();
    var sh = $("#letsbegin").height();

    if(wh > sh){
        $(".home section").each(function(){
            $(this).css({
                height: wh,
                padding: 0
            });
            var c = $(this).find(".container");
            c.css("padding-top", (wh - c.height()) / 2);
        });
    }
}