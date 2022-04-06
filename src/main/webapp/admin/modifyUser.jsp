<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>

<%@ page import="com.b361asd.auction.db.User"%>
<%@ page import="com.b361asd.auction.gui.TableData"%>
<%@ page import="java.util.Objects"%>

<html>

<head>
<meta charset="utf-8">
<title>BuyMe - Modify User</title>
<link rel="stylesheet" href="../style.css">
</head>

<body>

    <%
    Map<String, Object> data = null;
    TableData dataTable;
    String _userType = (String) session.getAttribute(IConstant.SESSION_ATTRIBUTE_USERTYPE);
    int targetUsrType = UserType.USER.getDatabaseUserType();
    if (_userType.equalsIgnoreCase(UserType.ADMIN.getSessionUserType())) {
        targetUsrType = UserType.REP.getDatabaseUserType();
    }
    String action = DBBase.getStringFromParamMap("action", request.getParameterMap());
    if (action.equals("updateUser")) {
        data = User.selectUser(request.getParameterMap(), targetUsrType);
        request.getSession().setAttribute(IConstant.SESSION_ATTRIBUTE_DATA_MAP, data);
    }
    dataTable = (TableData) (Objects.requireNonNull(data).get(IConstant.DATA_NAME_DATA));
    %>

    <%@include file="../header.jsp"%>
    <%@include file="nav.jsp"%>

    <table>
        <thead>
            <tr>
                <%
                out.println(dataTable.printHeaderForTable());
                %>
            </tr>
        </thead>
        <tbody>
            <%
            if (dataTable.rowCount() > 0) {
                for (int i = 0; i < dataTable.rowCount(); i++) {
                    out.println(dataTable.printRowStart(i));
                    //
                    out.println(dataTable.printOneRowInTable(i));
                    //
                    out.println("</tr>");
                }
            }
            %>
        </tbody>
    </table>

    <br />
    <br />

    <form action="listUser.jsp" method="post">
        <%
        out.println("<input type='hidden' name='action' value='updateUser'/>");
        out.println("<div><input type='hidden' name='username' value='" + dataTable.getOneCell(0, 0) + "'></div>");
        //
        out.println("<table>");
        out.println("<tr>");
        out.println("<td style='text-align:left'>Password</td>");
        out.println("<td style='text-align:left'>");
        out.println("<input type='text' name='password' value='" + dataTable.getOneCell(0, 1) + "' />");
        out.println("</td>");
        out.println("</tr>");
        out.println("<tr>");
        out.println("<td style='text-align:left'>Email Address</td>");
        out.println("<td style='text-align:left'>");
        out.println("<input type='text' name='email' value='" + dataTable.getOneCell(0, 2) + "' />");
        out.println("</td>");
        out.println("</tr>");
        out.println("<tr>");
        out.println("<td style='text-align:left'>First Name</td>");
        out.println("<td style='text-align:left'>");
        out.println("<input type='text' name='firstname' value='" + dataTable.getOneCell(0, 3) + "' />");
        out.println("</td>");
        out.println("</tr>");
        out.println("<tr>");
        out.println("<td style='text-align:left'>Last Name</td>");
        out.println("<td style='text-align:left'>");
        out.println("<input type='text' name='lastname' value='" + dataTable.getOneCell(0, 4) + "' />");
        out.println("</td>");
        out.println("</tr>");
        out.println("<tr>");
        out.println("<td style='text-align:left'>Address</td>");
        out.println("<td style='text-align:left'>");
        out.println("<input type='text' name='address' value='" + dataTable.getOneCell(0, 5) + "' />");
        out.println("</td>");
        out.println("</tr>");
        out.println("<tr>");
        out.println("<td style='text-align:left'>Phone Number</td>");
        out.println("<td style='text-align:left'>");
        out.println("<input type='text' name='phone' value='" + dataTable.getOneCell(0, 6) + "' />");
        out.println("</td>");
        out.println("</tr>");
        out.println("<tr>");
        out.println("<td style='text-align:left'></td>");
        out.println("<td style='text-align:left'>");
        out.println("<input type='submit' value='Submit'>");
        out.println("</td>");
        out.println("</tr>");
        out.println("</table>");
        %>
    </form>

</body>

</html>
