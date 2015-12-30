import java.util.Set;

public class RunMain {
	
	static int count = 1;
	
	public static Account accounts[];
	public static void main(String[] args) {

		final int numAccounts = 10;
		final int numThreads = 4;
		final int initailAmt = 10000;
		
		accounts = new Account[numAccounts];
		for (int i = 0; i < numAccounts; i++) {
			accounts[i] = new VAccount(initailAmt);
		}

		TimeSlicer timeSlicer = new TimeSlicer();
		
		System.out.println("=============================================================");
		for (int i = 0; i < numAccounts; i++) {
			System.out.println("Initial Balance in a/c:" + i + "=" + accounts[i].getBalance());
		}
		System.out.println("Sum of Total Amount in all the Accounts :" + (initailAmt * numAccounts));
		System.out.println("=============================================================");

		BankAccountTransaction bat[] = new BankAccountTransaction[numThreads];

		for (int i = 0; i < numThreads; i++) {
			bat[i] = new BankAccountTransaction(timeSlicer);
			bat[i].setName("T" + i);
			bat[i].start();
		}

		
		// waiting for all the Threads to finish there work (can use cyclic barrier)
		for (int i = 0; i < numThreads; i++) {
			try {
				bat[i].join();
			} catch (Throwable t) {
				throw new Error("error:" + t.getMessage());
			}
		}

		// display a/c final status
		long finalTotalAmount = 0;
		System.out.println("=============================================================");
		for (int i = 0; i < numAccounts; i++) {
			System.out.println("Final Balance in a/c:" + i + "=" + accounts[i].getBalance());
			finalTotalAmount += accounts[i].getBalance();
		}
		System.out.println("=============================================================");

		/*System.out.println("TestTransfers\t NumThreads:" + numThreads
				+ "\t NumAcconts:" + numAccounts + "\t TimeDiff:"
				+ (System.currentTimeMillis() - start));*/

		System.out.println("Final Total balance : " + finalTotalAmount);
		System.out.println("Restarts = " + BankAccountTransaction.restarts);
		System.out.println("Nested Restarts = " + BankAccountTransaction.nested_restart);

		// System.out.println("Counter = " +
		// bankAccountTransaction.counter.getCount());

	}
	
	public static synchronized void getThreadStates(TimeSlicer timeSlicer) {
		try {
			timeSlicer.freezeTime();
			Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
			Thread[] threadArray = threadSet.toArray(new Thread[threadSet.size()]);
			StringBuffer buf = new StringBuffer();
			for (int j = 0; j < threadArray.length; j++) {
				if (j == 0){
					buf.setLength(0);
					buf.append("\n At t" + count + "| ");
				}	
				if (threadArray[j] instanceof BankAccountTransaction) {
					BankAccountTransaction obj = (BankAccountTransaction) threadArray[j];
					if(obj.currentState != 0 && obj.toAccounts != null)
						buf.append(obj.getName() + ":" + BankAccountTransaction.states[obj.currentState]  + ":#" + obj.accNo1 + ":#" + obj.toAccounts.toString() + "|");
					else if(obj.currentState == 0 || obj.currentState == 1)
						buf.append(obj.getName() + ":" + BankAccountTransaction.states[obj.currentState] + "|");
					else 
						buf.append(obj.getName() + ":" + BankAccountTransaction.states[obj.currentState] + ":#" + obj.accNo1 + "|");
				}	
			}
			System.out.println(buf.toString());
			count++;
			timeSlicer.unFreezeTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}
