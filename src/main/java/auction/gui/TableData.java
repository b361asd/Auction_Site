package auction.gui;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to hold data for a 2 layer table.
 * Provides a function to sort tables based on column header.
 */
public class TableData {
   public static final String SUB_TABLE_HEADER_SIGN = "-";

   Map<String, Integer> mapHeaderToIndex;
   //
   List                 lstHeader;
   List                 lstRows;
   int[]                colSeq;
   String               description  = "";
   //
   int                  indexSorted  = -1;
   boolean              normalSorted = true;
   //
   String               signStandOut = null;
   int                  idxStandOut  = -1;


   public TableData(List _lstHeader, List _lstRows, int[] _colSeq) {
      lstHeader = _lstHeader;
      lstRows = _lstRows;
      colSeq = _colSeq;
      //
      mapHeaderToIndex = new HashMap<>();
      if (lstHeader != null) {
         for (int i = 0; i < lstHeader.size(); i++) {
            String temp = lstHeader.get(i) == null ? "" : lstHeader.get(i).toString();
            mapHeaderToIndex.put(temp, i);
         }
      }
   }

   public static void main(String[] args) {
      //Object one     = new BigDecimal(100.0);
      Object one = null;
      //
      if (one instanceof BigDecimal) {
         System.out.println("YES");
      }
      else {
         System.out.println("NO");
      }
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String input) {
      description = input;
   }

   public int rowCount() {
      return (lstRows == null) ? 0 : lstRows.size();
   }

   public int colCount() {
      return (colSeq == null) ? 0 : colSeq.length;
   }

   public List getRows() {
      return lstRows;
   }

   public String getSignStandOut() {
      return signStandOut;
   }

   public void setStandOut(String input, int idx) {
      signStandOut = input;
      idxStandOut = idx;
   }

   public Object getOneCell(int i, int j) {
      return ((List) (lstRows.get(i))).get(j);
   }

   public Object getOneCell(int i, String header) {
      Integer objIndex = mapHeaderToIndex.get(header);
      if (objIndex == null) {
         return "";
      }
      else {
         int index = objIndex;
         if (index >= 0) {
            return ((List) lstRows.get(i)).get(index);
         }
         else {
            return "";
         }
      }
   }

   public Object getLastCellInRow(int i) {
      List lst = (List) (lstRows.get(i));
      return lst.get(lst.size() - 1);
   }

   public void sortRowPerHeader(String header) {
      if (header != null && header.startsWith(SUB_TABLE_HEADER_SIGN)) {
         if (lstRows != null) {
            for (Object one : lstRows) {
               List lst = (List) one;
               //
               TableData tableData = (TableData) lst.get(lst.size() - 1);
               if (tableData != null) {
                  tableData.sortRowPerHeader(header.substring(SUB_TABLE_HEADER_SIGN.length()));
               }
            }
         }
      }
      else if (header != null) {
         int index = mapHeaderToIndex.get(header);
         //
         if (index >= 0 && index < lstHeader.size()) {
            Comparator<Object> comparatorNorm = Comparator.comparing(o -> ((List) o).get(index) == null ? "" : ((List) o).get(index).toString().trim());
            //
            boolean isTheSame = (indexSorted == index);
            if (isTheSame) {
               if (normalSorted) {
                  lstRows.sort(comparatorNorm.reversed());
               }
               else {
                  lstRows.sort(comparatorNorm);
               }
               normalSorted = !normalSorted;
            }
            else {
               lstRows.sort(comparatorNorm);
               normalSorted = true;
            }
            indexSorted = index;
         }
      }
   }

   public String printDescriptionForTable(boolean addOne) {
      return "<th colspan='" + (colCount() + (addOne ? 1 : 0)) + "'>" + Helper.escapeHTML(description) + "</th>";
   }

   public String printHeaderForTable() {
      return internalPrintHeaderForTable(false);
   }

   public String printSubHeaderForTable() {
      return internalPrintHeaderForTable(true);
   }

   private String internalPrintHeaderForTable(boolean subTable) {
      StringBuilder out = new StringBuilder();
      if (lstHeader != null && lstHeader.size() > 0 && colSeq != null && colSeq.length > 0) {
         for (int value : colSeq) {
            Object one     = lstHeader.get(value);
            String oneItem = (one == null) ? "" : one.toString();
            out.append("<th><div onclick=onClickHeader('").append(subTable ? SUB_TABLE_HEADER_SIGN : "").append(oneItem).append("')>").append(oneItem).append("</div></th>");
         }
      }
      return out.toString();
   }

   public String printOneRowInTable(int index) {
      return internalPrintOneRowInTable(index, null);
   }

   public String printOneRowInTableWithWidth(int index, String width) {
      return internalPrintOneRowInTable(index, width);
   }

   //timezone="America/New_York"
   private String internalPrintOneRowInTable(int index, String width) {
      List          row = (List) lstRows.get(index);
      StringBuilder out = new StringBuilder();
      if (row != null && row.size() > 0 && colSeq != null && colSeq.length > 0) {
         for (int value : colSeq) {
            Object one = row.get(value);
            //
            String oneItem = (one == null) ? "" : one.toString();
            if (one instanceof BigDecimal) {
               if (((BigDecimal) one).compareTo(new BigDecimal(-1)) == 0) {
                  oneItem = "";
               }
            }
            else if (one instanceof Timestamp) {
               Timestamp     ts  = (Timestamp) one;
               LocalDateTime ldt = LocalDateTime.ofInstant(ts.toInstant(), ZoneId.of("America/New_York"));
               //
               DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
               oneItem = ldt.format(formatter);
            }
            //
            if (width == null) {
               out.append("<td>").append(Helper.escapeHTML(oneItem)).append("</td>");
            }
            else {
               out.append("<td width='").append(width).append("'>").append(Helper.escapeHTML(oneItem)).append("</td>");
            }
         }
      }
      return out.toString();
   }

   public String printRowStart(int index) {
      List   row = (List) lstRows.get(index);
      String out;
      //
      String sign = (signStandOut == null) ? "_NULL" : signStandOut.trim();
      sign = (sign.equals("")) ? "_EMPTY" : sign;
      //
      String value = (idxStandOut < 0) ? "_NONE" : (row.get(idxStandOut).toString());
      //
      boolean isStandOut = sign.equalsIgnoreCase(value);
      //
      if (isStandOut) {
         out = "<tr style='color: red;'>";               //out = "<tr name='standout' class='standout'>";
      }
      else {
         out = "<tr add='" + sign + "_" + value + "'>";
      }
      //
      return out;
   }
}
