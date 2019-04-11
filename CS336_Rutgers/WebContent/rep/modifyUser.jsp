<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<%@ page import="rutgers.cs336.db.User" %>
<%@ page import="rutgers.cs336.gui.TableData" %>
<%@ page import="static rutgers.cs336.servlet.IConstant.*" %>
<%@ page import="static rutgers.cs336.db.DBBase.*" %>

<html>

<head>
	<meta charset="utf-8">
	<title>BuyMe - Modify User</title>
	<link type="text/css" rel="stylesheet" href="../style.css?v=1.0"/>

	<script type="text/javascript">
       function onClickHeader(value) {
           document.getElementById('input-sort').value = value;
           document.getElementById('form-sort').submit();
       }
	</script>
</head>

<body>

<%@include file="../header.jsp" %>
<%@include file="nav.jsp" %>

<%
	Map data = null;
	TableData dataTable = null;
	//
	String _userType = (String) session.getAttribute(SESSION_ATTRIBUTE_USERTYPE);
	int targetUsrType = 3;
	if (_userType.equalsIgnoreCase("1")) {
		targetUsrType = 2;
	}
	//
	String action = getStringFromParamMap("action", request.getParameterMap());
	if (action.equals("updateUser")) {
		data = User.selectUser(request.getParameterMap(), targetUsrType);
		request.getSession().setAttribute(SESSION_ATTRIBUTE_DATA_MAP, data);
	}
	//
	dataTable = (TableData) (data.get(DATA_NAME_DATA));
%>

<table>
	<thead>
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
				out.println(dataTable.printRowStart(i));
				//
				out.println(dataTable.printOneRowInTable(i));
				//
				out.println("</tr>");
			}
		}
	%>
	</tbody>
</table>

<form action="${pageContext.request.contextPath}/rep/listUser.jsp" method="post">
	<input type="hidden" name="action" value="updateUser"/>
	<%
		out.println("<div><input type='hidden' name='username' value='" + dataTable.getOneCell(0, 0) + "'></div>");
		//
		out.println("<div><input type='text' name='password' value='" + dataTable.getOneCell(0, 1) + "'></div>");
		out.println("<div><input type='text' name='email' value='" + dataTable.getOneCell(0, 2) + "'></div>");
		out.println("<div><input type='text' name='firstname' value='" + dataTable.getOneCell(0, 3) + "'></div>");
		out.println("<div><input type='text' name='lastname' value='" + dataTable.getOneCell(0, 4) + "'></div>");
		out.println("<div><input type='text' name='address' value='" + dataTable.getOneCell(0, 5) + "'></div>");
		out.println("<div><input type='text' name='phone' value='" + dataTable.getOneCell(0, 6) + "'></div>");
	%>

	<input type="submit" value="Submit">
</form>

</body>

</html>
