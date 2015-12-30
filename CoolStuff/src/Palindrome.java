import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Palindrome {

	public static void main(String[] str) throws NumberFormatException, IOException {
		BufferedReader bi = new BufferedReader(new InputStreamReader(System.in));
		int testCases = Integer.parseInt(bi.readLine());
		int[] numberArray = null;
		int length = 0;
		String numberStr = null;
		//testCases
		for (int t = 0; t < testCases; t++) {
			numberStr = bi.readLine();
			length = numberStr.length();
			numberArray = new int[length];
			for (int i = 0; i < length; i++) {
				numberArray[i] = Integer.parseInt(String.valueOf(numberStr.charAt(i)));
			}
			numberArray = increment(numberArray, numberArray.length - 1);
			printNextPalin(numberArray);
		}
	}

	public static void printNextPalin(int[] numberArray) {
		int lower = 0, upper = 0, mid = 0;
		if (numberArray.length % 2 == 0) {
			upper = numberArray.length / 2;
			lower = upper - 1;
			mid = lower;

		} else {
			lower = (int) Math.floor(numberArray.length / 2) - 1;
			upper = lower + 2;
			mid = lower + 1;
		}
		while (true) {
			if (numberArray[lower] > numberArray[upper]) {
				printPalin(numberArray);
				return;
			}
			if (numberArray[lower] == numberArray[upper]) {
				lower--;
				upper++;
				continue;
			}
			if (numberArray[lower] < numberArray[upper]) {
				if (numberArray.length % 2 == 0) {
					numberArray = increment(numberArray, mid);
				} else {
					numberArray = increment(numberArray, mid);
				}
				printPalin(numberArray);
				return;
			}
		}
	}

	public static void printPalin(int[] numberArray) {
		int mid = 0, rev = 0;
		if (numberArray.length % 2 == 0){
			mid = (numberArray.length / 2) - 1;
			rev = mid;
		} else {
			mid = (int) Math.floor(numberArray.length / 2);
			rev = mid - 1;
		}
		for(int i = 0; i <= mid; i++)
			System.out.print(numberArray[i]);
		for(int i = rev; i > -1 ; i--)
			System.out.print(numberArray[i]);
		System.out.println();

	}

	public static int[] increment(int[] numberArray, int atIndex) {
		int sum = 0;
		int index = atIndex == 0 ? numberArray.length - 1 : atIndex;
		int[] newNumberArray = null;
		boolean carry = false;
		do {
			carry = false;
			sum = numberArray[index] + 1;
			numberArray[index] = sum % 10;
			if (sum > 9) {
				carry = true;
			}
			index--;
		} while (carry && index > -1);

		if (sum > 9 && index == -1) {
			newNumberArray = new int[numberArray.length + 1];
			newNumberArray[0] = 1;
			numberArray = newNumberArray;
		}
		return numberArray;
	}
}
