#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>

bool check(char* array, int size, char symbol)
{
    for (int i = 0; i < size; i++) {
        if (array[i] == symbol) return false;
    }
    return true;
}

int main(void)
{
    char string[1000];
    int size = 0;
    char symbol = '0';
    
    while (symbol != '\n') {
        scanf("%c", &symbol);
        if (symbol != '\n') string[size++] = symbol;
    }
    string[size++] = '\0';

    char result[1000][2];
    int resSize = 0;

    for (int i = 0; i < size; i++) {
        
    }
    
    return 0;
}
