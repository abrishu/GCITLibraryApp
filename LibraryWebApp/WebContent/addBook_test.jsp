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
<link href="http://netdna.bootstrapcdn.com/twitter-bootstrap/2.3.1/css/bootstrap-combined.min.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" media="screen" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.9.3/css/bootstrap-select.min.css">
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script src="http://netdna.bootstrapcdn.com/twitter-bootstrap/2.3.1/js/bootstrap.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.9.3/js/bootstrap-select.min.js"></script>

<style>
.center_div{
    margin: 0 auto;
    width:70% /* value of your choice which suits your alignment */
}
</style>
<script type="text/javascript">

$(document).ready(function(e) {
	
});
</script>

</head>

<body>
<div class="container center_div jumbotron">
<h2>Add Book</h2>
<form action="addBook" method="post">
	Title: <br/> <input type="text" style="width: 400px;" class="form-control" 
			placeholder="Enter Book's title"  name="bookTitle" required autofocus /><br/>
	
	
	<input type="submit" class="btn btn-primary" />
</form>
</div>



</body>
</html>