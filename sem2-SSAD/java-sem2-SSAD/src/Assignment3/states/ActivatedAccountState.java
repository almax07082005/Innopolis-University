package Assignment3.states;

import Assignment3.exceptions.InsufficientFunds;
import Assignment3.exceptions.OperationsWithDeactivated;
import Assignment3.main.Account;

public class ActivatedAccountState extends AccountState {

    public ActivatedAccountState(Account account) {
        super(account);
    }

    @Override
    public void deposit(float amount) {
        account.setBalance(account.getBalance() + amount - amount * account.getFee());
    }

    @Override
    public void withdraw(float amount) throws InsufficientFunds {
        float newBalance = account.getBalance() - amount - amount * account.getFee();
        if (newBalance < 0) throw new InsufficientFunds();
        account.setBalance(newBalance);
    }

    @Override
    public void transfer(Account to, float amount) throws OperationsWithDeactivated, InsufficientFunds {
        if (!to.isActivated()) throw new OperationsWithDeactivated();
        account.withdraw(amount);
        to.deposit(amount);
    }

    @Override
    public String toString() {
        return "Active";
    }
}
