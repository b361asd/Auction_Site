<%@ page import="static rutgers.cs336.servlet.IConstant.*" %>
<%@ page import="java.util.Map" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<%
	Map _dataMapHeader = (Map) session.getAttribute(SESSION_ATTRIBUTE_DATA_MAP);
	if (_dataMapHeader != null && !(Boolean) _dataMapHeader.get(DATA_NAME_STATUS)) {
		String message = (String) _dataMapHeader.get(DATA_NAME_MESSAGE);
		out.println("<h1>" + message + "</h1>");
	}
	//
	String _sessionMessage;
	{
		String _userID    = (String) session.getAttribute(SESSION_ATTRIBUTE_USER);
		String _userType  = (String) session.getAttribute(SESSION_ATTRIBUTE_USERTYPE);
		String _userFName = (String) session.getAttribute(SESSION_ATTRIBUTE_USER_FNAME);
		String _userLName = (String) session.getAttribute(SESSION_ATTRIBUTE_USER_LNAME);
		String _message   = (String) session.getAttribute(SESSION_ATTRIBUTE_MESSAGE);
		//
		if (_userID == null) {
			_sessionMessage = "Welcome to BuyMe!";
		}
		else {
			_sessionMessage = "Welcome to BuyMe " + (_userType.equals("1") ? "Admin Site" : _userType.equals("2") ? "Representative Site" : "User Site") + ", " + _userFName + " " + _userLName + "!";
		}
		//
		if (_message != null && _message.length() > 0) {
			_sessionMessage = _sessionMessage + " (" + _message + ")";
		}
	}
%>
<h1><%=_sessionMessage%>
</h1>
