<%@include file="includes.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="com.gcitsolutions.libraryapp.Entity.Publisher"%>
<%@page import="com.gcitsolutions.libraryapp.Entity.Book"%>
<%@page import="com.gcitsolutions.libraryapp.Entity.Author"%>
<%@page import="java.util.List"%>
<%
	List<Book> books = (List<Book>) request.getAttribute("bookList");
	System.out.println(books.size());
	System.out.println(books.get(0).getTitle());
%>
<html>
<head>
<style>

.table-borderless tbody tr td, .table-borderless tbody tr th, .table-borderless thead tr th {
    border: none;
}
.table{
width: 200px;
 height: 400px;
 overflow: auto;
}

</style>
<title> Get Books</title>
</head>
<body>
<h3>No of Books :"${bookList.size()}"</h3>
<div id="viewBooks">
	<table class="table" >
	<c:forEach items="${bookList}" var="element">
	  <tr>
	    	<td>${element.id}</td>
	     	<td>${element.title}</td>
	     	<td>
	     		<table  style="margin-bottom:0px" class="table table-borderless">
				      <c:forEach items="${element.authorList}" var="author">
				      	<tr><td style="padding-top:0px;padding-bottom:0px">${author.name}</td></tr>
				      </c:forEach>
		     	</table>
	      	</td>
	  </tr>
	</c:forEach>
	</table>
</div>
</body>
</html>