<%@ page import="rutgers.cs336.db.GetOffer" %>
<%@ page import="java.util.Map" %>
<%@ page import="static rutgers.cs336.db.CreateOffer.PREFIX_CATEGORY_NAME" %>
<%@ page import="static rutgers.cs336.db.DBBase.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>BuyMe - Post a Bid</title>
	<link rel="stylesheet" href="style.css?v=1.0"/>

	<script type="text/javascript">
       function onCategoryChange(value) {
           value.action = "${pageContext.request.contextPath}/postOffer.jsp";
           value.submit();
       }
	</script>

	<%@ page language="java"
				contentType="text/html; charset=windows-1256"
				pageEncoding="windows-1256"
	%>
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
	//String offerID = getStringFromParamMap("offerID", request.getParameterMap());
	String offerID = "5642babcb54a4872b15353bed3712824";
	Map data = GetOffer.getOffer(offerID);
%>


<%
	for (Object one : data.entrySet()) {
		Map.Entry item  = (Map.Entry) one;
		String    key   = (String) item.getKey();
		String    value = (String) item.getValue();
		out.println("<h1>" + key + "=" + value + "</h1>");
	}
%>


<form action="${pageContext.request.contextPath}/listOffer.jsp" method="post">


	<div>Auction Days<input type="number" name="auction_days" min="1" max="30"></div>
	<div>Reserved Price<input type="number" name="reserved_price" min="0.01"></div>
	<div>Item Description<input type="text" name="description"></div>

	<input type="submit" value="submit">
</form>

<%@include file="userNav.jsp" %>

</body>

</html>
