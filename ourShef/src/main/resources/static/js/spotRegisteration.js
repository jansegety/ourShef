/* ####################### spotRegisteration ############################ */

// ////############ main spot image ############# When click the button, pop up
// File selector
let be = document.getElementById('btn-main-img');
be.addEventListener("click", popFileSelector1, false);

function popFileSelector1(event) {
    var inputMainImg = document.getElementById("input-main-img");
    var e = new MouseEvent("click");
    inputMainImg.dispatchEvent(e);
    event.stopPropagation();
};


////loading while Image processing to be uploaded (validation -> change extension -> resizing)
//@param1 : inputId
//@Param2 : type (ex : "profile", "addedSpot", "mainSpot"), 
//@Param3 : previewId(when type=addedSpot Enter spotAreaId instead)
//@Param4 : loaderEl Class
loadingWhenImageFileProcessing("input-main-img", "mainSpot", "main-spot-img", "loader");




// /////################ added spot image #################### When click the
// button, pop up File selector
be = document.getElementById('btn-added-imgs');
be.addEventListener("click", popFileSelector2, false);

function popFileSelector2(event) {
    var inputMainImg = document.getElementById("input-added-imgs");
    var e = new MouseEvent("click");
    inputMainImg.dispatchEvent(e);
    event.stopPropagation();
};


//////////Loading System + Image File Processing///////////
////loading while Image processing to be uploaded (validation -> change extension -> resizing)
//@param1 : inputId
//@Param2 : type (ex : "profile", "addedSpot", "mainSpot"), 
//@Param3 : previewId(when type=addedSpot Enter spotAreaId instead)
//@Param4 : loaderEl Class
loadingWhenImageFileProcessing("input-added-imgs", "addedSpot", "spot-imgs-area", "loader");



//starPointSystem
starPointSystem("startPointSystem-i1");

