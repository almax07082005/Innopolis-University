#include <iostream>
#include <vector>
#include <algorithm>
typedef std::vector<int> vint;

vint map(const vint& vector, auto lambda) {
    vint result = vector;
    std::for_each(result.begin(), result.end(), lambda);
    return result;
}

vint filter(const vint& vector, auto lambda) {
    vint result;
    for (const int& elem : vector) {
        if (lambda(elem)) result.push_back(elem);
    }
    return result;
}

int main() {
    vint vector{1, 2, 3, 4, 5};
    vector = map(vector, [](int& elem){elem++;});
    std::for_each(vector.begin(), vector.end(), [](const int& elem){std::cout << elem << ' ';});
    std::cout << std::endl;

    vector = filter(vector, [](const int& elem){return elem % 2;});
    std::for_each(vector.begin(), vector.end(), [](const int& elem){std::cout << elem << ' ';});

    return EXIT_SUCCESS;
}
