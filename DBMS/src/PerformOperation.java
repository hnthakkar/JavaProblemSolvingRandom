import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Scanner;

public class PerformOperation {
	
	private static LRUCache cache = null;

	public static void operate(String tableName, int operation){
		int record = 0;
		if(operation == DBMSMain.READ_OPERATION){
			System.out.println("Please Enter the record Number to be read :");
			Scanner scan = new Scanner(System.in);
			if(scan.hasNextLine()){
				record = scan.nextInt();
				read(tableName,record);
			}	
		} else if(operation == DBMSMain.WRITE_OPERATION){
			write(tableName);
		}
	}
	
	private static void read(String tableName, int record){
		if(cache == null){
			cache = new LRUCache(ReadMetadata._max_page_to_cache); 
		}
		TableDetails dual = DBMSMain.allTables.get(tableName);
		//HashMap recordDetail = (HashMap)dual.get(ReadMetadata.RECORD_DETAILS);
		//HashMap pageDetail = (HashMap)dual.get(ReadMetadata.PAGE_DETAILS);
		HashMap recordDetail = (HashMap)dual.getRecordDetails();
		HashMap pageDetail = (HashMap)dual.getPageDetails();
		RecordDetails rd = (RecordDetails)recordDetail.get(record);
		int pageNumber = rd.getPageNumber();
		String page = null;
		if(cache.get(tableName + pageNumber) != null){
			System.out.println("Reading from cache");
			page = cache.get(tableName + pageNumber);
		} else {
			PageDetails pgd = (PageDetails)pageDetail.get(pageNumber);
			byte[] buf = new byte[ReadMetadata._pageSize];
			try{
				RandomAccessFile raf = new RandomAccessFile(tableName + ".csv", "r");
				raf.seek(pgd.getStartIndex());
				raf.read(buf, 0, (int)(pgd.getEndIndex() - pgd.getStartIndex()));
				page = new String(buf);
				cache.put(tableName + pageNumber, page);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		if(page != null){
			String[] records = page.split(";");
			System.out.println(records[rd.getRecordNumberInPage()-1]);
		}
	}
	
	private static void write(String tableName){
		System.out.println("Please enter the row : ");
		String data = null; 
		Scanner scan = new Scanner(System.in);
		if(scan.hasNext()){
			data = scan.nextLine();
			/*if(data.lastIndexOf(";") != data.length()){
				data = data + ";";
			}*/
		}	
		File file =new File(tableName + ".csv");
		BufferedWriter bufferWritter = null;
		try{
		FileWriter fileWritter = new FileWriter(file.getName(),true);
	    bufferWritter = new BufferedWriter(fileWritter);
	    bufferWritter.write(data);
	    } catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(bufferWritter != null)
					bufferWritter.close();
			}catch(Exception e){
				
			}
		}
		//Need to think of a better way of doing this
		TableDetails dual = DBMSMain.allTables.get(tableName);
		int previousLastPage = dual.getLastPageOfTable();
		DBMSMain.allTables.put(tableName, ReadCSV.readCSV(tableName));
		int newLastPage = dual.getLastPageOfTable();
		if(previousLastPage == newLastPage)
			cache.invalidate(tableName + previousLastPage);
	}
}