import java.util.Hashtable;
import java.util.logging.*;


public class Logger
{
 
 StringBuffer logMessage; 
 
 public Logger()
 {
  // TODO Auto-generated constructor stub
  logMessage = new StringBuffer(); 
 }
 
 
 public void showlog()
 {
   System.out.println(logMessage.toString());
   logMessage.setLength(0);
 }
 
 public void log(String log)
 {
  logMessage.append("\n"+ Thread.currentThread().getName()+ ":" + log); 
 }
 
 
}
