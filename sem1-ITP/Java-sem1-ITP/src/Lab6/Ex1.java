package Lab6;
import java.util.Scanner;

public class Ex1 {
    public static void ex1() {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt(), b = scanner.nextInt();
        int temp = a;
        a = b;
        b = temp;
        System.out.println(a + " " + b);
        scanner.close();
    }
}
