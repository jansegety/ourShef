//////////Loading System + Image File Processing///////////
////loading while Image processing to be uploaded (validation -> change extension -> resizing)
//@param1 : inputId
//@Param2 : type (ex : "profile", "addedSpot", "mainSpot"), 
//@Param3 : previewId(when type=addedSpot Enter spotAreaId instead)
//@Param4 : loaderEl Class
function loadingWhenImageFileProcessing(inputElId, ImgType, previewId, loaderClass)
{
	document.getElementById(inputElId).addEventListener("change", 
	async (event) => {
		var loader = document.getElementsByClassName(loaderClass)[0];
		
		try{
			loader.style.visibility="visible";
			loader.style.opacity = 1;
			
		
		await imageFileProcessing(event.target, ImgType, previewId);
		
		}
		catch (e){
			console.error("파일 변환 과정에 이상이 발생했습니다. ", e);
			
		}
		finally	{
			loader.style.opacity = 0;
			loader.style.visibility="hidden";
		
		}
		
		
	}	
	, false);
}




//////////Image File Processing (validation -> change extension -> resizing)//////
async function imageFileProcessing(inputEl, ImgType, previewId){
	return new Promise(async (resolve)=>{
		initPreview(ImgType, previewId);
	
		if(validateImgFiles(inputEl,  ImgType))
		{
			validateImgFiles(inputEl);
			await convertHeicToJpg(inputEl);
			//@param1 : input, @Param2 : imgEl_ID for preview, @Param3 : resizingType (ex : "profile", "addedSpot", "mainSpot")	
			await resizeJpgFiles(inputEl, ImgType, previewId); 
		
		}
		
		resolve();
	});
	
}

//////////////initPreview////////////////////
function initPreview(imgType, previewId){
	
	if(imgType === "addedSpot")
	{
		let spotArea = document.getElementById(previewId);
		
		//Delete the created Img preview
		while(spotArea.hasChildNodes())
		{
			spotArea.removeChild(spotArea.firstChild);
		}	
		
	}
	
}

//////////////validate Img File////////////////////
function validateImgFiles(inputEl, imgType){
	
	var files = inputEl.files;
	
	//validate
	
	if(imgType === "addedSpot")
	{
			if (files.length > 6) {
	        alert("파일은 6개까지 올릴 수 있습니다.");
	       inputEl.value = "";
	        return false;
    	}
	}
	
	for(var i=0; i < files.length; i++)
	{
		if(!fileExtensionCheck(files[i], ["jpg", "jpeg", "png", "heic"]))
		{
			alert("이미지 파일만 올릴 수 있습니다.\n (ex : jpg, png, heic) ");
			inputEl.value = "";
	        return false;
		}
	}
	
	return true;
	
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

	//리스트중 하나라도 확장자가 같다면 true 반환
	var check = false;
	for (var i = 0; i < extensionList.length; i++) {
		if (extensionList[i] == fileExt)
			check = true;
	}
	return check
}


///////////////HEIC file to JPG///////////////////////////
function convertHeicToJpg(inputEl) {
	return new Promise(
		async (resolve)=>{
			let fileInputElement = inputEl;
			var imgFiles = fileInputElement.files;
			let container = new DataTransfer();
			
			console.log("convert Heic to JPG 실행");
		
			for (var i = 0; i < imgFiles.length; i++) {
				var file = imgFiles[i];
				var fileName = imgFiles[i].name;
				var fileNameExt = fileName.substr(fileName.lastIndexOf('.') + 1);
				var fileNameWithOutExt = fileName.substring(0, fileName.lastIndexOf('.'));
		
				//If it is a heic file
				if (fileNameExt == "heic") {
					var blob = file;
		
					await heic2any({
						blob: blob,
						toType: "image/jpeg",
						quality: 0.8
					})
						.then(function(resultBlob) {
		
							//adding converted picture to the original <input type="file">
							let file = new File([resultBlob], fileNameWithOutExt + ".jpg", {type: resultBlob.type, lastModified: new Date().getTime() });
							container.items.add(file);
							console.log("heic -> jpeg 변환 완료 : ",file.name);
		
						})
						.catch(function(x) {
							console.log("HEIC파일 변환에 실패하였습니다.");
						});
		
				} else {
					container.items.add(file);
					console.log("heic 파일이 아닙니다 : ",file.name);
				}
		
			}
			//Put the file converted to JPG
			fileInputElement.files = container.files;
			console.log("convert Heic to JPG 종료");
			resolve();
		}
	);
	

}

///////////////////Resize JPG File /////////////////////
function resizeJpgFiles(inputEl, imgType, previewId){
	return new Promise(
		async (resolve)=>{
			console.log("resizing 작업 시작");
			var files = inputEl.files;
			var toBlobCount = 0;
			let container = new DataTransfer();
		    var canvas = document.createElement("canvas");
		    var ctx = canvas.getContext("2d");
	
			for(var i = 0; i < files.length; i++)
			{
				
				var file = files[i];
				
				
				if(imgType === "profile")
				{
					canvas.width = 200; 
					var sourceImg = await blobToImage(file);
					var reductionRatio = canvas.width / sourceImg.naturalWidth;
					canvas.height = reductionRatio * sourceImg.naturalHeight;
					if(canvas.height >= 300)
					canvas.height = 300;
				}else if(imgType === "addedSpot")
				{
					canvas.width = 300; 
					var sourceImg = await blobToImage(file);
					var reductionRatio = canvas.width / sourceImg.naturalWidth;
					canvas.height = reductionRatio * sourceImg.naturalHeight;
					if(canvas.height >= 500)
					canvas.height = 500;
				}else //mainSpot
				{
					canvas.width = 600; 
					var sourceImg = await blobToImage(file);
					var reductionRatio = canvas.width / sourceImg.naturalWidth;
					canvas.height = reductionRatio * sourceImg.naturalHeight;
					if(canvas.height >= 1000)
					canvas.height = 1000;
				}
				
				
				ctx.fillStyle = "white";
				ctx.fillRect(0, 0, canvas.width, canvas.height);	
			    ctx.drawImage(sourceImg, 0, 0, canvas.width, canvas.height);
	
				//default
				var imageFormat = "image/png"; 
				
				if(fileExtensionCheck(file,["jpg","jpeg"]))
				{
					imageFormat = "image/jpeg";
				}
				
				console.log("image format : ",imageFormat);
				
				(function cavasToBlob(_file){
					
				   canvas.toBlob(
					    blob =>	{
						
							if(imgType === "profile" || imgType === "mainSpot")
							{
								let preview = document.getElementById(previewId);
								preview.src = URL.createObjectURL(blob);
								
								
								console.log("축소된 이미지 파일 이름, 사이즈: ", _file.name +", "+ blob.size);
								
							}
							else //addedSpot
							{
								let spotArea = document.getElementById(previewId);
							    //프레임 태그 생성
					            var newDiv = document.createElement("div");
					            newDiv.setAttribute("class", "spot-img-frame");
					            //이미지 태그 생성
					            var preview = document.createElement("img");
					            preview.setAttribute("class", "spot-img");
					            //프레임 + 이미지 태그
					            newDiv.appendChild(preview);
					            //스폿 이미지 영역에 추가
					            spotArea.appendChild(newDiv);
					            preview.src = URL.createObjectURL(blob);
			
								console.log("축소된 이미지 파일 이름, 사이즈: ", _file.name +", "+ blob.size);
							}
						
							
							
							let resizedFile = new File([blob], _file.name, {type:imageFormat, lastModified: new Date().getTime()});
							container.items.add(resizedFile);
							
						
							if(replaceInputfile())
							{
								console.log("resizing 작업 완료");
								resolve();
							}
							
							
					}, imageFormat );
					
				})(file);
			 
			}
			
			function replaceInputfile()
			{
				console.log("toBlobCount : " + (toBlobCount+1));
				
				if(++toBlobCount === inputEl.files.length)
				{
					console.log("resized Files input에 적용");
					inputEl.files =  container.files;
					return true;
					
				}
				return false;
			}
		});
}

const blobToImage = blob => {
  return new Promise(resolve => {
   
    var image = new Image();
    image.onload = () => resolve(image);
	var url = URL.createObjectURL(blob);
	image.src = url;

  });
};