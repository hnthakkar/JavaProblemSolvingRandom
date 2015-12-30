import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.TCustomSqlStatement;
import gudusoft.gsqlparser.TGSqlParser;
import gudusoft.gsqlparser.TStatementList;
import gudusoft.gsqlparser.nodes.TColumnDefinition;
import gudusoft.gsqlparser.nodes.TColumnDefinitionList;
import gudusoft.gsqlparser.nodes.TGroupBy;
import gudusoft.gsqlparser.nodes.TResultColumn;
import gudusoft.gsqlparser.nodes.TResultColumnList;
import gudusoft.gsqlparser.stmt.TCreateTableSqlStatement;
import gudusoft.gsqlparser.stmt.TSelectSqlStatement;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBSystem {
	public static String selectTable = null;
	public static int operationSelected = 0;
	public static final int READ_OPERATION = 1;
	public static final int WRITE_OPERATION = 2;
	public static HashMap<String, TableDetails> allTables = new HashMap<String, TableDetails>();
	private static LRUCache cache = null;

	public static int _pageSize;
	public static int _max_page_to_cache;

	public static Map<String, List<String>> _tableList = new HashMap<String, List<String>>();
	public static String path;

	public void readConfig(String configFilePath) {
		String pair[], line;
		boolean readTable = false;
		boolean readColumn = true;

		try {
			RandomAccessFile br = new RandomAccessFile(configFilePath, "r");
			line = br.readLine();
			StringBuffer tableName = new StringBuffer(10);
			List colList;
			while (line != null) {
				pair = line.split(" ");
				if (line.contains("PAGE_SIZE")) {
					_pageSize = Integer.parseInt(pair[1]);
				} else if (line.contains("NUM_PAGES")) {
					_max_page_to_cache = Integer.parseInt(pair[1]);
				} else if (line.contains("PATH_FOR_DATA")) {
					path = pair[1];
				} else if (line.toLowerCase().contains("begin")) {
					tableName.setLength(0);
					readTable = true;
				} else if (readTable) {
					tableName.append(line.toLowerCase());
					_tableList.put(tableName.toString(), null);
					readTable = false;
					readColumn = true;
				} else if (readColumn) {
					if(line.toLowerCase().contains("end")){
						readColumn = false;
						continue;
					}
					colList = _tableList.get(tableName.toString());
					if(colList == null){
						colList =  new ArrayList<String>(10);
					}
					
					colList.add(pair[0]);
					_tableList.put(tableName.toString(), colList);
				}
				line = br.readLine();
			}

		} catch (Exception e) {
			System.out.println("Problem reading congif");
		}
		
	}

	public void populateDBInfo() {
		for (Map.Entry<String, List<String>> entry : _tableList.entrySet()) {
			allTables.put(entry.getKey(), ReadCSV.readCSV(entry.getKey()));
		}
	}

	public String getRecord(String tableName, int record) {
		if (cache == null) {
			cache = new LRUCache(_max_page_to_cache);
		}
		TableDetails dual = DBSystem.allTables.get(tableName);
		HashMap recordDetail = (HashMap) dual.getRecordDetails();
		HashMap pageDetail = (HashMap) dual.getPageDetails();
		RecordDetails rd = (RecordDetails) recordDetail.get(record);
		int pageNumber = rd.getPageNumber();
		String page = null;
		if (cache.get(tableName + pageNumber) != null) {
			System.out.println("HIT :" + (tableName + pageNumber));
			page = cache.get(tableName + pageNumber);
		} else {
			System.out.println("MISS :" + (tableName + pageNumber));
			PageDetails pgd = (PageDetails) pageDetail.get(pageNumber);
			byte[] buf = new byte[_pageSize];
			try {
				RandomAccessFile raf = new RandomAccessFile(tableName + ".csv",
						"r");
				raf.seek(pgd.getStartIndex());
				raf.read(buf, 0,
						(int) (pgd.getEndIndex() - pgd.getStartIndex()));
				page = new String(buf);
				cache.put(tableName + pageNumber, page);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (page != null) {
			// page = page.trim();
			String[] records = page.split("\n");
			System.out.println(records[rd.getRecordNumberInPage() - 1]);
			// if(record > 8)
			// return records[rd.getRecordNumberInPage()-2];
			return records[rd.getRecordNumberInPage() - 1].trim();
			// return records[rd.getRecordNumberInPage()];
		}
		return null;
	}

	public void insertRecord(String tableName, String data) {
		//System.out.println("Please enter the row : ");
		File file = new File(tableName + ".csv");
		BufferedWriter bufferWritter = null;
		try {
			FileWriter fileWritter = new FileWriter(file.getName(), true);
			bufferWritter = new BufferedWriter(fileWritter);
			bufferWritter.write(data);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (bufferWritter != null)
					bufferWritter.close();
			} catch (Exception e) {

			}
		}
		// Need to think of a better way of doing this
		/*
		 * TableDetails dual = DBSystem.allTables.get(tableName); int
		 * previousLastPage = dual.getLastPageOfTable();
		 * DBSystem.allTables.put(tableName, ReadCSV.readCSV(tableName)); int
		 * newLastPage = dual.getLastPageOfTable(); if(previousLastPage ==
		 * newLastPage) cache.invalidate(tableName + previousLastPage);
		 */
	}

	//

	public void queryType(String query) {
		/*
		 * Deteremine the type of the query (select/create) and invoke
		 * appropriate method for it.
		 */

		/*
		 * Scanner s1= new Scanner(System.in);
		 * System.out.print("Enter the query: "); query = s1.nextLine();
		 */

		// Check syntax of query
		TGSqlParser sqlparser = new TGSqlParser(EDbVendor.dbvoracle);
		sqlparser.sqltext = query;

		int ret = sqlparser.parse();

		if (ret == 0) {
			// syntax ok! Now analyze the sql statement
			TStatementList sqlstatements = sqlparser.sqlstatements;

			for (int i = 0; i < sqlstatements.size(); i++) {
				analyzeStmt(sqlstatements.get(i));
				System.out.println("");
			}
		} else {
			System.out.println("Query syntax not OK !!! \n"
					+ sqlparser.getErrormessage());
		}

	}

	private void analyzeStmt(TCustomSqlStatement stmt) {

		switch (stmt.sqlstatementtype) {
		case sstselect:
			selectCommand((TSelectSqlStatement) stmt);
			break;
		case sstcreatetable:
			createCommand((TCreateTableSqlStatement) stmt);
			break;
		case sstupdate:
			break;
		
		case sstaltertable:
			break;
		case sstcreateview:
			break;
		default:
			System.out.println(stmt.sqlstatementtype.toString());
		}
	}

	/**
	 * 
	 * @param query
	 */
	private void createCommand(TCreateTableSqlStatement createStmt) {
		/*
		 * Use any SQL parser to parse the input query. Check if the table
		 * doesn't exists and execute the query. The execution of the query
		 * creates two files : <tablename>.data and <tablename>.csv. An entry
		 * should be made in the system config file used in previous
		 * deliverable. Print the query tokens as specified at the end.*format
		 * for the file is given below
		 */

		String tableName = createStmt.getTableName().toString();
		if(_tableList.containsKey(tableName.toLowerCase())){
			System.out.println("Table already exists!!");
			System.exit(0);
		}
		
		System.out.print("\nQuery type: \t Create");
		
		System.out.printf("\nTablename: \t%s", tableName);

		// create two new file (i.e. ".csv" and ".data" files)
		File csv = new File(tableName + ".csv");
		

		File data = new File(tableName + ".data");

		// make an entry in the config file
		File file = new File("config.txt");
		BufferedWriter bufferWritter = null;
		try {
			FileWriter fw = new FileWriter(csv.getName(), true);
			BufferedWriter buffWrt = new BufferedWriter(fw);
			buffWrt.write("");
			
			FileWriter fileWritter = new FileWriter(file.getName(), true);
			bufferWritter = new BufferedWriter(fileWritter);
			
			FileWriter dataWriter = new FileWriter(data.getName(), true);
			BufferedWriter bufferWritter1 = new BufferedWriter(dataWriter);
			
			bufferWritter.write("\nBEGIN");
			bufferWritter.write("\n" + tableName);

			System.out.print("\nAttributes: ");

			TColumnDefinitionList columnList = createStmt.getColumnList();

			for (int i = 0; i < columnList.size(); i++) {
				if (i >= 1) {
					System.out.print(",");
				}

				TColumnDefinition cd = columnList.getColumn(i);
				String colName = cd.getColumnName().toString();
				System.out.printf("  %s", colName);

				String colType = cd.getDatatype().toString();
				System.out.printf("  %s", colType);

				bufferWritter.write("\n" + colName + "," + colType);
				
				bufferWritter1.write("\n" + colName + "," + colType);

			}
			
			bufferWritter.write("\nEND");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (bufferWritter != null)
					bufferWritter.close();
			} catch (Exception e) {

			}
		}

		
			
	}

	private void selectCommand(TSelectSqlStatement selectStmt) {
		/*
		 * Use any SQL parser to parse the input query. Perform all validations
		 * (table name, attributes, datatypes, operations). Print the query
		 * tokens as specified below.
		 */


		String targetTable = //"employee";
		selectStmt.joins.getJoin(0).getTable().toString();
		
		if(!_tableList.containsKey(targetTable.toLowerCase())){
			System.out.println("Query is invalid !!");
			System.exit(0);
		}
		
		System.out.print("\nQuery type: \tSelect");
	
		//table name
		System.out.printf("\nTable:   \t%s", targetTable);
		
		
		// select list
		TResultColumnList resultColumnList = selectStmt.getResultColumnList();
		System.out.print("\nColumns: ");

		for (int i = 0; i < resultColumnList.size(); i++) {
			TResultColumn resultColumn = resultColumnList.getResultColumn(i);
			
			if("*".equals(resultColumn.getExpr().toString())){
				System.out.printf("\t%s", _tableList.get(targetTable));
			}
			else
			 System.out.printf("\t%s", resultColumn.getExpr().toString());
		}
		
	
		// distinct clause
		if (selectStmt.getSelectDistinct() != null) {
			long colNumber = selectStmt.getSelectDistinct().getColumnNo();
			System.out.println("\nDistinct: " + colNumber);
		//	System.out.printf("\nDistinct: \t%s", resultColumnList.getResultColumn((int)colNumber).getExpr().toString()	);
		}
		else{
			System.out.printf("\nDistinct: \t%s", "NA");
		}

		// where clause
		if (selectStmt.getWhereClause() != null) {
			System.out.printf("\nCondition: \t%s", selectStmt.getWhereClause()
					.getCondition().toString());
		}
		
		else{
			System.out.printf("\nCondition: \t%s", "NA");
		}

		// order by
		if (selectStmt.getOrderbyClause() != null) {
			System.out.printf("\nOrderby:");
			for (int i = 0; i < selectStmt.getOrderbyClause().getItems().size(); i++) {
				System.out.printf("\t%s", selectStmt.getOrderbyClause()
						.getItems().getOrderByItem(i).toString());

			}
		}
		
		else{
			System.out.printf("\nOrderby: \t%s", "NA");
		}

		// group by
		TGroupBy groupBy = selectStmt.getGroupByClause();

		if (groupBy != null) {
			
			//	Assuming only one column in group By clause
			System.out.printf("\nGroupby: \t%s", groupBy.getItems().getGroupByItem(0).getExpr().toString());
		}
		
		else{
			System.out.printf("\nGroupby: \t%s", "NA");
		}

		// having
		if (groupBy != null && groupBy.getHavingClause() != null) {
			System.out.printf("\nHaving : \t%s", groupBy.getHavingClause());
		}
		
		else{
			System.out.printf("\nHaving: \t%s", "NA");
		}
		
	}

}
