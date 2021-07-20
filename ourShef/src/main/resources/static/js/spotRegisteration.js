/* ####################### spotRegisteration ############################ */ 

//////############ main spot image #############
//When click the button, pop up File selector
let be = document.getElementById('btn-main-img');
be.addEventListener("click", popFileSelector1, false);

function popFileSelector1(event){
  var inputMainImg = document.getElementById("input-main-img");
  var e = new MouseEvent("click");
  inputMainImg.dispatchEvent(e);
  event.stopPropagation();
};

//when upload file, erase demo-image, show uplaoded img
let inputMainImg = document.getElementById("input-main-img");
inputMainImg.addEventListener('change', preViewImg, false);

function preViewImg(event){
  let mainImg = document.getElementById("main-spot-img");
  var preViewElement = document.getElementById("input-main-img");
  var imgFile = preViewElement.files[0];

  if(fileExtensionCheck(imgFile, ["jpg", "png"]))
  {
    mainImg.src = URL.createObjectURL(imgFile);
    mainImg.classList.remove("demo-img");
    event.stopPropagation();
  }
  else{
    alert("이미지 파일만 가능합니다.");
    document.getElementById("input-main-img").value="";
  }
  
  
}

///////################ added spot image ####################
//When click the button, pop up File selector
be = document.getElementById('btn-added-imgs');
be.addEventListener("click", popFileSelector2, false);

function popFileSelector2(event){
  var inputMainImg = document.getElementById("input-added-imgs");
  var e = new MouseEvent("click");
  inputMainImg.dispatchEvent(e);
  event.stopPropagation();
};

//when upload file, erase demo-image, show uplaoded img
inputMainImg = document.getElementById("input-added-imgs");
inputMainImg.addEventListener('change', preViewImg2, false);

function preViewImg2(event){
  let addedImgs = document.getElementsByClassName("spot-img");
  let spotArea = document.getElementsByClassName("spot-imgs-area")[0];
  var preViewElement = document.getElementById("input-added-imgs");
  var files = preViewElement.files;

  //파일 확장자 validation

  //만약 파일이 6개 이상 들어오면 오류 팝업
  if(files.length > 6)
  {
    alert("파일은 6개까지 올릴 수 있습니다.");
    document.getElementById("input-added-imgs").value="";
    return;
  }

  for(var i=0; i < files.length ; i++)
  {
    if(fileExtensionCheck(files[i], ["jpg", "png"]))
    {
      //프레임 태그 생성
      var newDiv = document.createElement("div");
      newDiv.setAttribute("class","spot-img-frame");
      //이미지 태그 생성
      var newImg = document.createElement("img");
      newImg.setAttribute("class","spot-img");
      //프레임 + 이미지 태그
      newDiv.appendChild(newImg);
      //스폿 이미지 영역에 추가
      spotArea.appendChild(newDiv);
      newImg.src = URL.createObjectURL(files[i]);
      console.log("태그 생성!");
    }
    else{
      alert("이미지 파일만 가능합니다.");
      document.getElementById("input-added-imgs").value="";
      while(spotArea.hasChildNodes()) //자식 노드 삭제
      {
        spotArea.removeChild(spotArea.firstChild);
      }
      return;
    }

  }
  event.stopPropagation();
}

//############### Star point system Start#####################

//point with click on the Star point System
let arrNum = document.getElementById("star-point-area-i1").getElementsByClassName("click-area").length;
for(var i=0; i<arrNum; i++)
{
  // i is click-area index //use Closer
  setEventListenerPointByClick(i);
}


function setEventListenerPointByClick(idx){

  var temp = document.getElementById("star-point-area-i1").getElementsByClassName("click-area")[idx];
  temp.addEventListener("click",update ,false);

  function update(event){
  //updat input value
  var score = (idx+1)*10;
  document.getElementById("input-star-point-i1").value = score;  

  //star pointer point link with input value
  let starFront = document.getElementById("star-point-area-i1").getElementsByClassName("star-front")[0];
  starFront.style.width = score + "%";

  event.stopPropagation();
  }
 
}

//############### Star point system End#####################