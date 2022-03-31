package com.b361asd.auction.db;

import com.b361asd.auction.gui.Helper;
import com.b361asd.auction.gui.TableData;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Offer extends DBBase {

    private static final int FIELD_START_INDEX = 12;

    public static List<String> lstHeader_OfferDefault =
            Arrays.asList(
                    "offerId",
                    "Seller",
                    "Category",
                    "Condition",
                    "Desc",
                    "initPrice",
                    "increment",
                    "minPrice",
                    "Start",
                    "End",
                    "status",
                    "CurrBid");

    public static int[] colSeq_OfferDefault = {2, 3, 4, 1, 5, 6, 11, 8, 9, 10};

    /**
     * Get Offers records for a set of offerIDs
     *
     * @param offerIDSet Set of offerIDs
     * @param showAll If true, include minPrice.
     * @return Data for GUI rendering
     */
    public static Map doSearchByOfferIDSet(Set<String> offerIDSet, boolean showAll) {
        StringBuilder sb;
        sb = FormatterOfferQuery.initQuerySearch();
        addCondition(
                sb,
                "o.offerID",
                OP_SZ_EQUAL_MULTI_NO_ESCAPE,
                getListOfStringsFromSet(offerIDSet, "'"),
                null);
        FormatterOfferQuery.doneQuerySearch(sb);
        Map output = doSearchOfferInternal(sb.toString(), showAll);
        TableData dataTable = (TableData) output.get(DATA_NAME_DATA);
        if (dataTable != null) {
            dataTable.setDescription("Search Bids");
        }
        return output;
    }

    /**
     * Get Offer records for a user (including all records, active or closed)
     *
     * @param userID User ID
     * @return Data for GUI rendering
     */
    public static Map doSearchUserActivity(String userID) {
        String sql = FormatterOfferQuery.buildSQLUserActivityOffer(userID);
        Map output = doSearchOfferInternal(sql, false);
        TableData dataTable = (TableData) output.get(DATA_NAME_DATA);
        if (dataTable != null) {
            dataTable.setDescription("User Activity");
        }
        return output;
    }

    /**
     * Get Offer records for a user (active only)
     *
     * @param userID User ID
     * @return Data for GUI rendering
     */
    public static Map doSearchMyOffer(String userID) {
        StringBuilder sb = FormatterOfferQuery.initQuerySearch();
        addCondition(sb, "o.seller", OP_SZ_EQUAL, userID, null);
        addCondition(sb, "o.status", OP_INT_EQUAL, "1", null);
        FormatterOfferQuery.doneQuerySearch(sb);
        String sql = sb.toString();
        Map output = doSearchOfferInternal(sql, true);
        TableData dataTable = (TableData) output.get(DATA_NAME_DATA);
        if (dataTable != null) {
            dataTable.setDescription("My Offers");
        }
        return output;
    }

    /**
     * Get similar Offer records for a user (active only)
     *
     * @param offerIDCategoryNameConditionCode Offer ID + Category Name + Condition Code
     * @return Data for GUI rendering
     */
    public static Map doSearchSimilar(String offerIDCategoryNameConditionCode) {
        String[] temp = offerIDCategoryNameConditionCode.split(",");
        String categoryName = temp[1];
        String conditionCode = Helper.getCodeFromCondition(temp[2]);
        String sql = FormatterOfferQuery.buildSQLSimilarOffer(categoryName, conditionCode);
        Map output = doSearchOfferInternal(sql, false);
        TableData dataTable = (TableData) output.get(DATA_NAME_DATA);
        if (dataTable != null) {
            dataTable.setStandOut(temp[0], 0);
            dataTable.setDescription("Similar Offers");
        }
        return output;
    }

    /**
     * Get Offer by Offer ID. Used in postBid (user) and modifyOffer (rep)
     *
     * @param offerID Offer ID
     * @param showAll True will show minPrice
     * @return Data for GUI rendering
     */
    public static Map doSearchOfferByID(String offerID, boolean showAll) {
        StringBuilder sb = FormatterOfferQuery.initQuerySearch();
        addCondition(sb, "o.offerID", OP_SZ_EQUAL, offerID, null);
        addCondition(sb, "o.status", OP_INT_EQUAL, "1", null);
        FormatterOfferQuery.doneQuerySearch(sb);
        String sql = sb.toString();
        Map output = doSearchOfferInternal(sql, showAll);
        TableData dataTable = (TableData) output.get(DATA_NAME_DATA);
        if (dataTable != null) {
            dataTable.setDescription("Bids for One Offer");
        }
        return output;
    }

    /**
     * Browse all active offers
     *
     * @return Data for GUI rendering
     */
    public static Map doBrowseOffer() {
        String sql = FormatterOfferQuery.buildSQLBrowseOffer();
        Map output = doSearchOfferInternal(sql, false);
        TableData dataTable = (TableData) output.get(DATA_NAME_DATA);
        if (dataTable != null) {
            dataTable.setDescription("Browse Offer");
        }
        return output;
    }

    /**
     * Perform search
     *
     * @param parameters Map of all parameters
     * @param showAll True will show minPrice
     * @return Data for GUI rendering
     */
    public static Map doSearchOffer(Map<String, String[]> parameters, boolean showAll) {
        StringBuilder sb = formatSQLWithParametersForSearchOrAlert(parameters, true);
        String sql = sb.toString();
        Map output = doSearchOfferInternal(sql, showAll);
        TableData dataTable = (TableData) output.get(DATA_NAME_DATA);
        if (dataTable != null) {
            dataTable.setDescription("Search Offer");
        }
        return output;
    }

    /**
     * Format SQL for searching offers
     *
     * @param parameters Map of all parameters
     * @param isSearch If true, will search. Otherwise, alert criteria for user interests
     * @return Formatted SQL
     */
    public static StringBuilder formatSQLWithParametersForSearchOrAlert(
            Map<String, String[]> parameters, boolean isSearch) {
        StringBuilder sb;
        if (isSearch) {
            sb = FormatterOfferQuery.initQuerySearch();
        } else {
            sb = FormatterOfferQuery.initQueryAlert();
        }
        String sellerOP = getStringFromParamMap("sellerOP", parameters);
        String sellerVal = getStringFromParamMap("sellerVal", parameters);
        addCondition(sb, "o.seller", sellerOP, sellerVal, null);
        String categoryNames = getListOfStringsFromParamMap("categoryName", 1, parameters, "'");
        String categoryNameOP = OP_SZ_EQUAL_MULTI_NO_ESCAPE;
        addCondition(sb, "o.categoryName", categoryNameOP, categoryNames, null);
        StringBuilder lstConditionCode = new StringBuilder();
        for (int i = 1; i <= 6; i++) {
            String temp = getStringFromParamMap("conditionCode_" + i, parameters);
            if (temp.length() > 0) {
                if (lstConditionCode.toString().equals("")) {
                    lstConditionCode = new StringBuilder("" + i);
                } else {
                    lstConditionCode.append(",").append(i);
                }
            }
        }
        addCondition(sb, "o.conditionCode", OP_INT_EQUAL_MULTI, lstConditionCode.toString(), null);
        String descriptionOP = getStringFromParamMap("descriptionOP", parameters);
        String descriptionVal = getStringFromParamMap("descriptionVal", parameters);
        addCondition(sb, "o.description", descriptionOP, descriptionVal, null);
        String statusOP = OP_INT_EQUAL;

        // Active
        String statusVal = "1";

        addCondition(sb, "o.status", statusOP, statusVal, null);
        FormatterOfferQuery.initFieldCondition(sb);
        String lstFieldIDs = getStringFromParamMap("lstFieldIDs", parameters);
        String[] fieldIDs = lstFieldIDs.split(",");
        for (String fieldID : fieldIDs) {
            String fieldOP = getStringFromParamMap("fieldop_" + fieldID, parameters);
            String fieldVal1 = getStringFromParamMap("fieldval1_" + fieldID, parameters);
            String fieldVal2 = getStringFromParamMap("fieldval2_" + fieldID, parameters);
            addFieldCondition(sb, fieldID, fieldOP, fieldVal1, fieldVal2);
        }
        FormatterOfferQuery.doneFieldCondition(sb);
        if (isSearch) {
            FormatterOfferQuery.doneQuerySearch(sb);
        } else {
            FormatterOfferQuery.doneQueryAlert(sb);
        }
        return sb;
    }

    /**
     * Format description for user interests
     *
     * @param parameters Map of all parameters
     * @return Formatted description
     */
    public static StringBuilder formatAlertDescription(Map<String, String[]> parameters) {
        StringBuilder sb = new StringBuilder();
        String sellerOP = getStringFromParamMap("sellerOP", parameters);
        String sellerVal = getStringFromParamMap("sellerVal", parameters);
        oneConditionDesc(sb, "seller", sellerOP, sellerVal, null);
        String categoryNameOP = OP_SZ_EQUAL_MULTI_NO_ESCAPE;
        String categoryNames = getListOfStringsFromParamMap("categoryName", 1, parameters, "");
        oneConditionDesc(sb, "categoryName", categoryNameOP, categoryNames, null);
        StringBuilder lstCondition = new StringBuilder();
        for (int i = 1; i <= 6; i++) {
            String temp = getStringFromParamMap("conditionCode_" + i, parameters);
            if (temp.length() > 0) {
                if (lstCondition.toString().equals("")) {
                    lstCondition = new StringBuilder(Helper.getConditionFromCode("" + i));
                } else {
                    lstCondition.append(",").append(Helper.getConditionFromCode("" + i));
                }
            }
        }
        oneConditionDesc(
                sb, "conditionCode", OP_SZ_EQUAL_MULTI_NO_ESCAPE, lstCondition.toString(), null);
        String descriptionOP = getStringFromParamMap("descriptionOP", parameters);
        String descriptionVal = getStringFromParamMap("descriptionVal", parameters);
        oneConditionDesc(sb, "description", descriptionOP, descriptionVal, null);
        Map<String, String> mapFieldIDToText = CategoryAndField.getMapFieldIDToText();
        String lstFieldIDs = getStringFromParamMap("lstFieldIDs", parameters);
        String[] fieldIDs = lstFieldIDs.split(",");
        for (String fieldID : fieldIDs) {
            String fieldOP = getStringFromParamMap("fieldop_" + fieldID, parameters);
            String fieldVal1 = getStringFromParamMap("fieldval1_" + fieldID, parameters);
            String fieldVal2 = getStringFromParamMap("fieldval2_" + fieldID, parameters);
            oneConditionDesc(sb, mapFieldIDToText.get(fieldID), fieldOP, fieldVal1, fieldVal2);
        }
        return sb;
    }

    /**
     * Actual searching function
     *
     * @param sql SQL statement
     * @param showAll If true, include minPrice
     * @return Data for GUI rendering
     */
    private static Map doSearchOfferInternal(String sql, boolean showAll) {
        Map output = new HashMap();
        List<String> lstHeader = new LinkedList<>();
        List<Object> lstRows = new LinkedList<>();

        // RowID-fieldID : fieldText
        Map<String, String> mapFields = new HashMap<>();

        Map<String, Integer> mapFieldIDToIndex = new HashMap<>();
        Map<Integer, String> mapIndexToFieldID = new HashMap<>();
        try (Connection con = getConnection();
                Statement statement = con.createStatement();
                ResultSet rs = statement.executeQuery(sql)) {
            String currentOfferID = "";
            int rowIndex = -1;
            while (rs.next()) {
                Object offerID = rs.getObject(1);
                Object seller = rs.getObject(2);
                Object categoryName = rs.getObject(3);
                Object conditionCode = rs.getObject(4);
                Object description = rs.getObject(5);
                Object initPrice = rs.getObject(6);
                Object increment = rs.getObject(7);
                Object minPrice = rs.getObject(8);
                Object startDate = rs.getObject(9);
                Object endDate = rs.getObject(10);
                Object status = rs.getObject(11);
                Object price = rs.getObject(12);
                Object fieldID = rs.getObject(13);
                Object fieldText = rs.getObject(14);
                Object fieldName = rs.getObject(15);
                Object fieldType = rs.getObject(16);
                if (currentOfferID.equals(offerID.toString())) {
                    // Continue with current row
                    if (fieldID != null) {
                        mapFields.put(
                                "" + rowIndex + "-" + fieldID,
                                fieldText == null ? "" : fieldText.toString());
                    }
                } else {
                    // New Row
                    List<Object> currentRow = new LinkedList<>();
                    lstRows.add(currentRow);
                    rowIndex++;
                    currentOfferID = offerID.toString();
                    currentRow.add(offerID);
                    currentRow.add(seller);
                    currentRow.add(categoryName);
                    currentRow.add(Helper.getConditionFromCode(conditionCode.toString()));
                    currentRow.add(description);
                    currentRow.add(initPrice);
                    currentRow.add(increment);
                    currentRow.add(minPrice);
                    currentRow.add(startDate);
                    currentRow.add(endDate);

                    Timestamp tmsTimestamp = (Timestamp) endDate;
                    System.out.println(tmsTimestamp);

                    currentRow.add(Helper.getStatusFromCode(status.toString()));
                    currentRow.add(price);
                    if (fieldID != null) {
                        mapFields.put(
                                "" + rowIndex + "-" + fieldID,
                                (fieldText == null) ? "" : fieldText.toString());
                    }
                    if (rowIndex == 0) {
                        lstHeader.add("offerId");
                        lstHeader.add("Seller");
                        lstHeader.add("Category");
                        lstHeader.add("Condition");
                        lstHeader.add("Desc");
                        lstHeader.add("initPrice");
                        lstHeader.add("increment");
                        lstHeader.add("minPrice");
                        lstHeader.add("Start");
                        lstHeader.add("End");
                        lstHeader.add("status");
                        lstHeader.add("CurrBid");
                    }
                }
                if (fieldID != null && mapFieldIDToIndex.get(fieldID.toString()) == null) {
                    mapFieldIDToIndex.put(fieldID.toString(), lstHeader.size());
                    lstHeader.add(fieldName.toString());
                }
            }
            // Reverse
            mapFieldIDToIndex.forEach((key, value) -> mapIndexToFieldID.put(value, key));

            for (int i = 0; i < lstRows.size(); i++) {
                List rowList = (List) lstRows.get(i);
                for (int j = FIELD_START_INDEX; j < lstHeader.size(); j++) {
                    String fieldId = mapIndexToFieldID.get(j);
                    String key = "" + i + "-" + fieldId;
                    String item = mapFields.get(key);
                    rowList.add(Objects.requireNonNullElse(item, ""));
                }
            }
            int[] colSeq;
            if (lstHeader.size() == 0) {
                lstHeader = lstHeader_OfferDefault;
                colSeq = colSeq_OfferDefault;
            } else {
                if (showAll) {
                    colSeq = new int[lstHeader.size() - 1]; // 1 less than headers
                    colSeq[0] = 2; // Category
                    colSeq[1] = 3; // Condition
                    colSeq[2] = 4; // Desc
                    colSeq[3] = 1; // Seller
                    colSeq[4] = 5; // initPrice
                    colSeq[5] = 6; // increment
                    colSeq[6] = 7; // minPrice
                    colSeq[7] = 11; // CurrBid
                    colSeq[8] = 8; // Start
                    colSeq[9] = 9; // End
                    colSeq[10] = 10; // status
                    // lstHeader.add("offerId");		0
                    int FIELD_STARTING_INDEX = 11; // 11 Starting Index for property
                    for (int i = FIELD_START_INDEX; i < lstHeader.size(); i++) {
                        colSeq[i + FIELD_STARTING_INDEX - FIELD_START_INDEX] = i;
                    }
                } else {
                    colSeq = new int[lstHeader.size() - 2]; // 2 less than headers
                    colSeq[0] = 2; // Category
                    colSeq[1] = 3; // Condition
                    colSeq[2] = 4; // Desc
                    colSeq[3] = 1; // Seller
                    colSeq[4] = 5; // initPrice
                    colSeq[5] = 6; // increment
                    colSeq[6] = 11; // CurrBid
                    colSeq[7] = 8; // Start
                    colSeq[8] = 9; // End
                    colSeq[9] = 10; // status
                    // lstHeader.add("offerId");		0
                    // lstHeader.add("minPrice");		7
                    int FIELD_STARTING_INDEX = 10; // 10 Starting Index for property
                    for (int i = FIELD_START_INDEX; i < lstHeader.size(); i++) {
                        colSeq[i + FIELD_STARTING_INDEX - FIELD_START_INDEX] = i;
                    }
                }
            }
            TableData dataTable = new TableData(lstHeader, lstRows, colSeq);
            output.put(DATA_NAME_DATA, dataTable);
            output.put(DATA_NAME_STATUS, true);
            output.put(DATA_NAME_MESSAGE, "OK");
        } catch (SQLException e) {
            output.put(DATA_NAME_STATUS, false);
            output.put(
                    DATA_NAME_MESSAGE,
                    MessageFormat.format(
                            "ERROR={0}, SQL_STATE={1}, SQL={2}",
                            e.getErrorCode(), e.getSQLState(), sql));
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            output.put(DATA_NAME_STATUS, false);
            output.put(
                    DATA_NAME_MESSAGE,
                    MessageFormat.format(
                            "ERROR=ClassNotFoundException, SQL_STATE={0}", e.getMessage()));
            e.printStackTrace();
        }
        return output;
    }

    /**
     * Create or modify offer
     *
     * @param userID User ID
     * @param parameters Map of all parameters
     * @param isCreate True if a new offer is created, false if modify
     * @return Data for GUI rendering
     */
    public static Map doCreateOrModifyOffer(
            String userID, Map<String, String[]> parameters, boolean isCreate) {
        Map output = new HashMap();
        String offerID;
        String[] temps = null;
        if (isCreate) {
            offerID = getUUID();
        } else {
            String offerIDCategoryNameUser =
                    getStringFromParamMap("offeridcategorynameuser", parameters);
            temps = offerIDCategoryNameUser.split(",");
            offerID = temps[0];
        }
        try (Connection con = getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement pStmtInsertOfferOrModify =
                    con.prepareStatement(isCreate ? SQL_OFFER_INSERT : SQL_OFFER_MODIFY)) {
                if (isCreate) {
                    BigDecimal initPrice = getBigDecimalFromParamMap("initPrice", parameters);
                    BigDecimal increment = getBigDecimalFromParamMap("increment", parameters);
                    BigDecimal minPrice = getBigDecimalFromParamMap("minPrice", parameters);
                    if (initPrice.compareTo(new BigDecimal(0)) <= 0) {
                        throw new Exception("initPrice is invalid: " + initPrice);
                    }
                    if (increment.compareTo(new BigDecimal(0)) <= 0) {
                        throw new Exception("increment is invalid: " + increment);
                    }
                    if (minPrice.compareTo(new BigDecimal(0)) > 0
                            && minPrice.compareTo(initPrice) < 0) {
                        throw new Exception(
                                MessageFormat.format(
                                        "minPrice is invalid: {0} less than {1}",
                                        minPrice, initPrice));
                    }
                    pStmtInsertOfferOrModify.setString(1, offerID);
                    pStmtInsertOfferOrModify.setString(
                            2, getStringFromParamMap("categoryName", parameters));
                    pStmtInsertOfferOrModify.setString(3, userID);
                    pStmtInsertOfferOrModify.setBigDecimal(4, initPrice);
                    pStmtInsertOfferOrModify.setBigDecimal(5, increment);
                    pStmtInsertOfferOrModify.setBigDecimal(6, minPrice);
                    pStmtInsertOfferOrModify.setInt(
                            7, getIntFromParamMap("conditionCode", parameters));
                    pStmtInsertOfferOrModify.setString(
                            8, getStringFromParamMap("description", parameters));
                    pStmtInsertOfferOrModify.setString(
                            9, getStringFromParamMap("endDate", parameters));
                } else {
                    pStmtInsertOfferOrModify.setBigDecimal(
                            1, getBigDecimalFromParamMap("minPrice", parameters));
                    pStmtInsertOfferOrModify.setInt(
                            2, getIntFromParamMap("conditionCode", parameters));
                    pStmtInsertOfferOrModify.setString(
                            3, getStringFromParamMap("description", parameters));
                    pStmtInsertOfferOrModify.setString(4, offerID);
                }
                pStmtInsertOfferOrModify.execute();
                int count = pStmtInsertOfferOrModify.getUpdateCount();
                if (count == 1) {
                    if (!isCreate) {
                        try (PreparedStatement pStmtDeleteField =
                                con.prepareStatement(SQL_OFFERFIELD_DELETE)) {
                            pStmtDeleteField.setString(1, offerID);
                            pStmtDeleteField.execute();
                        }
                    }
                    try (PreparedStatement pStmtInsertOfferField =
                            con.prepareStatement(SQL_OFFERFIELD_INSERT)) {
                        for (String s : parameters.keySet()) {
                            if (s.startsWith("fieldID_")) {
                                int fieldID = Integer.parseInt(s.substring("fieldID_".length()));
                                String value = parameters.get(s)[0];
                                if (value.trim().length() > 0) {
                                    pStmtInsertOfferField.setString(1, offerID);
                                    pStmtInsertOfferField.setInt(2, fieldID);
                                    pStmtInsertOfferField.setString(3, value);
                                    pStmtInsertOfferField.execute();
                                }
                            }
                        }
                    }
                    output.put(DATA_NAME_STATUS, true);
                    output.put(DATA_NAME_MESSAGE, "OK");
                    con.commit();
                } else {
                    output.put(DATA_NAME_STATUS, false);
                    output.put(
                            DATA_NAME_MESSAGE,
                            "Could not modify Offer. " + dumpParamMap(parameters));
                    con.rollback();
                }
            } catch (SQLException e) {
                con.rollback();
                con.setAutoCommit(true);
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
        } catch (Exception e) {
            output.put(DATA_NAME_STATUS, false);
            output.put(DATA_NAME_MESSAGE, "ERROR: ErrorCode=" + e.getMessage());
            e.printStackTrace();
        }
        if ((boolean) output.get(DATA_NAME_STATUS)) {
            if (isCreate) {
                doCreateAlerts(userID, offerID);
            } else {
                doCreateAlerts(temps[2], offerID);
            }
        }
        return output;
    }

    /**
     * Create Alerts when a new offer match the alert criteria
     *
     * @param userName User name
     * @param offerID Offer ID
     */
    public static void doCreateAlerts(String userName, String offerID) {
        List<Object> lstRows = new ArrayList<>();
        try (Connection con = getConnection();
                PreparedStatement pStmtSelectAlertCriterion =
                        con.prepareStatement(SQL_OFFERALERTCRITERION_SELECT_EX_USER)) {
            pStmtSelectAlertCriterion.setString(1, userName);
            try (ResultSet rs = pStmtSelectAlertCriterion.executeQuery()) {
                while (rs.next()) {
                    Object criterionID = rs.getObject(1);
                    Object buyer = rs.getObject(2);
                    Object criterionName = rs.getObject(3);
                    Object triggerTxt = rs.getObject(4);
                    Object description = rs.getObject(5);
                    Object generateDate = rs.getObject(6);
                    Object[] currentRow = new Object[6];
                    lstRows.add(currentRow);
                    currentRow[0] = criterionID;
                    currentRow[1] = buyer;
                    currentRow[2] = criterionName;
                    currentRow[3] = triggerTxt;
                    currentRow[4] = description;
                    currentRow[5] = generateDate;
                }
            }
            for (Object object : lstRows) {
                Object[] oneRow = (Object[]) object;
                String context =
                        "New offer met your criterion " + oneRow[2].toString() + " is posted.";
                try (PreparedStatement pStmtInsertAlert =
                        con.prepareStatement(oneRow[3].toString())) {
                    pStmtInsertAlert.setString(1, oneRow[1].toString());
                    pStmtInsertAlert.setString(2, offerID);
                    pStmtInsertAlert.setString(3, context);
                    pStmtInsertAlert.setString(4, offerID);
                    pStmtInsertAlert.execute();
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete an Offer. Bid &amp; Alert will be cascading delete
     *
     * @param parameters Map of all parameters
     * @return Data for GUI rendering
     */
    public static Map doCancelOffer(Map<String, String[]> parameters) {
        String offerID = getStringFromParamMap("offerid", parameters);
        Map output = new HashMap();
        try (Connection con = getConnection();
                PreparedStatement pStmtCancelOffer = con.prepareStatement(SQL_OFFER_CANCEL)) {
            pStmtCancelOffer.setString(1, offerID);
            pStmtCancelOffer.execute();
            int count = pStmtCancelOffer.getUpdateCount();
            if (count == 1) {
                output.put(DATA_NAME_STATUS, true);
                output.put(DATA_NAME_MESSAGE, "OK");
            } else {
                output.put(DATA_NAME_STATUS, false);
                output.put(DATA_NAME_MESSAGE, "Failed to cancel offer");
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
        return output;
    }
}
