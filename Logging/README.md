•	More than 50% of the log that we are feeding to Splunk are never used for any Dashboard or even searched. But of course logging is important part of the application.
Hence I have made a custom level of logging in log4j2 to distinguish the log message that we intend to feed to Splunk.
Following are the logging levels with their numeric value.

Trace(600)    >>    debug(500)    >>   info(400)   >> splunkfeed(350)    >>   warn(300)   >>  error(200)  >>  fatal(100)

Have done the configuration such that any logging statement with level splunkfeed or more specific will be written to a separate log file (i.e. SplunkFeed_appName.log), and all the other logging statement will be written to another log file (i.e. application.log) (we can do the settings that the splunkfeed logging message can also be added to application.log)
Code Example with all logging levels:
public class MainClass {
       
              private static final ExtendedLogger logger = ExtendedLogger.create(MainClass.class);
              public static void main(String[] str){
                     logger.trace("TRACE");
                     logger.debug("DEBUG");
                     logger.info("INFO");
                     logger.splunkfeed("Key=Value");
                     logger.warn("WARN");
                     logger.error("ERROR");
                     logger.fatal("FATAL");
              }
              
       }      // this ? written to SplunkFeed_appName.log file

This will significantly reduce the Splunk load and we might use our Splunk license far more effectively. 
All log messages in splunkfeed should be in the key=value format, Splunk automatically identifies such fields as interesting columns and we don’t have to configure an extractor for it.

Just in case if we need to reverse index application.log file as well, than we can use Plonk. 
Splunk has powerful Extractors and pipelined query, but reverse indexing and simple search is equally good in Plonk.

•	For a multithreaded application, where the log messages are interleaved, log4j recommends putting some value in the ThreadContext(i.e. like forClientCode=someValue or forUser=userName), in the method of filterClass(for web application) or serviceClass(for Webservices), and append this value to every logged message, so that we can distinctly identify log messages for a particular request. 

•	Many times we have a requirement, to find the elapsed time to cater a request (i.e. to execute a method), here the requirement changes very often and we might want to evaluate the sub-methods as well, so rather than flooding our java files with log messages we can use Aspect Oriented Programming to tap on a given method (like in spring we have around advice, which gives a callback before and after executing a method).

•	 Most of our applications are still using log4j-1.X version, the latest version log4j-2.X, is far advance and its Async appender uses a non-blocking technique and has much improved performance (reference)
Many times in PROD we need to change the logging level to get more details, but the application server requires to be re-started then. 
Log4j 2 has an attribute in the <Configuration> tag, which automatically detects changes in the configuration file after a given time and reconfigures itself. (reference)
