// Maksim Al Dandan
// Material from lab 1
#include <iostream>

void bubbleSort(int* array, int size) {
    bool swapped = true;
    while (swapped) {
        swapped = false;
        for (int i = 1; i < size; i++) {

            if (array[i - 1] > array[i]) {
                int temp = array[i];
                array[i] = array[i - 1];
                array[i - 1] = temp;

                swapped = true;
            }
        }
    }
}

int main() {
    int size;
    std::cin >> size;

    int array[size];
    for (int& element : array) std::cin >> element;

    bubbleSort(array, size);
    for (int& element : array) std::cout << element << ' ';

    return EXIT_SUCCESS;
}
