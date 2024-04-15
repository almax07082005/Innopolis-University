package Assignment3.states;

import Assignment3.exceptions.OperationsWithDeactivated;
import Assignment3.main.Account;

public class DeactivatedAccountState extends AccountState {

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
