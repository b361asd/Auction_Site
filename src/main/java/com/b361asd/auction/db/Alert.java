package com.b361asd.auction.db;

import com.b361asd.auction.gui.TableData;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Alert extends DBBase {

    private static final Logger LOGGER = Logger.getLogger(Alert.class.getName());

    private static final List<String> LST_HEADER_ALERT =
            Arrays.asList("alertID", "receiver", "offerID", "bidID", "content", "alertDate");

    private static final int[] COL_SEQ_ALERT = {4, 5};

    /**
     * Select alert for a user
     *
     * @param userID User ID
     * @return Data for GUI rendering
     */
    public static Map<String, Object> selectAlert(String userID) {
        Map<String, Object> output = new HashMap<>();
        List<Object> lstRows = new ArrayList<>();
        try (Connection con = getConnection();
                PreparedStatement preparedStmt = con.prepareStatement(SQL_ALERT_SELECT)) {
            preparedStmt.setString(1, userID);
            try (ResultSet rs = preparedStmt.executeQuery()) {
                while (rs.next()) {
                    Object alertID = rs.getObject(1);
                    Object receiver = rs.getObject(2);
                    Object offerID = rs.getObject(3);
                    Object bidID = rs.getObject(4);
                    Object content = rs.getObject(5);
                    Object alertDate = rs.getObject(6);
                    List<Object> currentRow = new ArrayList<>();
                    lstRows.add(currentRow);
                    currentRow.add(alertID);
                    currentRow.add(receiver);
                    currentRow.add(offerID);
                    currentRow.add(bidID);
                    currentRow.add(content);
                    currentRow.add(alertDate);
                }
            }
            TableData tableData = new TableData(LST_HEADER_ALERT, lstRows, COL_SEQ_ALERT);
            output.put(DATA_NAME_DATA, tableData);
            output.put(DATA_NAME_STATUS, true);
            output.put(DATA_NAME_MESSAGE, "OK");
        } catch (SQLException e) {
            output.put(DATA_NAME_STATUS, false);
            output.put(
                    DATA_NAME_MESSAGE,
                    MessageFormat.format(
                            "ERROR: {0}, SQL_STATE: {1}, DETAILS: {2}",
                            e.getErrorCode(), e.getSQLState(), exceptionToString(e)));
            LOGGER.log(Level.SEVERE, e.toString(), e);
        } catch (ClassNotFoundException e) {
            output.put(DATA_NAME_STATUS, false);
            output.put(
                    DATA_NAME_MESSAGE,
                    MessageFormat.format(
                            "ERROR: ClassNotFoundException, SQL_STATE: {0}, DETAILS: {1}",
                            e.getMessage(), exceptionToString(e)));
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
        return output;
    }

    /**
     * Delete an alert
     *
     * @param parameters Map containing alertID to deletes
     */
    public static void deleteAlert(Map<String, String[]> parameters) {
        Map<String, Object> output = new HashMap<>();
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
                    MessageFormat.format(
                            "ERROR: ErrorCode={0}, SQL_STATE={1}, Message={2}, {3}",
                            e.getErrorCode(),
                            e.getSQLState(),
                            e.getMessage(),
                            dumpParamMap(parameters)));
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            output.put(DATA_NAME_STATUS, false);
            output.put(
                    DATA_NAME_MESSAGE,
                    MessageFormat.format(
                            "ERROR: Code=ClassNotFoundException, Message={0}, {1}",
                            e.getMessage(), dumpParamMap(parameters)));
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
