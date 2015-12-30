public class MediumCounter extends Counter {

	TimeSlicer timeSlicer;

	public MediumCounter(TimeSlicer ts) {
		this.timeSlicer = ts;
	}

	@Override
	public void run() {
		while (true) {
			timeSlicer.checkTimeState();
			try {
				Thread.sleep(225);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			i++;
		}
	}
}
