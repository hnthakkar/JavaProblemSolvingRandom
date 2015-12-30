public class TimeSlicer {

	private boolean isTimeFreezed = false;

	public synchronized void freezeTime() {
		isTimeFreezed = true;
	}

	public synchronized void unFreezeTime() {
		isTimeFreezed = false;
		notifyAll();
	}

	public synchronized void checkTimeState() {
		while (isTimeFreezed) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
