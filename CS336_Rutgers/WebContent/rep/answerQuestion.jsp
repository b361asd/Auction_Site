<%@page import="rutgers.cs336.db.DBBase"%>
<%@page import="rutgers.cs336.db.Question"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<html>

<head>
	<meta charset="utf-8">
	<title>BuyMe - Remove Bid</title>
	<link rel="stylesheet" href="../style.css?v=1.0"/>
</head>

<body>

<%
	{
		String repID = (String) request.getSession().getAttribute(SESSION_ATTRIBUTE_USER);
		String questionID = DBBase.getStringFromParamMap("questionID", request.getParameterMap());
		String answer = DBBase.getStringFromParamMap("answer", request.getParameterMap());
		//
		Map dataUpdate;
		if (questionID != null && !questionID.equals("") && answer != null && !answer.equals("")) {
			dataUpdate = Question.answerQuestion(questionID, answer, repID);	
		}
	}
	//
	Map data = Question.retrieveOneQuestion();
	//
	request.getSession().setAttribute(SESSION_ATTRIBUTE_DATA_MAP, data);
%>

<%@include file="../header.jsp" %>
<%@include file="repNav.jsp" %>

<form action='${request.getAttribute("javax.servlet.forward.request_uri")}' method='post'>
	<%
		out.println("<input type='hidden' name='questionID' value='" + data.get("questionID") + "'/>");
	%>

	<%
		out.println("<h1>" + data.get("userID") + "</h1>");
	%>

	<%
		out.println("<h1>" + data.get("questionDate") + "</h1>");
	%>

	<%
		out.println("<textarea name='textarea' rows='5' cols='33' readonly>" + data.get("question") + "</textarea>");
	%>

	<label for="answer">Answer a Question:</label>
	<textarea id="answer" name="answer" rows="5" cols="33"></textarea>
	
	<input type="submit" value="Submit">
</form>

</body>

</html>
