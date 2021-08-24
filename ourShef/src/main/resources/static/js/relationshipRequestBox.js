/*################## relattionshipRequestBox ####################*/
//////////delete checkbox/////////////
let deleteMode = false;

let deleteButton = document.getElementsByClassName("btn-delete")[0];
deleteButton.onclick = dispatcherRelationshipRequestListToBeDeleted;

function dispatcherRelationshipRequestListToBeDeleted(){

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

	if(isChecked && confirm("정말 삭제하시겠습니까?")==true)
    {
      document.getElementById("form-i1").submit();
    }

    //all init to deleteMode false
    deleteMode = false;
    for (let i = 0; i < deleteCheckBoxList.length; i++){
     deleteCheckBoxList[i].style.display="none";
    }

  }

} 