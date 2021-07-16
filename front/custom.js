
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

/*####################### joinFomr ##############################*/

let input = document.getElementById("join-profile-img-file");
let preview = document.getElementById("join-profile-img");
input.addEventListener("change", showImageFile, false);
console.log(input);
console.log(preview);


function showImageFile(){
  const selectedFile = input.files[0];
  const showImg = preview;
  const url = URL.createObjectURL(input.files[0]);
  showImg.src = url;
}




