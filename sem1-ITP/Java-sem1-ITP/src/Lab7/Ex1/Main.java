package Lab7.Ex1;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Calculator calc = new Calculator(scanner.nextDouble(), scanner.next().charAt(0), scanner.nextDouble());
        System.out.println(calc.calculate());
        scanner.close();
    }
}
