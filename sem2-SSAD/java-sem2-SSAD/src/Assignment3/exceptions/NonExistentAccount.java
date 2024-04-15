package Assignment3.exceptions;

public class NonExistentAccount extends MainException {

    public NonExistentAccount(String accountName) {
        super(accountName);
    }

    @Override
    public String getMessage() {
        return String.format("Error: Account %s does not exist.", accountName);
    }
}
