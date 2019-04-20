<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<%@ page import="rutgers.cs336.db.User" %>
<%@ page import="rutgers.cs336.gui.TableData" %>
<%@ page import="static rutgers.cs336.servlet.IConstant.*" %>
<%@ page import="static rutgers.cs336.db.DBBase.*" %>

<html>

<head>
	<meta charset="utf-8">
	<title>BuyMe - Modify Rep</title>
	<link rel="stylesheet" href="../style.css?v=1.0"/>
</head>

<body>

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

<%@include file="../header.jsp" %>
<%@include file="nav.jsp" %>

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

<br/>
<br/>

<form action="${pageContext.request.contextPath}/admin/listUser.jsp" method="post">
	<%
		out.println("<input type='hidden' name='action' value='updateUser'/>");
		out.println("<div><input type='hidden' name='username' value='" + dataTable.getOneCell(0, 0) + "'></div>");
		//
		out.println("<table>");
		out.println("<tr>");
		out.println("<td style='text-align:left'>Password</td>");
		out.println("<td style='text-align:left'>");
		out.println("<input type='text' name='password' value='" + dataTable.getOneCell(0, 1) + "' />");
		out.println("</td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td style='text-align:left'>Email Address</td>");
		out.println("<td style='text-align:left'>");
		out.println("<input type='text' name='email' value='" + dataTable.getOneCell(0, 2) + "' />");
		out.println("</td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td style='text-align:left'>First Name</td>");
		out.println("<td style='text-align:left'>");
		out.println("<input type='text' name='firstname' value='" + dataTable.getOneCell(0, 3) + "' />");
		out.println("</td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td style='text-align:left'>Last Name</td>");
		out.println("<td style='text-align:left'>");
		out.println("<input type='text' name='lastname' value='" + dataTable.getOneCell(0, 4) + "' />");
		out.println("</td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td style='text-align:left'>Address</td>");
		out.println("<td style='text-align:left'>");
		out.println("<input type='text' name='address' value='" + dataTable.getOneCell(0, 5) + "' />");
		out.println("</td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td style='text-align:left'>Phone Number</td>");
		out.println("<td style='text-align:left'>");
		out.println("<input type='text' name='phone' value='" + dataTable.getOneCell(0, 6) + "' />");
		out.println("</td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td style='text-align:left'></td>");
		out.println("<td style='text-align:left'>");
		out.println("<input type='submit' value='Submit'>");
		out.println("</td>");
		out.println("</tr>");
		out.println("</table>");
	%>
</form>

</body>

</html>
