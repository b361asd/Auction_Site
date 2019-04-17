<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<html>

<head>
	<meta charset="utf-8">
	<title>BuyMe - Search Offers</title>
	<link rel="stylesheet" href="../style.css?v=1.0"/>
</head>

<body>

<%@include file="../header.jsp" %>
<%@include file="nav.jsp" %>

<form id="form-id-cancelOffer" action="${pageContext.request.contextPath}/rep/cancelOffer.jsp" method="post">
	<input id="input-id-cancelOffer" type="hidden" name="offerid" value="_"/>
</form>

<form id="form-id-modifyOffer" action="${pageContext.request.contextPath}/rep/modifyOffer.jsp" method="post">
	<input type="hidden" name="action" value="startModifyOffer"/>
	<input id="input-id-modifyOffer" type="hidden" name="offeridcategoryname" value="_"/>
</form>

<form id="form-sort" target="_self" method="post">
	<input id="input-sort" type="hidden" name="sort" value="_"/>

	<%@include file="../listOfferCommon.jsp" %>

</form>

</body>

</html>
