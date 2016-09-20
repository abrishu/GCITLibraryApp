   <!-- Modal content-->
   <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title">Confirm Delete</h4>
      </div>
      <div class="modal-body">
      <form action="delete">
       		<div id="divMessage"></div>
      </form>
      </div>
      <div class="modal-footer">
        <button type="button" id="btnDeleteBook" class="btn btn-default" >Delete</button>
        <button type="button"  class="btn btn-default" data-dismiss="modal">Close</button><br/>
        <span style="float:left;visibility:hidden" class="label label-success">Success Label</span>
        <span style="float:left;visibility:hidden" class="label label-danger">Danger Label</span>
      </div>
    </div>
  </div>
  
  <script type="text/javascript">

  function closeModalWindow(){
	  $('#deleteModal').modal('toggle');
  }

$(document).ready(function(){
	var action='<%=request.getParameter("deleteAction")%>';
	window.currentBookId= <%=request.getParameter("bookId")%>;
	if(action=='book'){
		$('#divMessage').html('Are you sure you want to delete ' + "<%=request.getParameter("title")%>" + '?');
	}
	$('#deleteModal').on('hidden.bs.modal', function(e)
			{
				$(this).removeData();
				loadBookData($('#searchString').val(),window.currentPageNumber)
			}) ;
});



$(document).on("click", "#btnDeleteBook", function(event){
	event.stopImmediatePropagation();
	 $.ajax({
		  url:"deleteBook",
		  data:{
			  "bookId":window.currentBookId
		  },
		  datatype:"json"
	  }).done(function(response){
		  if(response.success==false){
			  $('.label-danger').html(response.message);
			  $('.label-danger').css('visibility', 'visible');
			  setTimeout(closeModalWindow,1000);
		  }
		  
	  });
});

</script>
      		