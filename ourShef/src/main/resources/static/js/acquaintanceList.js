////////////reliabilitySystem/////////////////
reliabilitySystemByClass("reliabilitySystem");

//////////delete checkbox/////////////
let deleteMode = false;

let acquaintanceDeleteButton = document.getElementsByClassName("btn-acquaintance-delete")[0];
acquaintanceDeleteButton.onclick = dispatcherAcquaintanceListToBeDeleted;

function dispatcherAcquaintanceListToBeDeleted(){

  let deleteCheckBoxList = document.getElementsByClassName("input-delete");

	if(deleteMode == false)
  {
    deleteMode = true;
    for (let i = 0; i < deleteCheckBoxList.length; i++){
      deleteCheckBoxList[i].style.display="inline-block";
     }
    

  }
  else{
   
    // Send if any one is checked
	var isChecked = false;
    for (let i = 0; i < deleteCheckBoxList.length; i++){
      if(deleteCheckBoxList[i].checked == true){
       isChecked = true;
		break;
      }
    }

	if(isChecked && confirm("정말 삭제하시겠습니까?\n지인의 목록에서도 삭제됩니다.\n맛집 목록도 서로 볼 수 없습니다.")==true)
    {
      document.getElementById("form-i1").submit();
    }

    //init to deleteMode false
    deleteMode = false;
    for (let i = 0; i < deleteCheckBoxList.length; i++){
     deleteCheckBoxList[i].style.display="none";
    }

  }

} 


