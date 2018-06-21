<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="model.*"%>
<%@ page import="java.util.List"%>
<%
	List<Role> roles = (List<Role>)request.getAttribute("roles");
	List<Resource> resourses = (List<Resource>)request.getAttribute("resources");
	Access a = (Access)request.getAttribute("access");
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<span class="nav"><a href="/access">Back</a></span><p/>
<form name="post" method="post" action="edit">
<input type="hidden" name="id" value="<%= a.getId() %>"/>
<input type="hidden" name="info" value="editar"/>
<select name="rolesl">
<% if (roles.size() > 0) { %>
	<% for (int i = 0;i<roles.size();i++) { %>
		<% Role r = (Role)roles.get(i); %>
		<option value="<%= r.getKey() %>"><%= r.getName() %></option>
	<% } %>
<% } else { %>
<p/><span class="heading">No hay empleados registrados</span>
<% } %>
</select>

<select name="resourcesl">
<% if (resourses.size() > 0) { %>
	<% for (int i = 0;i<resourses.size();i++) { %>
		<% Resource res = (Resource)resourses.get(i); %>
		<option value="<%= res.getKey() %>"><%= res.getUrl() %></option>
	<% } %>
<% } else { %>
<p/><span class="heading">No hay empleados registrados</span>
<% } %>
</select>
<input type="submit" value="Submit">
</form>
</body>
</html>