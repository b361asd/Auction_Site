<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>

<%@ page import="auction.db.Offer" %>
<%@ page import="auction.gui.TableData" %>
<%@ page import="static auction.db.DBBase.getStringFromParamMap" %>
<%@ page import="static auction.servlet.IConstant.SESSION_ATTRIBUTE_DATA_MAP" %>
<%@ page import="static auction.servlet.IConstant.DATA_NAME_DATA" %>

<html>

<head>
   <meta charset="utf-8">
   <title>BuyMe - Search Offers</title>
   <link rel="stylesheet" href='../style.css'/>
</head>

<body>

<%
   Map data;
   TableData dataTable = null;
   //
   String action = getStringFromParamMap("action", request.getParameterMap());
   if (action.equalsIgnoreCase("cancelOffer")) {
      Offer.doCancelOffer(request.getParameterMap());
      //
      data = Offer.doBrowseOffer();
      request.getSession().setAttribute(SESSION_ATTRIBUTE_DATA_MAP, data);
   }
   else if (action.equalsIgnoreCase("listSimilar")) {
      String offeridcategorynameconditioncode = getStringFromParamMap("offeridcategorynameconditioncode", request.getParameterMap());
      data = Offer.doSearchSimilar(offeridcategorynameconditioncode);
      request.getSession().setAttribute(SESSION_ATTRIBUTE_DATA_MAP, data);
   }
   else if (action.equalsIgnoreCase("searchOffer")) {
      data = Offer.doSearchOffer(request.getParameterMap(), true);
      request.getSession().setAttribute(SESSION_ATTRIBUTE_DATA_MAP, data);
   }
   else if (action.equalsIgnoreCase("browseOffer")) {
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
   if (data != null) {
      dataTable = (TableData) (data.get(DATA_NAME_DATA));
   }
   //
   request.setAttribute("dataTable", dataTable);
%>

<%@include file="../header.jsp" %>
<%@include file="nav.jsp" %>

<%@include file="../listOfferCommon.jsp" %>

</body>

</html>
