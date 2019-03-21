package rutgers.cs336.db;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatterOfferQuery {
	private static       String                  OP_ANY                = "any";
	//
	public static        String                  OP_SZ_EQUAL           = "szequal";
	private static       String                  OP_SZ_NOT_EQUAL       = "sznotequal";
	private static       String                  OP_SZ_START_WITH      = "startwith";
	private static       String                  OP_SZ_CONTAIN         = "contain";
	//
	public static        String                  OP_INT_EQUAL          = "intequal";
	public static        String                  OP_INT_EQUAL_MULTI    = "intequalmulti";
	private static       String                  OP_INT_NOT_EQUAL      = "intnotequal";
	private static       String                  OP_INT_EQUAL_OR_OVER  = "equalorover";
	private static       String                  OP_INT_EQUAL_OR_UNDER = "equalorunder";
	private static       String                  OP_INT_BETWEEN        = "between";
	//
	private static       String                  OP_BOOL_TRUE          = "true";
	private static       String                  OP_BOOL_FALSE         = "false";
	//
	private static final HashMap<String, String> sqlTokens;
	private static       Pattern                 sqlTokenPattern;

	static {
		// MySQL escape sequences: https://dev.mysql.com/doc/refman/8.0/en/string-literals.html
		String[][] search_regex_replacement = new String[][]
				  {	//   Search string       Search regex        SQL replacement regex
							 {   "\u0000"    ,       "\\x00"     ,       "\\\\0"     },
							 {   "'"         ,       "'"         ,       "\\\\'"     },
							 {   "\""        ,       "\""        ,       "\\\\\""    },
							 {   "\b"        ,       "\\x08"     ,       "\\\\b"     },
							 {   "\n"        ,       "\\n"       ,       "\\\\n"     },
							 {   "\r"        ,       "\\r"       ,       "\\\\r"     },
							 {   "\t"        ,       "\\t"       ,       "\\\\t"     },
							 {   "\u001A"    ,       "\\x1A"     ,       "\\\\Z"     },
							 {   "\\"        ,       "\\\\"      ,       "\\\\\\\\"  }
				  };
		//
		sqlTokens = new HashMap<>();
		StringBuilder patternStr = new StringBuilder();
		for (String[] srr : search_regex_replacement) {
			sqlTokens.put(srr[0], srr[2]);
			patternStr.append((patternStr.length() == 0) ? "" : "|").append(srr[1]);
		}
		sqlTokenPattern = Pattern.compile('(' + patternStr.toString() + ')');
	}


	public static String escape(String s) {
		Matcher      matcher = sqlTokenPattern.matcher(s);
		StringBuffer sb      = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(sb, sqlTokens.get(matcher.group(1)));
		}
		matcher.appendTail(sb);
		return sb.toString();
	}


	public static StringBuilder initQuery(String categoryName) {
		StringBuilder sb = new StringBuilder();
		sb.append("select o.offerId, o.seller, o.categoryName, o.conditionCode, o.description, o.initPrice, o.increment, o.minPrice, o.startDate, o.endDate, o.status, o.price, of1.fieldID, of1.fieldText, of1.fieldName, of1.fieldType from (SELECT o1.*, b.price FROM Offer o1 LEFT OUTER JOIN (SELECT b1.price, b1.offerId FROM Bid b1 WHERE b1.price = (SELECT MAX(price) FROM Bid b where b.offerId = b1.offerId)) b ON o1.offerId = b.offerId) o inner join (SELECT of.*, cf1.sortOrder, f1.fieldName, f1.fieldType FROM OfferField of, CategoryField cf1, Field f1 WHERE of.fieldID = cf1.fieldID AND of.fieldID = f1.fieldID AND cf1.categoryName = '").append(categoryName).append("') of1 on o.offerId = of1.offerId");
		//
		return sb;
	}


	private static String oneCondition(String columnName, String op, String value, String valueAdd, boolean isCasting) {
		String output = "";
		//
		if (op.equals(OP_SZ_EQUAL) || op.equals(OP_SZ_NOT_EQUAL) || op.equals(OP_SZ_START_WITH) || op.equals(OP_SZ_CONTAIN)) {      // String
			value = escape((value == null ? "" : value.trim())).toUpperCase();
			if (value.equals("")) {
				output = "";
			}
			else {
				if (op.equals(OP_SZ_EQUAL)) {
					output = "(UPPER(" + columnName + ") = '" + value + "')";
				}
				else if (op.equals(OP_SZ_NOT_EQUAL)) {
					output = "(NOT UPPER(" + columnName + ") = '" + value + "')";
				}
				else if (op.equals(OP_SZ_START_WITH)) {
					output = "(UPPER(" + columnName + ") LIKE '" + value + "%')";
				}
				else if (op.equals(OP_SZ_CONTAIN)) {
					output = "(UPPER(" + columnName + ") LIKE '%" + value + "%')";
				}
			}
		}
		else if (op.equals(OP_INT_EQUAL) || op.equals(OP_INT_EQUAL_MULTI) || op.equals(OP_INT_NOT_EQUAL) || op.equals(OP_INT_EQUAL_OR_OVER) || op.equals(OP_INT_EQUAL_OR_UNDER) || op.equals(OP_INT_BETWEEN)) {      // Integer
			value = escape((value == null ? "" : value.trim())).toUpperCase();
			if (value.equals("")) {
				output = "";
			}
			else {
				if (op.equals(OP_INT_EQUAL)) {
					if (isCasting) {
						output = "(CAST(" + columnName + " AS SIGNED) = CAST(" + value + " AS SIGNED))";
					}
					else {
						output = "(" + columnName + " = " + value + ")";
					}
				}
				else if (op.equals(OP_INT_EQUAL_MULTI)) {
					if (isCasting) {
						output = "(CAST(" + columnName + " AS SIGNED) = CAST(" + value + " AS SIGNED))";
					}
					else {
						output = "(" + columnName + " IN (" + value + "))";
					}
				}
				else if (op.equals(OP_INT_NOT_EQUAL)) {
					if (isCasting) {
						output = "(NOT CAST(" + columnName + " AS SIGNED) = CAST(" + value + " AS SIGNED))";
					}
					else {
						output = "(NOT " + columnName + " = " + value + ")";
					}
				}
				else if (op.equals(OP_INT_EQUAL_OR_OVER)) {
					if (isCasting) {
						output = "(CAST(" + columnName + " AS SIGNED) >= CAST(" + value + " AS SIGNED))";
					}
					else {
						output = "(" + columnName + " >= " + value + ")";
					}
				}
				else if (op.equals(OP_INT_EQUAL_OR_UNDER)) {
					if (isCasting) {
						output = "(CAST(" + columnName + " AS SIGNED) <= CAST(" + value + " AS SIGNED))";
					}
					else {
						output = "(" + columnName + " <= " + value + ")";
					}
				}
				else if (op.equals(OP_INT_BETWEEN)) {
					valueAdd = escape((valueAdd == null ? "" : valueAdd.trim())).toUpperCase();
					if (valueAdd.equals("")) {
						output = "";
					}
					else {
						if (isCasting) {
							output = "(CAST(" + columnName + " AS SIGNED) BETWEEN CAST(" + value + " AS SIGNED) AND CAST(" + valueAdd + " AS SIGNED))";
						}
						else {
							output = "(" + columnName + " BETWEEN " + value + " AND " + valueAdd + ")";
						}
					}
				}
			}
		}
		else if (op.equals(OP_BOOL_TRUE) || op.equals(OP_BOOL_FALSE)) {                  // Boolean
			if (op.equals(OP_BOOL_TRUE)) {
				if (isCasting) {
					output = "(UPPER(" + columnName + ") = 'TRUE')";
				}
				else {
					output = "(" + columnName + ")";
				}
			}
			else {   // op.equals(OP_BOOL_FALSE)
				if (isCasting) {
					output = "(UPPER(" + columnName + ") = 'FALSE')";
				}
				else {
					output = "(NOT " + columnName + ")";
				}
			}
		}
		//
		return output;
	}


	public static StringBuilder addCondition(StringBuilder sb, String columnName, String op, String value, String valueAdd) {
		if (op.equals(OP_ANY)) {
			// Do Nothing
		}
		else {
			String temp = oneCondition("o." + columnName, op, value, valueAdd, false);
			if (temp.length() > 0) {
				sb.append(" and ").append(temp);
			}
		}
		//
		return sb;
	}


	public static StringBuilder initFieldCondition(StringBuilder sb) {
		return sb.append(" and (not exists (select * from OfferField of2 where of2.offerId = o.offerId and (false");
	}

	public static StringBuilder addFieldCondition(StringBuilder sb, String fieldId, String op, String value, String valueAdd) {
		if (op.equals(OP_ANY)) {
			// Do Nothing
		}
		else {
			String temp = oneCondition("of2.fieldText", op, value, valueAdd, true);
			if (temp.length() > 0) {
				sb.append(" or (of2.fieldID = ").append(fieldId).append(" and (not ").append(temp).append("))");
			}
		}
		//
		return sb;
	}

	public static StringBuilder doneFieldCondition(StringBuilder sb) {
		return sb.append(" ))) order by o.offerId, of1.fieldID");
	}


	public static void main(String[] args) {
		StringBuilder sb = initQuery("truck");
		//addCondition(sb, "offerID", OP_SZ_START_WITH, "Scratcges", null);
		//addCondition(sb, "seller", OP_SZ_NOT_EQUAL, "us'er", null);
		//addCondition(sb, "categoryName", OP_SZ_NOT_EQUAL, "car", null);
		addCondition(sb, "conditionCode", OP_INT_EQUAL_MULTI, "1,3,4,5,6", null);
		//addCondition(sb, "description", OP_SZ_CONTAIN, "Scratcges", null);
		//addCondition(sb, "initPrice", OP_INT_EQUAL_OR_OVER, "2", null);
		//addCondition(sb, "increment", OP_INT_EQUAL_OR_OVER, "4", null);
		//addCondition(sb, "minPrice", OP_ANY, "5", null);
		//addCondition(sb, "status", OP_INT_EQUAL_OR_OVER, "5", null);
		//addCondition(sb, "price", OP_INT_EQUAL_OR_OVER, "5", null);
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
}
/*
select o.offerId, o.seller, o.categoryName, o.conditionCode, o.description, o.initPrice, o.increment, o.minPrice, o.startDate, o.endDate, o.status, o.price, of1.fieldID, of1.fieldText, of1.fieldName, of1.fieldType from (SELECT o1.*, b.price FROM Offer o1 LEFT OUTER JOIN (SELECT b1.price, b1.offerId FROM Bid b1 WHERE b1.price = (SELECT MAX(price) FROM Bid b where b.offerId = b1.offerId)) b ON o1.offerId = b.offerId) o inner join (SELECT of.*, cf1.sortOrder, f1.fieldName, f1.fieldType FROM OfferField of, CategoryField cf1, Field f1 WHERE of.fieldID = cf1.fieldID AND of.fieldID = f1.fieldID AND cf1.categoryName = 'truck') of1 on o.offerId = of1.offerId
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
and (not exists (select * from OfferField of2 where of2.offerId = o.offerId and (false
or (of2.fieldID = 1 and (not (of2.fieldText = 'blue')))
or (of2.fieldID = 2 and (not (of2.fieldText = 'toyota')))
or (of2.fieldID = 3 and (not (of2.fieldText = '400')))
or (of2.fieldID = 4 and (not (of2.fieldText = 'yes')))
))) order by o.offerId, of1.sortOrder;
*/