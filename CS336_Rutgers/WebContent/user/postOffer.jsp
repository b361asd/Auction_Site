<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<%@ page import="static rutgers.cs336.db.DBBase.*" %>

<html>

<head>
	<meta charset="utf-8">
	<title>BuyMe - Post an Offer</title>
	<link rel="stylesheet" href="../style.css?v=1.0"/>

	<script type="text/javascript">
       function onCategoryChange(value) {
           var form = document.getElementById('form');
           form.action = "${pageContext.request.contextPath}/user/postOffer.jsp";
           form.submit();
       }
	</script>
</head>

<body>

<%
	String message = "Welcome to BuyMe!";
	if (session == null) {
	}
	else {
		message = (String) session.getAttribute("message");
		if (message == null) {
			message = "Welcome to BuyMe.";
		}
	}
	//
	String categoryNameFromParam = "CAT=" + getStringFromParamMap("categoryName", request.getParameterMap());
	//String paramMap = getParamMap(request.getParameterMap());
	//
	Map data = CategoryAndField.getCategoryField(getStringFromParamMap("categoryName", request.getParameterMap()));
	List lstCategory = (List) data.get(CategoryAndField.DATA_CATEGORY_LIST);
	List lstField = (List) data.get(CategoryAndField.DATA_FIELD_LIST);
	//
	TableData dataTable = null;				// From Modify order
	String offeridcategoryname = null;		// From Modify order
%>

<%@include file="nav.jsp" %>
<%@include file="../header.jsp" %>

<form id="form" action="${pageContext.request.contextPath}/user/postOfferResult.jsp" method="post">

	<%@include file="../createOfferCommon.jsp" %>

	<input type="submit" value="Submit">
</form>

</body>

</html>
