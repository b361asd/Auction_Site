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

public class OfferAlert extends DBBase {
    private static final List<String> lstHeader_OfferAlert =
            Arrays.asList(
                    "criterionID",
                    "buyer",
                    "criterionName",
                    "triggerTxt",
                    "description",
                    "generateDate");

    private static final int[] colSeq_OfferAlert = {1, 2, 4, 5};

    /**
     * Generate Offer Alert Criterion
     *
     * @param userID User ID
     * @param parameters Map of all parameters
     */
    public static void doGenerateNewOfferAlertCriterion(
            String userID, Map<String, String[]> parameters) {
        Map output = new HashMap();
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
                    "ERROR=" + e.getErrorCode() + ", SQL_STATE=" + e.getSQLState());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            output.put(DATA_NAME_STATUS, false);
            output.put(
                    DATA_NAME_MESSAGE,
                    "ERROR=" + "ClassNotFoundException" + ", SQL_STATE=" + e.getMessage());
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
    public static Map selectOfferAlert(String userName, boolean isUser) {
        Map output = new HashMap();
        List<Object> lstRows = new ArrayList<>();
        TableData tableData = new TableData(lstHeader_OfferAlert, lstRows, colSeq_OfferAlert);
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
     * Delete Offer Alert
     *
     * @param parameters Map of all parameters
     */
    public static void deleteOfferAlert(Map<String, String[]> parameters) {
        Map output = new HashMap();
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
