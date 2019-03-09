package rutgers.cs336.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class CreateBid extends DBBase {

	public static final String PARAM_NAME_OFFER_ID         = "offerId";
	public static final String PARAM_NAME_PRICE            = "price";
	public static final String PARAM_NAME_AUTO_REBID       = "isAutoRebid";
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
			pStmtInsertBid.setString(2, getStringFromParamMap(PARAM_NAME_OFFER_ID, parameters));
			pStmtInsertBid.setString(3, userID);
			pStmtInsertBid.setBigDecimal(4, getBigDecimalFromParamMap(PARAM_NAME_PRICE, parameters));
			pStmtInsertBid.setBoolean(5, getBooleanFromParamMap(PARAM_NAME_AUTO_REBID, parameters));
			pStmtInsertBid.setBigDecimal(6, getBigDecimalFromParamMap(PARAM_NAME_AUTO_REBID_LIMIT, parameters));
			//
			pStmtInsertBid.execute();
			//
			output.put(DATA_NAME_STATUS, true);
			output.put(DATA_NAME_MESSAGE, "OK CreateBid");
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
			output.put(DATA_NAME_MESSAGE,
			           "ERROR: ErrorCode=" + e.getErrorCode() + ", SQL_STATE=" + e.getSQLState() + ", Message=" + e.getMessage() + ", ParamMap=" + getParamMap(
					             parameters));
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
			output.put(DATA_NAME_MESSAGE,
			           "ERROR: Code=" + "ClassNotFoundException" + ", Message=" + e.getMessage() + ", ParamMap=" + getParamMap(
					             parameters));
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
		initTest();
		//
		Map<String, String[]> parameters = new HashMap<>();
	}
}
