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
}
