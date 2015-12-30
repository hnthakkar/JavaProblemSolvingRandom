package com.logging;

import java.util.UUID;

import org.apache.logging.log4j.EventLogger;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.message.StructuredDataMessage;

import com.logging2.OtherTestClass;

public class MainClass {
	
	private static final ExtendedLogger logger = ExtendedLogger.create(MainClass.class);
	
	public static void main(String[] str){
		//logger.entry();
		logger.trace("TRACE");
		logger.debug("DEBUG");
		logger.info("INFO");
		logger.splunkfeed("SPLUNKFEED");
		logger.warn("WARN");
		logger.error("ERROR");
		logger.fatal("FATAL");
		OtherTestClass.testMethod();
		/*StructuredDataMessage msg = new StructuredDataMessage("someString", null, "transfer");
		msg.put("toAccount", "some account");
		msg.put("fromAccount", "some other account");
		msg.put("amount", "777");
		EventLogger.logEvent(msg);
		logger.exit();*/
		//logger.catching(t);
		
	    //ThreadContext.put("clientCode", "CKF"); // Add the fishtag;
	   /* StructuredDataMessage msg = new StructuredDataMessage(ThreadContext.get("clientCode"), null, "transfer");
		msg.put("toAccount", "some account");
		msg.put("fromAccount", "some other account");
		msg.put("amount", "777");
		EventLogger.logEvent(msg);*/
		//logger.splunkfeed("SPLUNK FEED " + ThreadContext.get("clientCode"));
		//logger.error("ERROR");
	    //ThreadContext.clearAll();
	    //logger.entry();
		//memoryTestWithString();
		Runtime runtime = Runtime.getRuntime();
		runtime.gc();
		memoryTestWithString();
		//memoryTestWithObject();
		runtime.gc();
		long memory = runtime.totalMemory() - runtime.freeMemory();
		logger.splunkfeed("Used memory is bytes: " + memory);
		logger.splunkfeed("Used memory is megabytes: " + bytesToMegabytes(memory));
	}
	
	 /*private static final Logger logger = LogManager.getLogger("MainClass");
	 public static void main(String[] args) {
	 logger.info("Hello, World!");
	 }*/
	
	private static void memoryTestWithString(){
		Runtime runtime = Runtime.getRuntime();
		runtime.gc();
		for(int i=0; i < 100000; i++){
			logger.info("someInfo" + i + "someotherInfo" + (i/10));
		}
		
		/*long memory = runtime.totalMemory() - runtime.freeMemory();
		logger.splunkfeed("Used memory is bytes: " + memory);
		logger.splunkfeed("Used memory is megabytes: " + bytesToMegabytes(memory));*/
	}
	
	private static void memoryTestWithObject(){
		StructuredDataMessage msg = null;
		for(int i=0; i < 10000; i++){
			msg = new StructuredDataMessage("someInfo", null, "transfer");
			msg.put("someotherInfo", "i=" + i);
			//EventLogger.logEvent(msg);
		}
	}
	
	private static final long MEGABYTE = 1024L * 1024L;

	  public static long bytesToMegabytes(long bytes) {
	    return bytes / MEGABYTE;
	  }
}
