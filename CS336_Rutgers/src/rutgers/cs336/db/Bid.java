package rutgers.cs336.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import rutgers.cs336.gui.Helper;
import rutgers.cs336.gui.TableData;

public class Bid extends DBBase {


	private static final int FIELD_START_INDEX = 12;

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
			lstHeader.add("buyer");
			lstHeader.add("price");
			lstHeader.add("autoRebidLimit");
			lstHeader.add("bidDate");
			lstHeader.add("offerID");
			lstHeader.add("seller");
			lstHeader.add("categoryName");
			lstHeader.add("conditionCode");
			lstHeader.add("description");
			lstHeader.add("initPrice");
			lstHeader.add("increment");
			lstHeader.add("minPrice");
			lstHeader.add("startDate");
			lstHeader.add("endDate");
			lstHeader.add("status");
			lstHeader.add("currPrice");
			//
			while (rs.next()) {
				Object bidID       		= rs.getObject(1);
				Object buyer       		= rs.getObject(2);
				Object price       		= rs.getObject(3);
				Object autoRebidLimit   = rs.getObject(4);
				Object bidDate       	= rs.getObject(5);
				Object offerID       	= rs.getObject(6);
				Object seller       		= rs.getObject(7);
				Object categoryName     = rs.getObject(8);
				Object conditionCode    = rs.getObject(9);
				Object description      = rs.getObject(10);
				Object initPrice       	= rs.getObject(11);
				Object increment       	= rs.getObject(12);
				Object minPrice       	= rs.getObject(13);
				Object startDate       	= rs.getObject(14);
				Object endDate       	= rs.getObject(15);
				Object status       		= rs.getObject(16);
				Object currPrice       	= rs.getObject(17);
				//
				List currentRow = new LinkedList();
				lstRows.add(currentRow);
				//
				currentRow.add(bidID);
				currentRow.add(buyer);
				currentRow.add(price);
				currentRow.add(autoRebidLimit);
				currentRow.add(bidDate);
				currentRow.add(offerID);
				currentRow.add(seller);
				currentRow.add(categoryName);
				currentRow.add(Helper.getConditionFromCode(conditionCode.toString()));
				currentRow.add(description);
				currentRow.add(initPrice);
				currentRow.add(increment);
				currentRow.add(minPrice);
				currentRow.add(startDate);
				currentRow.add(endDate);
				currentRow.add(status);
				currentRow.add(currPrice);
			}
			//
			int[] colSeq = new int[lstHeader.size() - 2];
			{
				colSeq[0] = 1;      //
				colSeq[1] = 2;      //
				colSeq[2] = 3;      //
				colSeq[3] = 4;      //
				colSeq[4] = 6;      //
				colSeq[5] = 7;      //
				colSeq[6] = 8;      //
				colSeq[7] = 9;      //
				colSeq[8] = 10;      //
				colSeq[9] = 11;      //
				colSeq[10] = 12;      //
				colSeq[11] = 13;      //
				colSeq[12] = 14;      //
				colSeq[13] = 15;      //
				colSeq[14] = 16;      //
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
