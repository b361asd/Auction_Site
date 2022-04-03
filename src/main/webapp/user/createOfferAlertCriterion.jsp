<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>

<%@ page import="com.b361asd.auction.db.CategoryAndField"%>

<html>

<head>
<meta charset="utf-8">
<title>BuyMe - Generate Alert</title>
<link rel="stylesheet" href="../style.css">
<script>
	function onCategoryChange() {
		const form = document.getElementById('form');
		form.action = "${pageContext.request.contextPath}/user/createOfferAlertCriterion.jsp";
		form.submit();
	}
</script>
</head>

<body>

    <%
    CategoryAndField
            .getCategoryField(DBBase.getListOfStringsFromParamMap("categoryName", 1, request.getParameterMap(), ""));
    %>

    <%@include file="../header.jsp"%>
    <%@include file="nav.jsp"%>

    <form id="form" action="listOfferAlertCriterion.jsp" method="post">
        <%@include file="../searchOfferCommon.jsp"%>

        <input type="hidden" name="action" value="createAlertCriterion" />
        <input type="submit" value="Create Alert Criterion">
    </form>

</body>

</html>
