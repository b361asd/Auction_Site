import com.b361asd.auction.db.User;
import com.b361asd.auction.gui.UserType;
import com.b361asd.auction.servlet.IConstant;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    void testDoVerifyLogin() {
        Map<String, Object> map = User.doVerifyLogin("user", "user_pwd");
        System.out.println(IConstant.DATA_NAME_STATUS + "= " + map.get(IConstant.DATA_NAME_STATUS));
        System.out.println(
                IConstant.DATA_NAME_MESSAGE + "= " + map.get(IConstant.DATA_NAME_MESSAGE));
        System.out.println(
                IConstant.DATA_NAME_USER_TYPE + "= " + map.get(IConstant.DATA_NAME_USER_TYPE));
    }

    @Test
    void testDoAddUser() {
        Map<String, Object> map =
                User.doAddUser(
                        "abc",
                        "123",
                        "email",
                        "fN",
                        "lN",
                        "123 St",
                        "Pearl",
                        "NJ",
                        "01010",
                        "39239033",
                        UserType.USER.getDatabaseUserType());
        System.out.println(IConstant.DATA_NAME_STATUS + "= " + map.get(IConstant.DATA_NAME_STATUS));
        System.out.println(
                IConstant.DATA_NAME_MESSAGE + "= " + map.get(IConstant.DATA_NAME_MESSAGE));
        System.out.println(
                IConstant.DATA_NAME_USER_TYPE + "= " + map.get(IConstant.DATA_NAME_USER_TYPE));
    }

    @Test
    void testModifyUser() {
        Map<String, String[]> parameters = new HashMap<>();
        parameters.put("username", new String[] {"user"});
        parameters.put("password", new String[] {"user_pwd"});
        parameters.put("email", new String[] {"user@buyme.com"});
        parameters.put("firstname", new String[] {"Real"});
        parameters.put("lastname", new String[] {"Lnuser"});
        parameters.put("address", new String[] {"123 Main St., Nowhere Town, NJ 56789"});
        parameters.put("phone", new String[] {"2365678909"});
        Map<String, Object> map = User.modifyUser(parameters, UserType.USER.getDatabaseUserType());
        System.out.println(IConstant.DATA_NAME_STATUS + "= " + map.get(IConstant.DATA_NAME_STATUS));
        System.out.println(
                IConstant.DATA_NAME_MESSAGE + "= " + map.get(IConstant.DATA_NAME_MESSAGE));
        System.out.println(
                IConstant.DATA_NAME_USER_TYPE + "= " + map.get(IConstant.DATA_NAME_USER_TYPE));
    }
}
