<table>
   <thead>
   <tr>
      <th><A href="${pageContext.request.contextPath}/main/webapp/user/listAlert.jsp">Alerts</A></th>
      <th><A href="${pageContext.request.contextPath}/main/webapp/user/listMyOffer.jsp">My Offers</A></th>
      <th><A href="${pageContext.request.contextPath}/main/webapp/user/listMyBid.jsp">My Bids</A></th>
      <th><A href="${pageContext.request.contextPath}/main/webapp/user/listMyTrade.jsp">My Trades</A></th>
      <th><A href="${pageContext.request.contextPath}/main/webapp/user/listActivity.jsp">User Activity</A></th>
      <th><A href="${pageContext.request.contextPath}/main/webapp/user/postOffer.jsp">Post an Offer</A></th>
      <th><A href="${pageContext.request.contextPath}/main/webapp/user/searchOffer.jsp">Search Offer</A></th>
      <th><A href="${pageContext.request.contextPath}/main/webapp/user/createOfferAlertCriterion.jsp">Setup Interest</A></th>
      <th><A href="${pageContext.request.contextPath}/main/webapp/user/listOfferAlertCriterion.jsp">My Interests</A></th>
      <th><A href="${pageContext.request.contextPath}/main/webapp/user/postQuestion.jsp">Post Question</A></th>
      <th><A href="${pageContext.request.contextPath}/main/webapp/user/viewAnswer.jsp">View Answers</A></th>
      <th><A href="${pageContext.request.contextPath}/main/webapp/logout">Logout</A></th>
   </tr>
   </thead>
</table>

<br/>
<br/>


<form id="form-id-doBid" action="${pageContext.request.contextPath}/main/webapp/user/postBid.jsp" method="post">
   <input id="input-id-doBid" type="hidden" name="offerid" value="_"/>
</form>
<form id="form-id-listBid" action="${pageContext.request.contextPath}/main/webapp/user/listBidForOffer.jsp" method="post">
   <input id="input-id-listBid" type="hidden" name="offeridcategoryname" value="_"/>
</form>


<form id="form-id-listSimilar" action="${pageContext.request.contextPath}/main/webapp/user/listOffer.jsp" method="post">
   <input type="hidden" name="action" value="listSimilar"/>
   <input id="input-id-listSimilar" type="hidden" name="offeridcategorynameconditioncode" value="_"/>
</form>


<form id="form-deleteAlert" method="post">
   <input type="hidden" name="action" value="deleteAlert"/>
   <input id="input-deleteAlert" type="hidden" name="alertID" value="_"/>
</form>
<form id="form-viewAlertDetail" action="${pageContext.request.contextPath}/main/webapp/user/viewAlertDetail.jsp" method="post">
   <input type="hidden" name="action" value="viewAlertDetail"/>
   <input id="input-viewAlertDetail" type="hidden" name="offerIDbidID" value="_"/>
</form>

