/*################## relattionshipResponseBox ####################*/
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

////Accept or decline the request
//Accept
function acceptRequest(buttonElement){
	let relationshipRequestId = buttonElement.parentNode.getElementsByClassName("input-delete")[0].value;
	
	 var form = document.createElement("form");
         form.setAttribute("charset", "UTF-8");
         form.setAttribute("method", "Post");
         form.setAttribute("action", "/acquaintance/acceptRelationshipRequest"); 


    var hiddenField = document.createElement("input");
        hiddenField.setAttribute("type", "hidden");
        hiddenField.setAttribute("name", "relationshipRequestId");
        hiddenField.setAttribute("value", relationshipRequestId);

        form.appendChild(hiddenField);
	
		document.body.appendChild(form);

		form.submit();	
}
//Decline
function declineRequest(buttonElement){
	let relationshipRequestId = buttonElement.parentNode.getElementsByClassName("input-delete")[0].value;
	
	 var form = document.createElement("form");
         form.setAttribute("charset", "UTF-8");
         form.setAttribute("method", "Post");
         form.setAttribute("action", "/acquaintance/declineRelationshipRequest"); 


    var hiddenField = document.createElement("input");
        hiddenField.setAttribute("type", "hidden");
        hiddenField.setAttribute("name", "relationshipRequestId");
        hiddenField.setAttribute("value", relationshipRequestId);

        form.appendChild(hiddenField);

		document.body.appendChild(form);

		form.submit();	
}