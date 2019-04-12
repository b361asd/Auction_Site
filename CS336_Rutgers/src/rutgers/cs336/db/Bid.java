package rutgers.cs336.db;

import rutgers.cs336.gui.Helper;
import rutgers.cs336.gui.TableData;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

public class Bid extends DBBase {

	private static List  lstHeader_bid = Arrays.asList("bidID", "offerID", "buyer", "price", "autoRebidLimit", "bidDate");
	private static int[] colSeq_bid    = {0, 1, 2, 3, 4, 5};

	private static List lstHeader_bid1 = Arrays.asList("bidID", "buyer", "price", "bidDate");

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
			output.put(DATA_NAME_STATUS, true);
			output.put(DATA_NAME_MESSAGE, "OK");
		}
		catch (SQLException e) {
			output.put(DATA_NAME_STATUS, false);
			output.put(DATA_NAME_MESSAGE, "ERROR: " + e.getErrorCode() + ", SQL_STATE: " + e.getSQLState() + ", DETAILS: " + exceptionToString(e));
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
			output.put(DATA_NAME_STATUS, false);
			output.put(DATA_NAME_MESSAGE, "ERROR: " + "ClassNotFoundException" + ", SQL_STATE: " + e.getMessage() + ", DETAILS: " + exceptionToString(e));
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


	public static Map searchBid(Map<String, String[]> parameters, String userActivity) {
		String sql           = null;
		String bidIDStandout = null;
		//
		if (userActivity != null && userActivity.length() > 0) {
			StringBuilder sb = FormatterBidQuery.buildQueryUserActivity(userActivity);
			//
			sql = sb.toString();
		}
		else {
			StringBuilder sb = FormatterBidQuery.initQuerySearchAll();
			//
			String offerIDbidID = getStringFromParamMap("offerIDbidID", parameters);
			if (offerIDbidID.length() > 0) {
				String[] temps = offerIDbidID.split(",");
				//
				FormatterOfferQuery.addCondition(sb, "o.offerID", OP_SZ_EQUAL, temps[0], null);
				//
				if (temps.length >= 2) {
					bidIDStandout = temps[1];
				}
			}
			else {
				{
					String buyerOP  = getStringFromParamMap("buyerOP", parameters);
					String buyerVal = getStringFromParamMap("buyerVal", parameters);
					FormatterOfferQuery.addCondition(sb, "buyer", buyerOP, buyerVal, null);
				}
				//
				String bidIDofferIDBuyer = getStringFromParamMap("bidIDofferIDBuyer", parameters);
				bidIDStandout = null;
				if (!bidIDofferIDBuyer.equals("")) {
					String[] temps = bidIDofferIDBuyer.split(",");
					FormatterOfferQuery.addCondition(sb, "o.offerID", OP_SZ_EQUAL, temps[1], null);
					bidIDStandout = temps[0];
				}
				else {
					{
						String bidID = getStringFromParamMap("bidID", parameters);
						FormatterOfferQuery.addCondition(sb, "bidID", OP_SZ_EQUAL, bidID, null);
					}
					//
					{
						String offerID = getStringFromParamMap("offerID", parameters);
						FormatterOfferQuery.addCondition(sb, "o.offerID", OP_SZ_EQUAL, offerID, null);
					}
				}
			}
			//
			sql = sb.toString();
		}
		//
		Map               output     = new HashMap();
		Map<String, List> tempMap    = new HashMap<>();            //offerID -> Bids(in List)
		Set<String>       offerIDSet = new HashSet<>();            //offerID set
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
				List currentRow = new LinkedList();
				//
				currentRow.add(bidID);
				currentRow.add(offerID);
				currentRow.add(buyer);
				currentRow.add(price);
				currentRow.add(autoRebidLimit);
				currentRow.add(bidDate);
				//
				offerIDSet.add(offerID.toString());
				//
				List lstRows = tempMap.computeIfAbsent(offerID.toString(), k -> new ArrayList());
				//
				lstRows.add(currentRow);
			}
			//
			Map       offerMap       = null;
			TableData dataTableOffer = null;
			if (userActivity != null && userActivity.length() > 0) {
				offerMap = Offer.doSearchUserActivity(userActivity);
				dataTableOffer = (TableData) offerMap.get(DATA_NAME_DATA);
				dataTableOffer.setStandOut(userActivity, 1);
			}
			else {
				if (offerIDSet.size() > 0) {
					offerMap = Offer.doSearchByOfferIDSet(offerIDSet);
					dataTableOffer = (TableData) offerMap.get(DATA_NAME_DATA);
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
						TableData tableDataBiD = new TableData(lstHeader_bid, lstBidRows, colSeq_bid);
						//
						if (userActivity != null && userActivity.length() > 0) {
							tableDataBiD.setStandOut(userActivity, 2);      //buyer
						}
						//
						if (bidIDStandout != null) {
							tableDataBiD.setStandOut(bidIDStandout, 0);
						}
						//
						oneOfferRow.add(tableDataBiD);
					}
				}
			}
			//
			output.put(DATA_NAME_DATA, dataTableOffer);
			//
			output.put(DATA_NAME_STATUS, true);
			output.put(DATA_NAME_MESSAGE, "OK");
		}
		catch (SQLException e) {
			output.put(DATA_NAME_STATUS, false);
			output.put(DATA_NAME_MESSAGE, "ERROR=" + e.getErrorCode() + ", SQL_STATE=" + e.getSQLState() + ", SQL=" + (sql));
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
			output.put(DATA_NAME_STATUS, false);
			output.put(DATA_NAME_MESSAGE, "ERROR=" + "ClassNotFoundException" + ", SQL_STATE=" + e.getMessage());
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


	public static Map doCreateOrModifyBid(String userID, Map<String, String[]> parameters, boolean isCreate) {
		Map output = new HashMap();
		//
		String     offerId   = "";
		String     bidID     = "";
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
			newBid[0] = temps[2];
			newBid[0] = price;
			newBid[0] = autoRebidLimit;
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
			con = getConnection();
			con.setAutoCommit(false);
			//
			preparedStmtMaxPriceBid = con.prepareStatement(isCreate ? SQL_BID_SELECT_MAX_PRICE : SQL_BID_SELECT_MAX_PRICE_EX);
			preparedStmtMaxPriceBid.setString(1, offerId);
			if (!isCreate) {
				preparedStmtMaxPriceBid.setString(2, bidID);
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
			int outcome;               // 1 Start, 2 NotMeetInitPrice, 3 LessThanLastPlusDelta; 4: offerClosed 5 Out-bided; 10 OK
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
						pStmtModifyBid = con.prepareStatement(SQL_BID_UPDATE);
						pStmtModifyBid.setBigDecimal(1, (BigDecimal) current[2]);
						pStmtModifyBid.setBigDecimal(2, (BigDecimal) current[3]);
						pStmtModifyBid.setString(3, current[0].toString());
						pStmtModifyBid.execute();
					}
					else {
						if (pStmtInsertBid == null) {
							pStmtInsertBid = con.prepareStatement(SQL_BID_INSERT);
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
							pStmtInsertAlert = con.prepareStatement(SQL_ALERT_INSERT_BID);
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
				output.put(DATA_NAME_STATUS, true);
				output.put(DATA_NAME_MESSAGE, "BID " + (isCreate ? "CREATED" : "UPDATED") + " WITH AUTOREBID");
			}
			else if (outcome == 10) {
				con.commit();
				output.put(DATA_NAME_STATUS, true);
				output.put(DATA_NAME_MESSAGE, "BID " + (isCreate ? "CREATED" : "UPDATED"));
			}
			else if (outcome == 2) {
				con.rollback();
				output.put(DATA_NAME_STATUS, false);
				output.put(DATA_NAME_MESSAGE, "FAILED TO " + (isCreate ? "CREATED" : "UPDATED") + " BID DUE TO NotMeetInitPrice");
			}
			else if (outcome == 3) {
				con.rollback();
				output.put(DATA_NAME_STATUS, false);
				output.put(DATA_NAME_MESSAGE, "FAILED TO " + (isCreate ? "CREATED" : "UPDATED") + " BID DUE TO LessThanLastPlusDelta");
			}
			else {   // outcome == 4
				con.rollback();
				output.put(DATA_NAME_STATUS, false);
				output.put(DATA_NAME_MESSAGE, "FAILED TO " + (isCreate ? "CREATED" : "UPDATED") + " BID DUE TO Offer closed");
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
			output.put(DATA_NAME_STATUS, false);
			output.put(DATA_NAME_MESSAGE, "ERROR: ErrorCode=" + e.getErrorCode() + ", SQL_STATE=" + e.getSQLState() + ", Message=" + e.getMessage() + ", " + dumpParamMap(parameters));
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
			output.put(DATA_NAME_STATUS, false);
			output.put(DATA_NAME_MESSAGE, "ERROR: Code=" + "ClassNotFoundException" + ", Message=" + e.getMessage() + ", " + dumpParamMap(parameters));
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


	//Alert cascading delete
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
			preparedStmt = con.prepareStatement(SQL_BID_DELETE);
			//
			preparedStmt.setString(1, bidID);
			//
			preparedStmt.execute();
			//
			int count = preparedStmt.getUpdateCount();
			if (count == 0) {
				output.put(DATA_NAME_STATUS, false);
				output.put(DATA_NAME_MESSAGE, "Could not delete bid.");
			}
			else {
				output.put(DATA_NAME_STATUS, true);
				output.put(DATA_NAME_MESSAGE, "Bid deleted");
			}
		}
		catch (SQLException e) {
			output.put(DATA_NAME_STATUS, false);
			output.put(DATA_NAME_MESSAGE, "ERROR: " + e.getErrorCode() + ", SQL_STATE: " + e.getSQLState() + ", DETAILS: " + exceptionToString(e));
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
			output.put(DATA_NAME_STATUS, false);
			output.put(DATA_NAME_MESSAGE, "ERROR: " + "ClassNotFoundException" + ", SQL_STATE: " + e.getMessage() + ", DETAILS: " + exceptionToString(e));
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


	public static void main3(String[] args) {
		Map<String, String[]> parameters = new HashMap<>();
		//
		parameters.put("offerId", new String[]{"d6ac071c449c46b4812dd96b9bc8f197"});
		parameters.put("price", new String[]{"550"});
		parameters.put("autoRebidLimit", new String[]{"1200"});
		//
		Map map = doCreateOrModifyBid("user", parameters, true);
		//
		System.out.println(DATA_NAME_STATUS + "= " + map.get(DATA_NAME_STATUS));
		System.out.println(DATA_NAME_MESSAGE + "= " + map.get(DATA_NAME_MESSAGE));
		System.out.println(DATA_NAME_USER_TYPE + "= " + map.get(DATA_NAME_USER_TYPE));
	}

	public static void main2(String[] args) {
		Map<String, String[]> parameters = new HashMap<>();
		//
		parameters.put("bidIDofferIDBuyer", new String[]{"11fe20aabc7a4025928e9522544be2e3,8f0e1575b13040f88a840a6599174cc0,user"});
		parameters.put("price", new String[]{"1900"});
		parameters.put("autoRebidLimit", new String[]{"2500"});
		//
		Map map = doCreateOrModifyBid(null, parameters, false);
		//
		System.out.println(DATA_NAME_STATUS + "= " + map.get(DATA_NAME_STATUS));
		System.out.println(DATA_NAME_MESSAGE + "= " + map.get(DATA_NAME_MESSAGE));
		System.out.println(DATA_NAME_USER_TYPE + "= " + map.get(DATA_NAME_USER_TYPE));
	}

	public static void main1(String[] args) {
		Map<String, String[]> parameters = new HashMap<>();
		//
		//parameters.put("buyerOP", new String[]{"11fe20aabc7a4025928e9522544be2e3"});
		//parameters.put("buyerVal", new String[]{OP_SZ_EQUAL});
		//
		parameters.put("bidID", new String[]{"11fe20aabc7a4025928e9522544be2e3"});
		//
		Map map = searchBid(parameters, null);
		//
		System.out.println(DATA_NAME_STATUS + "= " + map.get(DATA_NAME_STATUS));
		System.out.println(DATA_NAME_MESSAGE + "= " + map.get(DATA_NAME_MESSAGE));
		System.out.println(DATA_NAME_USER_TYPE + "= " + map.get(DATA_NAME_USER_TYPE));
	}

	public static void main4(String[] args) {
		System.out.println("Start");
		//
		Map map = searchBid(null, "user");
		//
		System.out.println(DATA_NAME_STATUS + "= " + map.get(DATA_NAME_STATUS));
		System.out.println(DATA_NAME_MESSAGE + "= " + map.get(DATA_NAME_MESSAGE));
		System.out.println(DATA_NAME_USER_TYPE + "= " + map.get(DATA_NAME_USER_TYPE));
	}

	public static void main(String[] args) {
		Map<String, String[]> parameters = new HashMap<>();
		//
		parameters.put("offerIDbidID", new String[]{"72a5ac2ac47c44ed8006c31b72e53725,"});
		//
		Map map = searchBid(parameters, null);
		//
		System.out.println(DATA_NAME_STATUS + "= " + map.get(DATA_NAME_STATUS));
		System.out.println(DATA_NAME_MESSAGE + "= " + map.get(DATA_NAME_MESSAGE));
		System.out.println(DATA_NAME_USER_TYPE + "= " + map.get(DATA_NAME_USER_TYPE));
	}
}


/* All
SELECT t1.*, t2.currPrice FROM (SELECT b1.bidID, b1.buyer, b1.price, b1.autoRebidLimit, b1.bidDate, o1.offerID, o1.seller, o1.categoryName, o1.conditionCode, o1.description, o1.initPrice, o1.increment, o1.minPrice, o1.startDate, o1.endDate, o1.status FROM Bid b1 INNER JOIN Offer o1 ON b1.offerID = o1.offerID) t1 LEFT OUTER JOIN (SELECT b1.price as currPrice, b1.offerID FROM Bid b1 WHERE b1.price = (SELECT MAX(price) FROM Bid b where b.offerID = b1.offerID)) t2 ON t1.offerID = t2.offerID order by bidDate
*/

/* By Buyer
SELECT t1.*, t2.currPrice FROM (SELECT b1.bidID, b1.buyer, b1.price, b1.autoRebidLimit, b1.bidDate, o1.offerID, o1.seller, o1.categoryName, o1.conditionCode, o1.description, o1.initPrice, o1.increment, o1.minPrice, o1.startDate, o1.endDate, o1.status FROM Bid b1 INNER JOIN Offer o1 ON b1.offerID = o1.offerID AND b1.buyer = 'user') t1 LEFT OUTER JOIN (SELECT b1.price as currPrice, b1.offerID FROM Bid b1 WHERE b1.price = (SELECT MAX(price) FROM Bid b where b.offerID = b1.offerID)) t2 ON t1.offerID = t2.offerID order by bidDate
*/




/*
	public static Map _doCreateBid(String userID, Map<String, String[]> parameters) {
		Map output = new HashMap();
		//
		String bidId = getUUID();
		//
		Connection        con            = null;
		PreparedStatement pStmtInsertBid = null;
		try {
			con = getConnection();
			//
			String offerId = getStringFromParamMap("offerId", parameters);
			BigDecimal price = getBigDecimalFromParamMap("price", parameters);
			//
			pStmtInsertBid = con.prepareStatement(SQL_BID_INSERT);
			pStmtInsertBid.setString(1, bidId);
			pStmtInsertBid.setString(2, userID);
			pStmtInsertBid.setBigDecimal(3, price);
			pStmtInsertBid.setBigDecimal(4, getBigDecimalFromParamMap("autoRebidLimit", parameters));
			pStmtInsertBid.setBigDecimal(5, price);
			pStmtInsertBid.setString(6, offerId);
			pStmtInsertBid.setString(7, offerId);
			pStmtInsertBid.setBigDecimal(8, price);
			pStmtInsertBid.setString(9, offerId);
			//
			pStmtInsertBid.execute();
			//
			int count = pStmtInsertBid.getUpdateCount();
			//
			if (count == 1) {
				output.put(DATA_NAME_STATUS, true);
				output.put(DATA_NAME_MESSAGE, "Bid Created!");
			}
			else {
				output.put(DATA_NAME_STATUS, false);
				output.put(DATA_NAME_MESSAGE, "No bid created.");
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
			output.put(DATA_NAME_STATUS, false);
			output.put(DATA_NAME_MESSAGE, "ERROR: ErrorCode=" + e.getErrorCode() + ", SQL_STATE=" + e.getSQLState() + ", Message=" + e.getMessage() + ", " + dumpParamMap(parameters));
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
			output.put(DATA_NAME_STATUS, false);
			output.put(DATA_NAME_MESSAGE, "ERROR: Code=" + "ClassNotFoundException" + ", Message=" + e.getMessage() + ", " + dumpParamMap(parameters));
			e.printStackTrace();
		}
		finally {
			if (pStmtInsertBid != null) {
				try {
					pStmtInsertBid.close();
				}
				catch (Throwable t) {
					t.printStackTrace();
				}
			}
			//
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
	
	
*/