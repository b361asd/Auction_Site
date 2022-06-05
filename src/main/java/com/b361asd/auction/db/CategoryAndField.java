package com.b361asd.auction.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CategoryAndField extends DBBase {

    public record Category(String categoryName, boolean isCurr) {}

    public record Field(int fieldID, String fieldName, int fieldType) {}

    private static final Logger LOGGER = Logger.getLogger(CategoryAndField.class.getName());

    public static final String DATA_CATEGORY_LIST = "DATA_CATEGORY_LIST";
    public static final String DATA_FIELD_LIST = "DATA_FIELD_LIST";

    /**
     * Get fields for a list of CategoryNames. Ex: "car,truck" will return all fields for them
     *
     * @param categoryNamesFromParam Category Names
     * @return Data for GUI rendering
     */
    public static Map<String, Object> getCategoryField(String categoryNamesFromParam) {
        Map<String, Object> output = new HashMap<>();
        List<Field> lstField = new ArrayList<>();
        List<Category> lstCategory = new ArrayList<>();
        output.put(DATA_CATEGORY_LIST, lstCategory);
        output.put(DATA_FIELD_LIST, lstField);
        try (Connection con = getConnection();
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(SQL_CATEGORYFIELD_SELECT)) {
            Set<String> fieldIDSet = new HashSet<>();
            Set<String> categoryNameSet = new HashSet<>();
            while (rs.next()) {
                Object categoryName = rs.getObject(1);
                Object fieldID = rs.getObject(2);
                Object fieldName = rs.getObject(3);
                Object fieldType = rs.getObject(4);
                String sz_categoryName = categoryName.toString();
                int iFieldID = (Integer) fieldID;
                String szFieldName = fieldName.toString();
                int iFieldType = (Integer) fieldType;
                // Default the first one
                if (categoryNamesFromParam.equals("")) {
                    categoryNamesFromParam = sz_categoryName;
                }
                boolean isSelected =
                        ("," + categoryNamesFromParam + ",").contains("," + sz_categoryName + ",");
                if (isSelected && !fieldIDSet.contains("" + iFieldID)) {
                    lstField.add(new Field(iFieldID, szFieldName, iFieldType));
                    fieldIDSet.add("" + iFieldID);
                }
                if (!categoryNameSet.contains(sz_categoryName)) {
                    lstCategory.add(new Category(sz_categoryName, isSelected));
                    categoryNameSet.add(sz_categoryName);
                }
            }
            output.put(DATA_NAME_STATUS, true);
            output.put(DATA_NAME_MESSAGE, "OK");
        } catch (SQLException e) {
            output.put(DATA_NAME_STATUS, false);
            output.put(
                    DATA_NAME_MESSAGE,
                    MessageFormat.format(
                            "ERROR: {0}, SQL_STATE: {1}", e.getErrorCode(), e.getSQLState()));
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            output.put(DATA_NAME_STATUS, false);
            output.put(
                    DATA_NAME_MESSAGE,
                    MessageFormat.format(
                            "ERROR: ClassNotFoundException, SQL_STATE: {0}", e.getMessage()));
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        return output;
    }

    /**
     * Get Map
     *
     * @return Map of fieldId to fieldText
     */
    public static Map<String, String> getMapFieldIDToText() {
        Map<String, String> output = new HashMap<>();
        try (Connection con = getConnection();
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(ISQLConstant.SQL_FIELD_SELECT)) {
            while (rs.next()) {
                Object fieldID = rs.getObject(1);
                Object fieldName = rs.getObject(2);
                rs.getObject(3);
                output.put(fieldID.toString(), fieldName.toString());
            }
        } catch (SQLException | ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        return output;
    }
}
