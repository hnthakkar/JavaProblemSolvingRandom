
	import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
   
	public class CreateSortedSubLists{
	    
		private static String getField(String line) {
	    	return line.split(",")[0];// extract value you want to sort on
	    }

		public static void main(String[] args) throws Exception {
			int i=0;
			while(i<3)
			{
	    		String fname="C:/Users/mamatha/Desktop/practicejava/practice/temp/c_";
				BufferedReader reader = new BufferedReader(new FileReader(fname+i+".csv"));
				Map<Integer, List<String>> map = new TreeMap<Integer, List<String>>();
				String line = reader.readLine();//read header
				while ((line = reader.readLine()) != null) {
		    		Integer key = Integer.parseInt(getField(line));
		    		List<String> l = map.get(key);
		    		if (l == null) {
		    			l = new LinkedList<String>();
		    			map.put(key, l);
		    		}
		    		l.add(line);
		    	}
		    	reader.close();
		    	File f = new File("C:/Users/mamatha/Desktop/practicejava/practice/temp1/");
		    	f.mkdir();		//create temp1 directory.
		    	FileWriter writer = new FileWriter("C:/Users/mamatha/Desktop/practicejava/practice/temp1/s"+i+".csv");
		    
		    	for (List<String> list : map.values()) {
		    		for (String val : list) {
		    			writer.write(val);
		    			writer.write("\n");
		    		}
		    	}
		    	writer.close();
		    	i++;
			}
		}
	}

