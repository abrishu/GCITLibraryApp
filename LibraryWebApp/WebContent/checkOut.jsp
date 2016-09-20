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

function loadAvailableBooks(){
	var data1={};
	data1["branchId"]=$('#branchId').val();
	$.ajax({
        url : "getAvailableBooksForABranch",
        type : "GET",
        data : data1,
        dataType : "json",
        success : function(data) {
        	populateAvailableBooks(data);
        }
	});
}

function loadBranch(){
	<% for(LibraryBranch lib : branchList) { %>
		$('#branchId').append($("<option/>").val(<%=lib.getId()%>).text("<%=lib.getName()%>"));
	<% } %>
	loadAvailableBooks();
}

function populateAvailableBooks(bookList){
	var sel = $("#bookId");
	sel.empty();
	$.each(bookList, function(index,jsonObject) {
		if($("#bookId option[value='"+this.id+"']")){
			$("#bookId option[value='"+this.id+"']").remove();
			sel.append($("<option selected='selected' />").val(this.id).text(this.title));
		} 
	});
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
        					alert('Howdy !!! '+ data.name);
        					loadBranch();
        				}else{
        					alert('Invalid user !!! Please enter a valid card number');
        					$('#cardNum').val('');
        				}
        	}
	});
}

$(document).ready(function(){
	
	$('#cardNum').change(function(){
		$('#branchId').empty();
		$('#bookId').empty();
		var cardNum=$(this).val();
		validateBorrower(cardNum)
	});
	
	$('#branchId').change(function(){
		$('#bookId').empty();
		loadAvailableBooks();
	});
	
});


</script>


</head>

<body>

<h2>Check out a book</h2>
<form action="performCheckOut" method="post">
	Card Number: <input type="text" class="form-control" 
			placeholder="Enter Card Number" name="cardNum" id="cardNum" /><br/>
	Branch:
	<select id="branchId" name="branchId"  class="form-control" >
		
	</select><br/>
	Book:
	<select name="bookId" id="bookId" class="form-control">
		
	</select><br/>
	<input type="submit" class="btn btn-primary" />
</form>


</body>

</html>