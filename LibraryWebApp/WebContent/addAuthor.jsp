<%@include file="includes.jsp"%>
<html>
<head>
<style>
.center_div{
    margin: 0 auto;
    width:70% /* value of your choice which suits your alignment */
}
</style>

</head>

<div class="container center_div jumbotron">
<h2>Add Author</h2>
<div class="form-group">
	<form action="addAuthor" method="post" >
		<input type="text" style="width: 400px;" name="authorName" class="form-control" 
			placeholder="Enter Author's name" required autofocus/><br/>
		<input type="submit" class="btn btn-primary"/> 
	</form>
</div>
</div>
</html>