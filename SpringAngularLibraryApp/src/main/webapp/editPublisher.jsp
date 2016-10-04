<head>

</head>


    <!-- Modal content-->
   <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title">Update Publisher</h4>
      </div>
      <div class="modal-body">
      <form action="editPublisher" method="post">
      	Name:<br/>
       	<input type="text" value=''  id="newName" class="form-control" name="newName"><br/>
       	Address:<br/>
			<input type="text" value=''  id="newAddress" class="form-control" name="newAddress"><br/>
       	Phone:<br/>
       		<input type="text" value='' id="newPhone" class="form-control" name="newPhone"><br/>
       	<input type="hidden" value='<%=request.getParameter("pubId") %>' id="pubId" name="pubId" style="width: 400px;" class="form-control"><br/>
      </form>
      </div>
      <div class="modal-footer">
        <button type="button" id="btnUpdatePublisher" class="btn btn-default" >Update</button>
        <button type="button"  class="btn btn-default" data-dismiss="modal">Close</button><br/>
        <span style="float:left;visibility:hidden" class="label label-success">Success Label</span>
      </div>
    </div>
  </div>
  <script type="text/javascript">


/*Publishers loading and population*/

function closeModal(){
	 $('#editModal').modal('toggle');
}

function populateDefaultValues(publisherData){
	if(publisherData!=null){
		$('#newName').val(publisherData.name);	
		$('#newAddress').val(publisherData.address);	
		$('#newPhone').val(publisherData.phone);	
	}
}


/*function to load Publisher Information such as Publisher and Author List*/

function loadPublisherInfo(publisherId){
	$.ajax({
		url:"${pageContext.request.contextPath}/getPublisherInfo",
		data:{
			"pubId" : publisherId
		},
		datatype:"json"
	}).done(function(publisherData){
		populateDefaultValues(publisherData);
	});
}

$(document).ready(function(){
	loadPublisherInfo(<%=request.getParameter("pubId")%>);
});

$(document).on("click", "#btnUpdatePublisher", function(event){
	event.stopImmediatePropagation();
	$.ajax({
		url:"${pageContext.request.contextPath}/updatePublisher",
		data:{
			"pubId":$('#pubId').val(),
			"newName":$('#newName').val(),
			"newAddress":$('#newAddress').val(),
			"newPhone":$('#newPhone').val()
		},
		datatype:"text"
	}).done(function(){
		$('.label-success').css('visibility', 'visible');
		$('.label-success').text("Publisher details have been updated successfully");	
		setTimeout(closeModal,1000);
	});
});

</script>
      		