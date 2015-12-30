public class SlowCounter extends Counter {

	TimeSlicer timeSlicer;

	public SlowCounter(TimeSlicer ts) {
		this.timeSlicer = ts;
	}

	@Override
	public void run() {
		while (true) {
			timeSlicer.checkTimeState();
			try {
				Thread.sleep(440);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			i++;
		}
	}
}
