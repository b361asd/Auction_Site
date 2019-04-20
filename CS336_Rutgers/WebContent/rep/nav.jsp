<table>
	<thead>
	<tr>
		<th><A href="${pageContext.request.contextPath}/rep/repSearchOffer.jsp">Manager Offers</A></th>
		<th><A href="${pageContext.request.contextPath}/rep/repSearchBid.jsp">Manage Bids</A></th>
		<th><A href="${pageContext.request.contextPath}/rep/answerQuestion.jsp">Answer Questions</A></th>
		<th><A href="${pageContext.request.contextPath}/rep/listUser.jsp">Manage Users</A></th>
		<th><A href="${pageContext.request.contextPath}/logout">Logout</A></th>
	</tr>
	</thead>
</table>

<br/>
<br/>

<form id="form-id-cancelOffer" action="${pageContext.request.contextPath}/rep/cancelOffer.jsp" method="post">
	<input type="hidden" name="action" value="cancelOffer"/>
	<input id="input-id-cancelOffer" type="hidden" name="offerid" value="_"/>
</form>

<form id="form-id-modifyOffer" action="${pageContext.request.contextPath}/rep/modifyOffer.jsp" method="post">
	<input type="hidden" name="action" value="modifyOffer"/>
	<input id="input-id-modifyOffer" type="hidden" name="offeridcategorynameuser" value="_"/>
</form>

<form id="form-id-cancelBid" action="${pageContext.request.contextPath}/rep/cancelBid.jsp" method="post">
	<input type="hidden" name="action" value="cancelBid"/>
	<input id="input-id-cancelBid" type="hidden" name="bidID" value="_"/>
</form>

<form id="form-id-modifyBid" action="${pageContext.request.contextPath}/rep/modifyBid.jsp" method="post">
	<input type="hidden" name="action" value="modifyBid"/>
	<input id="input-id-modifyBid" type="hidden" name="bidIDofferIDBuyer" value="_"/>
</form>
