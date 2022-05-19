import com.b361asd.auction.db.DBBase;
import com.b361asd.auction.db.FormatterQuestionQuery;
import org.junit.jupiter.api.Test;

class FormatterQuestionQueryTest {

    @Test
    void testFormatQuestionQuery() {
        StringBuilder sb = FormatterQuestionQuery.initQuerySearch();
        FormatterQuestionQuery.addContainTagsCondition2Cols(sb, "question", "answer", "o");
        FormatterQuestionQuery.addDatetimeConditionLookBack(sb, "questionDate", 7);
        FormatterQuestionQuery.addCondition(
                sb,
                "o.categoryName",
                DBBase.OP_SZ_EQUAL_MULTI_NO_ESCAPE,
                "'car','motorbike'",
                null);
        System.out.println(sb);
    }
}
