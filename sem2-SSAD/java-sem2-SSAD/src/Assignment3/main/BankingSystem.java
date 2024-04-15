package Assignment3.main;

import Assignment3.exceptions.MainException;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public final class BankingSystem {

    private static BankingSystem bankingSystem;
    private final Map<String, Account> accounts;

    private static final DecimalFormat forMoney = new DecimalFormat("0.000");
    private static final DecimalFormat forFees = new DecimalFormat("0.0");

    public static DecimalFormat getForMoney() {
        return forMoney;
    }

    public static DecimalFormat getForFees() {
        return forFees;
    }

    private BankingSystem() {
        accounts = new HashMap<>();
    }

    public static BankingSystem getInstance() {

        if (bankingSystem == null) {
            bankingSystem = new BankingSystem();
        }

        return bankingSystem;
    }

    public void createAccount(String name, AccountType type, float initialDeposit) {
        accounts.put(name, new Account(name, type, initialDeposit));
        System.out.printf("A new %s account created for %s with an initial balance of $%s.%n",
                type,
                name,
                getForMoney().format(initialDeposit)
        );
    }

    public void deposit(String accountName, float amount) {
        Account account = accounts.get(accountName);

        try {
            account.deposit(amount);
            System.out.printf("%s successfully deposited $%s. New Balance: $%s.%n",
                    accountName,
                    getForMoney().format(amount),
                    getForMoney().format(account.getBalance()));
        } catch (MainException e) {
            System.out.println(e.getMessage(accountName));
        }
    }

    public void withdraw(String accountName, float amount) {
        Account account = accounts.get(accountName);

        try {
            account.withdraw(amount);
            System.out.printf("%s successfully withdrew $%s. New Balance: $%s. Transaction Fee: $%s (%s%%) in the system.%n",
                    accountName,
                    getForMoney().format(amount),
                    getForMoney().format(account.getBalance()),
                    getForMoney().format(amount * account.getFee()),
                    getForFees().format(account.getFee()));
        } catch (MainException exception) {
            exception.getMessage(accountName);
        }
    }

    public void transfer(String fromAccountName, String toAccountName, float amount) {
        Account fromAccount = accounts.get(fromAccountName);
        Account toAccount = accounts.get(toAccountName);

        try {
            fromAccount.transfer(toAccount, amount);
            System.out.printf("%s successfully transferred $%s to %s. New Balance: $%s. Transaction Fee: $%s (%s%%) in the system.%n",
                    fromAccountName,
                    getForMoney().format(amount),
                    toAccountName,
                    getForMoney().format(fromAccount.getBalance()),
                    getForMoney().format(amount * fromAccount.getFee()),
                    getForFees().format(fromAccount.getFee())
            );
        } catch (MainException exception) {
            exception.getMessage(fromAccountName);
        }
    }

    public void view(String accountName) {
        accounts.get(accountName).showInformation();
    }

    public void deactivate(String accountName) {
        try {
            accounts.get(accountName).deactivate();
            System.out.printf("%s's account is now deactivated.", accountName);
        } catch (MainException e) {
            System.out.println(e.getMessage(accountName));
        }
    }

    public void activate(String accountName) {
        try {
            accounts.get(accountName).activate();
            System.out.printf("%s's account is now activated.", accountName);
        } catch (MainException e) {
            System.out.println(e.getMessage(accountName));
        }
    }
}
