<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>

<%@ page import="com.b361asd.auction.db.Bid"%>

<html>

<head>
<meta charset="utf-8">
<title>BuyMe - Post Bid Results</title>
<link rel="stylesheet" href='../style.css' />
</head>

<body>

    <%
    String userID = (String) request.getSession().getAttribute("user");
    Map data = Bid.doCreateOrModifyBid(userID, request.getParameterMap(), true);
    String message;
    if ((Boolean) data.get(IConstant.DATA_NAME_STATUS)) {
        message = "Bid Posted.";
    } else {
        message = "Error in Posting Bid: " + data.get(IConstant.DATA_NAME_MESSAGE);
    }
    %>

    <%@include file="../header.jsp"%>
    <%@include file="nav.jsp"%>

    <h3><%=message%>
    </h3>

</body>

</html>
