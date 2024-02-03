#include <iostream>
#include <vector>

typedef std::vector<std::vector<int>> vvint;

int minorSum(int leftUpperCornerI, int leftUpperCornerJ, int sideLength, vvint field) {
    int sum = 0;
    for (int i = leftUpperCornerI; i - leftUpperCornerI < sideLength; i++) {
        for (int j = leftUpperCornerJ; j - leftUpperCornerJ < sideLength; j++) {
            sum += field[i][j];
        }
    }
    return sum;
}

int main() {
    int rows, columns, rank = 0;
    std::cin >> rows >> columns;
    vvint field;
    for (int i = 0; i < rows; i++) {
        field.push_back(std::vector<int>(columns));
        for (int j = 0; j < columns; j++) std::cin >> field[i][j];
    }

    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < columns; j++) {
            if (!field[i][j]) continue;
            if (!rank) rank++;
            int sum = 1;

            for (int k = 2; k + i <= rows && k + j <= columns; k++) {
                sum = minorSum(i, j, k, field);
                if (sum != k * k) break;
                if (rank < k) rank = k;
            }
        }
    }
    std::cout << rank;

    return EXIT_SUCCESS;
}
