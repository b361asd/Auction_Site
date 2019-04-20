<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<%@ page import="rutgers.cs336.db.Trade" %>
<%@ page import="rutgers.cs336.gui.TableData" %>
<%@ page import="static rutgers.cs336.servlet.IConstant.*" %>
<%@ page import="static rutgers.cs336.db.DBBase.*" %>

<html>

<head>
	<meta charset="utf-8">
	<title>BuyMe - Search Offers</title>
	<link rel="stylesheet" href="../style.css?v=1.0"/>
</head>

<body>

<%
	Map data = null;
	TableData dataTable = null;
	//
	String action = getStringFromParamMap("action", request.getParameterMap());
	if (action.equals("sort")) {
		data = (Map) request.getSession().getAttribute(SESSION_ATTRIBUTE_DATA_MAP);
		//
		dataTable = (TableData) (data.get(DATA_NAME_DATA));
		//
		String sort = getStringFromParamMap("sort", request.getParameterMap());
		dataTable.sortRowPerHeader(sort);
	}
	//
	int lookbackdays = getIntFromParamMap("lookbackdays", request.getParameterMap());
	if (lookbackdays < 1) {
		lookbackdays = 30;
	}
	//
	int limit = getIntFromParamMap("limit", request.getParameterMap());
	if (limit < 1) {
		limit = 10;
	}
	//
	if (data == null) {
		data = Trade.selectBestSellingMostRecentItems(lookbackdays, limit, false);
		request.getSession().setAttribute(SESSION_ATTRIBUTE_DATA_MAP, data);
		//
		dataTable = (TableData) (data.get(DATA_NAME_DATA));
	}
%>

<%@include file="../header.jsp" %>
<%@include file="nav.jsp" %>

<form method="post">
	<%
		out.println("<input type='hidden' name='action' value='buyerSummary'/>");
		//
		out.println("<table>");
		out.println("<tr>");
		out.println("<td>");
		out.println("<div align='left' class='allField'>Lookback Days");
		out.println("<input type='NUMBER' name='lookbackdays'value='" + lookbackdays + "' /></div>");
		out.println("</td>");
		//
		out.println("<td>");
		out.println("<div align='left' class='allField'>Limit");
		out.println("<input type='NUMBER' name='limit'value='" + limit + "' /></div>");
		out.println("</td>");
		//
		out.println("<td>");
		out.println("<input type='submit' value='Submit'>");
		out.println("</td>");
		out.println("</tr");
		out.println("</table>");
	%>
</form>

<table>
	<thead>
	<tr>
		<%
			out.println(dataTable.printDescriptionForTable(false));
		%>
	</tr>
	<tr>
		<%
			out.println(dataTable.printHeaderForTable());
		%>
	</tr>
	</thead>
	<tbody>
	<%
		if (dataTable.rowCount() > 0) {
			for (int i = 0; i < dataTable.rowCount(); i++) {
				out.println("<tr>");
				out.println(dataTable.printOneRowInTable(i));
				out.println("</tr>");
			}
		}
	%>
	</tbody>
</table>

</body>

</html>
