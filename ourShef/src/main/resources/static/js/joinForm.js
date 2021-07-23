/*####################### joinForm ##############################*/

// 프로필 등록 누르면 파일 선택창 뜨기
let profileBtn = document.getElementById("joinForm-btn-profile-img");
profileBtn.addEventListener("click", popChooseFile, false);

function popChooseFile(){
  var input = document.getElementById("joinFormProfileImgFile");
  var event = new MouseEvent('click');
  input.dispatchEvent(event);
};

// 사진 미리보기
let input = document.getElementById("joinFormProfileImgFile");
let preview = document.getElementById("joinForm-profile-img");
input.addEventListener("change", showImageFile, false);


function showImageFile(){
  const selectedFile = input.files[0];
  const showImg = preview;
  const url = URL.createObjectURL(input.files[0]);
  showImg.src = url;
};
