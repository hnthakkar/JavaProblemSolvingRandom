import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class Tester {

	public static void main(String[] args) throws Exception {
		int input[] = { 0, 1, 2, 1, 2, 2, 3, 41, 9, 39, 28, 1, 30, 38, 39, 31,-1, 42, 28 };
		RandomAccessFile f = null;
		List<String> lines = new ArrayList<String>();
		try {
			f = new RandomAccessFile("countries.csv", "r");
			String s = new String();
			while ((s = f.readLine()) != null) {
				lines.add(s);
			}
		} finally {
			try {
				if (f != null) {
					f.close();
				}
			} catch (Exception e) {
				System.out.println("");
			}
		}

		DBSystem dbs = new DBSystem();
		dbs.readConfig("config.txt");
		dbs.populateDBInfo();
		
		for (int inp : input) {
			if (inp != -1) {
				String op = dbs.getRecord("countries", inp);
				if (lines.get(inp).equals(op)) {

				} else {
					System.out.println();
					System.out.println("Fail for record number " + inp
							+ " expected = '" + lines.get(inp) + "' actual= '"
							+ op + "'");
					System.exit(-1);
				}
			} else
				dbs.insertRecord("countries", "record");
		}

		String[] query = {
				"select ID, Code from countries",
				"select distinct(ID) from countries where code=5",
				"select * from countries where name = 'India' group by (id) having (id) > 10 order by id",
				"create table student (id number, name varchar2(20))",
				"select col1 from notexisting"};

		for(int j=1; j<= query.length; j++){
			System.out.print("\nResult for Query " + j  + ": ");
			System.out.println("\n_________________________________");
			dbs.queryType(query[j-1]);	
		}
		
		System.exit(0);
	}

}
