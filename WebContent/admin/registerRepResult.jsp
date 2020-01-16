<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<%@ page import="b361asd.auction.db.User" %>
<%@ page import="static b361asd.auction.db.DBBase.*" %>

<html>

<head>
	<meta charset="utf-8">
	<title>BuyMe - Register a Representative</title>
	<link rel="stylesheet" href='${pageContext.request.contextPath}/style.css'/>
</head>

<body>

<%
	String username = request.getParameter("username");
	String password = request.getParameter("password");
	String email = request.getParameter("email");
	String firstName = request.getParameter("firstName");
	String lastName = request.getParameter("lastName");
	String street = request.getParameter("street");
	String city = request.getParameter("city");
	String state = request.getParameter("state");
	String zipCode = request.getParameter("zipCode");
	String phone = request.getParameter("phoneNumber");
	//
	Map data = User.doAddUser(username, password, email, firstName, lastName, street, city, state, zipCode, phone, 2);
	//
	session.setAttribute(SESSION_ATTRIBUTE_DATA_MAP, data);
	//
	String message = "";
	if ((Boolean) data.get(DATA_NAME_STATUS)) {
		message = "Rep registered.";
	}
	else {
		message = "Error in register rep: " + data.get(DATA_NAME_MESSAGE);
	}
	;
%>

<%@include file="../header.jsp" %>
<%@include file="nav.jsp" %>

<h3><%=message%>
</h3>

</body>

</html>
