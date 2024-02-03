#include <iostream>
#include <cmath>

template <typename T>
void mvector<T>::bubble_sort()
{
    for (int i = 0; i < len; i++)
        for (int j = 0; j < len - i - 1; j++)
            if (arr[j] > arr[j + 1]) {
                T tmp = arr[j];
                arr[j] = arr[j + 1];
                arr[j + 1] = tmp;
            }
}

template <typename T>
void mvector<T>::insertion_sort()
{
    for (int i = 1; i < len; i++) {
        T tmp = arr[i];
        int j = i;
        while (j > 0 && arr[j - 1] > tmp) {
            arr[j] = arr[j - 1];
            j--;
        }
        arr[j] = tmp;
    }
}

template <typename T>
void mvector<T>::selection_sort()
{
    for (int i = 0; i < len; i++) {
        int max = 0;
        for (int j = 1; j < len - i; j++) if (arr[j] > arr[max]) max = j;

        T tmp = arr[len - i - 1];
        arr[len - i - 1] = arr[max];
        arr[max] = tmp;
    }
}

template <typename T>
mvector <T> mvector<T>::operator-(std::initializer_list <T> a)
{
    mvector <T> res(*this);
    for (T el : a) {
        int index = res.find(el);
        if (index != -1) res.erase(index);
    }
    return res;
}

template <typename T>
void mvector<T>::operator-=(std::initializer_list <T> a)
{
    for (T el : a) {
        int index = (*this).find(el);
        if (index != -1) (*this).erase(index);
    }
}

template <typename T>
mvector <T> mvector<T>::operator-(mvector <T> a)
{
    mvector <T> res(*this);
    for (int i = 0; i < a.len; i++) {
        int index = res.find(a[i]);
        if (index != -1) res.erase(index);
    }
    return res;
}

template <typename T>
void mvector<T>::operator-=(mvector <T> a)
{
    for (int i = 0; i < a.len; i++) {
        int index = (*this).find(a[i]);
        if (index != -1) (*this).erase(index);
    }
}

template <typename T>
mvector <T> mvector<T>::operator+(std::initializer_list <T> a)
{
    mvector <int> res(*this);
    res.insert(len, a);
    return res;
}

template <typename T>
void mvector<T>::assign(int count, T a1, T d, std::string s)
{
    if (s == "alg") {
        len = count;
        arr = new T[len];
        for (int i = 0; i < len; i++) arr[i] = a1 + d * i;
    } else if (s == "geom") {
        len = count;
        arr = new T[len];
        for (int i = 0; i < len; i++) arr[i] = a1 * pow(d, i);
    }
}

template <typename T>
mvector<T>::mvector(int count, T a1, T d, std::string s)
{
    if (s == "alg") {
        len = count;
        arr = new T[len];
        for (int i = 0; i < len; i++) arr[i] = a1 + d * i;
    } else if (s == "geom") {
        len = count;
        arr = new T[len];
        for (int i = 0; i < len; i++) arr[i] = a1 * pow(d, i);
    }
}

template <typename T>
bool mvector<T>::operator!=(mvector <T> a)
{
    if (len != a.len) return true;
    for (int i = 0; i < len; i++) if (arr[i] != a[i]) return true;
    return false;
}

template <typename T>
bool mvector<T>::operator==(mvector <T> a)
{
    if (len != a.len) return false;
    for (int i = 0; i < len; i++) if (arr[i] != a[i]) return false;
    return true;
}

template <typename T>
mvector <T> mvector<T>::operator()(int stop, int start, int step)
{
    if (stop < 0 || stop >= len || start < 0 || start >= len) throw std::out_of_range("Going beyond the array boundaries.");
    mvector <T> res;
    for (int i = start; i <= stop; i += step) res.push_back(arr[i]);
    return res;
}

template <typename T>
void mvector<T>::reverse()
{
    mvector <T> res(*this);
    (*this).clear();
    for (int i = res.len - 1; i >= 0; i--) (*this).push_back(res[i]);
}

template <typename T>
mvector <T> mvector<T>::operator--(int)
{
    --(*this);
    return *this;
}

template <typename T>
mvector <T> mvector<T>::operator++(int)
{
    ++(*this);
    return *this;
}

template <typename T>
void mvector<T>::operator--()
{
    int index = (*this).find(1);
    if (index != -1) (*this).erase(index);
}

template <typename T>
void mvector<T>::operator-=(T num)
{
    int index = (*this).find(num);
    if (index != -1) (*this).erase(index);
}

template <typename T>
mvector <T> mvector<T>::operator-(T num)
{
    mvector <T> res(*this);
    int index = res.find(num);
    if (index != -1) res.erase(index);
    return res;
}

template <typename T>
void mvector<T>::operator*=(int mult)
{
    mvector <T> tmp(*this);
    (*this).clear();
    for (int i = 0; i < mult; i++) *this += tmp;
}

template <typename T>
void mvector<T>::operator+=(mvector <T> a)
{
    T *mas = (*this).data();
    len += a.len;
    arr = new T[len];

    for (int i = 0; i < len - a.len; i++) arr[i] = mas[i];
    for (int i = len - a.len; i < len; i++) arr[i] = a[i - len + a.len];
}

template <typename T>
mvector <T> mvector<T>::operator+(T num)
{
    mvector <T> res(*this);
    res.push_back(num);
    return res;
}

template <typename T>
int mvector<T>::countSimple()
{
    int count = 0;
    for (int i = 0; i < len; i++) {
        bool flag = true;
        for (T j = 2; j < arr[i]; j++) if (!(arr[i] % j)) {
            flag = false;
            break;
        }
        if (flag) count++;
    }
    return count;
}

template <typename T>
T mvector<T>::mult()
{
    T mult = 1;
    for (int i = 0; i < len; i++) mult *= arr[i];
    return mult;
}

template <typename T>
T mvector<T>::sum()
{
    T sum = 0;
    for (int i = 0; i < len; i++) sum += arr[i];
    return sum;
}

template <typename T>
int mvector<T>::find(T el)
{
    for (int i = 0; i < len; i++) if (el == arr[i]) return i;
    return -1;
}

template <typename T>
int mvector<T>::rfind(T el)
{
    for (int i = len - 1; i >= 0; i--) if (el == arr[i]) return i;
    return -1;
}

template <typename T>
int mvector<T>::count(T el)
{
    int count = 0;
    for (int i = 0; i < len; i++) if (el == arr[i]) count++;
    return count;
}

template <typename T>
void mvector<T>::sort(std::string str)
{
    if (str == "up") {
        for (int i = 0; i < len - 1; i++)
            for (int j = i + 1; j < len; j++) if (arr[i] > arr[j]) {
                T tmp = arr[i];
                arr[i] = arr[j];
                arr[j] = tmp;
            }
    }
    else if (str == "down") {
        for (int i = 0; i < len - 1; i++)
            for (int j = i + 1; j < len; j++) if (arr[i] < arr[j]) {
                T tmp = arr[i];
                arr[i] = arr[j];
                arr[j] = tmp;
            }
    }
}

template <typename T>
T mvector<T>::nok()
{
    for (T i = (*this).abs_max();; i++) {
        bool flag = true;
        for (int j = 0; j < len; j++) if (i % abs(arr[j])) {
            flag = false;
            break;
        }
        if (flag) return i;
    }
}

template <typename T>
T mvector<T>::max()
{
    T max = arr[0];
    for (int i = 0; i < len; i++) if (arr[i] > max) max = arr[i];
    return max;
}

template <typename T>
T mvector<T>::min()
{
    T min = arr[0];
    for (int i = 0; i < len; i++) if (arr[i] < min) min = arr[i];
    return min;
}

template <typename T>
T mvector<T>::abs_max()
{
    T max = abs(arr[0]);
    for (int i = 0; i < len; i++) if (abs(arr[i]) > max) max = abs(arr[i]);
    return max;
}

template <typename T>
T mvector<T>::abs_min()
{
    T min = abs(arr[0]);
    for (int i = 0; i < len; i++) if (abs(arr[i]) < min) min = abs(arr[i]);
    return min;
}

template <typename T>
T mvector<T>::nod()
{
    for (T i = (*this).abs_min(); i > 1; i--) {
        bool flag = true;
        for (int j = 0; j < len; j++) if (abs(arr[j]) % i) {
            flag = false;
            break;
        }
        if (flag) return i;
    }
    return 1;
}

template <typename T>
mvector <T> mvector<T>::operator*(int mult)
{
    mvector <T> res;
    for (int i = 0; i < mult; i++) res = res + *this;
    return res;
}

template <typename T>
mvector <T> mvector<T>::operator+(mvector <T> a)
{
    mvector <T> res(*this);
    T *mas = res.data();

    res.len = len + a.len;
    res.arr = new T[res.len];

    for (int i = 0; i < len; i++) res[i] = mas[i];
    for (int i = len; i < res.len; i++) res[i] = a[i - len];

    return res;
}

template <typename T>
void mvector<T>::swap(mvector <T> &a)
{
    mvector <T> tmp(a);
    a.assign(*this);
    (*this).assign(tmp);
}

template <typename T>
void mvector<T>::erase(T *it, int count)
{
    int index = it - arr;
    if (index + count > len) throw std::out_of_range("Going beyond the array boundaries.");
    for (int i = 0; i < count; i++) {
        for (int j = index; j < len - 1; j++) arr[j] = arr[j + 1];
        (*this).assign((*this).begin(), (*this).end() - 1);
    }
}

template <typename T>
void mvector<T>::erase(int index, int count)
{
    if (index + count > len) throw std::out_of_range("Going beyond the array boundaries.");
    for (int i = 0; i < count; i++) {
        for (int j = index; j < len - 1; j++) arr[j] = arr[j + 1];
        (*this).assign((*this).begin(), (*this).end() - 1);
    }
}

template <typename T>
void mvector<T>::assign(T *begin, T *end)
{
    int dist = end - begin + 1, j = 0;
    len = dist;
    arr = new T[len];
    for (T *i = begin; i < begin + dist; i++) arr[j++] = *i;
}

template <typename T>
void mvector<T>::assign(mvector <T> a)
{
    len = a.len;
    arr = new T[len];
    for (int i = 0; i < len; i++) arr[i] = a[i];
}

template <typename T>
void mvector<T>::pop_back()
{
    mvector <T> tmp((*this).begin(), (*this).end() - 1);
    (*this).assign(tmp);
}

template <typename T>
void mvector<T>::assign(std::initializer_list <T> a, int count)
{
    len = a.size() * count;
    arr = new T[len];
    int j = 0;
    for (int i = 0; i < count; i++) for (T element : a) arr[j++] = element;
}

template <typename T>
void mvector<T>::assign(T num, int count)
{
    (*this).clear();
    *this = mvector <T> (count, num);
}

template <typename T>
void mvector<T>::clear()
{
    len = 0;
    arr = new T[len];
}

template <typename T>
void mvector<T>::insert(T *it, std::initializer_list <T> a, int count)
{
    int index = it - arr;
    if (index > len) throw std::out_of_range("Going beyond the array boundaries.");
    else if (index == len) {
        for (int i = 0; i < count; i++) for (T element : a) (*this).push_back(element);
        return;
    }
    for (int i = 0; i < count; i++) {
        mvector <T> tmp(*this);
        arr = new T[len += a.size()];
        for (int j = 0, k = 0; k < tmp.len; j++, k++) {
            if (j == index) {
                for (T element : a) arr[j++] = element;
                k--;
                j--;
            } else arr[j] = tmp[k];
        }
        index += a.size();
    }
}

template <typename T>
void mvector<T>::insert(T *it, T num, int count)
{
    int index = it - arr;
    if (index > len) throw std::out_of_range("Going beyond the array boundaries.");
    else if (index == len) {
        for (int i = 0; i < count; i++) (*this).push_back(num);
        return;
    }
    for (int i = 0; i < count; i++) {
        mvector <T> tmp(*this);
        arr = new T[++len];
        for (int j = 0, k = 0; k < tmp.len; j++, k++) {
            if (j == index) {
                arr[j] = num;
                k--;
            } else arr[j] = tmp[k];
        }
        index++;
    }
}

template <typename T>
void mvector<T>::insert(int index, std::initializer_list <T> a, int count)
{
    if (index > len) throw std::out_of_range("Going beyond the array boundaries.");
    else if (index == len) {
        for (int i = 0; i < count; i++) for (T element : a) (*this).push_back(element);
        return;
    }
    for (int i = 0; i < count; i++) {
        mvector <T> tmp(*this);
        arr = new T[len += a.size()];
        for (int j = 0, k = 0; k < tmp.len; j++, k++) {
            if (j == index) {
                for (T element : a) arr[j++] = element;
                k--;
                j--;
            } else arr[j] = tmp[k];
        }
        index += a.size();
    }
}

template <typename T>
void mvector<T>::insert(int index, T num, int count)
{
    if (index > len) throw std::out_of_range("Going beyond the array boundaries.");
    else if (index == len) {
        for (int i = 0; i < count; i++) (*this).push_back(num);
        return;
    }
    for (int i = 0; i < count; i++) {
        mvector <T> tmp(*this);
        arr = new T[++len];
        for (int j = 0, k = 0; k < tmp.len; j++, k++) {
            if (j == index) {
                arr[j] = num;
                k--;
            } else arr[j] = tmp[k];
        }
        index++;
    }
}

template <typename T>
void mvector<T>::operator=(mvector <T> a)
{
    len = a.len;
    arr = new T[len];
    for (int i = 0; i < len; i++) arr[i] = a[i];
}

template <typename T>
void mvector<T>::push_back(T el)
{
    mvector <T> tmp(*this);
    arr = new T[++len];
    for (int i = 0; i < tmp.len; i++) arr[i] = tmp[i];
    arr[len - 1] = el;
}

template <typename T>
mvector<T>::mvector(std::initializer_list <T> a, int count)
{
    len = a.size() * count;
    arr = new T[len];
    int j = 0;
    for (int i = 0; i < count; i++) for (T element : a) arr[j++] = element;
}

template <typename T>
T* mvector<T>::data()
{
    T *mas = new T[len];
    for (int i = 0; i < len; i++) mas[i] = arr[i];
    return mas;
}

template <typename T>
T &mvector<T>::operator[](int index)
{
    if (index < 0) return arr[index + len];
    else if (index < len) return arr[index];
    throw std::out_of_range("Going beyond the array boundaries.");
}

template <typename T>
mvector<T>::mvector(T *begin, T *end)
{
    int dist = end - begin + 1, j = 0;
    len = dist;
    arr = new T[len];
    for (T *i = begin; i < begin + dist; i++) arr[j++] = *i;
}

template <typename T>
mvector<T>::mvector(int n, T num)
{
    len = n;
    arr = new T[len];
    for (int i = 0; i < len; i++) arr[i] = num;
}

template <typename T>
mvector<T>::mvector(const mvector <T> &a)
{
    len = a.len;
    arr = new T[len];
    for (int i = 0; i < len; i++) arr[i] = a.arr[i];
}

template <typename T>
mvector<T>::~mvector()
{
    len = 0;
    delete [] arr;
}

template <typename T1>
std::istream &operator>>(std::istream &read, mvector <T1> &a)
{
    read >> a.len;
    a.arr = new T1[a.len];
    for (int i = 0; i < a.len; i++) read >> a[i];
    return read;
}

template <typename T1>
std::ostream &operator<<(std::ostream &print, mvector <T1> &a)
{
    for (int i = 0; i < a.len; i++) {
        print << a[i];
        if (i != a.len - 1) print << ' ';
    }
    return print;
}
