import com.b361asd.auction.db.CategoryAndField;
import com.b361asd.auction.servlet.IConstant;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class CategoryAndFieldTest {

    @Test
    void testGetCategoryField() {
        System.out.println("Start");
        Map<String, Object> map = CategoryAndField.getCategoryField(null);
        System.out.println(IConstant.DATA_NAME_STATUS + "= " + map.get(IConstant.DATA_NAME_STATUS));
        System.out.println(
                IConstant.DATA_NAME_MESSAGE + "= " + map.get(IConstant.DATA_NAME_MESSAGE));
        System.out.println(
                IConstant.DATA_NAME_USER_TYPE + "= " + map.get(IConstant.DATA_NAME_USER_TYPE));
    }

    @Test
    void testGetMapFieldIDToText() {
        System.out.println("Start");
        Map map = CategoryAndField.getMapFieldIDToText();
        System.out.println(IConstant.DATA_NAME_STATUS + "= " + map.get(IConstant.DATA_NAME_STATUS));
        System.out.println(
                IConstant.DATA_NAME_MESSAGE + "= " + map.get(IConstant.DATA_NAME_MESSAGE));
        System.out.println(
                IConstant.DATA_NAME_USER_TYPE + "= " + map.get(IConstant.DATA_NAME_USER_TYPE));
    }
}
