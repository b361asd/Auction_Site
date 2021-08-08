package auction.servlet;

/** Constants for either Session or Map from DB to GUI. */
public interface IConstant {

    // DB output map
    String DATA_NAME_USER_TYPE = "DataNameUserType";
    String DATA_NAME_FIRST_NAME = "DataNameFirstName";
    String DATA_NAME_LAST_NAME = "DataNameLastName";
    //
    String DATA_NAME_STATUS = "DataNameStatus";
    String DATA_NAME_MESSAGE = "DataNameMessage";
    String DATA_NAME_DATA = "DataNameData";
    String DATA_NAME_DATA_ADD = "DataNameDataAdd";

    // Session
    String SESSION_ATTRIBUTE_MESSAGE = "message";
    //
    String SESSION_ATTRIBUTE_USERTYPE = "userType";
    String SESSION_ATTRIBUTE_USER = "user";
    String SESSION_ATTRIBUTE_USER_FNAME = "userFirstName";
    String SESSION_ATTRIBUTE_USER_LNAME = "userLastName";
    String SESSION_ATTRIBUTE_DATA_MAP = "DataMap";
}
