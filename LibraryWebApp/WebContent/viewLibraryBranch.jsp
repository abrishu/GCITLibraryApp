<%@include file="includes.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>View Library Branch</title>

<style>

.table-borderless tbody tr td, .table-borderless tbody tr th, .table-borderless thead tr th {
    border: none;
}

.center_div{
    margin: 0 auto;
    width:70% /* value of your choice which suits your alignment */
}
</style>

</head>

<body>


<div style="visibility:hidden" class="alert alert-success fade in">
    <a href="#" class="close" data-dismiss="alert">&times;</a>
    <strong>Success!</strong> Your message has been sent successfully.
</div>


<div class="container center_div">
	<div class="input-group">
	 	 <input type="text" style="width:400px;" class="form-control" placeholder="Branch Name" id="searchStringBranch" name="searchStringBranch">
	 	 <span class="input-group-addon"></span>
	 	 <input type="text" style="width:400px;" class="form-control" placeholder="Book Name" id="searchStringBook" name="searchStringBook">
	</div>
</div>


<nav aria-label="Page navigation">
  <ul id="paginationList" class="pagination">
  </ul>
</nav> 
 
<div class="panel panel-default">
		<div class="panel-heading">List of Books</div>
		<table id="tblLibraryBranch" class="table">
		</table>
</div>


<div id="editModal" class="modal fade" role="dialog">
  
</div>

<div id="deleteModal" class="modal fade" role="dialog">
  
</div>

<div id="addModal" class="modal fade" role="dialog">
  
</div>



<script type="text/javascript">

function processResultMessage(){
	
}



function returnString(data){
	if(data==null || data=='null'){
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

/*Populating Library Branch Table*/
function populateBookCopiesList(bookCopiesData){
	var headerData='<tr><th># No.</th><th>Branch Name</th><th>Branch Address</th><th>Book Name</th><th>No of Copies</th><th>Edit</th><th>Delete</th></tr>';
	var rowData='';
	var branchArray=[];
	var bookArray=[];
	var cnt=0;
	if(bookCopiesData!=null){
		
		$.each(bookCopiesData,function(index,bookCopy){
			//Setting the Values of Id and Name of Object
			branchArray[cnt]=this.branch.name;
			if(this.noOfCopies>5){
				rowData+='<tr>';
			}
			else{
				rowData+='<tr style="background-color:#CD5C5C">';
			}
			rowData+='<td>'+ (index+1) + '</td><td>' + returnString(this.branch.name) +'</td><td>' + this.branch.address +'</td><td>'+ this.book.title + '</td><td>'+ this.noOfCopies+ '</td>' ;
			//Setting the edit button for the given author object
			rowData+='<td><button type="button"'+ 'href="editBookCopies.jsp?bookId=' + this.book.id +'&branchId=' + this.branch.id  + '"data-toggle= "modal" data-target="#editModal" class="btn btn-success btn-sm btnEdit">Edit</button></td>';
			//Setting the delete button for the given author object
			rowData+='<td><button type="button"'+ 'href="delete.jsp?deleteAction=book&title=' +encodeURIComponent(this.title) + '&bookId=' + this.id   +  '" data-toggle="modal" data-target="#deleteModal" class="btn btn-danger btn-sm">Delete</button></td>'
			//Finally completing the row
			rowData+='</tr>';
			cnt++;
		});	
		$('#branch').typeahead({
			source:branchArray
		})
	}
	if(rowData==''){
		totalData=headerData+rowData +'<tr><td><span class="label label-danger">No Data to display</span></td><td></td><td></td><td></td><td></td><<td></td>/tr>';
	}else{
		totalData=headerData + rowData;
	}
	$('#tblLibraryBranch').html(totalData);
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
		dataList=dataList+'<li><a onclick = loadBookCopiesData("'+ $('#searchStringBranch').val() + '","' +$('#searchStringBook').val() +'",' + i + ');loadPaginationData("'+  $('#searchStringBranch').val() +'","'+ $('#searchStringBook').val() +'");>' + i + ' </a></li>';
	}
	$('#paginationList').html(firstArrow+dataList+lastArrow);
}


/* Loading author data based on the parameters given */
function loadBookCopiesData(searchStringBranch,searchStringBook,pageNo){ 
	window.currentPageNumber=pageNo;
	$.ajax({
		url:"searchBookCopies",
		data:{
			"searchStringBranch":searchStringBranch,
			"searchStringBook":searchStringBook,
			"pageNo":pageNo
		},
		datatype:"json"
	}).done(function(data){
		populateBookCopiesList(data);
	})
}


/*Loading pagination data based on the search string*/
function  loadPaginationData(searchStringBranch,searchStringBook){
	$.ajax({
		url:"paginationBookCopies",
		data:{
			"searchStringBranch":searchStringBranch,
			"searchStringBook":searchStringBook
		},
		datatype:"text"
	}).done(function(data){
		performPagination(data);
		highlight(window.currentPageNumber);
	})
}

	$(document).ready(function(){
		var searchStringBranch=returnString('<%=request.getParameter("searchStringBranch")%>');
		var searchStringBook=returnString('<%=request.getParameter("searchStringBook")%>');
		var pageNum = '<%=request.getParameter("pageNo")%>';
		if(pageNum=='null'){
			pageNum=1;
		}
		loadPaginationData(searchStringBranch,searchStringBook);
		loadBookCopiesData(searchStringBranch,searchStringBook,pageNum);
		/*on change AJAX call to load pagination info and authors*/
		$('#searchStringBranch').change(function(){
			loadBookCopiesData($(this).val(),$('#searchStringBook').val(),1);
			loadPaginationData($(this).val());
		});
		
		$('#searchStringBook').change(function(){
			loadBookCopiesData($('#searchStringBranch').val(),$(this).val(),1);
			loadPaginationData($(this).val());
		});
		
		
		/*When the modal screen is closed, below events would be triggered*/
		$('.modal').on('hidden.bs.modal', function(e)
				{
					$(this).removeData();
					loadBookCopiesData($('#searchStringBranch').val(),$('#searchStringBook').val(),window.currentPageNumber)
				}) ;
		
		
		/*Update Author*/
		$(document).on("click", "#btnUpdateAuthor", function(event){
			event.preventDefault();
			$.ajax({
				url:"",
				data:{

				},
				datatype:"text"
			}).done(function(){
				$('.label-success').css('visibility', 'visible');
				$('.label-success').text("Library details have been updated successfully");	
			});
		});	
	}); 
</script>


</body>
</html>