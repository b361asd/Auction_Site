<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<%@ page import="static rutgers.cs336.db.DBBase.*" %>
<%@ page import="static rutgers.cs336.gui.Helper.*" %>

<html>

<head>
	<meta charset="utf-8">
	<title>BuyMe - Search Offers</title>
	<link rel="stylesheet" href="../style.css?v=1.0"/>

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
	{
		//String paramMap = getParamMap(request.getParameterMap());
		//
		Map data = CategoryAndField.getCategoryField(getListOfStringsFromParamMap("categoryName", 1, request.getParameterMap(), ""));
		request.setAttribute("TEMP", data);
	}
%>

<%@include file="../header.jsp" %>
<%@include file="nav.jsp" %>

<form action="${pageContext.request.contextPath}/user/listOffer.jsp" method="post">
	<input type="hidden" name="action" value="browseOffer"/>
	<input type="submit" value="Browse">
</form>

<form id="form" action="${pageContext.request.contextPath}/user/listOffer.jsp" method="post">

	<%@include file="../searchOfferCommon.jsp" %>

	<input type="hidden" name="action" value="searchOffer"/>
	<input type="submit" value="Search">
</form>

</body>

</html>
