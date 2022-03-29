import com.b361asd.auction.db.Offer;
import com.b361asd.auction.servlet.IConstant;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class OfferTest {

    @Test
    void testDoCreateOrModifyOffer() {
        Map<String, String[]> parameters = new HashMap<>();
        parameters.put("categoryName", new String[] {"car"});
        parameters.put("initPrice", new String[] {"2000"});
        parameters.put("increment", new String[] {"100"});
        parameters.put("minPrice", new String[] {""});
        parameters.put("conditionCode", new String[] {"1"});
        parameters.put("description", new String[] {"go"});
        parameters.put("fieldID_1", new String[] {""});
        parameters.put("fieldID_2", new String[] {""});
        parameters.put("fieldID_3", new String[] {""});
        parameters.put("fieldID_4", new String[] {""});
        parameters.put("fieldID_5", new String[] {""});
        parameters.put("fieldID_6", new String[] {"yes"});
        parameters.put("fieldID_7", new String[] {""});
        parameters.put("endDate", new String[] {"2019-04-20T13:10:48"});

        Map map = Offer.doCreateOrModifyOffer("user", parameters, true);
        System.out.println(Offer.DATA_NAME_STATUS + "= " + map.get(Offer.DATA_NAME_STATUS));
        System.out.println(Offer.DATA_NAME_MESSAGE + "= " + map.get(IConstant.DATA_NAME_MESSAGE));
        System.out.println(
                Offer.DATA_NAME_USER_TYPE + "= " + map.get(IConstant.DATA_NAME_USER_TYPE));
    }

    @Test
    void testDoSearchUserActivity() {
        System.out.println("Start");
        Offer.doSearchUserActivity("user");
    }

    @Test
    void testDoCreateAlerts() {
        System.out.println("Start");
        Offer.doCreateAlerts("user1", "99936702ff2a428ba913dd02e5592fc4");
    }

    @Test
    void testDoCancelOffer() {
        Map<String, String[]> parameters = new HashMap<>();
        parameters.put("categoryName2", new String[] {"motorbike"});
        parameters.put("categoryName3", new String[] {"truck"});
        System.out.println(
                Offer.DATA_NAME_STATUS
                        + "= "
                        + Offer.getListOfStringsFromParamMap("categoryName", 1, parameters, "'"));
        System.out.println(
                Offer.DATA_NAME_STATUS
                        + "= "
                        + Offer.getListOfStringsFromParamMap("categoryName", 1, parameters, ""));

        Map map = Offer.doCancelOffer(parameters);
        System.out.println(Offer.DATA_NAME_STATUS + "= " + map.get(Offer.DATA_NAME_STATUS));
        System.out.println(
                IConstant.DATA_NAME_MESSAGE + "= " + map.get(IConstant.DATA_NAME_MESSAGE));
        System.out.println(Offer.DATA_NAME_USER_TYPE + "= " + map.get(Offer.DATA_NAME_USER_TYPE));
    }

    @Test
    void testDoSearchOffer() {
        Map<String, String[]> parameters = new HashMap<>();
        parameters.put("categoryName1", new String[] {"car"});
        parameters.put("fieldop_6", new String[] {"yes"});
        parameters.put("lstFieldIDs", new String[] {"1,2,3,4,5,6,7"});

        Map map = Offer.doSearchOffer(parameters, true);
        System.out.println(Offer.DATA_NAME_STATUS + "= " + map.get(Offer.DATA_NAME_STATUS));
        System.out.println(
                IConstant.DATA_NAME_MESSAGE + "= " + map.get(IConstant.DATA_NAME_MESSAGE));
        System.out.println(Offer.DATA_NAME_USER_TYPE + "= " + map.get(Offer.DATA_NAME_USER_TYPE));
    }
}
