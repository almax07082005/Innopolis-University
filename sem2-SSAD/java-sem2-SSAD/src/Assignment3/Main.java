package Assignment3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * The Main class is the entry point of the program. It initializes the banking system and executes the commands.
 */
public class Main {

    private static Scanner scanner;
    private static BankingSystem bankingSystem;

    /**
     * Initializes the banking system and the scanner.
     */
    private static void initialize() {
        bankingSystem = BankingSystem.getInstance();
        scanner = new Scanner(System.in);
    }

    /**
     * The main method of the program.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        initialize();
        int operationsAmount = Integer.parseInt(scanner.nextLine());
        for (int i = 0; i < operationsAmount; i++) {

            String[] commandsList = scanner.nextLine().split(" ");
            execute(commandsList);
        }

        scanner.close();
    }

    /**
     * Executes the given commands list.
     *
     * @param commandsList The list of commands to execute.
     */
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

/**
 * Represents a bank account.
 */
class Account {

    private AccountState state;
    private float balance;
    private final String name;
    private final AccountType type;
    private final List<TransactionEvent> transactionEvents;

    /**
     * Constructs an Account object with the specified name, type, and initial deposit.
     *
     * @param name           the name of the account holder
     * @param type           the type of the account
     * @param initialDeposit the initial deposit amount
     */
    public Account(String name, AccountType type, float initialDeposit) {
        state = new ActivatedAccountState(this);
        this.name = name;
        this.type = type;
        balance = initialDeposit;

        transactionEvents = new ArrayList<>();
        transactionEvents.add(new TransactionEvent(TransactionType.InitialDeposit, initialDeposit));
    }

    /**
     * Returns the name of the account holder.
     *
     * @return the name of the account holder
     */
    public String getName() {
        return name;
    }

    /**
     * Displays the information of the account, including the account holder's name, account type, balance, state, and transaction history.
     */
    public void showInformation() {
        System.out.printf("%s's Account: Type: %s, Balance: $%s, State: %s, Transactions: %s.%n",
                name,
                type,
                String.format("%.3f", balance),
                state,
                transactionEvents
        );
    }

    /**
     * Returns the current balance of the account.
     *
     * @return the current balance of the account
     */
    public float getBalance() {
        return balance;
    }

    /**
     * Sets the balance of the account to the specified amount.
     *
     * @param balance the new balance of the account
     */
    public void setBalance(float balance) {
        this.balance = balance;
    }

    /**
     * Returns the fee associated with the account type.
     *
     * @return the fee associated with the account type
     */
    public float getFee() {
        return type.getFee();
    }

    /**
     * Checks if the account is activated.
     *
     * @return true if the account is activated, false otherwise
     */
    public boolean isActivated() {
        return state.getClass() == ActivatedAccountState.class;
    }

    /**
     * Activates the account.
     *
     * @throws ActivateActivated if the account is already activated
     */
    public void activate() throws ActivateActivated {
        if (isActivated()) throw new ActivateActivated(name);
        state = new ActivatedAccountState(this);
    }

    /**
     * Deactivates the account.
     *
     * @throws DeactivateDeactivated if the account is already deactivated
     */
    public void deactivate() throws DeactivateDeactivated {
        if (!isActivated()) throw new DeactivateDeactivated(name);
        state = new DeactivatedAccountState(this);
    }

    /**
     * Deposits the specified amount into the account.
     *
     * @param amount the amount to deposit
     * @throws OperationsWithDeactivated if the account is deactivated
     */
    public void deposit(float amount) throws OperationsWithDeactivated {
        state.deposit(amount);
        transactionEvents.add(new TransactionEvent(TransactionType.Deposit, amount));
    }

    /**
     * Withdraws the specified amount from the account.
     *
     * @param amount the amount to withdraw
     * @throws InsufficientFunds       if the account balance is insufficient
     * @throws OperationsWithDeactivated if the account is deactivated
     */
    public void withdraw(float amount) throws InsufficientFunds, OperationsWithDeactivated {
        state.withdraw(amount);
        transactionEvents.add(new TransactionEvent(TransactionType.Withdrawal, amount));
    }

    /**
     * Transfers the specified amount from this account to the specified account.
     *
     * @param to     the account to transfer the amount to
     * @param amount the amount to transfer
     * @throws OperationsWithDeactivated if either account is deactivated
     * @throws InsufficientFunds       if the account balance is insufficient
     */
    public void transfer(Account to, float amount) throws OperationsWithDeactivated, InsufficientFunds {
        state.transfer(to, amount);
        transactionEvents.add(new TransactionEvent(TransactionType.Transfer, amount));
    }
}

/**
 * The AccountType enum represents different types of accounts.
 */
enum AccountType {

    /**
     * Savings account type with a fee of 1.5%.
     */
    Savings("Savings", 0.015f),

    /**
     * Checking account type with a fee of 2%.
     */
    Checking("Checking", 0.02f),

    /**
     * Business account type with a fee of 2.5%.
     */
    Business("Business", 0.025f);

    private final String type;
    private final float fee;

    /**
     * Constructs an AccountType with the specified type and fee.
     *
     * @param type The type of the account.
     * @param fee  The fee associated with the account.
     */
    AccountType(String type, float fee) {
        this.type = type;
        this.fee = fee;
    }

    /**
     * Returns the fee associated with the account.
     *
     * @return The fee of the account.
     */
    public float getFee() {
        return fee;
    }

    /**
     * Returns a string representation of the account type.
     *
     * @return The string representation of the account type.
     */
    @Override
    public String toString() {
        return type;
    }
}

/**
 * The BankingSystem class represents a banking system that manages accounts and provides various banking operations.
 */
final class BankingSystem {

    private static BankingSystem bankingSystem;
    private final Map<String, Account> accounts;

    private BankingSystem() {
        accounts = new HashMap<>();
    }

    /**
     * Returns the singleton instance of the BankingSystem class.
     *
     * @return The singleton instance of the BankingSystem class.
     */
    public static BankingSystem getInstance() {

        if (bankingSystem == null) {
            bankingSystem = new BankingSystem();
        }

        return bankingSystem;
    }

    /**
     * Creates a new account with the specified name, account type, and initial deposit.
     *
     * @param name           The name of the account holder.
     * @param type           The type of the account.
     * @param initialDeposit The initial deposit amount.
     */
    public void createAccount(String name, AccountType type, float initialDeposit) {
        accounts.put(name, new Account(name, type, initialDeposit));
        System.out.printf("A new %s account created for %s with an initial balance of $%s.%n",
                type,
                name,
                String.format("%.3f", initialDeposit)
        );
    }

    /**
     * Deposits the specified amount into the account with the given name.
     *
     * @param accountName The name of the account.
     * @param amount      The amount to deposit.
     * @throws NonExistentAccount If the account does not exist.
     */
    public void deposit(String accountName, float amount) throws NonExistentAccount {
        Account account = accounts.get(accountName);
        if (account == null) throw new NonExistentAccount(accountName);

        try {
            account.deposit(amount);
            System.out.printf("%s successfully deposited $%s. New Balance: $%s.%n",
                    accountName,
                    String.format("%.3f", amount),
                    String.format("%.3f", account.getBalance())
            );
        } catch (MainException exception) {
            System.out.println(exception.getMessage());
        }
    }

    /**
     * Withdraws the specified amount from the account with the given name.
     *
     * @param accountName The name of the account.
     * @param amount      The amount to withdraw.
     * @throws NonExistentAccount If the account does not exist.
     */
    public void withdraw(String accountName, float amount) throws NonExistentAccount {
        Account account = accounts.get(accountName);
        if (account == null) throw new NonExistentAccount(accountName);

        try {
            account.withdraw(amount);
            System.out.printf("%s successfully withdrew $%s. New Balance: $%s. Transaction Fee: $%s (%s%%) in the system.%n",
                    accountName,
                    String.format("%.3f", amount - amount * account.getFee()),
                    String.format("%.3f", account.getBalance()),
                    String.format("%.3f", amount * account.getFee()),
                    account.getFee() * 100
            );
        } catch (MainException exception) {
            System.out.println(exception.getMessage());
        }
    }

    /**
     * Transfers the specified amount from one account to another.
     *
     * @param fromAccountName The name of the account to transfer from.
     * @param toAccountName   The name of the account to transfer to.
     * @param amount          The amount to transfer.
     * @throws NonExistentAccount If either of the accounts does not exist.
     */
    public void transfer(String fromAccountName, String toAccountName, float amount) throws NonExistentAccount {
        Account fromAccount = accounts.get(fromAccountName);
        Account toAccount = accounts.get(toAccountName);
        if (fromAccount == null) throw new NonExistentAccount(fromAccountName);
        if (toAccount == null) throw new NonExistentAccount(toAccountName);

        try {
            fromAccount.transfer(toAccount, amount);
            System.out.printf("%s successfully transferred $%s to %s. New Balance: $%s. Transaction Fee: $%s (%s%%) in the system.%n",
                    fromAccountName,
                    String.format("%.3f", amount - amount * fromAccount.getFee()),
                    toAccountName,
                    String.format("%.3f", fromAccount.getBalance()),
                    String.format("%.3f", amount * fromAccount.getFee()),
                    fromAccount.getFee() * 100
            );
        } catch (MainException exception) {
            System.out.println(exception.getMessage());
        }
    }

    /**
     * Displays the information of the account with the given name.
     *
     * @param accountName The name of the account.
     * @throws NonExistentAccount If the account does not exist.
     */
    public void view(String accountName) throws NonExistentAccount {
        Account account = accounts.get(accountName);
        if (account == null) throw new NonExistentAccount(accountName);

        account.showInformation();
    }

    /**
     * Deactivates the account with the given name.
     *
     * @param accountName The name of the account.
     * @throws NonExistentAccount If the account does not exist.
     */
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

    /**
     * Activates the account with the given name.
     *
     * @param accountName The name of the account.
     * @throws NonExistentAccount If the account does not exist.
     */
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

/**
 * Represents the activated state of an account.
 * In this state, the account allows deposits, withdrawals, and transfers.
 * The account balance can be increased by depositing money and decreased by withdrawing money.
 * Transfers can be made to other accounts, with a fee deducted from the transferred amount.
 * The state is represented by the string "Active".
 */
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
    public void transfer(Account to, float amount) throws InsufficientFunds {
        float newBalance = account.getBalance() - amount;
        if (newBalance < 0) throw new InsufficientFunds(account.getName());
        account.setBalance(newBalance);

        to.setBalance(to.getBalance() + amount - amount * account.getFee());
    }

    @Override
    public String toString() {
        return "Active";
    }
}

/**
 * Represents the state of a deactivated account.
 * In this state, the account cannot perform withdrawals or transfers,
 * but can still receive deposits.
 */
class DeactivatedAccountState extends AccountState {

    /**
     * Constructs a new DeactivatedAccountState object with the specified account.
     * 
     * @param account the account associated with this state
     */
    public DeactivatedAccountState(Account account) {
        super(account);
    }

    /**
     * Deposits the specified amount into the account.
     * 
     * @param amount the amount to deposit
     */
    @Override
    public void deposit(float amount) {
        account.setBalance(account.getBalance() + amount);
    }

    /**
     * Throws an exception since withdrawals are not allowed in the deactivated state.
     * 
     * @param amount the amount to withdraw
     * @throws OperationsWithDeactivated if a withdrawal is attempted
     */
    @Override
    public void withdraw(float amount) throws OperationsWithDeactivated {
        throw new OperationsWithDeactivated(account.getName());
    }

    /**
     * Throws an exception since transfers are not allowed in the deactivated state.
     * 
     * @param to the account to transfer to
     * @param amount the amount to transfer
     * @throws OperationsWithDeactivated if a transfer is attempted
     */
    @Override
    public void transfer(Account to, float amount) throws OperationsWithDeactivated {
        throw new OperationsWithDeactivated(account.getName());
    }

    /**
     * Returns a string representation of the deactivated account state.
     * 
     * @return a string representation of the state
     */
    @Override
    public String toString() {
        return "Inactive";
    }
}

/**
 * Represents the type of a transaction.
 */
enum TransactionType {
    InitialDeposit("Initial Deposit"),
    Deposit("Deposit"),
    Withdrawal("Withdrawal"),
    Transfer("Transfer");

    private final String name;

    /**
     * Constructs a new TransactionType with the specified name.
     *
     * @param name the name of the transaction type
     */
    TransactionType(String name) {
        this.name = name;
    }

    /**
     * Returns the name of the transaction type.
     *
     * @return the name of the transaction type
     */
    @Override
    public String toString() {
        return name;
    }
}

/**
 * Represents a transaction event.
 */
class TransactionEvent {

    private final TransactionType transactionType;
    private final float amount;

    /**
     * Constructs a new TransactionEvent object.
     *
     * @param transactionType the type of the transaction
     * @param amount the amount of the transaction
     */
    public TransactionEvent(TransactionType transactionType, float amount) {
        this.transactionType = transactionType;
        this.amount = amount;
    }

    /**
     * Returns a string representation of the TransactionEvent object.
     *
     * @return a string representation of the TransactionEvent object
     */
    @Override
    public String toString() {
        return transactionType + " $" + String.format("%.3f", amount);
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

/**
 * Exception class representing an error when trying to activate an already activated account.
 */
class ActivateActivated extends MainException {

    /**
     * Constructs a new ActivateActivated exception with the specified account name.
     *
     * @param accountName the name of the account that is already activated
     */
    public ActivateActivated(String accountName) {
        super(accountName);
    }

    /**
     * Returns the error message for this exception.
     *
     * @return the error message indicating that the account is already activated
     */
    @Override
    public String getMessage() {
        return String.format("Error: Account %s is already activated.", accountName);
    }
}

/**
 * Exception class representing the scenario where an attempt is made to deactivate an already deactivated account.
 */
class DeactivateDeactivated extends MainException {

    /**
     * Constructs a new DeactivateDeactivated exception with the specified account name.
     *
     * @param accountName the name of the account that is already deactivated
     */
    public DeactivateDeactivated(String accountName) {
        super(accountName);
    }

    /**
     * Returns the error message for this exception.
     *
     * @return the error message indicating that the account is already deactivated
     */
    @Override
    public String getMessage() {
        return String.format("Error: Account %s is already deactivated.", accountName);
    }
}

/**
 * This exception is thrown when there are insufficient funds in an account.
 */
class InsufficientFunds extends MainException {

    /**
     * Constructs a new InsufficientFunds object with the specified account name.
     *
     * @param accountName the name of the account with insufficient funds
     */
    public InsufficientFunds(String accountName) {
        super(accountName);
    }

    /**
     * Returns the error message for this exception.
     *
     * @return the error message indicating insufficient funds for the account
     */
    @Override
    public String getMessage() {
        return String.format("Error: Insufficient funds for %s.", accountName);
    }
}

/**
 * Exception class representing a non-existent account.
 */
class NonExistentAccount extends MainException {

    /**
     * Constructs a new NonExistentAccount object with the specified account name.
     *
     * @param accountName the name of the non-existent account
     */
    public NonExistentAccount(String accountName) {
        super(accountName);
    }

    /**
     * Returns the error message for the non-existent account.
     *
     * @return the error message
     */
    @Override
    public String getMessage() {
        return String.format("Error: Account %s does not exist.", accountName);
    }
}

/**
 * This class represents an exception that is thrown when operations are performed on a deactivated account.
 * It extends the MainException class.
 */
class OperationsWithDeactivated extends MainException {

    /**
     * Constructs a new OperationsWithDeactivated object with the specified account name.
     *
     * @param accountName the name of the deactivated account
     */
    public OperationsWithDeactivated(String accountName) {
        super(accountName);
    }

    /**
     * Returns the error message for this exception.
     *
     * @return the error message indicating that the account is inactive
     */
    @Override
    public String getMessage() {
        return String.format("Error: Account %s is inactive.", accountName);
    }
}
