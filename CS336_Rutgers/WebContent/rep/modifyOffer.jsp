<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<%@ page import="rutgers.cs336.db.Offer" %>
<%@ page import="java.util.List" %>
<%@ page import="static rutgers.cs336.servlet.IConstant.*" %>
<%@ page import="static rutgers.cs336.db.DBBase.*" %>

<html>

<head>
	<meta charset="utf-8">
	<title>BuyMe - Modify Offers</title>
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

<form id="form-id-cancelBid" action="${pageContext.request.contextPath}/rep/cancelBid.jsp" method="post">
	<input id="input-id-cancelBid" type="hidden" name="bidID" value="_"/>
</form>

<form id="form-id-modifyBid" action="${pageContext.request.contextPath}/rep/modifyBid.jsp" method="post">
	<input id="input-id-modifyBid" type="hidden" name="bidID" value="_"/>
</form>

<form id="form-sort" target="_self" method="post">
	<input id="input-sort" type="hidden" name="sort" value="_"/>

	<%
		Map dataModify = null;
		Map data = null;
		Map categoryAndField = null;
		TableData dataTable = null;
		//
		String offeridcategoryname = DBBase.getStringFromParamMap("offeridcategoryname", request.getParameterMap());
		//
		String[] temps = offeridcategoryname.split(",");
		//
		String action = getStringFromParamMap("action", request.getParameterMap());
		if (action.equals("modifyOffer")) {
			dataModify = Offer.doCreateOrModifyOffer(null, request.getParameterMap(), false);
		}
		else if (action.equals("startModifyOffer")) {
		}
		//
		categoryAndField = CategoryAndField.getCategoryField(temps[1]);
		List lstCategory = (List) categoryAndField.get(CategoryAndField.DATA_CATEGORY_LIST);
		List lstField = (List) categoryAndField.get(CategoryAndField.DATA_FIELD_LIST);
		//
		data = Offer.doSearchOfferByID(temps[0]);
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

</form>

<form id="form" method="post">

	<%@include file="../createOfferCommon.jsp" %>

	<input type="submit" value="Submit">
</form>

</body>

</html>
