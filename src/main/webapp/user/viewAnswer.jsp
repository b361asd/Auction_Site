<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>

<%@ page import="com.b361asd.auction.db.Question"%>
<%@ page import="com.b361asd.auction.gui.TableData"%>
<%@ page import="com.b361asd.auction.gui.Helper"%>

<html>

<head>
<meta charset="utf-8">
<title>BuyMe - View Answers</title>
<link rel="stylesheet" href='../style.css' />
</head>

<body>

    <%
    Map data;
    TableData dataTable = null;
    //DBBase.
    String action = DBBase.getStringFromParamMap("action", request.getParameterMap());
    if (action.equals("sort")) {
        data = (Map) request.getSession().getAttribute(IConstant.SESSION_ATTRIBUTE_DATA_MAP);
        //
        dataTable = (TableData) (data.get(IConstant.DATA_NAME_DATA));
        //
        String sort = DBBase.getStringFromParamMap("sort", request.getParameterMap());
        dataTable.sortRowPerHeader(sort);
    } else {
        data = Question.searchClosedQuestion(request.getParameterMap());
        request.getSession().setAttribute(IConstant.SESSION_ATTRIBUTE_DATA_MAP, data);
    }
    //
    if (dataTable == null) {
        dataTable = (TableData) (data.get(IConstant.DATA_NAME_DATA));
    }
    %>

    <%@include file="../header.jsp"%>
    <%@include file="nav.jsp"%>

    <form method="post">
        <%
        out.println("<input type='hidden' name='action' value='searchQuestion'/>");
        //
        out.println("<table>");
        out.println("<tr>");
        out.println("<td>");
        out.println("<div align='left' class='allField'>userID");
        out.println(Helper.getOPSZSelection("useridOP"));
        out.println("<input type='text' name='useridVal'/></div>");
        out.println("</td>");
        out.println("<td>");
        out.println("<div align='left' class='allField'>repID");
        out.println(Helper.getOPSZSelection("repidOP"));
        out.println("<input type='text' name='repidVal'/></div>");
        out.println("</td>");
        out.println("<td>");
        out.println("<div align='left' class='allField'>Lookbackdays");
        out.println("<input type='number' name='lookbackdays' value='30'/></div>");
        out.println("</td>");
        out.println("</tr>");
        //
        out.println("<tr>");
        out.println("<td colspan='2'>");
        out.println("<div align='left' class='allField'>Keys(Comma Delimited. i.e.: good,toyota)");
        out.println("<input type='text' name='tags'/></div>");
        out.println("</td>");
        out.println("<td>");
        out.println("<input type='submit' value='Search'>");
        out.println("</td>");
        out.println("</tr");
        out.println("</table>");
        %>
    </form>

    <table>
        <thead>
            <tr>
                <%
                out.println(dataTable.printDescriptionForTable(false));
                %>
            </tr>
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
                    out.println("<tr>");
                    out.println(dataTable.printOneRowInTable(i));
                    out.println("</tr>");
                }
            }
            %>
        </tbody>
    </table>

</body>

</html>
