#include <iostream>

int main() {
    int seconds;
    std::cin >> seconds;
    std::cout << seconds / 3600 << ':' << seconds / 60 % 60 << ':' << seconds % 60;
    return EXIT_SUCCESS;
}
