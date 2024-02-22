#include <iostream>
#include <vector>
#include <algorithm>

int main() {
// Read the size of the array and the number of operations from the user

    int n, m;
    std::cin >> n >> m;

// Create an array of size n and read its elements from the user

    std::vector<int> array(n);
    for (int& elem : array) std::cin >> elem;

// Perform m operations on the array

    for (int i = 0; i < m; ++i) {

// Read the operation, index, and value from the user

        std::string operation;
        int index, value;
        std::cin >> operation >> index >> value;

// If the operation is "set", set the element at the given index to the given value

        if (operation == "set") {
            *(array.begin() + index) = value;
        }
        
// If the operation is "sum", calculate and print the sum of the elements from the given index to the given value

        else if (operation == "sum") {
            int sum = 0;
// std::for_each is a standard algorithm that applies a function to each element of a range
            
            std::for_each(array.begin() + index, array.begin() + value + 1, [&sum](int elem) { sum += elem; });
            std::cout << sum << std::endl;
        }
    }

// Print the elements of the array

    for (const int& elem : array) std::cout << elem << " ";
    return EXIT_SUCCESS;
}
