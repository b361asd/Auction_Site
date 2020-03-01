package b361asd.auction.db;

/**
 * Format SQL querying the Offer table
 */
public class FormatterOfferQuery extends DBBase {
   private static String BASE_SQL_REAL            = "select o2.offerID, o2.seller, o2.categoryName, o2.conditionCode, o2.description, o2.initPrice, o2.increment, o2.minPrice, o2.startDate, o2.endDate, o2.status, o2.price, of1.fieldID, of1.fieldText, of1.fieldName, of1.fieldType from (SELECT o1.*, b.price FROM Offer o1 LEFT OUTER JOIN (SELECT b1.price, b1.offerID FROM Bid b1 WHERE b1.price = (SELECT MAX(price) FROM Bid b where b.offerID = b1.offerID)) b ON o1.offerID = b.offerID) o2 LEFT OUTER join (SELECT of.*, f1.fieldName, f1.fieldType FROM OfferField of, Field f1 WHERE of.fieldID = f1.fieldID) of1 on o2.offerID = of1.offerID order by o2.offerID";
   //
   private static String SQL_BROWSE_ACTIVE        = "select o2.offerID, o2.seller, o2.categoryName, o2.conditionCode, o2.description, o2.initPrice, o2.increment, o2.minPrice, o2.startDate, o2.endDate, o2.status, o2.price, of1.fieldID, of1.fieldText, of1.fieldName, of1.fieldType from (SELECT o1.*, b.price FROM (SELECT * FROM Offer WHERE status = 1) o1 LEFT OUTER JOIN (SELECT b1.price, b1.offerID FROM Bid b1 WHERE b1.price = (SELECT MAX(price) FROM Bid b where b.offerID = b1.offerID)) b ON o1.offerID = b.offerID) o2 LEFT OUTER join (SELECT of.*, f1.fieldName, f1.fieldType FROM OfferField of, Field f1 WHERE of.fieldID = f1.fieldID) of1 on o2.offerID = of1.offerID order by o2.offerID";
   private static String PLACEHOLDER              = "$";
   private static String BASE_SQL_PLACEHOLDER     = "select o2.offerID, o2.seller, o2.categoryName, o2.conditionCode, o2.description, o2.initPrice, o2.increment, o2.minPrice, o2.startDate, o2.endDate, o2.status, o2.price, of1.fieldID, of1.fieldText, of1.fieldName, of1.fieldType from (SELECT o1.*, b.price FROM ($) o1 LEFT OUTER JOIN (SELECT b1.price, b1.offerID FROM Bid b1 WHERE b1.price = (SELECT MAX(price) FROM Bid b where b.offerID = b1.offerID)) b ON o1.offerID = b.offerID) o2 LEFT OUTER join (SELECT of.*, f1.fieldName, f1.fieldType FROM OfferField of, Field f1 WHERE of.fieldID = f1.fieldID) of1 on o2.offerID = of1.offerID order by o2.offerID";
   private static String BASE_SQL_PLACEHOLDER_1ST = "select o2.offerID, o2.seller, o2.categoryName, o2.conditionCode, o2.description, o2.initPrice, o2.increment, o2.minPrice, o2.startDate, o2.endDate, o2.status, o2.price, of1.fieldID, of1.fieldText, of1.fieldName, of1.fieldType from (SELECT o1.*, b.price FROM (";
   private static String BASE_SQL_PLACEHOLDER_2ND = ") o1 LEFT OUTER JOIN (SELECT b1.price, b1.offerID FROM Bid b1 WHERE b1.price = (SELECT MAX(price) FROM Bid b where b.offerID = b1.offerID)) b ON o1.offerID = b.offerID) o2 LEFT OUTER join (SELECT of.*, f1.fieldName, f1.fieldType FROM OfferField of, Field f1 WHERE of.fieldID = f1.fieldID) of1 on o2.offerID = of1.offerID order by o2.offerID";


   public static StringBuilder initQuerySearch() {
      StringBuilder sb = new StringBuilder();
      sb.append(BASE_SQL_PLACEHOLDER_1ST);
      sb.append("select * from Offer o WHERE TRUE");
      //
      return sb;
   }

   public static void doneQuerySearch(StringBuilder sb) {
      sb.append(BASE_SQL_PLACEHOLDER_2ND);
   }


   public static StringBuilder initQueryAlert() {
      StringBuilder sb = new StringBuilder();
      sb.append("Insert Alert (alertID, receiver, offerID, bidID, content, alertDate) select REPLACE(UUID(),'-',''), ?, ?, NULL, ?, NOW() from Offer o WHERE (o.offerID = ?)");
      //
      return sb;
   }

   public static void doneQueryAlert(StringBuilder sb) {
   }


   public static void initFieldCondition(StringBuilder sb) {
      sb.append(" and (not exists (select * from OfferField of2 where of2.offerID = o.offerID and (false");
   }

   public static void doneFieldCondition(StringBuilder sb) {
      sb.append(")))");
   }


   public static String buildSQLSimilarOffer(String categoryName, String conditionCode) {
      StringBuilder sb = initQuerySearch();
      //
      addCondition(sb, "o.categoryName", OP_SZ_EQUAL, categoryName, null);
      addCondition(sb, "o.conditionCode", OP_INT_EQUAL, conditionCode, null);
      addCondition(sb, "o.status", OP_INT_EQUAL, "1", null);
      //
      doneQuerySearch(sb);
      //
      return sb.toString();
   }


   public static String buildSQLBrowseOffer() {
      return SQL_BROWSE_ACTIVE;
   }


   public static String buildSQLUserActivityOffer(String userID) {
      StringBuilder sb = initQuerySearch();
      //
      sb.append(" and ((TRUE");
      addCondition(sb, "o.seller", OP_SZ_EQUAL, userID, null);
      sb.append(") OR EXISTS (SELECT * from Bid b2 where b2.offerID = o.offerID");
      addCondition(sb, "b2.buyer", OP_SZ_EQUAL, userID, null);
      sb.append("))");
      //
      doneQuerySearch(sb);
      //
      return sb.toString();
   }


   public static void main1(String[] args) {
      if (false) {
         StringBuilder sb = initQuerySearch();
         //addCondition(sb, "o.offerID", OP_SZ_START_WITH, "Scratcges", null);
         //addCondition(sb, "o.seller", OP_SZ_NOT_EQUAL, "us'er", null);
         addCondition(sb, "o.categoryName", OP_SZ_EQUAL_MULTI_NO_ESCAPE, "'car','motorbike'", null);
         //addCondition(sb, "o.conditionCode", OP_INT_EQUAL_MULTI, "1,3,4,5,6", null);
         //addCondition(sb, "o.description", OP_SZ_CONTAIN, "Scratcges", null);
         //addCondition(sb, "o.initPrice", OP_INT_EQUAL_OR_OVER, "2", null);
         //addCondition(sb, "o.increment", OP_INT_EQUAL_OR_OVER, "4", null);
         //addCondition(sb, "o.minPrice", OP_ANY, "5", null);
         //addCondition(sb, "o.status", OP_INT_EQUAL_OR_OVER, "5", null);
         //addCondition(sb, "o.price", OP_INT_EQUAL_OR_OVER, "5", null);
         //
         initFieldCondition(sb);
         //addFieldCondition(sb, "1", OP_INT_BETWEEN, "23", "56");
         //addFieldCondition(sb, "2", OP_BOOL_TRUE, "toyota", "");
         //addFieldCondition(sb, "3", OP_BOOL_FALSE, "400", "");
         //addFieldCondition(sb, "4", OP_INT_EQUAL_OR_UNDER, "400", "");
         //addFieldCondition(sb, "5", OP_INT_NOT_EQUAL, "400", "");
         //addFieldCondition(sb, "6", OP_INT_NOT_EQUAL, "400", "");
         //addFieldCondition(sb, "7", OP_INT_NOT_EQUAL, "400", "");
         //addFieldCondition(sb, "8", OP_INT_NOT_EQUAL, "400", "");
         doneFieldCondition(sb);
         //
         System.out.println(sb.toString());
      }
      //
      //
      if (false) {
         StringBuilder sb = initQueryAlert();
         //addCondition(sb, "o.offerID", OP_SZ_START_WITH, "Scratcges", null);
         //addCondition(sb, "o.seller", OP_SZ_NOT_EQUAL, "us'er", null);
         //addCondition(sb, "o.categoryName", OP_SZ_NOT_EQUAL, "car", null);
         addCondition(sb, "o.conditionCode", OP_INT_EQUAL_MULTI, "1,3,4,5,6", null);
         //addCondition(sb, "o.description", OP_SZ_CONTAIN, "Scratcges", null);
         //addCondition(sb, "o.initPrice", OP_INT_EQUAL_OR_OVER, "2", null);
         //addCondition(sb, "o.increment", OP_INT_EQUAL_OR_OVER, "4", null);
         //addCondition(sb, "o.minPrice", OP_ANY, "5", null);
         //addCondition(sb, "o.status", OP_INT_EQUAL_OR_OVER, "5", null);
         //addCondition(sb, "o.price", OP_INT_EQUAL_OR_OVER, "5", null);
         //
         initFieldCondition(sb);
         //addFieldCondition(sb, "1", OP_INT_BETWEEN, "23", "56");
         //addFieldCondition(sb, "2", OP_BOOL_TRUE, "toyota", "");
         //addFieldCondition(sb, "3", OP_BOOL_FALSE, "400", "");
         //addFieldCondition(sb, "4", OP_INT_EQUAL_OR_UNDER, "400", "");
         //addFieldCondition(sb, "5", OP_INT_NOT_EQUAL, "400", "");
         //addFieldCondition(sb, "6", OP_INT_NOT_EQUAL, "400", "");
         //addFieldCondition(sb, "7", OP_INT_NOT_EQUAL, "400", "");
         //addFieldCondition(sb, "8", OP_INT_NOT_EQUAL, "400", "");
         doneFieldCondition(sb);
         //
         System.out.println(sb.toString());
      }
      //
      //
      if (true) {
         System.out.println(buildSQLSimilarOffer("car", "1"));
      }
   }

   public static void main(String[] args) {
      System.out.println(buildSQLUserActivityOffer("user"));
   }
}


/* For Browse
select o.offerID, o.seller, o.categoryName, o.conditionCode, o.description, o.initPrice, o.increment, o.minPrice, o.startDate, o.endDate, o.status, o.price, of1.fieldID, of1.fieldText, of1.fieldName, of1.fieldType from (SELECT o1.*, b.price FROM Offer o1 LEFT OUTER JOIN (SELECT b1.price, b1.offerID FROM Bid b1 WHERE b1.price = (SELECT MAX(price) FROM Bid b where b.offerID = b1.offerID)) b ON o1.offerID = b.offerID) o LEFT OUTER join (SELECT of.*, f1.fieldName, f1.fieldType FROM OfferField of, Field f1 WHERE of.fieldID = f1.fieldID) of1 on o.offerID = of1.offerID and (o.status = 1) order by o.offerID
*/

/* For Similar
select o.offerID, o.seller, o.categoryName, o.conditionCode, o.description, o.initPrice, o.increment, o.minPrice, o.startDate, o.endDate, o.status, o.price, of1.fieldID, of1.fieldText, of1.fieldName, of1.fieldType from (SELECT o1.*, b.price FROM Offer o1 LEFT OUTER JOIN (SELECT b1.price, b1.offerID FROM Bid b1 WHERE b1.price = (SELECT MAX(price) FROM Bid b where b.offerID = b1.offerID)) b ON o1.offerID = b.offerID) o LEFT OUTER join (SELECT of.*, f1.fieldName, f1.fieldType FROM OfferField of, Field f1 WHERE of.fieldID = f1.fieldID) of1 on o.offerID = of1.offerID and (o.categoryName = ?) and (o.conditionCode = ?) and (o.status = 1) order by o.offerID
*/

/* General Search
select o.offerID, o.seller, o.categoryName, o.conditionCode, o.description, o.initPrice, o.increment, o.minPrice, o.startDate, o.endDate, o.status, o.price, of1.fieldID, of1.fieldText, of1.fieldName, of1.fieldType from (SELECT o1.*, b.price FROM Offer o1 LEFT OUTER JOIN (SELECT b1.price, b1.offerID FROM Bid b1 WHERE b1.price = (SELECT MAX(price) FROM Bid b where b.offerID = b1.offerID)) b ON o1.offerID = b.offerID) o LEFT OUTER join (SELECT of.*, f1.fieldName, f1.fieldType FROM OfferField of, Field f1 WHERE of.fieldID = f1.fieldID) of1 on o.offerID = of1.offerID
and (o.offerID='aaa')
and (o.seller='user')
and (o.categoryName='car')
and (o.conditionCode in (1,2))
and (o.description='Scratcges')
and (o.initPrice=2)
and (o.increment=4)
and (o.minPrice=5)
and (o.startDate < NOW() )
and (o.endDate > NOW())
and (o.status=1)
and (o.price=2)
and (not exists (select * from OfferField of2 where of2.offerID = o.offerID and (false
or (of2.fieldID = 1 and (not (of2.fieldText = 'blue')))
or (of2.fieldID = 2 and (not (of2.fieldText = 'toyota')))
or (of2.fieldID = 3 and (not (of2.fieldText = '400')))
or (of2.fieldID = 4 and (not (of2.fieldText = 'yes')))
))) order by o.offerID, of1.fieldID;
*/

/* For Alert
Insert Alert (alertID, receiver, offerID, bidID, content, alertDate) select REPLACE(UUID(),'-',''), ?, ?, NULL, ?, NOW() from Offer o WHERE (o.offerID = ?)
and (o.seller='user')
and (o.conditionCode in (1,2))
and (o.description='Scratcges')
and (o.initPrice=2)
and (o.increment=4)
and (o.minPrice=5)
and (o.startDate < NOW() )
and (o.endDate > NOW())
and (o.status=1)
and (o.price=2)
and (not exists (select * from OfferField of2 where of2.offerID = o.offerID and (false
or (of2.fieldID = 1 and (not (of2.fieldText = 'blue')))
or (of2.fieldID = 2 and (not (of2.fieldText = 'toyota')))
or (of2.fieldID = 3 and (not (of2.fieldText = '400')))
or (of2.fieldID = 4 and (not (of2.fieldText = 'yes')))
)));
*/

/* Conditions
select * from Offer o WHERE TRUE
AND (o.offerID = '6bc17ded8d0e4300ae8ce80a5fa85b8d')
and (o.seller='user')
and (o.conditionCode in (1,2))
and (o.status=1)
and (not exists (select * from OfferField of2 where of2.offerID = o.offerID and (false
or (of2.fieldID = 1 and (not (of2.fieldText = 'blue')))
or (of2.fieldID = 2 and (not (of2.fieldText = 'toyota')))
or (of2.fieldID = 3 and (not (of2.fieldText = '400')))
or (of2.fieldID = 4 and (not (of2.fieldText = 'yes')))
)));
*/
