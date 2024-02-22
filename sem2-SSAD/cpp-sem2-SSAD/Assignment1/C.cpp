#include <iostream>
#include <vector>

// Abstract base class for a data structure
class DataStructure {
public:
    // Pure virtual function for inserting an element
    virtual void insert(int x) = 0;
    // Pure virtual function for removing an element
    virtual void remove() = 0;
    // Pure virtual function for searching an element
    virtual bool search(int x) const = 0;
};

// Abstract base class for a sequential data structure
class SequentialDataStructure : virtual public DataStructure {
public:
    // Pure virtual function for pushing an element to the back
    virtual void pushBack(int x) = 0;
    // Pure virtual function for pushing an element to the front
    virtual void pushFront(int x) = 0;
    // Pure virtual function for popping an element from the back
    virtual void popBack() = 0;
    // Pure virtual function for popping an element from the front
    virtual void popFront() = 0;
};

// Abstract base class for a dynamic data structure
class DynamicDataStructure : virtual public DataStructure {
public:
    // Pure virtual function for resizing the data structure
    virtual void resize(int new_size) = 0;
    // Pure virtual function for clearing the data structure
    virtual void clear() = 0;
};

// Concrete class for a List, which is both a sequential and dynamic data structure
class List : public SequentialDataStructure, public DynamicDataStructure {
private:
    std::vector<int> list; // Internal storage for the list
public:
    // Insert an element to the back of the list
    void insert(int x) override {
        pushBack(x);
    }

    // Remove an element from the back of the list
    void remove() override {
        popBack();
    }

    // Search for an element in the list
    bool search(int x) const override {
        for (int i = 0; i < list.size(); i++) {
            if (list[i] == x) {
                return true;
            }
        }
        return false;
    }

    // Push an element to the back of the list
    void pushBack(int x) override {
        list.push_back(x);
    }

    // Push an element to the front of the list
    void pushFront(int x) override {
        list.insert(list.begin(), x);
    }

    // Pop an element from the back of the list
    void popBack() override {
        if (!list.empty()) {
            list.pop_back();
        }
    }

    // Pop an element from the front of the list
    void popFront() override {
        if (!list.empty()) {
            list.erase(list.begin());
        }
    }

    // Resize the list
    void resize(int new_size) override {
        list.resize(new_size);
    }

    // Clear the list
    void clear() override {
        list.clear();
    }

    // Overload the stream insertion operator to print the list
    friend std::ostream& operator<<(std::ostream& os, const List& l) {
        for (int i = 0; i < l.list.size(); i++) {
            os << l.list[i] << " ";
        }
        return os;
    }
};

int main() {
    int n;
    std::cin >> n;
    List list;

    // Process n operations on the list
    for (int i = 0; i < n; i++) {
        std::string operation;
        std::cin >> operation;
        if (operation == "insert") {
            int x;
            std::cin >> x;
            list.insert(x);
        } else if (operation == "remove") {
            list.remove();
        } else if (operation == "search") {
            int x;
            std::cin >> x;
            if (list.search(x)) {
                std::cout << "YES\n";
            } else {
                std::cout << "NO\n";
            }
        }
    }

    // Print the final state of the list
    std::cout << list;
    return EXIT_SUCCESS;
}
