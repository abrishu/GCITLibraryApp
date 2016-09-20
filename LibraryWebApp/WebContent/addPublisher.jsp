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
<h2>Add Publisher</h2>
<div class="form-group">
	<form action="addPublisher" method="post" >
		<input type="text" style="width: 400px;" name="name" class="form-control" 
			placeholder="Enter  name" required autofocus/><br/>
			<input type="text" style="width: 400px;" name="address" class="form-control" 
			placeholder="Enter address" required autofocus/><br/>
			<input type="text" style="width: 400px;" name="phone" class="form-control" 
			placeholder="Enter phone" required autofocus/><br/>
		<input type="submit" class="btn btn-primary"/> 
	</form>
</div>
</div>
</html>