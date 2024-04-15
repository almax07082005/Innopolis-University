package Assignment3.exceptions;

public class InsufficientFunds extends MainException {

    @Override
    public String getMessage(String accountName) {
        return String.format("Error: Insufficient funds for %s.", accountName);
    }
}
