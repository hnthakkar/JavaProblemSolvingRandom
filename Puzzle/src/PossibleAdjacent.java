import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.AsyncAppender;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

public class PossibleAdjacent {
	public static final int primeArray[] = { 2, 3, 5, 7, 11, 13, 17, 19, 23,
			29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97,
			101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163,
			167, 173, 179, 181, 191, 193, 197, 199, 211, 223, 227, 229, 233,
			239, 241, 251, 257, 263, 269, 271, 277, 281, 283, 293, 307, 311,
			313, 317, 331, 337, 347, 349, 353, 359, 367, 373, 379, 383, 389,
			397 };

	public static final HashMap<Integer, List<Integer>> possibleAdjOfEven = new HashMap<Integer, List<Integer>>();
	public static final HashMap<Integer, List<Integer>> possibleAdjOfOdd = new HashMap<Integer, List<Integer>>();
	public final static AtomicInteger noOfSequences = new AtomicInteger(1);
	public static Logger logger = Logger.getLogger(PossibleAdjacent.class);
	
	static {
		if(MainClass.logNeeded){
			Logger rootLogger = Logger.getRootLogger();
			rootLogger.setLevel(Level.INFO);
			 
			// Define log pattern layout
			PatternLayout layout = new PatternLayout("%m%n");
			if(MainClass.consolePrint == 2){
				try {
					// Define file appender with layout and output log file name
					RollingFileAppender fileAppender = new RollingFileAppender(layout, "274083_arrangements_"+ MainClass.range +".log");
		
					fileAppender.setAppend(false);
					fileAppender.setMaxFileSize("25MB");
					fileAppender.setMaxBackupIndex(100);
					fileAppender.setImmediateFlush(false);
					fileAppender.setName("mylog");
					fileAppender.setBufferedIO(true);
					fileAppender.setBufferSize(1000);
					
					//AsyncAppender asyncAppender = new AsyncAppender();
					//asyncAppender.setBufferSize(1000);
					//asyncAppender.addAppender(fileAppender);
					// Add the appender to root logger
					rootLogger.addAppender(fileAppender);
				} 
				catch (IOException e) {
					System.out.println("Failed to add appender !!");
				}
			} else {
				rootLogger.addAppender(new ConsoleAppender(layout));
			}
		}
		boolean isOdd = true;
		List<Integer> adjList = null;
		for (int i = 1; i <= MainClass.range; i++) {
			adjList = new LinkedList<Integer>();
			int adj;
			for (int j = 0; j < primeArray.length; j++) {
				adj = primeArray[j] - i;
				if (adj > 0 && adj != i && adj <= MainClass.range) {
					adjList.add(primeArray[j] - i);
				}
			}
			if (isOdd) {
				possibleAdjOfOdd.put(i, adjList);
				isOdd = false;
			} else {
				possibleAdjOfEven.put(i, adjList);
				isOdd = true;
			}
		}
	}
	
	/**
	 * Overloaded method to find the possible adjacent for a given Odd number
	 * 
	 * @param lastNum
	 *            , last number in the arrangement (which is a Odd number)
	 * @return boolean, returns true if a valid arrangement is found
	 */
	public static boolean findAdjOfOdd(int lastNum, List arrangement) {
		int count = noOfSequences.intValue();
		try{
			if(MainClass.cb != null && count > MainClass.uptoCount)
				MainClass.cb.await();
		} catch(Exception e){
			System.out.println("Exception in findAdjOfOdd" + e.toString());
		}
		if (arrangement.size() == MainClass.range) {
			int arrangementCount = noOfSequences.getAndIncrement();
			if(MainClass.logNeeded)
				logger.info(" " + arrangementCount + ":" + arrangement + ":" + System.currentTimeMillis());
			return false;
		}

		List<Integer> possibleAdj = possibleAdjOfOdd.get(lastNum);
		for (Integer adj : possibleAdj) {
			if (arrangement.contains(adj))
				continue;
			arrangement.add(adj);
			if (findAdjOfEven(adj, arrangement)) {
				return true;
			} else {
				arrangement.remove(adj);
				continue;
			}
		}
		return false;
	}

	/**
	 * Overloaded method to find the possible adjacent for a given Even number
	 * 
	 * @param lastNum
	 *            , last number in the arrangement (which is a Even number)
	 * @return boolean, returns true if a valid arrangement is found
	 */
	public static boolean findAdjOfEven(int lastNum, List arrangement) {
		int count = noOfSequences.intValue();
		try{
			if(MainClass.cb != null && count > MainClass.uptoCount)
				MainClass.cb.await();
		} catch(Exception e){
			System.out.println("Exception in findAdjOfEven" + e.toString());
		}
		if (arrangement.size() == MainClass.range) {
			int arrangementCount = noOfSequences.getAndIncrement();
			if(MainClass.logNeeded)
				logger.info(" " + arrangementCount + ":" + arrangement + ":" + System.currentTimeMillis());
			return false;
		}
		
		List<Integer> possibleAdj = possibleAdjOfEven.get(lastNum);
		for (Integer adj : possibleAdj) {
			if (arrangement.contains(adj))
				continue;
			arrangement.add(adj);
			if (findAdjOfOdd(adj, arrangement)) {
				return true;
			} else {
				arrangement.remove(adj);
				continue;
			}
		}
		return false;
	}
}