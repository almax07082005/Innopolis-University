package Lab14.Ex1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        final int SIZE = 7;
        Random random = new Random();
        List<Integer> list = new ArrayList<>();

        for (int i = 0; i < SIZE; i++) {
            list.add(random.nextInt());
        }

        Func func = number -> number % 3 == 0;
        for (Integer element : list) {
            if (func.filterNumbers(element)) {
                System.out.println(Math.abs(element));
            }
        }

    }
}
