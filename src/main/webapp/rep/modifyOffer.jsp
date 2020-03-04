<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>

<%@ page import="auction.db.Offer" %>
<%@ page import="static auction.servlet.IConstant.*" %>
<%@ page import="static auction.db.DBBase.*" %>
<%@ page import="auction.gui.TableData" %>
<%@ page import="auction.db.DBBase" %>
<%@ page import="auction.db.CategoryAndField" %>
<%@ page import="auction.gui.Helper" %>

<html>

<head>
   <meta charset="utf-8">
   <title>BuyMe - Modify Offers</title>
   <link rel="stylesheet" href='../style.css'/>
</head>

<body>

<%
   Map dataModify = null;
   Map data;
   Map categoryAndField;
   TableData dataTable;
   //
   String offeridcategorynameuser = DBBase.getStringFromParamMap("offeridcategorynameuser", request.getParameterMap());
   //
   String[] temps = offeridcategorynameuser.split(",");
   //
   String action = getStringFromParamMap("action", request.getParameterMap());
   if (action.equals("modifyOffer")) {
      dataModify = Offer.doCreateOrModifyOffer(temps[2], request.getParameterMap(), false);
   }
   //
   categoryAndField = CategoryAndField.getCategoryField(temps[1]);
   categoryAndField.get(CategoryAndField.DATA_CATEGORY_LIST);
   categoryAndField.get(CategoryAndField.DATA_FIELD_LIST);
   //
   data = Offer.doSearchOfferByID(temps[0], true);
   //
   request.getSession().setAttribute(SESSION_ATTRIBUTE_DATA_MAP, data);
   //
   dataTable = (TableData) (data.get(DATA_NAME_DATA));
   //
   if (dataModify != null) {
      boolean _status  = Helper.getStatus(dataModify);
      Helper.getMessage(dataModify);
      if (!_status) {
         Helper.setStatus(data, false);
         Helper.appendMessage(data, Helper.getMessage(dataModify));
      }
   }
%>


<%@include file="../header2.jsp" %>
<%@include file="nav.jsp" %>

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
         }
      }
   %>
   </tbody>
</table>

<form id="form" method="post">
   <%@include file="../createOfferCommon.jsp" %>

   <input type="submit" value="Submit">
</form>

</body>

</html>
