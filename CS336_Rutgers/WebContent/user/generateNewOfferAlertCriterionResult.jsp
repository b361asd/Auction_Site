<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<%@ page import="rutgers.cs336.db.SearchOffer" %>
<%@ page import="static rutgers.cs336.servlet.IConstant.*" %>

<html>

<head>
	<meta charset="utf-8">
	<title>BuyMe - Generate Alert</title>
	<link rel="stylesheet" href="../style.css?v=1.0"/>
</head>

<body>

<%
	String userID = (String) request.getSession().getAttribute("user");
	Map data = SearchOffer.doGenerateNewOfferAlertCriterion(userID, request.getParameterMap());
	//
	request.getSession().setAttribute(SESSION_ATTRIBUTE_DATA_MAP, data);
%>

<%@include file="../header.jsp" %>
<%@include file="userNav.jsp" %>

</body>

</html>
