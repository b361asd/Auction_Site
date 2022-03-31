package com.b361asd.auction.db;

import com.b361asd.auction.gui.Helper;
import com.b361asd.auction.gui.TableData;
import com.b361asd.auction.servlet.IConstant;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Bid extends DBBase {

    private static final List<String> lstHeader_bid =
            Arrays.asList("bidID", "offerID", "buyer", "price", "autoRebidLimit", "bidDate");
    private static final int[] colSeq_bid = {2, 3, 5};
    private static final int[] colSeq_bid_add = {2, 3, 4, 5};

    /**
     * Search bid and their relevant offers
     *
     * @param parameters Map containing parameters for other conditions
     * @param userActivity User ID for my activity screen
     * @param userMyBid User ID for my bid screen
     * @return Data for GUI rendering
     */
    public static Map searchBid(
            Map<String, String[]> parameters, String userActivity, String userMyBid) {
        String offerIDCategoryName = getStringFromParamMap("offeridcategoryname", parameters);

        // viewAlertDetail
        String offerIDBidID = getStringFromParamMap("offerIDbidID", parameters);

        String _action = getStringFromParamMap("action", parameters);
        String bidIDOfferIDBuyer = getStringFromParamMap("bidIDofferIDBuyer", parameters);

        boolean _listActivity = false;
        boolean _listMyBid = false;
        boolean _listBidForOffer = false;
        boolean _viewAlertDetail = false;
        boolean _listBid_Search = false;
        boolean _listBid_Browse = false;
        boolean _modifyBid = false;

        if (userActivity != null && userActivity.length() > 0) {
            _listActivity = true;
        } else if (userMyBid != null && userMyBid.length() > 0) {
            _listMyBid = true;
        } else if (offerIDCategoryName.length() > 0) {
            _listBidForOffer = true;
        } else if (offerIDBidID.length() > 0) {
            _viewAlertDetail = true;
        } else if (_action.equals("repSearchBid")) {
            _listBid_Search = true;
        } else if (_action.equals("repBrowseBid")) {
            _listBid_Browse = true;
        } else if (!bidIDOfferIDBuyer.equals("")) {
            _modifyBid = true;
        }
        String sql;
        String bidIDStandout = null;
        String userStandout = null;
        Set<String> offerIDSet = new HashSet<>();
        if (_listActivity) {
            // User: ListActivity.jsp
            StringBuilder sb = FormatterBidQuery.buildQueryUserActivity(userActivity);
            sql = sb.toString();
            userStandout = userActivity;
        } else if (_listMyBid) {
            // User: listMyBid.jsp
            StringBuilder sb = FormatterBidQuery.initQuerySearchAll();
            FormatterOfferQuery.addCondition(sb, "buyer", OP_SZ_EQUAL, userMyBid, null);
            FormatterOfferQuery.addCondition(sb, "status", OP_INT_EQUAL, "1", null);
            sql = sb.toString();
            userStandout = userMyBid;
        } else if (_listBidForOffer) {
            StringBuilder sb = FormatterBidQuery.initQuerySearchAll();
            String[] temps = offerIDCategoryName.split(",");
            FormatterOfferQuery.addCondition(sb, "o.offerID", OP_SZ_EQUAL, temps[0], null);
            sql = sb.toString();
            offerIDSet.add(temps[0]);
        } else if (_listBid_Search) {
            // repSearchBid for cancel and modify, should be
            // active Offer
            StringBuilder sb = FormatterBidQuery.initQuerySearchAll();
            String userRepBidSearch = getStringFromParamMap("userRepBidSearch", parameters);
            FormatterOfferQuery.addCondition(sb, "buyer", OP_SZ_EQUAL, userRepBidSearch, null);
            FormatterOfferQuery.addCondition(sb, "status", OP_INT_EQUAL, "1", null);
            sql = sb.toString();
        } else if (_listBid_Browse) {
            // repSearchBid for cancel and modify, should be
            // active Offer
            StringBuilder sb = FormatterBidQuery.initQuerySearchAll();
            FormatterOfferQuery.addCondition(sb, "status", OP_INT_EQUAL, "1", null);
            sql = sb.toString();
        } else if (_viewAlertDetail) {
            // user: listAlert.jsp(offerIDbidID) ->
            // viewAlertDetail.jsp
            StringBuilder sb = FormatterBidQuery.initQuerySearchAll();
            String[] temps = offerIDBidID.split(",");
            FormatterOfferQuery.addCondition(sb, "o.offerID", OP_SZ_EQUAL, temps[0], null);
            offerIDSet.add(temps[0]);
            if (temps.length >= 2) {
                bidIDStandout = temps[1];
            }
            sql = sb.toString();
            offerIDSet.add(temps[0]);
        } else if (_modifyBid) {
            // Rep: ListBid.jsp(bidIDofferIDBuyer) ->
            // modifyBid.jsp
            StringBuilder sb = FormatterBidQuery.initQuerySearchAll();
            String[] temps = bidIDOfferIDBuyer.split(",");
            FormatterOfferQuery.addCondition(sb, "o.offerID", OP_SZ_EQUAL, temps[1], null);
            bidIDStandout = temps[0];
            sql = sb.toString();
            offerIDSet.add(temps[1]);
        } else {
            StringBuilder sb = FormatterBidQuery.initQuerySearchAll();
            String bidID = getStringFromParamMap("bidID", parameters);
            FormatterOfferQuery.addCondition(sb, "bidID", OP_SZ_EQUAL, bidID, null);
            String offerID = getStringFromParamMap("offerID", parameters);
            FormatterOfferQuery.addCondition(sb, "o.offerID", OP_SZ_EQUAL, offerID, null);
            sql = sb.toString();
        }
        Map output = new HashMap();

        // offerID -> Bids(in List)
        Map<String, List> tempMap = new HashMap<>();
        try (Connection con = getConnection();
                Statement statement = con.createStatement();
                ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                Object bidID = rs.getObject(1);
                Object offerID = rs.getObject(2);
                Object buyer = rs.getObject(3);
                Object price = rs.getObject(4);
                Object autoRebidLimit = rs.getObject(5);
                Object bidDate = rs.getObject(6);

                List lstRows = tempMap.computeIfAbsent(offerID.toString(), k -> new ArrayList());
                List<Object> currentRow = new LinkedList<>();
                lstRows.add(currentRow);
                currentRow.add(bidID);
                currentRow.add(offerID);
                currentRow.add(buyer);
                currentRow.add(price);
                currentRow.add(autoRebidLimit);
                currentRow.add(bidDate);
                offerIDSet.add(offerID.toString());
            }
            Map offerMap;
            TableData dataTableOffer = null;
            if (_listActivity) {
                offerMap = Offer.doSearchUserActivity(userActivity);
                dataTableOffer = (TableData) offerMap.get(IConstant.DATA_NAME_DATA);
                dataTableOffer.setStandOut(userActivity, 1);
            } else {
                if (offerIDSet.size() > 0) {
                    offerMap =
                            Offer.doSearchByOfferIDSet(
                                    offerIDSet, _listBid_Search || _listBid_Browse || _modifyBid);
                    dataTableOffer = (TableData) offerMap.get(IConstant.DATA_NAME_DATA);
                }
            }
            if (dataTableOffer != null) {
                List lstOfferRows = dataTableOffer.getRows();
                for (Object one : lstOfferRows) {
                    List oneOfferRow = (List) one;
                    List lstBidRows = tempMap.get(oneOfferRow.get(0));
                    if (lstBidRows == null) {
                        oneOfferRow.add(null);
                    } else {
                        TableData tableDataBiD =
                                new TableData(
                                        lstHeader_bid,
                                        lstBidRows,
                                        ((_listActivity || _viewAlertDetail || _listBidForOffer)
                                                ? colSeq_bid
                                                : colSeq_bid_add));
                        if (bidIDStandout != null) {
                            tableDataBiD.setStandOut(bidIDStandout, 0); // bidID
                        } else if (userStandout != null) {
                            tableDataBiD.setStandOut(userStandout, 2); // user
                        }
                        oneOfferRow.add(tableDataBiD);
                    }
                }
            } else {
                dataTableOffer =
                        new TableData(
                                Offer.lstHeader_OfferDefault,
                                new LinkedList(),
                                Offer.colSeq_OfferDefault);
            }
            if (_listActivity) {
                dataTableOffer.setDescription("User Activities for " + userActivity);
            } else if (_listMyBid) {
                dataTableOffer.setDescription("My Open Bids");
            } else if (_listBidForOffer) {
                dataTableOffer.setDescription("Bids for One Open Offer");
            } else if (_viewAlertDetail) {
                dataTableOffer.setDescription("Offer and Its Bids for an Alert");
            } else if (_listBid_Search) {
                dataTableOffer.setDescription("Search Bids");
            } else if (_listBid_Browse) {
                dataTableOffer.setDescription("Browse Bids");
            } else if (_modifyBid) {
                dataTableOffer.setDescription("Bid To Be Modify");
            } else {
                dataTableOffer.setDescription("List of Bids");
            }
            output.put(IConstant.DATA_NAME_DATA, dataTableOffer);
            output.put(IConstant.DATA_NAME_STATUS, true);
            output.put(IConstant.DATA_NAME_MESSAGE, "OK");
        } catch (SQLException e) {
            output.put(IConstant.DATA_NAME_STATUS, false);
            output.put(
                    IConstant.DATA_NAME_MESSAGE,
                    MessageFormat.format(
                            "ERROR={0}, SQL_STATE={1}, SQL={2}",
                            e.getErrorCode(), e.getSQLState(), sql));
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            output.put(IConstant.DATA_NAME_STATUS, false);
            output.put(
                    IConstant.DATA_NAME_MESSAGE,
                    MessageFormat.format(
                            "ERROR=ClassNotFoundException, SQL_STATE={0}", e.getMessage()));
            e.printStackTrace();
        }
        return output;
    }

    private enum CreateModifyBidOutcome {
        NOT_MEET_INIT_PRICE,
        LESS_THAN_LAST_PLUS_DELTA,
        OFFER_CLOSED,
        OUTBID,
        OK
    }

    /**
     * Create or modify a bid
     *
     * @param userID User ID
     * @param parameters Map containing other parameters
     * @param isCreate True when created, false when modified
     * @return Data for GUI rendering
     */
    public static Map doCreateOrModifyBid(
            String userID, Map<String, String[]> parameters, boolean isCreate) {
        Map output = new HashMap();

        String offerId;
        String bidID;
        BigDecimal initPrice = null;
        BigDecimal increment = null;

        String seller = "";
        String categoryName = "";
        String conditionCode = "";
        String description = "";
        int status = -1;
        Object[] newBid = new Object[4];
        if (isCreate) {
            offerId = getStringFromParamMap("offerId", parameters);
            bidID = null;
            BigDecimal price = getBigDecimalFromParamMap("price", parameters);
            BigDecimal autoRebidLimit = getBigDecimalFromParamMap("autoRebidLimit", parameters);
            newBid[0] = bidID;
            newBid[1] = userID;
            newBid[2] = price;
            newBid[3] = autoRebidLimit;
        } else {
            String bidIDOfferIDBuyer = getStringFromParamMap("bidIDofferIDBuyer", parameters);
            String[] temps = bidIDOfferIDBuyer.split(",");
            offerId = temps[1];
            bidID = temps[0];
            BigDecimal price = getBigDecimalFromParamMap("price", parameters);
            BigDecimal autoRebidLimit = getBigDecimalFromParamMap("autoRebidLimit", parameters);
            newBid[0] = bidID;
            newBid[1] = temps[2];
            newBid[2] = price;
            newBid[3] = autoRebidLimit;
        }
        Object[] lastMaxBid = new Object[4];
        try (Connection con = getConnection()) {
            con.setAutoCommit(false);
            BigDecimal price = (BigDecimal) newBid[2];
            BigDecimal autoRebidLimit = (BigDecimal) newBid[3];
            if (price.compareTo(new BigDecimal(0)) <= 0) {
                throw new Exception("Invalid bid: price needs to be greater than 0: " + price);
            }
            if (autoRebidLimit.compareTo(new BigDecimal(0)) > 0
                    && autoRebidLimit.compareTo(price) < 0) {
                throw new Exception(
                        MessageFormat.format(
                                "Invalid bid: autoRebidLimit needs to be greater than price: {0} less than {1}",
                                autoRebidLimit, price));
            }
            try (PreparedStatement preparedStmtMaxPriceBid =
                    con.prepareStatement(
                            isCreate
                                    ? ISQLConstant.SQL_BID_SELECT_MAX_PRICE
                                    : ISQLConstant.SQL_BID_SELECT_MAX_PRICE_EX)) {
                preparedStmtMaxPriceBid.setString(1, offerId);
                if (!isCreate) {
                    preparedStmtMaxPriceBid.setString(2, bidID);
                    preparedStmtMaxPriceBid.setString(3, bidID);
                }
                try (ResultSet rs = preparedStmtMaxPriceBid.executeQuery()) {
                    if (rs.next()) {
                        Object _seller = rs.getObject(2);
                        Object _categoryName = rs.getObject(3);
                        Object _conditionCode = rs.getObject(4);
                        Object _description = rs.getObject(5);
                        Object _initPrice = rs.getObject(6);
                        Object _increment = rs.getObject(7);
                        Object _status = rs.getObject(11);
                        Object _bidID = rs.getObject(12);
                        Object _buyer = rs.getObject(13);
                        Object _price = rs.getObject(14);
                        Object _autoRebidLimit = rs.getObject(15);
                        initPrice = (BigDecimal) _initPrice;
                        increment = (BigDecimal) _increment;
                        seller = _seller == null ? "" : _seller.toString();
                        categoryName = _categoryName == null ? "" : _categoryName.toString();
                        conditionCode = _conditionCode == null ? "" : _conditionCode.toString();
                        description = _description == null ? "" : _description.toString();
                        status = (Integer) _status;
                        if (_bidID == null || _bidID.toString().length() == 0) {
                            lastMaxBid = null;
                        } else {
                            lastMaxBid[0] = _bidID;
                            lastMaxBid[1] = _buyer;
                            lastMaxBid[2] = _price;
                            lastMaxBid[3] = _autoRebidLimit;
                        }
                    }
                }
            } catch (SQLException e) {
                con.rollback();
                con.setAutoCommit(true);
            }
            Object[] current = newBid;
            Object[] last = lastMaxBid;
            if (isCreate) {
                current[0] = getUUID();
            }
            CreateModifyBidOutcome outcome;
            if (status != 1) {
                outcome = CreateModifyBidOutcome.OFFER_CLOSED;
            } else {
                boolean isModifyAndDoIt = !isCreate;
                while (true) {
                    BigDecimal last_price = last == null ? null : (BigDecimal) last[2];
                    BigDecimal last_autoRebidLimit = last == null ? null : (BigDecimal) last[3];
                    BigDecimal current_price = (BigDecimal) current[2];
                    // Check price meet offer
                    if (last == null) {
                        if (current_price.compareTo(initPrice) < 0) {
                            outcome = CreateModifyBidOutcome.NOT_MEET_INIT_PRICE;
                            break;
                        }
                    } else {
                        BigDecimal lastPlusDelta = last_price.add(increment);
                        if (current_price.compareTo(lastPlusDelta) < 0) {
                            outcome = CreateModifyBidOutcome.LESS_THAN_LAST_PLUS_DELTA;
                            break;
                        }
                    }
                    if (isModifyAndDoIt) {
                        isModifyAndDoIt = false;
                        try (PreparedStatement pStmtModifyBid =
                                con.prepareStatement(ISQLConstant.SQL_BID_UPDATE)) {
                            pStmtModifyBid.setBigDecimal(1, (BigDecimal) current[2]);
                            pStmtModifyBid.setBigDecimal(2, (BigDecimal) current[3]);
                            pStmtModifyBid.setString(3, current[0].toString());
                            pStmtModifyBid.execute();
                        }
                    } else {
                        try (PreparedStatement pStmtInsertBid =
                                con.prepareStatement(ISQLConstant.SQL_BID_INSERT)) {
                            pStmtInsertBid.setString(1, current[0].toString());
                            pStmtInsertBid.setString(2, offerId);
                            pStmtInsertBid.setString(3, current[1].toString());
                            pStmtInsertBid.setBigDecimal(4, (BigDecimal) current[2]);
                            pStmtInsertBid.setBigDecimal(5, (BigDecimal) current[3]);
                            pStmtInsertBid.execute();
                        }
                    }
                    if (last == null) {
                        outcome = CreateModifyBidOutcome.OK;
                        break;
                    } else {
                        BigDecimal new_bid = current_price.add(increment);
                        if (last_autoRebidLimit.compareTo(new_bid) >= 0) {
                            // Continue
                            last[0] = getUUID();
                            last[2] = new_bid;
                        } else {
                            // Out bid alert
                            String context =
                                    MessageFormat.format(
                                            "Your bid for a {0} ({1}, {2}) by seller {3} is outbidded.",
                                            categoryName,
                                            Helper.getConditionFromCode(conditionCode),
                                            description,
                                            seller);
                            try (PreparedStatement pStmtInsertAlert =
                                    con.prepareStatement(ISQLConstant.SQL_ALERT_INSERT_BID)) {
                                pStmtInsertAlert.setString(1, getUUID());
                                pStmtInsertAlert.setString(2, last[1].toString()); // user
                                pStmtInsertAlert.setString(3, offerId);
                                pStmtInsertAlert.setString(4, last[0].toString()); // bidID
                                pStmtInsertAlert.setString(5, context);
                                pStmtInsertAlert.execute();
                            }
                            outcome = CreateModifyBidOutcome.OUTBID;
                            break;
                        }
                    }
                    // Switch for next round
                    Object[] temp = current;
                    current = last;
                    last = temp;
                }
            }
            if (outcome == CreateModifyBidOutcome.OUTBID) {
                con.commit();
                output.put(IConstant.DATA_NAME_STATUS, true);
                output.put(
                        IConstant.DATA_NAME_MESSAGE,
                        MessageFormat.format(
                                "BID {0} WITH AUTOREBID", isCreate ? "CREATED" : "UPDATED"));
            } else if (outcome == CreateModifyBidOutcome.OK) {
                con.commit();
                output.put(IConstant.DATA_NAME_STATUS, true);
                output.put(
                        IConstant.DATA_NAME_MESSAGE,
                        MessageFormat.format("BID {0}", isCreate ? "CREATED" : "UPDATED"));
            } else if (outcome == CreateModifyBidOutcome.NOT_MEET_INIT_PRICE) {
                con.rollback();
                output.put(IConstant.DATA_NAME_STATUS, false);
                output.put(
                        IConstant.DATA_NAME_MESSAGE,
                        MessageFormat.format(
                                "FAILED TO {0} BID DUE TO NotMeetInitPrice",
                                isCreate ? "CREATED" : "UPDATED"));
            } else if (outcome == CreateModifyBidOutcome.LESS_THAN_LAST_PLUS_DELTA) {
                con.rollback();
                output.put(IConstant.DATA_NAME_STATUS, false);
                output.put(
                        IConstant.DATA_NAME_MESSAGE,
                        MessageFormat.format(
                                "FAILED TO {0} BID DUE TO LessThanLastPlusDelta",
                                isCreate ? "CREATED" : "UPDATED"));
            } else {
                con.rollback();
                output.put(IConstant.DATA_NAME_STATUS, false);
                output.put(
                        IConstant.DATA_NAME_MESSAGE,
                        MessageFormat.format(
                                "FAILED TO {0} BID DUE TO Offer closed",
                                isCreate ? "CREATED" : "UPDATED"));
            }
        } catch (SQLException e) {
            output.put(IConstant.DATA_NAME_STATUS, false);
            output.put(
                    IConstant.DATA_NAME_MESSAGE,
                    MessageFormat.format(
                            "ERROR: ErrorCode={0}, SQL_STATE={1}, Message={2}, {3}",
                            e.getErrorCode(),
                            e.getSQLState(),
                            e.getMessage(),
                            dumpParamMap(parameters)));
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            output.put(IConstant.DATA_NAME_STATUS, false);
            output.put(
                    IConstant.DATA_NAME_MESSAGE,
                    MessageFormat.format(
                            "ERROR: Code=ClassNotFoundException, Message={0}, {1}",
                            e.getMessage(), dumpParamMap(parameters)));
            e.printStackTrace();
        } catch (Exception e) {
            output.put(IConstant.DATA_NAME_STATUS, false);
            output.put(
                    IConstant.DATA_NAME_MESSAGE,
                    MessageFormat.format("ERROR: Code=Exception, Message={0}", e.getMessage()));
            e.printStackTrace();
        }
        return output;
    }

    /**
     * Delete a bid
     *
     * @param parameters Map containing bidID to be deleted
     * @return Data for GUI rendering
     */
    public static Map cancelBid(Map<String, String[]> parameters) {
        Map output = new HashMap();
        String bidID = getStringFromParamMap("bidID", parameters);
        try (Connection con = getConnection();
                PreparedStatement preparedStmt =
                        con.prepareStatement(ISQLConstant.SQL_BID_DELETE)) {
            preparedStmt.setString(1, bidID);
            preparedStmt.execute();
            int count = preparedStmt.getUpdateCount();
            if (count == 0) {
                output.put(IConstant.DATA_NAME_STATUS, false);
                output.put(IConstant.DATA_NAME_MESSAGE, "Could not delete bid.");
            } else {
                output.put(IConstant.DATA_NAME_STATUS, true);
                output.put(IConstant.DATA_NAME_MESSAGE, "Bid deleted");
            }
        } catch (SQLException e) {
            output.put(IConstant.DATA_NAME_STATUS, false);
            output.put(
                    IConstant.DATA_NAME_MESSAGE,
                    MessageFormat.format(
                            "ERROR: {0}, SQL_STATE: {1}, DETAILS: {2}",
                            e.getErrorCode(), e.getSQLState(), exceptionToString(e)));
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            output.put(IConstant.DATA_NAME_STATUS, false);
            output.put(
                    IConstant.DATA_NAME_MESSAGE,
                    MessageFormat.format(
                            "ERROR: ClassNotFoundException, SQL_STATE: {0}, DETAILS: {1}",
                            e.getMessage(), exceptionToString(e)));
            e.printStackTrace();
        }
        return output;
    }

    /**
     * Get min when modify a bid
     *
     * @param parameters Map containing other parameters
     * @return Min Bid
     */
    public static BigDecimal getMinForModifyBid(Map<String, String[]> parameters) {
        BigDecimal output = null;
        String bidIDOfferIDBuyer = getStringFromParamMap("bidIDofferIDBuyer", parameters);
        String[] temps = bidIDOfferIDBuyer.split(",");
        String bidID = temps[0];
        String offerID = temps[1];
        BigDecimal initPrice = null;
        BigDecimal increment = null;
        BigDecimal price = null;
        try (Connection con = getConnection();
                PreparedStatement preparedStmtMaxPriceBid =
                        con.prepareStatement(ISQLConstant.SQL_BID_SELECT_MAX_PRICE_EX)) {
            preparedStmtMaxPriceBid.setString(1, offerID);
            preparedStmtMaxPriceBid.setString(2, bidID);
            preparedStmtMaxPriceBid.setString(3, bidID);
            try (ResultSet rs = preparedStmtMaxPriceBid.executeQuery()) {
                if (rs.next()) {
                    Object _initPrice = rs.getObject(6);
                    Object _increment = rs.getObject(7);
                    Object _bidID = rs.getObject(12);
                    Object _price = rs.getObject(14);
                    initPrice = (BigDecimal) _initPrice;
                    increment = (BigDecimal) _increment;
                    if (_bidID != null && _bidID.toString().length() != 0) {
                        price = (BigDecimal) _price;
                    }
                }
            }
            if (initPrice != null && increment != null) {
                if (price == null) {
                    output = initPrice;
                } else {
                    output = price.add(increment);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }
}
