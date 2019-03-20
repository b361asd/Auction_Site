<%@page import="rutgers.cs336.db.SearchOffer"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<%@ page import="java.util.*" %>
<%@ page import="static rutgers.cs336.db.DBBase.*" %>
<%@ page import="rutgers.cs336.gui.Helper" %>

<html>

<head>
	<meta charset="utf-8">
	<title>BuyMe - Post a Bid</title>
	<link rel="stylesheet" href="../style.css?v=1.0"/>
</head>

<body>

<%
	Map data = SearchOffer.doSearchOfferByID(request.getParameterMap());
	//
	request.getSession().setAttribute(SESSION_ATTRIBUTE_DATA_MAP, data);
	//
	List lstHeader = (List) (data.get(DATA_NAME_DATA_ADD));
	List lstRows = (List) (data.get(DATA_NAME_DATA));
%>


<%@include file="../header.jsp" %>
<%@include file="userNav.jsp" %>

<table>
	<tr>
		<%
			// Header
			if (lstHeader != null && lstHeader.size() > 0) {
				for (int i = 1; i < lstHeader.size(); i++) {
					Object one = lstHeader.get(i);
					out.println("<td>" + one.toString() + "</td>");
				}
			}
		%>
	</tr>

	<%
		String offerID = "";
		if (lstRows != null) {
			for (Object oneRow : lstRows) {
				List lstOneRow = (List) oneRow;
				//
				offerID = (String) lstOneRow.get(0);
				//
				out.println("<tr>");
				for (int i = 1; i < lstOneRow.size(); i++) {	// Skip offerID
					Object oneField = lstOneRow.get(i);
					//
					String oneItem = oneField == null ? "" : oneField.toString();
					if (i == 3) {
						out.println("<td>" + Helper.getConditionFromCode(oneItem) + "</td>");
					}
					else {
						out.println("<td>" + oneItem + "</td>");
					}
				}
				//
				out.println("</tr>");
			}
		}
	%>

</table>


<form action="${pageContext.request.contextPath}/user/postBidResult.jsp" method="post">

	<%
		out.println("<input type='hidden' name='offerId' value='" + offerID + "'/>");
	%>

	<div>Price<input type="number" name="price" min="1"></div>
	<div>Auto Rebid Limit<input type="number" name="autoRebidLimit" min="1"></div>
	<input type="submit" value="Submit">
</form>

</body>

</html>
