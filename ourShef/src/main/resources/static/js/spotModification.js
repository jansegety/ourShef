/* ####################### spotModification ############################ */

// ////############ main spot image ############# When click the button, pop up
// File selector
let be = document.getElementById('btn-main-img');
be.addEventListener("click", popFileSelector1, false);

function popFileSelector1(event) {
    var inputMainImg = document.getElementById("spotMainImg");
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
loadingWhenImageFileProcessing("spotMainImg", "mainSpot", "main-spot-img", "loader");

// /////################ added spot image #################### When click the
// button, pop up File selector
be = document.getElementById('btn-added-imgs');
be.addEventListener("click", popFileSelector2, false);

function popFileSelector2(event) {
    var inputMainImg = document.getElementById("spotAddedImgs");
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
loadingWhenImageFileProcessing("spotAddedImgs", "addedSpot", "spot-imgs-area", "loader");

//starPointSystem
starPointSystem("starPointSystem-i1");
starPointSystemByClassForView("starPointSystem");

//Confirmation of intent to delete
function dispatchSpotToBeDeleted(form){

  if(confirm("정말 삭제하시겠습니까? 이 작업은 돌이킬 수 없습니다")==true)
  {
    sumitFormToDelete(form);
  }

}

//submit Form to Delete
function sumitFormToDelete (form){
	form.action="/spot/delete";
	form.submit();
} 
