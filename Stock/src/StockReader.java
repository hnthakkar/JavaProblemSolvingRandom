
public class StockReader implements Runnable {

 private int countDown = 10;
 private Stock stock;

 public StockReader(Stock inStock) {
  stock = inStock;
 }

 public void run() 
 {
  while (countDown > 0) 
  {
   try 
   {
     Thread.sleep(1000);
     
     String x = Thread.currentThread().getName();   
     
     Long stockTicker = stock.getPrice();
     
     System.out.println("Stock Price read by thread (" + x + "), current price " + stockTicker);
     countDown = countDown - 1;
     
   } 
   catch (InterruptedException e) 
   {
   }
   
  }
 }

}