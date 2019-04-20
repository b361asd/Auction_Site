<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<html>

<head>
	<meta charset="utf-8">
	<title>BuyMe - Search Offers</title>
	<link rel="stylesheet" href='${pageContext.request.contextPath}/style.css?v=1.0'/>
</head>

<body>

<%
	Map data = null;
	TableData dataTable = null;
	//
	String userID = (String) session.getAttribute(SESSION_ATTRIBUTE_USER);
	//
	String action = getStringFromParamMap("action", request.getParameterMap());
	if (action.equalsIgnoreCase("sort")) {
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
		data = Offer.doSearchMyOffer(userID);
		request.getSession().setAttribute(SESSION_ATTRIBUTE_DATA_MAP, data);
	}
	//
	if (data != null) {
		dataTable = (TableData) (data.get(DATA_NAME_DATA));
	}
	//
	request.setAttribute("dataTable", dataTable);
%>

<%@include file="../header.jsp" %>
<%@include file="nav.jsp" %>

<%@include file="../listOfferCommon.jsp" %>

</body>

</html>
