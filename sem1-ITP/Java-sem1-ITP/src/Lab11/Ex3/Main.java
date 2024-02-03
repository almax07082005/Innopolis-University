package Lab11.Ex3;

import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        // Create a set with strings
        Set<String> stringSet = new HashSet<>(Arrays.asList("one", "two", "three", "four", "five", "six"));

        System.out.println("Original Set: " + stringSet);

        // Remove elements with odd length
        stringSet.removeIf(s -> s.length() % 2 != 0);

        System.out.println("Set after removing elements with odd length: " + stringSet);
    }
}
