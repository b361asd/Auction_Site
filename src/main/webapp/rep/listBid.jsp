<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>

<%@ page import="com.b361asd.auction.db.Bid"%>
<%@ page import="com.b361asd.auction.db.DBBase"%>
<%@ page import="com.b361asd.auction.gui.TableData"%>
<%@ page import="java.util.Objects"%>

<html>

<head>
<meta charset="utf-8">
<title>BuyMe - List Bids</title>
<link rel="stylesheet" href='../style.css' />
</head>

<body>

    <%@include file="../header.jsp"%>
    <%@include file="nav.jsp"%>

    <form id="form-sort" target="_self" method="post">
        <input id="input-sort" type="hidden" name="sort" value="_" />

        <%
        Map data;
        TableData dataTable = null;
        //
        String action = DBBase.getStringFromParamMap("action", request.getParameterMap());
        if (action.equals("repSearchBid")) {
            data = Bid.searchBid(request.getParameterMap(), null, null);
            request.getSession().setAttribute(IConstant.SESSION_ATTRIBUTE_DATA_MAP, data);
        } else if (action.equals("repBrowseBid")) {
            data = Bid.searchBid(request.getParameterMap(), null, null);
            request.getSession().setAttribute(IConstant.SESSION_ATTRIBUTE_DATA_MAP, data);
        } else {
            data = (Map) request.getSession().getAttribute(IConstant.SESSION_ATTRIBUTE_DATA_MAP);
            if (data != null) {
                dataTable = (TableData) (data.get(IConstant.DATA_NAME_DATA));
            }
            //
            if (dataTable == null) {
            } else {
                String sort = DBBase.getStringFromParamMap("sort", request.getParameterMap());
                dataTable.sortRowPerHeader(sort);
            }
        }
        //
        //
        dataTable = (TableData) (Objects.requireNonNull(data).get(IConstant.DATA_NAME_DATA));
        //
        request.setAttribute("dataTable", dataTable);
        %>

        <%@include file="../showTableTwo.jsp"%>
    </form>
</body>

</html>
