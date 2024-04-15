package Assignment3.exceptions;

public class DeactivateDeactivated extends MainException {

    public DeactivateDeactivated(String accountName) {
        super(accountName);
    }

    @Override
    public String getMessage() {
        return String.format("Error: Account %s is already deactivated.", accountName);
    }
}
