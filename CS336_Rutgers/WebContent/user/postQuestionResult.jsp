<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<%@ page import="rutgers.cs336.db.CreateOffer" %>
<%@ page import="java.util.Map" %>
<%@ page import="static rutgers.cs336.servlet.IConstant.*" %>

<html>

<head>
	<meta charset="utf-8">
	<title>BuyMe - Post a Question</title>
	<link rel="stylesheet" href="../style.css?v=1.0"/>
</head>

<body>

<%
	String userID = (String) request.getSession().getAttribute("user");
	Map data = CreateOffer.doCreateOffer(userID, request.getParameterMap());
	//
	String status = data.get(DATA_NAME_STATUS).toString();
	String message = (String) data.get(DATA_NAME_MESSAGE);
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
<h1><%=status%>
</h1>
<h1><%=message%>
</h1>

<%@include file="userNav.jsp" %>

</body>

</html>
