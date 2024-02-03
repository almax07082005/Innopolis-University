package Warmup.A;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt(), m = scanner.nextInt();
        ArrayList<Integer> a = new ArrayList<>();
        for (int i = 0; i < n; i++) a.add(scanner.nextInt());

        for (int i = 0; i < m; i++) {
            int l = scanner.nextInt(), r = scanner.nextInt();
            if (l < 0 || l >= n || r < 0 || r >= n) continue;
            boolean flag = false;

            for (int j = l; j <= r; j++) {
                int min = Collections.min(a.subList(l, r + 1));
                
                if (a.get(j) != min) {
                    System.out.println(a.get(j));
                    flag = true;
                    break;
                }
            }

            if (!flag) System.out.println("NOT FOUND");
        }
        
        scanner.close();
    }
}