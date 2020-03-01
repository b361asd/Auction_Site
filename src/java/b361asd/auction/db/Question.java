package java.b361asd.auction.db;

import java.b361asd.auction.gui.Helper;
import java.b361asd.auction.gui.TableData;

import java.sql.*;
import java.util.*;

public class Question extends DBBase {

   private static List  lstHeader_question = Arrays.asList("questionID", "userID", "question", "answer", "repID", "questionDate", "answerDate");
   private static int[] colSeq_question    = {1, 4, 2, 3, 5, 6};

   /**
    * Search Questions
    *
    * @param parameters Map of all parameters
    * @return Data for GUI rendering
    */
   public static Map searchClosedQuestion(Map<String, String[]> parameters) {
      StringBuilder sb = FormatterQuestionQuery.initQuerySearch();
      //
      {
         String useridOP  = getStringFromParamMap("useridOP", parameters);
         String useridVal = getStringFromParamMap("useridVal", parameters);
         addCondition(sb, "userID", useridOP, useridVal, null);
      }
      //
      {
         String repidOP  = getStringFromParamMap("repidOP", parameters);
         String repidVal = getStringFromParamMap("repidVal", parameters);
         addCondition(sb, "repID", repidOP, repidVal, null);
      }
      //
      {
         String tags = getStringFromParamMap("tags", parameters);
         if (tags.length() > 0) {
            String[] temps = tags.split(",");
            for (String one : temps) {
               if (one != null && one.length() > 0) {
                  addContainTagsCondition2Cols(sb, "question", "answer", one);
               }
            }
         }
      }
      //
      {
         int lookbackdays = getIntFromParamMap("lookbackdays", parameters);
         if (lookbackdays < 1) {
            lookbackdays = 30;
         }
         addDatetimeConditionLookback(sb, "questionDate", lookbackdays);
      }
      //
      String sql = sb.toString();
      //
      Map       output    = new HashMap();
      List      lstRows   = new ArrayList();
      TableData tableData = new TableData(lstHeader_question, lstRows, colSeq_question);
      //
      output.put(DATA_NAME_DATA, tableData);
      //
      Connection con       = null;
      Statement  statement = null;
      try {
         con = getConnection();
         //
         statement = con.createStatement();
         //
         ResultSet rs = statement.executeQuery(sql);
         //
         while (rs.next()) {
            Object questionID   = rs.getObject(1);
            Object userID       = rs.getObject(2);
            Object question     = rs.getObject(3);
            Object answer       = rs.getObject(4);
            Object repID        = rs.getObject(5);
            Object questionDate = rs.getObject(6);
            Object answerDate   = rs.getObject(7);
            //
            List currentRow = new LinkedList();
            lstRows.add(currentRow);
            //
            currentRow.add(questionID);
            currentRow.add(userID);
            currentRow.add(question);
            currentRow.add(answer);
            currentRow.add(repID);
            currentRow.add(questionDate);
            currentRow.add(answerDate);
         }
         //
         output.put(DATA_NAME_STATUS, true);
         output.put(DATA_NAME_MESSAGE, "OK");
         //
         tableData.setDescription("Search Results for Closed Questions");
      }
      catch (SQLException e) {
         output.put(DATA_NAME_STATUS, false);
         output.put(DATA_NAME_MESSAGE, "ERROR=" + e.getErrorCode() + ", SQL_STATE=" + e.getSQLState() + ", SQL=" + (sql));
         tableData.setDescription((String) output.get(DATA_NAME_MESSAGE));
         e.printStackTrace();
      }
      catch (ClassNotFoundException e) {
         output.put(DATA_NAME_STATUS, false);
         output.put(DATA_NAME_MESSAGE, "ERROR=" + "ClassNotFoundException" + ", SQL_STATE=" + e.getMessage());
         tableData.setDescription((String) output.get(DATA_NAME_MESSAGE));
         e.printStackTrace();
      }
      finally {
         if (statement != null) {
            try {
               statement.close();
            }
            catch (Throwable e) {
               e.printStackTrace();
            }
         }
         if (con != null) {
            try {
               con.close();
            }
            catch (Throwable e) {
               e.printStackTrace();
            }
         }
      }
      //
      return output;
   }

   /**
    * Insert a Question
    *
    * @param userID   User ID
    * @param question Question to insert
    * @return Data for GUI rendering
    */
   public static Map insertQuestion(String userID, String question) {
      Map output = new HashMap();
      //
      Connection        con          = null;
      PreparedStatement preparedStmt = null;
      try {
         con = getConnection();
         //
         preparedStmt = con.prepareStatement(SQL_QUESTION_INSERT);
         //
         preparedStmt.setString(1, getUUID());
         preparedStmt.setString(2, userID);
         preparedStmt.setString(3, question);
         //
         preparedStmt.execute();
         //
         int count = preparedStmt.getUpdateCount();
         if (count == 0) {
            output.put(DATA_NAME_STATUS, false);
            output.put(DATA_NAME_MESSAGE, "Could not post your question.");
         }
         else {
            output.put(DATA_NAME_STATUS, true);
            output.put(DATA_NAME_MESSAGE, "Question posted!");
         }
      }
      catch (SQLException e) {
         output.put(DATA_NAME_STATUS, false);
         output.put(DATA_NAME_MESSAGE, "ERROR: " + e.getErrorCode() + ", SQL_STATE: " + e.getSQLState() + ", DETAILS: " + exceptionToString(e));
         e.printStackTrace();
      }
      catch (ClassNotFoundException e) {
         output.put(DATA_NAME_STATUS, false);
         output.put(DATA_NAME_MESSAGE, "ERROR: " + "ClassNotFoundException" + ", SQL_STATE: " + e.getMessage() + ", DETAILS: " + exceptionToString(e));
         e.printStackTrace();
      }
      finally {
         if (preparedStmt != null) {
            try {
               preparedStmt.close();
            }
            catch (Throwable e) {
               e.printStackTrace();
            }
         }
         if (con != null) {
            try {
               con.close();
            }
            catch (Throwable e) {
               e.printStackTrace();
            }
         }
      }
      //
      return output;
   }

   /**
    * Answer a Question
    *
    * @param questionID Question ID
    * @param answer     The answer
    * @param repID      Rep ID
    * @return Data for GUI rendering
    */
   public static Map answerQuestion(String questionID, String answer, String repID) {
      Map output = new HashMap();
      //
      Connection        con          = null;
      PreparedStatement preparedStmt = null;
      try {
         con = getConnection();
         //
         preparedStmt = con.prepareStatement(SQL_QUESTION_UPDATE_WITH_ANSWER);
         //
         preparedStmt.setString(1, answer);
         preparedStmt.setString(2, repID);
         preparedStmt.setString(3, questionID);
         //
         preparedStmt.execute();
         //
         int count = preparedStmt.getUpdateCount();
         if (count == 0) {
            output.put(DATA_NAME_STATUS, false);
            output.put(DATA_NAME_MESSAGE, "Could not post your answer.");
         }
         else {
            output.put(DATA_NAME_STATUS, true);
            output.put(DATA_NAME_MESSAGE, "Answer posted.");
         }
      }
      catch (SQLException e) {
         output.put(DATA_NAME_STATUS, false);
         output.put(DATA_NAME_MESSAGE, "ERROR: " + e.getErrorCode() + ", SQL_STATE: " + e.getSQLState() + ", DETAILS: " + exceptionToString(e));
         e.printStackTrace();
      }
      catch (ClassNotFoundException e) {
         output.put(DATA_NAME_STATUS, false);
         output.put(DATA_NAME_MESSAGE, "ERROR: " + "ClassNotFoundException" + ", SQL_STATE: " + e.getMessage() + ", DETAILS: " + exceptionToString(e));
         e.printStackTrace();
      }
      finally {
         if (preparedStmt != null) {
            try {
               preparedStmt.close();
            }
            catch (Throwable e) {
               e.printStackTrace();
            }
         }
         if (con != null) {
            try {
               con.close();
            }
            catch (Throwable e) {
               e.printStackTrace();
            }
         }
      }
      //
      return output;
   }


   /**
    * Retrieve List of open questions
    *
    * @return Data for GUI rendering
    */
   public static Map retrieveOpenQuestion() {
      Map output = new HashMap();
      //
      List   lstRows = new LinkedList();
      Helper.setData(output, lstRows);
      //
      Connection        con          = null;
      PreparedStatement preparedStmt = null;
      try {
         con = getConnection();
         //
         preparedStmt = con.prepareStatement(SQL_QUESTION_QUERY_OPEN);
         //
         ResultSet rs = preparedStmt.executeQuery();
         //
         while (rs.next()) {
            Object questionID   = rs.getObject(1);
            Object userID       = rs.getObject(2);
            Object question     = rs.getObject(3);
            Object questionDate = rs.getObject(4);
            //
            List currentRow = new LinkedList();
            lstRows.add(currentRow);
            //
            currentRow.add(questionID);
            currentRow.add(userID);
            currentRow.add(question);
            currentRow.add(questionDate);
         }
         //
         output.put(DATA_NAME_STATUS, true);
         output.put(DATA_NAME_MESSAGE, "OK");
      }
      catch (SQLException e) {
         output.put(DATA_NAME_STATUS, false);
         output.put(DATA_NAME_MESSAGE, "ERROR: " + e.getErrorCode() + ", SQL_STATE: " + e.getSQLState() + ", DETAILS: " + exceptionToString(e));
         e.printStackTrace();
      }
      catch (ClassNotFoundException e) {
         output.put(DATA_NAME_STATUS, false);
         output.put(DATA_NAME_MESSAGE, "ERROR: " + "ClassNotFoundException" + ", SQL_STATE: " + e.getMessage() + ", DETAILS: " + exceptionToString(e));
         e.printStackTrace();
      }
      finally {
         if (preparedStmt != null) {
            try {
               preparedStmt.close();
            }
            catch (Throwable e) {
               e.printStackTrace();
            }
         }
         if (con != null) {
            try {
               con.close();
            }
            catch (Throwable e) {
               e.printStackTrace();
            }
         }
      }
      //
      return output;
   }


   public static void main(String[] args) {
      Map<String, String[]> parameters = new HashMap<>();
      //
      //parameters.put("bidID", new String[]{"11fe20aabc7a4025928e9522544be2e3"});
      //
      Map map = searchClosedQuestion(parameters);
      //
      System.out.println(DATA_NAME_STATUS + "= " + map.get(DATA_NAME_STATUS));
      System.out.println(DATA_NAME_MESSAGE + "= " + map.get(DATA_NAME_MESSAGE));
      System.out.println(DATA_NAME_USER_TYPE + "= " + map.get(DATA_NAME_USER_TYPE));
   }
}
