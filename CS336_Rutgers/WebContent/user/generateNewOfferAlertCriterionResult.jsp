<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<%@ page import="rutgers.cs336.db.Offer" %>
<%@ page import="static rutgers.cs336.servlet.IConstant.*" %>

<html>

<head>
	<meta charset="utf-8">
	<title>BuyMe - BuyMe - Generate Alert</title>
	<link rel="stylesheet" href="../style.css?v=1.0"/>
</head>

<body>

<%
	String userID = (String) request.getSession().getAttribute("user");
	Map data = Offer.doGenerateNewOfferAlertCriterion(userID, request.getParameterMap());
	//
	request.getSession().setAttribute(SESSION_ATTRIBUTE_DATA_MAP, data);
%>

<%@include file="../header.jsp" %>
<%@include file="userNav.jsp" %>

</body>

</html>
