#include <iostream>
#include <unordered_map>
#include <unordered_set>
#include <algorithm>
#include <string>

struct pair_hash {
    template <class T1, class T2>
    std::size_t operator()(const std::pair<T1,T2>& p) const {
        auto h1 = std::hash<T1>{}(p.first);
        auto h2 = std::hash<T2>{}(p.second);
        return h1 ^ h2;
    }
};

typedef std::unordered_set<std::string> set;
typedef std::unordered_map<std::pair<std::string, char>, std::string, pair_hash> map;

bool checkWordInFSA(const std::string& word, const std::string& startingState, const map& transitions, const set& setOfFinalStates) {
    if (word == "_") return setOfFinalStates.contains(startingState);
    std::string currentState = startingState;

    for (char symbol : word) {
        try {
            currentState = transitions.at(std::make_pair(currentState, symbol));
        } catch (std::out_of_range& exception) {
            return false;
        }
    }

    return setOfFinalStates.contains(currentState);
}

int main() {
    int statesNumber, symbolsNumber, finalStatesNumber, wordsNumber;
    std::cin >> statesNumber >> symbolsNumber >> finalStatesNumber >> wordsNumber;

    set setOfStates;
    for (int i = 0; i < statesNumber; i++) {
        std::string state;
        std::cin >> state;
        setOfStates.insert(state);
    }

    std::string alphabet(symbolsNumber, '0');
    std::for_each(alphabet.begin(), alphabet.end(), [](char& elem){ std::cin >> elem; });

    map transitions;
    for (int i = 0; i < statesNumber * symbolsNumber; i++) {
        std::string state, newState;
        char input;
        std::cin >> state >> input >> newState;
        transitions.emplace(std::make_pair(state, input), newState);
    }

    std::string startingState;
    std::cin >> startingState;

    set setOfFinalStates;
    for (int i = 0; i < finalStatesNumber; i++) {
        std::string finalState;
        std::cin >> finalState;
        setOfFinalStates.insert(finalState);
    }

    for (int i = 0; i < wordsNumber; i++) {
        std::string word;
        std::cin >> word;
        std::cout << (checkWordInFSA(word, startingState, transitions, setOfFinalStates) ? "A " : "R ");
    }

    return EXIT_SUCCESS;
}
