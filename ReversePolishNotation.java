import java.io.*;
import java.util.Stack;

/**
 * @author caiguihao
 * @version 1.0
 * @since 2018-03-10
 */

public class ReversePolishNotation {
    private String infixExpression = "";
    double result = 0;
    private StringBuffer reservedPolishNotation;
    private String fileName = "input";

    public static void main(String[] args) {
        ReversePolishNotation notation = new ReversePolishNotation();
        notation.write(3);
        notation.read();



    }
    public void write(int numOfExpressions) {
        try {
            FileWriter file = new FileWriter(fileName);
            BufferedWriter bufferWriter = new BufferedWriter(file);
            BinaryTree tree = new BinaryTree(4);
            StringBuffer sample = new StringBuffer(tree.getRandromExpression());
            bufferWriter.write(sample.toString());
            sample.setLength(0);
            bufferWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    public void read() {
        try {
            FileReader file = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(file);
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                // remove whitespace
                infixExpression = line.replaceAll("\\s+", "");
                System.out.println(infixExpression+"\nRPN:");
                convert();
            }
            bufferedReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void convert() {
        Stack<Character> operatorStack = new Stack<Character>();
        reservedPolishNotation = new StringBuffer();
        Character currentChar, first = infixExpression.charAt(0);
        int start = 0;
        if (isOperator(first)) {
            if (first == '-') {
                reservedPolishNotation.append("m ");
            } else if (first == '+') {
                reservedPolishNotation.append("p ");
            }
            start += 1;
        }
        for (int i = start; i < infixExpression.length(); i++) {
            currentChar = infixExpression.charAt(i);

            if (isOperator(currentChar)) {
                Character previous = infixExpression.charAt(i-1);
                if (!Character.isDigit(previous) && previous != ')') {
                    if (currentChar == '-') {
                        reservedPolishNotation.append("m ");
                    } else if (currentChar == '+') {
                        reservedPolishNotation.append("p ");
                    }

                } else if (operatorStack.isEmpty()) {
                    operatorStack.push(currentChar);
                } else if ( precedenceCmp(currentChar, operatorStack.peek()) ){
                    operatorStack.push(currentChar);
                } else {
                    while (!operatorStack.isEmpty() && !precedenceCmp(currentChar, operatorStack.peek())) {
                        reservedPolishNotation.append(operatorStack.peek()+" ");
                        operatorStack.pop();
                    }
                    operatorStack.push(currentChar);
                }

            } else if (currentChar == '('){
                operatorStack.push(currentChar);
            } else if (currentChar == ')') {
                while (operatorStack.peek() != '(') {
                    reservedPolishNotation.append(operatorStack.peek()+" ");
                    operatorStack.pop();
                }
                operatorStack.pop();
            } else {
                while(Character.isDigit(currentChar) || currentChar == '.') {
                    reservedPolishNotation.append(currentChar);
                    i += 1;
                    if (i == infixExpression.length()) {
                        break;
                    } else {
                        currentChar = infixExpression.charAt(i);
                    }

                }
                reservedPolishNotation.append(' ');
                i -= 1;
            }
        }
        while (!operatorStack.isEmpty()) {
            reservedPolishNotation.append(operatorStack.peek()+" ");
            operatorStack.pop();
        }
        // output rpn
        System.out.println(reservedPolishNotation);
        calculation();
    }

    /**
     * check the current char in the infix expression
     * to see if it is the operator
     * @param currentChar current char in the infix expression
     * @return true if the current char is operator; false if it is not operator
     */
    public boolean isOperator(Character currentChar) {
        boolean flag = false;
        String operators = "+-*/";
        for (char current: operators.toCharArray()) {
            if (current == currentChar) {
                flag = true;
                return flag;
            }
        }
        return flag;
    }

    /**
     * compare precedence of the current operator and the top of stack
     * @param currentOperator the current operator index indicates
     * @param topOfStack operator on the top of stack
     * @return true if currentOperator is with higher precedence; otherwise, false
     */
    public boolean precedenceCmp(Character currentOperator, Character topOfStack) {
        boolean precedence = false;
        if (topOfStack == '(') {
            precedence = true;
        } else if (currentOperator == '*' || currentOperator == '/') {
            if (topOfStack != '*' || topOfStack != '/') {
                precedence = true;
            }
        }
        return precedence;
    }

    public void calculation() {
        double first = 0.0, second = 0.0;
        StringBuffer number = new StringBuffer();
        Stack<Double> calculationStack = new Stack<>();

        result = 0;
        for (int i = 0; i < reservedPolishNotation.length(); i++) {
            char current = reservedPolishNotation.charAt(i);
            if (current == 'm' || current == 'p') {
                if (current == 'm') {
                    number.append('-');
                } else {
                    number.append('+');
                }
                i += 2;
                while (i != reservedPolishNotation.length()) {

                    if (reservedPolishNotation.charAt(i) == ' ') {
                        break;
                    } else {
                        number.append(reservedPolishNotation.charAt(i));
                    }
                    i += 1;
                }
                calculationStack.push(Double.parseDouble(number.toString()));
                number.setLength(0);
                i -= 1;
            } else if (isOperator(current)) {
                first = calculationStack.peek();
                calculationStack.pop();
                second = calculationStack.peek();
                calculationStack.pop();

                switch (current) {
                    case '+': result = first + second;break;
                    case '-': result = first - second;break;
                    case '/': result = first / second;break;
                    case '*': result = first * second;break;
                }
                calculationStack.push(result);
            } else if(Character.isDigit(current)){
                number.append(current);
                i += 1;
                while (i != reservedPolishNotation.length()) {

                    if (reservedPolishNotation.charAt(i) == ' ') {
                        break;
                    } {
                        number.append(reservedPolishNotation.charAt(i));
                        i += 1;
                    }

                }
                calculationStack.push(Double.parseDouble(number.toString()));
                i -= 1;
                number.setLength(0);
            }

        }
        result = calculationStack.peek();
        System.out.println("result: "+String.format("%.2f", result));
        calculationStack.empty();

    }
}
