<%@include file="includes.jsp"%>
<%@page import="java.util.List"%>
<%@page import="com.gcitsolutions.libraryapp.Entity.LibraryBranch"%>
<%
List<LibraryBranch> libList=(List<LibraryBranch>)request.getAttribute("branchList");
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
	$('#selBranch').change(function(){
		if($('#selBranch').val()=='0'){
			$('#name').val('');
	 		$('#address').val('');
		}
		var data1={};
		data1["branchId"]=$('#selBranch').val();
		$.ajax({
	         url : "getBranchInfo",
	         type : "GET",
	         data : data1,
	         dataType : "json",
	         success : function(data) {
	        	 		$('#name').val(data["name"]);
	        	 		$('#address').val(data["address"]);
	         }
			});
	});
})

</script>

</head>
<body>
<div class="container center_div jumbotron">
<h2>Update Library Branch</h2>
<form action="updateLibraryBranch" method="post">
	Original Name: <select id='selBranch' name= "selBranch" class="form-control" >
			 <option value="0" selected="selected">Select Any option</option>
			 <% for(LibraryBranch br : libList) { %>
				<option value="<%=br.getId()%>"><%=br.getName()%></option>
		<% } %>
			 </select>
	New Name: 
	<input type="text" class="form-control" id="name" name="name"/><br/>
	Address:
	<input type="text" class="form-control" id="address" name="address" /><br/><br/>

	<input type="submit" class="btn btn-primary" />
</form>
</div>


</body>
</html>