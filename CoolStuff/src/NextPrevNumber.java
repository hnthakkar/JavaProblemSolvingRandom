import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class NextPrevNumber {

	static BufferedReader br;

	public static void main(String[] str) throws IOException {
		String number;
		int offset;

		while (true) {
			br = new BufferedReader(new InputStreamReader(System.in));
			number = br.readLine();
			offset = Integer.parseInt(br.readLine());
			if(offset<10 && offset >-10)
				System.out.println(adjacentNumber(number, offset));
			else
				System.out.println("WRONG OFFSET!");
		}
	}

	/**
	 * returns the adjacent number(given in string) with the single digit given
	 * difference, without converting to Integer
	 * @param number
	 * @return
	 */
	public static String adjacentNumber(final String number, int carry) {
		if (number.isEmpty())
			return null;
		int length = number.length();
		StringBuffer buf = new StringBuffer();
		char sample;
		int digit;
		while (--length >= 0) {
			sample = number.charAt(length);
			if (carry == 0) {
				buf.append(sample);
			} else if (carry > 0) {
				digit = sample - '0' + carry;
				buf.append(digit % 10);
				carry = digit / 10;
			} else if (carry < 0) {
				digit = sample - '0' + carry;
				if (digit < 0 && length > 0) {
					digit += 10;
					carry = -1;
				} else {
					carry = 0;
				}
				if (!(digit == 0) || !(length == 0))
					buf.append(digit);
			}
		}
		if (carry == 1) {
			buf.append(carry);
		}

		if (buf.charAt(0) != '-')
			buf = buf.reverse();

		return buf.toString();
	}
}
