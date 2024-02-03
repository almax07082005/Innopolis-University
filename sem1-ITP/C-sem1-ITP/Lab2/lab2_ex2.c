#include <stdio.h>
#include <stdbool.h>
#include <stdlib.h>

bool check(int* array, int size, int number)
{
    for (int i = 0; i < size; i++) {
        if (array[i] == number) return false;
    }
    return true;
}

int main(void)
{
    int size;
    scanf("%d", &size);

    int* array = (int*) calloc(size, sizeof(int));
    for (int i = 0; i < size; i++) scanf("%d", &array[i]);

    int* resultArray = (int*) calloc(size, sizeof(int));
    int resultSize = 0;

    for (int i = 0; i < size; i++) {
        if (check(resultArray, resultSize, array[i])) resultArray[resultSize++] = array[i];
    }

    for (int i = 0; i < resultSize; i++) printf("%d ", resultArray[i]);

    free(array);
    free(resultArray);
    return 0;
}
