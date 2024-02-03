#include <stdio.h>
#define SIZE 8
typedef unsigned long long ull;

union Integer {
    ull number;
    char charNumber[SIZE];
};

void encryption(char* charNumber)
{
    for (int i = 0; i < SIZE; i += 2) {
        char temp = charNumber[i];
        charNumber[i] = charNumber[i + 1];
        charNumber[i + 1] = temp;
    }
}

int main(void)
{
    union Integer integer;
    scanf("%llu", &integer.number);

    encryption(integer.charNumber);
    printf("%llu\n", integer.number);

    encryption(integer.charNumber);
    printf("%llu\n", integer.number);

    return 0;
}
