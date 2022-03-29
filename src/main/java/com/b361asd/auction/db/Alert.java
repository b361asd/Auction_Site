package com.b361asd.auction.db;

import com.b361asd.auction.gui.TableData;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Alert extends DBBase {

    private static final List<String> lstHeader_alert =
            Arrays.asList("alertID", "receiver", "offerID", "bidID", "content", "alertDate");

    private static final int[] colSeq_alert = {4, 5};

    /**
     * Select alert for a user
     *
     * @param userID User ID
     * @return Data for GUI rendering
     */
    public static Map selectAlert(String userID) {
        Map output = new HashMap();
        List lstRows = new ArrayList();
        try (Connection con = getConnection();
                PreparedStatement preparedStmt = con.prepareStatement(SQL_ALERT_SELECT); ) {
            preparedStmt.setString(1, userID);
            try (ResultSet rs = preparedStmt.executeQuery()) {
                while (rs.next()) {
                    Object alertID = rs.getObject(1);
                    Object receiver = rs.getObject(2);
                    Object offerID = rs.getObject(3);
                    Object bidID = rs.getObject(4);
                    Object content = rs.getObject(5);
                    Object alertDate = rs.getObject(6);
                    List currentRow = new LinkedList();
                    lstRows.add(currentRow);
                    currentRow.add(alertID);
                    currentRow.add(receiver);
                    currentRow.add(offerID);
                    currentRow.add(bidID);
                    currentRow.add(content);
                    currentRow.add(alertDate);
                }
            }
            TableData tableData = new TableData(lstHeader_alert, lstRows, colSeq_alert);
            output.put(DATA_NAME_DATA, tableData);
            output.put(DATA_NAME_STATUS, true);
            output.put(DATA_NAME_MESSAGE, "OK");
        } catch (SQLException e) {
            output.put(DATA_NAME_STATUS, false);
            output.put(
                    DATA_NAME_MESSAGE,
                    "ERROR: "
                            + e.getErrorCode()
                            + ", SQL_STATE: "
                            + e.getSQLState()
                            + ", DETAILS: "
                            + exceptionToString(e));
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            output.put(DATA_NAME_STATUS, false);
            output.put(
                    DATA_NAME_MESSAGE,
                    "ERROR: "
                            + "ClassNotFoundException"
                            + ", SQL_STATE: "
                            + e.getMessage()
                            + ", DETAILS: "
                            + exceptionToString(e));
            e.printStackTrace();
        }
        return output;
    }

    /**
     * Delete an alert
     *
     * @param parameters Map containing alertID to deletes
     */
    public static void deleteAlert(Map<String, String[]> parameters) {
        Map output = new HashMap();
        String alertID = getStringFromParamMap("alertID", parameters);
        try (Connection con = getConnection();
                PreparedStatement preparedStmt = con.prepareStatement(SQL_ALERT_DELETE)) {
            preparedStmt.setString(1, alertID);
            preparedStmt.execute();
            int count = preparedStmt.getUpdateCount();
            if (count == 1) {
                output.put(DATA_NAME_STATUS, true);
                output.put(DATA_NAME_MESSAGE, "OK");
            } else {
                output.put(DATA_NAME_STATUS, false);
                output.put(DATA_NAME_MESSAGE, "Failed to delete alert");
            }
        } catch (SQLException e) {
            output.put(DATA_NAME_STATUS, false);
            output.put(
                    DATA_NAME_MESSAGE,
                    "ERROR: ErrorCode="
                            + e.getErrorCode()
                            + ", SQL_STATE="
                            + e.getSQLState()
                            + ", Message="
                            + e.getMessage()
                            + ", "
                            + dumpParamMap(parameters));
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            output.put(DATA_NAME_STATUS, false);
            output.put(
                    DATA_NAME_MESSAGE,
                    "ERROR: Code="
                            + "ClassNotFoundException"
                            + ", Message="
                            + e.getMessage()
                            + ", "
                            + dumpParamMap(parameters));
            e.printStackTrace();
        }
    }
}
