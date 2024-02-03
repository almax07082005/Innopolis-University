#include <iostream>
#include <vector>

int main() {
    int size;
    std::cin >> size;
    std::vector<int> array;

    for (int i = 0; i < size; i++) {
        int temp;
        std::cin >> temp;
        bool unique = true;
        for (int j = 0; j < (i < array.size() ? i : array.size()); j++) {
            if (array.at(j) == temp) {
                unique = false;
                break;
            }
        }
        if (unique) array.push_back(temp);
    }
    for (int& element : array) std::cout << element << ' ';

    return EXIT_SUCCESS;
}
