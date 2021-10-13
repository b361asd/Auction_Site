package com.b361asd.auction.db;

/** Format SQL querying the {@link Question} table */
public class FormatterQuestionQuery extends DBBase {

    public static StringBuilder initQuerySearch() {
        StringBuilder sb = new StringBuilder();
        sb.append(
                        "SELECT questionID, userID, question, answer, repID, questionDate, answerDate FROM Question WHERE (NOT (answer IS NULL OR ")
                .append("answer = ''))");
        return sb;
    }

    public static void main(String[] args) {
        StringBuilder sb = initQuerySearch();
        addContainTagsCondition2Cols(sb, "question", "answer", "o");
        addDatetimeConditionLookBack(sb, "questionDate", 7);
        addCondition(sb, "o.categoryName", OP_SZ_EQUAL_MULTI_NO_ESCAPE, "'car','motorbike'", null);
        System.out.println(sb);
    }
}
