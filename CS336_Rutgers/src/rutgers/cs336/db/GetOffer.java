package rutgers.cs336.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class GetOffer extends DBBase {

	public static final String DATA_OFFER_ID = "DATA_OFFER_ID";

	public static Map getOffer(String offerID) {
		Map output = new HashMap();
		//
		Connection        con          = null;
		PreparedStatement preparedStmt = null;
		try {
			con = getConnection();
			//
			preparedStmt = con.prepareStatement(SQL_OFFER_SELECT);
			preparedStmt.setString(1, offerID);
			//
			ResultSet rs = preparedStmt.executeQuery();
			//
			while (rs.next()) {
				Object categoryName = rs.getObject(1);
				Object seller       = rs.getObject(2);
				Object min_price    = rs.getObject(3);
				Object description  = rs.getObject(4);
				Object startDate    = rs.getObject(5);
				Object endDate      = rs.getObject(6);
				Object status       = rs.getObject(7);
				//
				output.put("categoryName", categoryName.toString());
				output.put("seller", seller.toString());
				output.put("min_price", min_price.toString());
				output.put("description", description.toString());
				output.put("startDate", startDate.toString());
				output.put("endDate", endDate.toString());
				output.put("status", status.toString());
			}
			//
			preparedStmt = con.prepareStatement(SQL_OFFERFIELD_SELECT);
			preparedStmt.setString(1, offerID);
			//
			rs = preparedStmt.executeQuery();
			//
			while (rs.next()) {
				Object fieldID   = rs.getObject(1);
				Object fieldName = rs.getObject(2);
				Object fieldType = rs.getObject(3);
				Object fieldText = rs.getObject(4);
				//
				output.put(fieldName.toString(), fieldText.toString());
			}
		}
		catch (SQLException e) {
			output.put(DATA_NAME_STATUS, false);
			output.put(DATA_NAME_MESSAGE, "ERROR: " + e.getErrorCode() + ", SQL_STATE: " + e.getSQLState());
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
			output.put(DATA_NAME_STATUS, false);
			output.put(DATA_NAME_MESSAGE, "ERROR: " + "ClassNotFoundException" + ", SQL_STATE: " + e.getMessage());
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
		Map map = getOffer("5642babcb54a4872b15353bed3712824");
		//
		System.out.println(DATA_NAME_STATUS + "= " + map.get(DATA_NAME_STATUS));
		System.out.println(DATA_NAME_MESSAGE + "= " + map.get(DATA_NAME_MESSAGE));
		System.out.println(DATA_NAME_USER_TYPE + "= " + map.get(DATA_NAME_USER_TYPE));
	}
}
