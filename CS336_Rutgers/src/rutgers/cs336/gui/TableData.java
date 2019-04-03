package rutgers.cs336.gui;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableData {
	Map<String, Integer> mapHeaderToIndex;
	//
	List                 lstHeader;
	List                 lstRows;
	int[]                colSeq;
	//
	int                  indexSorted     = -1;
	boolean              normalSorted    = true;
	//
	String               offerIDStandOut = null;


	public int rowCount() {
		return (lstRows==null) ? 0 : lstRows.size();
	}
	public int colCount() {
		return (colSeq==null) ? 0 : colSeq.length;
	}
	
	
	public List getLstHeader() {
		return lstHeader;
	}


	public List getLstRows() {
		return lstRows;
	}


	public int[] getColSeq() {
		return colSeq;
	}

	public String getOfferIDStandOut() {
		return offerIDStandOut;
	}

	public void setOfferIDStandOut(String input) {
		offerIDStandOut = input;
	}

	public TableData(List _lstHeader, List _lstRows, int[] _colSeq) {
		lstHeader = _lstHeader;
		lstRows = _lstRows;
		colSeq = _colSeq;
		//
		mapHeaderToIndex = new HashMap<>();
		for (int i = 0; i < lstHeader.size(); i++) {
			mapHeaderToIndex.put(lstHeader.get(i).toString(), i);
		}
	}


	public Object getOneCell(int i, int j) {
		return ((List)(lstRows.get(i))).get(j);
	}
	public Object getLastCellInRow(int i) {
		List lst =  (List)(lstRows.get(i));
		return lst.get(lst.size() - 1);
	}
	
	
	public void sortRowPerHeader(String header) {
		if (header != null) {
			int index = mapHeaderToIndex.get(header);
			//
			Comparator<Object> comparatorRev  = (o1, o2) -> -(((List) o1).get(index) == null ? "" : ((List) o1).get(index).toString()).compareTo(((List) o2).get(index) == null ? "" : ((List) o2).get(index).toString());
			Comparator<Object> comparatorNorm = Comparator.comparing(o -> ((List) o).get(index) == null ? "" : ((List) o).get(index).toString());
			//
			if (index >= 0 && index < lstHeader.size()) {
				boolean isTheSame = (indexSorted == index);
				if (isTheSame) {
					if (normalSorted) {
						lstRows.sort(comparatorRev);
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
	
	public String printHeaderForTable() {
		String out = "";
		if (lstHeader != null && lstHeader.size() > 0 && colSeq != null && colSeq.length > 0) {
			for (int value : colSeq) {
				Object one     = lstHeader.get(value);
				String oneItem = (one == null) ? "" : one.toString();
				out = out + "<th><div onclick=onClickHeader('" + oneItem + "')>" + oneItem + "</div></th>";
			}
		}
		return out;
	}


	public String printOneRowInTable(int index) {
		List row = (List)lstRows.get(index);
		String out = "";
		if (row != null && row.size() > 0 && colSeq != null && colSeq.length > 0) {
			for (int value : colSeq) {
				Object one     = row.get(value);
				String oneItem = (one == null) ? "" : one.toString();
				out = out + "<td>" + oneItem + "</td>";
			}
		}
		return out;
	}
	

	public String printRowStart(int index) {
		List row = (List)lstRows.get(index);
		String out = "";
		//
		boolean isStandOut = offerIDStandOut != null && (row.get(0)).equals(offerIDStandOut);
		//
		if (isStandOut) {
			out = "<tr name='standout' class='standout'>";
		}
		else {
			out = "<tr>";
		}
		//
		return out;
	}

}
