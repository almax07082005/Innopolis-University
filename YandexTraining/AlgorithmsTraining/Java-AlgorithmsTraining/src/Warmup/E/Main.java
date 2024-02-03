package Warmup.E;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));

        int n = Integer.parseInt(reader.readLine());
        int[] a = new int[n];
        
        String[] input = reader.readLine().split(" ");
        int sum = 0;
        for (int i = 0; i < n; i++) {
            a[i] = Integer.parseInt(input[i]);
            sum += a[i];
        }
        sum -= n * a[0];

        StringBuilder sb = new StringBuilder();
        sb.append(sum);

        sum = sum + n * a[0] - 2 * a[0] - a[1] * (n - 2);
        sb.append(" " + sum);

        for (int i = 2; i < n; i++) {
            sum = sum - 2 * a[i - 1] - a[i] * (n - 2 * i) + a[i - 1] * (n - 2 * (i - 1));
            sb.append(" " + sum);
        }

        writer.write(sb.toString());
        writer.flush();

        reader.close();
        writer.close();
    }
}
