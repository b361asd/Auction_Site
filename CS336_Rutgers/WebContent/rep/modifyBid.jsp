<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<%@ page import="rutgers.cs336.db.Bid" %>
<%@ page import="rutgers.cs336.gui.TableData" %>
<%@ page import="static rutgers.cs336.servlet.IConstant.*" %>
<%@ page import="static rutgers.cs336.db.DBBase.*" %>

<html>

<head>
	<meta charset="utf-8">
	<title>BuyMe - Modify Bids</title>
	<link rel="stylesheet" href='${pageContext.request.contextPath}/style.css'/>
</head>

<body>

<%@include file="../header.jsp" %>
<%@include file="nav.jsp" %>

<%
	Map data;
	TableData dataTable;
	//
	String action = getStringFromParamMap("action", request.getParameterMap());
	if (action.equals("modifyBid")) {
		Map dataModify = Bid.doCreateOrModifyBid(null, request.getParameterMap(), false);
	}
	//
	data = Bid.searchBid(request.getParameterMap(), null, null);
	//
	String bidIDofferIDBuyer = DBBase.getStringFromParamMap("bidIDofferIDBuyer", request.getParameterMap());
	//
	dataTable = (TableData) (data.get(DATA_NAME_DATA));
	//
	request.setAttribute("dataTable", dataTable);
%>

<%@include file="../showTableTwo.jsp" %>

<form method="post">
	<%
		out.println("<input type='hidden' name='action' value='modifyBid'/>");
		out.println("<input type='hidden' name='bidIDofferIDBuyer' value='" + bidIDofferIDBuyer + "'/>");
	%>

	<div>Price<input type="number" name="price" min="1"></div>
	<div>Auto Rebid Limit<input type="number" name="autoRebidLimit" min="1"></div>
	<input type="submit" value="Submit">
</form>

</body>

</html>
