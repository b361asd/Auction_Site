<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<%@ page import="java.util.Map" %>
<%@ page import="static rutgers.cs336.db.DBBase.*" %>

<html>

<head>
	<meta charset="utf-8">
	<title>BuyMe - Set an Alert</title>
	<link rel="stylesheet" href="../style.css?v=1.0"/>

	<script type="text/javascript">
       function onCategoryChange(value) {
           value.action = "${pageContext.request.contextPath}/searchOffer.jsp";
           value.submit();
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
	String offerID = getStringFromParamMap("offerid", request.getParameterMap());
	Map data = null;
%>


<%
	for (Object one : data.entrySet()) {
		Map.Entry item  = (Map.Entry) one;
		String    key   = (String) item.getKey();
		String    value = (String) item.getValue();
		out.println("<h1>" + key + "=" + value + "</h1>");
	}
%>


<form action="${pageContext.request.contextPath}/user/setAlertResult.jsp" method="post">

	<%
		out.println("<input type=\"hidden\" name=\"offerId\" value=\"" + offerID + "\"/>");
	%>

	<div>Price<input type="number" name="price" min="0.00"></div>
	<div>Auto Rebid<input type="checkbox" name="isAutoRebid"></div>
	<div>Auto Rebid Limit<input type="number" name="autoRebidLimit" min="0.00"></div>
	<div>Auto Rebid Increment<input type="number" name="autoRebidIncrement" min="0.01"></div>

	<input type="submit" value="Submit">
</form>

<%@include file="userNav.jsp" %>

</body>

</html>
