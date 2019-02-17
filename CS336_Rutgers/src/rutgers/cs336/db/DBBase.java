package rutgers.cs336.db;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

public class DBBase {
	private static String dbURL;
	private static String user;
	private static String pwd;

	public static void init(String url, String username, String password) {
		dbURL = url;
		user = username;
		pwd = password;
	}

	public static void initTest() {
		// JDBC Connector URL
		init("jdbc:mysql://localhost:3306/BuyMe", "cs336", "cs336_password");
	}


	public static Connection getConnection() throws ClassNotFoundException, SQLException {
		// JDBC driver for MySQL. Latest: https://dev.mysql.com/downloads/connector/j/
		Class.forName("com.mysql.cj.jdbc.Driver");
		//
		Connection conn = DriverManager.getConnection(dbURL, user, pwd);
		//
		return conn;
	}

	public static String getUUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replace("-", "");
	}


	public static BigDecimal getBigDecimalFromParamMap(String name, Map<String, String[]> parameters) {
		String[] temps = parameters.get(name);
		//
		if (temps != null && temps.length > 0) {
			return new BigDecimal(temps[0]);
		}
		else {
			return new BigDecimal(-1);
		}
	}


	public static String getStringFromParamMap(String name, Map<String, String[]> parameters) {
		String[] temps = parameters.get(name);
		//
		if (temps != null && temps.length > 0) {
			return temps[0];
		}
		else {
			return "";
		}
	}


	public static int getIntFromParamMap(String name, Map<String, String[]> parameters) {
		String[] temps = parameters.get(name);
		//
		if (temps != null && temps.length > 0) {
			int iTemp = -1;
			try {
				iTemp = Integer.parseInt(temps[0]);
			}
			catch (NumberFormatException e) {
				e.printStackTrace();
			}
			//
			return iTemp;
		}
		return -2;
	}


	public static String getParamMap(Map<String, String[]> parameters) {
		StringBuilder output = new StringBuilder("Param: ");
		for (Map.Entry<String, String[]> s : parameters.entrySet()) {
			String   key    = s.getKey();
			String[] values = s.getValue();
			//
			output.append(key).append("=").append(values[0]).append(", ");
		}
		//
		return output.toString();
	}
}