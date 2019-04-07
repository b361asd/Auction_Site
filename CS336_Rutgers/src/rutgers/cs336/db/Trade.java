package rutgers.cs336.db;

import rutgers.cs336.gui.TableData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Trade extends DBBase {

	private static List  lstHeader_trade = Arrays.asList("Buyer", "Total", "Average", "Count");
	private static int[] colSeq_trade    = {0, 1, 2, 3};


	public static Map summaryByBuyer(Map<String, String[]> parameters) {
		int lookbackdays = getIntFromParamMap("lookbackdays", parameters);
		if (lookbackdays < 1) {
			lookbackdays = 30;
		}
		//
		Map       output    = new HashMap();
		List      lstRows   = new ArrayList();
		TableData tableData = new TableData(lstHeader_trade, lstRows, colSeq_trade);
		output.put(DATA_NAME_DATA, tableData);
		//
		Connection        con          = null;
		PreparedStatement preparedStmt = null;
		try {
			con = getConnection();
			//
			preparedStmt = con.prepareStatement(SQL_TRADE_TOTAL_BY_USER);
			//
			preparedStmt.setInt(1, lookbackdays);
			//
			ResultSet rs = preparedStmt.executeQuery();
			//
			while (rs.next()) {
				Object buyer   = rs.getObject(1);
				Object Total   = rs.getObject(2);
				Object Avarage = rs.getObject(3);
				Object Count   = rs.getObject(4);
				//
				List currentRow = new LinkedList();
				lstRows.add(currentRow);
				//
				currentRow.add(buyer);
				currentRow.add(Total);
				currentRow.add(Avarage);
				currentRow.add(Count);
			}
			//
			output.put(DATA_NAME_STATUS, true);
			output.put(DATA_NAME_MESSAGE, "OK");
			//
			tableData.setDescription("Summary Bu Buyers");
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
		Map<String, String[]> parameters = new HashMap<String, String[]>();
		//
		//parameters.put("bidID", new String[]{"11fe20aabc7a4025928e9522544be2e3"});
		//
		Map map = summaryByBuyer(parameters);
		//
		System.out.println(DATA_NAME_STATUS + "= " + map.get(DATA_NAME_STATUS));
		System.out.println(DATA_NAME_MESSAGE + "= " + map.get(DATA_NAME_MESSAGE));
		System.out.println(DATA_NAME_USER_TYPE + "= " + map.get(DATA_NAME_USER_TYPE));
	}
}
