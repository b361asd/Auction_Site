package rutgers.cs336.db;

import rutgers.cs336.gui.Helper;
import rutgers.cs336.gui.TableData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Trade extends DBBase {
	private static List  lstHeader_tradetotal             = Arrays.asList("Period", "Total", "Average", "Count");
	private static List  lstHeader_tradebycategoryname    = Arrays.asList("categoryName", "Total", "Average", "Count");
	private static List  lstHeader_tradebysimilar         = Arrays.asList("Similar", "Total", "Average", "Count");
	private static List  lstHeader_tradebybuyer           = Arrays.asList("Buyer", "Total", "Average", "Count");
	private static List  lstHeader_tradebyseller          = Arrays.asList("Seller", "Total", "Average", "Count");
	private static List  lstHeader_tradebyuser            = Arrays.asList("User", "Total", "Average", "Count");
	private static int[] colSeq_tradeby                   = {0, 1, 2, 3};
	//
	private static List  lstHeader_tradebybestsellingitem = Arrays.asList("price", "categoryName", "conditionCode", "description", "seller", "buyer", "tradeDate");
	private static int[] colSeq_tradebybestsellingitem    = {0, 1, 2, 3, 4, 5, 6};


	public static Map summaryBy(int lookbackdays, boolean isTotal, boolean isCategoryName, boolean isBuyer, boolean isSeller, boolean isUser) {
		Map  output  = new HashMap();
		List lstRows = new ArrayList();
		//
		List lstHeader = null;
		if (isTotal) {
			lstHeader = lstHeader_tradetotal;
		}
		else if (isCategoryName) {
			lstHeader = lstHeader_tradebycategoryname;
		}
		else if (isBuyer) {
			lstHeader = lstHeader_tradebybuyer;
		}
		else if (isSeller) {
			lstHeader = lstHeader_tradebyseller;
		}
		else if (isUser) {
			lstHeader = lstHeader_tradebyuser;
		}
		//
		TableData tableData = new TableData(lstHeader, lstRows, colSeq_tradeby);
		output.put(DATA_NAME_DATA, tableData);
		//
		Connection        con          = null;
		PreparedStatement preparedStmt = null;
		try {
			con = getConnection();
			//
			String sql = null;
			if (isTotal) {
				sql = SQL_TRADE_TOTAL;
			}
			else if (isCategoryName) {
				sql = SQL_TRADE_TOTAL_BY_CATEGORY;
			}
			else if (isBuyer) {
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
			//
			if (isTotal) {
			}
			else if (isCategoryName) {
				preparedStmt.setInt(1, lookbackdays);
			}
			else if (isBuyer) {
				preparedStmt.setInt(1, lookbackdays);
			}
			else if (isSeller) {
				preparedStmt.setInt(1, lookbackdays);
			}
			else if (isUser) {
				preparedStmt.setInt(1, lookbackdays);
				preparedStmt.setInt(2, lookbackdays);
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
			if (isTotal) {
				tableData.setDescription("Total Sales For Various Periods");
			}
			else if (isCategoryName) {
				tableData.setDescription("Sales by CategoryNames For The Last " + lookbackdays + " Days");
			}
			else if (isBuyer) {
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


	public static Map selectGroupSimilar(int lookbackdays) {
		Map  output  = new HashMap();
		List lstRows = new ArrayList();
		//
		TableData tableData = new TableData(lstHeader_tradebysimilar, lstRows, colSeq_tradeby);
		output.put(DATA_NAME_DATA, tableData);
		//
		Connection        con          = null;
		PreparedStatement preparedStmt = null;
		try {
			con = getConnection();
			//
			preparedStmt = con.prepareStatement(SQL_TRADE_TOTAL_BY_SIMILARGROUP);
			preparedStmt.setInt(1, lookbackdays);
			ResultSet rs = preparedStmt.executeQuery();
			//
			while (rs.next()) {
				Object categoryName  = rs.getObject(1);
				Object conditionCode = rs.getObject(2);
				Object Total         = rs.getObject(3);
				Object Average       = rs.getObject(4);
				Object Count         = rs.getObject(5);
				//
				List currentRow = new LinkedList();
				lstRows.add(currentRow);
				//
				currentRow.add(categoryName + ", " + Helper.getConditionFromCode(conditionCode.toString()));
				currentRow.add(Total);
				currentRow.add(Average);
				currentRow.add(Count);
			}
			//
			output.put(DATA_NAME_STATUS, true);
			output.put(DATA_NAME_MESSAGE, "OK");
			//
			tableData.setDescription("Sales Grouped by Similars For The Last " + lookbackdays + " Days");
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


	public static Map selectBestSellingItems(int lookbackdays, int limit) {
		Map  output  = new HashMap();
		List lstRows = new ArrayList();
		//
		TableData tableData = new TableData(lstHeader_tradebybestsellingitem, lstRows, colSeq_tradebybestsellingitem);
		output.put(DATA_NAME_DATA, tableData);
		//
		Connection        con          = null;
		PreparedStatement preparedStmt = null;
		try {
			con = getConnection();
			//
			preparedStmt = con.prepareStatement(SQL_TRADE_BEST_ITEM);
			preparedStmt.setInt(1, lookbackdays);
			preparedStmt.setInt(2, limit);
			ResultSet rs = preparedStmt.executeQuery();
			//
			while (rs.next()) {
				Object price         = rs.getObject(1);
				Object categoryName  = rs.getObject(2);
				Object conditionCode = rs.getObject(3);
				Object description   = rs.getObject(4);
				Object seller        = rs.getObject(5);
				Object buyer         = rs.getObject(6);
				Object tradeDate     = rs.getObject(7);
				//
				List currentRow = new LinkedList();
				lstRows.add(currentRow);
				//
				currentRow.add(price);
				currentRow.add(categoryName);
				currentRow.add(Helper.getConditionFromCode(conditionCode.toString()));
				currentRow.add(description);
				currentRow.add(seller);
				currentRow.add(buyer);
				currentRow.add(tradeDate);
			}
			//
			output.put(DATA_NAME_STATUS, true);
			output.put(DATA_NAME_MESSAGE, "OK");
			//
			tableData.setDescription("Top " + limit + " Best Selling Item(s) For The Last " + lookbackdays + " Days");
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
		//Map map = summaryByBuyerSellerUser(30, false, false, true);
		Map map = selectBestSellingItems(30, 10);
		//
		System.out.println(DATA_NAME_STATUS + "= " + map.get(DATA_NAME_STATUS));
		System.out.println(DATA_NAME_MESSAGE + "= " + map.get(DATA_NAME_MESSAGE));
		System.out.println(DATA_NAME_USER_TYPE + "= " + map.get(DATA_NAME_USER_TYPE));
	}
}