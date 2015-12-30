import jvstm.Atomic;
import jvstm.VBox;

/**
 * This is a Stock class with minimal fields 
 * 
 * @author sstomar
 *
 */
public class Stock 
{
  private String symbol;

  private VBox<Long> price = new VBox<Long>();

 
 Stock(String inSymbol, long balance) 
 {
     setPrice(balance);
 }

 public  @Atomic long getPrice() 
 {
     return price.get();
 }

 public @Atomic void setPrice(long newPrice) 
 {
   this.price.put(newPrice);
 }
 
 

 public String getSymbol() {
  return symbol;
 }

 public void setSymbol(String symbol) {
  this.symbol = symbol;
 }


 
 //////////////////////////////////////Transactional Method /////////////////////
 
 
 
 
}