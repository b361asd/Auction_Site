package com.b361asd.auction.gui;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Class to hold data for a 2 layer table. Provides a function to sort tables based on column
 * header.
 */
public class TableData {

    private static final String SUB_TABLE_HEADER_SIGN = "-";

    Map<String, Integer> mapHeaderToIndex;
    List<String> lstHeader;
    List<Object> lstRows;
    int[] colSeq;
    String description = "";
    int indexSorted = -1;
    boolean normalSorted = true;
    String signStandOut = null;
    int idxStandOut = -1;

    public TableData(List<String> lstHeader, List<Object> lstRows, int[] colSeq) {
        this.lstHeader = lstHeader;
        this.lstRows = lstRows;
        this.colSeq = colSeq;
        mapHeaderToIndex = new HashMap<>();
        if (this.lstHeader != null) {
            for (int i = 0; i < this.lstHeader.size(); i++) {
                String temp = this.lstHeader.get(i) == null ? "" : this.lstHeader.get(i);
                mapHeaderToIndex.put(temp, i);
            }
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String input) {
        description = input;
    }

    public int rowCount() {
        return Optional.ofNullable(lstRows).map(List::size).orElse(0);
    }

    public int colCount() {
        return Optional.ofNullable(colSeq).map(seq -> seq.length).orElse(0);
    }

    public List<Object> getRows() {
        return lstRows;
    }

    public void setStandOut(String input, int idx) {
        signStandOut = input;
        idxStandOut = idx;
    }

    public Object getOneCell(int i, int j) {
        return ((List<?>) (lstRows.get(i))).get(j);
    }

    public Object getOneCell(int i, String header) {
        Integer objIndex = mapHeaderToIndex.get(header);
        if (objIndex == null) {
            return "";
        }
        int index = objIndex;
        if (index >= 0) {
            return ((List<?>) lstRows.get(i)).get(index);
        }
        return "";
    }

    public Object getLastCellInRow(int i) {
        List lst = (List) (lstRows.get(i));
        return lst.get(lst.size() - 1);
    }

    public void sortRowPerHeader(String header) {
        Objects.requireNonNull(header);
        Objects.requireNonNull(lstRows);
        if (header.startsWith(SUB_TABLE_HEADER_SIGN)) {
            for (Object one : lstRows) {
                List lst = (List) one;
                TableData tableData = (TableData) lst.get(lst.size() - 1);
                Objects.requireNonNull(tableData)
                        .sortRowPerHeader(header.substring(SUB_TABLE_HEADER_SIGN.length()));
            }
        } else {
            int index = mapHeaderToIndex.get(header);
            if (index >= 0 && index < lstHeader.size()) {
                Comparator<Object> comparatorNorm =
                        Comparator.comparing(
                                o ->
                                        ((List<?>) o).get(index) == null
                                                ? ""
                                                : ((List<?>) o).get(index).toString().trim());
                boolean isTheSame = indexSorted == index;
                if (isTheSame) {
                    if (normalSorted) {
                        lstRows.sort(comparatorNorm.reversed());
                    } else {
                        lstRows.sort(comparatorNorm);
                    }
                    normalSorted = !normalSorted;
                } else {
                    lstRows.sort(comparatorNorm);
                    normalSorted = true;
                }
                indexSorted = index;
            }
        }
    }

    public String printDescriptionForTable(boolean addOne) {
        return MessageFormat.format(
                "<th colspan=''{0}''>{1}</th>",
                colCount() + (addOne ? 1 : 0), Helper.escapeHTML(description));
    }

    public String printHeaderForTable() {
        return internalPrintHeaderForTable(false);
    }

    public String printSubHeaderForTable() {
        return internalPrintHeaderForTable(true);
    }

    private String internalPrintHeaderForTable(boolean subTable) {
        Objects.requireNonNull(lstHeader);
        Objects.requireNonNull(colSeq);
        StringBuilder out = new StringBuilder();
        if (!lstHeader.isEmpty() && colSeq.length > 0) {
            for (int value : colSeq) {
                Object one = lstHeader.get(value);
                String oneItem = Optional.ofNullable(one).map(Object::toString).orElse("");
                out.append("<th><div onclick=onClickHeader('")
                        .append(subTable ? SUB_TABLE_HEADER_SIGN : "")
                        .append(oneItem)
                        .append("')>")
                        .append(oneItem)
                        .append("</div></th>");
            }
        }
        return out.toString();
    }

    public String printOneRowInTable(int index) {
        return internalPrintOneRowInTable(index);
    }

    private String internalPrintOneRowInTable(int index) {
        List row = (List) lstRows.get(index);
        StringBuilder out = new StringBuilder();
        Objects.requireNonNull(row);
        Objects.requireNonNull(colSeq);
        if (!row.isEmpty() && colSeq.length > 0) {
            for (int value : colSeq) {
                Object one = row.get(value);
                String oneItem = one == null ? "" : one.toString();
                if (one instanceof BigDecimal bd) {
                    if (bd.compareTo(new BigDecimal(-1)) == 0) {
                        oneItem = "";
                    }
                } else if (one instanceof Timestamp ts) {
                    LocalDateTime ldt =
                            LocalDateTime.ofInstant(ts.toInstant(), ZoneId.of("America/New_York"));
                    DateTimeFormatter formatter =
                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    oneItem = ldt.format(formatter);
                }
                out.append("<td>").append(Helper.escapeHTML(oneItem)).append("</td>");
            }
        }
        return out.toString();
    }

    public String printRowStart(int index) {
        List row = (List) lstRows.get(index);
        String sign = Optional.ofNullable(signStandOut).map(String::trim).orElse("_NULL");
        sign = sign.equals("") ? "_EMPTY" : sign;
        String value = idxStandOut < 0 ? "_NONE" : row.get(idxStandOut).toString();
        boolean isStandOut = sign.equalsIgnoreCase(value);
        // "<tr name='standout' class='standout'>";
        return isStandOut
                ? "<tr style='color: red;'>"
                : MessageFormat.format("<tr add=''{0}_{1}''>", sign, value);
    }
}
