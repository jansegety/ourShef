// shef 이미지 초기화
const demoImageElements = document.getElementsByClassName("demo-img");
for (var i = 0; i < demoImageElements.length; i++) {
    demoImageElements[i].src = "http://localhost:5501/img/shefmark.jpg";
}

// 확장자 검사기 file extension check, if file's extentsion exist in list, the
// function retrun true ex: fileExtensionCheck(fileObj, ["jpg", "png"])
function fileExtensionCheck(fileObj, extensionList) {
    var fileName = fileObj.name;
    var dotIndex = fileName.lastIndexOf('.');

    //확장자 명만 추출한 후 소문자로 변경
    var fileExt = fileName
        .substring(dotIndex + 1)
        .toLowerCase();
    console.log(fileExt);

    //리스트중 하나라도 확장자가 같다면 true 반환
    var check = false;
    for (var i = 0; i < extensionList.length; i++) {
        if (extensionList[i] == fileExt) 
            check = true;
        }
    return check
}

/////// StarPointSystem Function start///////

function starPointSystem(starPointSystemId) {

    const spsElement = document.getElementById(starPointSystemId);

    //point with click on the Star point System
    let arrNum = spsElement
        .getElementsByClassName("click-area")
        .length;
    for (var i = 0; i < arrNum; i++) {
        // i is click-area index use Closer
        setEventListenerPointByClick(i);
    }

    function setEventListenerPointByClick(idx) {

        var temp = spsElement.getElementsByClassName("click-area")[idx];
        temp.addEventListener("click", update, false);

        function update(event) {
            //updat input value
            var score = (idx + 1) * 10;
            spsElement
                .getElementsByClassName("input-star-point")[0]
                .value = score;

            //star pointer point link with input value
            let starFront = spsElement.getElementsByClassName("star-front")[0];
            starFront.style.width = score + "%";

            event.stopPropagation();
        }

    }
}

/*// ///// StarPointSystem Function end///////*/
/////////////reliabilitySystem Start////////////
/*// HTM을 받는 순간 변화시킨다. event listener가 아니다.*/
function reliabilitySystem(reliablilitySystemId) {
    const relElement = document.getElementById(reliablilitySystemId);
    const inputElement = relElement.getElementsByClassName(
        "input-reliability-size"
    )[0];
    const frontBar = relElement.getElementsByClassName("front")[0];
    let reliabilitySize = inputElement.value;

    if (reliabilitySize == -1) { //input 값이 -1인 경우 맛집 등록을 안 한 것으로 판단
        relElement
            .getElementsByClassName('reliabilityText')[0]
            .innerText = "NewBe"
        frontBar.style.width = "0";
        return;
    }

    frontBar.style.width = reliabilitySize + "%";
}
///////////////reliabilitySystem End//////////////