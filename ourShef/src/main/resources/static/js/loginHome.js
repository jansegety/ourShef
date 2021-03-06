
/*##################### LoginHome ########################*/
$(function(){
  /* slcik.js : history*/
  $('.user-spot-slider').slick({
    dots: false,
    infinite: false,
    autoplay: false,
    autoplaySpeed: 500,
    speed: 1500,
    slidesToShow: 3,
    slidesToScroll: 2,
    responsive: [
      {
        breakpoint: 1024,
        settings: {
          slidesToShow: 2,
          slidesToScroll: 1,
          infinite: true,
          dots: false
        }
      },
      {
        breakpoint: 600,
        settings: {
          slidesToShow: 1,
          slidesToScroll: 1
        }
      },
      {
        breakpoint: 480,
        settings: {
          slidesToShow: 1,
          slidesToScroll: 1
        }
      }
      // You can unslick at a given breakpoint now by adding:
      // settings: "unslick"
      // instead of a settings object
    ]
  })  

})

/*################ Reliability System ##################*/
reliabilitySystemByClass("reliabilitySystem");
starPointSystemByClassForView("starPointSystem");
