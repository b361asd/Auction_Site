<%@ page import="rutgers.cs336.db.CategoryAndField" %>
<%@ page import="java.util.List" %>
<%@ page import="static rutgers.cs336.gui.Helper.*" %>

<table>
	<tbody>

	<%
		//User Search Offer
		//User Create Alert Criterion
		//Rep Search Offer
		//
		//Map data = null;
		//
		List lstCategory = (List) data.get(CategoryAndField.DATA_CATEGORY_LIST);
		//
		String currUrl = request.getRequestURL().toString();
		if (currUrl.toUpperCase().contains("createOfferAlertCriterion".toUpperCase())) {
			out.println("<tr>");
			out.println("<td align='left'>");
			out.println("Name");
			out.println("</td>");
			out.println("<td align='left'>");
			out.println("<input type='text' name='criterionName' value='My Alert' />");
			out.println("</td>");
			out.println("</tr>");
		}
		//
		out.println("<tr>");
		out.println("<td align='left'>");
		out.println("categoryName");
		out.println("</td>");
		out.println("<td align='left'>");
		out.println(getCategoryNameCheckBox("categoryName", lstCategory));
		out.println("</td>");
		out.println("</tr>");
		//
		out.println("<tr>");
		out.println("<td align='left'>");
		out.println("seller");
		out.println("</td>");
		out.println("<td align='left'>");
		out.println(getOPSZSelection("sellerOP"));
		out.println("<input type='text' name='sellerVal'/>");
		out.println("</td>");
		out.println("</tr>");
		//
		out.println("<tr>");
		out.println("<td align='left'>");
		out.println("conditionCode");
		out.println("</td>");
		out.println("<td align='left'>");
		out.println(getConditionCodeCheckBox("conditionCode"));
		out.println("</td>");
		out.println("</tr>");
		//
		out.println("<tr>");
		out.println("<td align='left'>");
		out.println("description");
		out.println("</td>");
		out.println("<td align='left'>");
		out.println(getOPSZSelection("descriptionOP"));
		out.println("<input type='text' name='descriptionVal'/>");
		out.println("</td>");
		out.println("</tr>");
		//
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
				out.println("<tr>");
				out.println("<td align='left'>");
				out.println(fieldName);
				out.println("</td>");
				out.println("<td align='left'>");
				out.println(getOPSZSelection("fieldop_" + fieldID));
				out.println("<input type='text' name='fieldval1_" + fieldID + "'/>");
				out.println("</td>");
				out.println("</tr>");
			}
			// Integer
			else if (fieldType == 2) {
				out.println("<tr>");
				out.println("<td align='left'>");
				out.println(fieldName);
				out.println("</td>");
				out.println("<td align='left'>");
				out.println(getOPIntSelection("fieldop_" + fieldID));
				out.println("<input type='number' name = 'fieldval1_" + fieldID + "' / >");
				out.println("<input type='number' name = 'fieldval2_" + fieldID + "' / >");
				out.println("</td>");
				out.println("</tr>");
			}
			// Boolean
			else {
				out.println("<tr>");
				out.println("<td align='left'>");
				out.println(fieldName);
				out.println("</td>");
				out.println("<td align='left'>");
				out.println(getOPBoolSelection("fieldop_" + fieldID));
				out.println("</td>");
				out.println("</tr>");
			}
		}
		//
	%>

	</tbody>
</table>

<%
	out.println("<input name='lstFieldIDs' type='hidden' value='" + lstFieldIDs + "'/>");
%>
