<%@ page import="rutgers.cs336.db.GetCategoryField" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="static rutgers.cs336.db.CreateOffer.PREFIX_CATEGORY_NAME" %>
<%@ page import="static rutgers.cs336.db.DBBase.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>BuyMe - Post an Offer</title>
	<link rel="stylesheet" href="style.css?v=1.0"/>

	<script type="text/javascript">
       function onCategoryChange(value) {
           value.action = "${pageContext.request.contextPath}/postOffer.jsp";
           value.submit();
       }
	</script>

	<%@ page language="java"
				contentType="text/html; charset=windows-1256"
				pageEncoding="windows-1256"
	%>
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
	String categoryNameFromParam = "CAT=" + getStringFromParamMap(PREFIX_CATEGORY_NAME, request.getParameterMap());
	String paramMap = getParamMap(request.getParameterMap());
	//
	Map data = GetCategoryField.getCategoryField(request.getParameterMap());
%>
<h1><%=message%>
</h1>
<h1><%=categoryNameFromParam%>
</h1>
<h1><%=paramMap%>
</h1>

<form action="${pageContext.request.contextPath}/listOffer.jsp" method="post">

	<select name="categoryName" onchange="onCategoryChange(this.parentElement);">
		<%
			List lstCategory = (List) data.get(GetCategoryField.DATA_CATEGORY_LIST);
			for (Object o : lstCategory) {
				GetCategoryField.Category temp = (GetCategoryField.Category) o;
				out.println(
						  "<option " + (temp.isCurr() ? "selected " : "") + "value=\"" + temp.getCategoryName() + "\">" + temp
									 .getCategoryName() + "</option>");
			}
		%>
	</select><br>

	<%
		List lstField = (List) data.get(GetCategoryField.DATA_FIELD_LIST);
		for (Object o : lstField) {
			String categoryName = ((GetCategoryField.Field) o).getCategoryName();
			String fieldName    = ((GetCategoryField.Field) o).getFieldName();
			int    fieldID      = ((GetCategoryField.Field) o).getFieldID();
			out.println(
					  "<div class=\"allField " + categoryName + "\" >" + fieldName + "<input type = \"text\" name = \"fieldID_" + fieldID + "\" / ></div><br >");
		}
	%>

	<div>Auction Days<input type="number" name="auction_days" min="1" max="30"></div>
	<div>Reserved Price<input type="number" name="reserved_price" min="0.01"></div>
	<div>Item Description<input type="text" name="description"></div>

	<input type="submit" value="submit">
</form>

<%@include file="userNav.jsp" %>

</body>

</html>
