package rutgers.cs336.db;

public interface ISQLConstant {
	// MySQL JDBC Connector URL
	//init("jdbc:mysql://localhost:3306/BuyMe", "cs336", "cs336_password");
	String MySQL_URL      = "jdbc:mysql://cs336-buyme.cf7jfkoilx7f.us-east-1.rds.amazonaws.com:3306/cs336buyme";
	String MySQL_USER_ID  = "cs336";
	String MySQL_PASSWORD = "cs336_password";

	// User
	String SQL_USER_INSERT = "insert User (username, password, email, firstname, lastname, address, phone, active, usertype) VALUES (?, ?, ?, ?, ?, ?, ?, true, ?)";
	//
	String SQL_USER_SELECT = "select password, firstname, lastname, active, usertype from User where username=?";

	// Bid
	String SQL_BID_INSERT = "insert Bid (bidId, offerId, buyer, price, isAutoRebid, autoRebidLimit, date) VALUES (?, ?, ?, ?, ?, ?, NOW())";

	// CategoryField
	String SQL_CATEGORYFIELD_SELECT = "select categoryName, CategoryField.fieldID, fieldName, fieldType from CategoryField inner join Field on CategoryField.fieldID = Field.fieldID order by categoryName, CategoryField.fieldID";

	// Offer
	String SQL_OFFER_INSERT = "insert Offer (offerId, categoryName, seller, initPrice, increment, minPrice, conditionCode, description, startDate, endDate, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW(), DATE_ADD(NOW(), INTERVAL + ? DAY), 1)";

	String SQL_OFFER_SELECT = "select categoryName, seller, min_price, description, startDate, endDate, status from Offer where offerId = ?";

	String SQL_OFFER_SEARCH = "select offerId, categoryName, seller, min_price, description, startDate, endDate from Offer where status = 1 and categoryName = ? and description like ?";


	// OfferField
	String SQL_OFFERFIELD_INSERT = "insert OfferField (offerId, fieldID, fieldText) VALUES (?, ?, ?)";

	String SQL_OFFERFIELD_SELECT = "select OfferField.fieldID, fieldName, fieldType, fieldText from OfferField inner join Field on OfferField.fieldID = Field.fieldID where OfferField.offerId = ? order by OfferField.fieldID";

	String SQL_OFFERFIELD_SEARCH = "select OfferField.offerId, OfferField.fieldID, fieldName, fieldType, fieldText from OfferField inner join Field on OfferField.fieldID = Field.fieldID where OfferField.offerId in (select offerId from Offer where status = 1 and categoryName = ? and description like ?)";


	// Browse
//	String BROWSE_OPEN_OFFER (including current bid, sorted by different criteria)
//	String SEARCH_BY_CRITERIA
//	String SHOW_BID_HISTORY (just for one)
//	String OFFER_BID_BY_USER
//	String SIMILAR_ITEM


}

/*
Project Checklist:

I. Create accounts of users; login, logout

II. Auctions
	[] Seller creates auctions and posts items for sale
		[] Set all the characteristics of the item
		[] Set closing date and time
		[] Set a hidden minimum price (reserve)
	[] A buyer should be able to bid
		[] Manually
			[] Let the buyer set a new bid
		[] [] With automatic bidding
			[] Set a secret upper limit
			[] Put in a higher bid automatically for the user in case someone bids higher
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
		[] View the list of “similar” items on auctions in the preceding month (and auction information about them)


BROWSE_OPEN_OFFER (including current bid, sorted by different criteria)
SEARCH_BY_CRITERIA
SHOW_BID_HISTORY (just for one)
OFFER_BID_BY_USER
SIMILAR_ITEM



IV. Alerts and messaging functions
	[] Alert the buyer that a higher bid has been placed
	[] Alert the buyer in case someone bids more than your upper limit (for automatic bidding)
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