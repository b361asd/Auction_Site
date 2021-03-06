<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>

<%@ page import="auction.db.OfferAlert" %>
<%@ page import="auction.gui.Helper" %>
<%@ page import="static auction.servlet.IConstant.*" %>
<%@ page import="auction.gui.TableData" %>
<%@ page import="static auction.db.DBBase.*" %>

<html>

<head>
   <meta charset="utf-8">
   <title>BuyMe - Search Offers</title>
   <link rel="stylesheet" href='../style.css'/>
</head>

<body>

<%@include file="../header.jsp" %>
<%@include file="nav.jsp" %>

<form id="form-deleteOfferAlert" method="post">
   <input type="hidden" name="action" value="deleteOfferAlert"/>
   <input id="input-deleteOfferAlert" type="hidden" name="criterionID" value="_"/>
</form>

<%
   Map data = null;
   TableData dataTable;
   //
   String userID = (String) session.getAttribute(SESSION_ATTRIBUTE_USER);
   //
   String action = getStringFromParamMap("action", request.getParameterMap());
   if (action.equals("sort")) {
      data = (Map) request.getSession().getAttribute(SESSION_ATTRIBUTE_DATA_MAP);
      if (data != null) {
         dataTable = (TableData) (data.get(DATA_NAME_DATA));
         //
         if (dataTable != null) {
            String sort = getStringFromParamMap("sort", request.getParameterMap());
            dataTable.sortRowPerHeader(sort);
         }
         else {
            data = null;
         }
      }
   }
   else {
      if (action.equals("deleteOfferAlert")) {
         OfferAlert.deleteOfferAlert(request.getParameterMap());
         //request.getSession().setAttribute(SESSION_ATTRIBUTE_DATA_MAP, data);
      }
      else if (action.equals("createAlertCriterion")) {
         //String userID = (String) request.getSession().getAttribute("user");
         OfferAlert.doGenerateNewOfferAlertCriterion(userID, request.getParameterMap());
      }
   }
   //
   if (data == null) {
      data = OfferAlert.selectOfferAlert(userID, true);
      request.getSession().setAttribute(SESSION_ATTRIBUTE_DATA_MAP, data);
   }
   //
   dataTable = (TableData) (data.get(DATA_NAME_DATA));
%>


<table>
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
            out.println(Helper.getButton("form-deleteOfferAlert", "input-deleteOfferAlert", "" + dataTable.getOneCell(i, 0), "Delete"));
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

</body>
</html>
