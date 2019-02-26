package rutgers.cs336.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class CreateOffer extends DBBase implements IConstant {

	public static final String PREFIX_FIELD_ID      = "fieldID_";
	public static final String PREFIX_CATEGORY_NAME = "categoryName";


	private static final String sqlInsertOffer = "insert Offer (offerId, categoryName, seller, initPrice, increment, minPrice, conditionCode, description, startDate, endDate, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW(), DATE_ADD(NOW(), INTERVAL + ? DAY), 1)";

	private static final String sqlInsertOfferField = "insert OfferField (offerId, fieldID, fieldText) VALUES (?, ?, ?)";

	public static Map doCreateOffer(String userID, Map<String, String[]> parameters) {
		String debugInfo = "START";
		//
		Map output = new HashMap();
		//
		String offerID = getUUID();
		//
		Connection        con                   = null;
		PreparedStatement pStmtInsertOffer      = null;
		PreparedStatement pStmtInsertOfferField = null;
		try {
			con = getConnection();
			con.setAutoCommit(false);
			//
			pStmtInsertOffer = con.prepareStatement(sqlInsertOffer);
			pStmtInsertOffer.setString(1, offerID);
			pStmtInsertOffer.setString(2, getStringFromParamMap("categoryName", parameters));
			pStmtInsertOffer.setString(3, userID);
			pStmtInsertOffer.setBigDecimal(4, getBigDecimalFromParamMap("initPrice", parameters));
			pStmtInsertOffer.setBigDecimal(5, getBigDecimalFromParamMap("increment", parameters));
			pStmtInsertOffer.setBigDecimal(6, getBigDecimalFromParamMap("minPrice", parameters));
			pStmtInsertOffer.setInt(7, getPrefixIntFromParamMap("conditionCode", parameters, '_'));
			pStmtInsertOffer.setString(8, getStringFromParamMap("description", parameters));
			pStmtInsertOffer.setInt(9, getIntFromParamMap("auction_days", parameters));
			//
			pStmtInsertOffer.execute();
			//
			pStmtInsertOfferField = con.prepareStatement(sqlInsertOfferField);
			//
			for (String s : parameters.keySet()) {
				if (s.startsWith(PREFIX_FIELD_ID)) {
					int    fieldID = Integer.parseInt(s.substring(PREFIX_FIELD_ID.length()));
					String value   = parameters.get(s)[0];
					//
					debugInfo = "Processing|" + offerID + "|" + s + "|" + fieldID + "|" + value + "|";
					//
					pStmtInsertOfferField.setString(1, offerID);
					pStmtInsertOfferField.setInt(2, fieldID);
					pStmtInsertOfferField.setString(3, value);
					pStmtInsertOfferField.execute();
				}
			}
			//
			output.put(DATA_NAME_STATUS, true);
			output.put(DATA_NAME_MESSAGE, "OK");
			//
			con.commit();
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
					             parameters) + ", DebugInfo=" + debugInfo);
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
			if (pStmtInsertOffer != null) {
				try {
					pStmtInsertOffer.close();
				}
				catch (Throwable t) {
					t.printStackTrace();
				}
			}
			//
			if (pStmtInsertOfferField != null) {
				try {
					pStmtInsertOfferField.close();
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


