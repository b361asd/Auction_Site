package rutgers.cs336.db;

import rutgers.cs336.gui.Helper;
import rutgers.cs336.gui.TableData;

import java.sql.*;
import java.util.*;

public class Offer extends DBBase {

	private static List  lstHeader_offerdefault        = Arrays.asList("offerId", "Seller", "Category", "Condition", "Desc", "initPrice", "increment", "minPrice", "Start", "End", "status", "CurrBid");
	//
	private static int[] colSeq_offerdefault_ex_status = {2, 3, 4, 1, 5, 11, 8, 9};
	private static int[] colSeq_offerdefault_w_status  = {2, 3, 4, 1, 5, 11, 8, 9, 10};


	private static final int FIELD_START_INDEX = 12;

	// For search Bid
	public static Map doSearchByOfferIDSet(Set<String> offerIDSet) {
		StringBuilder sb;
		//
		sb = FormatterOfferQuery.initQuerySearch();
		//
		FormatterOfferQuery.addCondition(sb, "o.offerID", OP_SZ_EQUAL_MULTI_NO_ESCAPE, getListOfStringsFromSet(offerIDSet, "'"), null);
		//
		return doSearchOfferInternal(sb.toString(), true);
	}


	public static Map doSearchSimilar(String offeridcategorynameconditioncode) {
		Map output;
		//
		String[] temp = offeridcategorynameconditioncode.split(",");
		//
		String categoryName  = temp[1];
		String conditionCode = Helper.getCodeFromCondition(temp[2]);
		//
		StringBuilder sb = FormatterOfferQuery.buildSQLSimilarOffer(categoryName, conditionCode);
		//
		String sql = sb.toString();
		//
		output = doSearchOfferInternal(sql, true);
		//
		TableData dataTable = (TableData) output.get(DATA_NAME_DATA);
		//
		dataTable.setStandOut(temp[0], 0);
		//
		return output;
	}


	public static Map doSearchOfferByID(String offerid) {
		StringBuilder sb = FormatterOfferQuery.initQuerySearch();
		//
		{
			String offerIDOP  = FormatterOfferQuery.OP_SZ_EQUAL;
			String offerIDVal = offerid;
			FormatterOfferQuery.addCondition(sb, "o.offerID", offerIDOP, offerIDVal, null);
		}
		//
		{
			String statusOP  = FormatterOfferQuery.OP_INT_EQUAL;
			String statusVal = "1";    // Active
			FormatterOfferQuery.addCondition(sb, "o.status", statusOP, statusVal, null);
		}
		//
		String sql = sb.toString();
		//
		return doSearchOfferInternal(sql, true);
	}


	public static Map doBrowseOffer() {
		String sql = FormatterOfferQuery.buildSQLBrowseOffer();
		//
		return doSearchOfferInternal(sql, true);
	}

	public static Map doSearchOffer(Map<String, String[]> parameters) {
		StringBuilder sb  = formatSQLWithParametersForSearchOrAlert(parameters, null, true);
		String        sql = sb.toString();
		//
		return doSearchOfferInternal(sql, true);
	}


	public static StringBuilder formatSQLWithParametersForSearchOrAlert(Map<String, String[]> parameters, String userID, boolean isSearch) {      //Search or Alert
		StringBuilder sb;
		//
		if (isSearch) {
			sb = FormatterOfferQuery.initQuerySearch();
		}
		else {
			sb = FormatterOfferQuery.initQueryAlert(userID);
		}
		//
		if (isSearch) {                        // In alert, offerID placeholder will be replaced with real one
			String offerIDOP  = getStringFromParamMap("offerIDOP", parameters);
			String offerIDVal = getStringFromParamMap("offerIDVal", parameters);
			FormatterOfferQuery.addCondition(sb, "o.offerID", offerIDOP, offerIDVal, null);
		}
		//
		{
			String sellerOP  = getStringFromParamMap("sellerOP", parameters);
			String sellerVal = getStringFromParamMap("sellerVal", parameters);
			FormatterOfferQuery.addCondition(sb, "o.seller", sellerOP, sellerVal, null);
		}
		//
		{
			String categoryNames = getListOfStringsFromParamMap("categoryName", 1, parameters, "'");
			//
			String categoryNameOP  = FormatterOfferQuery.OP_SZ_EQUAL_MULTI_NO_ESCAPE;
			String categoryNameVal = categoryNames;
			FormatterOfferQuery.addCondition(sb, "o.categoryName", categoryNameOP, categoryNameVal, null);
		}
		//
		{
			String lstConditionCode = "";
			for (int i = 1; i <= 6; i++) {
				String temp = getStringFromParamMap("conditionCode_" + i, parameters);
				if (temp.length() > 0) {
					if (lstConditionCode.equals("")) {
						lstConditionCode = "" + i;
					}
					else {
						lstConditionCode = lstConditionCode + "," + i;
					}
				}
			}
			FormatterOfferQuery.addCondition(sb, "o.conditionCode", FormatterOfferQuery.OP_INT_EQUAL_MULTI, lstConditionCode, null);
		}
		{
			String descriptionOP  = getStringFromParamMap("descriptionOP", parameters);
			String descriptionVal = getStringFromParamMap("descriptionVal", parameters);
			FormatterOfferQuery.addCondition(sb, "o.description", descriptionOP, descriptionVal, null);
		}
		//
		if (isSearch) {                     // In alert, new offer has no bid / price yet
			String priceOP   = getStringFromParamMap("priceOP", parameters);
			String priceVal1 = getStringFromParamMap("priceVal1", parameters);
			String priceVal2 = getStringFromParamMap("priceVal2", parameters);
			FormatterOfferQuery.addCondition(sb, "o.price", priceOP, priceVal1, priceVal2);
		}
		//
		{
			String statusOP  = FormatterOfferQuery.OP_INT_EQUAL;
			String statusVal = "1";    // Active
			FormatterOfferQuery.addCondition(sb, "o.status", statusOP, statusVal, null);
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


	private static Map doSearchOfferInternal(String sql, boolean excludeStatus) {
		Map output = new HashMap();
		//
		List lstHeader = new LinkedList();
		List lstRows   = new LinkedList();
		//
		Map<String, String>  mapFields         = new HashMap<>();      //RowID-fieldID : fieldText
		Map<String, Integer> mapFieldIDToIndex = new HashMap<>();
		Map<Integer, String> mapIndexToFieldID = new HashMap<>();
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
			String currentofferID = "";
			int    rowIndex       = -1;
			while (rs.next()) {
				Object offerID       = rs.getObject(1);
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
				if (currentofferID.equals(offerID.toString())) {   // Continue with current row
					List currentRow = (List) lstRows.get(rowIndex);
					//
					mapFields.put("" + rowIndex + "-" + fieldID, (fieldText == null) ? "" : fieldText.toString());
				}
				else {   // New Row
					List currentRow = new LinkedList();
					lstRows.add(currentRow);
					rowIndex++;
					//
					currentofferID = offerID.toString();
					//
					currentRow.add(offerID);
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
			int[] colSeq;
			if (excludeStatus) {
				if (lstHeader.size() == 0) {
					lstHeader = lstHeader_offerdefault;
					colSeq = colSeq_offerdefault_ex_status;
				}
				else {
					colSeq = new int[lstHeader.size() - 4];
					//
					colSeq[0] = 2;         //Category
					colSeq[1] = 3;         //Condition
					colSeq[2] = 4;         //Desc
					colSeq[3] = 1;         //Seller
					colSeq[4] = 5;         //initPrice
					colSeq[5] = 11;      //CurrBid
					colSeq[6] = 8;         //Start
					colSeq[7] = 9;         //End
					//lstHeader.add("offerId");		0
					//lstHeader.add("increment");		6
					//lstHeader.add("minPrice");		7
					//lstHeader.add("status");			10
					for (int i = FIELD_START_INDEX; i < lstHeader.size(); i++) {
						colSeq[i + 8 - FIELD_START_INDEX] = i;
					}
				}
			}
			else {
				if (lstHeader.size() == 0) {
					lstHeader = lstHeader_offerdefault;
					colSeq = colSeq_offerdefault_w_status;
				}
				else {
					colSeq = new int[lstHeader.size() - 3];
					//
					colSeq[0] = 2;         //Category
					colSeq[1] = 3;         //Condition
					colSeq[2] = 4;         //Desc
					colSeq[3] = 1;         //Seller
					colSeq[4] = 5;         //initPrice
					colSeq[5] = 11;      //CurrBid
					colSeq[6] = 8;         //Start
					colSeq[7] = 9;         //End
					colSeq[8] = 10;      //status
					//lstHeader.add("offerId");		0
					//lstHeader.add("increment");		6
					//lstHeader.add("minPrice");		7
					for (int i = FIELD_START_INDEX; i < lstHeader.size(); i++) {
						colSeq[i + 9 - FIELD_START_INDEX] = i;
					}
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


	public static Map doCreateOffer(String userID, Map<String, String[]> parameters) {
		Map output = new HashMap();
		//
		String offerID = getUUID();
		//
		Connection        con                   = null;
		PreparedStatement pStmtInsertOffer      = null;
		PreparedStatement pStmtInsertOfferField = null;
		try {
			con = getConnection();
			con.setAutoCommit(false);
			//
			pStmtInsertOffer = con.prepareStatement(SQL_OFFER_INSERT);
			pStmtInsertOffer.setString(1, offerID);
			pStmtInsertOffer.setString(2, getStringFromParamMap("categoryName", parameters));
			pStmtInsertOffer.setString(3, userID);
			pStmtInsertOffer.setBigDecimal(4, getBigDecimalFromParamMap("initPrice", parameters));
			pStmtInsertOffer.setBigDecimal(5, getBigDecimalFromParamMap("increment", parameters));
			pStmtInsertOffer.setBigDecimal(6, getBigDecimalFromParamMap("minPrice", parameters));
			pStmtInsertOffer.setInt(7, getIntFromParamMap("conditionCode", parameters));
			pStmtInsertOffer.setString(8, getStringFromParamMap("description", parameters));
			pStmtInsertOffer.setString(9, getStringFromParamMap("endDate", parameters));
			//
			pStmtInsertOffer.execute();
			//
			pStmtInsertOfferField = con.prepareStatement(SQL_OFFERFIELD_INSERT);
			//
			for (String s : parameters.keySet()) {
				if (s.startsWith("fieldID_")) {
					int    fieldID = Integer.parseInt(s.substring("fieldID_".length()));
					String value   = parameters.get(s)[0];
					//
					pStmtInsertOfferField.setString(1, offerID);
					pStmtInsertOfferField.setInt(2, fieldID);
					pStmtInsertOfferField.setString(3, value);
					pStmtInsertOfferField.execute();
				}
			}
			//
			output.put(DATA_NAME_STATUS, true);
			output.put(DATA_NAME_MESSAGE, "OK");
			//
			con.commit();
		}
		catch (SQLException e) {
			if (con != null) {
				try {
					con.rollback();
				}
				catch (Throwable t) {
					t.printStackTrace();
				}
			}
			//
			output.put(DATA_NAME_STATUS, false);
			output.put(DATA_NAME_MESSAGE, "ERROR: ErrorCode=" + e.getErrorCode() + ", SQL_STATE=" + e.getSQLState() + ", Message=" + e.getMessage() + ", " + dumpParamMap(parameters));
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
			if (con != null) {
				try {
					con.rollback();
				}
				catch (Throwable t) {
					t.printStackTrace();
				}
			}
			//
			output.put(DATA_NAME_STATUS, false);
			output.put(DATA_NAME_MESSAGE, "ERROR: Code=" + "ClassNotFoundException" + ", Message=" + e.getMessage() + ", " + dumpParamMap(parameters));
			e.printStackTrace();
		}
		finally {
			if (pStmtInsertOffer != null) {
				try {
					pStmtInsertOffer.close();
				}
				catch (Throwable t) {
					t.printStackTrace();
				}
			}
			if (pStmtInsertOfferField != null) {
				try {
					pStmtInsertOfferField.close();
				}
				catch (Throwable t) {
					t.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.setAutoCommit(true);
					con.close();
				}
				catch (Throwable t) {
					t.printStackTrace();
				}
			}
		}
		//
		if ((Boolean) output.get(DATA_NAME_STATUS)) {
			doCreateAllerts(offerID);
		}
		//
		return output;
	}


	public static void doCreateAllerts(String offerID) {
		Connection        con                       = null;
		PreparedStatement pStmtSelectAlertCriterion = null;
		PreparedStatement pStmtInsertAlert          = null;
		//
		List lstRows = new ArrayList();
		//
		try {
			con = getConnection();
			//
			pStmtSelectAlertCriterion = con.prepareStatement(SQL_OFFERALERTCRITERION_SELECT);
			//
			ResultSet rs = pStmtSelectAlertCriterion.executeQuery();
			while (rs.next()) {
				Object criterionID   = rs.getObject(1);
				Object buyer         = rs.getObject(1);
				Object criterionName = rs.getObject(1);
				Object triggerTxt    = rs.getObject(1);
				Object generateDate  = rs.getObject(1);
				//
				//
				List currentRow = new LinkedList();
				lstRows.add(currentRow);
				//
				currentRow.add(criterionID);
				currentRow.add(buyer);
				currentRow.add(criterionName);
				currentRow.add(triggerTxt);
				currentRow.add(generateDate);
			}
			rs.close();
			//
			for (Iterator iterator = lstRows.iterator(); iterator.hasNext(); ) {
				Object object = (Object) iterator.next();
				//
				//run the Insert allert
				//
				//
				if (pStmtInsertAlert == null) {
					pStmtInsertAlert = con.prepareStatement(SQL_ALERT_INSERT_BID);
				}
				pStmtInsertAlert.setString(1, getUUID());
				pStmtInsertAlert.setString(2, "");
				pStmtInsertAlert.setString(3, "");
				pStmtInsertAlert.execute();
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		finally {
			if (pStmtSelectAlertCriterion != null) {
				try {
					pStmtSelectAlertCriterion.close();
				}
				catch (Throwable t) {
					t.printStackTrace();
				}
			}
			if (pStmtInsertAlert != null) {
				try {
					pStmtInsertAlert.close();
				}
				catch (Throwable t) {
					t.printStackTrace();
				}
			}
			if (pStmtInsertAlert != null) {
				try {
					pStmtInsertAlert.close();
				}
				catch (Throwable t) {
					t.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				}
				catch (Throwable t) {
					t.printStackTrace();
				}
			}
		}
	}


	//Bid Alert cascading delete
	public static Map doCancelOffer(Map<String, String[]> parameters) {
		String offerid = getStringFromParamMap("offerid", parameters);
		//
		Map output = new HashMap();
		//
		Connection        con              = null;
		PreparedStatement pStmtCancelOffer = null;
		try {
			con = getConnection();
			//
			pStmtCancelOffer = con.prepareStatement(SQL_OFFER_CANCEL);
			pStmtCancelOffer.setString(1, offerid);
			//
			pStmtCancelOffer.execute();
			//
			int count = pStmtCancelOffer.getUpdateCount();
			if (count == 1) {
				output.put(DATA_NAME_STATUS, true);
				output.put(DATA_NAME_MESSAGE, "OK");
			}
			else {
				output.put(DATA_NAME_STATUS, false);
				output.put(DATA_NAME_MESSAGE, "Failed to cancel offer");
			}
		}
		catch (SQLException e) {
			output.put(DATA_NAME_STATUS, false);
			output.put(DATA_NAME_MESSAGE, "ERROR: ErrorCode=" + e.getErrorCode() + ", SQL_STATE=" + e.getSQLState() + ", Message=" + e.getMessage() + ", " + dumpParamMap(parameters));
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
			output.put(DATA_NAME_STATUS, false);
			output.put(DATA_NAME_MESSAGE, "ERROR: Code=" + "ClassNotFoundException" + ", Message=" + e.getMessage() + ", " + dumpParamMap(parameters));
			e.printStackTrace();
		}
		finally {
			if (pStmtCancelOffer != null) {
				try {
					pStmtCancelOffer.close();
				}
				catch (Throwable t) {
					t.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				}
				catch (Throwable t) {
					t.printStackTrace();
				}
			}
		}
		//
		return output;
	}


	public static Map doModifyOffer(Map<String, String[]> parameters) {
		Map output = new HashMap();
		//
		Connection        con                   = null;
		PreparedStatement pStmtModify           = null;
		PreparedStatement pStmtDeleteField      = null;
		PreparedStatement pStmtInsertOfferField = null;
		//
		String offerid = getStringFromParamMap("offerid", parameters);
		//
		try {
			con = getConnection();
			con.setAutoCommit(false);
			//
			pStmtModify = con.prepareStatement(SQL_OFFER_MODIFY);
			pStmtModify.setBigDecimal(1, getBigDecimalFromParamMap("minPrice", parameters));
			pStmtModify.setInt(2, getPrefixIntFromParamMap("conditionCode", parameters, '_'));
			pStmtModify.setString(3, getStringFromParamMap("description", parameters));
			pStmtModify.setString(4, offerid);
			//
			pStmtModify.execute();
			//
			int count = pStmtModify.getUpdateCount();
			if (count == 1) {
				pStmtDeleteField = con.prepareStatement(SQL_OFFERFIELD_DELETE);
				pStmtDeleteField.setString(1, offerid);
				//
				pStmtDeleteField.execute();
				//
				pStmtInsertOfferField = con.prepareStatement(SQL_OFFERFIELD_INSERT);
				//
				for (String s : parameters.keySet()) {
					if (s.startsWith("fieldID_")) {
						int    fieldID = Integer.parseInt(s.substring("fieldID_".length()));
						String value   = parameters.get(s)[0];
						//
						pStmtInsertOfferField.setString(1, offerid);
						pStmtInsertOfferField.setInt(2, fieldID);
						pStmtInsertOfferField.setString(3, value);
						pStmtInsertOfferField.execute();
					}
				}
				//
				output.put(DATA_NAME_STATUS, true);
				output.put(DATA_NAME_MESSAGE, "OK");
			}
			else {
				output.put(DATA_NAME_STATUS, false);
				output.put(DATA_NAME_MESSAGE, "Could not modify Offer. " + dumpParamMap(parameters));
			}
			//
			con.commit();
		}
		catch (SQLException e) {
			if (con != null) {
				try {
					con.rollback();
				}
				catch (Throwable t) {
					t.printStackTrace();
				}
			}
			//
			output.put(DATA_NAME_STATUS, false);
			output.put(DATA_NAME_MESSAGE, "ERROR: ErrorCode=" + e.getErrorCode() + ", SQL_STATE=" + e.getSQLState() + ", Message=" + e.getMessage() + ", " + dumpParamMap(parameters));
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
			if (con != null) {
				try {
					con.rollback();
				}
				catch (Throwable t) {
					t.printStackTrace();
				}
			}
			//
			output.put(DATA_NAME_STATUS, false);
			output.put(DATA_NAME_MESSAGE, "ERROR: Code=" + "ClassNotFoundException" + ", Message=" + e.getMessage() + ", " + dumpParamMap(parameters));
			e.printStackTrace();
		}
		finally {
			if (pStmtModify != null) {
				try {
					pStmtModify.close();
				}
				catch (Throwable t) {
					t.printStackTrace();
				}
			}
			if (pStmtDeleteField != null) {
				try {
					pStmtDeleteField.close();
				}
				catch (Throwable t) {
					t.printStackTrace();
				}
			}
			if (pStmtInsertOfferField != null) {
				try {
					pStmtInsertOfferField.close();
				}
				catch (Throwable t) {
					t.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.setAutoCommit(true);
					con.close();
				}
				catch (Throwable t) {
					t.printStackTrace();
				}
			}
		}
		//
		return output;
	}


	public static void main(String[] args) {
		Map<String, String[]> parameters = new HashMap<>();
		//
		parameters.put("categoryName1", new String[]{"car"});
		parameters.put("fieldop_6", new String[]{"yes"});
		parameters.put("lstFieldIDs", new String[]{"1,2,3,4,5,6,7"});
		//
		Map map = doSearchOffer(parameters);
		//
		System.out.println(DATA_NAME_STATUS + "= " + map.get(DATA_NAME_STATUS));
		System.out.println(DATA_NAME_MESSAGE + "= " + map.get(DATA_NAME_MESSAGE));
		System.out.println(DATA_NAME_USER_TYPE + "= " + map.get(DATA_NAME_USER_TYPE));
	}

	public static void main1(String[] args) {
		Map<String, String[]> parameters = new HashMap<>();
		//
		parameters.put("categoryName2", new String[]{"motorbike"});
		parameters.put("categoryName3", new String[]{"truck"});
		//
		System.out.println(DATA_NAME_STATUS + "= " + getListOfStringsFromParamMap("categoryName", 1, parameters, "'"));
		System.out.println(DATA_NAME_STATUS + "= " + getListOfStringsFromParamMap("categoryName", 1, parameters, ""));
		//
		Map map = doCancelOffer(parameters);
		//
		System.out.println(DATA_NAME_STATUS + "= " + map.get(DATA_NAME_STATUS));
		System.out.println(DATA_NAME_MESSAGE + "= " + map.get(DATA_NAME_MESSAGE));
		System.out.println(DATA_NAME_USER_TYPE + "= " + map.get(DATA_NAME_USER_TYPE));
	}
}


//
//Params:categoryName1=car,offerIDOP=any,offerIDVal=,sellerOP=any,sellerVal=,conditionCode_1=yes,conditionCode_2=yes,conditionCode_3=yes,conditionCode_4=yes,conditionCode_5=yes,conditionCode_6=yes,descriptionOP=any,descriptionVal=,priceOP=any,priceVal1=,priceVal2=,fieldop_1=any,fieldval1_1=,fieldop_2=any,fieldval1_2=,fieldop_3=any,fieldval1_3=,fieldval2_3=,fieldop_4=any,fieldval1_4=,fieldop_5=any,fieldval1_5=,fieldval2_5=,fieldop_6=yes,fieldop_7=any,fieldval1_7=,lstFieldIDs=1,2,3,4,5,6,7,action=searchOffer

