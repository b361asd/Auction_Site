<%@ page import="rutgers.cs336.db.GetCategoryField" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>BuyMe - Post an Offer</title>
	<link rel="stylesheet" href="style.css?v=1.0"/>

	<script type="text/javascript">
       function onCategoryChange(value) {
           let x = document.getElementsByClassName("allField");
           for (let i = 0; i < x.length; i++) {
               if (x[i].className.indexOf(value) >= 0) {
                   x[i].style.display = "block";
               } else {
                   x[i].style.display = "none";
               }
           }
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

	Map data = GetCategoryField.getCategoryField();
%>
<h1><%=message%>
</h1>

<form action="${pageContext.request.contextPath}/register" method="post">

	<select id="category_select" onchange="onCategoryChange(this.value);">
		<%
			List lstCategory = (List) data.get(GetCategoryField.DATA_CATEGORY_LIST);
			for (Object o : lstCategory) {
				out.println("<option value=\"" + o + "\">" + o + "</option>");
			}
		%>
	</select><br>

	<%
		List lstField = (List) data.get(GetCategoryField.DATA_FIELD_LIST);
		for (Object o : lstField) {
			String categoryName = ((GetCategoryField.Field) o).getCategoryName();
			String fieldName    = ((GetCategoryField.Field) o).getFieldName();
			out.println(
					  "<div class= \"allField " + categoryName + "\" >" + fieldName + "<input type = \"text\" name = \"" + fieldName + "\" / ></div><br >");
		}
	%>

	<input type="submit" value="submit">
</form>

<%@include file="userNav.jsp" %>

</body>

</html>
