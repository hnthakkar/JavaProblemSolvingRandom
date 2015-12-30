import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class First42 {

	public static void main(String[] str) throws Throwable{
		Scanner scanner = new Scanner(System.in);
		int number;
		while(true){
			number = scanner.nextInt();
			if(number == 42)
				break;
			System.out.println(number);
		}
		
		List l = new ArrayList();
		//l.toa
	}
}
