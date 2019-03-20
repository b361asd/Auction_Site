package rutgers.cs336.gui;

import rutgers.cs336.servlet.IConstant;

public class Helper implements IConstant {

	public static void main(String[] args) {
		
		
	}
	
	public static String getOPSZSelection(String name) {
		return SELECT_OP_SZ_TYPE.replaceAll("\\?", name);
	}
	
	public static String getOPIntSelection(String name) {
		return SELECT_OP_INT_TYPE.replaceAll("\\?", name);
	}

	public static String getOPBoolSelection(String name) {
		return SELECT_OP_BOOL_TYPE.replaceAll("\\?", name);
	}

	public static String getConditionCodeSelection(String name) {
		return SELECT_CONDITION_CODE.replaceAll("\\?", name);
	}	
	
}
