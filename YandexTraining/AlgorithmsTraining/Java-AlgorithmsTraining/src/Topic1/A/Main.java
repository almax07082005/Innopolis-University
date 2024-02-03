package Topic1.A;

import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
        
        int size = Integer.parseInt(reader.readLine());
        List<Integer> array = new ArrayList<>();
        try {
            for (String element : reader.readLine().split(" ")) array.add(Integer.parseInt(element));
        }
        catch (Exception ignored) {}
        int pivot = Integer.parseInt(reader.readLine());

        int divider = partition(pivot, array, 0, size);
        writer.write(divider + "\n" + (size - divider));
        
        reader.close();
        writer.flush();
        writer.close();
    }

    public static int partition(int pivot, List<Integer> array, int startIndex, int endIndex) {
        int equal = startIndex, greater = startIndex;

        for (int i = startIndex; i < endIndex; i++) {
            if (array.get(i) == pivot) {
                int temp = array.get(greater);
                array.set(greater++, array.get(i));
                array.set(i, temp);
            }
            else if (array.get(i) < pivot) {
                int temp = array.get(i);
                array.set(i, array.get(greater));
                array.set(greater++, array.get(equal));
                array.set(equal++, temp);
            }
        }
        
        return equal;
    }
}
