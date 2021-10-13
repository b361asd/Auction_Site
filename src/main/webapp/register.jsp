<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>

<html>

<head>
<meta charset="utf-8">
<title>BuyMe - Register</title>
<link rel="stylesheet" href='style.css' />
</head>

<body>

    <%@include file="header.jsp"%>

    <form action="${pageContext.request.contextPath}/home" method="post">

        <%@include file="registerCommon.jsp"%>

    </form>

</body>

</html>
