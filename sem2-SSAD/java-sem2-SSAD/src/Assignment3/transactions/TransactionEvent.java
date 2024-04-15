package Assignment3.transactions;

import Assignment3.main.BankingSystem;

public class TransactionEvent {

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
