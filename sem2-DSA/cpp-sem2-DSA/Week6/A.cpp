// Maksim Al Dandan
#include <iostream>
#include <vector>
#include <cmath>
#include <algorithm>

template<typename T1, typename T2>
std::vector<T1> MaksimAlDandan_count_srt(std::vector<T1>& mainData, std::vector<T2>& satelliteData, int maxValue, std::vector<int>& indexes) {
    std::vector<T1> count(maxValue + 1);
    std::vector<T1> partialSums(maxValue + 1);
    std::vector<T1> resultMainData(mainData.size());
    std::vector<T2> resultSatelliteData(satelliteData.size());
    std::vector<int> resultIndexes(indexes.size());

    for (int i = 0; i < mainData.size(); i++) {
        count.at(mainData.at(i))++;
    }

    for (int i = 0; i < partialSums.size(); i++) {
        try {
            partialSums.at(i) = partialSums.at(i - 1) + count.at(i);
        } catch (const std::out_of_range& exception) {
            partialSums.at(i) = count.at(i);
        }
    }

    for (int i = int(mainData.size() - 1); i >= 0; i--) {
        resultIndexes.at(partialSums.at(mainData.at(i)) - 1) = (indexes.at(0) != -1 ? indexes.at(i) : i + 1);
        resultMainData.at(partialSums.at(mainData.at(i)) - 1) = mainData.at(i);
        resultSatelliteData.at(partialSums.at(mainData.at(i))-- - 1) = satelliteData.at(i);
    }

    mainData.clear();
    mainData.insert(mainData.begin(), resultMainData.begin(), resultMainData.end());

    satelliteData.clear();
    satelliteData.insert(satelliteData.begin(), resultSatelliteData.begin(), resultSatelliteData.end());

    indexes.clear();
    indexes.insert(indexes.begin(), resultIndexes.begin(), resultIndexes.end());

    return partialSums;
}

template <typename T>
int maxDigitsCount(const typename std::vector<T>::iterator begin, const typename std::vector<T>::iterator end) {
    int max = -1;

    for (auto it = begin; it < end; it++) {
        int count = 0;
        T number = *it;

        while (number) {
            count++;
            number /= 10;
        }

        if (count > max) max = count;
    }

    return max;
}

template <typename T>
int getDigit(T number, int position) {
    return number / T(pow(10, position)) % 10;
}

template <typename T>
void MaksimAlDandan_radix_srt(typename std::vector<T>::iterator begin, typename std::vector<T>::iterator end, typename std::vector<int>::iterator indexBegin, typename std::vector<int>::iterator indexEnd) {
    std::vector<T> mainData;
    std::vector<T> satelliteData;
    std::vector<int> indexes(indexBegin, indexEnd);

    for (auto it = begin; it < end; it++) {
        mainData.push_back(getDigit<T>(*it, 0));
        satelliteData.push_back(*it);
    }

    for (int i = 0; i < maxDigitsCount<T>(begin, end); i++) {
        MaksimAlDandan_count_srt(mainData, satelliteData, 9, indexes);

        for (int j = 0; j < satelliteData.size(); j++) {
            mainData.at(j) = getDigit<T>(satelliteData.at(j), i + 1);
        }
    }

    int index = int(satelliteData.size()) - 1;
    for (auto it = begin; it < end; it++) {
        *it = satelliteData.at(index--);
    }

    index = int(indexes.size()) - 1;
    for (auto it = indexBegin; it < indexEnd; it++) {
        *it = indexes.at(index--);
    }
}

int main() {
    int arraySize;
    std::cin >> arraySize;

    std::vector<int> mainData(arraySize);
    std::vector<int> satelliteData(arraySize);
    for (int i = 0; i < arraySize; i++) {
        std::cin >> mainData.at(i) >> satelliteData.at(i);
    }

    std::vector<int> indexes(arraySize, -1);
    std::vector<int> partialSums = MaksimAlDandan_count_srt(mainData, satelliteData, 100, indexes);

    for (int i = 0; i < partialSums.size() - 1; i++) {
        if (partialSums.at(i + 1) - partialSums.at(i) < 2) continue;
        MaksimAlDandan_radix_srt<int>(satelliteData.begin() + partialSums.at(i), satelliteData.begin() + partialSums.at(i + 1), indexes.begin() + partialSums.at(i), indexes.begin() + partialSums.at(i + 1));
    }

    std::reverse(indexes.begin(), indexes.end());
    std::for_each(indexes.begin(), indexes.end(), [](const int& elem){ std::cout << elem << ' '; });

    return EXIT_SUCCESS;
}
