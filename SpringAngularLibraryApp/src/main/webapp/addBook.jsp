<%@page import="com.gcitsolutions.libraryapp.Entity.Publisher"%>
<%@page import="com.gcitsolutions.libraryapp.Entity.Author"%>
<%@page import="com.gcitsolutions.libraryapp.Entity.Genre"%>
<%@page import="com.gcitsolutions.libraryapp.Service.Administrator" %>
<%@page import="java.util.List"%>
<%@page import="org.springframework.context.annotation.AnnotationConfigApplicationContext" %>
<%@page import="org.springframework.context.ApplicationContext" %>
<%@page import="com.gcitsolutions.libraryapp.SpringMVCLibraryAppConfig" %>
<%
	ApplicationContext context = new AnnotationConfigApplicationContext(SpringMVCLibraryAppConfig.class);
	Administrator adminService = (Administrator) context.getBean(Administrator.class);
	List<Author> authors = (List<Author>) adminService.getAllAuthors();
	List<Publisher> publishers = (List<Publisher>) adminService.getAllPublishers();
	List<Genre> genres = (List<Genre>) adminService.getAllGenres();
%>
<html>
<head>

<link href="${pageContext.servletContext.contextPath}/resources/css/select2.css" rel="stylesheet" />

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
	Genre:<br/>
	<select id="genreId" name="genreId" multiple="multiple"  class="form-control"  >
		<% for(Genre genre : genres) { %>
			<option value="<%=genre.getId()%>"><%=genre.getName()%></option>
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



<script src="${pageContext.servletContext.contextPath}/resources/js/select2.min.js"></script>
<script type="text/javascript">

function populateAuthors(){
		<% for(Author a : authors) { %>
			$('#authorId').append($("<option/>").val(<%=a.getId()%>).text("<%=a.getName()%>"));
		<% } %>
}

$(document).ready(function(e) {
	
	//populateAuthors();
	$('#authorId').append('<option selected="selected" value="0" >None</option>');
	$('#genreId').append('<option selected="selected" value="0" >None</option>');
	$('#authorId').select2();
	$('#genreId').select2();
	
});
</script>
</body>
</html>