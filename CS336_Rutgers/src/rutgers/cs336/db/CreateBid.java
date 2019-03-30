package rutgers.cs336.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class CreateBid extends DBBase {

	public static final String PARAM_NAME_OFFER_ID         = "offerId";
	public static final String PARAM_NAME_PRICE            = "price";
	public static final String PARAM_NAME_AUTO_REBID_LIMIT = "autoRebidLimit";

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
			pStmtInsertBid.setBigDecimal(3, getBigDecimalFromParamMap(PARAM_NAME_PRICE, parameters));
			pStmtInsertBid.setBigDecimal(4, getBigDecimalFromParamMap(PARAM_NAME_AUTO_REBID_LIMIT, parameters));
			pStmtInsertBid.setBigDecimal(5, getBigDecimalFromParamMap(PARAM_NAME_PRICE, parameters));
			pStmtInsertBid.setString(6, getStringFromParamMap(PARAM_NAME_OFFER_ID, parameters));
			pStmtInsertBid.setBigDecimal(7, getBigDecimalFromParamMap(PARAM_NAME_PRICE, parameters));
			pStmtInsertBid.setString(8, getStringFromParamMap(PARAM_NAME_OFFER_ID, parameters));
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


	public static void main(String[] args) {
		//
		Map<String, String[]> parameters = new HashMap<>();
	}
}
