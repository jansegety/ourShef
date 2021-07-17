/*####################### joinForm ##############################*/

// 프로필 등록 누르면 파일 선택창 뜨기
let profileBtn = document.getElementById("btn-profile-img");
profileBtn.addEventListener("click", popChooseFile, false);

function popChooseFile(){
  var input = document.getElementById("join-profile-img-file");
  var event = new MouseEvent('click');
  input.dispatchEvent(event);
};

// 사진 미리보기
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
};