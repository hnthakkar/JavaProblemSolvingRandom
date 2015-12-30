import java.util.Scanner;

public class PrimeGenerator {

	public static void main(String[] arg) {
		Scanner scanner = new Scanner(System.in);
		int testCases = scanner.nextInt();
		long startNum = 0;
		long endNum = 0;
		for (int t = 0; t < testCases; t++) {
			startNum = scanner.nextLong();
			if (startNum == 2){
				System.out.println(startNum);
				startNum = 3;
			} else if (startNum % 2 == 0)
				startNum++;
			
			endNum = scanner.nextLong();
			long rem = 0;
			for (long i = startNum; i <= endNum;) {
				rem = i % 6;
				if ((rem == 1 || rem == 5) && isPrime(i))
					System.out.println(i);
				i += 2;
			}
		}
	}

	private static boolean isPrime(long number) {
		boolean isPrime = true;
		long checkTill = (long)Math.sqrt(number);
		for (long i = 3; i <= checkTill;) {
			if (number % i == 0) {
				isPrime = false;
				break;
			}
			i += 2;
		}
		return isPrime;
	}
}
