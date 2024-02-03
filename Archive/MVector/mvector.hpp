#include <iostream>
#include <cmath>

template <typename T>
class mvector {
private:
    T* arr;
    int len;
public:
    mvector(int n = 0, T num = 0);
    mvector(const mvector <T>& a);
    mvector(T* begin, T* end);
    mvector(std::initializer_list <T> a, int count = 1);
    mvector(int count, T a1, T d, std::string s);
    ~mvector();
    T& operator[](int index);
    void operator=(mvector <T> a);
    mvector <T> operator-(T num);
    void operator-=(T num);
    mvector <T> operator-(mvector <T> a);
    void operator-=(mvector <T> a);
    mvector <T> operator-(std::initializer_list <T> a);
    void operator-=(std::initializer_list <T> a);
    mvector <T> operator+(mvector <T> a);
    void operator+=(mvector <T> a);
    mvector <T> operator+(T num);
    void operator+=(T num) { (*this).push_back(num); }
    mvector <T> operator+(std::initializer_list <T> a);
    void operator+=(std::initializer_list <T> a) { (*this).insert(len, a); }
    mvector <T> operator*(int mult);
    void operator*=(int mult);
    void operator++() { (*this).push_back(1); }
    mvector <T> operator++(int);
    void operator--();
    mvector <T> operator--(int);
    mvector <T> operator()(int stop, int start = 0, int step = 1);
    int size() { return len; }
    int find(T el);
    int rfind(T el);
    int count(T el);
    int countSimple();
    void sort(std::string str = "up");
    T* data();
    T* begin() { return arr; }
    T* end() { return arr + len - 1; }
    T nod();
    T nok();
    T max();
    T min();
    T abs_max();
    T abs_min();
    T sum();
    T mult();
    void push_back(T el);
    void pop_back();
    void insert(int index, T num, int count = 1);
    void insert(int index, std::initializer_list <T> a, int count = 1);
    void insert(T* it, T num, int count = 1);
    void insert(T* it, std::initializer_list <T> a, int count = 1);
    void clear();
    void erase(int index, int count = 1);
    void erase(T* it, int count = 1);
    void assign(T num, int count = 1);
    void assign(T* begin, T* end);
    void assign(mvector <T> a);
    void assign(std::initializer_list <T> a, int count = 1);
    void assign(int count, T a1, T d, std::string s);
    void swap(mvector <T>& a);
    void reverse();
    void selection_sort();
    void bubble_sort();
    void insertion_sort();
    bool empty() { return (len ? true : false); }
    bool operator==(mvector <T> a);
    bool operator!=(mvector <T> a);
    template <typename T1> friend std::istream& operator>>(std::istream& read, mvector <T1>& a);
    template <typename T1> friend std::ostream& operator<<(std::ostream& print, mvector <T1>& a);
};

#include "mvector.tcc"
