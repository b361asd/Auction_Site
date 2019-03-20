package rutgers.cs336.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchOffer extends DBBase {

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


	public static Map doSearchOffer(Map<String, String[]> parameters) {
		Map output = new HashMap();
		//
		List<OfferItem>        lstOffer = new ArrayList<>();
		Map<String, OfferItem> mapOffer = new HashMap<>();
		//
		Connection con       = null;
		Statement  statement = null;
		try {
			con = getConnection();
			//
			String category_Name  = getStringFromParamMap("categoryName", parameters);
			//
			StringBuilder sb = FormatterOfferQuery.initQuery(category_Name);
			//
			{
				String offerIDOP  = getStringFromParamMap("offerIDOP", parameters);
				String offerIDVal = getStringFromParamMap("offerIDVal", parameters);
				FormatterOfferQuery.addCondition(sb, "offerID", offerIDOP, offerIDVal, null);
			}
			//
			{
				String sellerOP  = getStringFromParamMap("sellerOP", parameters);
				String sellerVal = getStringFromParamMap("sellerVal", parameters);
				FormatterOfferQuery.addCondition(sb, "seller", sellerOP, sellerVal, null);
			}
			//
			//		out.println(getConditionCodeCheckBox("conditionCode"));

			{
				String lstConditionCode = "";
				for (int i = 1; i <= 6; i++) {
					String temp = getStringFromParamMap("conditionCode_" + i, parameters);
					if (temp != null && temp.length() > 0) {
						if (lstConditionCode.equals("")) {
							lstConditionCode = "" + i;
						}
						else {
							lstConditionCode = lstConditionCode + "," + i;
						}
					}
				}
				FormatterOfferQuery.addCondition(sb, "conditionCode", FormatterOfferQuery.OP_INT_EQUAL_MULTI, lstConditionCode, null);
			}
			//
			{
				String descriptionOP  = getStringFromParamMap("descriptionOP", parameters);
				String descriptionVal = getStringFromParamMap("descriptionVal", parameters);
				FormatterOfferQuery.addCondition(sb, "description", descriptionOP, descriptionVal, null);
			}
			//
			{
				String priceOP   = getStringFromParamMap("priceOP", parameters);
				String priceVal1 = getStringFromParamMap("priceVal1", parameters);
				String priceVal2 = getStringFromParamMap("priceVal2", parameters);
				FormatterOfferQuery.addCondition(sb, "price", priceOP, priceVal1, priceVal2);
			}
			//
			{
				String statusOP  = FormatterOfferQuery.OP_INT_EQUAL;
				String statusVal = "1";    // Active
				FormatterOfferQuery.addCondition(sb, "status", statusOP, statusVal, null);
			}
			//
			String   lstFieldIDs = getStringFromParamMap("lstFieldIDs", parameters);
			String[] fieldIDs    = lstFieldIDs.split(",");
			//
			FormatterOfferQuery.initFieldCondition(sb);
			//
			for (String fieldID : fieldIDs) {
				//
				String fieldOP   = getStringFromParamMap("fieldop_" + fieldID, parameters);
				String fieldVal1 = getStringFromParamMap("fieldval1_" + fieldID, parameters);
				String fieldVal2 = getStringFromParamMap("fieldval2_" + fieldID, parameters);
				FormatterOfferQuery.addFieldCondition(sb, fieldID, fieldOP, fieldVal1, fieldVal2);
			}
			//
			FormatterOfferQuery.doneFieldCondition(sb);
			//
			statement = con.createStatement();
			//
			ResultSet rs = statement.executeQuery(sb.toString());
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
			if (statement != null) {
				try {
					statement.close();
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
		Map map = doSearchOffer(null);
		//
		System.out.println(DATA_NAME_STATUS + "= " + map.get(DATA_NAME_STATUS));
		System.out.println(DATA_NAME_MESSAGE + "= " + map.get(DATA_NAME_MESSAGE));
		System.out.println(DATA_NAME_USER_TYPE + "= " + map.get(DATA_NAME_USER_TYPE));
	}
}
