<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<%@ page import="rutgers.cs336.db.Bid" %>
<%@ page import="rutgers.cs336.gui.TableData" %>
<%@ page import="static rutgers.cs336.servlet.IConstant.*" %>
<%@ page import="static rutgers.cs336.db.DBBase.*" %>

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
		Map data = null;
		TableData dataTable = null;
		//
		String action = getStringFromParamMap("action", request.getParameterMap());
		if (action.equals("modifyBid")) {
			Map dataModify = Bid.doCreateOrModifyBid(null, request.getParameterMap(), false);
		}
		//
		data = Bid.searchBid(request.getParameterMap());
		//
		String bidIDofferIDBuyer = DBBase.getStringFromParamMap("bidIDofferIDBuyer", request.getParameterMap());
		//
		dataTable = (TableData) (data.get(DATA_NAME_DATA));
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
					//
					out.println("<tr>");
					out.println("<td>Bids</td>");
					{
						TableData dataTableBid = (TableData) (dataTable.getLastCellInRow(i));
						//
						out.println("<td colspan='" + (dataTable.colCount() - 1) + "'>");
						//
						out.println("<table>");
						out.println("<thead>");
						out.println("<tr>");
						out.println("<td>Action</td>");
						out.println(dataTableBid.printHeaderForTable());
						out.println("</tr>");
						out.println("</thead>");
						out.println("<tbody>");
						if (dataTableBid.rowCount() > 0) {
							for (int j = 0; j < dataTableBid.rowCount(); j++) {
								out.println(dataTableBid.printRowStart(j));
								out.println("<td>");
								out.println("<button onclick=\"document.getElementById('input-id-cancelBid').value='" + dataTableBid.getOneCell(j, 0) + "'; document.getElementById('form-id-cancelBid').submit();\" class=\"favorite styled\" type=\"button\">Cancel Bid</button>");
								out.println("<button onclick=\"document.getElementById('input-id-modifyBid').value='" + dataTableBid.getOneCell(j, 0) + "'; document.getElementById('form-id-modifyBid').submit();\" class=\"favorite styled\" type=\"button\">Modify Bid</button>");
								out.println("</td>");
								//
								out.println(dataTableBid.printOneRowInTable(j));
								out.println("</tr>");
								//

							}
						}
						out.println("</tbody>");
						out.println("</table>");
					}
					out.println("</td>");
					out.println("</tr>");
				}
			}
		%>
		</tbody>
	</table>

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
