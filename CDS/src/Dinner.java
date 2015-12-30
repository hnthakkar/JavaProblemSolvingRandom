public class Dinner {
       public static void main(String[] args) throws Exception {
               final Philosopher[] philosophers = new Philosopher[5];
              Object[] chopsticks = new Object[philosophers.length];
              for (int i = 0; i < chopsticks.length; i++) {
                      chopsticks[i] = new Object();
              }
        for (int i = 0; i < philosophers.length; i++) {
              Object left = chopsticks[i];
              Object right = chopsticks[(i + 1) % chopsticks.length];
              if(i==0){
                   philosophers[i] = new Philosopher(right, left);
              }else{
                     philosophers[i] = new Philosopher(left, right);
              }
              Thread t = new Thread(philosophers[i], "Phil " + (i + 1));
              t.start();
     }
  }
}