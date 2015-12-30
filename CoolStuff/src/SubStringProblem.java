
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SubStringProblem {
	static BufferedReader br;

	public static void main(String[] args) throws IOException {
		br = new BufferedReader(new InputStreamReader(System.in));
		int t = Integer.parseInt(br.readLine());
		while (t-- > 0) {
			final String input = br.readLine();
			final int strlen = input.length();
			int len = 1;
			String result;
			Integer pos = null;
			while (len < strlen) {
				pos = findPos(input, len);
				if (pos != null) {
					break;
				}
				len++;
			}
			if (pos == null) {
				result = input;
				pos = 0;
			} else {
				result = input.substring(pos, pos + len);
			}
			printAnswer(result, pos);
		}
	}

	/**
	 * 
	 * @param input, the number as String
	 * @param len, length of the number
	 * @return
	 */
	public static Integer findPos(final String input, final int len) {
		if (len < 1 || input == null || input.isEmpty()) {
			return null;
		}
		final int strlen = input.length();
		if (len > strlen) {
			return null;
		}
		String next;
		boolean found = false;
		for (int i = 0; i < len; i++) {
			if (i + len > strlen) {
				return null;
			}
			String subString = input.substring(i, i + len);
			if (i > 0) {
				String prev = prevString(subString);
				String temp = input.substring(0, i);
				if (prev.lastIndexOf(temp) != prev.length() - temp.length()) {
					continue;
				}
			}
			// check next
			next = nextString(subString);
			int pos = i + len;
			boolean cont = false;
			while (pos + next.length() <= strlen) {
				if (input.indexOf(next, pos) != pos) {
					cont = true;
					break;
				} else {
					pos = pos + next.length();
					next = nextString(next);
				}
			}
			if (cont) {
				continue;
			}
			if (strlen - pos > 0) {
				String subNext = next.substring(0, strlen - pos);
				if (input.indexOf(subNext, pos) != pos) {
					continue;
				}
			}
			return i;
		}
		return null;
	}

	/**
	 * Method to find the next number, without converting to Integer
	 * @param input
	 * @return
	 */
	public static String nextString(final String input) {
		if (input == null) {
			return null;
		}
		final int len = input.length();
		StringBuilder sb = new StringBuilder();
		int carry = 1, temp;
		for (int i = len - 1; i >= 0; i--) {
			if (carry == 1) {
				if (input.charAt(i) == '9') {
					sb.append('0');
				} else {
					sb.append(input.charAt(i) - '0' + carry);
					carry = 0;
				}
			} else {
				sb.append(input.charAt(i) - '0');
			}
		}
		if (carry == 1) {
			sb.append(carry);
		}
		sb = sb.reverse();
		return sb.toString();
	}

	/**
	 * Method to find the previous number, without converting to Integer
	 * @param input
	 * @return
	 */
	public static String prevString(final String input) {
		if (input == null) {
			return null;
		}
		final int len = input.length();
		StringBuilder sb = new StringBuilder();
		int borrow = 1, temp;
		for (int i = len - 1; i >= 0; i--) {
			if (borrow == 1) {
				if (input.charAt(i) == '0') {
					sb.append('9');
				} else {
					if (i != 0 || input.charAt(i) != '1') {
						sb.append(input.charAt(i) - '0' - borrow);
					}
					borrow = 0;
				}
			} else {
				sb.append(input.charAt(i) - '0');
			}
		}
		sb = sb.reverse();
		return sb.toString();
	}

	public static void printAnswer(String input, int pos) {
		long ret = 0;
		long ten = 9;
		int len = input.length();
		int mod = 1000000007;
		for (int m = 1; m < len; m++) {
			ret += ten * m;
			ret %= mod;
			ten = ten * 10 % mod;
		}
		long num = 0;
		for (int i = 0; i < len; i++) {
			num = num * 10 + (input.charAt(i) - (i == 0 ? '1' : '0'));
			num %= mod;
		}
		ret += num * len;
		ret++;
		ret += mod - pos;
		System.out.println(ret % mod);
	}
}