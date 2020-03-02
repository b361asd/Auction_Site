package main.java.auction.db;

import main.java.auction.gui.Helper;
import main.java.auction.gui.TableData;
import main.java.auction.servlet.IConstant;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

public class Bid extends DBBase {

   private static List  lstHeader_bid  = Arrays.asList("bidID", "offerID", "buyer", "price", "autoRebidLimit", "bidDate");
   private static int[] colSeq_bid     = {2, 3, 5};
   private static int[] colSeq_bid_add = {2, 3, 4, 5};

   /**
    * Search bid and their relevant offers
    *
    * @param parameters   Map containing parameters for other conditions
    * @param userActivity User ID for my activity screen
    * @param userMyBid    User ID for my bid screen
    * @return Data for GUI rendering
    */
   public static Map searchBid(Map<String, String[]> parameters, String userActivity, String userMyBid) {
      String _offeridcategoryname = getStringFromParamMap("offeridcategoryname", parameters);
      String _offerIDbidID        = getStringFromParamMap("offerIDbidID", parameters);                  //viewAlertDetail
      String _action              = getStringFromParamMap("action", parameters);
      String _bidIDofferIDBuyer   = getStringFromParamMap("bidIDofferIDBuyer", parameters);
      //
      boolean _listActivity    = false;
      boolean _listMyBid       = false;
      boolean _listBidForOffer = false;
      boolean _viewAlertDetail = false;
      boolean _listBid_Search  = false;
      boolean _listBid_Browse  = false;
      boolean _modifyBid       = false;
      //
      if (userActivity != null && userActivity.length() > 0) {
         _listActivity = true;
      }
      else if (userMyBid != null && userMyBid.length() > 0) {
         _listMyBid = true;
      }
      else if (_offeridcategoryname.length() > 0) {
         _listBidForOffer = true;
      }
      else if (_offerIDbidID.length() > 0) {
         _viewAlertDetail = true;
      }
      else if (_action.equals("repSearchBid")) {
         _listBid_Search = true;
      }
      else if (_action.equals("repBrowseBid")) {
         _listBid_Browse = true;
      }
      else if (!_bidIDofferIDBuyer.equals("")) {
         _modifyBid = true;
      }
      //
      String      sql;
      String      bidIDStandout = null;
      String      userStandout  = null;
      Set<String> offerIDSet    = new HashSet<>();            //offerID set
      if (_listActivity) {                                          //User: ListActivity.jsp
         StringBuilder sb = FormatterBidQuery.buildQueryUserActivity(userActivity);
         //
         sql = sb.toString();
         userStandout = userActivity;
      }
      else if (_listMyBid) {                                          //User: listMyBid.jsp
         StringBuilder sb = FormatterBidQuery.initQuerySearchAll();
         FormatterOfferQuery.addCondition(sb, "buyer", OP_SZ_EQUAL, userMyBid, null);
         FormatterOfferQuery.addCondition(sb, "status", OP_INT_EQUAL, "1", null);
         //
         sql = sb.toString();
         userStandout = userMyBid;
      }
      else if (_listBidForOffer) {
         StringBuilder sb = FormatterBidQuery.initQuerySearchAll();
         //
         String[] temps = _offeridcategoryname.split(",");
         //
         FormatterOfferQuery.addCondition(sb, "o.offerID", OP_SZ_EQUAL, temps[0], null);
         //
         sql = sb.toString();
         offerIDSet.add(temps[0]);
      }
      else if (_listBid_Search) {                                                         //repSearchBid for cancel and modify, should be active Offer
         StringBuilder sb = FormatterBidQuery.initQuerySearchAll();
         //
         {
            String userRepBidSearch = getStringFromParamMap("userRepBidSearch", parameters);
            FormatterOfferQuery.addCondition(sb, "buyer", OP_SZ_EQUAL, userRepBidSearch, null);
         }
         //
         FormatterOfferQuery.addCondition(sb, "status", OP_INT_EQUAL, "1", null);
         //
         sql = sb.toString();
      }
      else if (_listBid_Browse) {                                                      //repSearchBid for cancel and modify, should be active Offer
         StringBuilder sb = FormatterBidQuery.initQuerySearchAll();
         //
         FormatterOfferQuery.addCondition(sb, "status", OP_INT_EQUAL, "1", null);
         //
         sql = sb.toString();
      }
      else if (_viewAlertDetail) {                                                            //user: listAlert.jsp(offerIDbidID) -> viewAlertDetail.jsp
         StringBuilder sb = FormatterBidQuery.initQuerySearchAll();
         //
         String[] temps = _offerIDbidID.split(",");
         //
         FormatterOfferQuery.addCondition(sb, "o.offerID", OP_SZ_EQUAL, temps[0], null);
         offerIDSet.add(temps[0]);
         //
         if (temps.length >= 2) {
            bidIDStandout = temps[1];
         }
         //
         sql = sb.toString();
         offerIDSet.add(temps[0]);
      }
      else if (_modifyBid) {                                                                  //Rep: ListBid.jsp(bidIDofferIDBuyer) -> modifyBid.jsp
         StringBuilder sb = FormatterBidQuery.initQuerySearchAll();
         //
         String[] temps = _bidIDofferIDBuyer.split(",");
         FormatterOfferQuery.addCondition(sb, "o.offerID", OP_SZ_EQUAL, temps[1], null);
         bidIDStandout = temps[0];
         //
         sql = sb.toString();
         offerIDSet.add(temps[1]);
      }
      else {
         StringBuilder sb = FormatterBidQuery.initQuerySearchAll();
         //
         bidIDStandout = null;
         {
            String bidID = getStringFromParamMap("bidID", parameters);
            FormatterOfferQuery.addCondition(sb, "bidID", OP_SZ_EQUAL, bidID, null);
         }
         //
         {
            String offerID = getStringFromParamMap("offerID", parameters);
            FormatterOfferQuery.addCondition(sb, "o.offerID", OP_SZ_EQUAL, offerID, null);
         }
         //
         sql = sb.toString();
      }
      //
      Map               output  = new HashMap();
      Map<String, List> tempMap = new HashMap<>();            //offerID -> Bids(in List)
      //
      Connection con       = null;
      Statement  statement = null;
      try {
         con = getConnection();
         //
         statement = con.createStatement();
         //
         ResultSet rs = statement.executeQuery(sql);
         //
         while (rs.next()) {
            Object bidID          = rs.getObject(1);
            Object offerID        = rs.getObject(2);
            Object buyer          = rs.getObject(3);
            Object price          = rs.getObject(4);
            Object autoRebidLimit = rs.getObject(5);
            Object bidDate        = rs.getObject(6);
            //
            List lstRows    = tempMap.computeIfAbsent(offerID.toString(), k -> new ArrayList());
            List currentRow = new LinkedList();
            lstRows.add(currentRow);
            //
            currentRow.add(bidID);
            currentRow.add(offerID);
            currentRow.add(buyer);
            currentRow.add(price);
            currentRow.add(autoRebidLimit);
            currentRow.add(bidDate);
            //
            offerIDSet.add(offerID.toString());
         }
         //
         Map       offerMap;
         TableData dataTableOffer = null;
         if (_listActivity) {
            offerMap = Offer.doSearchUserActivity(userActivity);
            if (offerMap != null) {
               dataTableOffer = (TableData) offerMap.get(IConstant.DATA_NAME_DATA);
               dataTableOffer.setStandOut(userActivity, 1);
            }
         }
         else {
            if (offerIDSet.size() > 0) {
               offerMap = Offer.doSearchByOfferIDSet(offerIDSet, _listBid_Search || _listBid_Browse || _modifyBid);
               if (offerMap != null) {
                  dataTableOffer = (TableData) offerMap.get(IConstant.DATA_NAME_DATA);
               }
            }
         }
         //
         if (dataTableOffer != null) {
            List lstOfferRows = dataTableOffer.getRows();
            for (Object one : lstOfferRows) {
               List oneOfferRow = (List) one;
               //
               List lstBidRows = tempMap.get(oneOfferRow.get(0));
               if (lstBidRows == null) {
                  oneOfferRow.add(null);
               }
               else {
                  TableData tableDataBiD = new TableData(lstHeader_bid, lstBidRows, ((_listActivity || _viewAlertDetail || _listBidForOffer) ? colSeq_bid : colSeq_bid_add));
                  //
                  if (bidIDStandout != null) {
                     tableDataBiD.setStandOut(bidIDStandout, 0);      //bidID
                  }
                  else if (userStandout != null) {
                     tableDataBiD.setStandOut(userStandout, 2);      //user
                  }
                  //
                  oneOfferRow.add(tableDataBiD);
               }
            }
         }
         else {
            dataTableOffer = new TableData(Offer.lstHeader_offerdefault, new LinkedList(), Offer.colSeq_offerdefault);
         }
         //
         if (_listActivity) {
            dataTableOffer.setDescription("User Activities for " + userActivity);
         }
         else if (_listMyBid) {
            dataTableOffer.setDescription("My Open Bids");
         }
         else if (_listBidForOffer) {
            dataTableOffer.setDescription("Bids for One Open Offer");
         }
         else if (_viewAlertDetail) {
            dataTableOffer.setDescription("Offer and Its Bids for an Alert");
         }
         else if (_listBid_Search) {
            dataTableOffer.setDescription("Search Bids");
         }
         else if (_listBid_Browse) {
            dataTableOffer.setDescription("Browse Bids");
         }
         else if (_modifyBid) {
            dataTableOffer.setDescription("Bid To Be Midify");
         }
         else {
            dataTableOffer.setDescription("List of Bids");
         }
         //
         output.put(IConstant.DATA_NAME_DATA, dataTableOffer);
         //
         output.put(IConstant.DATA_NAME_STATUS, true);
         output.put(IConstant.DATA_NAME_MESSAGE, "OK");
      }
      catch (SQLException e) {
         output.put(IConstant.DATA_NAME_STATUS, false);
         output.put(IConstant.DATA_NAME_MESSAGE, "ERROR=" + e.getErrorCode() + ", SQL_STATE=" + e.getSQLState() + ", SQL=" + (sql));
         e.printStackTrace();
      }
      catch (ClassNotFoundException e) {
         output.put(IConstant.DATA_NAME_STATUS, false);
         output.put(IConstant.DATA_NAME_MESSAGE, "ERROR=" + "ClassNotFoundException" + ", SQL_STATE=" + e.getMessage());
         e.printStackTrace();
      }
      finally {
         if (statement != null) {
            try {
               statement.close();
            }
            catch (Throwable e) {
               e.printStackTrace();
            }
         }
         if (con != null) {
            try {
               con.close();
            }
            catch (Throwable e) {
               e.printStackTrace();
            }
         }
      }
      //
      return output;
   }

   /**
    * Create or modify a bid
    *
    * @param userID     User ID
    * @param parameters Map containing other parameters
    * @param isCreate   True when created, false when modified
    * @return Data for GUI rendering
    */
   public static Map doCreateOrModifyBid(String userID, Map<String, String[]> parameters, boolean isCreate) {
      Map output = new HashMap();
      //
      String     offerId;
      String     bidID;
      BigDecimal initPrice = null;
      BigDecimal increment = null;
      //
      String seller        = "";
      String categoryName  = "";
      String conditionCode = "";
      String description   = "";
      int    status        = -1;
      //
      Object[] newBid = new Object[4];
      if (isCreate) {
         offerId = getStringFromParamMap("offerId", parameters);
         bidID = null;
         BigDecimal price          = getBigDecimalFromParamMap("price", parameters);
         BigDecimal autoRebidLimit = getBigDecimalFromParamMap("autoRebidLimit", parameters);
         //
         newBid[0] = bidID;
         newBid[1] = userID;
         newBid[2] = price;
         newBid[3] = autoRebidLimit;
      }
      else {
         String   bidIDofferIDBuyer = getStringFromParamMap("bidIDofferIDBuyer", parameters);
         String[] temps             = bidIDofferIDBuyer.split(",");
         //
         offerId = temps[1];
         bidID = temps[0];
         BigDecimal price          = getBigDecimalFromParamMap("price", parameters);
         BigDecimal autoRebidLimit = getBigDecimalFromParamMap("autoRebidLimit", parameters);
         //
         newBid[0] = bidID;
         newBid[1] = temps[2];
         newBid[2] = price;
         newBid[3] = autoRebidLimit;
      }
      //
      Connection        con                     = null;
      PreparedStatement preparedStmtMaxPriceBid = null;
      PreparedStatement pStmtModifyBid          = null;
      PreparedStatement pStmtInsertBid          = null;
      PreparedStatement pStmtInsertAlert        = null;
      //
      Object[] lastMaxBid = new Object[4];
      try {
         BigDecimal price          = (BigDecimal) newBid[2];
         BigDecimal autoRebidLimit = (BigDecimal) newBid[3];
         if (price == null) {
            throw new Exception("Invalid bid: need to set price");
         }
         else if (price.compareTo(new BigDecimal(0)) <= 0) {
            throw new Exception("Invalid bid: price needs to be greater than 0: " + price);
         }
         //
         if (autoRebidLimit == null) {
            new BigDecimal(-1);
         }
         else if (autoRebidLimit.compareTo(new BigDecimal(0)) > 0 && autoRebidLimit.compareTo(price) < 0) {
            throw new Exception("Invalid bid: autoRebidLimit needs to be greater than price: " + autoRebidLimit + " less than " + price);
         }
         //
         con = getConnection();
         con.setAutoCommit(false);
         //
         preparedStmtMaxPriceBid = con.prepareStatement(isCreate ? ISQLConstant.SQL_BID_SELECT_MAX_PRICE : ISQLConstant.SQL_BID_SELECT_MAX_PRICE_EX);
         preparedStmtMaxPriceBid.setString(1, offerId);
         if (!isCreate) {
            preparedStmtMaxPriceBid.setString(2, bidID);
            preparedStmtMaxPriceBid.setString(3, bidID);
         }
         //
         ResultSet rs = preparedStmtMaxPriceBid.executeQuery();
         if (rs.next()) {
            Object _offerID        = rs.getObject(1);
            Object _seller         = rs.getObject(2);
            Object _categoryName   = rs.getObject(3);
            Object _conditionCode  = rs.getObject(4);
            Object _description    = rs.getObject(5);
            Object _initPrice      = rs.getObject(6);
            Object _increment      = rs.getObject(7);
            Object _minPrice       = rs.getObject(8);
            Object _startDate      = rs.getObject(9);
            Object _endDate        = rs.getObject(10);
            Object _status         = rs.getObject(11);
            Object _bidID          = rs.getObject(12);
            Object _buyer          = rs.getObject(13);
            Object _price          = rs.getObject(14);
            Object _autoRebidLimit = rs.getObject(15);
            Object _bidDate        = rs.getObject(16);
            //
            initPrice = (BigDecimal) _initPrice;
            increment = (BigDecimal) _increment;
            //
            seller = _seller == null ? "" : _seller.toString();
            categoryName = _categoryName == null ? "" : _categoryName.toString();
            conditionCode = _conditionCode == null ? "" : _conditionCode.toString();
            description = _description == null ? "" : _description.toString();
            status = (Integer) _status;
            //
            if (_bidID == null || _bidID.toString().length() == 0) {
               lastMaxBid = null;
            }
            else {
               lastMaxBid[0] = _bidID;
               lastMaxBid[1] = _buyer;
               lastMaxBid[2] = _price;
               lastMaxBid[3] = _autoRebidLimit;
            }
         }
         //
         Object[] current = newBid;
         Object[] last    = lastMaxBid;
         //
         if (isCreate) {
            current[0] = getUUID();
         }
         //
         // 1 Start, 2 NotMeetInitPrice, 3 LessThanLastPlusDelta; 4: offerClosed 5 Out-bided; 10 OK
         int outcome;
         if (status != 1) {
            outcome = 4;
         }
         else {
            boolean isModifyAndDoit = !isCreate;
            while (true) {
               BigDecimal last_price          = (last == null) ? null : ((BigDecimal) last[2]);
               BigDecimal last_autoRebidLimit = (last == null) ? null : ((BigDecimal) last[3]);
               //
               BigDecimal current_price          = (BigDecimal) current[2];
               BigDecimal current_autoRebidLimit = (BigDecimal) current[3];
               //
               //Check price meet offer
               if (last == null) {
                  if (current_price.compareTo(initPrice) >= 0) {
                     //OK
                  }
                  else {
                     outcome = 2;
                     break;
                  }
               }
               else {
                  BigDecimal lastPlusDelta = last_price.add(increment);
                  if (current_price.compareTo(lastPlusDelta) >= 0) {
                     //OK
                  }
                  else {
                     outcome = 3;
                     break;
                  }
               }
               //
               if (isModifyAndDoit) {
                  isModifyAndDoit = false;
                  //
                  if (pStmtModifyBid == null) {
                     pStmtModifyBid = con.prepareStatement(ISQLConstant.SQL_BID_UPDATE);
                  }
                  pStmtModifyBid.setBigDecimal(1, (BigDecimal) current[2]);
                  pStmtModifyBid.setBigDecimal(2, (BigDecimal) current[3]);
                  pStmtModifyBid.setString(3, current[0].toString());
                  pStmtModifyBid.execute();
               }
               else {
                  if (pStmtInsertBid == null) {
                     pStmtInsertBid = con.prepareStatement(ISQLConstant.SQL_BID_INSERT);
                  }
                  pStmtInsertBid.setString(1, current[0].toString());
                  pStmtInsertBid.setString(2, offerId);
                  pStmtInsertBid.setString(3, current[1].toString());
                  pStmtInsertBid.setBigDecimal(4, (BigDecimal) current[2]);
                  pStmtInsertBid.setBigDecimal(5, (BigDecimal) current[3]);
                  pStmtInsertBid.execute();
               }
               //
               if (last == null) {
                  outcome = 10;
                  break;
               }
               else {
                  BigDecimal new_bid = current_price.add(increment);
                  //
                  if (last_autoRebidLimit.compareTo(new_bid) >= 0) {                                 //Continue
                     last[0] = getUUID();
                     last[2] = new_bid;
                  }
                  else {                  //Out bid alert
                     String context = "Your bid for a " + categoryName + " (" + Helper.getConditionFromCode(conditionCode) + ", " + description + ") by seller " + seller + " is outbidded.";
                     //
                     pStmtInsertAlert = con.prepareStatement(ISQLConstant.SQL_ALERT_INSERT_BID);
                     pStmtInsertAlert.setString(1, getUUID());
                     pStmtInsertAlert.setString(2, last[1].toString());                     //user
                     pStmtInsertAlert.setString(3, offerId);
                     pStmtInsertAlert.setString(4, last[0].toString());                     //bidID
                     pStmtInsertAlert.setString(5, context);
                     pStmtInsertAlert.execute();
                     //
                     outcome = 5;                                                               // Out-bided
                     break;
                  }
               }
               //
               //Switch for next round
               {
                  Object[] temp = current;
                  current = last;
                  last = temp;
               }
            }
         }
         //
         if (outcome == 5) {
            con.commit();
            output.put(IConstant.DATA_NAME_STATUS, true);
            output.put(IConstant.DATA_NAME_MESSAGE, "BID " + (isCreate ? "CREATED" : "UPDATED") + " WITH AUTOREBID");
         }
         else if (outcome == 10) {
            con.commit();
            output.put(IConstant.DATA_NAME_STATUS, true);
            output.put(IConstant.DATA_NAME_MESSAGE, "BID " + (isCreate ? "CREATED" : "UPDATED"));
         }
         else if (outcome == 2) {
            con.rollback();
            output.put(IConstant.DATA_NAME_STATUS, false);
            output.put(IConstant.DATA_NAME_MESSAGE, "FAILED TO " + (isCreate ? "CREATED" : "UPDATED") + " BID DUE TO NotMeetInitPrice");
         }
         else if (outcome == 3) {
            con.rollback();
            output.put(IConstant.DATA_NAME_STATUS, false);
            output.put(IConstant.DATA_NAME_MESSAGE, "FAILED TO " + (isCreate ? "CREATED" : "UPDATED") + " BID DUE TO LessThanLastPlusDelta");
         }
         else {   // outcome == 4
            con.rollback();
            output.put(IConstant.DATA_NAME_STATUS, false);
            output.put(IConstant.DATA_NAME_MESSAGE, "FAILED TO " + (isCreate ? "CREATED" : "UPDATED") + " BID DUE TO Offer closed");
         }
      }
      catch (SQLException e) {
         if (con != null) {
            try {
               con.rollback();
            }
            catch (Throwable t) {
               t.printStackTrace();
            }
         }
         //
         output.put(IConstant.DATA_NAME_STATUS, false);
         output.put(IConstant.DATA_NAME_MESSAGE, "ERROR: ErrorCode=" + e.getErrorCode() + ", SQL_STATE=" + e.getSQLState() + ", Message=" + e.getMessage() + ", " + dumpParamMap(parameters));
         e.printStackTrace();
      }
      catch (ClassNotFoundException e) {
         if (con != null) {
            try {
               con.rollback();
            }
            catch (Throwable t) {
               t.printStackTrace();
            }
         }
         //
         output.put(IConstant.DATA_NAME_STATUS, false);
         output.put(IConstant.DATA_NAME_MESSAGE, "ERROR: Code=" + "ClassNotFoundException" + ", Message=" + e.getMessage() + ", " + dumpParamMap(parameters));
         e.printStackTrace();
      }
      catch (Exception e) {
         if (con != null) {
            try {
               con.rollback();
            }
            catch (Throwable t) {
               t.printStackTrace();
            }
         }
         //
         output.put(IConstant.DATA_NAME_STATUS, false);
         output.put(IConstant.DATA_NAME_MESSAGE, "ERROR: Code=" + "Exception" + ", Message=" + e.getMessage());
         e.printStackTrace();
      }
      finally {
         if (preparedStmtMaxPriceBid != null) {
            try {
               preparedStmtMaxPriceBid.close();
            }
            catch (Throwable t) {
               t.printStackTrace();
            }
         }
         if (pStmtInsertBid != null) {
            try {
               pStmtInsertBid.close();
            }
            catch (Throwable t) {
               t.printStackTrace();
            }
         }
         if (pStmtModifyBid != null) {
            try {
               pStmtModifyBid.close();
            }
            catch (Throwable t) {
               t.printStackTrace();
            }
         }
         if (pStmtInsertAlert != null) {
            try {
               pStmtInsertAlert.close();
            }
            catch (Throwable t) {
               t.printStackTrace();
            }
         }
         if (con != null) {
            try {
               con.setAutoCommit(true);
               con.close();
            }
            catch (Throwable t) {
               t.printStackTrace();
            }
         }
      }
      //
      return output;
   }


   /**
    * Delete a bid
    *
    * @param parameters Map containing bidID to be deleted
    * @return Data for GUI rendering
    */
   public static Map cancelBid(Map<String, String[]> parameters) {
      Map output = new HashMap();
      //
      String bidID = getStringFromParamMap("bidID", parameters);
      //
      Connection        con          = null;
      PreparedStatement preparedStmt = null;
      try {
         con = getConnection();
         //
         preparedStmt = con.prepareStatement(ISQLConstant.SQL_BID_DELETE);
         //
         preparedStmt.setString(1, bidID);
         //
         preparedStmt.execute();
         //
         int count = preparedStmt.getUpdateCount();
         if (count == 0) {
            output.put(IConstant.DATA_NAME_STATUS, false);
            output.put(IConstant.DATA_NAME_MESSAGE, "Could not delete bid.");
         }
         else {
            output.put(IConstant.DATA_NAME_STATUS, true);
            output.put(IConstant.DATA_NAME_MESSAGE, "Bid deleted");
         }
      }
      catch (SQLException e) {
         output.put(IConstant.DATA_NAME_STATUS, false);
         output.put(IConstant.DATA_NAME_MESSAGE, "ERROR: " + e.getErrorCode() + ", SQL_STATE: " + e.getSQLState() + ", DETAILS: " + exceptionToString(e));
         e.printStackTrace();
      }
      catch (ClassNotFoundException e) {
         output.put(IConstant.DATA_NAME_STATUS, false);
         output.put(IConstant.DATA_NAME_MESSAGE, "ERROR: " + "ClassNotFoundException" + ", SQL_STATE: " + e.getMessage() + ", DETAILS: " + exceptionToString(e));
         e.printStackTrace();
      }
      finally {
         if (preparedStmt != null) {
            try {
               preparedStmt.close();
            }
            catch (Throwable e) {
               e.printStackTrace();
            }
         }
         if (con != null) {
            try {
               con.close();
            }
            catch (Throwable e) {
               e.printStackTrace();
            }
         }
      }
      //
      return output;
   }


   /**
    * Get min when modify a bid
    *
    * @param parameters Map containing other parameters
    * @return Min Bid
    */
   public static BigDecimal getMinForModifyBid(Map<String, String[]> parameters) {
      BigDecimal output = null;
      //
      String _bidIDofferIDBuyer = getStringFromParamMap("bidIDofferIDBuyer", parameters);
      //
      String[] temps   = _bidIDofferIDBuyer.split(",");
      String   bidID   = temps[0];
      String   offerID = temps[1];
      //
      BigDecimal initPrice = null;
      BigDecimal increment = null;
      //
      BigDecimal price          = null;
      BigDecimal autoRebidLimit = null;
      //
      Connection        con                     = null;
      PreparedStatement preparedStmtMaxPriceBid = null;
      try {
         con = getConnection();
         //
         preparedStmtMaxPriceBid = con.prepareStatement(ISQLConstant.SQL_BID_SELECT_MAX_PRICE_EX);
         preparedStmtMaxPriceBid.setString(1, offerID);
         preparedStmtMaxPriceBid.setString(2, bidID);
         preparedStmtMaxPriceBid.setString(3, bidID);
         //
         ResultSet rs = preparedStmtMaxPriceBid.executeQuery();
         if (rs.next()) {
            Object _offerID        = rs.getObject(1);
            Object _seller         = rs.getObject(2);
            Object _categoryName   = rs.getObject(3);
            Object _conditionCode  = rs.getObject(4);
            Object _description    = rs.getObject(5);
            Object _initPrice      = rs.getObject(6);
            Object _increment      = rs.getObject(7);
            Object _minPrice       = rs.getObject(8);
            Object _startDate      = rs.getObject(9);
            Object _endDate        = rs.getObject(10);
            Object _status         = rs.getObject(11);
            Object _bidID          = rs.getObject(12);
            Object _buyer          = rs.getObject(13);
            Object _price          = rs.getObject(14);
            Object _autoRebidLimit = rs.getObject(15);
            Object _bidDate        = rs.getObject(16);
            //
            initPrice = (BigDecimal) _initPrice;
            increment = (BigDecimal) _increment;
            //
            if (_bidID != null && _bidID.toString().length() != 0) {
               price = (BigDecimal) _price;
            }
         }
         //
         if (initPrice != null && increment != null) {
            if (price == null) {
               output = initPrice;
            }
            else {
               output = price.add(increment);
            }
         }
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      finally {
         if (preparedStmtMaxPriceBid != null) {
            try {
               preparedStmtMaxPriceBid.close();
            }
            catch (Throwable t) {
               t.printStackTrace();
            }
         }
         if (con != null) {
            try {
               con.close();
            }
            catch (Throwable t) {
               t.printStackTrace();
            }
         }
      }
      //
      return output;
   }


   public static void main3(String[] args) {
      Map<String, String[]> parameters = new HashMap<>();
      //
      parameters.put("offerId", new String[]{"d6ac071c449c46b4812dd96b9bc8f197"});
      parameters.put("price", new String[]{"550"});
      parameters.put("autoRebidLimit", new String[]{"1200"});
      //
      Map map = doCreateOrModifyBid("user", parameters, true);
      //
      System.out.println(IConstant.DATA_NAME_STATUS + "= " + map.get(IConstant.DATA_NAME_STATUS));
      System.out.println(IConstant.DATA_NAME_MESSAGE + "= " + map.get(IConstant.DATA_NAME_MESSAGE));
      System.out.println(IConstant.DATA_NAME_USER_TYPE + "= " + map.get(IConstant.DATA_NAME_USER_TYPE));
   }

   public static void main8(String[] args) {
      Map<String, String[]> parameters = new HashMap<>();
      //
      parameters.put("bidIDofferIDBuyer", new String[]{"02b064d413c044c5bafb8155bf525b3d,721fef17f8d84e30b7b852ae62df0e19,user"});
      parameters.put("price", new String[]{"1000"});
      parameters.put("autoRebidLimit", new String[]{"10"});
      //
      Map map = doCreateOrModifyBid(null, parameters, false);
      //
      System.out.println(IConstant.DATA_NAME_STATUS + "= " + map.get(IConstant.DATA_NAME_STATUS));
      System.out.println(IConstant.DATA_NAME_MESSAGE + "= " + map.get(IConstant.DATA_NAME_MESSAGE));
      System.out.println(IConstant.DATA_NAME_USER_TYPE + "= " + map.get(IConstant.DATA_NAME_USER_TYPE));
   }

   public static void main6(String[] args) {
      Map<String, String[]> parameters = new HashMap<>();
      //
      parameters.put("action", new String[]{"repSearchBid"});
      parameters.put("buyerOP", new String[]{"any"});
      parameters.put("buyerVal", new String[]{""});
      //
      //parameters.put("bidID", new String[]{"11fe20aabc7a4025928e9522544be2e3"});
      //
      Map map = searchBid(parameters, null, null);
      //
      System.out.println(IConstant.DATA_NAME_STATUS + "= " + map.get(IConstant.DATA_NAME_STATUS));
      System.out.println(IConstant.DATA_NAME_MESSAGE + "= " + map.get(IConstant.DATA_NAME_MESSAGE));
      System.out.println(IConstant.DATA_NAME_USER_TYPE + "= " + map.get(IConstant.DATA_NAME_USER_TYPE));
   }

   public static void main4(String[] args) {
      System.out.println("Start");
      //
      Map map = searchBid(null, "user", null);
      //
      System.out.println(IConstant.DATA_NAME_STATUS + "= " + map.get(IConstant.DATA_NAME_STATUS));
      System.out.println(IConstant.DATA_NAME_MESSAGE + "= " + map.get(IConstant.DATA_NAME_MESSAGE));
      System.out.println(IConstant.DATA_NAME_USER_TYPE + "= " + map.get(IConstant.DATA_NAME_USER_TYPE));
   }

   public static void main0(String[] args) {
      System.out.println(ISQLConstant.SQL_TRADE_TOTAL);
      //
      Map<String, String[]> parameters = new HashMap<>();
      //
      parameters.put("offeridcategoryname", new String[]{"9dee3107cdf444a7b4f0cd79524cfe53,car"});
      //
      Map map = searchBid(parameters, null, null);
      //
      System.out.println(IConstant.DATA_NAME_STATUS + "= " + map.get(IConstant.DATA_NAME_STATUS));
      System.out.println(IConstant.DATA_NAME_MESSAGE + "= " + map.get(IConstant.DATA_NAME_MESSAGE));
      System.out.println(IConstant.DATA_NAME_USER_TYPE + "= " + map.get(IConstant.DATA_NAME_USER_TYPE));
   }

   public static void main(String[] args) {
      System.out.println(ISQLConstant.SQL_TRADE_TOTAL);
      //
      Map<String, String[]> parameters = new HashMap<>();
      //
      parameters.put("bidIDofferIDBuyer", new String[]{"b2d4ba68a1d84841aebc0434988d7261,1ccf77b4a6cc46a5b9a471db65ae4618,test1"});
      //
      BigDecimal output = getMinForModifyBid(parameters);
      //
      System.out.println(output);
   }
}


//action=viewAlertDetail,offerIDbidID=6bc17ded8d0e4300ae8ce80a5fa85b8d,

/* All
SELECT t1.*, t2.currPrice FROM (SELECT b1.bidID, b1.buyer, b1.price, b1.autoRebidLimit, b1.bidDate, o1.offerID, o1.seller, o1.categoryName, o1.conditionCode, o1.description, o1.initPrice, o1.increment, o1.minPrice, o1.startDate, o1.endDate, o1.status FROM Bid b1 INNER JOIN Offer o1 ON b1.offerID = o1.offerID) t1 LEFT OUTER JOIN (SELECT b1.price as currPrice, b1.offerID FROM Bid b1 WHERE b1.price = (SELECT MAX(price) FROM Bid b where b.offerID = b1.offerID)) t2 ON t1.offerID = t2.offerID order by bidDate
*/

/* By Buyer
SELECT t1.*, t2.currPrice FROM (SELECT b1.bidID, b1.buyer, b1.price, b1.autoRebidLimit, b1.bidDate, o1.offerID, o1.seller, o1.categoryName, o1.conditionCode, o1.description, o1.initPrice, o1.increment, o1.minPrice, o1.startDate, o1.endDate, o1.status FROM Bid b1 INNER JOIN Offer o1 ON b1.offerID = o1.offerID AND b1.buyer = 'user') t1 LEFT OUTER JOIN (SELECT b1.price as currPrice, b1.offerID FROM Bid b1 WHERE b1.price = (SELECT MAX(price) FROM Bid b where b.offerID = b1.offerID)) t2 ON t1.offerID = t2.offerID order by bidDate
*/



/*
	//From list Offer: Get info for one OfferID
	public static Map getBidsForOffer(String offerID) {
		Map output = new HashMap();
		//
		List lstRows = new ArrayList();
		//
		Connection        con          = null;
		PreparedStatement preparedStmt = null;
		try {
			con = getConnection();
			//
			preparedStmt = con.prepareStatement(SQL_BID_SELECT_BY_OFFERID);
			//
			preparedStmt.setString(1, offerID);
			//
			ResultSet rs = preparedStmt.executeQuery();
			//
			while (rs.next()) {
				Object bidID   = rs.getObject(1);
				Object buyer   = rs.getObject(2);
				Object price   = rs.getObject(3);
				Object bidDate = rs.getObject(4);
				//
				List currentRow = new LinkedList();
				lstRows.add(currentRow);
				//
				currentRow.add(bidID);
				currentRow.add(buyer);
				currentRow.add(price);
				currentRow.add(bidDate);
			}
			//
			output.put(DATA_NAME_DATA, lstRows);
			output.put(DATA_NAME_DATA_ADD, lstHeader_bid1);
			//
			output.put(IConstant.DATA_NAME_STATUS, true);
			output.put(IConstant.DATA_NAME_MESSAGE, "OK");
		}
		catch (SQLException e) {
			output.put(IConstant.DATA_NAME_STATUS, false);
			output.put(IConstant.DATA_NAME_MESSAGE, "ERROR: " + e.getErrorCode() + ", SQL_STATE: " + e.getSQLState() + ", DETAILS: " + exceptionToString(e));
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
			output.put(IConstant.DATA_NAME_STATUS, false);
			output.put(IConstant.DATA_NAME_MESSAGE, "ERROR: " + "ClassNotFoundException" + ", SQL_STATE: " + e.getMessage() + ", DETAILS: " + exceptionToString(e));
			e.printStackTrace();
		}
		finally {
			if (preparedStmt != null) {try {preparedStmt.close();} catch (Throwable e) {e.printStackTrace();}}
			if (con != null) {try {con.close();} catch (Throwable e) {e.printStackTrace();}}
		}
		//
		return output;
	}
 */
