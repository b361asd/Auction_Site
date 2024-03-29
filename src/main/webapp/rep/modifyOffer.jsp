<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>

<%@ page import="com.b361asd.auction.db.CategoryAndField"%>
<%@ page import="com.b361asd.auction.db.DBBase"%>
<%@ page import="com.b361asd.auction.db.Offer"%>
<%@ page import="com.b361asd.auction.gui.Helper"%>
<%@ page import="com.b361asd.auction.gui.TableData"%>

<html>

<head>
<meta charset="utf-8">
<title>BuyMe - Modify Offers</title>
<link rel="stylesheet" href='../style.css' />
</head>

<body>
    <%
    TableData tableData;
    String offerIDCategoryNameUser =
            DBBase.getStringFromParamMap("offeridcategorynameuser", request.getParameterMap());
    String[] temps = offerIDCategoryNameUser.split(",");
    String action = DBBase.getStringFromParamMap("action", request.getParameterMap());
    Map<String, Object> dataModify = null;
    if (action.equals("modifyOffer")) {
        dataModify = Offer.doCreateOrModifyOffer(temps[2], request.getParameterMap(), false);
    }
    Map<String, Object> categoryAndField = CategoryAndField.getCategoryField(temps[1]);
    categoryAndField.get(CategoryAndField.DATA_CATEGORY_LIST);
    categoryAndField.get(CategoryAndField.DATA_FIELD_LIST);
    Map<String, Object> data = Offer.doSearchOfferByID(temps[0], true);
    request.getSession().setAttribute(IConstant.SESSION_ATTRIBUTE_DATA_MAP, data);
    tableData = (TableData) (data.get(IConstant.DATA_NAME_DATA));
    if (dataModify != null) {
        boolean _status = Helper.getStatus(dataModify);
        Helper.getMessage(dataModify);
        if (!_status) {
            Helper.setStatus(data, false);
            Helper.appendMessage(data, Helper.getMessage(dataModify));
        }
    }
    %>


    <%@include file="../header2.jsp"%>
    <%@include file="nav.jsp"%>

    <table>
        <thead>
            <tr>
                <%
                out.println(tableData.printHeaderForTable());
                %>
            </tr>
        </thead>
        <tbody>
            <%
            if (tableData.rowCount() > 0) {
                for (int i = 0; i < tableData.rowCount(); i++) {
                    out.println("<tr>");
                    out.println(tableData.printOneRowInTable(i));
                    out.println("</tr>");
                }
            }
            %>
        </tbody>
    </table>

    <form id="form" method="post">
        <%@include file="../createOfferCommon.jsp"%>

        <input type="submit" value="Submit">
    </form>

</body>

</html>
