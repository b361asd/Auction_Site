<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>BuyMe - Representative Home</title>
	<link rel="stylesheet" href="style.css?v=1.0"/>

	<%@ page language="java"
				contentType="text/html; charset=windows-1256"
				pageEncoding="windows-1256"
	%>
</head>

<body>
<%
	String message = "Welcome to BuyMe!";
	if (session == null) {
	} else {
		message = (String) session.getAttribute("message");
		if (message == null) {
			message = "Welcome to BuyMe.";
		}
	}
%>
<h1><%=message%>
</h1>

<form action="${pageContext.request.contextPath}/login">


	Please enter 1 your username
	<input type="text" name="username"/><br>

	Please enter 2 your password
	<input type="text" name="password"/>

	<input type="submit" value="submit">

</form>
</body>

</html>