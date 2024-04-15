package Assignment3.exceptions;

public abstract class MainException extends Exception {

    protected final String accountName;

    public MainException(String accountName) {
        this.accountName = accountName;
    }

    @Override
    public abstract String getMessage();
}
