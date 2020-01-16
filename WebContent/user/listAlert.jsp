<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>

<%@ page import="b361asd.auction.db.Alert" %>
<%@ page import="b361asd.auction.gui.Helper" %>
<%@ page import="static b361asd.auction.servlet.IConstant.*" %>
<%@ page import="b361asd.auction.gui.TableData" %>
<%@ page import="static b361asd.auction.db.DBBase.*" %>

<html>

<head>
    <meta charset="utf-8">
    <title>BuyMe - View Alerts</title>
    <link rel="stylesheet" href='../style.css'/>
</head>

<body>

<%
    Map data = null;
    TableData dataTable = null;
    //
    String userID = (String) request.getSession().getAttribute("user");
    //
    String action = getStringFromParamMap("action", request.getParameterMap());
    if (action.equals("sort")) {
        data = (Map) request.getSession().getAttribute(SESSION_ATTRIBUTE_DATA_MAP);
        if (data != null) {
            dataTable = (TableData) (data.get(DATA_NAME_DATA));
            //
            if (dataTable != null) {
                String sort = getStringFromParamMap("sort", request.getParameterMap());
                dataTable.sortRowPerHeader(sort);
            }
            else {
                data = null;
            }
        }
    }
    else {
        if (action.equals("deleteAlert")) {
            Map dataActivateUser = Alert.deleteAlert(request.getParameterMap());
        }
    }
    //
    if (data == null) {
        data = Alert.selectAlert(userID);
        request.getSession().setAttribute(SESSION_ATTRIBUTE_DATA_MAP, data);
    }
    //
    dataTable = (TableData) (data.get(DATA_NAME_DATA));
%>

<%@include file="../header.jsp" %>
<%@include file="nav.jsp" %>

<table>
    <thead>
    <tr>
        <td>Action</td>
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
                out.println("<td>");
                out.println(Helper.getButton("form-deleteAlert", "input-deleteAlert", "" + dataTable.getOneCell(i, 0), "Dismiss"));
                out.println(Helper.getButton("form-viewAlertDetail", "input-viewAlertDetail", "" + dataTable.getOneCell(i, 2) + "," + ((dataTable.getOneCell(i, 3) == null) ? "" : dataTable.getOneCell(i, 3)), "Detail"));
                out.println("</td>");
                //
                out.println(dataTable.printOneRowInTable(i));
                //
                out.println("</tr>");
            }
        }
    %>
    </tbody>
</table>

</body>
</html>
