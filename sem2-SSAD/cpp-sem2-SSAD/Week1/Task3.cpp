#include <iostream>

int main() {
    int size, iterator = 0;
    std::cin >> size;
    int array[size];

    for (int i = 0; i < size; i++) {
        int temp;
        std::cin >> temp;
        bool unique = true;
        for (int j = 0; j < (i < iterator ? i : iterator); j++) {
            if (array[j] == temp) {
                unique = false;
                break;
            }
        }
        if (unique) array[iterator++] = temp;
    }
    for (int i = 0; i < iterator; i++) std::cout << array[i] << ' ';

    return EXIT_SUCCESS;
}
