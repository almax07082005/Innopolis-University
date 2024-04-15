package Assignment3.main;

public enum AccountType {

    Savings("Savings", 1.5f),
    Checking("Checking", 2f),
    Business("Business", 2.5f);

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
