package Assignment3.main;

import Assignment3.exceptions.MainException;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static Map<String, Account> accounts;
    private static Map<String, Runnable> commandsMap;
    private static Scanner scanner;

    private static void initialize() {
        accounts = new HashMap<>();
        commandsMap = new HashMap<>();
        scanner = new Scanner(System.in);

        // TODO initialize this map
//        commandsMap.put("Create Account");
    }

    public static void main(String[] args) {
        initialize();
        int operationsAmount = scanner.nextInt();
        for (int i = 0; i < operationsAmount; i++) {

            String[] commandsList = scanner.nextLine().split(" ");
        }

        scanner.close();
    }

    public static void createAccount(String name, AccountType type, float initialDeposit) {
        accounts.put(name, new Account(name, type, initialDeposit));
        System.out.printf("A new %s account created for %s with an initial balance of %f.%n", type, name, initialDeposit);
    }

    public static void deposit(String accountName, float amount) {
        try {
            accounts.get(accountName).deposit(amount);
        } catch (MainException e) {
            System.out.println(e.getMessage(accountName));
        }
    }

    public static void withdraw(String accountName, float amount) {
        try {
            accounts.get(accountName).withdraw(amount);
        } catch (MainException exception) {
            exception.getMessage(accountName);
        }
    }

    public static void transfer(String fromAccountName, String toAccountName, float amount) {
        try {
            accounts.get(fromAccountName).transfer(accounts.get(toAccountName), amount);
        } catch (MainException exception) {
            exception.getMessage(fromAccountName);
        }
    }

    public static void view(String accountName) {
        accounts.get(accountName).showInformation();
    }

    public static void deactivate(String accountName) {
        try {
            accounts.get(accountName).deactivate();
        } catch (MainException e) {
            System.out.println(e.getMessage(accountName));
        }
    }

    public static void activate(String accountName) {
        try {
            accounts.get(accountName).activate();
        } catch (MainException e) {
            System.out.println(e.getMessage(accountName));
        }
    }
}
