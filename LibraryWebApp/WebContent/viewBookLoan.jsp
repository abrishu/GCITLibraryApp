<%@include file="includes.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>View Authors</title>
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

</head><body>

<!-- 
<div  style="visibility:hidden" class="alert alert-success fade in">
    <a href="#" class="close" data-dismiss="alert">&times;</a>
    <div id="divMsg">Your message has been sent successfully.</div>
</div>  -->
 
 

<input type="text" style="width:400px;" class="form-control" placeholder="Please enter your Card Number" id="cardNumber" name="cardNumber">
<br/><br/>

<div class="alertDiv">

</div>

<div id="mainBody">
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
				<table id="tblAvailableBooks" class="table">
				</table>
		</div>
		
		
		<div id="editModal" class="modal fade" role="dialog">
		  
		</div>
		
		<div id="deleteModal" class="modal fade" role="dialog">
		  
		</div>
		
		<div id="addModal" class="modal fade" role="dialog">
		  
		</div>

</div>

<script type="text/javascript">
function frameAnAlert(error,message){
	var alertHtml='';
	if(message!=''){
		if(error){
			alertHtml='<div  style="visibility:hidden" class="alert alert alert-danger fade in"> <a href="#" class="close" data-dismiss="alert">&times;</a><div id="divMsg"> '+ message +' .</div></div>';
		}else{
			alertHtml= '<div  style="visibility:hidden" class="alert alert-success fade in"> <a href="#" class="close" data-dismiss="alert">&times;</a><div id="divMsg"> '+ message +' .</div></div>';
		}
		$('.alertDiv').html(alertHtml);
		
		if(error){
			$('.alert-danger').css('visibility','visible');
		}else{
			$('.alert-success').css('visibility','visible');
		}	
	}
}


function printResult(){
	var result='<%=request.getAttribute("result")%>';
	if(result!='null'){
		$('.alert-success').css("visibility","visible");
		$('#divMsg').html(result);	
	}
}


function getCheckedOutBooks(){
	var cardNo=$('#cardNumber').val();
	$.ajax({
		url : "getReturnInfo",
        type : "GET",
        data :{
        	"cardNum":cardNo
        },
        datatype:"json"
	}).done(function(checkedOutBooks){
		window.checkedOutList=checkedOutBooks;
		loadBookCopiesData($('#searchStringBranch').val(),$('#searchStringBook').val(),window.currentPageNumber);
	});
}

function isCheckedOut(bookId,branchId){
	if(checkedOutList==null){
		return false;
	}else{
		if($.grep(checkedOutList,function(n,i){ return n.book.id==bookId && n.branch.id==branchId})==''){
			return false;
		}else{
			return true;
		}
	}
	
}

/*Populating Author Table*/
function populateAvailableBooks(bookCopiesData){
	var headerData='<tr><th># No.</th><th>Branch Name</th><th>Branch Address</th><th>Book Name</th><th>No of Copies</th><th>Check Out</th></tr>';
	var rowData='';
	var branchArray=[];
	var bookArray=[];
	var cnt=0;
	if(bookCopiesData!=null){
		
		$.each(bookCopiesData,function(index,bookCopy){
				//Setting the Values of Id and Name of Object
				rowData+='<tr><td>'+ (index+1) + '</td><td>' + returnString(this.branch.name) +'</td><td>' + this.branch.address +'</td><td>'+ this.book.title + '</td><td>'+ this.noOfCopies+ '</td>' ;
				//Setting the edit button for the given author object
				if(!isCheckedOut(this.book.id,this.branch.id)){
					rowData+='<td><button type="button"'+ 'href="checkOutBookCopies.jsp?bookId=' + this.book.id +'&branchId=' + this.branch.id +'&txnType=Checkout' + '&cardNumber=' + $('#cardNumber').val()  +'"data-toggle= "modal" data-target="#editModal" class="btn btn-success btn-sm btnEdit">Check out</button></td>'
				}else{
					rowData+='<td><button type="button"'+ 'href="checkOutBookCopies.jsp?bookId=' + this.book.id +'&branchId=' + this.branch.id +'&txnType=Return'+'&cardNumber=' + $('#cardNumber').val()  + '"data-toggle= "modal" data-target="#editModal" class="btn btn-danger btn-sm btnEdit">Return</button></td>'
				}
				rowData+='</tr>';

			cnt++;
		});	
		$('#branch').typeahead({
			source:branchArray
		})
	}
	if(rowData==''){
		totalData=headerData+rowData +'<tr><td><span class="label label-danger">No Data to display</span></td><td></td><td></td><td></td><td></td>/tr>';
	}else{
		totalData=headerData + rowData;
	}
	$('#tblAvailableBooks').html(totalData);
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
		populateAvailableBooks(data);
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

function validateBorrower(cardNum){
	var data1={};
	data1["cardNum"]=cardNum;
	$.ajax({
        url : "validateBorrower",
        type : "GET",
        data : data1,
        dataType : "json",
        success : function(data) {
        				if(data!=null){
        					frameAnAlert(false, "Hello <strong>"+ data.name+" </strong>!!! Welcome to GCIT Library management system. Please go through our catalog of books by entering branch names and books");
        					$('#mainBody').show();
        					getCheckedOutBooks();
        					loadPaginationData("","");
        					loadBookCopiesData("","",1);
        					
        				}else{
        					frameAnAlert(true,'Invalid user !!! Please enter a valid card number');
        					$('#tblAvailableBooks').html('');
        					$('#mainBody').hide();
        					//$('#cardNumber').val('');
        				}
        	}
	});
}

$('#cardNumber').change(function(){
	$('.alertDiv').html('');
	$('#searchStringBranch').empty();
	$('#searchStringBook').empty();
	var cardNumber=$(this).val();
	validateBorrower(cardNumber);
});


	$(document).ready(function(){
		var searchStringBranch=returnString('<%=request.getParameter("searchStringBranch")%>');
		var searchStringBook=returnString('<%=request.getParameter("searchStringBook")%>');
		var pageNum = '<%=request.getParameter("pageNo")%>';
		if(pageNum=='null'){
			pageNum=1;
		}
		
		$('#mainBody').hide();
		
		/*on change AJAX call to load pagination info and authors*/
		$('#searchStringBranch').change(function(){
			getCheckedOutBooks();
			loadBookCopiesData($(this).val(),$('#searchStringBook').val(),1);
			loadPaginationData($(this).val(),$('#searchStringBook').val());
		});
		
		$('#searchStringBook').change(function(){
			getCheckedOutBooks();
			loadBookCopiesData($('#searchStringBranch').val(),$(this).val(),1);
			loadPaginationData($('#searchStringBranch').val(),$(this).val());
		});
		
		
		/*When the modal screen is closed, below events would be triggered*/
		$('.modal').on('hidden.bs.modal', function(e)
				{
					$(this).removeData();
					getCheckedOutBooks();
					
				}) ;
	});
</script>


</body>
</html>