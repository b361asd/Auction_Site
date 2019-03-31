<%@ page import="rutgers.cs336.db.CategoryAndField" %>
<%@ page import="java.util.*" %>
<%@ page import="static rutgers.cs336.db.DBBase.*" %>
<%@ page import="static rutgers.cs336.gui.Helper.*" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

	<%
		Map data = (Map)request.getAttribute("TEMP");
	%>

	<div class='allField'>categoryName
		<%
			List lstCategory = (List) data.get(CategoryAndField.DATA_CATEGORY_LIST);
			out.println(getCategoryNameCheckBox("categoryName", lstCategory));
		%>
	</div>
	<br/>

	<%
		out.println("<div class='allField'>offerID");
		out.println(getOPSZSelection("offerIDOP"));
		out.println("<input type='text' name='offerIDVal'/></div><br/>");

		out.println("<div class='allField'>seller");
		out.println(getOPSZSelection("sellerOP"));
		out.println("<input type='text' name='sellerVal'/></div><br/>");

		out.println("<div class='allField'>conditionCode");
		out.println(getConditionCodeCheckBox("conditionCode"));
		out.println("</div><br/>");

		out.println("<div class='allField'>description");
		out.println(getOPSZSelection("descriptionOP"));
		out.println("<input type='text' name='descriptionVal'/></div><br/>");

		out.println("<div class='allField'>currentBidPrice");
		out.println(getOPIntSelection("priceOP"));
		out.println("<input type='number' name='priceVal1'/>");
		out.println("<input type='number' name='priceVal2'/></div><br/>");

		List lstField = (List) data.get(CategoryAndField.DATA_FIELD_LIST);
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
				out.println(getOPSZSelection("fieldop_" + fieldID));
				out.println("<input type='text' name='fieldval1_" + fieldID + "'/></div><br/>");
			}
			// Integer
			else if (fieldType == 2) {
				out.println("<div class='allField'>" + fieldName);
				out.println(getOPIntSelection("fieldop_" + fieldID));
				out.println("<input type='number' name = 'fieldval1_" + fieldID + "' / >");
				out.println("<input type='number' name = 'fieldval2_" + fieldID + "' / ></div><br/>");
			}
			// Boolean
			else {
				out.println("<div class='allField'>" + fieldName);
				out.println(getOPBoolSelection("fieldop_" + fieldID));
				out.println("</div><br/>");
			}
		}
		//
		out.println("<input name='lstFieldIDs' type='hidden' value='" + lstFieldIDs + "'/>");
	%>
