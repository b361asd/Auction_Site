<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<%@ page import="static rutgers.cs336.gui.Helper.*" %>

<html>

<head>
	<meta charset="utf-8">
	<title>BuyMe - Search Bids</title>
	<link rel="stylesheet" href="../style.css?v=1.0"/>
</head>

<body>

<%@include file="../header.jsp" %>
<%@include file="nav.jsp" %>

<table>

	<tr>
		<td>Browse</td>
		<td>
			<form action="${pageContext.request.contextPath}/rep/listBid.jsp" method="post">
				<input type="hidden" name="action" value="repBrowseBid"/>
				<input type="submit" value="Browse">
			</form>
		</td>
	</tr>

	<tr>
		<td>Search</td>
		<td>
			<form id="form" action="${pageContext.request.contextPath}/rep/listBid.jsp" method="post">
				<input type="hidden" name="action" value="repSearchBid"/>
				<%
					out.println("<div class='allField'>Buyer");
					out.println(getOPSZSelection("buyerOP"));
					out.println("<input type='text' name='buyerVal'/></div><br/>");
				%>
				<input type="submit" value="Search">
			</form>
		</td>
	</tr>

</table>

</body>

</html>
