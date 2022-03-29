import com.b361asd.auction.db.Alert;
import com.b361asd.auction.servlet.IConstant;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class AlertTest {

    @Test
    void insertAlert() {
        Map<String, String[]> parameters = new HashMap<>();
        parameters.put("username", new String[] {"user"});
        parameters.put("password", new String[] {"user_pwd"});
        parameters.put("email", new String[] {"user@buyme.com"});
        parameters.put("firstname", new String[] {"Real"});
        parameters.put("lastname", new String[] {"Lnuser"});
        parameters.put("address", new String[] {"123 Main St., Nowhere Town, NJ 56789"});
        parameters.put("phone", new String[] {"2365678909"});

        Map map = Alert.selectAlert("user");
        System.out.println(IConstant.DATA_NAME_STATUS + "= " + map.get(IConstant.DATA_NAME_STATUS));
        System.out.println(
                IConstant.DATA_NAME_MESSAGE + "= " + map.get(IConstant.DATA_NAME_MESSAGE));
        System.out.println(
                IConstant.DATA_NAME_USER_TYPE + "= " + map.get(IConstant.DATA_NAME_USER_TYPE));
    }
}
