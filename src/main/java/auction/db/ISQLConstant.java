package auction.db;

/**
 * Constants and SQL statement patterns.
 */
public interface ISQLConstant {

   // User
   String SQL_USER_INSERT        = "INSERT INTO User (username, password, email, firstname, lastname, address, phone, active, userType) " +
                                   "SELECT ?, ?, ?, ?, ?, ?, ?, true, ? FROM DUAL WHERE NOT EXISTS (SELECT * FROM User u WHERE UPPER(u" +
                                   ".username) = UPPER(?))";
   String SQL_USER_AUTH          = "SELECT password, firstname, lastname, active, userType FROM User WHERE UPPER(username) = UPPER(?)";
   String SQL_USER_SELECT        =
           "SELECT username, password, email, firstname, lastname, address, phone, active FROM User WHERE " + "userType = ?";
   String SQL_USER_SELECT_ONE    = "SELECT username, password, email, firstname, lastname, address, phone, active FROM User WHERE UPPER" +
                                   "(username) = UPPER(?) AND userType = ?";
   String SQL_USER_SELECT_USERID = "SELECT Distinct username FROM User WHERE userType = 3 order by username";
   String SQL_USER_ACTIVE        = "UPDATE User SET active = true where UPPER(username) = UPPER(?)";
   String SQL_USER_INACTIVE      = "UPDATE User SET active = false where UPPER(username) = UPPER(?)";
   String SQL_USER_UPDATE        = "UPDATE User SET password = ?, email = ?, firstname = ?, lastname = ?, address = ?, phone = ? WHERE " +
                                   "UPPER(username) = UPPER(?) AND userType = ?";


   // Bid
   String SQL_BID_INSERT              = "INSERT Bid (bidID, offerID, buyer, price, autoRebidLimit, bidDate) VALUES (?, ?, ?, ?, ?, NOW())";
   String SQL_BID_UPDATE              = "UPDATE Bid SET price = ?, autoRebidLimit = ?, bidDate = NOW() WHERE bidID = ?";
   String SQL_BID_DELETE              = "DELETE from Bid where bidID = ?";
   //   String SQL_BID_SELECT_EX           = "SELECT bidID, offerID, buyer, price, autoRebidLimit, bidDate FROM Bid";
   //   String SQL_BID_SELECT_BY_OFFERID   = "SELECT bidID, buyer, price, bidDate FROM Bid b WHERE b.offerID = ? ORDER BY price DESC";
   String SQL_BID_SELECT_MAX_PRICE    = "SELECT o.offerID, seller, categoryName, conditionCode, description, initPrice, increment, " +
                                        "minPrice, startDate, endDate, status, bidID, buyer, price, autoRebidLimit, bidDate FROM (select " +
                                        "* from Offer where offerID = ?) o LEFT OUTER JOIN Bid b ON o.offerID = b.offerID AND b.price = " +
                                        "(SELECT MAX(b2.price) FROM Bid b2 WHERE b2.offerID = o.offerID)";
   String SQL_BID_SELECT_MAX_PRICE_EX = "SELECT o.offerID, seller, categoryName, conditionCode, description, initPrice, increment, " +
                                        "minPrice, startDate, endDate, status, bidID, buyer, price, autoRebidLimit, bidDate FROM (select " +
                                        "* from Offer where offerID = ?) o LEFT OUTER JOIN Bid b ON o.offerID = b.offerID AND b.bidID <> " +
                                        "? AND b.price = (SELECT MAX(b2.price) FROM Bid b2 WHERE b2.offerID = o.offerID AND b2.bidID <> ?)";


   // CategoryField
   String SQL_CATEGORYFIELD_SELECT = "SELECT categoryName, CategoryField.fieldID, fieldName, fieldType FROM CategoryField INNER JOIN " +
                                     "Field ON CategoryField.fieldID = Field.fieldID ORDER BY categoryName, CategoryField.fieldID";
   String SQL_FIELD_SELECT         = "SELECT fieldID, fieldName, fieldType FROM Field";


   // Offer
   String SQL_OFFER_INSERT = "INSERT INTO Offer (offerID, categoryName, seller, initPrice, increment, minPrice, conditionCode, " +
                             "description, startDate, endDate, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW(), DATE_ADD(STR_TO_DATE(?," +
                             "'%Y-%m-%dT%H:%i:%s'), INTERVAL 4 HOUR), 1)";
   String SQL_OFFER_MODIFY = "UPDATE Offer SET minPrice = ?, conditionCode = ?, description = ? WHERE offerID = ? AND status = 1";
   String SQL_OFFER_CANCEL = "DELETE FROM Offer WHERE offerID = ? AND status = 1";
   //   String SQL_OFFER_SELECT = "SELECT categoryName, seller, min_price, description, startDate, endDate, status FROM Offer WHERE
   //   offerID = ?";
   //   String SQL_OFFER_SEARCH = "SELECT offerID, categoryName, seller, min_price, description, startDate, endDate FROM Offer WHERE
   //   status = " +
   //                             "1 and categoryName = ? and description LIKE ?";


   // OfferField
   String SQL_OFFERFIELD_INSERT = "INSERT INTO OfferField (offerID, fieldID, fieldText) VALUES (?, ?, ?)";
   String SQL_OFFERFIELD_DELETE = "DELETE from OfferField WHERE offerID = ?";
   //   String SQL_OFFERFIELD_SELECT = "SELECT OfferField.fieldID, fieldName, fieldType, fieldText FROM OfferField INNER JOIN Field ON " +
   //                                  "OfferField.fieldID = Field.fieldID WHERE OfferField.offerID = ? ORDER BY OfferField.fieldID";
   //   String SQL_OFFERFIELD_SEARCH = "SELECT OfferField.offerID, OfferField.fieldID, fieldName, fieldType, fieldText FROM OfferField
   //   INNER " +
   //                                  "JOIN Field ON OfferField.fieldID = Field.fieldID WHERE OfferField.offerID IN (SELECT offerID FROM
   //                                  " +
   //                                  "Offer WHERE status = 1 and categoryName = ? and description LIKE ?)";


   // Trade
   String SQL_TRADE_VIEW                  = "(SELECT t.tradeID, o.offerID, b.bidID, tradeDate, seller, categoryName, conditionCode, " +
                                            "description, status, buyer, price from Trade t, Offer o, Bid b WHERE t.offerID = o.offerID " +
                                            "and t.bidID = b.bidID AND tradeDate > DATE_SUB(NOW(), INTERVAL ? DAY)) tob";
   //
   String SQL_TRADE_TOTAL                 =
           "(SELECT 'For Last 24 Hours' as Period, IFNULL(SUM(price),0) AS Total, IFNULL(AVG(price),0) as Average, COUNT(*) AS Count FROM" +
           " " + SQL_TRADE_VIEW.replaceFirst("\\?", "1") + ")" + " UNION ALL " +
           "(SELECT 'For Last Week' as Period, IFNULL(SUM(price),0) AS Total, IFNULL(AVG(price),0) as Average, COUNT(*) AS Count FROM " +
           SQL_TRADE_VIEW.replaceFirst("\\?", "7") + ")" + " UNION ALL " +
           "(SELECT 'For Last Month' as Period, IFNULL(SUM(price),0) AS Total, IFNULL(AVG(price),0) as Average, COUNT(*) AS Count FROM " +
           SQL_TRADE_VIEW.replaceFirst("\\?", "30") + ")" + " UNION ALL " +
           "(SELECT 'For Last Quarter' as Period, IFNULL(SUM(price),0) AS Total, IFNULL(AVG(price),0) as Average, COUNT(*) AS Count FROM " +
           SQL_TRADE_VIEW.replaceFirst("\\?", "90") + ")" + " UNION ALL " +
           "(SELECT 'For Last Year' as Period, IFNULL(SUM(price),0) AS Total, IFNULL(AVG(price),0) as Average, COUNT(*) AS Count FROM " +
           SQL_TRADE_VIEW.replaceFirst("\\?", "365") + ")" + "";
   String SQL_TRADE_TOTAL_BY_CATEGORY     =
           "SELECT categoryName, SUM(price) as Total, AVG(price) as Average, COUNT(*) AS Count FROM " + SQL_TRADE_VIEW +
           " Group By categoryName order by Total DESC";
   String SQL_TRADE_TOTAL_BY_SIMILARGROUP =
           "SELECT categoryName, conditionCode, SUM(price) as Total, AVG(price) as Average, COUNT(*) AS Count FROM " + SQL_TRADE_VIEW +
           " Group By categoryName, conditionCode order by Total DESC";
   String SQL_TRADE_TOTAL_BY_BUYER        =
           "SELECT buyer as person, SUM(price) as Total, AVG(price) as Average, COUNT(*) AS Count FROM " + SQL_TRADE_VIEW +
           " Group By buyer order by Total DESC";
   String SQL_TRADE_TOTAL_BY_SELLER       =
           "SELECT seller as person, SUM(price) as Total, AVG(price) as Average, COUNT(*) AS Count FROM " + SQL_TRADE_VIEW +
           " Group By seller order by Total DESC";
   String SQL_TRADE_TOTAL_BY_USER         =
           "SELECT person, SUM(Total) AS Total, SUM(Total)/SUM(Count) AS Average, SUM(Count) AS Count FROM " +
           "((SELECT buyer as person, SUM(price) as Total, AVG(price) as Average, COUNT(*) AS Count FROM " + SQL_TRADE_VIEW +
           " Group By buyer)" + " UNION ALL " +
           "(SELECT seller as person, SUM(price) as Total, AVG(price) as Average, COUNT(*) AS Count FROM " + SQL_TRADE_VIEW +
           " Group By seller))" + " TWO GROUP By person order by Total DESC";
   //
   String SQL_TRADE_BEST_ITEM             =
           "SELECT price, categoryName, conditionCode, description, seller, buyer, tradeDate FROM " + SQL_TRADE_VIEW +
           " ORDER BY price DESC LIMIT 0, ?";
   String SQL_TRADE_RECENT_ITEM           =
           "SELECT price, categoryName, conditionCode, description, seller, buyer, tradeDate FROM " + SQL_TRADE_VIEW +
           " ORDER BY tradeDate DESC LIMIT 0, ?";
   String SQL_TRADE_MY_TRADE              =
           "SELECT price, categoryName, conditionCode, description, seller, buyer, tradeDate FROM " + SQL_TRADE_VIEW +
           " WHERE UPPER(seller) = UPPER(?) OR UPPER(buyer) = UPPER(?) ORDER BY tradeDate DESC LIMIT 0, ?";


   // Alert
   String SQL_ALERT_INSERT_BID = "INSERT INTO Alert (alertID, receiver, offerID, bidID, content, alertDate) VALUES (?, ?, ?, ?, ?, NOW())";
   String SQL_ALERT_SELECT     = "SELECT alertID, receiver, offerID, bidID, content, alertDate FROM Alert WHERE UPPER(receiver) = UPPER(?)";
   String SQL_ALERT_DELETE     = "DELETE FROM Alert WHERE alertID = ?";


   // OfferAlertCriterion
   String SQL_OFFERALERTCRITERION_INSERT         = "INSERT INTO OfferAlertCriterion (criterionID, buyer, criterionName, triggerTxt, " +
                                                   "description, generateDate) VALUES (?, ?, ?, ?, ?, NOW())";
   String SQL_OFFERALERTCRITERION_SELECT_EX_USER = "select criterionID, buyer, criterionName, triggerTxt, description, generateDate FROM " +
                                                   "OfferAlertCriterion Where NOT UPPER(buyer) = UPPER(?)";
   String SQL_OFFERALERTCRITERION_SELECT_USER    = "select criterionID, buyer, criterionName, triggerTxt, description, generateDate FROM " +
                                                   "OfferAlertCriterion Where UPPER(buyer) = UPPER(?)";
   String SQL_OFFERALERTCRITERION_DELETE         = "DELETE from OfferAlertCriterion WHERE criterionID = ?";


   // Question
   String SQL_QUESTION_INSERT             = "INSERT INTO Question (questionID, userID, question, questionDate) VALUES (?, ?, ?, NOW())";
   String SQL_QUESTION_UPDATE_WITH_ANSWER =
           "UPDATE Question SET answer = ?, repID = ?, answerDate = NOW() WHERE questionID = ? AND " + "(answer IS NULL OR answer = '')";
   String SQL_QUESTION_QUERY_OPEN         =
           "SELECT questionID, userID, question, questionDate FROM Question WHERE (answer IS NULL OR " + "answer = '')";
}
