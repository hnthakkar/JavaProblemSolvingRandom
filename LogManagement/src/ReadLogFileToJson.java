import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;


public class ReadLogFileToJson {
	final String file;
	int id = 1;
	List<JSONObject> jsonObjectList;
	
	public ReadLogFileToJson(String pfile){
		file = pfile;
		jsonObjectList = new ArrayList<JSONObject>();
		initailize();
	}
	
	private void initailize() {
		BufferedReader br = null;
	    try {
	    	br = new BufferedReader(new FileReader(file));
	        String line = br.readLine();
	        while (line != null) {
	            parseLineToJSONOject(line);
	            line = br.readLine();
	        }
	    } catch(Exception e){
	    	e.printStackTrace();
	    } finally {
	        try {
	        	if(br != null)
					br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
	}
	
	private void parseLineToJSONOject(String line){
		try {
			JSONObject obj = new JSONObject();
			obj.put("body", line);
			obj.put("Id", id++);
			checkForInteeligentData(obj, line);
			jsonObjectList.add(obj);
			//System.out.println(obj.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	private void checkForInteeligentData(JSONObject obj, String line){
		int fromIndex = 0;
		int index = 0;
		int attr_Start_Index = 0, attr_End_Index;
		char ch;
		String attr, value;
		try {
			while((index = line.indexOf("##", fromIndex)) != -1){
				attr_Start_Index = attr_End_Index = index+2;
				ch = line.charAt(attr_Start_Index);
				while( Character.isAlphabetic(ch) || Character.isDigit(ch)){
					attr_End_Index++;
					ch = line.charAt(attr_End_Index);
				}
				attr = line.substring(attr_Start_Index, attr_End_Index);
				attr_Start_Index = attr_End_Index = attr_End_Index + 2;
				ch = line.charAt(attr_Start_Index);
				while( Character.isAlphabetic(ch) || Character.isDigit(ch)){
					attr_End_Index++;
					ch = line.charAt(attr_End_Index);
				}
				value = line.substring(attr_Start_Index, attr_End_Index);
				obj.put(attr, value);
				fromIndex = attr_End_Index;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
