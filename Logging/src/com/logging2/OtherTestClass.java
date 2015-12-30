package com.logging2;

import com.logging.ExtendedLogger;

public class OtherTestClass {
	
	private static final ExtendedLogger logger = ExtendedLogger.create(OtherTestClass.class);
	
	public static void testMethod(){
		//logger.entry();
		logger.trace("OtherTestClass - TRACE");
		logger.debug("OtherTestClass - DEBUG");
		logger.info("OtherTestClass - INFO");
		logger.splunkfeed("OtherTestClass - SPLUNKFEED");
		logger.warn("OtherTestClass - WARN");
		logger.error("OtherTestClass - ERROR");
		logger.fatal("OtherTestClass - FATAL");
		
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
	}
	
	 /*private static final Logger logger = LogManager.getLogger("MainClass");
	 public static void main(String[] args) {
	 logger.info("Hello, World!");
	 }*/
}
