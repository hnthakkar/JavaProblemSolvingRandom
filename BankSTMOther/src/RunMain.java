/**
 * This is the main class to start this application 
 * 
 */
public class RunMain 
{
	
	static int count = 1;
	
	// Note: Limitation here is: no-of-accounts should be divisible by num_of_threads. 
	// 
	public final static int numAccounts = 3;
	public final static int numThreads = 3;
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) 
	{

  // Run the garbage collector
	 Runtime runtime = Runtime.getRuntime();
  runtime.gc();
  
		final BankAccountTransaction bankAccountTransaction = new BankAccountTransaction(numAccounts);
		
	 long initialTotalAmount = 0;
	 
		System.out.println("=============================================================");
		for (int i = 0; i < numAccounts; i++) 
		{
			//System.out.println("Initial Balance in a/c:" + i + "=" + bankAccountTransaction.accounts[i].getBalance());
			initialTotalAmount = initialTotalAmount + bankAccountTransaction.accounts[i].getBalance();
		}
		
	 System.out.println("Sum of initial Balance in All Accounts : " + initialTotalAmount);
	 
		System.out.println("=============================================================");

		Client clients[] = new Client[numThreads];

		// Creating the Clients(Threads)
		for (int i = 0; i < numThreads; i++) {
			clients[i] = new Client(bankAccountTransaction);
			clients[i].setNum_accounts(numAccounts); 
			clients[i].setNum_threads(numThreads); 
		}

		final long startTime = System.currentTimeMillis();

		// start all the Threads
		for (int i = 0; i < numThreads; i++) {
			clients[i].start();
		}

		// waiting for all the to finish there work (can use cyclic barrier)
		for (int i = 0; i < numThreads; i++) 
		{
 			try 
 			{
 				clients[i].join();
 			} catch (Throwable t) 
 			{
 				throw new Error("error:" + t.getMessage());
 			}
		}

		// display a/c final status
		long finalTotalAmount = 0;
		//System.out.println("=============================================================");
		for (int i = 0; i < numAccounts; i++) {
			//System.out.println("Final Balance in a/c:" + i + "=" + bankAccountTransaction.accounts[i].getBalance());
			finalTotalAmount += bankAccountTransaction.accounts[i].getBalance();
		}
		//System.out.println("=============================================================");

		System.out.println("Final Total balance : " + finalTotalAmount);
		System.out.println("Restarts = " + BankAccountTransaction.restarts);
		System.out.println("Success = " + BankAccountTransaction.success);
		System.out.println("Total Time Taken : " + (System.currentTimeMillis() - startTime) + " milliseconds");
  
	 
	 long memory = runtime.totalMemory() - runtime.freeMemory();
  //System.out.println("Used memory is bytes: " + memory);
  System.out.println("Used memory is megabytes: " + memory / (1024L * 1024L));
  
  
	}
	
}
