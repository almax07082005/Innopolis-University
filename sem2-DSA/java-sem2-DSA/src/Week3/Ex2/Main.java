package Week3.Ex2;
// Maksim Al Dandan

import java.util.ArrayList;
import java.util.LinkedList;
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

        int n = Integer.parseInt(reader.readLine());
        String[] textN = reader.readLine().split(" ");
        int m = Integer.parseInt(reader.readLine());
        String[] textM = reader.readLine().split(" ");

        Map<String, Boolean> mapTextN = new HashMap<>(n);
        Map<String, Boolean> mapTextM = new HashMap<>(m);
        List<String> listTextM = new ArrayList<>();

        for (String word : textN) mapTextN.put(word, false);
        for (String word : textM) {
            if (mapTextN.get(word) == null && mapTextM.get(word) == null) {
                listTextM.add(word);
                mapTextM.put(word, false);
            }
        }

        System.out.println(listTextM.size());
        for (String elem : listTextM) System.out.print(elem + " ");

        reader.close();
        writer.flush();
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

