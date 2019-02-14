package rutgers.cs336.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class VerifyLogin extends DaoBase implements IDaoConstant {

	public static Map doVerifyLogin(String userID, String pwd) {
		Map output = new HashMap();
		//
		Connection con = null;
		Statement stmt = null;
		try {
			con = getConnection();
			//
			stmt = con.createStatement();
			//
			ResultSet rs = stmt.executeQuery(
					  "select password, firstname, lastname, active, usertype from User where username='" + userID + "'");
			//
			if (rs.next()) {
				Object password   = rs.getObject(1);
				Object firstname  = rs.getObject(2);
				Object lastname   = rs.getObject(3);
				Object active     = rs.getObject(4);
				Object usertype   = rs.getObject(5);
				//
				if (!pwd.equals(password)) {
					output.put(DATA_NAME_STATUS, false);
					output.put(DATA_NAME_MESSAGE, "Wrong Password. Please try again.");
				}
				else if (!(Boolean) active) {
					output.put(DATA_NAME_STATUS, false);
					output.put(DATA_NAME_MESSAGE, "User not active. Contact a customer representative.");
				}
				else {
					output.put(DATA_NAME_STATUS, true);
					output.put(DATA_NAME_MESSAGE, "Welcome, " + firstname.toString() + " " + lastname.toString() + "!");
					output.put(DATA_NAME_USER_TYPE, usertype);
				}
			}
			else {
				output.put(DATA_NAME_STATUS, false);
				output.put(DATA_NAME_MESSAGE, "User does not exist. Try again.");
			}
		} catch (SQLException e) {
			output.put(DATA_NAME_STATUS, false);
			output.put(DATA_NAME_MESSAGE, "ERROR: " + e.getErrorCode() + ", SQL_STATE: " + e.getSQLState());
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			output.put(DATA_NAME_STATUS, false);
			output.put(DATA_NAME_MESSAGE, "ERROR: " + "ClassNotFoundException" + ", SQL_STATE: " + e.getMessage());
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
			//
			if (con != null) {
				try {
					con.close();
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		}
		//
		return output;
	}


	public static void main(String[] args) {
		// JDBC Connector URL
		init("jdbc:mysql://localhost:3306/BuyMe", "cs336", "cs336_password");
		//
		Map map = doVerifyLogin("admin", "admin_pwd");
		//
		System.out.println(DATA_NAME_STATUS + "= " + map.get(DATA_NAME_STATUS));
		System.out.println(DATA_NAME_MESSAGE + "= " + map.get(DATA_NAME_MESSAGE));
		System.out.println(DATA_NAME_USER_TYPE + "= " + map.get(DATA_NAME_USER_TYPE));
	}
}