package Lab9.Ex2;

public class VendingMachine {
    public static void main(String[] args) {
        System.out.println("This vending machine has the following drinks:");
        for (int i = 0; i < Drinks.values().length; i++) {
            System.out.print(Drinks.values()[i] + (i < Drinks.values().length - 1 ? ", " : ""));
        }
        System.out.println();
    }
}
