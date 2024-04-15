package Assignment3.exceptions;

public class ActivateActivated extends MainException {

    @Override
    public String getMessage(String accountName) {
        return String.format("Error: Account %s is already activated.", accountName);
    }
}
