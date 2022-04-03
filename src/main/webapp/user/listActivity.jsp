<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>

<%@ page import="com.b361asd.auction.db.Bid"%>
<%@ page import="com.b361asd.auction.db.DBBase"%>
<%@ page import="com.b361asd.auction.db.User"%>
<%@ page import="com.b361asd.auction.gui.TableData"%>
<%@ page import="com.b361asd.auction.gui.Helper"%>
<%@ page import="java.util.List"%>

<html>

<head>
<meta charset="utf-8">
<title>BuyMe - Search Offers</title>
<link rel="stylesheet" href='../style.css' />

<script>
	function onSelectChange() {
		const form = document.getElementById('form-getActivity');
		form.submit();
	}
</script>
</head>


<body>

    <%
    List lstUser = User.getUserList();
    //
    String userID = (String) request.getSession().getAttribute("user");
    //
    String userActivity = DBBase.getStringFromParamMap("userActivity", request.getParameterMap());
    if (userActivity.length() == 0) {
        userActivity = userID;
    }
    //
    Map data = null;
    TableData dataTable = null;
    //
    String action = DBBase.getStringFromParamMap("action", request.getParameterMap());
    if (action.equals("sort")) {
        data = (Map) request.getSession().getAttribute(SESSION_ATTRIBUTE_DATA_MAP);
        if (data != null) {
            dataTable = (TableData) (data.get(DATA_NAME_DATA));
            //
            if (dataTable != null) {
                String sort = DBBase.getStringFromParamMap("sort", request.getParameterMap());
                dataTable.sortRowPerHeader(sort);
            } else {
                data = null;
            }
        }
    }
    //
    if (data == null) {
        data = Bid.searchBid(null, userActivity, null);
        dataTable = (TableData) (data.get(DATA_NAME_DATA));
        request.getSession().setAttribute(SESSION_ATTRIBUTE_DATA_MAP, data);
    }
    //
    request.setAttribute("dataTable", dataTable);
    %>

    <%@include file="../header.jsp"%>
    <%@include file="nav.jsp"%>

    <form id='form-getActivity' method='post'>
        <%
        out.println("<input type='hidden' name='action' value='getActivity'/>");
        //
        out.println("<table>");
        //
        out.println("<tr>");
        out.println("<td>");
        out.println("Select A User:");
        out.println("</td>");
        out.println("<td>");
        out.println(Helper.getSelection("userActivity", lstUser.toArray(), userActivity));
        out.println("</td>");
        out.println("</tr");
        //
        out.println("</table>");
        %>
    </form>

    <%@include file="../showTableTwo.jsp"%>

</body>

</html>
