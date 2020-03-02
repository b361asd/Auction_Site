<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>

<%@ page import="main.java.auction.db.Offer" %>
<%@ page import="static main.java.auction.servlet.IConstant.*" %>

<html>

<head>
   <meta charset="utf-8">
   <title>BuyMe - Search Offers</title>
   <%--	<link rel="stylesheet" href='${pageContext.request.contextPath}/style.css'/>--%>
   <link rel="stylesheet" href="../style.css">
</head>

<body>

<%
   Map data = Offer.doCancelOffer(request.getParameterMap());
   //
   request.getSession().setAttribute(SESSION_ATTRIBUTE_DATA_MAP, data);
   //
   String message;
   if ((Boolean) data.get(DATA_NAME_STATUS)) {
      message = "Offer Cancelled.";
   }
   else {
      message = "Error in Cancelling Offer: " + data.get(DATA_NAME_MESSAGE);
   }
%>

<%@include file="../header.jsp" %>
<%@include file="nav.jsp" %>

<h3><%=message%>
</h3>

</body>

</html>
