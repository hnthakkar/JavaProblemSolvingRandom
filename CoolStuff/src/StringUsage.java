import java.util.StringTokenizer;


public class StringUsage {
	public static void main(String[] str){
		Integer i = new Integer(6);
		System.out.println(i);
		someMethod(i);
		System.out.println(i);
	}
	
	private static void someMethod(Integer i){
		i=7;
	}
}
