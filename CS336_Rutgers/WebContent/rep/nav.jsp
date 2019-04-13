<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<html>
<head>
	<meta charset="utf-8">
	<title>BuyMe - Representative Navigation</title>
	<link type="text/css" rel="stylesheet" href="../style.css?v=1.0"/>
</head>
<body>

<h4>
	<A href="${pageContext.request.contextPath}/rep/repSearchOffer.jsp">Manager Offers</A>
	<A href="${pageContext.request.contextPath}/rep/repSearchBid.jsp">Manage Bids</A>
	<A href="${pageContext.request.contextPath}/rep/answerQuestion.jsp">Answer Questions</A>
	<A href="${pageContext.request.contextPath}/rep/listUser.jsp">Manage Users</A>
	<A href="${pageContext.request.contextPath}/logout">Logout</A>
</h4>

</body>
</html>
