<head>
<link href="${pageContext.servletContext.contextPath}/resources/css/select2.css" rel="stylesheet" />
</head>
    <!-- Modal content-->
   <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title">Update Book Copies</h4>
      </div>
      <div class="modal-body">
      <form action="editBook" method="post">
       	Branch Name:<br/>
       	<input type="text" value=''  id="branchName" class="form-control" name="branchName"><br/>
       	Book
       	<input type="text" readOnly value='' id="bookName" class="form-control" name="bookName"><br/>
       	Address
       	<input type="text" value='' id="address" class="form-control" name="address"><br/>
       	No of Copies
       	<input type="text" readonly value='' id="noOfCopies" class="form-control" name="noOfCopies"><br/>
       	Add Number of Copies
       	<input type="text" value='' placeholder="Enter Number of Copies to Add" id="addCopies" class="form-control" name="addCopies"><br/>
       	<input type="hidden" value='<%=request.getParameter("bookId") %>' id="bookId" name="bookId" style="width: 400px;" class="form-control" ><br/>
       	<input type="hidden" value='<%=request.getParameter("branchId") %>' id="branchId" name="branchId" style="width: 400px;" class="form-control"><br/>
      </form>
      </div>
      <div class="modal-footer">
        <button type="button" id="btnUpdateBookCopies" class="btn btn-default" >Update</button>
        <button type="button"  class="btn btn-default" data-dismiss="modal">Close</button><br/>
        <span style="float:left;visibility:hidden" class="label label-success">Success Label</span>
      </div>
    </div>
  </div>
  <script src="${pageContext.servletContext.contextPath}/resources/js/select2.min.js"></script>

  <script type="text/javascript">





/*function to load Book Information such as Publisher and Author List*/

function loadBookInfo(bookId){
	$.ajax({
		url:"${pageContext.request.contextPath}/getBookInfo",
		data:{
			"bookId" : bookId
		},
		datatype:"json"
	}).done(function(bookData){
		populateDefaultBookValue(bookData);
	});
}

function populateDefaultBookValue(bookData){
	$('#bookName').val(bookData.title);
}

//Load Branch Information

function loadBranchInfo(branchId){
	$.ajax({
		url:"${pageContext.request.contextPath}/getBranchInfo",
		data:{
			"branchId" : branchId
		},
		datatype:"json"
	}).done(function(branchData){
		populateDefaultBranchValues(branchData);
	});
}


function populateDefaultBranchValues(branchData){
	$('#branchName').val(branchData.name);
	$('#address').val(branchData.address);
}


function loadBookCopyInfo(branchId,bookId){
	$.ajax({
		url:"${pageContext.request.contextPath}/getBookCopiesInfo",
		data:{
			"branchId" : branchId,
			"bookId":bookId
		},
		datatype:"json"
	}).done(function(bookCopyData){
		populateDefaultBookCopyValues(bookCopyData.noOfCopies);
	});
}

function populateDefaultBookCopyValues(bookCopyData){
	$('#noOfCopies').val(bookCopyData);
}

function closeModal(){
	 $('#editModal').modal('toggle');
}

function updateBookCopies(){
	$.ajax({
		url:"${pageContext.request.contextPath}/addBookCopies",
		data:{
			"bookId":$('#bookId').val(),
			"branchId":$('#branchId').val(),
			"addCopies":$('#addCopies').val()
		},
		datatype:"json",
		method:"post"
	}).done(function(data){
		if(data.success){
			$('.label-success').css('visibility', 'visible');
			$('.label-success').text(data.message);	
			setTimeout(closeModal,1000);
		}else{
			$('.label-danger').css('visibility', 'visible');
			$('.label-danger').text(data.message);	
		}
	});
}


function updateBranchDetails(){
	console.log('Updating branch details');
	$.ajax({
		method:"POST",
		url:"${pageContext.request.contextPath}/updateBranchDetails",
		data:{
			"branchId":$('#branchId').val(),
			"newName":$('#branchName').val(),
			"newAddress":$('#address').val()
		},
		datatype:"json"
	}).done(function(data){
		$('.label-success').css('visibility', 'visible');
		$('.label-success').text(data.message);	
	});
}

$(document).ready(function(){
	var bookId=<%=request.getParameter("bookId")%>;
	var branchId=<%=request.getParameter("branchId")%>;
	loadBookInfo(bookId);
	loadBranchInfo(branchId);
	loadBookCopyInfo(branchId,bookId);
});

$(document).on("click", "#btnUpdateBookCopies", function(event){
	event.stopImmediatePropagation();
	updateBranchDetails();
	updateBookCopies();
});

</script>
      		