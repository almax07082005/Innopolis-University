#include <iostream>
#include <vector>
#include <algorithm>
#include <cmath>
#include <sstream>
#include <string>
#define languageCount 3

std::string getPermutation(const std::vector<char>& symbolList, int number, int wordLength) {
    std::string numberInSystem;
    int symbolAmount = int(symbolList.size());

    for (int j = 0; j < wordLength; j++) {
        numberInSystem.insert(0, std::to_string(number % symbolAmount));
        number /= symbolAmount;
    }

    std::stringstream permutation;
    for (char symbol : numberInSystem) {
        permutation << symbolList[std::stoi(std::string(1, symbol))];
    }

    return permutation.str();
}

bool filterLanguage1(const std::string& string, const std::vector<char>& symbolList) {
    for (char symbol : symbolList) {
        if (std::count(string.begin(), string.end(), symbol) % 2) return false;
    }

    return true;
}

bool filterLanguage2(const std::string& string, const std::vector<char>& symbolList) {
    for (char symbol : symbolList) {
        if (string.find(symbol) == -1) return false;
    }

    return true;
}

bool filterLanguage3(const std::string& string, const std::vector<char>& symbolList) {
    int count = 0;

    for (char symbol : symbolList) {
        if (string.find(symbol) == -1) count++;
    }

    return count == 1;
}

void showResult(auto filterLanguage, int languageNumber, const std::vector<char>& symbolList, const std::vector<int>& wordAmounts) {
    int symbolCount = int(symbolList.size());
    if (symbolCount == 1 && languageNumber == 3) {
        std::cout << '_';
        return;
    }

    std::cout << (languageNumber == 1 ? "_ " : "");
    for (int wordLength = 1, wordIndex = 0; wordIndex < wordAmounts[languageNumber - 1] - (languageNumber == 1 ? 1 : 0); wordLength++) {
        int permutationCount = int(pow(symbolCount, wordLength));

        for (int i = 0; i < permutationCount && wordIndex < wordAmounts[languageNumber - 1] - (languageNumber == 1 ? 1 : 0); i++) {
            std::string permutation = getPermutation(symbolList, i, wordLength);

            if (filterLanguage(permutation, symbolList)) {
                std::cout << permutation << ' ';
                wordIndex++;
            }
        }
    }
    std::cout << std::endl;
}

int main() {
    int symbolAmount;
    std::cin >> symbolAmount;
    auto readOne = []<typename T>(T& elem){ std::cin >> elem; };

    std::vector<char> symbolList(symbolAmount);
    std::for_each(symbolList.begin(), symbolList.end(), readOne);

    std::vector<int> wordAmounts(languageCount);
    std::for_each(wordAmounts.begin(), wordAmounts.end(), readOne);

    showResult(filterLanguage1, 1, symbolList, wordAmounts);
    showResult(filterLanguage2, 2, symbolList, wordAmounts);
    showResult(filterLanguage3, 3, symbolList, wordAmounts);

    return EXIT_SUCCESS;
}
