<%@include file="includes.jsp"%>
<%@page import="java.util.List"%>
<%@page import="com.gcitsolutions.libraryapp.Entity.LibraryBranch"%>
<%
LibraryBranch lib=(LibraryBranch)request.getAttribute("branch");
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Update book Details</title>

<style>
.center_div{
    margin: 0 auto;
    width:70% /* value of your choice which suits your alignment */
}
</style>

<script type="text/javascript">
$(document).ready(function(){
})

</script>

</head>
<body>
<div class="container center_div jumbotron">
<h2>Update Library Branch</h2>
<form action="updateLibraryBranch" method="post">
	Original Name: <input type="text" id='name' readOnly class="form-control" 
			 name="bookTitle">
			 <br/>
	New Name: 
	<input type="text" class="form-control" name="name" value=<% %>/><br/>
	Address:
	<input type="text" class="form-control" name="address" value=<% %>  /><br/><br/>
	Phone:
	<input type="text" class="form-control" name="phone" value=<% %> /><br/><br/>
	<input type="submit" class="btn btn-primary" />
</form>
</div>


</body>
</html>