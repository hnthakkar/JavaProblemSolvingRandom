import java.util.Scanner;
import java.util.Stack;


public class ONP {

	public static void main(String[] str){
		Scanner scan = new Scanner(System.in);
		String line = null;
		Stack<Character> stack = new Stack<Character>();
		char[] charArray = null;
		int testCases = scan.nextInt();
		for(int t= 0; t < testCases; t++) {
			line = scan.next();
			charArray = line.toCharArray(); 
			for(int i= 0; i < charArray.length; i++){
				if(Character.isLetter(charArray[i])){
					System.out.print(charArray[i]);
					continue;
				} 
				
				if( !stack.isEmpty() && charArray[i] == ')'){
					char ch = stack.peek();
					while(ch != '('){
						System.out.print(stack.pop());
						if(stack.isEmpty())
							break;
						ch = stack.peek();
					}
					stack.pop();
					continue;
				}	
				
				while(!stack.isEmpty() && !isOpPreMoreThenStackOp(stack.peek(),charArray[i])){
					System.out.print(stack.pop());
				}
				stack.push(charArray[i]);
			}
			
			char ch = '\0';
			while(!stack.isEmpty()){
				ch = stack.pop();
				if(ch != '(')
					System.out.print(ch);
			}
			stack.clear();
			System.out.println();
		}	
		
	}
	
	public static boolean isOpPreMoreThenStackOp(char stackTopOperator, char operator){
		switch(operator){
			case '^' :
				if(stackTopOperator == '^')
					return false;
				return true;
				
			case '*':
			case '/':
			case '~':
				if(stackTopOperator == '^' || stackTopOperator == '*' || stackTopOperator == '/' || stackTopOperator == '~')
					return false;
				return true;
				
				
			case '+':
			case '-':
				if(stackTopOperator == '^' || stackTopOperator == '*' || stackTopOperator == '/' || stackTopOperator == '~' || stackTopOperator == '+' || stackTopOperator == '-')
					return false;
				return true;
				
			case '(':
			case '=':
				return true;
			
			default:
				return false;
							
		}
	}
}
