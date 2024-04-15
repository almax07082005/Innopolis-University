package Assignment3.exceptions;

public class ActivateActivated extends MainException {

    public ActivateActivated(String accountName) {
        super(accountName);
    }

    @Override
    public String getMessage() {
        return String.format("Error: Account %s is already activated.", accountName);
    }
}
