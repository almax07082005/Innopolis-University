package Assignment3.main;

import Assignment3.exceptions.ActivateActivated;
import Assignment3.exceptions.DeactivateDeactivated;
import Assignment3.exceptions.InsufficientFunds;
import Assignment3.exceptions.OperationsWithDeactivated;
import Assignment3.states.AccountState;
import Assignment3.states.ActivatedAccountState;
import Assignment3.states.DeactivatedAccountState;

public abstract class Account {

    private AccountState state;
    private float balance;

    private final String name;
    private final AccountType type;
    private final float initialDeposit;

    public Account(String name, AccountType type, float initialDeposit) {
        state = new ActivatedAccountState(this);
        this.name = name;
        this.type = type;
        balance = initialDeposit;
        this.initialDeposit = initialDeposit;
    }

    // TODO work with output
    public void showInformation() {
        System.out.printf("%s'Account: Type: %s, Balance: %f%n", name, type, balance);
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
        if (isActivated()) throw new ActivateActivated();
        state = new DeactivatedAccountState(this);
    }

    public void deactivate() throws DeactivateDeactivated {
        if (!isActivated()) throw new DeactivateDeactivated();
        state = new ActivatedAccountState(this);
    }

    public void deposit(float amount) throws OperationsWithDeactivated {
        state.deposit(amount);
    }

    public void withdraw(float amount) throws InsufficientFunds, OperationsWithDeactivated {
        state.withdraw(amount);
    }

    public void transfer(Account to, float amount) throws OperationsWithDeactivated, InsufficientFunds {
        state.transfer(to, amount);
    }
}
