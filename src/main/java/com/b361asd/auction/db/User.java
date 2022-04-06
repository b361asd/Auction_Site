package com.b361asd.auction.db;

import com.b361asd.auction.gui.TableData;
import com.b361asd.auction.servlet.IConstant;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class User extends DBBase {

    private static final List<String> lstHeader_user =
            Arrays.asList(
                    "username",
                    "password",
                    "email",
                    "firstname",
                    "lastname",
                    "address",
                    "phone",
                    "active");

    private static final int[] colSeq_user = {0, 1, 2, 3, 4, 5, 6, 7};

    /**
     * Select a list of Users
     *
     * @param parameters Map of all parameters
     * @param userType See {@link com.b361asd.auction.gui.UserType}
     * @return Data for GUI rendering
     */
    public static Map<String, Object> selectUser(Map<String, String[]> parameters, int userType) {
        Map<String, Object> output = new HashMap<>();
        List<Object> lstRows = new ArrayList<>();
        String in_username =
                Optional.ofNullable(parameters)
                        .map(stringMap -> getStringFromParamMap("username", stringMap))
                        .orElse("");
        try (Connection con = getConnection();
                PreparedStatement preparedStmt =
                        con.prepareStatement(
                                in_username.length() != 0
                                        ? SQL_USER_SELECT_ONE
                                        : SQL_USER_SELECT)) {
            if (in_username.length() != 0) {
                preparedStmt.setString(1, in_username);
                preparedStmt.setInt(2, userType);
            } else {
                preparedStmt.setInt(1, userType);
            }
            try (ResultSet rs = preparedStmt.executeQuery()) {
                while (rs.next()) {
                    Object username = rs.getObject(1);
                    Object password = rs.getObject(2);
                    Object email = rs.getObject(3);
                    Object firstname = rs.getObject(4);
                    Object lastname = rs.getObject(5);
                    Object address = rs.getObject(6);
                    Object phone = rs.getObject(7);
                    Object active = rs.getObject(8);

                    List<Object> currentRow = new LinkedList<>();
                    lstRows.add(currentRow);
                    currentRow.add(username);
                    currentRow.add(password);
                    currentRow.add(email);
                    currentRow.add(firstname);
                    currentRow.add(lastname);
                    currentRow.add(address);
                    currentRow.add(phone);
                    currentRow.add(active);
                }
            }
            TableData tableData = new TableData(lstHeader_user, lstRows, colSeq_user);
            output.put(IConstant.DATA_NAME_DATA, tableData);
            output.put(IConstant.DATA_NAME_STATUS, true);
            output.put(IConstant.DATA_NAME_MESSAGE, "OK");
        } catch (SQLException e) {
            output.put(IConstant.DATA_NAME_STATUS, false);
            output.put(
                    IConstant.DATA_NAME_MESSAGE,
                    MessageFormat.format(
                            "ERROR: {0}, SQL_STATE: {1}, DETAILS: {2}",
                            e.getErrorCode(), e.getSQLState(), exceptionToString(e)));
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            output.put(IConstant.DATA_NAME_STATUS, false);
            output.put(
                    IConstant.DATA_NAME_MESSAGE,
                    MessageFormat.format(
                            "ERROR: ClassNotFoundException, SQL_STATE: {0}, DETAILS: {1}",
                            e.getMessage(), exceptionToString(e)));
            e.printStackTrace();
        }
        return output;
    }

    /**
     * Get list of all active users
     *
     * @return List of active users
     */
    public static List<Object> getUserList() {
        List<Object> lst = new ArrayList<>();
        try (Connection con = getConnection();
                PreparedStatement preparedStmt = con.prepareStatement(SQL_USER_SELECT_USERID);
                ResultSet rs = preparedStmt.executeQuery()) {
            while (rs.next()) {
                Object username = rs.getObject(1);
                lst.add(username);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return lst;
    }

    /**
     * Enable or disable either a user or rep.
     *
     * @param parameters Map of all parameters
     * @param isActivate True to activate, false to deactivate it
     */
    public static void activateUser(Map<String, String[]> parameters, boolean isActivate) {
        Map<String, Object> output = new HashMap<>();
        String username = getStringFromParamMap("username", parameters);
        try (Connection con = getConnection();
                PreparedStatement preparedStmt =
                        con.prepareStatement(isActivate ? SQL_USER_ACTIVE : SQL_USER_INACTIVE)) {
            preparedStmt.setString(1, username);
            preparedStmt.execute();
            int count = preparedStmt.getUpdateCount();
            if (count == 1) {
                output.put(IConstant.DATA_NAME_STATUS, true);
                output.put(IConstant.DATA_NAME_MESSAGE, "OK");
            } else {
                output.put(IConstant.DATA_NAME_STATUS, false);
                output.put(
                        IConstant.DATA_NAME_MESSAGE,
                        MessageFormat.format(
                                "Failed to {0} user", isActivate ? "activate" : "deactivate"));
            }
        } catch (SQLException e) {
            output.put(IConstant.DATA_NAME_STATUS, false);
            output.put(
                    IConstant.DATA_NAME_MESSAGE,
                    MessageFormat.format(
                            "ERROR: ErrorCode={0}, SQL_STATE={1}, Message={2}, {3}",
                            e.getErrorCode(),
                            e.getSQLState(),
                            e.getMessage(),
                            dumpParamMap(parameters)));
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            output.put(IConstant.DATA_NAME_STATUS, false);
            output.put(
                    IConstant.DATA_NAME_MESSAGE,
                    MessageFormat.format(
                            "ERROR: Code=ClassNotFoundException, Message={0}, {1}",
                            e.getMessage(), dumpParamMap(parameters)));
            e.printStackTrace();
        }
    }

    /**
     * Modify user information
     *
     * @param parameters Map of all parameters
     * @param userType 2 for user, 3 for rep
     * @return Data for GUI rendering
     */
    public static Map<String, Object> modifyUser(Map<String, String[]> parameters, int userType) {
        Map<String, Object> output = new HashMap<>();

        String username = getStringFromParamMap("username", parameters);
        String password = getStringFromParamMap("password", parameters);
        String email = getStringFromParamMap("email", parameters);
        String firstname = getStringFromParamMap("firstname", parameters);
        String lastname = getStringFromParamMap("lastname", parameters);
        String address = getStringFromParamMap("address", parameters);
        String phone = getStringFromParamMap("phone", parameters);

        try (Connection con = getConnection()) {
            int count = 0;
            // https://stackoverflow.com/a/9260565
            try (PreparedStatement preparedStmt = con.prepareStatement(SQL_USER_UPDATE)) {
                preparedStmt.setString(1, password);
                preparedStmt.setString(2, email);
                preparedStmt.setString(3, firstname);
                preparedStmt.setString(4, lastname);
                preparedStmt.setString(5, address);
                preparedStmt.setString(6, phone);
                preparedStmt.setString(7, username);
                preparedStmt.setInt(8, userType);

                preparedStmt.execute();
                count = preparedStmt.getUpdateCount();
            } catch (SQLException e) {
                con.rollback();
            }
            if (count == 1) {
                output.put(IConstant.DATA_NAME_STATUS, true);
                output.put(IConstant.DATA_NAME_MESSAGE, "OK");
            } else {
                output.put(IConstant.DATA_NAME_STATUS, false);
                output.put(IConstant.DATA_NAME_MESSAGE, "Could not update user");
            }
        } catch (SQLException e) {
            output.put(IConstant.DATA_NAME_STATUS, false);
            output.put(
                    IConstant.DATA_NAME_MESSAGE,
                    MessageFormat.format(
                            "ERROR: ErrorCode={0}, SQL_STATE={1}, Message={2}, {3}",
                            e.getErrorCode(),
                            e.getSQLState(),
                            e.getMessage(),
                            dumpParamMap(parameters)));
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            output.put(IConstant.DATA_NAME_STATUS, false);
            output.put(
                    IConstant.DATA_NAME_MESSAGE,
                    MessageFormat.format(
                            "ERROR: Code=ClassNotFoundException, Message={0}, {1}",
                            e.getMessage(), dumpParamMap(parameters)));
            e.printStackTrace();
        }
        return output;
    }

    /**
     * Add a user
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
    public static Map<String, Object> doAddUser(
            String username,
            String password,
            String email,
            String firstName,
            String lastName,
            String street,
            String city,
            String state,
            String zipCode,
            String phone,
            int usertype) {
        Map<String, Object> output = new HashMap<>();
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);
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
        if (username.trim().length() == 0) {
            output.put(IConstant.DATA_NAME_STATUS, false);
            output.put(IConstant.DATA_NAME_MESSAGE, "username is mandatory.");
        } else if (password.trim().length() == 0) {
            output.put(IConstant.DATA_NAME_STATUS, false);
            output.put(IConstant.DATA_NAME_MESSAGE, "password is mandatory.");
        } else {
            try (Connection con = getConnection();
                    PreparedStatement preparedStmt = con.prepareStatement(SQL_USER_INSERT)) {
                preparedStmt.setString(1, username);
                preparedStmt.setString(2, password);
                preparedStmt.setString(3, email);
                preparedStmt.setString(4, firstName);
                preparedStmt.setString(5, lastName);

                // Address = street + city + state + zipCode
                preparedStmt.setString(6, street + " " + city + " " + state + " " + zipCode);

                preparedStmt.setString(7, phone);
                preparedStmt.setInt(8, usertype);
                preparedStmt.setString(9, username);

                preparedStmt.execute();
                int count = preparedStmt.getUpdateCount();
                if (count == 0) {
                    output.put(IConstant.DATA_NAME_STATUS, false);
                    output.put(IConstant.DATA_NAME_MESSAGE, "Could not register user.");
                } else {
                    output.put(IConstant.DATA_NAME_STATUS, true);
                    output.put(IConstant.DATA_NAME_MESSAGE, "User registered");
                }
            } catch (SQLException e) {
                output.put(IConstant.DATA_NAME_STATUS, false);
                output.put(
                        IConstant.DATA_NAME_MESSAGE,
                        MessageFormat.format(
                                "ERROR: {0}, SQL_STATE: {1}, DETAILS: {2}",
                                e.getErrorCode(), e.getSQLState(), exceptionToString(e)));
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                output.put(IConstant.DATA_NAME_STATUS, false);
                output.put(
                        IConstant.DATA_NAME_MESSAGE,
                        MessageFormat.format(
                                "ERROR: ClassNotFoundException, SQL_STATE: {0}, DETAILS: {1}",
                                e.getMessage(), exceptionToString(e)));
                e.printStackTrace();
            }
        }
        return output;
    }

    /**
     * Authenticate and authorize a user
     *
     * @param userID User ID
     * @param pwd Password
     * @return Data for GUI rendering
     */
    public static Map<String, Object> doVerifyLogin(String userID, String pwd) {
        Map<String, Object> output = new HashMap<>();
        try (Connection con = getConnection();
                PreparedStatement preparedStmt = con.prepareStatement(SQL_USER_AUTH)) {
            preparedStmt.setString(1, userID);
            try (ResultSet rs = preparedStmt.executeQuery()) {
                if (rs.next()) {
                    // Only 1 row
                    Object password = rs.getObject(1);
                    Object firstname = rs.getObject(2);
                    Object lastname = rs.getObject(3);
                    Object active = rs.getObject(4);
                    Object usertype = rs.getObject(5);
                    if (!pwd.equals(password)) {
                        output.put(IConstant.DATA_NAME_STATUS, false);
                        output.put(
                                IConstant.DATA_NAME_MESSAGE, "Wrong Password. Please try again.");
                    } else if (!(Boolean) active) {
                        output.put(IConstant.DATA_NAME_STATUS, false);
                        output.put(
                                IConstant.DATA_NAME_MESSAGE,
                                "User not active. Contact a customer representative.");
                    } else {
                        output.put(IConstant.DATA_NAME_STATUS, true);
                        output.put(
                                IConstant.DATA_NAME_MESSAGE,
                                MessageFormat.format(
                                        "Welcome, {0} {1}!",
                                        firstname.toString(), lastname.toString()));
                        output.put(IConstant.DATA_NAME_USER_TYPE, usertype);
                        output.put(IConstant.DATA_NAME_FIRST_NAME, firstname);
                        output.put(IConstant.DATA_NAME_LAST_NAME, lastname);
                    }
                } else {
                    output.put(IConstant.DATA_NAME_STATUS, false);
                    output.put(
                            IConstant.DATA_NAME_MESSAGE,
                            "User does not exist. Try another user or register");
                }
            }
        } catch (SQLException e) {
            output.put(IConstant.DATA_NAME_STATUS, false);
            output.put(
                    IConstant.DATA_NAME_MESSAGE,
                    MessageFormat.format(
                            "ERROR: {0}, SQL_STATE: {1}, DETAILS: {2}",
                            e.getErrorCode(), e.getSQLState(), exceptionToString(e)));
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            output.put(IConstant.DATA_NAME_STATUS, false);
            output.put(
                    IConstant.DATA_NAME_MESSAGE,
                    MessageFormat.format(
                            "ERROR: ClassNotFoundException, SQL_STATE: {0}, DETAILS: {1}",
                            e.getMessage(), exceptionToString(e)));
            e.printStackTrace();
        }
        return output;
    }
}
