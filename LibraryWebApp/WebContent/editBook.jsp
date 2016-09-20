<head>
<link href="css/select2.css" rel="stylesheet" />
<script src="js/select2.min.js"></script>
</head>


    <!-- Modal content-->
   <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title">Update Book</h4>
      </div>
      <div class="modal-body">
      <form action="editBook" method="post">
       	<input type="text" value='' placeholder="Enter Book's Name" id="newName" class="form-control" name="newName"><br/>
       	Author:<br/>
		<select multiple id="authorId" name="authorId"  class="form-control" >
		</select><br/>
       	Publisher:<br/>
       	<select id="pubId" name="pubId"  class="form-control" >
		</select><br/>
       	<input type="hidden" value='<%=request.getParameter("bookId") %>' id="bookId" name="bookId" style="width: 400px;" class="form-control" name="bookId"><br/>
      </form>
      </div>
      <div class="modal-footer">
        <button type="button" id="btnUpdateBook" class="btn btn-default" >Update</button>
        <button type="button"  class="btn btn-default" data-dismiss="modal">Close</button><br/>
        <span style="float:left;visibility:hidden" class="label label-success">Success Label</span>
      </div>
    </div>
  </div>
  
 
<script src="js/select2.min.js"></script>
  
  <script type="text/javascript">


/*Publishers loading and population*/

function closeModal(){
	 $('#editModal').modal('toggle');
}

function loadAllPublishers(defaultPub){
	$.ajax({
		url:"getAllPublishers",
		data:{},
		datatype:"json"
	}).done(function(publisherList){
		populatePubSelect(publisherList,defaultPub)
	});
}

function populatePubSelect(publisherList,defaultPub){
	$.each(publisherList,function(index,publisherRow){
		$('#pubId').append($("<option/>").val(publisherRow.id).text(publisherRow.name))
	});
		$('#pubId').append($("<option selected='selected'/>").val(0).text("None"));
	if(defaultPub==null){
	}else{
		$('#pubId').val(defaultPub.id);
	}
}


/*Authors loading and population*/
function loadAllAuthors(defaultAuthorList){
	$.ajax({
		url:"getAllAuthors",
		data:{},
		datatype:"json"
	}).done(function(authorList){
		populateAuthorSelect(authorList,defaultAuthorList)
	});
}

function populateAuthorSelect(authorList,defaultAuthorList){
	$.each(authorList,function(index,authorRow){
		$('#authorId').append($("<option/>").val(authorRow.id).text(authorRow.name))
	});
	if(defaultAuthorList==null){
		$('#authorId').append($("<option selected='selected'/>").val(0).text("None"));
	}else{
		$.each(defaultAuthorList,function(index,row){
			$('#authorId option[value=' + row.id +  ']').prop("selected", true);
		});
	}
	//$('#authorId').multiselect();
	$('#authorId').select2();
	$('.select2').css("width","100%");
}




function populateDefaultValues(bookData){
	
	var publisher=bookData.publisher;
	var authors=bookData.authorList;
	//Populate the existing book title
	if(bookData.title!=null){
		$('#newName').val(bookData.title);	
	}
	
	//Populate All the Publishers
	loadAllPublishers(publisher);
	
	//Populate All the Authors
	loadAllAuthors(authors);
	
	
}


/*function to load Book Information such as Publisher and Author List*/

function loadBookInfo(bookId){
	$.ajax({
		url:"getBookInfo",
		data:{
			"bookId" : bookId
		},
		datatype:"json"
	}).done(function(bookData){
		populateDefaultValues(bookData);
	});
}

$(document).ready(function(){
	loadBookInfo(<%=request.getParameter("bookId")%>);
	
	$('select').each(function(){
	});
});

$(document).on("click", "#btnUpdateBook", function(event){
	event.stopImmediatePropagation();
	$.ajax({
		url:"updateBook",
		data:{
			"bookId":$('#bookId').val(),
			"authorId":$('#authorId').val(),
			"newName":$('#newName').val(),
			"pubId":$('#pubId').val()
		},
		datatype:"text"
	}).done(function(){
		$('.label-success').css('visibility', 'visible');
		$('.label-success').text("Book details have been updated successfully");	
		setTimeout(closeModal,1000);
	});
});

</script>
      		