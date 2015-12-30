import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ReadMetadata extends DefaultHandler {

	// XML TAGS
	public static final String TAG_PAGE_SIZE = "pagesize";
	public static final String MAX_PAGE_TO_CACHE = "maxpagetocache";
	public static final String TAG_TABLE = "table";
	public static final String TAG_COLUMN = "column";
	public static int _pageSize;
	public static int _max_page_to_cache;
	
	//XML ATTRIBUTES
	public static final String ATTR_NAME = "name";
	public static final String ATTR_TYPE = "type";
	
	public String _value;
	public String _tableName;
	public Map<String,String> _columns;
	public static Map<String,Map<String,String>> _tableList = new HashMap<String,Map<String,String>>();
	public static Map<String,String> _config = new HashMap<String,String>();
	public static Map<Integer,RecordDetails> _recordDetail = new HashMap<Integer,RecordDetails>();
	
	public static final String  RECORD_DETAILS = "recordDetails";
	public static final String  PAGE_DETAILS = "pageDetails";
	
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equalsIgnoreCase(TAG_TABLE)) {
			_tableName = attributes.getValue(ATTR_NAME);
			_columns = new HashMap<String,String>();
		} else if (qName.equalsIgnoreCase(TAG_COLUMN)) {
			_columns.put(attributes.getValue(ATTR_NAME), attributes.getValue(ATTR_TYPE));
		}
	
	}

	public void endElement(String uri, String localName, String qName) throws SAXException {
		if(qName.equalsIgnoreCase(TAG_TABLE)){
			_tableList.put(_tableName, _columns);
			_tableName = null;
			_columns = null;
		} else if(qName.equalsIgnoreCase(TAG_PAGE_SIZE)){
			_pageSize = Integer.parseInt(_value);
			_config.put(TAG_PAGE_SIZE, _value);
		} else if(qName.equalsIgnoreCase(MAX_PAGE_TO_CACHE)){
			_max_page_to_cache = Integer.parseInt(_value);
			_config.put(MAX_PAGE_TO_CACHE, _value);
		}
	}
	
	public void characters(char[] buffer, int start, int length) {
        _value = new String(buffer, start, length);
	}

	/*public void readTableList() {
        //System.out.println("No of Tables : " + _tableList.size());
        for (Map.Entry<String, Map<String,String>> entry : _tableList.entrySet()) {
        	//System.out.println("\n **********************************");
            //System.out.println("Table Name : " + entry.getKey()); 
            Map<String,String> value = entry.getValue();
            for (Map.Entry<String,String> colEntry : value.entrySet()) {
            	//System.out.println("Column Name : " + colEntry.getKey() + " of type : " + colEntry.getValue());
            }
            //System.out.println("\n **********************************");
        }
    }
	
	public void readConfig() {
		//System.out.println("\n **********************************");
		//System.out.println("No of Config Parameters: " + _config.size());
		for (Map.Entry<String, String> entry : _config.entrySet()){
			//System.out.println("Config Name : " + entry.getKey() + " has value : " + entry.getValue());
		}
		//System.out.println("\n **********************************");
	}*/
}
