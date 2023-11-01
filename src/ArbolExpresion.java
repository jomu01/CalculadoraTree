import java.util.Stack;

class ExpressionEvaluator {

    private static int precedence(char operator) {
        if (operator == '+' || operator == '-') {
            return 1;
        } else if (operator == '*' || operator == '/' || operator == '%') {
            return 2;
        } else if (operator == '^') {
            return 3;
        }
        return 0;
    }

    private static int applyOperator(int a, int b, char operator) {
        switch (operator) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                return a / b;
            case '%':
                if (b == 0) {
                    throw new ArithmeticException("Modulo by zero");
                }
                return a % b;
            case '^':
                return (int) Math.pow(a, b);
        }
        return 0;
    }

    public int evaluateExpression(String expression) {
        Stack<Integer> valueStack = new Stack<>();
        Stack<Character> operatorStack = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);
            if (Character.isDigit(ch)) {
                StringBuilder num = new StringBuilder();
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    num.append(expression.charAt(i));
                    i++;
                }
                i--;
                valueStack.push(Integer.parseInt(num.toString()));
            } else if (ch == '(') {
                operatorStack.push(ch);
            } else if (ch == ')') {
                while (!operatorStack.isEmpty() && operatorStack.peek() != '(') {
                    char operator = operatorStack.pop();
                    int b = valueStack.pop();
                    int a = valueStack.pop();
                    valueStack.push(applyOperator(a, b, operator));
                }
                operatorStack.pop(); // Pop the '('
            } else if (ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '%' || ch == '^') {
                while (!operatorStack.isEmpty() && operatorStack.peek() != '(' && precedence(ch) <= precedence(operatorStack.peek())) {
                    char operator = operatorStack.pop();
                    int b = valueStack.pop();
                    int a = valueStack.pop();
                    valueStack.push(applyOperator(a, b, operator));
                }
                operatorStack.push(ch);
            }
        }

        while (!operatorStack.isEmpty()) {
            char operator = operatorStack.pop();
            int b = valueStack.pop();
            int a = valueStack.pop();
            valueStack.push(applyOperator(a, b, operator));
        }

        return valueStack.pop();
    }
}

