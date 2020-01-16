<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>

<%@ page import="static b361asd.auction.db.DBBase.*" %>

<html>

<head>
	<meta charset="utf-8">
	<title>BuyMe - Search Offers</title>
	<link rel="stylesheet" href='../style.css'/>

	<script type="text/javascript">
       function onCategoryChange() {
           const form = document.getElementById('form');
           form.action = "${pageContext.request.contextPath}/user/searchOffer.jsp";
           form.submit();
       }
	</script>
</head>

<body>

<%
	Map data = null;
	data = CategoryAndField.getCategoryField(getListOfStringsFromParamMap("categoryName", 1, request.getParameterMap(), ""));
%>

<%@include file="../header.jsp" %>
<%@include file="nav.jsp" %>

<form action="${pageContext.request.contextPath}/user/listOffer.jsp" method="post">
	<input type="hidden" name="action" value="browseOffer"/>
	<input type="submit" value="Browse All Open Offer">
</form>

<br/>
<br/>


<form id="form" action="${pageContext.request.contextPath}/user/listOffer.jsp" method="post">
	<%@include file="../searchOfferCommon.jsp" %>
	<input type="hidden" name="action" value="searchOffer"/>
	<input type="submit" value="Search">
</form>

</body>

</html>
