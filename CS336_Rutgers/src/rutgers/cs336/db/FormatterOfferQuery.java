package rutgers.cs336.db;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatterOfferQuery extends DBBase {


	public static StringBuilder initQuerySearch() {
		StringBuilder sb = new StringBuilder();
		sb.append(
				  "select o.offerID, o.seller, o.categoryName, o.conditionCode, o.description, o.initPrice, o.increment, o.minPrice, o.startDate, o.endDate, o.status, o.price, of1.fieldID, of1.fieldText, of1.fieldName, of1.fieldType from (SELECT o1.*, b.price FROM Offer o1 LEFT OUTER JOIN (SELECT b1.price, b1.offerID FROM Bid b1 WHERE b1.price = (SELECT MAX(price) FROM Bid b where b.offerID = b1.offerID)) b ON o1.offerID = b.offerID) o inner join (SELECT of.*, f1.fieldName, f1.fieldType FROM OfferField of, Field f1 WHERE of.fieldID = f1.fieldID) of1 on o.offerID = of1.offerID");
		//
		return sb;
	}

	public static StringBuilder initQueryAlert(String receiver, String categoryName) {
		StringBuilder sb = new StringBuilder();
		sb.append("insert Alert (alertID, receiver, offerID, bidID, alertDate, dismissedDate) select REPLACE(UUID(),'-',''), '").append(receiver).append("', o.offerID, NULL, NOW(), NULL from Offer o WHERE (o.offerID='$offerID$') and (o.categoryName='").append(categoryName).append("')");
		//
		return sb;
	}



	public static StringBuilder initFieldCondition(StringBuilder sb) {
		return sb.append(" and (not exists (select * from OfferField of2 where of2.offerID = o.offerID and (false");
	}



	public static StringBuilder doneFieldConditionSearch(StringBuilder sb) {
		return sb.append(" ))) order by o.offerID, of1.fieldID");
	}

	public static StringBuilder doneFieldConditionAlert(StringBuilder sb) {
		return sb.append(" )))");
	}


	public static StringBuilder buildSQLSimilarOffer(String categoryName, String conditionCode) {
		StringBuilder sb = new StringBuilder();
		sb.append(
				  "select o.offerID, o.seller, o.categoryName, o.conditionCode, o.description, o.initPrice, o.increment, o.minPrice, o.startDate, o.endDate, o.status, o.price, of1.fieldID, of1.fieldText, of1.fieldName, of1.fieldType from (SELECT o1.*, b.price FROM Offer o1 LEFT OUTER JOIN (SELECT b1.price, b1.offerID FROM Bid b1 WHERE b1.price = (SELECT MAX(price) FROM Bid b where b.offerID = b1.offerID)) b ON o1.offerID = b.offerID) o inner join (SELECT of.*, f1.fieldName, f1.fieldType FROM OfferField of, Field f1 WHERE of.fieldID = f1.fieldID) of1 on o.offerID = of1.offerID and (o.categoryName = '")
				  .append(categoryName)
				  .append("') and (o.conditionCode = ")
				  .append(conditionCode)
				  .append(") and (o.status = 1) order by o.offerID");
		//
		return sb;
	}


	public static String buildSQLBrowseOffer() {
		return "select o.offerID, o.seller, o.categoryName, o.conditionCode, o.description, o.initPrice, o.increment, o.minPrice, o.startDate, o.endDate, o.status, o.price, of1.fieldID, of1.fieldText, of1.fieldName, of1.fieldType from (SELECT o1.*, b.price FROM Offer o1 LEFT OUTER JOIN (SELECT b1.price, b1.offerID FROM Bid b1 WHERE b1.price = (SELECT MAX(price) FROM Bid b where b.offerID = b1.offerID)) b ON o1.offerID = b.offerID) o inner join (SELECT of.*, f1.fieldName, f1.fieldType FROM OfferField of, Field f1 WHERE of.fieldID = f1.fieldID) of1 on o.offerID = of1.offerID and (o.status = 1) order by o.offerID";
	}

	
	public static void main(String[] args) {
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
			doneFieldConditionSearch(sb);
			//
			System.out.println(sb.toString());
		}
		//
		//
		if (false) {
			StringBuilder sb = initQueryAlert("creator", "truck");
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
			doneFieldConditionAlert(sb);
			//
			System.out.println(sb.toString());
		}
		//
		//
		if (true) {
			StringBuilder sb = buildSQLSimilarOffer("car", "1");
			//
			System.out.println(sb.toString());
		}
	}


}


/* For Browse
select o.offerID, o.seller, o.categoryName, o.conditionCode, o.description, o.initPrice, o.increment, o.minPrice, o.startDate, o.endDate, o.status, o.price, of1.fieldID, of1.fieldText, of1.fieldName, of1.fieldType from (SELECT o1.*, b.price FROM Offer o1 LEFT OUTER JOIN (SELECT b1.price, b1.offerID FROM Bid b1 WHERE b1.price = (SELECT MAX(price) FROM Bid b where b.offerID = b1.offerID)) b ON o1.offerID = b.offerID) o inner join (SELECT of.*, f1.fieldName, f1.fieldType FROM OfferField of, Field f1 WHERE of.fieldID = f1.fieldID) of1 on o.offerID = of1.offerID and (o.status = 1) order by o.offerID
*/


/* For Similar
select o.offerID, o.seller, o.categoryName, o.conditionCode, o.description, o.initPrice, o.increment, o.minPrice, o.startDate, o.endDate, o.status, o.price, of1.fieldID, of1.fieldText, of1.fieldName, of1.fieldType from (SELECT o1.*, b.price FROM Offer o1 LEFT OUTER JOIN (SELECT b1.price, b1.offerID FROM Bid b1 WHERE b1.price = (SELECT MAX(price) FROM Bid b where b.offerID = b1.offerID)) b ON o1.offerID = b.offerID) o inner join (SELECT of.*, f1.fieldName, f1.fieldType FROM OfferField of, Field f1 WHERE of.fieldID = f1.fieldID) of1 on o.offerID = of1.offerID and (o.categoryName = ?) and (o.conditionCode = ?) and (o.status = 1) order by o.offerID
*/

/* General Search
select o.offerID, o.seller, o.categoryName, o.conditionCode, o.description, o.initPrice, o.increment, o.minPrice, o.startDate, o.endDate, o.status, o.price, of1.fieldID, of1.fieldText, of1.fieldName, of1.fieldType from (SELECT o1.*, b.price FROM Offer o1 LEFT OUTER JOIN (SELECT b1.price, b1.offerID FROM Bid b1 WHERE b1.price = (SELECT MAX(price) FROM Bid b where b.offerID = b1.offerID)) b ON o1.offerID = b.offerID) o inner join (SELECT of.*, f1.fieldName, f1.fieldType FROM OfferField of, Field f1 WHERE of.fieldID = f1.fieldID) of1 on o.offerID = of1.offerID
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
insert Alert (alertID, receiver, offerID, bidID, alertDate, dismissedDate) select REPLACE(UUID(),'-',''), '$receiver$', o.offerID, NULL, NOW(), NULL from Offer o WHERE (o.offerID='$offerID$') and (o.categoryName='$categoryName$')
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
