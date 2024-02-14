#include <iostream>

class Account {
protected:
    int accountNumber;
    double balance;
    std::string ownerName;
public:
    Account() = default;
    Account(int accountNumber, double balance, const std::string& ownerName) : accountNumber(accountNumber), balance(balance), ownerName(ownerName) {}

    int getAccountNumber() const {
        return accountNumber;
    }

    double getBalance() const {
        return balance;
    }

    const std::string &getOwnerName() const {
        return ownerName;
    }

    void withdraw(double amount) {
        balance -= amount;
    }

    void deposit(double amount) {
        balance += amount;
    }
};

class SavingsAccount : public Account {
private:
    double interestRate;
public:
    SavingsAccount() = default;
    SavingsAccount(int accountNumber, double balance, const std::string &ownerName, double interestRate) : Account(accountNumber, balance, ownerName), interestRate(interestRate) {}
    SavingsAccount(const SavingsAccount&) = delete;
    SavingsAccount& operator=(const SavingsAccount&) = delete;

    void calculateInterest() {
        deposit(interestRate / 100 * balance);
    }

    double getInterestRate() const {
        return interestRate;
    }
};

using std::cout, std::endl;
int main() {
    SavingsAccount savings(123456, 1000.0, "John Doe", 2.5);
    savings.deposit(500.0);
    savings.withdraw(200.0);
    savings.calculateInterest();

    cout << "Account Number: " << savings.getAccountNumber() << endl;
    cout << "Owner's Name: " << savings.getOwnerName() << endl;
    cout << "Current Balance: " << savings.getBalance() << endl;
    cout << "Interest Rate: " << savings.getInterestRate() << "%" << endl;

    return EXIT_SUCCESS;
}
