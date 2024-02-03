package Lab6;
import java.util.Scanner;

public class Ex2 {
    public static void ex2() {
        Scanner scanner = new Scanner(System.in);
        char symbol = scanner.next().charAt(0);
        System.out.println((int) symbol);
        scanner.close();
    }
}
