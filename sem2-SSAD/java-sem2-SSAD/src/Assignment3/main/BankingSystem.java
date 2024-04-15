package Assignment3.main;

public final class BankingSystem {

    private static BankingSystem bankingSystem;
    private BankingSystem() {}

    public static BankingSystem getInstance() {

        if (bankingSystem == null) {
            bankingSystem = new BankingSystem();
        }

        return bankingSystem;
    }
}
