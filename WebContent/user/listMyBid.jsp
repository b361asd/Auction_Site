<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<%@ page import="b361asd.auction.db.Bid" %>
<%@ page import="b361asd.auction.db.User" %>
<%@ page import="java.util.List" %>
<%@ page import="static b361asd.auction.servlet.IConstant.*" %>
<%@ page import="static b361asd.auction.db.DBBase.*" %>

<html>

<head>
	<meta charset="utf-8">
	<title>BuyMe - Search Offers</title>
	<link rel="stylesheet" href='${pageContext.request.contextPath}/style.css'/>

	<script type="text/javascript">
       function onSelectChange() {
           const form = document.getElementById('form-getActivity');
           form.submit();
       }
	</script>
</head>

<body>

<%
	//List lstUser = User.getUserList();
	//
	String userID = (String) request.getSession().getAttribute("user");
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
		data = Bid.searchBid(null, null, userID);
		request.getSession().setAttribute(SESSION_ATTRIBUTE_DATA_MAP, data);
	}
	//
	dataTable = (TableData) (data.get(DATA_NAME_DATA));
	//
	request.setAttribute("dataTable", dataTable);
%>

<%@include file="../header.jsp" %>
<%@include file="nav.jsp" %>

<%@include file="../showTableTwo.jsp" %>

</body>

</html>
