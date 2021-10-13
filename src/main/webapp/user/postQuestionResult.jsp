<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>

<%@ page import="static com.b361asd.auction.servlet.IConstant.*" %>
<%@page import="com.b361asd.auction.db.Question" %>
<%@ page import="com.b361asd.auction.db.DBBase" %>

<html>

<head>
   <meta charset="utf-8">
   <title>BuyMe - Post a Question</title>
   <link rel="stylesheet" href='../style.css'/>
</head>

<body>

<%
   String userID = (String) request.getSession().getAttribute("user");
   Map data = Question.insertQuestion(userID, DBBase.getStringFromParamMap("question", request.getParameterMap()));
   //
   //
   String message;
   if ((Boolean) data.get(DATA_NAME_STATUS)) {
      message = "Question Posted.";
   }
   else {
      message = "Error in Posting Question: " + data.get(DATA_NAME_MESSAGE);
   }
%>

<%@include file="../header.jsp" %>
<%@include file="nav.jsp" %>

<h3><%=message%>
</h3>

</body>

</html>
