/* ID6 Group 6
 * Georgios Dimoudis AM : 5212
 * Charilaos Chatzidimitriou AM : 5387
 * Omiros Chatziiordanis AM : 5388
 */

public class Calculator
{

    // Method to convert infix expression to postfix
    public static String infixToPostfix(String infix)
    {
        // initializing empty String for result
        StringBuilder postfix = new StringBuilder();
        // initializing empty stack
        Stack<Character> stack = new Stack<>();
        // for each character in the infix expression
        for (int i = 0; i < infix.length(); i++)
        {
            char c = infix.charAt(i);
            // if the character is a digit, add it to the result
            if (Character.isDigit(c))
            {
                postfix.append(c);
            }
            // if there is a left parenthesis, push it to the stack
            else if (c == '(')
            {
                stack.push(c);
            }
            // if there is a right parenthesis, pop the stack until the left parenthesis is found
            else if (c == ')')
            {
                while (!stack.isEmpty() && stack.peek() != '(')
                {
                    postfix.append(stack.pop());
                }
                // pop the left parenthesis
                stack.pop();
            }
            else
            {
                while (!stack.isEmpty() && precedence(c) <= precedence(stack.peek()))
                {
                    postfix.append(stack.pop());
                }
                stack.push(c);
            }
        }
            while (!stack.isEmpty())
            {
                postfix.append(stack.pop());
            }
        return postfix.toString();
    }

    // Method to check precedence of operators
    public static int precedence(char c)
    {
        if (c == '+' || c == '-')
        {
            return 1;
        }
        if (c == '*' || c == '/')
        {
            return 2;
        }
        return 0;
    }

    private static void calculate(String expr)
    {
        // Create a stack of integers
		Stack<Integer> vals = new Stack<>();
        // Create a stack of operators
		Stack<Character> ops = new Stack<>();

        // Scan all characters one by one
        int N = expr.length();
        char[] a = expr.toCharArray();
		int temp = 0;
		int op = 0;
        
        // Iterate through the expression
		for (int i=0; i<N; i++){
			Character c = a[i];
			
            // If the scanned character is a digit, push it to the stack
			if(Character.getNumericValue(c) >= 0 && Character.getNumericValue(c) <= 9){
                if(i-1>=0 && Character.getNumericValue(a[i-1]) >= 0 && Character.getNumericValue(a[i-1]) <= 9){
                    int temp2 = vals.pop();
                    temp2 = temp2*10 + Character.getNumericValue(c);
                    vals.push(temp2);
                }
				else{vals.push(Character.getNumericValue(c));}
			
            // If the scanned character is an operator, push it to the stack
			}else if(c.equals( "+".charAt(0)) || c.equals( "*".charAt(0)) || c.equals(  "-".charAt(0))){
				ops.push(c);
				op ++;
		
            // If the scanned character is an "(" , ignore it
			}else if(c.equals("(".charAt(0))){
				temp ++;
			
            // If the scanned character is an ")" , pop two elements from the stack and apply the operator
			}else if(c.equals( ")".charAt(0))){
				if (op == 0)break;
				char help = ops.pop();
		
                // If the operator is "+" , pop two elements from the stack and add them
				if(help == "*".charAt(0)){
					vals.push(vals.pop() * vals.pop());
				
                // If the operator is "-" , pop two elements from the stack and subtract them
				}else if(help == "+".charAt(0))
                {
					vals.push(vals.pop() + vals.pop());
				
                // If the operator is "*" , pop two elements from the stack and multiply them
				}else if(help == "-".charAt(0))
                {
					Integer num1 = vals.pop();
					Integer num2 = vals.pop();

					vals.push(num2 - num1);
				}
				temp --;
				op --;
			}
		}
		
		if(temp == 0 && op == 0){
			// Print the result with the logger
            System.out.println("Result: " + vals.pop());
		}else{
			// Warn the user that the expression is invalid
            System.out.println("Invalid expression");
            // Print where the error is
            System.out.println("Error at: " + expr);
		}
	}

	public static void main(String[] args){

        // Get the system current time in milliseconds
        long startTime = System.currentTimeMillis();
		String expr = args[0];
		
		// Print the expression and its length with the logger
        System.out.println("Expression: " + expr);
        System.out.println("Length: " + expr.length());
        // Log the expression in postfix form
		calculate(expr);
        System.out.println("Postfix: " + infixToPostfix(expr));

        // Get the system current time in milliseconds
        long endTime = System.currentTimeMillis();

        // Print the execution time with the logger
        System.out.println("Execution time: " + (endTime - startTime) + "ms");
	}
}