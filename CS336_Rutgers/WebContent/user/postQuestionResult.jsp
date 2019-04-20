<%@page import="rutgers.cs336.db.Question" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<%@ page import="static rutgers.cs336.servlet.IConstant.*" %>

<html>

<head>
	<meta charset="utf-8">
	<title>BuyMe - Post a Question</title>
	<link rel="stylesheet" href='${pageContext.request.contextPath}/style.css?v=1.0'/>
</head>

<body>

<%
	String userID = (String) request.getSession().getAttribute("user");
	Map data = Question.insertQuestion(userID, DBBase.getStringFromParamMap("question", request.getParameterMap()));
	//
	//
	String message = "";
	if ((Boolean) data.get(DATA_NAME_STATUS)) {
		message = "Question Posted.";
	}
	else {
		message = "Error in Posting Question: " + data.get(DATA_NAME_MESSAGE);
	}
	;
%>

<%@include file="../header.jsp" %>
<%@include file="nav.jsp" %>

<h3><%=message%>
</h3>

</body>

</html>
