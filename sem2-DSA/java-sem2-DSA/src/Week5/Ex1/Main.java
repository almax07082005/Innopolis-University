// Maksim Al Dandan
package Week5.Ex1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader scanner = new BufferedReader(new InputStreamReader(System.in));
        String[] input = scanner.readLine().split(" ");
        int N = Integer.parseInt(input[0]);
        int K = Integer.parseInt(input[1]);

        String[] words = scanner.readLine().split(" ");
        String text = scanner.readLine();

        boolean[] dp = new boolean[K + 1];
        int[] d = new int[K + 1];
        dp[0] = true;

        for (int i = 0; i < K; i++) {
            if (!dp[i]) continue;
            for (int j = 0; j < N; j++) {
                String word = words[j];
                int len = word.length();
                int end = i + len;
                if (end > K || dp[end]) continue;
                if (text.substring(i, end).equals(word)) {
                    dp[end] = true;
                    d[end] = j;
                }
            }
        }

        int index = K;

        List<String> result = new ArrayList<>();
        while (index != 0) {
            result.add(words[d[index]]);
            index -= words[d[index]].length();
        }
        
        Collections.reverse(result);

        for (String word : result) {
            System.out.print(word + " ");
        }

        scanner.close();
    }
}
