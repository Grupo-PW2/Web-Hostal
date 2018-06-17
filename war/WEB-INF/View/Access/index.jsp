<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List"%>
<%@ page import="model.entity.*"%>
<%
	List<Access> accesses = (List<Access>)request.getAttribute("accesses");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<a href="/index.html">Regresar</a>
<table border="0" cellspacing="1" cellpadding="5" bgcolor="#CCCCCC">
<tr>
<td>ID</td>
<td>Role</td>
<td>Resource</td>
<td>Status</td>
</tr>
<% if (accesses.size() > 0) { %>
	<% for (int i = 0;i<accesses.size();i++) { %>
		<% Access e = (Access)accesses.get(i); %>
		<tr style="background:#ffffff" onMouseOver="this.style.background='#eeeeee';" onMouseOut="this.style.background='#ffffff';">
		<td nowrap style="font-weight: bold;"><%= e.getId() %></td>
		<td style="font-weight: bold;"><%= e.getIdRole() %></td>
		<td style="font-weight: bold;"><%= e.getIdResource() %></td>
		<td style="font-weight: bold;"><%= e.getStatus() %></td>
		<td><a href="/access/view?id=<%= e.getId() %>">View</a></td>
		<td><a href="/access/edit?id=<%= e.getId() %>">Edit</a></td>
		<td><form name="post_<%= e.getId() %>" style="display:none;" method="post" action="/access/delete"><input type="hidden" name="accessId" value="<%= e.getId() %>"/></form><a href="#" onclick="if (confirm(&quot;Are you sure you want to delete # <%= e.getId() %>?&quot;)) { document.post_<%= e.getId() %>.submit(); } event.returnValue = false; return false;">Delete</a></td>
		</tr>
	<% } %>
</table>
<% } else { %>
<p/><span class="heading">No hay empleados registrados</span>
<% } %>
</body>
</html>