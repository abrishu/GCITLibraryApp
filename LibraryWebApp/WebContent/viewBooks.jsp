<%@include file="includes.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>View Authors</title>

<style>

.table-borderless tbody tr td, .table-borderless tbody tr th, .table-borderless thead tr th {
    border: none;
}
</style>
</head>

</head><body>


<div  style="visibility:hidden" class="alert alert-success fade in">
    <a href="#" class="close" data-dismiss="alert">&times;</a>
    <div id="divMsg">Your message has been sent successfully.</div>
</div>

<div class="input-group">
	 <form action="searchBooks" method="get">
	 	 <input type="text" class="form-control" placeholder="Book Name" id="searchString" name="searchString" aria-describedby="basic-addon1">
 	 </form>
</div>

<nav aria-label="Page navigation">
  <ul id="paginationList" class="pagination">
  </ul>
</nav> 
 
<div class="panel panel-default">
		<div class="panel-heading">List of Books</div>
		<table id="tblBook" class="table">
		</table>
</div>


<div id="editModal" class="modal fade" role="dialog">
  
</div>

<div id="deleteModal" class="modal fade" role="dialog">
  
</div>

<div id="addModal" class="modal fade" role="dialog">
  
</div>



<script type="text/javascript">

function printResult(){
	var result='<%=request.getAttribute("result")%>';
	if(result!='null'){
		$('.alert-success').css("visibility","visible");
		$('#divMsg').html(result);	
	}
}



function returnPublisherName(pubObj){
	if(pubObj==null || pubObj.name==null){
		return "";
	}else{
		return pubObj.name;
	}	
}

function returnString(data){
	if(data==null){
		return "";
	}else{
		return data;
	}
}

/*Returning author info as a Table*/

function returnListRow(authorData){
	var tmpRow='';
	var tmpTable='';
	if(authorData==null){
		return tmpRow;
	}else{
		$.each(authorData,function(index,authorObj){
			tmpRow+='<tr><td>'+returnString(authorObj.name)+'</td></tr>';
		});	
		tmpTable='<table class="table table-borderless">' + tmpRow + '</table>';
		return tmpTable;
	}
	
}

/*Populating Author Table*/
function populateBookList(bookData){
	var headerData='<tr><th># No.</th><th>Book Name</th><th>Publisher Name</th><th>Author Name(s)</th><th>Edit</th><th>Delete</th></tr>';
	var rowData='';
	if(bookData!=null){
		$.each(bookData,function(index,authorObject){
			//Setting the Values of Id and Name of Object
			rowData+='<tr><td>'+ (index+1) + '</td><td>' + returnString(this.title) +'</td><td>' + returnPublisherName(this.publisher) +'</td><td>'+ returnListRow(this.authorList) + '</td>' ;
			//Setting the edit button for the given author object
			rowData+='<td><button type="button"'+ 'href="editBook.jsp?bookId=' + this.id +'" id="btnEdit" data-toggle="modal" data-target="#editModal" class="btn btn-success btn-sm btnEdit">Edit</button></td>';
			//Setting the delete button for the given author object
			rowData+='<td><button type="button"'+ 'href="delete.jsp?deleteAction=book&title=' +encodeURIComponent(this.title) + '&bookId=' + this.id   +  '" data-toggle="modal" data-target="#deleteModal" class="btn btn-danger btn-sm">Delete</button></td>'
			//Finally completing the row
			rowData+='</tr>';
		});	
	}
	if(rowData==''){
		totalData=headerData+rowData +'<tr><td><span class="label label-danger">No Data to display</span></td><td></td><td></td><td></td><td></td><<td></td>/tr>';
	}else{
		totalData=headerData + rowData;
	}
	$('#tblBook').html(totalData);
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
		dataList=dataList+'<li><a onclick = loadBookData("'+ $('#searchString').val() + '",' + i + ');loadPaginationData("'+  $('#searchString').val() +'");>' + i + ' </a></li>';
	}
	$('#paginationList').html(firstArrow+dataList+lastArrow);
}


/* Loading author data based on the parameters given */
function loadBookData(searchString,pageNo){ 
	window.currentPageNumber=pageNo;
	$.ajax({
		url:"searchBooks",
		data:{
			"searchString":searchString,
			"pageNo":pageNo
		},
		datatype:"json"
	}).done(function(data){
		populateBookList(data);
	})
}


/*Loading pagination data based on the search string*/
function  loadPaginationData(searchString){
	$.ajax({
		url:"paginationBooks",
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
		
		printResult();
		
		var searchString='<%=request.getParameter("searchString")%>';
		var pageNum = '<%=request.getParameter("pageNo")%>';
		if(searchString=="null"){
			searchString="";
		}
		if(pageNum=='null'){
			pageNum=1;
		}
		loadPaginationData(searchString);
		loadBookData(searchString,pageNum);
		
		
		/*on change AJAX call to load pagination info and authors*/
		$('#searchString').change(function(){
			loadBookData($(this).val(),1);
			loadPaginationData($(this).val());
		});
		
		
		/*When the modal screen is closed, below events would be triggered*/
		$('.modal').on('hidden.bs.modal', function(e)
				{
					$(this).removeData();
					loadBookData($('#searchString').val(),window.currentPageNumber)
				}) ;
		
		
		/*Update Author*/
		$(document).on("click", "#btnUpdateAuthor", function(event){
			event.preventDefault();
			$.ajax({
				url:"updateBook",
				data:{
					"bookTitle":$('#authorId').val(),
					"bookTitleNew":$('#newName').val(),
					"authorId":$('#authorId').val(),
					"pubId":$('#pubId').val()
				},
				datatype:"text"
			}).done(function(){
				$('.label-success').css('visibility', 'visible');
				$('.label-success').text("Author details have been updated successfully");	
			});
		});
		
		
		
	}); 
</script>


</body>
</html>