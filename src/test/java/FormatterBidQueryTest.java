import com.b361asd.auction.db.FormatterBidQuery;
import org.junit.jupiter.api.Test;

public class FormatterBidQueryTest {

    @Test
    void testBuildQueryUserActivity() {
        StringBuilder sb = FormatterBidQuery.buildQueryUserActivity("user");
        System.out.println(sb);
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
