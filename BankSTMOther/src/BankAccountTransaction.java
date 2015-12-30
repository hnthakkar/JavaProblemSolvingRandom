
import java.util.Hashtable;
import java.util.Random;

import jvstm.CommitException;
import jvstm.NestedTransaction;
import jvstm.ReadWriteTransaction;
import jvstm.Transaction;

public class BankAccountTransaction {

	private static Random RND = new Random();
	Account accounts[];
	static int restarts = 0;
	static int success = 0;
 StringBuffer logMessage; 
 
	//StringBuilder logMessage; 
	
 private Hashtable<String, Logger> threadLog; 
 
 
 String threadName = Thread.currentThread().getName() ; 
 
	BankAccountTransaction(int numAccounts)
	{
		
	 threadLog = new Hashtable<String, Logger>(); 
	 
		accounts = new Account[numAccounts];

		for (int i = 0; i < numAccounts; i++) 
		{
			accounts[i] = new VAccount(100); // initial ammount 
		}
		
		logMessage = new StringBuffer();		
		//logMessage = new StringBuilder(); 
	}
	
	public void setLogger(String threadName, Logger logger)
	{
	 threadLog.put(threadName, logger); 
	}
	
	
	public  void showlog()
	{
	  synchronized(logMessage)
	  {
	   System.out.println(logMessage.toString());
	   logMessage.setLength(0);
	  // threadLog.get(Thread.currentThread().getName()).showlog(); 
	  }
	}
	
	public  void log(String log)
	{
	  logMessage.append("\n["+ Thread.currentThread().getName()+ "]:[" +
	    System.currentTimeMillis()%1000 +"]:"+ log); 
	 
	 // threadLog.get(Thread.currentThread().getName()).log("\n"+ Thread.currentThread().getName()+ ":" + log);
	 
	}
	
	/**
	 * This Method transfers amount by using NESTED transaction 
	 * 
	 */
	public void transferAmount_Nested()
	{
	 
	 ////////// Try Main transaction until SUCCEEED ///////////
	 showlog(); 
	 
	 while (true) 
  {
    Transaction transaction = Transaction.begin(false);
    
    int acctNo1 = RND.nextInt(accounts.length);  
    
    //// withdraw from ONE account 
    log("[Account#"+ acctNo1 +"]:Toplevel Transaction:BEGIN");
    
    Account acc1 = accounts[acctNo1];       
    log("[Account#" + acctNo1  + "]:Withdrawing" );   
    
    long value = acc1.getBalance() / 2;        
      
    acc1.withdraw(value);
    acc1.setDebit(value);    
   
    log("[Account#" + acctNo1  + "]:Withdrawn" +
      ", Balance=" + acc1.getBalance() +
      ", Debitted=" + acc1.getDebit());   
   
    
    try 
    {
      transferAmount_Internal(transaction, acctNo1, value);
      transaction.commitTx(true); 
      transaction = null;
     
      log("[Account#"+acctNo1+"]:Committed: Toplevel Transaction:END");
      
      
      showlog();
      return;
    }
    catch (CommitException ce)
    {
      
             
       log("[Account#"+acctNo1 +"]:Abort Toplevel Transaction *****"  ); 
       transaction.abortTx(); 
       transaction = null;
       //throw new Exception("Transaction Aborted"); 
       countRestart();
    }
    finally 
    {
      if (transaction != null) 
      {
        try{
        transaction.abortTx(); 
        }catch(Exception e){}
      }
    }
    
    log("[Account#ZZZ]:Retry: as transaction was failed due to conflict *****");
    
  }////////////End:while///////////////////////
	 
	 
	}/////////////////////////////////////////////////////
	
	
	static synchronized void countRestart() {
		restarts++;
	}
	
	static synchronized void countSuccess() {
		success++;
	}
	
	
	private void transferAmount_Internal(Transaction topLevel, int acctNo1, long value)
	{
	   
	    ////////////////////////////////////////////////////////////
	    NestedTransaction nestedTransaction = null; 
	    
	    long value2= 0L;
	    long value3 = 0L;
	    boolean success = false;  
	    int attempt = 0;
	    int acctNo2 = Integer.MIN_VALUE; 
	    
	    while(!success && attempt <= 3 )
	    {
	     nestedTransaction
                 = new NestedTransaction((ReadWriteTransaction) topLevel); 
	     
	     //log("Nested Transaction: Begin:"); 
	     
  	    try
  	    {
    	    nestedTransaction.start(); //(false);
    	    
    	    //acc1.withdraw(value);
    	    value2 = value/2;
    	    value3 = value - value2; 
    	    
    	    ////////// deposit in another account. ////////////
    	    acctNo2 = RND.nextInt(accounts.length);  
    	    while(acctNo2== acctNo1)
    	    {
    	     acctNo2 = RND.nextInt(accounts.length);
    	    }
    	    
    	    log("[Account#" + acctNo2  + "]:Nested Transaction:Begin:Trying to deposit"); 
    	    
    	    Account acc2 = accounts[acctNo2];        
    	    acc2.deposit(value2);        
    	    acc2.setCredit(value2);
    	    
    	    nestedTransaction.commitTx(true);
    	    
    	    log("[Account#" + acctNo2  + "]:Depositted, Balance=" + acc2.getBalance() 
           + ", Credited=" + acc2.getCredit());     	    
    	    log("[Account#" + acctNo2  + "]:Committed: Nested Transaction: End");    
    	    
    	    success=true; 
    	    nestedTransaction = null;
    	    
  	    }
  	    catch (CommitException ce) 
  	    {
  	      log("[Account#" + acctNo2  + "]:Nested Transaction " + nestedTransaction.getNumber() + " Failed, Abort"); 
  	      //throw new CommitException(); 
  	      nestedTransaction.abortTx();
  	      success = false; 
  	      attempt++;
  	      
  	      //return; 
  	    }
	    }	       
	    
	    //////////////////////2nd-nested transaction/////////////////////////
	    
	    nestedTransaction
	         = new NestedTransaction((ReadWriteTransaction) topLevel); 
	    
	    nestedTransaction.start(); // (false);
	    
	    int acctNo3 = RND.nextInt(accounts.length);  
	    
	    while(acctNo3== acctNo1)
     {
      acctNo3 = RND.nextInt(accounts.length);
     }
	    
	    log("[Account#" + acctNo3  + "]:Nested Transaction:Trying to deposit"); 
	    Account acc3 = accounts[acctNo3];        
	    
	    acc3.deposit(value3);        
	    acc3.setCredit(value3);
	    
	    nestedTransaction.commitTx(true);
	    
	    log("[Account#" + acctNo3  + "]:Depositted. "      
       + ", Balance=" + acc3.getBalance() 
       + ", Credited=" + acc3.getCredit() );
	   	    
	    ////////////////////////////////////////////////  
	   
	    log("[Account#" + acctNo3  + "]:Committed: Nested Transaction: End");    
	    
	   // mySleep(1000, 10);
	}
	
	
	
	/**
  * 
  */
 void transferAmount() 
 {
  while (true) 
  {
   Transaction transaction = Transaction.begin();
   // + ":Transaction#"+tx.getNumber()
   //System.out.println("\n" + Thread.currentThread() + ":Begin: New Transaction");
   try 
   {
    transferAmount_Internal();
    transaction.commit();
    countSuccess();
    //System.out.println(Thread.currentThread() + ":Commit *****\n");
    transaction = null;
    return;
   }
   catch (CommitException ce) 
   {
    //System.out.println(Thread.currentThread() + ":Abort *****\n");

    transaction.abort();
    transaction = null;
    countRestart();
   } finally 
   {
    if (transaction != null) {
     transaction.abort();
    }
   }
   //System.out.println(Thread.currentThread() + ":Retry: as transaction was failed due to conflict *****\n");
  }

 }


	/**
	 * Internal method which actually transfers amount from one account 
	 * to another account. 
	 * 
	 * Account are being picked up randomly. 
	 * 
	 * 
	 */
	private void transferAmount_Internal() 
	{
		int acctNo = RND.nextInt(accounts.length);
		// Account acc1 = ac[RND.nextInt(ac.length)];

		//withdraw from ONE account
		Account acc1 = accounts[acctNo];
		long value = acc1.getBalance() / 2;
		acc1.withdraw(value);
		acc1.setDebit(value);

		/*System.out.println(Thread.currentThread() + ":Withdrawn from a/c:"
				+ acctNo + ", Balance after withDrawn=" + acc1.getBalance()
				+ ", Debitted=" + acc1.getDebit());*/

		//deposit in another account.
		acctNo = RND.nextInt(accounts.length);
		Account acc2 = accounts[acctNo];
		acc2.deposit(value);
		acc2.setCredit(value);

		/*System.out.println(Thread.currentThread() + ":Deposit to a/c:" + acctNo
				+ ", Balance after Deposit=" + acc2.getBalance()
				+ ", Credited=" + acc2.getCredit());*/

		// counter.inc();

		//mySleep(1000, 10);
	}

	/*static void mySleep(long millis, int nanos) {
		try {
			Thread.sleep(millis, nanos);
		} catch (InterruptedException ie) {
		}
	}*/
}