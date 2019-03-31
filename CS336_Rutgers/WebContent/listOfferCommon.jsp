<%@ page import="rutgers.cs336.db.Offer" %>
<%@ page import="rutgers.cs336.gui.Helper" %>
<%@ page import="static rutgers.cs336.servlet.IConstant.*" %>
<%@ page import="rutgers.cs336.gui.TableData" %>
<%@ page import="static rutgers.cs336.db.DBBase.*" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<%
	Map data;
	TableData dataTable = null;
	//
	String action = getStringFromParamMap("action", request.getParameterMap());
	if (action.equals("listSimilar")) {
		String offeridcategorynameconditioncode = getStringFromParamMap("offeridcategorynameconditioncode", request.getParameterMap());
		data = Offer.doSearchSimilar(offeridcategorynameconditioncode);
		request.getSession().setAttribute(SESSION_ATTRIBUTE_DATA_MAP, data);
	}
	else if (action.equals("searchOffer")) {
		data = Offer.doSearchOffer(request.getParameterMap());
		request.getSession().setAttribute(SESSION_ATTRIBUTE_DATA_MAP, data);
	}
	else if (action.equals("browseOffer")) {
		data = Offer.doBrowseOffer();
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
				out.println("<button onclick=\"document.getElementById('input-id-doBid').value='" + lstOneRow.get(0) + "," + lstOneRow.get(2) + "'; document.getElementById('form-id-doBid').submit();\" class=\"favorite styled\" type=\"button\">Bid</button>");
				out.println("<button onclick=\"document.getElementById('input-id-listBid').value='" + lstOneRow.get(0) + "," + lstOneRow.get(2) + "'; document.getElementById('form-id-listBid').submit();\" class=\"favorite styled\" type=\"button\">List Bid</button>");
				if (!action.equals("listSimilar")) {
					out.println("<button onclick=\"document.getElementById('input-id-listSimilar').value='" + lstOneRow.get(0) + "," + lstOneRow.get(2) + "," + lstOneRow.get(3) + "'; document.getElementById('form-id-listSimilar').submit();\" class=\"favorite styled\" type=\"button\">List Similar</button>");
				}
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
