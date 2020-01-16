<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<%@ page import="b361asd.auction.db.User" %>
<%@ page import="b361asd.auction.gui.Helper" %>
<%@ page import="static b361asd.auction.servlet.IConstant.*" %>
<%@ page import="b361asd.auction.gui.TableData" %>
<%@ page import="static b361asd.auction.db.DBBase.*" %>

<html>

<head>
	<meta charset="utf-8">
	<title>BuyMe - Search Offers</title>
	<link rel="stylesheet" href='${pageContext.request.contextPath}/style.css'/>
</head>

<body>

<%@include file="../header.jsp" %>
<%@include file="nav.jsp" %>

<form id="form-activateUser" method="post">
	<input type="hidden" name="action" value="activateUser"/>
	<input id="input-activateUser" type="hidden" name="username" value="_"/>
</form>

<form id="form-deactivateUser" method="post">
	<input type="hidden" name="action" value="deactivateUser"/>
	<input id="input-deactivateUser" type="hidden" name="username" value="_"/>
</form>

<form id="form-updateUser" action="${pageContext.request.contextPath}/admin/modifyUser.jsp" method="post">
	<input type="hidden" name="action" value="updateUser"/>
	<input id="input-updateUser" type="hidden" name="username" value="_"/>
</form>

<A href="${pageContext.request.contextPath}/admin/registerRep.jsp">Register a Representative</A>

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
		if (action.equalsIgnoreCase("activateUser")) {
			Map dataActivateUser = User.activateUser(request.getParameterMap(), true);
			//request.getSession().setAttribute(SESSION_ATTRIBUTE_DATA_MAP, data);
		}
		else if (action.equalsIgnoreCase("deactivateUser")) {
			Map dataDeactivateUser = User.activateUser(request.getParameterMap(), false);
		}
		else if (action.equalsIgnoreCase("updateUser")) {
			Map dataupdateUser = User.modifyUser(request.getParameterMap(), targetUsrType);
		}
	}
	//
	if (data == null) {
		data = User.selectUser(null, targetUsrType);
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
				Object  active  = dataTable.getOneCell(i, 7);
				boolean bActive = (Boolean) active;
				//
				out.println("<td>");
				if (bActive) {
					out.println(Helper.getButton("form-deactivateUser", "input-deactivateUser", "" + dataTable.getOneCell(i, 0), "Disable"));
				}
				else {
					out.println(Helper.getButton("form-activateUser", "input-activateUser", "" + dataTable.getOneCell(i, 0), "Enable"));
				}
				out.println(Helper.getButton("form-updateUser", "input-updateUser", "" + dataTable.getOneCell(i, 0), "Update"));
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
