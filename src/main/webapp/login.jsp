<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>

<html>

<head>
   <meta charset="utf-8">
   <title>BuyMe - Login</title>
   <link rel="stylesheet" href='./style.css'/>
</head>

<body>

<%@include file="header.jsp" %>

<form action="${pageContext.request.contextPath}/main/webapp/home" method="post">

   <label>
      Please enter your username
      <input type="text" name="username"/>
   </label><br>
   <label>
      Please enter your password
      <input type="password" name="password"/>
   </label>

   <input type="submit" value="Submit">

</form>

<h2><A href="register.jsp">Register</A></h2>


</body>

</html>
