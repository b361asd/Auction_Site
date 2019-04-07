<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<%@ page import="rutgers.cs336.db.Offer" %>
<%@ page import="rutgers.cs336.gui.Helper" %>
<%@ page import="static rutgers.cs336.servlet.IConstant.*" %>
<%@ page import="java.util.List" %>

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
<%@include file="nav.jsp" %>

<form id="form-id-doBid" action="${pageContext.request.contextPath}/user/postBid.jsp" method="post">
	<input id="input-id" type="hidden" name="offerid" value="_"/>
</form>

<form id="form-id-listBid" action="${pageContext.request.contextPath}/user/listBid.jsp" method="post">
	<input id="input-id" type="hidden" name="offerid" value="_"/>
</form>

<form id="form-id-listSimilar" action="${pageContext.request.contextPath}/user/listSimilar.jsp" method="post">
	<input id="input-id" type="hidden" name="offerid" value="_"/>
</form>

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
		<td>Action</td>
	</tr>

	<%
		if (lstRows != null) {
			for (Object oneRow : lstRows) {
				List lstOneRow = (List) oneRow;
				//
				out.println("<tr>");
				for (int i = 1; i < lstOneRow.size(); i++) {   // Skip offerID
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
				out.println("<td>");
				out.println("<button onclick=\"document.getElementById('input-id').value='" + lstOneRow.get(0) + "'; document.getElementById('form-id').submit();\" class=\"favorite styled\" type=\"button\">Bid</button>");
				out.println("<button onclick=\"document.getElementById('input-id').value='" + lstOneRow.get(0) + "'; document.getElementById('form-id').submit();\" class=\"favorite styled\" type=\"button\">Bid</button>");
				out.println("<button onclick=\"document.getElementById('input-id').value='" + lstOneRow.get(0) + "'; document.getElementById('form-id').submit();\" class=\"favorite styled\" type=\"button\">Bid</button>");
				out.println("</td>");
				//
				out.println("</tr>");
			}
		}
	%>

</table>

</body>

</html>
