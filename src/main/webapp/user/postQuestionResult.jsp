<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>

<%@ page import="static main.java.auction.servlet.IConstant.*" %>
<%@page import="main.java.auction.db.Question" %>
<%@ page import="main.java.auction.db.DBBase" %>

<html>

<head>
   <meta charset="utf-8">
   <title>BuyMe - Post a Question</title>
   <link rel="stylesheet" href='${pageContext.request.contextPath}/main/webapp/style.css'/>
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
