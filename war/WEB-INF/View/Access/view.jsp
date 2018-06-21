<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="model.*"%>
<%
	Access access = (Access)request.getAttribute("access");
	String role = (String)request.getAttribute("role");
	String resource = (String)request.getAttribute("resource");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<span class="nav"><a href="/access">Back</a></span><p/>
<table border="0" cellspacing="1" cellpadding="5" bgcolor="#CCCCCC">
<tr bgcolor="#407BA8">
<td style="color: #ffffff; font-weight: bold;">ID</td>
<td bgcolor="#ffffff"><%= access.getId() %></td>
</tr>
<tr bgcolor="#407BA8">
<td style="color: #ffffff; font-weight: bold;">Role</td>
<td bgcolor="#ffffff"><%= role %></td>
</tr>
<tr bgcolor="#407BA8">
<td style="color: #ffffff; font-weight: bold;">Resource</td>
<td bgcolor="#ffffff"><%= resource %></td>
</tr>
</table>
</body>
</html>