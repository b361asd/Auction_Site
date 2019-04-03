package rutgers.cs336.db;

import rutgers.cs336.gui.TableData;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

public class Bid extends DBBase {

	public static Map getBidsForOffer(String offerID) {
		Map output = new HashMap();
		//
		List lstRows   = new ArrayList();
		List lstHeader = new ArrayList();
		//
		Connection        con          = null;
		PreparedStatement preparedStmt = null;
		try {
			con = getConnection();
			//
			preparedStmt = con.prepareStatement(SQL_BID_SELECT);
			//
			preparedStmt.setString(1, offerID);
			//
			ResultSet rs = preparedStmt.executeQuery();
			//
			int rowIndex = -1;
			while (rs.next()) {
				Object bidID   = rs.getObject(1);
				Object buyer   = rs.getObject(2);
				Object price   = rs.getObject(3);
				Object bidDate = rs.getObject(4);
				//
				List currentRow = new LinkedList();
				lstRows.add(currentRow);
				rowIndex++;
				//
				currentRow.add(bidID);
				currentRow.add(buyer);
				currentRow.add(price);
				currentRow.add(bidDate);
				//
				if (rowIndex == 0) {
					lstHeader.add("bidID");
					lstHeader.add("buyer");
					lstHeader.add("price");
					lstHeader.add("bidDate");
				}
			}
			//
			output.put(DATA_NAME_DATA, lstRows);
			output.put(DATA_NAME_DATA_ADD, lstHeader);
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
			//
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

	
	
	
	
	
	
	public static Map doCreateBid(String userID, Map<String, String[]> parameters) {
		Map output = new HashMap();
		//
		String bidId = getUUID();
		//
		Connection        con            = null;
		PreparedStatement pStmtInsertBid = null;
		try {
			con = getConnection();
			//
			pStmtInsertBid = con.prepareStatement(SQL_BID_INSERT);
			pStmtInsertBid.setString(1, bidId);
			pStmtInsertBid.setString(2, userID);
			pStmtInsertBid.setBigDecimal(3, getBigDecimalFromParamMap("price", parameters));
			pStmtInsertBid.setBigDecimal(4, getBigDecimalFromParamMap("autoRebidLimit", parameters));
			pStmtInsertBid.setBigDecimal(5, getBigDecimalFromParamMap("price", parameters));
			pStmtInsertBid.setString(6, getStringFromParamMap("offerId", parameters));
			pStmtInsertBid.setBigDecimal(7, getBigDecimalFromParamMap("price", parameters));
			pStmtInsertBid.setString(8, getStringFromParamMap("offerId", parameters));
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
			output.put(DATA_NAME_MESSAGE, "ERROR: ErrorCode=" + e.getErrorCode() + ", SQL_STATE=" + e.getSQLState() + ", Message=" + e.getMessage() + ", ParamMap=" + getParamMap(parameters));
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
			output.put(DATA_NAME_MESSAGE, "ERROR: Code=" + "ClassNotFoundException" + ", Message=" + e.getMessage() + ", ParamMap=" + getParamMap(parameters));
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

	
	public static Map doModifyBid(Map<String, String[]> parameters) {
		Map output = new HashMap();
		//
		String bidIDofferIDBuyer  = getStringFromParamMap("bidIDofferIDBuyer", parameters);
		String[] temps = bidIDofferIDBuyer.split("\\,");
		//
		BigDecimal price = getBigDecimalFromParamMap("price", parameters);
		BigDecimal autoRebidLimit = getBigDecimalFromParamMap("autoRebidLimit", parameters);
		//
		Connection        con                  = null;
		PreparedStatement pStmtDeleteBid      	= null;
		PreparedStatement pStmtInsertBid 		= null;
		try {
			con = getConnection();
			con.setAutoCommit(false);
			//
			pStmtDeleteBid = con.prepareStatement(SQL_BID_DELETE);
			pStmtDeleteBid.setString(1, temps[0]);
			pStmtDeleteBid.execute();
			//
			int countDelete = pStmtDeleteBid.getUpdateCount();
			if (countDelete==1) {
				pStmtInsertBid = con.prepareStatement(SQL_BID_INSERT);
				pStmtInsertBid.setString(1, temps[0]);
				pStmtInsertBid.setString(2, temps[2]);
				pStmtInsertBid.setBigDecimal(3, price);
				pStmtInsertBid.setBigDecimal(4, autoRebidLimit);
				pStmtInsertBid.setBigDecimal(5, price);
				pStmtInsertBid.setString(6, temps[1]);
				pStmtInsertBid.setBigDecimal(7, price);
				pStmtInsertBid.setString(8, temps[1]);
				//
				pStmtInsertBid.execute();
				//
				int countInsert = pStmtInsertBid.getUpdateCount();
				if (countInsert==1) {
					con.commit();
					//
					output.put(DATA_NAME_STATUS, true);
					output.put(DATA_NAME_MESSAGE, "BID UPDATED");
				}
				else {
					con.rollback();
					//
					output.put(DATA_NAME_STATUS, false);
					output.put(DATA_NAME_MESSAGE, "FAILED TO INSERT");
				}
			}
			else {
				con.rollback();
				//
				output.put(DATA_NAME_STATUS, false);
				output.put(DATA_NAME_MESSAGE, "FAILED TO DELETE");
			}
			//
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
			output.put(DATA_NAME_MESSAGE, "ERROR: ErrorCode=" + e.getErrorCode() + ", SQL_STATE=" + e.getSQLState() + ", Message=" + e.getMessage() + ", ParamMap=" + getParamMap(parameters));
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
			output.put(DATA_NAME_MESSAGE, "ERROR: Code=" + "ClassNotFoundException" + ", Message=" + e.getMessage() + ", ParamMap=" + getParamMap(parameters));
			e.printStackTrace();
		}
		finally {
			if (pStmtDeleteBid != null) {try {pStmtDeleteBid.close();} catch (Throwable t) {t.printStackTrace();}}
			if (pStmtInsertBid != null) {try {pStmtInsertBid.close();} catch (Throwable t) {t.printStackTrace();}}
			if (con != null) {try {con.setAutoCommit(true);con.close();} catch (Throwable t) {t.printStackTrace();}}
		}
		//
		return output;
	}
	
	
	
	public static Map cancelBid(Map<String, String[]> parameters) {
		Map output = new HashMap();
		//
		String bidID  = getStringFromParamMap("bidID", parameters);
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
			//
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



	public static Map searchBid(Map<String, String[]> parameters) {
		StringBuilder sb  = FormatterBidQuery.initQuerySearch();
		//
		{
			String buyerOP  = getStringFromParamMap("buyerOP", parameters);
			String buyerVal = getStringFromParamMap("buyerVal", parameters);
			FormatterOfferQuery.addCondition(sb, "buyer", buyerOP, buyerVal, null);
		}
		//
		String bidIDofferIDBuyer  = getStringFromParamMap("bidIDofferIDBuyer", parameters);
		String bidIDStandout = null;
		if (!bidIDofferIDBuyer.equals("")) {
			String[] temps = bidIDofferIDBuyer.split("\\,");
			FormatterOfferQuery.addCondition(sb, "o.offerID", OP_SZ_EQUAL, temps[1], null);
			bidIDStandout = temps[0];
		}
		else {
			{
				String bidID  = getStringFromParamMap("bidID", parameters);
				FormatterOfferQuery.addCondition(sb, "bidID", OP_SZ_EQUAL, bidID, null);
			}
			//
			{
				String offerID  = getStringFromParamMap("offerID", parameters);
				FormatterOfferQuery.addCondition(sb, "o.offerID", OP_SZ_EQUAL, offerID, null);
			}
		}
		//
		String sql = sb.toString();
		//
		Map output = new HashMap();
		Map<String,List> tempMap = new HashMap();
		Set<String> offerIDSet = new HashSet<>(); 
		//
		List lstHeader = new ArrayList();
		//
		Map<String, String>  mapFields         = new HashMap<>();
		Map<String, Integer> mapFieldIDToIndex = new HashMap<>();
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
			lstHeader.add("bidID");
			lstHeader.add("offerID");
			lstHeader.add("buyer");
			lstHeader.add("price");
			lstHeader.add("autoRebidLimit");
			lstHeader.add("bidDate");
			//
			int[] colSeq = new int[lstHeader.size() - 0];
			{
				colSeq[0] = 0;      //
				colSeq[1] = 1;      //
				colSeq[2] = 2;      //
				colSeq[3] = 3;      //
				colSeq[4] = 4;      //
				colSeq[5] = 5;      //
			}
			//
			while (rs.next()) {
				Object bidID       		= rs.getObject(1);
				Object offerID       	= rs.getObject(2);
				Object buyer       		= rs.getObject(3);
				Object price       		= rs.getObject(4);
				Object autoRebidLimit   = rs.getObject(5);
				Object bidDate       	= rs.getObject(6);
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
				List lstRows   = tempMap.get(offerID.toString());
				if (lstRows==null) {
					lstRows = new ArrayList();
					tempMap.put(offerID.toString(), lstRows);
				}
				lstRows.add(currentRow);
			}
			//
			Map offerMap = Offer.doSearchByOfferIDSet(offerIDSet);
			TableData dataTableOffer = (TableData)offerMap.get(DATA_NAME_DATA);
			List lstOfferRows = dataTableOffer.getLstRows();
			for (int i=0; i<lstOfferRows.size(); i++) {
				List oneOfferRow = (List)lstOfferRows.get(i);
				//
				List lstBidRows = tempMap.get(oneOfferRow.get(0));
				if (lstBidRows==null) {
					oneOfferRow.add(null);
				}
				else {
					TableData tableDataBiD = new TableData(lstHeader, lstBidRows, colSeq);
					if (bidIDStandout!=null) {
						tableDataBiD.setOfferIDStandOut(bidIDStandout);
					}
					oneOfferRow.add(tableDataBiD);
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
			//
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

	public static void main(String[] args) {
		Map<String, String[]> parameters = new HashMap<String, String[]>();
		//
		parameters.put("bidIDofferIDBuyer", new String[]{"11fe20aabc7a4025928e9522544be2e3,8f0e1575b13040f88a840a6599174cc0,user"});
		parameters.put("price", new String[]{"1900"});
		parameters.put("autoRebidLimit", new String[]{"2500"});
		//
		Map map = doModifyBid(parameters);
		//
		System.out.println(DATA_NAME_STATUS + "= " + map.get(DATA_NAME_STATUS));
		System.out.println(DATA_NAME_MESSAGE + "= " + map.get(DATA_NAME_MESSAGE));
		System.out.println(DATA_NAME_USER_TYPE + "= " + map.get(DATA_NAME_USER_TYPE));
	}
	public static void main1(String[] args) {
		Map<String, String[]> parameters = new HashMap<String, String[]>();
		//
		//parameters.put("buyerOP", new String[]{"11fe20aabc7a4025928e9522544be2e3"});
		//parameters.put("buyerVal", new String[]{OP_SZ_EQUAL});
		//
		parameters.put("bidID", new String[]{"11fe20aabc7a4025928e9522544be2e3"});
		//
		Map map = searchBid(parameters);
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
