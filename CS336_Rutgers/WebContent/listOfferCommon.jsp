<%@ page import="rutgers.cs336.db.Offer" %>
<%@ page import="rutgers.cs336.gui.Helper" %>
<%@ page import="static rutgers.cs336.servlet.IConstant.*" %>
<%@ page import="rutgers.cs336.gui.TableData" %>
<%@ page import="static rutgers.cs336.db.DBBase.*" %>

<%
	String __userID = (String) session.getAttribute(SESSION_ATTRIBUTE_USER);
	String __userType = (String) session.getAttribute(SESSION_ATTRIBUTE_USERTYPE);
	String _action = getStringFromParamMap("action", request.getParameterMap());
	//
	TableData _dataTable = (TableData) request.getAttribute("dataTable");
%>

<table>
	<thead>
	<tr>
		<%
			if (_dataTable != null) {
				out.println(_dataTable.printDescriptionForTable(true));
			}
		%>
	</tr>
	<tr>
		<td>Action</td>
		<%
			if (_dataTable != null) {
				out.println(_dataTable.printHeaderForTable());
			}
		%>
	</tr>
	</thead>
	<tbody>
	<%
		if (_dataTable != null && _dataTable.rowCount() > 0) {
			for (int i = 0; i < _dataTable.rowCount(); i++) {
				out.println(_dataTable.printRowStart(i));
				//
				out.println("<td>");
				if (__userType != null && __userType.equalsIgnoreCase("3")) {
					String seller = _dataTable.getOneCell(i, 1).toString();
					String status = _dataTable.getOneCell(i, 10).toString();
					if (!seller.equalsIgnoreCase(__userID) && status.equalsIgnoreCase("Active")) {
						out.println(Helper.getButton("form-id-doBid", "input-id-doBid", "" + _dataTable.getOneCell(i, 0), "Bid"));
					}
					out.println(Helper.getButton("form-id-listBid", "input-id-listBid", _dataTable.getOneCell(i, 0) + "," + _dataTable.getOneCell(i, 2), "List Bid"));
					if (!_action.equals("listSimilar")) {
						out.println(Helper.getButton("form-id-listSimilar", "input-id-listSimilar", _dataTable.getOneCell(i, 0) + "," + _dataTable.getOneCell(i, 2) + "," + _dataTable.getOneCell(i, 3), "List Similar"));
					}
				}
				else {
					out.println(Helper.getButton("form-id-cancelOffer", "input-id-cancelOffer", "" + _dataTable.getOneCell(i, 0), "Cancel"));
					out.println(Helper.getButton("form-id-modifyOffer", "input-id-modifyOffer", _dataTable.getOneCell(i, 0) + "," + _dataTable.getOneCell(i, 2) + "," + _dataTable.getOneCell(i, 1), "Modify"));
				}
				out.println("</td>");
				//
				out.println(_dataTable.printOneRowInTable(i));
				//
				out.println("</tr>");
			}
		}
	%>
	</tbody>
</table>
