
public class MainTest {

	public static void main(String arg[]){
		SomeWorker sm = new SomeWorker();
		sm.setDaemon(true);
		sm.start();
		System.out.println("Main Thread will exit now!!!");
		//System.exit(0);
	}
	
	
}

class SomeWorker extends Thread{
	
	@Override
	public void run(){
		int i = 0;
		while(true){
			System.out.println("I value :" + i++);
		}
	}
}