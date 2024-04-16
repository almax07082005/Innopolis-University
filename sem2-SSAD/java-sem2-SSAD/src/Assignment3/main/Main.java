package Assignment3.main;

import Assignment3.exceptions.NonExistentAccount;

import java.util.Scanner;

public class Main {

    private static Scanner scanner;
    private static BankingSystem bankingSystem;

    private static void initialize() {
        bankingSystem = BankingSystem.getInstance();
        scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        initialize();
        int operationsAmount = Integer.parseInt(scanner.nextLine());
        for (int i = 0; i < operationsAmount; i++) {

            String[] commandsList = scanner.nextLine().split(" ");
            execute(commandsList);
        }

        scanner.close();
    }

    public static void execute(String[] commandsList) {
        CommandType command = ((commandsList[0] + " " + commandsList[1]).equals(CommandType.CreateAccount.toString()) ? CommandType.CreateAccount : CommandType.valueOf(commandsList[0]));

        try {
            switch (command) {
                case CreateAccount:
                    bankingSystem.createAccount(commandsList[3], AccountType.valueOf(commandsList[2]), Float.parseFloat(commandsList[4]));
                    break;
                case Deposit:
                    bankingSystem.deposit(commandsList[1], Float.parseFloat(commandsList[2]));
                    break;
                case Withdraw:
                    bankingSystem.withdraw(commandsList[1], Float.parseFloat(commandsList[2]));
                    break;
                case Transfer:
                    bankingSystem.transfer(commandsList[1], commandsList[2], Float.parseFloat(commandsList[3]));
                    break;
                case View:
                    bankingSystem.view(commandsList[1]);
                    break;
                case Deactivate:
                    bankingSystem.deactivate(commandsList[1]);
                    break;
                case Activate:
                    bankingSystem.activate(commandsList[1]);
                    break;
            }
        } catch (NonExistentAccount exception) {
            System.out.println(exception.getMessage());
        }
    }
}
