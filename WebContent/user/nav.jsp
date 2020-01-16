<table>
    <thead>
    <tr>
        <th><A href="${pageContext.request.contextPath}/user/listAlert.jsp">Alerts</A></th>
        <th><A href="${pageContext.request.contextPath}/user/listMyOffer.jsp">My Offers</A></th>
        <th><A href="${pageContext.request.contextPath}/user/listMyBid.jsp">My Bids</A></th>
        <th><A href="${pageContext.request.contextPath}/user/listMyTrade.jsp">My Trades</A></th>
        <th><A href="${pageContext.request.contextPath}/user/listActivity.jsp">User Activity</A></th>
        <th><A href="${pageContext.request.contextPath}/user/postOffer.jsp">Post an Offer</A></th>
        <th><A href="${pageContext.request.contextPath}/user/searchOffer.jsp">Search Offer</A></th>
        <th><A href="${pageContext.request.contextPath}/user/createOfferAlertCriterion.jsp">Setup Interest</A></th>
        <th><A href="${pageContext.request.contextPath}/user/listOfferAlertCriterion.jsp">My Interests</A></th>
        <th><A href="${pageContext.request.contextPath}/user/postQuestion.jsp">Post Question</A></th>
        <th><A href="${pageContext.request.contextPath}/user/viewAnswer.jsp">View Answers</A></th>
        <th><A href="${pageContext.request.contextPath}/logout">Logout</A></th>
    </tr>
    </thead>
</table>

<br/>
<br/>


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


<form id="form-deleteAlert" method="post">
    <input type="hidden" name="action" value="deleteAlert"/>
    <input id="input-deleteAlert" type="hidden" name="alertID" value="_"/>
</form>
<form id="form-viewAlertDetail" action="${pageContext.request.contextPath}/user/viewAlertDetail.jsp" method="post">
    <input type="hidden" name="action" value="viewAlertDetail"/>
    <input id="input-viewAlertDetail" type="hidden" name="offerIDbidID" value="_"/>
</form>

