showHideSystem("comment-form-wrapper", "btn-leave-comment", "summary");
showHideSystem("comment", "btn-modification", "summary-comment-modification")
starPointSystem("starPointSystem-i1");
starPointSystemByClass("comment-modification-starPointSystem");
starPointSystemByClassForView("starPointSystem");
reliabilitySystemByClass("reliabilitySystem");

//Implement a function to delete comments per comment
let commentList = document.getElementsByClassName("comment");

for (let i = 0; i < commentList.length; i++) {

	let deleteCommentButton = commentList[i].getElementsByClassName("btn-delete-comment")[0];
	//check if there is no button
	if (deleteCommentButton == null)
		continue;

	deleteCommentButton.onclick = dispatchCommentToBeDeletedWrapperFunction(commentList[i]);

	//closer
	function dispatchCommentToBeDeletedWrapperFunction(commentElement) {

		return function dispatchCommentToBeDeleted() {
			if (confirm("정말 댓글을 삭제하시겠습니까?") == true) {
				commentElement.getElementsByClassName("form-delete")[0].submit();
			}
		}

	}



}

//comment modification cancel
for (let i = 0; i < commentList.length; i++) {


	let modificationCancelButton = commentList[i].getElementsByClassName("btn-modification-cancel")[0];

	//check if there is no button
	if (modificationCancelButton == null)
		continue;

	modificationCancelButton.onclick = clickSummaryWrapperFunction(commentList[i]);

	//closer
	function clickSummaryWrapperFunction(commentElemnt) {

		return function clickSummary() {
			commentElemnt.getElementsByClassName("summary-comment-modification")[0].click();
		}

	}

}