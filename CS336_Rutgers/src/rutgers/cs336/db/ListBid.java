package rutgers.cs336.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ListBid extends DBBase {

	
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
			int    rowIndex  = -1;
			while (rs.next()) {
				Object bidID  	= rs.getObject(1);
				Object buyer 	= rs.getObject(2);
				Object price  	= rs.getObject(3);
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
			output.put(DATA_NAME_MESSAGE,
			           "ERROR: " + e.getErrorCode() + ", SQL_STATE: " + e.getSQLState() + ", DETAILS: " + exceptionToString(e));
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
			output.put(DATA_NAME_STATUS, false);
			output.put(DATA_NAME_MESSAGE,
			           "ERROR: " + "ClassNotFoundException" + ", SQL_STATE: " + e.getMessage() + ", DETAILS: " + exceptionToString(e));
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


	public static void main(String[] args) {
	}
}