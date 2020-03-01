<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>

<%@ page import="static b361asd.auction.db.DBBase.*" %>

<html>

<head>
   <meta charset="utf-8">
   <title>BuyMe - Generate Alert</title>
   <%--	<link rel="stylesheet" href='${pageContext.request.contextPath}/style.css'/>--%>
   <link rel="stylesheet" href="../style.css">

   <script type="text/javascript">
       function onCategoryChange() {
           const form = document.getElementById('form');
           form.action = "${pageContext.request.contextPath}/user/createOfferAlertCriterion.jsp";
           form.submit();
       }
   </script>
</head>

<body>

<%
   CategoryAndField.getCategoryField(getListOfStringsFromParamMap("categoryName", 1, request.getParameterMap(), ""));
%>

<%@include file="../header.jsp" %>
<%@include file="nav.jsp" %>

<%--<form id="form" action="${pageContext.request.contextPath}/listOfferAlertCriterion.jsp" method="post">--%>
<form id="form" action="listOfferAlertCriterion.jsp" method="post">

   <%@include file="../searchOfferCommon.jsp" %>

   <input type="hidden" name="action" value="createAlertCriterion"/>
   <input type="submit" value="Create Alert Criterion">
</form>

</body>

</html>
