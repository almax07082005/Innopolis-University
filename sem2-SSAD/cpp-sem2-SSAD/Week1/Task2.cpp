#include <iostream>

void swapByPointer(int* number1, int* number2) {
    int temp = *number1;
    *number1 = *number2;
    *number2 = temp;
}

void swapByReference(int& number1, int& number2) {
    int temp = number1;
    number1 = number2;
    number2 = temp;
}

int main() {
    int number1, number2;
    std::cin >> number1 >> number2;

    swapByPointer(&number1, &number2);
    std::cout << number1 << ' ' << number2;
    std::cout << std::endl;

    swapByReference(number1, number2);
    std::cout << number1 << ' ' << number2;

    return EXIT_SUCCESS;
}
