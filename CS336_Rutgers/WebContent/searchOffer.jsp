<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<html>

<head>
	<meta charset="utf-8">
	<title>BuyMe - Search Offers</title>
	<link rel="stylesheet" href="style.css?v=1.0"/>
</head>

<body>

<%
	String userID = (String) request.getSession().getAttribute("user");
	//	Map data = CreateOffer.doCreateOffer(userID, request.getParameterMap());
	//
	//	String status = data.get(DATA_NAME_STATUS).toString();
	//	String message = (String) data.get(DATA_NAME_MESSAGE);
	//
	String sessionMessage = "Welcome to BuyMe!";
	if (request.getSession() == null) {
		sessionMessage = "Welcome to BuyMe no session!";
	}
	else {
		sessionMessage = (String) request.getSession().getAttribute("message");
		if (sessionMessage == null) {
			sessionMessage = "Welcome to BuyMe.";
		}
	}
%>
<h1><%=sessionMessage%>
</h1>

<form action="${pageContext.request.contextPath}/searchOfferResult.jsp" method="post">
	Please enter your criteria<input type="text" name="criteria"/>
	<input type="submit" value="Submit">
</form>


<%@include file="userNav.jsp" %>

</body>

</html>
