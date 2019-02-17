<%@ page import="rutgers.cs336.db.CreateOffer" %>
<%@ page import="java.util.Map" %>
<%@ page import="static rutgers.cs336.db.IDaoConstant.DATA_NAME_MESSAGE" %>
<%@ page import="static rutgers.cs336.db.IDaoConstant.DATA_NAME_STATUS" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>BuyMe - Your Offers</title>
	<link rel="stylesheet" href="style.css?v=1.0"/>

	<%@ page language="java"
				contentType="text/html; charset=windows-1256"
				pageEncoding="windows-1256"
	%>
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
