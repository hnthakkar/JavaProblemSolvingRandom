import javax.swing.JFrame;


public class Main {
	final static int N = 5;
	/**
	 * @param args
	 */
	static CombiningTree counter = new CombiningTree((int) Math.pow(2,Math.ceil(Math.log10(N) / Math.log10(2))));

	public static void main(String[] args) {
		System.out.println("main :"+Thread.currentThread().getId());
		Thread t[] = new Thread[N];
		for (int i = 0; i < N; i++) {
			final int j = i;
			// thread for each philosopher
			t[i] = new Thread(new MyRunnable(counter,j));
			t[i].setName(j+"");
			t[i].start();
		}
		// wait on philosopher
		try {
			for (int i = 0; i < N; i++)
				t[i].join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
