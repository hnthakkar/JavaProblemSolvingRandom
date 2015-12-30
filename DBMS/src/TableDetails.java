import java.util.HashMap;


public class TableDetails {

	private HashMap recordDetails = new HashMap();
	private HashMap pageDetails = new HashMap();
	private int lastPageOfTable=0;
	
	public int getLastPageOfTable() {
		return lastPageOfTable;
	}
	public void setLastPageOfTable(int lastPageOfTable) {
		this.lastPageOfTable = lastPageOfTable;
	}
	public HashMap getRecordDetails() {
		return recordDetails;
	}
	public void setRecordDetails(HashMap recordDetails) {
		this.recordDetails = recordDetails;
	}
	public HashMap getPageDetails() {
		return pageDetails;
	}
	public void setPageDetails(HashMap pageDetails) {
		this.pageDetails = pageDetails;
	}
}
