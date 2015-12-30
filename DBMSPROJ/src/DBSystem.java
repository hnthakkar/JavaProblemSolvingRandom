import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.TCustomSqlStatement;
import gudusoft.gsqlparser.TGSqlParser;
import gudusoft.gsqlparser.TStatementList;
import gudusoft.gsqlparser.nodes.TColumnDefinition;
import gudusoft.gsqlparser.nodes.TColumnDefinitionList;
import gudusoft.gsqlparser.nodes.TExpression;
import gudusoft.gsqlparser.nodes.TGroupBy;
import gudusoft.gsqlparser.nodes.TResultColumn;
import gudusoft.gsqlparser.nodes.TResultColumnList;
import gudusoft.gsqlparser.stmt.TCreateTableSqlStatement;
import gudusoft.gsqlparser.stmt.TSelectSqlStatement;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

	public static String _select_TargetTable = null;
	public static String _select_Columns = null;
	public static String _select_Condition = null;
	public static int[] _select_Column_order = null;
	public static boolean _has_Where_Cond = false;
	public static List _select_where_Column = null;
	public static List _select_OrderBy_Column = null;
	public static List _select_where_Column_cond = null;
	public static List _select_where_Column_Value = null;
	public static List _select_where_Multi_Connector = null;
	TGSqlParser sqlparser = null;

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
					if(line.toLowerCase().contains("primary_key")){
						
					}else if(line.toLowerCase().contains("end")){
						readColumn = false;
					}else{
						colList = _tableList.get(tableName.toString());
						if(colList == null){
							colList =  new ArrayList<String>(10);
						}
						colList.add(line.split(", ")[0]);
						_tableList.put(tableName.toString(), colList);
					}	
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
			//System.out.println("HIT :" + (tableName + pageNumber));
			page = cache.get(tableName + pageNumber);
		} else {
			//System.out.println("MISS :" + (tableName + pageNumber));
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
			String[] records = page.split("\n");
			return records[rd.getRecordNumberInPage() - 1].trim();
		}
		return null;
	}

	public void insertRecord(String tableName, String data) {
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
	}

	public void queryType(String query) {
		if(sqlparser == null)
			sqlparser = new TGSqlParser(EDbVendor.dbvoracle);
		sqlparser.sqltext = query;

		int ret = sqlparser.parse();
		if (ret == 0) {
			TStatementList sqlstatements = sqlparser.sqlstatements;
			for (int i = 0; i < sqlstatements.size(); i++) {
				analyzeStmt(sqlstatements.get(i));
				System.out.println("");
			}
		} else {
			System.out.println("Query syntax not OK !!! \n"	+ sqlparser.getErrormessage());
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
		String tableName = createStmt.getTableName().toString();
		if(_tableList.containsKey(tableName.toLowerCase())){
			System.out.println("Table already exists!!");
			return;
			//System.exit(0);
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
		_select_TargetTable = null;
		_select_Columns = null;
		_select_Condition = null;
		_select_Column_order = null;
		_has_Where_Cond = false;
		_select_where_Column = null;
		_select_where_Column_cond = null;
		_select_where_Column_Value = null;
		_select_where_Multi_Connector = null;

		String targetTable = selectStmt.joins.getJoin(0).getTable().toString();

		if(!_tableList.containsKey(targetTable.toLowerCase())){
			System.out.println("Query is invalid !!");
			return;
		}

		System.out.print("\nQuery type: \tSelect");

		_select_TargetTable = targetTable;
		System.out.printf("\nTable:   \t%s", targetTable);

		// select list
		TResultColumnList resultColumnList = selectStmt.getResultColumnList();
		System.out.print("\nColumns: ");

		for (int i = 0; i < resultColumnList.size(); i++) {
			TResultColumn resultColumn = resultColumnList.getResultColumn(i);

			if("*".equals(resultColumn.getExpr().toString())){
				_select_Columns = _tableList.get(targetTable).toString();
				String[] cols = _select_Columns.split(",");
				_select_Column_order = new int[cols.length];
				for(int j = 0; j < cols.length; j++ ){
					_select_Column_order[i] = i;
				}

				System.out.printf("\t%s", _tableList.get(targetTable));
			} else {
				_select_Columns = resultColumn.getExpr().toString();
				if(_select_Column_order == null)
					_select_Column_order = new int[resultColumnList.size()];
				List tableCols = _tableList.get(targetTable);
				for(int b=0; b<tableCols.size(); b ++){
					if(((String)tableCols.get(b)).equalsIgnoreCase(_select_Columns)){
						_select_Column_order[i] = b;
						break;
					}
				}
				System.out.printf("\t%s", resultColumn.getExpr().toString());
			}
		}

		// distinct clause
		if (selectStmt.getSelectDistinct() != null) {
			long colNumber = selectStmt.getSelectDistinct().getColumnNo();
			System.out.println("\nDistinct: " + resultColumnList);
		}else{
			System.out.printf("\nDistinct: \t%s", "NA");
		}

		// where clause
		if (selectStmt.getWhereClause() != null) {
			_has_Where_Cond = true;
			getWhereConditions(selectStmt);
			System.out.printf("\nCondition: \t%s", selectStmt.getWhereClause().getCondition().toString());
		}else{
			System.out.printf("\nCondition: \t%s", "NA");
		}

		// order by
		if (selectStmt.getOrderbyClause() != null) {
			System.out.printf("\nOrderby:");
			List tableCols = _tableList.get(_select_TargetTable);
			for (int i = 0; i < selectStmt.getOrderbyClause().getItems().size(); i++) {
				System.out.printf("\t%s", selectStmt.getOrderbyClause().getItems().getOrderByItem(i).toString());
				for(int b=0; b<tableCols.size(); b ++){
					if(((String)tableCols.get(b)).equalsIgnoreCase(selectStmt.getOrderbyClause().getItems().getOrderByItem(i).toString())){
						if(_select_OrderBy_Column == null)
							_select_OrderBy_Column = new ArrayList();
						_select_OrderBy_Column.add(b);
					}
				}
			}
		}else{
			System.out.printf("\nOrderby: \t%s", "NA");
		}

		// group by
		TGroupBy groupBy = selectStmt.getGroupByClause();

		if (groupBy != null) {
			//	Assuming only one column in group By clause
			System.out.printf("\nGroupby: \t%s", groupBy.getItems().getGroupByItem(0).getExpr().toString());
		}else{
			System.out.printf("\nGroupby: \t%s", "NA");
		}

		// having
		if (groupBy != null && groupBy.getHavingClause() != null) {
			System.out.printf("\nHaving : \t%s", groupBy.getHavingClause());
		}else{
			System.out.printf("\nHaving: \t%s", "NA\n");
		}
		executeSelectCommand();
	}


	public void executeSelectCommand() {
		TableDetails dual = DBSystem.allTables.get(_select_TargetTable);
		int totalRecordsInTable = dual.getNumberOfRecords();

		int fileSize = 0, numFiles = 0;
		BufferedWriter fos = null;

		try {
			List records = null;
			if(_has_Where_Cond && (_select_where_Multi_Connector == null || _select_where_Multi_Connector.contains("and")))
				records = getIntentiveRecordsForEquals();
			
			for (int i = 0; i <= totalRecordsInTable; i++) {
				String record = getRecord(_select_TargetTable, i);
				String[] cols = record.split(",");
				for (int a = 0; a < cols.length; a++) {
					cols[a] = cols[a].trim();
					if (cols[a].startsWith("\"") && cols[a].endsWith("\""))
						cols[a] = cols[a].substring(1, cols[a].length() - 1);
				}
				if (checkForCondition(cols)) {
					System.out.print("\n"); 
					for(int j =0; j <_select_Column_order.length; j++){ 
						if(j!=0)
							System.out.print(","); 
						System.out.print(cols[j]);
					}
				}
			}

		} /*catch (IOException io) {
			io.printStackTrace();
		}*/ finally {
				try {
					fos.flush();
					fos.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
		}
	}

	public List getIntentiveRecordsForEqualsAnd() {
		List allRecords = new ArrayList();
		HashMap map = allTables.get(_select_TargetTable).getIndex();
		List finalRecords = null;
		//for(int i=0; i<_select_where_Column.size(); i++) {
			HashMap colMap = (HashMap)map.get(_select_where_Column.get(0));
			List records = (List)colMap.get(_select_where_Column_Value.get(0));
			//if(_select_where_Multi_Connector != null){
				//if(((String)_select_where_Multi_Connector.get(i-1)).equalsIgnoreCase("and")){
					//records.
				//}
			//}
		//}
		return records;
	}
	
	public List getIntentiveRecordsForEqualsOr() {
		List allRecords = new ArrayList();
		HashMap map = allTables.get(_select_TargetTable).getIndex();
		List finalRecords = null;
		for(int i=0; i<_select_where_Column.size(); i++) {
			HashMap colMap = (HashMap)map.get(_select_where_Column.get(i));
			List records = (List)colMap.get(_select_where_Column_Value.get(i));
			if(_select_where_Multi_Connector != null){
				//if(((String)_select_where_Multi_Connector.get(i-1)).equalsIgnoreCase("and")){
					//records.
				//}
			//}
		//}
		return records;
	}
	
	public boolean checkForCondition(String[] cols){
		if(!_has_Where_Cond)
			return true;
		List allChecks = new ArrayList();
		for(int i=0; i<_select_where_Column.size(); i++){
			if(((String)_select_where_Column_cond.get(i)).equals("=")){
				if(!cols[(int)_select_where_Column.get(i)].equalsIgnoreCase((String)_select_where_Column_Value.get(i))){
					allChecks.add(0);
				}  else{
					allChecks.add(1);
				}
			} else if(((String)_select_where_Column_cond.get(i)).equals("!=")){
				if(cols[(int)_select_where_Column.get(i)].equalsIgnoreCase((String)_select_where_Column_Value.get(i))){
					allChecks.add(0);
				}  else{
					allChecks.add(1);
				}
			}else if(((String)_select_where_Column_cond.get(i)).equals("like")){
				if(!cols[(int)_select_where_Column.get(i)].toLowerCase().startsWith(((String)_select_where_Column_Value.get(i)).toLowerCase())){
					allChecks.add(0);
				}  else{
					allChecks.add(1);
				}
			}else if(((String)_select_where_Column_cond.get(i)).equals(">=")){
				if(Integer.parseInt(cols[(int)_select_where_Column.get(i)]) < Integer.parseInt((String)_select_where_Column_Value.get(i))){
					allChecks.add(0);
				}  else{
					allChecks.add(1);
				}
				
			}else if(((String)_select_where_Column_cond.get(i)).equals("<=")){
				if(Integer.parseInt(cols[(int)_select_where_Column.get(i)]) > Integer.parseInt((String)_select_where_Column_Value.get(i))){
					allChecks.add(0);
				}  else{
					allChecks.add(1);
				}
				
			}
		}
		int k =0;
		int result = (int)allChecks.get(k);
		if(_select_where_Multi_Connector != null){
			for(int j=0;j<_select_where_Multi_Connector.size();j++){
				if(((String)_select_where_Multi_Connector.get(j)).equalsIgnoreCase("and")){
					result = result & (int)allChecks.get(++k);
				} else {
					result = result | (int)allChecks.get(++k);
				}
			}
		}
		return result == 1;
	}

	public void getWhereConditions(TSelectSqlStatement selectStmt){
		getExpression(selectStmt.getWhereClause().getCondition().getLeftOperand(),selectStmt.getWhereClause().getCondition().getRightOperand());
	}

	public TExpression getExpression(TExpression leftExp, TExpression RightExp){
		if(RightExp.getOperatorToken() == null && leftExp.getOperatorToken() == null){
			String col = RightExp.getParentExpr().getLeftOperand().toString();
			List tableCols = _tableList.get(_select_TargetTable);

			for(int b=0; b<tableCols.size(); b ++){
				if(((String)tableCols.get(b)).equalsIgnoreCase(col)){
					if(_select_where_Column == null)
						_select_where_Column = new ArrayList();
					_select_where_Column.add(b);
					break;
				}
			}

			String op = RightExp.getParentExpr().getOperatorToken().toString();
			if(_select_where_Column_cond == null)
				_select_where_Column_cond = new ArrayList();
			_select_where_Column_cond.add(op);

			String value = RightExp.getParentExpr().getRightOperand().toString();
			if(value.endsWith("'") && value.startsWith("'"))
				value = value.substring(1,value.length()-1);
			if(_select_where_Column_Value == null)
				_select_where_Column_Value = new ArrayList();
			_select_where_Column_Value.add(value);
			return null;
		}

		getExpression(RightExp.getLeftOperand(), RightExp.getRightOperand());
		String multiCondConnector = RightExp.getParentExpr().getOperatorToken().toString();
		
		if(_select_where_Multi_Connector == null)
			_select_where_Multi_Connector = new ArrayList();
		_select_where_Multi_Connector.add(multiCondConnector);

		getExpression(leftExp.getLeftOperand(), leftExp.getRightOperand());
		return null;
	}
}