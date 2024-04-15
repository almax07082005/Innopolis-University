package Assignment3.exceptions;

public class InsufficientFunds extends MainException {

    public InsufficientFunds(String accountName) {
        super(accountName);
    }

    @Override
    public String getMessage() {
        return String.format("Error: Insufficient funds for %s.", accountName);
    }
}
