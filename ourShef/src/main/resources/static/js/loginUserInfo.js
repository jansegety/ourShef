/*####################### loginUserInfo ##############################*/

showHideSystem("modify-password-frame","btn-modify-password", "summary-modify-password");

// 프로필 등록 누르면 파일 선택창 뜨기
let profileBtn = document.getElementById("btn-profile-img");
profileBtn.addEventListener("click", popChooseFile, false);

function popChooseFile(){
  var input = document.getElementById("profileImgFile");
  var event = new MouseEvent('click');
  input.dispatchEvent(event);
};

//////////Loading System + Image File Processing///////////
////loading while Image processing to be uploaded (validation -> change extension -> resizing)
//@param1 : inputId
//@Param2 : type (ex : "profile", "addedSpot", "mainSpot"), 
//@Param3 : previewId(when type=addedSpot Enter spotAreaId instead)
//@Param4 : loaderEl Class
loadingWhenImageFileProcessing("profileImgFile", "profile", "profile-img", "loader");

//Confirmation of intent to delete account
let deleteButton = document.getElementsByClassName("btn-left")[0];
deleteButton.onclick = dispatchSpotToBeDeleted;

function dispatchSpotToBeDeleted(){

  if(confirm("정말 계정을 삭제하시겠습니까? 이 작업은 돌이킬 수 없습니다")==true)
  {
    window.location.href="/login/withdraw";
  }

}