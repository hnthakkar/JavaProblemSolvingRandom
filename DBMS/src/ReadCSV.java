import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.HashMap;

public class ReadCSV {

	public static TableDetails readCSV(String file) {
	//public static HashMap<Integer,Integer> readCSV(String file) {
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		HashMap dualMap = new HashMap();
		HashMap<Integer,RecordDetails> recordDetail = new HashMap<Integer,RecordDetails>();
		HashMap<Integer,PageDetails> pageDetail = new HashMap<Integer,PageDetails>();
		//HashMap<Integer,Integer> recordDetail = new HashMap<Integer,Integer>();
		 
		TableDetails tableDetails = new TableDetails();
		//PAGE-SIZE 
		byte[] cbuf = new byte[ReadMetadata._pageSize];
		int i;
		int recordNumber = 0;
		int recordInPage = 0;
		int pageNumber = 0;
		long pageStartIndex = 0;
		//long pageEndIndex = 0;
		//long recordStartIndex = 0;
		//long recordEndIndex = 0;
		int fileIndex = 0;
		
		try {
			//System.out.println("***************** reading "+ file +".csv file" + "*********************");
			//fis = new FileInputStream("Student.csv");
			fis = new FileInputStream(file + ".csv");
			
			int bufferPointer = 0;
			RecordDetails record = null;
			bis = new BufferedInputStream(fis);
			int lastRecordEnd = 0;
			while((i =bis.read())!=-1){
				//pageEndIndex++;
				//recordEndIndex++;
				//pageStartIndex++;
				fileIndex++;
				int lastIndex = 0;
				cbuf[bufferPointer++] = (byte)i;
				if(bufferPointer == ReadMetadata._pageSize){
					String page = new String(cbuf);
					lastIndex = page.lastIndexOf(";");
					page = page.substring(0, lastIndex+1);
					//System.out.println(page);
					Arrays.fill(cbuf, (byte)0);
					bufferPointer = 0;
					bis.reset();
					fileIndex = lastRecordEnd;
					//System.out.println("Page "+ pageNumber + " StartIndex : " + pageStartIndex + " EndIndex : " + fileIndex + " RecordsInPage: " + recordInPage);
					pageDetail.put(pageNumber, new PageDetails(pageStartIndex,fileIndex,recordInPage));
					tableDetails.setLastPageOfTable(pageNumber);
					pageNumber++;
					pageStartIndex = lastRecordEnd + 1;
					//record.setPageEndIndex(lastRecordEnd);
					recordInPage = 0;
					
					//System.out.println("Record Details : page numbr : " + record.getPageNumber() + " Page Start Index : " + record.getPageStartIndex() + " Page End Index : " + record.getPageEndIndex() + " Record In Page : " +record.get_recordNumberInPage());
					//recordEndIndex = lastIndex;
					//pageStartIndex = pageEndIndex + 1;
				}
				if(i == 59){
					recordNumber++;
					recordInPage++;
					//System.out.println("Record "+ recordInPage +" in Page : " + pageNumber);
					record = new RecordDetails(pageNumber,recordInPage);
					recordDetail.put(recordNumber, record);
					//recordDetail.put(recordNumber, pageNumber);
					//System.out.println("Record :" + recordNumber + " is in Page : " + pageNumber + " with StartIndex : " + recordStartIndex + " and EndIndex : " + fileIndex);
					//System.out.println("Record :" + recordNumber + " is in Page : " + pageNumber);
					bis.mark(fileIndex);
					lastIndex = fileIndex;
					lastRecordEnd = fileIndex;
					//recordEndIndex = fileIndex;
					//recordStartIndex = fileIndex + 1;
				}	
			}
			//record = new RecordDetails(pageNumber,recordInPage);
			//recordDetail.put(recordNumber, record);
			pageDetail.put(pageNumber, new PageDetails(pageStartIndex,fileIndex,recordInPage));
			//System.out.println("**************************************");
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null)
					fis.close();
				if (bis != null)
					bis.close();
			} catch (Exception e) {

			}
		}
		tableDetails.setPageDetails(pageDetail);
		tableDetails.setRecordDetails(recordDetail);
		//dualMap.put(ReadMetadata.RECORD_DETAILS, recordDetail);
		//dualMap.put(ReadMetadata.PAGE_DETAILS, pageDetail);
		return tableDetails;
	}
	
}
