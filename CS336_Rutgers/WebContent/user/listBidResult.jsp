<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<%@ page import="rutgers.cs336.db.Bid" %>
<%@ page import="java.util.List" %>
<%@ page import="static rutgers.cs336.servlet.IConstant.*" %>

<html>

<head>
	<meta charset="utf-8">
	<title>BuyMe - List Bids</title>
	<link rel="stylesheet" href="../style.css?v=1.0"/>
</head>

<body>

<%
	String offeridcategoryname = DBBase.getStringFromParamMap("offeridcategoryname", request.getParameterMap());
	//
	String[] temp = offeridcategoryname.split(",");
	//
	Map data = Bid.getBidsForOffer(temp[0]);
	//
	request.getSession().setAttribute(SESSION_ATTRIBUTE_DATA_MAP, data);
	//
	List lstHeader = (List) (data.get(DATA_NAME_DATA_ADD));
	List lstRows = (List) (data.get(DATA_NAME_DATA));
%>

<%@include file="../header.jsp" %>
<%@include file="nav.jsp" %>

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
		if (lstRows != null) {
			for (Object oneRow : lstRows) {
				List lstOneRow = (List) oneRow;
				//
				out.println("<tr>");
				for (int i = 1; i < lstOneRow.size(); i++) {   // Skip offerID
					Object oneField = lstOneRow.get(i);
					//
					String oneItem = oneField == null ? "" : oneField.toString();
					out.println("<td>" + oneItem + "</td>");
				}
				//
				out.println("</tr>");
			}
		}
	%>

</table>

</body>

</html>
