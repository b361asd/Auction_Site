package rutgers.cs336.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;



public class User extends DBBase {

	
	
	public static Map doAddUser(String username, String password, String email, String firstName, String lastName, String street, String city, String state, String zipCode, String phone, int usertype) {
		Map output = new HashMap();
		//
		Connection        con          = null;
		PreparedStatement preparedStmt = null;
		try {
			con = getConnection();
			//
			preparedStmt = con.prepareStatement(SQL_USER_INSERT);
			//
			preparedStmt.setString(1, username);
			preparedStmt.setString(2, password);
			preparedStmt.setString(3, email);
			preparedStmt.setString(4, firstName);
			preparedStmt.setString(5, lastName);
			preparedStmt.setString(6, street + " " + city + " " + state + " " + zipCode);		//address = street + city + state + zipCode
			preparedStmt.setString(7, phone);
			preparedStmt.setInt(8, usertype);
			//
			preparedStmt.execute();
			//
			int count = preparedStmt.getUpdateCount();
			if (count == 0) {
				output.put(DATA_NAME_STATUS, false);
				output.put(DATA_NAME_MESSAGE, "Could not register user.");
			}
			else {
				output.put(DATA_NAME_STATUS, true);
				output.put(DATA_NAME_MESSAGE, "User registered");
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

	

	public static Map doVerifyLogin(String userID, String pwd) {
		Map output = new HashMap();
		//
		Connection        con          = null;
		PreparedStatement preparedStmt = null;
		try {
			con = getConnection();
			//
			preparedStmt = con.prepareStatement(SQL_USER_SELECT);
			//
			preparedStmt.setString(1, userID);
			//
			ResultSet rs = preparedStmt.executeQuery();
			//
			if (rs.next()) {											//Only 1 row
				Object password  = rs.getObject(1);
				Object firstname = rs.getObject(2);
				Object lastname  = rs.getObject(3);
				Object active    = rs.getObject(4);
				Object usertype  = rs.getObject(5);
				//
				if (!pwd.equals(password)) {
					output.put(DATA_NAME_STATUS, false);
					output.put(DATA_NAME_MESSAGE, "Wrong Password. Please try again.");
				}
				else if (!(Boolean)active) {
					output.put(DATA_NAME_STATUS, false);
					output.put(DATA_NAME_MESSAGE, "User not active. Contact a customer representative.");
				}
				else {
					output.put(DATA_NAME_STATUS, true);
					output.put(DATA_NAME_MESSAGE, "Welcome, " + firstname.toString() + " " + lastname.toString() + "!");
					//
					output.put(DATA_NAME_USER_TYPE, 	usertype);
					output.put(DATA_NAME_FIRST_NAME, firstname);
					output.put(DATA_NAME_LAST_NAME, 	lastname);
				}
			}
			else {
				output.put(DATA_NAME_STATUS, false);
				output.put(DATA_NAME_MESSAGE, "User does not exist. Try another user or register");
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
		Map map = doAddUser("abc", "123", "email", "fN", "lN", "123 St", "Pearl", "NJ", "01010", "39239033", 3);
		//
		System.out.println(DATA_NAME_STATUS + "= " + map.get(DATA_NAME_STATUS));
		System.out.println(DATA_NAME_MESSAGE + "= " + map.get(DATA_NAME_MESSAGE));
		System.out.println(DATA_NAME_USER_TYPE + "= " + map.get(DATA_NAME_USER_TYPE));
	}
	


	public static void main1(String[] args) {
		Map map = doVerifyLogin("user", "user_pwd");
		//
		System.out.println(DATA_NAME_STATUS + "= " + map.get(DATA_NAME_STATUS));
		System.out.println(DATA_NAME_MESSAGE + "= " + map.get(DATA_NAME_MESSAGE));
		System.out.println(DATA_NAME_USER_TYPE + "= " + map.get(DATA_NAME_USER_TYPE));
	}
}