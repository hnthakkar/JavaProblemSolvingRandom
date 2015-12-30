import java.io.IOException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

public class LoggerExample {

	public static Logger logger = Logger.getLogger(LoggerExample.class);

	static {
		Logger rootLogger = Logger.getRootLogger();
		rootLogger.setLevel(Level.INFO);

		// Define log pattern layout
		PatternLayout layout = new PatternLayout("[%d{dd-MM HH:mm:ss:SSS}] %-5r %-5p [%c] (%t:%x) %m%n");
		try {
			// Define file appender with layout and output log file name
			RollingFileAppender fileAppender = new RollingFileAppender(layout, "example.log");

			fileAppender.setAppend(false);
			fileAppender.setMaxFileSize("25MB");
			fileAppender.setMaxBackupIndex(100);
			fileAppender.setImmediateFlush(false);
			fileAppender.setName("mylog");
			fileAppender.setBufferedIO(true);
			fileAppender.setBufferSize(1000);
			rootLogger.addAppender(fileAppender);
		} catch (IOException e) {
			System.out.println("Failed to add appender !!");
		}

	}
	
	public static void main(String[] str){
		int i = 0;
		while(true){
			logger.info("Value of i : " + i++);
		}
	}

}