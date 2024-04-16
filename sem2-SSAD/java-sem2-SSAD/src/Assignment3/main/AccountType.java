package Assignment3.main;

public enum AccountType {

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
