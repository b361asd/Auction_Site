<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>

<%@ page import="com.b361asd.auction.db.CategoryAndField"%>

<html>

<head>
<meta charset="utf-8">
<title>BuyMe - Post an Offer</title>
<link rel="stylesheet" href="../style.css">
<script>
	function onCategoryChange() {
		let form = document.getElementById('form');
		form.action = "${pageContext.request.contextPath}/user/postOffer.jsp";
		form.submit();
	}
</script>
</head>

<body>

    <%
    Map<String, Object> data =
            CategoryAndField.getCategoryField(
                    DBBase.getStringFromParamMap("categoryName", request.getParameterMap()));
    data.get(CategoryAndField.DATA_CATEGORY_LIST);
    data.get(CategoryAndField.DATA_FIELD_LIST);
    %>

    <%@include file="../header.jsp"%>
    <%@include file="nav.jsp"%>

    <form id="form"
        action="${pageContext.request.contextPath}/user/postOfferResult.jsp"
        method="post">

        <%@include file="../createOfferCommon.jsp"%>

        <input type="submit" value="Submit">
    </form>

</body>

</html>
