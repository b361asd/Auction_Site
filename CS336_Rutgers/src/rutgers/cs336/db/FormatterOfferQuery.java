package rutgers.cs336.db;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatterOfferQuery {
	private static final HashMap<String, String> sqlTokens;
	private static       Pattern                 sqlTokenPattern;

	static {
		// MySQL escape sequences: https://dev.mysql.com/doc/refman/8.0/en/string-literals.html
		String[][] search_regex_replacement = new String[][]
				  {
				  		  // Search string     Search regex        SQL replacement regex
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


	public static StringBuilder initQuery() {
		StringBuilder sb = new StringBuilder();
		sb.append(
				  "select o.offerId, o.categoryName, o.seller, o.initPrice, o.increment, o.minPrice, o.conditionCode, o.description, o.startDate, o.endDate, o.status, f.fieldID, f.fieldText from Offer o inner join OfferField f on o.offerId = f.offerId");
		//
		return sb;
	}

	public static StringBuilder addStringEqualCondition(StringBuilder sb, String columnName, String value) {
		return sb.append(" and (o.").append(columnName).append("='").append(escape(value)).append("')");
	}

	public static StringBuilder addIntEqualCondition(StringBuilder sb, String columnName, int value) {
		return sb.append(" and (o.").append(columnName).append("=").append(value).append(")");
	}

	public static StringBuilder initFieldCondition(StringBuilder sb) {
		return sb.append(" and (not exists (select * from OfferField f2 where f2.offerId = o.offerId and (");
	}

	public static StringBuilder doneFieldCondition(StringBuilder sb) {
		return sb.append(" ))) order by o.offerId, f.fieldID");
	}

	public static StringBuilder addFieldEqualCondition(StringBuilder sb, int fieldId, String value, boolean isFirst) {
		if (!isFirst) {
			sb.append(" or");
		}
		return sb.append(" (f2.fieldID = ")
				  .append(fieldId)
				  .append(" and (not (f2.fieldText = '")
				  .append(escape(value))
				  .append("')))");
	}


	public static void main(String[] args) {
		System.out.println(escape("%\"'blue"));
		StringBuilder sb = initQuery();
		sb = addStringEqualCondition(sb, "categoryName", "car");
		sb = addStringEqualCondition(sb, "seller", "user");
		sb = addIntEqualCondition(sb, "initPrice", 2);
		sb = addIntEqualCondition(sb, "increment", 4);
		sb = addIntEqualCondition(sb, "minPrice", 5);
		sb = addIntEqualCondition(sb, "conditionCode", 2);
		sb = addStringEqualCondition(sb, "description", "Scratcges");
		//
		initFieldCondition(sb);
		addFieldEqualCondition(sb, 1, "blue", true);
		addFieldEqualCondition(sb, 2, "toyota", false);
		addFieldEqualCondition(sb, 3, "400", false);
		addFieldEqualCondition(sb, 4, "yes", false);
		doneFieldCondition(sb);
		//
		System.out.println(sb.toString());
	}
}
/*
select o.offerId, o.categoryName, o.seller, o.initPrice, o.increment, o.minPrice, o.conditionCode, o.description, o.startDate, o.endDate, o.status, f.fieldID, f.fieldText from Offer o inner join OfferField f on o.offerId = f.offerId
and (o.categoryName='car')
and (o.seller='user')
and (o.initPrice=2)
and (o.increment=4)
and (o.minPrice=5)
and (o.conditionCode=2)
and (o.description='Scratcges')
and (o.startDate < NOW() )
and (o.endDate > NOW())
and (o.status=1)
and (not exists (select * from OfferField f2 where f2.offerId = o.offerId and (
(f2.fieldID = 1 and (not (f2.fieldText = 'blue')))
or (f2.fieldID = 2 and (not (f2.fieldText = 'toyota')))
or (f2.fieldID = 3 and (not (f2.fieldText = '400')))
or (f2.fieldID = 4 and (not (f2.fieldText = 'yes')))
))) order by o.offerId, f.fieldID;
*/