<%@include file="includes.jsp"%>
<%@page import="com.gcitsolutions.libraryapp.Entity.Author" %>
<%@page import="com.gcitsolutions.libraryapp.Service.Administrator" %>
<%@page import="java.util.List" %>
<%@page import="java.util.ArrayList" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>View Authors</title>
<script type="text/javascript">


/*Populating Author Table*/
function populateAuthorList(authorData){
	var headerData='<tr><th># No.</th><th>Author Name</th><th>Edit</th><th>Delete</th></tr>';
	var rowData='';
	var totalData='';
	if(authorData!=null){
		$.each(authorData,function(index,authorObject){
			//Setting the Values of Id and Name of Object
			rowData+='<tr><td>'+ (index+1) + '</td><td>' + this.name +'</td>';
			//Setting the edit button for the given author object
			rowData+='<td><button type="button"'+ 'href="editAuthor.jsp?authorId=' + this.id +'" id="btnEdit" data-toggle="modal" data-target="#editModal" class="btn btn-success btn-sm btnEdit">Edit</button></td>';
			//Setting the delete button for the given author object
			rowData+='<td><button type="button"'+ 'href="editAuthor.jsp?authorId=' + this.id   +  '" class="btn btn-danger btn-sm">Delete</button></td>'
			//Finally completing the row
			rowData+='</tr>';
		});
	}
	
	if(rowData==''){
		totalData=headerData+rowData +'<tr><td><span class="label label-danger">No Data to display</span></td><td></td><td></td><td></td></tr>';
	}else{
		totalData=headerData + rowData;
	}
	$('#tblAuthor').html(totalData);
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
		dataList=dataList+'<li><a onclick = loadAuthorData("'+ $('#searchString').val() + '",' + i + ');loadPaginationData("'+  $('#searchString').val() +'");>' + i + ' </a></li>';
	}
	$('#paginationList').html(firstArrow+dataList+lastArrow);
}


/* Loading author data based on the parameters given */
function loadAuthorData(searchString,pageNo){ 
	window.currentPageNumber=pageNo;
	$.ajax({
		url:"searchAuthors",
		data:{
			"searchString":searchString,
			"pageNo":pageNo
		},
		datatype:"json"
	}).done(function(data){
		populateAuthorList(data);
	})
}


/*Loading pagination data based on the search string*/
function  loadPaginationData(searchString){
	$.ajax({
		url:"paginationAuthors",
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
		var searchString='<%=request.getParameter("searchString")%>';
		var pageNum = '<%=request.getParameter("pageNo")%>';
		if(searchString=="null"){
			searchString="";
		}
		if(pageNum=='null'){
			pageNum=1;
		}
		loadPaginationData(searchString);
		loadAuthorData(searchString,pageNum);
		
		$('#searchString').change(function(){
			loadAuthorData($(this).val(),1);
			loadPaginationData($(this).val());
		});
	}); 
</script>
</head>
<body>

<div class="input-group">
	 <form action="searchAuthors" method="get">
	 	 <input type="text" class="form-control" placeholder="Author Name" id="searchString" name="searchString" aria-describedby="basic-addon1">
 	 </form>
</div>

<nav aria-label="Page navigation">
  <ul id="paginationList" class="pagination">
  </ul>
</nav> 
 
<div class="panel panel-default">
		<div class="panel-heading">List of Authors</div>
		<table id="tblAuthor" class="table">
		</table>
</div>


<div id="editModal" class="modal fade" role="dialog">
  
</div>


</body>
</html>