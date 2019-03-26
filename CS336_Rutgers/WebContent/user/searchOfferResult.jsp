<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<%@ page import="rutgers.cs336.db.Offer" %>
<%@ page import="java.util.List" %>
<%@ page import="static rutgers.cs336.servlet.IConstant.*" %>
<%@ page import="rutgers.cs336.gui.Helper" %>

<html>

<head>
	<meta charset="utf-8">
	<title>BuyMe - Search Offers</title>
	<link rel="stylesheet" href="../style.css?v=1.0"/>
</head>

<body>

<%
	Map data = Offer.doSearchOffer(request.getParameterMap());
	//
	request.getSession().setAttribute(SESSION_ATTRIBUTE_DATA_MAP, data);
	//
	List lstHeader = (List) (data.get(DATA_NAME_DATA_ADD));
	List lstRows = (List) (data.get(DATA_NAME_DATA));
%>

<%@include file="../header.jsp" %>
<%@include file="userNav.jsp" %>

<form id="form-id-doBid" action="${pageContext.request.contextPath}/user/postBid.jsp" method="post">
	<input id="input-id-doBid" type="hidden" name="offeridcategoryname" value="_"/>
</form>

<form id="form-id-listBid" action="${pageContext.request.contextPath}/user/listBidResult.jsp" method="post">
	<input id="input-id-listBid" type="hidden" name="offeridcategoryname" value="_"/>
</form>

<form id="form-id-listSimilar" action="${pageContext.request.contextPath}/user/listSimilar.jsp" method="post">
	<input id="input-id-listSimilar" type="hidden" name="offeridcategorynameconditioncode" value="_"/>
</form>

<table>
	<tr>
		<td>Action</td>
		<%
			// Header
			out.println(Helper.printOneRowInTable(lstHeader, 1));
		%>
	</tr>

	<%
		if (lstRows != null) {
			for (Object oneRow : lstRows) {
				List lstOneRow = (List) oneRow;
				//
				out.println("<tr>");
				//
				out.println("<td>");
				out.println("<button onclick=\"document.getElementById('input-id-doBid').value='" + lstOneRow.get(0) + "," + lstOneRow.get(1) + "'; document.getElementById('form-id-doBid').submit();\" class=\"favorite styled\" type=\"button\">Bid</button>");
				out.println("<button onclick=\"document.getElementById('input-id-listBid').value='" + lstOneRow.get(0) + "," + lstOneRow.get(1) + "'; document.getElementById('form-id-listBid').submit();\" class=\"favorite styled\" type=\"button\">List Bid</button>");
				out.println("<button onclick=\"document.getElementById('input-id-listSimilar').value='" + lstOneRow.get(0) + "," + lstOneRow.get(1) + "," + lstOneRow.get(3) + "'; document.getElementById('form-id-listSimilar').submit();\" class=\"favorite styled\" type=\"button\">List Similar</button>");
				out.println("</td>");
				//
				out.println(Helper.printOneRowInTable(lstOneRow, 1));
				//
				out.println("</tr>");
			}
		}
	%>
</table>

</body>

</html>
