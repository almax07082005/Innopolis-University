// Maksim Al Dandan
package Week5.Ex2;

import java.util.Scanner;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int W = scanner.nextInt();
        int[] weights = new int[n+1];
        int[] costs = new int[n+1];
        for (int i = 1; i <= n; i++) {
            weights[i] = scanner.nextInt();
        }
        for (int i = 1; i <= n; i++) {
            costs[i] = scanner.nextInt();
        }

        int[][] dp = new int[n+1][W+1];
        for (int i = 0; i <= n; i++) {
            for (int w = 0; w <= W; w++) {
                if (i == 0 || w == 0) {
                    dp[i][w] = 0;
                } else if (weights[i] <= w) {
                    dp[i][w] = Math.max(costs[i] + dp[i-1][w-weights[i]], dp[i-1][w]);
                } else {
                    dp[i][w] = dp[i-1][w];
                }
            }
        }

        int res = dp[n][W];
        int w = W;
        ArrayList<Integer> items = new ArrayList<>();
        for (int i = n; i > 0 && res > 0; i--) {
            if (res != dp[i-1][w]) {
                items.add(i);
                res -= costs[i];
                w -= weights[i];
            }
        }

        System.out.println(items.size());
        for (int i = items.size() - 1; i >= 0; i--) {
            System.out.print(items.get(i) + " ");
        }
        
        scanner.close();
    }
}
