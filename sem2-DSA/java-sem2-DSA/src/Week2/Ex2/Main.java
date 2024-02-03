package Week2.Ex2;
// Maksim Al Dandan

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        LinkedQueue<String> input = RPN.makeRPN(scanner.nextLine());
        LinkedStack<String> output = new LinkedStack<>();

        while (!input.isEmpty()) {
            String value = input.pop();
            if (RPN.isNumber(value)) output.push(value);
            else {
                int number2 = Integer.parseInt(output.pop());
                int number1 = Integer.parseInt(output.pop());

                switch (value) {
                    case "+":
                        output.push(String.valueOf(number1 + number2));
                        break;
                    case "-":
                        output.push(String.valueOf(number1 - number2));
                        break;
                    case "*":
                        output.push(String.valueOf(number1 * number2));
                        break;
                    case "/":
                        output.push(String.valueOf(number1 / number2));
                        break;
                    case "min":
                        output.push(String.valueOf(number1 < number2 ? number1 : number2));
                        break;
                    case "max":
                        output.push(String.valueOf(number1 > number2 ? number1 : number2));
                        break;
                }
            }
        }

        System.out.println(output.pop());
        scanner.close();
    }
}

abstract class RPN {
    public static LinkedQueue<String> makeRPN(String expression) {
        String[] tokens = expression.split(" ");
        LinkedQueue<String> output = new LinkedQueue<>();
        LinkedStack<String> stack = new LinkedStack<>();

        for (String token : tokens) {

            if (isNumber(token)) output.push(token);
            else if (isFunction(token)) stack.push(token);

            else if (isOperator(token)) {
                while (!stack.isEmpty() && isOperator(stack.peek()) && getPrecedence(stack.peek()) >= getPrecedence(token)) {
                    output.push(stack.pop());
                }
                stack.push(token);
            }
            else if (isComma(token)) {
                while (!isLeftParenthesis(stack.peek())) {
                    output.push(stack.pop());
                }
            }
            else if (isLeftParenthesis(token)) stack.push(token);

            else if (isRightParenthesis(token)) {
                while (!isLeftParenthesis(stack.peek())) {
                    output.push(stack.pop());
                }
                if (isLeftParenthesis(stack.peek())) stack.pop();
                if (isFunction(stack.peek())) output.push(stack.pop());
            }
        }
        while (!stack.isEmpty()) {
            output.push(stack.pop());
        }

        return output;
    }

    private static int getPrecedence(String operator) {
        switch (operator) {
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
                return 2;
            default:
                return -1;
        }
    }

    public static boolean isNumber(String string) {
        try {
            Integer.parseInt(string);
            return true;
        }
        catch (Exception ignored) {
            return false;
        }
    }
    private static boolean isFunction(String string) {
        if (string == null) return false;
        return string.equals("min") || string.equals("max");
    }
    private static boolean isOperator(String string) {
        if (string == null) return false;
        return string.equals("+") || string.equals("-") || string.equals("*") || string.equals("/");
    }
    private static boolean isComma(String string) {
        if (string == null) return false;
        return string.equals(",");
    }
    private static boolean isLeftParenthesis(String string) {
        if (string == null) return false;
        return string.equals("(");
    }
    private static boolean isRightParenthesis(String string) {
        if (string == null) return false;
        return string.equals(")");
    }
}

interface ADT<T> {
    int size();
    boolean isEmpty();
    void push(T value);
    T pop();
    T peek();
}

class LinkedQueue<T> implements ADT<T> {
    private static class Node<T> {
        private T value;
        private LinkedQueue.Node<T> next;
        private LinkedQueue.Node<T> previous;
    }

    private int size;
    private LinkedQueue.Node<T> head;
    private LinkedQueue.Node<T> tail;

    public LinkedQueue() {
        this.size = 0;
        this.head = null;
        this.tail = null;
    }

    public void show() {
        LinkedQueue.Node<T> cur = this.tail;
        while (cur != null) {
            System.out.print(cur.value + " ");
            cur = cur.previous;
        }
        System.out.println();
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public void push(T value) {
        this.size++;

        if (this.size == 1) {
            this.head = new LinkedQueue.Node<>();
            this.head.value = value;
            return;
        }
        else if (this.size == 2) {
            this.tail = this.head;
            this.head = new LinkedQueue.Node<>();
            this.tail.previous = this.head;
            this.head.value = value;
            this.head.next = this.tail;
            return;
        }

        LinkedQueue.Node<T> item = this.head;
        this.head = new LinkedQueue.Node<>();
        item.previous = this.head;
        this.head.value = value;
        this.head.next = item;
    }

    @Override
    public T pop() {
        if (this.isEmpty()) return null;

        this.size--;
        if (this.isEmpty()) {
            T value = this.head.value;
            this.head = null;
            return value;
        }
        else if (this.size == 1) {
            T value = this.tail.value;
            this.tail = null;
            this.head.next = null;
            return value;
        }

        T value = this.tail.value;
        this.tail = this.tail.previous;
        this.tail.next = null;
        return value;
    }

    @Override
    public T peek() {
        if (this.isEmpty()) return null;
        return this.head.value;
    }
}

class LinkedStack<T> implements ADT<T> {
    private static class Node<T> {
        private T value;
        private LinkedStack.Node<T> next;
    }

    private int size;
    private LinkedStack.Node<T> head;

    public LinkedStack() {
        this.size = 0;
        this.head = null;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public void push(T value) {
        if (this.isEmpty()) {
            this.head = new LinkedStack.Node<>();
            this.head.value = value;
            this.size++;
            return;
        }

        this.size++;
        LinkedStack.Node<T> item = this.head;
        this.head = new LinkedStack.Node<>();
        this.head.value = value;
        this.head.next = item;
    }

    @Override
    public T pop() {
        if (this.isEmpty()) return null;

        this.size--;
        T value = this.head.value;
        this.head = this.head.next;

        return value;
    }

    @Override
    public T peek() {
        if (this.isEmpty()) return null;
        return this.head.value;
    }
}
