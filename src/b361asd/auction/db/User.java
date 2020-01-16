package b361asd.auction.db;

import b361asd.auction.gui.TableData;
import b361asd.auction.servlet.IConstant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class User extends DBBase {

   private static List  lstHeader_user = Arrays.asList("username", "password", "email", "firstname", "lastname", "address", "phone", "active");
   private static int[] colSeq_user    = {0, 1, 2, 3, 4, 5, 6, 7};

   /**
    * Select a list of Users
    *
    * @param parameters Map of all parameters
    * @param userType   2 for rep, 3 for user
    * @return Data for GUI rendering
    */
   public static Map selectUser(Map<String, String[]> parameters, int userType) {
      Map  output  = new HashMap();
      List lstRows = new ArrayList();
      //
      String in_username = parameters == null ? "" : getStringFromParamMap("username", parameters);
      //
      Connection        con          = null;
      PreparedStatement preparedStmt = null;
      try {
         con = getConnection();
         //
         preparedStmt = con.prepareStatement(in_username.length() != 0 ? SQL_USER_SELECT_ONE : SQL_USER_SELECT);
         //
         if (in_username.length() != 0) {
            preparedStmt.setString(1, in_username);
            preparedStmt.setInt(2, userType);
         }
         else {
            preparedStmt.setInt(1, userType);
         }
         //
         ResultSet rs = preparedStmt.executeQuery();
         //
         while (rs.next()) {
            Object username  = rs.getObject(1);
            Object password  = rs.getObject(2);
            Object email     = rs.getObject(3);
            Object firstname = rs.getObject(4);
            Object lastname  = rs.getObject(5);
            Object address   = rs.getObject(6);
            Object phone     = rs.getObject(7);
            Object active    = rs.getObject(8);
            //
            List currentRow = new LinkedList();
            lstRows.add(currentRow);
            //
            currentRow.add(username);
            currentRow.add(password);
            currentRow.add(email);
            currentRow.add(firstname);
            currentRow.add(lastname);
            currentRow.add(address);
            currentRow.add(phone);
            currentRow.add(active);
         }
         //
         TableData tableData = new TableData(lstHeader_user, lstRows, colSeq_user);
         //
         output.put(IConstant.DATA_NAME_DATA, tableData);
         //
         output.put(IConstant.DATA_NAME_STATUS, true);
         output.put(IConstant.DATA_NAME_MESSAGE, "OK");
      }
      catch (SQLException e) {
         output.put(IConstant.DATA_NAME_STATUS, false);
         output.put(IConstant.DATA_NAME_MESSAGE, "ERROR: " + e.getErrorCode() + ", SQL_STATE: " + e.getSQLState() + ", DETAILS: " + exceptionToString(e));
         e.printStackTrace();
      }
      catch (ClassNotFoundException e) {
         output.put(IConstant.DATA_NAME_STATUS, false);
         output.put(IConstant.DATA_NAME_MESSAGE, "ERROR: " + "ClassNotFoundException" + ", SQL_STATE: " + e.getMessage() + ", DETAILS: " + exceptionToString(e));
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


   /**
    * Get list of all active users
    *
    * @return List of active users
    */
   public static List getUserList() {
      List lst = new ArrayList();
      //
      Connection        con          = null;
      PreparedStatement preparedStmt = null;
      try {
         con = getConnection();
         //
         preparedStmt = con.prepareStatement(SQL_USER_SELECTUSERID);
         //
         ResultSet rs = preparedStmt.executeQuery();
         //
         while (rs.next()) {
            Object username = rs.getObject(1);
            //
            lst.add(username);
         }
      }
      catch (SQLException | ClassNotFoundException e) {
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
      return lst;
   }


   /**
    * Enable or disable either an user or rep.
    *
    * @param parameters Map of all parameters
    * @param isActivate True to activate, false to deactivate it
    * @return Data for GUI rendering
    */
   public static Map activateUser(Map<String, String[]> parameters, boolean isActivate) {
      Map output = new HashMap();
      //
      String username = getStringFromParamMap("username", parameters);
      //
      Connection        con          = null;
      PreparedStatement preparedStmt = null;
      try {
         con = getConnection();
         //
         preparedStmt = con.prepareStatement(isActivate ? SQL_USER_ACTIVE : SQL_USER_DEACTIVE);
         preparedStmt.setString(1, username);
         //
         preparedStmt.execute();
         //
         int count = preparedStmt.getUpdateCount();
         if (count == 1) {
            output.put(IConstant.DATA_NAME_STATUS, true);
            output.put(IConstant.DATA_NAME_MESSAGE, "OK");
         }
         else {
            output.put(IConstant.DATA_NAME_STATUS, false);
            output.put(IConstant.DATA_NAME_MESSAGE, "Failed to " + (isActivate ? "activate" : "deactivate") + " user");
         }
      }
      catch (SQLException e) {
         output.put(IConstant.DATA_NAME_STATUS, false);
         output.put(IConstant.DATA_NAME_MESSAGE, "ERROR: ErrorCode=" + e.getErrorCode() + ", SQL_STATE=" + e.getSQLState() + ", Message=" + e.getMessage() + ", " + dumpParamMap(parameters));
         e.printStackTrace();
      }
      catch (ClassNotFoundException e) {
         output.put(IConstant.DATA_NAME_STATUS, false);
         output.put(IConstant.DATA_NAME_MESSAGE, "ERROR: Code=" + "ClassNotFoundException" + ", Message=" + e.getMessage() + ", " + dumpParamMap(parameters));
         e.printStackTrace();
      }
      finally {
         if (preparedStmt != null) {
            try {
               preparedStmt.close();
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


   /**
    * Modify user information
    *
    * @param parameters Map of all parameters
    * @param userType   2 for user, 3 for rep
    * @return Data for GUI rendering
    */
   public static Map modifyUser(Map<String, String[]> parameters, int userType) {
      Map output = new HashMap();
      //
      Connection        con          = null;
      PreparedStatement preparedStmt = null;
      //
      String username  = getStringFromParamMap("username", parameters);
      String password  = getStringFromParamMap("password", parameters);
      String email     = getStringFromParamMap("email", parameters);
      String firstname = getStringFromParamMap("firstname", parameters);
      String lastname  = getStringFromParamMap("lastname", parameters);
      String address   = getStringFromParamMap("address", parameters);
      String phone     = getStringFromParamMap("phone", parameters);
      //
      try {
         con = getConnection();
         //
         preparedStmt = con.prepareStatement(SQL_USER_UPDATE);
         //
         preparedStmt.setString(1, password);
         preparedStmt.setString(2, email);
         preparedStmt.setString(3, firstname);
         preparedStmt.setString(4, lastname);
         preparedStmt.setString(5, address);
         preparedStmt.setString(6, phone);
         preparedStmt.setString(7, username);
         preparedStmt.setInt(8, userType);
         //
         preparedStmt.execute();
         //
         int count = preparedStmt.getUpdateCount();
         if (count == 1) {
            output.put(IConstant.DATA_NAME_STATUS, true);
            output.put(IConstant.DATA_NAME_MESSAGE, "OK");
         }
         else {
            output.put(IConstant.DATA_NAME_STATUS, false);
            output.put(IConstant.DATA_NAME_MESSAGE, "Could not update user");
         }
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
         output.put(IConstant.DATA_NAME_STATUS, false);
         output.put(IConstant.DATA_NAME_MESSAGE, "ERROR: ErrorCode=" + e.getErrorCode() + ", SQL_STATE=" + e.getSQLState() + ", Message=" + e.getMessage() + ", " + dumpParamMap(parameters));
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
         output.put(IConstant.DATA_NAME_STATUS, false);
         output.put(IConstant.DATA_NAME_MESSAGE, "ERROR: Code=" + "ClassNotFoundException" + ", Message=" + e.getMessage() + ", " + dumpParamMap(parameters));
         e.printStackTrace();
      }
      finally {
         if (preparedStmt != null) {
            try {
               preparedStmt.close();
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


   /**
    * Add an user
    *
    * @param username Username
    * @param password Password
    * @param email Email
    * @param firstName First name
    * @param lastName Last name
    * @param street Street
    * @param city City
    * @param state State
    * @param zipCode Zip code
    * @param phone Phone number
    * @param usertype User type
    * @return Data for GUI rendering
    */
   public static Map doAddUser(String username, String password, String email, String firstName, String lastName, String street, String city, String state, String zipCode, String phone, int usertype) {
      Map output = new HashMap();
      //
      if (email == null) {
         email = " ";
      }
      if (firstName == null) {
         firstName = " ";
      }
      if (lastName == null) {
         lastName = " ";
      }
      if (street == null) {
         street = " ";
      }
      if (city == null) {
         city = " ";
      }
      if (state == null) {
         state = " ";
      }
      if (phone == null) {
         phone = " ";
      }
      //
      if (username == null || username.trim().length() == 0) {
         output.put(IConstant.DATA_NAME_STATUS, false);
         output.put(IConstant.DATA_NAME_MESSAGE, "username is mandatory.");
      }
      else if (password == null || password.trim().length() == 0) {
         output.put(IConstant.DATA_NAME_STATUS, false);
         output.put(IConstant.DATA_NAME_MESSAGE, "password is mandatory.");
      }
      else {
         Connection        con          = null;
         PreparedStatement preparedStmt = null;
         try {
            con = getConnection();
            //
            preparedStmt = con.prepareStatement(SQL_USER_INSERT);
            //
            preparedStmt.setString(1, username);
            preparedStmt.setString(2, password);
            preparedStmt.setString(3, email);
            preparedStmt.setString(4, firstName);
            preparedStmt.setString(5, lastName);
            preparedStmt.setString(6, street + " " + city + " " + state + " " + zipCode);      // Address = street + city + state + zipCode
            preparedStmt.setString(7, phone);
            preparedStmt.setInt(8, usertype);
            preparedStmt.setString(9, username);
            //
            preparedStmt.execute();
            //
            int count = preparedStmt.getUpdateCount();
            if (count == 0) {
               output.put(IConstant.DATA_NAME_STATUS, false);
               output.put(IConstant.DATA_NAME_MESSAGE, "Could not register user.");
            }
            else {
               output.put(IConstant.DATA_NAME_STATUS, true);
               output.put(IConstant.DATA_NAME_MESSAGE, "User registered");
            }
         }
         catch (SQLException e) {
            output.put(IConstant.DATA_NAME_STATUS, false);
            output.put(IConstant.DATA_NAME_MESSAGE, "ERROR: " + e.getErrorCode() + ", SQL_STATE: " + e.getSQLState() + ", DETAILS: " + exceptionToString(e));
            e.printStackTrace();
         }
         catch (ClassNotFoundException e) {
            output.put(IConstant.DATA_NAME_STATUS, false);
            output.put(IConstant.DATA_NAME_MESSAGE, "ERROR: " + "ClassNotFoundException" + ", SQL_STATE: " + e.getMessage() + ", DETAILS: " + exceptionToString(e));
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
            if (con != null) {
               try {
                  con.close();
               }
               catch (Throwable e) {
                  e.printStackTrace();
               }
            }
         }
      }
      //
      return output;
   }


   /**
    * Authenticate and authorize an user
    *
    * @param userID User ID
    * @param pwd    Password
    * @return Data for GUI rendering
    */
   public static Map doVerifyLogin(String userID, String pwd) {
      Map output = new HashMap();
      //
      Connection        con          = null;
      PreparedStatement preparedStmt = null;
      try {
         con = getConnection();
         //
         preparedStmt = con.prepareStatement(SQL_USER_AUTH);
         //
         preparedStmt.setString(1, userID);
         //
         ResultSet rs = preparedStmt.executeQuery();
         //
         if (rs.next()) {                                 // Only 1 row
            Object password  = rs.getObject(1);
            Object firstname = rs.getObject(2);
            Object lastname  = rs.getObject(3);
            Object active    = rs.getObject(4);
            Object usertype  = rs.getObject(5);
            //
            if (!pwd.equals(password)) {
               output.put(IConstant.DATA_NAME_STATUS, false);
               output.put(IConstant.DATA_NAME_MESSAGE, "Wrong Password. Please try again.");
            }
            else if (!(Boolean) active) {
               output.put(IConstant.DATA_NAME_STATUS, false);
               output.put(IConstant.DATA_NAME_MESSAGE, "User not active. Contact a customer representative.");
            }
            else {
               output.put(IConstant.DATA_NAME_STATUS, true);
               output.put(IConstant.DATA_NAME_MESSAGE, "Welcome, " + firstname.toString() + " " + lastname.toString() + "!");
               //
               output.put(IConstant.DATA_NAME_USER_TYPE, usertype);
               output.put(IConstant.DATA_NAME_FIRST_NAME, firstname);
               output.put(IConstant.DATA_NAME_LAST_NAME, lastname);
            }
         }
         else {
            output.put(IConstant.DATA_NAME_STATUS, false);
            output.put(IConstant.DATA_NAME_MESSAGE, "User does not exist. Try another user or register");
         }
      }
      catch (SQLException e) {
         output.put(IConstant.DATA_NAME_STATUS, false);
         output.put(IConstant.DATA_NAME_MESSAGE, "ERROR: " + e.getErrorCode() + ", SQL_STATE: " + e.getSQLState() + ", DETAILS: " + exceptionToString(e));
         e.printStackTrace();
      }
      catch (ClassNotFoundException e) {
         output.put(IConstant.DATA_NAME_STATUS, false);
         output.put(IConstant.DATA_NAME_MESSAGE, "ERROR: " + "ClassNotFoundException" + ", SQL_STATE: " + e.getMessage() + ", DETAILS: " + exceptionToString(e));
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


   public static void main(String[] args) {
      Map<String, String[]> parameters = new HashMap<>();
      //
      parameters.put("username", new String[]{"user"});
      parameters.put("password", new String[]{"user_pwd"});
      parameters.put("email", new String[]{"user@buyme.com"});
      parameters.put("firstname", new String[]{"Real"});
      parameters.put("lastname", new String[]{"Lnuser"});
      parameters.put("address", new String[]{"123 Main St., Nowhere Town, NJ 56789"});
      parameters.put("phone", new String[]{"2365678909"});
      //
      Map map = modifyUser(parameters, 3);
      //
      System.out.println(IConstant.DATA_NAME_STATUS + "= " + map.get(IConstant.DATA_NAME_STATUS));
      System.out.println(IConstant.DATA_NAME_MESSAGE + "= " + map.get(IConstant.DATA_NAME_MESSAGE));
      System.out.println(IConstant.DATA_NAME_USER_TYPE + "= " + map.get(IConstant.DATA_NAME_USER_TYPE));
   }

   public static void main2(String[] args) {
      Map map = doAddUser("abc", "123", "email", "fN", "lN", "123 St", "Pearl", "NJ", "01010", "39239033", 3);
      //
      System.out.println(IConstant.DATA_NAME_STATUS + "= " + map.get(IConstant.DATA_NAME_STATUS));
      System.out.println(IConstant.DATA_NAME_MESSAGE + "= " + map.get(IConstant.DATA_NAME_MESSAGE));
      System.out.println(IConstant.DATA_NAME_USER_TYPE + "= " + map.get(IConstant.DATA_NAME_USER_TYPE));
   }

   public static void main1(String[] args) {
      Map map = doVerifyLogin("user", "user_pwd");
      //
      System.out.println(IConstant.DATA_NAME_STATUS + "= " + map.get(IConstant.DATA_NAME_STATUS));
      System.out.println(IConstant.DATA_NAME_MESSAGE + "= " + map.get(IConstant.DATA_NAME_MESSAGE));
      System.out.println(IConstant.DATA_NAME_USER_TYPE + "= " + map.get(IConstant.DATA_NAME_USER_TYPE));
   }
}