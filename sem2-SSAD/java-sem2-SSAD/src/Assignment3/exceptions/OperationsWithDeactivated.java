package Assignment3.exceptions;

public class OperationsWithDeactivated extends MainException {

    public OperationsWithDeactivated(String accountName) {
        super(accountName);
    }

    @Override
    public String getMessage() {
        return String.format("Error: Account %s is inactive.", accountName);
    }
}
