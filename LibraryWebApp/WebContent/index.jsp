<%@include file="includes.jsp"%>

<%
	String result = (String) request.getAttribute("result");
%>

<% if(result != null) {%>
	<span style="color:green;"><%=result %></span><br/>
<% } %>
<center>
	<h3>Welcome to the GCIT Library Management System!</h3>
</center>
