<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<%@ page import="static rutgers.cs336.db.DBBase.*" %>
<%@ page import="java.util.List" %>
<%@ page import="rutgers.cs336.db.User" %>
<%@ page import="rutgers.cs336.gui.Helper" %>

<html>

<head>
	<meta charset="utf-8">
	<title>BuyMe - Search Offers</title>
	<link rel="stylesheet" href="../style.css?v=1.0"/>

	<script type="text/javascript">
       function onSelectChange() {
           //const form = document.getElementById('form-repBidSearch');
           //form.submit();
       }
	</script>
</head>

<body>

<%
	List lstUser = User.getUserList();
	//
	String userRepBidSearch = getStringFromParamMap("userRepBidSearch", request.getParameterMap());
	if (userRepBidSearch.length() == 0) {
		if (lstUser != null && lstUser.size() > 0) {
			userRepBidSearch = lstUser.get(0).toString();
		}
	}
%>

<%@include file="../header.jsp" %>
<%@include file="nav.jsp" %>

<table>

	<tr>
		<td>Browse</td>
		<td>
			<form action="${pageContext.request.contextPath}/rep/listBid.jsp" method="post">
				<input type="hidden" name="action" value="repBrowseBid"/>
				<input type="submit" value="Browse"/>
			</form>
		</td>
	</tr>

	<tr>
		<td>Search</td>
		<td>
			<form id="form-repBidSearch" action="${pageContext.request.contextPath}/rep/listBid.jsp" method="post">
				<%
					out.println("<input type='hidden' name='action' value='repSearchBid'/>");
					//
					out.println("<table>");
					//
					out.println("<tr>");
					out.println("<td>");
					out.println("Select A User:");
					out.println("</td>");
					out.println("<td>");
					out.println(Helper.getSelection("userRepBidSearch", lstUser.toArray(), userRepBidSearch));
					out.println("</td>");
					out.println("<td>");
					out.println("<input type='submit' value='Search' />");
					out.println("</td>");
					out.println("</tr");
					//
					out.println("</table>");
				%>
			</form>
		</td>
	</tr>

</table>

</body>

</html>
