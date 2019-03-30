<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<html>

<head>
	<meta charset="utf-8">
	<title>BuyMe - Login</title>
	<link rel="stylesheet" href="style.css?v=1.0"/>
</head>

<body>

<%@include file="header.jsp" %>

<form action="${pageContext.request.contextPath}/home" method="post">

	Please enter your username<input type="text" name="username"/><br>
	Please enter your password<input type="password" name="password"/>

	<input type="submit" value="Submit">

</form>

<h2><A href="${pageContext.request.contextPath}/register.jsp">Register</A></h2>


</body>

</html>
