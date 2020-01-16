package b361asd.auction.db;

/**
 * Format SQL querying the Question table
 */
public class FormatterQuestionQuery extends DBBase {

   public static StringBuilder initQuerySearch() {
      StringBuilder sb = new StringBuilder();
      sb.append("SELECT questionID, userID, question, answer, repID, questionDate, answerDate FROM Question WHERE (NOT (answer IS NULL OR answer = ''))");
      //
      return sb;
   }

   public static void main(String[] args) {
      StringBuilder sb = initQuerySearch();
      //
      addContainTagsCondition2Cols(sb, "question", "answer", "o");
      addDatetimeConditionLookback(sb, "questionDate", 7);
      //
      //addCondition(sb, "o.categoryName", OP_SZ_EQUAL_MULTI_NO_ESCAPE, "'car','motorbike'", null);
      //
      System.out.println(sb.toString());
   }
}

/*
	String SQL_QUESTION_QUERY_BY_USER      = "SELECT questionID, userID, question, answer, repID, questionDate, answerDate FROM Question Where true AND userID = ?";
	String SQL_QUESTION_QUERY_BY_REP       = "SELECT questionID, userID, question, answer, repID, questionDate, answerDate FROM Question Where true AND = ?";
	String SQL_QUESTION_QUERY_BY_1TAGS     = "SELECT questionID, userID, question, answer, repID, questionDate, answerDate FROM Question WHERE (question LIKE ? OR answer LIKE ?)";
	String SQL_QUESTION_QUERY_BY_2TAGS     = "SELECT questionID, userID, question, answer, repID, questionDate, answerDate FROM Question WHERE (question LIKE ? OR answer LIKE ?) AND (question LIKE ? OR answer LIKE ?)";
	String SQL_QUESTION_QUERY_BY_3TAGS     = "SELECT questionID, userID, question, answer, repID, questionDate, answerDate FROM Question WHERE (question LIKE ? OR answer LIKE ?) AND (question LIKE ? OR answer LIKE ?) AND (question LIKE ? OR answer LIKE ?)";
*/
