<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<%@ page import="rutgers.cs336.db.Offer" %>
<%@ page import="rutgers.cs336.gui.Helper" %>
<%@ page import="static rutgers.cs336.servlet.IConstant.*" %>
<%@ page import="rutgers.cs336.gui.TableData" %>
<%@ page import="static rutgers.cs336.db.DBBase.*" %>
<%@ page import="java.util.List" %>

<html>

<head>
	<meta charset="utf-8">
	<title>BuyMe - Search Offers</title>
	<link rel="stylesheet" href="../style.css?v=1.0"/>

	<script type="text/javascript">
       function onClickHeader(value) {
           document.getElementById('input-sort').value = value;
           document.getElementById('form-sort').submit();
       }
	</script>
</head>

<body>

<%
	TableData dataTable = null;
	//
	Map data = (Map) request.getSession().getAttribute(SESSION_ATTRIBUTE_DATA_MAP);
	if (data != null) {
		dataTable = (TableData) (data.get(DATA_NAME_DATA));
	}
	//
	if (dataTable == null) {
		data = Offer.doSearchOffer(request.getParameterMap());
		request.getSession().setAttribute(SESSION_ATTRIBUTE_DATA_MAP, data);
	}
	else {
		String sort = getStringFromParamMap("sort", request.getParameterMap());
		dataTable.sortRowPerHeader(sort);
	}
	//
	dataTable = (TableData) (data.get(DATA_NAME_DATA));
	List lstHeader = dataTable.getLstHeader();
	List lstRows = dataTable.getLstRows();
	int[] colSeq = dataTable.getColSeq();
%>

<%@include file="../header.jsp" %>
<%@include file="userNav.jsp" %>

<form id="form-id-doBid" action="${pageContext.request.contextPath}/user/postBid.jsp" method="post">
	<input id="input-id-doBid" type="hidden" name="offeridcategoryname" value="_"/>
</form>

<form id="form-id-listBid" action="${pageContext.request.contextPath}/user/listBidResult.jsp" method="post">
	<input id="input-id-listBid" type="hidden" name="offeridcategoryname" value="_"/>
</form>

<form id="form-id-listSimilar" action="${pageContext.request.contextPath}/user/listSimilar.jsp" method="post">
	<input id="input-id-listSimilar" type="hidden" name="offeridcategorynameconditioncode" value="_"/>
</form>

<form id="form-sort" target="_self" method="post">
	<input id="input-sort" type="hidden" name="sort" value="_"/>
	<table>
		<thead>
		<tr>
			<td>Action</td>
			<%
				// Header
				out.println(Helper.printHeaderForTable(lstHeader, colSeq));
			%>
		</tr>
		</thead>

		<tbody>
		<%
			if (lstRows != null) {
				for (Object oneRow : lstRows) {
					List lstOneRow = (List) oneRow;
					//
					out.println("<tr>");
					//
					out.println("<td>");
					out.println("<button onclick=\"document.getElementById('input-id-doBid').value='" + lstOneRow.get(
							  0) + "," + lstOneRow.get(
							  1) + "'; document.getElementById('form-id-doBid').submit();\" class=\"favorite styled\" type=\"button\">Bid</button>");
					out.println("<button onclick=\"document.getElementById('input-id-listBid').value='" + lstOneRow.get(
							  0) + "," + lstOneRow.get(
							  1) + "'; document.getElementById('form-id-listBid').submit();\" class=\"favorite styled\" type=\"button\">List Bid</button>");
					out.println("<button onclick=\"document.getElementById('input-id-listSimilar').value='" + lstOneRow.get(
							  0) + "," + lstOneRow.get(1) + "," + lstOneRow.get(
							  3) + "'; document.getElementById('form-id-listSimilar').submit();\" class=\"favorite styled\" type=\"button\">List Similar</button>");
					out.println("</td>");
					//
					out.println(Helper.printOneRowInTable(lstOneRow, colSeq));
					//
					out.println("</tr>");
				}
			}
		%>
		</tbody>

	</table>
</form>

</body>

</html>
