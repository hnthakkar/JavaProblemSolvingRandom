public class FastCounter extends Counter {

	TimeSlicer timeSlicer;

	public FastCounter(TimeSlicer ts) {
		this.timeSlicer = ts;
	}

	@Override
	public void run() {
		while (true) {
			timeSlicer.checkTimeState();
			try {
				Thread.sleep(115);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			i++;
			System.out.println("Fast Counter :" + i);
		}
	}
}
