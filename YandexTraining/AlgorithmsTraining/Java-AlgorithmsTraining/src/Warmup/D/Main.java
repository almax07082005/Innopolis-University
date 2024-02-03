package Warmup.D;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Main {

    public static boolean isAnagram(String string1, String string2) {

        ArrayList<Character> array1 = new ArrayList<>(), array2 = new ArrayList<>();
        for (int i = 0; i < string1.length(); i++) array1.add(string1.charAt(i));
        for (int i = 0; i < string2.length(); i++) array2.add(string2.charAt(i));

        array1.sort(Collections.reverseOrder());
        array2.sort(Collections.reverseOrder());
        
        if (array1.equals(array2)) return true;
        return false;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String string1 = scanner.next(), string2 = scanner.next();

        System.out.println(isAnagram(string1, string2) || isAnagram(string2, string1) ? "YES" : "NO");
        
        scanner.close();
    }
}
