<%@ page import="static rutgers.cs336.servlet.IConstant.*" %>
<%@ page import="java.util.Map" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<%
	Map dataMapHeader = (Map) session.getAttribute(SESSION_ATTRIBUTE_DATA_MAP);
	if (dataMapHeader!=null && !(Boolean)dataMapHeader.get(DATA_NAME_STATUS)) {
		String message = (String) dataMapHeader.get(DATA_NAME_MESSAGE);
		out.println("<h1>" + message + "</h1>");
	}
	//
	String sessionMessage;
	{
		String userID    = (String) session.getAttribute(SESSION_ATTRIBUTE_USER);
		String userType  = (String) session.getAttribute(SESSION_ATTRIBUTE_USERTYPE);
		String userFName = (String) session.getAttribute(SESSION_ATTRIBUTE_USER_FNAME);
		String userLName = (String) session.getAttribute(SESSION_ATTRIBUTE_USER_LNAME);
		//
		if (userID == null) {
			sessionMessage = "Welcome to BuyMe!";
		}
		else {
			sessionMessage = "Welcome, " + userType + " " + userFName + " " + userLName;
		}
	}
%>
<h1><%=sessionMessage%></h1>
