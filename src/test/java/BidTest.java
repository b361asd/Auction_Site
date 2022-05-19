import com.b361asd.auction.db.Bid;
import com.b361asd.auction.db.ISQLConstant;
import com.b361asd.auction.servlet.IConstant;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class BidTest {

    @Test
    void testMinForModifyBid() {
        System.out.println(ISQLConstant.SQL_TRADE_TOTAL);
        Map<String, String[]> parameters = new HashMap<>();
        parameters.put(
                "bidIDofferIDBuyer",
                new String[] {
                    "b2d4ba68a1d84841aebc0434988d7261,1ccf77b4a6cc46a5b9a471db65ae4618,test1"
                });
        BigDecimal output = Bid.getMinForModifyBid(parameters);
        System.out.println(output);
    }

    @Test
    void testSearchBidOne() {
        System.out.println(ISQLConstant.SQL_TRADE_TOTAL);
        Map<String, String[]> parameters = new HashMap<>();
        parameters.put(
                "offeridcategoryname", new String[] {"9dee3107cdf444a7b4f0cd79524cfe53,car"});
        Map<String, Object> map = Bid.searchBid(parameters, null, null);
        System.out.println(IConstant.DATA_NAME_STATUS + "= " + map.get(IConstant.DATA_NAME_STATUS));
        System.out.println(
                IConstant.DATA_NAME_MESSAGE + "= " + map.get(IConstant.DATA_NAME_MESSAGE));
        System.out.println(
                IConstant.DATA_NAME_USER_TYPE + "= " + map.get(IConstant.DATA_NAME_USER_TYPE));
    }

    @Test
    void testSearchBidTwo() {
        System.out.println("Start");
        Map<String, Object> map = Bid.searchBid(null, "user", null);
        System.out.println(IConstant.DATA_NAME_STATUS + "= " + map.get(IConstant.DATA_NAME_STATUS));
        System.out.println(
                IConstant.DATA_NAME_MESSAGE + "= " + map.get(IConstant.DATA_NAME_MESSAGE));
        System.out.println(
                IConstant.DATA_NAME_USER_TYPE + "= " + map.get(IConstant.DATA_NAME_USER_TYPE));
    }

    @Test
    void testSearchBidThree() {
        Map<String, String[]> parameters = new HashMap<>();
        parameters.put("action", new String[] {"repSearchBid"});
        parameters.put("buyerOP", new String[] {"any"});
        parameters.put("buyerVal", new String[] {""});
        Map<String, Object> map = Bid.searchBid(parameters, null, null);
        System.out.println(IConstant.DATA_NAME_STATUS + "= " + map.get(IConstant.DATA_NAME_STATUS));
        System.out.println(
                IConstant.DATA_NAME_MESSAGE + "= " + map.get(IConstant.DATA_NAME_MESSAGE));
        System.out.println(
                IConstant.DATA_NAME_USER_TYPE + "= " + map.get(IConstant.DATA_NAME_USER_TYPE));
    }

    @Test
    void testDoCreateOrModifyBidOne() {
        Map<String, String[]> parameters = new HashMap<>();
        parameters.put(
                "bidIDofferIDBuyer",
                new String[] {
                    "02b064d413c044c5bafb8155bf525b3d,721fef17f8d84e30b7b852ae62df0e19,user"
                });
        parameters.put("price", new String[] {"1000"});
        parameters.put("autoRebidLimit", new String[] {"10"});
        Map<String, Object> map = Bid.doCreateOrModifyBid(null, parameters, false);
        System.out.println(IConstant.DATA_NAME_STATUS + "= " + map.get(IConstant.DATA_NAME_STATUS));
        System.out.println(
                IConstant.DATA_NAME_MESSAGE + "= " + map.get(IConstant.DATA_NAME_MESSAGE));
        System.out.println(
                IConstant.DATA_NAME_USER_TYPE + "= " + map.get(IConstant.DATA_NAME_USER_TYPE));
    }

    @Test
    void testDoCreateOrModifyBidTwo() {
        Map<String, String[]> parameters = new HashMap<>();
        parameters.put("offerId", new String[] {"d6ac071c449c46b4812dd96b9bc8f197"});
        parameters.put("price", new String[] {"550"});
        parameters.put("autoRebidLimit", new String[] {"1200"});
        Map<String, Object> map = Bid.doCreateOrModifyBid("user", parameters, true);
        System.out.println(IConstant.DATA_NAME_STATUS + "= " + map.get(IConstant.DATA_NAME_STATUS));
        System.out.println(
                IConstant.DATA_NAME_MESSAGE + "= " + map.get(IConstant.DATA_NAME_MESSAGE));
        System.out.println(
                IConstant.DATA_NAME_USER_TYPE + "= " + map.get(IConstant.DATA_NAME_USER_TYPE));
    }
}
