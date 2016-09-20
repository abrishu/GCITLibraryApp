<%@page import="com.gcitsolutions.libraryapp.Entity.Author" %>
<%@page import="com.gcitsolutions.libraryapp.Service.Administrator" %>

<%
Author author=new Author(Integer.parseInt(request.getParameter("authorId")));
author=Administrator.getInstance().getAuthorById(author);
%>

<head>
<script type="text/javascript">

function closeModal(){
	 $('#editModal').modal('toggle');
}
$(document).ready(function(){
	$('.modal').on('hidden.bs.modal', function(e)
			{
				$(this).removeData();
				loadAuthorData($('#searchString').val(),window.currentPageNumber)
			}) ;
	
	$(document).on("click", "#btnUpdateAuthor", function(event){
		event.stopImmediatePropagation();
		$.ajax({
			url:"updateAuthor",
			data:{
				"authorId":$('#authorId').val(),
				"newName":$('#newName').val()
			},
			datatype:"text"
		}).done(function(){
			$('.label-success').css('visibility', 'visible');
			$('.label-success').text("Author details have been updated successfully");	
			setTimeout(closeModal,1000);
		});
	});
	
	
})
</script>
</head>
    <!-- Modal content-->
   <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title">Update Author</h4>
      </div>
      <div class="modal-body">
      <form action="editAuthor" method="post">
       	<input type="text" value='<%=author.getName() %>' placeholder="Enter Author's Name" id="newName" class="form-control" name="newName"><br/>
       	<input type="hidden" value=<%=author.getId() %> id="authorId" style="width: 400px;" class="form-control" name="authorId"><br/>
      </form>
      </div>
      <div class="modal-footer">
        <button type="button" id="btnUpdateAuthor" class="btn btn-default" >Update</button>
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button><br/>
        <span style="float:left;visibility:hidden" class="label label-success">Success Label</span>
      </div>
    </div>
  	
  </div>
    