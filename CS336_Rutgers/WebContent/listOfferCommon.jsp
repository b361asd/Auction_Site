<%@ page import="rutgers.cs336.db.Offer" %>
<%@ page import="rutgers.cs336.gui.Helper" %>
<%@ page import="static rutgers.cs336.servlet.IConstant.*" %>
<%@ page import="rutgers.cs336.gui.TableData" %>
<%@ page import="static rutgers.cs336.db.DBBase.*" %>
<%@ page import="java.util.Map" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<%
	String userType = (String) request.getSession().getAttribute("userType");
	//
	Map data = null;
	TableData dataTable = null;
	//
	String action = getStringFromParamMap("action", request.getParameterMap());
	switch (action) {
		case "listSimilar":
			String offeridcategorynameconditioncode = getStringFromParamMap("offeridcategorynameconditioncode", request.getParameterMap());
			data = Offer.doSearchSimilar(offeridcategorynameconditioncode);
			request.getSession().setAttribute(SESSION_ATTRIBUTE_DATA_MAP, data);
			break;
		case "searchOffer":
			data = Offer.doSearchOffer(request.getParameterMap());
			request.getSession().setAttribute(SESSION_ATTRIBUTE_DATA_MAP, data);
			break;
		case "browseOffer":
			data = Offer.doBrowseOffer();
			request.getSession().setAttribute(SESSION_ATTRIBUTE_DATA_MAP, data);
			break;
		default:
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
			break;
	}
	//
	dataTable = (TableData) (data.get(DATA_NAME_DATA));
%>

<table>
	<caption>Offer List</caption>
	<thead>
	<tr>
		<td>Action</td>
		<%
			out.println(dataTable.printHeaderForTable());
		%>
	</tr>
	</thead>
	<tbody>
	<%
		if (dataTable.rowCount() > 0) {
			for (int i = 0; i < dataTable.rowCount(); i++) {
				out.println(dataTable.printRowStart(i));
				//
				out.println("<td>");
				if (userType != null && userType.equalsIgnoreCase("3")) {
					out.println(Helper.getButton("form-id-doBid", "input-id-doBid", "" + dataTable.getOneCell(i, 0), "Bid"));
					out.println(Helper.getButton("form-id-listBid", "input-id-listBid", dataTable.getOneCell(i, 0) + "," + dataTable.getOneCell(i, 2), "List Bid"));
					if (!action.equals("listSimilar")) {
						out.println(Helper.getButton("form-id-listSimilar", "input-id-listSimilar", dataTable.getOneCell(i, 0) + "," + dataTable.getOneCell(i, 2) + "," + dataTable.getOneCell(i, 3), "List Similar"));
					}
				}
				else {
					out.println(Helper.getButton("form-id-cancelOffer", "input-id-cancelOffer", "" + dataTable.getOneCell(i, 0), "Cancel"));
					out.println(Helper.getButton("form-id-modifyOffer", "input-id-modifyOffer", dataTable.getOneCell(i, 0) + "," + dataTable.getOneCell(i, 2), "Modify"));
				}
				out.println("</td>");
				//
				out.println(dataTable.printOneRowInTable(i));
				//
				out.println("</tr>");
			}
		}
	%>
	</tbody>
</table>
