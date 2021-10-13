<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>

<html>

<head>
<meta charset="utf-8">
<title>BuyMe - Post a Question</title>
<link rel="stylesheet" href='../style.css' />
</head>

<body>

    <%@include file="../header.jsp"%>
    <%@include file="nav.jsp"%>

    <form
        action="${pageContext.request.contextPath}/user/postQuestionResult.jsp"
        method="post">

        <label for="question">Ask a Question:</label>

        <textarea id="question" name="question" rows="5" cols="33"></textarea>

        <input type="submit" value="Submit">
    </form>

</body>

</html>
