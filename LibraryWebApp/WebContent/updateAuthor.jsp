<%@include file="includes.jsp"%>
<%@page import="java.util.List"%>
<%@page import="com.gcitsolutions.libraryapp.Entity.Author"%>
<%
List<Author> authorList=(List<Author>)request.getAttribute("authorList");
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Update Author Details</title>
<style>
.center_div{
    margin: 0 auto;
    width:70% /* value of your choice which suits your alignment */
}
</style>

<script type="text/javascript">
$(document).ready(function(){
	$('#authorId').change(function(){
		$('#newName').val($('#authorId option:selected').text());
	})
})

</script>

</head>
<body>
<div class="container center_div jumbotron">
<h2>Update Author Details</h2>
<form action="updateAuthor" method="post">
	Original Name: <select style="width: 400px;"  id="authorId" name="authorId"   class="form-control">
		<% for(Author a : authorList) { %>
			<option value="<%=a.getId()%>"><%=a.getName()%></option>
		<% } %>
	</select>
	New Name:<br/> 
	<input type="text" id="newName" style="width: 400px;" class="form-control" name="newName"><br/>
	
	
	<input type="submit" class="btn btn-primary" />
</form>
</div>


</body>
</html>