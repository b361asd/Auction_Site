<%@ page import="main.java.auction.db.CategoryAndField" %>
<%@ page import="main.java.auction.gui.Helper" %>
<%@ page import="main.java.auction.gui.HelperDatetime" %>
<%@ page import="main.java.auction.gui.TableData" %>
<%@ page import="static main.java.auction.gui.Helper.*" %>
<%@ page import="java.util.List" %>


<%
   List lstCategory		= null;
   List lstField 			= null;
   TableData dataTable 	= null;						//IN  from Modify order
   String offeridcategorynameuser 		= null;						//IN  from Modify order
   //
   StringBuilder lstFieldIDs = null;                  //OUT from Modify order
   //
   String userType = (String) request.getSession().getAttribute("userType");
%>

<%
   out.println("<table>");
   out.println("<tbody>");
   //
   if (userType.equalsIgnoreCase("3")) {
      out.println("<tr>");
      out.println("<td align='left'>categoryName");
      out.println("</td>");
      out.println("<td align='left'>");
      out.println("<select name='categoryName' onchange='onCategoryChange(this.parentElement);'>");
      for (Object o : lstCategory) {
         CategoryAndField.Category temp = (CategoryAndField.Category) o;
         out.println("<option " + (temp.isCurr() ? "selected " : "") + "value=\"" + temp.getCategoryName() + "\">" + temp.getCategoryName() + "</option>");
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
      out.println(getConditionCodeSelection("conditionCode", "New"));
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
      //
      for (Object o : lstField) {
         String fieldName = ((CategoryAndField.Field) o).getFieldName();
         int    fieldID   = ((CategoryAndField.Field) o).getFieldID();
         int    fieldType = ((CategoryAndField.Field) o).getFieldType();
         //
         // String
         if (fieldType == 1) {
            out.println("<tr>");
            out.println("<td align='left'>" + fieldName);
            out.println("</td>");
            out.println("<td align='left'>");
            out.println("<input type='text' name='fieldID_" + fieldID + "' />");
            out.println("</td>");
            out.println("</tr>");
         }
         // Integer
         else if (fieldType == 2) {
            out.println("<tr>");
            out.println("<td align='left'>" + fieldName);
            out.println("</td>");
            out.println("<td align='left'>");
            out.println("<input type='number' name = 'fieldID_" + fieldID + "' />");
            out.println("</td>");
            out.println("</tr>");
         }
         // Boolean
         else {
            out.println("<tr>");
            out.println("<td align='left'>" + fieldName);
            out.println("</td>");
            out.println("<td align='left'>");
            out.println(getYesNoSelection("fieldID_" + fieldID, "yes"));
            out.println("</td>");
            out.println("</tr>");
         }
      }
      //
      out.println("<tr>");
      out.println("<td align='left'>End date");
      out.println("</td>");
      out.println("<td align='left'>");
      out.println("<input type='datetime-local' name='endDate' value='" + HelperDatetime.getDatetimeSZ(7) + "' />");
      out.println("</td>");
      out.println("</tr>");
   }
   else {
      out.println("<tr>");
      out.println("<td align='left'>Minimal Price");
      out.println("</td>");
      out.println("<td align='left'>");
      String minPrice;
      {
         minPrice = Helper.escapeHTML(dataTable.getOneCell(0, "minPrice").toString());
         if (minPrice.startsWith("-1")) {
            minPrice = "";
         }
      }
      out.println("<input type='Number' name='minPrice' value='" + minPrice + "' />");
      out.println("</td>");
      out.println("</tr>");
      //
      out.println("<tr>");
      out.println("<td align='left'>conditionCode");
      out.println("</td>");
      out.println("<td align='left'>");
      out.println(getConditionCodeSelection("conditionCode", dataTable.getOneCell(0, "Condition").toString()));
      out.println("</td>");
      out.println("</tr>");
      //
      out.println("<tr>");
      out.println("<td align='left'>Description");
      out.println("</td>");
      out.println("<td align='left'>");
      out.println("<input type='text' name='description' value='" + Helper.escapeHTML(dataTable.getOneCell(0, "Desc").toString()) + "' />");
      out.println("</td>");
      out.println("</tr>");
      //
      for (Object o : lstField) {
         String fieldName = ((CategoryAndField.Field) o).getFieldName();
         int    fieldID   = ((CategoryAndField.Field) o).getFieldID();
         int    fieldType = ((CategoryAndField.Field) o).getFieldType();
         //
         if (lstFieldIDs == null) {
            lstFieldIDs = new StringBuilder("" + fieldID);
         }
         else {
            lstFieldIDs.append(",").append(fieldID);
         }
         // String
         if (fieldType == 1) {
            out.println("<tr>");
            out.println("<td align='left'>" + fieldName);
            out.println("</td>");
            out.println("<td align='left'>");
            out.println("<input type='text' name='fieldID_" + fieldID + "' value='" + Helper.escapeHTML(dataTable.getOneCell(0, fieldName).toString()) + "' />");
            out.println("</td>");
            out.println("</tr>");
         }
         // Integer
         else if (fieldType == 2) {
            out.println("<tr>");
            out.println("<td align='left'>" + fieldName);
            out.println("</td>");
            out.println("<td align='left'>");
            out.println("<input type='number' name = 'fieldID_" + fieldID + "' value='" + dataTable.getOneCell(0, fieldName) + "' />");
            out.println("</td>");
            out.println("</tr>");
         }
         // Boolean
         else {
            out.println("<tr>");
            out.println("<td align='left'>" + fieldName);
            out.println("</td>");
            out.println("<td align='left'>");
            out.println(getYesNoSelection("fieldID_" + fieldID, dataTable.getOneCell(0, fieldName).toString()));
            out.println("</td>");
            out.println("</tr>");
         }
      }
   }
   //
   out.println("</tbody>");
   out.println("</table>");
   //
   if (!userType.equalsIgnoreCase("3")) {
      out.println("<input type='hidden' name='action' value='modifyOffer'/>");
      out.println("<input type='hidden' name='offeridcategorynameuser' value='" + offeridcategorynameuser + "'/>");
      //
      out.println("<input name='lstFieldIDs' type='hidden' value='" + lstFieldIDs + "'/>");
   }
%>
