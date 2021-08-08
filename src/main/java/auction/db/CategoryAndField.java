package auction.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CategoryAndField extends DBBase {

    public static final String DATA_CATEGORY_LIST = "DATA_CATEGORY_LIST";
    public static final String DATA_FIELD_LIST = "DATA_FIELD_LIST";

    /**
     * Get fields for a list of CategoryNames. Ex: "car,truck" will return all fields for them
     *
     * @param categoryNamesFromParam Category Names
     * @return Data for GUI rendering
     */
    public static Map getCategoryField(String categoryNamesFromParam) {
        Map output = new HashMap();
        //
        List<Field> lstField = new ArrayList<>();
        List<Category> lstCategory = new ArrayList<>();
        //
        output.put(DATA_CATEGORY_LIST, lstCategory);
        output.put(DATA_FIELD_LIST, lstField);
        //
        Connection con = null;
        Statement stmt = null;
        try {
            con = getConnection();
            //
            stmt = con.createStatement();
            //
            ResultSet rs = stmt.executeQuery(SQL_CATEGORYFIELD_SELECT);
            //
            Set<String> fieldIDSet = new HashSet<>();
            Set<String> categoryNameSet = new HashSet<>();
            //
            while (rs.next()) {
                Object categoryName = rs.getObject(1);
                Object fieldID = rs.getObject(2);
                Object fieldName = rs.getObject(3);
                Object fieldType = rs.getObject(4);
                //
                String sz_categoryName = categoryName.toString();
                int i_fieldID = (Integer) fieldID;
                String sz_fieldName = fieldName.toString();
                int i_fieldType = (Integer) fieldType;
                //
                // Default the first one
                if (categoryNamesFromParam.equals("")) {
                    categoryNamesFromParam = sz_categoryName;
                }
                //
                boolean isSelected =
                        ("," + categoryNamesFromParam + ",").contains("," + sz_categoryName + ",");
                //
                if (isSelected) {
                    if (!fieldIDSet.contains("" + i_fieldID)) {
                        lstField.add(new Field(i_fieldID, sz_fieldName, i_fieldType));
                        fieldIDSet.add("" + i_fieldID);
                    }
                }
                //
                if (!categoryNameSet.contains(sz_categoryName)) {
                    lstCategory.add(new Category(sz_categoryName, isSelected));
                    categoryNameSet.add(sz_categoryName);
                }
            }
            //
            output.put(DATA_NAME_STATUS, true);
            output.put(DATA_NAME_MESSAGE, "OK");
        } catch (SQLException e) {
            output.put(DATA_NAME_STATUS, false);
            output.put(
                    DATA_NAME_MESSAGE,
                    "ERROR: " + e.getErrorCode() + ", SQL_STATE: " + e.getSQLState());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            output.put(DATA_NAME_STATUS, false);
            output.put(
                    DATA_NAME_MESSAGE,
                    "ERROR: " + "ClassNotFoundException" + ", SQL_STATE: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
        //
        return output;
    }

    /**
     * Get Map
     *
     * @return Map of fieldId to fieldText
     */
    public static Map<String, String> getMapFieldIDToText() {
        Map<String, String> output = new HashMap<>();
        //
        Connection con = null;
        Statement stmt = null;
        try {
            con = getConnection();
            //
            stmt = con.createStatement();
            //
            ResultSet rs = stmt.executeQuery(ISQLConstant.SQL_FIELD_SELECT);
            //
            while (rs.next()) {
                Object fieldID = rs.getObject(1);
                Object fieldName = rs.getObject(2);
                rs.getObject(3);
                //
                output.put(fieldID.toString(), fieldName.toString());
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
        //
        return output;
    }

    public static void main(String[] args) {
        System.out.println("Start");
        //
        Map map1 = getMapFieldIDToText();
        //
        Map map = getCategoryField(null);
        //
        System.out.println(DATA_NAME_STATUS + "= " + map.get(DATA_NAME_STATUS));
        System.out.println(DATA_NAME_MESSAGE + "= " + map.get(DATA_NAME_MESSAGE));
        System.out.println(DATA_NAME_USER_TYPE + "= " + map.get(DATA_NAME_USER_TYPE));
    }

    public static class Category {
        String categoryName;
        boolean isCurr;

        //
        public Category(String categoryName, boolean isCurr) {
            this.categoryName = categoryName;
            this.isCurr = isCurr;
        }

        //
        public String getCategoryName() {
            return categoryName;
        }

        public boolean isCurr() {
            return isCurr;
        }
    }

    public static class Field {
        int fieldID;
        String fieldName;
        int fieldType;

        //
        public Field(int fieldID, String fieldName, int fieldType) {
            this.fieldID = fieldID;
            this.fieldName = fieldName;
            this.fieldType = fieldType;
        }

        //
        public int getFieldID() {
            return fieldID;
        }

        public String getFieldName() {
            return fieldName;
        }

        public int getFieldType() {
            return fieldType;
        }
    }
}
