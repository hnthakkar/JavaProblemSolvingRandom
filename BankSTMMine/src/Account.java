
public interface Account {

	public long getBalance();

	public void withdraw(long amount);

	public void deposit(long amount);

	public boolean canWithdraw(long amount);

	public void setCredit(Long credit);

	public Long getCredit();

	public void setDebit(Long dedit);

	public Long getDebit();

}
