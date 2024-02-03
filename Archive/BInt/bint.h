#pragma once
#include <iostream>
#include <vector>
#include <string>

class bint {
private:
    std::string str;
    bool sign;
    void optim() { while (str[0] == '0') str.erase(0, 1); }
    void equalize(bint& s);
public:
    bint(std::string s = "");
    bint(long long n);
    void reverse();
    friend bint abs(bint s);
    friend bint pow(bint s, bint pow);
    friend bint factorial(bint s);
    bint operator+(bint s);
    void operator+=(bint s) { *this = *this + s; }
    bint operator-(bint s);
    void operator-=(bint s) { *this = *this - s; }
    bint operator-();
    bint operator++();
    bint operator++(int);
    bint operator--();
    bint operator--(int);
    bint operator*(bint s);
    void operator*=(bint s) { *this = *this * s; }
    bool operator==(bint s) { return str == s.str && sign == s.sign; }
    bool operator!() { return str == ""; }
    bool operator<(bint s);
    bool operator<=(bint s) { return (*this == s ? 1 : *this < s); }
    bool operator>(bint s);
    bool operator>=(bint s) { return (*this == s ? 1 : *this > s); }
    friend std::istream& operator>>(std::istream& in, bint& l);
    friend std::ostream& operator<<(std::ostream& out, bint l);
};
