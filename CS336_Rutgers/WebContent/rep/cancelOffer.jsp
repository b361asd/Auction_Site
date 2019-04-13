<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<%@ page import="rutgers.cs336.db.Offer" %>
<%@ page import="static rutgers.cs336.servlet.IConstant.*" %>

<html>

<head>
	<meta charset="utf-8">
	<title>BuyMe - Cancel Offer</title>
	<link type="text/css" rel="stylesheet" href="../style.css?v=1.0"/>
</head>

<body>

<%
	Map data = Offer.doCancelOffer(request.getParameterMap());
	//
	request.getSession().setAttribute(SESSION_ATTRIBUTE_DATA_MAP, data);
%>


<%@include file="../header.jsp" %>
<%@include file="nav.jsp" %>

<form id="form-sort" target="_self" method="post">
	<input type="submit" value="Go Back">
</form>

</body>

</html>
