// shef 이미지 초기화
const demoImageElements = document.getElementsByClassName("demo-img");
for (var i = 0; i < demoImageElements.length; i++){
  demoImageElements[i].src = "http://localhost:8080/img/shefmark.jpg";
}


//확장자 검사기
//file extension check, if file's extentsion exist in list, the function retrun true
//ex: fileExtensionCheck(fileObj, ["jpg", "png"])
function fileExtensionCheck (fileObj, extensionList)
{
  var fileName = fileObj.name;
  var dotIndex = fileName.lastIndexOf('.');

  //확장자 명만 추출한 후 소문자로 변경
  var fileExt = fileName.substring(dotIndex+1).toLowerCase();
  console.log(fileExt);

  //리스트중 하나라도 확장자가 같다면 true 반환
  var check = false;
  for(var i = 0; i< extensionList.length; i++){
    if(extensionList[i] == fileExt)
      check = true;
  }
  return check
}