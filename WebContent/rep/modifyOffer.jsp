<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>

<%@ page import="b361asd.auction.db.Offer" %>
<%@ page import="static b361asd.auction.servlet.IConstant.*" %>
<%@ page import="static b361asd.auction.db.DBBase.*" %>

<html>

<head>
	<meta charset="utf-8">
	<title>BuyMe - Modify Offers</title>
	<link rel="stylesheet" href='${pageContext.request.contextPath}/style.css'/>
</head>

<body>

<%
	Map dataModify = null;
	Map data = null;
	Map categoryAndField = null;
	TableData dataTable = null;
	//
	String offeridcategorynameuser = DBBase.getStringFromParamMap("offeridcategorynameuser", request.getParameterMap());
	//
	String[] temps = offeridcategorynameuser.split(",");
	//
	String action = getStringFromParamMap("action", request.getParameterMap());
	if (action.equals("modifyOffer")) {
		dataModify = Offer.doCreateOrModifyOffer(temps[2], request.getParameterMap(), false);
	}
	//
	categoryAndField = CategoryAndField.getCategoryField(temps[1]);
	List lstCategory = (List) categoryAndField.get(CategoryAndField.DATA_CATEGORY_LIST);
	List lstField = (List) categoryAndField.get(CategoryAndField.DATA_FIELD_LIST);
	//
	data = Offer.doSearchOfferByID(temps[0], true);
	//
	request.getSession().setAttribute(SESSION_ATTRIBUTE_DATA_MAP, data);
	//
	dataTable = (TableData) (data.get(DATA_NAME_DATA));
	//
	if (dataModify != null) {
		boolean _status  = Helper.getStatus(dataModify);
		String  _message = Helper.getMessage(dataModify);
		if (!_status) {
			Helper.setStatus(data, false);
			Helper.appendMessage(data, Helper.getMessage(dataModify));
		}
	}
%>


<%@include file="../header2.jsp" %>
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
				out.println("<tr>");
				out.println(dataTable.printOneRowInTable(i));
				out.println("</tr>");
			}
		}
	%>
	</tbody>
</table>

<form id="form" method="post">
	<%@include file="../createOfferCommon.jsp" %>

	<input type="submit" value="Submit">
</form>

</body>

</html>
