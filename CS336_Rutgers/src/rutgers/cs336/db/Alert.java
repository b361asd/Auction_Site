package rutgers.cs336.db;

import rutgers.cs336.gui.TableData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Alert extends DBBase {

	private static List  lstHeader_alert = Arrays.asList("alertID", "receiver", "offerID", "bidID", "content", "alertDate");
	private static int[] colSeq_alert    = {4, 5};


	/**
	 * Select alert for a user
	 * @param userID User ID
	 * @return Data for GUI rendering
	 */
	public static Map selectAlert(String userID) {
		Map  output  = new HashMap();
		List lstRows = new ArrayList();
		//
		Connection        con          = null;
		PreparedStatement preparedStmt = null;
		try {
			con = getConnection();
			//
			preparedStmt = con.prepareStatement(SQL_ALERT_SELECT);
			//
			preparedStmt.setString(1, userID);
			//
			ResultSet rs = preparedStmt.executeQuery();
			//
			while (rs.next()) {
				Object alertID   = rs.getObject(1);
				Object receiver  = rs.getObject(2);
				Object offerID   = rs.getObject(3);
				Object bidID     = rs.getObject(4);
				Object content   = rs.getObject(5);
				Object alertDate = rs.getObject(6);
				//
				List currentRow = new LinkedList();
				lstRows.add(currentRow);
				//
				currentRow.add(alertID);
				currentRow.add(receiver);
				currentRow.add(offerID);
				currentRow.add(bidID);
				currentRow.add(content);
				currentRow.add(alertDate);
			}
			//
			TableData tableData = new TableData(lstHeader_alert, lstRows, colSeq_alert);
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
	 * Delete an alert
	 * @param parameters Map containing alertID to deletes
	 * @return Data for GUI rendering
	 */
	public static Map deleteAlert(Map<String, String[]> parameters) {
		Map output = new HashMap();
		//
		String alertID = getStringFromParamMap("alertID", parameters);
		//
		Connection        con          = null;
		PreparedStatement preparedStmt = null;
		try {
			con = getConnection();
			//
			preparedStmt = con.prepareStatement(SQL_ALERT_DELETE);
			preparedStmt.setString(1, alertID);
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
				output.put(DATA_NAME_MESSAGE, "Failed to delete alert");
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


	public static void main(String[] args) {
		Map<String, String[]> parameters = new HashMap<>();
		//
		parameters.put("username", new String[]{"user"});
		parameters.put("password", new String[]{"user_pwd"});
		parameters.put("email", new String[]{"user@buyme.com"});
		parameters.put("firstname", new String[]{"Real"});
		parameters.put("lastname", new String[]{"Lnuser"});
		parameters.put("address", new String[]{"123 Main St., Nowhere Town, NJ 56789"});
		parameters.put("phone", new String[]{"2365678909"});
		//
		Map map = selectAlert("user");
		//
		System.out.println(DATA_NAME_STATUS + "= " + map.get(DATA_NAME_STATUS));
		System.out.println(DATA_NAME_MESSAGE + "= " + map.get(DATA_NAME_MESSAGE));
		System.out.println(DATA_NAME_USER_TYPE + "= " + map.get(DATA_NAME_USER_TYPE));
	}
}