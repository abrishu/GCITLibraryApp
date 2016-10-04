<%@include file="includes.jsp"%>
<%@page import="com.gcitsolutions.libraryapp.Entity.LibraryBranch"%>
<%@page import="com.gcitsolutions.libraryapp.Entity.Book"%>
<%@page import="java.util.List"%>
<%
	List<LibraryBranch> branchList = (List<LibraryBranch>) request.getAttribute("branchList");
	List<Book> bookList = (List<Book>) request.getAttribute("bookList");
%>
<html>
<head>

<style>
.center_div{
    margin: 0 auto;
    width:70% /* value of your choice which suits your alignment */
}
</style>
<script type="text/javascript">

function getBookCopiesInfo(data1){
	$.ajax({
        url : "getBookCopiesInfo",
        type : "GET",
        data : data1,
        dataType : "text",
        success : function(data) {
        		if(isNaN(data)){
        			$('#noOfCopies').val(0);
        		}else{
        			$('#noOfCopies').val(data);
        		}
        },
        error: function(xhr, status, error) {
       	  var error1 = eval("(" + xhr.responseText + ")");
       	  console.log(error1.Message);
       	  console.log(geturl.getAllResponseHeaders());
       	  alert("error!"+ geturl.getAllResponseHeaders());
       	}
		});
}

$(document).ready(function(){
	var data1={};
	$('#bookId').change(function(){
		if($('#branchId').val()!=0 && $('#authorId').val()!=0){
			data1["branchId"]=$('#branchId').val();
			data1["bookId"]=$('#bookId').val();
			getBookCopiesInfo(data1);	
		}
	});

	$('#branchId').change(function(){
		if($('#branchId').val()!=0 && $('#bookId').val()!=0){
			data1["branchId"]=$('#branchId').val();
			data1["bookId"]=$('#bookId').val();
			getBookCopiesInfo(data1);	
		}
	});
	
	
});


</script>

</head>

<body>
<div class="container center_div jumbotron">
<h2>Add Book Copies to a Branch</h2>
<form action="addBookCopies" method="post">
	
	Book:
	<select id="bookId" name="bookId"  class="form-control" >
		<% for(Book b : bookList) { %>
			<option value="<%=b.getId()%>"><%=b.getTitle()%></option>
		<% } %>
	</select><br/>
	Branch:
	<select id="branchId" name="branchId" class="form-control">
		<% for(LibraryBranch lib : branchList) { %>
			<option value="<%=lib.getId()%>"><%=lib.getName()%></option>
		<% } %>
	</select><br/>
	Existing No of Copies
	<input type="text" readOnly class="form-control" 
			id="noOfCopies" name="noOfCopies" /><br/>
	<input type="text" class="form-control" 
			placeholder="Enter No of Copies" id="addCopies" name="addCopies" /><br/>	
	
	<input type="submit" class="btn btn-primary" />
</form>
</div>



</body>
</html>