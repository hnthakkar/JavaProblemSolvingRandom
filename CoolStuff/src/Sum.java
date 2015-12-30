import java.util.HashSet;
import java.util.Set;


public class Sum {

	public static void main(String[] str){
		int array[] = new int[] {12, 14, 17, 15, 19, 20, -11};
		int target;
		int sum = 9;
		Set set = new HashSet<Integer>(); 
		for(int ori:array){
			target = sum - ori;
			if(set.contains(target))
				System.out.println(" Pair : (" + ori + "," + target + ")");
			else
				set.add(ori);
		}
	}
}
