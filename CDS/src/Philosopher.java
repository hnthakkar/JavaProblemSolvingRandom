public class Philosopher implements Runnable {

   private final Object left;
   private final Object right;

   public Philosopher(Object left, Object right) {
     this.left = left;
     this.right = right;
   }
   private void ponder() throws InterruptedException {
     Thread.sleep(1000);
   }
   public void run() {
     try {
       while (true) {
         ponder(); // thinking
         synchronized (left) {
           ponder();
           synchronized (right) {
        	 System.out.println(Thread.currentThread().getName());
             ponder(); // eating
           }
         }
       }
     } catch (InterruptedException e) {
       Thread.currentThread().interrupt();
       return;
     }
   }
 }
