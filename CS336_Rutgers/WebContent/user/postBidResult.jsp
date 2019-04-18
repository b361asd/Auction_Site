<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<%@ page import="rutgers.cs336.db.Bid" %>
<%@ page import="static rutgers.cs336.servlet.IConstant.*" %>

<html>

<head>
	<meta charset="utf-8">
	<title>BuyMe - Post a Bid</title>
	<link rel="stylesheet" href="../style.css?v=1.0"/>
</head>

<body>

<%
	String userID = (String) request.getSession().getAttribute("user");
	Map data = Bid.doCreateOrModifyBid(userID, request.getParameterMap(), true);
	//
	String message = "";
	if ((Boolean) data.get(DATA_NAME_STATUS)) {
		message = "Bid Posted.";
	}
	else {
		message = "Error in Posting Bid: " + data.get(DATA_NAME_MESSAGE);
	}
	;
%>

<%@include file="../header.jsp" %>
<%@include file="nav.jsp" %>

<h3><%=message%>
</h3>

</body>

</html>
