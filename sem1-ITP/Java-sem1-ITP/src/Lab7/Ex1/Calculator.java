package Lab7.Ex1;

public class Calculator {
    double number1, number2;
    char operation;

    public Calculator(double number1, char operation, double number2) {
        this.number1 = number1;
        this.number2 = number2;
        this.operation = operation;
    }
    public double calculate() {
        switch (this.operation) {
            case '+': return this.number1 + this.number2;
            case '-': return this.number1 - this.number2;
            case '*': return this.number1 * this.number2;
            case '/': return this.number1 / this.number2;
        }
        return 0;
    }
}
