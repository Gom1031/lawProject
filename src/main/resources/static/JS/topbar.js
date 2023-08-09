$(function() {
    console.log("topbar.js");
    var lnb = $("#lnb").offset().top;
    $(window).scroll(function() {
        var windowTop = $(this).scrollTop();
  
        if(lnb <= windowTop) {
            $("#lnb").addClass("fixed");
        } else {
            $("#lnb").removeClass("fixed");
        }
    })
  });