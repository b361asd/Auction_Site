<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="rutgers.cs336.gui.TableData" %>
<%@ page import="static rutgers.cs336.db.DBBase.*" %>

<%
	//TableData dataTable = null;
	//
	boolean _isRep = false;
	{
		String _userType = (String) session.getAttribute(SESSION_ATTRIBUTE_USERTYPE);
		if (_userType.equalsIgnoreCase("2")) {
			_isRep = true;
		}
	}
	String _colWidth = null;
%>


<%
	if (dataTable == null) {
%>


<%
}
else {
%>


<table>
	<thead>
	<%
		out.println("<tr>");
		out.println(dataTable.printHeaderForTable());
		out.println("</tr>");
		//
		if (dataTable.rowCount() > 0) {
			for (int i = 0; i < dataTable.rowCount(); i++) {
				TableData dataTableBid = (TableData) (dataTable.getLastCellInRow(i));
				if (dataTableBid != null) {
					int iWidth = 100 / (dataTableBid.colCount() + ((_isRep) ? 1 : 0));
					_colWidth = "" + iWidth + "%";
					//
					out.println("<tr>");
					out.println("<td>Bids</td>");
					{
						out.println("<td colspan='" + (dataTable.colCount() - 1) + "'>");
						//
						out.println("<table width='100%'>");
						out.println("<thead>");
						out.println("<tr>");
						if (_isRep) {
							out.println("<td width='" + _colWidth + "'>Action</td>");
						}
						out.println(dataTableBid.printSubHeaderForTable(_colWidth));
						out.println("</tr>");
						out.println("</thead>");
						out.println("</table>");
					}
					out.println("</td>");
					out.println("</tr>");
					//
					break;
				}
			}
		}
	%>
	</thead>
	<tbody>
	<%
		if (dataTable.rowCount() > 0) {
			for (int i = 0; i < dataTable.rowCount(); i++) {
				out.println(dataTable.printRowStart(i));
				out.println(dataTable.printOneRowInTable(i));
				out.println("</tr>");
				//
				TableData dataTableBid = (TableData) (dataTable.getLastCellInRow(i));
				if (dataTableBid != null) {
					out.println("<tr>");
					out.println("<td>Bids</td>");
					{
						out.println("<td colspan='" + (dataTable.colCount() - 1) + "'>");
						//
						out.println("<table width='100%'>");
						out.println("<tbody>");
						if (dataTableBid.rowCount() > 0) {
							for (int j = 0; j < dataTableBid.rowCount(); j++) {
								if (_isRep) {
									out.println("<td width='" + _colWidth + "'>");
									out.println("<button onclick=\"document.getElementById('input-id-cancelBid').value='" + dataTableBid.getOneCell(j, 0) + "'; document.getElementById('form-id-cancelBid').submit();\" class=\"favorite styled\" type=\"button\">Cancel Bid</button>");
									out.println("<button onclick=\"document.getElementById('input-id-modifyBid').value='" + dataTableBid.getOneCell(j, 0) + "," + dataTableBid.getOneCell(j, 1) + "," + dataTableBid.getOneCell(j, 2) + "'; document.getElementById('form-id-modifyBid').submit();\" class=\"favorite styled\" type=\"button\">Modify Bid</button>");
									out.println("</td>");
								}
								//
								out.println(dataTableBid.printRowStart(j));
								out.println(dataTableBid.printOneRowInTableWithWidth(j, _colWidth));
								out.println("</tr>");
							}
						}
						out.println("</tbody>");
						out.println("</table>");
						//
						out.println("</td>");
					}
					out.println("</tr>");
				}
			}
		}
	%>
	</tbody>
</table>


<%
	}
%>
