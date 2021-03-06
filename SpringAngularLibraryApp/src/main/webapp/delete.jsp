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
        <button type="button" id="btnDelete" class="btn btn-default" >Delete</button>
        <button type="button"  class="btn btn-default" data-dismiss="modal">Close</button><br/>
        <span style="float:left;visibility:hidden" class="label label-success">Success Label</span>
        <span style="float:left;visibility:hidden" class="label label-danger">Danger Label</span>
      </div>
    </div>
  </div>
  
  <script type="text/javascript">

  function closeModalWindow(){
	  $('#deleteModal').modal('toggle');
	  deleteNearestRowFromTable(window.currentId);
  }

  function deleteNearestRowFromTable(id){
	  var ctrl=$("table tr[id='" + window.currentId+ "']");
	  ctrl.hide(2000);
  }
  
$(document).ready(function(){
	 window.action='<%=request.getParameter("deleteAction")%>';
	if(window.action=='book'){
		$('#divMessage').html('Are you sure you want to delete ' + "<%=request.getParameter("title")%>" + '?');
		window.currentId= <%=request.getParameter("bookId")%>;
	}else if(window.action=='author'){
		$('#divMessage').html('Are you sure you want to delete ' + "<%=request.getParameter("name")%>" + '?');
		window.currentId= <%=request.getParameter("authorId")%>;
	}else if(window.action=='publisher'){
		$('#divMessage').html('Are you sure you want to delete ' + "<%=request.getParameter("name")%>" + '?');
		window.currentId= <%=request.getParameter("pubId")%>;
	}else if(window.action=='genre'){
		$('#divMessage').html('Are you sure you want to delete ' + "<%=request.getParameter("name")%>" + '?');
		window.currentId= <%=request.getParameter("genreId")%>;
	}
			
	
	$('#deleteModal').on('hidden.bs.modal', function(e)
	{
 		$(this).removeData();
	}) ;
});



$(document).on("click", "#btnDelete", function(event){
	event.stopImmediatePropagation();
	var deleteUrl='';
	var data1={};
	if(window.action=='book'){
		deleteUrl="deleteBook";
		data1["bookId"]=window.currentId;
	}else if(window.action=='author'){
		deleteUrl="deleteAuthor";
		data1["authorId"]=window.currentId;
	}
	else if(window.action=='publisher'){
		deleteUrl="deletePublisher";
		data1["pubId"]=window.currentId;
	}
	else if(window.action=='genre'){
		deleteUrl="deleteGenre";
		data1["genreId"]=window.currentId;
	}
	 $.ajax({
		  url:deleteUrl,
		  data:data1,
		  datatype:"json"
	  }).done(function(response){
		  $('.label-danger').html(response.message);
		  $('.label-danger').css('visibility', 'visible');
		  if(response.success==true){
			  setTimeout(closeModalWindow,1000);
		  }
	  });
});

</script>
      		