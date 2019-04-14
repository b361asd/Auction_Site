package rutgers.cs336.db;

import rutgers.cs336.gui.TableData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Trade extends DBBase {

	private static List  lstHeader_tradebybuyer  = Arrays.asList("Buyer", "Total", "Average", "Count");
	private static List  lstHeader_tradebyseller = Arrays.asList("Seller", "Total", "Average", "Count");
	private static List  lstHeader_tradebyuser   = Arrays.asList("User", "Total", "Average", "Count");
	private static int[] colSeq_trade            = {0, 1, 2, 3};

	public static Map summaryByBuyerSeller(int lookbackdays, boolean isBuyer, boolean isSeller, boolean isUser) {
		Map  output  = new HashMap();
		List lstRows = new ArrayList();
		//
		List lstHeader = null;
		if (isBuyer) {
			lstHeader = lstHeader_tradebybuyer;
		}
		else if (isSeller) {
			lstHeader = lstHeader_tradebyseller;
		}
		else if (isUser) {
			lstHeader = lstHeader_tradebyuser;
		}
		//
		TableData tableData = new TableData(lstHeader, lstRows, colSeq_trade);
		output.put(DATA_NAME_DATA, tableData);
		//
		Connection        con          = null;
		PreparedStatement preparedStmt = null;
		try {
			con = getConnection();
			//
			String sql = null;
			if (isBuyer) {
				sql = SQL_TRADE_TOTAL_BY_BUYER;
			}
			else if (isSeller) {
				sql = SQL_TRADE_TOTAL_BY_SELLER;
			}
			else if (isUser) {
				sql = SQL_TRADE_TOTAL_BY_USER;
			}
			//
			preparedStmt = con.prepareStatement(sql);
			preparedStmt.setInt(1, lookbackdays);
			//
			if (!isBuyer && !isSeller) {
				if (isUser) {
					preparedStmt.setInt(2, lookbackdays);
				}
			}
			//
			ResultSet rs = preparedStmt.executeQuery();
			//
			while (rs.next()) {
				Object person  = rs.getObject(1);
				Object Total   = rs.getObject(2);
				Object Average = rs.getObject(3);
				Object Count   = rs.getObject(4);
				//
				List currentRow = new LinkedList();
				lstRows.add(currentRow);
				//
				currentRow.add(person);
				currentRow.add(Total);
				currentRow.add(Average);
				currentRow.add(Count);
			}
			//
			output.put(DATA_NAME_STATUS, true);
			output.put(DATA_NAME_MESSAGE, "OK");
			//
			if (isBuyer) {
				tableData.setDescription("Sales by Buyers For The Last " + lookbackdays + " Days");
			}
			else if (isSeller) {
				tableData.setDescription("Sales by Sellers For The Last " + lookbackdays + " Days");
			}
			else if (isUser) {
				tableData.setDescription("Sales by Users (as Either Buyer or Seller) For The Last " + lookbackdays + " Days");
			}
		}
		catch (SQLException e) {
			output.put(DATA_NAME_STATUS, false);
			output.put(DATA_NAME_MESSAGE, "ERROR=" + e.getErrorCode() + ", SQL_STATE=" + e.getSQLState());
			tableData.setDescription((String) output.get(DATA_NAME_MESSAGE));
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
			output.put(DATA_NAME_STATUS, false);
			output.put(DATA_NAME_MESSAGE, "ERROR=" + "ClassNotFoundException" + ", SQL_STATE=" + e.getMessage());
			tableData.setDescription((String) output.get(DATA_NAME_MESSAGE));
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


	public static void main(String[] args) {
		Map<String, String[]> parameters = new HashMap<>();
		//
		parameters.put("bidID", new String[]{"11fe20aabc7a4025928e9522544be2e3"});
		//
		Map map = summaryByBuyerSeller(30, false, false, true);
		//
		System.out.println(DATA_NAME_STATUS + "= " + map.get(DATA_NAME_STATUS));
		System.out.println(DATA_NAME_MESSAGE + "= " + map.get(DATA_NAME_MESSAGE));
		System.out.println(DATA_NAME_USER_TYPE + "= " + map.get(DATA_NAME_USER_TYPE));
	}
}
