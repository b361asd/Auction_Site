package rutgers.cs336.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class SearchOffer extends DBBase {


	public static Map doSearchOfferByID(Map<String, String[]> parameters) {
		return doSearchOfferInternal(parameters, 2);
	}

	public static Map doSearchOffer(Map<String, String[]> parameters) {
		return doSearchOfferInternal(parameters, 1);
	}

	private static Map doSearchOfferInternal(Map<String, String[]> parameters, int searchType) {
		Map output = new HashMap();
		//
		List lstRows   = new ArrayList();
		List lstHeader = new ArrayList();
		//
		StringBuilder sb = null;
		//
		Connection con       = null;
		Statement  statement = null;
		try {
			con = getConnection();
			//
			if (searchType == 1) {
				sb = formatSQLWithParameters(parameters);
			}
			else if (searchType == 2) {
				sb = formatSQLWithOfferID(parameters);
			}
			//
			statement = con.createStatement();
			//
			ResultSet rs = statement.executeQuery(sb.toString());
			//
			String currentofferId = "";
			int    rowIndex       = -1;
			while (rs.next()) {
				Object offerId       = rs.getObject(1);
				Object seller        = rs.getObject(2);
				Object categoryName  = rs.getObject(3);
				Object conditionCode = rs.getObject(4);
				Object description   = rs.getObject(5);
				Object initPrice     = rs.getObject(6);
				Object increment     = rs.getObject(7);
				Object minPrice      = rs.getObject(8);
				Object startDate     = rs.getObject(9);
				Object endDate       = rs.getObject(10);
				Object status        = rs.getObject(11);
				Object price         = rs.getObject(12);
				//
				Object fieldID   = rs.getObject(13);
				Object fieldText = rs.getObject(14);
				Object fieldName = rs.getObject(15);
				Object fieldType = rs.getObject(16);
				//
				if (currentofferId.equals(offerId.toString())) {   // Continue with current row
					List currentRow = (List) lstRows.get(rowIndex);
					//
					currentRow.add(fieldText);
					//
					if (rowIndex == 0) {
						lstHeader.add(fieldName.toString());
					}
				}
				else {   // New Row
					List currentRow = new LinkedList();
					lstRows.add(currentRow);
					rowIndex++;
					//
					currentofferId = offerId.toString();
					//
					currentRow.add(offerId);
					currentRow.add(categoryName);
					currentRow.add(seller);
					currentRow.add(conditionCode);
					currentRow.add(description);
					//currentRow.add(initPrice);
					//currentRow.add(increment);
					//currentRow.add(minPrice);
					currentRow.add(startDate);
					currentRow.add(endDate);
					//currentRow.add(status);
					currentRow.add(price);
					currentRow.add(fieldText);
					//
					if (rowIndex == 0) {
						lstHeader.add("offerId");
						lstHeader.add("Category");
						lstHeader.add("Seller");
						lstHeader.add("Condition");
						lstHeader.add("Desc");
						//lstHeader.add("initPrice");
						//lstHeader.add("increment");
						//lstHeader.add("minPrice");
						lstHeader.add("Start");
						lstHeader.add("End");
						//lstHeader.add("status");
						lstHeader.add("CurrBid");
						lstHeader.add(fieldName.toString());
					}
				}
			}
			//
			output.put(DATA_NAME_DATA, lstRows);
			output.put(DATA_NAME_DATA_ADD, lstHeader);
			//
			output.put(DATA_NAME_STATUS, true);
			output.put(DATA_NAME_MESSAGE, "OK");
		}
		catch (SQLException e) {
			output.put(DATA_NAME_STATUS, false);
			output.put(DATA_NAME_MESSAGE, "ERROR=" + e.getErrorCode() + ", SQL_STATE=" + e.getSQLState() + ", SQL=" + (sb != null ? sb.toString() : null));
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
			output.put(DATA_NAME_STATUS, false);
			output.put(DATA_NAME_MESSAGE, "ERROR=" + "ClassNotFoundException" + ", SQL_STATE=" + e.getMessage());
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


	private static StringBuilder formatSQLWithParameters(Map<String, String[]> parameters) {
		StringBuilder sb;
		//
		String category_Name = getStringFromParamMap("categoryName", parameters);
		//
		sb = FormatterOfferQuery.initQuery(category_Name);
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
		FormatterOfferQuery.initFieldCondition(sb);
		//
		String   lstFieldIDs = getStringFromParamMap("lstFieldIDs", parameters);
		String[] fieldIDs    = lstFieldIDs.split(",");
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
		return sb;
	}


	private static StringBuilder formatSQLWithOfferID(Map<String, String[]> parameters) {
		StringBuilder sb = null;
		//
		String offeridcategoryname = getStringFromParamMap("offeridcategoryname", parameters);
		//
		String[] temp = offeridcategoryname.split("\\,");
		//
		String category_Name = temp[1];
		//
		sb = FormatterOfferQuery.initQuery(category_Name);
		//
		{
			String offerIDOP  = FormatterOfferQuery.OP_SZ_EQUAL;
			String offerIDVal = temp[0];
			FormatterOfferQuery.addCondition(sb, "offerID", offerIDOP, offerIDVal, null);
		}
		//
		{
			String statusOP  = FormatterOfferQuery.OP_INT_EQUAL;
			String statusVal = "1";    // Active
			FormatterOfferQuery.addCondition(sb, "status", statusOP, statusVal, null);
		}
		//
		return sb;
	}


	public static void main(String[] args) {
		Map map = doSearchOffer(null);
		//
		System.out.println(DATA_NAME_STATUS + "= " + map.get(DATA_NAME_STATUS));
		System.out.println(DATA_NAME_MESSAGE + "= " + map.get(DATA_NAME_MESSAGE));
		System.out.println(DATA_NAME_USER_TYPE + "= " + map.get(DATA_NAME_USER_TYPE));
	}
}
