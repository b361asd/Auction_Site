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

	// Session
	String SESSION_ATTRIBUTE_MESSAGE    = "message";
	//
	String SESSION_ATTRIBUTE_USERTYPE   = "userType";
	String SESSION_ATTRIBUTE_USER       = "user";
	String SESSION_ATTRIBUTE_USER_FNAME = "userFirstName";
	String SESSION_ATTRIBUTE_USER_LNAME = "userLastName";
	String SESSION_ATTRIBUTE_DATA_MAP 	= "DataMap";

	// GUI
	String SELECT_OP_SZ_TYPE = "<select name='?'><option value='any'>Any</option><option value='szequal'>Equal</option><option value='sznotequal'>Not Equal</option><option value='startwith'>Starts With</option><option value='contain'>Contains</option></select>";
	String SELECT_OP_INT_TYPE = "<select name='?'><option value='any'>Any</option><option value='intequal'>Equal</option><option value='intnotequal'>Not Equal</option><option value='equalorover'>Greater Than or Equal Too</option><option value='equalorunder'>Less Than or Equal Too</option><option value='between'>Between</option></select>";
	String SELECT_OP_BOOL_TYPE = "<select name='?'><option value='any'>Any</option><option value='true'>True</option><option value='false'>False</option></select>";
	String SELECT_CONDITION_CODE = "<select name='?'><option value='1'>New</option><option value='2'>Like New</option><option value='3'>Manufacturer Refurbished</option><option value='4'>Seller Refurbished</option><option value='5'>Used</option><option value='6'>For parts or Not Working</option></select>";
	
}

