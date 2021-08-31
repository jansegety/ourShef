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

//when upload file, erase demo-image, show uplaoded img
let inputMainImg = document.getElementById("spotMainImg");
inputMainImg.addEventListener('change', preViewImg, false);

function preViewImg(event) {
    let mainImg = document.getElementById("main-spot-img");
    var preViewElement = document.getElementById("spotMainImg");
    var imgFile = preViewElement.files[0];

    if (fileExtensionCheck(imgFile, ["jpg", "jpeg", "png", "heic"])) {
        mainImg.src = URL.createObjectURL(imgFile);
        mainImg
            .classList
            .remove("demo-img");
        event.stopPropagation();
    } else {
        alert("이미지 파일만 가능합니다.");
        document
            .getElementById("spotMainImg")
            .value = "";
    }

}

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

//when upload file, erase demo-image, show uplaoded img
inputMainImg = document.getElementById("spotAddedImgs");
inputMainImg.addEventListener('change', preViewImg2, false);

function preViewImg2(event) {
    let addedImgs = document.getElementsByClassName("spot-img");
    let spotArea = document.getElementsByClassName("spot-imgs-area")[0];
    var preViewElement = document.getElementById("spotAddedImgs");
    var files = preViewElement.files;

	//Delete the created Img preview
	while(spotArea.hasChildNodes())
	{
		spotArea.removeChild(spotArea.firstChild);
	}

    //파일 확장자 validation 만약 파일이 6개 이상 들어오면 오류 팝업
    if (files.length > 6) {
        alert("파일은 6개까지 올릴 수 있습니다.");
        document
            .getElementById("spotAddedImgs")
            .value = "";
        return;
    }

    for (var i = 0; i < files.length; i++) {
        if (fileExtensionCheck(files[i], ["jpg", "jpeg", "png", "heic"])) {
            //프레임 태그 생성
            var newDiv = document.createElement("div");
            newDiv.setAttribute("class", "spot-img-frame");
            //이미지 태그 생성
            var newImg = document.createElement("img");
            newImg.setAttribute("class", "spot-img");
            //프레임 + 이미지 태그
            newDiv.appendChild(newImg);
            //스폿 이미지 영역에 추가
            spotArea.appendChild(newDiv);
            newImg.src = URL.createObjectURL(files[i]);
            console.log("태그 생성!");
        } else {
            alert("이미지 파일만 가능합니다.");
            document
                .getElementById("spotAddedImgs")
                .value = "";
            while (spotArea.hasChildNodes()) { //자식 노드 삭제
                spotArea.removeChild(spotArea.firstChild);
            }
            return;
        }

    }
    event.stopPropagation();
}

//pontStarSystem

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
