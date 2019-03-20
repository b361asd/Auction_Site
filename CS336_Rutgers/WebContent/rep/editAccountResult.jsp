<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<%@ page import="rutgers.cs336.db.AddUser" %>
<%@ page import="static rutgers.cs336.db.DBBase.*" %>

<html>

<head>
	<meta charset="utf-8">
	<title>BuyMe - Remove Bid</title>
	<link rel="stylesheet" href="../style.css?v=1.0"/>
</head>

<body>

<%
	{
		String username  = request.getParameter("username");
		String password  = request.getParameter("password");
		String email     = request.getParameter("email");
		String firstName = request.getParameter("firstName");
		String lastName  = request.getParameter("lastName");
		String street    = request.getParameter("street");
		String city      = request.getParameter("city");
		String state     = request.getParameter("state");
		String zipCode   = request.getParameter("zipCode");
		String phone     = request.getParameter("phoneNumber");
		//
		Map dataMap = AddUser.doAddUser(username, password, email, firstName, lastName, street, city, state, zipCode, phone, 2);
		//
		session.setAttribute(SESSION_ATTRIBUTE_DATA_MAP, dataMap);
	}
%>

<%@include file="../header.jsp" %>
<%@include file="repNav.jsp" %>

</body>

</html>
