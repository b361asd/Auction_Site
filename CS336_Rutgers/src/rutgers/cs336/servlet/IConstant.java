package rutgers.cs336.servlet;

public interface IConstant {
	// DB output map
	String DATA_NAME_USER_TYPE  = "DataNameUserType";
	String DATA_NAME_FIRST_NAME = "DataNameFirstName";
	String DATA_NAME_LAST_NAME  = "DataNameLastName";
	//
	String DATA_NAME_STATUS     = "DataNameStatus";
	String DATA_NAME_MESSAGE    = "DataNameMessage";
	//
	String DATA_NAME_DATA       = "DataNameData";
	String DATA_NAME_DATA_ADD   = "DataNameDataAdd";


	// Session
	String SESSION_ATTRIBUTE_MESSAGE    = "message";
	//
	String SESSION_ATTRIBUTE_USERTYPE   = "userType";
	String SESSION_ATTRIBUTE_USER       = "user";
	String SESSION_ATTRIBUTE_USER_FNAME = "userFirstName";
	String SESSION_ATTRIBUTE_USER_LNAME = "userLastName";
	String SESSION_ATTRIBUTE_DATA_MAP   = "DataMap";

	// GUI
	String SELECT_OP_SZ_TYPE       = "<select name='?'><option value='any'>Any</option><option value='szequal'>Equal</option><option value='sznotequal'>Not Equal</option><option value='startwith'>Starts With</option><option value='contain'>Contains</option></select>";
	String SELECT_OP_INT_TYPE      = "<select name='?'><option value='any'>Any</option><option value='intequal'>Equal</option><option value='intnotequal'>Not Equal</option><option value='equalorover'>Greater Than or Equal To</option><option value='equalorunder'>Less Than or Equal To</option><option value='between'>Between</option></select>";
	String SELECT_OP_BOOL_TYPE     = "<select name='?'><option value='any'>Any</option><option value='true'>True</option><option value='false'>False</option></select>";
	String CONDITION_CODE_CHECKBOX = "<div><input type='checkbox' id='new' name='?_1' value='yes' checked><label for='new'>New</label><input type='checkbox' id='likenew' name='?_2' value='yes' checked><label for='likenew'>Like New</label><input type='checkbox' id='manfrefurb' name='?_3'  value='yes' checked><label for='manfrefurb'>Manufacturer Refurbished</label><input type='checkbox' id='sellerrefurb' name='?_4' value='yes' checked><label for='sellerrefurb'>Seller Refurbished</label><input type='checkbox' id='used' name='?_5' value='yes' checked><label for='used'>Used</label><input type='checkbox' id='notwork' name='?_6' value='yes' checked><label for='notwork'>For parts or Not Working</label></div>";

}

