package Week3.Ex1;
// Maksim Al Dandan

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));

        int n = Integer.parseInt(reader.readLine());
        String[] text = reader.readLine().split(" ");
        Map<String, Integer> map = new HashMap<>(n);

        for (String word : text) {
            Integer amount = map.get(word);
            if (amount == null) map.put(word, 1);
            else map.put(word, amount + 1);
        }

        KeyValuePair<String, Integer>[] mapList = map.entrySet();
        sort(mapList, 0, mapList.length - 1);

        for (int i = mapList.length - 1; i >= 0; i--) {
            System.out.println(mapList[i].key + " " + mapList[i].value);
        }

        reader.close();
        writer.flush();
    }

    private static boolean compareStrings(String string1, String string2) {

        for (int i = 0; i < Math.min(string1.length(), string2.length()); i++) {
            if ((int) string1.charAt(i) < (int) string2.charAt(i)) return true;
            else if ((int) string1.charAt(i) > (int) string2.charAt(i)) return false;
        }

        return string1.length() <= string2.length();
    }

// taken from https://www.geeksforgeeks.org/merge-sort/
    private static void merge(KeyValuePair<String, Integer>[] arr, int l, int m, int r) {
		// Find sizes of two sub_arrays to be merged
		int n1 = m - l + 1;
		int n2 = r - m;

		// Create temp arrays
		KeyValuePair<String, Integer>[] L = new KeyValuePair[n1];
		KeyValuePair<String, Integer>[] R = new KeyValuePair[n2];

		// Copy data to temp arrays
		for (int i = 0; i < n1; ++i)
			L[i] = arr[l + i];
		for (int j = 0; j < n2; ++j)
			R[j] = arr[m + 1 + j];

		// Merge the temp arrays

		// Initial indices of first and second subarrays
		int i = 0, j = 0;

		// Initial index of merged subarray array
		int k = l;
		while (i < n1 && j < n2) {
			if (L[i].value < R[j].value) {
				arr[k] = L[i];
				i++;
			}
            else if (Objects.equals(L[i].value, R[j].value)) {
                if (compareStrings(L[i].key, R[j].key)) arr[k] = R[j++];
                else arr[k] = L[i++];
            }
			else {
				arr[k] = R[j];
				j++;
			}
			k++;
		}

		// Copy remaining elements of L[] if any
		while (i < n1) {
			arr[k] = L[i];
			i++;
			k++;
		}

		// Copy remaining elements of R[] if any
		while (j < n2) {
			arr[k] = R[j];
			j++;
			k++;
		}
	}

	// Main function that sorts arr[l..r] using
	// merge()
	private static void sort(KeyValuePair<String, Integer>[] arr, int l, int r) {
		if (l < r) {

			// Find the middle point
			int m = l + (r - l) / 2;

			// Sort first and second halves
			sort(arr, l, m);
			sort(arr, m + 1, r);

			// Merge the sorted halves
			merge(arr, l, m, r);
		}
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
