<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<%@ page import="rutgers.cs336.db.SearchOffer" %>
<%@ page import="java.util.List" %>
<%@ page import="static rutgers.cs336.servlet.IConstant.*" %>
<%@ page import="java.util.Map" %>

<html>

<head>
	<meta charset="utf-8">
	<title>BuyMe - Search Offers</title>
	<link rel="stylesheet" href="../style.css?v=1.0"/>

</head>

<body>

<%
	String userID = (String) request.getSession().getAttribute("user");
	Map data = SearchOffer.doSearchOffer(request.getParameterMap());
	//
	String status = data.get(DATA_NAME_STATUS).toString();
	String message = (String) data.get(DATA_NAME_MESSAGE);
	//
	String sessionMessage = "Welcome to BuyMe!";
	if (request.getSession() == null) {
		sessionMessage = "Welcome to BuyMe no session!";
	}
	else {
		sessionMessage = (String) request.getSession().getAttribute("message");
		if (sessionMessage == null) {
			sessionMessage = "Welcome to BuyMe.";
		}
	}

%>
<h1><%=sessionMessage%>
</h1>
<h1><%=status%>
</h1>
<h1><%=message%>
</h1>


<table>
	<tr>
		<td>Category</td>
		<td>Seller</td>
		<td>Reserved Price</td>
		<td>Start Date</td>
		<td>End Date</td>
		<td>Description</td>
		<td>Action</td>
	</tr>

	<form id="form-id" action="${pageContext.request.contextPath}/user/postBid.jspd.jsp" method="post">
		<input id="input-id" type="hidden" name="offerid" value="_"/>
	</form>

	<%
		List<SearchOffer.OfferItem> lstOffer = (List<SearchOffer.OfferItem>) data.get(DATA_NAME_DATA);
		for (SearchOffer.OfferItem one : lstOffer) {
			out.println("<tr><td>" + one.getCategoryName() + "</td>");
			out.println("<td>" + one.getSeller() + "</td>");
			out.println("<td>" + one.getMin_price() + "</td>");
			out.println("<td>" + one.getStartDate() + "</td>");
			out.println("<td>" + one.getEndDate() + "</td>");
			out.println("<td>" + one.getDetails() + "</td></tr>");
			out.println(
					  "<td>" + "<button onclick=\"document.getElementById('input-id').value='" + one.getOfferId() + "'; document.getElementById('form-id').submit();\" class=\"favorite styled\" type=\"button\">Bid</button>" + "</td></tr>");
		}
	%>

</table>


<%@include file="userNav.jsp" %>

</body>

</html>
