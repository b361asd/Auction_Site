<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<%@ page import="rutgers.cs336.db.Alert" %>
<%@ page import="rutgers.cs336.gui.Helper" %>
<%@ page import="static rutgers.cs336.servlet.IConstant.*" %>
<%@ page import="rutgers.cs336.gui.TableData" %>
<%@ page import="static rutgers.cs336.db.DBBase.*" %>

<html>

<head>
	<meta charset="utf-8">
	<title>BuyMe - View Alerts</title>
	<link type="text/css" rel="stylesheet" href="../style.css?v=1.0"/>
</head>

<body>

<%@include file="../header.jsp" %>
<%@include file="nav.jsp" %>

<form id="form-deleteAlert" method="post">
	<input type="hidden" name="action" value="deleteAlert"/>
	<input id="input-deleteAlert" type="hidden" name="alertID" value="_"/>
</form>

<form id="form-viewAlertDetail" action="${pageContext.request.contextPath}/user/viewAlertDetail.jsp" method="post">
	<input type="hidden" name="action" value="viewAlertDetail"/>
	<input id="input-viewAlertDetail" type="hidden" name="username" value="_"/>
</form>

<%
	Map data = null;
	TableData dataTable = null;
	//
	String userID = (String) request.getSession().getAttribute("user");
	//
	String action = getStringFromParamMap("action", request.getParameterMap());
	if (action.equals("sort")) {
		data = (Map) request.getSession().getAttribute(SESSION_ATTRIBUTE_DATA_MAP);
		if (data != null) {
			dataTable = (TableData) (data.get(DATA_NAME_DATA));
			//
			if (dataTable != null) {
				String sort = getStringFromParamMap("sort", request.getParameterMap());
				dataTable.sortRowPerHeader(sort);
			}
			else {
				data = null;
			}
		}
	}
	else {
		if (action.equals("deleteAlert")) {
			Map dataActivateUser = Alert.deleteAlert(request.getParameterMap());
			//request.getSession().setAttribute(SESSION_ATTRIBUTE_DATA_MAP, data);
		}
	}
	//
	if (data == null) {
		data = Alert.selectAlert(userID);
		request.getSession().setAttribute(SESSION_ATTRIBUTE_DATA_MAP, data);
	}
	//
	dataTable = (TableData) (data.get(DATA_NAME_DATA));
%>

<table>
	<thead>
	<tr>
		<td>Action</td>
		<%
			out.println(dataTable.printHeaderForTable());
		%>
	</tr>
	</thead>
	<tbody>
	<%
		if (dataTable.rowCount() > 0) {
			for (int i = 0; i < dataTable.rowCount(); i++) {
				out.println(dataTable.printRowStart(i));
				//
				out.println("<td>");
				out.println(Helper.getButton("form-deleteAlert", "input-deleteAlert", "" + dataTable.getOneCell(i, 0), "Dismiss"));
				out.println(Helper.getButton("form-viewAlertDetail", "input-viewAlertDetail", "" + dataTable.getOneCell(i, 1), "Detail"));
				out.println("</td>");
				//
				out.println(dataTable.printOneRowInTable(i));
				//
				out.println("</tr>");
			}
		}
	%>
	</tbody>
</table>

</body>
</html>
