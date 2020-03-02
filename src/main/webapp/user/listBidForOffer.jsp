<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>

<%@ page import="main.java.auction.db.Bid" %>
<%@ page import="static main.java.auction.servlet.IConstant.*" %>
<%@ page import="static main.java.auction.db.DBBase.*" %>
<%@ page import="main.java.auction.gui.TableData" %>

<html>

<head>
   <meta charset="utf-8">
   <title>BuyMe - List Bids</title>
   <link rel="stylesheet" href='../style.css'/>
</head>

<body>

<%
   Map data = null;
   TableData dataTable;
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
   //
   if (data == null) {
      data = Bid.searchBid(request.getParameterMap(), null, null);
      request.getSession().setAttribute(SESSION_ATTRIBUTE_DATA_MAP, data);
   }
   //
   dataTable = (TableData) (data.get(DATA_NAME_DATA));
   //
   request.setAttribute("dataTable", dataTable);
%>

<%@include file="../header.jsp" %>
<%@include file="nav.jsp" %>

<%@include file="../showTableTwo.jsp" %>

</body>

</html>
