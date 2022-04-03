<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>

<%@ page import="com.b361asd.auction.db.CategoryAndField"%>
<%@ page import="com.b361asd.auction.db.DBBase"%>

<html>

<head>
<meta charset="utf-8">
<title>BuyMe - Search Offers</title>
<link rel="stylesheet" href='../style.css' />

<script type="text/javascript">
	function onCategoryChange() {
		const form = document.getElementById('form');
		form.action = "${pageContext.request.contextPath}/main/rep/repSearchOffer.jsp";
		form.submit();
	}
</script>
</head>

<body>

    <%
    CategoryAndField.getCategoryField(
            DBBase.getListOfStringsFromParamMap(
                    "categoryName", 1, request.getParameterMap(), ""));
    %>

    <%@include file="../header.jsp"%>
    <%@include file="nav.jsp"%>

    <form action="${pageContext.request.contextPath}/rep/listOffer.jsp"
        method="post">
        <input type="hidden" name="action" value="browseOffer" />
        <table>
            <tbody>
                <tr>
                    <td>Browse</td>
                    <td><input type="submit" value="Browse"></td>
                </tr>
            </tbody>
        </table>
    </form>

    <br />
    <br />

    <form id="form"
        action="${pageContext.request.contextPath}/rep/listOffer.jsp"
        method="post">
        <%@include file="../searchOfferCommon.jsp"%>

        <input type="hidden" name="action" value="searchOffer" /> <input
            type="submit" value="Search" />
    </form>

</body>

</html>
