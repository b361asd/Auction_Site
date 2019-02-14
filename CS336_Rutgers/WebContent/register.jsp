<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>BuyMe - Register</title>
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
    }
    else {
        message = (String) session.getAttribute("message");
        if (message == null) {
            message = "Welcome to BuyMe.";
        }
    }
%>
<h1><%=message%></h1>

<form action="${pageContext.request.contextPath}/register" method="post">
    Username<input type="text" name="username"/><br>
    Password<input type="text" name="password"/><br>
    Email Address<input type="text" name="email"/><br>
    First Name<input type="text" name="firstName"/><br>
    Last Name<input type="text" name="lastName"/><br>
    Street<input type="text" name="street"/><br>
    City<input type="text" name="city"/><br>
    State<input type="text" name="state"/><br>
    Zip Code<input type="text" name="zipCode"/><br>
    Phone Number<input type="text" name="phoneNumber"/><br>
    <input type="submit" value="submit">
</form>

<h2><A href="${pageContext.request.contextPath}/register.jsp">Register</A></h2>


</body>

</html>
