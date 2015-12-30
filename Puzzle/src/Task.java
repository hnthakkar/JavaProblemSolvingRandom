import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;

public class Task implements Runnable {

	//private CyclicBarrier barrier;
	private int startElem;
	private boolean startElemOdd;

	public Task(int strtElem) {
		//barrier = cb;
		startElem = strtElem;
		startElemOdd = (strtElem % 2) == 0 ? false : true;
	}

	@Override
	public void run() {
		List list = new ArrayList();
		list.add(startElem);
		try {
			if (startElemOdd) {
				PossibleAdjacent.findAdjOfOdd(startElem, list);
			} else {
				PossibleAdjacent.findAdjOfEven(startElem, list);
			}
			MainClass.cb.await();
		} catch (InterruptedException e) {
			System.out.println("Exception in run of task InterruptedException" + e.toString());
		} catch (BrokenBarrierException e) {
			System.out.println("Exception in run of task BrokenBarrierException" + e.toString());
		}
	}
}
