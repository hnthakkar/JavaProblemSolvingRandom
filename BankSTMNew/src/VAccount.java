import jvstm.Atomic;
import jvstm.VBox;

class VAccount implements Account {

	private VBox<String> name = new VBox<String>();
	private VBox<Long> balance = new VBox<Long>();

	private VBox<Long> credit = new VBox<Long>();
	private VBox<Long> debit = new VBox<Long>();
	private VBox<String> datetime = new VBox<String>();

	VAccount(long balance) 
	{
		setBalance(balance);
	}

	public long getBalance() {
		return balance.get();
	}

	void setBalance(long newBalance) {
		this.balance.put(newBalance);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name.get();
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name.put(name);
	}

	/**
	 * @return the credit
	 */
	public Long getCredit() {
		return this.credit.get();
	}

	/**
	 * @param credit
	 *            the credit to set
	 */
	public void setCredit(Long credit) {
		this.credit.put(credit);
	}

	/**
	 * @return the debit
	 */
	public Long getDebit() {
		return this.debit.get();
	}

	/**
	 * @param debit
	 *            the debit to set
	 */
	public void setDebit(Long debit) {
		this.debit.put(debit);
	}

	/**
	 * @return the datetime
	 */
	public String getDatetime() {
		return this.datetime.get();
	}

	/**
	 * @param datetime
	 *            the datetime to set
	 */
	public void setDatetime(String datetime) {
		this.datetime.put(datetime);
	}

	// ////////////////////////////////////Transactional Method
	// /////////////////////

	@Atomic
	public void withdraw(long amount) {
		setBalance(getBalance() - amount);
	}

	@Atomic
	public void deposit(long amount) {
		setBalance(getBalance() + amount);
	}

	public boolean canWithdraw(long amount) {
		return amount < getBalance();
	}

}// //////////////////////////////////////////////////////////////
