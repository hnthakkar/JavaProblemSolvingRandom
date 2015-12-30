
public class StockApplication {

 public static void main(String[] args) throws Exception 
 {
 
  Stock stock = new Stock("APPL", new Long(600));

  Thread[] readerThreads = new Thread[10];
  Thread[] updateThreads = new Thread[5];
  
  for (int i = 0; i < 10; i++) 
  {
   readerThreads[i] = new Thread(new StockReader(stock));
   readerThreads[i].setName("#" + i);
  }
  for (int i = 0; i < 5; i++) 
  {
   updateThreads[i] = new Thread(new StockUpdater(stock));
   updateThreads[i].setName("#" + i);
  }
  
  ///////////////////////////////////////////
  for (int i = 0; i < 10; i++)
   readerThreads[i].start();

  for (int i = 0; i < 5; i++)
   updateThreads[i].start();

  Thread.sleep(3000);

 }
}
