import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class merge {

	public static void main(String[] args) throws Exception {
		File f = new File("C:/Users/mamatha/Desktop/practicejava/practice/temp1/");
		int numOfRecords = 10;		//TODO: pagesize/length of 1 record
		int numSortedSubLists = f.listFiles().length;
		Comparator<Data> comparator = new RowComparator();
		PriorityQueue<Data> queue = 
	            new PriorityQueue<Data>(numSortedSubLists, comparator);

		BufferedReader br[] = new BufferedReader[numSortedSubLists];
		for(int i=0;i<br.length;i++)
		{
			br[i] = new BufferedReader(new FileReader("C:/Users/mamatha/Desktop/practicejava/practice/temp1/s"+i+".csv"));
			ArrayList<String> sortedSubList = new ArrayList<String>();
			for(int j=0;j<numOfRecords;j++){
				String line = br[i].readLine();
				if(line != null)
					sortedSubList.add(line);
				else
					break;
			}
			Data d = new Data();
			d.rows = sortedSubList;
			d.fileNum = i;
			queue.add(d);
			//arr.add(sortedSubList);
		}

		while(!queue.isEmpty()){

			Data d = queue.remove();
			System.out.println(d.rows.get(0));		//write to csv
			d.rows.remove(0);
			if(d.rows.size()==0){
				//reload from file
				boolean flag = false;
				for(int j=0;j<numOfRecords;j++){
					String line = br[d.fileNum].readLine();
					if(line != null)
						d.rows.add(line);
					else{
						flag = true;
						break;
					}
				}
				if(!flag)		//buffer not empty. File still has some data.
					queue.add(d);
			}
			else
				queue.add(d);
		}

		//merge


		for(int i=0;i<br.length;i++)
		{
			br[i].close();
		}
	}
}
