<%@page import="rutgers.cs336.db.CategoryAndField" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<%@ page import="rutgers.cs336.gui.HelperDatetime" %>
<%@ page import="java.util.List" %>
<%@ page import="static rutgers.cs336.db.DBBase.*" %>

<html>

<head>
	<meta charset="utf-8">
	<title>BuyMe - Post an Offer</title>
	<link rel="stylesheet" href="../style.css?v=1.0"/>

	<script type="text/javascript">
       function onCategoryChange(value) {
           value.action = "${pageContext.request.contextPath}/user/postOffer.jsp";
           value.submit();
       }
	</script>
</head>

<body>

<%
	String message = "Welcome to BuyMe!";
	if (session == null) {
	}
	else {
		message = (String) session.getAttribute("message");
		if (message == null) {
			message = "Welcome to BuyMe.";
		}
	}
	//
	String categoryNameFromParam = "CAT=" + getStringFromParamMap("categoryName", request.getParameterMap());
	//String paramMap = getParamMap(request.getParameterMap());
	//
	Map data = CategoryAndField.getCategoryField(getStringFromParamMap("categoryName", request.getParameterMap()));
	List lstCategory = (List) data.get(CategoryAndField.DATA_CATEGORY_LIST);
%>
<h1><%=message%>
</h1>

<%@include file="nav.jsp" %>
<%@include file="../header.jsp" %>

<form action="${pageContext.request.contextPath}/user/postOfferResult.jsp" method="post">

	<select name="categoryName" onchange="onCategoryChange(this.parentElement);">
		<%
			for (Object o : lstCategory) {
				CategoryAndField.Category temp = (CategoryAndField.Category) o;
				out.println("<option " + (temp.isCurr() ? "selected " : "") + "value=\"" + temp.getCategoryName() + "\">" + temp.getCategoryName() + "</option>");
			}
		%>
	</select><br>

	<div>Initial Price<input type="NUMBER" name="initPrice" min="1"></div>
	<div>Price Increment<input type="Number" name="increment" min="1"></div>
	<div>Minimal Price<input type="Number" name="minPrice"></div>
	<div><select name="conditionCode" onchange="onCategoryChange(this.parentElement);">
		<option value='conditionCode_1'>New</option>
		<option value='conditionCode_2'>Like New</option>
		<option value='conditionCode_3'>Manufacturer Refurbished</option>
		<option value='conditionCode_4'>Seller Refurbished</option>
		<option value='conditionCode_5'>Used</option>
		<option value='conditionCode_6'>For parts or Not Working</option>
	</select></div>
	<div>Item Description<input type="text" name="description"></div>

	<%
		List lstField = (List) data.get(CategoryAndField.DATA_FIELD_LIST);
		for (Object o : lstField) {
			String fieldName = ((CategoryAndField.Field) o).getFieldName();
			int    fieldID   = ((CategoryAndField.Field) o).getFieldID();
			out.println("<div class='allField'>" + fieldName + "<input type = \"text\" name = \"fieldID_" + fieldID + "\" / ></div><br >");
		}
	%>

	<%
		out.println("<div>Auction Days<input type='datetime-local' name='endDate' value='" + HelperDatetime.getDatetimeSZ(7) + "' /></div>");
	%>

	<input type="submit" value="Submit">
</form>

</body>

</html>
