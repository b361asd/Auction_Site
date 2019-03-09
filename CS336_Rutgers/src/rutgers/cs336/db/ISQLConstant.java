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

}
