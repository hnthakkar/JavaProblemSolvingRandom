import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class chunks {
	public static void main(String[] args) throws Exception {
		FileReader fileReader = new FileReader("countries.csv");
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		String line="";
		int fileSize = 0, i = 0;
		File f = new File("./temp/");
		f.mkdir();		//create temp directory.
		BufferedWriter fos = new BufferedWriter(new FileWriter("./temp/c_"+i+".csv",true));
	while((line = bufferedReader.readLine()) != null) {
			//TODO: Change to page size
		    if(fileSize + line.getBytes().length > 100 ){
		        fos.flush();
		        fos.close();
		        i++;
		        fos = new BufferedWriter(new FileWriter("./temp/c_"+i+".csv",true));
		        fos.write(line + "\n");
		        fileSize = line.getBytes().length;
		    }else{
		        fos.write(line + "\n");
		        fileSize += line.getBytes().length;
		    }
		}          
		fos.flush();
		fos.close();
		bufferedReader.close();
	}
}
