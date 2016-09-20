<%@include file="includes.jsp"%>
<%@page import="com.gcitsolutions.libraryapp.Entity.Publisher"%>
<%@page import="com.gcitsolutions.libraryapp.Entity.Author"%>
<%@page import="java.util.List"%>
<%
	List<Author> authors = (List<Author>) request.getAttribute("authorList");
	List<Publisher> publishers = (List<Publisher>) request.getAttribute("pubList");
%>
<html>
<head>


<style>
.center_div{
    margin: 0 auto;
    width:70% /* value of your choice which suits your alignment */
}
</style>

</head>

<body>
<div class="container center_div jumbotron">
<h2>Add Book</h2>
<form action="addBook" method="post">
	Title: <br/> <input type="text" style="width: 400px;" class="form-control" 
			placeholder="Enter Book's title"  name="bookTitle" required autofocus /><br/>
	Author:<br/>
	<select id="authorId" name="authorId" multiple="multiple"  class="form-control"  >
		<% for(Author a : authors) { %>
			<option value="<%=a.getId()%>"><%=a.getName()%></option>
		<% } %>
	</select><br/><br/>
	Publisher:<br/>
	<select name="pubId" class="form-control" title="Choose one of the following">
		<% for(Publisher p : publishers) { %>
			<option value="<%=p.getId()%>"><%=p.getName()%></option>
		<% } %>
	</select><br/><br/>
	<input type="submit" class="btn btn-primary" />
</form>
</div>


<script type="text/javascript">

function populateAuthors(){
		<% for(Author a : authors) { %>
			$('#authorId').append($("<option/>").val(<%=a.getId()%>).text("<%=a.getName()%>"));
		<% } %>
}

$(document).ready(function(e) {
	
	//populateAuthors();
	$('#authorId').multiselect();
	
});
</script>
</body>
</html>