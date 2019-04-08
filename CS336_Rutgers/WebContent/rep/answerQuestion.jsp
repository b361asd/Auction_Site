<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<%@page import="rutgers.cs336.db.Question" %>
<%@page import="rutgers.cs336.gui.Helper" %>
<%@page import="java.util.List" %>

<html>

<head>
	<meta charset="utf-8">
	<title>BuyMe - Remove Bid</title>
	<link rel="stylesheet" href="../style.css?v=1.0"/>
</head>

<body>


<%
	{
		String repID      = (String) request.getSession().getAttribute(SESSION_ATTRIBUTE_USER);
		String questionID = DBBase.getStringFromParamMap("questionID", request.getParameterMap());
		String answer     = DBBase.getStringFromParamMap("answer", request.getParameterMap());
		//
		Map dataUpdate;
		if (questionID != null && !questionID.equals("") && answer != null && !answer.equals("")) {
			dataUpdate = Question.answerQuestion(questionID, answer, repID);
		}
	}
	//
	Map data = Question.retrieveOneQuestion();
	//
	List lstRows = (List) Helper.getData(data);
%>

<%@include file="../header.jsp" %>
<%@include file="nav.jsp" %>

<table>
	<thead>
	<tr>
		<th>User</th>
		<th>QuestionDate</th>
		<th>Question</th>
	</tr>
	</thead>
	<tbody>
	<%
		if (lstRows.size() > 0) {
			for (Object lstRow : lstRows) {
				List oneRow = (List) lstRow;
				out.println("<tr>");
				//
				out.println("<td>" + oneRow.get(1) + "</td>");
				out.println("<td>" + oneRow.get(3) + "</td>");
				//
				out.println("<td>");
				{
					out.println("<form method='post'>");
					out.println("<input type='hidden' name='questionID' value='" + oneRow.get(0) + "'/>");
					out.println("<label for='question'>Question</label>");
					out.println("<textarea id='question' rows='5' cols='33' readonly>" + Helper.escapeHTML(oneRow.get(2).toString()) + "</textarea>");
					out.println("<label for='answer'>Answer</label>");
					out.println("<textarea id='answer' name='answer' rows='5' cols='33'></textarea>");
					out.println("<input type='submit' value='Submit'>");
					out.println("</form>");
				}
				out.println("</td>");
				//
				out.println("</tr>");
			}
		}
	%>
	</tbody>
</table>


</body>

</html>
