package Warmup.G;

import java.util.Scanner;

// You have to make it faster
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        int rows = scanner.nextInt(), columns = scanner.nextInt(), rank = 0;
        int[][] field = new int[rows][columns];
        for (int i = 0; i < rows; i++) for (int j = 0; j < columns; j++) field[i][j] = scanner.nextInt();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {

                if (field[i][j] == 0) continue;
                if (rank == 0) rank++;
                int sum = 1;

                for (int k = 2; k + i <= rows && k + j <= columns; k++) {
                    sum = minorSum(i, j, k, field);
                    if (sum != k * k) break;
                    if (rank < k) rank = k;
                }
            }
        }
        System.out.println(rank);
        
        scanner.close();
    }

    public static int minorSum(int leftUpperCornerI, int leftUpperCornerJ, int sideLength, int[][] field) {

        int sum = 0;
        for (int i = leftUpperCornerI; i - leftUpperCornerI < sideLength; i++) {
            for (int j = leftUpperCornerJ; j - leftUpperCornerJ < sideLength; j++) {
                sum += field[i][j];
            }
        }
        
        return sum;
    }
}
