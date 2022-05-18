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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class OfferAlert extends DBBase {
    private static final List<String> LST_HEADER_OFFER_ALERT =
            Arrays.asList(
                    "criterionID",
                    "buyer",
                    "criterionName",
                    "triggerTxt",
                    "description",
                    "generateDate");

    private static final int[] COL_SEQ_OFFER_ALERT = {1, 2, 4, 5};

    /**
     * Generate Offer Alert Criterion
     *
     * @param userID User ID
     * @param parameters Map of all parameters
     */
    public static void doGenerateNewOfferAlertCriterion(
            String userID, Map<String, String[]> parameters) {
        Map<String, Object> output = new HashMap<>();
        String criterionName = getStringFromParamMap("criterionName", parameters);
        if (criterionName.length() == 0) {
            criterionName = "Unnamed";
        }
        StringBuilder sbTrigger = Offer.formatSQLWithParametersForSearchOrAlert(parameters, false);
        StringBuilder sbDesc = Offer.formatAlertDescription(parameters);
        try (Connection con = getConnection();
                PreparedStatement preparedStmt =
                        con.prepareStatement(ISQLConstant.SQL_OFFERALERTCRITERION_INSERT)) {
            preparedStmt.setString(1, getUUID());
            preparedStmt.setString(2, userID);
            preparedStmt.setString(3, criterionName);
            preparedStmt.setString(4, sbTrigger.toString());
            preparedStmt.setString(5, sbDesc.toString());
            preparedStmt.execute();
            output.put(DATA_NAME_STATUS, true);
            output.put(DATA_NAME_MESSAGE, "OK");
        } catch (SQLException e) {
            output.put(DATA_NAME_STATUS, false);
            output.put(
                    DATA_NAME_MESSAGE,
                    MessageFormat.format(
                            "ERROR={0}, SQL_STATE={1}", e.getErrorCode(), e.getSQLState()));
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            output.put(DATA_NAME_STATUS, false);
            output.put(
                    DATA_NAME_MESSAGE,
                    MessageFormat.format(
                            "ERROR=ClassNotFoundException, SQL_STATE={0}", e.getMessage()));
            e.printStackTrace();
        }
    }

    /**
     * Select an Offer Alert for a user
     *
     * @param userName User name
     * @param isUser True if User
     * @return Data for GUI rendering
     */
    public static Map<String, Object> selectOfferAlert(String userName, boolean isUser) {
        Map<String, Object> output = new HashMap<>();
        List<Object> lstRows = new ArrayList<>();
        TableData tableData = new TableData(LST_HEADER_OFFER_ALERT, lstRows, COL_SEQ_OFFER_ALERT);
        output.put(DATA_NAME_DATA, tableData);
        try (Connection con = getConnection();
                PreparedStatement preparedStmt =
                        con.prepareStatement(
                                isUser
                                        ? ISQLConstant.SQL_OFFERALERTCRITERION_SELECT_USER
                                        : ISQLConstant.SQL_OFFERALERTCRITERION_SELECT_EX_USER)) {
            preparedStmt.setString(1, userName);
            try (ResultSet rs = preparedStmt.executeQuery()) {
                while (rs.next()) {
                    Object criterionID = rs.getObject(1);
                    Object buyer = rs.getObject(2);
                    Object criterionName = rs.getObject(3);
                    Object triggerTxt = rs.getObject(4);
                    Object description = rs.getObject(5);
                    Object generateDate = rs.getObject(6);
                    List<Object> currentRow = new LinkedList<>();
                    lstRows.add(currentRow);
                    currentRow.add(criterionID);
                    currentRow.add(buyer);
                    currentRow.add(criterionName);
                    currentRow.add(triggerTxt);
                    currentRow.add(description);
                    currentRow.add(generateDate);
                }
            }
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
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            output.put(DATA_NAME_STATUS, false);
            output.put(
                    DATA_NAME_MESSAGE,
                    MessageFormat.format(
                            "ERROR: ClassNotFoundException, SQL_STATE: {0}, DETAILS: {1}",
                            e.getMessage(), exceptionToString(e)));
            e.printStackTrace();
        }
        return output;
    }

    /**
     * Delete Offer Alert
     *
     * @param parameters Map of all parameters
     */
    public static void deleteOfferAlert(Map<String, String[]> parameters) {
        Map<String, Object> output = new HashMap<>();
        String criterionID = getStringFromParamMap("criterionID", parameters);
        try (Connection con = getConnection();
                PreparedStatement preparedStmt =
                        con.prepareStatement(ISQLConstant.SQL_OFFERALERTCRITERION_DELETE)) {
            preparedStmt.setString(1, criterionID);
            preparedStmt.execute();
            int count = preparedStmt.getUpdateCount();
            if (count == 1) {
                output.put(DATA_NAME_STATUS, true);
                output.put(DATA_NAME_MESSAGE, "OK");
            } else {
                output.put(DATA_NAME_STATUS, false);
                output.put(DATA_NAME_MESSAGE, "Failed to delete criterionID");
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
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            output.put(DATA_NAME_STATUS, false);
            output.put(
                    DATA_NAME_MESSAGE,
                    MessageFormat.format(
                            "ERROR: Code=ClassNotFoundException, Message={0}, {1}",
                            e.getMessage(), dumpParamMap(parameters)));
            e.printStackTrace();
        }
    }
}
