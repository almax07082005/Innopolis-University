package Assignment3.main;

public enum CommandType {

    CreateAccount("Create Account"),
    Deposit("Deposit"),
    Withdraw("Withdraw"),
    Transfer("Transfer"),
    View("View"),
    Deactivate("Deactivate"),
    Activate("Activate");

    private final String name;

    CommandType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
