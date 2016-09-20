<%@include file="includes.jsp"%>
<html>

<h2>Add Library Branch </h2>
<div class="form-group">
	<form action="addLibraryBranch" method="post" >
		<input type="text" style="width: 400px;" name="name" class="form-control" 
			placeholder="Enter Library Branch's name" required autofocus/><br/>
			<input type="text" style="width: 400px;" name="address" class="form-control" 
			placeholder="Enter Library Branch's address" required autofocus/><br/>
		<input type="submit" class="btn btn-primary"/> 
	</form>
</div>
</html>