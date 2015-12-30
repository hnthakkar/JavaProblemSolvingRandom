import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

/**
 * Thread to generate sequence starting with a particular number
 * 
 * @author Payal(279653)
 * 
 */
public class TaskThread implements Runnable {

	private CyclicBarrier barrier;

	ArrayList<Integer> input;
	private int startsWith;
	// maximum no. of sequences to be generated
	private int maxSequences;

	public static Logger logger = Logger.getLogger(TaskThread.class);

	private static SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss.SSS");

	private final static int primeNumbers[] = { 2, 3, 5, 7, 11, 13, 17, 19, 23,
			29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97,
			101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163,
			167, 173, 179, 181, 191, 193, 197, 199, 211, 223, 227, 229, 233,
			239, 241, 251, 257, 263, 269, 271, 277, 281, 283, 293, 307, 311,
			313, 317, 331, 337, 347, 349, 353, 359, 367, 373, 379, 383, 389,
			397 };

	private static Set<Integer> primes;
	private final static AtomicInteger noOfSequences = new AtomicInteger(1);

	// Add all the prime numbers in the list
	static {
		primes = new HashSet<Integer>(primeNumbers.length);
		for (int i : primeNumbers) {
			primes.add(i);
		}

	}

	public TaskThread(CyclicBarrier barrier, ArrayList<Integer> input,
			int startsWith, int maxSequences) {
		this.input = (ArrayList<Integer>) input.clone();

		// {eg. startWith=5, then input = {5,2,3,4,1,6,7..}
		Collections.swap(this.input, 0, startsWith - 1);
		this.startsWith = startsWith;
		this.barrier = barrier;
		this.maxSequences = maxSequences;
	}

	@Override
	public void run() {
		try {
			nextSequence(input, 0, startsWith);
			this.barrier.await();

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
		} catch (BrokenBarrierException e) {
			// TODO Auto-generated catch block
		}
	}

	public void nextSequence(ArrayList<Integer> input, int startIndex,
			int startsWith) {

		if (noOfSequences.get() > maxSequences) {
			return;
		}
		
		int len = input.size();
		if (len == startIndex) {

			if (input.get(0) != startsWith) {
				return;
			}

			logger.info("Sequence " + noOfSequences.getAndIncrement() + " ; "
					+ Arrays.toString(input.toArray()) + " "
					+ sdf.format(new Date()));
		}
		else {
			boolean rangeIsOdd = len % 2 == 1;

			for (int i = startIndex; i < len; i++) {

				Collections.swap(input, i, startIndex);

				// If sequence does not start with the given starting number
				if ((startIndex == 0) && input.get(startIndex) < startsWith) {
					Collections.swap(input, startIndex, i);
					continue;
				}

				// If range is odd number, sequence will never start with even
				// number
				if (startIndex == 0 && rangeIsOdd
						&& (input.get(startIndex) % 2) != 1) {
					Collections.swap(input, startIndex, i);
					continue;
				}

				// If two adjacent numbers do not add to prime - do not permute
				if (startIndex > 0 && startIndex == i
						&& !isValid(input, startIndex)) {
					Collections.swap(input, startIndex, i);
					continue;

				}

				// If two adjacent numbers do not add to prime - do not permute
				if (startIndex > 0 && (startIndex == len - 1 || i > startIndex)
						&& !isValid(input, startIndex)) {
					Collections.swap(input, startIndex, i);
					continue;
				}

				nextSequence(input, startIndex + 1, startsWith);

				Collections.swap(input, startIndex, i);
			}

		}
	}

	/**
	 * Check if the given number and its adjacent number to its left add upto
	 * prime
	 * 
	 * @param input
	 * @param index
	 * @return
	 */
	public boolean isValid(ArrayList<Integer> input, int index) {

		if (!primes.contains(input.get(index) + input.get(index - 1))) {

			return false;
		}
		return true;
	}

}
