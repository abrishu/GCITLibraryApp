<%@page import="com.gcitsolutions.libraryapp.Entity.Genre" %>
<%@page import="com.gcitsolutions.libraryapp.Service.Administrator" %>
<%@page import="org.springframework.context.annotation.AnnotationConfigApplicationContext" %>
<%@page import="org.springframework.context.ApplicationContext" %>
<%@page import="com.gcitsolutions.libraryapp.SpringMVCLibraryAppConfig" %>
<%
ApplicationContext context = new AnnotationConfigApplicationContext(SpringMVCLibraryAppConfig.class);
Administrator adminService = (Administrator) context.getBean(Administrator.class);
Genre genre=new Genre(Integer.parseInt(request.getParameter("genreId")));
genre=adminService.getGenreById(genre);
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
				loadGenreData($('#searchString').val(),window.currentPageNumber);
				loadPaginationData($(this).val());
				highlightUpdatedRow($('#genreId').val());
			}) ;
	
	$(document).on("click", "#btnUpdateGenre", function(event){
		event.stopImmediatePropagation();
		$.ajax({
			url:"${pageContext.request.contextPath}/updateGenre",
			data:{
				"genreId":$('#genreId').val(),
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
        <h4 class="modal-title">Update Genre</h4>
      </div>
      <div class="modal-body">
      <form action="editGenre" method="post">
       	<input type="text" value='<%=genre.getName() %>' placeholder="Enter Genre's Name" id="newName" class="form-control" name="newName"><br/>
       	<input type="hidden" value=<%=genre.getId() %> id="genreId" style="width: 400px;" class="form-control" name="genreId"><br/>
      </form>
      </div>
      <div class="modal-footer">
        <button type="button" id="btnUpdateGenre" class="btn btn-default" >Update</button>
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button><br/>
        <span style="float:left;visibility:hidden" class="label label-success"></span>
         <span style="float:left;visibility:hidden" class="label label-danger"></span>
      </div>
    </div>
  	
  </div>
    