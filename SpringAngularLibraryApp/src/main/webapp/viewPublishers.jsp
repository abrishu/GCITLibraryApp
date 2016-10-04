<%@include file="includes.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>View Publishers</title>

<style>

.table-borderless tbody tr td, .table-borderless tbody tr th, .table-borderless thead tr th {
    border: none;
}
</style>
</head>

</head><body>

<div class="input-group">
<div class="alertDiv"></div>
	 <form action="searchPublishers" method="get">
	 	 <input type="text" class="form-control" placeholder="Publisher Name" id="searchString" name="searchString" aria-describedby="basic-addon1">
	 	 <br/><br/>
 	 <button type="button" href="showAddPublisher" data-toggle="modal" data-target="#addModal" class="btn btn-success btn-sm">Add Publisher</button>
 	 </form>
</div>

<nav aria-label="Page navigation">
  <ul id="paginationList" class="pagination">
  </ul>
</nav> 
 
<div class="panel panel-default">
		<div class="panel-heading">List of Publishers</div>
		<table id="tblPublisher" class="table">
		</table>
</div>


<div id="editModal" class="modal fade" role="dialog">
  
</div>

<div id="deleteModal" class="modal fade" role="dialog">
  
</div>

<div id="addModal" class="modal fade" role="dialog">
  
</div>



<script type="text/javascript">

function returnString(data){
	if(data==null){
		return "";
	}else{
		return data;
	}
}

function populatePublishersList(publisherData){
	var headerData='<tr><th># No.</th><th>Publisher Name</th><th>Address</th><th>Phone</th><th>Edit</th><th>Delete</th></tr>';
	var rowData='';
	if(publisherData!=null){
		$.each(publisherData,function(index,authorObject){
			//Setting the Values of Id and Name of Object
			rowData+='<tr id='+(index+1)+'><td>'+ (index+1) + '</td><td>' + returnString(this.name) +'</td><td>' + this.address +'</td><td>'+ this.phone + '</td>' ;
			//Setting the edit button for the given author object
			rowData+='<td><button type="button"'+ 'href="showEditPublisher?pubId=' + this.id +'" id="btnEdit" data-toggle="modal" data-target="#editModal" class="btn btn-success btn-sm btnEdit">Edit</button></td>';
			//Setting the delete button for the given author object
			rowData+='<td><button type="button" id="btnDeletePub"'+ 'href="showDeletePublisher?deleteAction=publisher&name=' +encodeURIComponent(this.name) + '&pubId=' + this.id   +  '" data-toggle="modal" data-target="#deleteModal" class="btn btn-danger btn-sm">Delete</button></td>'
			//Finally completing the row
			rowData+='</tr>';
		});	
	}
	if(rowData==''){
		totalData=headerData+rowData +'<tr><td><span class="label label-danger">No Data to display</span></td><td></td><td></td><td></td><td></td><<td></td>/tr>';
	}else{
		totalData=headerData + rowData;
	}
	$('#tblPublisher').html(totalData);
}

/*Highlight current cell*/

function highlight(i){
	
	if($('.pagination li')[i]!=null){
		$('.pagination li').each(function(){
			this.setAttribute("class","inactive")
		});
		$('.pagination li')[i].setAttribute("class","active")	
	}
}


/*Performing pagination - highlightCurrentCell(" + i +  ");*/
function performPagination(paginationCount){
	var firstArrow='<li><a href="#" aria-label="Previous"><span aria-hidden="true">&laquo;</span></a></li>';
	var lastArrow=' <li><a href="#" aria-label="Next"><span aria-hidden="true">&raquo;</span></a></li>';
	var dataList='';
	for(var i=1;i<=paginationCount;i++){
		dataList=dataList+'<li><a onclick = loadPublisherData("'+ $('#searchString').val() + '",' + i + ');loadPaginationData("'+  $('#searchString').val() +'");>' + i + ' </a></li>';
	}
	$('#paginationList').html(firstArrow+dataList+lastArrow);
}


/* Loading author data based on the parameters given */
function loadPublisherData(searchString,pageNo){ 
	window.currentPageNumber=pageNo;
	$.ajax({
		url:"${pageContext.request.contextPath}/searchPublishers",
		data:{
			"searchString":searchString,
			"pageNo":pageNo
		},
		datatype:"json"
	}).done(function(data){
		populatePublishersList(data);
	})
}


/*Loading pagination data based on the search string*/
function  loadPaginationData(searchString){
	$.ajax({
		url:"${pageContext.request.contextPath}/paginationPublishers",
		data:{
			"searchString":searchString
		},
		datatype:"text"
	}).done(function(data){
		performPagination(data);
		highlight(window.currentPageNumber);
	})
}

	$(document).ready(function(){
		checkResultAndError();
		var searchString='<%=request.getParameter("searchString")%>';
		var pageNum = '<%=request.getParameter("pageNo")%>';
		if(searchString=="null"){
			searchString="";
		}
		if(pageNum=='null'){
			pageNum=1;
		}
		loadPaginationData(searchString);
		loadPublisherData(searchString,pageNum);
		/*on change AJAX call to load pagination info and authors*/
		$('#searchString').change(function(){
			$('.alertDiv').html('');
			loadPublisherData($(this).val(),1);
			loadPaginationData($(this).val());
		});
		
		
		/*When the modal screen is closed, below events would be triggered*/
		$('.modal').on('hidden.bs.modal', function(e)
				{
			        $('.alertDiv').html('');
					$(this).removeData();
					console.log(window.currentPageNumber);
					loadPublisherData($('#searchString').val(),window.currentPageNumber);
					loadPaginationData($('#searchString').val());
				}) ;
		
	}); 
</script>


</body>
</html>