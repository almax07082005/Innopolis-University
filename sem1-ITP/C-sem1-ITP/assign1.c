/*
 * This program takes the following data (from file "input.txt"):
 * size of string, string;
 * size of array, array.
 * Then we need to replace the i-th character of S with the i-th character of the English language (lowercase).
 * The output should be modified string (to the file "output.txt").
*/

#include <stdio.h>
#include <stdbool.h>

// Defining constants

#define MAX_STRING_SIZE 50      // the maximum size of the string
#define MAX_ARRAY_SIZE 26       // the maximum size of the array
#define ASCII_ADDITION 97       // the summand for conversion i-th element of the array to English alphabet letter

// Declaring prototypes of functions

bool readStringSize(int* stringSize, FILE* input);
bool checkSymbolSuitability(char symbol);
bool readString(char* string, FILE* input, int stringSize);
bool readArraySize(int* arraySize, FILE* input, int stringSize);
bool readArray(int* array, FILE* input, int arraySize, int stringSize);
bool checkUniqueness(int* array, int arraySize);
bool checkInArrayOrNot(int* array, int arraySize, int number);

int main(void)
{
// Creating files for I/O and for input data

    FILE* input = fopen("input.txt", "r");
    FILE* output = fopen("output.txt", "w");

    int stringSize, arraySize;
    char string[MAX_STRING_SIZE + 1];       // MAX_STRING_SIZE + 1 because of null character in the end of the string
    int array[MAX_ARRAY_SIZE];

/*
 * All the functions written in if statement (except checkUniqueness) do the following:
 * read data (according to the name of function) and return true if all problem conditions are met, otherwise false.
 * Thus, if not all functions return true, we should print "Invalid inputs", close files, and finish the program.
*/

    if (!(readStringSize(&stringSize, input) && readString(string, input, stringSize) && readArraySize(&arraySize, input, stringSize) && readArray(array, input, arraySize, stringSize) && checkUniqueness(array, arraySize))) {

        fprintf(output, "Invalid inputs\n");
        fclose(input);
        fclose(output);
        return 0;
    }

// Going through all array elements and replacing the corresponding elements in the string according to the problem condition, then print the resultant string

    for (int i = 0; i < arraySize; i++) {
        string[array[i]] = (char)(array[i] - 1 + ASCII_ADDITION);
    }
    fprintf(output, "%s\n", string);

// Closing files and finishing the program

    fclose(input);
    fclose(output);
    return 0;
}

// Read stringSize from file, check according to the problem condition, and return true or false

bool readStringSize(int* stringSize, FILE* input)
{
    fscanf(input, "%d", stringSize);
    if (*stringSize >= 2 && *stringSize <= 50) return true;
    return false;
}

bool readString(char* string, FILE* input, const int stringSize)
{
// Before reading the string we need to read the last symbol from the first row: '\n'
// Then we can read the string

    char backSlashN;
    fscanf(input, "%c", &backSlashN);
    fscanf(input, "%s", string);

// If at least one element doesn't suit to the problem conditions, return false, otherwise true

    for (int i = 0; i < stringSize; i++) {
        if (!checkSymbolSuitability(string[i])) return false;
    }
    return true;
}

bool readArraySize(int* arraySize, FILE* input, const int stringSize)
{
// Choose min according to the problem condition

    int min = stringSize - 1;
    if (min > 26) min = 26;

// Read the array size, check the correctness, and return true or false

    fscanf(input, "%d", arraySize);
    if (*arraySize >= 1 && *arraySize <= min) return true;
    return false;
}

bool readArray(int* array, FILE* input, const int arraySize, const int stringSize)
{
// Before reading the array we need to read the last symbol from the third row: '\n'
// Then we can read the array

    char backSlashN;
    fscanf(input, "%c", &backSlashN);

// If at least one element doesn't suit to the problem conditions, return false, otherwise true

    for (int i = 0; i < arraySize; i++) {
        fscanf(input, "%d", &array[i]);
        if (array[i] >= stringSize || array[i] < 1 || array[i] > 26) return false;
    }
    return true;
}

bool checkSymbolSuitability(char symbol)
{
    #define SYMBOLS_AMOUNT 11       // defining amount of suitable symbols by constant
    
    char suitableSymbols[SYMBOLS_AMOUNT + 1] = "()*!@#$%&^";
    int symbolAsciiCode = (int) symbol;     // decimal in ascii table of the verifiable symbol

// Check the condition using ascii codes
// If this symbol is English uppercase, lowercase, or digit, return true

    if ((symbolAsciiCode >= 65 && symbolAsciiCode <= 90) || (symbolAsciiCode >= 97 && symbolAsciiCode <= 122) ||
    (symbolAsciiCode >= 48 && symbolAsciiCode <= 57)) {
        return true;
    }

// Otherwise if this symbol is one of suitableSymbols, return true, else false

    for (int i = 0; i < SYMBOLS_AMOUNT; i++) {
        if (symbol == suitableSymbols[i]) return true;
    }
    return false;
}

bool checkUniqueness(int* array, const int arraySize)
{
// Create anotherArray of the same size (let's call it capacity) and it's real size that we will update through program

    int anotherArray[MAX_ARRAY_SIZE] = {}, anotherArraySize = 0;

// Check: if current element is already in anotherArray, it implies that the original array is not unique (return false)
// Otherwise return true

    for (int i = 0; i < arraySize; i++) {
        if (checkInArrayOrNot(anotherArray, anotherArraySize, array[i])) return false;
        anotherArray[anotherArraySize++] = array[i];
    }
    return true;
}

// Check if number in array (obvious implementation)

bool checkInArrayOrNot(int* array, const int arraySize, int number)
{
    for (int i = 0; i < arraySize; i++) {
        if (array[i] == number) return true;
    }
    return false;
}
