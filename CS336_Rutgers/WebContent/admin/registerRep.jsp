<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<html>

<head>
	<meta charset="utf-8">
	<title>BuyMe - Register a Representative</title>
	<link rel="stylesheet" href="../style.css?v=1.0"/>

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

<form action="${pageContext.request.contextPath}/admin/registerRepResult.jsp" method="post">
	<input name="register" type="hidden" value="YES">
	Username<input type="text" name="username"/><br/>
	Password<input type="text" name="password"/><br/>
	Email Address<input type="text" name="email"/><br/>
	First Name<input type="text" name="firstName"/><br/>
	Last Name<input type="text" name="lastName"/><br/>
	Street<input type="text" name="street"/><br/>
	City<input type="text" name="city"/><br/>
	State<input type="text" name="state"/><br/>
	Zip Code<input type="text" name="zipCode"/><br/>
	Phone Number<input type="text" name="phoneNumber"/><br/>
	<input type="submit" value="submit">
</form>

</body>

</html>
