package rutgers.cs336.gui;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


	public Object getOneCell(int i, int j) {
		return ((List) (lstRows.get(i))).get(j);
	}

	public Object getOneCell(int i, String header) {
		int index = mapHeaderToIndex.get(header);
		if (index >= 0) {
			return ((List) lstRows.get(i)).get(index);
		}
		else {
			return "";
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


	public String printDescriptionForTable() {
		return "<th colspan='" + colCount() + "'>" + Helper.escapeHTML(description) + "</th>";
	}


	public String printHeaderForTable() {
		return internalPrintHeaderForTable(false, null);
	}

	public String printSubHeaderForTable(String width) {
		return internalPrintHeaderForTable(true, width);
	}

	private String internalPrintHeaderForTable(boolean subTable, String width) {
		String out = "";
		if (lstHeader != null && lstHeader.size() > 0 && colSeq != null && colSeq.length > 0) {
			for (int value : colSeq) {
				Object one     = lstHeader.get(value);
				String oneItem = (one == null) ? "" : one.toString();
				if (width == null) {
					out = out + "<th><div onclick=onClickHeader('" + (subTable ? SUB_TABLE_HEADER_SIGN : "") + oneItem + "')>" + oneItem + "</div></th>";
				}
				else {
					out = out + "<th width='" + width + "'><div onclick=onClickHeader('" + (subTable ? SUB_TABLE_HEADER_SIGN : "") + oneItem + "')>" + oneItem + "</div></th>";
				}
			}
		}
		return out;
	}


	public String printOneRowInTable(int index) {
		return internalPrintOneRowInTable(index, null);
	}

	public String printOneRowInTableWithWidth(int index, String width) {
		return internalPrintOneRowInTable(index, width);
	}

	private String internalPrintOneRowInTable(int index, String width) {
		List   row = (List) lstRows.get(index);
		String out = "";
		if (row != null && row.size() > 0 && colSeq != null && colSeq.length > 0) {
			for (int value : colSeq) {
				Object one     = row.get(value);
				String oneItem = (one == null) ? "" : one.toString();
				if (width == null) {
					out = out + "<td>" + Helper.escapeHTML(oneItem) + "</td>";
				}
				else {
					out = out + "<td width='" + width + "'>" + Helper.escapeHTML(oneItem) + "</td>";
				}
			}
		}
		return out;
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
