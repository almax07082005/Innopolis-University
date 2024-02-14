package Week4.Ex1;
// Maksim Al Dandan

import java.util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int amount = Integer.parseInt(reader.readLine());
        Map<Integer, String[]> students = new HashMap<>(amount);
        for (int i = 0; i < amount; i++) {
            String[] words = reader.readLine().split(" ");
            students.put(Integer.parseInt(words[0]), new String[]{words[1], words[2]});
        }

        for (String string : students.get(findMedian(students))) {
            System.out.print(string + " ");
        }
        reader.close();
    }

// Taken from Wikipedia
    public static int findMedian(Map<Integer, String[]> map) {
        int size = map.size();
        KeyValuePair<Integer, String[]>[] entries = map.entrySet();
        return select(entries, 0, size - 1, size / 2).key;
    }

    private static KeyValuePair<Integer, String[]> select(KeyValuePair<Integer, String[]>[] array, int left, int right, int n) {
        if (left == right) {
            return array[left];
        }

        int pivotIndex = randomPivot(left, right);
        pivotIndex = partition(array, left, right, pivotIndex);

        if (n == pivotIndex) {
            return array[n];
        } else if (n < pivotIndex) {
            return select(array, left, pivotIndex - 1, n);
        } else {
            return select(array, pivotIndex + 1, right, n);
        }
    }

    private static int partition(KeyValuePair<Integer, String[]>[] array, int left, int right, int pivotIndex) {
        KeyValuePair<Integer, String[]> pivotValue = array[pivotIndex];
        swap(array, pivotIndex, right);
        int storeIndex = left;

        for (int i = left; i < right; i++) {
            if (array[i].key.compareTo(pivotValue.key) < 0) {
                swap(array, storeIndex, i);
                storeIndex++;
            }
        }

        swap(array, right, storeIndex);
        return storeIndex;
    }

    private static void swap(KeyValuePair<Integer, String[]>[] array, int i, int j) {
        KeyValuePair<Integer, String[]> temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    private static int randomPivot(int left, int right) {
        return left + (int) Math.floor(Math.random() * (right - left + 1));
    }
}

// Taken from Lab 3
interface Map<K, V> {
    V get(K key);
    void put(K key, V value);
    void remove(K key);
    int size();
    boolean isEmpty();
    KeyValuePair<K, V>[] entrySet();
}

// Taken from Lab 3
class KeyValuePair<K, V> {
    public K key;
    public V value;
    public KeyValuePair(K key, V value) {
        this.key = key;
        this.value = value;
    }
}

// Taken from Lab 3
class HashMap<K, V> implements Map<K, V> {

    List<KeyValuePair<K, V>>[] hashTable;
    int capacity;
    int numberOfElements;

    @Override
    public KeyValuePair<K, V>[] entrySet() {
        List<KeyValuePair<K, V>> entryList = new ArrayList<>();

        for (List<KeyValuePair<K, V>> array : hashTable) {
            entryList.addAll(array);
        }

        KeyValuePair<K, V>[] entryArray = new KeyValuePair[entryList.size()];
        for (int i = 0; i < entryArray.length; i++) {
            entryArray[i] = entryList.get(i);
        }

        return entryArray;
    }

    public HashMap(int capacity) {
        this.capacity = capacity;
        this.numberOfElements = 0;
        this.hashTable = new List[capacity];
        for (int i = 0; i < capacity; ++i) {
            this.hashTable[i] = new LinkedList<>();
        }
    }
    @Override
    public V get(K key) {
        int i = key.hashCode() % this.capacity;

        if (i < 0) i *= -1;
        for (KeyValuePair<K, V> kv : hashTable[i]) {
            if(kv.key.equals(key)) {
                return kv.value;
            }
        }
        return null;
    }
    @Override
    public void put(K key, V value) {
        int i = key.hashCode() % this.capacity;

        if (i < 0) i *= -1;
        for(KeyValuePair<K, V> kv : this.hashTable[i]) {
            if (kv.key.equals(key)) {
                kv.value = value;
                return;
            }
        }
        this.hashTable[i].add(new KeyValuePair<>(key, value));
        this.numberOfElements++;
    }
    @Override
    public int size() {
        return this.numberOfElements;
    }
    @Override
    public void remove(K key) {
        int i = key.hashCode() % this.capacity;

        if (i < 0) i *= -1;
        for (KeyValuePair<K, V> kv : hashTable[i]) {
            if(kv.key.equals(key)) {
                kv.value = null;
                this.numberOfElements--;
                return;
            }
        }
    }
    @Override
    public boolean isEmpty() {
        return this.numberOfElements == 0;
    }
}
