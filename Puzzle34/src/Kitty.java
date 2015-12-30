/*Sri wants to learn the Thread concept of Java and he wants to implement and test the thread in relation with what his newly bought kitty does everyday.
  He likes the way it plays after eating and the way it sleeps. 
He codes adding those functionality as given below and was expecting an output as 

Tom: Meoww..... Meoww..... 
Tom: Zzzz... 
Tom: Mmmmm, mice 

to be displayed repeatedly but then he finally got stuck with the some error. 

Code : 
 */
public class Kitty {
	public final String name;
	public final String food;
	public final String sound;

	public Kitty(String name, String food, String sound) {
		this.name = name;
		this.food = food;
		this.sound = sound;
	}

	public void eat() {
		System.out.println(name + ": Mmmmm, " + food);
	}

	public void play() {
		System.out.println(name + ": " + sound + " " + sound);
	}

	public void sleep() {
		System.out.println(name + ": Zzzz...");
	}

	public void live() {
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					eat();
					play();
					sleep();
				}
			}
		}).start();
	}

	public static void main(String... args) {
		new Kitty("Tom", "mice", "Meoww.....").live();
	}
}
/*
 * 1. Identify the error and 2. Explain him why is it happening and 3. Assist
 * him in as many as multiple ways to overcome the error he faces.
 * 
 * Note : Sri is reluctant to change or disturb his method names and variables.
 * Solve them accordingly :)
 */