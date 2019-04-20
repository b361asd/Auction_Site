<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<%@ page import="rutgers.cs336.db.Bid" %>
<%@ page import="rutgers.cs336.db.User" %>
<%@ page import="java.util.List" %>
<%@ page import="static rutgers.cs336.servlet.IConstant.*" %>
<%@ page import="static rutgers.cs336.db.DBBase.*" %>

<html>

<head>
	<meta charset="utf-8">
	<title>BuyMe - Search Offers</title>
	<link rel="stylesheet" href="../style.css?v=1.0"/>

	<script type="text/javascript">
       function onSelectChange() {
           const form = document.getElementById('form-getActivity');
           form.submit();
       }
	</script>
</head>


<body>

<%
	List lstUser = User.getUserList();
	//
	String userID = (String) request.getSession().getAttribute("user");
	//
	String userActivity = getStringFromParamMap("userActivity", request.getParameterMap());
	if (userActivity.length() == 0) {
		userActivity = userID;
	}
	//
	Map data = null;
	TableData dataTable = null;
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
	//
	if (data == null) {
		data = Bid.searchBid(null, userActivity, null);
		dataTable = (TableData) (data.get(DATA_NAME_DATA));
		request.getSession().setAttribute(SESSION_ATTRIBUTE_DATA_MAP, data);
	}
	//
	request.setAttribute("dataTable", dataTable);
%>

<%@include file="../header.jsp" %>
<%@include file="nav.jsp" %>

<form id='form-getActivity' method='post'>
	<%
		out.println("<input type='hidden' name='action' value='getActivity'/>");
		//
		out.println("<table>");
		//
		out.println("<tr>");
		out.println("<td>");
		out.println("Select A User:");
		out.println("</td>");
		out.println("<td>");
		out.println(Helper.getSelection("userActivity", lstUser.toArray(), userActivity));
		out.println("</td>");
		out.println("</tr");
		//
		out.println("</table>");
	%>
</form>

<%@include file="../showTableTwo.jsp" %>

</body>

</html>
