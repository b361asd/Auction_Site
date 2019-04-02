<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<%@ page import="rutgers.cs336.db.Bid" %>
<%@ page import="rutgers.cs336.gui.Helper" %>
<%@ page import="static rutgers.cs336.servlet.IConstant.*" %>
<%@ page import="rutgers.cs336.gui.TableData" %>
<%@ page import="static rutgers.cs336.db.DBBase.*" %>
<%@ page import="java.util.List" %>

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
<%@include file="repNav.jsp" %>

<form id="form-id-cancelBid" action="${pageContext.request.contextPath}/rep/cancelBid.jsp" method="post">
	<input id="input-id-cancelBid" type="hidden" name="bidID" value="_"/>
</form>

<form id="form-id-modifyBid" action="${pageContext.request.contextPath}/rep/modifyBid.jsp" method="post">
	<input id="input-id-modifyBid" type="hidden" name="bidID" value="_"/>
</form>

<form id="form-sort" target="_self" method="post">
	<input id="input-sort" type="hidden" name="sort" value="_"/>
	
	<%
		Map data = null;
		TableData dataTable = null;
		//
		String action = getStringFromParamMap("action", request.getParameterMap());
		if (action.equals("searchBid")) {
			data = Bid.searchBid(request.getParameterMap());
			request.getSession().setAttribute(SESSION_ATTRIBUTE_DATA_MAP, data);
		}
		else if (action.equals("browseBid")) {
			data = Bid.searchBid(null);
			request.getSession().setAttribute(SESSION_ATTRIBUTE_DATA_MAP, data);
		}
		else {
			data = (Map) request.getSession().getAttribute(SESSION_ATTRIBUTE_DATA_MAP);
			if (data != null) {
				dataTable = (TableData) (data.get(DATA_NAME_DATA));
			}
			//
			if (dataTable == null) {
			}
			else {
				String sort = getStringFromParamMap("sort", request.getParameterMap());
				dataTable.sortRowPerHeader(sort);
			}
		}
		//
		//
		dataTable = (TableData) (data.get(DATA_NAME_DATA));
		List lstHeader = dataTable.getLstHeader();
		List lstRows = dataTable.getLstRows();
		int[] colSeq = dataTable.getColSeq();
		String offerIDStandOut = dataTable.getOfferIDStandOut();
	%>

	<table>
		<caption>Offer List</caption>
		<thead>
		<tr>
			<td>Action</td>
			<%
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
					boolean isStandOut = offerIDStandOut != null && (lstOneRow.get(0)).equals(offerIDStandOut);
					//
					if (isStandOut) {
						out.println("<tr name='standout' class='standout'>");
					}
					else {
						out.println("<tr>");
					}
					//
					out.println("<td>");
					out.println("<button onclick=\"document.getElementById('input-id-cancelBid').value='" + lstOneRow.get(0) + "'; document.getElementById('form-id-cancelBid').submit();\" class=\"favorite styled\" type=\"button\">Cabcel Bid</button>");
					out.println("<button onclick=\"document.getElementById('input-id-modifyBid').value='" + lstOneRow.get(0) + "'; document.getElementById('form-id-modifyBid').submit();\" class=\"favorite styled\" type=\"button\">Modify Bid</button>");
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
