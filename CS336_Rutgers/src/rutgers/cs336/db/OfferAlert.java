package rutgers.cs336.db;

import rutgers.cs336.gui.TableData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class OfferAlert extends DBBase {
	private static List  lstHeader_offeralert = Arrays.asList("criterionID", "buyer", "criterionName", "triggerTxt", "description", "generateDate");
	private static int[] colSeq_offeralert    = {1, 2, 4, 5};

	/**
	 * Generate Offer Alert Criterion
	 * @param userID User ID
	 * @param parameters Map of all parameters
	 * @return Data for GUI rendering
	 */
	public static Map doGenerateNewOfferAlertCriterion(String userID, Map<String, String[]> parameters) {
		Map output = new HashMap();
		//
		String criterionName = getStringFromParamMap("criterionName", parameters);
		if (criterionName.length() == 0) {
			criterionName = "Unnamed";
		}
		//
		StringBuilder sbTrigger = Offer.formatSQLWithParametersForSearchOrAlert(parameters, userID, false);
		StringBuilder sbDesc    = Offer.formatAlertDescription(parameters, userID);
		//
		Connection        con          = null;
		PreparedStatement preparedStmt = null;
		try {
			con = getConnection();
			//
			preparedStmt = con.prepareStatement(SQL_OFFERALERTCRITERION_INSERT);
			//
			preparedStmt.setString(1, getUUID());
			preparedStmt.setString(2, userID);
			preparedStmt.setString(3, criterionName);
			preparedStmt.setString(4, sbTrigger.toString());
			preparedStmt.setString(5, sbDesc.toString());
			//
			preparedStmt.execute();
			//
			output.put(DATA_NAME_STATUS, true);
			output.put(DATA_NAME_MESSAGE, "OK");
		}
		catch (SQLException e) {
			output.put(DATA_NAME_STATUS, false);
			output.put(DATA_NAME_MESSAGE, "ERROR=" + e.getErrorCode() + ", SQL_STATE=" + e.getSQLState());
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
			output.put(DATA_NAME_STATUS, false);
			output.put(DATA_NAME_MESSAGE, "ERROR=" + "ClassNotFoundException" + ", SQL_STATE=" + e.getMessage());
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

	/**
	 * Select an Offer Alert for an user 
	 * @param userName User name
	 * @param isUser True if User
	 * @return Data for GUI rendering
	 */
	public static Map selectOfferAlert(String userName, boolean isUser) {
		Map       output    = new HashMap();
		List      lstRows   = new ArrayList();
		TableData tableData = new TableData(lstHeader_offeralert, lstRows, colSeq_offeralert);
		output.put(DATA_NAME_DATA, tableData);
		//
		Connection        con          = null;
		PreparedStatement preparedStmt = null;
		try {
			con = getConnection();
			//
			preparedStmt = con.prepareStatement(isUser ? SQL_OFFERALERTCRITERION_SELECT_USER : SQL_OFFERALERTCRITERION_SELECT_EX_USER);
			preparedStmt.setString(1, userName);
			//
			ResultSet rs = preparedStmt.executeQuery();
			//
			while (rs.next()) {
				Object criterionID   = rs.getObject(1);
				Object buyer         = rs.getObject(2);
				Object criterionName = rs.getObject(3);
				Object triggerTxt    = rs.getObject(4);
				Object description   = rs.getObject(5);
				Object generateDate  = rs.getObject(6);
				//
				List currentRow = new LinkedList();
				lstRows.add(currentRow);
				//
				currentRow.add(criterionID);
				currentRow.add(buyer);
				currentRow.add(criterionName);
				currentRow.add(triggerTxt);
				currentRow.add(description);
				currentRow.add(generateDate);
			}
			//
			output.put(DATA_NAME_DATA, tableData);
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

	/**
	 * Delete Offer Alert
	 * @param parameters Map of all parameters
	 * @return Data for GUI rendering
	 */
	public static Map deleteOfferAlert(Map<String, String[]> parameters) {
		Map output = new HashMap();
		//
		String criterionID = getStringFromParamMap("criterionID", parameters);
		//
		Connection        con          = null;
		PreparedStatement preparedStmt = null;
		try {
			con = getConnection();
			//
			preparedStmt = con.prepareStatement(SQL_OFFERALERTCRITERION_DELETE);
			preparedStmt.setString(1, criterionID);
			//
			preparedStmt.execute();
			//
			int count = preparedStmt.getUpdateCount();
			if (count == 1) {
				output.put(DATA_NAME_STATUS, true);
				output.put(DATA_NAME_MESSAGE, "OK");
			}
			else {
				output.put(DATA_NAME_STATUS, false);
				output.put(DATA_NAME_MESSAGE, "Failed to delete criterionID");
			}
		}
		catch (SQLException e) {
			output.put(DATA_NAME_STATUS, false);
			output.put(DATA_NAME_MESSAGE, "ERROR: ErrorCode=" + e.getErrorCode() + ", SQL_STATE=" + e.getSQLState() + ", Message=" + e.getMessage() + ", " + dumpParamMap(parameters));
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
			output.put(DATA_NAME_STATUS, false);
			output.put(DATA_NAME_MESSAGE, "ERROR: Code=" + "ClassNotFoundException" + ", Message=" + e.getMessage() + ", " + dumpParamMap(parameters));
			e.printStackTrace();
		}
		finally {
			if (preparedStmt != null) {
				try {
					preparedStmt.close();
				}
				catch (Throwable t) {
					t.printStackTrace();
				}
			}
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


}
