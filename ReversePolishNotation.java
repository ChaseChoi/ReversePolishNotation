import java.io.*;
import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.script.*;

/**
 * @author caiguihao
 * @version 1.0
 * @since 2018-03-10
 */

/**
 * 1st command line arg: number of expressions
 * 2st command line arg: 0 invalid; 1 valid
 */
public class ReversePolishNotation {
    private String infixExpression = "";
    private double result = 0;
    private StringBuffer reservedPolishNotation;
    private String fileName = "input";

    public static void main(String[] args) {
        ReversePolishNotation notation = new ReversePolishNotation();
        int numOfExpression = Integer.parseInt(args[0]);
        int valid = Integer.parseInt(args[1]);
        notation.write(numOfExpression, valid);
        notation.read();
    }

    /**
     * Write specific number of expressions into the file indicated by fileName
     * @param numOfExpressions Specify the number of expressions
     * @param valid 0 means invalid; 1 means valid
     */
    private void write(int numOfExpressions, int valid) {
        try {
            FileWriter file = new FileWriter(fileName);
            BufferedWriter bufferWriter = new BufferedWriter(file);
            String numbers = "0123456789.";
            String operators = "+-*/()";
            StringBuilder sample;
            for (int i = 0; i < numOfExpressions; i++) {
                int numberOfOperands = ThreadLocalRandom.current().nextInt(1, 10);
                if (valid == 1) {
                    BinaryTree tree = new BinaryTree(numberOfOperands);
                    sample = new StringBuilder(tree.getRandomExpressions());
                } else {

                    sample = new StringBuilder();
                    for (int j = 0; j < numberOfOperands; j++) {
                        int opPosition = ThreadLocalRandom.current().nextInt(0, 6);
                        int numPosition = ThreadLocalRandom.current().nextInt(0, 11);
                        sample.append(numbers.charAt(numPosition));
                        sample.append(operators.charAt(opPosition));
                    }

                }

                bufferWriter.write(sample.toString());
                bufferWriter.newLine();
                sample.setLength(0);
            }
            bufferWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * This method reads the expressions stored in the file line by line,
     * then converts them to RPN, calculate and show the result.
     */
    private void read() {
        try {
            FileReader file = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(file);
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                // remove whitespace
                infixExpression = line.replaceAll("\\s+", "");
                System.out.println(infixExpression);
                if (isValid()) {
                    convert();
                }
            }
            bufferedReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method convert infixExpression to RPN,
     * and update the property reservedPolishNotation
     */
    private void convert() {
        Stack<Character> operatorStack = new Stack<>();
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
        System.out.println("RPN: "+reservedPolishNotation+"\n");
//        calculation();
    }

    /**
     * check the current char in the infix expression
     * to see if it is the operator
     * @param currentChar current char in the infix expression
     * @return true if the current char is operator; false if it is not operator
     */
    private boolean isOperator(Character currentChar) {
        String operators = "+-*/";
        for (char current: operators.toCharArray()) {
            if (current == currentChar) {
                return true;
            }
        }
        return false;
    }

    /**
     * compare precedence of the current operator and the top of stack
     * @param currentOperator the current operator index indicates
     * @param topOfStack operator on the top of stack
     * @return true if currentOperator is with higher precedence; otherwise, false
     */
    private boolean precedenceCmp(Character currentOperator, Character topOfStack) {
        if (topOfStack == '(') return true;
        else if (currentOperator == '*' || currentOperator == '/') {
            if (topOfStack != '*' || topOfStack != '/') {
                return true;
            }
        }
        return false;
    }

    /**
     * This method calculates the reservedPolishNotation,
     * and print out the result.
     */
    private void calculation() {
        double first, second;
        StringBuilder number = new StringBuilder();
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
                second = calculationStack.peek();
                calculationStack.pop();
                first = calculationStack.peek();
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


    /**
     * This method determines if the infixExpression is valid
     * @return true, when infixExpression is valid; false, when invalid
     */
    public boolean isValid(){
        String expression = infixExpression;
        Pattern arithmeticExp, parenthesisPattern;
        arithmeticExp = Pattern.compile("\\s*-?\\d+(.\\d+)?(\\s*[-+*/]\\s*-?\\d+(.\\d+)?)*\\s*");
        parenthesisPattern = Pattern.compile("[(]([^()]*)[)]");
        int count = 1;
        while (expression.contains("(") || expression.contains(")") ) {
            Matcher m = parenthesisPattern.matcher(expression);
            if (m.find()) {
                if (!arithmeticExp.matcher(m.group(1)).matches()) {
                    System.out.println("第"+count+"组括号表达式错误!");
                    return false;
                }
                expression = expression.substring(0, m.start()) + "1" + expression.substring(m.end());
                count += 1;
            } else {
                System.out.println("第"+count+"组括号不匹配!");
                return false;
            }
        }
       if (!arithmeticExp.matcher(expression).matches() ) {
           System.out.println("第"+count+"组括号表达式错误!");
           return false;
       } else {
            return true;
       }
    }
}
