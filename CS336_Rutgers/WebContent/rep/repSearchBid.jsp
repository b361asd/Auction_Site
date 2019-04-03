<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<%@ page import="rutgers.cs336.db.CategoryAndField" %>
<%@ page import="java.util.List" %>
<%@ page import="static rutgers.cs336.db.DBBase.*" %>
<%@ page import="static rutgers.cs336.gui.Helper.*" %>

<html>

<head>
	<meta charset="utf-8">
	<title>BuyMe - Search Offers</title>
	<link rel="stylesheet" href="../style.css?v=1.0"/>

	<script type="text/javascript">
       function onCategoryChange() {
           var form = document.getElementById('form');
           form.action = "${pageContext.request.contextPath}/user/searchOffer.jsp";
           form.submit();
       }
	</script>
</head>

<body>

<%
	{
		String paramMap = getParamMap(request.getParameterMap());
		//
		Map data = CategoryAndField.getCategoryField(getStringsFromParamMap("categoryName", 1, request.getParameterMap(), ""));
		request.setAttribute("TEMP",data);
	}
%>

<%@include file="../header.jsp" %>
<%@include file="repNav.jsp" %>

<form action="${pageContext.request.contextPath}/rep/listBid.jsp" method="post">
	<input type="hidden" name="action" value="browseBid"/>
	<input type="submit" value="Browse">
</form>

<form id="form" action="${pageContext.request.contextPath}/rep/listBid.jsp" method="post">

	<%
		out.println("<div class='allField'>Buyer");
		out.println(getOPSZSelection("buyerOP"));
		out.println("<input type='text' name='buyerVal'/></div><br/>");
	%>

	<input type="hidden" name="action" value="searchBid"/>
	<input type="submit" value="Search">
</form>

</body>

</html>
