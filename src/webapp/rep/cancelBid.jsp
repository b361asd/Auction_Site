<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>

<%@ page import="b361asd.auction.db.Bid" %>
<%@ page import="static b361asd.auction.servlet.IConstant.*" %>

<html>

<head>
   <meta charset="utf-8">
   <title>BuyMe - Search Offers</title>
   <%--	<link rel="stylesheet" href='${pageContext.request.contextPath}/style.css'/>--%>
   <link rel="stylesheet" href="../style.css">
</head>

<body>

<%
   Map data = Bid.cancelBid(request.getParameterMap());
   //
   request.getSession().setAttribute(SESSION_ATTRIBUTE_DATA_MAP, data);
   //
   String message;
   if ((Boolean) data.get(DATA_NAME_STATUS)) {
      message = "Bid Cancelled.";
   }
   else {
      message = "Error in Cancelling Bid: " + data.get(DATA_NAME_MESSAGE);
   }
   ;
%>


<%@include file="../header.jsp" %>
<%@include file="nav.jsp" %>

<h3><%=message%>
</h3>

</body>

</html>
