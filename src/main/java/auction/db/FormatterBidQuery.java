package auction.db;

/**
 * Format SQL querying the Bid table
 */
public class FormatterBidQuery extends DBBase {

   public static StringBuilder initQuerySearchAll() {
      StringBuilder sb = new StringBuilder();
      sb.append("SELECT bidID, b.offerID, buyer, price, autoRebidLimit, bidDate FROM Bid b INNER JOIN Offer o ON b.offerID = o.offerID");
      //
      return sb;
   }

   public static StringBuilder buildQueryUserActivity(String userID) {
      StringBuilder sb = new StringBuilder();
      sb.append("SELECT bidID, b.offerID, buyer, price, autoRebidLimit, bidDate FROM Bid b WHERE (b.offerID IN (SELECT distinct offerID ")
        .append("FROM Offer o1 where (TRUE");
      addCondition(sb, "o1.seller", OP_SZ_EQUAL, userID, null);
      sb.append(") OR EXISTS (SELECT * from Bid b1 where b1.offerID = o1.offerID");
      addCondition(sb, "b1.buyer", OP_SZ_EQUAL, userID, null);
      sb.append(")))");
      //
      return sb;
   }

   public static void main(String[] args) {
      StringBuilder sb = buildQueryUserActivity("user");
      //
      System.out.println(sb.toString());
   }
}

/*
Search
SELECT bidID, b.offerID, buyer, price, autoRebidLimit, bidDate FROM Bid b INNER JOIN Offer o ON b.offerID = o.offerID and o.status = 1 
AND (buyer = 'user')
AND (bidID = 'ABC')
AND (o.offerID = 'OK')

Activity
SELECT bidID, b.offerID, buyer, price, autoRebidLimit, bidDate FROM Bid b WHERE (b.offerID IN (SELECT distinct offerID FROM Offer o1
where (TRUE AND (o1.seller = 'user')) OR EXISTS (SELECT * from Bid b1 where b1.offerID = o1.offerID and (b1.buyer = 'user'))))
*/