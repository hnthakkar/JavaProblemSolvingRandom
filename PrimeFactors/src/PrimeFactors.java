import java.util.ArrayList;
import java.util.List;


public class PrimeFactors {
	
	public static void main(String[] arg){
		int number = 67232321;
		int copyOfNumber = number;
		List factors = new ArrayList<Integer>();
		factors.add(1);
		for(int i = 2; i < Math.ceil((double)number/2); i++){
			if(i > copyOfNumber)
				break;
			if(copyOfNumber % i == 0){
				factors.add(i);
				copyOfNumber /= i;
				i--;
				
			}
		}
		factors.add(number);
		System.out.println(factors);
	}

}
