<%@include file="includes.jsp"%>
<%@page import="com.gcitsolutions.libraryapp.Entity.Author" %>
<%@page import="com.gcitsolutions.libraryapp.Service.Administrator" %>
<%@page import="java.util.List" %>
<%@page import="java.util.ArrayList" %>
<%
	List<Author> authorList=new ArrayList<Author>();
	Integer pageCount=0;
	Integer noOfRecords=0;
	String searchString=request.getParameter("searchString");
	if(searchString!=null && !searchString.equals("")){
		noOfRecords=Administrator.getInstance().getCountOfAuthors(searchString);
	}else{
		noOfRecords=Administrator.getInstance().getCountOfAuthors("");
	}
	Integer pageSize=10;
	if(noOfRecords%10==0){
		if((noOfRecords/pageSize)==0){
			pageCount=1;
		}else{
			pageCount=(noOfRecords/pageSize);	
		}
	}else{
		pageCount=(noOfRecords/pageSize)+1;
	}
	if(request.getAttribute("authorList")!=null){
		authorList=(List<Author>)request.getAttribute("authorList");
	}else{
		authorList=Administrator.getInstance().getAllAuthors(1,10,null);
	}
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>View Authors</title>
<script type="text/javascript">

function populateAuthorList(authorData){
	var headerData='<tr><th># No.</th><th>Author Name</th><th>Edit</th><th>Delete</th></tr>';
	var rowData='';
	$.each(authorData,function(index,authorObject){
		//Setting the Values of Id and Name of Object
		rowData+='<tr><td>'+ index + '</td><td>' + this.name +'</td>';
		//Setting the edit button for the given author object
		rowData+='<td><button type="button"'+ 'href="editAuthor.jsp?authorId=' + this.id +' id="btnEdit" data-toggle="modal" data-target="#editModal" class="btn btn-success btn-sm btnEdit">Edit</button></td>';
		//Setting the delete button for the given author object
		rowData+='<td><button type="button"'+ 'href="editAuthor.jsp?authorId=' + this.id   +  ' class="btn btn-danger btn-sm">Delete</button></td>'
		//Finally completing the row
		rowData+='</tr>';
	});
	if(rowData==''){
		rowData='<tr> No Data to display </tr>';
	}
	var totalData=headerData + rowData;
	$('#tblAuthor').html(totalData);
}

function performPagination(paginationCount){
	var firstArrow='<li><a href="#" aria-label="Previous"><span aria-hidden="true">&laquo;</span></a></li>';
	var lastArrow=' <li><a href="#" aria-label="Next"><span aria-hidden="true">&raquo;</span></a></li>';
	var dataList='';
	for(var i=1;i<=paginationCount;i++){
		dataList=dataList+'<li><a href="pageAuthors?pageNo=' + i + '">'+i + '</a></li>';
	}
	alert(firstArrow+dataList+lastArrow);
	$('#paginationList').html(firstArrow+dataList+lastArrow);
	
}

function loadAuthorData(searchString,pageNo){
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

function  loadPaginationData(searchString){
	$.ajax({
		url:"paginationAuthors",
		data:{
			"searchString":$(this).val()
		},
		datatype:"text"
	}).done(function(data){
		performPagination(data);
	})
}

	$(document).ready(function(){
		loadPaginationData("");
		loadAuthorData("",1);
		
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
  		
  		<button type="button" class="btn-primary" id="search">Search</button>
 	 </form>
</div>

<nav aria-label="Page navigation">
  <ul id="paginationList" class="pagination">
    <li>
      <a href="#" aria-label="Previous">
        <span aria-hidden="true">&laquo;</span>
      </a>
    </li>
    <% for(int i=1;i<=pageCount;i++) {%>
   		<li><a href="pageAuthors?pageNo=<%=i%>&searchString=<%=searchString %>"><%=i %></a></li>
    <%} %>
    
    <li>
      <a href="#" aria-label="Next">
        <span aria-hidden="true">&raquo;</span>
      </a>
    </li>
  </ul>
</nav> 
 
<div class="panel panel-default">
		<div class="panel-heading">List of Authors</div>
		<table id="tblAuthor" class="table">
		<tr>
			<th># No.</th><th>Author Name</th><th>Edit</th><th>Delete</th>
		</tr>
		
		<% for(Author a:authorList){%>
			<tr><td><%=authorList.indexOf(a)+1%> </td><td><%=a.getName() %></td>
			<td><button type="button" href='editAuthor.jsp?authorId=<%=a.getId() %>'  id="btnEdit" data-toggle="modal" data-target="#editModal" class="btn btn-success btn-sm btnEdit">Edit</button></td>
			<td><button type="button"  class="btn btn-danger btn-sm">Delete</button></td>
			</tr>
		<%}%>
				
		</table>
</div>


<div id="editModal" class="modal fade" role="dialog">
  <div class="modal-dialog">
    <!-- Modal content-->
  	
  </div>
</div>


</body>
</html>