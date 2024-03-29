<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>

<%@ page import="com.b361asd.auction.db.Question"%>

<html>

<head>
<meta charset="utf-8">
<title>BuyMe - Post a Question</title>
<link rel="stylesheet" href='../style.css' />
</head>

<body>

    <%
    String userID = (String) request.getSession().getAttribute("user");
    Map<String, Object> data =
            Question.insertQuestion(
                    userID,
                    DBBase.getStringFromParamMap("question", request.getParameterMap()));
    String message;
    if ((Boolean) data.get(IConstant.DATA_NAME_STATUS)) {
        message = "Question Posted.";
    } else {
        message = "Error in Posting Question: " + data.get(IConstant.DATA_NAME_MESSAGE);
    }
    %>

    <%@include file="../header.jsp"%>
    <%@include file="nav.jsp"%>

    <h3><%=message%>
    </h3>

</body>

</html>
