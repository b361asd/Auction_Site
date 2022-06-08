package com.b361asd.auction.db;

import com.b361asd.auction.gui.Helper;
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

public class Trade extends DBBase {

    private static final Logger LOGGER = Logger.getLogger(Trade.class.getName());

    private static final List<String> LST_HEADER_TRADE_TOTAL =
            Arrays.asList("Period", "Total", "Average", "Count");
    private static final List<String> LST_HEADER_TRADE_BY_CATEGORY_NAME =
            Arrays.asList("categoryName", "Total", "Average", "Count");
    private static final List<String> LST_HEADER_TRADE_BY_SIMILAR =
            Arrays.asList("Similar", "Total", "Average", "Count");
    private static final List<String> LST_HEADER_TRADE_BY_BUYER =
            Arrays.asList("Buyer", "Total", "Average", "Count");
    private static final List<String> LST_HEADER_TRADE_BY_SELLER =
            Arrays.asList("Seller", "Total", "Average", "Count");
    private static final List<String> LST_HEADER_TRADE_BY_USER =
            Arrays.asList("User", "Total", "Average", "Count");

    private static final int[] COL_SEQ_TRADE_BY = {0, 1, 2, 3};

    private static final List<String> LST_HEADER_TRADE_BY_BEST_SELLING_ITEM =
            Arrays.asList(
                    "price",
                    "categoryName",
                    "conditionCode",
                    "description",
                    "seller",
                    "buyer",
                    "tradeDate");

    private static final int[] COL_SEQ_TRADE_BY_BEST_SELLING_ITEM = {0, 1, 2, 3, 4, 5, 6};

    /**
     * Summary of Trade for Report
     *
     * @param lookBackDays Days to look back
     * @param isTotal If true, then run total sales report.
     * @param isCategoryName If true, then run category name report.
     * @param isBuyer If true, then run buyer report.
     * @param isSeller If true, then run seller report.
     * @param isUser If true, then run user report (buyer and seller)
     * @return Data for GUI rendering
     */
    public static Map<String, Object> summaryBy(
            int lookBackDays,
            boolean isTotal,
            boolean isCategoryName,
            boolean isBuyer,
            boolean isSeller,
            boolean isUser) {
        Map<String, Object> output = new HashMap<>();
        List<Object> lstRows = new ArrayList<>();
        List<String> lstHeader = null;
        if (isTotal) {
            lstHeader = LST_HEADER_TRADE_TOTAL;
        } else if (isCategoryName) {
            lstHeader = LST_HEADER_TRADE_BY_CATEGORY_NAME;
        } else if (isBuyer) {
            lstHeader = LST_HEADER_TRADE_BY_BUYER;
        } else if (isSeller) {
            lstHeader = LST_HEADER_TRADE_BY_SELLER;
        } else if (isUser) {
            lstHeader = LST_HEADER_TRADE_BY_USER;
        }
        TableData tableData = new TableData(lstHeader, lstRows, COL_SEQ_TRADE_BY);
        output.put(DATA_NAME_DATA, tableData);
        try (Connection con = getConnection()) {
            String sql = null;
            if (isTotal) {
                sql = SQL_TRADE_TOTAL;
            } else if (isCategoryName) {
                sql = SQL_TRADE_TOTAL_BY_CATEGORY;
            } else if (isBuyer) {
                sql = SQL_TRADE_TOTAL_BY_BUYER;
            } else if (isSeller) {
                sql = SQL_TRADE_TOTAL_BY_SELLER;
            } else if (isUser) {
                sql = SQL_TRADE_TOTAL_BY_USER;
            }
            try (PreparedStatement preparedStmt = con.prepareStatement(sql)) {
                if (!isTotal) {
                    if (isCategoryName) {
                        preparedStmt.setInt(1, lookBackDays);
                    } else if (isBuyer) {
                        preparedStmt.setInt(1, lookBackDays);
                    } else if (isSeller) {
                        preparedStmt.setInt(1, lookBackDays);
                    } else if (isUser) {
                        preparedStmt.setInt(1, lookBackDays);
                        preparedStmt.setInt(2, lookBackDays);
                    }
                }
                try (ResultSet rs = preparedStmt.executeQuery()) {
                    while (rs.next()) {
                        Object person = rs.getObject(1);
                        Object total = rs.getObject(2);
                        Object average = rs.getObject(3);
                        Object count = rs.getObject(4);
                        List<Object> currentRow = new ArrayList<>();
                        lstRows.add(currentRow);
                        currentRow.add(person);
                        currentRow.add(total);
                        currentRow.add(average);
                        currentRow.add(count);
                    }
                    output.put(DATA_NAME_STATUS, true);
                    output.put(DATA_NAME_MESSAGE, "OK");
                }
            }
            if (isTotal) {
                tableData.setDescription("Total Sales For Various Periods");
            } else if (isCategoryName) {
                tableData.setDescription(
                        "Sales by CategoryNames For The Last " + lookBackDays + " Days");
            } else if (isBuyer) {
                tableData.setDescription("Sales by Buyers For The Last " + lookBackDays + " Days");
            } else if (isSeller) {
                tableData.setDescription("Sales by Sellers For The Last " + lookBackDays + " Days");
            } else if (isUser) {
                tableData.setDescription(
                        "Sales by Users (as Either Buyer or Seller) For The Last "
                                + lookBackDays
                                + " Days");
            }
        } catch (SQLException e) {
            output.put(DATA_NAME_STATUS, false);
            output.put(
                    DATA_NAME_MESSAGE,
                    MessageFormat.format(
                            "ERROR={0}, SQL_STATE={1}", e.getErrorCode(), e.getSQLState()));
            tableData.setDescription((String) output.get(DATA_NAME_MESSAGE));
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            output.put(DATA_NAME_STATUS, false);
            output.put(
                    DATA_NAME_MESSAGE,
                    MessageFormat.format(
                            "ERROR=ClassNotFoundException, SQL_STATE={0}", e.getMessage()));
            tableData.setDescription((String) output.get(DATA_NAME_MESSAGE));
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        return output;
    }

    /**
     * Select similar (same category and condition) group reports
     *
     * @param lookBackDays Look back days
     * @return Data for GUI rendering
     */
    public static Map<String, Object> selectGroupSimilar(int lookBackDays) {
        Map<String, Object> output = new HashMap<>();
        List<Object> lstRows = new ArrayList<>();
        TableData tableData = new TableData(LST_HEADER_TRADE_BY_SIMILAR, lstRows, COL_SEQ_TRADE_BY);
        output.put(DATA_NAME_DATA, tableData);
        try (Connection con = getConnection();
                PreparedStatement preparedStmt =
                        con.prepareStatement(SQL_TRADE_TOTAL_BY_SIMILARGROUP)) {
            preparedStmt.setInt(1, lookBackDays);
            try (ResultSet rs = preparedStmt.executeQuery()) {
                while (rs.next()) {
                    Object categoryName = rs.getObject(1);
                    Object conditionCode = rs.getObject(2);
                    Object total = rs.getObject(3);
                    Object average = rs.getObject(4);
                    Object count = rs.getObject(5);
                    List<Object> currentRow = new ArrayList<>();
                    lstRows.add(currentRow);
                    currentRow.add(
                            categoryName
                                    + ", "
                                    + Helper.getConditionFromCode(conditionCode.toString()));
                    currentRow.add(total);
                    currentRow.add(average);
                    currentRow.add(count);
                }
            }
            output.put(DATA_NAME_STATUS, true);
            output.put(DATA_NAME_MESSAGE, "OK");
            tableData.setDescription(
                    "Sales Grouped by Similar For The Last " + lookBackDays + " Days");
        } catch (SQLException e) {
            output.put(DATA_NAME_STATUS, false);
            output.put(
                    DATA_NAME_MESSAGE,
                    MessageFormat.format(
                            "ERROR={0}, SQL_STATE={1}", e.getErrorCode(), e.getSQLState()));
            tableData.setDescription((String) output.get(DATA_NAME_MESSAGE));
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            output.put(DATA_NAME_STATUS, false);
            output.put(
                    DATA_NAME_MESSAGE,
                    MessageFormat.format(
                            "ERROR=ClassNotFoundException, SQL_STATE={0}", e.getMessage()));
            tableData.setDescription((String) output.get(DATA_NAME_MESSAGE));
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        return output;
    }

    /**
     * Select best-selling or most recent items
     *
     * @param lookBackDays Look back days
     * @param limit Limit how many rows returned
     * @param isBestSelling True for best-selling, false for most recent
     * @return Data for GUI rendering
     */
    public static Map<String, Object> selectBestSellingMostRecentItems(
            int lookBackDays, int limit, boolean isBestSelling) {
        Map<String, Object> output = new HashMap<>();
        List<Object> lstRows = new ArrayList<>();
        TableData tableData =
                new TableData(
                        LST_HEADER_TRADE_BY_BEST_SELLING_ITEM,
                        lstRows,
                        COL_SEQ_TRADE_BY_BEST_SELLING_ITEM);
        output.put(DATA_NAME_DATA, tableData);
        try (Connection con = getConnection();
                PreparedStatement preparedStmt =
                        con.prepareStatement(
                                isBestSelling ? SQL_TRADE_BEST_ITEM : SQL_TRADE_RECENT_ITEM)) {
            preparedStmt.setInt(1, lookBackDays);
            preparedStmt.setInt(2, limit);
            try (ResultSet rs = preparedStmt.executeQuery()) {
                while (rs.next()) {
                    Object price = rs.getObject(1);
                    Object categoryName = rs.getObject(2);
                    Object conditionCode = rs.getObject(3);
                    Object description = rs.getObject(4);
                    Object seller = rs.getObject(5);
                    Object buyer = rs.getObject(6);
                    Object tradeDate = rs.getObject(7);
                    List<Object> currentRow = new ArrayList<>();
                    lstRows.add(currentRow);
                    currentRow.add(price);
                    currentRow.add(categoryName);
                    currentRow.add(Helper.getConditionFromCode(conditionCode.toString()));
                    currentRow.add(description);
                    currentRow.add(seller);
                    currentRow.add(buyer);
                    currentRow.add(tradeDate);
                }
            }
            output.put(DATA_NAME_STATUS, true);
            output.put(DATA_NAME_MESSAGE, "OK");
            if (isBestSelling) {
                tableData.setDescription(
                        MessageFormat.format(
                                "Top {0} Best Selling Item(s) For The Last {1} Days",
                                limit, lookBackDays));
            } else {
                tableData.setDescription(
                        MessageFormat.format(
                                "Most Recent {0} Trades, Limited For The Last {1} Days",
                                limit, lookBackDays));
            }
        } catch (SQLException e) {
            output.put(DATA_NAME_STATUS, false);
            output.put(
                    DATA_NAME_MESSAGE,
                    MessageFormat.format(
                            "ERROR={0}, SQL_STATE={1}", e.getErrorCode(), e.getSQLState()));
            tableData.setDescription((String) output.get(DATA_NAME_MESSAGE));
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            output.put(DATA_NAME_STATUS, false);
            output.put(
                    DATA_NAME_MESSAGE,
                    MessageFormat.format(
                            "ERROR=ClassNotFoundException, SQL_STATE={0}", e.getMessage()));
            tableData.setDescription((String) output.get(DATA_NAME_MESSAGE));
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        return output;
    }

    /**
     * Trade report for User (seller or buyer)
     *
     * @param userName User name
     * @param lookBackDays Look back days
     * @param limit Limit how many rows returned
     * @return Data for GUI rendering
     */
    public static Map<String, Object> selectMyTrade(String userName, int lookBackDays, int limit) {
        Map<String, Object> output = new HashMap<>();
        List<Object> lstRows = new ArrayList<>();
        TableData tableData =
                new TableData(
                        LST_HEADER_TRADE_BY_BEST_SELLING_ITEM,
                        lstRows,
                        COL_SEQ_TRADE_BY_BEST_SELLING_ITEM);
        output.put(DATA_NAME_DATA, tableData);
        try (Connection con = getConnection();
                PreparedStatement preparedStmt = con.prepareStatement(SQL_TRADE_MY_TRADE)) {
            preparedStmt.setInt(1, lookBackDays);
            preparedStmt.setString(2, userName);
            preparedStmt.setString(3, userName);
            preparedStmt.setInt(4, limit);
            try (ResultSet rs = preparedStmt.executeQuery()) {
                while (rs.next()) {
                    Object price = rs.getObject(1);
                    Object categoryName = rs.getObject(2);
                    Object conditionCode = rs.getObject(3);
                    Object description = rs.getObject(4);
                    Object seller = rs.getObject(5);
                    Object buyer = rs.getObject(6);
                    Object tradeDate = rs.getObject(7);
                    List<Object> currentRow = new ArrayList<>();
                    lstRows.add(currentRow);
                    currentRow.add(price);
                    currentRow.add(categoryName);
                    currentRow.add(Helper.getConditionFromCode(conditionCode.toString()));
                    currentRow.add(description);
                    currentRow.add(seller);
                    currentRow.add(buyer);
                    currentRow.add(tradeDate);
                }
            }
            output.put(DATA_NAME_STATUS, true);
            output.put(DATA_NAME_MESSAGE, "OK");
            tableData.setDescription(
                    MessageFormat.format(
                            "My Most Recent {0} Trades, Limited For The Last {1} Days",
                            limit, lookBackDays));
        } catch (SQLException e) {
            output.put(DATA_NAME_STATUS, false);
            output.put(
                    DATA_NAME_MESSAGE,
                    MessageFormat.format(
                            "ERROR={0}, SQL_STATE={1}", e.getErrorCode(), e.getSQLState()));
            tableData.setDescription((String) output.get(DATA_NAME_MESSAGE));
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            output.put(DATA_NAME_STATUS, false);
            output.put(
                    DATA_NAME_MESSAGE,
                    MessageFormat.format(
                            "ERROR=ClassNotFoundException, SQL_STATE={0}", e.getMessage()));
            tableData.setDescription((String) output.get(DATA_NAME_MESSAGE));
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        return output;
    }
}
