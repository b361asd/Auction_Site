package com.b361asd.auction.gui;

import com.b361asd.auction.db.CategoryAndField;
import com.b361asd.auction.servlet.IConstant;
import java.util.List;
import java.util.Map;

/** Helper functions */
public class Helper implements IConstant {

    private static final String[] S_CONDITIONS = {
        "New",
        "Like New",
        "Manufacturer Refurbished",
        "Seller Refurbished",
        "Used",
        "For parts or Not Working"
    };

    private static final String SELECT_OP_SZ_TYPE =
            "<select name='?'><option value='any'>Any</option><option "
                    + "value='szequal'>Equal</option><option value='sznotequal'>Not Equal</option><option "
                    + "value='startwith'>Starts With</option><option "
                    + "value='contain'>Contains</option></select>";

    private static final String SELECT_OP_INT_TYPE =
            "<select name='?'><option value='any'>Any</option><option "
                    + "value='intequal'>Equal</option><option value='intnotequal'>Not Equal</option><option"
                    + " value='equalorover'>Greater Than or Equal To</option><option "
                    + "value='equalorunder'>Less Than or Equal To</option><option "
                    + "value='between'>Between</option></select>";

    private static final String SELECT_OP_BOOL_TYPE =
            "<select name='?'><option value='any'>Any</option><option "
                    + "value='yes'>YES</option><option value='no'>NO</option></select>";

    private static final String CONDITION_CODE_CHECKBOX =
            "<div><input type='checkbox' id='new' name='?_1' value='yes' checked><label "
                    + "for='new'>New</label><input type='checkbox' id='likenew' name='?_2' value='yes'"
                    + " checked><label for='likenew'>Like New</label><input type='checkbox' "
                    + "id='manfrefurb' name='?_3'  value='yes' checked><label "
                    + "for='manfrefurb'>Manufacturer Refurbished</label><input type='checkbox' "
                    + "id='sellerrefurb' name='?_4' value='yes' checked><label "
                    + "for='sellerrefurb'>Seller Refurbished</label><input type='checkbox' id='used' "
                    + "name='?_5' value='yes' checked><label for='used'>Used</label><input "
                    + "type='checkbox' id='notwork' name='?_6' value='yes' checked><label "
                    + "for='notwork'>For parts or Not Working</label></div>";

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

    public static String getCategoryNameCheckBox(String name, List lstCategoryName) {
        StringBuilder sb = new StringBuilder();
        sb.append("<div>");
        for (int i = 0; i < lstCategoryName.size(); i++) {
            CategoryAndField.Category one = (CategoryAndField.Category) (lstCategoryName.get(i));
            sb.append("<input onchange='onCategoryChange();' type='checkbox' id='")
                    .append(one.categoryName())
                    .append("' name='")
                    .append(name)
                    .append(i + 1)
                    .append("' value='")
                    .append(one.categoryName());
            if (one.isCurr()) {
                sb.append("' checked><label for='");
            } else {
                sb.append("'><label for='");
            }
            sb.append(one.categoryName())
                    .append("'>")
                    .append(one.categoryName())
                    .append("</label>");
        }
        sb.append("</div>");
        return sb.toString();
    }

    public static String getConditionCodeSelection(String name, String selected) {
        StringBuilder sb = new StringBuilder();
        sb.append("<select name='").append(name).append("'>");
        for (int i = 0; i < S_CONDITIONS.length; i++) {
            if (S_CONDITIONS[i].equalsIgnoreCase(selected)) {
                sb.append("<option value='")
                        .append(i + 1)
                        .append("' selected>")
                        .append(S_CONDITIONS[i])
                        .append("</option>");
            } else {
                sb.append("<option value='")
                        .append(i + 1)
                        .append("'>")
                        .append(S_CONDITIONS[i])
                        .append("</option>");
            }
        }
        sb.append("</select>");
        return sb.toString();
    }

    public static String getSelection(String name, Object[] options, String selected) {
        StringBuilder sb = new StringBuilder();
        sb.append("<select name='").append(name).append("' onchange='onSelectChange();'>");
        for (Object option : options) {
            if (option.toString().equalsIgnoreCase(selected)) {
                sb.append("<option value='")
                        .append(option)
                        .append("' selected>")
                        .append(option)
                        .append("</option>");
            } else {
                sb.append("<option value='")
                        .append(option)
                        .append("'>")
                        .append(option)
                        .append("</option>");
            }
        }
        sb.append("</select>");
        return sb.toString();
    }

    public static String getYesNoSelection(String name, String selected) {
        boolean isYes = selected.equalsIgnoreCase("yes"); // IN DB: 'no' 'yes'
        return "<select name='"
                + name
                + "'>"
                + "<option value='yes'"
                + (isYes ? " selected" : "")
                + ">YES</option>"
                + "<option value='no'"
                + (isYes ? "" : " selected")
                + ">NO</option>"
                + "</select>";
    }

    public static String getConditionFromCode(String code) {
        return switch (code) {
            case "1" -> S_CONDITIONS[0];
            case "2" -> S_CONDITIONS[1];
            case "3" -> S_CONDITIONS[2];
            case "4" -> S_CONDITIONS[3];
            case "5" -> S_CONDITIONS[4];
            case "6" -> S_CONDITIONS[5];
            default -> "Unknown";
        };
    }

    public static String getCodeFromCondition(String condition) {
        return switch (condition) {
            case "New" -> "1";
            case "Like New" -> "2";
            case "Manufacturer Refurbished" -> "3";
            case "Seller Refurbished" -> "4";
            case "Used" -> "5";
            case "For parts or Not Working" -> "6";
            default -> "9";
        };
    }

    public static String getStatusFromCode(String status) {
        return switch (status) {
            case "1" -> "Active";
            case "3" -> "Completed";
            case "4" -> "No Bid";
            case "5" -> "Min Not Met";
            default -> "Unknown";
        };
    }

    public static String getButton(
            String formID, String inputID, String inputValue, String display) {
        return "<button onclick=\"document.getElementById('"
                + inputID
                + "').value='"
                + inputValue
                + "'; document.getElementById('"
                + formID
                + "').submit();\" class=\"buttonclass\" type=\"button\">"
                + display
                + "</button>";
    }

    public static String escapeHTML(String s) {
        StringBuilder out = new StringBuilder(Math.max(16, s.length()));
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c > 127 || c == '"' || c == '<' || c == '>' || c == '&') {
                out.append("&#").append((int) c).append(';');
            } else {
                out.append(c);
            }
        }
        return out.toString();
    }

    public static void setStatus(Map<String, Object> data, boolean status) {
        data.put(DATA_NAME_STATUS, status);
    }

    public static void appendMessage(Map<String, Object> data, String message) {
        data.put(DATA_NAME_MESSAGE, getMessage(data) + " " + message);
    }

    public static void setData(Map<String, Object> data, Object payload) {
        data.put(DATA_NAME_DATA, payload);
    }

    public static boolean getStatus(Map<String, Object> data) {
        return (Boolean) data.get(DATA_NAME_STATUS);
    }

    public static String getMessage(Map<String, Object> data) {
        return (String) data.get(DATA_NAME_MESSAGE);
    }

    public static Object getData(Map data) {
        return data.get(DATA_NAME_DATA);
    }
}
