package Assignment3.exceptions;

public class NonExistentAccount extends MainException {

    @Override
    public String getMessage(String accountName) {
        return String.format("Error: Account %s does not exist.", accountName);
    }
}
