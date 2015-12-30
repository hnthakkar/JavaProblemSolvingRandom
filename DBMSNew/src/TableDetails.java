import java.util.HashMap;


public class TableDetails {

	private HashMap recordDetails = new HashMap();
	private HashMap pageDetails = new HashMap();
	private int lastPageOfTable=0;
	
	/**
	 * 
	 * @return
	 */
	public int getLastPageOfTable() {
		return lastPageOfTable;
	}
	
	/**
	 * 
	 * @param lastPageOfTable
	 */
	public void setLastPageOfTable(int lastPageOfTable) {
		this.lastPageOfTable = lastPageOfTable;
	}
	
	/**
	 * 
	 * @return
	 */
	public HashMap getRecordDetails() {
		return recordDetails;
	}
	
	/**
	 * 
	 * @param <V>
	 * @param recordDetails
	 */
	public void setRecordDetails(HashMap<Integer,RecordDetails> recordDetails) {
		this.recordDetails = recordDetails;
	}
	
	/**
	 * 
	 * @return
	 */
	public HashMap getPageDetails() {
		return pageDetails;
	}
	
	/**
	 * 
	 * @param pageDetails
	 */
	public void setPageDetails(HashMap pageDetails) {
		this.pageDetails = pageDetails;
	}
}
