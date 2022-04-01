<%@ page import="com.b361asd.auction.db.CategoryAndField"%>
<%@page import="com.b361asd.auction.gui.Helper"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>

<table>
    <tbody>
        <%
        // User Search Offer
        // User Create Alert Criterion
        // Rep Search Offer
        Map data = null;
        List lstCategory = (List) data.get(CategoryAndField.DATA_CATEGORY_LIST);
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
        out.println("<tr>");
        out.println("<td align='left'>");
        out.println("categoryName");
        out.println("</td>");
        out.println("<td align='left'>");
        out.println(Helper.getCategoryNameCheckBox("categoryName", lstCategory));
        out.println("</td>");
        out.println("</tr>");
        //
        out.println("<tr>");
        out.println("<td align='left'>");
        out.println("seller");
        out.println("</td>");
        out.println("<td align='left'>");
        out.println(Helper.getOPSZSelection("sellerOP"));
        out.println("<input type='text' name='sellerVal'/>");
        out.println("</td>");
        out.println("</tr>");
        //
        out.println("<tr>");
        out.println("<td align='left'>");
        out.println("conditionCode");
        out.println("</td>");
        out.println("<td align='left'>");
        out.println(Helper.getConditionCodeCheckBox("conditionCode"));
        out.println("</td>");
        out.println("</tr>");
        //
        out.println("<tr>");
        out.println("<td align='left'>");
        out.println("description");
        out.println("</td>");
        out.println("<td align='left'>");
        out.println(Helper.getOPSZSelection("descriptionOP"));
        out.println("<input type='text' name='descriptionVal'/>");
        out.println("</td>");
        out.println("</tr>");
        List lstField = (List) data.get(CategoryAndField.DATA_FIELD_LIST);
        StringBuilder lstFieldIDs = null;
        for (Object o : lstField) {
            String fieldName = ((CategoryAndField.Field) o).getFieldName();
            int fieldID = ((CategoryAndField.Field) o).getFieldID();
            int fieldType = ((CategoryAndField.Field) o).getFieldType();
            if (lstFieldIDs == null) {
                lstFieldIDs = new StringBuilder("" + fieldID);
            } else {
                lstFieldIDs.append(",").append(fieldID);
            }
            // String
            if (fieldType == 1) {
                out.println("<tr>");
                out.println("<td align='left'>");
                out.println(fieldName);
                out.println("</td>");
                out.println("<td align='left'>");
                out.println(Helper.getOPSZSelection("fieldop_" + fieldID));
                out.println("<input type='text' name='fieldval1_" + fieldID + "'/>");
                out.println("</td>");
                out.println("</tr>");
            } else if (fieldType == 2) {
                // Integer
                out.println("<tr>");
                out.println("<td align='left'>");
                out.println(fieldName);
                out.println("</td>");
                out.println("<td align='left'>");
                out.println(Helper.getOPIntSelection("fieldop_" + fieldID));
                out.println("<input type='number' name = 'fieldval1_" + fieldID + "' / >");
                out.println("<input type='number' name = 'fieldval2_" + fieldID + "' / >");
                out.println("</td>");
                out.println("</tr>");
            } else {
                // Boolean
                out.println("<tr>");
                out.println("<td align='left'>");
                out.println(fieldName);
                out.println("</td>");
                out.println("<td align='left'>");
                out.println(Helper.getOPBoolSelection("fieldop_" + fieldID));
                out.println("</td>");
                out.println("</tr>");
            }
        }
        %>
    </tbody>
</table>

<%
out.println("<input name='lstFieldIDs' type='hidden' value='" + lstFieldIDs + "'/>");
%>
