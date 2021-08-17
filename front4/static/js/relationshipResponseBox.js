/*################## relattionshipResponseBox ####################*/
//////////delete checkbox/////////////
let deleteMode = false;

let deleteButton = document.getElementsByClassName("btn-delete")[0];
deleteButton.onclick = dispatcherRelationshipResponseListToBeDeleted;

function dispatcherRelationshipResponseListToBeDeleted(){

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
    for (let i = 0; i < deleteCheckBoxList.length; i++){
      if(deleteCheckBoxList[i].checked == true){
        if(confirm("정말 삭제하시겠습니까?")==true)
        {
          document.getElementById("form-i1").submit();
        }
      }
    }

    //all init to deleteMode false
    deleteMode = false;
    for (let i = 0; i < deleteCheckBoxList.length; i++){
     deleteCheckBoxList[i].style.display="none";
    }

  }

} 