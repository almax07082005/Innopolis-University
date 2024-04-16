package Assignment3;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

class Account {

    private AccountState state;
    private float balance;
    private final String name;
    private final AccountType type;
    private final List<TransactionEvent> transactionEvents;

    public Account(String name, AccountType type, float initialDeposit) {
        state = new ActivatedAccountState(this);
        this.name = name;
        this.type = type;
        balance = initialDeposit;

        transactionEvents = new ArrayList<>();
        transactionEvents.add(new TransactionEvent(TransactionType.InitialDeposit, initialDeposit));
    }

    public String getName() {
        return name;
    }

    public void showInformation() {
        System.out.printf("%s'Account: Type: %s, Balance: $%s, State: %s, Transactions: %s.%n",
                name,
                type,
                BankingSystem.getForMoney().format(balance),
                state,
                transactionEvents
        );
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public float getFee() {
        return type.getFee();
    }

    public boolean isActivated() {
        return state.getClass() == ActivatedAccountState.class;
    }

    public void activate() throws ActivateActivated {
        if (isActivated()) throw new ActivateActivated(name);
        state = new ActivatedAccountState(this);
    }

    public void deactivate() throws DeactivateDeactivated {
        if (!isActivated()) throw new DeactivateDeactivated(name);
        state = new DeactivatedAccountState(this);
    }

    public void deposit(float amount) throws OperationsWithDeactivated {
        state.deposit(amount);
        transactionEvents.add(new TransactionEvent(TransactionType.Deposit, amount));
    }

    public void withdraw(float amount) throws InsufficientFunds, OperationsWithDeactivated {
        state.withdraw(amount);
        transactionEvents.add(new TransactionEvent(TransactionType.Withdrawal, amount));
    }

    public void transfer(Account to, float amount) throws OperationsWithDeactivated, InsufficientFunds {
        state.transfer(to, amount);
        transactionEvents.add(new TransactionEvent(TransactionType.Transfer, amount));
    }
}

enum AccountType {

    Savings("Savings", 0.015f),
    Checking("Checking", 0.02f),
    Business("Business", 0.025f);

    private final String type;
    private final float fee;

    AccountType(String type, float fee) {
        this.type = type;
        this.fee = fee;
    }

    public float getFee() {
        return fee;
    }

    @Override
    public String toString() {
        return type;
    }
}

final class BankingSystem {

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
        } catch (MainException exception) {
            System.out.println(exception.getMessage());
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
                    getForFees().format(account.getFee() * 100)
            );
        } catch (MainException exception) {
            System.out.println(exception.getMessage());
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
                    getForFees().format(fromAccount.getFee() * 100)
            );
        } catch (MainException exception) {
            System.out.println(exception.getMessage());
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
            System.out.printf("%s's account is now deactivated.%n", accountName);
        } catch (MainException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public void activate(String accountName) throws NonExistentAccount {
        Account account = accounts.get(accountName);
        if (account == null) throw new NonExistentAccount(accountName);

        try {
            account.activate();
            System.out.printf("%s's account is now activated.%n", accountName);
        } catch (MainException exception) {
            System.out.println(exception.getMessage());
        }
    }
}

enum CommandType {

    CreateAccount("Create Account"),
    Deposit("Deposit"),
    Withdraw("Withdraw"),
    Transfer("Transfer"),
    View("View"),
    Deactivate("Deactivate"),
    Activate("Activate");

    private final String name;

    CommandType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}

abstract class AccountState {

    protected Account account;

    public AccountState(Account account) {
        this.account = account;
    }

    public abstract void deposit(float amount) throws OperationsWithDeactivated;
    public abstract void withdraw(float amount) throws OperationsWithDeactivated, InsufficientFunds;
    public abstract void transfer(Account to, float amount) throws OperationsWithDeactivated, InsufficientFunds;

    @Override
    public abstract String toString();
}

class ActivatedAccountState extends AccountState {

    public ActivatedAccountState(Account account) {
        super(account);
    }

    @Override
    public void deposit(float amount) {
        account.setBalance(account.getBalance() + amount);
    }

    @Override
    public void withdraw(float amount) throws InsufficientFunds {
        float newBalance = account.getBalance() - amount;
        if (newBalance < 0) throw new InsufficientFunds(account.getName());
        account.setBalance(newBalance);
    }

    @Override
    public void transfer(Account to, float amount) throws OperationsWithDeactivated, InsufficientFunds {
        if (!to.isActivated()) throw new OperationsWithDeactivated(account.getName());
        account.withdraw(amount);
        to.deposit(amount - amount * account.getFee());
    }

    @Override
    public String toString() {
        return "Active";
    }
}

class DeactivatedAccountState extends AccountState {

    public DeactivatedAccountState(Account account) {
        super(account);
    }

    @Override
    public void deposit(float amount) throws OperationsWithDeactivated {
        throw new OperationsWithDeactivated(account.getName());
    }

    @Override
    public void withdraw(float amount) throws OperationsWithDeactivated {
        throw new OperationsWithDeactivated(account.getName());
    }

    @Override
    public void transfer(Account to, float amount) throws OperationsWithDeactivated {
        throw new OperationsWithDeactivated(account.getName());
    }

    @Override
    public String toString() {
        return "Inactive";
    }
}

enum TransactionType {
    InitialDeposit,
    Deposit,
    Withdrawal,
    Transfer
}

class TransactionEvent {

    private final TransactionType transactionType;
    private final float amount;

    public TransactionEvent(TransactionType transactionType, float amount) {
        this.transactionType = transactionType;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return transactionType.name() + " $" + BankingSystem.getForMoney().format(amount);
    }
}

abstract class MainException extends Exception {

    protected final String accountName;

    public MainException(String accountName) {
        this.accountName = accountName;
    }

    @Override
    public abstract String getMessage();
}

class ActivateActivated extends MainException {

    public ActivateActivated(String accountName) {
        super(accountName);
    }

    @Override
    public String getMessage() {
        return String.format("Error: Account %s is already activated.", accountName);
    }
}

class DeactivateDeactivated extends MainException {

    public DeactivateDeactivated(String accountName) {
        super(accountName);
    }

    @Override
    public String getMessage() {
        return String.format("Error: Account %s is already deactivated.", accountName);
    }
}

class InsufficientFunds extends MainException {

    public InsufficientFunds(String accountName) {
        super(accountName);
    }

    @Override
    public String getMessage() {
        return String.format("Error: Insufficient funds for %s.", accountName);
    }
}

class NonExistentAccount extends MainException {

    public NonExistentAccount(String accountName) {
        super(accountName);
    }

    @Override
    public String getMessage() {
        return String.format("Error: Account %s does not exist.", accountName);
    }
}

class OperationsWithDeactivated extends MainException {

    public OperationsWithDeactivated(String accountName) {
        super(accountName);
    }

    @Override
    public String getMessage() {
        return String.format("Error: Account %s is inactive.", accountName);
    }
}
