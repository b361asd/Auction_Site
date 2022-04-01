<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>

<%@ page import="com.b361asd.auction.gui.TableData"%>
<%@ page import="com.b361asd.auction.db.Offer"%>
<%@page import="com.b361asd.auction.db.DBBase"%>

<html>
<head>
<meta charset="utf-8">
<title>BuyMe - Search Offers</title>
<link rel="stylesheet" href='../style.css' />
</head>

<body>

    <%
    Map data = null;
    TableData dataTable;
    //
    String userID = (String) session.getAttribute(IConstant.SESSION_ATTRIBUTE_USER);
    //
    String action = DBBase.getStringFromParamMap("action", request.getParameterMap());
    if (action.equalsIgnoreCase("sort")) {
        data = (Map) request.getSession().getAttribute(IConstant.SESSION_ATTRIBUTE_DATA_MAP);
        if (data != null) {
            dataTable = (TableData) (data.get(IConstant.DATA_NAME_DATA));
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
        data = Offer.doSearchMyOffer(userID);
        request.getSession().setAttribute(IConstant.SESSION_ATTRIBUTE_DATA_MAP, data);
    }
    //
    dataTable = (TableData) (data.get(IConstant.DATA_NAME_DATA));
    //
    request.setAttribute("dataTable", dataTable);
    %>

    <%@include file="../header.jsp"%>
    <%@include file="nav.jsp"%>

    <%@include file="../listOfferCommon.jsp"%>

</body>

</html>
