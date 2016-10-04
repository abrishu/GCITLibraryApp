<%@include file="includes.jsp"%>
<%@page import="com.gcitsolutions.libraryapp.Entity.Publisher"%>
<%@page import="com.gcitsolutions.libraryapp.Entity.Book"%>
<%@page import="com.gcitsolutions.libraryapp.Entity.Author"%>
<%@page import="java.util.List"%>
<%
	List<Book> books = (List<Book>) request.getAttribute("bookList");
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
$('body').on('click', 'a.delete', function() {
	var data1={};
	if(confirm('Are you sure you want to delete the book ?')){
		data1["bookId"]=$(this).closest('tr').attr('id');
		$.ajax({
	        url : "deleteBook",
	        type : "GET",
	        data : data1,
	        dataType : "text",
	        success : function(data) {
	       	  alert(data);
	        }
		});
		$(this).closest('tr').remove();
	}else{
	}
    
});

</script>

</head>

<body>
<div class="container center_div jumbotron">
<h2>Books</h2>
<form action="" method="post">
	<table id="tblBookInfo" class="table table-bordered">
		<% for(Book b : books) { %>
			<tr id=<%=b.getId()%>><td><%=b.getId() %></td><td><%=b.getTitle()%></td><td>
			<a class='delete' href="#">Delete</a></td></tr>
		<% } %>
	</table>
</form>
</div>



</body>
</html>