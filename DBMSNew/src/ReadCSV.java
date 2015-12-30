import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.HashMap;

public class ReadCSV {

	public static TableDetails readCSV(String file) {
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		HashMap<Integer,RecordDetails> recordDetail = new HashMap<Integer,RecordDetails>();
		HashMap<Integer,PageDetails> pageDetail = new HashMap<Integer,PageDetails>();
		 
		TableDetails tableDetails = new TableDetails();
		byte[] cbuf = new byte[DBSystem._pageSize];
		int i;
		int recordNumber = -1;
		int recordInPage = 0;
		int pageNumber = 0;
		long pageStartIndex = 0;
		int fileIndex = 0;
		
		try {
			fis = new FileInputStream(file + ".csv");
			
			int bufferPointer = 0;
			RecordDetails record = null;
			bis = new BufferedInputStream(fis);
			int lastRecordEnd = 0;
			//int recordsCounter = -1;
			while((i =bis.read())!=-1){
				fileIndex++;
				int lastIndex = 0;
				cbuf[bufferPointer++] = (byte)i;
				if(bufferPointer == DBSystem._pageSize){
					String page = new String(cbuf);
					lastIndex = page.lastIndexOf("\n");
					page = page.substring(0, lastIndex+1);
					Arrays.fill(cbuf, (byte)0);
					bufferPointer = 0;
					bis.reset();
					fileIndex = lastRecordEnd;
					pageDetail.put(pageNumber, new PageDetails(pageStartIndex,fileIndex,recordInPage));
					
					byte[] buf = new byte[DBSystem._pageSize];
					try{
						RandomAccessFile raf = new RandomAccessFile("countries.csv","r");
						raf.seek(pageStartIndex);
						raf.read(buf, 0, (int) (fileIndex - pageStartIndex));
						//System.out.println(new String(buf)); 						
					} catch (Exception e) {
						e.printStackTrace();
					}
					tableDetails.setLastPageOfTable(pageNumber);
					pageNumber++;
					pageStartIndex = lastRecordEnd;
					recordInPage = 0;
				}
				if (i == 10) {
					//recordsCounter++;
					recordNumber++;
					recordInPage++;
					record = new RecordDetails(pageNumber,recordInPage);
					recordDetail.put(recordNumber, record);
					bis.mark(fileIndex);
					lastIndex = fileIndex;
					lastRecordEnd = fileIndex;
				}
			}
			pageDetail.put(pageNumber, new PageDetails(pageStartIndex,fileIndex,recordInPage));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
				if (bis != null) {
					bis.close();
				}
			} catch (Exception e) {
				System.out.println("");
			}
		}
		tableDetails.setPageDetails(pageDetail);
		tableDetails.setRecordDetails(recordDetail);
		return tableDetails;
	}

}
