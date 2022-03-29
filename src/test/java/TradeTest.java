import com.b361asd.auction.db.Trade;
import com.b361asd.auction.servlet.IConstant;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class TradeTest {

    @Test
    void testSelectBestSellingMostRecentItems() {
        Map<String, String[]> parameters = new HashMap<>();
        parameters.put("bidID", new String[] {"11fe20aabc7a4025928e9522544be2e3"});
        Map map = Trade.selectBestSellingMostRecentItems(30, 10, true);
        System.out.println(IConstant.DATA_NAME_STATUS + "= " + map.get(IConstant.DATA_NAME_STATUS));
        System.out.println(
                IConstant.DATA_NAME_MESSAGE + "= " + map.get(IConstant.DATA_NAME_MESSAGE));
        System.out.println(
                IConstant.DATA_NAME_USER_TYPE + "= " + map.get(IConstant.DATA_NAME_USER_TYPE));
    }
}
