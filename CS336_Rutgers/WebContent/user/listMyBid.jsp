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

<form id="form-id-cancelBid" action="${pageContext.request.contextPath}/rep/cancelBid.jsp" method="post">
	<input id="input-id-cancelBid" type="hidden" name="bidID" value="_"/>
</form>

<form id="form-id-modifyBid" action="${pageContext.request.contextPath}/rep/modifyBid.jsp" method="post">
	<input id="input-id-modifyBid" type="hidden" name="bidIDofferIDBuyer" value="_"/>
</form>

<%
	List lstUser = User.getUserList();
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
		}
	}
	//
	if (data == null || dataTable == null) {
		data = Bid.searchBid(null, null, userID);
		dataTable = (TableData) (data.get(DATA_NAME_DATA));
		//
		request.getSession().setAttribute(SESSION_ATTRIBUTE_DATA_MAP, data);
	}
%>

<%@include file="../header.jsp" %>
<%@include file="nav.jsp" %>

<%@include file="../listBidCommon.jsp" %>

</body>

</html>
