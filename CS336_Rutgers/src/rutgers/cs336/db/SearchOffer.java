package rutgers.cs336.db;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchOffer extends DBBase implements IConstant {
	public static final String DATA_OFFER_ID = "DATA_OFFER_ID";

	public static class OfferItem {
		String offerId;
		String categoryName;
		String seller;
		String min_price;
		String details;
		String startDate;
		String endDate;

		//
		public OfferItem(Object obj_offerId, Object obj_categoryName, Object obj_seller, Object obj_min_price, Object obj_description, Object obj_startDate, Object obj_endDate) {
			this.offerId = obj_offerId.toString();
			this.categoryName = obj_categoryName.toString();
			this.seller = obj_seller.toString();
			this.min_price = obj_min_price.toString();
			this.details = obj_description.toString();
			this.startDate = obj_startDate.toString();
			this.endDate = obj_endDate.toString();
		}

		public void addDetails(Object key, Object value, Object type) {
			details += ", " + key + " is " + value;
		}

		public String getOfferId() {
			return offerId;
		}

		public String getCategoryName() {
			return categoryName;
		}

		public String getSeller() {
			return seller;
		}

		public String getMin_price() {
			return min_price;
		}

		public String getDetails() {
			return details;
		}

		public String getStartDate() {
			return startDate;
		}

		public String getEndDate() {
			return endDate;
		}
	}


	private static final String SQL_GET_OFFER = "select offerId, categoryName, seller, min_price, description, startDate, endDate from Offer where status = 1 and offerId like ?";

	private static final String SQL_GET_OFFER_FIELD = "select OfferField.offerId, OfferField.fieldID, fieldName, fieldType, fieldText from OfferField inner join Field on OfferField.fieldID = Field.fieldID where OfferField.offerId in (select offerId from Offer where status = 1 and offerId like ?)";

	public static Map doSearchOffer(Map<String, String[]> parameters) {
		Map output = new HashMap();
		//
		List<OfferItem>        lstOffer = new ArrayList<>();
		Map<String, OfferItem> mapOffer = new HashMap<>();
		//
		Connection        con          = null;
		PreparedStatement preparedStmt = null;
		try {
			con = getConnection();
			//
			preparedStmt = con.prepareStatement(SQL_GET_OFFER);
			preparedStmt.setString(1, "%");
			//
			ResultSet rs = preparedStmt.executeQuery();
			//
			while (rs.next()) {
				Object offerId      = rs.getObject(1);
				Object categoryName = rs.getObject(2);
				Object seller       = rs.getObject(3);
				Object min_price    = rs.getObject(4);
				Object description  = rs.getObject(5);
				Object startDate    = rs.getObject(6);
				Object endDate      = rs.getObject(7);
				//
				OfferItem one = new OfferItem(offerId, categoryName, seller, min_price, description, startDate, endDate);
				lstOffer.add(one);
				mapOffer.put(offerId.toString(), one);
			}
			//
			preparedStmt = con.prepareStatement(SQL_GET_OFFER_FIELD);
			preparedStmt.setString(1, "%");
			//
			rs = preparedStmt.executeQuery();
			//
			while (rs.next()) {
				Object offerId = rs.getObject(1);
				//Object fieldID = rs.getObject(2);
				Object fieldName = rs.getObject(3);
				Object fieldType = rs.getObject(4);
				Object fieldText = rs.getObject(5);
				//
				OfferItem one = mapOffer.get(offerId.toString());
				//
				one.addDetails(fieldName, fieldText, fieldType);
			}
			//
			output.put(DATA_NAME_DATA, lstOffer);
			//
			output.put(DATA_NAME_STATUS, true);
			output.put(DATA_NAME_MESSAGE, "OK");
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
		initTest();
		//
		Map map = doSearchOffer(null);
		//
		System.out.println(DATA_NAME_STATUS + "= " + map.get(DATA_NAME_STATUS));
		System.out.println(DATA_NAME_MESSAGE + "= " + map.get(DATA_NAME_MESSAGE));
		System.out.println(DATA_NAME_USER_TYPE + "= " + map.get(DATA_NAME_USER_TYPE));
	}
}
