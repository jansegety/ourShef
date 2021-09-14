
/*####################### joinForm ##############################*/

// 프로필 등록 누르면 파일 선택창 뜨기
let profileBtn = document.getElementById("joinForm-btn-profile-img");
profileBtn.addEventListener("click", popChooseFile, false);

function popChooseFile() {
	var input = document.getElementById("joinFormProfileImgFile");
	var event = new MouseEvent('click');
	input.dispatchEvent(event);
};



////loading while Image processing to be uploaded (validation -> change extension -> resizing)
//@param1 : inputId
//@Param2 : type (ex : "profile", "addedSpot", "mainSpot"), 
//@Param3 : previewId(when type=addedSpot Enter spotAreaId instead)
//@Param4 : loaderEl Id
loadingWhenImageFileProcessing("joinFormProfileImgFile", "profile", "joinForm-profile-img", "loader");	

