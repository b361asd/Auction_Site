package com.b361asd.auction.db;

/** Format SQL querying the {@link Offer} table */
public class FormatterOfferQuery extends DBBase {
    private static final String SQL_BROWSE_ACTIVE =
            "select o2.offerID, o2.seller, o2.categoryName, o2.conditionCode, o2"
                    + ".description, o2.initPrice, o2.increment, o2.minPrice, o2.startDate, o2"
                    + ".endDate, o2.status, o2.price, of1.fieldID, of1.fieldText, of1.fieldName, of1"
                    + ".fieldType from (SELECT o1.*, b.price FROM (SELECT * FROM Offer WHERE status ="
                    + " 1) o1 LEFT OUTER JOIN (SELECT b1.price, b1.offerID FROM Bid b1 WHERE b1.price"
                    + " = (SELECT MAX(price) FROM Bid b where b.offerID = b1.offerID)) b ON o1"
                    + ".offerID = b.offerID) o2 LEFT OUTER join (SELECT of.*, f1.fieldName, f1"
                    + ".fieldType FROM OfferField of, Field f1 WHERE of.fieldID = f1.fieldID) of1 on "
                    + "o2.offerID = of1.offerID order by o2.offerID";
    private static final String BASE_SQL_PLACEHOLDER_1ST =
            "select o2.offerID, o2.seller, o2.categoryName, o2.conditionCode, o2"
                    + ".description, o2.initPrice, o2.increment, o2.minPrice, o2.startDate, o2"
                    + ".endDate, o2.status, o2.price, of1.fieldID, of1.fieldText, of1.fieldName, of1"
                    + ".fieldType from (SELECT o1.*, b.price FROM (";
    private static final String BASE_SQL_PLACEHOLDER_2ND =
            ") o1 LEFT OUTER JOIN (SELECT b1.price, b1.offerID FROM Bid b1 WHERE b1.price ="
                    + " (SELECT MAX(price) FROM Bid b where b.offerID = b1.offerID)) b ON o1.offerID "
                    + "= b.offerID) o2 LEFT OUTER join (SELECT of.*, f1.fieldName, f1.fieldType FROM "
                    + "OfferField of, Field f1 WHERE of.fieldID = f1.fieldID) of1 on o2.offerID = of1"
                    + ".offerID order by o2.offerID";

    public static StringBuilder initQuerySearch() {
        StringBuilder sb = new StringBuilder();
        sb.append(BASE_SQL_PLACEHOLDER_1ST).append("select * from Offer o WHERE TRUE");
        return sb;
    }

    public static void doneQuerySearch(StringBuilder sb) {
        sb.append(BASE_SQL_PLACEHOLDER_2ND);
    }

    public static StringBuilder initQueryAlert() {
        StringBuilder sb = new StringBuilder();
        sb.append(
                        "Insert Alert (alertID, receiver, offerID, bidID, content, alertDate) select REPLACE(UUID(),'-',''), ?, ?, NULL, ?, NOW() ")
                .append("from Offer o WHERE (o.offerID = ?)");
        return sb;
    }

    public static void doneQueryAlert(StringBuilder sb) {}

    public static void initFieldCondition(StringBuilder sb) {
        sb.append(
                " and (not exists (select * from OfferField of2 where of2.offerID = o.offerID and (false");
    }

    public static void doneFieldCondition(StringBuilder sb) {
        sb.append(")))");
    }

    public static String buildSQLSimilarOffer(String categoryName, String conditionCode) {
        StringBuilder sb = initQuerySearch();
        addCondition(sb, "o.categoryName", OP_SZ_EQUAL, categoryName, null);
        addCondition(sb, "o.conditionCode", OP_INT_EQUAL, conditionCode, null);
        addCondition(sb, "o.status", OP_INT_EQUAL, "1", null);
        doneQuerySearch(sb);
        return sb.toString();
    }

    public static String buildSQLBrowseOffer() {
        return SQL_BROWSE_ACTIVE;
    }

    public static String buildSQLUserActivityOffer(String userID) {
        StringBuilder sb = initQuerySearch();
        sb.append(" and ((TRUE");
        addCondition(sb, "o.seller", OP_SZ_EQUAL, userID, null);
        sb.append(") OR EXISTS (SELECT * from Bid b2 where b2.offerID = o.offerID");
        addCondition(sb, "b2.buyer", OP_SZ_EQUAL, userID, null);
        sb.append("))");
        doneQuerySearch(sb);
        return sb.toString();
    }
}
