<%@page import="com.gcitsolutions.libraryapp.Entity.Author" %>
<%@page import="com.gcitsolutions.libraryapp.Service.Administrator" %>
<%@page import="org.springframework.context.annotation.AnnotationConfigApplicationContext" %>
<%@page import="org.springframework.context.ApplicationContext" %>
<%@page import="com.gcitsolutions.libraryapp.SpringMVCLibraryAppConfig" %>
<%
ApplicationContext context = new AnnotationConfigApplicationContext(SpringMVCLibraryAppConfig.class);
Administrator adminService = (Administrator) context.getBean(Administrator.class);
Author author=new Author(Integer.parseInt(request.getParameter("authorId")));
author=adminService.getAuthorById(author);
%>

<head>


<script type="text/javascript">

function closeModal(){
	 $('#editModal').modal('toggle');
}
function highlightUpdatedRow(id){
	  var ctrl=$("table tr[id='" + id+ "']");
}

$(document).ready(function(){
	$('.modal').on('hidden.bs.modal', function(e)
			{
				$('.alertDiv').html('');
				$(this).removeData();
				loadAuthorData($('#searchString').val(),window.currentPageNumber);
				loadPaginationData($(this).val());
				highlightUpdatedRow($('#authorId').val());
			}) ;
	
	$(document).on("click", "#btnUpdateAuthor", function(event){
		event.stopImmediatePropagation();
		$.ajax({
			url:"${pageContext.request.contextPath}/updateAuthor",
			data:{
				"authorId":$('#authorId').val(),
				"newName":$('#newName').val()
			},
			datatype:"json"
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
	});
	
	
})
</script>
</head>
    <!-- Modal content-->
   <div class="modal-dialog">
    <div class="modal-content">
    <h2>${message}</h2>
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
        <span style="float:left;visibility:hidden" class="label label-success"></span>
         <span style="float:left;visibility:hidden" class="label label-danger"></span>
      </div>
    </div>
  	
  </div>
    