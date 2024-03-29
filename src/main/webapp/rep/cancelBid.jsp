<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>

<%@ page import="com.b361asd.auction.db.Bid"%>

<html>

<head>
<meta charset="utf-8">
<title>BuyMe - Cancel Bid</title>
<link rel="stylesheet" href="../style.css">
</head>

<body>

    <%
    Map<String, Object> data = Bid.cancelBid(request.getParameterMap());
    request.getSession().setAttribute(IConstant.SESSION_ATTRIBUTE_DATA_MAP, data);
    String message;
    if ((Boolean) data.get(IConstant.DATA_NAME_STATUS)) {
        message = "Bid Cancelled.";
    } else {
        message = "Error in Cancelling Bid: " + data.get(IConstant.DATA_NAME_MESSAGE);
    }
    %>

    <%@include file="../header.jsp"%>
    <%@include file="nav.jsp"%>

    <h3><%=message%>
    </h3>

</body>

</html>
