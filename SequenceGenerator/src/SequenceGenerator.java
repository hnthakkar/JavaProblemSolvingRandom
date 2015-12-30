import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

/**
 * This class generates all possible sequences upto given limit in the given range such that
 * adjacent numbers add up to a prime number
 * 
 * @author Payal(279653)
 * 
 */
public class SequenceGenerator {

	ArrayList<Integer> input;

	public static void main(String args[]) {

		try {
			if (args.length != 3) {
				System.out.println("Enter three command line arguments - boolean, range and maximum sequences!!!");
				System.exit(0);
			}

			boolean printToLogFile = Boolean.parseBoolean(args[0]);
			int range = Integer.parseInt(args[1]);

			if (range < 1 || range > 200) {
				System.out.println("Sequence range should be between 1 to 200 only.");
				System.exit(0);

			}

			int maxSequences = Integer.parseInt(args[2]);
			setLogger(printToLogFile, range);

			System.out.println("Generating Sequences....");

			createThreads(range, maxSequences);

		} catch (Exception e) {
			System.out.println("Invalid arguments. Program exits !!!");
			System.exit(0);
		}

	}

	/**
	 * Create thread for sequence starting with each digit
	 * @param range
	 * @param maxSequences - number of sequences to be generated
	 */
	private static void createThreads(int range, int maxSequences) {
		final CyclicBarrier cb = new CyclicBarrier(range, new Runnable() {
			@Override
			public void run() {
				// This task will be executed once all thread reaches barrier
				System.out.println("\nAll sequences have been generated !! ");

			}
		});

		ArrayList<Integer> input = new ArrayList<Integer>(range);

		for (int i = 0; i < range; i++) {
			input.add(i, i + 1);
		}

		for (int i = 0; i < range; i++) {
			new Thread(new TaskThread(cb, input, i + 1, maxSequences),
					"Thread " + i).start();
		}
	}

	/**
	 * Set all logger properties
	 * @param printToLogFile - if false prints to colsone
	 * @param range -  Number range
	 */
	private static void setLogger(boolean printToLogFile, int range) {
		Logger rootLogger = Logger.getRootLogger();
		rootLogger.setLevel(Level.INFO);
		 
		// Define log pattern layout
		PatternLayout layout = new PatternLayout("%m%n");

		if (printToLogFile) {

			try {
				// Define file appender with layout and output log file name
				RollingFileAppender fileAppender = new RollingFileAppender(
						layout, "279653_primeseq_" + range + ".log");

				fileAppender.setAppend(false);
				fileAppender.setMaxFileSize("25MB");
				fileAppender.setMaxBackupIndex(100);
				// Add the appender to root logger
				rootLogger.addAppender(fileAppender);
			} 
			catch (IOException e) {
				System.out.println("Failed to add appender !!");
			}
		}

		else {
			// Add console appender to root logger
			rootLogger.addAppender(new ConsoleAppender(layout));
		}
	}

}