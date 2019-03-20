package rutgers.cs336.gui;

import rutgers.cs336.servlet.IConstant;

public class Helper implements IConstant {

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
}