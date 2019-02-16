package rutgers.cs336.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBBase {
	private static String dbURL;
	private static String user;
	private static String pwd;

	public static void init(String url, String username, String password) {
		dbURL = url;
		user = username;
		pwd = password;
	}

	public static Connection getConnection() throws ClassNotFoundException, SQLException {
		// JDBC driver for MySQL. Latest: https://dev.mysql.com/downloads/connector/j/
		Class.forName("com.mysql.cj.jdbc.Driver");
		//
		Connection conn = DriverManager.getConnection(dbURL, user, pwd);
		//
		return conn;
	}

	static {
		// JDBC Connector URL
		init("jdbc:mysql://localhost:3306/BuyMe", "cs336", "cs336_password");
		//
	}
}