import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jvstm.CommitException;
import jvstm.NestedTransaction;
import jvstm.ReadWriteTransaction;
import jvstm.Transaction;

public class BankAccountTransaction extends Thread{

	private static Random RND = new Random();
	public TimeSlicer timeSlicer;

	final static String[] states = {"No-Trans","Trans-Started","Trans-Committed","Trans-Aborted","Nested-Trans-Aborted", "Nested-Trans-Started", "Nested-trans-committed", "Trans-withdraw-done"};
	final int state_not_started = 0;
	final int state_started = 1;
	final int state_committed = 2;
	final int state_aborted = 3;
	final int state_nested_trans_aborted = 4;
	final int state_nested_trans_started = 5;
	final int state_nested_trans_committed = 6;
	final int state_trans_withdraw_done = 7;
	
	int currentState = 0;
	//int accNo1,accNo2;
	int accNo1;
	long initialBalance1,initialBalance2,afterWithdraw1,afterWithdraw2;
	int noOfToAccount;
	List<Integer> toAccounts = null;
	
	static int restarts = 0;
	static int success = 0;
	static int nested_restart = 0;

	BankAccountTransaction(TimeSlicer ptimeSlicer) {
		timeSlicer = ptimeSlicer;
	}

	@Override
	public void run(){
		//each thread performs 10 transaction
		for(int i = 0; i < 10; i++){
			transferAmount();
		}
		
	}
	
	void transferAmount() {
		try {
			while (true) {
				currentState = state_not_started;
				Thread.sleep(10);
				timeSlicer.checkTimeState();
				RunMain.getThreadStates(timeSlicer);
				Transaction transaction = Transaction.begin();
				currentState = state_started;
				Thread.sleep(10);
				timeSlicer.checkTimeState();
				RunMain.getThreadStates(timeSlicer);
				//System.out.println("\n" + Thread.currentThread() + ":Begin: New Transaction");
				try {
					toAccounts = null;
					transferAmount_Internal(transaction);
					transaction.commit();
					countSuccess();
					currentState = state_committed;
					Thread.sleep(10);
					timeSlicer.checkTimeState();
					RunMain.getThreadStates(timeSlicer);
					//System.out.println(Thread.currentThread() + ":Commit *****\n");
					transaction = null;
					return;
				} catch (CommitException ce) {
					//System.out.println(Thread.currentThread() + ":Abort *****\n");
					currentState = state_aborted;
					Transaction.abort();
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					timeSlicer.checkTimeState();
					RunMain.getThreadStates(timeSlicer);
					transaction = null;
					countRestart();
				} finally {
					if (transaction != null) {
						transaction.abort();
						transaction = null;
					}
				}
				//System.out.println(Thread.currentThread() + ":Retry: as transaction was failed due to conflict *****\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static synchronized void countRestart() {
		restarts++;
	}
	
	static synchronized void countSuccess() {
		success++;
	}
	
	static synchronized void countNestedRestart() {
		nested_restart++;
	}
	
	private void transferAmount_Internal(Transaction parentTransaction) {
		try {
			accNo1 = RND.nextInt(RunMain.accounts.length);
			
			//withdraw from ONE account
			Account acc1 = RunMain.accounts[accNo1];
			initialBalance1 = acc1.getBalance();
			acc1.withdraw(initialBalance1/2);
			acc1.setDebit(initialBalance1/2);
			
			currentState = state_trans_withdraw_done;
			Thread.sleep(10);
			timeSlicer.checkTimeState();
			RunMain.getThreadStates(timeSlicer);

			//afterWithdraw1 = acc1.getBalance();
			long withdrawnAmt = initialBalance1/2;
			//Randomly deposit in Multiple accounts
			noOfToAccount = RND.nextInt(3);
			if(noOfToAccount == 0)
				noOfToAccount = 1;
			//deposit in another account.
			//if noOfToAccount == 0, then it was withdraw transaction
			//while(noOfToAccount > 0){
			long totalDepositAmt = 0;
			NestedTransaction nt = null;
			
			for(int i = 0; i < noOfToAccount; i++){
				while(true){
					int toAcct = -1;
					try{
						nt = new NestedTransaction((ReadWriteTransaction)parentTransaction);
						nt.start();
						currentState = state_nested_trans_started;
						Thread.sleep(10);
						timeSlicer.checkTimeState();
						RunMain.getThreadStates(timeSlicer);
						
						if(toAccounts == null)
							toAccounts = new ArrayList<Integer>();
						toAcct = RND.nextInt(RunMain.accounts.length);
						while(accNo1 == toAcct || toAccounts.contains(toAcct)){
							toAcct = RND.nextInt(RunMain.accounts.length);
						}
						toAccounts.add(toAcct);
						Account acc2 = RunMain.accounts[toAccounts.get(i)];
						initialBalance2 = acc2.getBalance();
						long amt = i == (noOfToAccount-1) ? (withdrawnAmt-totalDepositAmt) :(withdrawnAmt/noOfToAccount);
						totalDepositAmt += amt;
						acc2.deposit(amt);
						acc2.setCredit(amt);
						afterWithdraw2 = acc2.getBalance();
						/*currentState = state_started;
						Thread.sleep(10);
						timeSlicer.checkTimeState();
						RunMain.getThreadStates(timeSlicer);*/
						nt.commitTx(true);
						currentState = state_nested_trans_committed;
						Thread.sleep(10);
						timeSlicer.checkTimeState();
						RunMain.getThreadStates(timeSlicer);
						break;
					}catch(Exception e){
						
						nt.abortTx();
						currentState = state_nested_trans_aborted;
						Thread.sleep(10);
						timeSlicer.checkTimeState();
						RunMain.getThreadStates(timeSlicer);
						toAccounts.remove((Object)toAcct);
						countNestedRestart();
					}
				}
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*static void mySleep(long millis, int nanos) {
		try {
			Thread.sleep(millis, nanos);
		} catch (InterruptedException ie) {
		}
	}*/
}