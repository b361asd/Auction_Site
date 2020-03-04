<%@ page import="static auction.servlet.IConstant.*" %>
<%@ page import="auction.db.DBBase" %>
<%@ page import="java.util.Map" %>

<%@ page contentType="text/html;charset=UTF-8" %>
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
   Map _dataMapHeader_ = (Map) session.getAttribute(SESSION_ATTRIBUTE_DATA_MAP);
   if (_dataMapHeader_ != null && !(Boolean) _dataMapHeader_.get(DATA_NAME_STATUS)) {
      String _message = (String) _dataMapHeader_.get(DATA_NAME_MESSAGE);
      out.println("<h2>" + _message + "</h2>");
   }
   //
   String _sessionMessage_;
   {
      String _userID_    = (String) session.getAttribute(SESSION_ATTRIBUTE_USER);
      String _userType_  = (String) session.getAttribute(SESSION_ATTRIBUTE_USERTYPE);
      String _userFName_ = (String) session.getAttribute(SESSION_ATTRIBUTE_USER_FNAME);
      String _userLName_ = (String) session.getAttribute(SESSION_ATTRIBUTE_USER_LNAME);
      String _message_   = (String) session.getAttribute(SESSION_ATTRIBUTE_MESSAGE);
      //
      if (_userID_ == null) {
         _sessionMessage_ = "Welcome to BuyMe!";
      }
      else {
         _sessionMessage_ = "Welcome to BuyMe " + (_userType_.equals("1") ? "Admin Site" : _userType_.equals("2") ? "Representative Site" : "User Site") + ", " + _userFName_ + " " + _userLName_ + "!";
      }
      //
      if (_message_ != null && _message_.length() > 0) {
         _sessionMessage_ = _sessionMessage_ + " (" + _message_ + ")";
      }
   }
   //
   String _debug = DBBase.dumpParamMap(request.getParameterMap());
%>
<p hidden><%=_debug%>
</p>
<h5><%=_sessionMessage_%>
</h5>
