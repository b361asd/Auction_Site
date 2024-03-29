<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>

<%@ page import="com.b361asd.auction.db.OfferAlert"%>
<%@ page import="com.b361asd.auction.gui.Helper"%>
<%@ page import="com.b361asd.auction.gui.TableData"%>

<html>

<head>
<meta charset="utf-8">
<title>BuyMe - Search Offers</title>
<link rel="stylesheet" href='../style.css' />
</head>

<body>

    <%@include file="../header.jsp"%>
    <%@include file="nav.jsp"%>

    <form id="form-deleteOfferAlert" method="post">
        <input type="hidden" name="action" value="deleteOfferAlert" />
        <input id="input-deleteOfferAlert" type="hidden"
            name="criterionID" value="_" />
    </form>

    <%
    Map data = null;
    TableData dataTable;
    String userID = (String) session.getAttribute(IConstant.SESSION_ATTRIBUTE_USER);
    String action = DBBase.getStringFromParamMap("action", request.getParameterMap());
    if (action.equals("sort")) {
        data = (Map) request.getSession().getAttribute(IConstant.SESSION_ATTRIBUTE_DATA_MAP);
        if (data != null) {
            dataTable = (TableData) (data.get(IConstant.DATA_NAME_DATA));
            if (dataTable != null) {
                String sort = DBBase.getStringFromParamMap("sort", request.getParameterMap());
                dataTable.sortRowPerHeader(sort);
            } else {
                data = null;
            }
        }
    } else {
        if (action.equals("deleteOfferAlert")) {
            OfferAlert.deleteOfferAlert(request.getParameterMap());
        } else if (action.equals("createAlertCriterion")) {
            OfferAlert.doGenerateNewOfferAlertCriterion(userID, request.getParameterMap());
        }
    }
    if (data == null) {
        data = OfferAlert.selectOfferAlert(userID, true);
        request.getSession().setAttribute(IConstant.SESSION_ATTRIBUTE_DATA_MAP, data);
    }
    dataTable = (TableData) (data.get(IConstant.DATA_NAME_DATA));
    %>


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
                    out.println("<td>");
                    out.println(
                            Helper.getButton(
                                    "form-deleteOfferAlert",
                                    "input-deleteOfferAlert",
                                    "" + dataTable.getOneCell(i, 0),
                                    "Delete"));
                    out.println("</td>");
                    out.println(dataTable.printOneRowInTable(i));
                    out.println("</tr>");
                }
            }
            %>
        </tbody>
    </table>

</body>
</html>
