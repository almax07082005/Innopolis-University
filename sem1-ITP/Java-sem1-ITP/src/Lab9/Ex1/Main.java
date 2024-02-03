package Lab9.Ex1;
import java.util.Scanner;

public class Main {
    public static void printMenu() {
        System.out.println("[0] – exit");
        System.out.println("[1] – print current string");
        System.out.println("[2] – append the string with a string that the user will input");
        System.out.println("[3] – insert a string that the user will input to the current string at a certain index");
        System.out.println("[4] – reverse current string");
        System.out.println("[5] – given indexes l and r delete substring in the range l to r from the current string");
        System.out.println("[6] – given indexes l and r replace substring in the range l to r from the current string with a string that will be given in the input");
    }
    public static StringBuilder reverseString(StringBuilder string) {
        StringBuilder reversedString = new StringBuilder();
        for (int i = string.length() - 1; i >= 0; i--) {
            reversedString.append(string.charAt(i));
        }
        return reversedString;
    }
    public static void main(String[] args) {
        printMenu();

        Scanner scanner = new Scanner(System.in);
        int input = scanner.nextInt();
        StringBuilder string = new StringBuilder();

        while (input >= 1 && input <= 6) {
            switch (input) {
                case 1: System.out.println(string); break;
                case 2: string.append(scanner.next()); break;
                case 3: string.insert(scanner.nextInt(), scanner.next()); break;
                case 4: string = reverseString(string); break;
                case 5: string.delete(scanner.nextInt(), scanner.nextInt()); break;
                case 6: string.replace(scanner.nextInt(), scanner.nextInt(), scanner.next()); break;
                default: System.out.println("Mistake in instruction code"); continue;
            }
            input = scanner.nextInt();
        }
        scanner.close();
    }
}
