package rutgers.cs336.gui;

import rutgers.cs336.db.CategoryAndField;
import rutgers.cs336.servlet.IConstant;

import java.util.List;

public class Helper implements IConstant {
	// GUI
	private static final String SELECT_OP_SZ_TYPE       = "<select name='?'><option value='any'>Any</option><option value='szequal'>Equal</option><option value='sznotequal'>Not Equal</option><option value='startwith'>Starts With</option><option value='contain'>Contains</option></select>";
	private static final String SELECT_OP_INT_TYPE      = "<select name='?'><option value='any'>Any</option><option value='intequal'>Equal</option><option value='intnotequal'>Not Equal</option><option value='equalorover'>Greater Than or Equal To</option><option value='equalorunder'>Less Than or Equal To</option><option value='between'>Between</option></select>";
	private static final String SELECT_OP_BOOL_TYPE     = "<select name='?'><option value='any'>Any</option><option value='true'>True</option><option value='false'>False</option></select>";
	private static final String CONDITION_CODE_CHECKBOX = "<div><input type='checkbox' id='new' name='?_1' value='yes' checked><label for='new'>New</label><input type='checkbox' id='likenew' name='?_2' value='yes' checked><label for='likenew'>Like New</label><input type='checkbox' id='manfrefurb' name='?_3'  value='yes' checked><label for='manfrefurb'>Manufacturer Refurbished</label><input type='checkbox' id='sellerrefurb' name='?_4' value='yes' checked><label for='sellerrefurb'>Seller Refurbished</label><input type='checkbox' id='used' name='?_5' value='yes' checked><label for='used'>Used</label><input type='checkbox' id='notwork' name='?_6' value='yes' checked><label for='notwork'>For parts or Not Working</label></div>";

	public static String getOPSZSelection(String name) {
		return SELECT_OP_SZ_TYPE.replaceAll("\\?", name);
	}

	public static String getOPIntSelection(String name) {
		return SELECT_OP_INT_TYPE.replaceAll("\\?", name);
	}

	public static String getOPBoolSelection(String name) {
		return SELECT_OP_BOOL_TYPE.replaceAll("\\?", name);
	}

	public static String getConditionCodeCheckBox(String name) {
		return CONDITION_CODE_CHECKBOX.replaceAll("\\?", name);
	}

	public static String getCategoryNameCheckBox(String name, List lstCategoryName) {
		StringBuilder sb = new StringBuilder();
		//
		sb.append("<div>");
		for (int i = 0; i < lstCategoryName.size(); i++) {
			CategoryAndField.Category one = (CategoryAndField.Category) (lstCategoryName.get(i));
			sb.append("<input onchange='onCategoryChange();' type='checkbox' id='");
			sb.append(one.getCategoryName());
			sb.append("' name='");
			sb.append(name).append(i + 1);
			sb.append("' value='");
			sb.append(one.getCategoryName());
			if (one.isCurr()) {
				sb.append("' checked><label for='");
			}
			else {
				sb.append("'><label for='");
			}
			sb.append(one.getCategoryName());
			sb.append("'>");
			sb.append(one.getCategoryName());
			sb.append("</label>");
		}
		sb.append("</div>");
		//
		return sb.toString();
	}


	public static String getConditionFromCode(String code) {
		switch (code) {
			case "1":
				return "New";
			case "2":
				return "Like New";
			case "3":
				return "Manufacturer Refurbished";
			case "4":
				return "Seller Refurbished";
			case "5":
				return "Used";
			case "6":
				return "For parts or Not Working";
			default:
				return "Unknown.";
		}
	}

	public static String getCodeFromCondition(String condition) {
		switch (condition) {
			case "New":
				return "1";
			case "Like New":
				return "2";
			case "Manufacturer Refurbished":
				return "3";
			case "Seller Refurbished":
				return "4";
			case "Used":
				return "5";
			case "For parts or Not Working":
				return "6";
			default:
				return "9";
		}
	}


	public static String _printHeaderForTable(List row, int[] colSeq) {
		String out = "";
		if (row != null && row.size() > 0 && colSeq != null && colSeq.length > 0) {
			for (int value : colSeq) {
				Object one     = row.get(value);
				String oneItem = (one == null) ? "" : one.toString();
				out = out + "<th><div onclick=onClickHeader('" + oneItem + "')>" + oneItem + "</div></th>";
			}
		}
		return out;
	}


	public static String _printOneRowInTable(List row, int[] colSeq) {
		String out = "";
		if (row != null && row.size() > 0 && colSeq != null && colSeq.length > 0) {
			for (int value : colSeq) {
				Object one     = row.get(value);
				String oneItem = (one == null) ? "" : one.toString();
				out = out + "<td>" + oneItem + "</td>";
			}
		}
		return out;
	}
}