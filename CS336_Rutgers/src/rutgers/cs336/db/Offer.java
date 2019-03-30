package rutgers.cs336.db;

import rutgers.cs336.gui.Helper;
import rutgers.cs336.gui.TableData;

import java.sql.*;
import java.util.*;

public class Offer extends DBBase {

	private static final int FIELD_START_INDEX = 12;

	public static Map doSearchSimilar(String offeridcategorynameconditioncode) {
		String[] temp = offeridcategorynameconditioncode.split(",");
		//
		String categoryName  = temp[1];
		String conditionCode = Helper.getCodeFromCondition(temp[2]);
		//
		StringBuilder sb = FormatterOfferQuery.buildSQLSimilarOffer(categoryName, conditionCode);
		//
		String sql = sb.toString();
		//
		return doSearchOfferInternal(sql);
	}


	public static Map doSearchOfferByID(Map<String, String[]> parameters) {
		String offeridcategoryname = getStringFromParamMap("offeridcategoryname", parameters);
		//
		String[] temp = offeridcategoryname.split(",");
		//
		String categoryName = temp[1];
		//
		StringBuilder sb = FormatterOfferQuery.initQuerySearch();
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
		String sql = sb.toString();
		//
		return doSearchOfferInternal(sql);
	}


	public static Map doSearchOffer(Map<String, String[]> parameters) {
		StringBuilder sb  = formatSQLWithParameters(parameters);
		String        sql = sb.toString();
		//
		return doSearchOfferInternal(sql);
	}

	private static Map doSearchOfferInternal(String sql) {
		Map output = new HashMap();
		//
		List lstHeader = new ArrayList();
		List lstRows   = new ArrayList();
		//
		Map<String, String>  mapFields         = new HashMap<>();
		Map<String, Integer> mapFieldIDToIndex = new HashMap<>();
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
					mapFields.put("" + rowIndex + "-" + fieldID, (fieldText == null) ? "" : fieldText.toString());
				}
				else {   // New Row
					List currentRow = new LinkedList();
					lstRows.add(currentRow);
					rowIndex++;
					//
					currentofferId = offerId.toString();
					//
					currentRow.add(offerId);
					currentRow.add(seller);
					currentRow.add(categoryName);
					currentRow.add(Helper.getConditionFromCode(conditionCode.toString()));
					currentRow.add(description);
					currentRow.add(initPrice);
					currentRow.add(increment);
					currentRow.add(minPrice);
					currentRow.add(startDate);
					currentRow.add(endDate);
					currentRow.add(status);
					currentRow.add(price);
					//
					mapFields.put("" + rowIndex + "-" + fieldID, (fieldText == null) ? "" : fieldText.toString());
					//
					if (rowIndex == 0) {
						lstHeader.add("offerId");
						lstHeader.add("Seller");
						lstHeader.add("Category");
						lstHeader.add("Condition");
						lstHeader.add("Desc");
						lstHeader.add("initPrice");
						lstHeader.add("increment");
						lstHeader.add("minPrice");
						lstHeader.add("Start");
						lstHeader.add("End");
						lstHeader.add("status");
						lstHeader.add("CurrBid");
					}
					//
				}
				//
				if (mapFieldIDToIndex.get(fieldID.toString()) == null) {
					mapFieldIDToIndex.put(fieldID.toString(), lstHeader.size());
					//
					lstHeader.add(fieldName.toString());
				}
			}
			//
			Map<Integer, String> mapIndexToFieldID = new HashMap<>();
			for (Map.Entry<String, Integer> entry : mapFieldIDToIndex.entrySet()) {
				mapIndexToFieldID.put(entry.getValue(), entry.getKey());      // Reverse
			}
			//
			for (int i = 0; i < lstRows.size(); i++) {
				List rowList = (List) lstRows.get(i);
				for (int j = FIELD_START_INDEX; j < lstHeader.size(); j++) {
					String fieldId = mapIndexToFieldID.get(j);
					String key     = "" + i + "-" + fieldId;
					String item    = mapFields.get(key);
					if (item == null) {
						rowList.add("");
					}
					else {
						rowList.add(item);
					}
				}
			}
			//
			int[] colSeq = new int[lstHeader.size() - 4];
			{
				colSeq[0] = 2;      //Category
				colSeq[1] = 3;      //Condition
				colSeq[2] = 4;      //Desc
				colSeq[3] = 1;      //Seller
				colSeq[4] = 5;      //initPrice
				colSeq[5] = 11;   //CurrBid
				colSeq[6] = 8;      //Start
				colSeq[7] = 9;      //End
				//lstHeader.add("offerId");		0
				//lstHeader.add("increment");		6
				//lstHeader.add("minPrice");		7
				//lstHeader.add("status");			10
				for (int i = FIELD_START_INDEX; i < lstHeader.size(); i++) {
					colSeq[i + 8 - FIELD_START_INDEX] = i;
				}
			}
			//
			TableData dataTable = new TableData(lstHeader, lstRows, colSeq);
			//
			output.put(DATA_NAME_DATA, dataTable);
			//
			output.put(DATA_NAME_STATUS, true);
			output.put(DATA_NAME_MESSAGE, "OK");
		}
		catch (SQLException e) {
			output.put(DATA_NAME_STATUS, false);
			output.put(DATA_NAME_MESSAGE, "ERROR=" + e.getErrorCode() + ", SQL_STATE=" + e.getSQLState() + ", SQL=" + (sql));
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


	public static Map doGenerateNewOfferAlertCriterion(String userID, Map<String, String[]> parameters) {
		Map output = new HashMap();
		//
		String        categoryName = getStringFromParamMap("categoryName", parameters);
		StringBuilder sb           = formatSQLWithParametersForSearchOrAlert(parameters, userID, false);
		//
		Connection        con          = null;
		PreparedStatement preparedStmt = null;
		try {
			con = getConnection();
			//
			preparedStmt = con.prepareStatement(SQL_OFFER_ALERT_CRITERION_INSERT);
			//
			preparedStmt.setString(1, getUUID());
			preparedStmt.setString(2, userID);
			preparedStmt.setString(3, categoryName);
			preparedStmt.setString(4, sb.toString());
			//
			preparedStmt.execute();
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


	private static StringBuilder formatSQLWithParameters(Map<String, String[]> parameters) {
		return formatSQLWithParametersForSearchOrAlert(parameters, null, true);
	}

	private static StringBuilder formatSQLWithParametersForSearchOrAlert(Map<String, String[]> parameters, String userID, boolean isSearch) {      //Search or Alert
		StringBuilder sb;
		//
		String categoryNames = getStringsFromParamMap("categoryName", 1, parameters, "'");
		//
		if (isSearch) {
			sb = FormatterOfferQuery.initQuerySearch();
		}
		else {
			sb = FormatterOfferQuery.initQueryAlert(userID, categoryNames);
		}
		//
		if (isSearch) {                        // In alert, offerID placeholder will be replaced with real one
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
		if (isSearch) {                     // In alert, categoryName is handled outside query
			String categoryNameOP  = FormatterOfferQuery.OP_SZ_EQUAL_MULTI_NO_ESCAPE;
			String categoryNameVal = categoryNames;
			FormatterOfferQuery.addCondition(sb, "categoryName", categoryNameOP, categoryNameVal, null);
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
		{
			String descriptionOP  = getStringFromParamMap("descriptionOP", parameters);
			String descriptionVal = getStringFromParamMap("descriptionVal", parameters);
			FormatterOfferQuery.addCondition(sb, "description", descriptionOP, descriptionVal, null);
		}
		//
		if (isSearch) {                     // In alert, new offer has no bid / price yet
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
		if (isSearch) {
			FormatterOfferQuery.doneFieldConditionSearch(sb);
		}
		else {
			FormatterOfferQuery.doneFieldConditionAlert(sb);
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
