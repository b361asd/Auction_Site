<%@ page import="static rutgers.cs336.servlet.IConstant.*" %>
<%@ page import="rutgers.cs336.db.DBBase" %>
<%@ page import="java.util.Map" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<script type="text/javascript">
    function onClickHeader(value) {
        document.getElementById('input-sort').value = value;
        document.getElementById('form-sort').submit();
    }
</script>

<form id="form-sort" method="post">
	<input type="hidden" name="action" value="sort"/>
	<input id="input-sort" type="hidden" name="sort" value="_"/>
</form>

<%
	Map _dataMapHeader = (Map) session.getAttribute(SESSION_ATTRIBUTE_DATA_MAP);
	if (_dataMapHeader != null && !(Boolean) _dataMapHeader.get(DATA_NAME_STATUS)) {
		String _message = (String) _dataMapHeader.get(DATA_NAME_MESSAGE);
		out.println("<h2>" + _message + "</h2>");
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
	//
	String _debug = DBBase.dumpParamMap(request.getParameterMap());
%>
<p hidden><%=_debug%>
</p>
<h5><%=_sessionMessage%>
</h5>
