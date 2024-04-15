package Assignment3.exceptions;

public class OperationsWithDeactivated extends Throwable {

    public String getMessage(String accountName) {
        return String.format("Error: Account %s is inactive.", accountName);
    }
}
