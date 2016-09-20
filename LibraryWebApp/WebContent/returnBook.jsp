<%@include file="includes.jsp"%>
<%@page import="java.util.List"%>
<%@page import="com.gcitsolutions.libraryapp.Entity.Book"%>
<%@page import="com.gcitsolutions.libraryapp.Entity.LibraryBranch"%>
<%
	List<LibraryBranch> branchList = (List<LibraryBranch>) request.getAttribute("branchList");
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style>
.center_div{
    margin: 0 auto;
    width:70% /* value of your choice which suits your alignment */
}
</style>

<script type="text/javascript">

$(document).ready(function(){
	
	$('#cardNum').change(function(){
		$('#result').text('');
		var cardNum=$(this).val();
		$("#tblBookInfo > tbody").html("");
		validateBorrower(cardNum)
	});
	$('body').on('click', 'a.return', function() {
		var rowNum;
		var data1={};
		if(confirm('Are you sure you want to delete the book ?')){
			data1["bookId"]=$(this).closest('tr').attr('bookid');
			data1["branchId"]=$(this).closest('tr').attr('branchid');
			data1["cardNo"]=$(this).closest('tr').attr('cardno');
			
			$.when(
			$.ajax({
		        url : "returnBook",
		        type : "GET",
		        data : data1,
		        dataType : "text",
		        success : function(data) {
		        }
			})
			).then(function(){
				$('#result').text("The book was returned successfully");
			});
		}else{
		}
	    
	});
	
});


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
        					alert('Howdy !!! '+ data.name);
        					loadCheckedOutBooks();
        				}else{
        					alert('Invalid user !!! Please enter a valid card number');
        					$('#cardNum').val('');
        				}
        	}
	});
}

function loadCheckedOutBooks(){
	var data1={};
	data1["cardNum"]=$('#cardNum').val();
	$.ajax({
        url : "getReturnInfo",
        type : "GET",
        data : data1,
        dataType : "json",
        success : function(data) {
        	populateCheckedOutBooks(data);
        }
	});
}

function populateCheckedOutBooks(bookloanlist){
	$('#tblBookInfo tbody').append('<th>Branch</th><th>Book</th><th>Date out</th><th>Due Date</th><th>Action</th>');
	$.each(bookloanlist, function(index,jsonObject) {	
		$('#tblBookInfo tbody').append('<tr class="child" id=' +(this.book.id +'-'+this.branch.id +'-'+$('#cardNum').val())  + ' bookid='+ this.book.id +' branchid='+this.branch.id +' cardno='+$('#cardNum').val()+'><td>'+ this.branch.name+'</td><td>'+ this.book.title +'</td><td>'+ this.dateOut +'</td><td>' + this.dueDate +'</td><td><a class="return" href="#">Return</a></tr>');
	});
}

</script>


</head>

<body>
<div class="container center_div jumbotron">
<h2>Return book</h2>
<form action="performCheckOut" method="post">
	Card Number: <input type="text" class="form-control" 
			placeholder="Enter Card Number" name="cardNum" id="cardNum" /><br/>
			<table id="tblBookInfo" class="table table-bordered">
			<tbody>
			
			</tbody>
			</table>
			<span id="result" style="color:green"></span>
</form>
</div>



</body>

</html>