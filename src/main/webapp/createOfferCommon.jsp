<%@ page import="com.b361asd.auction.db.CategoryAndField"%>
<%@ page import="com.b361asd.auction.gui.Helper"%>
<%@ page import="com.b361asd.auction.gui.HelperDateTime"%>
<%@ page import="com.b361asd.auction.gui.TableData"%>
<%@ page import="com.b361asd.auction.gui.UserType"%>
<%@ page import="java.text.MessageFormat"%>
<%@ page import="java.util.List"%>

<%
List<CategoryAndField.Category> lstCategory = null;
List<CategoryAndField.Field> lstField = null;
TableData dataTable = null; // IN  from Modify order
String offeridcategorynameuser = null; // IN  from Modify order
StringBuilder lstFieldIDs = null; // OUT from Modify order
String userType = (String) request.getSession().getAttribute("userType");
%>

<%
out.println("<table>");
out.println("<tbody>");
if (userType.equalsIgnoreCase(UserType.USER.getSessionUserType())) {
    out.println("<tr>");
    out.println("<td align='left'>categoryName");
    out.println("</td>");
    out.println("<td align='left'>");
    out.println(
            "<select name='categoryName' onchange='onCategoryChange(this.parentElement);'>");
    for (CategoryAndField.Category category : lstCategory) {
        out.println(
                MessageFormat.format(
                        "<option {0}value=\"{1}\">{2}</option>",
                        category.isCurr() ? "selected " : "",
                        category.getCategoryName(),
                        category.getCategoryName()));
    }
    out.println("</select>");
    out.println("</td>");
    out.println("</tr>");
    //
    out.println("<tr>");
    out.println("<td align='left'>initPrice");
    out.println("</td>");
    out.println("<td align='left'>");
    out.println("<input type='NUMBER' name='initPrice' min='1'>");
    out.println("</td>");
    out.println("</tr>");
    //
    out.println("<tr>");
    out.println("<td align='left'>Price Increment");
    out.println("</td>");
    out.println("<td align='left'>");
    out.println("<input type='Number' name='increment' min='1'>");
    out.println("</td>");
    out.println("</tr>");
    //
    out.println("<tr>");
    out.println("<td align='left'>Minimal Price");
    out.println("</td>");
    out.println("<td align='left'>");
    out.println("<input type='Number' name='minPrice'>");
    out.println("</td>");
    out.println("</tr>");
    //
    out.println("<tr>");
    out.println("<td align='left'>conditionCode");
    out.println("</td>");
    out.println("<td align='left'>");
    out.println(Helper.getConditionCodeSelection("conditionCode", "New"));
    out.println("</td>");
    out.println("</tr>");
    //
    out.println("<tr>");
    out.println("<td align='left'>Description");
    out.println("</td>");
    out.println("<td align='left'>");
    out.println("<input type='text' name='description'>");
    out.println("</td>");
    out.println("</tr>");
    for (CategoryAndField.Field field : lstField) {
        String fieldName = field.getFieldName();
        int fieldID = field.getFieldID();
        int fieldType = field.getFieldType();
        // String
        if (fieldType == 1) {
            out.println("<tr>");
            out.println("<td align='left'>" + fieldName);
            out.println("</td>");
            out.println("<td align='left'>");
            out.println("<input type='text' name='fieldID_" + fieldID + "' />");
            out.println("</td>");
            out.println("</tr>");
        } else if (fieldType == 2) {
            // Integer
            out.println("<tr>");
            out.println("<td align='left'>" + fieldName);
            out.println("</td>");
            out.println("<td align='left'>");
            out.println("<input type='number' name = 'fieldID_" + fieldID + "' />");
            out.println("</td>");
            out.println("</tr>");
        } else {
            // Boolean
            out.println("<tr>");
            out.println("<td align='left'>" + fieldName);
            out.println("</td>");
            out.println("<td align='left'>");
            out.println(Helper.getYesNoSelection("fieldID_" + fieldID, "yes"));
            out.println("</td>");
            out.println("</tr>");
        }
    }
    out.println("<tr>");
    out.println("<td align='left'>End date");
    out.println("</td>");
    out.println("<td align='left'>");
    out.println(
            MessageFormat.format(
                    "<input type=''datetime-local'' name=''endDate'' value=''{0}'' />",
                    HelperDateTime.getDatetimeSZ(7)));
    out.println("</td>");
    out.println("</tr>");
} else {
    out.println("<tr>");
    out.println("<td align='left'>Minimal Price");
    out.println("</td>");
    out.println("<td align='left'>");
    String minPrice;
    minPrice = Helper.escapeHTML(dataTable.getOneCell(0, "minPrice").toString());
    if (minPrice.startsWith("-1")) {
        minPrice = "";
    }
    out.println("<input type='Number' name='minPrice' value='" + minPrice + "' />");
    out.println("</td>");
    out.println("</tr>");
    //
    out.println("<tr>");
    out.println("<td align='left'>conditionCode");
    out.println("</td>");
    out.println("<td align='left'>");
    out.println(
            Helper.getConditionCodeSelection(
                    "conditionCode", dataTable.getOneCell(0, "Condition").toString()));
    out.println("</td>");
    out.println("</tr>");
    //
    out.println("<tr>");
    out.println("<td align='left'>Description");
    out.println("</td>");
    out.println("<td align='left'>");
        out.println(
                MessageFormat.format(
                        "<input type=''text'' name=''description'' value=''{0}'' />",
                        Helper.escapeHTML(dataTable.getOneCell(0, "Desc").toString())));
    out.println("</td>");
    out.println("</tr>");
    for (CategoryAndField.Field field : lstField) {
        String fieldName = field.getFieldName();
        int fieldID = field.getFieldID();
        int fieldType = field.getFieldType();
        if (lstFieldIDs == null) {
            lstFieldIDs = new StringBuilder("" + fieldID);
        } else {
            lstFieldIDs.append(",").append(fieldID);
        }
        // String
        if (fieldType == 1) {
            out.println("<tr>");
            out.println("<td align='left'>" + fieldName);
            out.println("</td>");
            out.println("<td align='left'>");
                    out.println(
                            MessageFormat.format(
                                    "<input type=''text'' name=''fieldID_{0}'' value=''{1}'' />",
                                    fieldID,
                                    Helper.escapeHTML(
                                            dataTable.getOneCell(0, fieldName).toString())));
            out.println("</td>");
            out.println("</tr>");
        } else if (fieldType == 2) {
            // Integer
            out.println("<tr>");
            out.println("<td align='left'>" + fieldName);
            out.println("</td>");
            out.println("<td align='left'>");
            out.println(
                    MessageFormat.format(
                            "<input type=''number'' name = ''fieldID_{0}'' value=''{1}'' />",
                            fieldID, dataTable.getOneCell(0, fieldName)));
            out.println("</td>");
            out.println("</tr>");
        } else {
            // Boolean
            out.println("<tr>");
            out.println("<td align='left'>" + fieldName);
            out.println("</td>");
            out.println("<td align='left'>");
            out.println(
                    Helper.getYesNoSelection(
                            "fieldID_" + fieldID,
                            dataTable.getOneCell(0, fieldName).toString()));
            out.println("</td>");
            out.println("</tr>");
        }
    }
}
out.println("</tbody>");
out.println("</table>");
if (!userType.equalsIgnoreCase(UserType.USER.getSessionUserType())) {
    out.println("<input type='hidden' name='action' value='modifyOffer'/>");
    out.println(
            MessageFormat.format(
                    "<input type=''hidden'' name=''offeridcategorynameuser'' value=''{0}''/>",
                    offeridcategorynameuser));
    out.println("<input name='lstFieldIDs' type='hidden' value='" + lstFieldIDs + "'/>");
}
%>
