import java.util.Scanner;


public class MainClass {
	
	public static void main(String[] arg ){
		String filename = "someLogFile.log";
		ReadLogFileToJson logToJson = new ReadLogFileToJson(filename);
		Scanner sc;
		try {
			sc = new Scanner(System.in);
			char ch;
			int lineNum;
			do{
				System.out.println("Enter the line number :");
				lineNum = sc.nextInt();
				System.out.println(logToJson.jsonObjectList.get(lineNum-1).toString());
				System.out.println("Press 'e' to exit, another to continue!");
				ch = sc.next(".").charAt(0);
			}while(ch != 'E' || ch != 'e');
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			sc = null;
		}
		System.exit(0);
	}

}
