import com.b361asd.auction.db.DBBase;
import com.b361asd.auction.db.FormatterOfferQuery;
import org.junit.jupiter.api.Test;

public class FormatterOfferQueryTest {

    @Test
    void testBuildSQLUserActivityOffer() {
        System.out.println(FormatterOfferQuery.buildSQLUserActivityOffer("user"));
    }

    @Test
    void testInsertOne() {
        StringBuilder sb = FormatterOfferQuery.initQuerySearch();
        // addCondition(sb, "o.offerID", OP_SZ_START_WITH, "Scratcges", null);
        // addCondition(sb, "o.seller", OP_SZ_NOT_EQUAL, "us'er", null);
        FormatterOfferQuery.addCondition(
                sb,
                "o.categoryName",
                DBBase.OP_SZ_EQUAL_MULTI_NO_ESCAPE,
                "'car','motorbike'",
                null);
        // addCondition(sb, "o.conditionCode", OP_INT_EQUAL_MULTI, "1,3,4,5,6", null);
        // addCondition(sb, "o.description", OP_SZ_CONTAIN, "Scratcges", null);
        // addCondition(sb, "o.initPrice", OP_INT_EQUAL_OR_OVER, "2", null);
        // addCondition(sb, "o.increment", OP_INT_EQUAL_OR_OVER, "4", null);
        // addCondition(sb, "o.minPrice", OP_ANY, "5", null);
        // addCondition(sb, "o.status", OP_INT_EQUAL_OR_OVER, "5", null);
        // addCondition(sb, "o.price", OP_INT_EQUAL_OR_OVER, "5", null);
        FormatterOfferQuery.initFieldCondition(sb);

        // addFieldCondition(sb, "1", OP_INT_BETWEEN, "23", "56");
        // addFieldCondition(sb, "2", OP_BOOL_TRUE, "toyota", "");
        // addFieldCondition(sb, "3", OP_BOOL_FALSE, "400", "");
        // addFieldCondition(sb, "4", OP_INT_EQUAL_OR_UNDER, "400", "");
        // addFieldCondition(sb, "5", OP_INT_NOT_EQUAL, "400", "");
        // addFieldCondition(sb, "6", OP_INT_NOT_EQUAL, "400", "");
        // addFieldCondition(sb, "7", OP_INT_NOT_EQUAL, "400", "");
        // addFieldCondition(sb, "8", OP_INT_NOT_EQUAL, "400", "");
        FormatterOfferQuery.doneFieldCondition(sb);

        System.out.println(sb);
    }

    @Test
    void testInsertTwo() {
        StringBuilder sb = FormatterOfferQuery.initQuerySearch();
        // addCondition(sb, "o.offerID", OP_SZ_START_WITH, "Scratcges", null);
        // addCondition(sb, "o.seller", OP_SZ_NOT_EQUAL, "us'er", null);
        FormatterOfferQuery.addCondition(
                sb,
                "o.categoryName",
                DBBase.OP_SZ_EQUAL_MULTI_NO_ESCAPE,
                "'car','motorbike'",
                null);
        // addCondition(sb, "o.conditionCode", OP_INT_EQUAL_MULTI, "1,3,4,5,6", null);
        // addCondition(sb, "o.description", OP_SZ_CONTAIN, "Scratcges", null);
        // addCondition(sb, "o.initPrice", OP_INT_EQUAL_OR_OVER, "2", null);
        // addCondition(sb, "o.increment", OP_INT_EQUAL_OR_OVER, "4", null);
        // addCondition(sb, "o.minPrice", OP_ANY, "5", null);
        // addCondition(sb, "o.status", OP_INT_EQUAL_OR_OVER, "5", null);
        // addCondition(sb, "o.price", OP_INT_EQUAL_OR_OVER, "5", null);
        FormatterOfferQuery.initFieldCondition(sb);

        // addFieldCondition(sb, "1", OP_INT_BETWEEN, "23", "56");
        // addFieldCondition(sb, "2", OP_BOOL_TRUE, "toyota", "");
        // addFieldCondition(sb, "3", OP_BOOL_FALSE, "400", "");
        // addFieldCondition(sb, "4", OP_INT_EQUAL_OR_UNDER, "400", "");
        // addFieldCondition(sb, "5", OP_INT_NOT_EQUAL, "400", "");
        // addFieldCondition(sb, "6", OP_INT_NOT_EQUAL, "400", "");
        // addFieldCondition(sb, "7", OP_INT_NOT_EQUAL, "400", "");
        // addFieldCondition(sb, "8", OP_INT_NOT_EQUAL, "400", "");
        FormatterOfferQuery.doneFieldCondition(sb);

        System.out.println(sb);
    }
}
