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
}