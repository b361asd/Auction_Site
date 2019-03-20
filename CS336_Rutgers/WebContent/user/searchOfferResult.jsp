<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<%@ page import="rutgers.cs336.db.SearchOffer" %>
<%@ page import="java.util.List" %>
<%@ page import="static rutgers.cs336.servlet.IConstant.*" %>

<html>

<head>
	<meta charset="utf-8">
	<title>BuyMe - Search Offers</title>
	<link rel="stylesheet" href="../style.css?v=1.0"/>
</head>

<body>

<%
	Map data = SearchOffer.doSearchOffer(request.getParameterMap());
	//
	request.getSession().setAttribute(SESSION_ATTRIBUTE_DATA_MAP, data);
	//
	List lstHeader = (List) (data.get(DATA_NAME_DATA_ADD));
	List lstRows = (List) (data.get(DATA_NAME_DATA));
%>

<%@include file="../header.jsp" %>
<%@include file="userNav.jsp" %>

<form id="form-id" action="${pageContext.request.contextPath}/user/postOfferResult.jsp" method="post">
	<input id="input-id" type="hidden" name="offerid" value="_"/>
</form>

<table>
	<tr>
		<%
			// Header
			if (lstHeader != null) {
				for (Object one : lstHeader) {
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
				for (Object oneField : lstOneRow) {
					String oneItem = oneField == null ? "" : oneField.toString();
					out.println("<td>" + oneItem + "</td>");
				}
				//
				out.println("<td>" + "<button onclick=\"document.getElementById('input-id').value='" + lstOneRow.get(0) + "'; document.getElementById('form-id').submit();\" class=\"favorite styled\" type=\"button\">Bid</button>" + "</td>");
				//
				out.println("</tr>");
			}
		}
	%>

</table>

</body>

</html>
