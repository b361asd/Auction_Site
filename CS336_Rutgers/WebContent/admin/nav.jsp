<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<html>
<head>
	<meta charset="utf-8">
	<title>BuyMe - Admin Navigation</title>
	<link rel="stylesheet" href="../style.css?v=1.0"/>
</head>
<body>

<table>
	<thead>
	<tr>
		<th><A href="${pageContext.request.contextPath}/admin/ReportSalesByBuyer.jsp">Sales By Buyer</A></th>
		<th><A href="${pageContext.request.contextPath}/admin/ReportSalesBySeller.jsp">Sales By Seller</A></th>
		<th><A href="${pageContext.request.contextPath}/admin/ReportSalesByUser.jsp">Sales By User</A></th>
		<th><A href="${pageContext.request.contextPath}/admin/registerRep.jsp">Register Rep</A></th>
		<th><A href="${pageContext.request.contextPath}/admin/listUser.jsp">Manage Reps</A></th>
		<th><A href="${pageContext.request.contextPath}/logout">Logout</A></th>
	</tr>
	</thead>
</table>


<h3>
</h3>

</body>
</html>
