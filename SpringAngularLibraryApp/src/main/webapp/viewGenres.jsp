 <%@include file="includes.jsp"%>
<%@page import="com.gcitsolutions.libraryapp.Entity.Genre" %>
<%@page import="com.gcitsolutions.libraryapp.Service.Administrator" %>
<%@page import="java.util.List" %>
<%@page import="java.util.ArrayList" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>View Genres</title>
<script type="text/javascript">


/*Populating Genre Table*/
function populateGenreList(genreData){
	var headerData='<tr><th># No.</th><th>Genre Name</th><th>Edit</th><th>Delete</th></tr>';
	var rowData='';
	var totalData='';
	if(genreData!=null){
		$.each(genreData,function(index,genreObject){
			//Setting the Values of Id and Name of Object
			rowData+='<tr id=' + this.id + '><td>'+ (index+1) + '</td><td>' + this.name +'</td>';
			//Setting the edit button for the given genre object
			rowData+='<td><button type="button"'+ 'href="showEditGenre?genreId=' + this.id + '" id="btnEdit" data-toggle="modal" data-target="#editModal" class="btn btn-success btn-sm btnEdit">Edit</button></td>';
			//Setting the delete button for the given genre object
			rowData+='<td><button type="button"'+ 'href="showDeleteGenre?deleteAction=genre&name=' +encodeURIComponent(this.name) + '&genreId=' + this.id + '" data-toggle="modal" data-target="#deleteModal" class="btn btn-danger btn-sm">Delete</button></td>';
			//Finally completing the row
			rowData+='</tr>';
		});
	}
	
	if(rowData==''){
		totalData=headerData+rowData +'<tr><td><span class="label label-danger">No Data to display</span></td><td></td><td></td><td></td></tr>';
	}else{
		totalData=headerData + rowData;
	}
	$('#tblGenre').html(totalData);
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
		dataList=dataList+'<li><a onclick = loadGenreData("'+ $('#searchString').val() + '",' + i + ');loadPaginationData("'+  $('#searchString').val() +'");>' + i + ' </a></li>';
	}
	$('#paginationList').html(firstArrow+dataList+lastArrow);
}


/* Loading genre data based on the parameters given */
function loadGenreData(searchString,pageNo){ 
	window.currentPageNumber=pageNo;
	$.ajax({
		url:"${pageContext.request.contextPath}/searchGenres",
		method:"GET",
		data:{
			"searchString":searchString,
			"pageNo":pageNo
		},
		datatype:"json"
	}).done(function(data){
		populateGenreList(data);
	})
}


/*Loading pagination data based on the search string*/
function  loadPaginationData(searchString){
	$.ajax({
		url:"${pageContext.request.contextPath}/paginationGenres",
		method:"GET",
		data:{
			"searchString":searchString
		}
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
		loadGenreData(searchString,pageNum);
		
		$('#searchString').change(function(){
			$('.alertDiv').html('');
			loadGenreData($(this).val(),1);
			loadPaginationData($(this).val());
		});
	}); 
</script>
</head>
<body>

<div class="input-group">
	<div class="alertDiv"></div>
	 <form action="searchGenres" method="get">
	 	 <input type="text" class="form-control" placeholder="Genre Name" id="searchString" name="searchString" aria-describedby="basic-addon1">
 	 	<br/><br/>
 	 <button type="button" href="showAddGenre" data-toggle="modal" data-target="#addModal" class="btn btn-success btn-sm">Add Genre</button>
 	 </form>
</div>

<nav aria-label="Page navigation">
  <ul id="paginationList" class="pagination">
  </ul>
</nav> 
 
<div class="panel panel-default">
		<div class="panel-heading">List of Genres</div>
		<table id="tblGenre" class="table">
		</table>
</div>


<div id="editModal" class="modal fade" role="dialog">
  
</div>

<div id="addModal" class="modal fade" role="dialog">
  
</div>

<div id="deleteModal" class="modal fade" role="dialog">
  
</div>



</body>
</html>