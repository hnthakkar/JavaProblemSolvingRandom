import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

import org.apache.log4j.Logger;
import org.apache.log4j.RollingFileAppender;

public class MainClass{

	public static int range;
	public static int consolePrint;
	public static boolean checkForEvenstart = false;
	public static int uptoCount;
	public static long startTime, endTime;
	public static CyclicBarrier cb;
	public static boolean logNeeded;
	
	public static void main(String[] arg) {
		try{
			consolePrint = Integer.parseInt(arg[0]);
			range = Integer.parseInt(arg[1]);
			uptoCount = Integer.parseInt(arg[2]);
		} catch(Exception e) {
			System.out.println("Problems with the Inputs");
		}
		logNeeded = consolePrint!=1 ? true: false;
		int barrierRange = 0;
		checkForEvenstart = range%2==0 ? true: false;
			
		startTime = System.currentTimeMillis();
		
		if(range < 7){
			boolean isOdd = true;
			List list;
	        for(int i=1; i<=range;i++){
	        	list = new ArrayList();
	        	list.add(i);
	        	if(isOdd){
	        		PossibleAdjacent.findAdjOfOdd(i, list);
	        		isOdd = false;
	        	} else {
	        		if(checkForEvenstart)
	        			PossibleAdjacent.findAdjOfEven(i, list);
	        		isOdd = true;
	        	}
	        		
	        }
	        endTime = System.currentTimeMillis();
	        System.out.println("Time taken ## " + (endTime - startTime) + " ## milliseconds");
	        System.out.println("Total arrangements found for " + range + " : " + PossibleAdjacent.noOfSequences.decrementAndGet());
	        if(consolePrint == 2){
		        RollingFileAppender rfa = (RollingFileAppender)Logger.getRootLogger().getAppender("mylog");
	        	rfa.setImmediateFlush(true);
	        	Logger.getLogger("mylog").info("ALL DONE");
	        }	
		} else {
			if(checkForEvenstart){
				barrierRange = range;
			} else {
				double d = (double)range/2;
				barrierRange = (int)Math.ceil(d);
			}	
			//creating CyclicBarrier 
	        cb = new CyclicBarrier(barrierRange, new Runnable(){
	            @Override
	            public void run(){
	                //This task will be executed once all thread reaches barrier
	            	endTime = System.currentTimeMillis();
	                System.out.println("Time taken ## " + (endTime - startTime) + " ## milliseconds");
	                System.out.println("Total arrangements found for " + range + " : " + PossibleAdjacent.noOfSequences.decrementAndGet());
	                if(consolePrint == 2){
		                try{
		                	RollingFileAppender rfa = (RollingFileAppender)Logger.getRootLogger().getAppender("mylog");
		                	rfa.setImmediateFlush(true);
		                	Logger.getLogger("mylog").info("ALL DONE");
		                } catch(Exception e){
		                	System.out.println("Exception in cyclic barrier run" + e.toString());
		                }
	                }    
	                System.exit(0);
	            }
	        });
	        
	        boolean isOdd = true;
	        for(int i=1; i<=range;i++){
	        	if(isOdd){
	        		new Thread(new Task(i)).start();
	        		isOdd = false;
	        	} else {
	        		if(checkForEvenstart)
	        			new Thread(new Task(i)).start();
	        		isOdd = true;
	        	}	
	        }
		}
    }
}
