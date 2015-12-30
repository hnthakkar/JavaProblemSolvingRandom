import java.util.Set;

public class MainClass {
	
	public static void main(String[] arg) {
		TimeSlicer timeSlicer = new TimeSlicer();

		/*  Instance of a slow Counter 	 */
		SlowCounter sc = new SlowCounter(timeSlicer);
		sc.setName("Slow Counter");
		sc.start();

		/*  Instance of a Medium Counter  */
		MediumCounter mc = new MediumCounter(timeSlicer);
		mc.setName("Medium Counter");
		mc.start();

		/*  Instance of a Fast Counter  */
		FastCounter fc = new FastCounter(timeSlicer);
		fc.setName("Fast Counter");
		fc.start();
		
		int counter = 0;
		while (true) {
			try {
				Thread.sleep(1000);
				timeSlicer.freezeTime();
				//System.out.println("All Counters Freezed!!");
				Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
				Thread[] threadArray = threadSet.toArray(new Thread[threadSet.size()]);
				for (int j = 0; j < threadArray.length; j++) {
					if(j==0)
						System.out.print("\n At time t"+ counter + " | ");
					if (threadArray[j] instanceof Counter)
						System.out.print(((Counter) threadArray[j]).getName()	+ " : i val= " + ((Counter) threadArray[j]).i + " | ");
				}
				System.out.print("\n____________________________________________________________________________________________________");
				counter++;
				//System.out.print("\nAll Counters Started Again!!");
				timeSlicer.unFreezeTime();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}