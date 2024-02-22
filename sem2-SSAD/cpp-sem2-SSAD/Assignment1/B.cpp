#include <iostream>
#include <iomanip>

// Define a class to represent complex numbers
class Complex {
private:
    double real;  // Real part of the complex number
    double imag;  // Imaginary part of the complex number
public:
    // Constructor to initialize real and imaginary parts of the complex number
    Complex(double real, double imag) : real(real), imag(imag) {}

    // Overload + operator to add two complex numbers
    Complex operator+(const Complex& c) const {
        return Complex(real + c.real, imag + c.imag);
    }

    // Overload - operator to subtract two complex numbers
    Complex operator-(const Complex& c) const {
        return Complex(real - c.real, imag - c.imag);
    }

    // Overload * operator to multiply two complex numbers
    Complex operator*(const Complex& c) const {
        return Complex(real * c.real - imag * c.imag, real * c.imag + imag * c.real);
    }

    // Overload / operator to divide two complex numbers
    Complex operator/(const Complex& c) const {
        double denominator = c.real * c.real + c.imag * c.imag;
        return Complex((real * c.real + imag * c.imag) / denominator, (imag * c.real - real * c.imag) / denominator);
    }

    // Overload == operator to compare two complex numbers for equality
    bool operator==(const Complex& c) const {
        return real == c.real && imag == c.imag;
    }

    // Overload != operator to compare two complex numbers for inequality
    bool operator!=(const Complex& c) const {
        return !(*this == c);
    }

    // Overload << operator to print complex numbers in a+bi format
    friend std::ostream& operator<<(std::ostream& os, const Complex& c) {
        os << std::fixed << std::setprecision(2) << c.real << (c.imag < 0 ? "" : "+") << c.imag << "i";
        return os;
    }

    // Destructor
    ~Complex() {}
};

int main() {
    int n;  // Number of operations to perform
    std::cin >> n;

    // Loop to perform n operations
    for (int i = 0; i < n; i++) {
        char op;  // Operation to perform
        double a, b, c, d;  // Real and imaginary parts of the two complex numbers
        std::cin >> op >> a >> b >> c >> d;

        Complex c1(a, b);  // First complex number
        Complex c2(c, d);  // Second complex number

        // Switch case to perform the operation based on the operator
        switch (op) {
            case '+':
                std::cout << c1 + c2 << std::endl;  // Add the two complex numbers
                break;
            case '-':
                std::cout << c1 - c2 << std::endl;  // Subtract the two complex numbers
                break;
            case '*':
                std::cout << c1 * c2 << std::endl;  // Multiply the two complex numbers
                break;
            case '/':
                std::cout << c1 / c2 << std::endl;  // Divide the two complex numbers
                break;
            case '=':
                std::cout << (c1 == c2 ? "true" : "false") << std::endl;  // Compare the two complex numbers for equality
                break;
        }
    }
    
    return EXIT_SUCCESS;  // Return success status
}
