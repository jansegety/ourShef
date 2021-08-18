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

// 사진 미리보기
let input = document.getElementById("profileImgFile");
let preview = document.getElementById("profile-img");
input.addEventListener("change", showImageFile, false);


function showImageFile(){
  const selectedFile = input.files[0];
  const showImg = preview;
  const url = URL.createObjectURL(input.files[0]);
  showImg.src = url;
};


//Confirmation of intent to delete account
let deleteButton = document.getElementsByClassName("btn-left")[0];
deleteButton.onclick = dispatchSpotToBeDeleted;

function dispatchSpotToBeDeleted(){

  if(confirm("정말 계정을 삭제하시겠습니까? 이 작업은 돌이킬 수 없습니다")==true)
  {
    window.location.href="/";
  }

}