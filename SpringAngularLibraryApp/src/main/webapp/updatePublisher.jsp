<%@include file="includes.jsp"%>
<%@page import="java.util.List"%>
<%@page import="com.gcitsolutions.libraryapp.Entity.Publisher"%>
<%
List<Publisher> pubList=(List<Publisher>)request.getAttribute("pubList");
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
	$('#publisherId').change(function(){
		var data1={};
		data1["publisherId"]=$(this).val();
		$('#name').val($('#publisherId option:selected').text());
		$.ajax({
	         url : "getPublisherInfo",
	         type : "GET",
	         data : data1,
	         dataType : "json",
	         success : function(data) {		
	         		 		$('#address').val(data.address);
	         		 		$('#phone').val(data.phone);
	         		 		
	         }
			});
	});
})

</script>

</head>
<body>
<div class="container center_div jumbotron">
<h2>Update Publisher</h2>
<form action="updatePublisher" method="post">
	Original Name: <select style="width: 400px;"  id="publisherId" name="publisherId"   class="form-control">
		<% for(Publisher p : pubList) { %>
			<option value="<%=p.getId()%>"><%=p.getName()%></option>
		<% } %>
	</select>
	New Name: 
	<input type="text" class="form-control" id="name" name="name" /><br/>
	Address:
	<input type="text" class="form-control" id="address" name="address" /><br/><br/>
	Phone:
	<input type="text" class="form-control" id="phone"  name="phone"/><br/><br/>
	<input type="submit" class="btn btn-primary" />
</form>
</div>


</body>
</html>