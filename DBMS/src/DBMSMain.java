import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;


public class DBMSMain {
	public static String selectTable = null;
	public static int operationSelected = 0;
	public static final int READ_OPERATION = 1;
	public static final int WRITE_OPERATION = 2;
	public static HashMap<String,TableDetails> allTables = new HashMap<String,TableDetails>();
	//public static HashMap<String,HashMap<Integer,Integer>> allTables = new HashMap<String,HashMap<Integer,Integer>>();
	
	public static void main(String[] arg) throws IOException, SAXException, ParserConfigurationException {
		SAXParserFactory spfac = SAXParserFactory.newInstance();
        SAXParser sp = spfac.newSAXParser();

        ReadMetadata handler = new ReadMetadata();
         
        sp.parse("metadata.xml", handler);
        //FOR TESTING PURPOSE
        //handler.readTableList();
        //handler.readConfig();
        for (Map.Entry<String, Map<String,String>> entry : ReadMetadata._tableList.entrySet()) {
        	allTables.put(entry.getKey(), ReadCSV.readCSV(entry.getKey()));
        }
        int choice = 0;
        do{
	        getTable();
	        System.out.println("Select table : " + selectTable + " and Selected operation : " + (operationSelected == READ_OPERATION?"READ":"WRITE"));
	        //Perform the read/write operation
	        PerformOperation.operate(selectTable, operationSelected);
	        System.out.println("*****************************");
	        System.out.println("To Continue Press 1");
	        System.out.println("To Exit Any other key");
	        Scanner scan = new Scanner(System.in);
			if(scan.hasNextLine())
				choice = scan.nextInt();
        }while(choice == 1);
    }

	public static void getTable(){
		int choice = 0;
        Map<Integer,String> reverseIndex; 
        System.out.println("Please specify the Table on which you need to perform operation :");
		int i = 1;
		reverseIndex = new HashMap<Integer,String>();
		for (Map.Entry<String, Map<String,String>> entry : ReadMetadata._tableList.entrySet()) {
        	System.out.println("Press " + i + " for Table : " + entry.getKey());
        	reverseIndex.put(i, entry.getKey());
        	i++;
        }
		System.out.println("Any Other key to Exit");
		Scanner scan = new Scanner(System.in);
		if(scan.hasNextLine()){
			choice = scan.nextInt();
			if(reverseIndex.containsKey(choice)){
				selectTable = reverseIndex.get(choice);
				getOperation();
			}else {
				System.exit(0);
			}
		}
	}
	
	public static void getOperation(){
		int choice = 0;
		System.out.println("Please specify the operation you want to perform :");
		System.out.println("For Read operation Press 1");
		System.out.println("For write operation Press 2");
		System.out.println("To Exit Press 3");
		Scanner scan = new Scanner(System.in);
		if(scan.hasNextLine()){
			choice = scan.nextInt();
			switch(choice){
				case 1:
					operationSelected = READ_OPERATION;
					break;
				case 2:
					operationSelected = WRITE_OPERATION;
					break;
				case 3:
				default:	
					System.exit(0);
			}
		}	
        	
    }
}

