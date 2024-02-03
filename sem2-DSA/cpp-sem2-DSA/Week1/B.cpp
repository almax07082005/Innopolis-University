// Maksim Al Dandan
#include <iostream>

class Map {
private:
    std::string key;
    int value;
public:
    Map(): value(0) {}
    std::string& getPlayer() {
        return this->key;
    }
    int& getScore() {
        return this->value;
    }
    friend std::istream& operator>>(std::istream& cin, Map& m);
    friend std::ostream& operator<<(std::ostream& cout, const Map& m);
};

std::ostream& operator<<(std::ostream& cout, const Map& m) {
    cout << m.key << ' ' << m.value;
    return cout;
}

std::istream& operator>>(std::istream& cin, Map& m) {
    cin >> m.getPlayer() >> m.getScore();
    return cin;
}

// Taken from https://www.geeksforgeeks.org/merge-sort/
void merge(Map array[], int const left, int const mid, int const right)
{
    int const subArrayOne = mid - left + 1;
    int const subArrayTwo = right - mid;
    Map *leftArray = new Map[subArrayOne], *rightArray = new Map[subArrayTwo];

    for (auto i = 0; i < subArrayOne; i++) leftArray[i] = array[left + i];
    for (auto j = 0; j < subArrayTwo; j++) rightArray[j] = array[mid + 1 + j];

    int indexOfSubArrayOne = 0, indexOfSubArrayTwo = 0;
    int indexOfMergedArray = left;

    while (indexOfSubArrayOne < subArrayOne && indexOfSubArrayTwo < subArrayTwo) {
        if (leftArray[indexOfSubArrayOne].getScore() <= rightArray[indexOfSubArrayTwo].getScore()) {
            array[indexOfMergedArray] = leftArray[indexOfSubArrayOne];
            indexOfSubArrayOne++;
        }
        else {
            array[indexOfMergedArray] = rightArray[indexOfSubArrayTwo];
            indexOfSubArrayTwo++;
        }
        indexOfMergedArray++;
    }

    while (indexOfSubArrayOne < subArrayOne) {
        array[indexOfMergedArray] = leftArray[indexOfSubArrayOne];
        indexOfSubArrayOne++;
        indexOfMergedArray++;
    }

    while (indexOfSubArrayTwo < subArrayTwo) {
        array[indexOfMergedArray] = rightArray[indexOfSubArrayTwo];
        indexOfSubArrayTwo++;
        indexOfMergedArray++;
    }

    delete[] leftArray;
    delete[] rightArray;
}

// Taken from https://www.geeksforgeeks.org/merge-sort/
void mergeSort(Map array[], int const begin, int const end)
{
    if (begin >= end) return;

    int mid = begin + (end - begin) / 2;
    mergeSort(array, begin, mid);
    mergeSort(array, mid + 1, end);
    merge(array, begin, mid, end);
}

void topRanking(Map* array, int size, Map* resultArray, int resultSize) {
    mergeSort(array, 0, size - 1);
    for (int i = 0; i < resultSize; i++) {
        resultArray[i] = array[size - i - 1];
    }
}

int main() {
    int size, maxTopRanking;
    std::cin >> size >> maxTopRanking;

    Map array[size];
    for (Map& element : array) std::cin >> element;

    int resultSize = maxTopRanking < size ? maxTopRanking : size;
    Map resultArray[resultSize];

    mergeSort(array, 0, size - 1);
    topRanking(array, size, resultArray, resultSize);

    for (Map& element : resultArray) std::cout << element << '\n';
    return EXIT_SUCCESS;
}
