package Assignment3.main;

import Assignment3.exceptions.MainException;
import Assignment3.exceptions.NonExistentAccount;

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

    public void deposit(String accountName, float amount) throws NonExistentAccount {
        Account account = accounts.get(accountName);
        if (account == null) throw new NonExistentAccount(accountName);

        try {
            account.deposit(amount);
            System.out.printf("%s successfully deposited $%s. New Balance: $%s.%n",
                    accountName,
                    getForMoney().format(amount),
                    getForMoney().format(account.getBalance())
            );
        } catch (MainException e) {
            System.out.println(e.getMessage());
        }
    }

    public void withdraw(String accountName, float amount) throws NonExistentAccount {
        Account account = accounts.get(accountName);
        if (account == null) throw new NonExistentAccount(accountName);

        try {
            account.withdraw(amount);
            System.out.printf("%s successfully withdrew $%s. New Balance: $%s. Transaction Fee: $%s (%s%%) in the system.%n",
                    accountName,
                    getForMoney().format(amount - amount * account.getFee()),
                    getForMoney().format(account.getBalance()),
                    getForMoney().format(amount * account.getFee()),
                    getForFees().format(account.getFee())
            );
        } catch (MainException exception) {
            exception.getMessage();
        }
    }

    public void transfer(String fromAccountName, String toAccountName, float amount) throws NonExistentAccount {
        Account fromAccount = accounts.get(fromAccountName);
        Account toAccount = accounts.get(toAccountName);
        if (fromAccount == null) throw new NonExistentAccount(fromAccountName);
        if (toAccount == null) throw new NonExistentAccount(toAccountName);

        try {
            fromAccount.transfer(toAccount, amount);
            System.out.printf("%s successfully transferred $%s to %s. New Balance: $%s. Transaction Fee: $%s (%s%%) in the system.%n",
                    fromAccountName,
                    getForMoney().format(amount - amount * fromAccount.getFee()),
                    toAccountName,
                    getForMoney().format(fromAccount.getBalance()),
                    getForMoney().format(amount * fromAccount.getFee()),
                    getForFees().format(fromAccount.getFee())
            );
        } catch (MainException exception) {
            exception.getMessage();
        }
    }

    public void view(String accountName) throws NonExistentAccount {
        Account account = accounts.get(accountName);
        if (account == null) throw new NonExistentAccount(accountName);

        account.showInformation();
    }

    public void deactivate(String accountName) throws NonExistentAccount {
        Account account = accounts.get(accountName);
        if (account == null) throw new NonExistentAccount(accountName);

        try {
            account.deactivate();
            System.out.printf("%s's account is now deactivated.", accountName);
        } catch (MainException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public void activate(String accountName) throws NonExistentAccount {
        Account account = accounts.get(accountName);
        if (account == null) throw new NonExistentAccount(accountName);

        try {
            account.activate();
            System.out.printf("%s's account is now activated.", accountName);
        } catch (MainException exception) {
            System.out.println(exception.getMessage());
        }
    }
}
