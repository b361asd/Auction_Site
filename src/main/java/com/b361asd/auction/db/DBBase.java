package com.b361asd.auction.db;

import com.b361asd.auction.gui.Helper;
import com.b361asd.auction.servlet.IConstant;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Tools and utilities for handling database calls */
public class DBBase extends Utils implements ISQLConstant, IConstant {
    private static final int MAX_CATEGORY_COUNT = 16;

    private static final HashMap<String, String> sqlTokens;
    private static final String OP_ANY = "any";

    public static final String OP_SZ_EQUAL = "szequal";
    public static final String OP_SZ_EQUAL_MULTI_NO_ESCAPE = "szequalmultine";
    private static final String OP_SZ_NOT_EQUAL = "sznotequal";
    private static final String OP_SZ_START_WITH = "startwith";
    private static final String OP_SZ_CONTAIN = "contain";

    public static final String OP_INT_EQUAL = "intequal";
    public static final String OP_INT_EQUAL_MULTI = "intequalmulti";
    private static final String OP_INT_NOT_EQUAL = "intnotequal";
    private static final String OP_INT_EQUAL_OR_OVER = "equalorover";
    private static final String OP_INT_EQUAL_OR_UNDER = "equalorunder";
    private static final String OP_INT_BETWEEN = "between";

    private static final String OP_BOOL_TRUE = "yes";
    private static final String OP_BOOL_FALSE = "no";
    private static final Pattern sqlTokenPattern;

    static {
        // MySQL escape sequences: https://dev.mysql.com/doc/refman/8.0/en/string-literals.html
        String[][] searchRegexReplacement =
                new String[][] {
                    // Search string       Search regex        SQL replacement regex
                    {"\u0000", "\\x00", "\\\\0"},
                    {"'", "'", "\\\\'"},
                    {"\"", "\"", "\\\\\""},
                    {"\b", "\\x08", "\\\\b"},
                    {"\n", "\\n", "\\\\n"},
                    {"\r", "\\r", "\\\\r"},
                    {"\t", "\\t", "\\\\t"},
                    {"\u001A", "\\x1A", "\\\\Z"},
                    {"\\", "\\\\", "\\\\\\\\"}
                };
        sqlTokens = new HashMap<>();
        StringBuilder patternStr = new StringBuilder();
        for (String[] srr : searchRegexReplacement) {
            sqlTokens.put(srr[0], srr[2]);
            patternStr.append((patternStr.length() == 0) ? "" : "|").append(srr[1]);
        }
        sqlTokenPattern = Pattern.compile('(' + patternStr.toString() + ')');
    }

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Connection connection = null;
        try (InputStream inputStream =
                DBBase.class.getClassLoader().getResourceAsStream("mysql.properties")) {
            Properties properties = new Properties();
            properties.load(inputStream);

            String MYSQL_JDBC_DRIVER = properties.getProperty("jdbc.driver");
            String MYSQL_URL = properties.getProperty("jdbc.url");
            String MYSQL_USERNAME = properties.getProperty("jdbc.username");
            String MYSQL_PASSWORD = properties.getProperty("jdbc.password");

            Class.forName(MYSQL_JDBC_DRIVER);
            connection = DriverManager.getConnection(MYSQL_URL, MYSQL_USERNAME, MYSQL_PASSWORD);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static String getUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "");
    }

    public static String escape(String s) {
        Matcher matcher = sqlTokenPattern.matcher(s);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            matcher.appendReplacement(sb, sqlTokens.get(matcher.group(1)));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public static String escapeToUpperCaseTrimNoNull(String input) {
        return escape(Optional.ofNullable(input).map(String::trim).orElse("")).toUpperCase();
    }

    public static String toUpperCaseTrimNoNull(String input) {
        return Optional.ofNullable(input).map(String::trim).orElse("").toUpperCase();
    }

    private static String oneCondition(
            String columnName, String op, String value, String valueAdd, boolean isCasting) {
        String output = "";
        switch (op) {
            case OP_SZ_EQUAL:
            case OP_SZ_EQUAL_MULTI_NO_ESCAPE:
            case OP_SZ_NOT_EQUAL:
            case OP_SZ_START_WITH:
            case OP_SZ_CONTAIN:
                // String
                if (op.equals(OP_SZ_EQUAL_MULTI_NO_ESCAPE)) {
                    value = toUpperCaseTrimNoNull(value);
                } else {
                    value = escapeToUpperCaseTrimNoNull(value);
                }
                if (value.equals("")) {
                    output = "";
                } else {
                    columnName = "UPPER(%s)".formatted(columnName);
                    output =
                            switch (op) {
                                case OP_SZ_EQUAL -> "(%s = '%s')".formatted(columnName, value);
                                    // No quote in input
                                case OP_SZ_EQUAL_MULTI_NO_ESCAPE -> "(%s in (%s))"
                                        .formatted(columnName, value);
                                case OP_SZ_NOT_EQUAL -> "(NOT %s = '%s')"
                                        .formatted(columnName, value);
                                case OP_SZ_START_WITH -> "(%s LIKE '%s%%')"
                                        .formatted(columnName, value);
                                case OP_SZ_CONTAIN -> "(%s LIKE '%%%s%%')"
                                        .formatted(columnName, value);
                                default -> output;
                            };
                }
                break;
            case OP_INT_EQUAL:
            case OP_INT_EQUAL_MULTI:
            case OP_INT_NOT_EQUAL:
            case OP_INT_EQUAL_OR_OVER:
            case OP_INT_EQUAL_OR_UNDER:
            case OP_INT_BETWEEN:
                // Integer
                value = escapeToUpperCaseTrimNoNull(value);
                if (value.equals("")) {
                    output = "";
                } else {
                    if (isCasting) {
                        columnName = "CAST(%s AS SIGNED)".formatted(columnName);
                    }
                    switch (op) {
                        case OP_INT_EQUAL -> output = "(%s = %s)".formatted(columnName, value);
                        case OP_INT_EQUAL_MULTI -> output =
                                "(%s IN (%s))".formatted(columnName, value);
                        case OP_INT_NOT_EQUAL -> output =
                                "(NOT %s = %s)".formatted(columnName, value);
                        case OP_INT_EQUAL_OR_OVER -> output =
                                "(%s >= %s)".formatted(columnName, value);
                        case OP_INT_EQUAL_OR_UNDER -> output =
                                "(%s <= %s)".formatted(columnName, value);
                        case OP_INT_BETWEEN -> {
                            valueAdd = escapeToUpperCaseTrimNoNull(valueAdd);
                            if (valueAdd.equals("")) {
                                output = "";
                            } else {
                                output =
                                        "(%s BETWEEN %s AND %s)"
                                                .formatted(columnName, value, valueAdd);
                            }
                        }
                    }
                }
                break;
            case OP_BOOL_TRUE:
            case OP_BOOL_FALSE:
                // Boolean
                if (op.equals(OP_BOOL_TRUE)) {
                    if (isCasting) {
                        output = "(UPPER(%s) = 'YES')".formatted(columnName);
                    } else {
                        output = "(%s)".formatted(columnName);
                    }
                } else {
                    if (isCasting) {
                        output = "(NOT UPPER(%s) = 'YES')".formatted(columnName);
                    } else {
                        output = "(NOT %s)".formatted(columnName);
                    }
                }
                break;
        }
        return output;
    }

    public static void addDatetimeConditionLookBack(
            StringBuilder sb, String columnName, int lookBackDay) {
        sb.append(" AND (")
                .append(columnName)
                .append(" >= DATE_SUB(NOW(), INTERVAL ")
                .append(lookBackDay)
                .append(" DAY))");
    }

    public static void addContainTagsCondition2Cols(
            StringBuilder sb, String columnName1, String columnName2, String value) {
        value = escapeToUpperCaseTrimNoNull(value);
        if (value.length() > 0) {
            sb.append(" and (")
                    .append(oneCondition(columnName1, OP_SZ_CONTAIN, value, "", false))
                    .append(" OR ")
                    .append(oneCondition(columnName2, OP_SZ_CONTAIN, value, "", false))
                    .append(")");
        }
    }

    public static void addCondition(
            StringBuilder sb, String columnName, String op, String value, String valueAdd) {
        if (!op.equals(OP_ANY)) {
            String temp = oneCondition(columnName, op, value, valueAdd, false);
            if (temp.length() > 0) {
                sb.append(" and ").append(temp);
            }
        }
    }

    public static void addFieldCondition(
            StringBuilder sb, String fieldID, String op, String value, String valueAdd) {
        if (!op.equals(OP_ANY)) {
            String temp = oneCondition("of2.fieldText", op, value, valueAdd, true);
            if (temp.length() > 0) {
                sb.append(" or (of2.fieldID = ")
                        .append(fieldID)
                        .append(" and (not ")
                        .append(temp)
                        .append("))");
            }
        }
    }

    public static void oneConditionDesc(
            StringBuilder sb, String columnName, String op, String value, String valueAdd) {
        Objects.requireNonNull(columnName, "Unknown_Field");
        Objects.requireNonNull(value);
        if (op.equalsIgnoreCase(OP_BOOL_TRUE)) {
            sb.append(columnName).append("; ");
        } else if (op.equalsIgnoreCase(OP_BOOL_FALSE)) {
            sb.append("not ").append(columnName).append("; ");
        } else if (value.trim().length() != 0) {
            if (op.equalsIgnoreCase(OP_ANY)) {
                sb.append("any ").append(columnName).append("; ");
            } else if (op.equalsIgnoreCase(OP_SZ_EQUAL)) {
                sb.append(columnName).append(" is ").append(value).append("; ");
            } else if (op.equalsIgnoreCase(OP_SZ_EQUAL_MULTI_NO_ESCAPE)) {
                sb.append(columnName).append(" is among (").append(value).append("); ");
            } else if (op.equalsIgnoreCase(OP_SZ_NOT_EQUAL)) {
                sb.append(columnName).append(" is not ").append(value).append("; ");
            } else if (op.equalsIgnoreCase(OP_SZ_START_WITH)) {
                sb.append(columnName).append(" starts with ").append(value).append("; ");
            } else if (op.equalsIgnoreCase(OP_SZ_CONTAIN)) {
                sb.append(columnName).append(" contains ").append(value).append("; ");
            } else if (op.equalsIgnoreCase(OP_INT_EQUAL)) {
                sb.append(columnName).append(" is ").append(value).append("; ");
            } else if (op.equalsIgnoreCase(OP_INT_EQUAL_MULTI)) {
                sb.append(columnName).append(" is among (").append(value).append("); ");
            } else if (op.equalsIgnoreCase(OP_INT_NOT_EQUAL)) {
                sb.append(columnName).append(" is not ").append(value).append("; ");
            } else if (op.equalsIgnoreCase(OP_INT_EQUAL_OR_OVER)) {
                sb.append(columnName).append(" is equal or over ").append(value).append("; ");
            } else if (op.equalsIgnoreCase(OP_INT_EQUAL_OR_UNDER)) {
                sb.append(columnName).append(" is equal or under ").append(value).append("; ");
            } else if (op.equalsIgnoreCase(OP_INT_BETWEEN)) {
                if (valueAdd.trim().length() != 0) {
                    sb.append(columnName)
                            .append(" is between ")
                            .append(value)
                            .append(" and ")
                            .append(valueAdd)
                            .append("; ");
                }
            } else {
                sb.append(columnName)
                        .append(" is in unknown relation to ")
                        .append(value)
                        .append("; ");
            }
        }
    }

    public static BigDecimal getBigDecimalFromParamMap(
            String name, Map<String, String[]> parameters) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(parameters);
        String[] temps = parameters.get(name);
        Objects.requireNonNull(temps);
        if (temps.length > 0 && temps[0].length() > 0) {
            return new BigDecimal(temps[0].trim());
        } else {
            return new BigDecimal(-1);
        }
    }

    public static String getStringFromParamMap(String name, Map<String, String[]> parameters) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(parameters);
        String[] temps = parameters.get(name);
        Objects.requireNonNull(temps);
        if (temps.length > 0) {
            return temps[0].trim();
        }
        return "";
    }

    public static int getIntFromParamMap(String name, Map<String, String[]> parameters) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(parameters);
        String[] temps = parameters.get(name);
        Objects.requireNonNull(temps);
        if (temps.length > 0 && temps[0].length() > 0) {
            int iTemp = -1;
            try {
                iTemp = Integer.parseInt(temps[0].trim());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            return iTemp;
        } else {
            return -2;
        }
    }

    public static String getListOfStringsFromSet(Set<String> set, String delimiter) {
        Objects.requireNonNull(set);
        StringBuilder out = new StringBuilder();
        for (String one : set) {
            Objects.requireNonNull(one);
            if (one.length() > 0) {
                if (out.toString().equals("")) {
                    out = new StringBuilder(delimiter + one + delimiter);
                } else {
                    out.append(",").append(delimiter).append(one).append(delimiter);
                }
            }
        }
        return out.toString();
    }

    public static String getListOfStringsFromParamMap(
            String name, int startIndex, Map<String, String[]> parameters, String delimiter) {
        StringBuilder out = new StringBuilder();
        Objects.requireNonNull(parameters);
        String[] temps;
        for (int i = startIndex; i < MAX_CATEGORY_COUNT; i++) {
            temps = parameters.get(name + i);
            Objects.requireNonNull(temps);
            String one = temps[0];
            Objects.requireNonNull(one);
            if (one.length() > 0) {
                if (out.toString().equals("")) {
                    out = new StringBuilder(delimiter + one + delimiter);
                } else {
                    out.append(",").append(delimiter).append(one).append(delimiter);
                }
            }
        }
        return out.toString();
    }

    // For debug
    public static String dumpParamMap(Map<String, String[]> parameters) {
        StringBuilder sb = new StringBuilder("Params:");
        parameters.forEach(
                (key, values) -> {
                    for (int i = 0; i < values.length; i++) {
                        if (i == 0) {
                            sb.append(key).append("=").append(values[i]).append(",");
                        } else {
                            sb.append(key)
                                    .append("(")
                                    .append(i)
                                    .append(")=")
                                    .append(values[i])
                                    .append(",");
                        }
                    }
                });
        String output = sb.toString();
        if (output.endsWith(",")) {
            output = output.substring(0, output.length() - 1);
        }
        return Helper.escapeHTML(output);
    }
}
