<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>

<head>
	<meta charset="utf-8">
	<title>BuyMe - Admin Home</title>
	<link rel="stylesheet" href="style.css?v=1.0"/>
</head>

<body>
<%
	String message = "Welcome to BuyMe!";
	if (session == null) {
	}
	else {
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