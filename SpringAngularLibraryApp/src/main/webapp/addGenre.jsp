
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
<h2>Add Genre</h2>
<div class="form-group">
	<form action="addGenre" method="post" >
		<input type="text" style="width: 400px;" name="genreName" class="form-control" 
			placeholder="Enter Genre name" required autofocus/><br/>
		<input type="submit" class="btn btn-primary"/> 
	</form>
</div>
</div>
</html>