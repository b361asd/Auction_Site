package rutgers.cs336.db;

public class FormatterBidQuery extends DBBase {

	public static StringBuilder initQuerySearch() {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT t1.*, t2.currPrice FROM (SELECT b1.bidID, b1.buyer, b1.price, b1.autoRebidLimit, b1.bidDate, o1.offerID, o1.seller, o1.categoryName, o1.conditionCode, o1.description, o1.initPrice, o1.increment, o1.minPrice, o1.startDate, o1.endDate, o1.status FROM Bid b1 INNER JOIN Offer o1 ON b1.offerID = o1.offerID");
		//
		return sb;
	}


	public static StringBuilder doneQuerySearch(StringBuilder sb) {
		return sb.append(" ) t1 LEFT OUTER JOIN (SELECT b1.price as currPrice, b1.offerID FROM Bid b1 WHERE b1.price = (SELECT MAX(price) FROM Bid b where b.offerID = b1.offerID)) t2 ON t1.offerID = t2.offerID order by bidDate");
	}

}

/*
SELECT t1.*, t2.currPrice FROM (SELECT b1.bidID, b1.buyer, b1.price, b1.autoRebidLimit, b1.bidDate, o1.offerID, o1.seller, o1.categoryName, o1.conditionCode, o1.description, o1.initPrice, o1.increment, o1.minPrice, o1.startDate, o1.endDate, o1.status FROM Bid b1 INNER JOIN Offer o1 ON b1.offerID = o1.offerID
AND (buyer = 'user')
AND (seller = 'user')
) t1 LEFT OUTER JOIN (SELECT b1.price as currPrice, b1.offerID FROM Bid b1 WHERE b1.price = (SELECT MAX(price) FROM Bid b where b.offerID = b1.offerID)) t2 ON t1.offerID = t2.offerID order by bidDate
*/