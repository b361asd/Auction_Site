package rutgers.cs336.db;

import rutgers.cs336.gui.TableData;

import java.sql.*;
import java.util.*;

public class Bid extends DBBase {

	//SQL_BID_SELECT_EX
	public static Map getBidToModify(Map<String, String[]> parameters) {
		Map output = new HashMap();
		//
		String bidID  = getStringFromParamMap("bidID", parameters);
		//
		Connection        con          = null;
		PreparedStatement preparedStmt = null;
		try {
			con = getConnection();
			//
			preparedStmt = con.prepareStatement(SQL_BID_SELECT_EX);
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
		FormatterBidQuery.doneQuerySearch(sb);
		//
		return doSearchBidInternal(sb.toString());
	}
	private static Map doSearchBidInternal(String sql) {
		//
		Map output = new HashMap();
		//
		List lstHeader = new ArrayList();
		List lstRows   = new ArrayList();
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
			while (rs.next()) {
				Object bidID       		= rs.getObject(1);
				Object offerID       	= rs.getObject(2);
				Object buyer       		= rs.getObject(3);
				Object price       		= rs.getObject(4);
				Object autoRebidLimit   = rs.getObject(5);
				Object bidDate       	= rs.getObject(6);
				//
				List currentRow = new LinkedList();
				lstRows.add(currentRow);
				//
				currentRow.add(bidID);
				currentRow.add(offerID);
				currentRow.add(buyer);
				currentRow.add(price);
				currentRow.add(autoRebidLimit);
				currentRow.add(bidDate);
			}
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
			TableData dataTable = new TableData(lstHeader, lstRows, colSeq);
			//
			output.put(DATA_NAME_DATA, dataTable);
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
		Map map = searchBid(null);
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
