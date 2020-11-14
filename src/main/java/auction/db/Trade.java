package auction.db;

import auction.gui.Helper;
import auction.gui.TableData;

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

public class Trade extends DBBase {
   private static final List<String> lstHeader_tradeTotal             = Arrays.asList("Period", "Total", "Average", "Count");
   private static final List<String> lstHeader_tradeByCategoryName    = Arrays.asList("categoryName", "Total", "Average", "Count");
   private static final List<String> lstHeader_tradeBySimilar         = Arrays.asList("Similar", "Total", "Average", "Count");
   private static final List<String> lstHeader_tradeByBuyer           = Arrays.asList("Buyer", "Total", "Average", "Count");
   private static final List<String> lstHeader_tradeBySeller          = Arrays.asList("Seller", "Total", "Average", "Count");
   private static final List<String> lstHeader_tradeByUser            = Arrays.asList("User", "Total", "Average", "Count");
   private static final int[]        colSeq_tradeBy                   = {0, 1, 2, 3};
   //
   private static final List<String> lstHeader_tradeByBestSellingItem = Arrays.asList("price", "categoryName", "conditionCode",
                                                                                      "description", "seller", "buyer", "tradeDate");
   private static final int[]        colSeq_tradeByBestSellingItem    = {0, 1, 2, 3, 4, 5, 6};

   /**
    * Summary of Trade for Report
    *
    * @param lookbackdays   Days to look back
    * @param isTotal        If true, then run total sales report.
    * @param isCategoryName If true, then run category name report.
    * @param isBuyer        If true, then run buyer report.
    * @param isSeller       If true, then run seller report.
    * @param isUser         If true, then run user report (buyer and seller)
    * @return Data for GUI rendering
    */
   public static Map summaryBy(int lookbackdays, boolean isTotal, boolean isCategoryName, boolean isBuyer, boolean isSeller,
                               boolean isUser) {
      Map  output  = new HashMap();
      List lstRows = new ArrayList();
      //
      List lstHeader = null;
      if (isTotal) {
         lstHeader = lstHeader_tradeTotal;
      }
      else if (isCategoryName) {
         lstHeader = lstHeader_tradeByCategoryName;
      }
      else if (isBuyer) {
         lstHeader = lstHeader_tradeByBuyer;
      }
      else if (isSeller) {
         lstHeader = lstHeader_tradeBySeller;
      }
      else if (isUser) {
         lstHeader = lstHeader_tradeByUser;
      }
      //
      TableData tableData = new TableData(lstHeader, lstRows, colSeq_tradeBy);
      output.put(DATA_NAME_DATA, tableData);
      //
      Connection        con          = null;
      PreparedStatement preparedStmt = null;
      try {
         con = getConnection();
         //
         String sql = null;
         if (isTotal) {
            sql = SQL_TRADE_TOTAL;
         }
         else if (isCategoryName) {
            sql = SQL_TRADE_TOTAL_BY_CATEGORY;
         }
         else if (isBuyer) {
            sql = SQL_TRADE_TOTAL_BY_BUYER;
         }
         else if (isSeller) {
            sql = SQL_TRADE_TOTAL_BY_SELLER;
         }
         else if (isUser) {
            sql = SQL_TRADE_TOTAL_BY_USER;
         }
         //
         preparedStmt = con.prepareStatement(sql);
         //
         if (!isTotal) {
            if (isCategoryName) {
               preparedStmt.setInt(1, lookbackdays);
            }
            else if (isBuyer) {
               preparedStmt.setInt(1, lookbackdays);
            }
            else if (isSeller) {
               preparedStmt.setInt(1, lookbackdays);
            }
            else if (isUser) {
               preparedStmt.setInt(1, lookbackdays);
               preparedStmt.setInt(2, lookbackdays);
            }
         }
         //
         ResultSet rs = preparedStmt.executeQuery();
         //
         while (rs.next()) {
            Object person  = rs.getObject(1);
            Object Total   = rs.getObject(2);
            Object Average = rs.getObject(3);
            Object Count   = rs.getObject(4);
            //
            List currentRow = new LinkedList();
            lstRows.add(currentRow);
            //
            currentRow.add(person);
            currentRow.add(Total);
            currentRow.add(Average);
            currentRow.add(Count);
         }
         //
         output.put(DATA_NAME_STATUS, true);
         output.put(DATA_NAME_MESSAGE, "OK");
         //
         if (isTotal) {
            tableData.setDescription("Total Sales For Various Periods");
         }
         else if (isCategoryName) {
            tableData.setDescription("Sales by CategoryNames For The Last " + lookbackdays + " Days");
         }
         else if (isBuyer) {
            tableData.setDescription("Sales by Buyers For The Last " + lookbackdays + " Days");
         }
         else if (isSeller) {
            tableData.setDescription("Sales by Sellers For The Last " + lookbackdays + " Days");
         }
         else if (isUser) {
            tableData.setDescription("Sales by Users (as Either Buyer or Seller) For The Last " + lookbackdays + " Days");
         }
      }
      catch (SQLException e) {
         output.put(DATA_NAME_STATUS, false);
         output.put(DATA_NAME_MESSAGE, "ERROR=" + e.getErrorCode() + ", SQL_STATE=" + e.getSQLState());
         tableData.setDescription((String) output.get(DATA_NAME_MESSAGE));
         e.printStackTrace();
      }
      catch (ClassNotFoundException e) {
         output.put(DATA_NAME_STATUS, false);
         output.put(DATA_NAME_MESSAGE, "ERROR=" + "ClassNotFoundException" + ", SQL_STATE=" + e.getMessage());
         tableData.setDescription((String) output.get(DATA_NAME_MESSAGE));
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
    * Select similar (same category and condition) group reports
    *
    * @param lookbackdays Lookback days
    * @return Data for GUI rendering
    */
   public static Map selectGroupSimilar(int lookbackdays) {
      Map  output  = new HashMap();
      List lstRows = new ArrayList();
      //
      TableData tableData = new TableData(lstHeader_tradeBySimilar, lstRows, colSeq_tradeBy);
      output.put(DATA_NAME_DATA, tableData);
      //
      Connection        con          = null;
      PreparedStatement preparedStmt = null;
      try {
         con = getConnection();
         //
         preparedStmt = con.prepareStatement(SQL_TRADE_TOTAL_BY_SIMILARGROUP);
         preparedStmt.setInt(1, lookbackdays);
         ResultSet rs = preparedStmt.executeQuery();
         //
         while (rs.next()) {
            Object categoryName  = rs.getObject(1);
            Object conditionCode = rs.getObject(2);
            Object Total         = rs.getObject(3);
            Object Average       = rs.getObject(4);
            Object Count         = rs.getObject(5);
            //
            List currentRow = new LinkedList();
            lstRows.add(currentRow);
            //
            currentRow.add(categoryName + ", " + Helper.getConditionFromCode(conditionCode.toString()));
            currentRow.add(Total);
            currentRow.add(Average);
            currentRow.add(Count);
         }
         //
         output.put(DATA_NAME_STATUS, true);
         output.put(DATA_NAME_MESSAGE, "OK");
         //
         tableData.setDescription("Sales Grouped by Similar For The Last " + lookbackdays + " Days");
      }
      catch (SQLException e) {
         output.put(DATA_NAME_STATUS, false);
         output.put(DATA_NAME_MESSAGE, "ERROR=" + e.getErrorCode() + ", SQL_STATE=" + e.getSQLState());
         tableData.setDescription((String) output.get(DATA_NAME_MESSAGE));
         e.printStackTrace();
      }
      catch (ClassNotFoundException e) {
         output.put(DATA_NAME_STATUS, false);
         output.put(DATA_NAME_MESSAGE, "ERROR=" + "ClassNotFoundException" + ", SQL_STATE=" + e.getMessage());
         tableData.setDescription((String) output.get(DATA_NAME_MESSAGE));
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
    * Select best selling or most recent items
    *
    * @param lookbackdays  Lookback days
    * @param limit         Limit how many rows returned
    * @param isBestSelling True for best selling, false for most recent
    * @return Data for GUI rendering
    */
   public static Map selectBestSellingMostRecentItems(int lookbackdays, int limit, boolean isBestSelling) {
      Map  output  = new HashMap();
      List lstRows = new ArrayList();
      //
      TableData tableData = new TableData(lstHeader_tradeByBestSellingItem, lstRows, colSeq_tradeByBestSellingItem);
      output.put(DATA_NAME_DATA, tableData);
      //
      Connection        con          = null;
      PreparedStatement preparedStmt = null;
      try {
         con = getConnection();
         //
         preparedStmt = con.prepareStatement(isBestSelling ? SQL_TRADE_BEST_ITEM : SQL_TRADE_RECENT_ITEM);
         preparedStmt.setInt(1, lookbackdays);
         preparedStmt.setInt(2, limit);
         ResultSet rs = preparedStmt.executeQuery();
         //
         while (rs.next()) {
            Object price         = rs.getObject(1);
            Object categoryName  = rs.getObject(2);
            Object conditionCode = rs.getObject(3);
            Object description   = rs.getObject(4);
            Object seller        = rs.getObject(5);
            Object buyer         = rs.getObject(6);
            Object tradeDate     = rs.getObject(7);
            //
            List currentRow = new LinkedList();
            lstRows.add(currentRow);
            //
            currentRow.add(price);
            currentRow.add(categoryName);
            currentRow.add(Helper.getConditionFromCode(conditionCode.toString()));
            currentRow.add(description);
            currentRow.add(seller);
            currentRow.add(buyer);
            currentRow.add(tradeDate);
         }
         //
         output.put(DATA_NAME_STATUS, true);
         output.put(DATA_NAME_MESSAGE, "OK");
         //
         if (isBestSelling) {
            tableData.setDescription("Top " + limit + " Best Selling Item(s) For The Last " + lookbackdays + " Days");
         }
         else {
            tableData.setDescription("Most Recent " + limit + " Trades, Limited For The Last " + lookbackdays + " Days");
         }
      }
      catch (SQLException e) {
         output.put(DATA_NAME_STATUS, false);
         output.put(DATA_NAME_MESSAGE, "ERROR=" + e.getErrorCode() + ", SQL_STATE=" + e.getSQLState());
         tableData.setDescription((String) output.get(DATA_NAME_MESSAGE));
         e.printStackTrace();
      }
      catch (ClassNotFoundException e) {
         output.put(DATA_NAME_STATUS, false);
         output.put(DATA_NAME_MESSAGE, "ERROR=" + "ClassNotFoundException" + ", SQL_STATE=" + e.getMessage());
         tableData.setDescription((String) output.get(DATA_NAME_MESSAGE));
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
    * Trade report for User (seller or buyer)
    *
    * @param userName     User name
    * @param lookbackdays Lookback days
    * @param limit        Limit how many rows returned
    * @return Data for GUI rendering
    */
   public static Map selectMyTrade(String userName, int lookbackdays, int limit) {
      Map  output  = new HashMap();
      List lstRows = new ArrayList();
      //
      TableData tableData = new TableData(lstHeader_tradeByBestSellingItem, lstRows, colSeq_tradeByBestSellingItem);
      output.put(DATA_NAME_DATA, tableData);
      //
      Connection        con          = null;
      PreparedStatement preparedStmt = null;
      try {
         con = getConnection();
         //
         preparedStmt = con.prepareStatement(SQL_TRADE_MY_TRADE);
         preparedStmt.setInt(1, lookbackdays);
         preparedStmt.setString(2, userName);
         preparedStmt.setString(3, userName);
         preparedStmt.setInt(4, limit);
         ResultSet rs = preparedStmt.executeQuery();
         //
         while (rs.next()) {
            Object price         = rs.getObject(1);
            Object categoryName  = rs.getObject(2);
            Object conditionCode = rs.getObject(3);
            Object description   = rs.getObject(4);
            Object seller        = rs.getObject(5);
            Object buyer         = rs.getObject(6);
            Object tradeDate     = rs.getObject(7);
            //
            List currentRow = new LinkedList();
            lstRows.add(currentRow);
            //
            currentRow.add(price);
            currentRow.add(categoryName);
            currentRow.add(Helper.getConditionFromCode(conditionCode.toString()));
            currentRow.add(description);
            currentRow.add(seller);
            currentRow.add(buyer);
            currentRow.add(tradeDate);
         }
         //
         output.put(DATA_NAME_STATUS, true);
         output.put(DATA_NAME_MESSAGE, "OK");
         //
         tableData.setDescription("My Most Recent " + limit + " Trades, Limited For The Last " + lookbackdays + " Days");
      }
      catch (SQLException e) {
         output.put(DATA_NAME_STATUS, false);
         output.put(DATA_NAME_MESSAGE, "ERROR=" + e.getErrorCode() + ", SQL_STATE=" + e.getSQLState());
         tableData.setDescription((String) output.get(DATA_NAME_MESSAGE));
         e.printStackTrace();
      }
      catch (ClassNotFoundException e) {
         output.put(DATA_NAME_STATUS, false);
         output.put(DATA_NAME_MESSAGE, "ERROR=" + "ClassNotFoundException" + ", SQL_STATE=" + e.getMessage());
         tableData.setDescription((String) output.get(DATA_NAME_MESSAGE));
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


   public static void main(String[] args) {
      Map<String, String[]> parameters = new HashMap<>();
      //
      parameters.put("bidID", new String[]{"11fe20aabc7a4025928e9522544be2e3"});
      //
      //Map map = summaryByBuyerSellerUser(30, false, false, true);
      Map map = selectBestSellingMostRecentItems(30, 10, true);
      //
      System.out.println(DATA_NAME_STATUS + "= " + map.get(DATA_NAME_STATUS));
      System.out.println(DATA_NAME_MESSAGE + "= " + map.get(DATA_NAME_MESSAGE));
      System.out.println(DATA_NAME_USER_TYPE + "= " + map.get(DATA_NAME_USER_TYPE));
   }
}
