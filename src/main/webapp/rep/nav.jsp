<table>
   <thead>
   <tr>
      <th><A href="repSearchOffer.jsp">Manager Offers</A></th>
      <th><A href="repSearchBid.jsp">Manage Bids</A></th>
      <th><A href="answerQuestion.jsp">Answer Questions</A></th>
      <th><A href="listUser.jsp">Manage Users</A></th>
      <th><A href="${pageContext.request.contextPath}/logout">Logout</A></th>
   </tr>
   </thead>
</table>

<br/>
<br/>

<form id="form-id-cancelOffer" action="cancelOffer.jsp" method="post">
   <input type="hidden" name="action" value="cancelOffer"/>
   <input id="input-id-cancelOffer" type="hidden" name="offerid" value="_"/>
</form>

<form id="form-id-modifyOffer" action="modifyOffer.jsp" method="post">
   <input type="hidden" name="action" value="modifyOffer"/>
   <input id="input-id-modifyOffer" type="hidden" name="offeridcategorynameuser" value="_"/>
</form>

<form id="form-id-cancelBid" action="cancelBid.jsp" method="post">
   <input type="hidden" name="action" value="cancelBid"/>
   <input id="input-id-cancelBid" type="hidden" name="bidID" value="_"/>
</form>

<form id="form-id-modifyBid" action="modifyBid.jsp" method="post">
   <input type="hidden" name="action" value="startModifyBid"/>
   <input id="input-id-modifyBid" type="hidden" name="bidIDofferIDBuyer" value="_"/>
</form>
