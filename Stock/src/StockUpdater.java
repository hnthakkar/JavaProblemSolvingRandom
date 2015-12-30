
import java.util.concurrent.atomic.AtomicLong;

import jvstm.CommitException;
import jvstm.Transaction;

public class StockUpdater implements Runnable {

 private int countDown = 5;
 private static AtomicLong mCounter = new AtomicLong(); 
 private Stock stock;

 public StockUpdater(Stock inStock) {
  stock = inStock;
 }

 public void run() 
 {
   Transaction transaction = null;
   String threadName = Thread.currentThread().getName();
   Long newPrice = null;
  
   while (countDown > 0) 
   {
     try 
     {
      
      transaction = Transaction.begin();
      System.out.println(threadName  +":-------------------------------------" );    
      
      newPrice = stock.getPrice() + mCounter.getAndIncrement(); 
      
      Thread.sleep(1000);
      
      stock.setPrice(newPrice); 
      
      transaction.commit();
      System.out.println(threadName +":Commit: newPrice=" + newPrice + " *****\n");
      
     }
     catch (CommitException ce)
     {
       System.out.println(threadName  +":Abort: newPrice=" + newPrice + " *****\n" );       
       transaction.abort();
       transaction = null;
     }
     catch (InterruptedException e)
     {
       
     }
            
     //System.out.println("Quote update by thread:" +threadName+ ":, current price " + stock.getPrice());
     countDown = countDown - 1;
   
  }/////////// while loop/////
 }
}