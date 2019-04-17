<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<%@ page import="rutgers.cs336.db.Offer" %>
<%@ page import="static rutgers.cs336.servlet.IConstant.*" %>

<html>

<head>
	<meta charset="utf-8">
	<title>BuyMe - Post an Offer</title>
	<link rel="stylesheet" href="../style.css?v=1.0"/>
</head>

<body>

<%
	String userID = (String) request.getSession().getAttribute("user");
	Map data = Offer.doCreateOrModifyOffer(userID, request.getParameterMap(), true);
	//
	String message = "";
	if ((Boolean) data.get(DATA_NAME_STATUS)) {
		message = "Bid Posted.";
	}
	else {
		message = "Error in Posting Offer: " + data.get(DATA_NAME_MESSAGE);
	}
	;
%>


<h1><%=message%>
</h1>

<%@include file="nav.jsp" %>
<%@include file="../header.jsp" %>

<h3><%=message%>
</h3>

</body>

</html>
