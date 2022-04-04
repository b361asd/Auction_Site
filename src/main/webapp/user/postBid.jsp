<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>

<%@ page import="com.b361asd.auction.db.Offer"%>
<%@ page import="com.b361asd.auction.gui.TableData"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.text.MessageFormat"%>
<%@ page import="java.util.Optional"%>

<html>

<head>
<meta charset="utf-8">
<title>BuyMe - Post a Bid</title>
<link rel="stylesheet" href='../style.css' />
</head>

<body>

    <%
    String offerid = DBBase.getStringFromParamMap("offerid", request.getParameterMap());
    Map data = Offer.doSearchOfferByID(offerid, false);
    request.getSession().setAttribute(IConstant.SESSION_ATTRIBUTE_DATA_MAP, data);
    TableData dataTable = (TableData) (data.get(IConstant.DATA_NAME_DATA));
    %>


    <%@include file="../header.jsp"%>
    <%@include file="nav.jsp"%>

    <table>
        <tr>
            <%
            out.println(dataTable.printHeaderForTable());
            %>
        </tr>
        <%
        if (dataTable.rowCount() > 0) {
            for (int i = 0; i < dataTable.rowCount(); i++) {
                out.println("<tr>");
                out.println(dataTable.printOneRowInTable(i));
                out.println("</tr>");
            }
        }
        %>
    </table>

    <br />
    <br />

    <form
        action="${pageContext.request.contextPath}/user/postBidResult.jsp"
        method="post">
        <%
        out.println("<input type='hidden' name='offerId' value='" + offerid + "'/>");
        BigDecimal initPrice = (BigDecimal) dataTable.getOneCell(0, 5);
        BigDecimal increment = (BigDecimal) dataTable.getOneCell(0, 6);
        BigDecimal curPrice = (BigDecimal) dataTable.getOneCell(0, 11);
        BigDecimal minPrice =
                Optional.ofNullable(curPrice).map(price -> price.add(increment)).orElse(initPrice);
        out.println(
                MessageFormat.format(
                        "<div>Price(min {0})<input type=''number'' name=''price'' min=''{1}'' VALUE=''{2}''></div>",
                        minPrice, minPrice, minPrice));
        %>

        <div>
            Auto Rebid Limit<label> <input type="number"
                name="autoRebidLimit">
            </label>
        </div>
        <input type="submit" value="Submit">
    </form>

</body>

</html>
