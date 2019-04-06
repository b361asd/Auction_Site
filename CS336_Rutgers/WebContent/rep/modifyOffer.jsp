<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<%@ page import="rutgers.cs336.db.CategoryAndField" %>
<%@ page import="rutgers.cs336.db.Offer" %>
<%@ page import="rutgers.cs336.gui.Helper" %>
<%@ page import="rutgers.cs336.gui.TableData" %>
<%@ page import="java.util.List" %>
<%@ page import="static rutgers.cs336.servlet.IConstant.*" %>
<%@ page import="static rutgers.cs336.db.DBBase.*" %>
<%@ page import="static rutgers.cs336.db.DBBase.*" %>
<%@ page import="static rutgers.cs336.gui.Helper.*" %>

<html>

<head>
	<meta charset="utf-8">
	<title>BuyMe - Search Offers</title>
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
		String[] temps = offeridcategoryname.split("\\,");
		//
		String action = getStringFromParamMap("action", request.getParameterMap());
		if (action.equals("modifyOffer")) {
			dataModify = Offer.doModifyOffer(request.getParameterMap());
		}
		else if (action.equals("startModifyOffer")) {
		}
		//
		categoryAndField = CategoryAndField.getCategoryField(temps[1]);
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

<form method="post">
	<%
		out.println("<input type='hidden' name='action' value='modifyOffer'/>");
		out.println("<input type='hidden' name='offeridcategoryname' value='" + offeridcategoryname + "'/>");
	%>

	<%
		List lstCategory = (List) categoryAndField.get(CategoryAndField.DATA_CATEGORY_LIST);

		out.println("<div class='allField'>conditionCode");
		out.println(getConditionCodeSelection("conditionCode", dataTable.getOneCell(0, "Condition").toString()));
		out.println("</div><br/>");

		out.println("<div class='allField'>description");
		out.println("<input type='text' name='descriptionVal' value='" + dataTable.getOneCell(0, "Desc") + "' /></div><br/>");

		List lstField = (List) categoryAndField.get(CategoryAndField.DATA_FIELD_LIST);
		String lstFieldIDs = null;
		for (Object o : lstField) {
			String fieldName = ((CategoryAndField.Field) o).getFieldName();
			int    fieldID   = ((CategoryAndField.Field) o).getFieldID();
			int    fieldType = ((CategoryAndField.Field) o).getFieldType();
			//
			if (lstFieldIDs == null) {
				lstFieldIDs = "" + fieldID;
			}
			else {
				lstFieldIDs = lstFieldIDs + "," + fieldID;
			}
			// String
			if (fieldType == 1) {
				out.println("<div class='allField'>" + fieldName);
				out.println("<input type='text' name='fieldval1_" + fieldID + "' value='" + dataTable.getOneCell(0, fieldName) + "' /></div><br/>");
			}
			// Integer
			else if (fieldType == 2) {
				out.println("<div class='allField'>" + fieldName);
				out.println("<input type='number' name = 'fieldval2_" + fieldID + "' value='" + dataTable.getOneCell(0, fieldName) + "' /></div><br/>");
			}
			// Boolean
			else {
				out.println("<div class='allField'>" + fieldName);
				out.println(getYesNoSelection("fieldop_" + fieldID, dataTable.getOneCell(0, fieldName).toString()));
				out.println("</div><br/>");
			}
		}
		//
		out.println("<input name='lstFieldIDs' type='hidden' value='" + lstFieldIDs + "'/>");
	%>


	<input type="submit" value="Submit">
</form>

</body>

</html>
