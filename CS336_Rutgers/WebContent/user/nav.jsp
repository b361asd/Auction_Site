<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<table>
	<thead>
	<tr>
		<th><A href="${pageContext.request.contextPath}/user/listActivity.jsp">User Activity</A></th>
		<th><A href="${pageContext.request.contextPath}/user/listMyBid.jsp">My Bids</A></th>
		<th><A href="${pageContext.request.contextPath}/user/postOffer.jsp">Post an Offer</A></th>
		<th><A href="${pageContext.request.contextPath}/user/searchOffer.jsp">Search Offer</A></th>
		<th><A href="${pageContext.request.contextPath}/user/createOfferAlertCriterion.jsp">Setup Offer Alert</A></th>
		<th><A href="${pageContext.request.contextPath}/user/listOfferAlertCriterion.jsp">Manage OfferAlert Settings</A></th>
		<th><A href="${pageContext.request.contextPath}/user/listAlert.jsp">Alerts</A></th>
		<th><A href="${pageContext.request.contextPath}/user/postQuestion.jsp">Post Question</A></th>
		<th><A href="${pageContext.request.contextPath}/user/viewAnswer.jsp">Search Question</A></th>
		<th><A href="${pageContext.request.contextPath}/logout">Logout</A></th>
	</tr>
	</thead>
</table>

<form id="form-id-doBid" action="${pageContext.request.contextPath}/user/postBid.jsp" method="post">
	<input id="input-id-doBid" type="hidden" name="offerid" value="_"/>
</form>

<form id="form-id-listBid" action="${pageContext.request.contextPath}/user/listBidForOffer.jsp" method="post">
	<input id="input-id-listBid" type="hidden" name="offeridcategoryname" value="_"/>
</form>

<form id="form-id-listSimilar" action="${pageContext.request.contextPath}/user/listOffer.jsp" method="post">
	<input type="hidden" name="action" value="listSimilar"/>
	<input id="input-id-listSimilar" type="hidden" name="offeridcategorynameconditioncode" value="_"/>
</form>
