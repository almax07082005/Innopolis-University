package Topic1.E;

import java.util.Arrays;

public class Main {
    private static final int SIZE = 10;
    
    public static void main(String[] args) {
        int[] array = {8, 3, 4, 4, 2, 6, 9, 4, 9, 8, 3};
        array = countSort(array, array.length, 1);
        System.out.println(Arrays.toString(array));
    }

    public static int[] countSort(int[] array, int arraySize, int div) {
        int[] count = new int[SIZE];
        int[] position = new int[SIZE];
        int[] result = new int[arraySize];

        for (int i = 0; i < arraySize; i++) {
            count[array[i] / div % 10]++;
        }
        for (int i = 1; i < SIZE; i++) {
            position[i] = position[i - 1] + count[i - 1];
        }
        for (int i = 0; i < arraySize; i++) {
            result[position[array[i] / div % 10]++] = array[i];
        }
        
        return result;
    }

    public static int[] bitwiseSort(int[] array, int arraySize) {
        // write loop for bitwise sort

        return array;
    }
}