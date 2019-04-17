<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<%@ page import="rutgers.cs336.db.Bid" %>
<%@ page import="static rutgers.cs336.servlet.IConstant.*" %>
<%@ page import="static rutgers.cs336.db.DBBase.*" %>

<html>

<head>
	<meta charset="utf-8">
	<title>BuyMe - Modify Bids</title>
	<link rel="stylesheet" href="../style.css?v=1.0"/>
</head>

<body>

<%@include file="../header.jsp" %>
<%@include file="nav.jsp" %>

<form id="form-id-cancelBid" action="${pageContext.request.contextPath}/rep/cancelBid.jsp" method="post">
	<input id="input-id-cancelBid" type="hidden" name="bidID" value="_"/>
</form>

<form id="form-id-modifyBid" action="${pageContext.request.contextPath}/rep/modifyBid.jsp" method="post">
	<input id="input-id-modifyBid" type="hidden" name="bidID" value="_"/>
</form>

<form id="form-sort" target="_self" method="post">
	<input id="input-sort" type="hidden" name="sort" value="_"/>

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

</form>

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
