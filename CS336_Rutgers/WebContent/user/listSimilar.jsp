<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<%@ page import="rutgers.cs336.db.Offer" %>
<%@ page import="rutgers.cs336.gui.Helper" %>
<%@ page import="rutgers.cs336.gui.TableData" %>
<%@ page import="java.util.List" %>
<%@ page import="static rutgers.cs336.db.DBBase.*" %>

<html>

<head>
	<meta charset="utf-8">
	<title>BuyMe - Search Offers</title>
	<link rel="stylesheet" href="../style.css?v=1.0"/>

	<script type="text/javascript">
       function onCategoryChange(value) {
           value.action = "${pageContext.request.contextPath}/user/searchOffer.jsp";	// Post to itself
           value.submit();
       }

       function onIntegerOPChange(value) {
           //nextElementSibling
       }
	</script>
</head>

<body>

<%
	TableData dataTable;
	//
	Map data = Offer.doSearchSimilar(request.getParameterMap());
	//
	dataTable = (TableData) (data.get(DATA_NAME_DATA));
	List lstHeader = dataTable.getLstHeader();
	List lstRows = dataTable.getLstRows();
	int[] colSeq = dataTable.getColSeq();
%>

<%@include file="../header.jsp" %>
<%@include file="userNav.jsp" %>

<table>
	<thead>
	<tr>
		<%
			out.println(Helper.printHeaderForTable(lstHeader, colSeq));
		%>
	</tr>
	</thead>

	<tbody>
	<%
		if (lstRows != null) {
			for (Object oneRow : lstRows) {
				List lstOneRow = (List) oneRow;
				//
				out.println("<tr>");
				//
				out.println(Helper.printOneRowInTable(lstOneRow, colSeq));
				//
				out.println("</tr>");
			}
		}
	%>
	</tbody>

</table>

</body>

</html>
