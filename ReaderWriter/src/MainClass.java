import java.util.Set;

public class MainClass {

	public static void main(String[] arg){
		FifoReadWriterLock readWriter = new FifoReadWriterLock();
		TimeSlicer timeSlicer = new TimeSlicer();
		
		//Creating 4 ReadClients
		for(int i = 0; i < 4; i++){
			ReaderClient readerClient = new ReaderClient(timeSlicer,readWriter,1200);
			readerClient.setName("R" + i);
			readerClient.start();
		}
		
		//Creating 2 WriteClients
		for(int i = 0; i < 2; i++){
			WriterClient writerClient = new WriterClient(timeSlicer,readWriter,1400);
			writerClient.setName("W" + i);
			writerClient.start();
		}	
		
		
		int counter = 1;
		while (true) {
			try {
				//After every 1 second pause all the Thread and get their state
				Thread.sleep(1000);
				//Set a boolean to true, which is referred by all the Threads in their run method 
				timeSlicer.freezeTime();
				Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
				Thread[] threadArray = threadSet.toArray(new Thread[threadSet.size()]);
				for (int j = 0; j < threadArray.length; j++) {
					if(j==0)
						System.out.print("\n At time t"+ counter + " | ");
					if (threadArray[j] instanceof Client)
						System.out.print(((Client) threadArray[j]).getName() +":"+ ((Client) threadArray[j]).isActive + " | ");
				}
				//System.out.print("\n_______________________________________________________________________________________________");
				counter++;
				//Set the boolean to false
				timeSlicer.unFreezeTime();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
