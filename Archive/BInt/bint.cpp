#include "bint.h"

void bint::equalize(bint& s)
{
    unsigned min = (str.length() < s.str.length() ? str.length() : s.str.length()),
        max = (s.str.length() < str.length() ? str.length() : s.str.length());

    if (str.length() < s.str.length()) for (unsigned i = 0; i < max - min; i++) str.insert(str.begin(), '0');
    else for (unsigned i = 0; i < max - min; i++) s.str.insert(s.str.begin(), '0');
}

bint::bint(std::string s)
{
    if (s[0] == '-') {
        sign = true;
        s.erase(0, 1);
    }
    else sign = false;
    str = s;
    this->optim();
}

bint::bint(long long n)
{
    if (!n) {
        str = "";
        sign = false;
    }
    else if (n < 0) {
        sign = true;
        n = -n;
        str = std::to_string(n);
    }
    else {
        sign = false;
        str = std::to_string(n);
    }
}

void bint::reverse()
{
    std::string tmp;
    for (unsigned i = 0; i < str.length(); i++) tmp.insert(tmp.begin(), str[i]);
    str = tmp;
    this->optim();
}

bint factorial(bint s)
{
    bint res = 1;
    for (bint i = 2; i <= s; i++) res *= i;
    return res;
}

bint pow(bint s, bint pow)
{
    if (!pow) return 1;
    bint res = s;
    for (bint i = 1; i < pow; i++) res *= s;
    return res;
}

bint abs(bint s)
{
    bint res = s;
    res.sign = false;
    return res;
}

bint bint::operator+(bint s)
{
    if (*this >= 0 && s < 0) return *this - abs(s);
    else if (*this < 0 && s >= 0) return s - abs(*this);
    else if (*this < 0 && s < 0) {
        bint res, t = abs(*this), st = abs(s);
        this->equalize(s);

        int next = 0;
        for (int i = t.str.length() - 1; i >= 0; i--) {
            int tmp = int(t.str[i]) + int(st.str[i]) - '0' * 2 + next;
            next = tmp / 10;
            tmp -= next * 10;
            res.str.push_back(char(tmp) + '0');
        }
        if (next) res.str.push_back(char(next) + '0');

        res.reverse();
        this->optim();
        s.optim();
        return -res;
    }
    else {
        bint res;
        this->equalize(s);

        int next = 0;
        for (int i = str.length() - 1; i >= 0; i--) {
            int tmp = int(str[i]) + int(s.str[i]) - '0' * 2 + next;
            next = tmp / 10;
            tmp -= next * 10;
            res.str.push_back(char(tmp) + '0');
        }
        if (next) res.str.push_back(char(next) + '0');

        res.reverse();
        this->optim();
        s.optim();
        return res;
    }
}

bint bint::operator-(bint s)
{
    if (*this < 0 && s > 0) return -(abs(*this) + s);
    else if (*this >= 0 && s < 0) return *this + abs(s);
    else if (*this >= 0 && s >= 0) {
        if (*this > s) {
            bint res, tmp = str;
            this->equalize(s);

            for (int i = tmp.str.length() - 1; i >= 0; i--) {
                if (tmp.str[i] >= s.str[i]) res.str.push_back(char(int(tmp.str[i]) - int(s.str[i])) + '0');
                else {
                    int j;
                    for (j = i - 1; tmp.str[j] == '0'; j--) continue;
                    if (j != i - 1) for (int k = j + 1; k < i; k++) tmp.str[k] += 9;
                    tmp.str[i] += 10;
                    tmp.str[j] -= 1;
                    res.str.push_back(char(int(tmp.str[i]) - int(s.str[i])) + '0');
                }
            }

            res.reverse();
            this->optim();
            s.optim();
            return res;
        }
        else if (*this == s) return bint(0);
        else {
            bint res, tmp = s.str;
            this->equalize(s);

            for (int i = tmp.str.length() - 1; i >= 0; i--) {
                if (tmp.str[i] >= str[i]) res.str.push_back(char(int(tmp.str[i]) - int(str[i])) + '0');
                else {
                    int j;
                    for (j = i - 1; tmp.str[j] == '0'; j--) continue;
                    if (j != i - 1) for (int k = j + 1; k < i; k++) tmp.str[k] += 9;
                    tmp.str[i] += 10;
                    tmp.str[j] -= 1;
                    res.str.push_back(char(int(tmp.str[i]) - int(str[i])) + '0');
                }
            }

            res.reverse();
            this->optim();
            s.optim();
            return -res;
        }
    }
    else {
        if (abs(*this) > abs(s)) {
            bint res, tmp = str;
            this->equalize(s);

            for (int i = tmp.str.length() - 1; i >= 0; i--) {
                if (tmp.str[i] >= s.str[i]) res.str.push_back(char(int(tmp.str[i]) - int(s.str[i])) + '0');
                else {
                    int j;
                    for (j = i - 1; tmp.str[j] == '0'; j--) continue;
                    if (j != i - 1) for (int k = j + 1; k < i; k++) tmp.str[k] += 9;
                    tmp.str[i] += 10;
                    tmp.str[j] -= 1;
                    res.str.push_back(char(int(tmp.str[i]) - int(s.str[i])) + '0');
                }
            }

            res.reverse();
            this->optim();
            s.optim();
            return -res;
        }
        else if (*this == s) return bint(0);
        else {
            bint res, tmp = s.str;
            this->equalize(s);

            for (int i = tmp.str.length() - 1; i >= 0; i--) {
                if (tmp.str[i] >= str[i]) res.str.push_back(char(int(tmp.str[i]) - int(str[i])) + '0');
                else {
                    int j;
                    for (j = i - 1; tmp.str[j] == '0'; j--) continue;
                    if (j != i - 1) for (int k = j + 1; k < i; k++) tmp.str[k] += 9;
                    tmp.str[i] += 10;
                    tmp.str[j] -= 1;
                    res.str.push_back(char(int(tmp.str[i]) - int(str[i])) + '0');
                }
            }

            res.reverse();
            this->optim();
            s.optim();
            return res;
        }
    }
}

bint bint::operator-()
{
    if (!*this) return *this;
    bint res = *this;
    res.sign = !sign;
    return res;
}

bint bint::operator++()
{
    *this += 1;
    return *this;
}

bint bint::operator--(int)
{
    bint res = *this;
    *this -= 1;
    return res;
}

bint bint::operator++(int)
{
    bint res = *this;
    *this += 1;
    return res;
}

bint bint::operator--()
{
    *this -= 1;
    return *this;
}

bint bint::operator*(bint s)
{
    if (sign ^ s.sign) {
        bint res;
        this->equalize(s);

        int count = 0;
        for (int i = str.length() - 1; i >= 0; i--) {
            bint tmp;
            for (int j = 0; j < int(s.str[i]) - '0'; j++) tmp += abs(*this);
            for (int j = 0; j < count; j++) tmp.str.push_back('0');
            count++;
            res += tmp;
        }

        res.optim();
        this->optim();
        s.optim();
        return -res;
    }
    else {
        bint res;
        this->equalize(s);

        int count = 0;
        for (int i = str.length() - 1; i >= 0; i--) {
            bint tmp;
            for (int j = 0; j < int(s.str[i]) - '0'; j++) tmp += abs(*this);
            for (int j = 0; j < count; j++) tmp.str.push_back('0');
            count++;
            res += tmp;
        }

        res.optim();
        this->optim();
        s.optim();
        return res;
    }
}

bool bint::operator>(bint s)
{
    if (!sign && !s.sign) {
        this->equalize(s);
        for (unsigned i = 0; i < str.length(); i++)
            if (str[i] != s.str[i]) {
                bool tmp = int(str[i]) > int(s.str[i]);
                this->optim();
                return tmp;
            }
        this->optim();
        return 0;
    }
    else if (sign && !s.sign) return 0;
    else if (!sign && s.sign) return 1;
    else {
        this->equalize(s);
        for (unsigned i = 0; i < str.length(); i++)
            if (str[i] != s.str[i]) {
                bool tmp = int(str[i]) > int(s.str[i]);
                this->optim();
                return !tmp;
            }
        this->optim();
        return 0;
    }
}

bool bint::operator<(bint s)
{
    if (!sign && !s.sign) {
        this->equalize(s);
        for (unsigned i = 0; i < str.length(); i++)
            if (str[i] != s.str[i]) {
                bool tmp = int(str[i]) < int(s.str[i]);
                this->optim();
                return tmp;
            }
        this->optim();
        return 0;
    }
    else if (sign && !s.sign) return 1;
    else if (!sign && s.sign) return 0;
    else {
        this->equalize(s);
        for (unsigned i = 0; i < str.length(); i++)
            if (str[i] != s.str[i]) {
                bool tmp = int(str[i]) < int(s.str[i]);
                this->optim();
                return !tmp;
            }
        this->optim();
        return 0;
    }
}

std::ostream& operator<<(std::ostream& out, bint l)
{
    if (!l.str.length()) out << '0';
    else if (l.sign) out << '-';
    out << l.str;
    return out;
}

std::istream& operator>>(std::istream& in, bint& l)
{
    std::string s;
    in >> s;
    if (s[0] == '-') {
        l.sign = true;
        s.erase(0, 1);
    }
    else l.sign = false;
    l.str = s;
    l.optim();
    return in;
}
