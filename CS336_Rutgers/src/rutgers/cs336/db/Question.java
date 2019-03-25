package rutgers.cs336.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Question extends DBBase {

	
	
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
			//
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
				output.put(DATA_NAME_MESSAGE, "Answer posted!");
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
			//
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
	
	
	
	public static Map retrieveOneQuestion() {
		Map output = new HashMap();
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
			if (rs.next()) {											// Only 1 row
				Object questionID  	= rs.getObject(1);
				Object userID 			= rs.getObject(2);
				Object question  		= rs.getObject(3);
				Object questionDate  = rs.getObject(4);
				//
				output.put(DATA_NAME_STATUS, true);
				output.put(DATA_NAME_MESSAGE, "One question!");
				//
				output.put("questionID", 	questionID);
				output.put("userID", 		userID);
				output.put("question", 		question);
				output.put("questionDate", questionDate);
			}
			else {
				output.put(DATA_NAME_STATUS, false);
				output.put(DATA_NAME_MESSAGE, "No questions to answer!");
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
			//
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

	}

}
