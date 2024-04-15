package Assignment3.states;

import Assignment3.exceptions.InsufficientFunds;
import Assignment3.exceptions.OperationsWithDeactivated;
import Assignment3.main.Account;

public abstract class AccountState {

    protected Account account;

    public AccountState(Account account) {
        this.account = account;
    }

    public abstract void deposit(float amount) throws OperationsWithDeactivated;
    public abstract void withdraw(float amount) throws OperationsWithDeactivated, InsufficientFunds;
    public abstract void transfer(Account to, float amount) throws OperationsWithDeactivated, InsufficientFunds;
}
