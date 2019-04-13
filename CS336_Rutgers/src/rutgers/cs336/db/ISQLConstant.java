package rutgers.cs336.db;

public interface ISQLConstant {
	// MySQL JDBC Connector URL
	//init("jdbc:mysql://localhost:3306/BuyMe", "cs336", "cs336_password");
	String MySQL_URL      = "jdbc:mysql://cs336-buyme.cf7jfkoilx7f.us-east-1.rds.amazonaws.com:3306/cs336buyme";
	String MySQL_USER_ID  = "cs336";
	String MySQL_PASSWORD = "cs336_password";


	// User
	String SQL_USER_INSERT       = "INSERT INTO User (username, password, email, firstname, lastname, address, phone, active, userType) SELECT ?, ?, ?, ?, ?, ?, ?, true, ? FROM DUAL WHERE NOT EXISTS (SELECT * FROM User u WHERE UPPER(u.username) = UPPER(?))";
	String SQL_USER_AUTH         = "SELECT password, firstname, lastname, active, userType FROM User WHERE username = ?";
	String SQL_USER_SELECT       = "SELECT username, password, email, firstname, lastname, address, phone, active FROM User WHERE userType = ?";
	String SQL_USER_SELECT_ONE   = "SELECT username, password, email, firstname, lastname, address, phone, active FROM User WHERE username = ? AND userType = ?";
	String sQL_USER_SELECTUSERID = "SELECT Distinct username FROM User WHERE userType = 3 order by username";
	String SQL_USER_ACTIVE       = "UPDATE User SET active = true where username = ?";
	String SQL_USER_DEACTIVE     = "UPDATE User SET active = false where username = ?";
	String SQL_USER_UPDATE       = "UPDATE User SET password = ?, email = ?, firstname = ?, lastname = ?, address = ?, phone = ? WHERE username = ? AND userType = ?";


	// Bid
	String SQL_BID_INSERT              = "INSERT Bid (bidID, offerID, buyer, price, autoRebidLimit, bidDate) VALUES (?, ?, ?, ?, ?, NOW())";
	String SQL_BID_UPDATE              = "UPDATE Bid SET price = ?, autoRebidLimit = ?, bidDate = NOW() WHERE bidID = ?";
	//String SQL_BID_INSERT    			= "INSERT Bid (bidID, offerID, buyer, price, autoRebidLimit, bidDate) SELECT ?, o.offerID, ?, ?, ?, NOW() FROM Offer o WHERE o.endDate >= NOW() AND (NOT o.minPrice > ?) AND o.offerID = ? AND o.status = 1 AND ((NOT EXISTS (SELECT * FROM Bid b3 WHERE b3.offerID = ?)) OR (? > (SELECT MAX(b2.price) FROM Bid b2 WHERE b2.offerID = ?)))";
	String SQL_BID_DELETE              = "DELETE from Bid where bidID = ?";
	String SQL_BID_SELECT_EX           = "SELECT bidID, offerID, buyer, price, autoRebidLimit, bidDate FROM Bid";
	String SQL_BID_SELECT_BY_OFFERID   = "SELECT bidID, buyer, price, bidDate FROM Bid b WHERE b.offerID = ? ORDER BY price DESC";
	String SQL_BID_SELECT_MAX_PRICE    = "SELECT o.offerID, seller, categoryName, conditionCode, description, initPrice, increment, minPrice, startDate, endDate, status, bidID, buyer, price, autoRebidLimit, bidDate FROM (select * from Offer where offerID = ?) o LEFT OUTER JOIN Bid b ON o.offerID = b.offerID AND b.price = (SELECT MAX(b2.price) FROM Bid b2 WHERE b2.offerID = o.offerID)";
	String SQL_BID_SELECT_MAX_PRICE_EX = "SELECT o.offerID, seller, categoryName, conditionCode, description, initPrice, increment, minPrice, startDate, endDate, status, bidID, buyer, price, autoRebidLimit, bidDate FROM (select * from Offer where offerID = ?) o LEFT OUTER JOIN Bid b ON o.offerID = b.offerID AND b.bidID <> ? AND b.price = (SELECT MAX(b2.price) FROM Bid b2 WHERE b2.offerID = o.offerID)";


	// CategoryField
	String SQL_CATEGORYFIELD_SELECT = "SELECT categoryName, CategoryField.fieldID, fieldName, fieldType FROM CategoryField INNER JOIN Field ON CategoryField.fieldID = Field.fieldID ORDER BY categoryName, CategoryField.fieldID";


	// Offer
	String SQL_OFFER_INSERT = "INSERT INTO Offer (offerID, categoryName, seller, initPrice, increment, minPrice, conditionCode, description, startDate, endDate, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW(), STR_TO_DATE(?,'%Y-%m-%dT%H:%i:%s'), 1)";
	String SQL_OFFER_MODIFY = "UPDATE Offer SET minPrice = ?, conditionCode = ?, description = ? WHERE offerID = ? AND status = 1";
	String SQL_OFFER_CANCEL = "DELETE FROM Offer WHERE offerID = ? AND status = 1";
	String SQL_OFFER_SELECT = "SELECT categoryName, seller, min_price, description, startDate, endDate, status FROM Offer WHERE offerID = ?";
	String SQL_OFFER_SEARCH = "SELECT offerID, categoryName, seller, min_price, description, startDate, endDate FROM Offer WHERE status = 1 and categoryName = ? and description LIKE ?";


	// OfferField
	String SQL_OFFERFIELD_INSERT = "INSERT INTO OfferField (offerID, fieldID, fieldText) VALUES (?, ?, ?)";
	String SQL_OFFERFIELD_DELETE = "DELETE from OfferField WHERE offerID = ?";
	String SQL_OFFERFIELD_SELECT = "SELECT OfferField.fieldID, fieldName, fieldType, fieldText FROM OfferField INNER JOIN Field ON OfferField.fieldID = Field.fieldID WHERE OfferField.offerID = ? ORDER BY OfferField.fieldID";
	String SQL_OFFERFIELD_SEARCH = "SELECT OfferField.offerID, OfferField.fieldID, fieldName, fieldType, fieldText FROM OfferField INNER JOIN Field ON OfferField.fieldID = Field.fieldID WHERE OfferField.offerID IN (SELECT offerID FROM Offer WHERE status = 1 and categoryName = ? and description LIKE ?)";


	// Trade
	String SQL_TRADE_VIEW          = "(SELECT t.tradeID, o.offerID, b.bidID, tradeDate, seller, categoryName, conditionCode, description, status, buyer, price from Trade t, Offer o, Bid b WHERE t.offerID = o.offerID and t.bidID = b.bidID AND tradeDate > DATE_SUB(NOW(), INTERVAL ? DAY)) tob";
	String SQL_TRADE_TOTAL_BY_USER = "SELECT buyer, SUM(price) as Total, AVG(price) as Average, COUNT(*) AS Count FROM " + SQL_TRADE_VIEW + " Group By buyer order by Total DESC";

	// Browse
	//	String BROWSE_OPEN_OFFER (including current bid, sorted by different criteria)
	//	String SEARCH_BY_CRITERIA
	//	String SHOW_BID_HISTORY (just for one)
	//	String OFFER_BID_BY_USER
	//	String SIMILAR_ITEM

	// Alert
	String SQL_ALERT_INSERT_BID = "INSERT INTO Alert (alertID, receiver, offerID, bidID, content, alertDate) VALUES (?, ?, ?, ?, ?, NOW())";
	String SQL_ALERT_SELECT     = "SELECT alertID, receiver, offerID, bidID, content, alertDate FROM Alert WHERE receiver = ?";
	String SQL_ALERT_DELETE     = "DELETE FROM Alert WHERE alertID = ?";


	// OfferAlertCriterion
	String SQL_OFFERALERTCRITERION_INSERT      = "INSERT INTO OfferAlertCriterion (criterionID, buyer, criterionName, triggerTxt, generateDate) VALUES (?, ?, ?, ?, NOW())";
	String SQL_OFFERALERTCRITERION_SELECT      = "select criterionID, buyer, criterionName, triggerTxt, generateDate FROM OfferAlertCriterion";
	String SQL_OFFERALERTCRITERION_SELECT_USER = "select criterionID, buyer, criterionName, triggerTxt, generateDate FROM OfferAlertCriterion Where buyer = ?";
	String SQL_OFFERALERTCRITERION_DELETE      = "DELETE from OfferAlertCriterion WHERE criterionID = ?";


	// Question
	String SQL_QUESTION_INSERT             = "INSERT INTO Question (questionID, userID, question, questionDate) VALUES (?, ?, ?, NOW())";
	String SQL_QUESTION_UPDATE_WITH_ANSWER = "UPDATE Question SET answer = ?, repID = ?, answerDate = NOW() WHERE questionID = ? AND (answer IS NULL OR answer = '')";
	String SQL_QUESTION_QUERY_OPEN         = "SELECT questionID, userID, question, questionDate FROM Question WHERE (answer IS NULL OR answer = '')";
}

/*
Project Checklist:

I. Create accounts of users; login, logout

II. Auctions
	[] Seller creates auctions and posts items for sale
		[] Set all the characteristics of the item
		[] Set closing date and time
		[*] Set a hidden minimum price (reserve)
	[] A buyer should be able to bid
		[] Manually
			[] Let the buyer set a new bid
		[*] With automatic bidding
			[*] Set a secret upper limit
			[*] Put in a higher bid automatically for the user in case someone bids higher
	[] Define the winner of the auction
		[] When the closing time has come, check if the seller has set a reserve
			[] If yes: if the reserve is higher than the last bid none is the winner.
			[] If no: whoever has the higher bid is the winner

III. Browsing and advanced search functionality
	[] Let people browse on the items and see the status of the current bidding
	[] Sort by different criteria (by type, bidding price, etc.)
	[] Search the list of items by various criteria.
	[] An user should be able to:
		[] View all the history of bids for any specific auction
		[] View the list of all auctions a specific buyer or seller has participated in
		[] View the list of "similar" items on auctions in the preceding month (and auction information about them)


BROWSE_OPEN_OFFER (including current bid, sorted by different criteria)
SEARCH_BY_CRITERIA
SHOW_BID_HISTORY (just for one)
OFFER_BID_BY_USER
SIMILAR_ITEM



IV. Alerts and messaging functions
	[] Alert the buyer that a higher bid has been placed
	[*] Alert the buyer in case someone bids more than your upper limit (for automatic bidding)
	[] Let user set an alert for specific items s/he is interested
		[] Get an alert when the item becomes available
	[] User can post questions
	[] User can search and browse questions/answers

V. Customer representatives & admin functions
	[] Admin (create an admin account ahead of time)
		[] Creates accounts for customer representatives
		[] Generates sales reports for:
			[] Total earnings
			[] Earnings per:
				[] Item
				[] Item type
				[] End-user
			[] Best-selling items
			[] Best buyers
	[] Customer representative:
		[] Answers to questions of users
		[] Edits account information, bids and auctions
		[] Removes bids
		[] Removes auctions
(Everywhere above where there is a function for creating something, one should/could have ones for modifying and deleting it; these are not listed, in order to make the description shorter. But this is an opportunity for "staged development": enhance your system (and grade :) as time allows at the end by adding these features.)
 */