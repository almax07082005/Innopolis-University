package Assignment3.exceptions;

public class DeactivateDeactivated extends MainException {

    @Override
    public String getMessage(String accountName) {
        return String.format("Error: Account %s is already deactivated.", accountName);
    }
}
