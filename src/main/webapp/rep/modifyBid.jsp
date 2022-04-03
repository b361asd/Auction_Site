<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>

<%@ page import="com.b361asd.auction.db.Bid"%>
<%@ page import="com.b361asd.auction.db.DBBase"%>
<%@ page import="com.b361asd.auction.gui.TableData"%>
<%@ page import="java.math.BigDecimal"%>

<html>

<head>
<meta charset="utf-8">
<title>BuyMe - Modify Bids</title>
<link rel="stylesheet" href='../style.css' />
</head>

<body>

    <%
    Map data;
    TableData dataTable;
    String message = "";
    String action = DBBase.getStringFromParamMap("action", request.getParameterMap());
    if (action.equals("modifyBid")) {
        Map dataModify = Bid.doCreateOrModifyBid(null, request.getParameterMap(), false);
        if ((Boolean) dataModify.get(IConstant.DATA_NAME_STATUS)) {
            message = "Bid Posted.";
        } else {
            message = "Error in Posting Bid: " + dataModify.get(IConstant.DATA_NAME_MESSAGE);
        }
    }
    data = Bid.searchBid(request.getParameterMap(), null, null);
    String bidIDofferIDBuyer =
            DBBase.getStringFromParamMap("bidIDofferIDBuyer", request.getParameterMap());
    BigDecimal min = Bid.getMinForModifyBid(request.getParameterMap());
    dataTable = (TableData) (data.get(IConstant.DATA_NAME_DATA));
    request.setAttribute("dataTable", dataTable);
    %>

    <%@include file="../header2.jsp"%>
    <%@include file="nav.jsp"%>

    <h3><%=message%>
    </h3>

    <%@include file="../showTableTwo.jsp"%>

    <br />
    <br />

    <form method="post">
        <%
        out.println("<input type='hidden' name='action' value='modifyBid'/>");
        out.println("<input type='hidden' name='bidIDofferIDBuyer' value='" + bidIDofferIDBuyer + "'/>");
        %>

        <table>
            <tr>
                <%
                if (min != null) {
                    out.println("<td>Price(min " + min + ")</td>");
                } else {
                    out.println("<td>Price</td>");
                }
                %>
                <td>
                    <%
                    if (min != null) {
                        out.println("<input type='number' name='price' min='" + min + "' VALUE='" + min + "'>");
                    } else {
                        out.println("<input type='number' name='price' min='1'>");
                    }
                    %>
                </td>
            </tr>
            <tr>
                <td>Auto Rebid Limit</td>
                <td>
                    <%
                    out.println("<input type='number' name='autoRebidLimit' />");
                    %>
                </td>
            </tr>
            <tr>
                <td></td>
                <td><input type="submit" value="Submit"></td>
            </tr>
        </table>

    </form>

</body>

</html>
