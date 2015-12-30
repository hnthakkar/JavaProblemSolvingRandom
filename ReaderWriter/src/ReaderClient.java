
public class ReaderClient extends Client {
	FifoReadWriterLock lock;
	long haltTime;
	TimeSlicer timeSlicer;
	
	public ReaderClient(TimeSlicer ts, FifoReadWriterLock readerWriterLock, long waitTime){
		lock = readerWriterLock;
		haltTime = waitTime;
		timeSlicer = ts;
	}
	
	@Override
	public void run(){
		while(true){
			try{
				timeSlicer.checkTimeState();
				lock.readLock().lock();
				isActive = true;
				//Read Some file
				Thread.sleep(haltTime);
			}catch(InterruptedException ie){
				//TODO
			} finally {
				lock.readLock().unlock();
				isActive = false;
				try {
					//wait for some time
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}	
	}

}
