// shef 이미지 초기화
const demoImageElements = document.getElementsByClassName("demo-img");
for (var i = 0; i < demoImageElements.length; i++) {
	demoImageElements[i].src = "../../static/img/shefmark.jpg";
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
/////for Id/////
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
/////By Class For View/////
function starPointSystemByClassForView(starPointSystemClass) {

	const spsElements = document.getElementsByClassName(starPointSystemClass);

	for (let i = 0; i < spsElements.length; i++) {
		
		const inputElement = spsElements[i].getElementsByClassName(
			"input-star-point"
		)[0];
		const frontStarBar = spsElements[i].getElementsByClassName("star-front")[0];
		let frontStarSize = inputElement.value;

		if(frontStarSize == -1)
			frontStarBar.style.width = "50%";

		frontStarBar.style.width = frontStarSize + "%";

	}


}


/*// ///// StarPointSystem Function end///////*/
/////////////reliabilitySystem Start////////////
/*// HTM을 받는 순간 변화시킨다. event listener가 아니다.*/
///////////// for Id ///////////////////////
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
			.innerText = "noVisits"
		frontBar.style.width = "0";
		return;
	}

	frontBar.style.width = reliabilitySize + "%";
}
////////////for Class////////////////
function reliabilitySystemByClass(reliablilitySystemClass) {
	const relElements = document.getElementsByClassName(reliablilitySystemClass);
	
	
	for (let i = 0; i < relElements.length; i++) {
		const inputElement = relElements[i].getElementsByClassName(
			"input-reliability-size"
		)[0];
		const frontBar = relElements[i].getElementsByClassName("front")[0];
		let reliabilitySize = inputElement.value;

		if (reliabilitySize == -1) { //input 값이 -1인 경우 맛집 등록을 안 한 것으로 판단
			relElements[i]
				.getElementsByClassName('reliabilityText')[0]
				.innerText = "noVisits"
			frontBar.style.width = "0";
			continue;
		}

		frontBar.style.width = reliabilitySize + "%";
	}
}
///////////////reliabilitySystem End//////////////