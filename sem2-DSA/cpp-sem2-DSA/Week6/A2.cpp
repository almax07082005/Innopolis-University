// Maksim Al Dandan
#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

struct Item {
    int index;
    int currentBid;
    int maxBid;
};

// Counting sort implementation
void MaksimAlDandan_count_srt(vector<Item>& items, int exp) {
    vector<int> count(10, 0);
    vector<Item> output(items.size());

    for (const auto& item : items)
        count[(item.maxBid / exp) % 10]++;

    for (int i = 1; i < 10; i++)
        count[i] += count[i - 1];

    for (int i = int(items.size()) - 1; i >= 0; i--) {
        output[count[(items[i].maxBid / exp) % 10] - 1] = items[i];
        count[(items[i].maxBid / exp) % 10]--;
    }

    for (int i = 0; i < items.size(); i++)
        items[i] = output[i];
}

// Radix sort implementation
void MaksimAlDandan_radix_srt(vector<Item>& items) {
    int maxBid = INT_MIN;
    for (const auto& item : items)
        maxBid = max(maxBid, item.maxBid);

    for (int exp = 1; maxBid / exp > 0; exp *= 10)
        MaksimAlDandan_count_srt(items, exp);
}

// Comparator function for sorting items
bool compareItems(const Item& a, const Item& b) {
    if (a.currentBid != b.currentBid)
        return a.currentBid > b.currentBid;
    else if (a.maxBid != b.maxBid)
        return a.maxBid < b.maxBid;
    else
        return a.index < b.index;
}

void merge(vector<Item>& items, int left, int mid, int right) {
    int i, j, k;
    int n1 = mid - left + 1;
    int n2 = right - mid;

    vector<Item> L(n1), R(n2);

    for (i = 0; i < n1; i++)
        L[i] = items[left + i];
    for (j = 0; j < n2; j++)
        R[j] = items[mid + 1 + j];

    i = 0;
    j = 0;
    k = left;
    while (i < n1 && j < n2) {
        if (compareItems(L[i], R[j])) {
            items[k] = L[i];
            i++;
        } else {
            items[k] = R[j];
            j++;
        }
        k++;
    }

    while (i < n1) {
        items[k] = L[i];
        i++;
        k++;
    }

    while (j < n2) {
        items[k] = R[j];
        j++;
        k++;
    }
}

void MaksimAlDandan_merge_srt(vector<Item>& items, int left, int right) {
    if (left < right) {
        int mid = left + (right - left) / 2;

        MaksimAlDandan_merge_srt(items, left, mid);
        MaksimAlDandan_merge_srt(items, mid + 1, right);

        merge(items, left, mid, right);
    }
}

int main() {
    int n;
    cin >> n;

    vector<Item> items(n);
    for (int i = 0; i < n; i++) {
        items[i].index = i + 1;
        cin >> items[i].currentBid >> items[i].maxBid;
    }

    MaksimAlDandan_radix_srt(items);
    MaksimAlDandan_merge_srt(items, 0, int(items.size()) - 1);

    // Outputting indices of items in the original sequence
    for (const auto& item : items)
        cout << item.index << " ";

    return 0;
}
