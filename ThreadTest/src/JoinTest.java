
public class JoinTest {
	public static void main(String[] arg){
		SomeOtherWorker[] threadCollection = new SomeOtherWorker[10];
		for (int i = 0; i < 10; i++) {
			threadCollection[i] = new SomeOtherWorker();
			threadCollection[i].setName("Name:" + i);
			threadCollection[i].start();
		}
		
		for (int i = 0; i < 10; i++) {
			try {
				System.out.println("Current Thread " + Thread.currentThread());
				threadCollection[i].join();
			} catch (Throwable t) {
				throw new Error("error:" + t.getMessage());
			}
		}
	}
}

class SomeOtherWorker extends Thread{
	
	@Override
	public void run(){
		int i = 0;
		while(i<10){
			try {
				Thread.sleep(100);
				i++;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}