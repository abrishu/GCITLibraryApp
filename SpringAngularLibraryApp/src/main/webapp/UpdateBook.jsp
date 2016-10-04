<%@include file="includes.jsp"%>
<%@page import="java.util.List"%>
<%@page import="com.gcitsolutions.libraryapp.Entity.Book"%>
<%@page import="com.gcitsolutions.libraryapp.Entity.Publisher"%>
<%@page import="com.gcitsolutions.libraryapp.Entity.Author"%>
<%
	List<Book> books = (List<Book>) request.getAttribute("bookList");
	List<Author> authList = (List<Author>) request.getAttribute("authorList");
	List<Publisher> pubList=(List<Publisher>) request.getAttribute("pubList");
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
	addANoSelectionToSelectControls();
	popDefaultAuthors();
	popDefaultPublisher();
	$('#selBookList').change(function(){
		var data1={};
		data1["bookId"]=$('#selBookList').val();
		$.ajax({
	         url : "getBookInfo",
	         type : "GET",
	         data : data1,
	         dataType : "json",
	         success : function(data) {
	         				$('#bookTitle').val($('#selBookList option:selected').text());
	         		 		populateAuthors(data["authorList"]);
	         		  		populatePublisher(data["publisher"]);
	         },
	         error: function(xhr, status, error) {
	        	  var error1 = eval("(" + xhr.responseText + ")");
	        	  console.log(error1.Message);
	        	  console.log(geturl.getAllResponseHeaders());
	        	  alert("error!"+ geturl.getAllResponseHeaders());
	        	}
			});
	});
})

function addANoSelectionToSelectControls(){
	$('select').each(function(){
		if(!$("#authorId option[value='"+0+"']")){
			this.append($("<option selected='selected' />").val(0).text(None));
		}	
	});
}

function popDefaultAuthors(){
	addANoSelectionToSelectControls();
	<% for(Author a : authList) { %>
	$('#authorId').append($("<option/>").val(<%=a.getId()%>).text("<%=a.getName()%>"));
	<% } %>
}

function popDefaultPublisher(){
	addANoSelectionToSelectControls();
	<% for(Publisher pub : pubList) { %>
		$('#pubId').append($("<option/>").val(<%=pub.getId()%>).text("<%=pub.getName()%>"));
	<% } %>
	
}


function populateAuthors(authorList){
	var sel = $("#authorId");
	sel.empty();
	popDefaultAuthors();
	$.each(authorList, function(index,jsonObject) {
		if($("#authorId option[value='"+this.id+"']")){
			$("#authorId option[value='"+this.id+"']").remove();
			sel.append($("<option selected='selected' />").val(this.id).text(this.name));
		} 
	});
}


function populatePublisher(publisher){
	var sel = $("#pubId");
	sel.empty();
	popDefaultPublisher();
	var sel = $("#pubId");
	if($("#pubId option[value='"+publisher.id+"']")){
		$("#pubId option[value='"+publisher.id+"']").remove();
		sel.append($("<option selected='selected' />").val(publisher.id).text(publisher.name));
	} 
}


</script>

</head>
<body>
<div class="container center_div jumbotron">
<h2>Update Book</h2>
<form action="updateBook" method="post">
	Original Title: <select id='selBookList' class="form-control" 
			 name="bookTitle">
			 <% for(Book b : books) { %>
			<option value="<%=b.getId()%>"><%=b.getTitle()%></option>
		<% } %>
			 </select>
			 <br/>
	
	New Title: <input type="text" id="bookTitle" class="form-control" 
			placeholder="Enter Book's title" name="bookTitleNew" /><br/>
	Author:<br/>
	<select multiple id="authorId" name="authorId"  class="form-control" >

	</select><br/>
	Publisher:
	<select id="pubId" name="pubId" class="form-control">
	</select><br/>
	<input type="submit" class="btn btn-primary" />
</form>
</div>


</body>
</html>