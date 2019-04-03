package rutgers.cs336.db;

public class FormatterBidQuery extends DBBase {

	public static StringBuilder initQuerySearch() {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT bidID, o.offerID, buyer, price, autoRebidLimit, bidDate FROM Bid b INNER JOIN Offer o ON b.offerID = o.offerID and o.status = 1");
		//
		return sb;
	}
}

/*
SELECT bidID, o.offerID, buyer, price, autoRebidLimit, bidDate FROM Bid b INNER JOIN Offer o ON b.offerID = o.offerID and o.status = 1 
AND (buyer = 'user')
AND (bidID = 'ABC')
AND (o.offerID = 'OK')
*/