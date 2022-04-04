<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>

<%@ page import="com.b361asd.auction.db.Bid"%>

<html>

<head>
<meta charset="utf-8">
<title>BuyMe - View Alert Details</title>
<link rel="stylesheet" href='../style.css' />
</head>

<body>

    <%
    Map data = null;
    TableData dataTable;
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
    }
    if (data == null) {
        data = Bid.searchBid(request.getParameterMap(), null, null);
        request.getSession().setAttribute(IConstant.SESSION_ATTRIBUTE_DATA_MAP, data);
    }
    dataTable = (TableData) (data.get(IConstant.DATA_NAME_DATA));
    request.setAttribute("dataTable", dataTable);
    %>

    <%@include file="../header.jsp"%>
    <%@include file="nav.jsp"%>

    <form id="form-sort" target="_self" method="post">
        <input id="input-sort" type="hidden" name="sort" value="_" />

        <%@include file="../showTableTwo.jsp"%>
    </form>

</body>

</html>
